package server.social_media.hitbox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.simple.parser.ParseException;

import server.core.PlayerHandler;

public class Hitbox {
	
	/**
	 * A list of all {@link HitboxUser} objects
	 */
	List<HitboxUser> users = new ArrayList<>(Arrays.asList(
			new HitboxUser("gokugaming", "GokuGaming"),
			new HitboxUser("Jamianxd", "jamianXD"),
			new HitboxUser("", ""),
			new HitboxUser("", ""),
			new HitboxUser("", "")
	));
	
	/**
	 * Updates each of the {@link HitboxUser} objects by calling the
	 * read function of each user.
	 */
	public void update() {
		Iterator<HitboxUser> iterator = users.iterator();
		while (iterator.hasNext()) {
			HitboxUser user = iterator.next();
			try {
				user.read();
				checkLiveStatus(user);
			} catch (IllegalStateException | IOException | ParseException e) {
				//iterator.remove();
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Checks the status of the live stream and will announce to all players
	 * that there is an active live stream if one is live.
	 * @param user	the {@link HitboxUser}
	 */
	private void checkLiveStatus(HitboxUser user) {
		if (!user.isLive() && !user.isLiveAnnounced()) {
			return;
		}
		if (user.isLive() && user.isLiveAnnounced()) {
			return;
		}
		if (!user.isLive() && user.isLiveAnnounced()) {
			//PlayerHandler.executeGlobalMessage("@cr3@The livestream '"+user.getName()+"' is now offline. Please tune in next time.");
			user.setLiveAnnounced(false);
			return;
		}
		if (user.isLive() && !user.isLiveAnnounced()) {
			//PlayerHandler.executeGlobalMessage("@cr3@" + "The livestream '"+user.getName()+"' is online at hitbox.tv/"+user.getName());
			user.setLiveAnnounced(true);
		}
	}

}
