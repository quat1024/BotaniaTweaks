package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.Collection;

public class JeiPluginTweak extends Tweak {
	@Override
	protected Collection<String> computeAffectedClasses() {
		return ImmutableList.of("vazkii.botania.client.integration.jei.JEIBotaniaPlugin");
	}
	
	@Override
	String getLogMessage(String transformedName) {
		return "Patching Botania's corporea request keybind...";
	}
	
	@Override
	void doPatch(String transformedName, ClassNode node) {
		//Taking a simple approach, just hooking after Botania writes to the field and overwriting it
		//Basically this is janky @Inject(at = @At("RETURN"))
		for(MethodNode method : node.methods) {
			if(method.name.equals("onRuntimeAvailable")) {
				//Iterate backwards to find the RETURN
				AbstractInsnNode ret = method.instructions.getLast();
				while(ret.getOpcode() != RETURN) ret = ret.getPrevious();
				
				InsnList patch = new InsnList();
				patch.add(new VarInsnNode(ALOAD, 1)); //IJeiRuntime
				patch.add(new MethodInsnNode(
					INVOKESTATIC,
					getHooksClass("Jei"),
					"patchCorporeaKeybind",
					"(Lmezz/jei/api/IJeiRuntime;)V",
					false
				));
				
				method.instructions.insertBefore(ret, patch);
				return;
			}
		}
	}
}
