package server.model.npcs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import com.google.gson.Gson;

/**
 * NPCLootTable.java
 * 
 * Manages individual NPC loot tables, and the 'probabilities' of a specific
 * item dropping. This was developed and based on RDS logic (to an extent).
 * 
 * @author Administrator
 *
 */
public class NPCLootTable {

	/**
	 * An integer value that represents the amount of rolls to take place for
	 * this <code>NPCLootTable</code>'s {@link #dynamicDrops}. Generally
	 * speaking, this is to allow the possibility for multiple {@link NPCLoot}
	 * {@link Item}'s to drop.
	 */
	private final int rolls;

	/**
	 * An array that contains all the ID's of an {@link NPC} this
	 * <code>NPCLootTable</code> is valid for.
	 */
	private final int[] npcIdentifiers;

	/**
	 * An {@link ArrayList} containing <code>NPCLoot</code> instances that
	 * should always 'drop'.
	 */
	private final ArrayList<NPCLoot> staticDrops = new ArrayList<>();

	/**
	 * An {@link ArrayList} containing <code>NPCLoot</code> that drops on a 'hit
	 * on chance' basis.
	 */
	private final ArrayList<NPCLoot> dynamicDrops = new ArrayList<>();

	/**
	 * The range, for probability, when rolling for an {@link NPCLoot}.
	 */
	public static final int ROLL_MULTIPLIER = 100;

	/**
	 * A {@link HashMap} that represents the repository of
	 * <code>NPCLootTable</code>'s (Value) assigned to an {@link NPC} identifier
	 * (name) (Key).
	 */
	public static final HashMap<Integer, NPCLootTable> DROP_TABLE_REPO = new HashMap<>();

	/**
	 * The constructor of <code>NPCLootTabl</code> for creating new instances.
	 * 
	 * @param rolls
	 *            The amount of 'rolls' allowed to take place for multiple
	 *            {@link NPCLoot} {@link Item}'s to drop.
	 */
	public NPCLootTable(final int rolls, final int... npcIdentifiers) {
		this.rolls = rolls;
		this.npcIdentifiers = npcIdentifiers;
	}

	/** was texting sorr xd
	 * This method handles the 'possibility' of getting loot from the
	 * {@link #dynamicDrops} ArrayList collection. Based on the number of
	 * 'rolls' able to be done for an <code>NPCLootTable</code>, each loop cycle
	 * will shuffle a copied collection of {@link #dynamicDrops}. A random
	 * number(double) is then generated between .01 & 100. The first
	 * <code>NPCLoot</code> to have a {@link NPCLoot#getHitRollCeil()} higher in
	 * value than the rolled number, will be 'hit' and added to the end
	 * result(what generated the dropped loot). Again, the point of allowing
	 * multiple rolls for {@link NPCLoot} is to allow drops to be dynamically
	 * changed on an NPC to NPC basis.
	 * 
	 * @param chanceOffset
	 *            A double value that changes the probability of an
	 *            {@link NPCLoot} being 'hit' on roll.
	 * @return An ArrayList collection containing all loot that was rolled for
	 *         and 'hit'.
	 */
	private ArrayList<NPCLoot> getRolledLoot(final double chanceOffset) {
		// A 'copy' of the dynamicDrops.
		final ArrayList<NPCLoot> temp = new ArrayList<>(dynamicDrops);
		final ArrayList<NPCLoot> rolledLoot = new ArrayList<>();
		final Random random = new Random();

		
		
		for (int i = 0; i < rolls; i++) {// so for the .125 you wanted how many kills for that? so ur 0.125 or w/e is incorrect atm and u want me to fix is what uve been trying to say? I want the correct decimal for 512 :L
			final double roll = (random.nextDouble() * ROLL_MULTIPLIER);
			// Randomizes the loot ArrayList copy.
			Collections.shuffle(temp);
			
			for (final NPCLoot loot : temp) { //help me make an equation to convert it to the RS drop rate ;p cant really 'convert' it with out knowing jagex's actual formula, u just gotta use the system give me an example of what a 1-512 drop rate would look liek lol that means 1 out of 512 kills right yea
				
				if (loot == null) {
					continue;
				}
				
				final double _chanceOffset = (loot.getHitRollCeil() * chanceOffset);// 0.125 * 1 (or 1.25 if ring of wealth ro w/e it is)
				if ((loot.getHitRollCeil() + _chanceOffset) >= roll) {// if 0.125 + (0.125 * 1) >= that random number * 100
					// so if 0.25 >= roll then it succeeds in this case yes
					rolledLoot.add(loot);
					temp.remove(loot);
					break;
				}
			}
		}
		return rolledLoot;
	}

	
	
	/**
	 * Creates a new {@link ArrayList} collection containing all
	 * <code>NPCLoot</code> that will be dropped.
	 * 
	 * @param chanceOffset
	 *            A double based modification for increasing the drop
	 *            'potential' for {@link NPCLoot}. For example, Ring of Wealth.
	 * @return {@link ArrayList} treasure.
	 */
	public ArrayList<NPCLoot> getGeneratedLoot(final int chanceOffset) {
		final ArrayList<NPCLoot> generatedLoot = new ArrayList<>(staticDrops);
		generatedLoot.addAll(getRolledLoot(chanceOffset));

		return generatedLoot;
	}

	/**
	 * Gets the <code>NPCLootTable</code> assigned to an {@link NPC} by ID.
	 * 
	 * @param npcID
	 *            The ID of the {@link NPC}.
	 * @return <code>NPCLootTable</code>.
	 */
	public static final NPCLootTable forID(final int npcID) {
		// The following code block is used as a fall back for if the npcID
		// doesn't exist as a key. Just basically returns a default
		// NPCLootTable.
		if (!DROP_TABLE_REPO.containsKey(npcID))
			return DROP_TABLE_REPO.get(1118);

		return DROP_TABLE_REPO.get(npcID);
	}

	/**
	 * This method serializes all the <code>NPCLootTable</code>'s under the
	 * directory ./data/drops/ from JSON format. The number represented as the
	 * file name, 1.json for example, is the identifier for a Man, and the data
	 * within that file will represent it's <code>NPCLootTable</code>.
	 */
	public static final void load() {
		final Gson gson = new Gson();
		final File dir = new File("./Data/json/drops/");

		for (final File file : dir.listFiles()) {
			try (final BufferedReader parse = new BufferedReader(
					new FileReader(file))) {
				System.out.println(file.getName());
				final NPCLootTable table = gson.fromJson(parse,
						NPCLootTable.class);
				
				for (final int key : table.npcIdentifiers) {
					DROP_TABLE_REPO.put(key, table);
				}
			} catch (IOException e) {
				System.out.println("ERROR IN: " + file.getName());
				e.printStackTrace();
			}
		}
	}
}
