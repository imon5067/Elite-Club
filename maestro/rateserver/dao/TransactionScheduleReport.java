package com.maestro.rateserver.dao;

import com.maestro.rateserver.util.StringUtil;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class TransactionScheduleReport
{
  Statement stmt = null;
  StringUtil strUtil = null;
  Hashtable dateRangeHt = null;
  Hashtable allUserHt = null;
  Hashtable allAccountHt = null;

  public TransactionScheduleReport(Statement paramStatement)
  {
    this.stmt = paramStatement;
    this.strUtil = new StringUtil();
  }

  public Hashtable getDateRange(String paramString1, String paramString2)
    throws Exception
  {
    String str1 = "";
    ResultSet localResultSet = null;
    Hashtable localHashtable = new Hashtable();
    String str2 = "";
    str1 = "SELECT to_char(TO_DATE('" + paramString1 + "','DD-MM-YYYY')+ROWNUM,'dd-mm-rrrr') as date_range FROM ( SELECT 1 nothing FROM DUAL CONNECT BY LEVEL <= 1000 ) where " + "TO_DATE('" + paramString1 + "','DD-MM-YYYY')+ROWNUM<=TO_DATE('" + paramString2 + "','DD-MM-YYYY')";
    System.out.println("sql::" + str1);
    localResultSet = this.stmt.executeQuery(str1);
    while (localResultSet.next())
    {
      str2 = localResultSet.getString("date_range");
      System.out.println("amount::" + str2);
      localHashtable.put(str2, str2);
    }
    localHashtable.put(paramString1, paramString1);
    localHashtable.put(paramString2, paramString2);
    this.dateRangeHt = localHashtable;
    return localHashtable;
  }

  public Hashtable getAllUserDateWise(String paramString1, String paramString2, int paramInt)
    throws Exception
  {
    String str1 = "";
    ResultSet localResultSet = null;
    Hashtable localHashtable = new Hashtable();
    ArrayList localArrayList = new ArrayList();
    String str2 = "";
    str1 = "select distinct user_id from transaction@OMS_LINK trans,transaction_detail@OMS_LINK trans_dtl where action_date  between to_date('" + paramString1 + "','rrrr-mm-dd') and to_date('" + paramString2 + "','rrrr-mm-dd') and trans_dtl.account_id=" + paramInt;
    System.out.println("sql::" + str1);
    localResultSet = this.stmt.executeQuery(str1);
    while (localResultSet.next())
    {
      str2 = localResultSet.getString("user_id");
      if ((str2 != null) && (str2.length() > 0) && (!str2.equals("null")))
        localHashtable.put(str2, str2);
      System.out.println("amount::" + localResultSet.getString("user_id"));
    }
    this.allUserHt = localHashtable;
    return localHashtable;
  }

  public Hashtable getAllAccount()
    throws Exception
  {
    String str1 = "";
    String str2 = "";
    Hashtable localHashtable = new Hashtable();
    String str3 = "select id,name from account_group@OMS_LINK start with id in (select id from account_group@OMS_LINK where parent_id=0) connect by prior id=parent_id ";
    ResultSet localResultSet = this.stmt.executeQuery(str3);
    while (localResultSet.next())
    {
      str1 = localResultSet.getString("id");
      str2 = localResultSet.getString("name");
      if ((str1 == null) || (str1.length() <= 0) || (str1.equals("null")))
        continue;
      localHashtable.put(str1, str2);
    }
    this.allAccountHt = localHashtable;
    return localHashtable;
  }

  public Hashtable getUserWiseData(int paramInt, Hashtable paramHashtable)
    throws Exception
  {
    Enumeration localEnumeration = paramHashtable.keys();
    Hashtable localHashtable = new Hashtable();
    String str1 = "";
    String str2 = "";
    while (localEnumeration.hasMoreElements())
      str1 = (String)paramHashtable.get(localEnumeration.nextElement());
    return null;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.dao.TransactionScheduleReport
 * JD-Core Version:    0.6.0
 */