public final class SpotAnim {

	public static void unpackConfig(StreamLoader streamLoader) {
		Stream stream = new Stream(streamLoader.getDataForName("spotanim.dat"));
		int length = stream.readUnsignedWord();
		if (cache == null)
			cache = new SpotAnim[length];
		for (int j = 0; j < length; j++) {
			if (cache[j] == null)
				cache[j] = new SpotAnim();
			cache[j].anInt404 = j;
			cache[j].readValues(stream);
		}

	}

	public void readValues(Stream stream) {
		do {
			int i = stream.readUnsignedByte();
			if (i == 0)
				return;
			if (i == 1)
				anInt405 = stream.readUnsignedWord();
			else if (i == 2) {
				anInt406 = stream.readUnsignedWord();
				if (Animation.anims != null)
					aAnimation_407 = Animation.anims[anInt406];
			} else if (i == 4)
				anInt410 = stream.readUnsignedWord();
			else if (i == 5)
				anInt411 = stream.readUnsignedWord();
			else if (i == 6)
				anInt412 = stream.readUnsignedWord();
			else if (i == 7)
				anInt413 = stream.readUnsignedWord();
			else if (i == 8)
				anInt414 = stream.readUnsignedWord();
			else if (i == 40) {
				int j = stream.readUnsignedByte();
				for (int k = 0; k < j; k++) {
					anIntArray408[k] = stream.readUnsignedWord();
					anIntArray409[k] = stream.readUnsignedWord();
				}
			} else
				System.out.println("Error unrecognised spotanim config code: "
						+ i);
		} while (true);
	}

	
	public void readValuesOld(Stream stream) {
		do {
			int i = stream.readUnsignedByte();
			if (i == 0)
				return;
			if (i == 1)
				anInt405 = stream.readUnsignedWord();
			else if (i == 2) {
				anInt406 = stream.readUnsignedWord();
				if (Animation.anims != null)
					aAnimation_407 = Animation.anims[anInt406];
			} else if (i == 4)
				anInt410 = stream.readUnsignedWord();
			else if (i == 5)
				anInt411 = stream.readUnsignedWord();
			else if (i == 6)
				anInt412 = stream.readUnsignedWord();
			else if (i == 7)
				anInt413 = stream.readUnsignedByte();
			else if (i == 8)
				anInt414 = stream.readUnsignedByte();
			else if (i == 40) {
				int j = stream.readUnsignedByte();// OSRS data dumped and packed by Valentino > All rights goes to valentino!
				for (int k = 0; k < j; k++) {
					anIntArray408[k] = stream.readUnsignedWord();
					anIntArray409[k] = stream.readUnsignedWord();
				}
			} else
				System.out.println("Error unrecognised spotanim config code: "
						+ i);
		} while (true);
	}

	public Model getModel() {
		Model model = (Model) aMRUNodes_415.insertFromCache(anInt404);
		if (model != null)
			return model;
		model = Model.method462(anInt405);
		if (model == null)
			return null;
		for (int i = 0; i < 6; i++)
			if (anIntArray408[0] != 0)
				model.method476(anIntArray408[i], anIntArray409[i]);

		aMRUNodes_415.removeFromCache(model, anInt404);
		return model;
	}

	private SpotAnim() {
		anInt406 = -1;
		anIntArray408 = new int[6];
		anIntArray409 = new int[6];
		anInt410 = 128;
		anInt411 = 128;
	}

	public static SpotAnim cache[];
	private int anInt404;
	private int anInt405;
	private int anInt406;
	public Animation aAnimation_407;
	private int[] anIntArray408;
	private int[] anIntArray409;
	public int anInt410;
	public int anInt411;
	public int anInt412;
	public int anInt413;
	public int anInt414;
	public static MRUNodes aMRUNodes_415 = new MRUNodes(30);

}
