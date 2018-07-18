package quaternary.botaniatweaks.modules.extendedcrafting;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import quaternary.botaniatweaks.modules.shared.block.AbstractBlockCompatCrate;
import quaternary.botaniatweaks.modules.shared.tile.AbstractTileCompatCrate;
import vazkii.botania.api.lexicon.LexiconEntry;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class BlockExtCraftCrate extends AbstractBlockCompatCrate {
	public BlockExtCraftCrate(Supplier<AbstractTileCompatCrate<IRecipe>> tileSupplier) {
		this.tileSupplier = tileSupplier;
	}
	
	private final Supplier<AbstractTileCompatCrate<IRecipe>> tileSupplier;
	
	@Override
	public LexiconEntry getEntry(World world, BlockPos blockPos, EntityPlayer entityPlayer, ItemStack itemStack) {
		return ExtendedCraftingCompat.extCrateEntry;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return tileSupplier.get();
	}
}
