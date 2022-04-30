package kaptainwutax.seedcrackerX.api;

import net.minecraftforge.eventbus.api.Event;

public class SeedPushEvent extends Event {
    private long seed;

    public SeedPushEvent(long seed) {
        this.seed = seed;
    }

    public long getSeed() {
        return seed;
    }
}
