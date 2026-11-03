/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.FilenameUtils
 */
package radmin.client.gui.screens;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.io.FilenameUtils;
import radmin.client.MeteorClient;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.WindowScreen;
import radmin.client.gui.widgets.containers.WTable;
import radmin.client.gui.widgets.input.WTextBox;
import radmin.client.gui.widgets.pressable.WButton;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.misc.Notebot;
import radmin.client.utils.Utils;
import radmin.client.utils.notebot.decoder.SongDecoders;

public class NotebotSongsScreen
extends WindowScreen {
    private static final Notebot notebot = Modules.get().get(Notebot.class);
    private WTextBox filter;
    private String filterText = "";
    private WTable table;

    public NotebotSongsScreen(GuiTheme theme) {
        super(theme, "Notebot Songs");
    }

    @Override
    public void initWidgets() {
        WButton randomSong = this.add(this.theme.button("Random Song")).minWidth(400.0).expandX().widget();
        randomSong.action = notebot::playRandomSong;
        this.filter = this.add(this.theme.textBox("", "Search for the songs...")).minWidth(400.0).expandX().widget();
        this.filter.setFocused(true);
        this.filter.action = () -> {
            this.filterText = this.filter.get().trim();
            this.table.clear();
            this.initSongsTable();
        };
        this.table = this.add(this.theme.table()).widget();
        this.initSongsTable();
    }

    private void initSongsTable() {
        AtomicBoolean noSongsFound = new AtomicBoolean(true);
        try {
            Files.list(MeteorClient.FOLDER.toPath().resolve("notebot")).forEach(path -> {
                String name;
                if (SongDecoders.hasDecoder(path) && Utils.searchTextDefault(name = path.getFileName().toString(), this.filterText, false)) {
                    this.addPath((Path)path);
                    noSongsFound.set(false);
                }
            });
        }
        catch (IOException iOException) {
            this.table.add(this.theme.label("Missing radmin-client/notebot folder.")).expandCellX();
            this.table.row();
        }
        if (noSongsFound.get()) {
            this.table.add(this.theme.label("No songs found.")).expandCellX().center();
        }
    }

    private void addPath(Path path) {
        this.table.add(this.theme.horizontalSeparator()).expandX().minWidth(400.0);
        this.table.row();
        this.table.add(this.theme.label(FilenameUtils.getBaseName((String)path.getFileName().toString()))).expandCellX();
        WButton load = this.table.add(this.theme.button("Load")).right().widget();
        load.action = () -> notebot.loadSong(path.toFile());
        WButton preview = this.table.add(this.theme.button("Preview")).right().widget();
        preview.action = () -> notebot.previewSong(path.toFile());
        this.table.row();
    }
}

