package me.lefted.lunacyforge.guiscreen.interpreter;

import me.lefted.lunacyforge.guiapi.Element;
import me.lefted.lunacyforge.modules.Module;
import me.lefted.lunacyforge.modules.Reach;
import me.lefted.lunacyforge.utils.DrawUtils;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class ModuleContainer extends Element {

    // CONSTANTS
    public static final int WIDTH = 350;
    public static final int HEIGHT = 30;
    private static final ResourceLocation SEARCH_CONTAINER = new ResourceLocation("lunacyforge", "search_container.png");

    // ATTRIBUTES
    private Module module;
    private int visibleYTop;
    private int visibleYBottom;

    // CONSTRUCTOR
    public ModuleContainer(Module module) {
	this.module = module;
	init();
    }

    // METHODS
    private void init() {
	final ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
	setPosX(sc.getScaledWidth() / 2 - WIDTH / 2);
    }
    
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	if (isVisible()) {
	    final DrawUtils utils = DrawUtils.INSTANCE;
	    final ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
	    final int width = sc.getScaledWidth();
	    final int height = sc.getScaledHeight();

	    utils.bindTexture(SEARCH_CONTAINER);
	    utils.drawTexturedRectangle(posX, posY, 0, 0, WIDTH, HEIGHT);
	    drawString(Minecraft.getMinecraft().fontRendererObj, module.getName(), posX + 20, posY + 13, 0xffffff);
	}
    }

    // opens the module settings
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	if (isVisible() && isMouseOver(mouseX, mouseY)) {
	    // DEBUG
	    Logger.logChatMessage("Module " + module.getName() + " has been clicked");
	}
    }

    public void updateVisibleCoords(int scissorBoxTop, int scissorBoxBottom) {
	visibleYTop = (posY >= scissorBoxTop) ? posY : scissorBoxTop;
	visibleYBottom = (posY + HEIGHT <= scissorBoxBottom) ? posY + HEIGHT : scissorBoxBottom;
    }
    
    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
	boolean flag1 = mouseX <= posX + WIDTH && mouseX >= posX;
	boolean flag2 = mouseY <= visibleYBottom && mouseY >= visibleYTop;
	return flag1 && flag2;
    }
}
