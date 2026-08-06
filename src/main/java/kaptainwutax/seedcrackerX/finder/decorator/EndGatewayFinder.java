package kaptainwutax.seedcrackerX.finder.decorator;

import com.seedfinding.mcfeature.decorator.EndGateway;
import kaptainwutax.seedcrackerX.Features;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.cracker.DataAddedEvent;
import kaptainwutax.seedcrackerX.finder.BlockFinder;
import kaptainwutax.seedcrackerX.finder.Finder;
import kaptainwutax.seedcrackerX.render.Color;
import kaptainwutax.seedcrackerX.render.Cuboid;
import kaptainwutax.seedcrackerX.util.BiomeFixer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

public class EndGatewayFinder extends BlockFinder {

    public EndGatewayFinder(Level world, ChunkPos chunkPos) {
        super(world, chunkPos, Blocks.END_GATEWAY);
        this.searchPositions = CHUNK_POSITIONS;
    }

    public static List<Finder> create(Level world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new EndGatewayFinder(world, chunkPos));
        return finders;
    }

    @Override
    public List<BlockPos> findInChunk() {
        Biome biome = this.world.getNoiseBiome((this.chunkPos.x << 2) + 2, 64, (this.chunkPos.z << 2) + 2).value();
        if(!Features.END_GATEWAY.isValidBiome(BiomeFixer.swap(biome)))return new ArrayList<>();

        List<BlockPos> result = super.findInChunk();
        List<BlockPos> newResult = new ArrayList<>();

        result.forEach(pos -> {
            int height = this.findHeight(pos);

            if (height >= 3 && height <= 9) {
                newResult.add(pos);

                EndGateway.Data data = Features.END_GATEWAY.at(pos.getX(), pos.getZ(), height);

                if (SeedCracker.get().getDataStorage().addBaseData(data, DataAddedEvent.POKE_STRUCTURES)) {
                    this.renderers.add(new Cuboid(pos.offset(-1, -2, -1), pos.offset(2, 3, 2), new Color(102, 102, 210)));
                }
            }
        });

        return newResult;
    }

    private int findHeight(BlockPos pos) {
        int height = 0;

        while (pos.getY() >= 0) {
            pos = pos.below();
            height++;

            BlockState state = this.world.getBlockState(pos);

            //Bedrock generates below gateways.
            if (state.getBlock() == Blocks.BEDROCK || state.getBlock() != Blocks.END_STONE) {
                continue;
            }

            break;
        }

        return height - 1;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isEnd(dimension);
    }

}
