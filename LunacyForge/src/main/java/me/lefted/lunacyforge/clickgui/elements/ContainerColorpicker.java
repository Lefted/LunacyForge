package me.lefted.lunacyforge.clickgui.elements;

import java.awt.Color;
import java.util.function.Consumer;

import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.clickgui.container.SettingContainer;
import me.lefted.lunacyforge.clickgui.elements.api.Element;
import me.lefted.lunacyforge.clickgui.screens.SettingsScreen;
import me.lefted.lunacyforge.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

/**
 * Element to chosse colors from <br>
 * Credits: ZonixClient
 * 
 * @author Lefted <br>
 */
public class ContainerColorpicker extends Element {

    // CONSTANTS
    public static final int PREVIEW_WIDTH = 48;
    public static final int PREVIEW_HEIGHT = 12;
    public static final ResourceLocation PRESS_SOUND = new ResourceLocation("gui.button.press");

    // ATTRIBUTES
    private int originalHeight;
    private boolean opened = false;
    private boolean hasAlpha = true;
    private SettingsScreen screen;
    private SettingContainer parent; // needed to extend the container when this is opened
    private Spectrum spectrum;
    private HueSlider hueSlider;
    private AlphaSlider alphaSlider;
    private Consumer<float[]> hsbaConsumer;
    private Consumer<Color> colorObjConsumer;
    private Consumer<float[]> rgbaConsumer;

    /**
     * hue 0-1, saturation 0-1, brightness 0-1, alpha 0-1
     */
    private float[] hsba;

    // CONSTRUCTOR
    public ContainerColorpicker(SettingsScreen screen, SettingContainer parent, Color color) {
	setVisible(true);

	this.screen = screen;
	this.parent = parent;
	final float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
	this.hsba = new float[] { hsb[0], hsb[1], hsb[2], color.getAlpha() / 255F };
	// this.hsba = new int[] { (int) (hsb[0] * 255), (int) (hsb[1] * 255), (int) (hsb[2] * 255), color.getAlpha() };
	spectrum = new Spectrum(screen, this);
	hueSlider = new HueSlider(screen, this);
	alphaSlider = new AlphaSlider(screen, this);
    }

    // CONSTRUCTOR
    public ContainerColorpicker(SettingsScreen screen, SettingContainer parent, float[] hsba) {
	setVisible(true);

	this.screen = screen;
	this.parent = parent;
	this.hsba = hsba;
	spectrum = new Spectrum(screen, this);
	hueSlider = new HueSlider(screen, this);
	alphaSlider = new AlphaSlider(screen, this);
    }

    // CONSTRUCTOR
    public ContainerColorpicker(SettingsScreen screen, SettingContainer parent, float[] rgba, Object leaveThisNull) {
	setVisible(true);

	this.screen = screen;
	this.parent = parent;
	final float[] hsb = Color.RGBtoHSB((int) (rgba[0] * 255), (int) (rgba[1] * 255), (int) (rgba[2] * 255), null);
	this.hsba = new float[] { hsb[0], hsb[1], hsb[2], rgba[3] };
	spectrum = new Spectrum(screen, this);
	hueSlider = new HueSlider(screen, this);
	alphaSlider = new AlphaSlider(screen, this);
    }

    // METHODS
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	final Minecraft mc = Minecraft.getMinecraft();

