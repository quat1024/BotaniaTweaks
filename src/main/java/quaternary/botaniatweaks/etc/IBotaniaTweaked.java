package quaternary.botaniatweaks.etc;

/**
 * Marker interface for blocks or items that registry replace Botania.
 * Used in the tooltip event to draw a "Tweaked by Botania Tweaks" tooltip.
 * Also patched on to flowers & stuff with asm.
 * */
public interface IBotaniaTweaked {
	boolean isTweaked();
}
