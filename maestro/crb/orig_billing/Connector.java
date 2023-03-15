/*     */ package com.maestro.crb.orig_billing;
/*     */
/*     */ import com.maestro.crb.orig_billing.info.Inf;
/*     */ import java.io.PrintStream;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */
/*     */ public class Connector
/*     */ {
/*  17 */   private Connection connection = null;
/*     */
/*  19 */   private String host = Inf.getDbHost();
/*  20 */   private String port = "1521";
/*  21 */   private String database = Inf.getDatabase();
/*  22 */   private String user = Inf.getDbUname();
/*  23 */   private String password = Inf.getDbPass();
/*     */
/*  25 */   private PreparedStatement pstmt = null;
/*     */
/*  27 */   public static String CONNECTION_STRING = "jdbc:oracle:thin:@##HOST:##PORT:##DATABASE";
/*  28 */   public static String DRIVER_CLASS_NAME = "oracle.jdbc.driver.OracleDriver";
/*     */
/*     */   public Connector()
/*     */   {
/*     */   }
/*     */
/*     */   public Connector(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
/*     */   {
/*  39 */     this.host = paramString1;
/*  40 */     this.port = paramString2;
/*  41 */     this.database = paramString3;
/*  42 */     this.user = paramString4;
/*  43 */     this.password = paramString5;
/*     */   }
/*     */
/*     */   public void connect(boolean paramBoolean)
/*     */     throws MaestroException
/*     */   {
/*     */     try
/*     */     {
/*  52 */       Class.forName(DRIVER_CLASS_NAME);
/*  53 */       String str = CONNECTION_STRING.replaceAll("##HOST", this.host).replaceAll("##PORT", this.port).replaceAll("##DATABASE", this.database);
/*  54 */       System.out.println(str);
/*  55 */       this.connection = DriverManager.getConnection(str, this.user, this.password);
/*  56 */       this.connection.setAutoCommit(paramBoolean);
/*  57 */       System.out.println("Connected :-)");
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/*  59 */       throw new MaestroException("[CONNECT.CLASS_NOT_FOUND]: " + localClassNotFoundException.getMessage());
/*     */     } catch (SQLException localSQLException) {
/*  61 */       throw new MaestroException("[CONNECT.SQL]: " + localSQLException.getMessage());
/*     */     }
/*     */   }
/*     */
/*     */   public void close()
/*     */     throws MaestroException
/*     */   {
/*     */     try
/*     */     {
/*  70 */       System.out.println("Closed:)");
/*  71 */       this.connection.close();
/*     */     } catch (SQLException localSQLException) {
/*  73 */       throw new MaestroException("[DISCONNECT]: " + localSQLException.getMessage());
/*     */     }
/*     */   }
/*     */
/*     */   public String getHost()
/*     */   {
/*  80 */     return this.host;
/*     */   }
/*     */
/*     */   public void setHost(String paramString)
/*     */   {
/*  86 */     this.host = paramString;
/*     */   }
/*     */
/*     */   public String getPort()
/*     */   {
/*  92 */     return this.port;
/*     */   }
/*     */
/*     */   public void setPort(String paramString)
/*     */   {
/*  98 */     this.port = paramString;
/*     */   }
/*     */
/*     */   public String getDatabase()
/*     */   {
/* 104 */     return this.database;
/*     */   }
/*     */
/*     */   public void setDatabase(String paramString)
/*     */   {
/* 110 */     this.database = paramString;
/*     */   }
/*     */
/*     */   public String getUser()
/*     */   {
/* 116 */     return this.user;
/*     */   }
/*     */
/*     */   public void setUser(String paramString)
/*     */   {
/* 122 */     this.user = paramString;
/*     */   }
/*     */
/*     */   public Connection getConnection()
/*     */   {
/* 128 */     return this.connection;
/*     */   }
/*     */
/*     */   public void setPassword(String paramString)
/*     */   {
/* 134 */     this.password = paramString;
/*     */   }
/*     */
/*     */   public int executeUpdate(String paramString)
/*     */     throws MaestroException
/*     */   {
/*     */     try
/*     */     {
/* 144 */       closeCursor();
/* 145 */       this.pstmt = this.connection.prepareStatement(paramString);
/* 146 */       return this.pstmt.executeUpdate(); } catch (SQLException localSQLException) {
	throw new MaestroException("[SQL]: " + localSQLException.getMessage());
/*     */     }
/* 148 */
/*     */   }
/*     */
/*     */   public int executeUpdate(String paramString, String[] paramArrayOfString)
/*     */     throws MaestroException
/*     */   {
/*     */     try
/*     */     {
/* 157 */       closeCursor();
/* 158 */       this.pstmt = this.connection.prepareStatement(paramString);
/* 159 */       for (int i = 1; i <= paramArrayOfString.length; i++)
/* 160 */         this.pstmt.setString(i, paramArrayOfString[(i - 1)]);
/* 161 */       return this.pstmt.executeUpdate(); } catch (SQLException localSQLException) {
				throw new MaestroException("[SQL]: " + localSQLException.getMessage());
/*     */     }
/* 163 */
/*     */   }
/*     */
/*     */   public ResultSet executeQuery(String paramString, String[] paramArrayOfString)
/*     */     throws MaestroException
/*     */   {
/*     */     try
/*     */     {
/* 175 */       closeCursor();
/* 176 */       this.pstmt = this.connection.prepareStatement(paramString);
/* 177 */       for (int i = 1; i <= paramArrayOfString.length; i++)
/* 178 */         this.pstmt.setString(i, paramArrayOfString[(i - 1)]);
/* 179 */       return this.pstmt.executeQuery(); } catch (SQLException localSQLException) {
		throw new MaestroException("[SQL]: " + localSQLException.getMessage());
/*     */     }
/* 181 */
/*     */   }
/*     */
/*     */   public ResultSet executeQuery(String paramString)
/*     */     throws MaestroException
/*     */   {
/*     */     try
/*     */     {
/* 192 */       closeCursor();
/* 193 */       this.pstmt = this.connection.prepareStatement(paramString);
/* 194 */       return this.pstmt.executeQuery();
	      }
		catch (SQLException localSQLException) {
		throw new MaestroException("[SQL]: " + localSQLException.getMessage());
/*     */     }
/* 196 */
/*     */   }
/*     */
/*     */   public void commit()
/*     */     throws MaestroException
/*     */   {
/*     */     try
/*     */     {
/* 206 */       this.connection.commit();
/*     */     } catch (SQLException localSQLException) {
/* 208 */       throw new MaestroException("[SQL]: " + localSQLException.getMessage());
/*     */     }
/*     */   }
/*     */
/*     */   public void rollback()
/*     */     throws MaestroException
/*     */   {
/*     */     try
/*     */     {
/* 219 */       this.connection.rollback();
/*     */     } catch (SQLException localSQLException) {
/* 221 */       throw new MaestroException("[SQL]: " + localSQLException.getMessage());
/*     */     }
/*     */   }
/*     */
/*     */   public void closeCursor()
/*     */   {
/*     */     try
/*     */     {
/* 229 */       this.pstmt.close();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.crb.orig_billing.Connector
 * JD-Core Version:    0.6.0
 */