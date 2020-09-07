package kaptainwutax.seedcracker.finder.decorator;

import kaptainwutax.seedcracker.finder.BlockFinder;
import kaptainwutax.seedcracker.util.PosIterator;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.*;
import java.util.stream.Collectors;

public abstract class OreFinder extends BlockFinder {

    public static final int LOWEST = -1;
    public static final int HIGHEST = 1;

    protected OreFeatureConfig oreFeatureConfig;

    public OreFinder(World world, ChunkPos chunkPos, OreFeatureConfig oreFeatureConfig) {
        super(world, chunkPos, oreFeatureConfig.state);
        this.oreFeatureConfig = oreFeatureConfig;
    }

    @Override
    public final List<BlockPos> findInChunk() {
        List<BlockPos> result = super.findInChunk();
        List<Set<BlockPos>> veins = new ArrayList<>();

        while(!result.isEmpty()) {
            Set<BlockPos> vein = this.buildVeinRecursively(result.get(0), new HashSet<>());
            vein.forEach(result::remove);
            veins.add(vein);
        }

        veins.removeIf(vein -> vein.size() > this.oreFeatureConfig.size);
        veins.removeIf(vein -> !this.isCompleteVein(vein));

        this.findOreVeins(veins);
        return result;
    }

    public Set<BlockPos> buildVeinRecursively(BlockPos start, Set<BlockPos> progress) {
        progress.add(start);

        PosIterator.create(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1)).forEach(offset -> {
            BlockPos pos = start.add(offset);
            BlockState state = this.world.getBlockState(pos);
            
            if(!progress.contains(pos) && state.equals(this.oreFeatureConfig.state)) {
                this.buildVeinRecursively(pos, progress);
            }
        });

