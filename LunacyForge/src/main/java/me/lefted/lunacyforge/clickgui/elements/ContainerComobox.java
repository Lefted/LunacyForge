package me.lefted.lunacyforge.clickgui.elements;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Consumer;

import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import me.lefted.lunacyforge.clickgui.elements.api.Element;
import me.lefted.lunacyforge.clickgui.screens.SettingsScreen;
import me.lefted.lunacyforge.config.ClientConfig;
import me.lefted.lunacyforge.utils.DrawUtils;
import me.lefted.lunacyforge.utils.HSLColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import scala.actors.threadpool.Arrays;

public class ContainerComobox extends Element {

    // CONSTANTS
    public static final int ENTRY_WIDTH = 128;
    public static final int ENTRY_HEIGHT = 16;
    public static final ResourceLocation COMBOBOX = new ResourceLocation("lunacyforge" + "/container_light.png");
    private static final ResourceLocation PRESS_SOUND = new ResourceLocation("gui.button.press");

    private static final int TEX_WIDTH = 700;
    private static final int TEX_HEIGHT = 60;
    private static final int RADIUS = 8;
    private static final float SCALE = 0.3F;
    private static final float SCALED_TEX_WIDTH = TEX_WIDTH * SCALE;
    private static final float SCALED_TEX_HEIGHT = TEX_HEIGHT * SCALE;

    // ATTRIBUTES
    private SettingsScreen screen;
    private SettingContainer parent; // needed to extend the container when this is opened
    private int originalHeight;
    private boolean opened;
    private LinkedList<String> entries; // used for sorting the entries
    private Map<String, Integer> entryValueMap; // used for determining the value
    private Consumer<String> stringConsumer;
    private Consumer<Integer> intConsumer;

    private int selectedEntryIndex;

