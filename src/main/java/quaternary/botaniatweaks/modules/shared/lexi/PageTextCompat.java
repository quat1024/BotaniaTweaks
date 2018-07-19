package quaternary.botaniatweaks.modules.shared.lexi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.*;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.common.lexicon.page.PageText;

public class PageTextCompat extends PageText {
	String mod;
	
	public PageTextCompat(String unlocalizedName, String mod) {
		super(unlocalizedName);
		this.mod = mod;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		super.renderScreen(gui, mx, my);
		
		//Ok so basically I want to render a "ribbon" on the GUI like the CompatLexiconEntry
		//but just over an individual page
		GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
		if(currentScreen instanceof GuiLexicon) {
			GuiLexicon lexiScreen = (GuiLexicon) currentScreen;
			
			try {
				//Find some GuiLexicon variables useful for positioning the ribbon correctly
				int left = ReflectionHelper.getPrivateValue(GuiLexicon.class, lexiScreen, "left");
				int top = ReflectionHelper.getPrivateValue(GuiLexicon.class, lexiScreen, "top");
				int titleHeight = (int) ReflectionHelper.findMethod(GuiLexicon.class, "getTitleHeight", null).invoke(lexiScreen);
				
				//73 = GuiLexicon.guiWidth / 2
				//Call the same method that CompatLexiconEntry subtitles eventually show up through
				lexiScreen.drawBookmark(left + 73, top - titleHeight - 10, "[" + mod + "]", true, 191);
			} catch (Exception e) {
				;
			}
		}
	}
}
