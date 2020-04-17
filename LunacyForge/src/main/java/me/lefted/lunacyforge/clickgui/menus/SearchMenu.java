package me.lefted.lunacyforge.clickgui.menus;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.clickgui.container.ModuleContainer;
import me.lefted.lunacyforge.clickgui.elements.ClientSettingsButton;
import me.lefted.lunacyforge.clickgui.elements.GuiSecurity;
import me.lefted.lunacyforge.clickgui.elements.SearchBar;
import me.lefted.lunacyforge.clickgui.utils.ScissorBox;
import me.lefted.lunacyforge.guiapi.Panel;
import me.lefted.lunacyforge.modules.ClickGui;
import me.lefted.lunacyforge.modules.KeepSprint;
import me.lefted.lunacyforge.modules.Module;
import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.utils.ColorUtils;
import me.lefted.lunacyforge.utils.DrawUtils;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class SearchMenu extends Panel {

    // CONSTANTS
    private static final int CONTAINER_SPACING = 4;

    // ATTRIBUTES
    private boolean shouldBlur = true;
    private boolean initDone = false;
    private ArrayList<ModuleContainer> resultingContainers; // list of all modules depending on the searchcontext
    private GuiSecurity security;
    private SearchBar search;
    private ScissorBox scissorBox; // used to cut off rendering when scrolling
    private ClientSettingsButton btnSettings;
    private ClientSettingsMenu settingsMenu; // place to configure client specific settings
    private static Menu menu; // saves the state of the menu: search, settings, module

    // INSTANCE
    public static SearchMenu instance;

    // CONSTRUCTOR
    public SearchMenu() {
	super(0, 0);
	setDrawDefaultBackground(false);
	setVerticalScrolling(true);
	setVerticalWheelScrolling(true);
	resultingContainers = new ArrayList<ModuleContainer>();

	settingsMenu = new ClientSettingsMenu();
    }

    // METHODS
    // TODO is called twice
    @Override
    public void initGui() {
	// (re)add all available modules as container
	addAllModules(resultingContainers);

	// create the gui security
	security = new GuiSecurity();

	// settings button
	btnSettings = new ClientSettingsButton();
	btnSettings.setCallback(() -> toggleSettingsMenu());

	// create searchbar (needs current resolution, thats why it's in initGui)
	search = new SearchBar(resultingContainers, this);

	// scissor box
	final int boxTop = search.getPosY() + SearchBar.HEIGHT + CONTAINER_SPACING * 5;
	final int boxBottom = boxTop + 250;
	scissorBox = new ScissorBox(boxTop, boxBottom);

	// reset scroll
	setY(0);

	// set panel borders
	setPanelBorders();

	// blur may be set by the user later
	shouldBlur = false;
	/*
	 * Start blur
	 */
	if (shouldBlur && OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
	    if (mc.entityRenderer.getShaderGroup() != null) {
		mc.entityRenderer.getShaderGroup().deleteShaderGroup();
	    }
	    mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
	}

	// set menu to search
	menu = Menu.SEARCH;

	// initialisation finished
	initDone = true;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	// wait for searchbar, scissorbox to be setup
	if (!initDone) {
	    return;
	}

	final DrawUtils utils = DrawUtils.INSTANCE;

	// background
	drawCustomBackground();

	// gui security
	security.draw(mouseX, mouseY, partialTicks);

	// enable blending
	GlStateManager.enableBlend();
	GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

	// module container
	if (menu == Menu.SEARCH) {

	    final int startY = scissorBox.getBoxTop();

	    // scissor box
	    scissorBox.cut(search.getPosX(), scissorBox.getBoxTop(), search.getPosX() + SearchBar.WIDTH, scissorBox.getBoxBottom());
	    GL11.glEnable(GL11.GL_SCISSOR_TEST);

	    // for all containers
	    for (int i = 0; i < resultingContainers.size(); i++) {
		final ModuleContainer container = this.resultingContainers.get(i);

		// update module container positions
		container.setPosY(startY + ModuleContainer.HEIGHT * i + CONTAINER_SPACING * i + this.getY());
		// and its visible coords
		container.updateVisibleCoords(scissorBox.getBoxTop(), scissorBox.getBoxBottom());

		// if completly out of scissorbox make them invisible
		if ((container.getPosY() + ModuleContainer.HEIGHT) <= startY || container.getPosY() >= startY + 250) {
		    container.setVisible(false);
		} else {
		    container.setVisible(true);
		}

		// and draw them
		container.draw(mouseX, mouseY, partialTicks);
	    }

	    // end of module container -> end of scissor box
	    GL11.glDisable(GL11.GL_SCISSOR_TEST);

	    // searchbar
	    search.draw(mouseX, mouseY, partialTicks);

	    // settings button
	    btnSettings.draw(mouseX, mouseY, partialTicks);
	} else if (menu == Menu.SETTINGS) {
	    settingsMenu.drawScreen(mouseX, mouseY, partialTicks);
	}

	// disable blending
	GlStateManager.disableBlend();
	super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawCustomBackground() {
	// light background
	int alpha = 60;
	this.drawGradientRect(0, 0, this.width, this.height, ColorUtils.toRGB(255, 255, 255, alpha), ColorUtils.toRGB(255, 255, 255, alpha));
    }

    @Override
    public boolean doesGuiPauseGame() {
	return false;
    }

    @Override
    public void handleMouseInput() throws IOException {
	// wait for searchbar, scissorbox to be setup
	if (!initDone) {
	    return;
	}

	// own panel aswell as mouseClicked, clickmoved and released
	super.handleMouseInput();

	// settingsmenu panel
	if (menu == Menu.SETTINGS) {
	    if (settingsMenu.isVerticalWheelScrolling()) {
		int delta = Mouse.getEventDWheel();
		if (delta != 0) {
		    if (delta > 0) {
			delta = -1;
		    } else if (delta < 0) {
			delta = 1;
		    }
		    settingsMenu.scrollVerticalByAmount(delta * this.scrollMultiplier);
		}
	    }
	}
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	// wait for searchbar, scissorbox to be setup
	if (!initDone) {
	    return;
	}

	// call the panels mouseClicked
	super.mouseClicked(mouseX, mouseY, mouseButton);

	if (menu == Menu.SEARCH) {
	    for (ModuleContainer container : this.resultingContainers) {
		container.mouseClicked(mouseX, mouseY, mouseButton);
	    }

	    search.mouseClicked(mouseX, mouseY, mouseButton);
	    btnSettings.mouseClicked(mouseX, mouseY, mouseButton);
	} else if (menu == Menu.SETTINGS) {
	    settingsMenu.mouseClicked(mouseX, mouseY, mouseButton);
	}

	security.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick) {
	// wait for searchbar, scissorbox to be setup
	if (!initDone) {
	    return;
	}

	// own panel
	super.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceClick);
	// settingsmenu
	if (menu == Menu.SETTINGS) {
	    settingsMenu.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceClick);
	}
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	// wait for searchbar, scissorbox to be setup
	if (!initDone) {
	    return;
	}

	// own panel
	super.mouseReleased(mouseX, mouseY, mouseButton);
	// settingsmenu
	if (menu == Menu.SETTINGS) {
	    settingsMenu.mouseReleased(mouseX, mouseY, mouseButton);
	}
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
	// wait for searchbar, scissorbox to be setup
	if (!initDone) {
	    return;
	}

	if (keyCode == Keyboard.KEY_ESCAPE) {
	    super.keyTyped(typedChar, keyCode);
	} else {
	    if (menu == Menu.SEARCH) {
		// searchbar
		search.keyTyped(typedChar, keyCode);
	    } else if (menu == Menu.SETTINGS) {
		settingsMenu.keyTyped(typedChar, keyCode);
	    }
	}
    }

    @Override
    public void updateScreen() {
	// wait for searchbar, scissorbox to be setup
	if (!initDone) {
	    return;
	}

	super.updateScreen();

	// movement
	if (!search.getTextfield().isFocused()) {
	    // InventoryMove Credits @Andrew Saint 2.3
	    KeyBinding[] moveKeys = new KeyBinding[] { mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft,
		    mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint };
	    KeyBinding[] array = moveKeys;
	    int length = moveKeys.length;
	    for (int i = 0; i < length; ++i) {
		KeyBinding bind = array[i];
		KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
	    }
	}

	if (menu == Menu.SEARCH) {
	    // searchbar
	    search.updateScreen();
	} else if (menu == Menu.SETTINGS) {
	    settingsMenu.updateScreen();
	}
    }

    // add all available modules
    public static void addAllModules(ArrayList<ModuleContainer> list) {
	// clear container list
	list.clear();
	// add all available modules as container
	for (Module module : ModuleManager.getModuleList()) {
	    if (module instanceof ClickGui) {
		continue;
	    }
	    // create new container
	    final ModuleContainer container = new ModuleContainer(module);

	    // and ad it
	    list.add(container);
	}
    }

    // sets the borders accrodingly to how much the user needs to be able to scroll
    public void setPanelBorders() {
	// calculate the sum height of all containers and spacing
	final int sumHeight = ModuleContainer.HEIGHT * resultingContainers.size() + CONTAINER_SPACING * (resultingContainers.size() - 1);
	// calculate the scissorbox height
	final int scissorBoxHeight = scissorBox.getBoxBottom() - scissorBox.getBoxTop();

	if (sumHeight > scissorBoxHeight) {
	    getBorders().setMinY(-(sumHeight - scissorBoxHeight));
	} else {
	    getBorders().setMinY(0);
	}
	getBorders().setMaxY(0);
    }

    private void toggleSettingsMenu() {
	Logger.logChatMessage("TOggle");
	if (menu == Menu.SEARCH) {
	    settingsMenu.initGui();
	    menu = Menu.SETTINGS;
	}
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
	if (menu == Menu.SETTINGS) {
	    settingsMenu.onGuiClosed();
	}
	initDone = false;
    }

    public static Menu getMenu() {
	return menu;
    }

    public static void setMenu(Menu menu) {
	SearchMenu.menu = menu;
    }
}