    // CONSTRUCTOR
    /**
     * @param entries The entries you want to be able to select. Corresponding to the value (first entry = 0)
     */
    public ContainerComobox(SettingsScreen screen, SettingContainer parent, int selectedEntryIndex, String... entries) {
	this.parent = parent;
	this.screen = screen;

	entryValueMap = new HashMap<String, Integer>();

	// fill entry to value map
	for (int i = 0; i < entries.length; i++) {
	    entryValueMap.put(entries[i], i);
	}

	this.entries = new LinkedList<>();
	this.entries.addAll(Arrays.asList(entries));

	// make sure selected one is first in list
	this.selectedEntryIndex = selectedEntryIndex;
	final String selectedEntrySave = this.entries.get(selectedEntryIndex);
	this.entries.remove(selectedEntryIndex);
	this.entries.addFirst(selectedEntrySave);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	final String selectedEntry = entries.get(0);
	if (!opened) {
	    // draw selected entry

	    drawClosedEntry(mouseX, mouseY, posX, posY, selectedEntry);
	} else {
	    // draw selcted entry
	    drawEntryTop(mouseX, mouseY, posX, posY, selectedEntry);

	    // draw other entries
	    // do not draw the first one (selected one) again
	    for (int i = 1; i < entries.size(); i++) {
		if (i == entries.size() - 1) {
		    drawEntryBottom(mouseX, mouseY, posX, posY + ENTRY_HEIGHT * i, entries.get(i));
		} else {
		    drawEntryMiddle(mouseX, mouseY, posX, posY + ENTRY_HEIGHT * i, entries.get(i));
		}
	    }
	}
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	try {
	    final String selectedEntry = entries.get(0);
	    if (opened) {
		if (isMouseOver(mouseX, mouseY)) {
		    final int mouseOverEntryIndex = getMouseOverEntryIndex(mouseX, mouseY);

		    // if the clicked entry is the already selected one, close
		    if (mouseOverEntryIndex == 0) {
			closeCombobox();
		    } else {
			closeCombobox();
			updateSelectedEntry(mouseOverEntryIndex);
		    }
		} else {
		    closeCombobox();
		}
	    } else {
		if (isMouseOver(mouseX, mouseY)) {
		    openCombobox();
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
	if (opened) {
	    final int bottom = posY + entries.size() * ENTRY_HEIGHT;
	    return mouseX >= posX && mouseX <= posX + ENTRY_WIDTH && mouseY >= posY && mouseY <= bottom;
	} else {
	    return mouseX >= posX && mouseX <= posX + ENTRY_WIDTH && mouseY >= posY && mouseY <= posY + ENTRY_HEIGHT;
	}
    }

    private boolean isMouseOverEntry(int mouseX, int mouseY, int posX, int posY) {
	return mouseX >= posX && mouseX <= posX + ENTRY_WIDTH && mouseY >= posY && mouseY <= posY + ENTRY_HEIGHT;
    }

    // returns the entry the mouse is hovering over
    private int getMouseOverEntryIndex(int mouseX, int mouseY) {
	final int index = (mouseY - posY) / ENTRY_HEIGHT;
	return index;
    }

    private void updateSelectedEntry(int newSelectedEntryIndex) {
	final String newSelectedEntrySave = entries.get(newSelectedEntryIndex);

	// add the selcted entry at the top of the list
	entries.remove(newSelectedEntryIndex);
	entries.addFirst(newSelectedEntrySave);

	if (stringConsumer != null) {
	    stringConsumer.accept(newSelectedEntrySave);
	}
	if (intConsumer != null) {
	    intConsumer.accept(entryValueMap.get(newSelectedEntrySave));
	}
	selectedEntryIndex = entryValueMap.get(newSelectedEntrySave);
    }

    private void drawClosedEntry(int mouseX, int mouseY, int posX, int posY, String entry) {
	final DrawUtils utils = DrawUtils.INSTANCE;

	// background
	GlStateManager.enableBlend();
	utils.bindTexture(COMBOBOX);

	int textColor = 14737632;
	if (isMouseOverEntry(mouseX, mouseY, posX, posY)) {
	    textColor = 16777120;
	    // tint();
	    GL11.glColor4f(0.9F, 0.9F, 0.9F, 1F);
	}

	// middle part
	Gui.drawScaledCustomSizeModalRect(posX + RADIUS, posY, RADIUS, RADIUS, 1, 1, ENTRY_WIDTH - 2 * RADIUS, ENTRY_HEIGHT, SCALED_TEX_WIDTH,
	    SCALED_TEX_HEIGHT);

	// left side
	Gui.drawScaledCustomSizeModalRect(posX, posY + RADIUS, RADIUS, RADIUS, 1, 1, RADIUS, ENTRY_HEIGHT - 2 * RADIUS, SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	// right side
	Gui.drawScaledCustomSizeModalRect(posX + ENTRY_WIDTH - RADIUS, posY + RADIUS, RADIUS, RADIUS, 1, 1, RADIUS, ENTRY_HEIGHT - 2 * RADIUS, SCALED_TEX_WIDTH,
	    SCALED_TEX_HEIGHT);

	// top left
	Gui.drawScaledCustomSizeModalRect(posX, posY, 0, 0, RADIUS, RADIUS, RADIUS, RADIUS, SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	// top right
	Gui.drawScaledCustomSizeModalRect(posX + ENTRY_WIDTH - RADIUS, posY, -RADIUS, 0, RADIUS, RADIUS, RADIUS, RADIUS, SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	// bottom left
	Gui.drawScaledCustomSizeModalRect(posX, posY + ENTRY_HEIGHT - RADIUS, 0, -RADIUS, RADIUS, RADIUS, RADIUS, RADIUS, SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	// bottom right
	Gui.drawScaledCustomSizeModalRect(posX + ENTRY_WIDTH - RADIUS, posY + ENTRY_HEIGHT - RADIUS, -RADIUS, -RADIUS, RADIUS, RADIUS, RADIUS, RADIUS,
	    SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	GlStateManager.disableBlend();
	GL11.glColor4f(1F, 1F, 1F, 1F);

	// text
	utils.drawStringWithShadow(entry, posX + ENTRY_WIDTH / 2 - utils.getStringWidth(entry) / 2, posY + 4, textColor);

	GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    private void drawEntryTop(int mouseX, int mouseY, int posX, int posY, String entry) {
	final DrawUtils utils = DrawUtils.INSTANCE;

	// background
	GlStateManager.enableBlend();
	utils.bindTexture(COMBOBOX);

	int textColor = 14737632;
	if (isMouseOverEntry(mouseX, mouseY, posX, posY)) {
	    textColor = 16777120;
	    // tint();
	    GL11.glColor4f(0.9F, 0.9F, 0.9F, 1F);
	}

	// middle
	utils.drawScaledCustomSizeModalRect(posX, posY + RADIUS, 0, RADIUS, 1, 1, ENTRY_WIDTH, ENTRY_HEIGHT - RADIUS, SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	// middle top
	utils.drawScaledCustomSizeModalRect(posX + RADIUS, posY, RADIUS, 0, 1, 1, ENTRY_WIDTH - 2 * RADIUS, RADIUS, SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	// top left corner
	utils.drawScaledCustomSizeModalRect(posX, posY, 0, 0, RADIUS, RADIUS, RADIUS, RADIUS, SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	// top right corner
	utils.drawScaledCustomSizeModalRect(posX + ENTRY_WIDTH - RADIUS, posY, -RADIUS, 0, RADIUS, RADIUS, RADIUS, RADIUS, SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	GlStateManager.disableBlend();
	// utils.drawRectWidthHeight(posX, posY, ENTRY_WIDTH, ENTRY_HEIGHT, 0xFF767676);

	// text
	utils.drawStringWithShadow(entry, posX + ENTRY_WIDTH / 2 - utils.getStringWidth(entry) / 2, posY + 4, textColor);
	GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    private void drawEntryMiddle(int mouseX, int mouseY, int posX, int posY, String entry) {
	final DrawUtils utils = DrawUtils.INSTANCE;

	// background
	GlStateManager.enableBlend();
	utils.bindTexture(COMBOBOX);

	int textColor = 14737632;
	if (isMouseOverEntry(mouseX, mouseY, posX, posY)) {
	    textColor = 16777120;
	    // tint();
	    GL11.glColor4f(0.9F, 0.9F, 0.9F, 1F);
	}

	utils.drawScaledCustomSizeModalRect(posX, posY, RADIUS + 1, RADIUS + 1, 1, 1, ENTRY_WIDTH, ENTRY_HEIGHT, SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);
	GlStateManager.disableBlend();

	// text
	utils.drawStringWithShadow(entry, posX + ENTRY_WIDTH / 2 - utils.getStringWidth(entry) / 2, posY + 4, textColor);
	GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    private void drawEntryBottom(int mouseX, int mouseY, int posX, int posY, String entry) {
	final DrawUtils utils = DrawUtils.INSTANCE;

	// background
	GlStateManager.enableBlend();
	utils.bindTexture(COMBOBOX);

	int textColor = 14737632;
	if (isMouseOverEntry(mouseX, mouseY, posX, posY)) {
	    textColor = 16777120;
	    // tint();
	    GL11.glColor4f(0.9F, 0.9F, 0.9F, 1F);
	}

	// middle
	utils.drawScaledCustomSizeModalRect(posX, posY, RADIUS, RADIUS, 1, 1, ENTRY_WIDTH, ENTRY_HEIGHT - RADIUS, SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	// bottom middle
	utils.drawScaledCustomSizeModalRect(posX + RADIUS, posY + ENTRY_HEIGHT - RADIUS, RADIUS, RADIUS, 1, 1, ENTRY_WIDTH - 2 * RADIUS, RADIUS,
	    SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	// bottom left
	Gui.drawScaledCustomSizeModalRect(posX, posY + ENTRY_HEIGHT - RADIUS, 0, -RADIUS, RADIUS, RADIUS, RADIUS, RADIUS, SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	// bottom right
	Gui.drawScaledCustomSizeModalRect(posX + ENTRY_WIDTH - RADIUS, posY + ENTRY_HEIGHT - RADIUS, -RADIUS, -RADIUS, RADIUS, RADIUS, RADIUS, RADIUS,
	    SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	GlStateManager.disableBlend();

	// utils.drawRectWidthHeight(posX, posY, ENTRY_WIDTH, ENTRY_HEIGHT, 0xFF767676);

	// text
	utils.drawStringWithShadow(entry, posX + ENTRY_WIDTH / 2 - utils.getStringWidth(entry) / 2, posY + 4, textColor);
	GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    private void tint() {
	// get hue
	HSLColor co = new HSLColor(ClientConfig.getGuiColor());

	Color coo = HSLColor.toRGB(co.getHue(), 100, 90);

	float r = coo.getRed() / 255F;
	float g = coo.getGreen() / 255F;
	float b = coo.getBlue() / 255F;

	GL11.glColor4f(r, g, b, 1F);
    }

    public void setStringConsumer(Consumer<String> consumer) {
	this.stringConsumer = consumer;
    }

    public void setIntConsumer(Consumer<Integer> consumer) {
	this.intConsumer = consumer;
    }

    private void closeCombobox() {
	playPressSound(Minecraft.getMinecraft().getSoundHandler());
	opened = false;
	parent.setHeight(originalHeight);
	if (parent.getGroup() != null) {
	    parent.getGroup().updateHeight();
	}
	screen.setPanelBorders();
	screen.scrollVerticalByAmount(0);
    }

    private void openCombobox() {
	playPressSound(Minecraft.getMinecraft().getSoundHandler());
	originalHeight = parent.getHeight();

	int extraNeeded = (entries.size() - 1) * ENTRY_HEIGHT;
	parent.setHeight(parent.getHeight() + extraNeeded);
	if (parent.getGroup() != null) {
	    parent.getGroup().updateHeight();
	}
	opened = true;
	screen.setPanelBorders();
    }

    public void playPressSound(SoundHandler soundHandler) {
	soundHandler.playSound(PositionedSoundRecord.create(PRESS_SOUND, 1.0F));
    }

    public int getValue() {
	return this.selectedEntryIndex;
    }
}
