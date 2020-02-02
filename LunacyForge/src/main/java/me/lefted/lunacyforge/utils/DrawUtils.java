package me.lefted.lunacyforge.utils;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class DrawUtils extends Gui {

    // INSTANCE
    public static final DrawUtils INSTANCE = new DrawUtils();

    // ATTRIBUTES
    final private Minecraft mc = Minecraft.getMinecraft();
    final private ScaledResolution scaledResolution = new ScaledResolution(this.mc);
    private FontRenderer fontRenderer = mc.fontRendererObj;

    // METHODS
    public void bindTexture(ResourceLocation resourceLocation) {
	this.mc.getTextureManager().bindTexture(resourceLocation);
    }

    /**
     * best working
     */
    public void drawTexturedRectangle(int x, int y, int u, int v, int width, int height) {
	this.drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, width, height);
    }
    
    public void drawVerticalLine(int x, int startY, int endY, int color) {
	if (endY < startY) {
	    int i = startY;
	    startY = endY;
	    endY = i;
	}

	drawRect(x, startY + 1, x + 1, endY, color);
    }

    public void drawHorizontalLine(int startX, int endX, int y, int color) {
	if (endX < startX) {
	    int i = startX;
	    startX = endX;
	    endX = i;
	}

	this.drawRect(startX, y, endX + 1, y + 1, color);
    }

    public void drawSelectionBox(int x, int width, int y, int height) {
	Tessellator tessellator = Tessellator.getInstance();
	WorldRenderer worldrenderer = tessellator.getWorldRenderer();

	int i1 = x + (width / 2 - width / 2);
	int j1 = x + width / 2 + width / 2;
	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	GlStateManager.disableTexture2D();
	worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
	worldrenderer.pos((double) i1, (double) (y + height + 2), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
	worldrenderer.pos((double) j1, (double) (y + height + 2), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
	worldrenderer.pos((double) j1, (double) (y - 2), 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
	worldrenderer.pos((double) i1, (double) (y - 2), 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
	worldrenderer.pos((double) (i1 + 1), (double) (y + height + 1), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
	worldrenderer.pos((double) (j1 - 1), (double) (y + height + 1), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
	worldrenderer.pos((double) (j1 - 1), (double) (y - 1), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
	worldrenderer.pos((double) (i1 + 1), (double) (y - 1), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
	tessellator.draw();
	GlStateManager.enableTexture2D();
    }

    public void drawRect(int x, int y, int width, int height, float color) {
	this.drawRect(x, y, x + width, y + height, new Float(color).intValue());
    }

    public void drawRect(double left, double top, double right, double bottom, int color) {
	if (left < right) {
	    double i = left;
	    left = right;
	    right = i;
	}

	if (top < bottom) {
	    double j = top;
	    top = bottom;
	    bottom = j;
	}

	float f3 = (color >> 24 & 0xFF) / 255.0F;
	float f = (color >> 16 & 0xFF) / 255.0F;
	float f1 = (color >> 8 & 0xFF) / 255.0F;
	float f2 = (color & 0xFF) / 255.0F;
	Tessellator tessellator = Tessellator.getInstance();
	WorldRenderer worldrenderer = tessellator.getWorldRenderer();
	GlStateManager.enableBlend();
	GlStateManager.disableTexture2D();
	GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	GlStateManager.color(f, f1, f2, f3);
	worldrenderer.begin(7, DefaultVertexFormats.POSITION);
	worldrenderer.pos(left, bottom, 0.0D).endVertex();
	worldrenderer.pos(right, bottom, 0.0D).endVertex();
	worldrenderer.pos(right, top, 0.0D).endVertex();
	worldrenderer.pos(left, top, 0.0D).endVertex();
	tessellator.draw();
	GlStateManager.enableTexture2D();
	GlStateManager.disableBlend();
    }

    public boolean drawRect(int mouseX, int mouseY, double left, double top, double right, double bottom, int color, int hoverColor) {
	boolean hover = (mouseX > left && mouseX < right && mouseY > top && mouseY < bottom);
	drawRect(left, top, right, bottom, hover ? hoverColor : color);
	return hover;
    }

    public boolean drawRect(int mouseX, int mouseY, String displayString, double left, double top, double right, double bottom, int color, int hoverColor) {
	boolean hover = (mouseX > left && mouseX < right && mouseY > top && mouseY < bottom);
	drawRect(left, top, right, bottom, hover ? hoverColor : color);
	drawCenteredString(displayString, left + (right - left) / 2.0D, top + (bottom - top) / 2.0D - 4.0D);
	return hover;
    }

    public void drawRectangle(int left, int top, int right, int bottom, int color) {
	this.drawRect(left, top, right, bottom, color);
    }

    public void drawTexturedModalRect(double x, double y, double textureX, double textureY, double width, double height) {
	this.drawTexturedModalRect((int) x, (int) y, (int) textureX, (int) textureY, (int) width, (int) height);
    }

    public void drawTexturedModalRect(double left, double top, double right, double bottom) {
	double textureX = 0.0D;
	double textureY = 0.0D;
	double x = left;
	double y = top;
	double width = right - left;
	double height = bottom - top;
	float f = 0.00390625F;
	float f1 = 0.00390625F;
	Tessellator tessellator = Tessellator.getInstance();
	WorldRenderer worldrenderer = tessellator.getWorldRenderer();
	worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
	worldrenderer.pos(x + 0.0D, y + height, this.zLevel).tex(((float) (textureX + 0.0D) * f), ((float) (textureY + height) * f1)).endVertex();
	worldrenderer.pos(x + width, y + height, this.zLevel).tex(((float) (textureX + width) * f), ((float) (textureY + height) * f1)).endVertex();
	worldrenderer.pos(x + width, y + 0.0D, this.zLevel).tex(((float) (textureX + width) * f), ((float) (textureY + 0.0D) * f1)).endVertex();
	worldrenderer.pos(x + 0.0D, y + 0.0D, this.zLevel).tex(((float) (textureX + 0.0D) * f), ((float) (textureY + 0.0D) * f1)).endVertex();
	tessellator.draw();
    }

    /*
     * x, y position to be rendered
     * imageWidth, imageHeight should be 256D
     * maxWidt, maxHeight are size in game		 
     */
    public void drawTexture(double x, double y, double imageWidth, double imageHeight, double maxWidth, double maxHeight, float alpha) {
	GL11.glPushMatrix();
	double sizeWidth = maxWidth / imageWidth;
	double sizeHeight = maxHeight / imageHeight;
	GL11.glScaled(sizeWidth, sizeHeight, 0.0D);
	if (alpha <= 1.0F) {
	    GlStateManager.enableAlpha();
	    GlStateManager.enableBlend();
	    GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
	}
	this.drawTexturedModalRect(x / sizeWidth, y / sizeHeight, x / sizeWidth + imageWidth, y / sizeHeight + imageHeight);
	if (alpha <= 1.0F) {
	    GlStateManager.disableAlpha();
	    GlStateManager.disableBlend();
	}
	GL11.glPopMatrix();
    }

    public void drawRawTexture(double x, double y, double imageWidth, double imageHeight, double maxWidth, double maxHeight) {
	GL11.glPushMatrix();
	double sizeWidth = maxWidth / imageWidth;
	double sizeHeight = maxHeight / imageHeight;
	GL11.glScaled(sizeWidth, sizeHeight, 0.0D);
	drawTexturedModalRect(x / sizeWidth, y / sizeHeight, x / sizeWidth + imageWidth, y / sizeHeight + imageHeight);
	GL11.glPopMatrix();
    }

    public void drawTexture(double x, double y, double imageWidth, double imageHeight, double maxWidth, double maxHeight) {
	drawTexture(x, y, imageWidth, imageHeight, maxWidth, maxHeight, 1.0F);
    }

    public void drawTexture(double x, double y, double texturePosX, double texturePosY, double imageWidth, double imageHeight, double maxWidth,
	double maxHeight, float alpha) {
	GL11.glPushMatrix();
	double sizeWidth = maxWidth / imageWidth;
	double sizeHeight = maxHeight / imageHeight;
	GL11.glScaled(sizeWidth, sizeHeight, 0.0D);
	if (alpha <= 1.0F) {
	    GlStateManager.enableAlpha();
	    GlStateManager.enableBlend();
	    GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
	}
	drawUVTexture(x / sizeWidth, y / sizeHeight, texturePosX, texturePosY, x / sizeWidth + imageWidth - x / sizeWidth, y / sizeHeight + imageHeight - y
	    / sizeHeight);
	if (alpha <= 1.0F) {
	    GlStateManager.disableAlpha();
	    GlStateManager.disableBlend();
	}
	GL11.glPopMatrix();
    }

    public void drawTexture(double x, double y, double texturePosX, double texturePosY, double imageWidth, double imageHeight, double maxWidth,
	double maxHeight) {
	drawTexture(x, y, texturePosX, texturePosY, imageWidth, imageHeight, maxWidth, maxHeight, 1.0F);
    }

    private void drawUVTexture(double x, double y, double textureX, double textureY, double width, double height) {
	float f = 0.00390625F;
	float f1 = 0.00390625F;
	Tessellator tessellator = Tessellator.getInstance();
	WorldRenderer worldrenderer = tessellator.getWorldRenderer();
	worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
	worldrenderer.pos(x + 0.0D, y + height, this.zLevel).tex(((float) (textureX + 0.0D) * f), ((float) (textureY + height) * f1)).endVertex();
	worldrenderer.pos(x + width, y + height, this.zLevel).tex(((float) (textureX + width) * f), ((float) (textureY + height) * f1)).endVertex();
	worldrenderer.pos(x + width, y + 0.0D, this.zLevel).tex(((float) (textureX + width) * f), ((float) (textureY + 0.0D) * f1)).endVertex();
	worldrenderer.pos(x + 0.0D, y + 0.0D, this.zLevel).tex(((float) (textureX + 0.0D) * f), ((float) (textureY + 0.0D) * f1)).endVertex();
	tessellator.draw();
    }

    public void drawRectBorder(double left, double top, double right, double bottom, int color, double thickness) {
	drawRect(left + thickness, top, right - thickness, top + thickness, color);

	drawRect(right - thickness, top, right, bottom, color);

	drawRect(left + thickness, bottom - thickness, right - thickness, bottom, color);

	drawRect(left, top, left + thickness, bottom, color);
    }

    public void drawString(String text, double x, double y) {
	this.fontRenderer.drawString(text, (float) x, (float) y, 16777215, true);
    }

    public void drawStringWithShadow(String text, double x, double y, int color) {
	this.fontRenderer.drawStringWithShadow(text, (float) x, (float) y, color);
    }

    public void drawRightString(String text, double x, double y) {
	this.drawString(text, x - getStringWidth(text), y);
    }

    public void drawRightStringWithShadow(String text, int x, int y, int color) {
	this.fontRenderer.drawStringWithShadow(text, (x - getStringWidth(text)), y, color);
    }

    public void drawCenteredString(String text, double x, double y) {
	drawString(text, x - (getStringWidth(text) / 2), y);
    }

    public void drawString(String text, double x, double y, double size) {
	GL11.glPushMatrix();
	GL11.glScaled(size, size, size);
	drawString(text, x / size, y / size);
	GL11.glPopMatrix();
    }

    public void drawCenteredString(String text, double x, double y, double size) {
	GL11.glPushMatrix();
	GL11.glScaled(size, size, size);
	drawCenteredString(text, x / size, y / size);
	GL11.glPopMatrix();
    }

    public void drawRightString(String text, double x, double y, double size) {
	GL11.glPushMatrix();
	GL11.glScaled(size, size, size);
	drawString(text, x / size - getStringWidth(text), y / size);
	GL11.glPopMatrix();
    }

    public int getStringWidth(String text) {
	return this.fontRenderer.getStringWidth(text);
    }

    public int getWidth() {
	return this.scaledResolution.getScaledWidth();
    }

    public int getHeight() {
	return this.scaledResolution.getScaledHeight();
    }
}
