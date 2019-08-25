package cn.winde.bd.listener;

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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.material.Dye;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.io.BukkitObjectOutputStream;

import com.winde.bd.SQL.SQLPlayers;

import cn.winde.bd.Main;
import cn.winde.bd.bdevent.BdEvent;
import cn.winde.bd.bdevent.BddelEvent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by DELL on 2015/8/10.
 */
public class PlayerEvent implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (e.getDrops().isEmpty())
			return;
		String name = e.getEntity().getName();
		List items = new ArrayList();
		if (Main.item.containsKey(name))
			items.addAll((Collection) Main.item.get(name));
		List<ItemStack> ditems = new ArrayList();
		ditems.addAll(e.getDrops());
		for (ItemStack dropitem : ditems) {
			if (dropitem.getItemMeta().hasLore()) {
				if (dropitem.getItemMeta().getLore().toString().contains(Main.config.getString("blore"))) {
					items.add(dropitem);
					e.getDrops().remove(dropitem);
				}
			}
		}
		if (!items.isEmpty())
			Main.item.put(e.getEntity().getName(), items);

	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDeath(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if (Main.item.containsKey(p.getName())) {
			List<ItemStack> items = (List) Main.item.get(p.getName());
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

	@EventHandler(priority = EventPriority.HIGH)
	public void itemband(PlayerInteractEvent e) throws SQLException {
		Player p = e.getPlayer();
		if (p.getItemInHand() != null) {
			ItemStack item = p.getItemInHand();
			if (item.hasItemMeta()) {
				if(item.getItemMeta().hasLore()){
				List<String> lore = item.getItemMeta().getLore();
				if (lore.contains(Main.config.getString("lorehead"))) {
					lore.remove(Main.config.getString("lorehead"));
					lore.add(Main.config.getString("blore"));
					lore.add(p.getName());
					 UUID uuid = UUID.randomUUID();
				    lore.add(uuid.toString());
					p.setItemInHand(chuli(p,item, lore,uuid.toString()));
				}
				if (lore.toString().contains(Main.config.getString("blore"))) {
					for(String s:Main.blacklist){
						if(lore.toString().contains(s)){
							e.setCancelled(true);
							p.setItemInHand(new ItemStack(Material.AIR));
							p.sendMessage(Main.config.getString("msg.msg6"));
							BddelEvent event = new BddelEvent(p, item);
							Bukkit.getServer().getPluginManager().callEvent(event);
							e.setCancelled(true);
						}
					}
				}
				if (lore.contains(Main.config.getString("normal.lorehead"))) {
					lore.remove(Main.config.getString("normal.lorehead"));
					lore.add(Main.config.getString("normal.blore"));
					lore.add(p.getName());
					p.setItemInHand(normalchuli(p,item, lore));
				}
				}

			}
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void itemband(PlayerPickupItemEvent e){
		Player p = e.getPlayer();
		
		if (e.getItem().getItemStack() != null) {
			ItemStack item = e.getItem().getItemStack();
			if (item.hasItemMeta()) {
				if(item.getItemMeta().hasLore()){
				List<String> lore = item.getItemMeta().getLore();
				if (lore.toString().contains(Main.config.getString("blore"))) {
					if(!lore.contains(p.getName())){
						e.setCancelled(true);
					}

				}
				}

			}
		}
		}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void itemband(PlayerDropItemEvent e)  {
		Player p = e.getPlayer();
		
		if (e.getItemDrop().getItemStack() != null) {
			ItemStack item = e.getItemDrop().getItemStack();
			if (item.hasItemMeta()) {
				if(item.getItemMeta().hasLore()){
				List<String> lore = item.getItemMeta().getLore();
				if (lore.toString().contains(Main.config.getString("blore"))) {
						e.setCancelled(true);
						p.sendMessage(Main.config.getString("msg.msg2"));
				}
//				普通绑定物品丢弃
//				if (lore.toString().contains(Main.config.getString("normal.blore"))) {
//					
//					p.sendMessage(Main.config.getString("msg.msg2"));
//					e.setCancelled(true);
//
//			}
				}
			}
		}
		}
	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		ItemStack heldItem = player.getInventory().getItem(event.getNewSlot());
		if ((heldItem != null) && (heldItem.getItemMeta().hasLore())) {
			if (heldItem.getItemMeta().getLore().toString().contains(Main.config.getString("blore"))) {
				List<String>lore=heldItem.getItemMeta().getLore();
				for(String s:Main.blacklist){
					if(lore.toString().contains(s)){
						event.setCancelled(true);
						player.setItemInHand(new ItemStack(Material.AIR));
						player.sendMessage(Main.config.getString("msg.msg6"));
						BddelEvent event2 = new BddelEvent(player, heldItem);
						Bukkit.getServer().getPluginManager().callEvent(event2);
						event.setCancelled(true);
					}
				}
			}
		}
	}
	@EventHandler
	public void onPlayerItemHeld(PlayerJoinEvent event) {
		Main.blacklist=SQLPlayers.getblacklist();

	}
	@EventHandler
	public void InventoryOpen(InventoryOpenEvent event) {
		for (ItemStack im : event.getInventory().getContents()) {
				if ((im != null) && (im.getItemMeta().hasLore())) {
					if (im.getItemMeta().getLore().toString().contains(Main.config.getString("blore"))) {
						List<String> lore = im.getItemMeta().getLore();
						for (String s : Main.blacklist) {
						if (lore.toString().contains(s)) {
							event.setCancelled(true);
							event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
							event.getPlayer().sendMessage(Main.config.getString("msg.msg6"));
							BddelEvent event2 = new BddelEvent((Player)event.getPlayer(), im);
							Bukkit.getServer().getPluginManager().callEvent(event2);
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void itemband(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();
		if (e.getCursor() != null) {
			ItemStack item = e.getCursor();
			if (item.hasItemMeta()) {
				if(item.getItemMeta().hasLore()){
				List<String> lore = item.getItemMeta().getLore();
				if (lore.toString().contains(Main.config.getString("blore"))) {
					if(!lore.contains(p.getName())){
				          e.setCancelled(true);
				          e.setCurrentItem(new ItemStack(Material.AIR, 1));
						p.sendMessage(Main.config.getString("msg.msg3"));
					}
					for(String s:Main.blacklist){
						if(lore.toString().contains(s)){
							e.setCancelled(true);
							p.setItemInHand(new ItemStack(Material.AIR));
							p.sendMessage(Main.config.getString("msg.msg6"));
							BddelEvent event2 = new BddelEvent(p, item);
							Bukkit.getServer().getPluginManager().callEvent(event2);
							e.setCancelled(true);
						}
					}
				}
				}
			}
		}
	}
//	@EventHandler(priority = EventPriority.HIGH)
//	public void itemband(InventoryMoveItemEvent e){
//		Player p = (Player) e.getDestination().getViewers();
//		
//		if (e.getItem() != null) {
//			ItemStack item = e.getItem();
//			if (item.hasItemMeta()) {
//				if(item.getItemMeta().hasLore()){
//				List<String> lore = item.getItemMeta().getLore();
//				if (lore.toString().contains(Main.config.getString("blore"))) {
//					if(!lore.contains(p.getName())){
//						e.setCancelled(true);
//						p.sendMessage(Main.config.getString("msg.msg3"));
//					}
//
//				}
//				if (lore.toString().contains(Main.config.getString("normal.blore"))) {
//					if(!lore.contains(p.getName())){
//						e.setCancelled(true);
//						p.sendMessage(Main.config.getString("msg.msg3"));
//					}
//
//				}
//				}
//
//			}
//		}
//	}

	public static ItemStack chuli(Player p,ItemStack it, List lore,String uuid) throws SQLException {
		ItemStack i = it;
		ItemMeta id = i.getItemMeta();
		id.setLore(lore);
		i.setItemMeta(id);
		create(p, i, uuid);
		return i;
	}
	public static ItemStack normalchuli(Player p,ItemStack it, List lore) throws SQLException {
		ItemStack i = it;
		ItemMeta id = i.getItemMeta();
		id.setLore(lore);
		i.setItemMeta(id);
		return i;
	}

	public static void create(Player creator, ItemStack inventory,String uuid) throws SQLException {
		BdEvent event = new BdEvent(creator, inventory);
		Bukkit.getServer().getPluginManager().callEvent(event);

		if (!event.isCancelled()) {
			ByteArrayOutputStream invByteArray = new ByteArrayOutputStream();
			try {
				BukkitObjectOutputStream invObjOS = new BukkitObjectOutputStream(invByteArray);
				invObjOS.writeObject(inventory);
				invObjOS.close();
			} catch (IOException ioexception) {
				ioexception.printStackTrace();
			}
			SQLPlayers.additem(creator.getName(), invByteArray.toByteArray(), uuid.toString());
		}
	}
}
