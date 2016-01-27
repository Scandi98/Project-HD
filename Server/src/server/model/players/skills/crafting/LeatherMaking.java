package server.model.players.skills.crafting;

import server.Config;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.Client;

public abstract class LeatherMaking {

	public static final int THREAD = 1734;
	public static final int NEEDLE = 1733;
	public static final int LEATHER_MAKING = 1249;

	protected Client player;

	protected int used;

	protected int used2;

	protected int result;

	protected int amount;

	protected int manualAmount;

	protected int level;

	protected double experience;

	protected LeatherMaking(Client player, int used, int used2, int result, int amount, int manualAmount, int level, double experience) {
		this.player = player;
		this.used = used;
		this.used2 = used2;
		this.result = result;
		this.manualAmount = manualAmount;
		this.amount = amount;
		this.level = level;
		this.experience = experience;
	}

	public boolean makeLeatherAction() {
		player.getPA().removeAllWindows();
		if (!player.getItems().playerHasItem(NEEDLE)) {
			player.sendMessage("You need a needle to do this.");
			return true;
		}
		if (!player.getItems().playerHasItem(THREAD)) {
			player.sendMessage("You need thread to do this.");
			return true;
		}
		if (!player.getItems().playerHasItem(used, used2)) {
			player.sendMessage("You need "+used2+" " + player.getItems().getItemName(used) + " to do this.");
			return true;
		}
		if (player.playerLevel[player.playerCrafting] < level) {
			player.sendMessage("You need a crafting level of " + level + " to make this.");
			return true;
		}

		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {
			int actionAmount = amount != 0 ? amount : manualAmount;
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task) || actionAmount == 0) {
					player.startAnimation(-1);
					container.stop();
					return;
				}
				if (!player.getItems().playerHasItem(LeatherCrafting.THREAD)) {
					player.sendMessage("You have run out of thread!");
					container.stop();
					return;
				}
				if (!player.getItems().playerHasItem(used, used2)) {
					player.sendMessage("You have run out of "+player.getItems().getItemName(used)+"!");
					container.stop();
					return;
				}
				player.startAnimation(LEATHER_MAKING);
				player.sendMessage("You make some " + player.getItems().getItemName(result) + ".");
				player.getItems().deleteItem(THREAD, 1);
				player.getItems().deleteItem3(used, used2);
				player.getItems().addItem(result, 1);
				player.getPA().addSkillXP((int) experience * Config.CRAFTING_EXPERIENCE, player.playerCrafting);
				actionAmount--;
				container.setTick(3);

			}

			@Override
			public void stop() {
				player.startAnimation(-1);
			}
		});
		CycleEventHandler.getSingleton().addEvent(player, player.getSkilling(), 1);
		return true;
	}

}

