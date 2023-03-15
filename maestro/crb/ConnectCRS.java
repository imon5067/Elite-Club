/*    */ package com.maestro.crb;
/*    */ 
/*    */ import com.mysql.jdbc.Driver;
/*    */ import java.io.PrintStream;
/*    */ import java.sql.Connection;
/*    */ import java.sql.Statement;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class ConnectCRS
/*    */ {
/* 10 */   private Connection con = null;
/* 11 */   private Statement stmt = null;
/* 12 */   private String msg = "";
/*    */ 
/*    */   public Statement connect()
/*    */   {
/*    */     try
/*    */     {
/* 18 */       Class.forName("com.mysql.jdbc.Driver");
/* 19 */       Driver localDriver = new Driver();
/*    */ 
/* 21 */       Properties localProperties = new Properties();
/* 22 */       localProperties.put("user", "crs");
/* 23 */       localProperties.put("password", "goldleaf");
/*    */ 
/* 25 */       this.con = localDriver.connect("jdbc:mysql://216.214.161.55/crs", localProperties);
/* 26 */       this.stmt = this.con.createStatement();
/* 27 */       System.out.println("Connected :-)");
/*    */     }
/*    */     catch (Exception localException)
/*    */     {
/* 32 */       System.out.println("Connect failed :-(");
/* 33 */       System.out.println("CreateUserTable :: Exception in Connect in " + localException.getMessage());
/* 34 */       this.msg = ("<p><br><center><font color=red> ConnectCRS :: Exception in Connect in " + localException.getMessage());
/* 35 */       localException.printStackTrace();
/*    */     }
/*    */ 
/* 38 */     return this.stmt;
/*    */   }
/*    */ 
/*    */   public void close() {
/*    */     try {
/* 43 */       this.stmt.close();
/* 44 */       this.con.close();
/* 45 */       System.out.println("Closed :-)");
/*    */     }
/*    */     catch (Exception localException) {
/* 48 */       localException.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   protected void finalize() {
/* 53 */     close();
/*    */   }
/*    */ 
/*    */   public String getMessage() {
/* 57 */     return this.msg;
/*    */   }
/*    */   public static void main(String[] paramArrayOfString) {
/* 60 */     ConnectCRS localConnectCRS = new ConnectCRS();
/* 61 */     localConnectCRS.connect();
/* 62 */     localConnectCRS.close();
/*    */   }
/*    */ }

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.crb.ConnectCRS
 * JD-Core Version:    0.6.0
 */