package com.ekstemicraft.plugin.ecsync;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;

public class ECSCommandExecutor implements CommandExecutor {
	
	@SuppressWarnings("unused")
	private ECSync plugin;
	SQl sql = new SQl();
	
	
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
		
		
	if(label.equalsIgnoreCase("deny")){
		
		if(args.length == 1){
			
			
			
			
			
		}else{
			sender.sendMessage("Command syntax: /deny <playername>");
			sender.sendMessage("This command denies guests from verifying themselves, good if there is a player, and you don't want him/her to get build rights");
		}
		
	}
	if(label.equalsIgnoreCase("savebook")){
		
		if(!(sender instanceof Player)){
			sender.sendMessage("Command cannot be ran from console! Bad boy!");
			return true;
		}
		//Casting sender to Player
		Player player = (Player) sender;
		if(player.hasPermission("ecsync.savebook")){
			
			if(player.getItemInHand().getType().equals(Material.WRITTEN_BOOK)){
				BookMeta meta = (BookMeta) player.getItemInHand().getItemMeta();
				ECSBooks.saveBook(meta.getTitle(), meta);
				
				player.sendMessage("Book saved as " + meta.getTitle() + ".yml");
				
				
			}else{
				player.sendMessage("You need a written book in hand to use this command!");
				return true;
			}
			
			
		}else{
			player.sendMessage("No permishun! Uncle_Emil forbids!");
			return true;
		}
		
		
		
	}
	if(label.equalsIgnoreCase("book")){
	
		
			Player player = (Player) sender;
			if(args.length == 0){
				
               player.sendMessage("Specify the name of the book, please.");
			}
			else{
				ECSBooks book = ECSBooks.getBook(args[0]);
				
				if(book != null){
					book.spawnBook(player, 0); // 0 for not being a new guy.
				}
				else{
					player.sendMessage("That book doesn't exist!");
				}
			}
					
		
	}
	if(label.equalsIgnoreCase("booklist")){
		Player player = (Player) sender;
		ECSBooks.bookList(player);
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
	 */
	}

}
