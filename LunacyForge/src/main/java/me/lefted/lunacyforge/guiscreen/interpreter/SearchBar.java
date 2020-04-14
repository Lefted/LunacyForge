package me.lefted.lunacyforge.guiscreen.interpreter;

import java.util.ArrayList;

import me.lefted.lunacyforge.guiapi.Element;
import me.lefted.lunacyforge.modules.ClickGui;
import me.lefted.lunacyforge.modules.KeepSprint;
import me.lefted.lunacyforge.modules.Module;
import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.utils.DrawUtils;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class SearchBar extends Element {

    // CONSTANTS
    public static final int WIDTH = 350;
    public static final int HEIGHT = 30;
    private static final ResourceLocation SEARCH = new ResourceLocation("lunacyforge", "search5.png");

    // ATTRIBUTES
    private SearchBarTextfield textfield;
    private ArrayList<ModuleContainer> results;
    private ClickGuiScreen parent;

    // CONSTRUCTOR
    public SearchBar(ArrayList<ModuleContainer> results, ClickGuiScreen parent) {
	this.results = results;
	this.parent = parent;

	final ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
	setPosX(sc.getScaledWidth() / 2 - WIDTH / 2);
	setPosY(sc.getScaledHeight() / 10);

	// textfield
	textfield = new SearchBarTextfield(posX + 38, posY + 13, WIDTH, HEIGHT, this);
	textfield.setMaxStringLength(48);
	textfield.setCanLoseFocus(true);
	textfield.setEnableBackgroundDrawing(false);
    }

    // METHODS
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	final DrawUtils utils = DrawUtils.INSTANCE;

	utils.bindTexture(SEARCH);
	utils.drawTexturedRectangle(posX, posY, 0, 0, WIDTH, HEIGHT);

	// textfield
	textfield.draw(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	if (isMouseOver(mouseX, mouseY)) {
	    // DEBUG
	    Logger.logChatMessage("search has been clicked");

	}
	// textfield (must be outside so that is can loose focus)
	textfield.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
	// textfield
	textfield.updateScreen();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
	// textfield
	textfield.keyTyped(typedChar, keyCode);

	// update search results
	updateSearchresults(textfield.getText());
    }

    private void updateSearchresults(String text) {
	// remove spaces that are in front of the text
	final String noSpaces = text.trim().replaceAll(" +", " ");

	// if there is only whitespace, dont filter
	if (noSpaces.equalsIgnoreCase(" ") || noSpaces.isEmpty()) {
	    ClickGuiScreen.addAllModules(results);

	    // update scroll borders
	    parent.setPanelBorders();
	    return;
	}

	// else create an array of inputTags
	final String[] inputTags = noSpaces.split(" ");

	// clear the results list
	results.clear();

	// for all modules
	moduleLoop: for (Module module : ModuleManager.getModuleList()) {

	    // check inputTags
	    for (String inputTag : inputTags) {
		// if one tag matches modulename
		if (inputTag.equalsIgnoreCase(module.getName()) || inputTag.toLowerCase().contains(module.getName().toLowerCase()) || module.getName()
		    .toLowerCase().startsWith(inputTag.toLowerCase())) {
		    // add the module and check next module
		    final ModuleContainer container = new ModuleContainer(module);
		    results.add(container);
		    continue moduleLoop;
		}

		// if module doesn't have any other tags (other than its name) continue
		if (!module.hasTags()) {
		    continue moduleLoop;
		}

		// else if one tag matches moduletags
		for (String moduleTag : module.getTags()) {
		    if (inputTag.equalsIgnoreCase(moduleTag) || inputTag.toLowerCase().contains(moduleTag.toLowerCase()) || moduleTag.toLowerCase().startsWith(
			inputTag.toLowerCase())) {
			// add the module and check next module
			final ModuleContainer container = new ModuleContainer(module);
			results.add(container);
			continue moduleLoop;
		    }
		}

	    }
	}

	// update scroll borders
	parent.setPanelBorders();
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
	boolean flag1 = mouseX >= posX && mouseX <= posX + WIDTH;
	boolean flag2 = mouseY >= posY && mouseY <= posY + HEIGHT;
	return flag1 && flag2;
    }

    public SearchBarTextfield getTextfield() {
	return textfield;
    }
}
