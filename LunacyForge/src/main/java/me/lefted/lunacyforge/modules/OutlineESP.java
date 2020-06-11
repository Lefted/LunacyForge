package me.lefted.lunacyforge.modules;

import java.awt.Color;

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
import me.lefted.lunacyforge.valuesystem.NodeTreeManager;
import me.lefted.lunacyforge.valuesystem.Value;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;

@ModuleInfo(description = "Makes entities visible", tags = { "PlayerEPS" })
public class OutlineESP extends Module {

    // VALUES
    @ContainerInfo(groupID = 0)
    @CheckboxInfo(description = "Players")
    public Value<Boolean> includePlayers = new Value<Boolean>(this, "includePlayers", true, new String[] { "playerMode", "useTeamColor", "useFriendColor",
	    "friendColorToggleKeybind" }, newValue -> Value.createHandler(4, newValue));

    @ContainerInfo(groupID = 0)
    // TODO change predicate in something with boolean that affect certain children
    @ComboInfo(description = "Mode", entryNames = { "Vanilla", "Hexception", "OutlineShader" })
    public Value<Integer> playerMode = new Value<Integer>(this, "playerMode", 2, new String[] { "playerHexceptionWidth", "playerOutlineShaderRadius" },
	newValue -> {
	    switch (newValue) {
	    case 0:
		return new boolean[] { false, false };
	    case 1:
		return new boolean[] { true, false };
	    case 2:
		return new boolean[] { false, true };
	    default:
		return null;
	    }
	});

    @ContainerInfo(groupID = 0)
    @SliderInfo(description = "Line Width", min = 1, max = 10, step = 0.5D, numberType = NumberType.DECIMAL)
    public Value<Float> playerHexceptionWidth = new Value<Float>(this, "playerHexceptionWidth", 3F);

    @ContainerInfo(groupID = 0)
    @SliderInfo(description = "Radius", min = 1, max = 5, step = 0.125D, numberType = NumberType.DECIMAL)
    public Value<Float> playerOutlineShaderRadius = new Value<Float>(this, "playerOutlineShaderRadius", 1.375F);

    @ContainerInfo(groupID = 0)
    @CheckboxInfo(description = "Use Teamcolor")
    public Value<Boolean> useTeamColor = new Value<Boolean>(this, "useTeamColor", true, new String[] { "playerColor" }, newValue -> Value.createHandler(1,
	!newValue));

    @ContainerInfo(groupID = 0)
    @ColorInfo(description = "Color", hasAlpha = true)
    public Value<float[]> playerColor = new Value<float[]>(this, "playerColor", new float[] { 0F, 0.5F, 1F, 1F });

    @ContainerInfo(groupID = 0)
    @CheckboxInfo(description = "Friends have unique color")
    public Value<Boolean> useFriendColor = new Value<Boolean>(this, "useFriendColor", true, new String[] { "friendColor" }, newValue -> Value.createHandler(1,
	newValue));

    @ContainerInfo(groupID = 0)
    @ColorInfo(description = "Friend Color", hasAlpha = true)
    public Value<float[]> friendColor = new Value<float[]>(this, "friendColor", new float[] { 0F, 0.5F, 0.0F, 0.5F });

    @ContainerInfo(groupID = 0)
    @KeybindInfo(description = "Toggle Friend Color")
    public Value<Integer> friendColorToggleKeybind = new Value<Integer>(this, "friendColorToggleKeybind", 0);

    @ContainerInfo(groupID = 1)
    @CheckboxInfo(description = "Hostiles")
    public Value<Boolean> includeHostiles = new Value<Boolean>(this, "includeHostiles", false, new String[] { "hostileMode", "hostileColor" }, newValue -> Value
	.createHandler(2, newValue));

