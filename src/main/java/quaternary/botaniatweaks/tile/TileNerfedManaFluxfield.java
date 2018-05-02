package quaternary.botaniatweaks.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.IEnergyStorage;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;
import vazkii.botania.api.mana.IManaReceiver;

public class TileNerfedManaFluxfield extends TileEntity implements IManaReceiver, ITickable {
	public static final int ENERGY_BUFFER_SIZE = 1000;
	public static final int MAX_EXTRACTION_RATE = 1600;
	
	int manaBuffer = 0;
	int energy = 0;
	
	public IEnergyStorage energyHandler = new IEnergyStorage() {		
		@Override
		public int getEnergyStored() {
			return energy;
		}
		
		@Override
		public int getMaxEnergyStored() {
			return ENERGY_BUFFER_SIZE;
		}
		
		@Override
		public boolean canExtract() {
			return true;
		}
		
		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			int toExtract = Math.min(MAX_EXTRACTION_RATE, Math.min(energy, maxExtract));
			if(!simulate) energy -= toExtract;
			return toExtract;
		}
		
		//Other mods cannot fill the fluxfield with energy...
		@Override
		public boolean canReceive() {
			return false;
		}
		
		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			return 0;
		}
	};
	
	//...but I can!
	//this method is more like an item handler than a power handler
	//it returns the amount of energy that did *not* fit
	int sneakyReceiveEnergy(int maxReceive, boolean simulate) {
		int freeSpace = ENERGY_BUFFER_SIZE - energy;
		if(maxReceive <= freeSpace) { //can it all fit?
			if(!simulate) energy += maxReceive;
			return 0;
		} else {
			//how much can fit?
			if(!simulate) energy = ENERGY_BUFFER_SIZE;
			return maxReceive - freeSpace;
		}
	}
	
	@Override
	public void update() {
		int manaThreshold = BotaniaTweaksConfig.MANA_SHOTS_PER_ENERGY_BURST / 160;
		while(manaBuffer >= manaThreshold) {
			int leftover = sneakyReceiveEnergy(BotaniaTweaksConfig.FE_PER_ENERGY_BURST, true);
			if(leftover == 0) {
				sneakyReceiveEnergy(BotaniaTweaksConfig.FE_PER_ENERGY_BURST, false);
				manaBuffer -= manaThreshold;
			} else {
				break;
			}
		}
	}
	
	@Override
	public boolean isFull() {
		return energy >= ENERGY_BUFFER_SIZE;
	}
	
	@Override
	public void recieveMana(int mana) {
		manaBuffer += mana;
	}
	
	@Override
	public boolean canRecieveManaFromBursts() {
		return true;
	}
	
	@Override
	public int getCurrentMana() {
		return manaBuffer;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("ManaBuffer", manaBuffer);
		nbt.setInteger("EnergyBuffer", energy);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		manaBuffer = nbt.getInteger("ManaBuffer");
		energy = nbt.getInteger("EnergyBuffer");
	}
}
