package kaptainwutax.seedcrackerX.render;

public record Color(int red, int green, int blue) {

    public static final Color WHITE = new Color(255, 255, 255);

    public float getFRed() {
        return this.red() / 255.0F;
    }

    public float getFGreen() {
        return this.green() / 255.0F;
    }

    public float getFBlue() {
        return this.blue() / 255.0F;
    }

}
