package quaternary.botaniatweaks.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.IEnergyStorage;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;
import vazkii.botania.api.mana.IManaReceiver;

import javax.annotation.Nullable;

public class TileNerfedManaFluxfield extends TileEntity implements IManaReceiver, ITickable {
	static final int ENERGY_BUFFER_SIZE = 1000;
	static final int MAX_EXTRACTION_RATE = 1600;
	
	int manaBuffer = 0;
	static final int MAX_MANA_BUFFER = 10000;
	
	public EnergyHandler handler = new EnergyHandler();
	
	class EnergyHandler implements IEnergyStorage {
		int energy = 0;
		
		@Override
		public int getEnergyStored() {
			return energy;
		}
		
		void setEnergyStored(int energy) {
			this.energy = energy;
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
		
		//but I can!
		int sneakyReceiveEnergy(int maxReceive, boolean simulate) {
			int freeSpace = getMaxEnergyStored() - energy;
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
		public int receiveEnergy(int maxReceive, boolean simulate) {
			return 0;
		}
	}
	
	@Override
	public void update() {		
		int manaThreshold = BotaniaTweaksConfig.MANA_SHOTS_PER_ENERGY_BURST * 160;
		while(manaBuffer >= manaThreshold) {
			int leftover = handler.sneakyReceiveEnergy(BotaniaTweaksConfig.FE_PER_ENERGY_BURST, true);
			if(leftover == 0) {
				handler.sneakyReceiveEnergy(BotaniaTweaksConfig.FE_PER_ENERGY_BURST, false);
				manaBuffer -= manaThreshold;
			} else {
				break;
			}
		}
	}
	
	@Override
	public boolean isFull() {
		return handler.getEnergyStored() >= ENERGY_BUFFER_SIZE;
	}
	
	@Override
	public void recieveMana(int mana) {
		manaBuffer += mana;
	}
	
	@Override
	public boolean canRecieveManaFromBursts() {
		return manaBuffer <= MAX_MANA_BUFFER;
	}
	
	@Override
	public int getCurrentMana() {
		return manaBuffer;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("ManaBuffer", manaBuffer);
		nbt.setInteger("EnergyBuffer", handler.getEnergyStored());
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		manaBuffer = nbt.getInteger("ManaBuffer");
		handler.setEnergyStored(nbt.getInteger("EnergyBuffer"));
	}
	
	@CapabilityInject(IEnergyStorage.class)
	public static final Capability<IEnergyStorage> ENERGY_CAP = null;
	
	@Override
	@SuppressWarnings("ConstantConditions")
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		if(capability == ENERGY_CAP) return true;
		else return super.hasCapability(capability, facing);
	}
	
	@Nullable
	@Override
	@SuppressWarnings("ConstantConditions")
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if(capability == ENERGY_CAP) return ENERGY_CAP.cast(handler);
		else return super.getCapability(capability, facing);
	}
}
