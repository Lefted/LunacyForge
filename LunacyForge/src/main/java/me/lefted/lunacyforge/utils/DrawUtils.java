package me.lefted.lunacyforge.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.LunacyForge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class DrawUtils extends Gui {

    // CONSTANTS
    private static final ResourceLocation CONTAINER_DARK = new ResourceLocation("lunacyforge" + "/container_dark.png");
    private static final ResourceLocation CONTAINER_LIGHT = new ResourceLocation("lunacyforge" + "/container_light.png");
    private static final int TEX_WIDTH = 700;
    private static final int TEX_HEIGHT = 60;
    private static final int RADIUS = 8;
    private static final float SCALE = 0.3F;
    private static final float SCALED_TEX_WIDTH = TEX_WIDTH * SCALE;
    private static final float SCALED_TEX_HEIGHT = TEX_HEIGHT * SCALE;

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

    // USETHIS to draw a container texture
    public static void drawDarkContainer(int posX, int posY, int ingameWidth, int ingameHeight) {
	// blending
	GlStateManager.enableBlend();
	GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

	DrawUtils.INSTANCE.bindTexture(CONTAINER_DARK);

	// middle part
	Gui.drawScaledCustomSizeModalRect(posX + RADIUS, posY, RADIUS, RADIUS, 1, 1, ingameWidth - 2 * RADIUS, ingameHeight, SCALED_TEX_WIDTH,
	    SCALED_TEX_HEIGHT);

	// left side
	Gui.drawScaledCustomSizeModalRect(posX, posY + RADIUS, RADIUS, RADIUS, 1, 1, RADIUS, ingameHeight - 2 * RADIUS, SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	// right side
	Gui.drawScaledCustomSizeModalRect(posX + ingameWidth - RADIUS, posY + RADIUS, RADIUS, RADIUS, 1, 1, RADIUS, ingameHeight - 2 * RADIUS, SCALED_TEX_WIDTH,
	    SCALED_TEX_HEIGHT);

	// top left
	Gui.drawScaledCustomSizeModalRect(posX, posY, 0, 0, RADIUS, RADIUS, RADIUS, RADIUS, SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	// top right
	Gui.drawScaledCustomSizeModalRect(posX + ingameWidth - RADIUS, posY, -RADIUS, 0, RADIUS, RADIUS, RADIUS, RADIUS, SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	// bottom left
	Gui.drawScaledCustomSizeModalRect(posX, posY + ingameHeight - RADIUS, 0, -RADIUS, RADIUS, RADIUS, RADIUS, RADIUS, SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	// bottom right
	Gui.drawScaledCustomSizeModalRect(posX + ingameWidth - RADIUS, posY + ingameHeight - RADIUS, -RADIUS, -RADIUS, RADIUS, RADIUS, RADIUS, RADIUS,
	    SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);
    }

    // USETHIS to draw a light container texture
    public static void drawLightContainer(int posX, int posY, int ingameWidth, int ingameHeight) {
	// blending
	GlStateManager.enableBlend();
	GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

	DrawUtils.INSTANCE.bindTexture(CONTAINER_LIGHT);

	// middle part
	Gui.drawScaledCustomSizeModalRect(posX + RADIUS, posY, RADIUS, RADIUS, 1, 1, ingameWidth - 2 * RADIUS, ingameHeight, SCALED_TEX_WIDTH,
	    SCALED_TEX_HEIGHT);

	// left side
	Gui.drawScaledCustomSizeModalRect(posX, posY + RADIUS, RADIUS, RADIUS, 1, 1, RADIUS, ingameHeight - 2 * RADIUS, SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	// right side
	Gui.drawScaledCustomSizeModalRect(posX + ingameWidth - RADIUS, posY + RADIUS, RADIUS, RADIUS, 1, 1, RADIUS, ingameHeight - 2 * RADIUS, SCALED_TEX_WIDTH,
	    SCALED_TEX_HEIGHT);

	// top left
	Gui.drawScaledCustomSizeModalRect(posX, posY, 0, 0, RADIUS, RADIUS, RADIUS, RADIUS, SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	// top right
	Gui.drawScaledCustomSizeModalRect(posX + ingameWidth - RADIUS, posY, -RADIUS, 0, RADIUS, RADIUS, RADIUS, RADIUS, SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	// bottom left
	Gui.drawScaledCustomSizeModalRect(posX, posY + ingameHeight - RADIUS, 0, -RADIUS, RADIUS, RADIUS, RADIUS, RADIUS, SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);

	// bottom right
	Gui.drawScaledCustomSizeModalRect(posX + ingameWidth - RADIUS, posY + ingameHeight - RADIUS, -RADIUS, -RADIUS, RADIUS, RADIUS, RADIUS, RADIUS,
	    SCALED_TEX_WIDTH, SCALED_TEX_HEIGHT);
    }

    public void drawGradientRect4(int left, int top, int right, int bottom, int colorTopRight, int colorTopLeft, int colorBottomLeft, int colorBottomRight) {
	// top right
	float a1 = (float) (colorTopRight >> 24 & 255) / 255.0F;
	float r1 = (float) (colorTopRight >> 16 & 255) / 255.0F;
	float g1 = (float) (colorTopRight >> 8 & 255) / 255.0F;
	float b1 = (float) (colorTopRight & 255) / 255.0F;

	// top left
	float a2 = (float) (colorTopLeft >> 24 & 255) / 255.0F;
	float r2 = (float) (colorTopLeft >> 16 & 255) / 255.0F;
	float g2 = (float) (colorTopLeft >> 8 & 255) / 255.0F;
	float b2 = (float) (colorTopLeft & 255) / 255.0F;

	// bottom left
	float a3 = (float) (colorBottomLeft >> 24 & 255) / 255.0F;
	float r3 = (float) (colorBottomLeft >> 16 & 255) / 255.0F;
	float g3 = (float) (colorBottomLeft >> 8 & 255) / 255.0F;
	float b3 = (float) (colorBottomLeft & 255) / 255.0F;

	// bottom right
	float a4 = (float) (colorBottomRight >> 24 & 255) / 255.0F;
	float r4 = (float) (colorBottomRight >> 16 & 255) / 255.0F;
	float g4 = (float) (colorBottomRight >> 8 & 255) / 255.0F;
	float b4 = (float) (colorBottomRight & 255) / 255.0F;

	GlStateManager.disableTexture2D();
	GlStateManager.enableBlend();
	GlStateManager.disableAlpha();
	GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	GlStateManager.shadeModel(7425);
	Tessellator tessellator = Tessellator.getInstance();
	WorldRenderer worldrenderer = tessellator.getWorldRenderer();
	worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
	worldrenderer.pos((double) right, (double) top, (double) this.zLevel).color(r1, g1, b1, a1).endVertex();
	worldrenderer.pos((double) left, (double) top, (double) this.zLevel).color(r2, g2, b2, a2).endVertex();
	worldrenderer.pos((double) left, (double) bottom, (double) this.zLevel).color(r3, g3, b3, a3).endVertex();
	worldrenderer.pos((double) right, (double) bottom, (double) this.zLevel).color(r4, g4, b4, a4).endVertex();
	tessellator.draw();
	GlStateManager.shadeModel(7424);
	GlStateManager.disableBlend();
	GlStateManager.enableAlpha();
	GlStateManager.enableTexture2D();
    }

    /* guicolor
     */
    public void guiColor() {
	final Color c = LunacyForge.instance.clientConfig.getGuiColor();
	final float r = c.getRed() / 255F;
	final float g = c.getGreen() / 255F;
	final float b = c.getBlue() / 255F;
	GlStateManager.color(r, g, b, 1F);
    }

    public void guiColor(float offset) {
	final Color c = LunacyForge.instance.clientConfig.getGuiColor();
	final float r = c.getRed() / 255F + offset;
	final float g = c.getGreen() / 255F + offset;
	final float b = c.getBlue() / 255F + offset;
	GlStateManager.color(r, g, b, 1F);
    }

    public float[] getGuiColor() {
	final Color c = LunacyForge.instance.clientConfig.getGuiColor();
	final float[] rgb = { c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F };
	return rgb;
    }

    public void drawHoverText(String text, int charactersPerLine, int posX, int posY) {
	drawTooltip(StringUtils.getParts(text, charactersPerLine), posX, posY);
    }

    public static List<String> getPartsOld(String string, int partitionSize) {
	List<String> parts = new ArrayList<String>();
	int len = string.length();

	for (int i = 0; i < len; i += partitionSize) {
	    parts.add(string.substring(i, Math.min(len, i + partitionSize)));
	}

	return parts;
    }

    /**
     * best working
     */
    public void drawTexturedRectangle(int x, int y, int u, int v, int width, int height) {
	this.drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, width, height);
    }

    public void drawCustomBackground(int width, int height) {
	// light background
	int alpha = 60;
	this.drawGradientRect(0, 0, width, height, ColorUtils.toRGB(255, 255, 255, alpha), ColorUtils.toRGB(255, 255, 255, alpha));
    }

    /**
     * Draws a List of strings as a tooltip. Every entry is drawn on a seperate line.
     */
    public void drawTooltip(List<String> textLines, int x, int y) {
	final ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());

	if (!textLines.isEmpty()) {
	    GlStateManager.disableRescaleNormal();
	    // RenderHelper.disableStandardItemLighting();
	    // GlStateManager.disableLighting();
	    GlStateManager.disableDepth();
	    int i = 0;

	    for (String s : textLines) {
		int j = fontRenderer.getStringWidth(s);

		if (j > i) {
		    i = j;
		}
	    }

	    int l1 = x + 12;
	    int i2 = y - 12;
	    int k = 8;

	    if (textLines.size() > 1) {
		k += 2 + (textLines.size() - 1) * 10;
	    }

	    if (l1 + i > sc.getScaledWidth()) {
		l1 -= 28 + i;
	    }

	    if (i2 + k + 6 > sc.getScaledHeight()) {
		i2 = sc.getScaledHeight() - k - 6;
	    }
	    this.zLevel = 300.0F;

	    GL11.glColor4f(0.4F, 0.4F, 0.4F, 1.0F);
	    drawDarkContainer(l1 - 3, i2 - 3, i + 6, k + 6);
	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

	    for (int k1 = 0; k1 < textLines.size(); ++k1) {
		String s1 = (String) textLines.get(k1);
		fontRenderer.drawStringWithShadow(s1, (float) l1, (float) i2, -1);

		if (k1 == 0) {
		    i2 += 2;
		}

		i2 += 10;
	    }

	    this.zLevel = 0.0F;
	    // this.itemRender.zLevel = 0.0F;
	    // GlStateManager.enableLighting();
	    GlStateManager.enableDepth();
	    // RenderHelper.enableStandardItemLighting();
	    GlStateManager.enableRescaleNormal();
	}
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

    /* draw a rectangle using width and height*/
    public void drawRectWidthHeight(int x, int y, int width, int height, int color) {
	this.drawRect(x, y, x + width, y + height, color);
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
