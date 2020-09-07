package kaptainwutax.seedcracker.finder.decorator;

import kaptainwutax.featureutils.decorator.EndGateway;
import kaptainwutax.seedcracker.Features;
import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.DataAddedEvent;
import kaptainwutax.seedcracker.finder.BlockFinder;
import kaptainwutax.seedcracker.finder.Finder;
import kaptainwutax.seedcracker.render.Color;
import kaptainwutax.seedcracker.render.Cuboid;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

public class EndGatewayFinder extends BlockFinder {

    protected static List<BlockPos> SEARCH_POSITIONS = buildSearchPositions(CHUNK_POSITIONS, pos -> {
        return false;
    });

    public EndGatewayFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, Blocks.END_GATEWAY);
        this.searchPositions = SEARCH_POSITIONS;
    }

    @Override
    public List<BlockPos> findInChunk() {
        Biome biome = this.world.getBiomeForNoiseGen((this.chunkPos.x << 2) + 2, 0, (this.chunkPos.z << 2) + 2);

        List<BlockPos> result = super.findInChunk();
        List<BlockPos> newResult = new ArrayList<>();

        result.forEach(pos -> {
            int height = this.findHeight(pos);

            if(height >= 3 && height <= 9) {
                newResult.add(pos);

                EndGateway.Data data = Features.END_GATEWAY.at(pos.getX(), pos.getZ(), height);

                if(SeedCracker.get().getDataStorage().addBaseData(data, DataAddedEvent.POKE_STRUCTURES)) {
                    this.renderers.add(new Cuboid(pos.add(-1, -2, -1), pos.add(2, 3, 2), new Color(102, 102, 210)));
                }
            }
        });

        return newResult;
    }

    private int findHeight(BlockPos pos) {
        int height = 0;

        while(pos.getY() >= 0) {
            pos = pos.down();
            height++;

            BlockState state = this.world.getBlockState(pos);

            //Bedrock generates below gateways.
            if(state.getBlock() == Blocks.BEDROCK || state.getBlock() != Blocks.END_STONE) {
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

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new EndGatewayFinder(world, chunkPos));
        return finders;
    }

}
