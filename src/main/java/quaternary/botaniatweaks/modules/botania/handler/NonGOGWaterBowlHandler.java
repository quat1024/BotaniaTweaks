package quaternary.botaniatweaks.modules.botania.handler;

import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import quaternary.botaniatweaks.modules.botania.config.BotaniaConfig;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

public class NonGOGWaterBowlHandler {
	@SubscribeEvent
	public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
		//This method is pretty much a gigantic copypasta from SkyblockWorldEvents.java.
		//Changes noted.
		
		//Tweaks: check config value, not whether GOG is loaded.
		if(!Botania.gardenOfGlassLoaded && BotaniaConfig.NON_GOG_WATER_BOWL) {
			ItemStack equipped = event.getItemStack();
			//Tweaks: remove the pebbles mechanic block, this isn't about pebbles
			if(!equipped.isEmpty() && equipped.getItem() == Items.BOWL) {
				RayTraceResult rtr = ToolCommons.raytraceFromEntity(event.getWorld(), event.getEntityPlayer(), true, 4.5F);
				if(rtr != null) {
					if (rtr.typeOfHit == net.minecraft.util.math.RayTraceResult.Type.BLOCK) {
						if(event.getWorld().getBlockState(rtr.getBlockPos()).getMaterial() == Material.WATER) {
							if(!event.getWorld().isRemote) {
								equipped.shrink(1);
								
								if(equipped.isEmpty())
									event.getEntityPlayer().setHeldItem(event.getHand(), new ItemStack(ModItems.waterBowl));
								else ItemHandlerHelper.giveItemToPlayer(event.getEntityPlayer(), new ItemStack(ModItems.waterBowl));
							}
							
							event.setCanceled(true);
							event.setCancellationResult(EnumActionResult.SUCCESS);
						}
					}
				}
			}
		}
	}
}
