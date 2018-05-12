package quaternary.botaniatweaks.asm;

public class BotaniaTweakerHooks {
	public static int getPassiveDecayTime() {
		return 5; //config todo
	}
	
	public static boolean shouldFlowerDecay(String name) {
		return name.equals("Endoflame"); //todo
	}
}
