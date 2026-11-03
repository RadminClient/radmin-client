/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.MatchException
 *  org.meteordev.starscript.utils.SemanticToken
 *  org.meteordev.starscript.utils.SemanticTokenProvider
 *  org.meteordev.starscript.utils.SemanticTokenType
 */
package radmin.client.gui.utils;

import java.util.ArrayList;
import java.util.List;
import org.meteordev.starscript.utils.SemanticToken;
import org.meteordev.starscript.utils.SemanticTokenProvider;
import org.meteordev.starscript.utils.SemanticTokenType;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.widgets.input.WTextBox;
import radmin.client.systems.hud.elements.TextHud;
import radmin.client.utils.misc.MeteorStarscript;
import radmin.client.utils.render.color.Color;

public class StarscriptTextBoxRenderer
implements WTextBox.Renderer {
    private static final Color RED = new Color(225, 25, 25);
    private final List<SemanticToken> tokens = new ArrayList<SemanticToken>();
    private final List<Section> sections = new ArrayList<Section>();
    private String lastText;

    @Override
    public void render(GuiRenderer renderer, double x, double y, String text, Color color) {
        if (this.lastText == null || !this.lastText.equals(text)) {
            this.lastText = text;
            SemanticTokenProvider.get((String)text, this.tokens);
            this.convertTokensToSections(renderer.theme);
        }
        for (Section section : this.sections) {
            renderer.text(section.text, x, y, section.color, false);
            x += renderer.theme.textWidth(section.text);
        }
    }

    @Override
    public List<String> getCompletions(String text, int position) {
        ArrayList<String> completions = new ArrayList<String>();
        MeteorStarscript.ss.getCompletions(text, position, (completion, function) -> completions.add((String)(function ? completion + "(" : completion)));
        completions.sort(String::compareToIgnoreCase);
        return completions;
    }

    private void convertTokensToSections(GuiTheme theme) {
        this.sections.clear();
        int start = 0;
        for (SemanticToken token : this.tokens) {
            if (start != token.start) {
                this.sections.add(new Section(this.lastText.substring(start, token.start), theme.starscriptTextColor()));
            }
            String text = this.lastText.substring(token.start, token.end);
            this.sections.add(new Section(text, StarscriptTextBoxRenderer.getColorForToken(theme, token.type, text)));
            start = token.end;
        }
        if (start < this.lastText.length()) {
            this.sections.add(new Section(this.lastText.substring(start), theme.starscriptTextColor()));
        }
    }

    private static Color getColorForToken(GuiTheme theme, SemanticTokenType type, String text) {
        return switch (type) {
            default -> throw new MatchException(null, null);
            case SemanticTokenType.Dot -> {
                Color var3_3;
                yield var3_3 = theme.starscriptDotColor();
            }
            case SemanticTokenType.Comma -> {
                Color var3_4;
                yield var3_4 = theme.starscriptCommaColor();
            }
            case SemanticTokenType.Operator -> {
                Color var3_5;
                yield var3_5 = theme.starscriptOperatorColor();
            }
            case SemanticTokenType.String -> {
                Color var3_6;
                yield var3_6 = theme.starscriptStringColor();
            }
            case SemanticTokenType.Number -> {
                Color var3_7;
                yield var3_7 = theme.starscriptNumberColor();
            }
            case SemanticTokenType.Keyword -> {
                Color var3_8;
                yield var3_8 = theme.starscriptKeywordColor();
            }
            case SemanticTokenType.Paren -> {
                Color var3_9;
                yield var3_9 = theme.starscriptParenthesisColor();
            }
            case SemanticTokenType.Brace -> {
                Color var3_10;
                yield var3_10 = theme.starscriptBraceColor();
            }
            case SemanticTokenType.Identifier -> {
                Color var3_11;
                yield var3_11 = theme.starscriptTextColor();
            }
            case SemanticTokenType.Map -> {
                Color var3_12;
                yield var3_12 = theme.starscriptAccessedObjectColor();
            }
            case SemanticTokenType.Section -> {
                if (text.startsWith("#")) {
                    text = text.substring(1);
                }
                try {
                    Color var3_13;
                    yield var3_13 = TextHud.getSectionColor(Integer.parseInt(text));
                }
                catch (NumberFormatException var4_16) {
                    Color var3_14;
                    yield var3_14 = theme.starscriptTextColor();
                }
            }
            case SemanticTokenType.Error -> {
                Color var3_15;
                yield var3_15 = RED;
            }
        };
    }

    private record Section(String text, Color color) {
    }
}

