package server.tools;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class NPCDropChecker implements Runnable {
	static int kills = 0;
	static Thread thread = new Thread(new NPCDropChecker());
	static double value = 0.1;
	int timesRan = 0;
	int timesKilled;
	private static double rate;
	private ArrayList<Integer> list = new ArrayList<>();
	private static boolean row = false;
	
	public static void main(String[] args) {
		System.out.println("Starting");
		Scanner scanner = new Scanner(System.in);
		System.out.println("Drop Rate ?");
		rate = scanner.nextDouble();
		
		System.out.println("ROW ?");
		row = scanner.nextBoolean();
		if (rate != 0) {
			thread.start();
		}
	}

	@Override
	public void run() {
		while (true) {
			final Random random = new Random();
			final double roll = (random.nextDouble() * 100);
			double multiplier = 1.0;
			if (row)
				multiplier *= 1.25;
			if (rate * multiplier >= roll && timesRan <= 10000) {
				// System.out.println("Drop in " + kills + " Kills.");
				list.add(kills);
				kills = 0;
				timesRan++;
				// thread.stop();
			} else if (timesRan == 10000) {
				for (int i = 0; i < list.size(); i++) {
					timesKilled += list.get(i);
				}
				System.out.println("Average Kills: " + timesKilled
						/ list.size());
				thread.stop();
			}
			kills++;
		}
	}

}
