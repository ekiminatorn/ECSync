package com.ekstemicraft.plugin.ecsync;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import com.mysql.jdbc.PreparedStatement;

public class SQl  {
	
	public Map<String, String> playerUserIDs = new HashMap<String, String>();
	
	public void loadUserIDfromDB(String playerName) throws Exception{
	
	Class.forName("com.mysql.jdbc.Driver");
	
	Connection con = DriverManager.getConnection("jdbc:mysql://ipadress:3306/dbname", "user", "password");
	//Probably gonna change the credentials to config file, so they aren't exposed to the world ;)
	PreparedStatement statement = (PreparedStatement) con.prepareStatement("SELECT * FROM xf_user_field_value WHERE field_id = 'minecraft_username' AND field_value = '" + playerName + "'");
	ResultSet result = statement.executeQuery();
	String userID = null;
	
	if (result.next()){
		userID = result.getString("user_id");
	}
	if (userID == null){
		Bukkit.getLogger().info("[ECSync] " + playerName + " Not Linked");
		//log that user doesn't exists in the forum software
		
	}else{
		//log that user exists in the forum software
		playerUserIDs.put(playerName, userID);
		Bukkit.getLogger().info("[ECSync] " + playerName + " Linked with userID " + userID);
	}
	
	
 }
}