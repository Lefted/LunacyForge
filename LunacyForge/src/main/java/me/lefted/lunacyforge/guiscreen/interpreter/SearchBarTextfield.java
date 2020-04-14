package me.lefted.lunacyforge.guiscreen.interpreter;

import me.lefted.lunacyforge.guiapi.Textfield;
import net.minecraft.client.gui.Gui;

public class SearchBarTextfield extends Textfield {

    // ATTRIBUTES
    private SearchBar parent;
    
    // CONSTRUCTOR
    public SearchBarTextfield(int x, int y, int width, int height, SearchBar parent) {
	super(x, y, width, height);
	this.parent = parent;
    }

    // METHODS
    @Override
    public void keyTyped(char typedChar, int keyCode) {
	super.keyTyped(typedChar, keyCode);
	// TODO update resulting list
    }
    
    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
	return mouseX >= parent.getPosX() && mouseX < parent.getPosX() + parent.WIDTH && mouseY >= parent.getPosY() && mouseY < parent.getPosY() + parent.HEIGHT;
    }
}
