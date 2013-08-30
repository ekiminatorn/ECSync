package com.ekstemicraft.plugin.ecsync;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class ECSync extends JavaPlugin{

	
	
	@Override
	public void onEnable() {
		//Enable message
		getLogger().info("Enabling ECSync");
		
		
		//Enabling/registering  Listeners
		PluginManager pm = Bukkit.getServer().getPluginManager();
				pm.registerEvents(new ECSyncListener(null), this);
					
	}
	
	@Override
	public void onDisable() {
		//Disable message
		getLogger().info("Disabling ECSync");
		

	}
	
	
	
 }
