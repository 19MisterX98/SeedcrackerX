package kaptainwutax.seedcrackerX.cracker.storage;

import com.seedfinding.mccore.version.MCVersion;
import com.seedfinding.mcfeature.Feature;
import com.seedfinding.mcfeature.decorator.DesertWell;
import com.seedfinding.mcfeature.decorator.EndGateway;
import com.seedfinding.mcfeature.structure.*;
import kaptainwutax.seedcrackerX.config.ConfigScreen;
import kaptainwutax.seedcrackerX.cracker.BiomeData;
import kaptainwutax.seedcrackerX.cracker.DataAddedEvent;
import kaptainwutax.seedcrackerX.cracker.HashedSeedData;
import kaptainwutax.seedcrackerX.cracker.PillarData;
import kaptainwutax.seedcrackerX.cracker.decorator.*;
import kaptainwutax.seedcrackerX.finder.BlockUpdateQueue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class DataStorage {

    public static final Comparator<Entry<Feature.Data<?>>> SEED_DATA_COMPARATOR = (s1, s2) -> {
        boolean isStructure1 = s1.data.feature instanceof Structure;
        boolean isStructure2 = s2.data.feature instanceof Structure;

        //Structures always come before decorators.
        if (isStructure1 != isStructure2) {
            return isStructure2 ? 1 : -1;
        }

        if (s1.equals(s2)) {
            return 0;
        }

        double diff = getBits(s2.data.feature, false) - getBits(s1.data.feature, false);
        return diff == 0 ? 1 : (int) Math.signum(diff);
    };
    public ScheduledSet<Entry<Feature.Data<?>>> baseSeedData = new ScheduledSet<>(SEED_DATA_COMPARATOR);
    public HashedSeedData hashedSeedData = null;
    public BlockUpdateQueue blockUpdateQueue = new BlockUpdateQueue();
    public boolean openGui = false;
    protected TimeMachine timeMachine = new TimeMachine(this);
    protected Set<Consumer<DataStorage>> scheduledData = ConcurrentHashMap.newKeySet();
    protected PillarData pillarData = null;
    protected ScheduledSet<Entry<BiomeData>> biomeSeedData = new ScheduledSet<>(null);

    public static double getBits(Feature<?, ?> feature, boolean decorators18) {
        if (feature instanceof UniformStructure<?> s) {
            return Math.log(s.getOffset() * s.getOffset()) / Math.log(2);
        } else if (feature instanceof TriangularStructure<?> s) {
            return Math.log(s.getPeak() * s.getPeak()) / Math.log(2);
        }
        if (!decorators18 && feature instanceof Decorator && feature.getVersion().isNewerThan(MCVersion.v1_17_1))
            return 0;
        if (feature instanceof BuriedTreasure) return Math.log(100) / Math.log(2);
        if (feature instanceof DesertWell) return Math.log(1000 * 16 * 16) / Math.log(2);
        if (feature instanceof Dungeon) return Math.log(256 * 16 * 16 * 0.125D) / Math.log(2);
        if (feature instanceof DeepDungeon) return Math.log(58 * 16 * 16 * 0.25D) / Math.log(2);
        if (feature instanceof EmeraldOre) return Math.log(28 * 16 * 16 * 0.5D) / Math.log(2);
        if (feature instanceof EndGateway) return Math.log(700 * 16 * 16 * 7) / Math.log(2);
        if (feature instanceof WarpedFungus) return 0;

        throw new UnsupportedOperationException("go do implement bits count for " + feature.getName() + " you fool");
    }

    public void tick() {
        if (openGui) {
            ConfigScreen configscreen = new ConfigScreen();
            Screen screen = configscreen.getConfigScreenByCloth(MinecraftClient.getInstance().currentScreen);
            MinecraftClient.getInstance().setScreen(screen);
            openGui = false;
        }
        if (!this.timeMachine.isRunning) {
            this.baseSeedData.dump();
            this.biomeSeedData.dump();
            blockUpdateQueue.tick();

            this.timeMachine.isRunning = true;

            TimeMachine.SERVICE.submit(() -> {
                try {
                    this.scheduledData.removeIf(c -> {
                        c.accept(this);
                        return true;
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                this.timeMachine.isRunning = false;
            });
        }
    }

    public synchronized boolean addPillarData(PillarData data, DataAddedEvent event) {
        boolean isAdded = this.pillarData == null;

        if (isAdded && data != null) {
            this.pillarData = data;
            this.schedule(event::onDataAdded);
        }

        return isAdded;
    }

    public synchronized boolean addBaseData(Feature.Data<?> data, DataAddedEvent event) {
        Entry<Feature.Data<?>> e = new Entry<>(data, event);

        if (this.baseSeedData.contains(e)) {
            return false;
        }

        this.baseSeedData.scheduleAdd(e);
        this.schedule(event::onDataAdded);
        return true;
    }

    public synchronized boolean addBiomeData(BiomeData data, DataAddedEvent event) {
        Entry<BiomeData> e = new Entry<>(data, event);

        if (this.biomeSeedData.contains(e)) {
            return false;
        }

        this.biomeSeedData.scheduleAdd(e);
        this.schedule(event::onDataAdded);
        return true;
    }

    public synchronized boolean addHashedSeedData(HashedSeedData data, DataAddedEvent event) {
        if (this.hashedSeedData == null || this.hashedSeedData.getHashedSeed() != data.getHashedSeed()) {
            this.hashedSeedData = data;
            this.schedule(event::onDataAdded);
            return true;
        }

        return false;
    }

    public void schedule(Consumer<DataStorage> consumer) {
        this.scheduledData.add(consumer);
    }

    public TimeMachine getTimeMachine() {
        return this.timeMachine;
    }

    public double getBaseBits() {
        double bits = 0.0D;

        for (Entry<Feature.Data<?>> e : this.baseSeedData) {
            if (!(e.data.feature instanceof PillagerOutpost)) {
                bits += getBits(e.data.feature, false);
            }
        }
        return bits;
    }

    public double getLiftingBits() {
        double bits = 0.0D;

        for (Entry<Feature.Data<?>> e : this.baseSeedData) {
            if (e.data.feature instanceof OldStructure structure) {
                bits += Math.log(structure.getOffset() * structure.getOffset()) / Math.log(2);
            } else if (e.data.feature instanceof Shipwreck shipwreck) {
                bits += Math.log(shipwreck.getOffset() * shipwreck.getOffset()) / Math.log(2);
            }
        }
        return bits;
    }

    public double getDecoratorBits() {
        double bits = 0.0D;

        for (Entry<Feature.Data<?>> e : this.baseSeedData) {
            if (e.data.feature instanceof Decorator decorator) {
                bits += getBits(decorator, true);
            }
        }
        return bits;
    }

    public double getWantedBits() {
        return 32.0D;
    }

    public boolean notEnoughBiomeData() {
        return this.biomeSeedData.size() < 7;
    }

    public void clear() {
        this.scheduledData = ConcurrentHashMap.newKeySet();
        this.pillarData = null;
        this.baseSeedData = new ScheduledSet<>(SEED_DATA_COMPARATOR);
        this.biomeSeedData = new ScheduledSet<>(null);
        //this.hashedSeedData = null;
        this.timeMachine.shouldTerminate = true;
        this.timeMachine = new TimeMachine(this);
        this.blockUpdateQueue = new BlockUpdateQueue();
    }

    public static class Entry<T> {
        public final T data;
        public final DataAddedEvent event;

        public Entry(T data, DataAddedEvent event) {
            this.data = data;
            this.event = event;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Entry<?> entry)) return false;

            if (this.data instanceof Feature.Data<?> d1 && entry.data instanceof Feature.Data<?> d2) {
                return d1.feature == d2.feature && d1.chunkX == d2.chunkX && d1.chunkZ == d2.chunkZ;
            } else if (this.data instanceof BiomeData && entry.data instanceof BiomeData) {
                return this.data.equals(entry.data);
            }

            return false;
        }

        @Override
        public int hashCode() {
            if (this.data instanceof Feature.Data<?>) {
                return ((Feature.Data<?>) this.data).chunkX * 31 + ((Feature.Data<?>) this.data).chunkZ;
            } else if (this.data instanceof BiomeData) {
                return this.data.hashCode();
            }

            return super.hashCode();
        }
    }

}
