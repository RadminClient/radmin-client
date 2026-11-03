/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.world.level.block.NoteBlock
 *  net.minecraft.world.level.block.state.properties.NoteBlockInstrument
 *  net.minecraft.world.level.block.state.properties.Property
 */
package radmin.client.utils.notebot.instrumentdetect;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.Property;
import radmin.client.utils.notebot.instrumentdetect.InstrumentDetectFunction;

public enum InstrumentDetectMode {
    BlockState((noteBlock, blockPos) -> (NoteBlockInstrument)noteBlock.getValue((Property)NoteBlock.INSTRUMENT)),
    BelowBlock((blockState, blockPos) -> Minecraft.getInstance().level.getBlockState(blockPos.below()).instrument());

    private final InstrumentDetectFunction instrumentDetectFunction;

    private InstrumentDetectMode(InstrumentDetectFunction instrumentDetectFunction) {
        this.instrumentDetectFunction = instrumentDetectFunction;
    }

    public InstrumentDetectFunction getInstrumentDetectFunction() {
        return this.instrumentDetectFunction;
    }
}

