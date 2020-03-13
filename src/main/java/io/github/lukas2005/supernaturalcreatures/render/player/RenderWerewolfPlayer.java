package io.github.lukas2005.supernaturalcreatures.render.player;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import io.github.lukas2005.supernaturalcreatures.player.werewolf.WerewolfPlayer;
import io.github.lukas2005.supernaturalcreatures.render.model.ModelWerewolf;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@OnlyIn(Dist.CLIENT)
public class RenderWerewolfPlayer extends LivingRenderer<AbstractClientPlayerEntity, ModelWerewolf<AbstractClientPlayerEntity>> {

    public RenderWerewolfPlayer(EntityRendererManager renderManager) {
        this(renderManager, false);
    }

    public RenderWerewolfPlayer(EntityRendererManager renderManager, boolean useSmallArms) {
        super(renderManager, new ModelWerewolf<>(0.0f, useSmallArms), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new BipedModel<>(0.5F), new BipedModel<>(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
        //this.addLayer(new ArrowLayer<>(this));
        //this.addLayer(new Deadmau5HeadLayer(this));
        //this.addLayer(new CapeLayer(this));
        this.addLayer(new HeadLayer<>(this));
        this.addLayer(new ElytraLayer<>(this));
        //this.addLayer(new ParrotVariantLayer<>(this));
        //this.addLayer(new SpinAttackEffectLayer<>(this));
        this.addLayer(new LayerSkinOverlay<>(this));
    }

//    @Override
//    public void doRender(AbstractClientPlayerEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
//        if (!entity.isUser() || this.renderManager.info.getRenderViewEntity() == entity) {
//            double d0 = y;
//            if (entity.getPose() == Pose.CROUCHING) {
//                d0 = y - 0.125D;
//            }
//
//            this.setModelVisibilities(entity);
//            GlStateManager.setProfile(GlStateManager.Profile.PLAYER_SKIN);
//            super.doRender(entity, x, d0, z, entityYaw, partialTicks);
//            GlStateManager.unsetProfile(GlStateManager.Profile.PLAYER_SKIN);
//        }
//    }



    private static Method getArmPose;

    static {
        try {
            getArmPose = PlayerRenderer.class.getDeclaredMethod("getArmPose", AbstractClientPlayerEntity.class, ItemStack.class, ItemStack.class, Hand.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    EntityRenderer<? super AbstractClientPlayerEntity> render;
    Entity lastPlayer;

    private BipedModel.ArmPose getArmPose(AbstractClientPlayerEntity playerIn, ItemStack itemStackMain, ItemStack itemStackOff, Hand handIn) {
        if (lastPlayer != playerIn)
            render = renderManager.getRenderer(playerIn);
            lastPlayer = playerIn;
        try {
            return (BipedModel.ArmPose) getArmPose.invoke(render, playerIn, itemStackMain, itemStackOff, handIn);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setModelVisibilities(AbstractClientPlayerEntity clientPlayer) {
        ModelWerewolf playermodel = this.getEntityModel();
        EntityRenderer<? super AbstractClientPlayerEntity> renderer = renderManager.getRenderer(clientPlayer);
        if (clientPlayer.isSpectator()) {
            playermodel.setVisible(false);
            playermodel.bipedHead.showModel = true;
            playermodel.bipedHeadwear.showModel = true;
        } else {
            ItemStack itemstack = clientPlayer.getHeldItemMainhand();
            ItemStack itemstack1 = clientPlayer.getHeldItemOffhand();
            playermodel.setVisible(true);
            playermodel.bipedHeadwear.showModel = clientPlayer.isWearing(PlayerModelPart.HAT);
            playermodel.bipedBodyWear.showModel = clientPlayer.isWearing(PlayerModelPart.JACKET);
            playermodel.bipedLeftLegwear.showModel = clientPlayer.isWearing(PlayerModelPart.LEFT_PANTS_LEG);
            playermodel.bipedRightLegwear.showModel = clientPlayer.isWearing(PlayerModelPart.RIGHT_PANTS_LEG);
            playermodel.bipedLeftArmwear.showModel = clientPlayer.isWearing(PlayerModelPart.LEFT_SLEEVE);
            playermodel.bipedRightArmwear.showModel = clientPlayer.isWearing(PlayerModelPart.RIGHT_SLEEVE);
            playermodel.isSneak = clientPlayer.isCrouching();
            BipedModel.ArmPose bipedmodel$armpose = this.getArmPose(clientPlayer, itemstack, itemstack1, Hand.MAIN_HAND);
            BipedModel.ArmPose bipedmodel$armpose1 = this.getArmPose(clientPlayer, itemstack, itemstack1, Hand.OFF_HAND);
            if (clientPlayer.getPrimaryHand() == HandSide.RIGHT) {
                playermodel.rightArmPose = bipedmodel$armpose;
                playermodel.leftArmPose = bipedmodel$armpose1;
            } else {
                playermodel.rightArmPose = bipedmodel$armpose1;
                playermodel.leftArmPose = bipedmodel$armpose;
            }
        }

    }

//    private BipedModel.ArmPose func_217766_a(AbstractClientPlayerEntity p_217766_1_, ItemStack p_217766_2_, ItemStack p_217766_3_, Hand p_217766_4_) {
//        BipedModel.ArmPose bipedmodel$armpose = BipedModel.ArmPose.EMPTY;
//        ItemStack itemstack = p_217766_4_ == Hand.MAIN_HAND ? p_217766_2_ : p_217766_3_;
//        if (!itemstack.isEmpty()) {
//            bipedmodel$armpose = BipedModel.ArmPose.ITEM;
//            if (p_217766_1_.getItemInUseCount() > 0) {
//                UseAction useaction = itemstack.getUseAction();
//                if (useaction == UseAction.BLOCK) {
//                    bipedmodel$armpose = BipedModel.ArmPose.BLOCK;
//                } else if (useaction == UseAction.BOW) {
//                    bipedmodel$armpose = BipedModel.ArmPose.BOW_AND_ARROW;
//                } else if (useaction == UseAction.SPEAR) {
//                    bipedmodel$armpose = BipedModel.ArmPose.THROW_SPEAR;
//                } else if (useaction == UseAction.CROSSBOW && p_217766_4_ == p_217766_1_.getActiveHand()) {
//                    bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_CHARGE;
//                }
//            } else {
//                boolean flag3 = p_217766_2_.getItem() == Items.CROSSBOW;
//                boolean flag = CrossbowItem.isCharged(p_217766_2_);
//                boolean flag1 = p_217766_3_.getItem() == Items.CROSSBOW;
//                boolean flag2 = CrossbowItem.isCharged(p_217766_3_);
//                if (flag3 && flag) {
//                    bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_HOLD;
//                }
//
//                if (flag1 && flag2 && p_217766_2_.getItem().getUseAction(p_217766_2_) == UseAction.NONE) {
//                    bipedmodel$armpose = BipedModel.ArmPose.CROSSBOW_HOLD;
//                }
//            }
//        }
//
//        return bipedmodel$armpose;
//    }

    @Override
    public ResourceLocation getEntityTexture(AbstractClientPlayerEntity entity) {
        return WerewolfPlayer.of(entity).getSkin();
    }


    @Override
    protected void preRenderCallback(AbstractClientPlayerEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        GL11.glScalef(0.9375F, 0.9375F, 0.9375F);
    }

    @Override
    protected void renderName(AbstractClientPlayerEntity entityIn, String displayNameIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        double d0 = this.renderManager.squareDistanceTo(entityIn);
        matrixStackIn.push();
        if (d0 < 100.0D) {
            Scoreboard scoreboard = entityIn.getWorldScoreboard();
            ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);
            if (scoreobjective != null) {
                Score score = scoreboard.getOrCreateScore(entityIn.getScoreboardName(), scoreobjective);
                super.renderName(entityIn, score.getScorePoints() + " " + scoreobjective.getDisplayName().getFormattedText(), matrixStackIn, bufferIn, packedLightIn);
                matrixStackIn.translate(0.0D, 0.25874999165534973D, 0.0D);
            }
        }

        super.renderName(entityIn, displayNameIn, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.pop();
    }

//    public void renderRightArm(AbstractClientPlayerEntity clientPlayer) {
//        float f = 1.0F;
//        GlStateManager.color3f(1.0F, 1.0F, 1.0F);
//        float f1 = 0.0625F;
//        ModelWerewolf<AbstractClientPlayerEntity> playermodel = this.getEntityModel();
//        this.setModelVisibilities(clientPlayer);
//        GlStateManager.enableBlend();
//        playermodel.swingProgress = 0.0F;
//        playermodel.isSneak = false;
//        playermodel.swimAnimation = 0.0F;
//        playermodel.setRotationAngles(clientPlayer, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
//        playermodel.bipedRightArm.rotateAngleX = 0.0F;
//        playermodel.bipedRightArm.render(0.0625F);
//        playermodel.bipedRightArmwear.rotateAngleX = 0.0F;
//        playermodel.bipedRightArmwear.render(0.0625F);
//        GlStateManager.disableBlend();
//    }

//    public void renderArm(AbstractClientPlayerEntity clientPlayer, HandSide hand) {
//        GL11.glColor3f(1.0F, 1.0F, 1.0F);
//        ModelWerewolf<AbstractClientPlayerEntity> model = this.getEntityModel();
//
//        this.setModelVisibilities(clientPlayer);
//
//        GlStateManager.enableBlend();
//        model.isSneak = false;
//        model.swingProgress = 0.0F;
//        model.swimAnimation = 0.0F;
//        model.setRotationAngles(clientPlayer, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
//
//        ModelRenderer arm = hand == HandSide.RIGHT ? model.bipedRightArm : model.bipedLeftArm;
//        ModelRenderer armWear = hand == HandSide.RIGHT ? model.bipedRightArmwear : model.bipedLeftArmwear;
//
//        arm.rotateAngleX = 0.0F;
//        arm.render(0.0625F);
//        armWear.rotateAngleX = 0.0F;
//        armWear.render(0.0625F);
//        GlStateManager.disableBlend();
//    }

    @Override
    protected void applyRotations(AbstractClientPlayerEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        float f = entityLiving.getSwimAnimation(partialTicks);
        float f3;
        float f2;
        if (entityLiving.isElytraFlying()) {
            super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
            f3 = (float) entityLiving.getTicksElytraFlying() + partialTicks;
            f2 = MathHelper.clamp(f3 * f3 / 100.0F, 0.0F, 1.0F);
            if (!entityLiving.isSpinAttacking()) {
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(f2 * (-90.0F - entityLiving.rotationPitch)));
            }

            Vec3d vec3d = entityLiving.getLook(partialTicks);
            Vec3d vec3d1 = entityLiving.getMotion();
            double d0 = Entity.horizontalMag(vec3d1);
            double d1 = Entity.horizontalMag(vec3d);
            if (d0 > 0.0D && d1 > 0.0D) {
                double d2 = (vec3d1.x * vec3d.x + vec3d1.z * vec3d.z) / (Math.sqrt(d0) * Math.sqrt(d1));
                double d3 = vec3d1.x * vec3d.z - vec3d1.z * vec3d.x;
                matrixStackIn.rotate(Vector3f.YP.rotation((float) (Math.signum(d3) * Math.acos(d2))));
            }
        } else if (f > 0.0F) {
            super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
            f3 = entityLiving.isInWater() ? -90.0F - entityLiving.rotationPitch : -90.0F;
            f2 = MathHelper.lerp(f, 0.0F, f3);
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(f2));
            if (entityLiving.isActualySwimming()) {
                matrixStackIn.translate(0.0D, -1.0D, 0.30000001192092896D);
            }
        } else {
            super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        }
    }
}
