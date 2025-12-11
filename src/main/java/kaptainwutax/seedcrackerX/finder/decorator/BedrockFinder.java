package kaptainwutax.seedcrackerX.finder.decorator;

import com.seedfinding.mccore.version.MCVersion;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.cracker.DataAddedEvent;
import kaptainwutax.seedcrackerX.finder.Finder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

public class BedrockFinder extends Finder {

    public BedrockFinder(Level world, ChunkPos chunkPos) {
        super(world, chunkPos);
    }

    public static List<Finder> create(Level world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();

        if (!Config.get().getVersion().isNewerOrEqualTo(MCVersion.v1_18)) {
            return finders;
        }

        finders.add(new BedrockFinder(world, chunkPos));
        return finders;
    }

    @Override
    public List<BlockPos> findInChunk() {
        LevelChunk chunk = (LevelChunk) this.world.getChunk(this.chunkPos.x, this.chunkPos.z);
        BlockPos origin = this.chunkPos.getWorldPosition();

        if (this.isNether(this.world.dimensionType())) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    BlockPos floorPos = origin.offset(x, 4, z);
                    if (chunk.getBlockState(floorPos).getBlock() == Blocks.BEDROCK) {
                        SeedCracker.get().getDataStorage().addBedrockData(floorPos, DataAddedEvent.POKE_BEDROCK);
                    }

                    BlockPos roofPos = origin.offset(x, 123, z);
                    if (chunk.getBlockState(roofPos).getBlock() == Blocks.BEDROCK) {
                        SeedCracker.get().getDataStorage().addBedrockData(roofPos, DataAddedEvent.POKE_BEDROCK);
                    }
                }
            }
        } else if (this.isOverworld(this.world.dimensionType())) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    BlockPos pos = origin.offset(x, -60, z);
                    if (chunk.getBlockState(pos).getBlock() == Blocks.BEDROCK) {
                        SeedCracker.get().getDataStorage().addBedrockData(pos, DataAddedEvent.POKE_BEDROCK);
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isNether(dimension) || this.isOverworld(dimension);
    }

}
