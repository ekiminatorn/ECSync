package com.ekstemicraft.plugin.ecsync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
 
/**
* You are welcome to use, redistribute and modify your own copies of this class.
* If you use this code, please provide credit to my original plugin
*
* Example usage:
* TwitchStream stream = new TwitchStream("mychannel");
* if(stream.isOnline){
*  Bukkit.broadcastMessage("Our stream is online!");
* }
*
* @author chasechocolate
*/
 
public class TwitchStream {
    private String channel;
 
    private URL url;
    private BufferedReader reader;
    private int stream;
    private int taskID;
 
    private boolean online = false;
 
    public TwitchStream(){
        this.channel = "uncle_emil";
        stream = 0;
 
        refresh();
    }
 
    public void refresh(){
        try {
            this.url = new URL("http://api.justin.tv/api/stream/list.json?jsonp=&channel=" + channel); //Bukkit automatically adds the URL tags, remove them when you copy the class
            this.reader = new BufferedReader(new InputStreamReader(url.openStream()));
 
            if(!(reader.readLine().equals("[]"))){
            	stream = 1;
                online = true;
               
            } else {
            	stream = 0;
                online = false;
                scheduler();
            }
            
          
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }      
        
    }
 
    public URL getUrl(){
        return this.url;
    }
 
    public boolean isOnline(){
        return this.online;
    }
    
    public void checking(){
    	refresh();

     	if (stream == 1){
        	
    		taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(ECSync.main, new Runnable(){

    			@Override
    			public void run() {
                   refresh();
                   if (stream == 1){
                	   Bukkit.broadcastMessage(ChatColor.AQUA + "[BNTwitch] Uncle_Emil is streaming, check out at: www.twitch.tv/uncle_emil");
                   }else{
                	   Bukkit.getScheduler().cancelTask(taskID);
                	   scheduler();
                   }
    				
    				
    			}		
    			
    		}, 72000L, 72000L);
        	}
    	
    	
    }
    
    public void scheduler(){

    	if (stream == 0){

    		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ECSync.main, new Runnable(){

    			@Override
    			public void run() {
    				refresh();
    				if(isOnline()){
    					checking();
    			    Bukkit.broadcastMessage(ChatColor.AQUA + "[BNTwitch] Uncle_Emil is streaming, check out at: www.twitch.tv/uncle_emil");

    				}
    			}		
    			
    		}, 6000L);
    			
    	}else{
    		checking();
     	   Bukkit.broadcastMessage(ChatColor.AQUA + "[BNTwitch] Uncle_Emil is streaming, check out at: www.twitch.tv/uncle_emil");
    	}
    	
    	
    	
    }
    
}