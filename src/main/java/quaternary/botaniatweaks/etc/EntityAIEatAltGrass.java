package quaternary.botaniatweaks.etc;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class EntityAIEatAltGrass extends EntityAIEatGrass {
	
	public EntityAIEatAltGrass(EntityLiving eater) {
		super(eater);
	}
	
	@GameRegistry.ObjectHolder("botania:altgrass")
	public static final Block ALT_GRASS = Blocks.AIR;
	
	@Override
	public boolean shouldExecute() {
		if(grassEaterEntity.getRNG().nextInt(grassEaterEntity.isChild() ? 50 : 1000) != 0) {
			return false;
		} else {
			return grassEaterEntity.world.getBlockState(grassEaterEntity.getPosition().down()).getBlock() == ALT_GRASS;
		}
	}
	
	public void updateTask() {
		this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);
		
		if(this.eatingGrassTimer == 4) {
			BlockPos grassPos = grassEaterEntity.getPosition().down();
			
			if(grassEaterEntity.world.getBlockState(grassPos).getBlock() == ALT_GRASS) {
				if(net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(grassEaterEntity.world, this.grassEaterEntity)) {
					grassEaterEntity.world.playEvent(2001, grassPos, Block.getIdFromBlock(Blocks.GRASS));
					grassEaterEntity.world.setBlockState(grassPos, Blocks.DIRT.getDefaultState(), 2);
				}
				
				this.grassEaterEntity.eatGrassBonus();
			}
		}
	}
}
