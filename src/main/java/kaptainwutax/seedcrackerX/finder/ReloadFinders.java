package kaptainwutax.seedcrackerX.finder;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.ChunkPos;

public class ReloadFinders {
    public MinecraftClient client = MinecraftClient.getInstance();
    public void reload() {
        int renderdistance = client.options.viewDistance;

        int playerChunkX = (int) (Math.round(client.player.getX()) >> 4);
        int playerChunkZ = (int) (Math.round(client.player.getZ()) >> 4);
        for(int i = playerChunkX - renderdistance;i < playerChunkX + renderdistance; i++) {
            for(int j = playerChunkZ - renderdistance;j < playerChunkZ + renderdistance; j++) {
                FinderQueue.get().onChunkData(client.world, new ChunkPos(i, j));
            }
        }
    }
}
