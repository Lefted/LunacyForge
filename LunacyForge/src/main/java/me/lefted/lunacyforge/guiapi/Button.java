package me.lefted.lunacyforge.guiapi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Button extends Element {

    // CONSTANTS
    private static final ResourceLocation PRESS_SOUND = new ResourceLocation("gui.button.press");
    
    // ATTRIBUTES
    private ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    private int width;
    private int height;
    private String displayString;
    private boolean enabled;
    private boolean hovered;
    private Callback callback;

    // CONSTRUCTOR
    public Button(int x, int y, int width, int height, String buttonText) {
	this.width = 200;
	this.height = 20;
	this.enabled = true;
	this.setVisible(true);
	this.setPosX(x);
	this.setPosY(y);
	this.width = width;
	this.height = height;
	this.displayString = buttonText;
    }

    // METHODS
    public void playPressSound(SoundHandler soundHandlerIn) {
	soundHandlerIn.playSound(PositionedSoundRecord.create(PRESS_SOUND, 1.0F));
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
	    final boolean hovered = isMouseOver(mouseX, mouseY);


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
