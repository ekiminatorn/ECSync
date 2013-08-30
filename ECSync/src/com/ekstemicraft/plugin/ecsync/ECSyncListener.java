package com.ekstemicraft.plugin.ecsync;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ECSyncListener implements Listener {
	
	ECSync pl;
	SQl sql = new SQl();
	public ECSyncListener(ECSync plugin){
		pl = plugin;
	}
	
	
	@EventHandler
	public void playerLogin(PlayerJoinEvent event){
		String playerName = event.getPlayer().getName();
		try {
			sql.loadUserIDfromDB(playerName);
		} catch (Exception e) {
			pl.getLogger().severe("Error at playerLoginSync: " +  e);
		}
	}
	
	
	

}
