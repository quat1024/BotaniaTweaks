package quaternary.botaniatweaks.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import quaternary.botaniatweaks.asm.tweaks.*;

import java.util.ArrayList;
import java.util.List;

public class BotaniaTweakerTransformer implements IClassTransformer, Opcodes {
	public static final String HOOKS = "quaternary/botaniatweaks/asm/BotaniaTweakerHooks";
	
	static List<Tweak> tweaks = new ArrayList<>();
	static List<String> allPatchedClasses = new ArrayList<>();
	
	static {
		tweaks.add(new PassiveDecayTimeTweak());
		tweaks.add(new EverythingCanDecayTweak());
		tweaks.add(new ManastormChargeOutputTweak());
		tweaks.add(new EntropinnyumAntiTNTDuplicationTweak());
		tweaks.add(new SpectrolusOutputTweak());
		tweaks.add(new EverythingApothecaryTweak());
		tweaks.add(new OrechidPriceTweak());
		tweaks.add(new AgricraftCropHornHarvestTweak());
		tweaks.add(new ManaStatisticsTweak());
		tweaks.add(new CreativeManaPoolSizeTweak());
		tweaks.add(new NBTAwareRuneAltarAndPetalApothecaryStackMatchingTweak());
		tweaks.add(new RosaArcanaOutputTweak());
		
		for(Tweak t : tweaks) {
			allPatchedClasses.addAll(t.getAffectedClasses());
		}
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		
		//simple pass/fail test
		if(!allPatchedClasses.contains(transformedName)) return basicClass;
		
		ClassReader reader = new ClassReader(basicClass);
		ClassNode node = new ClassNode();
		reader.accept(node, 0);
		
		for(Tweak t : tweaks) {
			t.patch(transformedName, node);
		}
		
		ClassWriter writer = new WorkaroundClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		node.accept(writer);
		
		return writer.toByteArray();
	}
}
