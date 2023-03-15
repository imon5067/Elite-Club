/*    */ package com.maestro.crb.orig_billing;
/*    */
/*    */ import com.maestro.crb.orig_billing.info.Inf;
/*    */ import java.io.PrintStream;
/*    */ import java.sql.Connection;
/*    */ import java.sql.DriverManager;
/*    */ import java.sql.Statement;
/*    */
/*    */ public class ConnectDB
/*    */ {
/* 10 */   private Connection con = null;
/* 11 */   private Statement stmt = null;
/* 12 */   private String msg = "";
/*    */
/*    */   public Statement connect(boolean paramBoolean)
/*    */   {
/*    */     try
/*    */     {
/* 18 */       Class.forName("oracle.jdbc.driver.OracleDriver");
/* 19 */       String str = "jdbc:oracle:thin:@" + Inf.getDbHost() + ":1521:" + Inf.getDatabase();
/* 20 */       System.out.println(str);
/* 21 */       this.con = DriverManager.getConnection(str, Inf.getDbUname(), Inf.getDbPass());
/* 22 */       this.con.setAutoCommit(paramBoolean);
/* 23 */       this.stmt = this.con.createStatement();
/* 24 */       System.out.println("Connected :-)");
/*    */     }
/*    */     catch (Exception localException)
/*    */     {
/* 30 */       System.out.println("Connect failed :-(");
/* 31 */       System.out.println("CreateUserTable :: Exception in Connect in " + localException.getMessage());
/* 32 */       this.msg = ("<p><br><center><font color=red> ConnectDB :: Exception in Connect in " + localException.getMessage());
/* 33 */       localException.printStackTrace();
/*    */     }
/*    */
/* 36 */     return this.stmt;
/*    */   }
/*    */
/*    */   public Statement connect()
/*    */   {
/* 41 */     return connect(true);
/*    */   }
/*    */
/*    */   public void close() {
/*    */     try {
/* 46 */       this.stmt.close(); } catch (Exception localException1) {
/*    */     }
/*    */     try {
/* 49 */       this.con.close();
/* 50 */       System.out.println("Closed :-)");
/*    */     }
/*    */     catch (Exception localException2) {
/* 53 */       localException2.printStackTrace();
/*    */     }
/*    */   }
/*    */
/*    */   protected void finalize()
/*    */   {
/* 60 */     close();
/*    */   }
/*    */
/*    */   public String getMessage() {
/* 64 */     return this.msg;
/*    */   }
/*    */
/*    */   public static void main(String[] paramArrayOfString) {
/* 68 */     ConnectDB localConnectDB = new ConnectDB();
/* 69 */     localConnectDB.connect();
/* 70 */     localConnectDB.close();
/*    */   }
/*    */ }

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.crb.orig_billing.ConnectDB
 * JD-Core Version:    0.6.0
 */