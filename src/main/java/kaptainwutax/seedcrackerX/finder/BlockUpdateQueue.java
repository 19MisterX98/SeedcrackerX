package kaptainwutax.seedcrackerX.finder;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.*;

public class BlockUpdateQueue {
    private final Queue<ArrayList<BlockPos>> blocks = new LinkedList<>();
    private final HashSet<BlockPos> alreadyChecked = new HashSet<>();

    public boolean add(ArrayList<BlockPos> blockPoses, BlockPos originPos) {
        if (alreadyChecked.add(originPos)) {
            blocks.add(blockPoses);
            return true;
        }
        return false;
    }

    public void tick() {
        if(blocks.isEmpty()) return;
        ArrayList<BlockPos> current = blocks.peek();
        for (int i = 0; i < 5; i++) {
            if(current.isEmpty()) {
                blocks.remove();
                if(blocks.isEmpty()) {
                    return;
                } else {
                    current = blocks.peek();
                }
            }
            if (MinecraftClient.getInstance().getNetworkHandler() == null) {
                blocks.clear();
                return;
            }
            //System.out.println(MinecraftClient.getInstance().world.getBlockState(current.get(0)));
            PlayerActionC2SPacket p = new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, current.remove(0),
                    Direction.DOWN);
            MinecraftClient.getInstance().getNetworkHandler().sendPacket(p);
        }
    }
}
