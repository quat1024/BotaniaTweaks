package quaternary.botaniatweaks.modules.botania.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import quaternary.botaniatweaks.modules.botania.config.BotaniaConfig;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.BlockPylon;

public class BlockCustomManaPylon extends BlockPylon {
	@Override
	public float getEnchantPowerBonus(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() != this) return 8; //kinda strange behavior, but it emulates super, so...!
		
		switch (state.getValue(BotaniaStateProps.PYLON_VARIANT)) {
			case MANA: return BotaniaConfig.MANA_PYLON_ENCHANT_POWER;
			case NATURA: return BotaniaConfig.NATURA_PYLON_ENCHANT_POWER;
			case GAIA: return BotaniaConfig.GAIA_PYLON_ENCHANT_POWER;
		}
		
		return 8;
	}
}
