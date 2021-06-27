package me.cortex.TreeCracker.LCG;

import me.cortex.TreeCracker.NotImplementedException;
import me.cortex.TreeCracker.optional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

//TODO: make so that custom comparisons can be done, e.g. nextInt(nextInt(256)-5)
public class LcgTester {
    private static final float NLOG2 = -(float) Math.log(2);

    public int currentStep = 0;
    public List<LcgCall<?>> comparisons = new ArrayList<>();

    public List<LcgCall<?>> getComparisons() {
        return new ArrayList<>(comparisons);
    }

    public void advance(int calls) {
        currentStep += calls;
    }

    public void advance() {
        advance(1);
    }

    public LcgComparison<Float> nextFloat() {
        LcgCall<Float> comparison = new LcgCall<>(CallType.nextFloat, currentStep);
        comparisons.add(comparison);
        this.advance();
        return comparison;
    }

    public LcgComparison<Integer> nextInt() {
        LcgCall<Integer> comparison = new LcgCall<>(CallType.nextInt, currentStep);
        comparisons.add(comparison);
        this.advance();
        return comparison;
    }

    //TODO: put the bound into the comparison
    public LcgComparison<Integer> nextInt(int bound) {
        LcgCall<Integer> comparison = new LcgCall<>(CallType.nextIntBounded, currentStep, bound);
        comparisons.add(comparison);
        this.advance();
        return comparison;
    }

    public LcgComparison<Long> nextLong() {
        LcgCall<Long> comparison = new LcgCall<>(CallType.nextLong, currentStep);
        comparisons.add(comparison);
        this.advance();
        return comparison;
    }


    //TODO: put the bound into the comparison
    public LcgComparison<Boolean> nextBoolean() {
        LcgCall<Boolean> comparison = new LcgCall<>(CallType.nextBoolean, currentStep);
        comparisons.add(comparison);
        this.advance();
        return comparison;
    }


    public float getFilterPower() {
        return (float) comparisons.stream().mapToDouble(LcgCall::getFilterBitCount).sum();
    }



    //TODO: lock some of the comparisons when call type is of a specific thing, e.g. you cant compare a boolean with less than,
    //only equal or not equal to
    public static class LcgCall<T> extends LcgComparison<T> {
        public final CallType callType;

        //TODO: make callIndex a public getter but protected setter
        private int callIndex;
        public final optional<T> bound = optional.empty();

        protected LcgCall(CallType type, int callIndex) {
            this.callType = type;
            this.callIndex = callIndex;
        }

        protected LcgCall(CallType type, int callIndex, T bound) {
            this(type, callIndex);
            this.bound.set(bound);
        }

        public int getLCGCallSkipIndex() {
            return callIndex + 1;
        }

        public int getCallIndex() {
            return callIndex;
        }


