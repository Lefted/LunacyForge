package me.lefted.lunacyforge.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import me.lefted.lunacyforge.modules.ModuleManager;
import me.lefted.lunacyforge.modules.Velocity;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient implements INetHandlerPlayClient {

    // METHODS
    @Redirect(method = "handleEntityVelocity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(DDD)V", ordinal = 0, args = "log=false"))
    public void setVelocityProxy(Entity owner, double x, double y, double z) {
	Velocity velocity = (Velocity) ModuleManager.getModule(Velocity.class);
	if (velocity.isEnabled()) {
	    owner.setVelocity(x * (velocity.multiplier.getObject().intValue() / 100D), y, z * (velocity.multiplier.getObject().intValue() / 100D));
	} else {
	    owner.setVelocity(x, y, z);
	}
	
    }

    
}
