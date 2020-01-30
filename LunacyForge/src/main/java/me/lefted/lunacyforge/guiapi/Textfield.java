package me.lefted.lunacyforge.guiapi;

import java.util.function.Consumer;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;

public class Textfield extends Element {

    // ATTRIBUTES
    private static FontRenderer fontRendererInstance = Minecraft.getMinecraft().fontRendererObj;
    private final int width;
    private final int height;
    private int maxStringLength = 32;
    private int cursorCounter;
    private String text = "";
    private boolean enableBackgroundDrawing = true;
    private Consumer<String> consumer;

    /**
     * if true the textbox can lose focus by clicking elsewhere on the screen
     */
    private boolean canLoseFocus = true;

    /**
     * If this value is true along with isEnabled, keyTyped will process the keys.
     */
    private boolean isFocused;

    /**
     * If this value is true along with isFocused, keyTyped will process the keys.
     */
    private boolean isEnabled = true;

    /**
     * The current character index that should be used as start of the rendered text.
     */
    private int lineScrollOffset;
    private int cursorPosition;

    /** other selection position, maybe the same as the cursor */
    private int selectionEnd;
    private int enabledColor = 14737632;
    private int disabledColor = 7368816;
    private Predicate<String> validator = Predicates.<String>alwaysTrue();

    // CONSTRUCOTOR
    public Textfield(int x, int y, int width, int height) {
	this.setPosX(x);
	this.setPosY(y);
	this.width = width;
	this.height = height;
	this.setVisible(true);
    }

    // METHODS
    /**
     * Increments the cursor counter
     */
    public void updateCursorCounter() {
	++this.cursorCounter;
    }

    /**
     * Sets the text of the textbox
     */
    public void setText(String text) {
	if (this.validator.apply(text)) {
	    if (text.length() > this.maxStringLength) {
		this.text = text.substring(0, this.maxStringLength);
		if (this.consumer != null) {
		    this.consumer.accept(this.text);
		}
	    } else {
		this.text = text;
		if (this.consumer != null) {
		    this.consumer.accept(this.text);
		}
	    }
	    this.setCursorPositionEnd();
	}
    }

    /**
     * Returns the contents of the textbox
     */
    public String getText() {
	return this.text;
    }

    /**
     * returns the text between the cursor and selectionEnd
     */
    public String getSelectedText() {
	int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
	int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
	return this.text.substring(i, j);
    }

    public void setValidator(Predicate<String> theValidator) {
	this.validator = theValidator;
    }

    /**
     * replaces selected text, or inserts text at the position on the cursor
     */
    public void writeText(String text) {
	String s = "";
	String s1 = ChatAllowedCharacters.filterAllowedCharacters(text);
	int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
	int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
	int k = this.maxStringLength - this.text.length() - (i - j);
	int l = 0;

	if (this.text.length() > 0) {
	    s = s + this.text.substring(0, i);
	}

	if (k < s1.length()) {
	    s = s + s1.substring(0, k);
	    l = k;
	} else {
	    s = s + s1;
	    l = s1.length();
	}

	if (this.text.length() > 0 && j < this.text.length()) {
	    s = s + this.text.substring(j);
	}

	if (this.validator.apply(s)) {
	    this.text = s;
	    if (this.consumer != null) {
		this.consumer.accept(this.text);
	    }
	    this.moveCursorBy(i - this.selectionEnd + l);
	}
    }

    /**
     * Deletes the specified number of words starting at the cursor position. Negative numbers will delete words left of the cursor.
     */
    public void deleteWords(int amount) {
	if (this.text.length() != 0) {
	    if (this.selectionEnd != this.cursorPosition) {
		this.writeText("");
	    } else {
		this.deleteFromCursor(this.getNthWordFromCursor(amount) - this.cursorPosition);
	    }
	}
    }

    /**
     * delete the selected text, otherwsie deletes characters from either side of the cursor. params: delete num
     */
    public void deleteFromCursor(int deleteNum) {
	if (this.text.length() != 0) {
	    if (this.selectionEnd != this.cursorPosition) {
		this.writeText("");
	    } else {
		boolean flag = deleteNum < 0;
		int i = flag ? this.cursorPosition + deleteNum : this.cursorPosition;
		int j = flag ? this.cursorPosition : this.cursorPosition + deleteNum;
		String s = "";

		if (i >= 0) {
		    s = this.text.substring(0, i);
		}

		if (j < this.text.length()) {
		    s = s + this.text.substring(j);
		}

		if (this.validator.apply(s)) {
		    this.text = s;
		    if (this.consumer != null) {
			this.consumer.accept(this.text);
		    }
		    if (flag) {
			this.moveCursorBy(deleteNum);
		    }
		}
	    }
	}
    }

