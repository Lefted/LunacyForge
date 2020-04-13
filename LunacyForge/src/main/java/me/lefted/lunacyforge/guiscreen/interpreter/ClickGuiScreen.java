package me.lefted.lunacyforge.guiscreen.interpreter;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.guiapi.Panel;
import me.lefted.lunacyforge.modules.ClickGui;
import me.lefted.lunacyforge.modules.Module;
import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.utils.ColorUtils;
import me.lefted.lunacyforge.utils.DrawUtils;
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
    private static final ResourceLocation SEARCH = new ResourceLocation("lunacyforge", "search2.png");
    private static final ResourceLocation MEME_NIGGA = new ResourceLocation("lunacyforge", "meme_nigga.png");

    // ATTRIBUTES
    private boolean shouldBlur = true;
    private ArrayList<ModuleContainer> container;

    // INSTANCE
    public static ClickGuiScreen instance;

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
	// TODO make search own element
	final int searchWidth = 350;
	final int searchHeight = 30;
	final int searchY = this.height / 10;
	final int searchX = this.width / 2 - searchWidth / 2;

	// search
	utils.bindTexture(SEARCH);
	utils.drawTexturedRectangle(searchX, searchY, 0, 0, searchWidth, searchHeight);
	// meme nigga
	utils.bindTexture(MEME_NIGGA);
	utils.drawTexturedRectangle(this.width - 46, this.height - 64, 0, 0, 48, 64);

	// module container
	final int startY = searchY + searchHeight + CONTAINER_SPACING * 5;

	// scissor box
	scissorBox(searchX, startY, searchX + searchWidth, startY + 250);
	GL11.glEnable(GL11.GL_SCISSOR_TEST);

	// for all containers
	for (int i = 0; i < container.size(); i++) {
	    final ModuleContainer container = this.container.get(i);

	    // update module container positions
	    container.setPosY(startY + ModuleContainer.HEIGHT * i + CONTAINER_SPACING * i + this.getY());
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

    // Credits Wurst @Alexander1998
    private void scissorBox(int x, int y, int xend, int yend) {
	int width = xend - x;
	int height = yend - y;
	ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
	int factor = sr.getScaleFactor();
	int bottomY = (Minecraft.getMinecraft()).currentScreen.height - yend;
	GL11.glScissor(x * factor, bottomY * factor, width * factor, height * factor);
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
	    // and ad it
	    this.container.add(container);
	}

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
