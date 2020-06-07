package me.lefted.lunacyforge.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.darkmagician6.eventapi.EventManager;

import me.lefted.lunacyforge.events.Render2DEvent;
import me.lefted.lunacyforge.utils.ClassUtils;
import net.minecraft.profiler.Profiler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin(Profiler.class)
public class MixinProfiler {

    @Inject(method = "startSection", at = @At("HEAD"))
    private void startSection(String name, CallbackInfo callbackInfo) {
        if (name.equals("bossHealth") && ClassUtils.hasClass("net.labymod.api.LabyModAPI")) {
            EventManager.call(new Render2DEvent(0F));
        }
    }
}