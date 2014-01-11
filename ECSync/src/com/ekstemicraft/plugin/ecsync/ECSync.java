package com.ekstemicraft.plugin.ecsync;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
		getCommand("promote").setExecutor(new ECSCommandExecutor(this));
		getCommand("applicationcenterrejected").setExecutor(new ECSApplicationCenterCMDExecutor(this));
		getCommand("applicationcenterapproved").setExecutor(new ECSApplicationCenterCMDExecutor(this));
		getCommand("applicationcenternotify").setExecutor(new ECSApplicationCenterCMDExecutor(this));
	
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

		/*Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ECSync.main, new Runnable(){

			@Override
			public void run() {

			   TwitchStream stream = new TwitchStream();
				   stream.scheduler();

				}		
			
		}, 300L); */
			
		guestReminder();		
		
	}

	@Override
	public void onDisable() {
		//Disable method

		main = null;
	}
	
	public void guestReminder(){
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(ECSync.main, new Runnable(){

			@Override
			public void run() {
                for (Player p : Bukkit.getOnlinePlayers()){

					if(!(p.hasPermission("ecsync.dontremind"))){
					  p.sendMessage(ChatColor.AQUA + "Remember to register at our website! http://bladenode.net");
					  p.sendMessage(ChatColor.AQUA + "When you have registered and verified your email, execute /verify in-game to receive build rights!");
					}
				}
				
				
			}		
			
		}, 3600L, 3600L);
		
		
	}
	
 }