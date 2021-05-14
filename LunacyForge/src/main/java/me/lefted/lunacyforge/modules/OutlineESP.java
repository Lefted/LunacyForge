package me.lefted.lunacyforge.modules;

import org.lwjgl.input.Keyboard;

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
import me.lefted.lunacyforge.events.UpdateEvent;
import me.lefted.lunacyforge.friends.FriendManager;
import me.lefted.lunacyforge.implementations.ITimer;
import me.lefted.lunacyforge.shader.FramebufferShader;
import me.lefted.lunacyforge.shader.shaders.FriendOutlineShader;
import me.lefted.lunacyforge.shader.shaders.OutlineShader;
import me.lefted.lunacyforge.utils.Logger;
import me.lefted.lunacyforge.valuesystem.NodeTreeManager;
import me.lefted.lunacyforge.valuesystem.Value;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;

/**
 * @author Moritz
 *
 */
@ModuleInfo(description = "Makes entities visible", tags = { "PlayerEPS" })
public class OutlineESP extends Module {

    // VALUES
    @ContainerInfo(groupID = 0)
    @CheckboxInfo(description = "Players")
    public Value<Boolean> includePlayers = new Value<Boolean>(this, "includePlayers", true, new String[] { "playerMode" }, newValue -> Value.createHandler(1,
	newValue));

    @ContainerInfo(groupID = 0)
    @ComboInfo(description = "Mode", entryNames = { "Vanilla", "Hexception", "OutlineShader" })
    public Value<Integer> playerMode = new Value<Integer>(this, "playerMode", 2, new String[] { "playerHexceptionWidth", "useTeamColor", "useFriendColor",
	    "friendColorToggleKeybind" }, newValue -> {
		switch (newValue) {
		case 0:
		    return new boolean[] { false, true, true, true };
		case 1:
		    return new boolean[] { true, true, true, true };
		case 2:
		    return new boolean[] { false, false, true, true };
		default:
		    return null;
		}
	    });

    @ContainerInfo(groupID = 0)
    @SliderInfo(description = "Line Width", min = 1, max = 10, step = 0.5D, numberType = NumberType.DECIMAL)
    public Value<Float> playerHexceptionWidth = new Value<Float>(this, "playerHexceptionWidth", 3F);

    @ContainerInfo(groupID = 0, hoverText = "If enabled, the outline color will adapt to the team")
    @CheckboxInfo(description = "Teams")
    public Value<Boolean> useTeamColor = new Value<Boolean>(this, "useTeamColor", true, new String[] { "playerColor" }, newValue -> Value.createHandler(1,
	!newValue));

    @ContainerInfo(groupID = 0)
    @ColorInfo(description = "Color", hasAlpha = true)
    public Value<float[]> playerColor = new Value<float[]>(this, "playerColor", new float[] { 0F, 0.5F, 1F, 1F });

    @ContainerInfo(groupID = 0, hoverText = "If enabled, friends can have a different color than other players")
    @CheckboxInfo(description = "Friends")
    public Value<Boolean> useFriendColor = new Value<Boolean>(this, "useFriendColor", true, new String[] { "friendColor", "friendMode" }, newValue -> Value
	.createHandler(2, newValue));

    @ContainerInfo(groupID = 0)
    @ColorInfo(description = "Friend Color", hasAlpha = true)
    public Value<float[]> friendColor = new Value<float[]>(this, "friendColor", new float[] { 0F, 0.5F, 0.0F, 0.5F });

    @ContainerInfo(groupID = 0, hoverText = "Choose the mode your friends are outlined")
    @ComboInfo(description = "Friend Mode", entryNames = { "Hexception", "OutlineShader" })
    public Value<Integer> friendMode = new Value<Integer>(this, "friendMode", 1, new String[] { "friendOutlineShaderRadius" }, newValue -> Value.createHandler(
	1, newValue == 1));

    @ContainerInfo(groupID = 0)
    @SliderInfo(description = "FriendOutlineShader Radius", min = 1, max = 3, step = 0.125D, numberType = NumberType.DECIMAL)
    public Value<Float> friendOutlineShaderRadius = new Value<Float>(this, "friendOutlineShaderRadius", 1.375F);

