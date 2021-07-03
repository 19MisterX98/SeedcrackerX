package me.cortex.TreeCracker;

import kaptainwutax.mcutils.rand.ChunkRand;
import kaptainwutax.mcutils.version.MCVersion;
import kaptainwutax.seedutils.rand.JRand;
import me.cortex.TreeCracker.program.TreeCrackerProgram;
import me.cortex.TreeCracker.trees.Simple116BlobTree;
import me.cortex.TreeCracker.trees.TreePos;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        //TreeCrackerProgram program = new TreeCrackerProgram(
        //        new Simple116BlobTree(Simple116BlobTree.FOREST_OAK_TREE, new TreePos(5, 13), 4, new int[]{1,0,0,0,1,0,0,1,0,1,0,1}),
        //        new Simple116BlobTree(Simple116BlobTree.FOREST_OAK_TREE, new TreePos(11, 5), 6, new int[]{0,0,0,1,0,1,1,1,1,0,0,1}),
        //        new Simple116BlobTree(Simple116BlobTree.FOREST_OAK_TREE, new TreePos(13, 14), 5, new int[]{1,1,1,0,0,0,1,0,0,0,1,0})
        //);



        /*program = new TreeCrackerProgram(
                new Simple112BlobTree(Simple112BlobTree.FOREST_OAK_TREE, new TreePos(7,11), 4, new int[]{-1,-1,1,-1, -1,-1,0,-1,  -1,-1,-1,-1}),
                new Simple112BlobTree(Simple112BlobTree.FOREST_OAK_TREE, new TreePos(13,10), 5, new int[]{0,-1,1,-1,  0,-1,1,-1, -1,-1,-1,-1}),

                new Simple112BlobTree(Simple112BlobTree.FOREST_BIRCH_TREE, new TreePos(13,7), 7, new int[]{1,-1,1,-1,  1,-1,0,-1,  -1,-1,1,-1}),


                new Simple112BlobTree(Simple112BlobTree.FOREST_BIRCH_TREE, new TreePos(9,5), 7, new int[]{0,-1,1,-1,  1,-1,1,-1,  1,-1,1,-1}),

                new Simple112BlobTree(Simple112BlobTree.FOREST_BIRCH_TREE, new TreePos(4,8), 5, new int[]{0,0,1,-1,  1,1,0,-1,  1,-1,1,-1})

        );*/

        //program.generateCracker().exportSource(new File(net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().toFile(), "out.cu"));




    }
}
