package me.lefted.lunacyforge.guiapi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Button extends Element {

    // ATTRIBUTES
    private ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    private int width;
    private int height;
    private String displayString;
    private boolean enabled;
    private boolean hovered;
    private Callback callback;

    public Button(int x, int y, int widthIn, int heightIn, String buttonText) {
	this.width = 200;
	this.height = 20;
	this.enabled = true;
	this.setVisible(true);
	this.setPosX(x);
	this.setPosY(y);
	this.width = widthIn;
	this.height = heightIn;
	this.displayString = buttonText;
    }

    public boolean isMouseOver() {
	return this.hovered;
    }

    private void playPressSound(SoundHandler soundHandlerIn) {
	soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getWidth() {
	return this.width;
    }

    public void setWidth(int width) {
	this.width = width;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	final Minecraft mc = Minecraft.getMinecraft();

	if (this.isVisible()) {
	    FontRenderer fontrenderer = mc.fontRendererObj;
	    mc.getTextureManager().bindTexture(buttonTextures);
	    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	    this.hovered = mouseX >= this.getPosX() && mouseY >= this.getPosY() && mouseX < this.getPosX() + this.width && mouseY < this.getPosY()
		+ this.height;
	    // int i = this.getHoverState(this.hovered);
	    int i = 1;

	    if (!this.enabled) {
		i = 0;
	    } else if (this.hovered) {
		i = 2;
	    }

	    GlStateManager.enableBlend();
	    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	    GlStateManager.blendFunc(770, 771);
	    this.drawTexturedModalRect(this.getPosX(), this.getPosY(), 0, 46 + i * 20, this.width / 2, this.height);
	    this.drawTexturedModalRect(this.getPosX() + this.width / 2, this.getPosY(), 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
	    int j = 14737632;

	    if (!this.enabled) {
		j = 10526880;
	    } else if (this.hovered) {
		j = 16777120;
	    }

	    this.drawCenteredString(fontrenderer, this.displayString, this.getPosX() + this.width / 2, this.getPosY() + (this.height - 8) / 2, j);
	}
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	if (this.enabled && this.isVisible() && mouseX >= this.getPosX() && mouseY >= this.getPosY() && mouseX < this.getPosX() + this.width && mouseY < this
	    .getPosY() + this.height) {
	    this.playPressSound(Minecraft.getMinecraft().getSoundHandler());
	    if (this.callback != null) {
		this.callback.invoke();
	    }
	}

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick) {
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
    }

    @Override
    public void updateScreen() {
    }
    
    public Callback getCallback() {
	return callback;
    }

    public void setCallback(Callback callback) {
	this.callback = callback;
    }

    public ResourceLocation getButtonTextures() {
        return buttonTextures;
    }

    public void setButtonTextures(ResourceLocation buttonTextures) {
        this.buttonTextures = buttonTextures;
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
