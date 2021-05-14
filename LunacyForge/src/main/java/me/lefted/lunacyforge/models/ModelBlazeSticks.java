package me.lefted.lunacyforge.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBlazeSticks extends ModelBase {

    // ATTRIBUTES
    private ModelRenderer[] blazeSticks = new ModelRenderer[12];

    // CONSTRUCTOR
    public ModelBlazeSticks() {
	for (int i = 0; i < this.blazeSticks.length; ++i) {
	    this.blazeSticks[i] = new ModelRenderer(this, 0, 16);
	    this.blazeSticks[i].addBox(0.0F, 0.0F, 0.0F, 2, 8, 2);
	}

    }

    // METHODS
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
	this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);

	for (int i = 0; i < this.blazeSticks.length; ++i) {
	    this.blazeSticks[i].render(scale);
	}
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor,
	Entity entityIn) {
	float f = ageInTicks * (float) Math.PI * -0.1F;

	for (int i = 0; i < 4; ++i) {
	    this.blazeSticks[i].rotationPointY = -2.0F + MathHelper.cos(((float) (i * 2) + ageInTicks) * 0.25F);
	    this.blazeSticks[i].rotationPointX = MathHelper.cos(f) * 9.0F;
	    this.blazeSticks[i].rotationPointZ = MathHelper.sin(f) * 9.0F;
	    ++f;
	}

	f = ((float) Math.PI / 4F) + ageInTicks * (float) Math.PI * 0.03F;

	for (int j = 4; j < 8; ++j) {
	    this.blazeSticks[j].rotationPointY = 2.0F + MathHelper.cos(((float) (j * 2) + ageInTicks) * 0.25F);
	    this.blazeSticks[j].rotationPointX = MathHelper.cos(f) * 7.0F;
	    this.blazeSticks[j].rotationPointZ = MathHelper.sin(f) * 7.0F;
	    ++f;
	}

	f = 0.47123894F + ageInTicks * (float) Math.PI * -0.05F;

	for (int k = 8; k < 12; ++k) {
	    this.blazeSticks[k].rotationPointY = 11.0F + MathHelper.cos(((float) k * 1.5F + ageInTicks) * 0.5F);
	    this.blazeSticks[k].rotationPointX = MathHelper.cos(f) * 5.0F;
	    this.blazeSticks[k].rotationPointZ = MathHelper.sin(f) * 5.0F;
	    ++f;
	}
    }
}