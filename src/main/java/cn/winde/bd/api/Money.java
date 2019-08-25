 package cn.winde.bd.api;
 
 import org.bukkit.entity.Player;

import cn.winde.bd.Main;
 
 public class Money
 {
   public static boolean haveMoney(Player p, double money)
   {
     return money <= Main.playerPoints.getAPI().look(p.getName());
   }
 
   public static void takeMoney(Player p, double money) {
	   Main.playerPoints.getAPI().take(p.getName(), (int) money);
   }
 
   public static void giveMoney(Player p, double money) {
	   Main.playerPoints.getAPI().give(p.getName(), (int) money);
   }
 }

