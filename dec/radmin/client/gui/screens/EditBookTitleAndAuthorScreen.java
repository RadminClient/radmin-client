/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screens.inventory.BookViewScreen$BookAccess
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ServerboundEditBookPacket
 *  net.minecraft.server.network.Filterable
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.WrittenBookContent
 */
package radmin.client.gui.screens;

import java.util.ArrayList;
import java.util.Optional;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WrittenBookContent;
import radmin.client.MeteorClient;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.WindowScreen;
import radmin.client.gui.widgets.containers.WTable;
import radmin.client.gui.widgets.input.WTextBox;

public class EditBookTitleAndAuthorScreen
extends WindowScreen {
    private final ItemStack itemStack;
    private final InteractionHand hand;

    public EditBookTitleAndAuthorScreen(GuiTheme theme, ItemStack itemStack, InteractionHand hand) {
        super(theme, "Edit title & author");
        this.itemStack = itemStack;
        this.hand = hand;
    }

    @Override
    public void initWidgets() {
        WTable t = this.add(this.theme.table()).expandX().widget();
        t.add(this.theme.label("Title"));
        WTextBox title = t.add(this.theme.textBox((String)((WrittenBookContent)this.itemStack.get(DataComponents.WRITTEN_BOOK_CONTENT)).title().get(MeteorClient.mc.isTextFilteringEnabled()))).minWidth(220.0).expandX().widget();
        t.row();
        t.add(this.theme.label("Author"));
        WTextBox author = t.add(this.theme.textBox(((WrittenBookContent)this.itemStack.get(DataComponents.WRITTEN_BOOK_CONTENT)).author())).minWidth(220.0).expandX().widget();
        t.row();
        t.add(this.theme.button((String)"Done")).expandX().widget().action = () -> {
            WrittenBookContent component = (WrittenBookContent)this.itemStack.get(DataComponents.WRITTEN_BOOK_CONTENT);
            WrittenBookContent newComponent = new WrittenBookContent(Filterable.passThrough((Object)title.get()), author.get(), component.generation(), component.pages(), component.resolved());
            this.itemStack.set(DataComponents.WRITTEN_BOOK_CONTENT, (Object)newComponent);
            BookViewScreen.BookAccess contents = new BookViewScreen.BookAccess(((WrittenBookContent)this.itemStack.get(DataComponents.WRITTEN_BOOK_CONTENT)).getPages(MeteorClient.mc.isTextFilteringEnabled()));
            ArrayList<String> pages = new ArrayList<String>(contents.getPageCount());
            for (int i = 0; i < contents.getPageCount(); ++i) {
                pages.add(contents.getPage(i).getString());
            }
            MeteorClient.mc.getConnection().send((Packet)new ServerboundEditBookPacket(this.hand == InteractionHand.MAIN_HAND ? MeteorClient.mc.player.getInventory().getSelectedSlot() : 40, pages, Optional.of(title.get())));
            this.onClose();
        };
    }
}

