package kaptainwutax.seedcrackerX.finder;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.List;

@FunctionalInterface
public interface FinderBuilder {

    List<Finder> build(Level world, ChunkPos chunkPos);

}
