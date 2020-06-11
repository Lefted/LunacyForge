/* LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/ */
package me.lefted.lunacyforge.shader;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.awt.Color;

import me.lefted.lunacyforge.implementations.ISetupCameraTransformAccessor;
import me.lefted.lunacyforge.injection.mixins.MixinEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;

/**
 * @author TheSlowly
 */
public abstract class FramebufferShader extends Shader {

    private static Framebuffer framebuffer;
    private Minecraft mc = Minecraft.getMinecraft();

    protected float red, green, blue, alpha = 1F;
    protected float radius = 2F;
    protected float quality = 1F;

    private boolean entityShadows;

    public FramebufferShader(final String fragmentShader) {
	super(fragmentShader);
    }

    public void startDraw(final float partialTicks) {
	GlStateManager.enableAlpha();

	GlStateManager.pushMatrix();
	GlStateManager.pushAttrib();

	framebuffer = setupFrameBuffer(framebuffer);
	framebuffer.framebufferClear();
	framebuffer.bindFramebuffer(true);
	entityShadows = mc.gameSettings.entityShadows;
	mc.gameSettings.entityShadows = false;

	// DEBUG
	final ISetupCameraTransformAccessor entityRenderer = (ISetupCameraTransformAccessor) mc.entityRenderer;
	// mc.entityRenderer.setupCameraTransform(partialTicks, 0);
	entityRenderer.setupCameraTransformAccessor(partialTicks, 0);
    }

    /**
     * @param color   r, g, b, a 0-1F
     * @param radius
     * @param quality
     */
    public void stopDraw(final float[] color, final float radius, final float quality) {
	mc.gameSettings.entityShadows = entityShadows;
	glEnable(GL_BLEND);
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	mc.getFramebuffer().bindFramebuffer(true);

	// red = color.getRed() / 255F;
	// green = color.getGreen() / 255F;
	// blue = color.getBlue() / 255F;
	// alpha = color.getAlpha() / 255F;

	red = color[0];
	green = color[1];
	blue = color[2];
	alpha = color[3];

	this.radius = radius;
	this.quality = quality;

	mc.entityRenderer.disableLightmap();
	RenderHelper.disableStandardItemLighting();

	startShader();
	mc.entityRenderer.setupOverlayRendering();
	drawFramebuffer(framebuffer);
	stopShader();

	mc.entityRenderer.disableLightmap();

	GlStateManager.popMatrix();
	GlStateManager.popAttrib();
    }

    /**
     * @param frameBuffer
     * @return frameBuffer
     * @author TheSlowly
     */
    public Framebuffer setupFrameBuffer(Framebuffer frameBuffer) {
	if (frameBuffer != null)
	    frameBuffer.deleteFramebuffer();

	frameBuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);

	return frameBuffer;
    }

    /**
     * @author TheSlowly
     */
    public void drawFramebuffer(final Framebuffer framebuffer) {
	final ScaledResolution scaledResolution = new ScaledResolution(mc);
	glBindTexture(GL_TEXTURE_2D, framebuffer.framebufferTexture);
	glBegin(GL_QUADS);
	glTexCoord2d(0, 1);
	glVertex2d(0, 0);
	glTexCoord2d(0, 0);
	glVertex2d(0, scaledResolution.getScaledHeight());
	glTexCoord2d(1, 0);
	glVertex2d(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
	glTexCoord2d(1, 1);
	glVertex2d(scaledResolution.getScaledWidth(), 0);
	glEnd();
	glUseProgram(0);
    }
}