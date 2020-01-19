package me.lefted.lunacyforge.testgui;

import java.io.IOException;

import me.lefted.lunacyforge.guiapi.Label;
import me.lefted.lunacyforge.guiapi.Panel;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.gui.GuiScreen;

public class Screen extends GuiScreen {

    private Panel panel = new Panel(0, 0);

    @Override
    public void initGui() {
	this.panel.getElements().add(new Label(10, 10, "Party"));
    }
    
    
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	super.drawScreen(mouseX, mouseY, partialTicks);
	this.drawDefaultBackground();
	this.panel.draw(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
	this.panel.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
	this.panel.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
	this.panel.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
	super.keyTyped(typedChar, keyCode);
	this.panel.keyTyped(typedChar, keyCode);
    }
}
