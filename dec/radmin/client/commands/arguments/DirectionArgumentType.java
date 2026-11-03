/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.commands.arguments.StringRepresentableArgument
 *  net.minecraft.core.Direction
 */
package radmin.client.commands.arguments;

import com.mojang.serialization.Codec;
import net.minecraft.commands.arguments.StringRepresentableArgument;
import net.minecraft.core.Direction;

public class DirectionArgumentType
extends StringRepresentableArgument<Direction> {
    private static final DirectionArgumentType INSTANCE = new DirectionArgumentType();

    private DirectionArgumentType() {
        super((Codec)Direction.CODEC, Direction::values);
    }

    public static DirectionArgumentType create() {
        return INSTANCE;
    }
}

