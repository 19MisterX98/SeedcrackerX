package kaptainwutax.seedcrackerX.cracker.decorator;

import com.seedfinding.latticg.reversal.DynamicProgram;
import com.seedfinding.latticg.reversal.calltype.java.JavaCalls;
import com.seedfinding.latticg.util.LCG;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

public class FullFungusData {


    public final ArrayList<Integer> layerSizes = new ArrayList<>();
    public final int[][][] layers;
    public final ArrayList<Integer> vines = new ArrayList<>();
    public final ArrayList<Integer> bigtrunkData = new ArrayList<>();
    public final int estimatedData;
    public boolean big;
    public int height;
    public int vineLayerSize;

    public FullFungusData(List<Integer> layerSizes, int[][][] layers, ArrayList<Integer> vines, boolean big, int height, int vineLayerSize, ArrayList<Integer> bigTrunkData, int estimatedData) {
        this.layerSizes.addAll(layerSizes);
        this.layers = layers.clone();
        this.vines.addAll(vines);
        this.big = big;
        this.height = height;
        this.vineLayerSize = vineLayerSize;
        this.bigtrunkData.addAll(bigTrunkData);
        this.estimatedData = estimatedData;
    }


    public static FullFungusData getBestFungus(List<FullFungusData> fungusList) {
        int data = 0;
        FullFungusData out = null;

        for (FullFungusData fungus : fungusList) {
            int fungusData = fungus.estimatedData;
            if (fungusData > data) {
                data = fungusData;
                out = fungus;
            }
        }

        return out;
    }

    public LongStream crackSeed() {
        int doppelt = 0;
        if (height > 7 && height % 2 == 0) {
            doppelt = 1;
            if (height > 13) {
                doppelt = 2;
            }
        }

        DynamicProgram device = DynamicProgram.create(LCG.JAVA);
        if (doppelt < 2) {
            device.skip(2);
        } else {
            device.skip(1);
            device.add(JavaCalls.nextInt(12).equalTo(0));
        }
        if (big) {
            device.add(JavaCalls.nextFloat().betweenII(0F, 0.06F));
        } else {
            device.skip(1);
        }

        if (big) {
            for (int blockdata : bigtrunkData) {
                if (blockdata == 0) {
                    device.skip(1);
                } else if (blockdata == 1) {
                    device.add(JavaCalls.nextFloat().betweenII(0F, 0.1F));
                }
            }
        }

        device.skip(2);

        ArrayList<Integer> done = new ArrayList<>();
        for (int j = 3; j > 0; j--) {

            for (int i = 0; i < vineLayerSize * 8; i++) {
                if (vines.get(i) == 0 && !done.contains(i)) {
                    device.skip(1);

                } else if (vines.get(i) == j) {
                    done.add(i);
                    device.add(JavaCalls.nextFloat().betweenII(0F, 0.15F));

                } else if (!done.contains(i)) {
                    device.skip(1);

                }
            }

            device.skip(1);
        }
        int relativePos;
        int blockType;
        int layer = 0;

        for (int size : layerSizes) {
            size *= 2;

            for (int x = 0; x <= size; x++) {

                boolean siteX = x == 0 || x == size;
                for (int z = 0; z <= size; z++) {

                    boolean siteZ = z == 0 || z == size;

                    relativePos = (siteX ? 1 : 0) + (siteZ ? 1 : 0);

                    blockType = layers[layer][x][z];

                    generateBlock(relativePos, blockType, device);
                }
            }

            device.skip(1);
            layer++;
        }
        return device.reverse();
    }

    private void generateBlock(int relativePos, int blockType, DynamicProgram device) {

        if (blockType == 3) return;
        switch (relativePos) {
            case 0:
                //Inside
                switch (blockType) {
                    case 0:
                        device.skip(2);
                        break;
                    case 1:
                        device.skip(3);
                        break;
                    case 2:
                        device.add(JavaCalls.nextFloat().betweenII(0F, 0.1F));
                }
                break;
            case 1:
                //Wall
                switch (blockType) {
                    case 0:
                        device.skip(1);
                        device.add(JavaCalls.nextFloat().betweenII(0.98F, 1F));
                        break;
                    case 1:
                        device.skip(3);
                        break;
                    case 2:
                        device.add(JavaCalls.nextFloat().betweenII(0F, 5.0E-4F));
                }
                break;
            case 2:
                //Corner
                switch (blockType) {
                    case 0:
                        device.skip(2);
                        break;
                    case 1:
                        device.skip(3);
                        break;
                    case 2:
                        device.add(JavaCalls.nextFloat().betweenII(0F, 0.01F));
                }
                break;
        }
    }
}
