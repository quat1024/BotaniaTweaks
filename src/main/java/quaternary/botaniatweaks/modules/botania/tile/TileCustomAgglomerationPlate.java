package quaternary.botaniatweaks.modules.botania.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.items.ItemHandlerHelper;
import quaternary.botaniatweaks.modules.botania.config.BotaniaConfig;
import quaternary.botaniatweaks.modules.botania.net.PacketCustomTerraPlate;
import quaternary.botaniatweaks.modules.botania.recipe.AgglomerationRecipe;
import quaternary.botaniatweaks.modules.botania.recipe.AgglomerationRecipes;
import quaternary.botaniatweaks.modules.shared.net.BotaniaTweaksPacketHandler;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.*;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.entity.EntitySpark;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TileCustomAgglomerationPlate extends TileEntity implements ISparkAttachable, ITickable {
	int currentMana;
	int maxMana;
	
	@Override
	public void update() {
		if(world.isRemote) return;
		
		world.profiler.startSection("botaniatweaks_agglo_plate");
		
		world.profiler.startSection("discoverStacks");
		List<EntityItem> itemEntities = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos));
		if(itemEntities.isEmpty()) {
			world.profiler.endSection();
			world.profiler.endSection();
			return;
		}
		
		List<ItemStack> itemStacks = itemEntities.stream().map(EntityItem::getItem).collect(Collectors.toList());
		world.profiler.endSection();
		
		//Hack: "collect" the item stacks together.
		//This makes it work better with multiple copies of a stackable item in recipes.
		//E.g. two separate bricks now is treated the same as 2 stacked bricks.
		if(BotaniaConfig.PROCESS_CUSTOM_AGGLO_STACKS) {
			world.profiler.startSection("collectStacks");
			List<ItemStack> collectedStacks = new ArrayList<>();
			
			for(ItemStack stack : itemStacks) {
				boolean matched = false;
				for(ItemStack collect : collectedStacks) {
					if(ItemHandlerHelper.canItemStacksStack(stack, collect)) {
						collect.grow(stack.getCount());
						matched = true;
						break;
					}
				}
				
				if(!matched) {
					collectedStacks.add(stack.copy());
				}
			}
			itemStacks = collectedStacks;
			world.profiler.endSection();
		}
		
		world.profiler.startSection("recipeMatching");
		Optional<AgglomerationRecipe> optionalRecipe = AgglomerationRecipes.findMatchingRecipe(world, pos, itemStacks);
		world.profiler.endSection();
		
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
				
				IMessage packet = new PacketCustomTerraPlate(pos, recipe.color1, recipe.color2, (float) currentMana / maxMana);
				BotaniaTweaksPacketHandler.sendToAllAround(packet, world, pos);
			}
			
			if(currentMana >= maxMana) {
				//all done! spawn the item
				itemEntities.forEach(Entity::setDead);
				ItemStack output = recipe.getRecipeOutputCopy();
				EntityItem outputItem = new EntityItem(world, pos.getX() + .5, pos.getY() + .3, pos.getZ() + .5, output);
                int bucketCount = recipe.getBucketCount();
				
				//Make it not literally jump off the plate
				outputItem.motionX = 0;
				outputItem.motionY = 0;
				outputItem.motionZ = 0;
				
				world.spawnEntity(outputItem);

                if (bucketCount > 0) {
                    EntityItem bucketOutput = new EntityItem(world, pos.getX() + .5, pos.getY() + .3, pos.getZ() + .5, new ItemStack(Items.BUCKET, bucketCount, 0));
                    bucketOutput.motionX = 0;
                    bucketOutput.motionY = 0;
                    bucketOutput.motionZ = 0;

                    world.spawnEntity(bucketOutput);
                }
				
				//consume/replace the blocks, if the recipe asked for it
				if(recipe.multiblockCenterReplace != null) {
					replaceBlock(world, pos.down(), recipe.multiblockCenterReplace);
				}
				
				if(recipe.multiblockEdgeReplace != null) {
					for(EnumFacing horiz : EnumFacing.HORIZONTALS) {
						replaceBlock(world, pos.down().offset(horiz), recipe.multiblockEdgeReplace);
					}
				}
				
				if(recipe.multiblockCornerReplace != null) {
					for(EnumFacing horiz : EnumFacing.HORIZONTALS) {
						replaceBlock(world, pos.down().offset(horiz).offset(horiz.rotateY()), recipe.multiblockCornerReplace);
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
		
		world.profiler.endSection();
	}
	
	public boolean isCrafting() {
		return maxMana != 0;
	}
	
	public double getCraftingPercentage() {
		if(!isCrafting()) return 0;
		else return Math.min(1, (double) currentMana / maxMana);
	}
	
	private void replaceBlock(World w, BlockPos pos, IBlockState replace) {
		w.playEvent(2001, pos, Block.getStateId(world.getBlockState(pos)));
		w.setBlockState(pos, replace, 3);
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
