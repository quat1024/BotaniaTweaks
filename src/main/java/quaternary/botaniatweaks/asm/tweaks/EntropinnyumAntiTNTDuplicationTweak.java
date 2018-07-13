package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.Collection;
import java.util.ListIterator;

public class EntropinnyumAntiTNTDuplicationTweak extends Tweak {
	@Override
	protected Collection<String> computeAffectedClasses() {
		return ImmutableList.of("vazkii.botania.common.block.subtile.generating.SubTileEntropinnyum");
	}
	
	@Override
	String getLogMessage(String transformedName) {
		return "Patching the entropinnyum's TNT preferences...";
	}
	
	@Override
	void doPatch(String transformedName, ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("onUpdate")) {
				InsnList ins = method.instructions;
				
				ListIterator<AbstractInsnNode> inserator = ins.iterator();
				while(inserator.hasNext()) {
					AbstractInsnNode in = inserator.next();
					
					//The first invokeinterface in this method is to create an iterator for the list of EntityTNTPrimed
					//Let's borrow that list and filter it first
					if(in.getOpcode() == INVOKEINTERFACE) {
						MethodInsnNode hook = new MethodInsnNode(INVOKESTATIC, getHooksClass(), "processTNTList", "(Ljava/util/List;)Ljava/util/List;", false);
						ins.insertBefore(in, hook);
						return; //Ok byeeeeee
					}
				}
			}
		}
	}
}
