package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.Collection;

public class AaaaaaaaaaaaTweak extends Tweak {
	private static final String ITEMSTACK = "net.minecraft.item.ItemStack";
	private static final String RESOURCELOCATION = "net.minecraft.util.ResourceLocation";
	
	@Override
	protected Collection<String> computeAffectedClasses() {
		return ImmutableList.of(ITEMSTACK, RESOURCELOCATION);
	}
	
	@Override
	String getLogMessage(String transformedName) {
		return "Imagine having mappings errors in 2020 lmao";
	}
	
	@Override
	void doPatch(String transformedName, ClassNode node) {
		switch(transformedName) {
			case ITEMSTACK:
				MethodNode unlocalizedName = new MethodNode(ASM5, "getUnlocalizedName", "()Ljava/lang/String;", null, null);
				unlocalizedName.access = ACC_PUBLIC | ACC_SYNTHETIC;
				unlocalizedName.instructions.add(new VarInsnNode(ALOAD, 0));
				unlocalizedName.instructions.add(new MethodInsnNode(INVOKEVIRTUAL, ITEMSTACK.replace('.', '/'), "getTranslationKey", "()Ljava/lang/String;", false));
				unlocalizedName.instructions.add(new InsnNode(ARETURN));
				node.methods.add(unlocalizedName);
				break;
			case RESOURCELOCATION:
				MethodNode resourceDomain = new MethodNode(ASM5, "getResourceDomain", "()Ljava/lang/String;", null, null);
				resourceDomain.access = ACC_PUBLIC | ACC_SYNTHETIC;
				resourceDomain.instructions.add(new VarInsnNode(ALOAD, 0));
				resourceDomain.instructions.add(new MethodInsnNode(INVOKEVIRTUAL, RESOURCELOCATION.replace('.', '/'), "getNamespace", "()Ljava/lang/String;", false));
				resourceDomain.instructions.add(new InsnNode(ARETURN));
				node.methods.add(resourceDomain);
				break;
		}
	}
}
