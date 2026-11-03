/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screens.Screen
 */
package radmin.client.utils.render.prompts;

import net.minecraft.client.gui.screens.Screen;
import radmin.client.MeteorClient;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.GuiThemes;
import radmin.client.gui.widgets.pressable.WButton;
import radmin.client.utils.render.prompts.Prompt;

public class OkPrompt
extends Prompt<OkPrompt> {
    private Runnable onOk = () -> {};

    private OkPrompt(GuiTheme theme, Screen parent) {
        super(theme, parent);
    }

    public static OkPrompt create() {
        return new OkPrompt(GuiThemes.get(), MeteorClient.mc.screen);
    }

    public static OkPrompt create(GuiTheme theme, Screen parent) {
        return new OkPrompt(theme, parent);
    }

    public OkPrompt onOk(Runnable action) {
        this.onOk = action;
        return this;
    }

    @Override
    protected void initialiseWidgets(Prompt.PromptScreen screen) {
        WButton okButton = screen.list.add(this.theme.button("Ok")).expandX().widget();
        okButton.action = () -> {
            this.dontShowAgain(screen);
            this.onOk.run();
            screen.onClose();
        };
    }
}

