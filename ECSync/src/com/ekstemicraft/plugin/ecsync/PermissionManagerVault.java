package com.ekstemicraft.plugin.ecsync;


import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import net.milkbowl.vault.permission.Permission;

public class PermissionManagerVault {

	private static Permission vault;
	ECSync pl;
	
	public PermissionManagerVault(){
		RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
		vault = rsp.getProvider();
				
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
	
	
}
