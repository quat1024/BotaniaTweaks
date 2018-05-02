package quaternary.botaniatweaks.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;

public class TileCustomAgglomerationPlate extends TileEntity implements ISparkAttachable, ITickable {
	@Override
	public void update() {
		
	}
	
	@Override
	public boolean canAttachSpark(ItemStack itemStack) {
		return false;
	}
	
	@Override
	public void attachSpark(ISparkEntity iSparkEntity) {
		
	}
	
	@Override
	public int getAvailableSpaceForMana() {
		return 0;
	}
	
	@Override
	public ISparkEntity getAttachedSpark() {
		return null;
	}
	
	@Override
	public boolean areIncomingTranfersDone() {
		return false;
	}
	
	@Override
	public boolean isFull() {
		return false;
	}
	
	@Override
	public void recieveMana(int i) {
		
	}
	
	@Override
	public boolean canRecieveManaFromBursts() {
		return false;
	}
	
	@Override
	public int getCurrentMana() {
		return 0;
	}
}
