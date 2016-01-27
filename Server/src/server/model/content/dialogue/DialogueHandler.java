package server.model.content.dialogue;

import server.cache.RSNPCDefinition;
import server.core.World;
import server.model.players.Client;
import server.model.players.PlayerSave;
import server.tick.Tickable;

public class DialogueHandler {

	private Client c;

	public DialogueHandler(Client client) {
		this.c = client;
	}

	/**
	 * Handles all talking
	 * 
	 * @param dialogue
	 *            The dialogue you want to use
	 * @param npcId
	 *            The npc id that the chat will focus on during the chat
	 */
	public void sendDialogues(int dialogue, int npcId) {
		c.talkingNpc = npcId;
		switch (dialogue) {
		case 3438:
			if (c.playerLevel[0] >= 98 && c.playerLevel[1] >= 98
			&& c.playerLevel[2] >= 98 && c.playerLevel[3] >= 98
			&& c.playerLevel[4] >= 98 && c.playerLevel[5] >= 98
			&& c.playerLevel[6] >= 98 && c.playerLevel[7] >= 98
			&& c.playerLevel[8] >= 98 && c.playerLevel[9] >= 98
			&& c.playerLevel[10] >= 98 && c.playerLevel[11] >= 98
			&& c.playerLevel[12] >= 98 && c.playerLevel[13] >= 98
			&& c.playerLevel[14] >= 98 && c.playerLevel[15] >= 98
			&& c.playerLevel[16] >= 98 && c.playerLevel[17] >= 98
			&& c.playerLevel[18] >= 98 && c.playerLevel[19] >= 98
			&& c.playerLevel[20] >= 99) {
				sendNpcChat2(
						"You seem to be a master in all skills, Would you ",
						"like to purchase a completionist cape for 5M?",
						c.talkingNpc, "Comp Master");
				c.nextChat = 3439;
			} else {
				c.sendMessage("Come back when you're a master in all skills.");
			}
			break;
		case 3439:
			sendOption2("Purchase cape.", "No thanks.");
			c.dialogueAction = 3439;
			break;
		case 2040:
			sendOption2("Return to Zulrah's Shrine", "Nothing.");
			c.dialogueAction = 2040;
			break;
		case 11941:
			c.getDH().sendOption4("One", "Five", "Ten", "All");
			c.dialogueAction = 11941;
			break;
		case 1835:
			sendNpcChat2(
					"Please help me my eggs have been stolen!",
					"The eggs must be delivered today or Easter will be ruined!",
					c.talkingNpc, "Easter Bunny");
			c.nextChat = 1836;
			break;
		case 1836:
			sendNpcChat2(
					"Would you please help me recover the eggs and",
					"save Easter? I may have a reward for you if you can help.",
					c.talkingNpc, "Easter Bunny");
			c.nextChat = 1837;
			break;
		case 1837:
			c.getDH().sendOption2("Yes, I will help you!", "No, sorry im busy");
			c.dialogueAction = 1837;
			break;
		case 1838:
			sendNpcChat4(
					"Thank you for helping me, Their are 6 eggs spread around",
					"Large Cities. The eggs will be in crates or boxes in the following cities:",
					"Varrock, Edgeville, Camelot, Ardougne,",
					"Yanille, Lumbridge", c.talkingNpc, "Easter Bunny");
			c.nextChat = -1;
			break;
		case 1839:
			sendNpcChat3("Thank you so much for recovering my easter eggs!",
					"Easter can now move on and children across",
					"the world will enjoy Easter!", c.talkingNpc,
					"Easter Bunny");
			c.nextChat = 1840;
			break;
		case 1840:
			c.getDH().sendOption2(
					"Please Accept this Gift as my token of appreciation",
					"No thanks.");
			c.dialogueAction = 1840;
			break;
		case 1841:
			sendNpcChat3("The eggs will be in crates in the following cities:",
					"Varrock, Edgeville, Camelot, Ardougne,",
					"Yanille, Lumbridge", c.talkingNpc, "Easter Bunny");
			c.nextChat = -1;
			break;
		case 638:
			sendNpcChat1("Would you like me to decant youre pots for 500k?",
					c.talkingNpc, "Apothecary");
			c.nextChat = 639;
			break;
		case 639:
			c.getDH().sendOption2("Yes, please", "No Thank you.");
			c.dialogueAction = 639;
			break;
		case 9998:
			sendNpcChat1("Would you like to Try out the new Iron Man Mode?",
					c.talkingNpc, "Hans");
			c.nextChat = 9999;
			break;
		case 9999:
			c.getDH().sendOption2(
					"Would you like to try the new Iron Man Mode?",
					"No Thank you.");
			c.dialogueAction = 9999;
			break;
		case 1522:
			sendNpcChat1("Do you really want to claim Donator Status?",
					c.talkingNpc, "Santa");
			c.nextChat = 1523;
			break;
		case 1523:
			c.getDH().sendOption2("Yes", "No");
			c.dialogueAction = 1523;
			break;
		case 5733:
			c.getDH().sendOption5("IP Ban", "Ban", "Mute", "Kick", "Jail");
			c.dialogueAction = 5733;
			break;
		case 1337:
			sendNpcChat1("Please Choose your Title Color", c.talkingNpc,
					"Title Changer");
			c.nextChat = 1338;
			break;
		case 1338:
			c.getDH().sendOption5("Orange", "Purple", "Red", "Green",
					"Next Page");
			c.dialogueAction = 1338;
			break;
		case 2244:
			sendNpcChat1("Do you want change your spellbooks?", c.talkingNpc,
					"Lumbridge Guide");
			c.nextChat = 2245;
			break;
		case 2245:
			c.getDH().sendOption3(
					"Teleport me to Lunar Island, for lunars spellbook!",
					"Teleport me to Desert Pyramid, for ancients spellbook!",
					"No thanks, i will stay here.");
			c.dialogueAction = 2245;
			c.nextChat = 0;
			break;
		case 69:
			c.getDH().sendNpcChat1(
					"Hello! Do you want to choose your clothes?", c.talkingNpc,
					"Thessalia");
			c.sendMessage("@red@You must right-click Thessalia to change your clothes.");
			c.nextChat = 0;
			break;
		case 70:
			sendNpcChat2("You even defeated TzTok-Jad, I am most impressed!",
					"Please accept this gift as a reward.", c.talkingNpc,
					"Tzhaar-Mej-Tal");
			c.nextChat = -1;
			break;
		case 6969:
			c.getDH().sendNpcChat2("I'm not working right now sir.",
					"If you wan't me to work, talk to Ardi give me a job.",
					c.talkingNpc, "Unemployed");
			c.sendMessage("This NPC do not have an action, if you have any suggestion for this NPC, post on forums.");
			c.nextChat = 0;
			break;
		/* LOGIN 1st TIME */
		case 769:
			c.getDH().sendNpcChat2("Welcome to Ardi!",
					"You must select your starter package.", c.talkingNpc,
					"Ardi");
			c.nextChat = 770;
			break;
		case 770:
			sendStatement("Remember we're on beta, we will have a reset before official release.");
			c.nextChat = 771;
			break;
		case 771:
			c.getDH().sendOption3("Master (levels & items)",
					"Zerker (levels & items)", "Pure (levels & items)");
			c.dialogueAction = 771;
			break;
		/* END LOGIN */
		case 691:
			c.getDH().sendNpcChat2("Welcome to 2007remake.",
					"Please, read what i've to tell you...", c.talkingNpc,
					"Mod Ardi");
			c.nextChat = 692;
			// c.loggedIn = 1;
			break;
		case 692:
			sendNpcChat4("2007remake's on pre-alpha state.",
					"Then you can spawn items, and set your levels.",
					"But remember, it's just for pre-alpha sir...",
					"When we do the official release...", c.talkingNpc,
					"Mod Ardi");
			c.nextChat = 693;
			break;
		case 693:
			sendNpcChat4("We will have economy reset and,",
					"this commands will be removed too...",
					"Please, report glitches, and post suggestions",
					"on forums, for i can code, and we get 100% ready!",
					c.talkingNpc, "Mod Ardi");
			c.sendMessage("@red@You're online in 2007remake pre-alpha.");
			c.sendMessage("@red@Pre-alpha's to find glitches, and post suggestions in forums...");
			c.sendMessage("@red@Thanks for your attention sir.");
			c.nextChat = 0;
			break;
		/* AL KHARID */
		case 1022:
			c.getDH().sendPlayerChat1("Can I come through this gate?");
			c.nextChat = 1023;
			break;

		case 1023:
			c.getDH().sendNpcChat1(
					"You must pay a toll of 10 gold coins to pass.",
					c.talkingNpc, "Border Guard");
			c.nextChat = 1024;
			break;
		case 1025:
			c.getDH().sendPlayerChat1("Okay, I'll pay.");
			c.nextChat = 1026;
			break;
		case 1026:
			c.getDH().sendPlayerChat1("Who does my money go to?");
			c.nextChat = 1027;
			break;
		case 1027:
			c.getDH().sendNpcChat2("The money goes to the city of Al-Kharid.",
					"Will you pay the toll?", c.talkingNpc, "Border Guard");
			c.nextChat = 1028;
			break;
		case 1028:
			c.getDH().sendOption2("Okay, I'll pay.",
					"No thanks, I'll walk around.");
			c.dialogueAction = 508;
			break;
		case 1029:
			c.getDH().sendPlayerChat1("No thanks, I'll walk around.");
			c.nextChat = 0;
			break;

		case 22:
			sendOption2("Pick the flowers", "Leave the flowers");
			c.nextChat = 0;
			c.dialogueAction = 22;
			break;
		/** Bank Settings **/
		case 1013:
			c.getDH().sendNpcChat1("Good day. How may I help you?",
					c.talkingNpc, "Banker");
			c.nextChat = 1014;
			break;
		/** What is this place? **/
		case 1015:
			c.getDH().sendPlayerChat1("What is this place?");
			c.nextChat = 1016;
			break;
		case 1016:
			c.getDH().sendNpcChat2("This is the bank of 2007remake.",
					"We have many branches in many towns.", c.talkingNpc,
					"Banker");
			c.nextChat = 0;
			break;
		/**
		 * Note on P I N. In order to check your "Pin Settings. You must have
		 * enter your Bank Pin first
		 **/
		/** I don't know option for Bank Pin **/
		case 0:
			c.talkingNpc = -1;
			c.getPA().removeAllWindows();
			c.nextChat = 0;
			break;
		case 1:
			sendStatement("You found a hidden tunnel! Do you want to enter it?");
			c.dialogueAction = 1;
			c.nextChat = 2;
			break;
		case 2:
			sendOption2("Yea! I'm fearless!", "No way! That looks scary!");
			c.dialogueAction = 1;
			c.nextChat = 0;
			break;
		case 3:
			sendNpcChat4(
					"Hello!",
					"My name is Duradel and I am a master of the slayer skill.",
					"I can assign you a slayer task suitable to your combat level.",
					"Would you like a slayer task?", c.talkingNpc, "Duradel");
			c.nextChat = 4;
			break;
		case 5:
			sendNpcChat4("Hello adventurer...",
					"My name is Kolodion, the master of this mage bank.",
					"Would you like to play a minigame in order ",
					"to earn points towards recieving magic related prizes?",
					c.talkingNpc, "Kolodion");
			c.nextChat = 6;
			break;
		case 6:
			sendNpcChat4("The way the game works is as follows...",
					"You will be teleported to the wilderness,",
					"You must kill mages to recieve points,",
					"redeem points with the chamber guardian.", c.talkingNpc,
					"Kolodion");
			c.nextChat = 15;
			break;
		case 11:
			sendNpcChat4(
					"Hello!",
					"My name is Duradel and I am a master of the slayer skill.",
					"I can assign you a slayer task suitable to your combat level.",
					"Would you like a slayer task?", c.talkingNpc, "Duradel");
			c.nextChat = 12;
			break;
		case 12:
			sendOption3("Yes I would like a slayer task.",
					"I would like an Elite Task (85+ Slayer)",
					"No I would not like a slayer task.");
			c.dialogueAction = 5;
			break;
		case 13:
			sendNpcChat4(
					"Hello!",
					"My name is Duradel and I am a master of the slayer skill.",
					"I see I have already assigned you a task to complete.",
					"Would you like me to give you an easier task?",
					c.talkingNpc, "Duradel");
			c.nextChat = 14;
			break;
		case 14:
			sendOption2("Yes I would like an easier task.",
					"No I would like to keep my task.");
			c.dialogueAction = 6;
			c.nextChat = 0;
			break;
		case 15:
			sendOption2("Yes I would like to play",
					"No, sounds too dangerous for me.");
			c.dialogueAction = 7;
			break;
		case 16:
			sendOption2("I would like to reset my barrows brothers.",
					"I would like to fix all my barrows");
			c.dialogueAction = 8;
			break;
		case 17:
			sendOption5("Air", "Mind", "Water", "Earth", "More");
			c.dialogueAction = 10;
			c.dialogueId = 17;
			c.teleAction = -1;
			break;
		case 18:
			sendOption5("Fire", "Body", "Cosmic", "Astral", "More");
			c.dialogueAction = 11;
			c.dialogueId = 18;
			c.teleAction = -1;
			break;
		case 19:
			sendOption5("Nature", "Law", "Death", "Blood", "More");
			c.dialogueAction = 12;
			c.dialogueId = 19;
			c.teleAction = -1;
			break;
		case 20:
			sendNpcChat4(
					"Haha, hello",
					"My name is Wizard Distentor! I am the master of clue scroll reading.",
					"I can read the magic signs of a clue scroll",
					"You got to pay me 100K for reading the clue though!",
					c.talkingNpc, "Wizard Distentor");
			c.nextChat = 21;
			break;
		case 21:
			sendOption2("Yes I would like to pay 100K", "I don't think so sir");
			c.dialogueAction = 50;
			break;
		case 23:
			sendNpcChat4("Greetings, Adventure",
					"I'm the legendary Vesta seller",
					"With 120 noted Lime Stones, and 20 Million GP",
					"I'll be selling you the Vesta's Spear", c.talkingNpc,
					"Legends Guard");
			c.nextChat = 24;
			break;
		case 54:
			sendOption2("Buy Vesta's Spear", "I can't afford that");
			c.dialogueAction = 51;
			break;
		case 56:
			sendStatement("Hello " + c.playerName + ", you currently have "
					+ c.pkPoints + " PK points.");
			break;

		case 57:
			c.getPA().sendNewString("Teleport to shops?", 2460);
			c.getPA().sendNewString("Yes.", 2461);
			c.getPA().sendNewString("No.", 2462);
			c.getPA().sendFrame164(2459);
			c.dialogueAction = 27;
			break;

		/**
		 * Recipe for disaster - Sir Amik Varze
		 **/

		case 26:
			sendPlayerChat1("Yes");
			c.nextChat = 28;
			break;
		// DuoSlayer
		case 27:
			c.nextChat = 28;
			sendNpcChat2("Hello there, I'm the dual slayer master.",
					"How can I help you?", c.talkingNpc, "Duo Slayer Master");
			break;
		case 28:
			sendOption4("Assign us a task please",
					"Cancel the task (-10points)", "Shop", "Enchanted Gem");
			c.dialogueAction = 8203;
			break;

		case 29:
			c.getPA().removeAllWindows();
			c.nextChat = 0;
			break;
		case 30:
			sendNpcChat4("Congratulations!",
					"You have defeated all Recipe for Disaster bosses",
					"and have now gained access to the Culinaromancer's chest",
					"and the Culinaromancer's item store.", c.talkingNpc,
					"Sir Amik Varze");
			c.nextChat = 0;
			PlayerSave.saveGame(c);
			break;

		/**
		 * Horror from the deep
		 **/
		case 32:
			sendNpcChat4("", "Would you like to start the quest",
					"Horror from the Deep?", "", c.talkingNpc, "Jossik");
			c.nextChat = 33;
			break;
		case 33:
			sendNpcChat4("", "You will have to be able to defeat a level-100 ",
					"Dagannoth mother with different styles of attacks.", "",
					c.talkingNpc, "Jossik");
			c.nextChat = 34;
			break;
		case 35:
			sendPlayerChat1("Yes I am willing to fight!");
			c.nextChat = 37;
			break;
		case 36:
			sendPlayerChat1("No thanks, I am not strong enough.");
			c.nextChat = 0;
			break;

		/**
		 * Desert Treasure dialogue
		 */
		case 41:
			sendNpcChat4("", "Do you want to start the quest",
					"Desert treasure?", "", c.talkingNpc, "Archaeologist");
			c.nextChat = 42;
			break;
		case 42:
			sendNpcChat4("", "You will have to fight four high level bosses,",
					"after each boss you will be brought back",
					"here to refill your supplies if it is needed.",
					c.talkingNpc, "Archaeologist");
			c.nextChat = 43;
			break;
		case 44:
			sendPlayerChat1("Yes I want to fight!");
			c.nextChat = 51;
			break;
		case 45:
			sendPlayerChat1("No thanks, I am not ready.");
			c.nextChat = 0;
			break;
		case 49:
			sendPlayerChat1("Yes, I am ready!");
			c.nextChat = 52;
			break;
		case 50:
			sendPlayerChat1("No, I am not ready.");
			c.nextChat = 0;
			break;

		/**
		 * Cook's Assistant
		 */
		case 100:
			sendNpcChat1("What am I to do?", c.talkingNpc, "Cook");
			c.nextChat = 101;
			break;
		case 102:
			sendPlayerChat1("What`s wrong?");
			c.nextChat = 103;
			break;
		case 103:
			sendNpcChat3(
					"Oh dear, oh dear, oh dear, Im in a terrible terrible",
					"mess! It`s the Duke`s birthday today, and I should be",
					"making him a lovely big birthday cake.", c.talkingNpc,
					"Cook");
			c.nextChat = 104;
			break;
		case 104:
			sendNpcChat4(
					"I`ve forgotten to buy the ingredients. I`ll never get",
					"them in time now. He`ll sack me! What will I do? I have",
					"four children and a goat to look after. Would you help",
					"me? Please?", c.talkingNpc, "Cook");
			c.nextChat = 105;
			break;
		case 107:
			sendNpcChat2("Oh thank you, thank you. I need milk, an egg and",
					"flour. I`d be very grateful if you can get them for me.",
					c.talkingNpc, "Cook");
			c.nextChat = 108;
			break;
		case 109:
			sendNpcChat1(
					"How are you getting on with finding the ingredients?",
					c.talkingNpc, "Cook");
			c.nextChat = 110;
			break;
		case 110:
			sendPlayerChat1("Here's a bucket of milk.");
			c.getItems().deleteItem(1927, 1);
			c.nextChat = 111;
			break;
		case 111:
			sendPlayerChat1("Here's a pot of flour.");
			c.getItems().deleteItem(1933, 1);
			c.nextChat = 112;
			break;
		case 113:
			sendNpcChat2("You've brough me everything I need! I am saved!",
					"Thank you!", c.talkingNpc, "Cook");
			c.nextChat = 0;
			break;
		/*
		 * case 114: sendPlayerChat1("So do I get to go the Duke's Party?");
		 * c.nextChat = 115; break; case 115:
		 * sendNpcChat2("I'm afraid not, only the big cheeses get to dine with the"
		 * , "Duke.", c.talkingNpc, "Cook"); c.nextChat = 116; break; case 116:
		 * sendPlayerChat2
		 * ("Well, maybe one day I'll be important enough to sit on",
		 * "the Duke's table."); c.nextChat = 117; break; case 117:
		 * sendNpcChat1("Maybe, but I won't be holding my breath.",
		 * c.talkingNpc, "Cook"); c.cooksA++; c.cooksA++;
		 * c.getPA().loadQuests(); c.getAA2().COOK2(); c.nextChat = 0; break;
		 */

		// ** Getting Items - Cook's Assistant **//
		case 118:
			sendNpcChat3("There`s a mill fairly close, Go North then West.",
					"Mill Lane Mill is just off the road to Draynor. I",
					"usually get my flour from there.", c.talkingNpc, "Cook");
			c.nextChat = 119;
			break;
		case 119:
			sendNpcChat2(
					"Talk to Millie, she`ll help, she`s a lovely girl and a fine",
					"Miller.", c.talkingNpc, "Cook");
			c.nextChat = 108;
			break;
		case 120:
			sendNpcChat2(
					"There is a cattle field on the other side of the river,",
					"just across the road from the Groats` Farm.",
					c.talkingNpc, "Cook");
			c.nextChat = 121;
			break;
		case 121:
			sendNpcChat3(
					"Talk to Gillie Groats, she looks after the Dairy Cows -",
					"She`ll tell you everything you need to know about",
					"milking cows!", c.talkingNpc, "Cook");
			c.nextChat = 108;
			break;
		case 122:
			sendNpcChat2("I normally get my eggs from the Groats` farm on the",
					"other side of the river.", c.talkingNpc, "Cook");
			c.nextChat = 123;
			break;
		case 123:
			sendNpcChat1("But any chicken should lay eggs.", c.talkingNpc,
					"Cook");
			c.nextChat = 108;
			break;
		case 124:
			sendPlayerChat1("Actually, I know where to find these stuff");
			c.nextChat = 0;
			break;
		case 125:
			sendPlayerChat1("You're a cook why, don't you bake me a cake?");
			c.nextChat = 126;
			break;
		case 126:
			sendNpcChat1("*sniff* Dont talk to me about cakes...",
					c.talkingNpc, "Cook");
			c.nextChat = 102;
			break;
		case 127:
			sendPlayerChat1("You don't look very happy.");
			c.nextChat = 128;
			break;
		case 128:
			sendNpcChat2(
					"No, I`m not. The world is caving in around me - I am",
					"overcome by dark feelings of impending doom.",
					c.talkingNpc, "Cook");
			c.nextChat = 129;
			break;
		case 130:
			sendPlayerChat1("Nice hat!");
			c.nextChat = 131;
			break;
		case 131:
			sendNpcChat1(
					"Err thank you. It`s a pretty ordinary cook`s hat really.",
					c.talkingNpc, "Cook");
			c.nextChat = 132;
			break;
		case 132:
			sendPlayerChat1("Still, suits you. The trousers are pretty special too.");
			c.nextChat = 133;
			break;
		case 133:
			sendNpcChat1("It`s all standard cook`s issue uniform...",
					c.talkingNpc, "Cook");
			c.nextChat = 134;
			break;
		case 134:
			sendPlayerChat2(
					"The whole hat, apron, stripey trousers ensemble -",
					"it works. It makes you look like a real cook.");
			c.nextChat = 135;
			break;
		case 135:
			sendNpcChat2(
					"I am a real cook!, I haven`t got time to be chatting",
					"about Culinary Fashion. I`m in desperate need of help.",
					c.talkingNpc, "Cook");
			c.nextChat = 102;
			break;
		case 136:
			sendPlayerChat1("I'd take off the rest of the day if I were you.");
			c.nextChat = 137;
			break;
		case 137:
			sendNpcChat2(
					"No, that`s the worst thing I could do. I`d get in terrible",
					"trouble.", c.talkingNpc, "Cook");
			c.nextChat = 138;
			break;
		case 138:
			sendPlayerChat1("Well maybe you need to take a holiday...");
			c.nextChat = 139;
			break;
		case 139:
			sendNpcChat2(
					"That would be nice but the duke doesn`t allow holidays",
					"for core staff.", c.talkingNpc, "Cook");
			c.nextChat = 140;
			break;
		case 140:
			sendPlayerChat2("Hmm, why not run away to the sea and start a new",
					"life as a Pirate.");
			c.nextChat = 141;
			break;
		case 141:
			sendNpcChat2(
					"My wife gets sea sick, and i have an irrational fear of",
					"eyepatches. I don`t see it working myself.", c.talkingNpc,
					"Cook");
			c.nextChat = 142;
			break;
		case 142:
			sendPlayerChat1("I`m afraid I've run out of ideas.");
			c.nextChat = 143;
			break;
		case 143:
			sendNpcChat1("I know I`m doomed.", c.talkingNpc, "Cook");
			c.nextChat = 102;
			break;

		//

		case 144:
			sendNpcChat1("Nice day, isn't it?", c.talkingNpc, "");
			c.nextChat = 0;
			break;

		/*
		 * Doric's Quest
		 */

		case 300:
			sendNpcChat1("Why hello there adventurer, how can I help you?",
					c.talkingNpc, "Doric");
			c.nextChat = 301;
			break;

		case 299:
			sendPlayerChat1("I'm just passing by.");
			c.nextChat = 302;
			break;

		case 302:
			sendNpcChat1("Very well, so long.", c.talkingNpc, "Doric");
			c.nextChat = 0;
			break;

		case 303:
			sendPlayerChat1("Nice place you got here.");
			c.nextChat = 304;
			break;

		case 304:
			sendNpcChat1("Why thank you kind sir.", c.talkingNpc, "Doric");
			c.nextChat = 305;
			break;

		case 305:
			sendPlayerChat1("My pleasure.");
			c.nextChat = 0;
			break;

		case 306:
			sendPlayerChat1("I'm looking for a quest.");
			c.nextChat = 307;
			break;

		case 307:
			sendNpcChat2("A quest you say? Hmm...",
					"Can you run me a quick errand?", c.talkingNpc, "Doric");
			c.nextChat = 308;
			break;

		case 309:
			sendPlayerChat1("I need to go.");
			c.nextChat = 0;
			break;

		case 310:
			sendPlayerChat1("Of course!");
			c.nextChat = 311;
			break;

		case 311:
			sendNpcChat3("Very good! I need some materials for a new ",
					"pickaxe I'm working on, is there any way you ",
					"could go get these?", c.talkingNpc, "Doric");
			c.nextChat = 312;
			break;

		case 312:
			sendPlayerChat1("Sure, what materials?");
			c.nextChat = 313;
			break;

		case 313:
			sendNpcChat3("6 lumps of clay,", "4 copper ores,",
					"and 2 iron ores.", c.talkingNpc, "Doric");
			c.nextChat = 314;
			break;

		case 315:
			sendNpcChat1("Thank you adventurer, hurry back!", c.talkingNpc,
					"Doric");
			c.nextChat = 0;
			break;

		case 316:
			sendNpcChat1("Have you got all the materials yet?", c.talkingNpc,
					"Doric");
			c.nextChat = 317;
			break;

		case 317:
			sendPlayerChat1("Not all of them.");
			c.nextChat = 0;
			break;

		case 318:
			sendNpcChat1("Have you got all the materials yet?", c.talkingNpc,
					"Doric");
			c.nextChat = 319;
			break;

		case 319:
			sendPlayerChat1("Yep! Right here.");
			c.nextChat = 320;
			c.getItems().deleteItem(434, 6);
			c.getItems().deleteItem(436, 4);
			c.getItems().deleteItem(440, 2);
			break;

		case 320:
			sendNpcChat2("Thank you so much adventurer, heres a reward",
					"for any hardships you may have encountered.",
					c.talkingNpc, "Doric");
			c.nextChat = 0;
			c.sendMessage("Congradulations, you have completed Doric's Quest!");
			break;

		case 321:
			sendNpcChat1("Welcome to my home, feel free to use my anvils!",
					c.talkingNpc, "Doric");
			c.nextChat = 0;
			break;
		case 322:
			sendStatement("Are you ready to visit the chest room?");
			c.nextChat = 323;
			c.dialogueAction = 322;
			break;
		case 323:
			sendOption2("Yes, I've killed all the other brothers!",
					"No, I still need to kill more brothers");
			c.nextChat = 0;
			break;
		case 324:
			sendStatement("Are you sure you want to combine them?");
			c.nextChat = 325;
			c.dialogueAction = 324;
			break;
		case 325:
			sendOption2("Yes, I wish to combine them, ",
					"No i'm not to sure what it'll do.");
			c.nextChat = 0;
			break;
		case 326:
			sendStatement("Are you sure you want to combine them?");
			c.nextChat = 327;
			c.dialogueAction = 326;
			break;
		case 327:
			sendOption2("Yes, I wish to combine them, ",
					"No i'm not to sure what it'll do.");
			c.nextChat = 0;
			break;
		case 3515:
			sendOption2("Obtain Completionist cape",
					"Don't Obtain Completionist cape");
			c.dialogueAction = 3516;
			break;
			
		}
	}

