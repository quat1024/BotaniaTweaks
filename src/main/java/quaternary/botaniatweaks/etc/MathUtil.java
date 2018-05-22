package quaternary.botaniatweaks.etc;

import net.minecraft.util.math.MathHelper;

public class MathUtil {
	//The most useful function ever.
	//From https://stackoverflow.com/questions/3451553/value-remapping
	public static double rangeRemap(double value, double low1, double high1, double low2, double high2) {
		return low2 + (value - low1) * (high2 - low2) / (high1 - low1);
	}
	
	public static int lerpColor(int color1, int color2, float progress) {
		int r1 = (color1 & 0xFF0000) >> 16;
		int g1 = (color1 & 0x00FF00) >> 8;
		int b1 = (color1 & 0x0000FF);
		
		int r2 = (color2 & 0xFF0000) >> 16;
		int g2 = (color2 & 0x00FF00) >> 8;
		int b2 = (color2 & 0x0000FF);
		
		//yea it's just an rgb lerp, sue me.
		int lerpR = (int) MathHelper.clampedLerp(r1, r2, progress);
		int lerpG = (int) MathHelper.clampedLerp(g1, g2, progress);
		int lerpB = (int) MathHelper.clampedLerp(b1, b2, progress);
		
		return (lerpR << 16) | (lerpG << 8) | lerpB;
	}
}
