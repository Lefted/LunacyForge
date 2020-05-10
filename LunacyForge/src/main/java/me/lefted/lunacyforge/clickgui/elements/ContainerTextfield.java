package me.lefted.lunacyforge.clickgui.elements;

import java.util.function.Consumer;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import me.lefted.lunacyforge.guiapi.Textfield;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;

public class ContainerTextfield extends Textfield {

    // ATTRIBUTES
    public static final int DEFAULT_HEIGHT = 20;

    // CONSTRUCTOR
    public ContainerTextfield(int width) {
	super(0, 0, width, DEFAULT_HEIGHT);
    }

    public ContainerTextfield(int width, int height) {
	super(0, 0, width, height);
    }

    public ContainerTextfield(int x, int y, int width, int height) {
	super(x, y, width, height);
    }

    // METHODS
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	if (this.isVisible()) {
	    if (this.getEnableBackgroundDrawing()) {
		
		SettingContainer.drawContainerTexture(posX, posY, width, height);
		
//		drawRect(this.getPosX() - 1, this.getPosY() - 1, this.getPosX() + this.width + 1, this.getPosY() + this.height + 1, -6250336);
//		drawRect(this.getPosX(), this.getPosY(), this.getPosX() + this.width, this.getPosY() + this.height, -16777216);
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

}