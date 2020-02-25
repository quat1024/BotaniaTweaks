package quaternary.botaniatweaks.modules.shared.lib;

import com.google.common.collect.ForwardingMap;
import sun.reflect.Reflection;

import java.util.HashMap;
import java.util.Map;

public final class NiceTryMap extends ForwardingMap<String, Integer> {
	private final HashMap<String, Integer> backing = new HashMap<String, Integer>(){
		@Override
		public Integer put(String key, Integer value) {
			if(!Thread.currentThread().getStackTrace()[4].getClassName().equals("quaternary.botaniatweaks.modules.shared.lib.NiceTryMap")) {
				throw new IllegalStateException("nope");
			}
			
			return super.put(key, value);
		}
	};
	
	static {
		Reflection.registerFieldsToFilter(NiceTryMap.class, "backing");
	}
	
	@Override
	protected final Map<String, Integer> delegate() {
		return backing;
	}
	
	@Override
	public final Integer put(String key, Integer value) {
		if(GeneratingFlowers.flowerDataFromName(key).isPassive && (value < 1 || value > 72000)) {
			throw new IllegalArgumentException("Out of range value for passive flower");
		} else return super.put(key, value);
	}
	
	@Override
	public final void putAll(Map<? extends String, ? extends Integer> map) {
		standardPutAll(map);
	}
}
