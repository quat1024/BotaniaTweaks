package quaternary.botaniatweaks.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import quaternary.botaniatweaks.recipe.AgglomerationRecipe;
import quaternary.botaniatweaks.recipe.AgglomerationRecipes;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.*;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.entity.EntitySpark;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TileCustomAgglomerationPlate extends TileEntity implements ISparkAttachable, ITickable {
	int currentMana;
	int maxMana;
	
	@Override
	public void update() {
		if(world.isRemote) return;
		
		List<EntityItem> itemEntities = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos));
		if(itemEntities.isEmpty()) return;
		
		List<ItemStack> itemStacks = itemEntities.stream().map(EntityItem::getItem).collect(Collectors.toList());
		Optional<AgglomerationRecipe> optionalRecipe = AgglomerationRecipes.findMatchingRecipe(world, pos, itemStacks);
		
		if(optionalRecipe.isPresent()) {
			AgglomerationRecipe recipe = optionalRecipe.get();
			maxMana = recipe.getManaCost();
			
			//attract mana from nearby sporked pools
			ISparkEntity mySpork = getAttachedSpark();
			if(mySpork != null) {
				for(ISparkEntity otherSpork : SparkHelper.getSparksAround(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5)) {
					if(mySpork != otherSpork && otherSpork.getAttachedTile() instanceof IManaPool) {
						otherSpork.registerTransfer(mySpork);
					}
				}
			}
			
			//display Fanciness
			if(currentMana > 0) {
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
				//TODO: packet (regular one only works for regular tiles)
				//PacketHandler.sendToNearby(world, getPos(), new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.TERRA_PLATE, pos.getX(), pos.getY(), pos.getZ(), new int[0]));
			}
			
			if(currentMana >= maxMana) {
				//all done! spawn the item
				itemEntities.forEach(Entity::setDead);
				ItemStack output = recipe.getRecipeOutputCopy();
				EntityItem outputItem = new EntityItem(world, pos.getX() + .5, pos.getY() + .3, pos.getZ() + .5, output);
				outputItem.setVelocity(0, 0, 0); //No!
				world.spawnEntity(outputItem);
				
				//consume the blocks, if the recipe asked for it
				if(recipe.consumesCenter) {
					vanishBlock(world, pos.down());
				}
				
				if(recipe.consumesEdge) {
					for(EnumFacing horiz : EnumFacing.HORIZONTALS) {
						vanishBlock(world, pos.down().offset(horiz));
					}
				}
				
				if(recipe.consumesCorner) {
					for(EnumFacing horiz : EnumFacing.HORIZONTALS) {
						vanishBlock(world, pos.down().offset(horiz).offset(horiz.rotateY()));
					}
				}
				
				//fanciness
				world.playSound(null, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, ModSounds.terrasteelCraft, SoundCategory.BLOCKS, 1.0F, 1.0F);
				
				currentMana = 0;
				maxMana = 0;
			}
		} else {
			maxMana = 0;
		}
		
		//drain mana if there is no matching recipe
		if(maxMana == 0) recieveMana(0);
	}
	
	private void vanishBlock(World w, BlockPos pos) {
		w.playEvent(2001, pos, Block.getStateId(world.getBlockState(pos)));
		w.setBlockToAir(pos);
	}
	
	private void updateComparator() {
		world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
	}
	
	@Override
	public boolean canAttachSpark(ItemStack spork) {
		return true;
	}
	
	@Override
	public void attachSpark(ISparkEntity spork) {
		//No-op
	}
	
	@Override
	public int getAvailableSpaceForMana() {
		return Math.max(0, maxMana - currentMana);
	}
	
	@Override
	public ISparkEntity getAttachedSpark() {
		// *holds up spork
		List<Entity> sporks = world.getEntitiesWithinAABB(EntitySpark.class, new AxisAlignedBB(pos.up()));
		return sporks.size() == 1 ? (ISparkEntity) sporks.get(0) : null;
	}
	
	@Override
	public boolean areIncomingTranfersDone() {
		return maxMana == 0;
	}
	
	@Override
	public boolean isFull() {
		return currentMana >= maxMana;
	}
	
	@Override
	public void recieveMana(int i) {
		currentMana = MathHelper.clamp(currentMana + i, 0, maxMana);
		updateComparator();
	}
	
	@Override
	public boolean canRecieveManaFromBursts() {
		return maxMana != 0;
	}
	
	@Override
	public int getCurrentMana() {
		return currentMana;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("Mana", currentMana);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		currentMana = nbt.getInteger("Mana");
		super.readFromNBT(nbt);
	}
}
