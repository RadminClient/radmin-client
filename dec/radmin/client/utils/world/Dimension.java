/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.utils.world;

public enum Dimension {
    Overworld,
    Nether,
    End;


    public Dimension opposite() {
        return switch (this.ordinal()) {
            case 0 -> Nether;
            case 1 -> Overworld;
            default -> this;
        };
    }

    public String toString() {
        return switch (this.ordinal()) {
            case 1 -> "minecraft:the_nether";
            case 2 -> "minecraft:the_end";
            default -> "minecraft:overworld";
        };
    }
}