        public float getFilterBitCount() {
            if (!configured)
                throw new IllegalStateException("Call is not configured");

            if (callType == CallType.nextBoolean)
                return 1;

            if (callType == CallType.nextFloat) {
                float comparingTo = (Float)this.comparingTo;
                if (comparingTo < 0)
                    throw new IllegalStateException("Comparing to negative float");

                switch (comparingType) {//TODO: FIX THIS IS ALL COMPLETLY WRONG
                    case equalTo:
                        return 24;
                    case notEqualTo:
                        return 0;//Not technically true, but close enough to being true
                    case lessThan:
                    case lessThanOrEqualTo://VERY DODGY as it does not take into account the 2^24 increments
                        return (float) Math.log(comparingTo)/NLOG2;//(int)(((double) comparingTo)*(1<<24))

                    case greaterThan:
                    case greaterThanOrEqualTo://VERY DODGY as it does not take into account the 2^24 increments
                        return (float) Math.log(1.0-comparingTo)/NLOG2;
                }
            }



            if (callType == CallType.nextInt) {
                int comparingTo = (Integer)this.comparingTo;
                //TODO: IMPLEMENT
                /*
                switch (comparingType) {
                    case equalTo:
                        return 32;
                    case notEqualTo:
                        return 0;//Not technically true, but close enough to being true

                    case lessThan:
                        return (float) Math.log((Float)comparingTo)/NLOG2;

                    case lessThanOrEqualTo:
                        return (float) Math.log((Float)comparingTo)/NLOG2;

                    case greaterThan:
                        return (float) Math.log((Float)comparingTo)/NLOG2;
                    case greaterThanOrEqualTo://Dodgy
                        return (float) Math.log(1.0-(Float)comparingTo)/NLOG2;
                }
                 */
                throw new IllegalStateException("Unimplmented unbounded nextint");
            }

            if (callType == CallType.nextIntBounded) {
                if (!bound.isPresent())
                    throw new IllegalStateException("Next int bounded call without bound present");
                int comparingTo = (Integer) this.comparingTo;
                int bound = (Integer) this.bound.get();

                if (comparingTo>=bound)
                    throw new IllegalStateException("compareing to greater than bound");
                //TODO: fix and add all the edge cases for everything
                switch (comparingType) {
                    case equalTo:
                    case greaterThan:
                    case greaterThanOrEqualTo:
                    case lessThan:
                    case lessThanOrEqualTo:
                        if (comparingTo < 0)
                            throw new IllegalStateException("Comparison less than zero");
                }


                switch (comparingType) {
                    case equalTo:
                        return (float) Math.log(1.0/bound)/NLOG2;
                    case notEqualTo:
                        return (float) Math.log((float) (bound - 1) / bound) / NLOG2;
                    case lessThan:
                        return (float) Math.log((float) (comparingTo) / bound) / NLOG2;//Check
                    case lessThanOrEqualTo:
                        return (float) Math.log((float) (comparingTo + 1) / bound) / NLOG2;//Check
                    case greaterThan:
                        return (float) Math.log(1-(float) (comparingTo) / bound) / NLOG2;//Check
                    case greaterThanOrEqualTo:
                        return (float) Math.log(1-(float) (comparingTo + 1) / bound) / NLOG2;//Check
                }
            }





            throw new IllegalStateException("Unknown call combination type to compute bit filter");
        }

        @Override
        protected LcgCall<T> clone() {
            LcgCall<T> clone = new LcgCall<T>(callType, callIndex, bound.get());
            clone.configured = this.configured;
            clone.comparingType = this.comparingType;
            clone.comparingTo = this.comparingTo;
            return clone;
        }


        public float getOperationCost() {
            switch (callType) {
                case nextIntBounded:
                    return (((Integer)bound.get()&((Integer)bound.get() - 1))==0)?1:10;
            }

            return 1;
        }



        public boolean evaluateLcgCall(Random random) {
            switch (callType) {
                case nextFloat:
                    return evaluateComparison((T)(Float)random.nextFloat());
                case nextIntBounded:
                    return evaluateComparison((T)(Integer)random.nextInt((Integer) bound.get()));
                case nextBoolean:
                    return evaluateComparison((T)(Boolean)random.nextBoolean());
                case nextDouble:
                    return evaluateComparison((T)(Double)random.nextDouble());
                case nextInt:
                    return evaluateComparison((T)(Integer)random.nextInt());
                case nextLong:
                    return evaluateComparison((T)(Long)random.nextLong());
                default:
                    throw new NotImplementedException();
            }
        }
    }

    public void replicateOnto(LcgTester tester) {
        int index = tester.currentStep;
        for (LcgCall<?> call : comparisons) {
            call = call.clone();
            call.callIndex += index;
            tester.comparisons.add(call);
        }
        tester.currentStep += currentStep;
    }

    public boolean doesRngPass(long seed) {
        for(LcgCall<?> call : comparisons) {
            if (!call.evaluateLcgCall(new Random(new ConfiguredLcg(call.getLCGCallSkipIndex()).nextSeed(seed)^0x5DEECE66DL)))
                return false;
        }
        return true;
    }



    public enum CallType {
        nextInt,
        nextIntBounded,
        nextBoolean,
        nextLong,
        nextFloat,
        nextDouble
    }
    public static final Comparator<LcgCall<?>> SORT_BY_COST = Comparator.comparing(LcgCall::getOperationCost);
    public static final Comparator<LcgCall<?>> SORT_BY_POWER = Comparator.comparing(call->-call.getFilterBitCount());
    public static final Comparator<LcgCall<?>> SORT_BY_INDEX = Comparator.comparing(LcgCall::getCallIndex);
    public static final Comparator<LcgCall<?>> SORT_BY_COST_THEN_POWER = SORT_BY_COST.thenComparing(SORT_BY_POWER).thenComparing(SORT_BY_INDEX);

}
