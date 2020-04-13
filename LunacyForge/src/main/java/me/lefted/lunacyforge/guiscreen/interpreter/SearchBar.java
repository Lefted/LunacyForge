package me.lefted.lunacyforge.guiscreen.interpreter;

import me.lefted.lunacyforge.guiapi.Element;
import me.lefted.lunacyforge.utils.DrawUtils;
import me.lefted.lunacyforge.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class SearchBar extends Element{

    // CONSTANTS
    public static final int WIDTH = 350;
    public static final int HEIGHT = 30;
    private static final ResourceLocation SEARCH = new ResourceLocation("lunacyforge", "search2.png");
    
    // CONSTRUCTOR
    public SearchBar() {
    }
    
    // METHODS
    public void init() {
	final ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
	setPosX(sc.getScaledWidth() / 2 - WIDTH / 2);
	setPosY(sc.getScaledHeight() / 10);
    }
    
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	final DrawUtils utils = DrawUtils.INSTANCE;
	
	utils.bindTexture(SEARCH);
	utils.drawTexturedRectangle(posX, posY, 0, 0, WIDTH, HEIGHT);
    }
    
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	if (isMouseOver(mouseX, mouseY)) {
	    // DEBUG
	    Logger.logChatMessage("search has been clicked");
	}
    }
    
    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
	boolean flag1 = mouseX >= posX && mouseX <= posX + WIDTH;
	boolean flag2 = mouseY >= posY && mouseY <= posY + HEIGHT;
	return flag1 && flag2;
    }
}
