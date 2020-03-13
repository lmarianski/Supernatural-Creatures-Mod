package io.github.lukas2005.supernaturalcreatures.skill;

import io.github.lukas2005.supernaturalcreatures.player.capability.IPlayerData;
import io.github.lukas2005.supernaturalcreatures.skill.tree.SkillTree;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.*;

public class Skill {

	public static Map<String, Skill> skills = new HashMap<>();

	ResourceLocation icon;
	int iconU;
	int iconV;
	int iconWidth;
	int iconHeight;

	SkillTree tree;
	Level level;

	ResourceLocation uniqueId;

	List<Skill> dependencies = new ArrayList<>();
	List<Skill> dependants = new ArrayList<>();


	int cost;
	String description;

	public Skill(ResourceLocation uniqueId) {
		this.uniqueId = uniqueId;
		skills.put(uniqueId.toString(), this);
	}

	public Skill(String modid, String name) {
		this(new ResourceLocation(modid, name));
	}

	public Skill(String name) {
		this(new ResourceLocation(Reference.MOD_ID, name));
	}

	public Skill setIcon(ResourceLocation icon, int u, int v, int width, int height) {
		this.icon = icon;
		this.iconU = u;
		this.iconV = v;
		this.iconWidth = width;
		this.iconHeight = height;
		return this;
	}

	public Skill setDescription(String description) {
		this.description = description;
		return this;
	}

	public Skill setCost(int cost) {
		this.cost = cost;
		return this;
	}

	public int getCost() {
		return cost;
	}

	public Skill setRequiredSkills(Skill...skills) {
		dependencies.addAll(Arrays.asList(skills));
		for (Skill skill : skills) {
			skill.dependants.add(this);
		}
		return this;
	}

	@Override
	public String toString() {
		return uniqueId.toString();
	}

	public List<Skill> getDependants() {
		return dependants;
	}

	public List<Skill> getDependencies() {
		return dependencies;
	}

	//@SideOnly(Side.CLIENT)
	public void drawIcon(int x, int y, int width, int height) {
		if (icon != null) {
			Minecraft.getInstance().getTextureManager().bindTexture(icon);
			// TODO Gui.drawScaledCustomSizeModalRect(x, y, iconU, iconV, width, height, width, height, width, height);
		}
	}

	public void onPlayerTick(TickEvent.PlayerTickEvent e, IPlayerData playerData) {
	}

	/**
	 * A LivingHurtEvent but with checks if the attacker is a player
	 */
	public void onPlayerAttack(LivingHurtEvent e, PlayerEntity attacker, IPlayerData attackerData, LivingEntity victim) {
	}

	/**
	 * A LivingHurtEvent but with checks if the victim is a player
	 */
	public void onPlayerHurt(LivingHurtEvent e, LivingEntity attacker, PlayerEntity victim, IPlayerData victimData) {
	}

	/**
	 * A LivingHurtEvent but with checks if the victim and the attacker is a player. Called for the attacker.
	 * Note that both
	 * {@link #onPlayerHurt} and
	 * {@link #onPlayerAttack}
	 * will get called too (for respective entities)
	 */
	public void onPlayerAttackPlayer(LivingHurtEvent e, PlayerEntity attacker, IPlayerData attackerData, PlayerEntity victim, IPlayerData victimData) {
	}


	/**
	 * A LivingHurtEvent but with checks if the victim and the attacker is a player. Called for the victim.
	 * Note that both
	 * {@link #onPlayerHurt} and
	 * {@link #onPlayerAttack}
	 * will get called too (for respective entities)
	 */
	public void onPlayerHurtPlayer(LivingHurtEvent e, PlayerEntity attacker, IPlayerData attackerData, PlayerEntity victim, IPlayerData victimData) {
	}

	public String getTranslationKey() {
		return "skill."+uniqueId.toString().replaceAll("[:/]", ".");
	}

	//@SideOnly(Side.CLIENT)
	public String getLocalizedName() {
		return I18n.format(getTranslationKey());
	}
}
