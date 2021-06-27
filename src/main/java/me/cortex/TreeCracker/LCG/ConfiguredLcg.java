package me.cortex.TreeCracker.LCG;

public class ConfiguredLcg {

    public final long multipler;
    public final long addend;

    public ConfiguredLcg(int skips) {
        long multiplier = 1;
        long addend = 0;

        long intermediateMultiplier = 0x5DEECE66DL;
        long intermediateAddend = 0xBL;

        for(long k = skips; k != 0; k >>>= 1) {
            if((k & 1) != 0) {
                multiplier *= intermediateMultiplier;
                addend = intermediateMultiplier * addend + intermediateAddend;
            }

            intermediateAddend = (intermediateMultiplier + 1) * intermediateAddend;
            intermediateMultiplier *= intermediateMultiplier;
        }

        this.multipler = multiplier & ((1L<<48)-1);
        this.addend = addend & ((1L<<48)-1);
    }




    public long nextSeed(long seed) {
        return (seed * multipler + addend)&((1L<<48)-1);
    }
}
