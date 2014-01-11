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
				sender.sendMessage(ChatColor.RED + "This command is ONLY for the Application Center. Incident reported to Administrators");
			}
			
		}
		
		
		
		
		
		
	return true;	
	}

}
