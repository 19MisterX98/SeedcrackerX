package kaptainwutax.seedcracker.render;

public class Color {

	public static final Color WHITE = new Color(255, 255, 255);

	private final int red;
	private final int green;
	private final int blue;

	public Color(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public int getRed() {
		return this.red;
	}

	public int getGreen() {
		return this.green;
	}

	public int getBlue() {
		return this.blue;
	}

	public float getFRed() {
		return this.getRed() / 255.0F;
	}

	public float getFGreen() {
		return this.getGreen() / 255.0F;
	}

	public float getFBlue() {
		return this.getBlue() / 255.0F;
	}

}