    // @ContainerInfo(groupID = 4)
    // @SliderInfo(description = "OutlineShader Radius", min = 1, max = 3, step = 0.125D, numberType = NumberType.DECIMAL)
    // public Value<Float> outlineShaderRadius = new Value<Float>(this, "outlineShaderRadius", 1.375F);

    @ContainerInfo(groupID = 0)
    @KeybindInfo(description = "Toggle Friends Keybind")
    public Value<Integer> friendColorToggleKeybind = new Value<Integer>(this, "friendColorToggleKeybind", 0);

    @ContainerInfo(groupID = 1)
    @CheckboxInfo(description = "Hostiles")
    public Value<Boolean> includeHostiles = new Value<Boolean>(this, "includeHostiles", false, new String[] { "hostileMode" }, newValue -> Value.createHandler(
	1, newValue));

    @ContainerInfo(groupID = 1)
    @ComboInfo(description = "Mode", entryNames = { "Vanilla", "Hexception", "OutlineShader" })
    public Value<Integer> hostileMode = new Value<Integer>(this, "hostileMode", 1, new String[] { "hostileHexceptionWidth", "hostileColor" }, newValue -> {
	switch (newValue) {
	case 0:
	    return new boolean[] { false, true };
	case 1:
	    return new boolean[] { true, true };
	case 2:
	    return new boolean[] { false, false };
	default:
	    return null;
	}
    });

    @ContainerInfo(groupID = 1)
    @SliderInfo(description = "Line Width", min = 1, max = 10, step = 0.5D, numberType = NumberType.DECIMAL)
    public Value<Float> hostileHexceptionWidth = new Value<Float>(this, "hostileHexceptionWidth", 3F);

    @ContainerInfo(groupID = 1)
    @ColorInfo(description = "Color", hasAlpha = true)
    public Value<float[]> hostileColor = new Value<float[]>(this, "hostileColor", new float[] { 1F, 0.5F, 0.5F, 1F });

    @ContainerInfo(groupID = 2)
    @CheckboxInfo(description = "Animals")
    public Value<Boolean> includeAnimals = new Value<Boolean>(this, "includeAnimals", false, new String[] { "animalMode" }, newValue -> Value.createHandler(1,
	newValue));

    @ContainerInfo(groupID = 2)
    @ComboInfo(description = "Mode", entryNames = { "Vanilla", "Hexception", "OutlineShader" })
    public Value<Integer> animalMode = new Value<Integer>(this, "animalMode", 1, new String[] { "animalHexceptionWidth", "animalColor" }, newValue -> {
	switch (newValue) {
	case 0:
	    return new boolean[] { false, true };
	case 1:
	    return new boolean[] { true, true };
	case 2:
	    return new boolean[] { false, false };
	default:
	    return null;
	}
    });

    @ContainerInfo(groupID = 2)
    @SliderInfo(description = "Line Width", min = 1, max = 10, step = 0.5D, numberType = NumberType.DECIMAL)
    public Value<Float> animalHexceptionWidth = new Value<Float>(this, "animalHexceptionWidth", 3F);

    @ContainerInfo(groupID = 2)
    @ColorInfo(description = "Color", hasAlpha = true)
    public Value<float[]> animalColor = new Value<float[]>(this, "animalColor", new float[] { 0.5F, 1F, 0.5F, 1F });

    @ContainerInfo(groupID = 3)
    @CheckboxInfo(description = "Items")
    public Value<Boolean> includeItems = new Value<Boolean>(this, "includeItems", false);

    @ContainerInfo(groupID = 4)
    @SliderInfo(description = "OutlineShader Radius", min = 1, max = 3, step = 0.125D, numberType = NumberType.DECIMAL)
    public Value<Float> outlineShaderRadius = new Value<Float>(this, "outlineShaderRadius", 1.375F);

    @ContainerInfo(groupID = 4)
    @ColorInfo(description = "OutlineShader Color", hasAlpha = true)
    public Value<float[]> outlineShaderColor = new Value<float[]>(this, "outlineShaderColor", new float[] { 1F, 0.5F, 0F, 1F });

    // CONSTRUCTOR
    public OutlineESP() {
	super("OutlineEPS", Category.RENDER);

	// NodeTree.instance.connectNodes();
	NodeTreeManager.INSTANCE.getTreeMap().get(this).connectNodes();
    }

