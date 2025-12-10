package kaptainwutax.seedcrackerX.structures;

import com.seedfinding.mcbiome.biome.Biome;
import com.seedfinding.mccore.state.Dimension;
import com.seedfinding.mccore.version.MCVersion;
import com.seedfinding.mccore.version.VersionMap;
import com.seedfinding.mcfeature.structure.RegionStructure;
import com.seedfinding.mcfeature.structure.UniformStructure;

public class TrialChambers extends UniformStructure<TrialChambers> {

    public static final VersionMap<Config> CONFIGS = new VersionMap<Config>()
            .add(MCVersion.v1_21, new Config(34, 12, 94251327));

    public TrialChambers(MCVersion version) {
        this(CONFIGS.getAsOf(version), version);
    }

    public TrialChambers(RegionStructure.Config config, MCVersion version) {
        super(config, version);
    }

    public static String name() {
        return "trial_chambers";
    }

    @Override
    public Dimension getValidDimension() {
        return Dimension.OVERWORLD;
    }

    @Override
    public boolean isValidBiome(Biome biome) {
        // FIXME: Deep Dark doesn't exist
        return true;
    }
}