package com.ekstemicraft.plugin.ecsync;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class ECSync extends JavaPlugin{


public static ECSync main; //This makes sure I can access plugin.etcblah using <classname>.main in other classes.
	@Override
	public void onEnable() {
		//Enable method

        main = this; //This makes sure I can access plugin.etcblah using <classname>.main in other classes.
		//Enabling/registering  Listeners
		PluginManager pm = Bukkit.getServer().getPluginManager();
				pm.registerEvents(new ECSyncListener(null), this);
				
		//Setting ECSCommandExecutor as the... CommandExecutor!
		getCommand("sync").setExecutor(new ECSCommandExecutor(this));
		getCommand("verify").setExecutor(new ECSCommandExecutor(this));
			
		//Config file
		File file = new File(getDataFolder() + File.separator + "config.yml");
		if (!file.exists()){
			
			this.getConfig().options().copyDefaults(true);
			this.getLogger().info("Generating ECSync config");
			this.getConfig().addDefault("database.ip", "ipadress");
			this.getConfig().addDefault("database.user", "username");
			this.getConfig().addDefault("database.database", "databasename");
			this.getConfig().addDefault("database.password", "password");
			this.saveConfig();
			
		}
	}

	@Override
	public void onDisable() {
		//Disable method

		main = null;
	}
 }