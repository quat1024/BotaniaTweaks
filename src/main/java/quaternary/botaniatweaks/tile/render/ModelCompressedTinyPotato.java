package quaternary.botaniatweaks.tile.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import quaternary.botaniatweaks.util.MathUtil;

public class ModelCompressedTinyPotato extends ModelBase {
	final ModelRenderer potato;
	
	int compressionLevel;
	float modelScale;
	float requiredYOffset;
	
	public ModelCompressedTinyPotato(int compressionLevel) {
		this.compressionLevel = compressionLevel;
		
		this.textureWidth = 16;
		this.textureHeight = 16;
		this.potato = new ModelRenderer(this, 0, 0);
		this.potato.addBox(0, 0, 0, 4, 6, 4);
		this.potato.setRotationPoint(-2, 18, -2);
		this.potato.setTextureSize(64, 32);
		
		//I DONT KNOW WHY THIS WORKS
		modelScale = (float) MathUtil.rangeRemap(compressionLevel, 0, 8, 1/16d, 4/16d);
		requiredYOffset = (float) MathUtil.rangeRemap(compressionLevel, 0, 8, 0, -4.5);
	}
	
	public void render() {
		GlStateManager.translate(0, requiredYOffset, 0);
		potato.render(modelScale);
		GlStateManager.translate(0, -requiredYOffset, 0);
	}
}
