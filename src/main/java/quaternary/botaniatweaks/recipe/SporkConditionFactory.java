package quaternary.botaniatweaks.recipe;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;

import java.util.function.BooleanSupplier;

public class SporkConditionFactory implements IConditionFactory {
	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		boolean value = JsonUtils.getBoolean(json, "value", true);
		return () -> {
			return BotaniaTweaksConfig.SPORK == value;
		};
	}
}
