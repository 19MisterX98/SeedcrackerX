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
import kaptainwutax.seedcrackerX.util.Log;
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

public class WarpedFungusFinder extends BlockFinder {
    protected static List<BlockPos> SEARCH_POSITIONS = buildSearchPositions(CHUNK_POSITIONS, pos -> pos.getY() > 127);

    public WarpedFungusFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, Blocks.WARPED_STEM);
        this.searchPositions = SEARCH_POSITIONS;
    }

    @Override
    public List<BlockPos> findInChunk() {
        Biome biome = this.world.getBiomeForNoiseGen((this.chunkPos.x << 2) + 2, 0, (this.chunkPos.z << 2) + 2);
        List<BlockPos> newResult = new ArrayList<>();

        if(!Features.WARPED_FUNGUS.isValidBiome(BiomeFixer.swap(biome))) return new ArrayList<>();

        List<BlockPos> result = super.findInChunk();

        List<FullFungusData> fullFungusData = new ArrayList<>();
        List<BlockBox> renderBox = new ArrayList<>();


        result.removeIf(pos -> {
            Block soil = this.world.getBlockState(pos.down()).getBlock();
            if(soil != Blocks.NETHERRACK && soil != Blocks.WARPED_NYLIUM) {
                return true;
            }

            int bigCount = 0;
            if(this.world.getBlockState(pos.north()).getBlock() == Blocks.WARPED_STEM) bigCount++;
            if(this.world.getBlockState(pos.east()).getBlock() == Blocks.WARPED_STEM) bigCount++;
            if(this.world.getBlockState(pos.west()).getBlock() == Blocks.WARPED_STEM) bigCount++;
            if(this.world.getBlockState(pos.south()).getBlock() == Blocks.WARPED_STEM) bigCount++;

            if(bigCount != 0 && bigCount != 4) return true;
            boolean big = bigCount == 4;

            //this.renderers.add(new Cube(pos, new Color(255, 50, 157)));
            newResult.add(pos);

            BlockPos.Mutable trunk = pos.mutableCopy();
            int i = 0;
            while (this.world.getBlockState(trunk.add(0,i,0)).getBlock() == Blocks.WARPED_STEM) {
                i++;
            }
            if(i < 4 || i >26 || (i>14 && i % 2 ==1)) {
                return false;
            }
            //BlockBox box = new BlockBox(pos,pos.add(1,i,1));
            //this.renderers.add(new Cuboid(box, new Color(0, 255, 255)));


            boolean validLayer;
            int height = i;

            List<Integer> layerSizes = new ArrayList<>();
            int upperLayerSize = 1;
            do {
                validLayer = false;
                for(int layerSize = upperLayerSize+1;upperLayerSize-1<=layerSize;layerSize--) {
                    if(layerSize < 1) break;
                    int countRing = countRing(pos.add(0,i,0),layerSize);
                    //System.out.println(countRing);
                    if(countRing == 0) {

                        i--;
                        layerSizes.add(layerSize);
                        upperLayerSize = layerSize;
                        validLayer = true;
                        //BlockBox box = new BlockBox(pos.add(layerSize+1,i+1,layerSize+1),pos.add(-layerSize,i+2,-layerSize));

                        break;
                    } else if (countRing == 3) {
                        return false; //found a wrong block
                    }else if (countRing == 2) {break;} //not enough blocks for a full layer, might be vines

                }
            } while (validLayer);
            if(layerSizes.size()<2)return false;
            int heeeight = height;

            //testing for things inside of the vines
            int ring = countRing(pos.add(0,i,0),upperLayerSize+1);
            int vineRingSize = upperLayerSize;
            if(ring == 0 || ring == 2) {
                vineRingSize++;
            } else if (ring == 3) {
                return false;
            }
            for (int y = i-2;y <= i;y++) {
                for (int x = -vineRingSize+1; x < vineRingSize;x++) {
                    for (int z = -vineRingSize+1; z < vineRingSize; z++) {
                        if (x == 0 && z == 0) continue;
                        Block block = this.world.getBlockState(pos.add(x, y, z)).getBlock();
                        if (block != Blocks.AIR) return false;
                    }
                }
            }
            //vine data
            List<Integer> vine = new ArrayList<>();
            for (int x = -vineRingSize;x <= vineRingSize;x++) {
                for (int z = -vineRingSize;z <= vineRingSize;z++) {
                    if(Math.abs(x) != vineRingSize && Math.abs(z) != vineRingSize) continue;
                    boolean isVine = false;
                    for (int y = i-2;y <= i;y++) {
                        Block block = this.world.getBlockState(pos.add(x, y, z)).getBlock();
                        if(block.is(Blocks.WARPED_WART_BLOCK)) {
                            if(isVine) continue;
                            vine.add(i-y+1);
                            //System.out.println(i-y+1+" at x: "+ x+" z: "+z);
                            isVine = true;
                        }else if(!block.is(Blocks.AIR) || isVine) {
                            return false;
                        }else if (y ==i) {
                            vine.add(0);
                            //System.out.println("0 at x: "+ x+" z: "+z);
                        }
                    }
                }
            }

            for (int layerSize : layerSizes) {
                renderBox.add(new BlockBox(pos.add(layerSize + 1, heeeight, layerSize + 1), pos.add(-layerSize, heeeight + 1, -layerSize)));

                heeeight--;
            }
            renderBox.add(new BlockBox(pos.add(-vineRingSize+1,i-2,-vineRingSize+1),pos.add(vineRingSize,i+1,vineRingSize)));
            renderBox.add(new BlockBox(pos.add(-vineRingSize,i-2,-vineRingSize),pos.add(vineRingSize+1,i+1,vineRingSize+1)));

            Collections.reverse(layerSizes);
            int counter = 0;
            int[][][] layers = new int[layerSizes.size()][11][11];
            for(int y = i+1;y <= height;y++) {
                int layerSize = layerSizes.get(counter);
                for (int x = -layerSize;x<=layerSize;x++) {
                    for (int z = -layerSize;z<=layerSize;z++) {
                        Block block = this.world.getBlockState(pos.add(x, y, z)).getBlock();
                        int blocktype = -1;
                        if(block.is(Blocks.AIR)) {
                            blocktype = 0;
                        } else if(block.is(Blocks.WARPED_WART_BLOCK)) {
                            blocktype = 1;
                        } else if(block.is(Blocks.SHROOMLIGHT)) {
                            blocktype = 2;
                        } else if(block.is(Blocks.WARPED_STEM)) {
                            blocktype = 3;
                        } else {
                            blocktype = 0;
                            System.out.println("error found illegal Block: "+block.getName()+" at "+pos.add(x,y,z).toShortString());
                        }
                        layers[counter][x+layerSize][z+layerSize] = blocktype;
                    }
                }
                counter++;
            }
            FullFungusData fungusData = new FullFungusData(layerSizes,layers,vine,false,height,vineRingSize);

            if(fungusData.getData() < 18) return true;

            fullFungusData.add(fungusData);

            //System.out.println("Fungus at: "+pos.toShortString()+" layers: " +layerSizes.size()+"in chunk "+this.chunkPos.x+" "+this.chunkPos.z);

            return false;
        });

        //System.out.println(newResult.size()+" fullFungi: "+fullFungusData.size()+" "+this.chunkPos.x+" "+this.chunkPos.z);
        if(fullFungusData.isEmpty()) return new ArrayList<>();

        if(newResult.size() < 5) return new ArrayList<>();

        FullFungusData bestFungus = FullFungusData.getBestFungus(fullFungusData);

        if(bestFungus == null) return new ArrayList<>();

        System.out.println(newResult.size()+" fungi in chunk: "+this.chunkPos.x+" "+this.chunkPos.z);

        WarpedFungus.Data data = Features.WARPED_FUNGUS.at(chunkPos.getStartX(),chunkPos.getStartZ(),BiomeFixer.swap(biome),newResult,bestFungus);



        if(SeedCracker.get().getDataStorage().addBaseData(data, data::onDataAdded)) {

            Log.warn("Looking with positions: ");
            for(BlockPos pos:newResult) {
                Log.warn(pos.toShortString());
                this.renderers.add(new Cube(pos,new Color(200,0,0)));
            }
            System.out.println("best Fungus has "+bestFungus.layerSizes.size()+" layers in "+this.chunkPos.x+" "+this.chunkPos.z);
            for (BlockBox box:renderBox) {
                //todo add renderers to every fungus individualy
                this.renderers.add(new Cuboid(box, new Color(0, 255, 255)));
            }
            /*
            FullFungusData using = FullFungusData.getBestFungus(fullFungusData);
            for(int i = 0; i <= using.layerSizes.size();i++) {
                for (int x = 0;x <= using.layerSizes.get(i)*2;x++) {
                    String printer = "";
                    for (int z = 0;z <= using.layerSizes.get(i)*2;z++) {
                        printer += using.layers[i][x][z];
                    }
                    Log.debug(printer);
                }
                Log.debug("============");
            }*/
        }
        return newResult;
    }










    private static final Predicate<Block> prdc = block -> (block == Blocks.SHROOMLIGHT || block == Blocks.WARPED_WART_BLOCK);

    private int countRing(BlockPos pos,int ringSize) {
        int counter = 0;
        int deco =0;

        //todo check only rings
        for(int x = -ringSize;x <= ringSize; x++) {
            for(int z = -ringSize;z <= ringSize; z++) {
                if(Math.abs(x) != ringSize && Math.abs(z) != ringSize) continue;
                counter++;
                Block block = this.world.getBlockState(pos.add(x,0,z)).getBlock();

                if(prdc.test(block)) {
                    deco++;
                } else if (block != Blocks.AIR){
                    //found something wrong
                    return 3;
                }
            }
        }
        //correct layer
        if(counter / 2 < deco) return 0;
        //nothing found
        if (deco == 0) return 1;
        //found not enough
        return 2;
    }


    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isNether(dimension);
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new WarpedFungusFinder(world, chunkPos));


        /*
        finders.add(new WarpedFungusFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z)));
        finders.add(new WarpedFungusFinder(world, new ChunkPos(chunkPos.x, chunkPos.z - 1)));
        finders.add(new WarpedFungusFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z - 1)));

        finders.add(new WarpedFungusFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z)));
        finders.add(new WarpedFungusFinder(world, new ChunkPos(chunkPos.x, chunkPos.z + 1)));
        finders.add(new WarpedFungusFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z + 1)));

        finders.add(new WarpedFungusFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z - 1)));
        finders.add(new WarpedFungusFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z + 1)));
         */

        return finders;
    }
}
