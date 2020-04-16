package me.lefted.lunacyforge.guiscreen.interpreter;

import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.guiapi.Button;
import me.lefted.lunacyforge.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class BackButton extends Button {

    // CONSTANTS
    private static final int WIDTH = 18;
    private static final int HEIGHT = 18;
    private static final ResourceLocation SETTINGS_BG = new ResourceLocation("lunacyforge", "btn_settings_bg.png");
    private static final ResourceLocation SETTINGS_BACK = new ResourceLocation("lunacyforge", "btn_settings_back.png");

    // ATTRIBUTES

    // CONSTRUCTOR
    public BackButton() {
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
	    mc.getTextureManager().bindTexture(SETTINGS_BACK);
	    utils.drawTexturedRectangle(this.getPosX(), this.getPosY(), 0, 0, WIDTH, HEIGHT);
	}
    }
}