    @ContainerInfo(groupID = 1)
    @ComboInfo(description = "Mode", entryNames = { "Vanilla", "Hexception", "OutlineShader" })
    public Value<Integer> hostileMode = new Value<Integer>(this, "hostileMode", 1, new String[] { "hostileHexceptionWidth", "hostileOutlineShaderRadius" },
	newValue -> {
	    switch (newValue) {
	    case 0:
		return new boolean[] { false, false };
	    case 1:
		return new boolean[] { true, false };
	    case 2:
		return new boolean[] { false, true };
	    default:
		return null;
	    }
	});

    @ContainerInfo(groupID = 1)
    @SliderInfo(description = "Line Width", min = 1, max = 10, step = 0.5D, numberType = NumberType.DECIMAL)
    public Value<Float> hostileHexceptionWidth = new Value<Float>(this, "hostileHexceptionWidth", 3F);

    @ContainerInfo(groupID = 1)
    @SliderInfo(description = "Radius", min = 1, max = 5, step = 0.125D, numberType = NumberType.DECIMAL)
    public Value<Float> hostileOutlineShaderRadius = new Value<Float>(this, "hostileOutlineShaderRadius", 1.375F);

    @ContainerInfo(groupID = 1)
    @ColorInfo(description = "Color", hasAlpha = true)
    public Value<float[]> hostileColor = new Value<float[]>(this, "hostileColor", new float[] { 1F, 0.5F, 0.5F, 1F });

    @ContainerInfo(groupID = 2)
    @CheckboxInfo(description = "Animals")
    public Value<Boolean> includeAnimals = new Value<Boolean>(this, "includeAnimals", false, new String[] { "animalMode", "animalColor" }, newValue -> Value
	.createHandler(2, newValue));

    @ContainerInfo(groupID = 2)
    @ComboInfo(description = "Mode", entryNames = { "Vanilla", "Hexception", "OutlineShader" })
    public Value<Integer> animalMode = new Value<Integer>(this, "animalMode", 1, new String[] { "animalHexceptionWidth", "animalOutlineShaderRadius" },
	newValue -> {
	    switch (newValue) {
	    case 0:
		return new boolean[] { false, false };
	    case 1:
		return new boolean[] { true, false };
	    case 2:
		return new boolean[] { false, true };
	    default:
		return null;
	    }
	});

    @ContainerInfo(groupID = 2)
    @SliderInfo(description = "Line Width", min = 1, max = 10, step = 0.5D, numberType = NumberType.DECIMAL)
    public Value<Float> animalHexceptionWidth = new Value<Float>(this, "animalHexceptionWidth", 3F);

    @ContainerInfo(groupID = 2)
    @SliderInfo(description = "Radius", min = 1, max = 5, step = 0.125D, numberType = NumberType.DECIMAL)
    public Value<Float> animalOutlineShaderRadius = new Value<Float>(this, "animalOutlineShaderRadius", 1.375F);

    @ContainerInfo(groupID = 2)
    @ColorInfo(description = "Color", hasAlpha = true)
    public Value<float[]> animalColor = new Value<float[]>(this, "animalColor", new float[] { 0.5F, 1F, 0.5F, 1F });

    @ContainerInfo(groupID = 3)
    @CheckboxInfo(description = "Items")
    public Value<Boolean> includeItems = new Value<Boolean>(this, "includeItems", false, new String[] { "itemOutlineShaderRadius", "itemColor" },
	newValue -> Value.createHandler(2, newValue));

    @ContainerInfo(groupID = 3)
    @SliderInfo(description = "Radius", min = 1, max = 5, step = 0.125D, numberType = NumberType.DECIMAL)
    public Value<Float> itemOutlineShaderRadius = new Value<Float>(this, "itemOutlineShaderRadius", 1.375F);

    @ContainerInfo(groupID = 3)
    @ColorInfo(description = "Color", hasAlpha = true)
    public Value<float[]> itemColor = new Value<float[]>(this, "itemColor", new float[] { 1F, 0.5F, 0F, 1F });

    // CONSTRUCTOR
    public OutlineESP() {
	super("OutlineEPS", Category.RENDER);

	// NodeTree.instance.connectNodes();
	NodeTreeManager.INSTANCE.getTreeMap().get(this).connectNodes();
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
