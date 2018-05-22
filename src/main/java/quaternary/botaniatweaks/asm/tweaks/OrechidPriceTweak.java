package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.ListIterator;

public class OrechidPriceTweak extends Tweak {
	@Override
	List<String> getAffectedClassesImpl() {
		return ImmutableList.of("vazkii.botania.common.block.subtile.functional.SubTileOrechid");
	}
	
	@Override
	String getName(String transformedName) {
		return "the orechid's price";
	}
	
	@Override
	void patchImpl(String transformedName, ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("getCost") || method.name.equals("getDelay")) {
				InsnList ins = method.instructions;
				
				ListIterator<AbstractInsnNode> inserator = ins.iterator();
				while(inserator.hasNext()) {
					AbstractInsnNode in = inserator.next();
					if(in.getOpcode() == GETSTATIC) {
						FieldInsnNode staticGet = new FieldInsnNode(GETSTATIC, getHooksClass(), "orechidGog", "Z");
						inserator.remove();
						inserator.add(staticGet);
						break;
					}
				}
			}
		}
	}
}
