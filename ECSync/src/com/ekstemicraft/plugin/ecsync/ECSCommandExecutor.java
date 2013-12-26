package com.ekstemicraft.plugin.ecsync;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ECSCommandExecutor implements CommandExecutor {
	
	@SuppressWarnings("unused")
	private ECSync plugin;
	SQl sql = new SQl();
	public String groupToChange;
	boolean isAllowedToChange = true;
	
	
	public ECSCommandExecutor(ECSync plugin){
		
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		
		if(label.equalsIgnoreCase("sync")){
			
			if(sender instanceof Player){
				if(args.length > 1){
					sender.sendMessage("Too many arguments! Command usage: /sync <playername>");
					return true;
				}
				if(args.length == 1){
					String playerName = args[0];
					if(SQl.playerUserIDs.containsKey(playerName)){
					sender.sendMessage(ChatColor.AQUA + "[BNSync] " + ChatColor.AQUA + "Synchronized " + playerName + "'s group to forum");
					
					try {
						sql.loadUserIDfromDB(playerName);
					} catch (Exception e) {
						Bukkit.getLogger().severe("Error at onCommand() sync. " + e.getMessage());
					}
					return true;
					}
					sender.sendMessage(ChatColor.AQUA + "[BNSync] " + ChatColor.RED + playerName + " is not registered at the website!");
                  return true;
				}
				
				Player player = (Player) sender;
				String playerName = player.getName();
				if(SQl.playerUserIDs.containsKey(playerName)){
				sender.sendMessage(ChatColor.AQUA + "[BNSync] " + ChatColor.AQUA + "Synchronized your group to forum.");
				try {
					sql.loadUserIDfromDB(playerName);
				} catch (Exception e) {
					Bukkit.getLogger().severe("Error at onCommand() /sync. " + e.getMessage());
				}
				return true;
				}else{
					sender.sendMessage(ChatColor.AQUA + "[BNSync] " + ChatColor.RED + "You are not registered at our website!");
					return true;
				}
			}
			
			else{
				if(args.length == 1){
					String playerName = args[0];
					sender.sendMessage(ChatColor.AQUA + "Synchronized " + playerName + "'s group to forum");
					try {
						sql.loadUserIDfromDB(playerName);
					} catch (Exception e) {
						Bukkit.getLogger().severe("Error at onCommand() sync. " + e.getMessage());
					}
                  return true;
				}
				sender.sendMessage("Console cannot sync himself!");
			}
		}
		
		
		if(label.equalsIgnoreCase("verify")){
			if(sender instanceof Player){
				sender.sendMessage(ChatColor.AQUA + "[BNSync] Querying database...");
				String playerName = sender.getName();
				
				
				
				try {
					sql.loadUserIDfromDBForVerifyCommand(playerName);
				} catch (Exception e1) {
					Bukkit.getLogger().severe(e1.getMessage());
				}
				
				if(!SQl.playerUserIDs.containsKey(playerName)){
					sender.sendMessage(ChatColor.RED + "User not found in website! Have you registered or did you mistype your Minecraft username on the site?");
					sender.sendMessage(ChatColor.RED + "If you mistyped your Minecraft username on the site, contact online Staff members, or via support.");
					Bukkit.broadcast("Notice! User " + playerName + " failed verification, maybe not registered, or mistyped their Minecraft username on the site. Redirect this message to Uncle_Emil", "ecsync.receive");
					return true;
				}
				int verifyresult = 0;

				try {
					verifyresult = sql.isVerified(playerName);
				} catch (Exception e) {
					Bukkit.getLogger().severe("Error at onCommand verify " + e);
					sender.sendMessage(ChatColor.AQUA + "[BNSync] " + ChatColor.RED + "We encountered an error while querying the database, please contact Staff");
				}
					if(verifyresult == 1){
						sender.sendMessage(ChatColor.AQUA + "[BNSync] " + ChatColor.GREEN + "Successfully verified! You received build rights!");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "perm player addgroup " + playerName + " newbie");
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "perm player removegroup " + playerName + " default");
						Bukkit.getLogger().info(playerName + " received build rights.");
						Bukkit.broadcast("User " + playerName + " received build rights.", "ecsync.receive");
					}
				    if(verifyresult == 2){
				    	sender.sendMessage(ChatColor.AQUA + "[BNSync] " + ChatColor.GREEN + "You are already verified!");
				    	
				    }
				    if(verifyresult == 3){
				    	sender.sendMessage(ChatColor.AQUA + "[BNSync] " + ChatColor.RED + "Email adress not confirmed, please confirm your email adress on the site, and try again!");
				    }	
			}
		}
	
	if(label.equalsIgnoreCase("promote")){
        //Permission check
		if(sender.hasPermission("ecsync.promote")){
		
		PermissionManagerVault perms = new PermissionManagerVault();
		//Checks if the command has 1 argument (the playername to promote)	
		if(args.length == 1){
			String playerName = args[0];
           String[] groups = perms.getGroups(playerName);

			//Checks if user is found, or user is not in any groups at all (Usually guest if no groups found)
			if(groups.length == 0){
				sender.sendMessage(ChatColor.RED + "User not found or is guest..?");
				return true;
			} 
			//If there is only ONE group
			if(!(groups.length > 1)){
				/*
				 * Don't be confused that we use '1' to test how many arguments there is, and that we use 0 in groups to get the argument
				 */
				String currentGroup = groups[0];
                isAllowedToChange = true;
				switch(currentGroup.toLowerCase()){
				case "newbie":
					groupToChange = "regular";
					break;
				case "regular":
					groupToChange = "member";
					
					break;
				case "member":
					groupToChange = "respected";
					break;
				case "respected":
					groupToChange = "trusted";
					break;
				case "trusted":
					groupToChange = "veteran";
					break;
				case "veteran":
					isAllowedToChange = false;
					break;
				default:
					isAllowedToChange = false;
					break;
				}
				if(isAllowedToChange){
					perms.promoteUser(playerName, groupToChange, currentGroup.toLowerCase());
					sender.sendMessage(ChatColor.AQUA + playerName + ChatColor.GREEN +  " has been promoted to " + ChatColor.AQUA + groupToChange);
				}else{
					sender.sendMessage(ChatColor.RED + "I'm confused! Is the player already end of line (Veteran) or in special group? (Architect, etc.)");
				}
			//If there is more than one group, send error message to promoter	
			}else{
				sender.sendMessage("User is in multiple groups. To be on the safe-side, I wont promote automatically. Has to be done manually with these commands:");
				sender.sendMessage("Check users groups with: /perm player groups <playername>");
				sender.sendMessage("Remove user from the unnecessary groups with /perm player removegroup <playername> <groupname>");
				sender.sendMessage("After that, promote user manually to the group with /perm player addgroup <playername> <groupnametopromote>");
				sender.sendMessage(ChatColor.RED + "If in doubt, ask Emil!");
			}
				
		}
		//Section where we manually specify what group to put in
		//Checks if there is two arguments (playername and what group to promote to.)
		if(args.length == 2){
			String playerName = args[0];
			String groupToChange = args[1];
			String[] groups = perms.getGroups(playerName);
			
			if(groups.length == 0){
				sender.sendMessage(ChatColor.RED + "User not found or is guest..?");
				return true;
			}
			//If there is only ONE group
			if(!(groups.length > 1)){
				String oldGroup = groups[0];
				
				if(perms.groupExist(groupToChange)){
                   perms.promoteUser(playerName, groupToChange, oldGroup.toLowerCase());
					sender.sendMessage(ChatColor.AQUA + playerName + ChatColor.GREEN +  " has been promoted to " + ChatColor.AQUA + groupToChange);
				
				}else{
					sender.sendMessage(ChatColor.RED + "The specified group is not found");
				}
				
			}else{
				sender.sendMessage("User is in multiple groups. To be on the safe-side, I wont promote automatically. Has to be done manually with these commands:");
				sender.sendMessage("Check users groups with: /perm player groups <playername>");
				sender.sendMessage("Remove user from the unnecessary groups with /perm player removegroup <playername> <groupname>");
				sender.sendMessage("After that, promote user manually to the group with /perm player addgroup <playername> <groupnametopromote>");
				sender.sendMessage(ChatColor.RED + "If in doubt, ask Emil!");
			}
			
		}	
		
		}
		
	}
		
	return true;
	
	/**
	 * Command method. 
	 * sync: Synchronizes player group to forum if it has changed.
	 * 
	 * verify: Checks if the user is registered on the forum, and email is confirmed. 
	 * If verifyresult is 1: Player gets buildrights (Changes permissiongroup to newbie).
	 * If verifyresult is 2: Sends a message to the player telling that he/she is already verified at the site.
	 * If verifyresult is 3: Sends a message to the player telling that he/she email adress is not confirmed.
	 * 
	 * promote: Promoting command. Promotes the specified user according to the rank tree or manually specifying the group to promote to.
	 */
	}

}
