package cn.winde.listener;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.material.Dye;
import org.bukkit.plugin.Plugin;

import cn.winde.bd.Main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.inventory.ItemStack;
/**
 * Created by DELL on 2015/8/10.
 */
public class PlayerEvent implements Listener {
	
	  @EventHandler(priority=EventPriority.HIGH)
	  public void onPlayerDeath(PlayerDeathEvent e) {
		    if (e.getDrops().isEmpty()) return;
		    String name = e.getEntity().getName();
		    List items = new ArrayList();
		    if (Main.item.containsKey(name)) 
		    	items.addAll((Collection)Main.item.get(name));
		    List<ItemStack> ditems = new ArrayList();
		    ditems.addAll(e.getDrops());
		    for (ItemStack dropitem : ditems) {
		    	if(dropitem.getItemMeta().hasLore()){
		      if (dropitem.getItemMeta().getLore().contains("绑定于-" + e.getEntity().getName())) {
		        items.add(dropitem);
		        e.getDrops().remove(dropitem);
		      }
		    }
		    }
		    if (!items.isEmpty())
		    	Main.item.put(e.getEntity().getName(), items);
		    e.getEntity().sendMessage(""+Main.item.size());

    }
	  @EventHandler(priority=EventPriority.HIGH)
	  public void onPlayerDeath(PlayerRespawnEvent e) {
		    Player p = e.getPlayer();
		    if (Main.item.containsKey(p.getName())) {
		      List<ItemStack> items = (List)Main.item.get(p.getName());
		      ArrayList ritems = new ArrayList();
		      ArrayList aitems = new ArrayList();
		      for (ItemStack dropitem : items) {
		        ritems.add(dropitem);
		        HashMap imap = p.getInventory().addItem(new ItemStack[] { dropitem });
		        if (!imap.isEmpty()) {
		          Iterator it = imap.entrySet().iterator();
		        }
		      }
		      items.removeAll(ritems);
		      items.addAll(aitems);
		      if (items.isEmpty())
		          Main.item.remove(p.getName());
		        else
		       Main.item.put(p.getName(), items);
		 
    }
		    Main.item.remove(p.getName());
	  }
	  @EventHandler(priority=EventPriority.HIGH)
	  public void itemband(PlayerInteractEvent e) {
		    Player p = e.getPlayer();
		    
		    if(p.getItemInHand()!=null){
		    	ItemStack item =p.getItemInHand();
		    	if(item.getItemMeta().hasLore()){
		    		List<String> lore=item.getItemMeta().getLore();
		    		if(lore.contains(Main.config.getString("lorehead")));
		    	}
		    }
	  }
}

