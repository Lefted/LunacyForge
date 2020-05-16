package me.lefted.lunacyforge.clickgui.container;

import me.lefted.lunacyforge.clickgui.elements.ContainerCheckbox;
import me.lefted.lunacyforge.clickgui.elements.api.Textfield;
import me.lefted.lunacyforge.clickgui.screens.ModuleSettingsScreen;
import me.lefted.lunacyforge.modules.Module;
import me.lefted.lunacyforge.modules.Reach;
import me.lefted.lunacyforge.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

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
	});

	// set x position of togglebox
	togglebox.setPosX(this.getPosX() + 350 - togglebox.WIDTH - 10);
	setSettingOffsetY(7);

	// add the togglebox
	setSettingElement(togglebox);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	// only pass click call if its visible of if the element is a text field
	if (mouseY <= visibleTop && mouseY <= visibleBottom && !(getSettingElement() instanceof Textfield)) {
	    return;
	}

	// if rightlick show further info
	if (isMouseOver(mouseX, mouseY) && mouseButton == 1) {
	    ModuleSettingsScreen.instance.changeModule(module);
	    Minecraft.getMinecraft().displayGuiScreen(ModuleSettingsScreen.instance);
	} else {
	    super.mouseClicked(mouseX, mouseY, mouseButton);
	}
    }

    //
    // @Override
    // public void setPosY(int posY) {
    // // own
    // super.setPosY(posY);
    // // togglebox
    // togglebox.setPosY(posY + 7);
    // }

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
