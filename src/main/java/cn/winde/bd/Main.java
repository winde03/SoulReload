package cn.winde.bd;


import java.util.HashMap;
import java.util.List;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.winde.bd.SQL.SQLPlayers;
import com.winde.bd.SQL.SQLUtils;

import cn.winde.bd.command.Bdcmd;
import cn.winde.bd.listener.PlayerEvent;


public class Main extends JavaPlugin implements Listener {
	public static HashMap<String, List<ItemStack>> item;
	public static Plugin p;
	public static PlayerPoints playerPoints;
	public static FileConfiguration config;
	public static List<String> blacklist;
    @Override
    public void onEnable() {
    	item = new HashMap();
    	p=this;
    	saveDefaultConfig();
    	config = getConfig();
        getLogger().info("灵魂绑定");
        getLogger().info("灵魂绑定QQ：619405934");
        getServer().getPluginManager().registerEvents(new PlayerEvent(), this);
        getCommand("bd").setExecutor(new Bdcmd());
        hookPlayerPoints();
        try
        {
          SQLUtils.connect();
        } catch (Exception e) {
          return;
        }
        blacklist= SQLPlayers.getblacklist();
        System.out.println(blacklist);
    }
    @Override
    public void onDisable() {
    	getLogger().info("灵魂绑定修正");
    }
    private boolean hookPlayerPoints() {
	    final Plugin plugin = this.getServer().getPluginManager().getPlugin("PlayerPoints");
	    playerPoints = PlayerPoints.class.cast(plugin);
	    return playerPoints != null; 
	}
   public static PlayerPoints getPlayerPoints() {
	    return playerPoints;
	}

}
