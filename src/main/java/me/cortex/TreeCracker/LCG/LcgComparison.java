package me.cortex.TreeCracker.LCG;

public class LcgComparison<T> {
    public T comparingTo;
    public Type comparingType;
    boolean configured = false;

    public void greaterThan(T comparingTo) {
        if (configured)
            throw new IllegalStateException("LcgComparison already configured");
        configured = true;
        this.comparingTo = comparingTo;
        this.comparingType = Type.greaterThan;

    }

    public void lessThan(T comparingTo) {
        if (configured)
            throw new IllegalStateException("LcgComparison already configured");
        configured = true;
        this.comparingTo = comparingTo;
        this.comparingType = Type.lessThan;
    }

    public void greaterThanOrEqualTo(T comparingTo) {
        if (configured)
            throw new IllegalStateException("LcgComparison already configured");
        configured = true;
        this.comparingTo = comparingTo;
        this.comparingType = Type.greaterThanOrEqualTo;
    }

    public void lessThanOrEqualTo(T comparingTo) {
        if (configured)
            throw new IllegalStateException("LcgComparison already configured");
        configured = true;
        this.comparingTo = comparingTo;
        this.comparingType = Type.lessThanOrEqualTo;
    }

    public void equalTo(T comparingTo) {
        if (configured)
            throw new IllegalStateException("LcgComparison already configured");
        configured = true;
        this.comparingTo = comparingTo;
        this.comparingType = Type.equalTo;
    }

    public void notEqualTo(T comparingTo) {
        if (configured)
            throw new IllegalStateException("LcgComparison already configured");
        configured = true;
        this.comparingTo = comparingTo;
        this.comparingType = Type.notEqualTo;
    }


    public enum Type {
        greaterThan,
        lessThan,
        greaterThanOrEqualTo,
        lessThanOrEqualTo,
        equalTo,
        notEqualTo
    }


    public static String GetTypeStringOperator(Type type) {
        switch (type) {
            case equalTo:
                return "==";
            case lessThan:
                return "<";
            case notEqualTo:
                return "!=";
            case greaterThan:
                return ">";
            case lessThanOrEqualTo:
                return "<=";
            case greaterThanOrEqualTo:
                return ">=";
        }
        throw new IllegalStateException("Achievement unlocked: how tf did you get here");
    }
    public static String GetInvertedTypeStringOperator(Type type) {
        switch (type) {
            case equalTo:
                return "!=";
            case lessThan:
                return ">=";
            case notEqualTo:
                return "==";
            case greaterThan:
                return "<=";
            case lessThanOrEqualTo:
                return ">";
            case greaterThanOrEqualTo:
                return "<";
        }
        throw new IllegalStateException("Achievement unlocked: how tf did you get here");
    }


    public boolean evaluateComparison(T value) {
        Comparable<T> comparator = (Comparable<T>)value;
        int comparison = comparator.compareTo(comparingTo);
        switch (comparingType) {
            case greaterThanOrEqualTo:
                return comparison >= 0;
            case greaterThan:
                return comparison > 0;
            case lessThanOrEqualTo:
                return comparison <= 0;
            case lessThan:
                return comparison < 0;
            case equalTo:
                return comparison == 0;
            case notEqualTo:
                return comparison != 0;
        }
        throw new IllegalStateException("Idk, getting here should be impossible");
    }
}
