package me.lefted.lunacyforge.clickgui.screens;

import java.util.ArrayList;

import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import me.lefted.lunacyforge.clickgui.elements.BackButton;
import me.lefted.lunacyforge.clickgui.elements.ContainerButton;
import me.lefted.lunacyforge.clickgui.elements.ContainerTextfield;
import me.lefted.lunacyforge.clickgui.elements.SearchBar;
import me.lefted.lunacyforge.clickgui.elements.api.Button;
import me.lefted.lunacyforge.friends.FriendManager;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.gui.ScaledResolution;
import scala.actors.threadpool.Arrays;

public class AddFriendScreen extends SettingsScreen {

    // INSTANCE
    public static AddFriendScreen instance;

    // ATTRIBUTES
    private BackButton backButton;
    private ContainerButton doneButton;
    private ContainerTextfield field;
    private boolean invMove = true;

    // METHODS
    @Override
    public void initOtherElements() {
	// pass call
	super.initOtherElements();

	// invMove
	invMove = true;

	// back button
	backButton = new BackButton();
	backButton.setCallback(() -> mc.displayGuiScreen(FriendSettingsScreen.instance));

	// done button
	final ScaledResolution sc = new ScaledResolution(mc);

	doneButton = new ContainerButton(75, 20, "Done");
	doneButton.setPosX(sc.getScaledWidth() / 2 + 350 / 2 - 85);
	doneButton.setPosY(getListY() + 35);
	doneButton.setCallback(() -> validate());
    }

    @Override
    public void addAllSettings(ArrayList<SettingContainer> settings) {
	final SettingContainer container = new SettingContainer(350, 60);
	container.centerX();
	container.setDescription("Friend name");

	field = new ContainerTextfield(129);
	field.setPosX(container.getPosX() + container.getWidth() - field.getWidth() - 17);

	container.setSettingElement(field);
	container.setSettingOffsetY(5);
	settings.add(container);
    }

    @Override
    public void drawOtherElements(int mouseX, int mouseY, float partialTicks) {
	// pass call
	super.drawOtherElements(mouseX, mouseY, partialTicks);

	// back button
	backButton.draw(mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawOtherElementsAfter(int mouseX, int mouseY, float partialTicks) {
	// pass call
	super.drawOtherElements(mouseX, mouseY, partialTicks);

	// done button
	doneButton.draw(mouseX, mouseY, partialTicks);
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	// deals with nullpointerexceptions
	if (!initDone) {
	    return;
	}

	if (mouseButton == 1) {
	    backButton.getCallback().invoke();
	    return;
	}

	// pass call
	super.mouseClicked(mouseX, mouseY, mouseButton);

	// back button
	backButton.mouseClicked(mouseX, mouseY, mouseButton);

	// done button
	doneButton.mouseClicked(mouseX, mouseY, mouseButton);

	if (field.isFocused()) {
	    invMove = false;
	} else {
	    invMove = true;
	}
    }

    private void validate() {
	final String text = field.getText();
	
	final String[] parts = text.trim().split(" ");
	
	if (parts.length != 0) {
	    final String name = parts[0];
	    if (!name.isEmpty()) {
		FriendManager.instance.addFriend(name);
		mc.displayGuiScreen(FriendSettingsScreen.instance);
	    }
	}
    }

    @Override
    public boolean doesGuiPauseGame() {
	return false;
    }

    @Override
    public boolean isUseInventoryMove() {
	return invMove;
    }
}
