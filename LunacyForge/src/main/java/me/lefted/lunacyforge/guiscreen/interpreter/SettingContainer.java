package me.lefted.lunacyforge.guiscreen.interpreter;

import me.lefted.lunacyforge.guiapi.Element;
import me.lefted.lunacyforge.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

/* This class combines the container texture, a description and an element. */
public class SettingContainer extends Element {

    // CONSTANTS
    public static final int WIDTH = 350;
    public static final int HEIGHT = 30;
    private static final ResourceLocation CONTAINER = new ResourceLocation("lunacyforge", "container.png");

    // ATTRIBUTES
    private Element settingElement;
    private String description;

    // CONSTRUCTOR
    public SettingContainer() {
	final ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
	setPosX(sc.getScaledWidth() / 2 - WIDTH / 2);
	setVisible(true);
    }

    // METHODS
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	final DrawUtils utils = DrawUtils.INSTANCE;

	// render container
	utils.bindTexture(CONTAINER);
	utils.drawTexturedRectangle(posX, posY, 0, 0, ModuleContainer.WIDTH, ModuleContainer.HEIGHT);

	// render description
	// TODO offset x, y
	utils.drawString(description, posX, posY);

	if (settingElement != null) {
	    settingElement.draw(mouseX, mouseY, partialTicks);
	}
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	if (settingElement != null) {
	    settingElement.mouseClicked(mouseX, mouseY, mouseButton);
	}
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick) {
	if (settingElement != null) {
	    settingElement.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceClick);
	}
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	if (settingElement != null) {
	    settingElement.mouseReleased(mouseX, mouseY, mouseButton);
	}
    }

    @Override
    public void updateScreen() {
	if (settingElement != null) {
	    settingElement.updateScreen();
	}
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
	if (settingElement != null) {
	    settingElement.keyTyped(typedChar, keyCode);
	}
    }

    @Override
    public void setPosY(int posY) {
	// own
	super.setPosY(posY);
	// setting
	if (settingElement != null) {
	    settingElement.setPosY(posY);
	}
    }

    public Element getSettingElement() {
	return settingElement;
    }

    public void setSettingElement(Element settingElement) {
	this.settingElement = settingElement;

	// apply own positioning
	settingElement.setPosX(settingElement.getPosX() + getPosX());
	settingElement.setPosY(settingElement.getPosY() + getPosY());
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }
}
