package me.cortex.TreeCracker.program;

import me.cortex.TreeCracker.LCG.ConfiguredLcg;
import me.cortex.TreeCracker.LCG.LcgTester;
import me.cortex.TreeCracker.postProcessor.PostProcessor;
import me.cortex.TreeCracker.trees.ICrackableTree;
import me.cortex.TreeCracker.trees.TreePos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TreeCrackerProgram {
    ICrackableTree[] trees;

    public Tree primary;
    public List<Tree> secondary;

    int maxTreeLookback;

    private final CudaProgram program = new CudaProgram();
    private final CudaLCGEmitter lcg_emitter = new CudaLCGEmitter();


    public TreeCrackerProgram(int maxTreeLookback, ICrackableTree... trees) {
        this.trees = trees;
        this.maxTreeLookback = maxTreeLookback;
    }
    public TreeCrackerProgram(ICrackableTree... trees) {
        this(187, trees);
    }

    private void calculateTreeOrder() {
        List<Tree> trees = Stream.of(this.trees).map(Tree::new)
                .sorted((a, b) -> Float.compare(-a.test.getFilterPower(), -b.test.getFilterPower()))//Negative to inverse sort order
                .collect(Collectors.toList());

        primary = trees.remove(0);
        secondary = trees;
    }

    //TODO: make common/shared rng calls be done together so that second kernel filtering is alot faster
    private String generateTreeCheckFunctions() {
        lcg_emitter.lcgVariableName = "seed";
        lcg_emitter.comparisonFailedReturn = "return false";
        StringBuilder builder = new StringBuilder();
        for (Tree tree : secondary) {
            builder.append(lcg_emitter.createFunction(
                    "AuxTreeCheck_"+secondary.indexOf(tree),
                    "bool",
                    "const uint64_t seed",
                    lcg_emitter.assembleLcgTester(tree.test) + "\treturn true;"));
            builder.append("\n\n\n\n");
        }
        return builder.toString();
    }

    //TODO: REPLACE CURRENT INITIAL FILTER THING WITH A 2D LATTICE
    public CudaProgram generateCracker() {
        try {
            program.loadFromResources("CudaCrackerTemplate.cu");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        calculateTreeOrder();

        System.out.println("Using primary tree at " + primary.pos + " with filter power of " + (primary.test.getFilterPower()+8) + " bits");

        //TODO: CLEAN UP THIS SYSTEM
        lcg_emitter.lcgVariableName = "seed";
        program.replaceFirstUsingKeyword("LCG_REVERSE_STAGE_2_REPLACEMENT", lcg_emitter.emitLcg(new ConfiguredLcg(-maxTreeLookback)));
        program.replaceAllUsingKeyword("MAX_TREE_RNG_RANGE_REPLACEMENT", Integer.toString(maxTreeLookback));
        program.replaceFirstUsingKeyword("AUXILIARY_TREE_COUNT", Integer.toString(secondary.size()));
        program.replaceFirstUsingKeyword("AUX_TREE_FUNCTIONS_REPLACEMENT", generateTreeCheckFunctions());

        //TODO: more testing on lattice method to make sure it works 100%
        LcgTester primaryTester = new LcgTester();
        //primaryTester.nextInt(16).equalTo(primary.pos.z);
        primaryTester.advance(1);
        primary.test.replicateOnto(primaryTester);
        program.replaceFirstUsingKeyword("INIT_TREE_INNER_X", Integer.toString(primary.pos.x));
        program.replaceFirstUsingKeyword("INIT_TREE_INNER_Z", Integer.toString(primary.pos.z));
        lcg_emitter.lcgVariableName = "seed";
        lcg_emitter.comparisonFailedReturn = "return";
        program.replaceFirstUsingKeyword("PRIMARY_TREE_FILTER", lcg_emitter.assembleLcgTester(primaryTester));

        //#define TREE_TEST(testMethod, index, expected_x, expected_z, IF_TYPE)
        StringBuilder auxTreeInnerLoopCheck = new StringBuilder();
        for (Tree tree : secondary) {
            auxTreeInnerLoopCheck.append(String.format("TREE_TEST(AuxTreeCheck_%s, %s, %s, %s, %s)\n", secondary.indexOf(tree), secondary.indexOf(tree), tree.pos.x, tree.pos.z, secondary.indexOf(tree) == 0 ? "if":"else if"));
        }
        program.replaceFirstUsingKeyword("AUX_TREE_TEST_INNER_LOOP_CALL_REPLACEMENT", auxTreeInnerLoopCheck.toString());


        return program;
    }


    public PostProcessor createPostProcessor() {
        ArrayList<Tree> trees = new ArrayList<>(this.secondary);
        trees.add(primary);
        return new PostProcessor(trees.toArray(new Tree[0]), maxTreeLookback);
    }







    public static class Tree {
        public TreePos pos;
        public LcgTester test;
        ICrackableTree tree;
        public Tree(ICrackableTree tree) {
            this.tree = tree;
            this.pos = tree.getTreePosition();
            this.test = new LcgTester();
            tree.generateTreeTest(test);
        }
    }
}
