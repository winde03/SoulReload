/*    */ package cn.winde.bd.bdevent;
/*    */ 
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public final class BdEvent extends Event
/*    */ {
/*  9 */   private static final HandlerList handlers = new HandlerList();
/* 10 */   private boolean cancelled = false;
/*    */   private Player creator;
/*    */   private ItemStack kitName;
/*    */ 
/*    */   public BdEvent(Player player, ItemStack item)
/*    */   {
/* 15 */     this.creator = player;
/* 16 */     this.kitName = item;
/*    */   }
/*    */ 
/*    */   public boolean isCancelled() {
/* 20 */     return this.cancelled;
/*    */   }
/*    */ 
/*    */   public void setCancelled(boolean cancel) {
/* 24 */     this.cancelled = cancel;
/*    */   }
/*    */ 
/*    */   public ItemStack getName() {
/* 28 */     return this.kitName;
/*    */   }
/*    */ 
/*    */   public Player getCreator() {
/* 32 */     return this.creator;
/*    */   }
/*    */ 
/*    */   public HandlerList getHandlers()
/*    */   {
/* 37 */     return handlers;
/*    */   }
/*    */ 
/*    */   public static HandlerList getHandlerList() {
/* 41 */     return handlers;
/*    */   }
/*    */ }

