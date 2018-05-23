package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.List;

public class EntropinnyumOutputTweak extends Tweak {
	@Override
	public List<String> getAffectedClassesImpl() {
		return ImmutableList.of("vazkii.botania.common.block.subtile.generating.SubTileEntropinnyum");
	}
	
	@Override
	public String getName(String transformedName) {
		return "the entropinnyum's mana output";
	}
	
	@Override
	public void patchImpl(String transformedName, ClassNode node) {
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