	/*
	 * Information Box
	 */

	public void sendOption(String s) {
		c.getPA().sendFrame164(13758);
		c.getPA().sendNewString("Select an Option", 2470);
		c.getPA().sendNewString(s, 2471);
		c.getPA().sendNewString("Click here to continue", 2473);
	}

	public void sendOption3(String s, String s1, String s2) {
		c.getPA().sendNewString("Select an Option", 2470);
		c.getPA().sendNewString(s, 2471);
		c.getPA().sendNewString(s1, 2472);
		c.getPA().sendNewString(s2, 2473);
		c.getPA().sendFrame164(2469);
	}

	private void sendNpcChat1(String s, int ChatNpc, String name) {
		c.getPA().sendFrame200(4883, 591);
		c.getPA().sendNewString(name, 4884);
		c.getPA().sendNewString(s, 4885);
		
		c.getPA().sendFrame75(ChatNpc, 4883);
		c.getPA().sendFrame164(4882);
	}

	public void sendNpcChat2(String s, String s1, int ChatNpc, String name) {
		c.getPA().sendFrame200(4888, 591);
		c.getPA().sendNewString(name, 4889);
		c.getPA().sendNewString(s, 4890);
		c.getPA().sendNewString(s1, 4891);
		
		c.getPA().sendFrame75(ChatNpc, 4888);
		c.getPA().sendFrame164(4887);
	}

