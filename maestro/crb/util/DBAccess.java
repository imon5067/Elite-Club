package com.maestro.crb.util;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import javax.servlet.jsp.JspWriter;

public class DBAccess
{
  public Statement stmt;

  public DBAccess(Statement paramStatement)
  {
    this.stmt = paramStatement;
  }

  public Hashtable getInBilling(int paramInt)
  {
    Hashtable localHashtable = new Hashtable();
    try
    {
      ResultSet localResultSet = this.stmt.executeQuery("select * from in_BILLING where user_id='" + paramInt + "' and main_dest=1 ");
      while (localResultSet.next())
      {
        String str1 = localResultSet.getString("code");
        String str2 = localResultSet.getString("country");
        double d = localResultSet.getDouble("rate");
        int i = localResultSet.getInt("gress");
        int j = localResultSet.getInt("less");
        int k = localResultSet.getInt("bs_1");
        int m = localResultSet.getInt("bs_2");
        int n = localResultSet.getInt("max_talktime");
        if (n == 0)
          n = 28800;
        CodeRate localCodeRate = new CodeRate(str1, str2, d, k, m, i, j, n);
        localHashtable.put(str1, localCodeRate);
      }
    }
    catch (Exception localException)
    {
      System.out.println("Exception in DBAccess , getInBilling :: " + localException.getMessage());
    }
    return localHashtable;
  }

  public CodeRate getInBilling(int paramInt, String paramString)
  {
    CodeRate localCodeRate = null;
    int i = 0;
    try
    {
      ResultSet localResultSet = this.stmt.executeQuery("select * from in_BILLING where user_id='" + paramInt + "' and code='" + paramString + "' ");
      String str;
      double d;
      int j;
      int k;
      int m;
      int n;
      int i1;
      while (localResultSet.next())
      {
        str = localResultSet.getString("country");
        d = localResultSet.getDouble("rate");
        j = localResultSet.getInt("gress");
        k = localResultSet.getInt("less");
        m = localResultSet.getInt("bs_1");
        n = localResultSet.getInt("bs_2");
        i1 = localResultSet.getInt("max_talktime");
        if (i1 == 0)
          i1 = 28800;
        localCodeRate = new CodeRate(paramString, str, d, m, n, j, k, i1);
        i = 1;
      }
      if (i == 0)
      {
        localResultSet = this.stmt.executeQuery("select * from default_in_BILLING where code='" + paramString + "' ");
        while (localResultSet.next())
        {
          str = localResultSet.getString("country");
          d = localResultSet.getDouble("rate");
          j = localResultSet.getInt("gress");
          k = localResultSet.getInt("less");
          m = localResultSet.getInt("bs_1");
          n = localResultSet.getInt("bs_2");
          i1 = localResultSet.getInt("max_talktime");
          if (i1 == 0)
            i1 = 28800;
          localCodeRate = new CodeRate(paramString, str, d, m, n, j, k, i1);
        }
      }
    }
    catch (Exception localException)
    {
      System.out.println("Exception in DBAccess , getInBilling :: " + localException.getMessage());
    }
    return localCodeRate;
  }

  public CodeRate getOutBilling(int paramInt, String paramString)
  {
    CodeRate localCodeRate = null;
    int i = 0;
    try
    {
      ResultSet localResultSet = this.stmt.executeQuery("select * from out_BILLING where user_id='" + paramInt + "' and code='" + paramString + "' ");
      String str;
      double d;
      int j;
      int k;
      int m;
      int n;
      int i1;
      while (localResultSet.next())
      {
        str = localResultSet.getString("country");
        d = localResultSet.getDouble("rate");
        j = localResultSet.getInt("gress");
        k = localResultSet.getInt("less");
        m = localResultSet.getInt("bs_1");
        n = localResultSet.getInt("bs_2");
        i1 = localResultSet.getInt("max_talktime");
        if (i1 == 0)
          i1 = 28800;
        localCodeRate = new CodeRate(paramString, str, d, m, n, j, k, i1);
        i = 1;
      }
      if (i == 0)
      {
        localResultSet = this.stmt.executeQuery("select * from default_out_BILLING where code='" + paramString + "' ");
        while (localResultSet.next())
        {
          str = localResultSet.getString("country");
          d = localResultSet.getDouble("rate");
          j = localResultSet.getInt("gress");
          k = localResultSet.getInt("less");
          m = localResultSet.getInt("bs_1");
          n = localResultSet.getInt("bs_2");
          i1 = localResultSet.getInt("max_talktime");
          if (i1 == 0)
            i1 = 28800;
          localCodeRate = new CodeRate(paramString, str, d, m, n, j, k, i1);
        }
      }
    }
    catch (Exception localException)
    {
      System.out.println("Exception in DBAccess , getOutBilling :: " + localException.getMessage());
    }
    return localCodeRate;
  }

