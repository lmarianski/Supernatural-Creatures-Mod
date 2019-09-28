package io.github.lukas2005.supernaturalcreatures.render.entity;

import io.github.lukas2005.supernaturalcreatures.Reference;
import io.github.lukas2005.supernaturalcreatures.entity.EntityVampire;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.util.ResourceLocation;

public class EntityVampireRenderer extends LivingRenderer<EntityVampire, BipedModel<EntityVampire>> {

	private static final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "");

	public EntityVampireRenderer(EntityRendererManager manager) {
		super(manager, new BipedModel<>(), 1);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityVampire entity) {
		return texture;
	}
}
