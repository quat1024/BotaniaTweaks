package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.Collection;

public class FlowerDurabilityTweak extends Tweak {
	@Override
	protected Collection<String> computeAffectedClasses() {
		return ImmutableList.of("vazkii.botania.common.item.block.ItemBlockSpecialFlower");
	}
	
	@Override
	String getLogMessage(String transformedName) {
		return "Adding a flower durability bar...";
	}
	
	@Override
	void doPatch(String transformedName, ClassNode node) {
		MethodNode showDurabilityBar = new MethodNode(ASM5,
			ACC_PUBLIC | ACC_SYNTHETIC,
			"showDurabilityBar",
			"(Lnet/minecraft/item/ItemStack;)Z",
			null, null
		);
		showDurabilityBar.instructions.add(new VarInsnNode(ALOAD, 1));
		showDurabilityBar.instructions.add(new MethodInsnNode(
			INVOKESTATIC,
			getHooksClass(),
			"durabilityShowDurabilityBar",
			"(Lnet/minecraft/item/ItemStack;)Z",
			false
		));
		showDurabilityBar.instructions.add(new InsnNode(IRETURN));
		node.methods.add(showDurabilityBar);
		
		MethodNode getDurabilityForDisplay = new MethodNode(ASM5,
			ACC_PUBLIC | ACC_SYNTHETIC,
			"getDurabilityForDisplay",
			"(Lnet/minecraft/item/ItemStack;)D",
			null, null
		);
		getDurabilityForDisplay.instructions.add(new VarInsnNode(ALOAD, 1));
		getDurabilityForDisplay.instructions.add(new MethodInsnNode(
			INVOKESTATIC,
			getHooksClass(),
			"durabilityGetDurabilityForDisplay",
			"(Lnet/minecraft/item/ItemStack;)D",
			false
		));
		getDurabilityForDisplay.instructions.add(new InsnNode(DRETURN));
		node.methods.add(getDurabilityForDisplay);
	}
}
