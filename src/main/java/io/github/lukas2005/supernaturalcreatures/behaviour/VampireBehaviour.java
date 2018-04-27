package io.github.lukas2005.supernaturalcreatures.behaviour;

import io.github.lukas2005.supernaturalcreatures.DamageSources;
import io.github.lukas2005.supernaturalcreatures.IFactory;
import io.github.lukas2005.supernaturalcreatures.Reference;
import io.github.lukas2005.supernaturalcreatures.Utils;
import io.github.lukas2005.supernaturalcreatures.capabilities.IPlayerDataCapability;
import io.github.lukas2005.supernaturalcreatures.capabilities.ModCapabilities;
import io.github.lukas2005.supernaturalcreatures.entity.EntityVampire;
import io.github.lukas2005.supernaturalcreatures.enums.CreatureType;
import io.github.lukas2005.supernaturalcreatures.enums.ResistanceLevel;
import io.github.lukas2005.supernaturalcreatures.network.DamageEntityMessage;
import io.github.lukas2005.supernaturalcreatures.network.NetworkManager;
import io.github.lukas2005.supernaturalcreatures.skill.Level;
import io.github.lukas2005.supernaturalcreatures.skill.SkillHelper;
import io.github.lukas2005.supernaturalcreatures.skill.SkillTree;
import io.github.lukas2005.supernaturalcreatures.skill.vampire.SkillHardTeeth;
import io.github.lukas2005.supernaturalcreatures.skill.vampire.SkillSunScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldServer;
import net.minecraft.world.end.DragonFightManager;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VampireBehaviour extends DefaultCreatureBehaviour {

	public static final ArrayList<ResourceLocation> eyeOverlays = new ArrayList<>();
	public static final ArrayList<ResourceLocation> fangOverlays = new ArrayList<>();

	public static final int EYE_OVERLAY_COUNT = 1;
	public static final int FANG_OVERLAY_COUNT = 1;

	public static final Map<Class<? extends EntityLivingBase>, IFactory<EntityLivingBase>> VAMPIRE_FACTORIES = new HashMap<>();

	@SideOnly(Side.CLIENT)
	public static final KeyBinding VAMPIRE_BITE = new KeyBinding("key."+Reference.MOD_ID+".vampire_bite", Keyboard.KEY_R, Reference.NAME);

	public VampireBehaviour() {
		super();
	}

	static {
		VAMPIRE_FACTORIES.put(EntitySheep.class, args -> {

			WorldServer worldServer = (WorldServer) args[3];

			EntityLivingBase e = new EntityVampire(worldServer);
			e.setPosition((double)args[0], (double)args[1], (double)args[2]);

			return e;
		});
	}

	public static void turnPlayerIntoVampire(IPlayerDataCapability playerData, EntityPlayer player) {
		playerData.setCreatureType(CreatureType.VAMPIRE);
		playerData.syncData(player);
	}

	public static void turnPlayerIntoVampire(EntityPlayer player) {
		IPlayerDataCapability playerData = player.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null);
		turnPlayerIntoVampire(playerData, player);
	}

	public static void turnIntoVampire(EntityLivingBase entity) {
		if (VAMPIRE_FACTORIES.containsKey(entity.getClass())) {
			entity.setDead();
			EntityLivingBase vampEntity = VAMPIRE_FACTORIES.get(entity.getClass()).newInstance(entity.posX, entity.posY, entity.posZ, entity.getEntityWorld());
			entity.getEntityWorld().spawnEntityInWorld(vampEntity);
		}
	}

	public static boolean canBecomeVampire(EntityLivingBase e) {
		return e instanceof EntityPlayer || VAMPIRE_FACTORIES.containsKey(e.getClass());
	}

	@Override
	public ResistanceLevel getResistanceLevel(DamageSource source) {
		return source.isFireDamage() ? ResistanceLevel.WEAKNESS : super.getResistanceLevel(source);
	}

	@Override
	public void applyWeakness(LivingHurtEvent hurtEvent, IPlayerDataCapability playerData, CreatureType type, EntityPlayer player) {
		if (hurtEvent.getSource().isFireDamage()) {
			hurtEvent.setAmount(hurtEvent.getAmount()+5f);
		}
	}

	@Override
	public void onPlayerTick(TickEvent.PlayerTickEvent e, IPlayerDataCapability playerData) {
		if (Utils.gettingSundamge(e.player)) {
			if (playerData.hasData("vampire.sunDamage")) {
				float sunDamage = Math.min(0, Float.parseFloat(playerData.getData("vampire.sunDamage")));
				if (sunDamage > 0) {
					e.player.attackEntityFrom(DamageSources.SUN, sunDamage);
				}
			}
		}
		if (e.side == Side.CLIENT) {
			if (VAMPIRE_BITE.isKeyDown()) {
				Entity entity = Minecraft.getMinecraft().objectMouseOver.entityHit;
				if (entity != null) {
					NetworkManager.INSTANCE.sendToServer(new DamageEntityMessage(e.player, entity, DamageSources.vampireBite(e.player), 4f));
				}
			}
		}
	}

	@Override
	public void playerSkinOverlayRender(IPlayerDataCapability playerData, AbstractClientPlayer player, RenderPlayer playerRenderer, float scale, float partialTicks) {
		int eyeType = Math.max(-1, Math.min(Integer.parseInt(playerData.getData("vampire.eyes")), eyeOverlays.size() - 1));
		int fangType = Math.max(-1, Math.min(Integer.parseInt(playerData.getData("vampire.fangs")), fangOverlays.size() - 1));

		ResourceLocation fangs = null;
		ResourceLocation eyes = null;

		if (fangType > -1) {
			fangs = fangOverlays.get(fangType);
		}
		if (eyeType > -1) {
			eyes = eyeOverlays.get(eyeType);
		}

		if (player.isSneaking()) {
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}

		if (fangs != null) {
			playerRenderer.bindTexture(fangs);
			playerRenderer.getMainModel().bipedHead.render(scale);
		}

		if (eyes != null) {
			playerRenderer.bindTexture(eyes);
			playerRenderer.getMainModel().bipedHead.render(scale);
		}
	}

	@Override
	public void initSkillTree(SkillTree tree) {
		Level lvl1 = tree.addLevel();

		Level lvl2 = tree.addLevel();

		Level lvl3 = tree.addLevel();

		Level lvl4 = tree.addLevel();

		try {
			SkillHelper.createSkillChain(SkillSunScreen.class, true, tree, (constructorParams, skill) -> {
				constructorParams[0]=(int)constructorParams[0]+1;
				skill.setIcon(new ResourceLocation(Reference.MOD_ID, "textures/skills/vampire/test1.jpg"), 0, 0, 916, 873);
			}, 1);
			SkillHelper.createSkillChain(SkillHardTeeth.class, true, tree, (constructorParams, skill) -> {
				constructorParams[0]=(int)constructorParams[0]+1;
				skill.setIcon(new ResourceLocation(Reference.MOD_ID, "textures/skills/vampire/test2.png"), 0, 0, 35, 35);
			}, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
