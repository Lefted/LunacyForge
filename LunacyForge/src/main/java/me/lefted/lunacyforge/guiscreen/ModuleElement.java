package me.lefted.lunacyforge.guiscreen;

import java.awt.Color;
import java.util.ArrayList;

import me.lefted.lunacyforge.guiapi.Checkbox;
import me.lefted.lunacyforge.guiapi.Element;
import me.lefted.lunacyforge.guiapi.Label;
import me.lefted.lunacyforge.modules.Module;
import me.lefted.lunacyforge.modules.Rectangle;
import me.lefted.lunacyforge.utils.ColorUtils;
import me.lefted.lunacyforge.utils.DrawUtils;
import net.minecraft.client.Minecraft;

/* contains the module: enabled button, label and further settings */
public class ModuleElement extends Element {

    // ATTRIBUTES
    private Module module;
    private Rectangle rect;
    private Checkbox checkboxElement;
    private Label label;
    private int screenWidth;
    private int width; // the width of the container
    private int height; // the height of the container
    private int yIn; // how far the container is offset in y
    private int right; // x + width
    private int spacing; // y spacing between containers
    private ArrayList<Element> elementList;
    private int index;
    private boolean elapsed;

    // CONSTRUCTOR
    public ModuleElement(Module module, int index, ArrayList<Element> elementList) {
	this.module = module;
	this.index = index;
	this.elementList = elementList;
	this.elapsed = false;

	this.screenWidth = Minecraft.getMinecraft().currentScreen.width;
	this.width = screenWidth / 2;
	this.height = 50;
	this.yIn = 70;
	this.posX = screenWidth / 2 - width / 2;
	this.right = this.posX + width;
	this.spacing = 10;
	this.posY = yIn + height * index + spacing * index;

	this.rect = new Rectangle(this.getPosX(), this.getPosY(), width, height, ColorUtils.toRGB(new Color(22, 15, 48)));
	this.rect.setSelection(true);
	this.label = new Label(this.getPosX() + 2, this.getPosY() + 1, module.getName(), 0x20C20E);

	this.checkboxElement = new Checkbox(this.right - 20, this.getPosY(), this.module.isEnabled());
	this.checkboxElement.setVisible(true);

	this.elementList.add(this.rect);
	this.elementList.add(this.label);
	this.elementList.add(this.checkboxElement);
    }

    // METHODS
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
	final DrawUtils utils = DrawUtils.INSTANCE;

	utils.drawHorizontalLine(this.getPosX() + 1, this.getPosX() + utils.getStringWidth(this.module.getName()) + 4, this.getPosY() + 10, ColorUtils.toRGB(
	    new Color(128, 128, 128)));
	utils.drawVerticalLine(this.getPosX() + utils.getStringWidth(this.module.getName()) + 4, this.getPosY() - 2, this.getPosY() + 10, ColorUtils.toRGB(
	    new Color(128, 128, 128)));
    }

    /* fixes a bug preventing multiple elements from (un-)folding at the same time */
    private boolean shouldFold = false;
    private boolean shouldUnfold = false;

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	if (this.isMouseOver(mouseX, mouseY) && mouseButton == 1) {

	    // unfold or fold the element
	    if (this.elapsed) {
		this.shouldFold = true;
	    } else {
		this.shouldUnfold = true;
	    }
	}
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	if (this.isMouseOver(mouseX, mouseY) && mouseButton == 1) {
	    // System.out.println(this.module.getName());
	    // unfold or fold the element
	    if (this.shouldFold) {
		this.fold(100);
		this.shouldFold = false;
	    } else if (this.shouldUnfold) {
		this.unfold(100);
		this.shouldUnfold = false;
	    }
	}
    }

    private void fold(int amount) {
	// move all other elements under this one up
	for (Element element : this.elementList) {
	    if (element instanceof ModuleElement && element != this) {
		ModuleElement moduleElement = (ModuleElement) element;

		if (moduleElement.getIndex() > this.index) {
		    moduleElement.offsetY(-amount);
		}
	    }
	}
	// decrease own height
	this.height -= amount;
	this.rect.setHeight(this.rect.getHeight() - amount);

	this.elapsed = false;
    }

    private void unfold(int amount) {
	// move all other elements under this one down
	for (Element element : this.elementList) {
	    if (element instanceof ModuleElement && element != this) {
		ModuleElement moduleElement = (ModuleElement) element;
		if (moduleElement.getIndex() > this.index) {
		    moduleElement.offsetY(amount);
		}
	    }
	}
	// increase own height
	this.height += amount;
	this.rect.setHeight(this.rect.getHeight() + amount);

	this.elapsed = true;
    }

    /*
     * returns if the mouse is over this element
     */
    public boolean isMouseOver(int mouseX, int mouseY) {
	boolean hovered = mouseX >= this.getPosX() && mouseY >= this.getPosY() && mouseX < this.getPosX() + this.width && mouseY < this.getPosY() + this.height;
	return hovered;
    }

    /*
     * moves the module vertically, used for folding
     */
    public void offsetY(int offsetY) {
	super.setPosY(this.posY + offsetY);
	this.rect.setPosY(this.rect.getPosY() + offsetY);
	this.label.setPosY(this.label.getPosY() + offsetY);
	this.checkboxElement.setPosY(this.checkboxElement.getPosY() + offsetY);
    }

    public int getIndex() {
	return index;
    }

    // public boolean isElapsed() {
    // return elapsed;
    // }
    //
    // public void setElapsed(boolean elapsed) {
    // this.elapsed = elapsed;
    // }
}
