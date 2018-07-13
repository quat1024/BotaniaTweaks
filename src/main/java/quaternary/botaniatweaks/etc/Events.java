package quaternary.botaniatweaks.etc;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.IItemHandler;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.block.BlockCompressedTinyPotato;
import quaternary.botaniatweaks.block.BlockPottedTinyPotato;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;
import quaternary.botaniatweaks.etc.advancement.AdvancementHandler;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.block.corporea.BlockCorporeaBase;
import vazkii.botania.common.block.decor.BlockTinyPotato;
import vazkii.botania.common.entity.EntityCorporeaSpark;
import vazkii.botania.common.item.ItemCorporeaSpark;

import java.util.List;

public class Events {
	@CapabilityInject(IItemHandler.class)
	public static final Capability<IItemHandler> ITEM_HANDLER_CAP = null;
	
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
		for(int i = 0; i < 9; i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.getItem() instanceof ItemCorporeaSpark && stack.getItemDamage() == 0) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}
	
	static ItemStack findDye(InventoryPlayer inv) {
		for(int i = 0; i < 9; i++) {
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
		
		if(e.getEntity() instanceof EntityTNTPrimed) {
			processTNTDupe((EntityTNTPrimed) e.getEntity());
		}
	}
	
	@SubscribeEvent
	public static void update(TickEvent.WorldTickEvent e) {
		if(e.world.isRemote || e.phase == TickEvent.Phase.START) return;
		e.world.getEntities(EntityTNTPrimed.class, (ent) -> true).forEach(Events::processTNTDupe);
		
		if(BotaniaTweaksConfig.MANA_GENERATION_STATISTICS && e.world.getTotalWorldTime() % 200 == 0) {
			AdvancementHandler.handle(e.world);
		}
	}
	
	static void processTNTDupe(EntityTNTPrimed tnt) {
		World world = tnt.world;
		if(tnt.getFuse() != 79) return;
		
		BlockPos tntPos = tnt.getPosition();
		
		int score = 0;
		
		//TNT duplicators usually involve slime blocks, detector rails, and minecarts
		Iterable<BlockPos.MutableBlockPos> nearbyPositions = BlockPos.getAllInBoxMutable(tntPos.add(-1, -1, -1), tntPos.add(1, 1, 1));
		
		for(BlockPos pos : nearbyPositions) {
			Block b = getBlockOrMovingBlock(world, pos);
			if(b == Blocks.SLIME_BLOCK) score++;
			if(b == Blocks.DETECTOR_RAIL) score += 5;
		}
		
		List<EntityMinecart> nearbyCarts = world.getEntitiesWithinAABB(EntityMinecart.class, new AxisAlignedBB(tntPos.add(-2, -2, -2), tntPos.add(2, 2, 2)));
		if(!nearbyCarts.isEmpty()) {
			score += 3;
			
			for(EntityMinecart cart : nearbyCarts) {
				Block atCart = getBlockOrMovingBlock(world, cart.getPosition());
				Block belowCart = getBlockOrMovingBlock(world, cart.getPosition().down());
				
				if(atCart == Blocks.DETECTOR_RAIL) score += 4;
				if(belowCart == Blocks.DETECTOR_RAIL) score += 4;
			}
		}
		
		if(score >= BotaniaTweaksConfig.TNT_DUPE_HEURISTIC) tnt.getTags().add("CheatyDupe");
	}
	
	static Block getBlockOrMovingBlock(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityPiston) {
			return ((TileEntityPiston) tile).getPistonState().getBlock();
		} else return world.getBlockState(pos).getBlock();
	}
	
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
		
		if(heldBlock instanceof BlockCompressedTinyPotato && clickedBlock instanceof BlockTinyPotato) {
			Util.sendMeOrMySonChat(e.getEntityPlayer(), Util.getPotatoCompressionLevel(heldBlock), Util.getPotatoCompressionLevel(clickedBlock));
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