        return progress;
    }

    private boolean isCompleteVein(Set<BlockPos> vein) {
        for(BlockPos pos: vein) {
            for(Direction direction: Direction.values()) {
                BlockState state = this.world.getBlockState(pos.offset(direction));
                if(!state.equals(this.oreFeatureConfig.state) &&
                        !this.oreFeatureConfig.target.test(state, null))return false;
            }
        }

        return true;
    }

    public boolean canVeinTo(BlockPos b1, BlockPos b2) {
        return Math.max(Math.max(Math.abs(b1.getX() - b2.getX()), Math.abs(b1.getY() - b2.getY())), Math.abs(b1.getZ() - b2.getZ())) <= 1;
    }

    public int findX(Set<BlockPos> vein, int type) {
        return getInt(vein.stream().map(BlockPos::getX).collect(Collectors.toList()), type);
    }

    public int findY(Set<BlockPos> vein, int type) {
        return getInt(vein.stream().map(BlockPos::getY).collect(Collectors.toList()), type);
    }

    public int findZ(Set<BlockPos> vein, int type) {
        return getInt(vein.stream().map(BlockPos::getZ).collect(Collectors.toList()), type);
    }

    private int getInt(List<Integer> ints, int type) {
        Collections.sort(ints);
        return ints.get(type == HIGHEST ? ints.size() - 1 : 0);
    }

    public abstract void findOreVeins(List<Set<BlockPos>> veins);

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isOverworld(dimension);
    }

    /*
    public boolean method_13628(float piRand, int nextIntA, int nextIntB, BlockPos blockPos_1, OreFeatureConfig oreFeatureConfig_1) {
        float float_2 = (float)oreFeatureConfig_1.size / 8.0F;
        int int_1 = MathHelper.ceil(((float)oreFeatureConfig_1.size / 16.0F * 2.0F + 1.0F) / 2.0F);
        double double_1 = (float)blockPos_1.getX() + MathHelper.sin(piRand) * float_2;
        double double_2 = (float)blockPos_1.getX() - MathHelper.sin(piRand) * float_2;
        double double_3 = (float)blockPos_1.getZ() + MathHelper.cos(piRand) * float_2;
        double double_4 = (float)blockPos_1.getZ() - MathHelper.cos(piRand) * float_2;
        double double_5 = blockPos_1.getY() + nextIntA - 2;
        double double_6 = blockPos_1.getY() + nextIntB - 2;
        int int_3 = blockPos_1.getX() - MathHelper.ceil(float_2) - int_1;
        int int_4 = blockPos_1.getY() - 2 - int_1;
        int int_5 = blockPos_1.getZ() - MathHelper.ceil(float_2) - int_1;
        int int_6 = 2 * (MathHelper.ceil(float_2) + int_1);
        int int_7 = 2 * (2 + int_1);

        for(int int_8 = int_3; int_8 <= int_3 + int_6; ++int_8) {
            for(int int_9 = int_5; int_9 <= int_5 + int_6; ++int_9) {
                return this.generateVeinPart(oreFeatureConfig_1, double_1, double_2, double_3, double_4, double_5, double_6, int_3, int_4, int_5, int_6, int_7);
            }
        }

        return false;
    }

    protected boolean generateVeinPart(OreFeatureConfig oreConfig, double double_1, double double_2, double double_3, double double_4, double double_5, double double_6, int int_1, int int_2, int int_3, int int_4, int int_5) {
        int int_6 = 0;
        BitSet bitSet_1 = new BitSet(int_4 * int_5 * int_4);
        BlockPos.Mutable blockPos$Mutable_1 = new BlockPos.Mutable();
        double[] oreData = new double[oreConfig.size * 4];

        for(int genStep = 0; genStep < oreConfig.size; ++genStep) {
            float genStepProgress = (float)genStep / (float)oreConfig.size;
            double xOffset = double_1 + (double_2 - double_1) * genStepProgress;
            double yOffset = double_5 + (double_6 - double_5) * genStepProgress;
            double zOffset = double_3 + (double_4 - double_3) * genStepProgress;
            double genSizeMultiplier = random_1.nextDouble() * (double)oreConfig.size / 16.0D;
            double randomBoundOffset = ((double)(MathHelper.sin((float)(Math.PI * genStepProgress)) + 1.0F) * genSizeMultiplier + 1.0D) / 2.0D;
            oreData[genStep * 4 + 0] = xOffset;
            oreData[genStep * 4 + 1] = yOffset;
            oreData[genStep * 4 + 2] = zOffset;
            oreData[genStep * 4 + 3] = randomBoundOffset;
        }

        for(int genStep = 0; genStep < oreConfig.size - 1; ++genStep) {
            if (oreData[genStep * 4 + 3] > 0.0D) {
                for(int int_9 = genStep + 1; int_9 < oreConfig.size; ++int_9) {
                    if (oreData[int_9 * 4 + 3] > 0.0D) {
                        double xOffset = oreData[genStep * 4 + 0] - oreData[int_9 * 4 + 0];
                        double yOffset = oreData[genStep * 4 + 1] - oreData[int_9 * 4 + 1];
                        double zOffset = oreData[genStep * 4 + 2] - oreData[int_9 * 4 + 2];
                        double genSizeMultiplier = oreData[genStep * 4 + 3] - oreData[int_9 * 4 + 3];
                        if (genSizeMultiplier * genSizeMultiplier > xOffset * xOffset + yOffset * yOffset + zOffset * zOffset) {
                            if (genSizeMultiplier > 0.0D) {
                                oreData[int_9 * 4 + 3] = -1.0D;
                            } else {
                                oreData[genStep * 4 + 3] = -1.0D;
                            }
                        }
                    }
                }
            }
        }

        for(int genStep = 0; genStep < oreConfig.size; ++genStep) {
            double randomBoundOffset = oreData[genStep * 4 + 3];
            if (randomBoundOffset < 0.0D)continue;

            double xOffset = oreData[genStep * 4 + 0];
            double yOffset = oreData[genStep * 4 + 1];
            double zOffset = oreData[genStep * 4 + 2];
            int lowerXbound = Math.max(MathHelper.floor(xOffset - randomBoundOffset), int_1);
            int lowerYbound = Math.max(MathHelper.floor(yOffset - randomBoundOffset), int_2);
            int lowerZbound = Math.max(MathHelper.floor(zOffset - randomBoundOffset), int_3);
            int upperXbound = Math.max(MathHelper.floor(xOffset + randomBoundOffset), lowerXbound);
            int upperYbound = Math.max(MathHelper.floor(yOffset + randomBoundOffset), lowerYbound);
            int upperZbound = Math.max(MathHelper.floor(zOffset + randomBoundOffset), lowerZbound);

            for(int attemptedPosX = lowerXbound; attemptedPosX <= upperXbound; ++attemptedPosX) {
                for(int attemptedPosY = lowerYbound; attemptedPosY <= upperYbound; ++attemptedPosY) {
                    for(int attemptedPosZ = lowerZbound; attemptedPosZ <= upperZbound; ++attemptedPosZ) {
                        double posX = ((double) attemptedPosX + 0.5D - xOffset) / randomBoundOffset;
                        double posY = ((double) attemptedPosY + 0.5D - yOffset) / randomBoundOffset;
                        double posZ = ((double) attemptedPosZ + 0.5D - zOffset) / randomBoundOffset;
                        boolean posInUnitSphere = posX * posX + posY * posY + posZ * posZ < 1.0D;

                        if(posInUnitSphere) {
                            int int_20 = attemptedPosX - int_1 + (attemptedPosY - int_2) * int_4 + (attemptedPosZ - int_3) * int_4 * int_5;
                            if (!bitSet_1.get(int_20)) {
                                bitSet_1.set(int_20);
                                blockPos$Mutable_1.set(attemptedPosX, attemptedPosY, attemptedPosZ);
                                if (oreConfig.target.getCondition().test(iWorld_1.getBlockState(blockPos$Mutable_1))) {
                                    iWorld_1.setBlockState(blockPos$Mutable_1, oreConfig.state, 2);
                                    ++int_6;
                                }
                            }
                        }
                    }
                }
            }
        }

        return int_6 > 0;
    }*/

}