    /**
     * see @getNthNextWordFromPos() params: N, position
     */
    public int getNthWordFromCursor(int n) {
	return this.getNthWordFromPos(n, this.getCursorPosition());
    }

    /**
     * gets the position of the nth word. N may be negative, then it looks backwards. params: N, position
     */
    public int getNthWordFromPos(int n, int position) {
	return this.func_146197_a(n, position, true);
    }

    public int func_146197_a(int p_146197_1_, int p_146197_2_, boolean p_146197_3_) {
	int i = p_146197_2_;
	boolean flag = p_146197_1_ < 0;
	int j = Math.abs(p_146197_1_);

	for (int k = 0; k < j; ++k) {
	    if (!flag) {
		int l = this.text.length();
		i = this.text.indexOf(32, i);

		if (i == -1) {
		    i = l;
		} else {
		    while (p_146197_3_ && i < l && this.text.charAt(i) == 32) {
			++i;
		    }
		}
	    } else {
		while (p_146197_3_ && i > 0 && this.text.charAt(i - 1) == 32) {
		    --i;
		}
		while (i > 0 && this.text.charAt(i - 1) != 32) {
		    --i;
		}
	    }
	}
	return i;
    }

    /**
     * Moves the text cursor by a specified number of characters and clears the selection
     */
    public void moveCursorBy(int amount) {
	this.setCursorPosition(this.selectionEnd + amount);
    }

    /**
     * sets the position of the cursor to the provided index
     */
    public void setCursorPosition(int curosrPosition) {
	this.cursorPosition = curosrPosition;
	int i = this.text.length();
	this.cursorPosition = MathHelper.clamp_int(this.cursorPosition, 0, i);
	this.setSelectionPos(this.cursorPosition);
    }

    /**
     * sets the cursors position to the beginning
     */
    public void setCursorPositionZero() {
	this.setCursorPosition(0);
    }

    /**
     * sets the cursors position to after the text
     */
    public void setCursorPositionEnd() {
	this.setCursorPosition(this.text.length());
    }

    /**
     * Call this method from your GuiScreen to process the keys into the textbox
     */
    public boolean textboxKeyTyped(char typedChar, int keyCode) {
	if (!this.isFocused) {
	    return false;
	} else if (GuiScreen.isKeyComboCtrlA(keyCode)) {
	    this.setCursorPositionEnd();
	    this.setSelectionPos(0);
	    return true;
	} else if (GuiScreen.isKeyComboCtrlC(keyCode)) {
	    GuiScreen.setClipboardString(this.getSelectedText());
	    return true;
	} else if (GuiScreen.isKeyComboCtrlV(keyCode)) {
	    if (this.isEnabled) {
		this.writeText(GuiScreen.getClipboardString());
	    }

	    return true;
	} else if (GuiScreen.isKeyComboCtrlX(keyCode)) {
	    GuiScreen.setClipboardString(this.getSelectedText());

	    if (this.isEnabled) {
		this.writeText("");
	    }

	    return true;
	} else {
	    switch (keyCode) {
	    case 14:
		if (GuiScreen.isCtrlKeyDown()) {
		    if (this.isEnabled) {
			this.deleteWords(-1);
		    }
		} else if (this.isEnabled) {
		    this.deleteFromCursor(-1);
		}

		return true;

	    case 199:
		if (GuiScreen.isShiftKeyDown()) {
		    this.setSelectionPos(0);
		} else {
		    this.setCursorPositionZero();
		}

		return true;

	    case 203:
		if (GuiScreen.isShiftKeyDown()) {
		    if (GuiScreen.isCtrlKeyDown()) {
			this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
		    } else {
			this.setSelectionPos(this.getSelectionEnd() - 1);
		    }
		} else if (GuiScreen.isCtrlKeyDown()) {
		    this.setCursorPosition(this.getNthWordFromCursor(-1));
		} else {
		    this.moveCursorBy(-1);
		}

		return true;

	    case 205:
		if (GuiScreen.isShiftKeyDown()) {
		    if (GuiScreen.isCtrlKeyDown()) {
			this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
		    } else {
			this.setSelectionPos(this.getSelectionEnd() + 1);
		    }
		} else if (GuiScreen.isCtrlKeyDown()) {
		    this.setCursorPosition(this.getNthWordFromCursor(1));
		} else {
		    this.moveCursorBy(1);
		}

		return true;

	    case 207:
		if (GuiScreen.isShiftKeyDown()) {
		    this.setSelectionPos(this.text.length());
		} else {
		    this.setCursorPositionEnd();
		}

		return true;

	    case 211:
		if (GuiScreen.isCtrlKeyDown()) {
		    if (this.isEnabled) {
			this.deleteWords(1);
		    }
		} else if (this.isEnabled) {
		    this.deleteFromCursor(1);
		}

		return true;

	    default:
		if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
		    if (this.isEnabled) {
			this.writeText(Character.toString(typedChar));
		    }

		    return true;
		} else {
		    return false;
		}
	    }
	}
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
	boolean flag = mouseX >= this.getPosX() && mouseX < this.getPosX() + this.width && mouseY >= this.getPosY() && mouseY < this.getPosY() + this.height;

