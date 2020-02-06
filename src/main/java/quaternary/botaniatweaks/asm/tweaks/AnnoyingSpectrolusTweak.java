package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.Collection;
import java.util.Objects;

//NB: This is a *very poor* coremod implementation.
//Please don't write ASM like this, there are lots of tools available to make your code nice and pretty.
//This is better suited to Mixin anyways.
//Just... do as I say, not as I do.


public class AnnoyingSpectrolusTweak extends Tweak {
	@Override
	protected Collection<String> computeAffectedClasses() {
		return ImmutableList.of("vazkii.botania.common.block.subtile.generating.SubTileSpectrolus");
	}
	
	@Override
	String getLogMessage(String transformedName) {
		return "Patching the Spectrolus to add advanced mode...";
	}
	
	@Override
	void doPatch(String transformedName, ClassNode node) {
		
		//Add the field.
		node.fields.add(new FieldNode(
			ACC_PUBLIC | ACC_SYNTHETIC,
			"btweaks$wool_indices",
			"[I",
			null,
			null
		));
		
		//Initialize it.
		{
			InsnList insns = Objects.requireNonNull(getMethod(node, "<init>")).instructions; //default constructor
			
			AbstractInsnNode ret = insns.getLast();
			while (ret.getOpcode() != RETURN) ret = ret.getPrevious();
			
			InsnList hook = new InsnList();
			hook.add(new VarInsnNode(ALOAD, 0)); //this
			hook.add(new MethodInsnNode(
				INVOKESTATIC,
				getHooksClass(),
				"hardSpectrolusInitialize",
				"(Lvazkii/botania/common/block/subtile/generating/SubTileSpectrolus;)V",
				false
			));
			
			insns.insertBefore(ret, hook);
		}
		
		//Add getters and setters for it, and implement an interface exposing those getters and setters.
		node.interfaces.add("quaternary/botaniatweaks/asm/monkeypatch/IHardSpectrolus");
		
		{
			MethodNode getter = new MethodNode(ASM5,
				ACC_PUBLIC | ACC_SYNTHETIC,
				"btweaks$getWoolOrder",
				"()[I",
				null, null
			);
			getter.instructions.add(new VarInsnNode(ALOAD, 0)); //this
			getter.instructions.add(new FieldInsnNode(
				GETFIELD,
				"vazkii/botania/common/block/subtile/generating/SubTileSpectrolus",
				"btweaks$wool_indices",
				"[I"
			));
			getter.instructions.add(new InsnNode(ARETURN));
			
			node.methods.add(getter);
		}
		
		{
			MethodNode setter = new MethodNode(ASM5,
				ACC_PUBLIC | ACC_SYNTHETIC,
				"btweaks$setWoolOrder",
				"([I)V",
				null, null
			);
			setter.instructions.add(new VarInsnNode(ALOAD, 0)); //this
			setter.instructions.add(new VarInsnNode(ALOAD, 1)); //argument
			setter.instructions.add(new FieldInsnNode(
				PUTFIELD,
				"vazkii/botania/common/block/subtile/generating/SubTileSpectrolus",
				"btweaks$wool_indices",
				"[I"
			));
			setter.instructions.add(new InsnNode(RETURN));
			
			node.methods.add(setter);
		}
		
		//Also add another little getter and setter pair, which I need for a dropStack fix
		{
			MethodNode getter = new MethodNode(ASM5,
				ACC_PUBLIC | ACC_SYNTHETIC,
				"btweaks$getNextColor",
				"()I",
				null, null
			);
			
			getter.instructions.add(new VarInsnNode(ALOAD, 0)); //this
			getter.instructions.add(new FieldInsnNode(
				GETFIELD,
				"vazkii/botania/common/block/subtile/generating/SubTileSpectrolus",
				"nextColor",
				"I"
			));
			getter.instructions.add(new InsnNode(IRETURN));
			
			node.methods.add(getter);
		}
		
		{
			MethodNode setter = new MethodNode(ASM5,
				ACC_PUBLIC | ACC_SYNTHETIC,
				"btweaks$setNextColor",
				"(I)V",
				null, null
			);
			setter.instructions.add(new VarInsnNode(ALOAD, 0)); //this
			setter.instructions.add(new VarInsnNode(ILOAD, 1)); //argument
			setter.instructions.add(new FieldInsnNode(
				PUTFIELD,
				"vazkii/botania/common/block/subtile/generating/SubTileSpectrolus",
				"nextColor",
				"I"
			));
			setter.instructions.add(new InsnNode(RETURN));
			
			node.methods.add(setter);
		}
		
		//Save it to NBT (well, break out into helper functions to save it to NBT)
		{
			InsnList insns = Objects.requireNonNull(getMethod(node, "writeToPacketNBT")).instructions;
			
			AbstractInsnNode ret = insns.getLast();
			while (ret.getOpcode() != RETURN) ret = ret.getPrevious();
			
			InsnList hook = new InsnList();
			hook.add(new VarInsnNode(ALOAD, 0)); //this
			hook.add(new VarInsnNode(ALOAD, 1)); //NBTTagCompound
			hook.add(new MethodInsnNode(
				INVOKESTATIC,
				getHooksClass(),
				"hardSpectrolusWriteToPacketNBT",
				"(Lvazkii/botania/common/block/subtile/generating/SubTileSpectrolus;Lnet/minecraft/nbt/NBTTagCompound;)V",
				false
			));
			
			insns.insertBefore(ret, hook);
		}
		
		//and read it back.
		{
			InsnList insns = Objects.requireNonNull(getMethod(node, "readFromPacketNBT")).instructions;
			
			AbstractInsnNode ret = insns.getLast();
			while(ret.getOpcode() != RETURN) ret = ret.getPrevious();
			
			InsnList hook = new InsnList();
			hook.add(new VarInsnNode(ALOAD, 0)); //this
			hook.add(new VarInsnNode(ALOAD, 1)); //NBTTagCompound
			hook.add(new MethodInsnNode(
				INVOKESTATIC,
				getHooksClass(),
				"hardSpectrolusReadFromPacketNBT",
				"(Lvazkii/botania/common/block/subtile/generating/SubTileSpectrolus;Lnet/minecraft/nbt/NBTTagCompound;)V",
				false
			));
			
			insns.insertBefore(ret, hook);
		}
		
		//Save it to itemstack NBT as well.
		//This means implementing a new function.
		{
			MethodNode placedBy = new MethodNode(ASM5,
				ACC_PUBLIC | ACC_SYNTHETIC,
				"populateDropStackNBTs",
				"(Ljava/util/List;)V",
				null, null
			);
			
			//Call super
			placedBy.instructions.add(new VarInsnNode(ALOAD, 0));
			placedBy.instructions.add(new VarInsnNode(ALOAD, 1));
			placedBy.instructions.add(new InsnNode(DUP2));
			placedBy.instructions.add(new MethodInsnNode(
				INVOKESPECIAL,
				"vazkii/botania/api/subtile/SubTileGenerating",
				"populateDropStackNBTs",
				"(Ljava/util/List;)V",
				false
			));
			//Hook
			placedBy.instructions.add(new MethodInsnNode(
				INVOKESTATIC,
				getHooksClass(),
				"hardSpectrolusPopulateDropStackNBTs",
				"(Lvazkii/botania/common/block/subtile/generating/SubTileSpectrolus;Ljava/util/List;)V",
				false
			));
			placedBy.instructions.add(new InsnNode(RETURN));
			
			node.methods.add(placedBy);
		}
		
		//Also hook onBlockPlacedBy to initialize/scramble the array for the first time.
		{
			MethodNode placedBy = new MethodNode(ASM5,
				ACC_PUBLIC | ACC_SYNTHETIC,
				"onBlockPlacedBy",
				"(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;)V",
				null, null
			);
			placedBy.instructions.add(new VarInsnNode(ALOAD, 0)); //this
			placedBy.instructions.add(new VarInsnNode(ALOAD, 1)); //world
			placedBy.instructions.add(new VarInsnNode(ALOAD, 5)); //stack
			placedBy.instructions.add(new MethodInsnNode(
				INVOKESTATIC,
				getHooksClass(),
				"hardSpectrolusOnBlockPlacedBy",
				"(Lvazkii/botania/common/block/subtile/generating/SubTileSpectrolus;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)V",
				false
			));
			placedBy.instructions.add(new InsnNode(RETURN));
			
			node.methods.add(placedBy);
		}
		
		//Finally, make the array have an effect on the game
		//Make the flower read from the array
		{
			InsnList insns = Objects.requireNonNull(getMethod(node, "onUpdate")).instructions;
			AbstractInsnNode point = insns.getFirst();
			//The first icmpne happens to be the nextColor lookup
			//bad and brittle? Yes!
			while(point.getOpcode() != IF_ICMPNE) point = point.getNext();
			
			InsnList hook = new InsnList();
			//get the array
			hook.add(new VarInsnNode(ALOAD, 0));
			hook.add(new FieldInsnNode(
				GETFIELD,
				"vazkii/botania/common/block/subtile/generating/SubTileSpectrolus",
				"btweaks$wool_indices",
				"[I"
			));
			//index it
			hook.add(new InsnNode(SWAP)); //(it's a bit easier to hook this location)
			hook.add(new InsnNode(IALOAD));
			
			insns.insertBefore(point, hook);
		}
		
		//Make the tooltip read from the array
		{
			MethodNode renderHud = getMethod(node, "renderHUD");
			if(renderHud != null) { //might be stripped from the server
				AbstractInsnNode point = renderHud.instructions.getFirst();
				//The nextColor get is the first instance of getField
				//Yes, I know, it's brittle...
				while(point.getOpcode() != GETFIELD) point = point.getNext();
				
				InsnList hook = new InsnList();
				//get the array
				hook.add(new VarInsnNode(ALOAD, 0));
				hook.add(new FieldInsnNode(
					GETFIELD,
					"vazkii/botania/common/block/subtile/generating/SubTileSpectrolus",
					"btweaks$wool_indices",
					"[I"
				));
				//index it
				hook.add(new InsnNode(SWAP));
				hook.add(new InsnNode(IALOAD));
				
				renderHud.instructions.insert(point, hook);
			}
		}
	}
	
	private static MethodNode getMethod(ClassNode node, String name) {
		for(MethodNode method : node.methods) {
			if(method.name.equals(name)) return method;
		}
		return null;
	}
}
