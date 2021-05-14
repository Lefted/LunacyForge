package me.lefted.lunacyforge.clickgui.elements;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.clickgui.container.ModuleContainer;
import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import me.lefted.lunacyforge.clickgui.elements.api.Element;
import me.lefted.lunacyforge.clickgui.screens.SearchScreen;
import me.lefted.lunacyforge.config.ClientConfig;
import me.lefted.lunacyforge.modules.Module;
import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class SearchBar extends Element {

    // CONSTANTS
    public static final int WIDTH = 350;
    public static final int HEIGHT = 30;
    private static final ResourceLocation SEARCH_ICON = new ResourceLocation("lunacyforge" + "/search_icon.png");

    // ATTRIBUTES
    private SearchBarTextfield textfield;
    private ArrayList<SettingContainer> results;
    private SearchScreen parent;

    // CONSTRUCTOR
    public SearchBar(ArrayList<SettingContainer> results, SearchScreen parent) {
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

	utils.drawDarkContainer(posX, posY, 350, 30);

	GL11.glEnable(GL11.GL_BLEND);

	utils.bindTexture(SEARCH_ICON);

	final int offX = 13;
	final int offY = 5;

	final int ingameWidth = 22;
	final int ingameHeight = 21;

	final int texWidth = 96;
	final int texHeight = 96;

	final float scale = 1.0F;
	final float scaledTexWidth = texWidth * scale;
	final float scaledTexHeight = texHeight * scale;

	utils.drawScaledCustomSizeModalRect(posX + offX, posY + offY, 0, 0, texWidth, texHeight, ingameWidth, ingameHeight, scaledTexWidth, scaledTexHeight);

	// utils.bindTexture(SEARCH);
	// utils.drawTexturedRectangle(posX, posY, 0, 0, WIDTH, HEIGHT);

	// textfield
	textfield.draw(mouseX, mouseY, partialTicks);

	// reset color mask
	GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	// textfield (even if mouse is not over the search, so it can loose focus)
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

	// update search results if textfield is focused
	if (textfield.isFocused()) {
	    updateSearchresults(textfield.getText());
	}
    }

    private void updateSearchresults(String text) {
	// remove spaces that are in front of the text
	final String noSpaces = text.trim().replaceAll(" +", " ");

	// if there is only whitespace, dont filter
	if (noSpaces.equalsIgnoreCase(" ") || noSpaces.isEmpty()) {
	    SearchScreen.addAllModules(results);

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

	    if (module.isRage() && !ClientConfig.isShowRageMods()) {
		continue moduleLoop;
	    }

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

		// if one tag matches one category
		if (module.getCategory().name().toLowerCase().startsWith(inputTag.toLowerCase()) || inputTag.equalsIgnoreCase(module.getCategory().name())) {
		    // add the module and check next module
		    final ModuleContainer container = new ModuleContainer(module);
		    results.add(container);
		    continue moduleLoop;
		}

		// if module doesn't have any tags, continue
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
	// update scroll height
	parent.scrollVerticalByAmount(0);
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
