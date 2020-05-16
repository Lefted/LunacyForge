package me.lefted.lunacyforge.clickgui.elements;

import me.lefted.lunacyforge.clickgui.elements.api.Element;
import me.lefted.lunacyforge.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiSecurity extends Element {

    // CONSTANTS
    private static final int WIDTH = 48;
    private static final int HEIGHT = 64;
    private static final ResourceLocation MEME_NIGGA = new ResourceLocation("lunacyforge", "meme_nigga.png");

    // ATTRIBUTES
    private ScaledResolution sc;
    private static boolean tripping = false;

    // CONSTRUCTOR
    public GuiSecurity() {
	sc = new ScaledResolution(Minecraft.getMinecraft());
	posX = sc.getScaledWidth() - 46;
	posY = sc.getScaledHeight() - 64;
    }

    // METHODS
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	final DrawUtils utils = DrawUtils.INSTANCE;

	utils.bindTexture(MEME_NIGGA);
	utils.drawTexturedRectangle(posX, posY, 0, 0, WIDTH, HEIGHT);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	if (isVisible() && isMouseOver(mouseX, mouseY)) {
	    trip();
	}
    }

    private void trip() {
	tripping = !tripping;

	final Minecraft mc = Minecraft.getMinecraft();
	// enable shader
	if (tripping) {
	    if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
		if (mc.entityRenderer.getShaderGroup() != null) {
		    mc.entityRenderer.getShaderGroup().deleteShaderGroup();
		}
		mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/wobble.json"));
	    }
	} else {
	    // disable shader
	    if (mc.entityRenderer.getShaderGroup() != null) {
		mc.entityRenderer.getShaderGroup().deleteShaderGroup();
		mc.entityRenderer.stopUseShader();// = null;
	    }
	}
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
	boolean flag1 = mouseX >= posX && mouseX <= posX + WIDTH;
	boolean flag2 = mouseY >= posY && mouseY <= posY + HEIGHT;
	return flag1 && flag2;
    }

    public static boolean isTripping() {
        return tripping;
    }

    public static void setTripping(boolean tripping) {
        GuiSecurity.tripping = tripping;
    }
}
