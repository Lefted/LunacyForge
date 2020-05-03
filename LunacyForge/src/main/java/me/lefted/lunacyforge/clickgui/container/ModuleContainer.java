package me.lefted.lunacyforge.clickgui.container;

import java.util.function.Consumer;

import me.lefted.lunacyforge.clickgui.elements.ContainerCheckbox;
import me.lefted.lunacyforge.guiapi.Checkbox;
import me.lefted.lunacyforge.guiapi.Element;
import me.lefted.lunacyforge.modules.Module;
import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.utils.DrawUtils;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ModuleContainer extends SettingContainer {

    // ATTRIBUTES
    private Module module;
    // private int visibleYTop;
    // private int visibleYBottom;
    private ContainerCheckbox togglebox;

    // CONSTRUCTOR
    public ModuleContainer(Module module) {
	super();
	this.module = module;
	final ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());

	// set description to module name
	setDescription(module.getName());

	// set own x position
	setPosX(sc.getScaledWidth() / 2 - DEFAULT_WIDTH / 2);

	// create togglebox
	togglebox = new ContainerCheckbox(module.isEnabled());

	// add callback
	togglebox.setConsumer((state) -> {
	    module.setEnabled(state.booleanValue());
	    System.out.println("changing module state from modulecontainer");
	});

	// set x position of togglebox
	togglebox.setPosX(this.getPosX() + 350 - togglebox.WIDTH - 10);

	// add the togglebox
	setSettingElement(togglebox);
    }

    @Override
    public void setPosY(int posY) {
	// own
	super.setPosY(posY);
	// togglebox
	togglebox.setPosY(posY + 7);
    }

    //
    //
    // // METHODS
    // @Override
    // public void draw(int mouseX, int mouseY, float partialTicks) {
    // if (isVisible()) {
    // final DrawUtils utils = DrawUtils.INSTANCE;
    //
    // // bar
    // utils.bindTexture(MODULE_CONTAINER);
    // utils.drawTexturedRectangle(posX, posY, 0, 0, ModuleContainer.WIDTH, ModuleContainer.HEIGHT);
    //
    // // togglebox
    // // DEBUG
    // togglebox.draw(mouseX, mouseY, partialTicks);
    // // name
    // drawString(Minecraft.getMinecraft().fontRendererObj, module.getName(), posX + 20, posY + 12, 0xffffff);
    // }
    // }
    //
    // // opens the module settings
    // @Override
    // public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    // if (isVisible() && isMouseOver(mouseX, mouseY)) {
    // // TODO
    // // show module settings
    //
    // // togglebox
    // togglebox.mouseClicked(mouseX, mouseY, mouseButton);
    // }
    // }
    //
    // public void updateVisibleCoords(int scissorBoxTop, int scissorBoxBottom) {
    // visibleYTop = (posY >= scissorBoxTop) ? posY : scissorBoxTop;
    // visibleYBottom = (posY + HEIGHT <= scissorBoxBottom) ? posY + HEIGHT : scissorBoxBottom;
    // }
    //
    // @Override
    // public boolean isMouseOver(int mouseX, int mouseY) {
    // boolean flag1 = mouseX <= posX + WIDTH && mouseX >= posX;
    // boolean flag2 = mouseY <= visibleYBottom && mouseY >= visibleYTop;
    // return flag1 && flag2;
    // }
    //
}
