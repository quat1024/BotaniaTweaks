package quaternary.botaniatweaks.compat.shared;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.*;
import java.util.function.Consumer;

public class OptionalExtensions {
	public static List<Consumer<IForgeRegistry<Block>>> BLOCK_CALLBACKS = new ArrayList<>();
	public static List<Consumer<IForgeRegistry<Item>>> ITEM_CALLBACKS = new ArrayList<>();
	public static List<Runnable> MODEL_CALLBACKS = new ArrayList<>();
	public static List<Runnable> LEXICON_CALLBACKS = new ArrayList<>();
	
	public static void callBlocks(IForgeRegistry<Block> reg) {
		BLOCK_CALLBACKS.forEach(func -> func.accept(reg));
	}
	
	public static void callItems(IForgeRegistry<Item> reg) {
		ITEM_CALLBACKS.forEach(func -> func.accept(reg));
	}
	
	public static void callModels() {
		MODEL_CALLBACKS.forEach(Runnable::run);
	}
	
	public static void callLexicon() {
		LEXICON_CALLBACKS.forEach(Runnable::run);
	}
}
