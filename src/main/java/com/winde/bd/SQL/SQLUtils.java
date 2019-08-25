 package com.winde.bd.SQL;
 
 import java.sql.Connection;
 import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
 import java.sql.SQLException;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.Properties;
 import java.util.logging.Logger;

import cn.winde.bd.Main;

 
 public abstract class SQLUtils
 {
   protected static Connection connection = null;
   private static Map<String, PreparedStatement> statementCache = new HashMap();
   private static boolean useStatementCache = true;
 
   public static boolean connect() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
     if (connection != null) {
       return true;
     }
     Driver driver = (Driver)Class.forName("com.mysql.jdbc.Driver").newInstance();
     Properties properties = new Properties();
     connection = DriverManager.getConnection("jdbc:mysql://"+ Main.config.getString("mySQLHost")+"/"+Main.config.getString("mySQLDatabase"), Main.config.getString("mySQLUsername"), Main.config.getString("mySQLPassword"));;
     SQLPlayers.createTable();
     if (connection == null) {
       throw new NullPointerException("Connecting to database failed!");
     }
     return true;
   }
 
   public static void dispose() {
     statementCache.clear();
     try {
       if (connection != null)
         connection.close();
     }
     catch (SQLException e) {
     }
     connection = null;
   }
 
   public static Connection getConnection() {
     if (connection == null) {
       throw new NullPointerException("No connection!");
     }
     return connection;
   }
 
   public static PreparedStatement prepare(String sql) throws SQLException {
     return prepare(sql, false);
   }
 
   public static PreparedStatement prepare(String sql, boolean returnGeneratedKeys) throws SQLException {
     if (connection == null) {
       throw new SQLException("No connection");
     }
     if ((useStatementCache) && (statementCache.containsKey(sql))) {
       return (PreparedStatement)statementCache.get(sql);
     }
     PreparedStatement preparedStatement = returnGeneratedKeys ? connection.prepareStatement(sql, 1) : connection.prepareStatement(sql);
     statementCache.put(sql, preparedStatement);
     return preparedStatement;
   }
 
   public static boolean useStatementCache() {
     return useStatementCache;
   }
 
   public static void setUseStatementCache(boolean useStatementCache) {
     useStatementCache = useStatementCache;
   }
 }

