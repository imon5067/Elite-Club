package com.maestro.crb.orig_billing;

import com.maestro.crb.orig_billing.info.Inf;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class UserConnectDB
{
  private Connection con = null;
  private Statement stmt = null;
  private String msg = "";

  public Statement connect(boolean paramBoolean, String paramString1, String paramString2)
  {
    try
    {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      String str = "jdbc:oracle:thin:@" + Inf.getDbHost() + ":1521:" + Inf.getDatabase();
      System.out.println("DB User: " + paramString1);
      System.out.println("DB Pass:" + paramString2);
      this.con = DriverManager.getConnection(str, paramString1, paramString2);
      this.con.setAutoCommit(paramBoolean);
      this.stmt = this.con.createStatement();
      System.out.println("Connected :-)");
    }
    catch (Exception localException)
    {
      System.out.println("Connect failed :-(");
      System.out.println("CreateUserTable :: Exception in Connect in " + localException.getMessage());
      this.msg = ("<p><br><center><font color=red> ConnectDB :: Exception in Connect in " + localException.getMessage());
      localException.printStackTrace();
    }
    return this.stmt;
  }

  public void close()
  {
    try
    {
      this.stmt.close();
    }
    catch (Exception localException1)
    {
    }
    try
    {
      this.con.close();
      System.out.println("Closed :-)");
    }
    catch (Exception localException2)
    {
      localException2.printStackTrace();
    }
  }

  protected void finalize()
  {
    close();
  }

  public String getMessage()
  {
    return this.msg;
  }

  public static void main(String[] paramArrayOfString)
  {
    ConnectDB localConnectDB = new ConnectDB();
    localConnectDB.connect();
    localConnectDB.close();
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.crb.orig_billing.UserConnectDB
 * JD-Core Version:    0.6.0
 */