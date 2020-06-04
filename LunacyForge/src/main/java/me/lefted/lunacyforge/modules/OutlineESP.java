package me.lefted.lunacyforge.modules;

import java.util.Iterator;
import java.util.function.Predicate;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import me.lefted.lunacyforge.clickgui.annotations.CheckboxInfo;
import me.lefted.lunacyforge.clickgui.annotations.ColorInfo;
import me.lefted.lunacyforge.clickgui.annotations.ComboInfo;
import me.lefted.lunacyforge.clickgui.annotations.ContainerInfo;
import me.lefted.lunacyforge.clickgui.annotations.ModuleInfo;
import me.lefted.lunacyforge.clickgui.annotations.SliderInfo;
import me.lefted.lunacyforge.clickgui.elements.ContainerSlider.NumberType;
import me.lefted.lunacyforge.valuesystem.Children;
import me.lefted.lunacyforge.valuesystem.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleInfo(description = "Makes entities visible", tags = { "PlayerEPS" })
public class OutlineESP extends Module {

    // VALUES
    // TODO teams
    @ComboInfo(description = "Mode", entryNames = { "Vanilla", "Hexception" })
    public Value<Integer> mode = new Value<Integer>("mode", 1);

    @ContainerInfo(hoverText = "Only applies for Hexception mode")
    @SliderInfo(description = "Line width", min = 1, max = 10, step = 1, numberType = NumberType.INTEGER)
    public Value<Integer> lineWidth = new Value<Integer>("lineWidth", 3);

    // DEBUG
    @ColorInfo(description = "Default color", hasAlpha = true)
    public Value<float[]> outlineColor = new Value<float[]>("outlineColor", new float[] { 0F, 0.5F, 1F, 1F });

    // private Value<Boolean> uniqueFriendColor = new Value<Boolean>("customFriendColor", true);
    // make this affect friend color
    @CheckboxInfo(description = "Friends have unique color")
    private Value<Boolean> uniqueFriendColor = new Value<Boolean>("customFriendColor", true, new Children<Boolean>(0, new int[] { 1 }, newValue -> newValue));

    @ColorInfo(description = "Friend color", hasAlpha = true)
    public Value<float[]> friendColor = new Value<float[]>("friendColor", new float[] { 0.5686F, 0F, 0.5882F, 1F }, new Children<float[]>(1, null, null));

    // CONSTRUCTOR
    public OutlineESP() {
	super("OutlineEPS", Category.RENDER);
    }

    // @SubscribeEvent
    // public void onRender(RenderLivingEvent.Pre<EntityLivingBase> event) {
    //
    // }

    // METHODS
    @Override
    public void onEnable() {
	// MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
	// MinecraftForge.EVENT_BUS.unregister(this);
    }

}