	public void sendStartInfo(String text, String text1, String text2,
			String text3, String title) {
		c.getPA().sendFrame164(6179);
		c.getPA().sendNewString(title, 6180);
		c.getPA().sendNewString(text, 6181);
		c.getPA().sendNewString(text1, 6182);
		c.getPA().sendNewString(text2, 6183);
		c.getPA().sendNewString(text3, 6184);
	}

	/*
	 * Options
	 */

	public void sendOption2(String s, String s1) {
		c.getPA().sendNewString("Select an Option", 2460);
		c.getPA().sendNewString(s, 2461);
		c.getPA().sendNewString(s1, 2462);
		
		c.getPA().sendFrame164(2459);
	}

	public void sendOption4(String s, String s1, String s2, String s3) {
		c.getPA().sendNewString("Select an Option", 2481);
		c.getPA().sendNewString(s, 2482);
		c.getPA().sendNewString(s1, 2483);
		c.getPA().sendNewString(s2, 2484);
		c.getPA().sendNewString(s3, 2485);
		
		c.getPA().sendFrame164(2480);
	}

	public void sendOption5(String s, String s1, String s2, String s3, String s4) {
		c.getPA().sendNewString("Select an Option", 2493);
		c.getPA().sendNewString(s, 2494);
		c.getPA().sendNewString(s1, 2495);
		c.getPA().sendNewString(s2, 2496);
		c.getPA().sendNewString(s3, 2497);
		c.getPA().sendNewString(s4, 2498);
		
		c.getPA().sendFrame164(2492);
	}
	public void sendOption6(String s, String s1, String s2, String s3, String s4, String s5) {
		c.getPA().sendNewString("Select an Option", 2500);
		c.getPA().sendNewString(s, 2501);
		c.getPA().sendNewString(s1, 2502);
		c.getPA().sendNewString(s2, 2503);
		c.getPA().sendNewString(s3, 2504);
		c.getPA().sendNewString(s4, 2505);
		c.getPA().sendNewString(s5, 2506);
		
		c.getPA().sendFrame164(2499);
	}