    // METHODS
    @EventTarget
    public void onRender2D(final Render2DEvent event) {
	// only go through entites if any uses outline shader
	if (isUsingOutlineShader()) {
	    renderOutlineShader(event.getPartialTicks());
	    if (includePlayers.getObject().booleanValue() && playerMode.getObject().intValue() != 0 && useFriendColor.getObject().booleanValue()) {
		renderFriendOutlineShader(event.getPartialTicks());
	    }
	}
    }

    private void renderOutlineShader(float partialTicks) {
	final FramebufferShader shader = OutlineShader.OUTLINE_SHADER;
	if (shader == null) {
	    return;
	}
	shader.startDraw(partialTicks);

	try {
	    for (final Entity entity : mc.theWorld.loadedEntityList) {
		// first person fix
		if (entity instanceof EntityPlayerSP && mc.gameSettings.thirdPersonView == 0) {
		    continue;
		}

		if (!outlineShaderShouldRenderEntity(entity)) {
		    continue;
		}

		final ITimer timer = (ITimer) mc;
		mc.getRenderManager().renderEntityStatic(entity, timer.getMinecraftTimer().renderPartialTicks, true);
	    }
	} catch (final Exception ex) {
	    Logger.logChatMessage("An error occurred while rendering all entities for shader esp" + ex);
	}
	shader.stopDraw(outlineShaderColor.getObject(), outlineShaderRadius.getObject().floatValue(), 1F);
    }

    private void renderFriendOutlineShader(float partialTicks) {
	final FramebufferShader shader = FriendOutlineShader.FRIEND_OUTLINE_SHADER;
	if (shader == null) {
	    return;
	}
	shader.startDraw(partialTicks);

	try {
	    for (final Entity entity : mc.theWorld.loadedEntityList) {
		// first person fix
		if (entity instanceof EntityPlayerSP && mc.gameSettings.thirdPersonView == 0) {
		    continue;
		}

		if (!friendOutlineShaderShouldRenderEntity(entity)) {
		    continue;
		}

		final ITimer timer = (ITimer) mc;
		mc.getRenderManager().renderEntityStatic(entity, timer.getMinecraftTimer().renderPartialTicks, true);
	    }
	} catch (final Exception ex) {
	    Logger.logChatMessage("An error occurred while rendering all entities for shader esp" + ex);
	}
	shader.stopDraw(friendColor.getObject(), friendOutlineShaderRadius.getObject().floatValue(), 1F);
    }

    private boolean wasKeyPressed = false;

    @EventTarget
    public void onUpdate(final UpdateEvent event) {
	if (friendColorToggleKeybind.getObject().intValue() != 0 && mc.currentScreen == null) {
	    if (wasKeyPressed && !Keyboard.isKeyDown(friendColorToggleKeybind.getObject().intValue())) {
		useFriendColor.setObject(!useFriendColor.getObject().booleanValue());
		wasKeyPressed = false;

		if (useFriendColor.getObject().booleanValue()) {
		    Logger.logChatMessage("Friends will now have a unique color");
		} else {
		    Logger.logChatMessage("Friends will no longer have a different color");
		}
	    } else if (Keyboard.isKeyDown(friendColorToggleKeybind.getObject().intValue())) {
		wasKeyPressed = true;
	    }
	}
    }

    /**
     * @param entity
     * @return returns if the entity should be rendered using the outline shader
     */
    public boolean outlineShaderShouldRenderEntity(Entity entity) {
	if (entity instanceof EntityPlayer) {
	    if (includePlayers.getObject().booleanValue() && playerMode.getObject().intValue() == 2 && !(useFriendColor.getObject().booleanValue()
		&& FriendManager.instance.isPlayerFriend(((EntityPlayer) entity).getGameProfile().getName()))) {
		return true;
	    }
	} else if (entity instanceof EntityMob) {
	    if (includeHostiles.getObject().booleanValue() && hostileMode.getObject().intValue() == 2) {
		return true;
	    }
	} else if (entity instanceof EntityAnimal) {
	    if (includeAnimals.getObject().booleanValue() && animalMode.getObject().intValue() == 2) {
		return true;
	    }
	} else if (entity instanceof EntityItem) {
	    if (includeItems.getObject().booleanValue()) {
		return true;
	    }
	}
	return false;
    }

