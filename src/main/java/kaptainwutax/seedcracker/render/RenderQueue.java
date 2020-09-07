package kaptainwutax.seedcracker.render;

import net.minecraft.client.util.math.MatrixStack;

import java.util.*;
import java.util.function.Consumer;

public class RenderQueue {

    private final static RenderQueue INSTANCE = new RenderQueue();

    private Map<String, List<Consumer<MatrixStack>>> typeRunnableMap = new HashMap<>();
    private MatrixStack matrixStack = null;

    public static RenderQueue get() {
        return INSTANCE;
    }

    public void add(String type, Consumer<MatrixStack> runnable) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(runnable);

        if(!this.typeRunnableMap.containsKey(type)) {
            this.typeRunnableMap.put(type, new ArrayList<>());
        }

        List<Consumer<MatrixStack>> runnableList = this.typeRunnableMap.get(type);
        runnableList.add(runnable);
    }

    public void remove(String type, Consumer<MatrixStack> runnable) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(runnable);

        if(!this.typeRunnableMap.containsKey(type)) {
            return;
        }

        List<Consumer<MatrixStack>> runnableList = this.typeRunnableMap.get(type);
        runnableList.remove(runnable);
    }

    public void setTrackRender(MatrixStack matrixStack) {
        this.matrixStack = matrixStack;
    }

    public void onRender(String type) {
        if(this.matrixStack == null || !this.typeRunnableMap.containsKey(type))return;
        this.typeRunnableMap.get(type).forEach(r -> r.accept(this.matrixStack));
    }

}
