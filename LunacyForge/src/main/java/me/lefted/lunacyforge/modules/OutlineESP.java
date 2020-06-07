package me.lefted.lunacyforge.modules;

import java.awt.Color;
import java.util.function.Predicate;

import org.apache.http.util.EntityUtils;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import me.lefted.lunacyforge.clickgui.annotations.CheckboxInfo;
import me.lefted.lunacyforge.clickgui.annotations.ColorInfo;
import me.lefted.lunacyforge.clickgui.annotations.ComboInfo;
import me.lefted.lunacyforge.clickgui.annotations.ContainerInfo;
import me.lefted.lunacyforge.clickgui.annotations.KeybindInfo;
import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;
import me.lefted.lunacyforge.clickgui.annotations.SliderInfo;
import me.lefted.lunacyforge.clickgui.elements.ContainerSlider.NumberType;
import me.lefted.lunacyforge.events.Render2DEvent;
import me.lefted.lunacyforge.implementations.ITimer;
import me.lefted.lunacyforge.shader.FramebufferShader;
import me.lefted.lunacyforge.shader.shaders.OutlineShader;
import me.lefted.lunacyforge.utils.Logger;
import me.lefted.lunacyforge.valuesystem.Value;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;

@ModuleInfo(description = "Makes entities visible", tags = { "PlayerEPS" })
public class OutlineESP extends Module {

    // VALUES
    @ContainerInfo(groupID = 0)
    @CheckboxInfo(description = "Players")
    public Value<Boolean> includePlayers = new Value<Boolean>("includePlayers", true, new String[] { "playerMode", "playerHexceptionWidth",
	    "playerOutlineShaderRadius", "useTeamColor", "playerColor", "useFriendColor", "friendColor", "friendColorToggleKeybind" }, newValue -> newValue);

    @ContainerInfo(groupID = 0)
    // TODO change predicate in something with boolean that affect certain children
    @ComboInfo(description = "Mode", entryNames = { "Vanilla", "Hexception", "OutlineShader" })
    public Value<Integer> playerMode = new Value<Integer>("playerMode", 2/*, new String[] { "playerHexceptionWidth", "playerOutlineShaderRadius" },
									 newValue -> newValue == 2*/);

    @ContainerInfo(groupID = 0)
    @SliderInfo(description = "Line Width", min = 1, max = 10, step = 0.5D, numberType = NumberType.DECIMAL)
    public Value<Float> playerHexceptionWidth = new Value<Float>("playerHexceptionWidth", 3F);

    @ContainerInfo(groupID = 0)
    @SliderInfo(description = "Radius", min = 1, max = 5, step = 0.125D, numberType = NumberType.DECIMAL)
    public Value<Float> playerOutlineShaderRadius = new Value<Float>("playerOutlineShaderRadius", 1.375F);

    @ContainerInfo(groupID = 0)
    @CheckboxInfo(description = "Use Teamcolor")
    public Value<Boolean> useTeamColor = new Value<Boolean>("useTeamColor", true, new String[] { "playerColor" }, newValue -> !newValue);

    @ContainerInfo(groupID = 0)
    @ColorInfo(description = "Color", hasAlpha = true)
    public Value<float[]> playerColor = new Value<float[]>("playerColor", new float[] { 0F, 0.5F, 1F, 1F });

    @ContainerInfo(groupID = 0)
    @CheckboxInfo(description = "Friends have unique color")
    public Value<Boolean> useFriendColor = new Value<Boolean>("useFriendColor", true);

    @ContainerInfo(groupID = 0)
    @ColorInfo(description = "Friend Color", hasAlpha = true)
    public Value<float[]> friendColor = new Value<float[]>("friendColor", new float[] { 0F, 0.5F, 0.0F, 0.5F });

    @ContainerInfo(groupID = 0)
    @KeybindInfo(description = "Toggle Friend Color")
    public Value<Integer> friendColorToggleKeybind = new Value<Integer>("friendColorToggleKeybind", 0);

    // @ComboInfo(description = "Mode", entryNames = { "Vanilla", "Hexception" })
    // public Value<Integer> mode = new Value<Integer>("mode", 1);
    //
    // @ContainerInfo(hoverText = "Only applies for Hexception mode")
    // @SliderInfo(description = "Line width", min = 1, max = 10, step = 1, numberType = NumberType.INTEGER)
    // public Value<Integer> lineWidth = new Value<Integer>("lineWidth", 3);
    //
    // // DEBUG
    // @ColorInfo(description = "Default color", hasAlpha = true)
    // public Value<float[]> outlineColor = new Value<float[]>("outlineColor", new float[] { 0F, 0.5F, 1F, 1F });
    //
    // @CheckboxInfo(description = "Friends have unique color")
    // private Value<Boolean> uniqueFriendColor = new Value<Boolean>("customFriendColor", true, new String[] { "friendColor" }, newValue -> newValue);
    //
    // @ColorInfo(description = "Friend color", hasAlpha = true)
    // public Value<float[]> friendColor = new Value<float[]>("friendColor", new float[] { 0.5686F, 0F, 0.5882F, 1F });

    // CONSTRUCTOR
    public OutlineESP() {
	super("OutlineEPS", Category.RENDER);
    }

    // METHODS
    @EventTarget
    public void onRender2D(final Render2DEvent event) {
	// final String mode = modeValue.get().toLowerCase();
	// final FramebufferShader shader = mode.equalsIgnoreCase("shaderoutline") ? OutlineShader.OUTLINE_SHADER
	// : mode.equalsIgnoreCase("shaderglow") ? GlowShader.GLOW_SHADER : null;
	final FramebufferShader shader = OutlineShader.OUTLINE_SHADER;
	if (shader == null) {
	    return;
	}

	shader.startDraw(event.getPartialTicks());
	// boolean renderNameTags = false;
	try {
	    for (final Entity entity : mc.theWorld.loadedEntityList) {

		// first person fix
		if (entity instanceof EntityPlayerSP && mc.gameSettings.thirdPersonView == 0) {
		    continue;
		}

		final ITimer timer = (ITimer) mc;
		mc.getRenderManager().renderEntityStatic(entity, timer.getITimer().renderPartialTicks, true);
	    }
	} catch (final Exception ex) {
	    Logger.logChatMessage("An error occurred while rendering all entities for shader esp" + ex);
	}
	// renderNameTags = true;
	// final float radius = mode.equalsIgnoreCase("shaderoutline") ? shaderOutlineRadius.get()
	// : mode.equalsIgnoreCase("shaderglow") ? shaderGlowRadius.get() : 1F;
	final float radius = 1.35F;

	// TODO get Color
	shader.stopDraw(Color.WHITE, radius, 1F);
    }

    // METHODS
    @Override
    public void onEnable() {
	EventManager.register(this);
    }

    @Override
    public void onDisable() {
	EventManager.unregister(this);
    }

}
