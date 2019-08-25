package cn.winde.bd.command;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.winde.bd.SQL.SQLPlayers;

import cn.winde.bd.Main;
import cn.winde.bd.api.Money;
import cn.winde.bd.listener.PlayerEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by DELL on 2015/8/18.
 */
public class Bdcmd implements CommandExecutor {
	public ArrayList<String> all = new ArrayList();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s2, String[] s) {

    	if(s.length==0){
    		commandSender.sendMessage("/bd create 将手中的物品生成为使用后绑定物品");
    		commandSender.sendMessage("/bd list 列出本人所拥有的所有绑定物品");
    		commandSender.sendMessage("/bd find 序号 找回我丢失的物品（老物品失效并被销毁）");
    		return true;
    	}
    	Player p = (Player)commandSender;
    	if(s.length==1){
    		if(s[0].equals("list")){
    			Map map = SQLPlayers.getitemlist(p.getName());
    			Iterator iter = map.entrySet().iterator();
    			int i= 1;
    			while (iter.hasNext()) {
    			Map.Entry entry = (Map.Entry) iter.next();
    			String key = (String) entry.getKey();
    			ItemStack val = (ItemStack) entry.getValue();
    			p.sendMessage(i+":"+val.getItemMeta().getDisplayName()+"  物品ID:"+key);
    			i=i+1;
    			}
    			p.sendMessage(Main.config.getString("msg.msg4"));
    			return true;
    		}
    		

    	      
    	}
    	
    	if(s.length==2){
    		if(s[0].equals("find")){
    			ItemStack item= SQLPlayers.getitemlist(p.getName()).get(s[1]);
    			if(item!=null){
    				if(SQLPlayers.getitemiswho(s[1]).equals(p.getName())){
    				if(Money.haveMoney(p, Main.config.getInt("money"))){
    					List<String>lore=item.getItemMeta().getLore();
        				lore.remove(s[1]);
        				lore.remove(Main.config.getString("blore"));
        				lore.remove(p.getName());
    					lore.add(Main.config.getString("blore"));
    					lore.add(p.getName());
    					 UUID uuid = UUID.randomUUID();
    				    lore.add(uuid.toString());
    				    try {
    				    	p.getInventory().addItem(findchuli(p,item,lore,uuid.toString()));
    					} catch (SQLException e) {
    						// TODO 自动生成的 catch 块
    						e.printStackTrace();
    					}
        				SQLPlayers.setitemtype(s[1], "1");
        				Main.blacklist.add(s[1]);
        				Money.takeMoney(p,Main.config.getInt("money"));
        				p.sendMessage(Main.config.getString("msg.msg7"));
        				return true;
    				}
    				else{
    					p.sendMessage(Main.config.getString("msg.msg8"));
    					return true;
    				}
    				}
    				
    			}
    			else{
    				p.sendMessage(Main.config.getString("msg.msg5"));
    				return true;
    			}
    		}

    	}
        if(s.length==3){

        }
		return true;
        
    }
	public static ItemStack findchuli(Player p,ItemStack it, List lore,String uuid) throws SQLException {
		ItemStack i = it;
		ItemMeta id = i.getItemMeta();
		id.setLore(lore);
		i.setItemMeta(id);
		PlayerEvent.create(p, i, uuid);
		return i;
	}

}
