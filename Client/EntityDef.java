import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public final class EntityDef {

	public static EntityDef forID(int i) {
		for (int j = 0; j < 20; j++)
			if (cache[j].interfaceType == (long) i)
				return cache[j];

		anInt56 = (anInt56 + 1) % 20;
		EntityDef entityDef = cache[anInt56] = new EntityDef();
		stream.currentOffset = streamIndices[i];
		entityDef.interfaceType = i;
		entityDef.readValues(stream);
		if (i == 2207) {
			System.out.println("Walk: " + entityDef.walkAnim + ", Stand: " + entityDef.standAnim);
		}
		switch(i) {
		/*case 1751://PURPLE PORTAL.
			entityDef.anIntArray94 = new int[2];
			entityDef.anIntArray94[0] = 14533;
			entityDef.anIntArray94[1] = 14526;
			//entityDef.anIntArray94[2] = 14522;
			entityDef.name = "Portal";
			entityDef.anIntArray70 = new int[] { -11149, -11033, -10792, -10317, -10333 };
			entityDef.anIntArray76 = new int[] { -22293, -22184, -22320,  -22330, -22343 };
			entityDef.actions = new String[5];
			entityDef.actions[1] = "Attack";
			entityDef.standAnim = 3933;
			entityDef.walkAnim = 3933;
			entityDef.combatLevel = 0;
			entityDef.anInt86 = 128;
			entityDef.anInt91 = 128;
			break;
		case 1747://PINK PORTAL
			entityDef.anIntArray94 = new int[3];
			entityDef.anIntArray94[0] = 14533;
			entityDef.anIntArray94[1] = 14527;
			entityDef.anIntArray94[2] = 14528;
			entityDef.name = "Portal";
			entityDef.anIntArray70 = new int[] { -11149, -11033, -10792, -10317, -10333 };
			entityDef.anIntArray76 = new int[] { 10355, 10471, 10941,  11200, 11787 };
			entityDef.actions = new String[5];
			entityDef.actions[1] = "Attack";
			entityDef.standAnim = 3933;
			entityDef.walkAnim = 3933;
			entityDef.combatLevel = 0;
			entityDef.anInt86 = 128;
			entityDef.anInt91 = 128;
			break;
		case 1749://YELLOW PORTAL
			entityDef.anIntArray94 = new int[2];
			entityDef.anIntArray94[0] = 14533;
			entityDef.anIntArray94[1] = 14522;
			entityDef.name = "Portal";
			entityDef.anIntArray70 = new int[] { -11149, -11033, -10792, -10317, -10333 };
			entityDef.anIntArray76 = new int[] { 10355, 10471, 10941,  11200, 11787 };
			entityDef.actions = new String[5];
			entityDef.actions[1] = "Attack";
			entityDef.standAnim = 3933;
			entityDef.walkAnim = 3933;
			entityDef.combatLevel = 0;
			entityDef.anInt86 = 128;
			entityDef.anInt91 = 128;
			break;
		case 1750://RED PORTAL?
			entityDef.anIntArray94 = new int[4];
			entityDef.anIntArray94[0] = 14533;
			entityDef.anIntArray94[1] = 14523;
			entityDef.anIntArray94[2] = 14524;
			entityDef.anIntArray94[3] = 14525;
			entityDef.name = "Portal";
			entityDef.anIntArray70 = new int[] { -11149, -11033, -10792, -10317, -10333 };
			entityDef.anIntArray76 = new int[] { -2125, -2249, -2373,  -2490, -2490 };
			entityDef.actions = new String[5];
			entityDef.actions[1] = "Attack";
			entityDef.standAnim = 3933;
			entityDef.walkAnim = 3933;
			entityDef.combatLevel = 0;
			entityDef.anInt86 = 128;
			entityDef.anInt91 = 128;
			break;*/
		case 3162:
			//System.out.println("86: " + entityDef.anInt86 + ", 91: "+ entityDef.anInt91);
			entityDef.anInt86 = 119;
			entityDef.anInt91 = 119;
			break;
		/* Zulrah */
		case 2042:
			entityDef.description = "The green hooded serpent of the poison waste.".getBytes();
			entityDef.anInt86 = 119;
			entityDef.anInt91 = 119;
			break;
		case 2043:
			entityDef.description = "The crimson hooded serpent of the poison waste.".getBytes();
			entityDef.anInt86 = 119;
			entityDef.anInt91 = 119;
			break;
		case 2044:
			entityDef.description = "The turqoise hooded serpent of the poison waste.".getBytes();
			entityDef.anInt86 = 119;
			entityDef.anInt91 = 119;
			break;
			
			/* Corporeal Beast */
		case 319:
			entityDef.description = "A vision of supernatural horror.".getBytes();
			break;
		}
		/*	switch (i) {
		case 1575:
			entityDef.combatLevel = 214;
			break;
		case 3151:
			entityDef.anIntArray94 = new int[1];
			entityDef.anIntArray94[0] = 65310;
			entityDef.name = "Vet'ion";
			entityDef.actions = new String[] { null, "Attack", null, null, null };
			EntityDef vet = forID(90);
			entityDef.standAnim = vet.standAnim;
			entityDef.walkAnim = vet.walkAnim;
			entityDef.combatLevel = 454;
			entityDef.anInt86 = 180;
			entityDef.anInt91 = 180;
			break;
		case 2640:
			entityDef.name = "Smoke devil";
			entityDef.combatLevel = 160;
			entityDef.anIntArray94[0] = 65309;
			EntityDef smoke = forID(1624);
			entityDef.standAnim = smoke.standAnim;
			entityDef.walkAnim = smoke.standAnim;
			entityDef.actions = smoke.actions;
			entityDef.anInt86 = smoke.anInt86;
			entityDef.anInt91 = smoke.anInt91;
			break;
		case 3153:
			entityDef.name = "Thermonuclear smoke devil";
			entityDef.combatLevel = 301;
			entityDef.anIntArray94[0] = 65309;
			smoke = forID(1624);
			entityDef.standAnim = smoke.standAnim;
			entityDef.walkAnim = smoke.standAnim;
			entityDef.actions = smoke.actions;
			entityDef.anInt86 = 225;
			entityDef.anInt91 = 225;
			break;
		case 3150:
			entityDef.name = "Emblem Trader";
			entityDef.anIntArray94 = new int[8];
			entityDef.anIntArray94[0] = 65301;
			entityDef.anIntArray94[1] = 65302;
			entityDef.anIntArray94[2] = 65303;
			entityDef.anIntArray94[3] = 65304;
			entityDef.anIntArray94[4] = 65305;
			entityDef.anIntArray94[5] = 65306;
			entityDef.anIntArray94[6] = 65307;
			entityDef.anIntArray94[7] = 65308;
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[1] = null;
			entityDef.actions[2] = "Trade";
			entityDef.actions[3] = "Hide-streaks";
			entityDef.actions[4] = "Skull";
			entityDef.standAnim = 813;
			entityDef.walkAnim = 1205;
			entityDef.anIntArray76 = new int[6];
			entityDef.anIntArray76[0] = 10776;
			entityDef.anIntArray76[1] = 10892;
			entityDef.anIntArray76[2] = 10776;
			entityDef.anIntArray76[3] = 10892;
			entityDef.anIntArray76[4] = 10892;
			entityDef.anIntArray76[5] = 10892;
			entityDef.anIntArray70 = new int[7];
			entityDef.anIntArray70[0] = 94;
			entityDef.anIntArray70[1] = 43449;
			entityDef.anIntArray70[2] = 43439;
			entityDef.anIntArray70[3] = 43228;
			entityDef.anIntArray70[4] = 43228;
			entityDef.anIntArray70[5] = 43115;
			entityDef.anIntArray70[6] = 43340;
			break;
		case 4056:
			entityDef.name = "Zulrah";
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			entityDef.anIntArray94 = new int[2];
			entityDef.anIntArray94[0] = 14407;
			entityDef.anIntArray94[1] = 14407;
			entityDef.standAnim = 5070;
			entityDef.walkAnim = 5070;
			entityDef.combatLevel = 115;
			entityDef.anInt86 = 100;
			entityDef.anInt91 = 100;
			break;
		case 3847:
			entityDef.name = "Kraken";
			entityDef.combatLevel = 291;
			entityDef.anIntArray94[0] = 28231;
			EntityDef kraken = forID(3847);
			entityDef.standAnim = kraken.standAnim;
			entityDef.walkAnim = kraken.walkAnim;
			entityDef.actions = kraken.actions;
			entityDef.anInt86 = 140;
			entityDef.anInt91 = 120;
			break;
		case 494:
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[2] = "Bank";
			entityDef.actions[3] = "Auction-House";
			break;

		case 599:
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[2] = "Change-appearance";
			break;

		case 437:
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[2] = "Trade";
			break;

		case 5023:
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[2] = "Trade";
			entityDef.actions[3] = "Check-points";
			break;

		case 26:
			entityDef.standAnim = 808;
			break;

		case 2028:
			entityDef.standAnim = 2074;
			break;

		case 1186:
			entityDef.name = "Oberon";
			entityDef.description = "He will devour your tears".getBytes();
			break;

		case 555:
		case 1511:
		case 461:
		case 576:
		case 549:
		case 2620:
		case 2622:
		case 2623:
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Trade";
			break;

		case 569:
		case 570:
		case 571:
		case 572:
		case 573:
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			break;

		case 1599:
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[2] = "Trade";
			entityDef.actions[3] = "Rewards";
			break;
		case 134:
			entityDef.anIntArray94 = new int[2];
			entityDef.anIntArray94[0] = 28294;
			entityDef.anIntArray94[1] = 28295;
			entityDef.name = "Venenatis";
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			entityDef.anInt86 = 40;
			entityDef.anInt91 = 40;
			EntityDef ven = forID(60);
			entityDef.standAnim = ven.standAnim;
			entityDef.walkAnim = ven.walkAnim;
			entityDef.combatLevel = 464;
			entityDef.aByte68 = 1;
			break;
		case 4173:
			entityDef.anIntArray94 = new int[2];
			entityDef.anIntArray94[0] = 28294;
			entityDef.anIntArray94[1] = 28295;
			entityDef.name = "Venenatis";
			entityDef.actions = new String[] { null, "Attack", null, null, null };
			entityDef.anInt86 = 200;
			entityDef.anInt91 = 200;
			ven = forID(60);
			entityDef.standAnim = ven.standAnim;
			entityDef.walkAnim = ven.walkAnim;
			entityDef.combatLevel = 464;
			break;
		case 3943:
			entityDef.anInt86 = 70 + 50;
			entityDef.anInt91 = 60 + 50;
			break;
		case 105:
			entityDef.name = "Callisto";
			entityDef.combatLevel = 470;
			entityDef.anIntArray94[0] = 28298;
			EntityDef callisto = forID(105);
			entityDef.standAnim = callisto.standAnim;
			entityDef.walkAnim = callisto.walkAnim;
			entityDef.actions = callisto.actions;
			entityDef.anInt86 = 70 + 50;
			entityDef.anInt91 = 60 + 50;
			break;
		case 608:
			entityDef.name = "Sir Amik Varze";
			entityDef.actions[2] = "Exchange Scrolls";
			break;
		case 4000:
			entityDef.name = "King Black Dragon";
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			entityDef.anIntArray94 = new int[4];
			entityDef.anIntArray94[0] = 17414;
			entityDef.anIntArray94[1] = 17415;
			entityDef.anIntArray94[2] = 17429;
			entityDef.anIntArray94[3] = 17422;
			entityDef.combatLevel = 276;
			entityDef.standAnim = 90;
			entityDef.walkAnim = 4635;
			entityDef.anInt86 = 50;
			entityDef.anInt91 = 50;
			break;

		case 4001:
			entityDef.name = "General Graardor";
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			entityDef.anIntArray94 = new int[2];
			entityDef.anIntArray94[0] = 27785;
			entityDef.anIntArray94[1] = 27789;
			entityDef.combatLevel = 624;
			entityDef.standAnim = 7059;
			entityDef.walkAnim = 7058;
			entityDef.anInt86 = 40;
			entityDef.anInt91 = 40;
			break;

		case 4003:
			entityDef.name = "Chaos Elemental";
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			entityDef.anIntArray94 = new int[1];
			entityDef.anIntArray94[0] = 11216;
			entityDef.combatLevel = 305;
			entityDef.standAnim = 3144;
			entityDef.walkAnim = 3145;
			entityDef.anInt86 = 40;
			entityDef.anInt91 = 40;
			break;

		case 4005:
			entityDef.name = "Kree Arra";
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			entityDef.anIntArray94 = new int[2];
			entityDef.anIntArray94[0] = 28003;
			entityDef.anIntArray94[1] = 28004;
			entityDef.combatLevel = 580;
			entityDef.standAnim = 6972;
			entityDef.walkAnim = 6973;
			entityDef.anInt86 = 40;
			entityDef.anInt91 = 40;
			break;

		case 4006:
			entityDef.name = "K'ril Tsutsaroth";
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			entityDef.anIntArray94 = new int[5];
			entityDef.anIntArray94[0] = 27768;
			entityDef.anIntArray94[1] = 27773;
			entityDef.anIntArray94[2] = 27764;
			entityDef.anIntArray94[3] = 27765;
			entityDef.anIntArray94[4] = 27770;
			entityDef.combatLevel = 650;
			entityDef.standAnim = 6943;
			entityDef.walkAnim = 6942;
			entityDef.anInt86 = 40;
			entityDef.anInt91 = 40;
			break;

		case 4007:
			entityDef.name = "Commander Zilyana";
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			entityDef.anIntArray94 = new int[4];
			entityDef.anIntArray94[0] = 28057;
			entityDef.anIntArray94[1] = 28071;
			entityDef.anIntArray94[2] = 28078;
			entityDef.anIntArray94[3] = 28056;
			entityDef.combatLevel = 596;
			entityDef.standAnim = 6963;
			entityDef.walkAnim = 6962;
			entityDef.anInt86 = 70;
			entityDef.anInt91 = 70;
			break;

		case 4008:
			entityDef.name = "Dagannoth Supreme";
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			entityDef.anIntArray94 = new int[2];
			entityDef.anIntArray94[0] = 9941;
			entityDef.anIntArray94[1] = 9943;
			entityDef.combatLevel = 303;
			entityDef.standAnim = 2850;
			entityDef.walkAnim = 2849;
			entityDef.anInt86 = 70;
			entityDef.anInt91 = 70;
			break;

		case 4009:
			entityDef.name = "Dagannoth Prime"; // 9940, 9943, 9942
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			entityDef.anIntArray94 = new int[3];
			entityDef.anIntArray94[0] = 9940;
			entityDef.anIntArray94[1] = 9943;
			entityDef.anIntArray94[2] = 9942;
			entityDef.anIntArray70 = new int[] { 11930, 27144, 16536, 16540 };
			entityDef.anIntArray76 = new int[] { 5931, 1688, 21530, 21534 };
			entityDef.combatLevel = 303;
			entityDef.standAnim = 2850;
			entityDef.walkAnim = 2849;
			entityDef.anInt86 = 70;
			entityDef.anInt91 = 70;
			break;

		case 4010:
			entityDef.name = "Dagannoth Rex";
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			entityDef.anIntArray94 = new int[1];
			entityDef.anIntArray94[0] = 9941;
			entityDef.anIntArray70 = new int[] { 16536, 16540, 27144, 2477 };
			entityDef.anIntArray76 = new int[] { 7322, 7326, 10403, 2595 };
			entityDef.combatLevel = 303;
			entityDef.standAnim = 2850;
			entityDef.walkAnim = 2849;
			entityDef.anInt86 = 70;
			entityDef.anInt91 = 70;
			break;

		case 4011:
			entityDef.name = "Ahrim the Blighted";
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			entityDef.anIntArray94 = new int[1];
			entityDef.anIntArray94[0] = 6668;
			entityDef.combatLevel = 98;
			entityDef.standAnim = 813;
			entityDef.walkAnim = 1205;
			entityDef.anInt86 = 100;
			entityDef.anInt91 = 100;
			break;

		case 4012:
			entityDef.name = "Dharok the Wretched";
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			entityDef.anIntArray94 = new int[6];
			entityDef.anIntArray94[0] = 6652;
			entityDef.anIntArray94[1] = 6671;
			entityDef.anIntArray94[2] = 6640;
			entityDef.anIntArray94[3] = 6661;
			entityDef.anIntArray94[4] = 6703;
			entityDef.anIntArray94[5] = 6679;
			entityDef.combatLevel = 115;
			entityDef.standAnim = 2065;
			entityDef.walkAnim = 2064;
			entityDef.anInt86 = 100;
			entityDef.anInt91 = 100;
			break;

		case 4013:
			entityDef.name = "Guthan the Infested";
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			entityDef.anIntArray94 = new int[6];
			entityDef.anIntArray94[0] = 6654;
			entityDef.anIntArray94[1] = 6674;
			entityDef.anIntArray94[2] = 6642;
			entityDef.anIntArray94[3] = 6666;
			entityDef.anIntArray94[4] = 6679;
			entityDef.anIntArray94[5] = 6710;
			entityDef.standAnim = 813;
			entityDef.walkAnim = 1205;
			entityDef.combatLevel = 115;
			entityDef.anInt86 = 100;
			entityDef.anInt91 = 100;
			break;

		case 4014:
			entityDef.name = "Karil the Tainted";
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			entityDef.anIntArray94 = new int[1];
			entityDef.anIntArray94[0] = 6675;
			entityDef.standAnim = 2074;
			entityDef.walkAnim = 2076;
			entityDef.combatLevel = 98;
			entityDef.anInt86 = 100;
			entityDef.anInt91 = 100;
			break;

		case 4015:
			entityDef.name = "Torag the Corrupted";
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			entityDef.anIntArray94 = new int[6];
			entityDef.anIntArray94[0] = 6657;
			entityDef.anIntArray94[1] = 6677;
			entityDef.anIntArray94[2] = 6645;
			entityDef.anIntArray94[3] = 6663;
			entityDef.anIntArray94[4] = 6708;
			entityDef.anIntArray94[5] = 6679;
			entityDef.standAnim = 808;
			entityDef.walkAnim = 819;
			entityDef.combatLevel = 115;
			entityDef.anInt86 = 100;
			entityDef.anInt91 = 100;
			break;
		case 4016:
			entityDef.name = "Verac the Defiled";
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			entityDef.anIntArray94 = new int[2];
			entityDef.anIntArray94[0] = 6678;
			entityDef.anIntArray94[1] = 6705;
			entityDef.standAnim = 2061;
			entityDef.walkAnim = 2060;
			entityDef.combatLevel = 115;
			entityDef.anInt86 = 100;
			entityDef.anInt91 = 100;
			break;
		case 4017:// 15096
			entityDef.name = "Giant Sea Snake";
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			entityDef.anIntArray94 = new int[1];
			entityDef.anIntArray94[0] = 15096;
			EntityDef snake = forID(3943);
			entityDef.standAnim = snake.standAnim;
			entityDef.walkAnim = snake.walkAnim;
			entityDef.combatLevel = 149;
			entityDef.anInt86 = 20;
			entityDef.anInt91 = 20;
			entityDef.aByte68 = 1;
			break;
		case 4018:
			entityDef.name = "Callisto";
			entityDef.combatLevel = 470;
			entityDef.anIntArray94[0] = 28298;
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			callisto = forID(105);
			entityDef.standAnim = callisto.standAnim;
			entityDef.walkAnim = callisto.walkAnim;
			entityDef.anInt86 = 40;
			entityDef.anInt91 = 40;
			break;
		case 4019:
			entityDef.name = "Kalphite Princess";
			entityDef.combatLevel = 470;
			entityDef.anIntArray94[0] = 24597;
			entityDef.anIntArray94[1] = 24598;
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			callisto = forID(1158);
			entityDef.standAnim = callisto.standAnim;
			entityDef.walkAnim = callisto.walkAnim;
			entityDef.anInt86 = 30;
			entityDef.anInt91 = 30;
			break;
		case 4022:
			entityDef.name = "Pet smoke devil";
			entityDef.combatLevel = 301;
			entityDef.anIntArray94[0] = 65309;
			smoke = forID(1624);
			entityDef.standAnim = smoke.standAnim;
			entityDef.walkAnim = smoke.standAnim;
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			entityDef.anInt86 = 60;
			entityDef.anInt91 = 60;
			break;
		case 3999:
			entityDef.anIntArray94 = new int[1];
			entityDef.anIntArray94[0] = 65310;
			entityDef.name = "Vet'ion jr.";
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Pick-up";
			vet = forID(90);
			entityDef.standAnim = vet.standAnim;
			entityDef.walkAnim = vet.walkAnim;
			entityDef.combatLevel = 454;
			entityDef.anInt86 = 60;
			entityDef.anInt91 = 60;
			break;
		}*/
		return entityDef;
	}

	private boolean ac;
	private boolean aq;
	private int az;
	private int anInt2165;
	private int anInt2189;

	public Model method160() {
		if (childrenIDs != null) {
			EntityDef entityDef = method161();
			if (entityDef == null)
				return null;
			else
				return entityDef.method160();
		}
		if (anIntArray73 == null)
			return null;
		boolean flag1 = false;
		for (int i = 0; i < anIntArray73.length; i++)
			if (!Model.method463(anIntArray73[i]))
				flag1 = true;

		if (flag1)
			return null;
		Model aclass30_sub2_sub4_sub6s[] = new Model[anIntArray73.length];
		for (int j = 0; j < anIntArray73.length; j++)
			aclass30_sub2_sub4_sub6s[j] = Model.method462(anIntArray73[j]);

		Model model;
		if (aclass30_sub2_sub4_sub6s.length == 1)
			model = aclass30_sub2_sub4_sub6s[0];
		else
			model = new Model(aclass30_sub2_sub4_sub6s.length,
					aclass30_sub2_sub4_sub6s);
		if (anIntArray76 != null) {
			for (int k = 0; k < anIntArray76.length; k++)
				model.method476(anIntArray76[k], anIntArray70[k]);

		}
		return model;
	}

	public EntityDef method161() {
		int j = -1;
		if (anInt57 != -1) {
			VarBit varBit = VarBit.cache[anInt57];
			int k = varBit.anInt648;
			int l = varBit.anInt649;
			int i1 = varBit.anInt650;
			int j1 = Client.anIntArray1232[i1 - l];
			j = clientInstance.variousSettings[k] >> l & j1;
		} else if (anInt59 != -1)
			j = clientInstance.variousSettings[anInt59];
		if (j < 0 || j >= childrenIDs.length || childrenIDs[j] == -1)
			return null;
		else
			return forID(childrenIDs[j]);
	}

	public static void unpackConfig(StreamLoader streamLoader) {
		stream = new Stream(streamLoader.getDataForName("npc.dat"));
		Stream stream2 = new Stream(streamLoader.getDataForName("npc.idx"));
		
//		stream = new Stream(FileOperations.ReadFile("./osrsnpc.dat"));
//		Stream stream2 = new Stream(FileOperations.ReadFile("./osrsnpc.idx"));
		int totalNPCs = stream2.readUnsignedWord();
		streamIndices = new int[totalNPCs];
		int i = 0;
		for (int j = 0; j < totalNPCs; j++) {
			streamIndices[j] = i;
			i += stream2.readUnsignedWord();
		}

		cache = new EntityDef[20];
		for (int k = 0; k < 20; k++)
			cache[k] = new EntityDef();
		
/*		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("./npclist.txt"));
			for (int j = 0; j < totalNPCs - 21; j++) {
				EntityDef def = EntityDef.forID(j);
				writer.write("name= "+def.name+"_"+j+", actions= "+Arrays.toString(def.actions)+", models="+(def.anIntArray94 != null ? Arrays.toString(def.anIntArray94) : "null")+", size"+def.aByte68);
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public static void nullLoader() {
		mruNodes = null;
		streamIndices = null;
		cache = null;
		stream = null;
	}
	//
		public Model method164(int j, int k, int ai[]) {
			if (childrenIDs != null) {
				EntityDef entityDef = method161();
				if (entityDef == null)
					return null;
				else
					return entityDef.method164(j, k, ai);
			}
			Model model = (Model) mruNodes.insertFromCache(interfaceType);
			if (model == null) {
				boolean flag = false;
				for (int i1 = 0; i1 < anIntArray94.length; i1++)
					if (!Model.method463(anIntArray94[i1]))
						flag = true;
	
				if (flag)
					return null;
				Model aclass30_sub2_sub4_sub6s[] = new Model[anIntArray94.length];
				for (int j1 = 0; j1 < anIntArray94.length; j1++)
					aclass30_sub2_sub4_sub6s[j1] = Model
					.method462(anIntArray94[j1]);
	
				if (aclass30_sub2_sub4_sub6s.length == 1)
					model = aclass30_sub2_sub4_sub6s[0];
				else
					model = new Model(aclass30_sub2_sub4_sub6s.length,
							aclass30_sub2_sub4_sub6s);
				if (anIntArray76 != null) {
					for (int k1 = 0; k1 < anIntArray76.length; k1++)
						model.method476(anIntArray76[k1], anIntArray70[k1]);
	
				}
				model.method469();
				model.method479(64 + anInt85, 850 + anInt92, -30, -50, -30, true);
				mruNodes.removeFromCache(model, interfaceType);
			}
			Model model_1 = Model.aModel_1621;
			model_1.method464(model, Class36.method532(k) & Class36.method532(j));
			if (k != -1 && j != -1)
				model_1.method471(ai, j, k);
			else if (k != -1)
				model_1.method470(k);
			if (anInt91 != 128 || anInt86 != 128)
				model_1.method478(anInt91, anInt91, anInt86);
			model_1.method466();
			model_1.anIntArrayArray1658 = null;
			model_1.anIntArrayArray1657 = null;
			if (aByte68 == 1)
				model_1.aBoolean1659 = true;
			return model_1;
		}

	public Model method164(int j, int frame, int ai[], int nextFrame, int idk, int idk2) {
		if (childrenIDs != null) {
			EntityDef entityDef = method161();
			if (entityDef == null)
				return null;
			else
				return entityDef.method164(j, frame, ai, nextFrame, idk, idk2);
		}
		Model model = (Model) mruNodes.insertFromCache(interfaceType);
		if (model == null) {
			boolean flag = false;
			for (int i1 = 0; i1 < anIntArray94.length; i1++)
				if (!Model.method463(anIntArray94[i1]))
					flag = true;

			if (flag)
				return null;
			Model aclass30_sub2_sub4_sub6s[] = new Model[anIntArray94.length];
			for (int j1 = 0; j1 < anIntArray94.length; j1++)
				aclass30_sub2_sub4_sub6s[j1] = Model
				.method462(anIntArray94[j1]);

			if (aclass30_sub2_sub4_sub6s.length == 1)
				model = aclass30_sub2_sub4_sub6s[0];
			else
				model = new Model(aclass30_sub2_sub4_sub6s.length,
						aclass30_sub2_sub4_sub6s);
			if (anIntArray76 != null) {
				for (int k1 = 0; k1 < anIntArray76.length; k1++)
					model.method476(anIntArray76[k1], anIntArray70[k1]);

			}
			model.method469();
			model.method478(132, 132, 132);
			model.method479(84 + anInt85, 1000 + anInt92, -90, -580, -90, true);
			mruNodes.removeFromCache(model, interfaceType);
		}
		Model model_1 = Model.aModel_1621;
		model_1.method464(model, Class36.method532(frame) & Class36.method532(j) & Class36.method532(nextFrame));
		if (frame != -1 && j != -1)
			model_1.method471(ai, j, frame);
		else if (frame != -1 && nextFrame != -1)
			model_1.method470(frame, nextFrame, idk, idk2);
		else if (frame != -1)
			model_1.method470(frame);
		if (anInt91 != 128 || anInt86 != 128)
			model_1.method478(anInt91, anInt91, anInt86);
		model_1.method466();
		model_1.anIntArrayArray1658 = null;
		model_1.anIntArrayArray1657 = null;
		if (aByte68 == 1)
			model_1.aBoolean1659 = true;
		return model_1;
	}


	public void readValues(Stream stream) {
		do {
			int i = stream.readUnsignedByte();
			if (i == 0)
				return;
			if (i == 1) {
				int j = stream.readUnsignedByte();
				anIntArray94 = new int[j];
				for (int j1 = 0; j1 < j; j1++)
					anIntArray94[j1] = stream.readUnsignedWord();

			} else if (i == 2)
				name = stream.readNewString();
			else if (i == 3)
				description = stream.readBytes();
			else if (i == 12)
				aByte68 = stream.readSignedByte();
			else if (i == 13)
				standAnim = stream.readUnsignedWord();
			else if (i == 14)
				walkAnim = stream.readUnsignedWord();
			else if (i == 17) {
				walkAnim = stream.readUnsignedWord();
				anInt58 = stream.readUnsignedWord();
				anInt83 = stream.readUnsignedWord();
				anInt55 = stream.readUnsignedWord();
			} else if (i >= 30 && i < 40) {
				if (actions == null)
					actions = new String[5];
				actions[i - 30] = stream.readNewString();
				if (actions[i - 30].equalsIgnoreCase("hidden"))
					actions[i - 30] = null;
			} else if (i == 40) {
				int k = stream.readUnsignedByte();
				anIntArray76 = new int[k];
				anIntArray70 = new int[k];
				for (int k1 = 0; k1 < k; k1++) {
					anIntArray76[k1] = stream.readUnsignedWord();
					anIntArray70[k1] = stream.readUnsignedWord();
				}

			}  else if (i == 41) {
				int k = stream.readUnsignedByte();
				retextureSrc = new int[k];
				retextureDst = new int[k];
				for (int k1 = 0; k1 < k; k1++) {
					retextureSrc[k1] = stream.readUnsignedWord();
					retextureDst[k1] = stream.readUnsignedWord();
				}
			} else if (i == 60) {
				int l = stream.readUnsignedByte();
				anIntArray73 = new int[l];
				for (int l1 = 0; l1 < l; l1++)
					anIntArray73[l1] = stream.readUnsignedWord();

			} else if (i == 90)
				stream.readUnsignedWord();
			else if (i == 91)
				stream.readUnsignedWord();
			else if (i == 92)
				stream.readUnsignedWord();
			else if (i == 93)
				aBoolean87 = false;
			else if (i == 95)
				combatLevel = stream.readUnsignedWord();
			else if (i == 97)
				anInt91 = stream.readUnsignedWord();
			else if (i == 98)
				anInt86 = stream.readUnsignedWord();
			else if (i == 99)
				aBoolean93 = true;
			else if (i == 100)
				anInt85 = stream.readSignedByte();
			else if (i == 101)
				anInt92 = stream.readSignedByte() * 5;
			else if (i == 102)
				anInt75 = stream.readUnsignedWord();
			else if (i == 103)
				anInt79 = stream.readUnsignedWord();
			else if (i == 106) {
				anInt57 = stream.readUnsignedWord();
				if (anInt57 == 65535)
					anInt57 = -1;
				anInt59 = stream.readUnsignedWord();
				if (anInt59 == 65535)
					anInt59 = -1;
				int i1 = stream.readUnsignedByte();
				childrenIDs = new int[i1 + 1];
				for (int i2 = 0; i2 <= i1; i2++) {
					childrenIDs[i2] = stream.readUnsignedWord();
					if (childrenIDs[i2] == 65535)
						childrenIDs[i2] = -1;
				}

			} else if (i == 107)
				aBoolean84 = false;
			else if (i == 109)
				this.ac = false;
			else if (i == 111)
				this.aq = true;
			else if (i == 112)
				this.az = stream.readUnsignedByte();
			else
				System.out.println("MISSING" + i);
		} while (true);
	}

//	public void readValues(Stream stream) {
//		do {
//			int i = stream.readUnsignedByte();
//			if (i == 0)
//				return;
//			if (i == 1) {
//				int j = stream.readUnsignedByte();
//				anIntArray94 = new int[j];
//				for (int j1 = 0; j1 < j; j1++)
//					anIntArray94[j1] = stream.readUnsignedWord();
//
//			} else if (i == 2)
//				name = stream.readString2();
//			else if (i == 3)
//				description = stream.readBytes();
//			else if (i == 12)
//				aByte68 = stream.readSignedByte();
//			else if (i == 13)
//				standAnim = stream.readUnsignedWord();
//			else if (i == 14)
//				walkAnim = stream.readUnsignedWord();
//			else if(i == 15) {
//				this.anInt2165 =stream.readUnsignedWord();
//			} else if(i == 16) {
//				this.anInt2189 = stream.readUnsignedWord();
//			} else if (i == 17) {
//				walkAnim = stream.readUnsignedWord();
//				anInt58 = stream.readUnsignedWord();
//				anInt83 = stream.readUnsignedWord();
//				anInt55 = stream.readUnsignedWord();
//			} else if (i >= 30 && i < 35) {
//				if (actions == null)
//					actions = new String[5];
//				actions[i - 30] = stream.readString2();
//				if (actions[i - 30].equalsIgnoreCase("hidden"))
//					actions[i - 30] = null;
//			} else if (i == 40) {
//				int k = stream.readUnsignedByte();
//				anIntArray76 = new int[k];
//				anIntArray70 = new int[k];
//				for (int k1 = 0; k1 < k; k1++) {
//					anIntArray76[k1] = stream.readUnsignedWord();
//					anIntArray70[k1] = stream.readUnsignedWord();
//				}
//
//			}  else if (i == 41) {
//				int k = stream.readUnsignedByte();
//				retextureSrc = new int[k];
//				retextureDst = new int[k];
//				for (int k1 = 0; k1 < k; k1++) {
//					retextureSrc[k1] = stream.readUnsignedWord();
//					retextureDst[k1] = stream.readUnsignedWord();
//				}
//			} else if (i == 60) {
//				int l = stream.readUnsignedByte();
//				anIntArray73 = new int[l];
//				for (int l1 = 0; l1 < l; l1++)
//					anIntArray73[l1] = stream.readUnsignedWord();
//
////			} else if (i == 90)
////				stream.readUnsignedWord();
////			else if (i == 91)
////				stream.readUnsignedWord();
////			else if (i == 92)
////				stream.readUnsignedWord();
//			} else if (i == 93)
//				aBoolean87 = false;
//			else if (i == 95)
//				combatLevel = stream.readUnsignedWord();
//			else if (i == 97)
//				anInt91 = stream.readUnsignedWord();
//			else if (i == 98)
//				anInt86 = stream.readUnsignedWord();
//			else if (i == 99)
//				aBoolean93 = true;
//			else if (i == 100)
//				anInt85 = stream.readSignedByte();
//			else if (i == 101)
//				anInt92 = stream.readSignedByte();
//			else if (i == 102)
//				anInt75 = stream.readUnsignedWord();
//			else if (i == 103)
//				anInt79 = stream.readUnsignedWord();
//			else if (i == 106) {
//				anInt57 = stream.readUnsignedWord();
//				if (anInt57 == 65535)
//					anInt57 = -1;
//				anInt59 = stream.readUnsignedWord();
//				if (anInt59 == 65535)
//					anInt59 = -1;
//				int i1 = stream.readUnsignedByte();
//				childrenIDs = new int[i1 + 1];
//				for (int i2 = 0; i2 <= i1; i2++) {
//					childrenIDs[i2] = stream.readUnsignedWord();
//					if (childrenIDs[i2] == 65535)
//						childrenIDs[i2] = -1;
//				}
//
//			} else if (i == 107)
//				aBoolean84 = false;
//			else if (i == 109)
//				this.ac = false;
//			else if (i == 111)
//				this.aq = true;
////			else if (i == 112)
////				this.az = stream.readUnsignedByte();
//			else
//				System.out.println("MISSING" + i);
//		} while (true);
//	}
	public void readValuesOld(Stream stream) {
		do {
			int i = stream.readUnsignedByte();
			if (i == 0)
				return;
			if (i == 1) {
				int j = stream.readUnsignedByte();
				anIntArray94 = new int[j];
				for (int j1 = 0; j1 < j; j1++)
					anIntArray94[j1] = stream.readUnsignedWord();

			} else if (i == 2)
				name = stream.readNewString();
			else if (i == 3)
				description = stream.readBytes();
			else if (i == 12)
				aByte68 = stream.readSignedByte();
			else if (i == 13)
				standAnim = stream.readUnsignedWord();
			else if (i == 14)
				walkAnim = stream.readUnsignedWord();
			else if (i == 17) {
				walkAnim = stream.readUnsignedWord();
				anInt58 = stream.readUnsignedWord();
				anInt83 = stream.readUnsignedWord();
				anInt55 = stream.readUnsignedWord();
			} else if (i >= 30 && i < 40) {
				if (actions == null)
					actions = new String[5];
				actions[i - 30] = stream.readNewString();
				if (actions[i - 30].equalsIgnoreCase("hidden"))
					actions[i - 30] = null;
			} else if (i == 40) {
				int k = stream.readUnsignedByte();
				anIntArray76 = new int[k];
				anIntArray70 = new int[k];
				for (int k1 = 0; k1 < k; k1++) {
					anIntArray76[k1] = stream.readUnsignedWord();
					anIntArray70[k1] = stream.readUnsignedWord();
				}

			} else if (i == 60) {
				int l = stream.readUnsignedByte();
				anIntArray73 = new int[l];
				for (int l1 = 0; l1 < l; l1++)
					anIntArray73[l1] = stream.readUnsignedWord();

			} else if (i == 90)
				stream.readUnsignedWord();
			else if (i == 91)
				stream.readUnsignedWord();
			else if (i == 92)
				stream.readUnsignedWord();
			else if (i == 93)
				aBoolean87 = false;
			else if (i == 95)
				combatLevel = stream.readUnsignedWord();
			else if (i == 97)
				anInt91 = stream.readUnsignedWord();
			else if (i == 98)
				anInt86 = stream.readUnsignedWord();
			else if (i == 99)
				aBoolean93 = true;
			else if (i == 100)
				anInt85 = stream.readSignedByte();
			else if (i == 101)
				anInt92 = stream.readSignedByte() * 5;
			else if (i == 102)
				anInt75 = stream.readUnsignedWord();
			else if (i == 103)
				anInt79 = stream.readUnsignedWord();
			else if (i == 106) {
				anInt57 = stream.readUnsignedWord();
				if (anInt57 == 65535)
					anInt57 = -1;
				anInt59 = stream.readUnsignedWord();
				if (anInt59 == 65535)
					anInt59 = -1;
				int i1 = stream.readUnsignedByte();
				childrenIDs = new int[i1 + 1];
				for (int i2 = 0; i2 <= i1; i2++) {
					childrenIDs[i2] = stream.readUnsignedWord();
					if (childrenIDs[i2] == 65535)
						childrenIDs[i2] = -1;
				}

			} else if (i == 107)
				aBoolean84 = false;
		} while (true);
	}



	public EntityDef() {
		anInt55 = -1;
		anInt57 = -1;
		anInt58 = -1;
		anInt59 = -1;
		combatLevel = -1;
		anInt64 = 1834;
		walkAnim = -1;
		aByte68 = 1;
		anInt75 = -1;
		standAnim = -1;
		interfaceType = -1L;
		anInt79 = 32;
		anInt83 = -1;
		aBoolean84 = true;
		anInt86 = 128;
		aBoolean87 = true;
		anInt91 = 128;
		aBoolean93 = false;
	}

	public int anInt55;
	public static int anInt56;
	public int anInt57;
	public int anInt58;
	public int anInt59;
	public static Stream stream;
	public int combatLevel;
	public final int anInt64;
	public String name;
	public String actions[];
	public int walkAnim;
	public byte aByte68;
	public int[] anIntArray70;
	public static int[] streamIndices;
	public int[] anIntArray73;
	public int anInt75;
	public int[] anIntArray76;
	public int standAnim;
	public long interfaceType;
	public int anInt79;
	public static EntityDef[] cache;
	public static Client clientInstance;
	public int anInt83;
	public boolean aBoolean84;
	public int anInt85;
	public int anInt86;
	public boolean aBoolean87;
	public int childrenIDs[];
	public byte description[];
	public int anInt91;
	public int anInt92;
	public boolean aBoolean93;
	public int[] anIntArray94;
	private int[] retextureDst;
	private int[] retextureSrc;
	public static MRUNodes mruNodes = new MRUNodes(30);

}
