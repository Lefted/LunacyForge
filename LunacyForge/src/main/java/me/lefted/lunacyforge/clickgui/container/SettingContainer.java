package me.lefted.lunacyforge.clickgui.container;

import java.util.function.Predicate;

import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.clickgui.utils.ScissorBox;
import me.lefted.lunacyforge.guiapi.Element;
import me.lefted.lunacyforge.guiapi.Textfield;
import me.lefted.lunacyforge.utils.DrawUtils;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import scala.actors.threadpool.Arrays;

/* This class combines the container texture, a description and an element. */
// TODO mouseClick is not passed to all containers when not a container was clicked
public class SettingContainer extends Element {

    // CONSTANTS
    public static final int DEFAULT_WIDTH = 350;
    public static final int DEFAULT_HEIGHT = 30;

    private static final int TEX_WIDTH = 700;
    private static final int TEX_HEIGHT = 60;
    private static final int RADIUS = 8;
    private static final float SCALE = 0.3F;
    private static final float SCALED_TEX_WIDTH = TEX_WIDTH * SCALE;
    private static final float SCALED_TEX_HEIGHT = TEX_HEIGHT * SCALE;

    // ATTRIBUTES
    private Element settingElement;
    private String description;
    private String hoverText;
    private int width;
    protected int height;
    protected int visibleTop;
    protected int visibleBottom;
    protected int visibleLeft;
    protected int visibleRight;

    private int offsetY;
    private int backgroundLevel = 0; // determines if this is drawn before other settings or after

    private SettingsGroup settingGroup;
    private Predicate<int[]> isMouseOverHoverArea;

    // CONSTRUCTOR
    public SettingContainer(int x, int y, int width, int height) {
	this.posX = x;
	this.posY = y;
	this.width = width;
	this.height = height;
	setVisible(true);
    }

    // CONSTRUCTOR
    public SettingContainer(int width, int height) {
	this.width = width;
	this.height = height;
	this.posX = 0;
	this.posY = 0;
	setVisible(true);
    }

    // CONSTRUCTOR
    public SettingContainer() {
	this.posX = 0;
	this.posY = 0;
	this.width = DEFAULT_WIDTH;
	this.height = DEFAULT_HEIGHT;
	setVisible(true);
    }

    // METHODS
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	final DrawUtils utils = DrawUtils.INSTANCE;
	if (isVisible()) {

	    // draw container background
	    if (!isInGroup()) {
		drawContainer();
	    }
	    // render description
	    utils.drawString(description, posX + 10, posY + 12);
	    if (settingElement != null) {
		settingElement.draw(mouseX, mouseY, partialTicks);
	    }
	}
    }

    // draw hover
    public void drawHoverText(int mouseX, int mouseY) {
	if (hoverText != null && !hoverText.isEmpty()) {
	    if (isMouseOverHoverArea(mouseX, mouseY)) {
		DrawUtils.INSTANCE.drawHoverText(hoverText, 50, mouseX, mouseY + 10);
	    }
	}
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	// TODO unfocus textfield instead if its a textfield
	// prevent passing the call to the textfield if its not for unfocusing

	// only pass click call if its visible of if the element is a text field
	if (mouseY <= visibleTop && mouseY <= visibleBottom && !(settingElement instanceof Textfield)) {
	    return;
	}

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

    private boolean isMouseOverHoverArea(int mouseX, int mouseY) {
	if (isMouseOverHoverArea != null) {
	    final int[] pos = { mouseX, mouseY };
	    return isMouseOverHoverArea.test(pos);
	}
	return mouseX >= posX && mouseX <= posX + Math.min(90, DrawUtils.INSTANCE.getStringWidth(description)) + 20 && mouseY >= posY && mouseY <= posY
	    + height;
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
	// final int offset = posY - getPosY();

	// apply to own position
	super.setPosY(posY);
	// setting
	if (settingElement != null) {
	    // apply offset to setting
	    settingElement.setPosY(posY + getSettingOffsetY());
	}
    }

    private void drawContainer() {
	DrawUtils.INSTANCE.drawDarkContainer(posX, posY, width, height);
    }

    // USETHIS
    public int getSettingOffsetY() {
	return offsetY;
    }

    // USETHIS
    public void centerX() {
	final ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
	posX = sc.getScaledWidth() / 2 - this.width / 2;
    }

    // USETHIS to determine if the setting is in background/foreground
    public void setBackgroundLevel(int backgroundLevel) {
	this.backgroundLevel = backgroundLevel;
    }

    // USETHIS to set the hovertext
    public void setHoverText(String text) {
	this.hoverText = text;
    }

    public int getBackgroundLevel() {
	return backgroundLevel;
    }

    public void setSettingOffsetY(int offsetY) {
	this.offsetY = offsetY;
    }

    // update the coordinates of the visible portions of the container
    public void updateVisibleCoords(ScissorBox scissorBox) {
	visibleTop = (posY >= scissorBox.getY()) ? posY : scissorBox.getY();
	visibleBottom = (posY + height <= (scissorBox.getY() + scissorBox.getHeight())) ? (posY + height) : (scissorBox.getY() + scissorBox.getHeight());

	visibleLeft = (posX >= scissorBox.getX()) ? posX : scissorBox.getX();
	visibleRight = (posX + width <= scissorBox.getX() + scissorBox.getWidth()) ? posX + width : scissorBox.getX() + scissorBox.getWidth();
    }

    public Element getSettingElement() {
	return settingElement;
    }

    public void setSettingElement(Element settingElement) {
	this.settingElement = settingElement;

	// combine positioning of setting with own one
	settingElement.setPosX(settingElement.getPosX());
	settingElement.setPosY(settingElement.getPosY());
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

    public void setHeight(int height) {
	this.height = height;
    }

    public void setIsMouseOverHoverAreaPredicate(Predicate<int[]> predicate) {
	this.isMouseOverHoverArea = predicate;
    }

    public boolean isInGroup() {
	return settingGroup != null && settingGroup.getSettings() != null && !settingGroup.getSettings().isEmpty();
    }

    public void setGroup(SettingsGroup group) {
	this.settingGroup = group;
    }
}
