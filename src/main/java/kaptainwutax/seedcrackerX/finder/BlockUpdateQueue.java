package kaptainwutax.seedcrackerX.finder;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class BlockUpdateQueue {
    private final Queue<Tuple<Thread, ArrayList<BlockPos>>> blocksAndAction = new LinkedList<>();
    private final HashSet<BlockPos> alreadyChecked = new HashSet<>();

    public boolean add(ArrayList<BlockPos> blockPoses, BlockPos originPos, Thread operationAtEnd) {
        if (alreadyChecked.add(originPos)) {
            blocksAndAction.add(new Tuple<>(operationAtEnd, blockPoses));
            return true;
        }
        return false;
    }

    public void tick() {
        if (blocksAndAction.isEmpty()) return;

        Tuple<Thread, ArrayList<BlockPos>> current = blocksAndAction.peek();
        ArrayList<BlockPos> currentBlocks = current.getB();
        for (int i = 0; i < 5; i++) {
            if (currentBlocks.isEmpty()) {
                current.getA().start();
                blocksAndAction.remove();
                if (blocksAndAction.isEmpty()) {
                    return;
                } else {
                    current = blocksAndAction.peek();
                    currentBlocks = current.getB();
                }
            }
            if (Minecraft.getInstance().getConnection() == null) {
                blocksAndAction.clear();
                return;
            }
            ServerboundPlayerActionPacket p = new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, currentBlocks.remove(0),
                    Direction.DOWN);
            Minecraft.getInstance().getConnection().send(p);
        }
    }
}
