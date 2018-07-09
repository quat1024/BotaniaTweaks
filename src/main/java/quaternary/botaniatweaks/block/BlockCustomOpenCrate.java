package quaternary.botaniatweaks.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;
import quaternary.botaniatweaks.etc.IBotaniaTweaked;
import quaternary.botaniatweaks.tile.TileCustomCraftyCrate;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.CrateVariant;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.BlockOpenCrate;
import vazkii.botania.common.block.tile.TileOpenCrate;

import javax.annotation.Nonnull;

public class BlockCustomOpenCrate extends BlockOpenCrate implements IBotaniaTweaked {
	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		if(state.getValue(BotaniaStateProps.CRATE_VARIANT) == CrateVariant.OPEN) return new TileOpenCrate();
		else return new TileCustomCraftyCrate();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, BlockPos pos) {
		if(BotaniaTweaksConfig.ADVANCED_CRAFTY_CRATE) {
			TileEntity tile = world.getTileEntity(pos);
			
			if(tile instanceof TileCustomCraftyCrate) {
				TileCustomCraftyCrate ccrate = (TileCustomCraftyCrate) tile;
				
				GlStateManager.pushMatrix();
				GlStateManager.translate(res.getScaledWidth() / 2, res.getScaledHeight() / 2, 0);
				GlStateManager.translate(-5, 45, 0);
				
				HUDHandler.renderManaBar(0, 0, 0x56d8a2, 0.75f, ccrate.getCurrentMana(), ccrate.getMaxMana());
				
				float progress = ((float) ccrate.getManaPerItem() / ccrate.getMaxMana()) * ccrate.getItemCount() * 100;
				
				Tessellator t = Tessellator.getInstance();
				BufferBuilder buf = t.getBuffer();
				
				GlStateManager.disableTexture2D();
				GlStateManager.disableAlpha();
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				GlStateManager.glLineWidth(4);
				GlStateManager.shadeModel(GL11.GL_SMOOTH);
				
				buf.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
				buf.pos(progress, 0, 0).color(86 / 255f, 216 / 255f, 162 / 255f, 1.0f).endVertex();
				buf.pos(progress, -20, 0).color(86 / 255f, 216 / 255f, 162 / 255f, 0.0f).endVertex();
				t.draw();
				
				GlStateManager.shadeModel(GL11.GL_FLAT);
				GlStateManager.glLineWidth(1);
				GlStateManager.disableBlend();
				GlStateManager.enableAlpha();
				GlStateManager.enableTexture2D();
				
				GlStateManager.popMatrix();
			}
		}
		
		//Render this afterwards, idk there's some weird shit goin on with the item rendering
		//If I do this after drawing the mana bar it doesn't state leak and fuck up the transparency
		//It's 2am I don't want to find the stateleak.
		super.renderHUD(mc, res, world, pos);
	}
	
	@Override
	public boolean isTweaked() {
		return true;
	}
}
