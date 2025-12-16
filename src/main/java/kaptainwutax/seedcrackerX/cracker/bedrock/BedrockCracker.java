package kaptainwutax.seedcrackerX.cracker.bedrock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BedrockCracker {

    private final BedrockData data;

    public BedrockCracker(BedrockData data) {
        this.data = data;
    }

    public Set<Long> crackStructureSeeds() {
        if (!data.canCrack()) {
            return new HashSet<>();
        }

        NetherCracker cracker = new NetherCracker(data);
        List<Long> seeds = cracker.crack();

        return new HashSet<>(seeds);
    }

    public Set<Long> crackWorldSeeds(Set<Long> structureSeeds) {
        if (structureSeeds.isEmpty()) {
            return new HashSet<>();
        }

        List<Long> seeds = new ArrayList<>();

        if (data.hasOverworld()) {
            for (long structureSeed : structureSeeds) {
                OverworldCracker.addToList(structureSeed, seeds, data);
            }
        }

        return new HashSet<>(seeds);
    }

}
