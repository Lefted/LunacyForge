package me.lefted.lunacyforge.shader.shaders;

import org.lwjgl.opengl.GL20;

import me.lefted.lunacyforge.shader.FramebufferShader;
import net.minecraft.client.Minecraft;

public final class FriendOutlineShader extends FramebufferShader {

    public static final FriendOutlineShader FRIEND_OUTLINE_SHADER = new FriendOutlineShader();
    private static final Minecraft mc = Minecraft.getMinecraft();

    public FriendOutlineShader() {
	super("outline.frag");
    }

    @Override
    public void setupUniforms() {
	setupUniform("texture");
	setupUniform("texelSize");
	setupUniform("color");
	setupUniform("divider");
	setupUniform("radius");
	setupUniform("maxSample");
    }

    @Override
    public void updateUniforms() {
	GL20.glUniform1i(getUniform("texture"), 0);
	GL20.glUniform2f(getUniform("texelSize"), 1F / mc.displayWidth * (radius * quality), 1F / mc.displayHeight * (radius * quality));
	GL20.glUniform4f(getUniform("color"), red, green, blue, alpha);
	GL20.glUniform1f(getUniform("radius"), radius);
    }
}