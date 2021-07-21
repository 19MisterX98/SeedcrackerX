package kaptainwutax.seedcrackerX.util;

public class HeightContext {
    private final int bottomY;
    private final int topY;

    public HeightContext(int minY, int maxY) {
        this.bottomY = minY;
        this.topY = maxY;
    }

    public int getTopY() {
        return topY;
    }

    public int getBottomY() {
        return bottomY;
    }

    public int getHeight() {
        return topY - bottomY;
    }

    public int getDistanceToBottom(int yValue) {
        return yValue - bottomY;
    }

    public int getDistanceToTop(int yValue) {
        return topY - yValue - 1;
    }
}
