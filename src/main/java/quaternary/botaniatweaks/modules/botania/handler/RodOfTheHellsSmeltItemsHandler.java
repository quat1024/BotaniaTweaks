package quaternary.botaniatweaks.modules.botania.handler;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.ModItems;

import java.util.List;

public class RodOfTheHellsSmeltItemsHandler {
	private static final int MANA_COST_PER_ITEM = 500; //It's expensive to encourage actual furnaces ;)
	private static final int RADIUS = 5; //from EntityFlameRing
	
	private static final int RADIUS_SQ = RADIUS * RADIUS;
	private static final double HALF_RADIUS = RADIUS / 2d;
	
	@SubscribeEvent
	public static void rightClick(PlayerInteractEvent.RightClickBlock e) {
		//Todo Config!
		
		ItemStack stack = e.getItemStack();
		if(stack.getItem() == ModItems.fireRod) {
			World world = e.getWorld();
			if(world.isRemote) return; //?
			
			EntityPlayer player = e.getEntityPlayer();
			if(player.getCooldownTracker().hasCooldown(stack.getItem())) return;
			
			Vec3d hit = e.getHitVec();
			
			AxisAlignedBB aabb = new AxisAlignedBB(hit.addVector(-HALF_RADIUS, -.5, -HALF_RADIUS), hit.addVector(HALF_RADIUS, .5, HALF_RADIUS));
			List<EntityItem> nearbyItemEntities = world.getEntitiesWithinAABB(EntityItem.class, aabb);
			
			fail:
			for(EntityItem ent : nearbyItemEntities) {
				double distSq = (ent.posX - hit.x * ent.posX - hit.x) + (ent.posZ - hit.z * ent.posZ - hit.z);
				if(distSq >= RADIUS_SQ) continue;
				
				ItemStack stackToSmelt = ent.getItem();
				
				ItemStack smelted = FurnaceRecipes.instance().getSmeltingResult(stackToSmelt); //Do not mutate!!!!!!!
				if(smelted.isEmpty()) continue;
				
				int count = stackToSmelt.getCount();
				for(int i = 0; i < count; i++) {
					if(ManaItemHandler.requestManaExactForTool(stack, player, MANA_COST_PER_ITEM, false)) {
						ManaItemHandler.requestManaExactForTool(stack, player, MANA_COST_PER_ITEM, true);
						EntityItem smeltedEnt = new EntityItem(world, ent.posX, ent.posY, ent.posZ, smelted.copy());
						smeltedEnt.motionX = 0;
						smeltedEnt.motionY = 0;
						smeltedEnt.motionZ = 0;
						//TODO: This lags pretty hard lmao.
						world.spawnEntity(smeltedEnt);
						stackToSmelt.shrink(1);
					} else break fail; //Out of mana. Might as well just stop checking
				}
			}
		}
	}
}
