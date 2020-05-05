package me.lefted.lunacyforge.clickgui.screens;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.lefted.lunacyforge.clickgui.container.ModuleContainer;
import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import me.lefted.lunacyforge.clickgui.elements.ClientSettingsButton;
import me.lefted.lunacyforge.clickgui.elements.ContainerCheckbox;
import me.lefted.lunacyforge.clickgui.elements.GuiSecurity;
import me.lefted.lunacyforge.clickgui.elements.SearchBar;
import me.lefted.lunacyforge.modules.ClickGui;
import me.lefted.lunacyforge.modules.Module;
import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class SearchScreen extends SettingsScreen {

    // ATTRIBUTES
    private boolean shouldBlur = false;
    // private ArrayList<ModuleContainer> resultingContainers; // list of all modules depending on the searchcontext
    private GuiSecurity security;
    private SearchBar search;
    private ClientSettingsButton btnSettings;

    // INSTANCE
    public static SearchScreen instance;

    // // CONSTRUCTOR
    // public SearchScreen() {
    // super();
    // setDrawDefaultBackground(false);
    // setVerticalScrolling(true);
    // setVerticalWheelScrolling(true);
    // resultingContainers = new ArrayList<ModuleContainer>();
    // }

    // METHODS
    @Override
    public void initOtherElements() {
	// settings button
	btnSettings = new ClientSettingsButton();
	btnSettings.setCallback(() -> Minecraft.getMinecraft().displayGuiScreen(ClientSettingsScreen.instance));

	// security
	security = new GuiSecurity();

	// searchbar
	search = new SearchBar(this.getSettings(), this);
    }

    // TODO is called twice
    @Override
    public void addAllSettings(ArrayList<SettingContainer> settings) {
	// add all modules
	addAllModules(settings);
    }

    // add all available modules
    public static void addAllModules(ArrayList<SettingContainer> settings) {
	// clear container list
	settings.clear();
	// add all available modules as container
	for (Module module : ModuleManager.getModuleList()) {
	    if (module instanceof ClickGui) {
		continue;
	    }
	    // create new container
	    final ModuleContainer container = new ModuleContainer(module);

	    // and add it
	    settings.add(container);
	}
    }

    @Override
    public void drawOtherElements(int mouseX, int mouseY, float partialTicks) {
	// search
	search.draw(mouseX, mouseY, partialTicks);
	// security
	security.draw(mouseX, mouseY, partialTicks);
	// settings button
	btnSettings.draw(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
	return false;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	// deals with nullpointerexceptions
	if (!initDone) {
	    return;
	}

	// pass call to containers
	super.mouseClicked(mouseX, mouseY, mouseButton);

	// security
	security.mouseClicked(mouseX, mouseY, mouseButton);
	// settings button
	btnSettings.mouseClicked(mouseX, mouseY, mouseButton);
	// search
	search.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
	// wait for searchbar, scissorbox to be setup
	if (!initDone) {
	    return;
	}

	// pass call to containers
	super.keyTyped(typedChar, keyCode);

	// searchbar
	search.keyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen() {
	// wait for searchbar, scissorbox to be setup
	if (!initDone) {
	    return;
	}

	// pass call to containers
	super.updateScreen();

//	// TODO check if any text field is focused
//	// movement
//	if (!search.getTextfield().isFocused()) {
//	    // InventoryMove Credits @Andrew Saint 2.3
//	    KeyBinding[] moveKeys = new KeyBinding[] { mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft,
//		    mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint };
//	    KeyBinding[] array = moveKeys;
//	    int length = moveKeys.length;
//	    for (int i = 0; i < length; ++i) {
//		KeyBinding bind = array[i];
//		KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
//	    }
//	}

	// searchbar
	search.updateScreen();
    }

    @Override
    public void onGuiClosed() {
	/*
	 * End blur 
	 */
	if (shouldBlur && mc.entityRenderer.getShaderGroup() != null) {
	    mc.entityRenderer.getShaderGroup().deleteShaderGroup();
	    mc.entityRenderer.stopUseShader();// = null;
	}
	initDone = false;

	// pass call
	super.onGuiClosed();
    }
    
    @Override
    public boolean isUseInventoryMove() {
	return !search.getTextfield().isFocused();
    }

    // @Override
    // public void initGui() {
    // // (re)add all available modules as container
    // addAllModules(resultingContainers);
    //
    // // create the gui security
    // security = new GuiSecurity();
    //
    // // settings button
    // btnSettings = new ClientSettingsButton();
    // btnSettings.setCallback(() -> toggleSettingsMenu());
    //
    // // create searchbar (needs current resolution, thats why it's in initGui)
    // search = new SearchBar(resultingContainers, this);
    //
    // // scissor box
    // final int boxTop = search.getPosY() + SearchBar.HEIGHT + CONTAINER_SPACING * 5;
    // final int boxBottom = boxTop + 250;
    // scissorBox = new ScissorBox(boxTop, boxBottom);
    //
    // // reset scroll
    // setY(0);
    //
    // // set panel borders
    // setPanelBorders();
    //
    // // blur may be set by the user later
    // shouldBlur = false;
    // /*
    // * Start blur
    // */
    // if (shouldBlur && OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
    // if (mc.entityRenderer.getShaderGroup() != null) {
    // mc.entityRenderer.getShaderGroup().deleteShaderGroup();
    // }
    // mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
    // }
    //
    // // initialisation finished
    // initDone = true;
    // }

    // @Override
    // public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    // // wait for searchbar, scissorbox to be setup
    // if (!initDone) {
    // return;
    // }
    //
    // final DrawUtils utils = DrawUtils.INSTANCE;
    //
    // // background
    // drawCustomBackground();
    //
    // // gui security
    // security.draw(mouseX, mouseY, partialTicks);
    //
    // // enable blending
    // GlStateManager.enableBlend();
    // GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    //
    // // module container
    // final int startY = scissorBox.getBoxTop();
    //
    // // scissor box
    // scissorBox.cut(search.getPosX(), scissorBox.getBoxTop(), search.getPosX() + SearchBar.WIDTH, scissorBox.getBoxBottom());
    // GL11.glEnable(GL11.GL_SCISSOR_TEST);
    //
    // // for all containers
    // for (int i = 0; i < resultingContainers.size(); i++) {
    // final ModuleContainer container = this.resultingContainers.get(i);
    //
    // // update module container positions
    // container.setPosY(startY + ModuleContainer.HEIGHT * i + CONTAINER_SPACING * i + this.getY());
    // // and its visible coords
    // container.updateVisibleCoords(scissorBox.getBoxTop(), scissorBox.getBoxBottom());
    //
    // // if completly out of scissorbox make them invisible
    // if ((container.getPosY() + ModuleContainer.HEIGHT) <= startY || container.getPosY() >= startY + 250) {
    // container.setVisible(false);
    // } else {
    // container.setVisible(true);
    // }
    //
    // // and draw them
    // container.draw(mouseX, mouseY, partialTicks);
    // }
    //
    // // end of module container -> end of scissor box
    // GL11.glDisable(GL11.GL_SCISSOR_TEST);
    //
    // // searchbar
    // search.draw(mouseX, mouseY, partialTicks);
    //
    // // settings button
    // btnSettings.draw(mouseX, mouseY, partialTicks);
    //
    // // disable blending
    // GlStateManager.disableBlend();
    // }

    // @Override
    // public void handleMouseInput() throws IOException {
    // // wait for searchbar, scissorbox to be setup
    // if (!initDone) {
    // return;
    // }
    //
    // // own panel aswell as mouseClicked, clickmoved and released
    // super.handleMouseInput();
    //
    // // settingsmenu panel
    // if (menu == Menu.SETTINGS) {
    // if (settingsMenu.isVerticalWheelScrolling()) {
    // int delta = Mouse.getEventDWheel();
    // if (delta != 0) {
    // if (delta > 0) {
    // delta = -1;
    // } else if (delta < 0) {
    // delta = 1;
    // }
    // settingsMenu.scrollVerticalByAmount(delta * this.scrollMultiplier);
    // }
    // }
    // }
    // }

    // @Override
    // public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick) {
    // // wait for searchbar, scissorbox to be setup
    // if (!initDone) {
    // return;
    // }
    //
    // // own panel
    // super.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceClick);
    // // settingsmenu
    // if (menu == Menu.SETTINGS) {
    // settingsMenu.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceClick);
    // }
    // }

    // @Override
    // public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    // // wait for searchbar, scissorbox to be setup
    // if (!initDone) {
    // return;
    // }
    //
    // // own panel
    // super.mouseReleased(mouseX, mouseY, mouseButton);
    // // settingsmenu
    // if (menu == Menu.SETTINGS) {
    // settingsMenu.mouseReleased(mouseX, mouseY, mouseButton);
    // }
    // }

    // sets the borders accrodingly to how much the user needs to be able to scroll
    // public void setPanelBorders() {
    // // calculate the sum height of all containers and spacing
    // final int sumHeight = ModuleContainer.HEIGHT * resultingContainers.size() + CONTAINER_SPACING * (resultingContainers.size() - 1);
    // // calculate the scissorbox height
    // final int scissorBoxHeight = scissorBox.getBoxBottom() - scissorBox.getBoxTop();
    //
    // if (sumHeight > scissorBoxHeight) {
    // getBorders().setMinY(-(sumHeight - scissorBoxHeight));
    // } else {
    // getBorders().setMinY(0);
    // }
    // getBorders().setMaxY(0);
    // }
}
