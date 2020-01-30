package me.lefted.lunacyforge.guiapi;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;

/* something like radiobuttons add callbacks to each button enable/disable of buttons will be handled by this */
public class ButtonSelection extends Element {

    // ATTRIBUTES
    private ArrayList<Button> buttonList = new ArrayList<Button>();

    // CONSTRUCTOR
    public ButtonSelection(int indexSelected, Button... buttons) {
	this.addButton(buttons);
	this.buttonList.get(indexSelected).setEnabled(false);
	this.setVisible(true);
	this.posX = 0;
	this.posY = 0;
    }

    // METHODS
    private void addButton(Button... buttons) {
	for (Button button : buttons) {
	    this.buttonList.add(button);
	}
    }

    public List getButtonList() {
	return this.buttonList;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	if (!this.isVisible()) {
	    return;
	}
	for (Button button : this.buttonList) {
	    button.draw(mouseX, mouseY, partialTicks);
	}
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	if (!this.isVisible()) {
	    return;
	}
	for (Button button : this.buttonList) {
	    // TODO make mouseClicked return boolean
	    if (button.isMouseOver(mouseX, mouseY) && button.isEnabled()) {
		button.setEnabled(false);
		button.playPressSound(Minecraft.getMinecraft().getSoundHandler());
		if (button.getCallback() != null) {
		    button.getCallback().invoke();
		}
		this.enableAllOtherButtons(this.buttonList.indexOf(button));
	    }
	}
    }

    private void enableAllOtherButtons(int index) {
	for (int i = 0; i < this.buttonList.size(); i++) {
	    if (i != index) {
		this.buttonList.get(i).setEnabled(true);
	    }
	}
    }

    @Override
    public void setPosX(int x) {
	final int offsetX = x - this.getPosX();
	this.posX = x;

	// offset the elements accordingly
	for (Button button : this.buttonList) {
	    button.setPosX(button.getPosX() + offsetX);
	}
    }

    @Override
    public void setPosY(int y) {
	final int offsetY = y - this.getPosY();
	this.posY = y;

	// offset the elements accordingly
	for (Button button : this.buttonList) {
	    button.setPosY(button.getPosY() + offsetY);
	}
    }
}
