/**
 * This class was originally created by <Vazkii>. It
 * was distributed as part of the Botania Mod. Get
 * the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * This class has been modified by <quaternary>, for
 * the purpose of adapting it to Botania Tweaks.
 *
 * File Created @ [Jul 18, 2014, 8:05:08 PM (GMT)]
 */
package quaternary.botaniatweaks.tile;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraftforge.items.ItemHandlerHelper;
import quaternary.botaniatweaks.block.BlockCompressedTinyPotato;
import quaternary.botaniatweaks.etc.Util;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.lib.LibMisc;

public class TileCompressedTinyPotato extends TileSimpleInventory implements ITickable {
	public int jumpTicks = 0;
	public String name = "";
	public int nextDoIt = 0;
	private static final String TAG_NAME = "name";
	
	public int compressionLevel;
	
	public TileCompressedTinyPotato() {
		this(-1);
	}
	
	public TileCompressedTinyPotato(int compressionLevel) {
		this.compressionLevel = compressionLevel;
	}
	
	public void interact(EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side) {		
		int index = side.getIndex();
		if(index >= 0) {
			ItemStack stackAt = getItemHandler().getStackInSlot(index).copy();
			if(!stackAt.isEmpty() && stack.isEmpty()) {
				player.setHeldItem(hand, stackAt);
				getItemHandler().setStackInSlot(index, ItemStack.EMPTY);
			} else if(!stack.isEmpty()) {
				ItemStack copy = stack.splitStack(1);
				
				if(stack.isEmpty())
					player.setHeldItem(hand, stackAt);
				else if(!stackAt.isEmpty()) {
					ItemHandlerHelper.giveItemToPlayer(player, stackAt);
				}
				
				getItemHandler().setStackInSlot(index, copy);
			}
		}
		
		jump();
		
		if(!world.isRemote) {
			if(name.toLowerCase().trim().endsWith("shia labeouf") && nextDoIt == 0) {
				nextDoIt = 40;
				world.playSound(null, pos, ModSounds.doit, SoundCategory.BLOCKS, 1F, 1F);
			}
			
			for(int i = 0; i < getSizeInventory(); i++) {
				ItemStack stackAt = getItemHandler().getStackInSlot(i);
				if(stackAt.isEmpty()) continue;
				Block blockAt = Block.getBlockFromItem(stackAt.getItem());
				int compressionAt = Util.getPotatoCompressionLevel(blockAt);
				if(compressionAt != -1) {
					Util.sendMeOrMySonChat(player, compressionAt, this.compressionLevel);
				}
			}
			
			PlayerHelper.grantCriterion((EntityPlayerMP) player, new ResourceLocation(LibMisc.MOD_ID, "main/tiny_potato_pet"), "code_triggered");
		}
	}
	
	public void jump() {
		if(jumpTicks == 0)
			jumpTicks = 20;
	}
	
	@Override
	public void update() {
		if(compressionLevel == -1) {
			Block b = world.getBlockState(pos).getBlock();
			if(b instanceof BlockCompressedTinyPotato) {
				compressionLevel = ((BlockCompressedTinyPotato) b).compressionLevel;
			}
		}
		if(world.rand.nextInt(100) == 0)
			jump();
		
		if(jumpTicks > 0)
			jumpTicks--;
		if(nextDoIt > 0)
			nextDoIt--;
	}
	
	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		super.writePacketNBT(cmp);
		cmp.setString(TAG_NAME, name);
	}
	
	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		super.readPacketNBT(cmp);
		name = cmp.getString(TAG_NAME);
	}
	
	@Override
	public int getSizeInventory() {
		return 6;
	}
}