package me.lefted.lunacyforge.guiscreen.interpreter;

import me.lefted.lunacyforge.config.ClientConfig;
import me.lefted.lunacyforge.guiapi.Button;
import me.lefted.lunacyforge.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ClientSettingsButton extends Button {

    // CONSTANTS
    private static final int WIDTH = 18;
    private static final int HEIGHT = 18;
    private static final ResourceLocation SETTINGS_BG = new ResourceLocation("lunacyforge", "btn_settings_bg.png");
    private static final ResourceLocation SETTINGS_GEAR = new ResourceLocation("lunacyforge", "btn_settings_gear.png");

    // ATTRIBUTES

    // CONSTRUCTOR
    public ClientSettingsButton() {
	super(0, 0, WIDTH, HEIGHT, "");
	final ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
	setPosX(2);
	setPosY(2);
	setVisible(true);
    }

    // METHODS
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	final Minecraft mc = Minecraft.getMinecraft();
	if (this.isVisible()) {
	    final boolean hovered = isMouseOver(mouseX, mouseY);
	    final DrawUtils utils = DrawUtils.INSTANCE;

	    // background
	    mc.getTextureManager().bindTexture(SETTINGS_BG);
	    utils.drawTexturedRectangle(this.getPosX(), this.getPosY(), 0, 0, WIDTH, HEIGHT);
	    
	    if (hovered) {
		utils.guiColor();
	    }
	    // gear
	    mc.getTextureManager().bindTexture(SETTINGS_GEAR);
	    utils.drawTexturedRectangle(this.getPosX(), this.getPosY(), 0, 0, WIDTH, HEIGHT);
	}
    }
}
