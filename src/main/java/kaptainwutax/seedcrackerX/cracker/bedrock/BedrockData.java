package kaptainwutax.seedcrackerX.cracker.bedrock;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BedrockData {

    private static final int NETHER_SIZE = 128;
    private static final int OVERWORLD_SIZE = 512;

    private final Set<BlockPos> netherRoof = new HashSet<>();
    private final Set<BlockPos> netherFloor = new HashSet<>();
    private final Set<BlockPos> overworld = new HashSet<>();

    public synchronized void add(BlockPos pos) {
        int y = pos.getY();

        if (y == 4 && netherFloor.size() < NETHER_SIZE) {
            netherFloor.add(pos);
        } else if (y == 123 && netherRoof.size() < NETHER_SIZE) {
            netherRoof.add(pos);
        } else if (y == -60 && overworld.size() < OVERWORLD_SIZE) {
            overworld.add(pos);
        }
    }

    public boolean canCrack() {
        return netherFloor.size() >= NETHER_SIZE || netherRoof.size() >= NETHER_SIZE;
    }

    public boolean hasOverworld() {
        return overworld.size() >= OVERWORLD_SIZE;
    }

    public synchronized List<BlockPos> getNetherRoof() {
        return new ArrayList<>(netherRoof);
    }

    public synchronized List<BlockPos> getNetherFloor() {
        return new ArrayList<>(netherFloor);
    }

    public synchronized List<BlockPos> getOverworld() {
        return new ArrayList<>(overworld);
    }

    public int getNetherFloorCount() {
        return netherFloor.size();
    }

    public int getNetherRoofCount() {
        return netherRoof.size();
    }

    public int getOverworldCount() {
        return overworld.size();
    }

    public synchronized void reset() {
        netherFloor.clear();
        netherRoof.clear();
        overworld.clear();
    }

}
