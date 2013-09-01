package com.ekstemicraft.plugin.ecsync;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import com.mysql.jdbc.PreparedStatement;

public class SQl  {

	public Map<String, String> playerUserIDs = new HashMap<String, String>();
	public Map<String, String> webPlayerGroupIDs = new HashMap<String, String>();

	public void loadUserIDfromDB(String playerName) throws Exception{

	Class.forName("com.mysql.jdbc.Driver");

	Connection con = DriverManager.getConnection("jdbc:mysql://ipadress:3306/db", "user", "pass");
	//Probably gonna change the credentials to config file, so they aren't exposed to the world ;)
	PreparedStatement statement = (PreparedStatement) con.prepareStatement("SELECT * FROM xf_user_field_value WHERE field_id = 'minecraft_username' AND field_value = '" + playerName + "'");
	ResultSet result = statement.executeQuery();
	String userID = null;

	if (result.next()){
		userID = result.getString("user_id");
	}
	if (userID == null){
		Bukkit.getLogger().info("[ECSync] " + playerName + " Not Linked");
		con.close();
		//log that user doesn't exists in the forum software

	}else{
		//log that user exists in the forum software
		playerUserIDs.put(playerName, userID);
		Bukkit.getLogger().info("[ECSync] " + playerName + " Linked with userID " + userID);
		PermissionManagerVault perms = new PermissionManagerVault();
		perms.getGroups(playerName);
		con.close();
		webUserGroupID(playerName);
	}
/** Gets user ID from xenforo. Called when a player joins the server
 * 
 */
  }
	
	
	public void webUserGroupID(String playerName) throws Exception{
		
		Class.forName("com.mysql.jdbc.Driver");

		Connection con = DriverManager.getConnection("jdbc:mysql://ip:3306/db", "user", "pass");
		//Probably gonna change the credentials to config file, so they aren't exposed to the world ;)
		PreparedStatement statement = (PreparedStatement) con.prepareStatement("SELECT user_group_id FROM xf_user WHERE user_id = '" + getUserID(playerName) + "'");
		ResultSet result = statement.executeQuery();
		String groupID = null;
		if (result.next()){
			groupID = result.getString("user_group_id");
		}
		if (groupID == null){
			con.close();
			//log that user doesn't exists in the forum software

		}else{
			//log that user exists in the forum software
          Bukkit.broadcastMessage(groupID);
          webPlayerGroupIDs.put(playerName, groupID);
          syncPlayer(playerName);
			con.close();
		}
		/** Gets user group ID from Xenforo. Called after loadUserIDfromDB() has done it's job, 
		 * if the user is registered on the forum.
		 * 
		 */
	}
	

	
	public String getUserID(String playerName){
		if (!playerUserIDs.containsKey(playerName)){
			try {
				loadUserIDfromDB(playerName);
			} catch (Exception e) {
				Bukkit.getLogger().severe("Error at getUserID(): " + e.getMessage());
			}
		}
		return playerUserIDs.get(playerName);
		
		/** Retrieves UserID from Hashmap, or if not there, from the Database.
		 * 
		 */
	}
	
	public String getWebUserGroup(String playerName){
		if (!webPlayerGroupIDs.containsKey(playerName)){
			return null;
		}
		return webPlayerGroupIDs.get(playerName);
		
		/** Retrieves UserGroupID from Xenforo forum. If not found, returns null
		 * 
		 */
	}
	
	public void syncPlayer(String playerName){
		PermissionManagerVault perms = new PermissionManagerVault();
		Config config = new Config(null);
		String[] Groups = perms.getGroups(playerName);
		String webGroup = getWebUserGroup(playerName);
		String oldGroup = Arrays.toString(Groups);
		Bukkit.broadcastMessage(oldGroup);
		if (oldGroup.equalsIgnoreCase(config.getGroupNamebyGroupID(webGroup))){
			
			Bukkit.broadcastMessage("whut!");
		}
	}
	/** This is where syncing happens. Not fully working yet! Some MySQL statements needed
	 * 
	 */
	
}