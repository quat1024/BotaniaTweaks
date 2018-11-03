package quaternary.botaniatweaks.modules.botania;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.modules.IModule;
import quaternary.botaniatweaks.modules.botania.advancement.ManagenStatisticsAdvancementHandler;
import quaternary.botaniatweaks.modules.botania.block.*;
import quaternary.botaniatweaks.modules.botania.command.CommandDebugManaGenerationStats;
import quaternary.botaniatweaks.modules.botania.command.CommandResetManaGenerationStats;
import quaternary.botaniatweaks.modules.botania.config.BotaniaConfig;
import quaternary.botaniatweaks.modules.botania.handler.*;
import quaternary.botaniatweaks.modules.botania.item.*;
import quaternary.botaniatweaks.modules.botania.lexi.BotaniaLexiconHandler;
import quaternary.botaniatweaks.modules.botania.misc.BehaviorEnderAirDispenser;
import quaternary.botaniatweaks.modules.botania.net.PacketAdvancedCrateFX;
import quaternary.botaniatweaks.modules.botania.net.PacketCustomTerraPlate;
import quaternary.botaniatweaks.modules.botania.recipe.AgglomerationRecipes;
import quaternary.botaniatweaks.modules.botania.tile.*;
import quaternary.botaniatweaks.modules.botania.tile.render.RenderTileCompressedTinyPotato;
import quaternary.botaniatweaks.modules.shared.helper.*;
import quaternary.botaniatweaks.modules.shared.net.BotaniaTweaksPacketHandler;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.common.block.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class ModuleBotania implements IModule {
	@Override
	public void preinit() {
		//Warn if the Botania version is wrong because btweaks is so fkin fragile lmao
		ModContainer botania = MiscHelpers.getBotaniaModContainer();
		
		try {
			String minorVersionString = botania.getDisplayVersion().split("-")[1];
			double versionNumber = Double.parseDouble(minorVersionString);
			int flooredVersion = MathHelper.floor(versionNumber);
			
			if(flooredVersion != BotaniaTweaks.TESTED_BOTANIA_VERSION) {
				BotaniaTweaks.LOG.warn("********************************");
				BotaniaTweaks.LOG.warn("Detected a Botania version mismatch!");
				BotaniaTweaks.LOG.warn("Expected version {}, found version {}.", BotaniaTweaks.TESTED_BOTANIA_VERSION, flooredVersion);
				BotaniaTweaks.LOG.warn("This may cause issues and crashes! Please report any");
				BotaniaTweaks.LOG.warn("errors and crashes to Botania Tweaks first. Thanks!");
				BotaniaTweaks.LOG.warn("********************************");
			}
			
		} catch(Exception asdf) {
			BotaniaTweaks.LOG.warn("********************************");
			BotaniaTweaks.LOG.warn("Unable to detect or parse Botania's version!!!");
			BotaniaTweaks.LOG.warn("This is BAD!!! Serious incompatibilities and crashes may happen!!!");
			BotaniaTweaks.LOG.warn("********************************");
		}
		
		BotaniaTweaks.PROXY.registerSidedEventClasses(() -> CommonEvents.class, () -> ClientEvents.class);
		
		MinecraftForge.EVENT_BUS.register(AutoCorporeaSparkPlaceBlockHandler.class);
		MinecraftForge.EVENT_BUS.register(SheepEatAltGrassJoinWorldHandler.class);
		MinecraftForge.EVENT_BUS.register(TNTDuplicatorDetectionWorldTickHander.class);
		MinecraftForge.EVENT_BUS.register(ManagenStatisticsAdvancementWorldTickHandler.class);
		MinecraftForge.EVENT_BUS.register(PotatoRightClickHandler.class);
		MinecraftForge.EVENT_BUS.register(NonGOGWaterBowlHandler.class);
		BotaniaTweaks.PROXY.registerSidedEventClasses(null, () -> TooltipHandler.class);
		
		BotaniaRegistryReplacements.registerOverrides();
	}
	
	@Override
	public void init() {
		ManagenStatisticsAdvancementHandler.init();
		
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.GLASS_BOTTLE, new BehaviorEnderAirDispenser(BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(Items.GLASS_BOTTLE)));
		
		BotaniaTweaksPacketHandler.registerMessage(PacketCustomTerraPlate.Response.class, PacketCustomTerraPlate.class, Side.CLIENT);
		BotaniaTweaksPacketHandler.registerMessage(PacketAdvancedCrateFX.Response.class, PacketAdvancedCrateFX.class, Side.CLIENT);
	}
	
	@Override
	public void postinit() {
		BotaniaLexiconHandler.registerLexicon();
		
		//Also a good time to apply this little tweak, I guess TODO find a better home for this blcok of code
		if(BotaniaConfig.CHEAP_FLINT_TO_POWDER) {
			for(RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes) {
				Object inO = recipe.getInput();
				if(!(inO instanceof ItemStack)) continue;
				ItemStack in = (ItemStack) inO;
				ItemStack out = recipe.getOutput();
				IBlockState cata = recipe.getCatalyst();
				
				if(cata == RecipeManaInfusion.alchemyState && in.getItem() == Items.FLINT && out.getItem() == Items.GUNPOWDER && recipe.getManaToConsume() == 4000) {
					try {
						EnumHelper.setFailsafeFieldValue(ReflectionHelper.findField(RecipeManaInfusion.class, "mana"), recipe, 200);
					} catch (Exception eeee) {
						throw new RuntimeException("Problem applying cheapFlintToPowder tweak: ", eeee);
					}
					
					break;
				}
			}
		}
	}
	
	@Override
	public void readConfig(Configuration config) {
		BotaniaConfig.readConfig(config);
	}
	
	@Override
	public void serverStarting(FMLServerStartingEvent e) {
		e.registerServerCommand(new CommandDebugManaGenerationStats());
		e.registerServerCommand(new CommandResetManaGenerationStats());
	}
	
	private static List<BlockCompressedTinyPotato> taters = new ArrayList<>(8);
	private static BlockPottedTinyPotato pottedTater;
	
	public static class CommonEvents {
		@SubscribeEvent
		public static void blocks(RegistryEvent.Register<Block> e) {
			IForgeRegistry<Block> reg = e.getRegistry();
			
			for(int compressionLevel = 1; compressionLevel <= 8; compressionLevel++) {
				String regName = "compressed_tiny_potato_" + compressionLevel;
				BlockCompressedTinyPotato tater = RegHelpers.createBlock(new BlockCompressedTinyPotato(compressionLevel), regName);
				taters.add(tater);
				reg.register(tater);
			}
			
			pottedTater = RegHelpers.createBlock(new BlockPottedTinyPotato(), "potted_tiny_potato");
			reg.register(pottedTater);
			
			GameRegistry.registerTileEntity(TileNerfedManaFluxfield.class, BotaniaTweaks.MODID + ":tweaked_fluxfield");
			GameRegistry.registerTileEntity(TileCustomAgglomerationPlate.class, BotaniaTweaks.MODID + ":custom_agglomeration_plate");
			GameRegistry.registerTileEntity(TileCompressedTinyPotato.class, BotaniaTweaks.MODID + ":compressed_tiny_potato");
			GameRegistry.registerTileEntity(TileCustomCraftyCrate.class, BotaniaTweaks.MODID + ":custom_crafty_crate");
		}
		
		@SubscribeEvent
		public static void items(RegistryEvent.Register<Item> e) {
			IForgeRegistry<Item> reg = e.getRegistry();
			
			for(BlockCompressedTinyPotato tater : taters) {
				ItemBlock ib = tater.compressionLevel == 8 ? new ItemBlockRainbowBarf(tater) : new ItemBlock(tater);
				reg.register(RegHelpers.createItemBlock(ib));
			}
			
			reg.register(RegHelpers.createItemBlock(new ItemBlock(pottedTater)));
		}
		
		@SubscribeEvent
		public static void recipes(RegistryEvent.Register<IRecipe> e) {
			//As long as this runs before RegistryEvent.Register with LOWEST priority,
			//things in here will be available to Crafttweaker.
			AgglomerationRecipes.onRecipeEvent();
		}
		
		/// RIP SPORKS 2018-2018 "nobody fucking misses these"
		@SubscribeEvent
		public static void missingItemMappings(RegistryEvent.MissingMappings<Item> e) {
			for(RegistryEvent.MissingMappings.Mapping<Item> missing : e.getMappings()) {
				if(missing.key.getPath().endsWith("spork")) missing.ignore();
			}
		}
		
		@SubscribeEvent
		public static void missingRecipeMappings(RegistryEvent.MissingMappings<IRecipe> e) {
			for(RegistryEvent.MissingMappings.Mapping<IRecipe> missing : e.getMappings()) {
				if(missing.key.getPath().endsWith("spork")) missing.ignore();
			}
		}
	}
	
	public static class ClientEvents {
		@SubscribeEvent
		public static void models(ModelRegistryEvent e) {
			//This whole method sucks dick. TODO make it not suck dick.
			
			for(BlockCompressedTinyPotato tater : taters) {
				ClientHelpers.setModel(tater.getRegistryName().getPath());
			}
			
			ClientHelpers.setModel("potted_tiny_potato");
			
			ClientRegistry.bindTileEntitySpecialRenderer(TileCompressedTinyPotato.class, new RenderTileCompressedTinyPotato());
		}
	}
}
