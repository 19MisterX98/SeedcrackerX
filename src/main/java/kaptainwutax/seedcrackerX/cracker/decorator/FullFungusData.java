package kaptainwutax.seedcrackerX.cracker.decorator;

import java.util.ArrayList;
import java.util.List;

public class FullFungusData {
    public final ArrayList<Integer> layerSizes = new ArrayList<>();
    public final int[][][] layers;
    public final ArrayList<Integer> vines = new ArrayList<>();
    public boolean big;
    public int height;
    public int vineLayerSize;
    public final ArrayList<Integer> bigtrunkData = new ArrayList<>();

    public FullFungusData(List<Integer> layerSizes, int[][][] layers, ArrayList<Integer> vines, boolean big, int height, int vineLayerSize, ArrayList<Integer> bigTrunkData) {
        this.layerSizes.addAll(layerSizes);
        this.layers = layers.clone();
        this.vines.addAll(vines);
        this.big = big;
        this.height = height;
        this.vineLayerSize = vineLayerSize;
        this.bigtrunkData.addAll(bigTrunkData);
    }

    public int getData() {
        //this is dirty. I have no clue on how to do this right
        int data= 0;
        for(int vine:this.vines) {
            if(vine != 0) {
                data++;
            }
        }
        data += this.layerSizes.size();
        if(this.big) {
            data += 2;
            for (int i:this.bigtrunkData) {
                if(i == 1)
                    data++;
            }
        }
        return data;
    }

    public static FullFungusData getBestFungus(List<FullFungusData> fungusList) {
        int data = 0;
        FullFungusData out = null;

        for(FullFungusData fungus:fungusList) {
            int fungusData = fungus.getData();
            if(fungusData>data) {
                data = fungusData;
                out = fungus;
            }
        }

        return out;
    }
}
