package me.lefted.lunacyforge.clickgui.container;

import me.lefted.lunacyforge.clickgui.elements.ContainerCheckbox;
import me.lefted.lunacyforge.guiapi.Checkbox;
import me.lefted.lunacyforge.guiapi.Element;
import me.lefted.lunacyforge.modules.Module;
import me.lefted.lunacyforge.utils.DrawUtils;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ModuleContainer extends Element {

    // CONSTANTS
    public static final int WIDTH = 350;
    public static final int HEIGHT = 30;
    private static final ResourceLocation MODULE_CONTAINER = new ResourceLocation("lunacyforge", "container.png");

    // ATTRIBUTES
    private Module module;
    private int visibleYTop;
    private int visibleYBottom;
    private ContainerCheckbox togglebox;

    // CONSTRUCTOR
    public ModuleContainer(Module module) {
	this.module = module;
	init();
    }

    // METHODS
    private void init() {
	final ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
	setPosX(sc.getScaledWidth() / 2 - WIDTH / 2);
	// togglebox
	togglebox = new ContainerCheckbox(posX, false);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	if (isVisible()) {
	    final DrawUtils utils = DrawUtils.INSTANCE;

	    // bar
	    utils.bindTexture(MODULE_CONTAINER);
	    utils.drawTexturedRectangle(posX, posY, 0, 0, ModuleContainer.WIDTH, ModuleContainer.HEIGHT);

	    // togglebox
	    // DEBUG
	    togglebox.draw(mouseX, mouseY, partialTicks);
	    // name
	    drawString(Minecraft.getMinecraft().fontRendererObj, module.getName(), posX + 20, posY + 12, 0xffffff);
	}
    }

    // opens the module settings
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	if (isVisible() && isMouseOver(mouseX, mouseY)) {
	    // TODO
	    // show module settings

	    // togglebox
	    togglebox.mouseClicked(mouseX, mouseY, mouseButton);
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

    @Override
    public void setPosY(int posY) {
	// own
	super.setPosY(posY);
	// togglebox
	togglebox.setPosY(posY);
    }
}
