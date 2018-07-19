package quaternary.botaniatweaks.modules.shared.lexi;

import vazkii.botania.api.lexicon.IAddonEntry;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.common.lexicon.BasicLexiconEntry;

public class DoubleCompatLexiconEntry extends BasicLexiconEntry implements IAddonEntry {
	final String mod1;
	final String mod2;
	
	public DoubleCompatLexiconEntry(String unlocalizedName, LexiconCategory category, String mod1, String mod2) {
		super(unlocalizedName, category);
		this.mod1 = mod1;
		this.mod2 = mod2;
	}
	
	public String getSubtitle() {
		return "[" + mod1 + " x " + mod2 + "]";
	}
}
