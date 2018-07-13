package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.Collection;

public class EntropinnyumOutputTweak extends Tweak {
	@Override
	public Collection<String> computeAffectedClasses() {
		return ImmutableList.of("vazkii.botania.common.block.subtile.generating.SubTileEntropinnyum");
	}
	
	@Override
	public String getLogMessage(String transformedName) {
		return "Patching the entropinnyum's mana output...";
	}
	
	@Override
	public void doPatch(String transformedName, ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("getMaxMana")) {
				InsnList ins = method.instructions;
				
				ins.clear();
				
				ins.add(new MethodInsnNode(INVOKESTATIC, getHooksClass(), "getEntropinnyumMaxMana", "()I", false));
				ins.add(new InsnNode(IRETURN));
				break;
			}
		}
	}
}
