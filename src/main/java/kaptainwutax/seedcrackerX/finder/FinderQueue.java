package kaptainwutax.seedcrackerX.finder;

import com.mojang.blaze3d.vertex.PoseStack;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.render.Cuboid;
import kaptainwutax.seedcrackerX.render.EndMainPassEvent;
import kaptainwutax.seedcrackerX.render.ExtractStateEvent;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class FinderQueue {

    private final static FinderQueue INSTANCE = new FinderQueue();
    private static final Logger log = LoggerFactory.getLogger(FinderQueue.class);
    public static ExecutorService SERVICE = Executors.newFixedThreadPool(5);

    private static final RenderStateDataKey<Set<Cuboid>> CUBOID_SET_KEY = RenderStateDataKey.create(() -> "SeedCrackerX cuboid set");

    public FinderControl finderControl = new FinderControl();

    private FinderQueue() {
        this.clear();
    }

    public static void registerEvents() {
        ExtractStateEvent.EXTRACT_STATE.register((state, camera, deltaTracker) -> FinderQueue.get().extractCuboids(state, camera, deltaTracker));
        EndMainPassEvent.END_MAIN_PASS.register((bufferSource, poseStack, state) -> FinderQueue.get().renderCuboids(bufferSource, poseStack, state));
    }

    public static FinderQueue get() {
        return INSTANCE;
    }

    public void onChunkData(Level world, ChunkPos chunkPos) {
        if (!Config.get().active) return;

        getActiveFinderTypes().forEach(type -> {
            SERVICE.submit(() -> {
                try {
                    List<Finder> finders = type.finderBuilder.build(world, chunkPos);

                    finders.forEach(finder -> {
                        if (finder.isValidDimension(world.dimensionType())) {
                            finder.findInChunk();
                            this.finderControl.addFinder(type, finder);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private void extractCuboids(LevelRenderState state, Camera camera, DeltaTracker deltaTracker) {
        if (Config.get().render == Config.RenderType.OFF) {
            state.setData(CUBOID_SET_KEY, Collections.emptySet());
            return;
        }
        Set<Cuboid> cuboids = new HashSet<>();
        this.finderControl.getActiveFinders().forEach(finder -> {
            if (finder.shouldRender()) {
                finder.cuboids.forEach(cuboid -> cuboids.add(cuboid.offset(camera)));
            }
        });
        state.setData(CUBOID_SET_KEY, cuboids);
    }

    public void renderCuboids(MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, LevelRenderState state) {
        Set<Cuboid> cuboids = state.getData(CUBOID_SET_KEY);
        if (cuboids == null) {
            return;
        }
        cuboids.forEach(cuboid -> cuboid.render(poseStack, bufferSource));
    }

    public List<Finder.Type> getActiveFinderTypes() {
        return Arrays.stream(Finder.Type.values())
                .filter(type -> type.enabled.get())
                .collect(Collectors.toList());
    }

    public void clear() {
        this.finderControl = new FinderControl();
    }
}
