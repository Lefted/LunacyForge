package me.lefted.lunacyforge.guiscreenold.interpreter;

import me.lefted.lunacyforge.guiapi.Panel;
import me.lefted.lunacyforge.guiscreenold.Interpreter;
import me.lefted.lunacyforge.modules.Rectangle;
import me.lefted.lunacyforge.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/* The actual screen where you can change settings */
public class ModuleScreen extends Panel {

    // CONSTRUCTOR
    public ModuleScreen() {
	super(0, 0);
	this.setDrawDefaultBackground(true);
	this.setVerticalScrolling(true);
	this.setScrollMouseButtons(1);
    }

    // METHODS
    @Override
    public void initGui() {
	super.initGui();
	// clear prevous elements
	this.getElements().clear();

	// adds all available modules
	Interpreter.addInterpretedModules(this.getElements());
    }

    // DEBUG
    private static final ResourceLocation SEARCH = new ResourceLocation("lunacyforge", "search.png");

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	super.drawScreen(mouseX, mouseY, partialTicks);

	final DrawUtils utils = DrawUtils.INSTANCE;

	utils.bindTexture(SEARCH);
	utils.drawTexturedRectangle(this.width / 2 - 350 / 2, 50, 0, 0, 350, 30);
    }

}
