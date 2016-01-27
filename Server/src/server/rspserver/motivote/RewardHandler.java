package server.rspserver.motivote;

import com.rspserver.motivote.MotivoteHandler;
import com.rspserver.motivote.Reward;

import server.core.PlayerHandler;
import server.model.players.Client;
import server.model.players.Player;


public class RewardHandler extends MotivoteHandler<Reward>
{
	@Override
	public void onCompletion(Reward reward)
	{
		// SOME OF THIS CODE WILL BE DIFFERENT FOR YOUR SERVER, CHANGE IT ACCORDINGLY. everything to do with motivote will stay the same!
		int itemID = -1;
		
		if (reward.rewardName().equalsIgnoreCase("Vote Point"))
		{
			itemID = 4012;
		}
		
		if (PlayerHandler.isPlayerOn(reward.username()))
		{
			Player p = PlayerHandler.getPlayer(reward.username());
			
			if (p != null && p.isActive == true) // check isActive to make sure player is active. some servers, like project insanity, need extra checks.
			{
				synchronized(p)
				{
					Client c = (Client)p;
					
					if (c.getItems().addItem(itemID, reward.amount()))
					{
						c.votPoints += 2;
						c.getItems().addItemToBank(995, 1000000);
						c.sendMessage("You've received your vote reward! Congratulations! Look in Bank!");
						reward.complete();
					}
					else
					{
						c.sendMessage("Could not give you your reward item, try creating space.");
					}
				}
			}
		}
	}
}