	if (this.canLoseFocus) {
	    this.setFocused(flag);
	}

	if (this.isFocused && flag && mouseButton == 0) {
	    int i = mouseX - this.getPosX();

	    if (this.enableBackgroundDrawing) {
		i -= 4;
	    }

	    String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
	    this.setCursorPosition(this.fontRendererInstance.trimStringToWidth(s, i).length() + this.lineScrollOffset);
	}
	return flag;
    }

    /**
     * Draws the textbox
     */
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	if (this.isVisible()) {
	    if (this.getEnableBackgroundDrawing()) {
		drawRect(this.getPosX() - 1, this.getPosY() - 1, this.getPosX() + this.width + 1, this.getPosY() + this.height + 1, -6250336);
		drawRect(this.getPosX(), this.getPosY(), this.getPosX() + this.width, this.getPosY() + this.height, -16777216);
	    }

	    int i = this.isEnabled ? this.enabledColor : this.disabledColor;
	    int j = this.cursorPosition - this.lineScrollOffset;
	    int k = this.selectionEnd - this.lineScrollOffset;
	    String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
	    boolean flag = j >= 0 && j <= s.length();
	    boolean flag1 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
	    int l = this.enableBackgroundDrawing ? this.getPosX() + 4 : this.getPosX();
	    int i1 = this.enableBackgroundDrawing ? this.getPosY() + (this.height - 8) / 2 : this.getPosY();
	    int j1 = l;

	    if (k > s.length()) {
		k = s.length();
	    }

	    if (s.length() > 0) {
		String s1 = flag ? s.substring(0, j) : s;
		j1 = this.fontRendererInstance.drawStringWithShadow(s1, (float) l, (float) i1, i);
	    }

	    boolean flag2 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
	    int k1 = j1;

	    if (!flag) {
		k1 = j > 0 ? l + this.width : l;
	    } else if (flag2) {
		k1 = j1 - 1;
		--j1;
	    }

	    if (s.length() > 0 && flag && j < s.length()) {
		j1 = this.fontRendererInstance.drawStringWithShadow(s.substring(j), (float) j1, (float) i1, i);
	    }

	    if (flag1) {
		if (flag2) {
		    Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + this.fontRendererInstance.FONT_HEIGHT, -3092272);
		} else {
		    this.fontRendererInstance.drawStringWithShadow("_", (float) k1, (float) i1, i);
		}
	    }

	    if (k != j) {
		int l1 = l + this.fontRendererInstance.getStringWidth(s.substring(0, k));
		this.drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + this.fontRendererInstance.FONT_HEIGHT);
	    }
	}
    }

    /**
     * draws the vertical line cursor in the textbox
     */
    private void drawCursorVertical(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_) {
	if (p_146188_1_ < p_146188_3_) {
	    int i = p_146188_1_;
	    p_146188_1_ = p_146188_3_;
	    p_146188_3_ = i;
	}

	if (p_146188_2_ < p_146188_4_) {
	    int j = p_146188_2_;
	    p_146188_2_ = p_146188_4_;
	    p_146188_4_ = j;
	}

	if (p_146188_3_ > this.getPosX() + this.width) {
	    p_146188_3_ = this.getPosX() + this.width;
	}

	if (p_146188_1_ > this.getPosX() + this.width) {
	    p_146188_1_ = this.getPosX() + this.width;
	}

	Tessellator tessellator = Tessellator.getInstance();
	WorldRenderer worldrenderer = tessellator.getWorldRenderer();
	GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
	GlStateManager.disableTexture2D();
	GlStateManager.enableColorLogic();
	GlStateManager.colorLogicOp(5387);
	worldrenderer.begin(7, DefaultVertexFormats.POSITION);
	worldrenderer.pos((double) p_146188_1_, (double) p_146188_4_, 0.0D).endVertex();
	worldrenderer.pos((double) p_146188_3_, (double) p_146188_4_, 0.0D).endVertex();
	worldrenderer.pos((double) p_146188_3_, (double) p_146188_2_, 0.0D).endVertex();
	worldrenderer.pos((double) p_146188_1_, (double) p_146188_2_, 0.0D).endVertex();
	tessellator.draw();
	GlStateManager.disableColorLogic();
	GlStateManager.enableTexture2D();
    }

    public void setMaxStringLength(int maxStringLength) {
	this.maxStringLength = maxStringLength;

	if (this.text.length() > maxStringLength) {
	    this.text = this.text.substring(0, maxStringLength);
	    if (this.consumer != null) {
		this.consumer.accept(this.text);
	    }
	}
    }

    /**
     * returns the maximum number of character that can be contained in this textbox
     */
    public int getMaxStringLength() {
	return this.maxStringLength;
    }

    /**
     * returns the current position of the cursor
     */
    public int getCursorPosition() {
	return this.cursorPosition;
    }

    /**
     * get enable drawing background and outline
     */
    public boolean getEnableBackgroundDrawing() {
	return this.enableBackgroundDrawing;
    }

    /**
     * enable drawing background and outline
     */
    public void setEnableBackgroundDrawing(boolean drawBackground) {
	this.enableBackgroundDrawing = drawBackground;
    }

    /**
     * Sets the text colour for this textbox (disabled text will not use this colour)
     */
    public void setTextColor(int textColor) {
	this.enabledColor = textColor;
    }

    public void setDisabledTextColour(int disabledTextColour) {
	this.disabledColor = disabledTextColour;
    }

    /**
     * Sets focus to this gui element
     */
    public void setFocused(boolean focused) {
	if (focused && !this.isFocused) {
	    this.cursorCounter = 0;
	}
	this.isFocused = focused;
    }

    /**
     * Getter for the focused field
     */
    public boolean isFocused() {
	return this.isFocused;
    }

    public void setEnabled(boolean enabled) {
	this.isEnabled = enabled;
    }

    /**
     * the side of the selection that is not the cursor, may be the same as the cursor
     */
    public int getSelectionEnd() {
	return this.selectionEnd;
    }

    /**
     * returns the width of the textbox depending on if background drawing is enabled
     */
    public int getWidth() {
	return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
    }

    /**
     * Sets the position of the selection anchor (i.e. position the selection was started at)
     */
    public void setSelectionPos(int selectionPos) {
	int i = this.text.length();

	if (selectionPos > i) {
	    selectionPos = i;
	}

	if (selectionPos < 0) {
	    selectionPos = 0;
	}

	this.selectionEnd = selectionPos;

	if (this.fontRendererInstance != null) {
	    if (this.lineScrollOffset > i) {
		this.lineScrollOffset = i;
	    }

	    int j = this.getWidth();
	    String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), j);
	    int k = s.length() + this.lineScrollOffset;

	    if (selectionPos == this.lineScrollOffset) {
		this.lineScrollOffset -= this.fontRendererInstance.trimStringToWidth(this.text, j, true).length();
	    }

	    if (selectionPos > k) {
		this.lineScrollOffset += selectionPos - k;
	    } else if (selectionPos <= this.lineScrollOffset) {
		this.lineScrollOffset -= this.lineScrollOffset - selectionPos;
	    }

	    this.lineScrollOffset = MathHelper.clamp_int(this.lineScrollOffset, 0, i);
	}
    }

    /**
     * if true the textbox can lose focus by clicking elsewhere on the screen
     */
    public void setCanLoseFocus(boolean canLoseFocus) {
	this.canLoseFocus = canLoseFocus;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
	this.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen() {
	this.updateCursorCounter();
    }

    public Consumer<String> getConsumer() {
	return consumer;
    }

    public void setConsumer(Consumer<String> consumer) {
	this.consumer = consumer;
    }
}
