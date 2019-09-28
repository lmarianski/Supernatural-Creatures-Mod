
package io.github.lukas2005.supernaturalcreatures.render.model;


import com.mojang.blaze3d.platform.GlStateManager;
import io.github.lukas2005.supernaturalcreatures.player.SCMPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.HandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class ModelWerewolf<T extends LivingEntity> extends BipedModel<T> {
    public RendererModel bipedLeftArmwear;
    public RendererModel bipedRightArmwear;
    public RendererModel bipedLeftLegwear;
    public RendererModel bipedRightLegwear;
    public RendererModel bipedBodyWear;
    RendererModel leftEar;
    RendererModel rightEar;
    RendererModel tail;
    RendererModel nose;

    boolean useSmallArms;

    public ModelWerewolf() {
        this(0.0F);
    }

    public ModelWerewolf(float modelSize) {
        this(modelSize, false);
    }


    public ModelWerewolf(float modelSize, boolean smallArmsIn) {
        super(modelSize, 0.0F, 128, 128);

        this.useSmallArms = smallArmsIn;

        if (smallArmsIn) {
            this.bipedLeftArm = new RendererModel(this, 32, 48);
            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
            this.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);

            this.bipedRightArm = new RendererModel(this, 40, 16);
            this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
            this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);

            this.bipedLeftArmwear = new RendererModel(this, 48, 48);
            this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
            this.bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);

            this.bipedRightArmwear = new RendererModel(this, 40, 32);
            this.bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
            this.bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);
        } else {
            this.bipedLeftArmwear = new RendererModel(this, 48, 48);
            this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
            this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);

            this.bipedRightArmwear = new RendererModel(this, 40, 32);
            this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
            this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
        }

        this.bipedLeftLegwear = new RendererModel(this, 0, 48);
        this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
        this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);

        this.bipedRightLegwear = new RendererModel(this, 0, 32);
        this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
        this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);

        this.bipedBodyWear = new RendererModel(this, 16, 32);
        this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize + 0.25F);
        this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);

        this.leftEar = new RendererModel(this, 0, 0);
        this.leftEar.addBox(2.0F, -10.0F, 0.0F, 2, 2, 1);
        this.leftEar.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftEar.setTextureSize(64, 64);
        this.leftEar.mirror = true;
        setRotation(this.leftEar, 0.0F, 0.0F, 0.0F);

        this.rightEar = new RendererModel(this, 0, 0);
        this.rightEar.addBox(-4.0F, -10.0F, 0.0F, 2, 2, 1);
        this.rightEar.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightEar.setTextureSize(64, 64);
        this.rightEar.mirror = true;
        setRotation(this.rightEar, 0.0F, 0.0F, 0.0F);
        this.rightEar.mirror = false;

        this.tail = new RendererModel(this, 56, 16);
        this.tail.addBox(0.0F, 0.0F, 0.0F, 2, 10, 2);
        this.tail.setRotationPoint(-1.0F, 10.0F, 0.0F);
        this.tail.setTextureSize(64, 64);
        this.tail.mirror = true;
        setRotation(this.tail, 0.8F, 0.0F, 0.0F);

        this.nose = new RendererModel(this, 25, 0);
        this.nose.addBox(-2.0F, -4.0F, -7.0F, 4, 3, 3);
        this.nose.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.nose.setTextureSize(64, 64);
        this.nose.mirror = true;
        setRotation(this.nose, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public void render(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        if (entityIn.isBeingRidden()) {
            this.bipedRightArm.rotateAngleX = -3.0F;
            this.bipedLeftArm.rotateAngleX = -3.0F;
            this.bipedRightArmwear.rotateAngleX = -3.0F;
            this.bipedLeftArmwear.rotateAngleX = -3.0F;
        }

        GlStateManager.pushMatrix();

        this.tail.render(scale);

        if (this.isChild) {
            GlStateManager.scalef(0.5F, 0.5F, 0.5F);
            GlStateManager.translatef(0.0F, 24.0F * scale, 0.0F);
        } else {
            if (entityIn.shouldRenderSneaking()) {
                GlStateManager.translatef(0.0F, 0.2F, 0.0F);
            }
        }

        this.bipedLeftLegwear.render(scale);
        this.bipedRightLegwear.render(scale);
        this.bipedLeftArmwear.render(scale);
        this.bipedRightArmwear.render(scale);
        this.bipedBodyWear.render(scale);

        this.leftEar.render(scale);
        this.rightEar.render(scale);
        this.nose.render(scale);

        GlStateManager.popMatrix();
    }


    private void setRotation(RendererModel model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        super.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

        this.bipedLeftLegwear.copyModelAngles(this.bipedLeftLeg);
        this.bipedRightLegwear.copyModelAngles(this.bipedRightLeg);
        this.bipedLeftArmwear.copyModelAngles(this.bipedLeftArm);
        this.bipedRightArmwear.copyModelAngles(this.bipedRightArm);
        this.bipedBodyWear.copyModelAngles(this.bipedBody);

        this.nose.rotateAngleY = this.bipedHead.rotateAngleY;
        this.nose.rotateAngleX = this.bipedHead.rotateAngleX;
        this.nose.rotationPointY = this.bipedHead.rotationPointY;
        this.leftEar.rotateAngleY = this.bipedHead.rotateAngleY;
        this.leftEar.rotateAngleX = this.bipedHead.rotateAngleX;
        this.leftEar.rotationPointY = this.bipedHead.rotationPointY;
        this.rightEar.rotateAngleY = this.bipedHead.rotateAngleY;
        this.rightEar.rotateAngleX = this.bipedHead.rotateAngleX;
        this.rightEar.rotationPointY = this.bipedHead.rotationPointY;

        if (this.isSneak) {
            //this.bipedHead.rotationPointY += 3.0F;
            //this.bipedHead.rotationPointY += 3.0F;
            //this.bipedHead.rotationPointY += 3.0F;
            this.tail.rotationPointY = 13.0F;
            this.tail.rotationPointZ = 5.5F;
            this.tail.rotateAngleX = 1.2F;
        } else {
            this.tail.rotationPointY = 10.0F;
            this.tail.rotationPointZ = 0.0F;
            this.tail.rotateAngleX = 0.8F;
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        this.bipedLeftArmwear.showModel = visible;
        this.bipedRightArmwear.showModel = visible;
        this.bipedLeftLegwear.showModel = visible;
        this.bipedRightLegwear.showModel = visible;
        this.bipedBodyWear.showModel = visible;
    }

    @Override
    public void postRenderArm(float scale, HandSide side) {
        RendererModel modelrenderer = getArmForSide(side);

        if (this.useSmallArms) {
            float f = 0.5F * ((side == HandSide.RIGHT) ? 1 : -1);

            modelrenderer.rotationPointX += f;
            modelrenderer.postRender(scale);
            modelrenderer.rotationPointX -= f;
        } else {
            modelrenderer.postRender(scale);
        }
    }
}


