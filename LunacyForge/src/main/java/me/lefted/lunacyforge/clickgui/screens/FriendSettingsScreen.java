package me.lefted.lunacyforge.clickgui.screens;

import java.util.ArrayList;
import java.util.List;

import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import me.lefted.lunacyforge.clickgui.elements.AddFriendButton;
import me.lefted.lunacyforge.clickgui.elements.BackButton;
import me.lefted.lunacyforge.clickgui.elements.ContainerButton;
import me.lefted.lunacyforge.friends.FriendManager;

public class FriendSettingsScreen extends SettingsScreen {

    // INSTANCE
    public static FriendSettingsScreen instance;

    // ATTRIBUTES
    private BackButton backButton;
    private AddFriendButton addFriendButton;

    // METHODS
    @Override
    public void initOtherElements() {
	// pass call
	super.initOtherElements();

	// back button
	backButton = new BackButton();
	backButton.setCallback(() -> mc.displayGuiScreen(SearchScreen.instance));
	// add friend button
	addFriendButton = new AddFriendButton();
	addFriendButton.setCallback(() -> mc.displayGuiScreen(AddFriendScreen.instance));
    }

    @Override
    public void addAllSettings(ArrayList<SettingContainer> settings) {
	settings.clear();

	final List<String> friends = FriendManager.instance.getFriendlist();

	if (friends.isEmpty() || friends.size() == 0) {
	    return;
	}

	friends.forEach(friendName -> {

	    SettingContainer container = new SettingContainer();
	    container.centerX();
	    container.setDescription(friendName);

	    ContainerButton button = new ContainerButton(75, 16, "Delete");
	    button.setPosX(container.getPosX() + container.getWidth() - button.getWidth() - 10);
	    button.setCallback(() -> {
		FriendManager.instance.removeFriend(friendName);
		container.setDescription("[removed]");
	    });

	    container.setSettingOffsetY(6);
	    container.setSettingElement(button);
	    settings.add(container);
	});
    }

    @Override
    public void drawOtherElements(int mouseX, int mouseY, float partialTicks) {
	// pass call
	super.drawOtherElements(mouseX, mouseY, partialTicks);

	// back button
	backButton.draw(mouseX, mouseY, partialTicks);
	// add friend button
	addFriendButton.draw(mouseX, mouseY, partialTicks);
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
	// add friend button
	addFriendButton.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean doesGuiPauseGame() {
	return false;
    }

    @Override
    public boolean isUseInventoryMove() {
	return true;
    }

}
