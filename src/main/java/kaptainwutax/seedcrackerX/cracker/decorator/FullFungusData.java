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

    public FullFungusData(List<Integer> layerSizes, int[][][] layers, List<Integer> vines, boolean big, int height, int vineLayerSize) {
        this.layerSizes.addAll(layerSizes);
        this.layers = layers.clone();
        this.vines.addAll(vines);
        this.big = big;
        this.height = height;
        this.vineLayerSize = vineLayerSize;
    }

    public int getData() {
        //this is dirty. I have no clue on how to do this right
        int data= 0;
        for(int vine:vines) {
            if(vine != 0) {
                data++;
            }
        }
        data += layerSizes.size();
        if(big) {
            data += 2;
        }
        return data;
    }

    public static FullFungusData getBestFungus(List<FullFungusData> fungusList) {
        int data = 17;
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
