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

public class YesNoPrompt
extends Prompt<YesNoPrompt> {
    private Runnable onYes = () -> {};
    private Runnable onNo = () -> {};

    private YesNoPrompt(GuiTheme theme, Screen parent) {
        super(theme, parent);
    }

    public static YesNoPrompt create() {
        return new YesNoPrompt(GuiThemes.get(), MeteorClient.mc.screen);
    }

    public static YesNoPrompt create(GuiTheme theme, Screen parent) {
        return new YesNoPrompt(theme, parent);
    }

    public YesNoPrompt onYes(Runnable action) {
        this.onYes = action;
        return this;
    }

    public YesNoPrompt onNo(Runnable action) {
        this.onNo = action;
        return this;
    }

    @Override
    protected void initialiseWidgets(Prompt.PromptScreen screen) {
        WButton yesButton = screen.list.add(this.theme.button("Yes")).expandX().widget();
        yesButton.action = () -> {
            this.dontShowAgain(screen);
            this.onYes.run();
            screen.onClose();
        };
        WButton noButton = screen.list.add(this.theme.button("No")).expandX().widget();
        noButton.action = () -> {
            this.dontShowAgain(screen);
            this.onNo.run();
            screen.onClose();
        };
    }
}

