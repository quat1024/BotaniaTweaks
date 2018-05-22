package quaternary.botaniatweaks.etc;

public class MathUtil {
	//The most useful function ever.
	//From https://stackoverflow.com/questions/3451553/value-remapping
	public static double rangeRemap(double value, double low1, double high1, double low2, double high2) {
		return low2 + (value - low1) * (high2 - low2) / (high1 - low1);
	}
}
