package me.lefted.lunacyforge.guiapi;

import java.util.function.Consumer;

import me.lefted.lunacyforge.utils.ColorUtils;
import me.lefted.lunacyforge.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class Checkbox extends Element {

    // ATTRIBUTES
    private static final ResourceLocation BUTTON_CHECKED = new ResourceLocation("lunacyforge", "checkbox.png");
    private static final int WIDTH = 16;
    private static final int HEIGHT = 16;
    private boolean checked;
    private Consumer<Boolean> consumer;

    public Checkbox(int x, int y, boolean checked) {
	this.setPosX(x);
	this.setPosY(y);
	this.checked = checked;
    }

    // METHODS
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	if (this.isVisible()) {
	    final DrawUtils utils = DrawUtils.INSTANCE;
	    final boolean hover = this.isMouseOver(mouseX, mouseY);

	    utils.drawRect(this.getPosX(), this.getPosY(), this.getPosX() + 16, this.getPosY() + 16, ColorUtils.toRGB(0, 0, 0, 255));
	    utils.drawRect(this.getPosX() + 1, this.getPosY() + 1, this.getPosX() + 16 - 1, this.getPosY() + 16 - 1, ColorUtils.toRGB(170, 170, 170, 255));
	    utils.drawRect(this.getPosX() + 2, this.getPosY() + 2, this.getPosX() + 16 - 2, this.getPosY() + 16 - 2, ColorUtils.toRGB(hover ? 100 : 120, hover
		? 100
		: 120, hover ? 100 : 120, 255));

	    if (this.checked) {
		utils.bindTexture(BUTTON_CHECKED);
		utils.drawTexture((double) (this.getPosX() + 1), (double) (this.getPosY() - 1), 256.0D, 256.0D, 16.0D, 16.0D, 1.0F);
	    } else {
		drawRect(this.getPosX() + 16 / 3, this.getPosY() + 16 / 3, this.getPosX() + 16 - 16 / 3 + 1, this.getPosY() + 16 - 16 / 3 + 1, ColorUtils.toRGB(
		    100, 100, 100, 255));
		drawRect(this.getPosX() + 16 / 3 - 1, this.getPosY() + 16 / 3 - 1, this.getPosX() + 16 - 16 / 3, this.getPosY() + 16 - 16 / 3, ColorUtils.toRGB(
		    150, 150, 150, 255));
	    }
	}
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
	return mouseX > this.getPosX() && mouseX < this.getPosX() + 16 && mouseY > this.getPosY() && mouseY < this.getPosY() + 16;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	Minecraft mc = Minecraft.getMinecraft();

	if (this.isVisible() && this.isMouseOver(mouseX, mouseY) && mouseButton == 0) {
	    this.playPressSound();
	    if (this.checked) {
		this.checked = false;
		if (this.consumer != null) {
		    this.consumer.accept(this.checked);
		}
	    } else {
		this.checked = true;
		if (this.consumer != null) {
		    this.consumer.accept(this.checked);
		}
	    }
	}
    }

    private void playPressSound() {
	Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public String toString() {
	return "Checkbox [checked=" + this.checked + ", posX=" + this.getPosX() + ", posY=" + this.getPosY() + "]";
    }

    public Consumer<Boolean> getConsumer() {
	return consumer;
    }

    public void setConsumer(Consumer<Boolean> consumer) {
	this.consumer = consumer;
    }
}