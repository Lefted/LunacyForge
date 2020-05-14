package me.lefted.lunacyforge.clickgui.elements;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;

import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.clickgui.container.ContainerButton;
import me.lefted.lunacyforge.guiapi.Element;
import me.lefted.lunacyforge.utils.DrawUtils;
import me.lefted.lunacyforge.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ContainerSlider extends Element {

    // CONSTANTS
    public static final int WIDTH = 128;
    public static final int HEIGHT = 10;
    public static final ResourceLocation SLIDER_THUMB = new ResourceLocation("lunacyforge", "slider_thumb.png");
    public static final ResourceLocation SLIDER_TRACK = new ResourceLocation("lunacyforge", "slider_track.png");

    // ATTRIBUTES
    private int minValue = 0;
    private int maxValue = 10;
    private double stepValue = 1; // the steps you change the value
    private boolean hovered;
    private boolean dragging;

    private double value;
    private NumberType numberType;
    private Consumer<Double> consumer;

    // CONSTRUCTOR
    /**
     * @param numberType
     * @param minValue
     * @param maxValue
     * @param stepValue  Should be >= 1 if numberType is INTEGER
     */
    public ContainerSlider(NumberType numberType, int minValue, int maxValue, double stepValue) {
	this.numberType = numberType;
	this.minValue = minValue;
	this.maxValue = maxValue;
	this.stepValue = stepValue;
    }

    // METHODS
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	if (mouseButton == 0) {
	    // updateHovered(mouseX, mouseY);

	    if (isMouseOver(mouseX, mouseY)) {
		dragging = true;
		updateValue(mouseX, mouseY);
		playPressSound(Minecraft.getMinecraft().getSoundHandler());
	    }
	}
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
	return mouseX >= this.posX && mouseY >= this.posY && mouseX <= this.posX + WIDTH && mouseY <= this.posY + HEIGHT;
    }

    private void updateValue(int mouseX, int mouseY) {
	if (dragging) {
	    double oldValue = value;
	    double newValue = Math.max(Math.min((mouseX - this.posX) / (double) WIDTH * (maxValue - minValue) + minValue, maxValue), minValue);

	    // this makes the value snap according to the stepValue
	    newValue = closestMultiple(newValue, stepValue);

	    // callback consumer
	    value = newValue;
	    if (oldValue != newValue && consumer != null) {
		consumer.accept(newValue);
	    }
	}
    }

    // function to find the number closest to n
    // and divisible by m
    private double closestNumber(double n, double m) {
	// find the quotient
	double q = n / m;

	// 1st possible closest number
	double n1 = m * q;

	// 2nd possible closest number
	double n2 = (n * m) > 0 ? (m * (q + 1)) : (m * (q - 1));

	// if true, then n1 is the required closest number
	if (Math.abs(n - n1) < Math.abs(n - n2))
	    return n1;

	// else n2 is the required closest number
	return n2;
    }

    // Function to calculate the smallest multiple
    private double closestMultiple(double n, double x) {
	if (x > n) {
	    if (n > x / 2) {
		return x;
	    }
	    return 0;
	}
	n = n + x / 2;
	n = n - (n % x);
	return n;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	if (mouseButton == 0) {
	    // updateHovered(mouseX, mouseY);

	    if (dragging) {
		updateValue(mouseX, mouseY);
		dragging = false;
	    }
	}
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick) {
	// updateHovered(mouseX, mouseY);
	updateValue(mouseX, mouseY);
    }

    // TODO use textures
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	final DrawUtils utils = DrawUtils.INSTANCE;

	// track
	int radius = 4;
	// thumb
	int thumbWidth = 10;
	double thumbPos = (value - minValue) / (maxValue - minValue) * (WIDTH - thumbWidth);

	// utils.drawRectWidthHeight(posX, posY, WIDTH, HEIGHT, (isMouseOver(mouseX, mouseY) || dragging) ? 0xFF515151 : 0xFF404040);

	GlStateManager.enableBlend();

	utils.bindTexture(SLIDER_TRACK);
	// left rounding
	utils.drawModalRectWithCustomSizedTexture(posX + thumbWidth / 2, posY + 4, 0, 0, radius, HEIGHT / 3, WIDTH / 3, HEIGHT / 3);
	// right rounding
	utils.drawModalRectWithCustomSizedTexture(posX + WIDTH - radius - thumbWidth / 2, posY + 4, WIDTH - radius, 0, radius, HEIGHT / 3, WIDTH / 3 + 0.65F,
	    HEIGHT / 3);
	// mid
	utils.drawModalRectWithCustomSizedTexture(posX + thumbWidth / 2 + radius, posY + 4, radius + 3, 0, WIDTH - 2 * radius - thumbWidth, HEIGHT / 3, WIDTH
	    - 2 * radius + 4, HEIGHT / 3);

	boolean flag = mouseX <= posX + thumbPos + thumbWidth && mouseX >= posX + thumbPos && mouseY >= posY && mouseY <= posY + HEIGHT;

	utils.bindTexture(SLIDER_THUMB);

	if (flag || dragging) {
	    final float[] rgb = utils.getGuiColor();
	    final float colorOffset = 0.1F;
	    GL11.glColor4f(rgb[0] + colorOffset, rgb[1] + colorOffset, rgb[2] + colorOffset, 1F);
	} else {
	    utils.guiColor();
	}

	utils.drawTexturedRectangle((int) (posX + thumbPos), posY, 0, 0, thumbWidth, HEIGHT);
	GL11.glColor4f(1F, 1F, 1F, 1F);

	GlStateManager.disableBlend();
    }

    private void playPressSound(SoundHandler soundHandler) {
	soundHandler.playSound(PositionedSoundRecord.create(ContainerButton.PRESS_SOUND, 1.0F));
    }

    public String getValueString() {
	return numberType.getFormatter().apply(value);
    }

    public double getValue() {
	return value;
    }

    public void setValue(double value) {
	this.value = value;
    }

    public void setConsumer(Consumer<Double> consumer) {
	this.consumer = consumer;
    }

    // INNER CLASS
    public enum NumberType {
	PERCENT(number -> String.format(Locale.ENGLISH, "%.1f%%", number.floatValue())), DECIMAL(number -> String.format(Locale.ENGLISH, "%." + StringUtils
	    .getNotNullDecimalCount(number + "") + "f", number.floatValue())), INTEGER(number -> Long.toString(number.longValue()));

	private Function<Number, String> formatter;

	NumberType(Function<Number, String> formatter) {
	    this.formatter = formatter;
	}

	public Function<Number, String> getFormatter() {
	    return formatter;
	}
    }
}
