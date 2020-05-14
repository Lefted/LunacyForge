package me.lefted.lunacyforge.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.darkmagician6.eventapi.EventManager;

import me.lefted.lunacyforge.config.ClientConfig;
import me.lefted.lunacyforge.events.UpdateEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    /* dispatches UpdateEvent*/
    @Inject(method = "onUpdate", at = @At("HEAD"))
    private void onUpdate(CallbackInfo callback) {
	if (ClientConfig.isEnabled()) {
	    final UpdateEvent event = new UpdateEvent();
	    EventManager.call(event);
	}
    }

}
