package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.Collection;
import java.util.ListIterator;

public class OrechidPriceTweak extends Tweak {
	@Override
	protected Collection<String> computeAffectedClasses() {
		return ImmutableList.of("vazkii.botania.common.block.subtile.functional.SubTileOrechid");
	}
	
	@Override
	String getLogMessage(String transformedName) {
		return "Patching the orechid's price...";
	}
	
	@Override
	void doPatch(String transformedName, ClassNode node) {
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