    /**
     * @param entity
     * @return returns if the entity should be rendered as friend using the friend outline shader
     */
    public boolean friendOutlineShaderShouldRenderEntity(Entity entity) {
	if (entity instanceof EntityPlayer) {
	    if (playerMode.getObject().intValue() == 1 && friendMode.getObject().intValue() == 1 && FriendManager.instance.isPlayerFriend(
		((EntityPlayer) entity).getGameProfile().getName())) {
		return true;
	    } else if (playerMode.getObject().intValue() == 2 && friendMode.getObject().intValue() == 1 && FriendManager.instance.isPlayerFriend(
		((EntityPlayer) entity).getGameProfile().getName())) {
		return true;
	    }
	}

	return false;
    }

    /**
     * @param entity
     * @return returns if the entity should be rendered using hexception's outline utils
     */
    public boolean hexceptionShouldRenderEntity(Entity entity) {

	if (entity instanceof EntityPlayer && includePlayers.getObject().booleanValue() && playerMode.getObject().intValue() != 0) {
	    if (!(useFriendColor.getObject().booleanValue() && friendMode.getObject().intValue() == 1 && FriendManager.instance.isPlayerFriend(
		((EntityPlayer) entity).getGameProfile().getName()))) {
		return true;
	    }
	} else if (entity instanceof EntityMob && includeHostiles.getObject().booleanValue() && hostileMode.getObject().intValue() == 1) {
	    return true;
	} else if (entity instanceof EntityAnimal && includeAnimals.getObject().booleanValue() && animalMode.getObject().intValue() == 1) {
	    return true;
	}
	return false;
    }

    /**
     * @param entity
     * @return retruns the line width for the specific entity when using hexception's outline utils
     */
    public float getHexceptionLineWidth(Entity entity) {

	if (entity instanceof EntityPlayer) {
	    return playerHexceptionWidth.getObject().floatValue();
	} else if (entity instanceof EntityMob) {
	    return hostileHexceptionWidth.getObject().floatValue();
	} else if (entity instanceof EntityAnimal) {
	    return animalHexceptionWidth.getObject().floatValue();
	}

	// default value
	return 3.0F;
    }

    public float[] getHexceptionColor(Entity entity) {

	if (entity instanceof EntityPlayer) {
	    final EntityPlayer player = (EntityPlayer) entity;

	    if (useFriendColor.getObject().booleanValue() && FriendManager.instance.isPlayerFriend(player.getGameProfile().getName())) {
		return friendColor.getObject();
	    }

	    if (useTeamColor.getObject().booleanValue()) {
		int i = 16777215;
		ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam) player.getTeam();
		if (scoreplayerteam != null) {
		    String s = FontRenderer.getFormatFromString(scoreplayerteam.getColorPrefix());

		    if (s.length() >= 2) {
			i = mc.fontRendererObj.getColorCode(s.charAt(1));
		    }
		}

		final float f1 = (float) (i >> 16 & 255) / 255.0F;
		final float f2 = (float) (i >> 8 & 255) / 255.0F;
		final float f = (float) (i & 255) / 255.0F;

		return new float[] { f1, f2, f, 1.0F };
	    }

	    return playerColor.getObject();
	} else if (entity instanceof EntityMob) {
	    return hostileColor.getObject();
	} else if (entity instanceof EntityAnimal) {
	    return animalColor.getObject();
	}

	// default value white
	return new float[] { 1F, 1F, 1F, 1F };
    }

    // returns if any shader should render
    public boolean isUsingOutlineShader() {

	// if players are on and (if mode is (hex or spec) and friend is out)
	if (includePlayers.getObject().booleanValue() && (playerMode.getObject() <= 2 && useFriendColor.getObject().booleanValue() && friendMode
	    .getObject() == 1)) {
	    return true;
	}

	// if hostiles are on and hostile mode is out
	if (includeHostiles.getObject().booleanValue() && hostileMode.getObject().intValue() == 2) {
	    return true;
	}

	// if animals are on and animal mode is out
	if (includeAnimals.getObject().booleanValue() && animalMode.getObject().intValue() == 2) {
	    return true;
	}

	// items (currently only in out mode available)
	if (includeItems.getObject().booleanValue()) {
	    return true;
	}

	return false;
    }

    @Override
    public void onEnable() {
	EventManager.register(this);
    }

    @Override
    public void onDisable() {
	EventManager.unregister(this);
    }

}
