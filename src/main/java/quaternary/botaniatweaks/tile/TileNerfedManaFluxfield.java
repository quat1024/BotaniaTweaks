package quaternary.botaniatweaks.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.IEnergyStorage;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;
import vazkii.botania.api.mana.IManaReceiver;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileNerfedManaFluxfield extends TileEntity implements IManaReceiver, ITickable {
	static int energyBufferSize = 10000;
	static final int MAX_EXTRACTION_RATE = 1600;
	
	int manaBuffer = 0;
	static final int MAX_MANA_BUFFER = 10000;
	
	public EnergyHandler handler = new EnergyHandler();
	
	@CapabilityInject(IEnergyStorage.class)
	public static final Capability<IEnergyStorage> ENERGY_CAP = null;
	
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
			return energyBufferSize;
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
				if(!simulate) energy = energyBufferSize;
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
		
		energyBufferSize = BotaniaTweaksConfig.FE_PER_ENERGY_BURST * 10;
		
		while(manaBuffer >= manaThreshold) {
			int leftover = handler.sneakyReceiveEnergy(BotaniaTweaksConfig.FE_PER_ENERGY_BURST, true);
			if(leftover == 0) {
				handler.sneakyReceiveEnergy(BotaniaTweaksConfig.FE_PER_ENERGY_BURST, false);
				manaBuffer -= manaThreshold;
			} else {
				break;
			}
		}
		
		//try to empty into nearby FE receivers
		//first, count how many are nearby
		ArrayList<IEnergyStorage> nearbyCaps = new ArrayList<>();
		for(EnumFacing whichWay : EnumFacing.values()) {
			BlockPos checkPos = pos.offset(whichWay);
			TileEntity tile = world.getTileEntity(checkPos);
			if(tile != null) {
				IEnergyStorage cap = null;
				if(tile.hasCapability(ENERGY_CAP, null)) cap = tile.getCapability(ENERGY_CAP, null);
				if(tile.hasCapability(ENERGY_CAP, whichWay.getOpposite())) cap = tile.getCapability(ENERGY_CAP, whichWay.getOpposite());
				if(cap != null) nearbyCaps.add(cap);
			}
		}
		
		//next, distribute power evenly among them, if possible
		if(nearbyCaps.size() == 0) return;
		
		int powerToDistribute = handler.getEnergyStored() / nearbyCaps.size();
		for(IEnergyStorage cap : nearbyCaps) {
			int wouldReceivedPower = cap.receiveEnergy(powerToDistribute, true);
			if(wouldReceivedPower != 0) {
				cap.receiveEnergy(wouldReceivedPower, false);
				handler.extractEnergy(wouldReceivedPower, false);
			}
		}
	}
	
	@Override
	public boolean isFull() {
		return handler.getEnergyStored() >= energyBufferSize;
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
