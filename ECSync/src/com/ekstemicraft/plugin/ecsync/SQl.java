package com.ekstemicraft.plugin.ecsync;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import com.mysql.jdbc.PreparedStatement;

public class SQl  {

	public static Map<String, String> playerUserIDs = new HashMap<String, String>();
	public static Map<String, String> webPlayerGroupIDs = new HashMap<String, String>();
    public ECSync pl;
	public void loadUserIDfromDB(String playerName) throws Exception{

	Class.forName("com.mysql.jdbc.Driver");
	pl = ECSync.main;
	
	Connection con = DriverManager.getConnection("jdbc:mysql://" + pl.getConfig().getString("database.ip") + ":3306/" + pl.getConfig().getString("database.database"), pl.getConfig().getString("database.user"), pl.getConfig().getString("database.password"));

	//Changed credential fetching from the config instead!
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
		con.close();
		webUserGroupID(playerName);
	}
/** Gets user ID from xenforo. Called when a player joins the server
 * 
 */
  }	
	
	public void webUserGroupID(String playerName) throws Exception{

		pl = ECSync.main;
		Class.forName("com.mysql.jdbc.Driver");

		Connection con = DriverManager.getConnection("jdbc:mysql://" + pl.getConfig().getString("database.ip") + ":3306/" + pl.getConfig().getString("database.database"), pl.getConfig().getString("database.user"), pl.getConfig().getString("database.password"));
		//Changed credentials fetching from config instead!
		PreparedStatement statement = (PreparedStatement) con.prepareStatement("SELECT user_group_id FROM xf_user WHERE user_id = '" + getUserID(playerName) + "'");
		ResultSet result = statement.executeQuery();
		String groupID = null;
		if (result.next()){
			groupID = result.getString("user_group_id");
		}
		if (groupID == null){
			con.close();
			//User doesnt exist in forumsoftware

		}else{
			//user exists in forumsoftware
          webPlayerGroupIDs.put(playerName, groupID);
          syncPlayer(playerName);
			con.close();
		}
		/** Gets user group ID from Xenforo. Called after loadUserIDfromDB() has done it's job, 
		 * if the user is registered on the forum.
		 * 
		 */
	}
	
    public void updateWebUserGroupID(String playerName, String groupID) throws Exception{
    	Class.forName("com.mysql.jdbc.Driver");
    	
    	Connection con = DriverManager.getConnection("jdbc:mysql://" + pl.getConfig().getString("database.ip") + ":3306/" + pl.getConfig().getString("database.database"), pl.getConfig().getString("database.user"), pl.getConfig().getString("database.password"));
		PreparedStatement statement = (PreparedStatement) con.prepareStatement("UPDATE xf_user SET user_group_id = " + groupID + " WHERE user_id = " + getUserID(playerName));
		statement.executeUpdate();

		Bukkit.getLogger().info("[ECSync] Player " + playerName + " forum group has been changed to " + groupID);

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
       String groupsStripped = StringUtils.join(Groups, ",");
       if (Groups.length == 0){ //Checks if user is guest, and doesnt sync
    	   return;
       }
       if (groupsStripped.contains("Staff")){ //Checks if user is Staff, and doesnt sync
    	   return;
       }
       if (groupsStripped.contains("Owner")){ //Checks if user is Staff, and doesnt sync
    	   return;
       }
       if (groupsStripped.contains("Buy")){ //Checks if user is donor, and syncs specially.
    	   if(!groupsStripped.contains(config.getGroupNamebyGroupID(webGroup))){
    		   String groupID = "14";
    		   try {
				updateWebUserGroupID(playerName, groupID);
			} catch (Exception e) {
				Bukkit.getLogger().severe("Error at playerSync() Buy part: " + e.getMessage());
			}
    	   }
    	   return;
       }
		if (!Groups[0].equalsIgnoreCase(config.getGroupNamebyGroupID(webGroup))){
			//Checks if playerpermissiongroup has been changed, if true, starts syncing.
			String groupID = config.getWebappGroupIDbyGroupName(Groups[0]);
			try {
				updateWebUserGroupID(playerName, groupID);
			} catch (Exception e) {
				Bukkit.getLogger().severe("Error at syncPlayer(): " + e.getMessage());
			} 
			return;
		}
		
		return;
	/** 
	 * This is where syncing happens. 
	 * 
	 */		
	}
	
	public int isVerified(String playerName) throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		pl = ECSync.main;
		
		String verified = null;
		String userState = null;

		Connection con = DriverManager.getConnection("jdbc:mysql://" + pl.getConfig().getString("database.ip") + ":3306/" + pl.getConfig().getString("database.database"), pl.getConfig().getString("database.user"), pl.getConfig().getString("database.password"));
		//Changed credentials to be fetched from the config instead!
		PreparedStatement statement = (PreparedStatement) con.prepareStatement("SELECT is_verified FROM xf_user WHERE user_id = '" + getUserID(playerName) + "'");
		ResultSet resultVerified = statement.executeQuery();
		PreparedStatement statement2 = (PreparedStatement) con.prepareStatement("SELECT user_state FROM xf_user WHERE user_id = '" + getUserID(playerName) + "'");
		ResultSet resultUserState = statement2.executeQuery();
		
		if (resultVerified.next()){
			verified = resultVerified.getString("is_verified");
		}
		if (resultUserState.next()){
			userState = resultUserState.getString("user_state");
		}
		if(userState.equalsIgnoreCase("valid")){ //If email is confirmed
			
			if(verified.equalsIgnoreCase("0")){ //If user is not verified yet.
			
			PreparedStatement statement3 = (PreparedStatement) con.prepareStatement("UPDATE xf_user SET is_verified = 1 WHERE user_id = " + getUserID(playerName));
			statement3.executeUpdate();
			return 1; //Success!
			}
			else{
			return 2; //Already verified!
		 }	
		}
		return 3; //Account not email confirmed!
		
		/**
		 * Method for checking if user is email confirmed and verify state is 0 (is not verified.)
		 * If the user is not verified, and the email is confirmed, changes verify state to 1 (is verified)
		 * And returns three states:
		 * 1: Success, changes player permission group to newbie (Got build rights.).
		 * 2: User is already verified on the site.
		 * 3: Account email is not confirmed.
		 */
	}
	
}