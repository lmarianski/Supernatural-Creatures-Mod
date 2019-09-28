//package io.github.lukas2005.supernaturalcreatures.gui;
//
//import io.github.lukas2005.supernaturalcreatures.Reference;
//import io.github.lukas2005.supernaturalcreatures.player.capabilities.IPlayerDataCapability;
//import io.github.lukas2005.supernaturalcreatures.player.capabilities.ModCapabilities;
//import io.github.lukas2005.supernaturalcreatures.skill.Level;
//import io.github.lukas2005.supernaturalcreatures.skill.Skill;
//import net.minecraft.block.Block;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.client.gui.widget.button.Button;
//import net.minecraft.client.gui.widget.button.OptionButton;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.RenderHelper;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.client.renderer.texture.TextureMap;
//import net.minecraft.client.resources.I18n;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.init.Blocks;
//import net.minecraft.stats.AchievementList;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.text.StringTextComponent;
//import org.lwjgl.input.Mouse;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Random;
//
//public class GuiSkillTree extends Screen {
//	private static final int X_MIN = AchievementList.minDisplayColumn * 24 - 112;
//	private static final int Y_MIN = AchievementList.minDisplayRow * 24 - 112;
//	private static final int X_MAX = AchievementList.maxDisplayColumn * 24 - 77;
//	private static final int Y_MAX = AchievementList.maxDisplayRow * 24 - 77;
//	private static final ResourceLocation ACHIEVEMENT_BACKGROUND = new ResourceLocation("textures/gui/achievement/achievement_background.png");
//	protected int imageWidth = 256;
//	protected int imageHeight = 202;
//	protected int xLastScroll;
//	protected int yLastScroll;
//	protected float zoom = 1.0F;
//	protected double xScrollO;
//	protected double yScrollO;
//	protected double xScrollP;
//	protected double yScrollP;
//	protected double xScrollTarget;
//	protected double yScrollTarget;
//	private int scrolling;
//	private boolean loadingAchievements = true;
//
//	private PlayerEntity player;
//	private IPlayerDataCapability playerData;
//
//	public GuiSkillTree(PlayerEntity player) {
//		super(new StringTextComponent("Hi"));
//		this.player = player;
//		playerData = player.getCapability(ModCapabilities.PLAYER_DATA_CAPABILITY, null).orElse(null);
//
//		int i = 141;
//		int j = 141;
//		this.xScrollTarget = (double) (AchievementList.OPEN_INVENTORY.displayColumn * 24 - 70 - 12);
//		this.xScrollO = this.xScrollTarget;
//		this.xScrollP = this.xScrollTarget;
//		this.yScrollTarget = (double) (AchievementList.OPEN_INVENTORY.displayRow * 24 - 70);
//		this.yScrollO = this.yScrollTarget;
//		this.yScrollP = this.yScrollTarget;
//	}
//
//	/**
//	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
//	 * window resizes, the buttonList is cleared beforehand.
//	 */
//	public void initGui() {
//		this.loadingAchievements = false;
//		this.buttons.clear();
//		//this.buttons.add(new OptionButton(1, this.width / 2 + 24, this.height / 2 + 74, 80, 20, I18n.format("gui.done")));
//	}
//
//	/**
//	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
//	 */
//	protected void actionPerformed(Button button) {
//		if (!this.loadingAchievements) {
//			if (button.id == 1) {
//				playerData.syncData(player);
//				mc.setIngameFocus();
//			}
//		}
//	}
//
//	@Override
//	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
//		super.mouseClicked(mouseX, mouseY, mouseButton);
//
//		int i = MathHelper.floor_double(this.xScrollO + (this.xScrollP - this.xScrollO));
//		int j = MathHelper.floor_double(this.yScrollO + (this.yScrollP - this.yScrollO));
//
//		if (i < X_MIN) {
//			i = X_MIN;
//		}
//
//		if (j < Y_MIN) {
//			j = Y_MIN;
//		}
//
//		if (i >= X_MAX) {
//			i = X_MAX - 1;
//		}
//
//		if (j >= Y_MAX) {
//			j = Y_MAX - 1;
//		}
//
//		int k = (this.width - this.imageWidth) / 2;
//		int l = (this.height - this.imageHeight) / 2;
//		int i1 = k + 16;
//		int j1 = l + 17;
//
//		int y = 0;
//		for (Level level : playerData.getCreatureType().getBehaviour().getSkillTree().getLevels()) {
//			int x = 0;
//			for (Skill skill : level.skills) {
//				int x1 = x-i-23;
//				int y1 = y-j-120;
//
//				float w = (float)(mouseX - i1) * this.zoom;
//				float h = (float)(mouseY - j1) * this.zoom;
//
//				if (w >= x1 && w <= x1 + 22 && h >= y1 && h <= y1 + 22) {
//					if (!playerData.getSkills().contains(skill)) {
//						playerData.addSkill(skill, player);
//					} else {
//						playerData.removeSkill(skill);
//					}
//				}
//
//				x+=27;
//			}
//			y+=37;
//		}
//	}
//
//	/**
//	 * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
//	 * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
//	 */
//	protected void keyTyped(char typedChar, int keyCode) throws IOException {
//		if (this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) {
//			playerData.syncData(player);
//			this.mc.setIngameFocus();
//		} else {
//			super.keyTyped(typedChar, keyCode);
//		}
//	}
//
//	/**
//	 * Draws the screen and all the components in it.
//	 */
//	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//		if (this.loadingAchievements) {
//			this.drawDefaultBackground();
//			this.drawCenteredString(this.fontRendererObj, I18n.format("gui."+Reference.MOD_ID+".skillTree.downloadingData"), this.width / 2, this.height / 2, 16777215);
//			this.drawCenteredString(this.fontRendererObj, LOADING_STRINGS[(int) (Minecraft.getSystemTime() / 150L % (long) LOADING_STRINGS.length)], this.width / 2, this.height / 2 + this.fontRendererObj.FONT_HEIGHT * 2, 16777215);
//		} else {
//			if (Mouse.isButtonDown(0)) {
//				int i = (this.width - this.imageWidth) / 2;
//				int j = (this.height - this.imageHeight) / 2;
//				int k = i + 8;
//				int l = j + 17;
//
//				if ((this.scrolling == 0 || this.scrolling == 1) && mouseX >= k && mouseX < k + 224 && mouseY >= l && mouseY < l + 155) {
//					if (this.scrolling == 0) {
//						this.scrolling = 1;
//					} else {
//						this.xScrollP -= (double) ((float) (mouseX - this.xLastScroll) * this.zoom);
//						this.yScrollP -= (double) ((float) (mouseY - this.yLastScroll) * this.zoom);
//						this.xScrollO = this.xScrollP;
//						this.yScrollO = this.yScrollP;
//						this.xScrollTarget = this.xScrollP;
//						this.yScrollTarget = this.yScrollP;
//					}
//
//					this.xLastScroll = mouseX;
//					this.yLastScroll = mouseY;
//				}
//			} else {
//				this.scrolling = 0;
//			}
//
//			int i1 = Mouse.getDWheel();
//			float f2 = this.zoom;
//
//			if (i1 < 0) {
//				this.zoom += 0.25F;
//			} else if (i1 > 0) {
//				this.zoom -= 0.25F;
//			}
//
//			this.zoom = MathHelper.clamp_float(this.zoom, 1.0F, 2.0F);
//
//			if (this.zoom != f2) {
//				float f3 = f2 * (float) this.imageWidth;
//				float f4 = f2 * (float) this.imageHeight;
//				float f = this.zoom * (float) this.imageWidth;
//				float f1 = this.zoom * (float) this.imageHeight;
//				this.xScrollP -= (double) ((f - f3) * 0.5F);
//				this.yScrollP -= (double) ((f1 - f4) * 0.5F);
//				this.xScrollO = this.xScrollP;
//				this.yScrollO = this.yScrollP;
//				this.xScrollTarget = this.xScrollP;
//				this.yScrollTarget = this.yScrollP;
//			}
//
//			if (this.xScrollTarget < (double) X_MIN) {
//				this.xScrollTarget = (double) X_MIN;
//			}
//
//			if (this.yScrollTarget < (double) Y_MIN) {
//				this.yScrollTarget = (double) Y_MIN;
//			}
//
//			if (this.xScrollTarget >= (double) X_MAX) {
//				this.xScrollTarget = (double) (X_MAX - 1);
//			}
//
//			if (this.yScrollTarget >= (double) Y_MAX) {
//				this.yScrollTarget = (double) (Y_MAX - 1);
//			}
//
//			this.drawDefaultBackground();
//			this.drawAchievementScreen(mouseX, mouseY, partialTicks);
//			GlStateManager.disableLighting();
//			GlStateManager.disableDepth();
//			this.drawTitle();
//			GlStateManager.enableLighting();
//			GlStateManager.enableDepth();
//		}
//	}
//
//	public void doneLoading() {
//		if (this.loadingAchievements) {
//			this.loadingAchievements = false;
//		}
//	}
//
//	/**
//	 * Called from the main game loop to update the screen.
//	 */
//	public void updateScreen() {
//		if (!this.loadingAchievements) {
//			this.xScrollO = this.xScrollP;
//			this.yScrollO = this.yScrollP;
//			double d0 = this.xScrollTarget - this.xScrollP;
//			double d1 = this.yScrollTarget - this.yScrollP;
//
//			if (d0 * d0 + d1 * d1 < 4.0D) {
//				this.xScrollP += d0;
//				this.yScrollP += d1;
//			} else {
//				this.xScrollP += d0 * 0.85D;
//				this.yScrollP += d1 * 0.85D;
//			}
//		}
//	}
//
//	protected void drawTitle() {
//		int i = (this.width - this.imageWidth) / 2;
//		int j = (this.height - this.imageHeight) / 2;
//		this.fontRendererObj.drawString(I18n.format("gui."+Reference.MOD_ID+".skillTree.title"), i + 15, j + 5, 4210752);
//	}
//
//	protected void drawAchievementScreen(int mouseX, int mouseY, float partialTicks) {
//		int i = MathHelper.floor_double(this.xScrollO + (this.xScrollP - this.xScrollO) * (double) partialTicks);
//		int j = MathHelper.floor_double(this.yScrollO + (this.yScrollP - this.yScrollO) * (double) partialTicks);
//
//		if (i < X_MIN) {
//			i = X_MIN;
//		}
//
//		if (j < Y_MIN) {
//			j = Y_MIN;
//		}
//
//		if (i >= X_MAX) {
//			i = X_MAX - 1;
//		}
//
//		if (j >= Y_MAX) {
//			j = Y_MAX - 1;
//		}
//
//		int k = (this.width - this.imageWidth) / 2;
//		int l = (this.height - this.imageHeight) / 2;
//		int i1 = k + 16;
//		int j1 = l + 17;
//		this.zLevel = 0.0F;
//		GlStateManager.depthFunc(518);
//		GlStateManager.pushMatrix();
//		GlStateManager.translate((float) i1, (float) j1, -200.0F);
//		// FIXES models rendering weirdly in the acheivements pane
//		// see https://github.com/MinecraftForge/MinecraftForge/commit/1b7ce7592caafb760ec93066184182ae0711e793#commitcomment-10512284
//		GlStateManager.scale(1.0F / this.zoom, 1.0F / this.zoom, 1.0F);
//		GlStateManager.enableTexture2D();
//		GlStateManager.disableLighting();
//		GlStateManager.enableRescaleNormal();
//		GlStateManager.enableColorMaterial();
//
//		int k1 = i + 288 >> 4;
//		int l1 = j + 288 >> 4;
//		int i2 = (i + 288) % 16;
//		int j2 = (j + 288) % 16;
//
//		Random random = new Random();
//		float f = 16.0F / this.zoom;
//		float f1 = 16.0F / this.zoom;
//
//		for (int l3 = 0; (float) l3 * f - (float) j2 < 155.0F; ++l3) {
//			float f2 = 0.6F - (float) (l1 + l3) / 25.0F * 0.3F;
//			GlStateManager.color(f2, f2, f2, 1.0F);
//
//			for (int i4 = 0; (float) i4 * f1 - (float) i2 < 224.0F; ++i4) {
//				random.setSeed((long) (this.mc.getSession().getPlayerID().hashCode() + k1 + i4 + (l1 + l3) * 16));
//				int j4 = random.nextInt(1 + l1 + l3) + (l1 + l3) / 2;
//				TextureAtlasSprite textureatlassprite = null;
//
//				if (j4 <= 37 && l1 + l3 != 35) {
//					if (j4 == 22) {
//						if (random.nextInt(2) == 0) {
//							textureatlassprite = this.getTexture(Blocks.DIAMOND_ORE);
//						} else {
//							textureatlassprite = this.getTexture(Blocks.REDSTONE_ORE);
//						}
//					} else if (j4 == 10) {
//						textureatlassprite = this.getTexture(Blocks.IRON_ORE);
//					} else if (j4 == 8) {
//						textureatlassprite = this.getTexture(Blocks.COAL_ORE);
//					} else if (j4 > 4) {
//						textureatlassprite = this.getTexture(Blocks.STONE);
//					} else if (j4 > 0) {
//						textureatlassprite = this.getTexture(Blocks.DIRT);
//					}
//				} else {
//					textureatlassprite = this.getTexture(Blocks.BEDROCK);
//				}
//
//				this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//				this.drawTexturedModalRect(i4 * 16 - i2, l3 * 16 - j2, textureatlassprite, 16, 16);
//			}
//		}
//
//		GlStateManager.enableDepth();
//		GlStateManager.depthFunc(515);
//
//		int y = 0;
//
//		Skill toolTipSkill = null;
//
//		for (Level level : playerData.getCreatureType().getBehaviour().getSkillTree().getLevels()) {
//			int x = 0;
//			for (Skill skill : level.skills) {
//				int x1 = x-i-23;
//				int y1 = y-j-120;
//
//				for (Skill skillDep : skill.getDependants()) {
//					int color = -16777216;
//
//					if (playerData.getSkills().contains(skillDep)) {
//						color = -6250336;
//					} else if (playerData.getSkills().containsAll(skillDep.getDependencies())) {
//						color = -16711936;
//					}
//
//					drawVerticalLine(x1+11, y1+20, y1+49, color);
//				}
//
//				if (playerData.getSkills().contains(skill)) {
//					GlStateManager.color(0.75F, 0.75F, 0.75F, 1.0F);
//				} else if (playerData.getSkills().containsAll(skill.getDependencies())) {
//					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//				} else {
//					GlStateManager.color(0.2F, 0.2F, 0.2F, 1.0F);
//				}
//
//				this.mc.getTextureManager().bindTexture(ACHIEVEMENT_BACKGROUND);
//				this.drawTexturedModalRect(x1, y1,2,204, 22, 22);
//				//skill.drawIcon(x1, y1, 22, 22);
//
//				float w = (float)(mouseX - i1) * this.zoom;
//				float h = (float)(mouseY - j1) * this.zoom;
//
//				if (w >= x1 && w <= x1 + 22 && h >= y1 && h <= y1 + 22) {
//					toolTipSkill = skill;
//				}
//
//				x+=27;
//			}
//			y+=37;
//		}
//
//		GlStateManager.disableDepth();
//		GlStateManager.enableBlend();
//		GlStateManager.popMatrix();
//		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//		this.mc.getTextureManager().bindTexture(ACHIEVEMENT_BACKGROUND);
//		this.drawTexturedModalRect(k, l, 0, 0, this.imageWidth, this.imageHeight);
//		this.zLevel = 0.0F;
//		GlStateManager.depthFunc(515);
//		GlStateManager.disableDepth();
//		GlStateManager.enableTexture2D();
//		super.drawScreen(mouseX, mouseY, partialTicks);
//
//		GlStateManager.enableDepth();
//		GlStateManager.enableLighting();
//		RenderHelper.disableStandardItemLighting();
//
//		if (toolTipSkill != null) {
//			StringBuilder builder = new StringBuilder();
//
//			builder.append(toolTipSkill.getLocalizedName());
//
//			renderToolTip(builder, mouseX, mouseY);
//		}
//	}
//
//	private TextureAtlasSprite getTexture(Block blockIn) {
//		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(blockIn.getDefaultState());
//	}
//
//	/**
//	 * Returns true if this GUI should pause the game when it is displayed in single-player
//	 */
//	public boolean doesGuiPauseGame() {
//		return !this.loadingAchievements;
//	}
//
//	protected void renderToolTip(StringBuilder builder, int x, int y) {
//		this.drawHoveringText(Arrays.asList(builder.toString().split("\n")), x, y);
//	}
//}