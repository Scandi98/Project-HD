
package server.model.content;

import server.model.players.Client;

public class GodBooks {

	public static void fillBook(Client c, int oldBook, int newBook, int page1, int page2, int page3) {
		if (c.getItems().playerHasItem(oldBook, 1) && c.getItems().playerHasItem(page1, 1) && c.getItems().playerHasItem(page2, 1) && c.getItems().playerHasItem(page3, 1)) {
			c.getItems().deleteItem(oldBook, c.getItems().getItemSlot(oldBook), 1);
			c.getItems().deleteItem(page1, c.getItems().getItemSlot(page1), 1);
			c.getItems().deleteItem(page2, c.getItems().getItemSlot(page2), 1);
			c.getItems().deleteItem(page3, c.getItems().getItemSlot(page3), 1);
			c.getItems().addItem(newBook, 1);
		} else {
			c.sendMessage("You need all 3 pages to fill the book!");
		}
	}
	
	public static void itemOnItemHandle(Client c, int useWith, int itemUsed)
	{		
		if ((useWith == 3827) || (useWith == 3827) || (useWith == 3827) && (itemUsed == 3839)) { // sara
			fillBook(c, 3839, 3840, 3827, 3828, 3829);
		}		
		if ((itemUsed == 3827) || (itemUsed == 3828) || (itemUsed == 3829) || (useWith == 3839)) {// sara
			fillBook(c, 3839, 3840, 3827, 3828, 3829);
		}		
		if ((useWith == 3831) || (useWith == 3832) || (useWith == 3833) && (itemUsed == 3841)) { // zam
			fillBook(c, 3841, 3842, 3831, 3832, 3833);
		}		
		if ((itemUsed == 3831) || (itemUsed == 3832) || (itemUsed == 3833) || (useWith == 3841)) { // zam
			fillBook(c, 3841, 3842, 3831, 3832, 3833);
		}		
		if ((useWith == 3835) || (useWith == 3836) || (useWith == 3837) && (itemUsed == 3843)) { // guth
			fillBook(c, 3843, 3844, 3835, 3836, 3837);
		}		
		if ((itemUsed == 3835) || (itemUsed == 3836) || (itemUsed == 3837) || (useWith == 3843)) { // guth
			fillBook(c, 3843, 3844, 3835, 3836, 3837);
		}
	}

}