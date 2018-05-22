package quaternary.botaniatweaks.etc;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class RainbowBarfFontRenderer extends FontRenderer {
	private static RainbowBarfFontRenderer INST;
	
	boolean first = false;
	
	public static RainbowBarfFontRenderer get() {
		INST.first = true;
		return INST;
	}
	
	static {
		Minecraft mc = Minecraft.getMinecraft();
		ResourceLocation ascii = new ResourceLocation("minecraft:textures/font/ascii.png");
		INST = new RainbowBarfFontRenderer(mc.gameSettings, ascii, mc.renderEngine, false);
		
		((IReloadableResourceManager)mc.getResourceManager()).registerReloadListener(INST);
	}
	
	private RainbowBarfFontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
		super(gameSettingsIn, location, textureManagerIn, unicode);
	}
	
	@Override
	public int drawString(String text, float x, float y, int color, boolean dropShadow) {		
		if(!first) return super.drawString(text, x, y, color, dropShadow);
		first = false;
		
		float posX = x;
		float huehuehue = (Minecraft.getSystemTime() / 700f) % 1;
		float huehuehueStep = (float) MathUtil.rangeRemap(Math.sin(Minecraft.getSystemTime() / 2000f) % 6.28318, -1, 1, 0.01, 0.15);
		
		String textRender = ChatFormatting.stripFormatting(text);
		
		for(int i=0; i < textRender.length(); i++) {			
			int c = (color & 0xFF000000) | MathHelper.hsvToRGB(huehuehue, .8f, 1);
			
			float yOffset = MathHelper.sin(i + (Minecraft.getSystemTime() / 300f));
			
			posX = super.drawString(String.valueOf(textRender.charAt(i)), posX, y + yOffset, c, true) - 1;
			
			huehuehue += huehuehueStep;
			huehuehue %= 1;
		}
		
		return (int) posX;
	}
}
