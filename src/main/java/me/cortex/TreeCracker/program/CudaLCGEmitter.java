package me.cortex.TreeCracker.program;

import me.cortex.TreeCracker.LCG.ConfiguredLcg;
import me.cortex.TreeCracker.LCG.LcgTester;

import java.util.stream.Collectors;

import static me.cortex.TreeCracker.LCG.LcgComparison.GetInvertedTypeStringOperator;

public class CudaLCGEmitter {
    public String comparisonFailedReturn;//Mutable, when emitting an lcg test, will return this on fail
    public String lcgVariableName;//Mutable, when emitting an lcg test, will use this as the lcg seed source, DOES NOT CHANGE IT





    //TODO: Optimization things
    public String emitLcgTest(LcgTester.LcgCall<?> call) {
        String invertedComparison = "";
        String lcg = emitLcg(new ConfiguredLcg(call.getLCGCallSkipIndex()));
        switch (call.callType) {
            case nextBoolean:
                boolean nextBoolEqual = true;
                switch (call.comparingType) {
                    case equalTo:
                        nextBoolEqual = !((Boolean) call.comparingTo);
                        break;
                    case notEqualTo:
                        nextBoolEqual = (Boolean) call.comparingTo;
                        break;
                    default:
                        throw new IllegalStateException("Invalid comparing type");
                }
                //We want the inverted comparison;
                nextBoolEqual = !nextBoolEqual;
                invertedComparison = String.format("(((%s)&(1LLU<<47))%s0)", lcg, nextBoolEqual?"==":"!=");
                break;

            case nextFloat:
                switch (call.comparingType) {
                    case lessThanOrEqualTo://NOTE!!!! Putting them under the same thing is quite dangeorus as it misses out on 1 state
                    case lessThan:
                        invertedComparison = String.format("((%s)&((1LLU<<24)-1)<<(48-24))>(%sLLU<<(48-24))", lcg,((long)(((double)(Float)call.comparingTo)*(1 << 24))));//Its greater than cause we want the inverse
                        break;
                    case greaterThanOrEqualTo:
                    case greaterThan:
                        invertedComparison = String.format("((%s)&((1LLU<<24)-1)<<(48-24))<(%sLLU<<(48-24))", lcg,((long)(((double)(Float)call.comparingTo)*(1 << 24))));//Its less than cause we want the inverse
                        break;
                    default:
                        throw new IllegalStateException("Invalid comparing type");
                }
                break;

            case nextIntBounded:
                int nextIntBound = (Integer) call.bound.get();
                if ((nextIntBound&(nextIntBound-1))==0) {//Its pow 2
                    int nextIntBoundBits = Integer.numberOfTrailingZeros(nextIntBound);
                    invertedComparison = String.format("(%s&(((1LLU<<%s)-1)<<(48-%s))) %s (%sLLU<<(48-%s))", lcg, nextIntBoundBits, nextIntBoundBits, GetInvertedTypeStringOperator(call.comparingType), call.comparingTo, nextIntBoundBits);
                } else {
                    lcg = String.format("(((%s>>(48-31))&((1LLU<<31)-1)) %% %d)",lcg,nextIntBound);
                    invertedComparison = String.format("%s %s %s", lcg, GetInvertedTypeStringOperator(call.comparingType), call.comparingTo);
                }
                break;

            default:
                throw new IllegalStateException("Call type not implemented");
        }


        return String.format("if (%s) %s;", invertedComparison, comparisonFailedReturn);
    }

    public String emitLcg(ConfiguredLcg lcg) {
        return String.format("((%s)*%dLLU + %dLLU)", lcgVariableName, lcg.multipler, lcg.addend);
    }

    //TODO: ADD AN OPTIMIZER THAT MOVES ALL THE MODULO CALLS TO THE END AND ADDS STUFF LIKE __syncthreads()
    //TODO: MAKE IT SORT BY MOST FILTER POWER FIRST over operation cost
    public String assembleLcgTester(LcgTester tester) {
        StringBuilder builder = new StringBuilder();
        for (LcgTester.LcgCall<?> call : tester.getComparisons().stream()
                .sorted(LcgTester.SORT_BY_COST_THEN_POWER)
                .collect(Collectors.toList())) {
            builder.append("\t");
            builder.append(emitLcgTest(call));
            builder.append("\n");
        }
        return builder.toString();
    }



    public String createFunction(String name, String returnType, String parameters, String contents) {
        String function = String.format("DEVICEABLE %s static inline %s(%s) {\n%s\n}",returnType,name,parameters,contents);

        return function;
    }
}
