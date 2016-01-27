package server.model.content.instance.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Bosses {
	
	private static Map<Integer, Boss> bosses = new HashMap<>();
	
	//public static final Zulrah ZULRAH = new Zulrah(2042);

	static {
		//bosses.put(ZULRAH.npcId, ZULRAH);
	
	}
		

	public static Boss get(int npcId) {
		if (!bosses.containsKey(npcId))
			return null;
		return bosses.get(npcId);
	}
	
	public static boolean isBoss(int npcId) {
		return Objects.nonNull(get(npcId));
	}
}
