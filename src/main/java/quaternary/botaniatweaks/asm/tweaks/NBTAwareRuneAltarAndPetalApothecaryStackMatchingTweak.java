package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.Collection;

//I'm coming for you, InternalFrameInternalFrameTitlePaneInternalFrameTitlePaneMaximizeButtonWindowNotFocusedState!
public class NBTAwareRuneAltarAndPetalApothecaryStackMatchingTweak extends Tweak {
	@Override
	protected Collection<String> computeAffectedClasses() {
		return ImmutableList.of("vazkii.botania.api.recipe.RecipePetals");
	}
	
	@Override
	String getLogMessage(String transformedName) {
		return "Patching runic altar/petal apothecary item matching logic...";
	}
	
	@Override
	void doPatch(String transformedName, ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("simpleAreStacksEqual")) {
				InsnList ins = method.instructions;
				
				ins.clear();
				
				ins.add(new VarInsnNode(ALOAD, 1));
				ins.add(new VarInsnNode(ALOAD, 2));
				ins.add(new MethodInsnNode(INVOKESTATIC, getHooksClass(), "simpleAreStacksEqualHook", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", false));
				ins.add(new InsnNode(IRETURN));
				break;
			}
		}
	}
}