  public String getCode(int paramInt1, int paramInt2, String paramString, JspWriter paramJspWriter)
  {
    String str1 = null;
    int i = 0;
    ResultSet localResultSet;
    if (paramInt1 == 1)
      try
      {
        String str2 = "select max(length(code)) as aa, code from out_BILLING where user_id='" + paramInt2 + "' and instr('" + paramString + "',code)=1 group by code order by aa desc";
        System.out.println("@@@ : " + str2);
        localResultSet = this.stmt.executeQuery(str2);
        if (localResultSet.next())
        {
          str1 = localResultSet.getString("code");
          System.out.println("@@ : " + str1);
          i = 1;
        }
        if (i == 0)
        {
          str2 = "select max(length(code)) as aa, code from default_out_BILLING where instr('" + paramString + "',code)=1 group by code order by aa desc";
          System.out.println("@@@ : " + str2);
          localResultSet = this.stmt.executeQuery(str2);
          if (localResultSet.next())
            str1 = localResultSet.getString("code");
          System.out.println("@@ default : " + str1);
        }
      }
      catch (Exception localException1)
      {
        System.out.println("Exception in DBAccess , getCode out :: " + localException1.getMessage());
      }
    else
      try
      {
        String str3 = "select max(length(code)) as aa, code from in_BILLING where user_id='" + paramInt2 + "' and instr('" + paramString + "',code)=1 group by code order by aa desc";
        System.out.println("@@@ : " + str3);
        localResultSet = this.stmt.executeQuery(str3);
        if (localResultSet.next())
        {
          str1 = localResultSet.getString("code");
          System.out.println("@@ : " + str1);
          i = 1;
        }
        if (i == 0)
        {
          str3 = "select max(length(code)) as aa, code from default_in_BILLING where instr('" + paramString + "',code)=1 group by code order by aa desc";
          System.out.println("@@@ : " + str3);
          localResultSet = this.stmt.executeQuery(str3);
          if (localResultSet.next())
            str1 = localResultSet.getString("code");
          System.out.println("@@ default : " + str1);
        }
      }
      catch (Exception localException2)
      {
        System.out.println("Exception in DBAccess , getCode in :: " + localException2.getMessage());
      }
    return str1;
  }

  public Hashtable getOutBilling(int paramInt)
  {
    Hashtable localHashtable = new Hashtable();
    try
    {
      ResultSet localResultSet = this.stmt.executeQuery("select * from out_BILLING where user_id='" + paramInt + "' and main_dest=1 ");
      while (localResultSet.next())
      {
        String str1 = localResultSet.getString("code");
        String str2 = localResultSet.getString("country");
        double d = localResultSet.getDouble("rate");
        int i = localResultSet.getInt("gress");
        int j = localResultSet.getInt("less");
        int k = localResultSet.getInt("bs_1");
        int m = localResultSet.getInt("bs_2");
        int n = localResultSet.getInt("max_talktime");
        if (n == 0)
          n = 28800;
        CodeRate localCodeRate = new CodeRate(str1, str2, d, k, m, i, j, n);
        localHashtable.put(str1, localCodeRate);
      }
    }
    catch (Exception localException)
    {
      System.out.println("Exception in DBAccess , getInBilling :: " + localException.getMessage());
    }
    return localHashtable;
  }

  public String[] getUserId(int paramInt, String paramString1, String paramString2, String paramString3, JspWriter paramJspWriter)
  {
    String[] arrayOfString = null;
    ResultSet localResultSet;
    String str3;
    String str4;
    if (paramInt == 1)
      try
      {
        String str1 = "select max(length(out_prefix)) as aa, user_id,out_prefix from user_IP where ip='" + paramString2 + "' and instr('" + paramString3 + "',out_prefix)=1 and active_out=1 group by user_id, out_prefix order by aa desc";
        paramJspWriter.println("<br> " + str1);
        localResultSet = this.stmt.executeQuery(str1);
        if (localResultSet.next())
        {
          str3 = localResultSet.getString("user_id");
          str4 = localResultSet.getString("out_prefix");
          arrayOfString = new String[2];
          arrayOfString[0] = str3;
          arrayOfString[1] = str4;
        }
        else
        {
          str1 = "select user_id from user_IP where ip='" + paramString2 + "' and out_prefix is null and active_out=1 ";
          paramJspWriter.println("<br> " + str1);
          localResultSet = this.stmt.executeQuery(str1);
          if (localResultSet.next())
          {
            str3 = localResultSet.getString("user_id");
            str4 = "";
            arrayOfString = new String[2];
            arrayOfString[0] = str3;
            arrayOfString[1] = str4;
          }
        }
      }
      catch (Exception localException1)
      {
        System.out.println("Exception in DBAccess , getUserId out :: " + localException1.getMessage());
        localException1.printStackTrace();
      }
    else
      try
      {
        String str2 = "select max(length(in_prefix)) as aa, user_id,in_prefix from user_IP where ip='" + paramString1 + "' and instr('" + paramString3 + "',in_prefix)=1 and active_in=1 group by user_id, in_prefix order by aa desc";
        paramJspWriter.println("<br> " + str2);
        localResultSet = this.stmt.executeQuery(str2);
        if (localResultSet.next())
        {
          str3 = localResultSet.getString("user_id");
          str4 = localResultSet.getString("in_prefix");
          arrayOfString = new String[2];
          arrayOfString[0] = str3;
          arrayOfString[1] = str4;
        }
        else
        {
          str2 = "select user_id from user_IP where ip='" + paramString1 + "' and in_prefix is null and active_in=1 ";
          paramJspWriter.println("<br> " + str2);
          localResultSet = this.stmt.executeQuery(str2);
          if (localResultSet.next())
          {
            str3 = localResultSet.getString("user_id");
            str4 = "";
            arrayOfString = new String[2];
            arrayOfString[0] = str3;
            arrayOfString[1] = str4;
          }
        }
      }
      catch (Exception localException2)
      {
        System.out.println("Exception in DBAccess , getUserId in :: " + localException2.getMessage());
        localException2.printStackTrace();
      }
    return arrayOfString;
  }

  public User getUser(int paramInt)
  {
    return new User(paramInt, this.stmt);
  }

  public void close()
  {
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.crb.util.DBAccess
 * JD-Core Version:    0.6.0
 */