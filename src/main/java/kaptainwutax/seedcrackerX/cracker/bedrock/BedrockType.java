package kaptainwutax.seedcrackerX.cracker.bedrock;

public enum BedrockType {
    ROOF("minecraft:bedrock_roof", 123, 128, true),
    FLOOR_NETHER("minecraft:bedrock_floor", 0, 5, false),
    FLOOR_OVERWORLD("minecraft:bedrock_floor", -64, -59, false);

    public final String name;
    public final int startY;
    public final int endY;
    public final boolean invert;

    BedrockType(String name, int startY, int endY, boolean invert) {
        this.name = name;
        this.startY = startY;
        this.endY = endY;
        this.invert = invert;
    }

    public static BedrockType getFromY(int y) {
        for (BedrockType type : values()) {
            if (y >= type.startY && y < type.endY) {
                return type;
            }
        }

        throw new IllegalArgumentException("No bedrock type found for Y value of: " + y);
    }

}
