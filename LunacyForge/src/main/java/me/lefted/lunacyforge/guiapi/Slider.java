	package me.lefted.lunacyforge.guiapi;

import java.awt.Color;
import java.util.function.Consumer;

import me.lefted.lunacyforge.utils.ColorUtils;
import me.lefted.lunacyforge.utils.DrawUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Slider extends Element {

    // ATTRIBUTES
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation("textures/gui/widgets.png");
    private static final ResourceLocation SLIDER_TEXTURE = new ResourceLocation("lunacyforge", "slider.png");
    private int min;
    private int max;
    private int step;
    private Integer value;
    private int width;
    private Button button;
    private Consumer<Integer> consumer;
    
    private boolean dragging = false;

    // CONSTRUCTOR
    public Slider(int x, int y, int width, int min, int max, int step, int value) {
	this.posX = x;
	this.posY = y;
	this.min = min;
	this.max = max;
	this.step = step;
	this.value = Integer.valueOf(value);
	this.setVisible(true);
	this.width = width;

	this.button = new Button(this.posX + 50, this.posY, 8, 20, "");
    }

    // METHODS
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	if (!this.isVisible()) {
	    return;
	}
	final DrawUtils utils = DrawUtils.INSTANCE;
	final int lineY = this.getPosY() + 8;

	utils.bindTexture(Slider.SLIDER_TEXTURE);

	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	//
	// GlStateManager.enableBlend();
	// GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	// GlStateManager.blendFunc(770, 771);
	utils.drawTexturedRectangle(this.getPosX(), lineY, 0, 0, 200, 5);

	
//	double pos = 
	
	this.button.draw(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	if (this.button.isMouseOver(mouseX, mouseY)) {
	    this.dragging = true;
	}
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	if (this.dragging) {
	    this.dragging = false;
//	    this.value = Integer.valueOf((int) (this.dragValue / this.step) * this.step);
	}
    }
    
    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceClick) {

    }

    public Consumer<Integer> getConsumer() {
	return consumer;
    }

    public void setConsumer(Consumer<Integer> consumer) {
	this.consumer = consumer;
    }

    public int getMin() {
	return min;
    }

    public void setMin(int min) {
	this.min = min;
	if (this.value < this.min) {
	    this.setValue(this.min);
	}
    }

    public int getMax() {
	return max;
    }

    public void setMax(int max) {
	this.max = max;
	if (this.value > this.max) {
	    this.setValue(this.max);
	}
    }

    public int getStep() {
	return step;
    }

    public void setStep(int step) {
	this.step = step;
    }

    public int getValue() {
	return value;
    }

    public void setValue(int value) {
	this.value = value;
	if (this.consumer != null) {
	    this.consumer.accept(this.value);
	}
    }

    public int getWidth() {
	return width;
    }

    public void setWidth(int width) {
	this.width = width;
    }

    @Override
    public void setPosX(int posX) {
	super.setPosX(posX);

	this.button.setPosX(posX);
    }

    @Override
    public void setPosY(int posY) {
	super.setPosY(posY);

	this.button.setPosY(posY);
    }
}
