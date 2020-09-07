package kaptainwutax.seedcracker.finder.decorator;

import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.DataAddedEvent;
import kaptainwutax.seedcracker.cracker.PillarData;
import kaptainwutax.seedcracker.finder.BlockFinder;
import kaptainwutax.seedcracker.finder.Finder;
import kaptainwutax.seedcracker.render.Color;
import kaptainwutax.seedcracker.render.Cube;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EndPillarsFinder extends Finder {

    private boolean alreadyFound;
    protected BedrockMarkerFinder[] bedrockMarkers = new BedrockMarkerFinder[10];

    public EndPillarsFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos);

        this.alreadyFound = !SeedCracker.get().getDataStorage().addPillarData(null, DataAddedEvent.POKE_PILLARS);
        if(this.alreadyFound)return;

        for(int i = 0; i < this.bedrockMarkers.length; i++) {
            int x = MathHelper.floor(42.0D * Math.cos(2.0D * (-Math.PI + (Math.PI / 10.0D) * (double)i)));
            int z = MathHelper.floor(42.0D * Math.sin(2.0D * (-Math.PI + (Math.PI / 10.0D) * (double)i)));
            this.bedrockMarkers[i] = new BedrockMarkerFinder(this.world, new ChunkPos(new BlockPos(x, 0, z)), new BlockPos(x, 0, z));
        }
    }

    @Override
    public List<BlockPos> findInChunk() {
        List<BlockPos> result = new ArrayList<>();

        for(BedrockMarkerFinder bedrockMarker: this.bedrockMarkers) {
            if(bedrockMarker == null)continue;
            result.addAll(bedrockMarker.findInChunk());
        }

        if(result.size() == this.bedrockMarkers.length) {
            PillarData pillarData = new PillarData(result.stream().map(Vec3i::getY).collect(Collectors.toList()));

            if(SeedCracker.get().getDataStorage().addPillarData(pillarData, DataAddedEvent.POKE_PILLARS)) {
                result.forEach(pos -> this.renderers.add(new Cube(pos, new Color(128, 0, 128))));
            }

        }

        return result;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isEnd(dimension);
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new EndPillarsFinder(world, chunkPos));
        return finders;
    }

    public static class BedrockMarkerFinder extends BlockFinder {

        protected static List<BlockPos> SEARCH_POSITIONS = buildSearchPositions(CHUNK_POSITIONS, pos -> {
            if(pos.getY() < 76)return true;
            if(pos.getY() > 76 + 3 * 10)return true;
            return false;
        });

        public BedrockMarkerFinder(World world, ChunkPos chunkPos, BlockPos xz) {
            super(world, chunkPos, Blocks.BEDROCK);
            this.searchPositions = SEARCH_POSITIONS;
        }

        @Override
        public List<BlockPos> findInChunk() {
            return super.findInChunk();
        }

        @Override
        public boolean isValidDimension(DimensionType dimension) {
            return true;
        }

    }

}
