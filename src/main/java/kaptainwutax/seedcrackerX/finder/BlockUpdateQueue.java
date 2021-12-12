package kaptainwutax.seedcrackerX.finder;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class BlockUpdateQueue {
    private final Queue<Pair<Thread, ArrayList<BlockPos>>> blocksAndAction = new LinkedList<>();
    private final HashSet<BlockPos> alreadyChecked = new HashSet<>();

    public boolean add(ArrayList<BlockPos> blockPoses, BlockPos originPos, Thread operationAtEnd) {
        if (alreadyChecked.add(originPos)) {
            blocksAndAction.add(new Pair<>(operationAtEnd, blockPoses));
            return true;
        }
        return false;
    }

    public void tick() {
        if (blocksAndAction.isEmpty()) return;

        Pair<Thread, ArrayList<BlockPos>> current = blocksAndAction.peek();
        ArrayList<BlockPos> currentBlocks = current.getRight();
        for (int i = 0; i < 5; i++) {
            if (currentBlocks.isEmpty()) {
                current.getLeft().start();
                blocksAndAction.remove();
                if (blocksAndAction.isEmpty()) {
                    return;
                } else {
                    current = blocksAndAction.peek();
                    currentBlocks = current.getRight();
                }
            }
            if (MinecraftClient.getInstance().getNetworkHandler() == null) {
                blocksAndAction.clear();
                return;
            }
            PlayerActionC2SPacket p = new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, currentBlocks.remove(0),
                    Direction.DOWN);
            MinecraftClient.getInstance().getNetworkHandler().sendPacket(p);
        }
    }
}