	/*
	 * Statements
	 */

	public void sendStatement(String s) { // 1 line click here to continue chat
											// box interface
		c.getPA().sendFrame164(356);
		c.getPA().sendNewString(s, 357);
		c.getPA().sendNewString("Click here to continue", 358);
	}

	public void sendNpcChat3(String s, String s1, String s2, int ChatNpc,
			String name) {
		c.getPA().sendFrame200(4894, 591);
		c.getPA().sendNewString(name, 4895);
		c.getPA().sendNewString(s, 4896);
		c.getPA().sendNewString(s1, 4897);
		c.getPA().sendNewString(s2, 4898);
		c.getPA().sendFrame75(ChatNpc, 4894);
		c.getPA().sendFrame164(4893);
	}

	/*
	 * Npc Chatting
	 */

	private void sendNpcChat4(String s, String s1, String s2, String s3,
			int ChatNpc, String name) {

		c.getPA().sendFrame200(4901, 591);
		c.getPA().sendNewString(name, 4902);
		c.getPA().sendNewString(s, 4903);
		c.getPA().sendNewString(s1, 4904);
		c.getPA().sendNewString(s2, 4905);
		c.getPA().sendNewString(s3, 4906);
		c.getPA().sendFrame75(ChatNpc, 4901);
		c.getPA().sendFrame164(4900);
	}

