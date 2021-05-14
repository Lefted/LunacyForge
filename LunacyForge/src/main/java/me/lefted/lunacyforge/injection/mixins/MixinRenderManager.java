package me.lefted.lunacyforge.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import me.lefted.lunacyforge.implementations.IRenderPlayerGetter;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin(RenderManager.class)
public abstract class MixinRenderManager implements IRenderPlayerGetter{

    @Shadow
    private RenderPlayer playerRenderer;
    
    @Override
    public RenderPlayer getPlayerRenderer() {
        return this.playerRenderer;
    }
    
}
