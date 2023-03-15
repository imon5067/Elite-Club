package com.maestro.rateserver.dao;

import com.maestro.crb.orig_billing.info.Inf;
import com.maestro.rateserver.model.Item;
import com.maestro.rateserver.model.UserRate;
import com.maestro.rateserver.util.StringUtil;
import java.io.PrintStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import oracle.jdbc.OracleCallableStatement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

public class UserRateDAO
  implements Serializable
{
  Statement stmt = null;
  StringUtil strUtil = null;

  public UserRateDAO(Statement paramStatement)
  {
    this.stmt = paramStatement;
    this.strUtil = new StringUtil();
  }

  public String readexcell()
    throws Exception
  {
    Connection localConnection = null;
    Statement localStatement = null;
    String str1 = "failure";
    try
    {
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      localConnection = DriverManager.getConnection("jdbc:odbc:qa-list", "", "");
      localStatement = localConnection.createStatement();
      String str2 = "select * from [Visiting_Card$]";
      ResultSet localResultSet = localStatement.executeQuery(str2);
      System.out.println("Found the following URLs for March 2000:");
      while (localResultSet.next())
        System.out.println(localResultSet.getString(1));
    }
    catch (Exception localException1)
    {
      System.err.println(localException1);
    }
    finally
    {
      try
      {
        localStatement.close();
        localConnection.close();
        str1 = "success";
      }
      catch (Exception localException2)
      {
        System.err.println(localException2);
      }
    }
    return str1;
  }

  public Hashtable getUserRate(Statement paramStatement, String paramString1, String paramString2)
    throws Exception
  {
    Hashtable localHashtable = new Hashtable();
    String str1 = "SELECT DEST_CODE FORM DESTINATION_CATEGORY WHERE COUNTRY='" + paramString1 + "' AND DEST_CAT=' " + paramString2 + "'";
    ResultSet localResultSet = paramStatement.executeQuery(str1);
    String str2 = "";
    while (localResultSet.next())
    {
      str2 = localResultSet.getString("DEST_CODE");
      localHashtable.put(str2, getRate());
    }
    return localHashtable;
  }

  public Hashtable getUserRate(String paramString)
    throws Exception
  {
    ResultSet localResultSet = null;
    String str1 = "";
    str1 = "select id,code as dest_code from country where upper(name)=upper('" + paramString + "') ";
    localResultSet = this.stmt.executeQuery(str1);
    String str2 = "";
    String str3 = "";
    if (localResultSet.next())
    {
      str3 = localResultSet.getString("id");
      str2 = localResultSet.getString("dest_code");
    }
    ArrayList localArrayList = new ArrayList();
    str1 = "select distinct uip.USER_ID from USER_IP uip,USERS u where uip.ACTIVE_OUT=1 and  u.ACTIVE='1' and u.ID=uip.user_id  and u.id not in(select distinct user_id from IGNORE_CARRIER where COUNTRY_ID=" + str3 + " or COUNTRY_ID is null) order by uip.user_id";
    localResultSet = this.stmt.executeQuery(str1);
    while (localResultSet.next())
      localArrayList.add(localResultSet.getString("USER_ID"));
    str1 = "select distinct b.user_id user_id,b.rate rate,b.code code,c.dest_cat dest_cat from out_billing b,destination_category c,user_ip uip,USERS u where u.ACTIVE='1' and u.ID=uip.user_id and c.country=upper('" + paramString + "') and substr(c.dest_code , 1, 1) between '0' and '9' and b.code=c.dest_code  and uip.user_id=b.user_id and uip.active_out=1 and u.id not in(select distinct user_id from IGNORE_CARRIER where COUNTRY_ID=" + str3 + "  or COUNTRY_ID is null) order by c.dest_cat,b.code ,b.user_id";
    localResultSet = this.stmt.executeQuery(str1);
    Hashtable localHashtable1 = new Hashtable();
    Hashtable localHashtable3 = new Hashtable();
    UserRate localUserRate = null;
    Object localObject = new ArrayList();
    String str4 = "";
    int i = 0;
    String str5 = "";
    double d1 = 0.0D;
    String str6 = "";
    String str7 = "";
    Hashtable localHashtable2;
    int j;
    String str8;
    String str9;
    int k;
    double d2;
    int m;
    while (localResultSet.next())
    {
      d1 = localResultSet.getDouble("rate");
      i = localResultSet.getInt("user_id");
      str4 = localResultSet.getString("code");
      localUserRate = new UserRate(localResultSet.getInt("user_id"), d1);
      localHashtable3.put(i + ":" + str4, "" + d1);
      if ((str5.length() > 0) && (!str5.equals(str4)) && (localObject != null) && (((List)localObject).size() > 0))
      {
        localHashtable2 = (Hashtable)localHashtable1.get(str7);
        localObject = (List)localHashtable2.get(str5);
        j = str5.length();
        str8 = str5;
        str9 = "";
        for (k = 0; (localArrayList != null) && (k < localArrayList.size()); k++)
        {
          str9 = (String)localArrayList.get(k);
          d2 = 0.0D;
          if (localHashtable3.containsKey(str9 + ":" + str5))
            continue;
          for (m = j - 1; (m > 0) && (m >= str2.length()); m--)
          {
            str8 = str5.substring(0, m);
            if (!localHashtable3.containsKey(str9 + ":" + str8))
              continue;
            try
            {
              d2 = Double.parseDouble((String)localHashtable3.get(str9 + ":" + str8));
              localUserRate = new UserRate(Integer.parseInt(str9), d2);
              ((List)localObject).add(localUserRate);
            }
            catch (Exception localException1)
            {
            }
            localHashtable3.put(str9 + ":" + str5, "" + d2);
            break;
          }
        }
        localHashtable2.remove(str5);
        localHashtable2.put(str5, localObject);
      }
      localUserRate = new UserRate(localResultSet.getInt("user_id"), d1);
      if (localHashtable1.containsKey(localResultSet.getString("dest_cat")))
      {
        localHashtable2 = (Hashtable)localHashtable1.get(localResultSet.getString("dest_cat"));
        if (localHashtable2.containsKey(str4))
        {
          localObject = (List)localHashtable2.get(str4);
          ((List)localObject).add(localUserRate);
          localHashtable2.remove(str4);
          localHashtable2.put(str4, localObject);
        }
        else
        {
          localObject = new ArrayList();
          ((List)localObject).add(localUserRate);
          localHashtable2.put(str4, localObject);
        }
      }
      else
      {
        localObject = new ArrayList();
        ((List)localObject).add(localUserRate);
        localHashtable2 = new Hashtable();
        localHashtable2.put(str4, localObject);
        localHashtable1.put(localResultSet.getString("dest_cat"), localHashtable2);
        str7 = localResultSet.getString("dest_cat");
      }
      str5 = str4;
    }
    if ((str5.length() > 0) && (localObject != null) && (((List)localObject).size() > 0))
    {
      localHashtable2 = (Hashtable)localHashtable1.get(str7);
      localObject = (List)localHashtable2.get(str5);
      j = str5.length();
      str8 = str5;
      str9 = "";
      for (k = 0; k < localArrayList.size(); k++)
      {
        str9 = (String)localArrayList.get(k);
        d2 = 0.0D;
        if (localHashtable3.containsKey(str9 + ":" + str5))
          continue;
        for (m = j - 1; (m > 0) && (m >= str2.length()); m--)
        {
          str8 = str5.substring(0, m);
          if (!localHashtable3.containsKey(str9 + ":" + str8))
            continue;
          try
          {
            d2 = Double.parseDouble((String)localHashtable3.get(str9 + ":" + str8));
            localUserRate = new UserRate(Integer.parseInt(str9), d2);
            ((List)localObject).add(localUserRate);
          }
          catch (Exception localException2)
          {
          }
          localHashtable3.put(str9 + ":" + str5, "" + d2);
          break;
        }
      }
      localHashtable2.remove(str5);
      localHashtable2.put(str5, localObject);
    }
    return (Hashtable)localHashtable1;
  }

  public Hashtable getusername()
    throws Exception
  {
    String str = "select distinct u.id id,u.uname uname from users u,user_ip up where up.user_id=u.id and up.active_out=1";
    ResultSet localResultSet = this.stmt.executeQuery(str);
    Hashtable localHashtable = new Hashtable();
    while (localResultSet.next())
      localHashtable.put("" + localResultSet.getInt("id"), localResultSet.getString("uname"));
    return localHashtable;
  }

  public List getRate()
  {
    UserRate localUserRate = new UserRate();
    return localUserRate.dummyUserRate();
  }

  public ArrayList getUpdateList(List paramList, String paramString1, String paramString2, ArrayList paramArrayList)
    throws Exception
  {
    String str1 = "";
    Connection localConnection = this.stmt.getConnection();
    String str2 = "";
    String str3 = "SELECT RATECHART_ID FROM PACKAGE_CONFIG WHERE PACK_ID=" + paramString1;
    ResultSet localResultSet = this.stmt.executeQuery(str3);
    while (localResultSet.next())
      str2 = localResultSet.getString("RATECHART_ID");
    for (int i = 0; i < paramList.size(); i++)
    {
      str1 = (String)paramList.get(i);
      str3 = "SELECT RATE_ID,DEST_CODE,COUNTRY,RATE_PEAK,RATE_OFF_PEAK,RATE_DUMMY,RATE_DUMMY_OFFPEAK,LAST_CHANGE,IS_FLAT,OFF_PEAK_START,OFF_PEAK_END,ISUNLIMITED,ISOPEN,IS_PROGRESSIVE_BILLING,CB_FIRST_LEG_IP_RATE,CONNECTION_FEE,UPDATE_DATE FROM RATECHART_DTL WHERE ((SELECT TO_CHAR(MAX(PKG_RATE_SEND_DATE),'DD-MM-RRRR HH24:MI:SS')FROM RATE_SEND_INFO WHERE PACK_ID=" + paramString1 + " AND (COUNTRY='" + str1 + "' or country='*%'))" + "<" + "(SELECT TO_CHAR(MAX(UPDATE_DATE),'DD-MM-RRRR HH24:MI:SS') FROM RATECHART_DTL WHERE DEST_CODE IN " + "(select distinct dest_code from destination_category where (country ='" + str1 + "' or country='*%')))) or " + "(SELECT TO_CHAR(MAX(PKG_RATE_SEND_DATE),'DD-MM-RRRR HH24:MI:SS')FROM RATE_SEND_INFO WHERE PACK_ID=" + paramString1 + " AND (COUNTRY='" + str1 + "' or country='*%')) is null AND RATE_ID=" + str2;
      System.out.println("dtl qry::" + str3);
      System.out.println("ratechart_dtl size::" + paramArrayList.size());
      localResultSet = this.stmt.executeQuery(str3);
      while (localResultSet.next())
        paramArrayList.add(localResultSet.getString("COUNTRY") + "," + localResultSet.getString("DEST_CODE") + "," + localResultSet.getString("RATE_PEAK") + "," + localResultSet.getString("RATE_OFF_PEAK") + "," + localResultSet.getString("RATE_DUMMY") + "," + localResultSet.getString("RATE_DUMMY_OFFPEAK"));
    }
    return paramArrayList;
  }

  public ArrayList getMailRateChartWithDate(String paramString1, String paramString2, String paramString3, String paramString4, ArrayList paramArrayList, String paramString5, String paramString6)
    throws Exception
  {
    String str1 = "";
    String str2 = "";
    String str3 = "";
    String str13 = "";
    str3 = " select RATECHART_ID from PACKAGE_CONFIG where pack_id=" + paramString1;
    ResultSet localResultSet = this.stmt.executeQuery(str3);
    String str4 = null;
    if (localResultSet.next())
      str4 = localResultSet.getString(1);
    str3 = " SELECT distinct DEST_CODE,COUNTRY,  RATE_PEAK deliverymin,  RATE_OFF_PEAK delv_off_peak,   RATE_DUMMY declaremin,   RATE_DUMMY_OFFPEAK declare_off_peak,  TO_CHAR(EFFECT_DATE,'DD-MM-RRRR') EFFECT_DATE  from RATECHART_DTL where dest_code in(   select dest_code from RATECHART_DTL where rate_id=" + str4 + "  group by dest_code " + " having count(dest_code)>1 ) and  rate_id=" + str4 + " and " + paramString4 + " order by COUNTRY asc, EFFECT_DATE desc ";
    localResultSet = this.stmt.executeQuery(str3);
    Hashtable localHashtable1 = new Hashtable();
    Object localObject1 = null;
    Object localObject2 = null;
    String str5 = null;
    String str6 = null;
    String str7 = null;
    String str8 = null;
    String str9 = null;
    String str10 = null;
    while (localResultSet.next())
    {
      str5 = localResultSet.getString("DEST_CODE").trim();
      str6 = localResultSet.getString("EFFECT_DATE");
      str7 = localResultSet.getString("deliverymin");
      str8 = localResultSet.getString("delv_off_peak");
      str9 = localResultSet.getString("declaremin");
      str10 = localResultSet.getString("declare_off_peak");
      if ((localObject1 != null) && (str5.equals(localObject1)))
      {
        if (localHashtable1.containsKey(localObject1 + "<sp>" + (String)localObject2))
          localHashtable1.put(localObject1 + "<sp>" + (String)localObject2, str7 + "," + str9 + "," + str8 + "," + str10);
        localHashtable1.put(str5 + "<sp>" + str6, "0");
        localObject1 = str5;
        localObject2 = str6;
        continue;
      }
      localObject1 = str5;
      localObject2 = str6;
      localHashtable1.put(localObject1 + "<sp>" + (String)localObject2, "0");
    }
    Hashtable localHashtable2 = new Hashtable();
    str3 = " select * from (SELECT distinct DEST_CODE,  RATE_PEAK deliverymin,  RATE_OFF_PEAK delv_off_peak,   RATE_DUMMY declaremin,   RATE_DUMMY_OFFPEAK declare_off_peak,  TO_CHAR(EFFECT_DATE,'DD-MM-RRRR') EFFECT_DATE,row_number() OVER (Partition by dest_code order by EFFECT_DATE desc) efdateorder  from RATECHART_DTL where dest_code in(   select dest_code from RATECHART_DTL where rate_id=" + str4 + "  group by dest_code " + " having count(dest_code)>1 ) and  rate_id=" + str4 + " and to_date(EFFECT_DATE,'DD-MM-RRRR HH24:MI:SS')  < to_date('" + paramString6 + "  00:00:00','DD-MM-RRRR HH24:MI:SS') " + " )temptable where temptable.efdateorder=1 ";
    localResultSet = this.stmt.executeQuery(str3);
    while (localResultSet.next())
      localHashtable2.put(localResultSet.getString("DEST_CODE"), localResultSet.getDouble("deliverymin") + "," + localResultSet.getDouble("declaremin") + "," + localResultSet.getDouble("delv_off_peak") + "," + localResultSet.getDouble("declare_off_peak"));
    String str11 = "";
    String str12 = "";
    str3 = " SELECT distinct RATE_ID,DEST_CODE,COUNTRY,  RATE_PEAK deliverymin,  RATE_OFF_PEAK delv_off_peak,   RATE_DUMMY declaremin,   RATE_DUMMY_OFFPEAK declare_off_peak,  TO_CHAR(EFFECT_DATE,'DD-MM-RRRR') EFFECT_DATE   FROM RATECHART_DTL r  WHERE rate_id= " + str4 + "   and   " + paramString4 + "   and DEST_CODE  " + " in (select DEST_CODE from DESTINATION_CATEGORY) order by country asc, EFFECT_DATE desc ";
    localResultSet = this.stmt.executeQuery(str3);
    while (localResultSet.next())
    {
      if ((localResultSet.getString("COUNTRY") != null) && (localResultSet.getString("COUNTRY").length() > 0) && (!localResultSet.getString("COUNTRY").equals("null")))
      {
        str1 = str1 + localResultSet.getString("COUNTRY") + ",";
        str2 = str2 + localResultSet.getString("COUNTRY") + ",";
      }
      else
      {
        str1 = str1 + ",";
        str2 = str2 + ",";
      }
      if ((localResultSet.getString("DEST_CODE") != null) && (localResultSet.getString("DEST_CODE").length() > 0) && (!localResultSet.getString("DEST_CODE").equals("null")))
      {
        str1 = str1 + localResultSet.getString("DEST_CODE") + ",";
        str2 = str2 + localResultSet.getString("DEST_CODE") + ",";
      }
      else
      {
        str1 = str1 + ",";
        str2 = str2 + ",";
      }
      if ((localResultSet.getString("deliverymin") != null) && (localResultSet.getString("deliverymin").length() > 0) && (!localResultSet.getString("deliverymin").equals("null")))
      {
        str1 = str1 + (!localResultSet.getString("deliverymin").equals("0") ? "" + Double.parseDouble(paramString5) / Double.parseDouble(localResultSet.getString("deliverymin")) : "0") + ",";
        str2 = str2 + localResultSet.getString("deliverymin") + ",";
        str11 = localResultSet.getString("deliverymin");
      }
      else
      {
        str1 = str1 + ",";
        str2 = str2 + ",";
        str11 = "0";
      }
      if ((localResultSet.getString("declaremin") != null) && (localResultSet.getString("declaremin").length() > 0) && (!localResultSet.getString("declaremin").equals("null")))
        str1 = str1 + (!localResultSet.getString("declaremin").equals("0") ? "" + Double.parseDouble(paramString5) / Double.parseDouble(localResultSet.getString("declaremin")) : "0") + ",";
      else
        str1 = str1 + ",";
      if ((localResultSet.getString("delv_off_peak") != null) && (localResultSet.getString("delv_off_peak").length() > 0) && (!localResultSet.getString("delv_off_peak").equals("null")))
        str1 = str1 + (!localResultSet.getString("delv_off_peak").equals("0") ? "" + Double.parseDouble(paramString5) / Double.parseDouble(localResultSet.getString("delv_off_peak")) : "0") + ",";
      else
        str1 = str1 + ",";
      if ((localResultSet.getString("declare_off_peak") != null) && (localResultSet.getString("declare_off_peak").length() > 0) && (!localResultSet.getString("declare_off_peak").equals("null")))
        str1 = str1 + (!localResultSet.getString("declare_off_peak").equals("0") ? "" + Double.parseDouble(paramString5) / Double.parseDouble(localResultSet.getString("declare_off_peak")) : "0") + ",";
      else
        str1 = str1 + ",";
      if ((localResultSet.getString("EFFECT_DATE") != null) && (localResultSet.getString("EFFECT_DATE").length() > 0) && (!localResultSet.getString("EFFECT_DATE").equals("null")))
      {
        str1 = str1 + localResultSet.getString("EFFECT_DATE") + ",";
        str2 = str2 + localResultSet.getString("EFFECT_DATE") + ",";
      }
      else
      {
        str2 = str2 + "N/A,";
        str1 = str1 + "N/A,";
      }
      if (localHashtable1.containsKey(localResultSet.getString("DEST_CODE") + "<sp>" + localResultSet.getString("EFFECT_DATE")))
      {
        str13 = (String)localHashtable1.get(localResultSet.getString("DEST_CODE") + "<sp>" + localResultSet.getString("EFFECT_DATE"));
        if (str13.equals("0"))
        {
          if (localHashtable2.containsKey(localResultSet.getString("DEST_CODE")))
          {
            String str14 = (String)localHashtable2.get(localResultSet.getString("DEST_CODE"));
            str1 = str1 + (!str14.split(",")[0].equals("0") ? "" + Double.parseDouble(paramString5) / Double.parseDouble(str14.split(",")[0]) : "0") + ",";
            str1 = str1 + (!str14.split(",")[1].equals("0") ? "" + Double.parseDouble(paramString5) / Double.parseDouble(str14.split(",")[1]) : "0") + ",";
            str1 = str1 + (!str14.split(",")[2].equals("0") ? "" + Double.parseDouble(paramString5) / Double.parseDouble(str14.split(",")[2]) : "0") + ",";
            str1 = str1 + (!str14.split(",")[3].equals("0") ? "" + Double.parseDouble(paramString5) / Double.parseDouble(str14.split(",")[3]) : "0") + ",";
            str12 = str14.split(",")[0];
          }
          else
          {
            str1 = str1 + "N/A,N/A,N/A,N/A";
            str12 = "0";
          }
        }
        else if (str13.length() > 1)
        {
          str1 = str1 + (!str13.split(",")[0].equals("0") ? "" + Double.parseDouble(paramString5) / Double.parseDouble(str13.split(",")[0]) : "0") + ",";
          str1 = str1 + (!str13.split(",")[1].equals("0") ? "" + Double.parseDouble(paramString5) / Double.parseDouble(str13.split(",")[1]) : "0") + ",";
          str1 = str1 + (!str13.split(",")[2].equals("0") ? "" + Double.parseDouble(paramString5) / Double.parseDouble(str13.split(",")[2]) : "0") + ",";
          str1 = str1 + (!str13.split(",")[3].equals("0") ? "" + Double.parseDouble(paramString5) / Double.parseDouble(str13.split(",")[3]) : "0") + ",";
          str12 = str13.split(",")[0];
        }
      }
      else if (localHashtable2.containsKey(localResultSet.getString("DEST_CODE")))
      {
        str13 = (String)localHashtable2.get(localResultSet.getString("DEST_CODE"));
        str1 = str1 + (!str13.split(",")[0].equals("0") ? "" + Double.parseDouble(paramString5) / Double.parseDouble(str13.split(",")[0]) : "0") + ",";
        str1 = str1 + (!str13.split(",")[1].equals("0") ? "" + Double.parseDouble(paramString5) / Double.parseDouble(str13.split(",")[1]) : "0") + ",";
        str1 = str1 + (!str13.split(",")[2].equals("0") ? "" + Double.parseDouble(paramString5) / Double.parseDouble(str13.split(",")[2]) : "0") + ",";
        str1 = str1 + (!str13.split(",")[3].equals("0") ? "" + Double.parseDouble(paramString5) / Double.parseDouble(str13.split(",")[3]) : "0") + ",";
        str12 = str13.split(",")[0];
      }
      else
      {
        str1 = str1 + "N/A,N/A,N/A,N/A";
        str12 = "0";
      }

      if (Double.parseDouble(str12) == 0.0D)
        str13 = "New Rate";
      else if (Double.parseDouble(str11) > Double.parseDouble(str12))
        str13 = "Increased";
      else if (Double.parseDouble(str11) < Double.parseDouble(str12))
        str13 = "Decreased";
      else if (Double.parseDouble(str11) == Double.parseDouble(str12))
        str13 = "No Changed";
      str2 = str2 + str13;
      if (!paramString3.equals("-1"))
        paramArrayList.add(str2);
      if (!paramString2.equals("-1"))
        paramArrayList.add(str1);
      str1 = "";
      str2 = "";
    }
    return (ArrayList)paramArrayList;
  }

  public ArrayList Not_using_getMailRateChartWithDate(String paramString1, String paramString2, String paramString3, String paramString4, ArrayList paramArrayList, String paramString5)
    throws Exception
  {
    return paramArrayList;
  }

  public ArrayList getMailRateChart(String paramString1, String paramString2, ArrayList paramArrayList)
    throws Exception
  {
    String str1 = "";
    String str2 = "";
    Connection localConnection = this.stmt.getConnection();
    String str3 = "";
    String str4 = "SELECT RATECHART_ID FROM PACKAGE_CONFIG WHERE PACK_ID=" + paramString1;
    ResultSet localResultSet = this.stmt.executeQuery(str4);
    while (localResultSet.next())
      str3 = localResultSet.getString("RATECHART_ID");
    str4 = "SELECT RATE_ID,DEST_CODE,COUNTRY,RATE_PEAK,RATE_OFF_PEAK,RATE_DUMMY,RATE_DUMMY_OFFPEAK,LAST_CHANGE,IS_FLAT,OFF_PEAK_START,OFF_PEAK_END,ISUNLIMITED,ISOPEN,IS_PROGRESSIVE_BILLING,CB_FIRST_LEG_IP_RATE,CONNECTION_FEE,UPDATE_DATE,TO_CHAR(EFFECT_DATE,'DD-MM-RRRR HH24:MI:SS') EFFECT_DATE,TO_CHAR(END_DATE,'DD-MM-RRRR HH24:MI:SS') END_DATE FROM RATECHART_DTL WHERE ((SELECT TO_CHAR(MAX(PKG_RATE_SEND_DATE),'DD-MM-RRRR HH24:MI:SS')FROM RATE_SEND_INFO WHERE PACK_ID=" + paramString1 + " AND (COUNTRY='" + str1 + "' or country='*%'))" + "<" + "(SELECT TO_CHAR(MAX(UPDATE_DATE),'DD-MM-RRRR HH24:MI:SS') FROM RATECHART_DTL WHERE DEST_CODE IN " + "(select distinct dest_code from destination_category where (country ='" + str1 + "' or country='*%')))) or " + "(SELECT TO_CHAR(MAX(PKG_RATE_SEND_DATE),'DD-MM-RRRR HH24:MI:SS')FROM RATE_SEND_INFO WHERE PACK_ID=" + paramString1 + " AND (COUNTRY='" + str1 + "' or country='*%')) is null AND RATE_ID=" + str3;
    System.out.println("dtl qry::" + str4);
    System.out.println("ratechart_dtl size::" + paramArrayList.size());
    localResultSet = this.stmt.executeQuery(str4);
    while (localResultSet.next())
    {
      if ((localResultSet.getString("COUNTRY") != null) && (localResultSet.getString("COUNTRY").length() > 0) && (!localResultSet.getString("COUNTRY").equals("null")))
        str2 = str2 + localResultSet.getString("COUNTRY") + ",";
      else
        str2 = str2 + ",";
      if ((localResultSet.getString("DEST_CODE") != null) && (localResultSet.getString("DEST_CODE").length() > 0) && (!localResultSet.getString("DEST_CODE").equals("null")))
        str2 = str2 + localResultSet.getString("DEST_CODE") + ",";
      else
        str2 = str2 + ",";
      if ((localResultSet.getString("RATE_PEAK") != null) && (localResultSet.getString("RATE_PEAK").length() > 0) && (!localResultSet.getString("RATE_PEAK").equals("null")))
        str2 = str2 + localResultSet.getString("RATE_PEAK") + ",";
      else
        str2 = str2 + ",";
      if ((localResultSet.getString("RATE_OFF_PEAK") != null) && (localResultSet.getString("RATE_OFF_PEAK").length() > 0) && (!localResultSet.getString("RATE_OFF_PEAK").equals("null")))
        str2 = str2 + localResultSet.getString("RATE_OFF_PEAK") + ",";
      else
        str2 = str2 + ",";
      if ((localResultSet.getString("RATE_DUMMY") != null) && (localResultSet.getString("RATE_DUMMY").length() > 0) && (!localResultSet.getString("RATE_DUMMY").equals("null")))
        str2 = str2 + localResultSet.getString("RATE_DUMMY") + ",";
      else
        str2 = str2 + ",";
      if ((localResultSet.getString("RATE_DUMMY_OFFPEAK") != null) && (localResultSet.getString("RATE_DUMMY_OFFPEAK").length() > 0) && (!localResultSet.getString("RATE_DUMMY_OFFPEAK").equals("null")))
        str2 = str2 + localResultSet.getString("RATE_DUMMY_OFFPEAK") + ",";
      else
        str2 = str2 + ",";
      if ((localResultSet.getString("EFFECT_DATE") != null) && (localResultSet.getString("EFFECT_DATE").length() > 0) && (!localResultSet.getString("EFFECT_DATE").equals("null")))
        str2 = str2 + localResultSet.getString("EFFECT_DATE") + ",";
      else
        str2 = str2 + ",N/A";
      if ((localResultSet.getString("END_DATE") != null) && (localResultSet.getString("END_DATE").length() > 0) && (!localResultSet.getString("END_DATE").equals("null")))
        str2 = str2 + localResultSet.getString("END_DATE") + ",";
      else
        str2 = str2 + ",N/A";
      paramArrayList.add(str2);
      str2 = "";
    }
    return paramArrayList;
  }

  public Vector uploadPackageRates(double paramDouble, String paramString1, String paramString2)
    throws Exception
  {
    Connection localConnection = this.stmt.getConnection();
    Vector localVector = new Vector();
    String str1 = "";
    String str2 = "No country selected..!";
    String str3 = "";
    String str4 = "";
    OracleCallableStatement localOracleCallableStatement = (OracleCallableStatement)localConnection.prepareCall("BEGIN PROC_SEND_PACK_RATE(?,?,?,?,?,?); END;");
    localOracleCallableStatement.setString(1, paramString1);
    localOracleCallableStatement.setDouble(2, paramDouble);
    localOracleCallableStatement.registerOutParameter(3, 12);
    localOracleCallableStatement.registerOutParameter(4, 12);
    localOracleCallableStatement.registerOutParameter(5, 12);
    localOracleCallableStatement.registerOutParameter(6, 12);
    localOracleCallableStatement.execute();
    str1 = localOracleCallableStatement.getString(3);
    str2 = localOracleCallableStatement.getString(4);
    str3 = localOracleCallableStatement.getString(5);
    str4 = localOracleCallableStatement.getString(6);
    localVector.add(0, "" + str1);
    localVector.add(1, "" + str2);
    localVector.add(2, "" + str4);
    localVector.add(3, "" + str3);
    System.out.println("succes msg::" + str1 + "," + str2 + "," + str4 + "," + str3);
    return localVector;
  }

  public Vector uploadPackageRates(double paramDouble, String paramString1, String paramString2, String paramString3)
    throws Exception
  {
    Connection localConnection = this.stmt.getConnection();
    Vector localVector = new Vector();
    String str1 = "";
    String str2 = "No country selected..!";
    String str3 = "";
    String str4 = "";
    OracleCallableStatement localOracleCallableStatement = null;
    if ((paramString3 != null) && (paramString3.length() > 0))
    {
      localOracleCallableStatement = (OracleCallableStatement)localConnection.prepareCall("BEGIN PROC_SEND_PACK_RATE(?,?,?,?,?,?,?); END;");
      localOracleCallableStatement.setString(1, paramString1);
      localOracleCallableStatement.setDouble(2, paramDouble);
      localOracleCallableStatement.setString(3, paramString3);
    }
    else
    {
      localOracleCallableStatement = (OracleCallableStatement)localConnection.prepareCall("BEGIN PROC_SEND_PACK_RATE(?,?,?,?,?,?,?); END;");
      localOracleCallableStatement.setString(1, paramString1);
      localOracleCallableStatement.setDouble(2, paramDouble);
      localOracleCallableStatement.setString(3, "");
    }
    localOracleCallableStatement.registerOutParameter(4, 12);
    localOracleCallableStatement.registerOutParameter(5, 12);
    localOracleCallableStatement.registerOutParameter(6, 12);
    localOracleCallableStatement.registerOutParameter(7, 12);
    localOracleCallableStatement.execute();
    str1 = localOracleCallableStatement.getString(4);
    str2 = localOracleCallableStatement.getString(5);
    str3 = localOracleCallableStatement.getString(6);
    str4 = localOracleCallableStatement.getString(7);
    localVector.add(0, "" + str1);
    localVector.add(1, "" + str2);
    localVector.add(2, "" + str4);
    localVector.add(3, "" + str3);
    System.out.println("succes msg::" + str1 + "," + str2 + "," + str4 + "," + str3);
    return localVector;
  }

  public String updateMaximRateChart(String paramString)
    throws Exception
  {
    paramString = paramString.replaceAll(",", "");
    Connection localConnection = this.stmt.getConnection();
    String str1 = "";
    String str2 = "";
    String str3 = "SELECT DB.username FROM USER_CONN_CONFIG UCC,USER_CONFIG UC,dba_db_links DB WHERE DB.db_link=UCC.DLNK_NAME  AND UC.USER_CONN_ID=UCC.ID AND UC.USER_ID=" + paramString;
    ResultSet localResultSet = this.stmt.executeQuery(str3);
    while (localResultSet.next())
      str1 = localResultSet.getString(1);
    str3 = "select " + str1 + ".update_ratechart() from dual";
    localResultSet = this.stmt.executeQuery(str3);
    while (localResultSet.next())
    {
      str2 = localResultSet.getString(1);
      System.out.println("Result:" + str2);
    }
    str3 = "truncate table " + str1 + ".ratechart_temp";
    System.out.println("qry::::::::" + str3);
    return str2;
  }

  public Vector getEmailBody(String paramString)
    throws Exception
  {
    Vector localVector = new Vector();
    String str = "select dc.country country,dc.dest_name dest_name,dc.dest_code dest_code,dc.min_rate rate,(dc.min_rate*1.00) usr_rate from destination_category dc where dc.min_rate>0 and dc.country not in(select distinct country from RATE_SEND_INFO where PKG_RATE_SEND_DATE>dc.update_date and APP_USER_ID=" + paramString + ") order by dc.dest_name,dc.dest_code";
    ResultSet localResultSet = this.stmt.executeQuery(str);
    while (localResultSet.next())
      localVector.add(localResultSet.getString("country") + "," + localResultSet.getString("dest_name") + "," + localResultSet.getString("dest_code") + "," + localResultSet.getString("rate") + "," + localResultSet.getString("usr_rate"));
    return localVector;
  }

  public Vector uploadRates(List paramList, String paramString1, String paramString2)
    throws Exception
  {
    Connection localConnection = this.stmt.getConnection();
    Vector localVector = new Vector();
    String str1 = "";
    String str2 = "No country selected..!";
    String str3 = "";
    String str4 = "";
    if ((paramList != null) && (paramList.size() > 0))
    {
      String[] arrayOfString = (String[])paramList.toArray(new String[paramList.size()]);
      ArrayDescriptor localArrayDescriptor = ArrayDescriptor.createDescriptor("STR_ARRAY", localConnection);
      ARRAY localARRAY = new ARRAY(localArrayDescriptor, localConnection, arrayOfString);
      OracleCallableStatement localOracleCallableStatement = (OracleCallableStatement)localConnection.prepareCall("BEGIN PROC_SENDRATE(?,?,?,?,?,?,?); END;");
      localOracleCallableStatement.setARRAY(1, localARRAY);
      localOracleCallableStatement.setString(2, paramString2);
      localOracleCallableStatement.setString(3, paramString1);
      localOracleCallableStatement.registerOutParameter(4, 12);
      localOracleCallableStatement.registerOutParameter(5, 12);
      localOracleCallableStatement.registerOutParameter(6, 12);
      localOracleCallableStatement.registerOutParameter(7, 12);
      localOracleCallableStatement.execute();
      str1 = localOracleCallableStatement.getString(4);
      str2 = localOracleCallableStatement.getString(5);
      str3 = localOracleCallableStatement.getString(6);
      str4 = localOracleCallableStatement.getString(7);
    }
    localVector.add(0, "" + str1);
    localVector.add(1, "" + str2);
    localVector.add(2, "" + str4);
    localVector.add(3, "" + str3);
    System.out.println(localVector.toString());
    return localVector;
  }

  public List getRatePkgGroups(int paramInt)
    throws Exception
  {
    ArrayList localArrayList1 = new ArrayList();
    Object localObject1 = new ArrayList();
    Object localObject2 = new ArrayList();
    String str1 = "SELECT id, name, detail FROM package_group";
    if (paramInt > 0)
      str1 = str1 + " where id=" + paramInt;
    ResultSet localResultSet = this.stmt.executeQuery(str1);
    ArrayList localArrayList2 = new ArrayList();
    while ((localResultSet != null) && (localResultSet.next()))
    {
      Item localItem1 = new Item(localResultSet.getInt("id"), localResultSet.getString("name"), localResultSet.getString("detail"));
      localArrayList2.add(localItem1);
    }
    System.out.println(localArrayList2);
    for (int j = 0; j < localArrayList2.size(); j++)
    {
      Item localItem2 = (Item)localArrayList2.get(j);
      int i = localItem2.getId();
      String str2 = localItem2.getName();
      String str3 = localItem2.getDetail();
      localResultSet = this.stmt.executeQuery("SELECT p.pack_id pack_id, p.pack_name pack_name FROM package_config p,PACKAGE_GROUP_PACKAGE pg where pg.pack_grp_id=" + i + " and p.pack_id=pg.pack_id");
      System.out.println("SELECT p.pack_id pack_id, p.pack_name pack_name FROM package_config p,PACKAGE_GROUP_PACKAGE pg where pg.pack_grp_id=" + i + " and p.pack_id=pg.pack_id");
      ArrayList localArrayList3 = new ArrayList();
      while ((localResultSet != null) && (localResultSet.next()))
      {
        System.out.println("pack_id::" + localResultSet.getInt("pack_id") + " pack_name:::" + localResultSet.getString("pack_name"));
        localObject1 = new Item(localResultSet.getInt("pack_id"), localResultSet.getString("pack_name"), "");
        localArrayList3.add(localObject1);
      }

      str1 = "SELECT rg.id id,c.name country,rg.name rategroup, rpg.RATE RATE,rpg.RATE_DUMMY RATE_DUMMY, rpg.RATE_OFFPEAK RATE_OFFPEAK ,rpg.RATE_DUMMY_OFFPEAK RATE_DUMMY_OFFPEAK,to_char(rpg.EFFECTIVE_DATE,'rrrr-mm-dd') EFFECTIVE_DATE,to_char(rpg.end_date,'rrrr-mm-dd') end_date  FROM RATE_GROUP rg,RATE_PKG_GROUP rpg,country c where  rpg.RT_GRP_ID=rg.id and rg.COUNTRY_ID=c.id and  rpg.PKG_GRP_ID=" + i;
      localResultSet = this.stmt.executeQuery(str1);
      while ((localResultSet != null) && (localResultSet.next()))
      {
        localObject2 = new UserRate(localResultSet.getInt("id"), localResultSet.getString("country") + ":" + localResultSet.getString("rategroup"), localResultSet.getDouble("RATE"), localResultSet.getDouble("RATE_OFFPEAK"), localResultSet.getDouble("RATE_DUMMY"), localResultSet.getDouble("RATE_DUMMY_OFFPEAK"), localResultSet.getString("EFFECTIVE_DATE"), localResultSet.getString("end_date"));
        ((List)localObject1).add(localObject2);
      }

      ((List)localObject2).add(localArrayList3);
      ((List)localObject2).add(localObject1);
      localItem2.setChildList((List)localObject2);
      localArrayList1.add(localItem2);
    }
    return (List)(List)localArrayList1;
  }

  public Hashtable getUserRateBetweenDates(String paramString1, String paramString2, String paramString3)
    throws Exception
  {
    String str1 = "";
    if ((paramString2 != null) && (paramString2.length() > 0) && (!paramString2.equals("null")))
      str1 = " and b.effective_date>=to_date('" + paramString2 + "','rrrr-mm-dd')";
    if ((paramString3 != null) && (paramString3.length() > 0) && (!paramString3.equals("null")))
      str1 = str1 + " and b.end_date<=to_date('" + paramString3 + "','rrrr-mm-dd')";
    ResultSet localResultSet = null;
    String str2 = "select dest_code from destination_category where country=upper('" + paramString1 + "')  and substr(dest_code , 1, 1) between '1' and '9' order by dest_code asc";
    localResultSet = this.stmt.executeQuery(str2);
    String str3 = "";
    if (localResultSet.next())
      str3 = localResultSet.getString("dest_code");
    ArrayList localArrayList = new ArrayList();
    str2 = "select distinct USER_ID from USER_IP uip,USERS u where uip.ACTIVE_OUT=1 and  u.ACTIVE='1' and u.ID=uip.user_id order by uip.user_id";
    localResultSet = this.stmt.executeQuery(str2);
    while (localResultSet.next())
      localArrayList.add(localResultSet.getString("USER_ID"));
    str2 = "select distinct b.user_id user_id,b.rate rate,b.code code,c.dest_cat dest_cat from out_billing_history b,destination_category c,user_ip uip,USERS u where u.ACTIVE='1'  and u.ID=uip.user_id and b.code like '" + str3 + "%' and b.code=c.dest_code  and uip.user_id=b.user_id and uip.active_out=1 " + str1 + " order by c.dest_cat,b.code ,b.user_id";
    System.out.println("userrate qry:::" + str2);
    localResultSet = this.stmt.executeQuery(str2);
    Hashtable localHashtable1 = new Hashtable();
    Hashtable localHashtable3 = new Hashtable();
    UserRate localUserRate = null;
    Object localObject = new ArrayList();
    String str4 = "";
    int i = 0;
    String str5 = "";
    double d1 = 0.0D;
    String str6 = "";
    String str7 = "";
    Hashtable localHashtable2;
    int j;
    String str8;
    String str9;
    int k;
    double d2;
    int m;
    while (localResultSet.next())
    {
      d1 = localResultSet.getDouble("rate");
      i = localResultSet.getInt("user_id");
      str4 = localResultSet.getString("code");
      localUserRate = new UserRate(localResultSet.getInt("user_id"), d1);
      localHashtable3.put(i + ":" + str4, "" + d1);
      if ((str5.length() > 0) && (!str5.equals(str4)) && (localObject != null) && (((List)localObject).size() > 0))
      {
        localHashtable2 = (Hashtable)localHashtable1.get(str7);
        localObject = (List)localHashtable2.get(str5);
        j = str5.length();
        str8 = str5;
        str9 = "";
        for (k = 0; (localArrayList != null) && (k < localArrayList.size()); k++)
        {
          str9 = (String)localArrayList.get(k);
          d2 = 0.0D;
          if (localHashtable3.containsKey(str9 + ":" + str5))
            continue;
          for (m = j - 1; (m > 0) && (m >= str3.length()); m--)
          {
            str8 = str5.substring(0, m);
            if (!localHashtable3.containsKey(str9 + ":" + str8))
              continue;
            try
            {
              d2 = Double.parseDouble((String)localHashtable3.get(str9 + ":" + str8));
              localUserRate = new UserRate(Integer.parseInt(str9), d2);
              ((List)localObject).add(localUserRate);
            }
            catch (Exception localException1)
            {
            }
            localHashtable3.put(str9 + ":" + str5, "" + d2);
            break;
          }
        }
        localHashtable2.remove(str5);
        localHashtable2.put(str5, localObject);
      }
      localUserRate = new UserRate(localResultSet.getInt("user_id"), d1);
      if (localHashtable1.containsKey(localResultSet.getString("dest_cat")))
      {
        localHashtable2 = (Hashtable)localHashtable1.get(localResultSet.getString("dest_cat"));
        if (localHashtable2.containsKey(str4))
        {
          localObject = (List)localHashtable2.get(str4);
          ((List)localObject).add(localUserRate);
          localHashtable2.remove(str4);
          localHashtable2.put(str4, localObject);
        }
        else
        {
          localObject = new ArrayList();
          ((List)localObject).add(localUserRate);
          localHashtable2.put(str4, localObject);
        }
      }
      else
      {
        localObject = new ArrayList();
        ((List)localObject).add(localUserRate);
        localHashtable2 = new Hashtable();
        localHashtable2.put(str4, localObject);
        localHashtable1.put(localResultSet.getString("dest_cat"), localHashtable2);
        str7 = localResultSet.getString("dest_cat");
      }
      str5 = str4;
    }
    if ((str5.length() > 0) && (localObject != null) && (((List)localObject).size() > 0))
    {
      localHashtable2 = (Hashtable)localHashtable1.get(str7);
      localObject = (List)localHashtable2.get(str5);
      j = str5.length();
      str8 = str5;
      str9 = "";
      for (k = 0; k < localArrayList.size(); k++)
      {
        str9 = (String)localArrayList.get(k);
        d2 = 0.0D;
        if (localHashtable3.containsKey(str9 + ":" + str5))
          continue;
        for (m = j - 1; (m > 0) && (m >= str3.length()); m--)
        {
          str8 = str5.substring(0, m);
          if (!localHashtable3.containsKey(str9 + ":" + str8))
            continue;
          try
          {
            d2 = Double.parseDouble((String)localHashtable3.get(str9 + ":" + str8));
            localUserRate = new UserRate(Integer.parseInt(str9), d2);
            ((List)localObject).add(localUserRate);
          }
          catch (Exception localException2)
          {
          }
          localHashtable3.put(str9 + ":" + str5, "" + d2);
          break;
        }
      }
      localHashtable2.remove(str5);
      localHashtable2.put(str5, localObject);
    }
    return (Hashtable)localHashtable1;
  }

  public int shiftCodeGroup(String paramString1, String paramString2)
    throws Exception
  {
    int i = 0;
    ResultSet localResultSet = null;
    String str2 = "";
    long l1 = -1L;
    long l2 = -1L;
    int j = 1;
    String str1 = "select d.dest_cat as dest_cat,c.id as country_id from destination_category d,country c where d.dest_code='" + paramString1 + "' and c.name=d.country";
    localResultSet = this.stmt.executeQuery(str1);
    if (localResultSet.next())
    {
      str2 = localResultSet.getString("dest_cat");
      l1 = localResultSet.getLong("country_id");
    }
    System.out.println("destCatOld:" + str2 + " countryId:" + l1);
    str1 = "select id from rate_group where country_id=" + l1 + " and name='" + paramString2 + "'";
    localResultSet = this.stmt.executeQuery(str1);
    if (localResultSet.next())
    {
      l2 = localResultSet.getLong("id");
      j = 0;
    }
    else
    {
      str1 = "select RATE_GROUP_SQ.nextval as id from dual";
      localResultSet = this.stmt.executeQuery(str1);
      if (localResultSet.next())
      {
        l2 = localResultSet.getLong("id");
        str1 = "INSERT INTO rate_group(id,name,country_id) values(" + l2 + ",'" + paramString2 + "'," + l1 + ")";
        this.stmt.executeUpdate(str1);
        j = 1;
      }
      this.stmt.getConnection().commit();
    }
    str1 = "UPDATE destination_category SET dest_cat='" + paramString2 + "'  WHERE dest_code = '" + paramString1 + "' and dest_cat!='" + paramString2 + "'";
    this.stmt.executeUpdate(str1);
    str1 = "select pc.pack_id as pack_id,rd.rate_id as rate_id,rd.rate_peak as rate_peak,nvl(rd.RATE_OFF_PEAK,rd.rate_peak) as RATE_OFF_PEAK, nvl(rd.RATE_DUMMY,0) as RATE_DUMMY,nvl(rd.RATE_DUMMY_OFFPEAK,rd.RATE_DUMMY) as RATE_DUMMY_OFFPEAK,nvl(rd.IS_FLAT,'Y') as IS_FLAT,to_char(rd.OFF_PEAK_START,'hh24:mi:ss') as OFF_PEAK_START, to_char(rd.OFF_PEAK_END,'hh24:mi:ss') as OFF_PEAK_END,to_char(rd.effect_date,'" + Inf.getDateFormat() + " hh24:mi:ss') as effect_date,to_char(rd.end_date,'" + Inf.getDateFormat() + " hh24:mi:ss') as end_date,rd.CONNECTION_FEE as CONNECTION_FEE," + " nvl(rd.ISUNLIMITED,'N') as ISUNLIMITED,nvl(rd.ISOPEN,'Y') as ISOPEN,nvl(rd.IS_PROGRESSIVE_BILLING,'N') as IS_PROGRESSIVE_BILLING,rd.CB_FIRST_LEG_IP_RATE as CB_FIRST_LEG_IP_RATE" + " from ratechart_dtl rd,package_config pc where pc.ratechart_id=rd.rate_id and rd.dest_code='" + paramString1 + "' and (rd.end_date is null or rd.end_date>sysdate)" + " and rd.effect_date=(select max(effect_date) from ratechart_dtl rtd where rtd.dest_code=rd.dest_code and (rtd.end_date is null or rtd.end_date>sysdate) and rd.rate_id=rtd.rate_id)";
    localResultSet = this.stmt.executeQuery(str1);
    String str3 = "";
    String[] arrayOfString = new String[16];
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    while (localResultSet.next())
    {
      str3 = str3 + "," + localResultSet.getString("pack_id");
      arrayOfString = new String[16];
      arrayOfString[0] = localResultSet.getString("pack_id");
      arrayOfString[1] = localResultSet.getString("rate_id");
      arrayOfString[2] = localResultSet.getString("rate_peak");
      arrayOfString[3] = localResultSet.getString("RATE_OFF_PEAK");
      arrayOfString[4] = localResultSet.getString("RATE_DUMMY");
      arrayOfString[5] = localResultSet.getString("RATE_DUMMY_OFFPEAK");
      arrayOfString[6] = localResultSet.getString("IS_FLAT");
      arrayOfString[7] = localResultSet.getString("OFF_PEAK_START");
      arrayOfString[8] = localResultSet.getString("OFF_PEAK_END");
      arrayOfString[9] = localResultSet.getString("CONNECTION_FEE");
      arrayOfString[10] = localResultSet.getString("ISUNLIMITED");
      arrayOfString[11] = localResultSet.getString("ISOPEN");
      arrayOfString[12] = localResultSet.getString("IS_PROGRESSIVE_BILLING");
      arrayOfString[13] = localResultSet.getString("CB_FIRST_LEG_IP_RATE");
      arrayOfString[14] = localResultSet.getString("effect_date");
      arrayOfString[15] = localResultSet.getString("end_date");
      localArrayList1.add(arrayOfString);
    }
    String str6;
    int k=0;
    if (j == 0)
      for (k = 0; k < localArrayList1.size(); k++)
      {
        arrayOfString = (String[])localArrayList1.get(k);
        str1 = "select ID,PKG_GRP_ID,RATE,RATE_DUMMY,RATE_OFFPEAK,RATE_DUMMY_OFFPEAK,to_char(OFF_PEAK_START,'hh24:mi:ss') as OFF_PEAK_START,to_char(OFF_PEAK_END,'hh24:mi:ss') as OFF_PEAK_END,nvl(IS_FLAT,'Y') as IS_FLAT,nvl(IS_OPEN,'Y') as IS_OPEN,CONNECTION_FEE,nvl(IS_PROGRESSIVE_BILLING,'N') as IS_PROGRESSIVE_BILLING,MAX_TALKTIME,MAIN_DEST,to_char(EFFECTIVE_DATE,'" + Inf.getDateFormat() + " hh24:mi:ss') as effect_date," + "to_char(end_date,'" + Inf.getDateFormat() + " hh24:mi:ss') as end_date from RATE_PKG_GROUP where RT_GRP_ID=" + l2 + " and PACK_ID=" + arrayOfString[0] + " and (end_date is null or end_date>sysdate) and effective_date<sysdate";
        localResultSet = this.stmt.executeQuery(str1);
        if (localResultSet.next())
        {
          String str4 = "null";
          String str5 = "null";
          str6 = "null";
          if ((localResultSet.getString("OFF_PEAK_START") != null) && (localResultSet.getString("OFF_PEAK_START").length() > 5))
            str4 = "to_date('" + localResultSet.getString("OFF_PEAK_START") + "','hh24:mi:ss')";
          if ((localResultSet.getString("OFF_PEAK_END") != null) && (localResultSet.getString("OFF_PEAK_END").length() > 5))
            str5 = "to_date('" + localResultSet.getString("OFF_PEAK_END") + "','hh24:mi:ss')";
          if ((localResultSet.getString("effect_date") != null) && (localResultSet.getString("effect_date").length() > 5))
            str6 = "to_date('" + localResultSet.getString("effect_date") + "','" + Inf.getDateFormat() + " hh24:mi:ss')";
          str1 = "update ratechart_dtl set rate_peak=" + localResultSet.getDouble("RATE") + ",RATE_OFF_PEAK=" + localResultSet.getDouble("RATE_OFFPEAK") + ",RATE_DUMMY=" + localResultSet.getDouble("RATE_DUMMY") + ",RATE_DUMMY_OFFPEAK=" + localResultSet.getDouble("RATE_DUMMY_OFFPEAK") + ",OFF_PEAK_START=" + str4 + ",OFF_PEAK_END=" + str5 + ",IS_FLAT='" + localResultSet.getString("IS_FLAT") + "',ISOPEN='" + localResultSet.getString("IS_OPEN") + "',CONNECTION_FEE=" + localResultSet.getDouble("CONNECTION_FEE") + ",IS_PROGRESSIVE_BILLING='" + localResultSet.getString("IS_PROGRESSIVE_BILLING") + "',effect_date=to_date('" + localResultSet.getString("effect_date") + "','" + Inf.getDateFormat() + " hh24:mi:ss')" + " where rate_id=" + arrayOfString[1] + " and dest_code='" + paramString1 + "' and to_char(effect_date,'" + Inf.getDateFormat() + " hh24:mi:ss')='" + arrayOfString[14] + "' and to_char(end_date,'" + Inf.getDateFormat() + " hh24:mi:ss')";
          if (arrayOfString[15] == null)
            str1 = str1 + " is null";
          else
            str1 = str1 + arrayOfString[15];
          this.stmt.executeUpdate(str1);
        }
        else
        {
          localArrayList2.add(arrayOfString);
        }
      }
    localArrayList2.addAll(localArrayList1);
    for (k = 0; k < localArrayList2.size(); k++)
    {
      arrayOfString = (String[])localArrayList2.get(k);
      long l3 = -1L;
      str6 = "null";
      String str7 = "null";
      String str8 = "null";
      double d1 = 1.0D;
      double d2 = 1.0D;
      if ((arrayOfString[7] != null) && (arrayOfString[7].length() > 5))
        str6 = "to_date('" + arrayOfString[7] + "','hh24:mi:ss')";
      if ((arrayOfString[8] != null) && (arrayOfString[8].length() > 5))
        str7 = "to_date('" + arrayOfString[8] + "','hh24:mi:ss')";
      if ((arrayOfString[15] != null) && (arrayOfString[15].length() > 5))
        str8 = "to_date('" + arrayOfString[15] + "','" + Inf.getDateFormat() + " hh24:mi:ss')";
      if ((arrayOfString[4] != null) && (Double.parseDouble(arrayOfString[4]) > 0.0D))
        d1 = 10.0D / Double.parseDouble(arrayOfString[4]);
      if ((arrayOfString[5] != null) && (Double.parseDouble(arrayOfString[5]) > 0.0D))
        d2 = 10.0D / Double.parseDouble(arrayOfString[5]);
      str1 = "select PACK_GRP_ID from PACKAGE_GROUP_PACKAGE where pack_id=" + arrayOfString[0];
      localResultSet = this.stmt.executeQuery(str1);
      int m = 0;
      if (localResultSet.next())
      {
        l3 = localResultSet.getLong("PACK_GRP_ID");
        m = 1;
      }
      if (m == 0)
        continue;
      str1 = "select ID,PKG_GRP_ID,RATE,RATE_DUMMY,nvl(RATE_OFFPEAK,RATE) as RATE_OFFPEAK,nvl(RATE_DUMMY_OFFPEAK,RATE_DUMMY) as RATE_DUMMY_OFFPEAK,to_char(OFF_PEAK_START,'hh24:mi:ss') as OFF_PEAK_START,to_char(OFF_PEAK_END,'hh24:mi:ss') as OFF_PEAK_END,nvl(IS_FLAT,'Y') as IS_FLAT,nvl(IS_OPEN,'Y') as IS_OPEN,CONNECTION_FEE,nvl(IS_PROGRESSIVE_BILLING,'N') as IS_PROGRESSIVE_BILLING,to_char(EFFECTIVE_DATE,'" + Inf.getDateFormat() + " hh24:mi:ss') as effect_date," + "to_char(end_date,'" + Inf.getDateFormat() + " hh24:mi:ss') as end_date from RATE_PKG_GROUP where RT_GRP_ID=" + l2 + " and pkg_grp_id=" + l3 + " and (end_date is null or end_date>sysdate) and not effective_date>sysdate and pack_id is not null";
      localResultSet = this.stmt.executeQuery(str1);
      if (localResultSet.next())
      {
        m = 1;
        arrayOfString[2] = localResultSet.getString("RATE");
        arrayOfString[3] = localResultSet.getString("RATE_OFFPEAK");
        arrayOfString[4] = localResultSet.getString("RATE_DUMMY");
        arrayOfString[5] = localResultSet.getString("RATE_DUMMY_OFFPEAK");
        arrayOfString[6] = localResultSet.getString("IS_FLAT");
        arrayOfString[7] = localResultSet.getString("OFF_PEAK_START");
        arrayOfString[8] = localResultSet.getString("OFF_PEAK_END");
        arrayOfString[9] = localResultSet.getString("CONNECTION_FEE");
        arrayOfString[11] = localResultSet.getString("IS_OPEN");
        arrayOfString[12] = localResultSet.getString("IS_PROGRESSIVE_BILLING");
        arrayOfString[15] = localResultSet.getString("end_date");
      }
      else
      {
        m = 0;
      }
      int n = -1;
      if (m == 0)
      {
        str1 = "select ID from RATE_PKG_GROUP where RT_GRP_ID=" + l2 + " and pkg_grp_id=" + l3 + " and (end_date is null or end_date>sysdate) and not effective_date>sysdate and pack_id is null";
        localResultSet = this.stmt.executeQuery(str1);
        System.out.println(".....qry.5.0:" + str1);
        if (localResultSet.next())
        {
          n = localResultSet.getInt("ID");
          str1 = "update rate_pkg_group set rate=" + arrayOfString[2] + ", rate_dummy=" + arrayOfString[2] + ",is_flat='" + arrayOfString[6] + "',is_open='" + arrayOfString[11] + "',is_progressive_billing='" + arrayOfString[12] + "',RATE_OFFPEAK='" + arrayOfString[3] + "',RATE_DUMMY_OFFPEAK=" + arrayOfString[5] + ",OFF_PEAK_START=" + str6 + ",OFF_PEAK_END=" + str7 + ",CONNECTION_FEE=" + arrayOfString[9] + ",PRICE=10,DUMMY_MINUTE=" + d1 + ",DUMMY_OFF_MINUTE=" + d2 + ",END_DATE=" + str8 + ",effective_date=sysdate where id=" + n;
          System.out.println(".....qry.5.0.1:" + str1);
          this.stmt.executeUpdate(str1);
        }
        if (n < 1)
        {
          str1 = "INSERT INTO rate_pkg_group(id, rt_grp_id, pkg_grp_id, rate, rate_dummy,is_flat,is_open,is_progressive_billing,RATE_OFFPEAK,RATE_DUMMY_OFFPEAK,OFF_PEAK_START,OFF_PEAK_END,MAX_TALKTIME,MAIN_DEST,CONNECTION_FEE,PRICE,DUMMY_MINUTE,DUMMY_OFF_MINUTE,END_DATE,effective_date) VALUES (rate_pkg_group_sq.nextval, " + l2 + "," + l3 + ", " + arrayOfString[2] + ", " + arrayOfString[4] + ",'" + arrayOfString[6] + "','" + arrayOfString[11] + "','" + arrayOfString[12] + "','" + arrayOfString[3] + "'," + arrayOfString[5] + "," + str6 + "," + str7 + ",0,0," + arrayOfString[9] + ",10," + d1 + "," + d2 + "," + str8 + ",sysdate)";
          this.stmt.executeUpdate(str1);
        }
      }
      str1 = "select ID from rate_pkg_group where rt_grp_id=" + l2 + " and pkg_grp_id=" + l3 + " and pack_id=" + arrayOfString[0] + " and (end_date is null or end_date>sysdate) and not effective_date>sysdate ";
      localResultSet = this.stmt.executeQuery(str1);
      n = -1;
      m = 0;
      if (localResultSet.next())
      {
        m = 1;
        n = localResultSet.getInt("ID");
      }
      if (n > 0)
      {
        str1 = "update rate_pkg_group set rate=" + arrayOfString[2] + ",rate_dummy=" + arrayOfString[4] + ",is_flat='" + arrayOfString[6] + "',is_open='" + arrayOfString[11] + "',is_progressive_billing='" + arrayOfString[12] + "',RATE_OFFPEAK='" + arrayOfString[3] + "',RATE_DUMMY_OFFPEAK=" + arrayOfString[5] + ",OFF_PEAK_START=" + str6 + ",OFF_PEAK_END=" + str7 + ",CONNECTION_FEE=" + arrayOfString[9] + ",PRICE=10,DUMMY_MINUTE=" + d1 + ",DUMMY_OFF_MINUTE=" + d2 + ",END_DATE=" + str8 + ",effective_date=sysdate where id=" + n;
        System.out.println(">>>>>>pack_id=" + arrayOfString[0] + ",pkgGrpId:" + l3 + " qry 5.2::" + str1);
        this.stmt.executeUpdate(str1);
      }
      else
      {
        str1 = "INSERT INTO rate_pkg_group(id, rt_grp_id, pkg_grp_id, rate, rate_dummy,pack_id,is_flat,is_open,is_progressive_billing,RATE_OFFPEAK,RATE_DUMMY_OFFPEAK,OFF_PEAK_START,OFF_PEAK_END,MAX_TALKTIME,MAIN_DEST,CONNECTION_FEE,PRICE,DUMMY_MINUTE,DUMMY_OFF_MINUTE,END_DATE,effective_date) VALUES (rate_pkg_group_sq.nextval, " + l2 + "," + l3 + ", " + arrayOfString[2] + ", " + arrayOfString[4] + "," + arrayOfString[0] + ",'" + arrayOfString[6] + "','" + arrayOfString[11] + "','" + arrayOfString[12] + "','" + arrayOfString[3] + "'," + arrayOfString[5] + "," + str6 + "," + str7 + ",0,0," + arrayOfString[9] + ",10," + d1 + "," + d2 + "," + str8 + ",sysdate)";
        this.stmt.executeUpdate(str1);
      }
    }
    this.stmt.getConnection().commit();
    return i;
  }

  public String delInvalidOrigRatechartCodes(String paramString1, String paramString2)
    throws Exception
  {
    String str = "fail";
    OracleCallableStatement localOracleCallableStatement = null;
    localOracleCallableStatement = (OracleCallableStatement)this.stmt.getConnection().prepareCall("BEGIN proc_remove_invalid_code@" + paramString2 + "(?,?); END;");
    localOracleCallableStatement.setString(1, paramString1);
    localOracleCallableStatement.registerOutParameter(2, 12);
    localOracleCallableStatement.execute();
    str = localOracleCallableStatement.getString(2);
    this.stmt.getConnection().commit();
    System.out.println("stationId:" + paramString1 + "  dbLink:" + paramString2 + " status:" + str);
    return str;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.dao.UserRateDAO
 * JD-Core Version:    0.6.0
 */