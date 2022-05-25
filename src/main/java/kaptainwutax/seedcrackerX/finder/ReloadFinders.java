package kaptainwutax.seedcrackerX.finder;

import kaptainwutax.seedcrackerX.finder.decorator.EndPillarsFinder;
import kaptainwutax.seedcrackerX.finder.decorator.ore.EmeraldOreFinder;
import kaptainwutax.seedcrackerX.finder.structure.*;
import kaptainwutax.seedcrackerX.util.HeightContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class ReloadFinders {
    public MinecraftClient client = MinecraftClient.getInstance();

    public static void reloadHeight(int minY, int maxY) {
        Finder.CHUNK_POSITIONS.clear();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = minY; y < maxY; y++) {
                    Finder.CHUNK_POSITIONS.add(new BlockPos(x, y, z));
                }
            }
        }
        Finder.heightContext = new HeightContext(minY, maxY);

        EmeraldOreFinder.reloadSearchPositions();
        EndPillarsFinder.BedrockMarkerFinder.reloadSearchPositions();
        AbstractTempleFinder.reloadSearchPositions();
        BuriedTreasureFinder.reloadSearchPositions();
        EndCityFinder.reloadSearchPositions();
        MonumentFinder.reloadSearchPositions();
        OutpostFinder.reloadSearchPositions();
        IglooFinder.reloadSearchPositions();
    }

    public void reload() {
        int renderdistance = client.options.getViewDistance().getValue();

        int playerChunkX = (int) (Math.round(client.player.getX()) >> 4);
        int playerChunkZ = (int) (Math.round(client.player.getZ()) >> 4);
        for (int i = playerChunkX - renderdistance; i < playerChunkX + renderdistance; i++) {
            for (int j = playerChunkZ - renderdistance; j < playerChunkZ + renderdistance; j++) {
                FinderQueue.get().onChunkData(client.world, new ChunkPos(i, j));
            }
        }
    }
}
