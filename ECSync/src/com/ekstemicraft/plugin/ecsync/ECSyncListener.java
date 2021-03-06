package com.ekstemicraft.plugin.ecsync;



import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class ECSyncListener implements Listener {
	
private final ECSync plugin;
	SQl sql = new SQl();
	String playerName = null;	
	public ECSyncListener(ECSync plugin){
	this.plugin = plugin;

	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerLogin(final AsyncPlayerPreLoginEvent event){
		playerName = event.getName();	

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ECSync.main, new Runnable(){

			@Override
			public void run() {

				try {
					sql.loadUserIDfromDB(playerName);
				} catch (Exception e) {
					plugin.getLogger().severe("Error at playerLoginSync: " +  e);
				}
				
			}		
			
		}, 20L);
		
		/** Listens on PlayerJoinEvent, and calls loadUserIDfromDB() when a player joins after 1 second of delay.
		 * The delay is in place to allow the player to get into the game, and she/he doesnt need to wait on the login screen. 
		 * Changed to AsyncPlayerPreLoginEvent, was PlayerLoginEvent.
		 */
	}
	
}
