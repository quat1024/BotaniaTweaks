package quaternary.botaniatweaks.modules.botania.recipe;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import quaternary.botaniatweaks.modules.botania.config.BotaniaConfig;

import java.util.function.BooleanSupplier;

public class SporkConditionFactory implements IConditionFactory {
	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		boolean value = JsonUtils.getBoolean(json, "value", true);
		return () -> {
			return BotaniaConfig.SPORK == value;
		};
	}
}
