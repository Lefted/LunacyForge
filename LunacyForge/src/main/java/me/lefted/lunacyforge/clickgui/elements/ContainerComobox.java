package me.lefted.lunacyforge.clickgui.elements;

import java.util.LinkedList;
import java.util.function.Consumer;

import me.lefted.lunacyforge.guiapi.Element;
import me.lefted.lunacyforge.utils.DrawUtils;
import scala.actors.threadpool.Arrays;

public class ContainerComobox extends Element {

    // CONSTANTS
    public static int ENTRY_WIDTH = 128;
    public static int ENTRY_HEIGHT = 20;

    // ATTRIBUTES
    private boolean opened;
    private LinkedList<String> entries;
    private Consumer<String> consumer;

    // CONSTRUCTOR
    /**
     * @param entries The entries you want to be able to select
     */
    public ContainerComobox(int selectedEntryIndex, String... entries) {
	this.entries = new LinkedList<>();
	this.entries.addAll(Arrays.asList(entries));

	// make sure selected one is first in list
	final String selectedEntrySave = this.entries.get(selectedEntryIndex);
	this.entries.remove(selectedEntryIndex);
	this.entries.addFirst(selectedEntrySave);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	final String selectedEntry = entries.get(0);
	if (!opened) {
	    // draw selected entry
	    drawEntry(posX, posY, selectedEntry);
	} else {
	    // draw selcted entry
	    drawEntry(posX, posY, selectedEntry);

	    // draw other entries
	    // do not draw the first one (selected one) again
	    for (int i = 1; i < entries.size(); i++) {
		drawEntry(posX, posY + ENTRY_HEIGHT * i, entries.get(i));
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
		    opened = true;
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

    // returns the entry the mouse is hovering over
    private int getMouseOverEntryIndex(int mouseX, int mouseY) {
	final int index = (mouseY - posY) / ENTRY_HEIGHT;
	return index;
    }

    private void updateSelectedEntry(int newSelectedEntryIndex) {
	final String newSelectedEntrySave = entries.get(newSelectedEntryIndex);

	entries.remove(newSelectedEntryIndex);
	entries.addFirst(newSelectedEntrySave);

	if (consumer != null) {
	    consumer.accept(newSelectedEntrySave);
	}
    }

    private void drawEntry(int posX, int posY, String entry) {
	final DrawUtils utils = DrawUtils.INSTANCE;

	// background
	utils.drawRectWidthHeight(posX, posY, ENTRY_WIDTH, ENTRY_HEIGHT, 0xFF767676);

	// text
	utils.drawStringWithShadow(entry, posX + ENTRY_WIDTH / 2 - utils.getStringWidth(entry) / 2, posY, 0xffffff);
    }

    public void setConsumer(Consumer<String> consumer) {
	this.consumer = consumer;
    }

    private void closeCombobox() {
	opened = false;
    }
}
