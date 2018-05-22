package quaternary.botaniatweaks.etc;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.IItemHandler;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.block.corporea.BlockCorporeaBase;
import vazkii.botania.common.entity.EntityCorporeaSpark;
import vazkii.botania.common.item.ItemCorporeaSpark;

@Mod.EventBusSubscriber(modid = BotaniaTweaks.MODID)
public class Events {
	
	@CapabilityInject(IItemHandler.class)
	public static final Capability<IItemHandler> ITEM_HANDLER_CAP = null;
	
	@GameRegistry.ObjectHolder("botania:corporeaspark")
	public static final Item SPORK = Items.AIR;
	
	@SubscribeEvent
	public static void place(BlockEvent.PlaceEvent e) {
		if(e.getWorld().isRemote) return;
		if(!BotaniaTweaksConfig.AUTO_CORPOREA_SPARK) return;
		if(e.getPlayer().isSneaking()) return;
		
		Block placedBlock = e.getPlacedBlock().getBlock();
		
		if(placedBlock instanceof BlockCorporeaBase) {
			ItemStack spork = findSpork(e.getPlayer().inventory);
			if(spork.isEmpty()) return;
			
			ItemStack dye = findDye(e.getPlayer().inventory);
			
			World world = e.getWorld();
			BlockPos pos = e.getPos();
			
			//Duplicate the corporea spark placement logic:
			
			TileEntity tile = world.getTileEntity(pos);
			if(tile == null) return; //Somehow?
			
			if((tile.hasCapability(ITEM_HANDLER_CAP, EnumFacing.UP) || tile.hasCapability(ITEM_HANDLER_CAP, null)) && !CorporeaHelper.doesBlockHaveSpark(world, pos)) {
				EntityCorporeaSpark spark = new EntityCorporeaSpark(world);
				
				spark.setPosition((double) pos.getX() + 0.5D, (double) pos.getY() + 1.5D, (double) pos.getZ() + 0.5D);
				
				if(!dye.isEmpty()) {
					spark.setNetwork(EnumDyeColor.byMetadata(dye.getMetadata()));
					dye.shrink(1);
				}
				
				world.spawnEntity(spark);
				spork.shrink(1);
				
				world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 0);
			}
		}
	}
	
	static ItemStack findSpork(InventoryPlayer inv) {		
		for(int i=0; i < 9; i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.getItem() instanceof ItemCorporeaSpark && stack.getItemDamage() == 0) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}
	
	static ItemStack findDye(InventoryPlayer inv) {
		for(int i=0; i < 9; i++) {
			ItemStack stack = inv.getStackInSlot(i);
			//Vazgoo why did you make it the same name as vanilla dye
			//Left fully qualified for clarity
			if(stack.getItem() instanceof vazkii.botania.common.item.material.ItemDye && stack.getItemDamage() != 0) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}
	
	@SubscribeEvent
	public static void joinWorld(EntityJoinWorldEvent e) {
		if(BotaniaTweaksConfig.SHEEP_EAT_ALT_GRASS && e.getEntity() instanceof EntitySheep) {
			EntitySheep sheep = (EntitySheep) e.getEntity();
			
			sheep.tasks.addTask(5, new EntityAIEatAltGrass(sheep));
		}
	}
}
