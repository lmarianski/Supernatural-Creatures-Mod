package io.github.lukas2005.supernaturalcreatures.entity.render;

import io.github.lukas2005.supernaturalcreatures.Reference;
import io.github.lukas2005.supernaturalcreatures.entity.EntityVampire;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class EntityVampireRenderer extends RenderLiving<EntityVampire> {

	private static final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "");

	public EntityVampireRenderer(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
		super(rendermanagerIn, modelbaseIn, shadowsizeIn);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityVampire entity) {
		return texture;
	}
}
