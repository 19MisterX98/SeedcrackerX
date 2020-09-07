package kaptainwutax.seedcracker.util;

import java.util.function.BiPredicate;

public class Predicates {

	public static BiPredicate<Integer, Integer> EQUAL_TO = Integer::equals;
	public static BiPredicate<Integer, Integer> NOT_EQUAL_TO = (a, b) -> !a.equals(b);
	public static BiPredicate<Integer, Integer> LESS_THAN = (a, b) -> a < b;
	public static BiPredicate<Integer, Integer> MORE_THAN = (a, b) -> a > b;
	public static BiPredicate<Integer, Integer> LESS_OR_EQUAL_TO = (a, b) -> a <= b;
	public static BiPredicate<Integer, Integer> MORE_OR_EQUAL_TO = (a, b) -> a >= b;

}
