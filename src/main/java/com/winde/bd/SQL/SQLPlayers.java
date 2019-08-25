package com.winde.bd.SQL;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;

public class SQLPlayers extends SQLUtils {
	private static Object lock = new Object();

	public static boolean tableExist(String playerUUID) {
		try {
			Statement statement = getConnection().createStatement();
			DatabaseMetaData md = statement.getConnection().getMetaData();
			ResultSet rs = md.getTables(null, null, playerUUID, null);
			if (rs.next())
				return true;
		} catch (SQLException localSQLException) {
		}
		return false;
	}

	public static void createTable() {
		synchronized (lock) {
			try {

				PreparedStatement statement = prepare(
						"CREATE TABLE itembd (id INT(100) AUTO_INCREMENT,Player VARCHAR(200) NOT NULL,itemid VARCHAR(255) NOT NULL DEFAULT '0',item BLOB,type VARCHAR(255) NOT NULL DEFAULT '0',PRIMARY KEY (id)) CHARACTER SET utf8 COLLATE utf8_general_ci");
				statement.executeUpdate();
				Logger.getGlobal().info("Creating player tables");
			} catch (SQLException e) {
				Logger.getGlobal().info("C2222");
			}
		}
	}

	public static String playerExist(String playerUUID) {
		try {

			PreparedStatement statement = prepare("SELECT * FROM vip WHERE Player = " + playerUUID);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString("now"));
				return rs.getString("now");
			}
		} catch (SQLException e) {

		}
		return null;
	}
	   public static void deletelist(String id) {
		     try {
		       PreparedStatement statement = prepare("DELETE FROM `itembd` WHERE `itembd`.`itemid` = '"+id+"'");
		       statement.executeUpdate();
		     } catch (SQLException e) {
		     }
		   }
	public static void additem(String playerUUID, byte[] item, String itemid) {
		synchronized (lock) {
			try {
				PreparedStatement statement = prepare("INSERT into itembd (Player, itemid, item) VALUES (?, ?, ?)");
				statement.setString(1, playerUUID);
				statement.setString(2, itemid);
				statement.setBytes(3, item);
				statement.executeUpdate();
			} catch (SQLException e) {
			}
		}
	}
	   public static byte[] getitem(String id) {
		     try {
		       PreparedStatement statement = prepare("SELECT * FROM `itembd` WHERE itemid = '" + id + "' ");
		       ResultSet rs = statement.executeQuery();
		       while (rs.next())
		           return rs.getBytes("item");
		     }
		     catch (SQLException e)
		     {
		     }
		     return null;
		   }
	   public static String getitemiswho(String id) {
		     try {
		       PreparedStatement statement = prepare("SELECT * FROM `itembd` WHERE itemid = '" + id + "' ");
		       ResultSet rs = statement.executeQuery();
		       while (rs.next())
		           return rs.getString("player");
		     }
		     catch (SQLException e)
		     {
		     }
		     return "";
		   }
	   public static HashMap<String, ItemStack> getitemlist(String player) {
		   HashMap<String, ItemStack>itemlist=new HashMap<>();
		     try {
		       PreparedStatement statement = prepare("SELECT * FROM `itembd` WHERE player = '" + player + "' AND type='0'");
		       ResultSet rs = statement.executeQuery();
		       while (rs.next()){
		    	   itemlist.put(rs.getString("itemid"),getInventory(rs.getBytes("item")));
		       }
		       return itemlist;
		     }
		     catch (SQLException e)
		     {
		     }
		     return null;
		   }
	   public static List<String> getblacklist() {
		   List<String>itemlist = new ArrayList<String>();
		     try {
		       PreparedStatement statement = prepare("SELECT * FROM `itembd` WHERE `type` = '1'");
		       ResultSet rs = statement.executeQuery();
		       while (rs.next()){
		    	   String ss=rs.getString("itemid");
		    	   itemlist.add(ss);
		       }
		       return itemlist;
		     }
		     catch (SQLException e)
		     {
		     }
		     return itemlist;
		   }
	public static void setitemtype(String itemid, String type) {
		synchronized (lock) {
			try {
				PreparedStatement statement = prepare("UPDATE itembd SET type = ? WHERE itemid = ?");
				statement.setString(1, type);
				statement.setString(2, itemid);
				statement.executeUpdate();
			} catch (SQLException e) {

			}
		}
	}

	public static String getvip(String playerUUID) {
		try {
			PreparedStatement statement = prepare("SELECT * FROM `vip` WHERE Player = '" + playerUUID + "' ");
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				return rs.getString("now");
			}
		} catch (SQLException e) {

		}
		return null;
	}

	public static Long gettime(String playerUUID, String vip) {
		try {
			PreparedStatement statement = prepare("SELECT * FROM `vip` WHERE Player = '" + playerUUID + "' ");
			ResultSet rs = statement.executeQuery();
			while (rs.next())
				return Long.valueOf(rs.getString(vip));
		} catch (SQLException e) {

		}
		return 0L;
	}
	   public static ItemStack getInventory(byte[] byteInv) {
		 
		     ByteArrayInputStream ByteArIS = new ByteArrayInputStream(byteInv);
		     
		     Object inv = null;
		     try {
		       BukkitObjectInputStream invObjIS = new BukkitObjectInputStream(ByteArIS);
		       inv = invObjIS.readObject();
		       invObjIS.close();
		     } catch (IOException ioexception) {
		       ioexception.printStackTrace();
		     } catch (ClassNotFoundException classNotFoundException) {
		       classNotFoundException.printStackTrace();
		     }
		     return ((ItemStack)inv);
		   }

}

/*
 * Location: C:\Users\Administrator\Desktop\梦技术\任务\EasyKits-v2.0.5.jar Qualified
 * Name: info.TrenTech.EasyKits.SQL.SQLPlayers JD-Core Version: 0.6.1
 */