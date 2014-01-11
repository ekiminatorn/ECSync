package com.ekstemicraft.plugin.ecsync;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ECSApplicationCenterCMDExecutor implements CommandExecutor {

	@SuppressWarnings("unused")
	private ECSync plugin;
	
	
	public ECSApplicationCenterCMDExecutor(ECSync plugin){
		
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
	
		//Application Center rejected applicant
		if(label.equalsIgnoreCase("applicationcenterrejected")){
			if(!(sender instanceof Player)){
				if (args.length == 0){
					//If no args specified
					Bukkit.getLogger().severe("Applicationcenterrejected command got no arguments!");
					return true;
				}
				
				if(Bukkit.getPlayerExact(args[0]) == null){
					//Rejected player not online
					return true;
				}else{
					
					if (args.length == 1){
						//Only the name has been specified
						Bukkit.getPlayerExact(args[0]).sendMessage(ChatColor.AQUA + "[BNAC] We're sorry. Your application has been" + ChatColor.DARK_RED + " rejected" + ChatColor.AQUA + ". No reason specified");

					}
					if (args.length == 2){
						//Name and reason has been specified
						Bukkit.getPlayerExact(args[0]).sendMessage(ChatColor.AQUA + "[BNAC] We're sorry. Your application has been" + ChatColor.DARK_RED + " rejected." + ChatColor.AQUA + " Reason: " + args[1]);
					}
							
				}				
			}else{
				sender.sendMessage(ChatColor.RED + "This command is ONLY for the Application Center. Incident logged");
				String logplayer = sender.getName();
				Bukkit.getLogger().severe(logplayer + "used applicationcenterrejected command. Action denied.");
			}
			
		}
		
		//Send message to applicant of approval if online
		if(label.equalsIgnoreCase("applicationcenterapproved")){
			
			if(!(sender instanceof Player)){
				if(args.length == 0){
					//If no args specified
					Bukkit.getLogger().severe("Applicationcenterapproved command got no arguments!");
					return true;	
				}
				if(args.length == 1){
					//Approved player argument
					//Set player to newbie and take away guest
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "perm player addgroup " + args[0] + " newbie");
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "perm player removegroup " + args[0] + " default");
					
					if(Bukkit.getPlayerExact(args[0]) == null){
						//Approved player not online
						return true;
					}else{
                         Bukkit.getPlayerExact(args[0]).sendMessage(ChatColor.AQUA + "[BNAC] Congratulations! Your application has been " + ChatColor.GREEN + "approved!");
                         
					}
				
				}else{
					return true;
				}
							
			}else{
				sender.sendMessage(ChatColor.RED + "This command is ONLY for the Application Center. Incident logged");
				String logplayer = sender.getName();
				Bukkit.getLogger().severe(logplayer + "used applicationcenterapproved command. Action denied.");
			}
			
			
			
			
		}
		
		
		
		
		
		
	return true;	
	}

}
