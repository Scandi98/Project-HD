package server.world;

import server.model.players.Client;

public class Home {
	public static void addHome(Client client) {
		for (int i = 0; i < 6; i++)
			client.getPA().checkObjectSpawn(2213, 2799 - i, 3154, 0, 10); // Bank
																			// Booths
		client.getPA().checkObjectSpawn(2561, 2793, 3168, 0, 10);
		client.getPA().checkObjectSpawn(409, 2796, 3169, 2, 10);
		client.getPA().checkObjectSpawn(9682, 2799, 3168, 2, 10);
		client.getPA().checkObjectSpawn(3515, 2799, 3163, 1, 10);
		client.getPA().checkObjectSpawn(375, 2791, 3166, 1, 10);
	}

	public static void removeHome(Client client) {
		client.getPA().checkObjectSpawn(-1, 2799, 3169, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2799, 3167, 1, 1);
		client.getPA().checkObjectSpawn(-1, 2797, 3157, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2796, 3157, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2794, 3157, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2794, 3156, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2795, 3156, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2796, 3156, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2797, 3156, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2798, 3156, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2799, 3154, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2798, 3154, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2797, 3154, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2796, 3154, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2795, 3154, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2794, 3154, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2795, 3160, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2795, 3159, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2794, 3160, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2794, 3161, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2797, 3161, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2798, 3159, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2798, 3160, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2799, 3157, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2799, 3160, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2799, 3161, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2799, 3164, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2799, 3165, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2799, 3166, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2796, 3166, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2795, 3166, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2794, 3166, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2793, 3166, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2791, 3167, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2791, 3166, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2793, 3164, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2794, 3164, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2795, 3164, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2796, 3164, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2793, 3165, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2795, 3165, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2791, 3164, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2791, 3162, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2791, 3161, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2791, 3160, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2791, 3157, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2797, 3169, 1, 10);
		client.getPA().checkObjectSpawn(-1, 2798, 3169, 1, 10);
	}

}
