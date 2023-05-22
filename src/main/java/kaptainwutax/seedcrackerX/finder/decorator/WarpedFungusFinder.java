package kaptainwutax.seedcrackerX.finder.decorator;

import kaptainwutax.seedcrackerX.Features;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.cracker.decorator.FullFungusData;
import kaptainwutax.seedcrackerX.cracker.decorator.WarpedFungus;
import kaptainwutax.seedcrackerX.finder.BlockFinder;
import kaptainwutax.seedcrackerX.finder.Finder;
import kaptainwutax.seedcrackerX.render.Color;
import kaptainwutax.seedcrackerX.render.Cube;
import kaptainwutax.seedcrackerX.render.Cuboid;
import kaptainwutax.seedcrackerX.util.BiomeFixer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarpedFungusFinder extends BlockFinder {
    private static final Logger logger = LoggerFactory.getLogger("warpedFungusFinder");

    private static final Predicate<Block> prdc = block -> (block == Blocks.SHROOMLIGHT || block == Blocks.WARPED_WART_BLOCK);

    public WarpedFungusFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, Blocks.WARPED_STEM);
        this.searchPositions = CHUNK_POSITIONS;
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new WarpedFungusFinder(world, chunkPos));

        finders.add(new WarpedFungusFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z)));
        finders.add(new WarpedFungusFinder(world, new ChunkPos(chunkPos.x, chunkPos.z - 1)));
        finders.add(new WarpedFungusFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z - 1)));

        finders.add(new WarpedFungusFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z)));
        finders.add(new WarpedFungusFinder(world, new ChunkPos(chunkPos.x, chunkPos.z + 1)));
        finders.add(new WarpedFungusFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z + 1)));

        finders.add(new WarpedFungusFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z - 1)));
        finders.add(new WarpedFungusFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z + 1)));

        return finders;
    }

    @Override
    public List<BlockPos> findInChunk() {
        Biome biome = this.world.getBiomeForNoiseGen((this.chunkPos.x << 2) + 2, 64, (this.chunkPos.z << 2) + 2).value();
        List<BlockPos> newResult = new ArrayList<>();

        if (!Features.WARPED_FUNGUS.isValidBiome(BiomeFixer.swap(biome))) return new ArrayList<>();

        List<BlockPos> result = super.findInChunk();

        List<FullFungusData> fullFungusData = new ArrayList<>();
        List<BlockBox> renderBox = new ArrayList<>();


        result.removeIf(pos -> {
            Block soil = this.world.getBlockState(pos.down()).getBlock();
            if (soil != Blocks.NETHERRACK && soil != Blocks.WARPED_NYLIUM) {
                return true;
            }

            int bigCount = 0;
            if (this.world.getBlockState(pos.north()).getBlock() == Blocks.WARPED_STEM) bigCount++;
            if (this.world.getBlockState(pos.east()).getBlock() == Blocks.WARPED_STEM) bigCount++;
            if (this.world.getBlockState(pos.west()).getBlock() == Blocks.WARPED_STEM) bigCount++;
            if (this.world.getBlockState(pos.south()).getBlock() == Blocks.WARPED_STEM) bigCount++;

            if (bigCount != 0 && bigCount != 4) return true;
            boolean big = bigCount == 4;

            newResult.add(pos);

            BlockPos.Mutable trunk = pos.mutableCopy();
            int i = 0;
            while (this.world.getBlockState(trunk.add(0, i, 0)).getBlock() == Blocks.WARPED_STEM) {
                i++;
            }
            if (i < 4 || i > 26 || (i > 14 && i % 2 == 1)) {
                return false;
            }

            int dataCounter = 0;
            ArrayList<Integer> bigTrunkData = new ArrayList<>();
            if (big) {
                dataCounter++;
                for (int x = -1; x < 2; x += 2) {
                    for (int z = -1; z < 2; z += 2) {
                        for (int height = 0; height < i; height++) {

                            Block block = this.world.getBlockState(trunk.add(x, height, z)).getBlock();
                            if (block == Blocks.WARPED_STEM) {
                                dataCounter++;
                                bigTrunkData.add(1);

                            } else if (isNetherPlant(block) || block == Blocks.WARPED_WART_BLOCK || block == Blocks.SHROOMLIGHT || block == Blocks.AIR) {
                                bigTrunkData.add(0);
                            }
                        }
                    }
                }
            }

            boolean validLayer;
            int height = i;

            ArrayList<Integer> layerSizes = new ArrayList<>();
            int upperLayerSize = 1;
            do {
                validLayer = false;
                for (int layerSize = upperLayerSize + 1; upperLayerSize - 1 <= layerSize; layerSize--) {

                    if (layerSize < 1) break;
                    int countRing = countRing(pos.add(0, i, 0), layerSize);

                    if (countRing == 0) {
                        i--;
                        layerSizes.add(layerSize);
                        upperLayerSize = layerSize;
                        validLayer = true;
                        break;

                    } else if (countRing == 3) {
                        return false; //found a wrong block
                    } else if (countRing == 2) break; //not enough blocks for a full layer, might be vines

                }
            } while (validLayer);
            if (layerSizes.size() < 2) return false;
            int heeeight = height;

            //testing for things inside of the vines
            int ring = countRing(pos.add(0, i, 0), upperLayerSize + 1);
            int vineRingSize = upperLayerSize;
            if (ring == 0 || ring == 2) {
                vineRingSize++;
            } else if (ring == 3) {
                return false;
            }
            for (int y = i - 2; y <= i; y++) {
                for (int x = -vineRingSize + 1; x < vineRingSize; x++) {
                    for (int z = -vineRingSize + 1; z < vineRingSize; z++) {
                        if (big && -2 < x && x < 2 && -2 < z && z < 2) continue;
                        if (x == 0 && z == 0) continue;
                        Block block = this.world.getBlockState(pos.add(x, y, z)).getBlock();
                        if (isNetherPlant(block) && block != Blocks.AIR)
                            return false;
                    }
                }
            }
            //vine data
            ArrayList<Integer> vine = new ArrayList<>();
            for (int x = -vineRingSize; x <= vineRingSize; x++) {
                for (int z = -vineRingSize; z <= vineRingSize; z++) {
                    if (Math.abs(x) != vineRingSize && Math.abs(z) != vineRingSize) continue;
                    boolean isVine = false;
                    for (int y = i - 2; y <= i; y++) {
                        Block block = this.world.getBlockState(pos.add(x, y, z)).getBlock();
                        if (block == Blocks.WARPED_WART_BLOCK) {
                            if (isVine) continue;
                            vine.add(i - y + 1);
                            //System.out.println(i-y+1+" at x: "+ x+" z: "+z);
                            isVine = true;
                            dataCounter++;
                        } else if (isNetherPlant(block) && block != Blocks.AIR || isVine) {
                            return false;
                        } else if (y == i) {
                            vine.add(0);
                            //System.out.println("0 at x: "+ x+" z: "+z);
                        }
                    }
                }
            }

            Collections.reverse(layerSizes);
            int counter = 0;
            int[][][] layers = new int[layerSizes.size()][11][11];
            for (int y = i + 1; y <= height; y++) {
                int layerSize = layerSizes.get(counter);
                for (int x = -layerSize; x <= layerSize; x++) {
                    for (int z = -layerSize; z <= layerSize; z++) {
                        Block block = this.world.getBlockState(pos.add(x, y, z)).getBlock();
                        int blocktype = -1;
                        if (block == Blocks.AIR) {
                            blocktype = 0;
                        } else if (block == Blocks.WARPED_WART_BLOCK) {
                            blocktype = 1;
                        } else if (block == Blocks.SHROOMLIGHT) {
                            dataCounter++;
                            blocktype = 2;
                        } else if (block == Blocks.WARPED_STEM) {
                            blocktype = 3;
                        } else {
                            blocktype = 0;
                            logger.error("error found illegal Block: " + block.getName() + " at " + pos.add(x, y, z).toShortString());
                        }
                        layers[counter][x + layerSize][z + layerSize] = blocktype;
                    }
                }
                counter++;
            }
            if (dataCounter < 13) return true;

            FullFungusData fungusData = new FullFungusData(layerSizes, layers, vine, big, height, vineRingSize, bigTrunkData, dataCounter);
            fullFungusData.add(fungusData);

            Collections.reverse(layerSizes);
            for (int layerSize : layerSizes) {
                renderBox.add(BlockBox.create(pos.add(layerSize + 1, heeeight, layerSize + 1), pos.add(-layerSize, heeeight + 1, -layerSize)));
                heeeight--;
            }
            renderBox.add(BlockBox.create(pos.add(-vineRingSize + 1, i - 2, -vineRingSize + 1), pos.add(vineRingSize, i + 1, vineRingSize)));
            renderBox.add(BlockBox.create(pos.add(-vineRingSize, i - 2, -vineRingSize), pos.add(vineRingSize + 1, i + 1, vineRingSize + 1)));

            return false;
        });

        if (fullFungusData.isEmpty()) return new ArrayList<>();
        if (newResult.size() < 5) return new ArrayList<>();

        FullFungusData bestFungus = FullFungusData.getBestFungus(fullFungusData);

        if (bestFungus == null) return new ArrayList<>();

        WarpedFungus.Data data = Features.WARPED_FUNGUS.at(chunkPos.getStartX(), chunkPos.getStartZ(), BiomeFixer.swap(biome), newResult, bestFungus);


        if (SeedCracker.get().getDataStorage().addBaseData(data, data::onDataAdded)) {

            for (BlockBox box : renderBox) {
                this.renderers.add(new Cuboid(box, new Color(0, 255, 255)));
            }

            for (BlockPos pos : newResult) {
                this.renderers.add(new Cube(pos, new Color(255, 30, 0)));
            }
        }
        return newResult;
    }

    private int countRing(BlockPos pos, int ringSize) {
        int counter = 0;
        int deco = 0;

        for (int x = -ringSize; x <= ringSize; x++) {
            for (int z = -ringSize; z <= ringSize; z++) {
                if (Math.abs(x) != ringSize && Math.abs(z) != ringSize) continue;
                counter++;
                Block block = this.world.getBlockState(pos.add(x, 0, z)).getBlock();

                if (prdc.test(block)) {
                    deco++;
                } else if (block != Blocks.AIR) {
                    //found something wrong
                    return 3;
                }
            }
        }
        //correct layer
        if (counter / 2 < deco) return 0;
        //nothing found
        if (deco == 0) return 1;
        //found not enough
        return 2;
    }

    private boolean isNetherPlant(Block block) {
        return block == Blocks.TWISTING_VINES || block == Blocks.TWISTING_VINES_PLANT || block == Blocks.NETHER_SPROUTS ||
                block == Blocks.WARPED_ROOTS || block == Blocks.WARPED_FUNGUS || block == Blocks.CRIMSON_FUNGUS ||
                block == Blocks.CRIMSON_ROOTS;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isNether(dimension);
    }
}