	if (this.isVisible()) {

	    if (opened) {
		spectrum.draw(mouseX, mouseY, partialTicks);
		hueSlider.draw(mouseX, mouseY, partialTicks);
		if (hasAlpha) {
		    alphaSlider.draw(mouseX, mouseY, partialTicks);
		}
	    }

	    drawPreview(mouseX, mouseY);

	    // reset color mask
	    GL11.glColor4f(1F, 1F, 1F, 1F);
	}
    }

    /**
     * Draw the preview color
     * 
     * @param mouseX
     * @param mouseY
     */
    private void drawPreview(int mouseX, int mouseY) {
	final Color rgb = Color.getHSBColor(hsba[0], hsba[1], hsba[2]);
	final Color rgba = new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), (int) (hsba[3] * 255));

	GlStateManager.disableBlend();

	// outline
	Tessellator tessellator = Tessellator.getInstance();
	WorldRenderer wr = tessellator.getWorldRenderer();
	DrawUtils.INSTANCE.drawRectWidthHeight(posX + 90 - 1, posY - 1, PREVIEW_WIDTH + 2, PREVIEW_HEIGHT + 2, 0xff9b9b9b);

	// checkerboard pattern
	boolean gray = false;
	for (int y = 0; y < 12; y += 4) {
	    for (int x = 0; x < 48; x += 4) {

		if (gray) {
		    DrawUtils.INSTANCE.drawRectWidthHeight(posX + 90 + x, posY + y, 4, 4, -7303024);
		} else {
		    DrawUtils.INSTANCE.drawRectWidthHeight(posX + 90 + x, posY + y, 4, 4, -1);
		}
		gray = !gray;
	    }
	    if (y % 2 == 0) {
		gray = !gray;
	    }
	}

	GlStateManager.enableBlend();
	// color
	DrawUtils.INSTANCE.drawRectWidthHeight(posX + 90, posY, PREVIEW_WIDTH, PREVIEW_HEIGHT, rgba.getRGB());

	GlStateManager.disableBlend();
    }

    private boolean isMouseOverPreview(int mouseX, int mouseY) {
	return mouseX >= posX + 90 - 1 && mouseX <= posX + 90 - 1 + PREVIEW_WIDTH + 2 && mouseY >= posY - 1 && mouseY <= posY - 1 + PREVIEW_HEIGHT + 2;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	if (opened) {
	    spectrum.mouseClicked(mouseX, mouseY, mouseButton);
	    hueSlider.mouseClicked(mouseX, mouseY, mouseButton);
	    if (hasAlpha) {
		alphaSlider.mouseClicked(mouseX, mouseY, mouseButton);
	    }
	}

	if (isMouseOverPreview(mouseX, mouseY)) {
	    if (opened) {
		close();
	    } else {
		open();
	    }
	}
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick) {
	if (opened) {
	    spectrum.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceClick);
	    hueSlider.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceClick);
	    if (hasAlpha) {
		alphaSlider.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceClick);
	    }
	}
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	if (opened) {
	    spectrum.mouseReleased(mouseX, mouseY, mouseButton);
	    hueSlider.mouseReleased(mouseX, mouseY, mouseButton);
	    if (hasAlpha) {
		alphaSlider.mouseReleased(mouseX, mouseY, mouseButton);
	    }
	}
    }

    public void playPressSound(SoundHandler soundHandler) {
	soundHandler.playSound(PositionedSoundRecord.create(PRESS_SOUND, 1.0F));
    }

    private void open() {
	playPressSound(Minecraft.getMinecraft().getSoundHandler());
	originalHeight = parent.getHeight();

	final int extraNeeded = 40;
	parent.setHeight(parent.getHeight() + extraNeeded);
	if (parent.getGroup() != null) {
	    parent.getGroup().updateHeight();
	}

	opened = true;
	screen.setPanelBorders();
    }

    private void close() {
	playPressSound(Minecraft.getMinecraft().getSoundHandler());
	opened = false;
	parent.setHeight(originalHeight);
	if (parent.getGroup() != null) {
	    parent.getGroup().updateHeight();
	}
	screen.setPanelBorders();
	screen.scrollVerticalByAmount(0);
    }

    @Override
    public void setPosX(int posX) {
	// pass call
	super.setPosX(posX);

	// apply to gradient
	spectrum.setPosX(posX);
	// apply to hueslider
	hueSlider.setPosX(posX + 5 + spectrum.WIDTH);
	// apply to alphaslider
	alphaSlider.setPosX(hueSlider.getPosX() + hueSlider.WIDTH + 5);
    }

    @Override
    public void setPosY(int posY) {
	// pass call
	super.setPosY(posY);

	// apply to gradient
	spectrum.setPosY(posY);
	// apply to hueslider
	hueSlider.setPosY(posY);
	// apply to alphaslider
	alphaSlider.setPosY(posY);
    }

    public int getWidth() {
	return spectrum.WIDTH + 5 + hueSlider.WIDTH + 5 + alphaSlider.WIDTH;
    }

    /**
     * @return hue 0-1, saturation 0-1, brightness 0-1, alpha 0-1
     */
    public float[] getHSBA() {
	return hsba;
    }

    /**
     * @param hsba hue 0-1, saturation 0-1, brightness 0-1, alpha 0-1
     */
    public void setHSBA(float[] hsba) {
	this.hsba = hsba;

	if (hsbaConsumer != null) {
	    hsbaConsumer.accept(this.hsba);
	}

	if (colorObjConsumer != null) {
	    final Color rgb = Color.getHSBColor(hsba[0], hsba[1], hsba[2]);
	    final Color rgba = new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), (int) (hsba[3] * 255));

	    colorObjConsumer.accept(rgba);
	}
	if (rgbaConsumer != null) {
	    final int rgb = Color.HSBtoRGB(this.hsba[0], this.hsba[1], this.hsba[2]);
	    final float red = ((rgb >> 16) & 0xFF) / 255F;
	    final float green = ((rgb >> 8) & 0xFF) / 255F;
	    final float blue = (rgb & 0xFF) / 255F;
	    rgbaConsumer.accept(new float[] { red, green, blue, this.hsba[3] });
	}
    }

    public void setHSBAConsumer(Consumer<float[]> consumer) {
	this.hsbaConsumer = consumer;
    }

    public Consumer<float[]> getHSBAConsumer() {
	return this.hsbaConsumer;
    }

    public void setColorObjConsumer(Consumer<Color> consumer) {
	this.colorObjConsumer = consumer;
    }

    public Consumer<Color> getColorObjConsumer() {
	return this.colorObjConsumer;
    }

    public void setRGBAConsumer(Consumer<float[]> consumer) {
	this.rgbaConsumer = consumer;
    }

    public Consumer<float[]> getRGBAConsumer() {
	return this.rgbaConsumer;
    }

    // USETHIS to disable alpha control
    public void setHasAlpha(boolean hasAlpha) {
	this.hasAlpha = hasAlpha;
    }

    public boolean getHasAlpha() {
	return this.hasAlpha;
    }

    // spectrum
    private class Spectrum extends Element {

	// CONSTANTS
	public static final int WIDTH = 50;
	public static final int HEIGHT = WIDTH;

	// ATTRIBUTES
	// private Integer color;
	private boolean dragging = false;
	private SettingsScreen screen;
	private ContainerColorpicker parent;

	// CONSTRUCTOR
	public Spectrum(SettingsScreen screen, ContainerColorpicker parent) {
	    // this.color = color;
	    this.parent = parent;
	    this.screen = screen;
	}

	// METHODS
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
	    GlStateManager.disableTexture2D();
	    GlStateManager.enableBlend();
	    GlStateManager.disableAlpha();
	    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

	    Tessellator tessellator = Tessellator.getInstance();
	    WorldRenderer wr = tessellator.getWorldRenderer();

	    // outline
	    int outlineColor = 0xff9b9b9b;
	    float f = (float) (outlineColor >> 16 & 255) / 255.0F;
	    float f1 = (float) (outlineColor >> 8 & 255) / 255.0F;
	    float f2 = (float) (outlineColor & 255) / 255.0F;
	    float f3 = (float) (outlineColor >> 24 & 255) / 255.0F;

	    wr.begin(7, DefaultVertexFormats.POSITION_COLOR);
	    wr.pos((double) posX - 1, (double) posY + HEIGHT + 1, 0.0D).color(f, f1, f2, f3).endVertex();
	    wr.pos((double) posX + WIDTH + 1, (double) posY + HEIGHT + 1, 0.0D).color(f, f1, f2, f3).endVertex();
	    wr.pos((double) posX + WIDTH + 1, (double) posY - 1, 0.0D).color(f, f1, f2, f3).endVertex();
	    wr.pos((double) posX - 1, (double) posY - 1, 0.0D).color(f, f1, f2, f3).endVertex();
	    tessellator.draw();

	    // spectrum
	    for (int x = 0; x < WIDTH; x++) {
		for (int y = 0; y < HEIGHT; y++) {
		    float saturation = (float) x / WIDTH;
		    float brightness = 1.0F - (float) y / HEIGHT;

		    int colorAtXY = Color.HSBtoRGB(parent.getHSBA()[0], saturation, brightness);

		    // one pixel at a time
		    wr.begin(7, DefaultVertexFormats.POSITION);
		    GL11.glColor4f((colorAtXY >> 16 & 0xFF) / 255.0F, (colorAtXY >> 8 & 0xFF) / 255.0F, (colorAtXY & 0xFF) / 255.0F, 1.0F);

		    wr.pos((posX + x), (posY + y + 1.0F), 0.0D).endVertex();
		    wr.pos((posX + x + 1.0F), (posY + y + 1.0F), 0.0D).endVertex();
		    wr.pos((posX + x + 1.0F), (posY + y), 0.0D).endVertex();
		    wr.pos((posX + x), (posY + y), 0.0D).endVertex();
		    tessellator.draw();
		    GL11.glColor4f(1F, 1F, 1F, 1F);
		}
	    }

	    GlStateManager.disableBlend();
	    GlStateManager.enableAlpha();
	    GlStateManager.enableTexture2D();
	}

	@Override
	public boolean isMouseOver(int mouseX, int mouseY) {
	    return mouseX >= posX && mouseX <= posX + WIDTH && mouseY >= posY && mouseY <= posY + HEIGHT;
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	    if (mouseButton == 0) {
		if (isMouseOver(mouseX, mouseY)) {
		    dragging = true;
		    screen.setDisabledScrolling(true);
		    updateValue(mouseX, mouseY);
		    playPressSound(Minecraft.getMinecraft().getSoundHandler());
		}
	    }
	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick) {
	    updateValue(mouseX, mouseY);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	    if (mouseButton == 0) {
		if (dragging) {
		    updateValue(mouseX, mouseY);
		    dragging = false;
		    screen.setDisabledScrolling(false);
		}
	    }
	}

	private void updateValue(int mouseX, int mouseY) {
	    if (dragging) {
		int yDiff = mouseY - this.posY;
		int xDiff = mouseX - this.posX;
		float newBrightnessValue = 1 - (int) Math.max(Math.min((float) yDiff / HEIGHT * 255, 255), 0) / 255F;
		// somehow without this if its 0, it will be white
		if (newBrightnessValue == 0F) {
		    newBrightnessValue = 0.01F;
		}
		float newSaturationValue = (int) Math.max(Math.min((float) xDiff / HEIGHT * 255, 255), 0) / 255F;
		float[] hsbaold = parent.getHSBA();
		parent.setHSBA(new float[] { hsbaold[0], newSaturationValue, newBrightnessValue, hsbaold[3] });
	    }
	}
    }

    // hue slider
    private class HueSlider extends Element {

	// CONSTANTS
	public static final int WIDTH = 8;
	public static final int HEIGHT = 50;

	// ATTRIBUTES
	private boolean dragging;
	private SettingsScreen screen;
	private ContainerColorpicker parent;

	// CONSTRUCTOR
	public HueSlider(SettingsScreen screen, ContainerColorpicker parent) {
	    this.screen = screen;
	    this.parent = parent;
	}

	// METHODS
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
	    GlStateManager.disableTexture2D();
	    GlStateManager.enableBlend();
	    GlStateManager.disableAlpha();
	    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

	    Tessellator tessellator = Tessellator.getInstance();
	    WorldRenderer wr = tessellator.getWorldRenderer();

	    // outline
	    int outlineColor = 0xff9b9b9b;
	    float f3 = (float) (outlineColor >> 24 & 255) / 255.0F;
	    float f = (float) (outlineColor >> 16 & 255) / 255.0F;
	    float f1 = (float) (outlineColor >> 8 & 255) / 255.0F;
	    float f2 = (float) (outlineColor & 255) / 255.0F;

	    wr.begin(7, DefaultVertexFormats.POSITION_COLOR);
	    wr.pos((double) posX - 1, (double) posY + HEIGHT + 1, 0.0D).color(f, f1, f2, f3).endVertex();
	    wr.pos((double) posX + WIDTH + 1, (double) posY + HEIGHT + 1, 0.0D).color(f, f1, f2, f3).endVertex();
	    wr.pos((double) posX + WIDTH + 1, (double) posY - 1, 0.0D).color(f, f1, f2, f3).endVertex();
	    wr.pos((double) posX - 1, (double) posY - 1, 0.0D).color(f, f1, f2, f3).endVertex();
	    tessellator.draw();

	    // hue
	    for (int j = 0; j < HEIGHT; j++) {
		// Draw hue
		int rgb = Color.HSBtoRGB((float) j / HEIGHT, 1.0F, 1.0F);
		DrawUtils.INSTANCE.drawRectWidthHeight(posX, posY + j, WIDTH, 1, rgb);
	    }

	    // slider thumb
	    float sliderY = -1.0F + HEIGHT * parent.getHSBA()[0];
	    DrawUtils.INSTANCE.drawRectWidthHeight(posX - 1, posY + (int) sliderY, 10, 2, 0xff000000);

	    GlStateManager.disableBlend();
	    GlStateManager.enableAlpha();
	    GlStateManager.enableTexture2D();

	    // reset color mask
	    GL11.glColor4f(1F, 1F, 1F, 1F);
	}

	@Override
	public boolean isMouseOver(int mouseX, int mouseY) {
	    return mouseX >= posX && mouseX <= posX + WIDTH && mouseY >= posY && mouseY <= posY + HEIGHT;
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	    if (mouseButton == 0) {
		if (isMouseOver(mouseX, mouseY)) {
		    dragging = true;
		    screen.setDisabledScrolling(true);
		    updateValue(mouseX, mouseY);
		    playPressSound(Minecraft.getMinecraft().getSoundHandler());
		}
	    }
	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick) {
	    updateValue(mouseX, mouseY);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	    if (mouseButton == 0) {
		if (dragging) {
		    updateValue(mouseX, mouseY);
		    dragging = false;
		    screen.setDisabledScrolling(false);
		}
	    }
	}

	private void updateValue(int mouseX, int mouseY) {
	    if (dragging) {
		int yDiff = mouseY - this.posY;
		float newValue = (int) Math.max(Math.min((float) yDiff / HEIGHT * 255, 255), 0) / 255F;
		float[] hsbaold = parent.getHSBA();
		parent.setHSBA(new float[] { newValue, hsbaold[1], hsbaold[2], hsbaold[3] });
	    }
	}

    }

    // alpha slider
    private class AlphaSlider extends Element {
	// CONSTANTS
	public static final int WIDTH = 8;
	public static final int HEIGHT = 50;

	// ATTRIBUTES
	private boolean dragging;
	private SettingsScreen screen;
	private ContainerColorpicker parent;

	// CONSTRUCTOR
	public AlphaSlider(SettingsScreen screen, ContainerColorpicker parent) {
	    this.screen = screen;
	    this.parent = parent;
	}

	// METHODS
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
	    GlStateManager.disableTexture2D();
	    GlStateManager.enableBlend();
	    GlStateManager.disableAlpha();
	    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

	    Tessellator tessellator = Tessellator.getInstance();
	    WorldRenderer wr = tessellator.getWorldRenderer();

	    // outline
	    int outlineColor = 0xff9b9b9b;
	    float f3 = (float) (outlineColor >> 24 & 255) / 255.0F;
	    float f = (float) (outlineColor >> 16 & 255) / 255.0F;
	    float f1 = (float) (outlineColor >> 8 & 255) / 255.0F;
	    float f2 = (float) (outlineColor & 255) / 255.0F;

	    wr.begin(7, DefaultVertexFormats.POSITION_COLOR);
	    wr.pos((double) posX - 1, (double) posY + HEIGHT + 1, 0.0D).color(f, f1, f2, f3).endVertex();
	    wr.pos((double) posX + WIDTH + 1, (double) posY + HEIGHT + 1, 0.0D).color(f, f1, f2, f3).endVertex();
	    wr.pos((double) posX + WIDTH + 1, (double) posY - 1, 0.0D).color(f, f1, f2, f3).endVertex();
	    wr.pos((double) posX - 1, (double) posY - 1, 0.0D).color(f, f1, f2, f3).endVertex();
	    tessellator.draw();

	    // checkerboard pattern for alpha
	    boolean left = true;
	    for (int j = 2; j < HEIGHT; j += 4) {
		if (!left) {
		    DrawUtils.INSTANCE.drawRectWidthHeight(posX, posY + j, 4, 4, -1);
		    DrawUtils.INSTANCE.drawRectWidthHeight(posX + 4, posY + j, 4, 4, -7303024);
		    if (j < HEIGHT - 4.0F) {
			DrawUtils.INSTANCE.drawRectWidthHeight(posX, posY + j + 4, 4, 4, -7303024);
			DrawUtils.INSTANCE.drawRectWidthHeight(posX + 4, posY + j + 4, 4, 4, -1);
		    }
		}

		left = !left;
	    }

	    // alpha gradient
	    int c = Color.HSBtoRGB(parent.getHSBA()[0], 1F, 1F);
	    for (int j = 0; j < HEIGHT; j++) {
		// draw one pixel at a time
		int rgb = (new Color(c >> 16 & 0xFF, c >> 8 & 0xFF, c & 0xFF, Math.round(255.0F - (float) j / HEIGHT * 255.0F))).getRGB();
		DrawUtils.INSTANCE.drawRectWidthHeight(posX, posY + j, 8, 1, rgb);
	    }

	    // slider thumb
	    float sliderY = HEIGHT - (1 + HEIGHT * getHSBA()[3]);
	    DrawUtils.INSTANCE.drawRectWidthHeight(posX - 1, posY + (int) sliderY, 10, 2, 0xff000000);

	    GlStateManager.disableBlend();
	    GlStateManager.enableAlpha();
	    GlStateManager.enableTexture2D();

	    // reset color mask
	    GL11.glColor4f(1F, 1F, 1F, 1F);
	}

	@Override
	public boolean isMouseOver(int mouseX, int mouseY) {
	    return mouseX >= posX && mouseX <= posX + WIDTH && mouseY >= posY && mouseY <= posY + HEIGHT;
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	    if (mouseButton == 0) {
		if (isMouseOver(mouseX, mouseY)) {
		    dragging = true;
		    screen.setDisabledScrolling(true);
		    updateValue(mouseX, mouseY);
		    playPressSound(Minecraft.getMinecraft().getSoundHandler());
		}
	    }
	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick) {
	    updateValue(mouseX, mouseY);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	    if (mouseButton == 0) {
		if (dragging) {
		    updateValue(mouseX, mouseY);
		    dragging = false;
		    screen.setDisabledScrolling(false);
		}
	    }
	}

	private void updateValue(int mouseX, int mouseY) {
	    if (dragging) {
		int yDiff = mouseY - this.posY;
		float newValue = 1 - (int) Math.max(Math.min((float) yDiff / HEIGHT * 255, 255), 0) / 255F;
		float[] hsbaold = parent.getHSBA();
		parent.setHSBA(new float[] { hsbaold[0], hsbaold[1], hsbaold[2], newValue });
	    }
	}
    }
}
