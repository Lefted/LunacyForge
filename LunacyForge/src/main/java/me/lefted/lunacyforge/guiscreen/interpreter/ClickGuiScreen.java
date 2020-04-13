package me.lefted.lunacyforge.guiscreen.interpreter;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

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

public class ClickGuiScreen extends Panel {

    // CONSTANTS
    private static final int CONTAINER_SPACING = 4;
    private static final ResourceLocation MEME_NIGGA = new ResourceLocation("lunacyforge", "meme_nigga.png");

    // ATTRIBUTES
    private boolean shouldBlur = true;
    private ArrayList<ModuleContainer> container;
    private SearchBar search;
    private ScissorBox scissorBox;

    // INSTANCE
    public static ClickGuiScreen instance;
    // private int scissorBoxTop;
    // private int scissorBoxBottom;

    // CONSTRUCTOR
    public ClickGuiScreen() {
	super(0, 0);
	setDrawDefaultBackground(false);
	setVerticalScrolling(true);
	container = new ArrayList<ModuleContainer>();
    }

    // METHODS
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	final DrawUtils utils = DrawUtils.INSTANCE;

	drawCustomBackground();

	GlStateManager.enableBlend();

	// searchbar
	search.draw(mouseX, mouseY, partialTicks);

	// meme nigga
	utils.bindTexture(MEME_NIGGA);
	utils.drawTexturedRectangle(this.width - 46, this.height - 64, 0, 0, 48, 64);

	// module container
	final int startY = search.getPosY() + SearchBar.HEIGHT + CONTAINER_SPACING * 5;

	// scissor box
	scissorBox.cut(search.getPosX(), scissorBox.getBoxTop(), search.getPosX() + SearchBar.WIDTH, scissorBox.getBoxBottom());
	GL11.glEnable(GL11.GL_SCISSOR_TEST);

	// for all containers
	for (int i = 0; i < container.size(); i++) {
	    final ModuleContainer container = this.container.get(i);

	    // update module container positions
	    container.setPosY(startY + ModuleContainer.HEIGHT * i + CONTAINER_SPACING * i + this.getY());
	    // and its visible coords
	    container.updateVisibleCoords(scissorBox.getBoxTop(), scissorBox.getBoxBottom());

	    // if out of scissorbox make them invisible
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
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	// call the panels mouseClicked
	super.mouseClicked(mouseX, mouseY, mouseButton);

	for (ModuleContainer container : this.container) {
	    container.mouseClicked(mouseX, mouseY, mouseButton);
	}

	search.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
	super.updateScreen();

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

    // TODO is called twice
    @Override
    public void initGui() {
	// clear container list
	container.clear();
	// add all available modules as container
	for (Module module : ModuleManager.getModuleList()) {
	    if (module instanceof ClickGui) {
		continue;
	    }
	    final ModuleContainer container = new ModuleContainer(module);

	    if (module instanceof KeepSprint) {
		container.setVisible(false);
	    }

	    // and ad it
	    this.container.add(container);
	}

	// DEBUG
	for (Module module : ModuleManager.getModuleList()) {
	    if (module instanceof ClickGui) {
		continue;
	    }
	    final ModuleContainer container = new ModuleContainer(module);

	    if (module instanceof KeepSprint) {
		container.setVisible(false);
	    }

	    // and ad it
	    this.container.add(container);
	}

	// set panel borders
	setPanelBorders();
	// init searchbar
	search = new SearchBar();
	search.init();
	
	// scissor box
	final int boxTop = search.getPosY() + SearchBar.HEIGHT + CONTAINER_SPACING * 5;
	final int boxBottom = boxTop + 250;
	scissorBox = new ScissorBox(boxTop, boxBottom);
	
	// set panel posY to 0
	setY(0);
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
    }

    // sets the borders accrodingly to how much the user needs to be able to scroll
    private void setPanelBorders() {
	// calculate the sum height of all containers and spacing
	final int sumHeight = ModuleContainer.HEIGHT * container.size() + CONTAINER_SPACING * (container.size() - 1);
	// calculate the scissorbox height
	final int scissorBoxHeight = scissorBox.getBoxBottom() - scissorBox.getBoxTop();

	if (sumHeight > scissorBoxHeight) {
	    getBorders().setMinY(-(sumHeight - scissorBoxHeight));
	}
	getBorders().setMaxY(0);
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
    }

}
