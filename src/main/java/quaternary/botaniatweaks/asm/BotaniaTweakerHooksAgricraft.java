package quaternary.botaniatweaks.asm;

import com.infinityraider.agricraft.blocks.BlockCrop;
import com.infinityraider.agricraft.tiles.TileEntityCrop;
import com.infinityraider.infinitylib.utility.WorldHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.item.IHornHarvestable;

public class BotaniaTweakerHooksAgricraft {
	public static boolean canHornHarvestAgriCrop(IHornHarvestable.EnumHornType dooter) {
		return dooter == IHornHarvestable.EnumHornType.WILD;
	}
	
	public static boolean hasSpecialHornHarvestAgriCrop(IHornHarvestable.EnumHornType dooter) {
		return dooter == IHornHarvestable.EnumHornType.WILD;
	}
	
	public static void harvestByHornAgriCrop(BlockCrop crop, World world, BlockPos pos, IHornHarvestable.EnumHornType dooter) {
		if(dooter != IHornHarvestable.EnumHornType.WILD) return;
		
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityCrop) {
			TileEntityCrop cropTile = (TileEntityCrop) tile;
			if(cropTile.isMature()) {
				//Null EntityPlayer here looks scary but it's marked @Nullable so
				cropTile.onHarvest((stack) -> WorldHelper.spawnItemInWorld(world, pos, stack), null);
			}
		}
	}
}
