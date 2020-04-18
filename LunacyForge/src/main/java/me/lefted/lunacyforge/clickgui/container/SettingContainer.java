package me.lefted.lunacyforge.clickgui.container;

import me.lefted.lunacyforge.clickgui.utils.ScissorBox;
import me.lefted.lunacyforge.guiapi.Element;
import me.lefted.lunacyforge.utils.DrawUtils;
import net.minecraft.util.ResourceLocation;

/* This class combines the container texture, a description and an element. */
public class SettingContainer extends Element {

    // CONSTANTS
    // public static final int WIDTH = 350;
    // public static final int HEIGHT = 30;
    private static final ResourceLocation CONTAINER_CORNERS = new ResourceLocation("lunacyforge", "container.png");
    private static final int CORNER_RADIUS = 9;

    // ATTRIBUTES
    private Element settingElement;
    private String description;
    private int width;
    private int height;
    private int visibleTop;
    private int visibleBottom;
    private int visibleLeft;
    private int visibleRight;

    // CONSTRUCTOR
    public SettingContainer(int x, int y, int width, int height) {
	this.posX = x;
	this.posY = y;
	this.width = width;
	this.height = height;

	// final ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
	// setPosX(sc.getScaledWidth() / 2 - WIDTH / 2);
	setVisible(true);
    }

    // CONSTRUCTOR
    public SettingContainer(int width, int height) {
	this.posX = 0;
	this.posY = 0;
	this.width = width;
	this.height = height;

	// final ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
	// setPosX(sc.getScaledWidth() / 2 - WIDTH / 2);
	setVisible(true);
    }

    // METHODS
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	final DrawUtils utils = DrawUtils.INSTANCE;
	if (isVisible()) {

	    // draw container background
	    drawContainer(utils);
	    // render description
	    // TODO offset x, y properly
	    utils.drawString(description, posX, posY);
	    if (settingElement != null) {
		settingElement.draw(mouseX, mouseY, partialTicks);
	    }
	}
    }

    // draws the container according to x, y, width, height with round corners
    private void drawContainer(DrawUtils utils) {
	// draw rectangle 1
	utils.drawRectWidthHeight(posX + CORNER_RADIUS, posY, width - 2 * CORNER_RADIUS, CORNER_RADIUS, 0x393334);
	// draw rectangle 2
	utils.drawRectWidthHeight(posX, posY + CORNER_RADIUS, width, height - 2 * CORNER_RADIUS, 0x393334);
	// draw rectangle 3
	utils.drawRectWidthHeight(posX + CORNER_RADIUS, height - CORNER_RADIUS, width - 2 * CORNER_RADIUS, CORNER_RADIUS, 0x393334);

	// TODO draw corners

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	// TODO if click out of visible portions and setting is textfield, unfocus textfield

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
    public boolean isMouseOver(int mouseX, int mouseY) {
	boolean flag1 = mouseX <= visibleRight && mouseX >= visibleLeft;
	boolean flag2 = mouseY <= visibleBottom && mouseY >= visibleTop;
	return flag1 && flag2;
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
	final int offset = posY - getPosY();

	// apply to own position
	super.setPosY(posY);
	// setting
	if (settingElement != null) {
	    // apply offset to setting
	    settingElement.setPosY(settingElement.getPosX() + offset);
	}
    }

    // update the coordinates of the visible portions of the container
    public void updateVisibleCoords(ScissorBox scissorBox) {
	visibleTop = (posY >= scissorBox.getY()) ? posY : scissorBox.getY();
	visibleBottom = (posY + height <= (scissorBox.getY() + scissorBox.getHeight())) ? (posY + height) : (scissorBox.getY() + scissorBox.getHeight());
    }

    public Element getSettingElement() {
	return settingElement;
    }

    public void setSettingElement(Element settingElement) {
	this.settingElement = settingElement;

	// combine positioning of setting with own one
	settingElement.setPosX(settingElement.getPosX() + getPosX());
	settingElement.setPosY(settingElement.getPosY() + getPosY());
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }
}