	/*
	 * Player Chating Back
	 */

	private void sendPlayerChat1(String s) {
		c.getPA().sendFrame200(969, 591);
		c.getPA().sendFrame185(969);
		c.getPA().sendFrame164(968);
		c.getPA().sendNewString(c.playerName, 970);
		c.getPA().sendNewString(s, 971);
	}

	private void sendPlayerChat2(String s, String s1) {
		c.getPA().sendFrame200(974, 591);
		c.getPA().sendFrame185(974);
		c.getPA().sendFrame164(973);
		
		c.getPA().sendNewString(c.playerName, 975);
		c.getPA().sendNewString(s, 976);
		c.getPA().sendNewString(s1, 977);
	}

	private void sendPlayerChat3(String s, String s1, String s2) {
		c.getPA().sendFrame200(980, 591);
		c.getPA().sendFrame185(980);
		c.getPA().sendFrame164(979);
		
		c.getPA().sendNewString(c.playerName, 981);
		c.getPA().sendNewString(s, 982);
		c.getPA().sendNewString(s1, 983);
		c.getPA().sendNewString(s2, 984);
	}

	private void sendPlayerChat4(String s, String s1, String s2, String s3) {
		c.getPA().sendFrame200(987, 591);
		c.getPA().sendNewString(c.playerName, 988);
		c.getPA().sendNewString(s, 989);
		c.getPA().sendNewString(s1, 990);
		c.getPA().sendNewString(s2, 991);
		c.getPA().sendNewString(s3, 992);
		c.getPA().sendFrame185(987);
		c.getPA().sendFrame164(986);
	}
}
