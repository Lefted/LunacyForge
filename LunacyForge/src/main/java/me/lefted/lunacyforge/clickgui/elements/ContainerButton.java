package me.lefted.lunacyforge.clickgui.elements;

import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.clickgui.elements.api.Callback;
import me.lefted.lunacyforge.clickgui.elements.api.Element;
import me.lefted.lunacyforge.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ContainerButton extends Element {

    // CONSTANTS
    public static final ResourceLocation PRESS_SOUND = new ResourceLocation("gui.button.press");

    // ATTRIBUTES
    private int width;
    private int height;
    private String displayString;
    private boolean enabled;
    private Callback callback;

    // CONSTRUCTOR
    public ContainerButton(int width, int height, String buttonText) {
	posX = 0;
	posY = 0;
	this.width = 200;
	this.height = 20;
	this.enabled = true;
	this.setVisible(true);
	this.width = width;
	this.height = height;
	this.displayString = buttonText;
    }

    // METHODS
    public void playPressSound(SoundHandler soundHandler) {
	soundHandler.playSound(PositionedSoundRecord.create(PRESS_SOUND, 1.0F));
    }

    public int getWidth() {
	return this.width;
    }

    public int getHeight() {
	return this.height;
    }

    public void setWidth(int width) {
	this.width = width;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	final Minecraft mc = Minecraft.getMinecraft();

	if (this.isVisible()) {
	    final boolean hovered = isMouseOver(mouseX, mouseY);

	    // background texture
	    if (hovered) {
		GlStateManager.color(0.9F, 0.9F, 0.9F, 1F);
	    } else {
		GlStateManager.color(0.95F, 0.95F, 0.95F, 1F);
	    }
	    DrawUtils.INSTANCE.drawLightContainer(posX, posY, width, height);

	    // text
	    int textColor = 14737632;
	    if (!this.enabled) {
		textColor = 10526880;
	    } else if (hovered) {
		textColor = 16777120;
	    }
	    this.drawCenteredString(mc.fontRendererObj, this.displayString, this.getPosX() + this.width / 2, this.getPosY() + (this.height - 8) / 2, textColor);
	    // reset color mask
	    GL11.glColor4f(1F, 1F, 1F, 1F);
	    
	    
	    DrawUtils.INSTANCE.drawRect(posX, posY, posX + width, posY + height, 0xffffffff);
	    DrawUtils.INSTANCE.drawGradientRect4(posX, posY, posX + width, posY + height, 0xffFF0000, 0xffFFFFFF, 0xff000000, 0xff000000);
	    
	}
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
	return mouseX >= this.getPosX() && mouseY >= this.getPosY() && mouseX < this.getPosX() + this.width && mouseY < this.getPosY() + this.height;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	if (this.enabled && this.isVisible() && isMouseOver(mouseX, mouseY)) {
	    this.playPressSound(Minecraft.getMinecraft().getSoundHandler());
	    if (this.callback != null) {
		this.callback.invoke();
	    }
	}
    }

    public Callback getCallback() {
	return callback;
    }

    public void setCallback(Callback callback) {
	this.callback = callback;
    }

    public String getDisplayString() {
	return displayString;
    }

    public void setDisplayString(String displayString) {
	this.displayString = displayString;
    }

    public boolean isEnabled() {
	return enabled;
    }

    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }
}
