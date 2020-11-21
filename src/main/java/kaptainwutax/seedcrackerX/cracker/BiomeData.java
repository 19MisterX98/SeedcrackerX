package kaptainwutax.seedcrackerX.cracker;

import kaptainwutax.biomeutils.Biome;
import kaptainwutax.biomeutils.source.BiomeSource;
import kaptainwutax.seedcrackerX.SeedCracker;

import kaptainwutax.seedutils.mc.MCVersion;

public class BiomeData {

    public final Biome biome;
    public final int x;
    public final int z;

    public BiomeData(Biome biome, int x, int z) {
        this.biome = biome;
        this.x = x;
        this.z = z;
    }

    public boolean test(BiomeSource source) {
        if(SeedCracker.MC_VERSION.isNewerOrEqualTo(MCVersion.v1_15)) {
            return source.getBiomeForNoiseGen(this.x, 0, this.z) == this.biome;
        }else {
            return source.getBiome(this.x, 0, this.z) == this.biome;
        }
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof BiomeData))return false;
        BiomeData data = (BiomeData)o;
        return this.biome == data.biome;
    }

    @Override
    public int hashCode() {
        return this.biome.getName().hashCode();
    }
}
