package me.lefted.lunacyforge.injection.mixins;

import javax.swing.text.html.parser.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import me.lefted.lunacyforge.injection.mixins.dummies.BoundingBoxer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin(Entity.class)
public abstract class MixinEntity extends Object implements BoundingBoxer{

    @Shadow
    public AxisAlignedBB boundingBox;
    
    @Override
    public AxisAlignedBB getBoundingBox() {
	return this.boundingBox;
    }
    
}
