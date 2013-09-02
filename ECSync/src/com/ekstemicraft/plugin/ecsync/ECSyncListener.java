package com.ekstemicraft.plugin.ecsync;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class ECSyncListener implements Listener {
	
	ECSync pl;
	SQl sql = new SQl();
	public ECSyncListener(ECSync plugin){
		pl = plugin;
	}
	
	
	@EventHandler
	public void playerLogin(AsyncPlayerPreLoginEvent event){
		String playerName = event.getName();
		try {
			sql.loadUserIDfromDB(playerName);
		} catch (Exception e) {
			pl.getLogger().severe("Error at playerLoginSync: " +  e);
		}
		/** Listens on PlayerJoinEvent, and calls loadUserIDfromDB() when a player joins. 
		 * Changed to AsyncPlayerPreLoginEvent, was PlayerLoginEvent.
		 *
		 */
		
	}
	
	
	

}
