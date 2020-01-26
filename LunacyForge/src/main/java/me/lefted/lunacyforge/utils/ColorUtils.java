package me.lefted.lunacyforge.utils;

import java.awt.Color;

public class ColorUtils {

    // METHODS
    public static Color rainbowEffect(long offset, float fade) {

	float hue = (float) (System.nanoTime() + offset) / 1.0E10F % 1.0F;
	long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.0F)).intValue()), 16);
	Color c = new Color((int) color);

	return new Color(c.getRed() / 255.0F * fade, c.getGreen() / 255.0F * fade, c.getBlue() / 255.0F * fade, c.getAlpha() / 255.0F);
    }

    public static int toRGB(int r, int g, int b, int a) {
	return (a & 255) << 24 | (r & 255) << 16 | (g & 255) << 8 | (b & 255) << 0;
    }

    public static int toRGB(Color color) {
	return toRGB(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
}
