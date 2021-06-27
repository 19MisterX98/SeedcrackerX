package me.cortex.TreeCracker.LCG;

import java.math.BigInteger;

public class DiscreteLog {

    private static long gcd(long a, long b) {
        if (b == 0)
            return a;
        return gcd(b, a % b);
    }

    // Returns modulo inverse of a with
    // respect to m using extended Euclid
    // Algorithm Assumption: a and m are
    // coprimes, i.e., gcd(a, m) = 1
    // stolen code, now handles the case where gcd(a,m) != 1
    private static long euclideanHelper(long a, long m) {
        long m0 = m;
        long y = 0, x = 1;
        if (m == 1)
            return 0;
        long gcd = gcd(a,m);
        while (a > gcd) {
            long q = a / m;
            long t = m;
            m = a % m;
            a = t;
            t = y;
            y = x - q * y;
            x = t;
        }
        if (x < 0)
            x += m0;
        return x;
    }

    private static long theta(long num) {
        if (num % 4 == 3) {
            num = (1L << 50) - num;
        }
        BigInteger xhat = BigInteger.valueOf(num);
        xhat = xhat.modPow(BigInteger.ONE.shiftLeft(49), BigInteger.ONE.shiftLeft(99));
        xhat = xhat.subtract(BigInteger.ONE);
        xhat = xhat.divide(BigInteger.ONE.shiftLeft(51));
        xhat = xhat.mod(BigInteger.ONE.shiftLeft(48));
        return xhat.longValue();
    }

    public static long distanceFromZero(long seed){
        long a = 25214903917L;
        long b = (((seed * (0x5deece66dL - 1)) * 179120439724963L) + 1) & ((1L<<50)-1);
        long abar = theta(a);
        long bbar = theta(b);
        long gcd = gcd(abar,(1L << 48));
        if (bbar % gcd != 0) {
            throw new Error();
        }
        return (bbar * euclideanHelper(abar,(1L << 48)) & 0xFFFFFFFFFFFFL)/gcd; //+ i*(1L << 48)/gcd;
    }

}