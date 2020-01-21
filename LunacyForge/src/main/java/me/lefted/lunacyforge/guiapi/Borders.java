package me.lefted.lunacyforge.guiapi;

public class Borders {

    // ATTRIBUTES
    private boolean maxXDefined = false;
    private boolean minXDefined = false;
    private boolean maxYDefined = false;
    private boolean minYDefined = false;
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;

    // METHODS
    public int getMaxX() {
	return maxX;
    }

    public Borders setMaxX(int maxX) {
	this.maxX = maxX;
	this.maxXDefined = true;
	return this;
    }

    public int getMinX() {
	return minX;
    }

    public Borders setMinX(int minX) {
	this.minX = minX;
	this.minXDefined = true;
	return this;
    }

    public int getMaxY() {
	return maxY;
    }

    public Borders setMaxY(int maxY) {
	this.maxY = maxY;
	this.maxYDefined = true;
	return this;
    }

    public int getMinY() {
	return minY;
    }

    public Borders setMinY(int minY) {
	this.minY = minY;
	this.minYDefined = true;
	return this;
    }

    public boolean isMaxXDefined() {
        return maxXDefined;
    }

    public boolean isMinXDefined() {
        return minXDefined;
    }

    public boolean isMaxYDefined() {
        return maxYDefined;
    }

    public boolean isMinYDefined() {
        return minYDefined;
    }
}
