package com.ekstemicraft.plugin.ecsync;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import net.milkbowl.vault.permission.Permission;

public class PermissionManagerVault {

	private static Permission vault;
	ECSync pl;
	
	public PermissionManagerVault(){
		RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
		vault = rsp.getProvider();
				
	}
	
	public String[] getAllPermissionGroups(){
		

		return vault.getGroups();
		/** Gets all the available Permission groups.
		 * 
		 */
	}	
	
	public String[] getGroups(String playerName){
		

		return vault.getPlayerGroups((String) null, playerName);
		/** Gets the players groups using Vault
		 * 
		 */
	}
	
	public boolean addToGroup(String playerName, String groupNew){
		return vault.playerAddGroup((String) null, playerName, groupNew);
		/** Adds the user to a new group, lovely!
		 * 
		 */
	}
	
	public boolean removeFromGroup(String playerName, String groupOld){
		return vault.playerRemoveGroup((String) null, playerName, groupOld);
		/** Removes the user from group, lovely!
		 * 
		 */
	}
	public boolean groupExist(String value){
		String[] groups = getAllPermissionGroups();
		
		for (int i = 0; i < groups.length; i++){
			if(groups[i].equalsIgnoreCase(value)){
				return true;
			}
		}
		
		return false;
		/** 
		 * Checks if the group specified actually exists
		 */
	}
	public void promoteUser(String playerName, String newGroup, String oldGroup){
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "perm player addgroup " + playerName + " " + newGroup);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "perm player removegroup " + playerName + " " + oldGroup);
		
		Player playerWhoGotPromoted = Bukkit.getPlayerExact(playerName);
		Bukkit.broadcastMessage(ChatColor.AQUA + playerWhoGotPromoted.getName() + ChatColor.GOLD + " has been promoted to " + ChatColor.AQUA + newGroup);
		/**
		 * Promotes the user. (Adds to new group and removes from the old one) 
		 * I don't really trust the methods up there, so I gonna make it manually.
		 */
	}
		
}
