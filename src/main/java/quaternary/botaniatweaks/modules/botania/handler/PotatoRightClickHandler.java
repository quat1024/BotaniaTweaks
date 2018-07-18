package quaternary.botaniatweaks.modules.botania.handler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.modules.botania.block.BlockCompressedTinyPotato;
import quaternary.botaniatweaks.modules.botania.block.BlockPottedTinyPotato;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;
import quaternary.botaniatweaks.etc.Util;
import vazkii.botania.common.block.decor.BlockTinyPotato;

public class PotatoRightClickHandler {
	@GameRegistry.ObjectHolder(BotaniaTweaks.MODID + ":potted_tiny_potato")
	public static final Block POTTED_TATER = Blocks.AIR;
	
	@SubscribeEvent
	public static void rightClick(PlayerInteractEvent.RightClickBlock e) {
		if(e.getWorld().isRemote) return;
		
		Block clickedBlock = e.getWorld().getBlockState(e.getPos()).getBlock();
		ItemStack held = e.getEntityPlayer().getHeldItem(e.getHand());
		Block heldBlock = Block.getBlockFromItem(held.getItem());
		
		if(BotaniaTweaksConfig.POTTED_TINY_POTATO && heldBlock instanceof BlockTinyPotato && clickedBlock instanceof BlockFlowerPot) {
			handlePottedPotato(held, e);
		}
		
		//Edge case: handle "me or my son" message when clicking a vanilla potato with a modded potato
		//This isn't usually handled since ofc Botania doesn't know about the compressed potatoes
		if(heldBlock instanceof BlockCompressedTinyPotato && clickedBlock instanceof BlockTinyPotato) {
			Util.sendMeOrMySonChat(e.getEntityPlayer(), Util.getPotatoCompressionLevel(heldBlock), 0);
		}
	}
	
	private static void handlePottedPotato(ItemStack held, PlayerInteractEvent.RightClickBlock e) {
		if(!e.getEntityPlayer().isCreative()) held.shrink(1);
		
		e.getWorld().setBlockState(e.getPos(), POTTED_TATER.getDefaultState().withProperty(BlockPottedTinyPotato.POTATO_FACING, e.getEntityPlayer().getHorizontalFacing().getOpposite()));
		
		//Prevent the potato from popping right back out again
		e.setUseBlock(Event.Result.DENY);
		//Prevent the player from placing another potato if they hold more than one
		e.setUseItem(Event.Result.DENY);
	}
}
