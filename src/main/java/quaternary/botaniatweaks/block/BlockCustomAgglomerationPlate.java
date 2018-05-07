package quaternary.botaniatweaks.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.recipe.AgglomerationRecipes;
import quaternary.botaniatweaks.tile.TileCustomAgglomerationPlate;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.mana.BlockTerraPlate;
import vazkii.botania.common.core.BotaniaCreativeTab;

import javax.annotation.Nonnull;

//COPY from BlockTerraPlate.java. Changes noted.
public class BlockCustomAgglomerationPlate extends BlockTerraPlate implements ILexiconable {	
	public BlockCustomAgglomerationPlate() {
		BotaniaAPI.blacklistBlockFromMagnet(this, Short.MAX_VALUE);
		//ReflectionHelper.setPrivateValue(ModBlocks.class, null, this, "terraPlate");
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing s, float xs, float ys, float zs) {
		if(hand != EnumHand.MAIN_HAND) return false; //You're welcome for not clicking the shield on to the plate.
		
		//allow the player to place the item on the agglomeration plate with right click
		ItemStack heldStack = player.getHeldItem(hand);
		if(!AgglomerationRecipes.containsItem(heldStack)) return false;
		
		ItemStack oneHeld = heldStack.splitStack(1);
		EntityItem ent = new EntityItem(world, pos.getX() + .5, pos.getY() + 3/16d, pos.getZ() + .5, oneHeld);
		ent.motionX = 0; ent.motionY = 0; ent.motionZ = 0;
		world.spawnEntity(ent);
		return true;
	}
	
	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		//BOTANIATWEAKS: new tile entity
		return new TileCustomAgglomerationPlate();
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return 0; //for now todo
	}
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(tab == BotaniaTweaks.TAB || tab == BotaniaCreativeTab.INSTANCE) {
			super.getSubBlocks(tab, items);
		}
	}
}
