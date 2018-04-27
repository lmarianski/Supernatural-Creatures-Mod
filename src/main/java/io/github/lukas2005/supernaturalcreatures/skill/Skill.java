package io.github.lukas2005.supernaturalcreatures.skill;

import io.github.lukas2005.supernaturalcreatures.Reference;
import io.github.lukas2005.supernaturalcreatures.capabilities.IPlayerDataCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

	@SideOnly(Side.CLIENT)
	public void drawIcon(int x, int y, int width, int height) {
		if (icon != null) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(icon);
			Gui.drawScaledCustomSizeModalRect(x, y, iconU, iconV, width, height, width, height, width, height);
		}
	}

	public void onPlayerTick(TickEvent.PlayerTickEvent e, IPlayerDataCapability playerData) {
	}

	/**
	 * A LivingHurtEvent but with checks if the attacker is a player
	 */
	public void onPlayerAttack(LivingHurtEvent e, EntityPlayer attacker, IPlayerDataCapability attackerData, EntityLivingBase victim) {
	}

	/**
	 * A LivingHurtEvent but with checks if the victim is a player
	 */
	public void onPlayerHurt(LivingHurtEvent e, EntityLivingBase attacker, EntityPlayer victim, IPlayerDataCapability victimData) {
	}

	/**
	 * A LivingHurtEvent but with checks if the victim and the attacker is a player. Called for the attacker.
	 * Note that both
	 * {@link #onPlayerHurt} and
	 * {@link #onPlayerAttack}
	 * will get called too (for respective entities)
	 */
	public void onPlayerAttackPlayer(LivingHurtEvent e, EntityPlayer attacker, IPlayerDataCapability attackerData, EntityPlayer victim, IPlayerDataCapability victimData) {
	}


	/**
	 * A LivingHurtEvent but with checks if the victim and the attacker is a player. Called for the victim.
	 * Note that both
	 * {@link #onPlayerHurt} and
	 * {@link #onPlayerAttack}
	 * will get called too (for respective entities)
	 */
	public void onPlayerHurtPlayer(LivingHurtEvent e, EntityPlayer attacker, IPlayerDataCapability attackerData, EntityPlayer victim, IPlayerDataCapability victimData) {
	}

	public String getTranslationKey() {
		return "skill."+uniqueId.toString().replaceAll("[:/]", ".");
	}

	@SideOnly(Side.CLIENT)
	public String getLocalizedName() {
		return I18n.format(getTranslationKey());
	}
}
