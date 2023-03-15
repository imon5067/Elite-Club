package com.maestro.rateserver.dao;

import com.maestro.rateserver.model.TransactionReportAccountModel;
import com.maestro.rateserver.model.TransactionReportDayModel;
import com.maestro.rateserver.model.TransactionReportUserModel;
import com.maestro.rateserver.util.StringUtil;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class TransactionDAO
{
  Statement stmt = null;
  StringUtil strUtil = null;
  Hashtable dateRangeHt = null;
  Hashtable allUserHt = null;
  Hashtable allAccountHt = null;

  public TransactionDAO(Statement paramStatement)
  {
    this.stmt = paramStatement;
    this.strUtil = new StringUtil();
  }

  public ArrayList getAllUserDateWise(String paramString1, String paramString2, int paramInt)
    throws Exception
  {
    String str1 = "";
    ResultSet localResultSet = null;
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    String str2 = "";
    str1 = "select distinct user_id from transaction@OMS_LINK trans,transaction_detail@OMS_LINK trans_dtl where TRANSACTION_DATE  between to_date('" + paramString1 + " 00:00:00','" + "dd-mm-rrrr hh24:mi:ss" + "') and to_date('" + paramString2 + " 23:59:59','" + "dd-mm-rrrr hh24:mi:ss" + "')";
    localResultSet = this.stmt.executeQuery(str1);
    while (localResultSet.next())
    {
      str2 = localResultSet.getString("user_id");
      if ((str2 == null) || (str2.length() <= 0) || (str2.equals("null")))
        continue;
      localArrayList1.add(str2);
    }
    return localArrayList1;
  }

  public ArrayList getAllAccount(int paramInt, String paramString)
    throws Exception
  {
    String[] arrayOfString = null;
    ArrayList localArrayList = new ArrayList();
    String str = "select id,name,parent_id,is_group from account_group@OMS_LINK";
    if ((paramString != null) && (paramString.length() > 0))
      str = str + " where is_group='" + paramString + "'";
    str = str + " connect by prior id= parent_id start with id=" + paramInt;
    ResultSet localResultSet = this.stmt.executeQuery(str);
    while (localResultSet.next())
    {
      arrayOfString = new String[3];
      arrayOfString[0] = localResultSet.getString("id");
      arrayOfString[1] = localResultSet.getString("name");
      arrayOfString[2] = localResultSet.getString("parent_id");
      localArrayList.add(arrayOfString);
    }
    return localArrayList;
  }

  public Hashtable getDayTransByType(String paramString1, String paramString2, String paramString3)
    throws Exception
  {
    String str1 = "transaction_date";
    String str2 = "AMOUNT";
    if ("trn_propose".equals(paramString3))
    {
      str1 = "transaction_date";
      str2 = "PROPOSED_AMOUNT";
    }
    else if ("trn_schedule".equals(paramString3))
    {
      str1 = "SCHEDULE_DATE";
      str2 = "SCHEDULE_AMOUNT";
    }
    ArrayList localArrayList = new ArrayList();
    Hashtable localHashtable = new Hashtable();
    String str4 = "";
    double[] arrayOfDouble = null;
    String str5 = "select to_char(t." + str1 + ",'" + "dd-mm-rrrr" + "') as tDate,d.account_id accId,d.user_id usrId,nvl(sum(d." + str2 + "*d.TRANSACTION_TYPE),0) amount from TRANSACTION_DETAIL" + "OMS_LINK" + " d,TRANSACTION" + "OMS_LINK" + " t where d.transaction_id=t.id and t.transaction_date < to_date('" + paramString1 + " 00:00:00','" + "dd-mm-rrrr hh24:mi:ss" + "') group by to_char(t." + str1 + ",'" + "dd-mm-rrrr" + "'),d.account_id,d.user_id";
    ResultSet localResultSet = this.stmt.executeQuery(str5);
    String str3;
    while (localResultSet.next())
    {
      str3 = localResultSet.getString("tDate") + "_" + localResultSet.getString("accId") + ":" + localResultSet.getString("usrId");
      if (localHashtable.containsKey(str3))
        arrayOfDouble = (double[])localHashtable.get(str3);
      else
        arrayOfDouble = new double[3];
      arrayOfDouble[0] = localResultSet.getDouble("amount");
    }
    str5 = "select to_char(t." + str1 + ",'" + "dd-mm-rrrr" + "') as tDate,d.account_id accId,d.user_id usrId,d.TRANSACTION_TYPE as type,nvl(sum(d." + str2 + "),0) amount from TRANSACTION_DETAIL" + "OMS_LINK" + " d,TRANSACTION" + "OMS_LINK" + " t where d.transaction_id=t.id and t.transaction_date between to_date('" + paramString1 + " 00:00:00','" + "dd-mm-rrrr hh24:mi:ss" + "') and to_date('" + paramString2 + " 23:59:59','" + "dd-mm-rrrr hh24:mi:ss" + "') group by to_char(t." + str1 + ",'" + "dd-mm-rrrr" + "'),d.account_id,d.user_id,d.TRANSACTION_TYPE";
    localResultSet = this.stmt.executeQuery(str5);
    while (localResultSet.next())
    {
      str3 = localResultSet.getString("tDate") + "_" + localResultSet.getString("accId") + ":" + localResultSet.getString("usrId");
      if (localHashtable.containsKey(str3))
      {
        arrayOfDouble = (double[])localHashtable.get(str3);
      }
      else
      {
        arrayOfDouble = new double[3];
        arrayOfDouble[0] = 0.0D;
      }
      if (localResultSet.getInt("type") == 1)
        arrayOfDouble[1] = localResultSet.getDouble("amount");
      else if (localResultSet.getInt("type") == -1)
        arrayOfDouble[2] = localResultSet.getDouble("amount");
      localHashtable.put(str3, arrayOfDouble);
    }
    return localHashtable;
  }

  public List getDateRange(String paramString1, String paramString2)
    throws Exception
  {
    String str1 = "";
    ResultSet localResultSet = null;
    ArrayList localArrayList = new ArrayList();
    String str2 = "";
    str1 = "SELECT to_char(TO_DATE('" + paramString1 + "','" + "dd-mm-rrrr" + "')+ROWNUM-1,'ddmmrr') as date_range FROM ( SELECT 1 nothing FROM DUAL CONNECT BY LEVEL <= 1000 ) where " + "TO_DATE('" + paramString1 + " 00:00:00','" + "dd-mm-rrrr hh24:mi:ss" + "')+ROWNUM-1<=TO_DATE('" + paramString2 + " 23:59:59','" + "dd-mm-rrrr hh24:mi:ss" + "')";
    localResultSet = this.stmt.executeQuery(str1);
    while (localResultSet.next())
      localArrayList.add(localResultSet.getString("date_range"));
    return localArrayList;
  }

  public Hashtable getTransactionReportByType(String paramString1, String paramString2)
    throws Exception
  {
    Hashtable localHashtable1 = new Hashtable();
    String str1 = "";
    ArrayList localArrayList1 = getAllUserDateWise(paramString1, paramString2, -1);
    Hashtable localHashtable2 = getDayTransByType(paramString1, paramString2, "trn_propose");
    Hashtable localHashtable3 = getDayTransByType(paramString1, paramString2, "trn_schedule");
    Hashtable localHashtable4 = getDayTransByType(paramString1, paramString2, "");
    List localList = getDateRange(paramString1, paramString2);
    ArrayList localArrayList2 = new ArrayList();
    String[] arrayOfString = null;
    String str2 = "select id,name,parent_id from account_groupOMS_LINK where is_group='N'  connect by prior id= parent_id start with id in(1,2)";
    ResultSet localResultSet = this.stmt.executeQuery(str2);
    while (localResultSet.next())
    {
      arrayOfString = new String[3];
      arrayOfString[0] = localResultSet.getString("id");
      arrayOfString[1] = localResultSet.getString("name");
      arrayOfString[2] = localResultSet.getString("parent_id");
      localArrayList2.add(arrayOfString);
    }
    for (int i = 0; (localArrayList2 != null) && (i < localArrayList2.size()); i++)
      for (int j = 0; (localArrayList1 != null) && (j < localArrayList1.size()); j++)
        for (int k = 0; (localList != null) && (k < localList.size()); k++)
          str1 = localList.get(k) + "_" + localArrayList2.get(i) + ":" + localArrayList1.get(j);
    return localHashtable1;
  }

  public String[] getDays(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws Exception
  {
    if ((paramInt1 < 1940) || (paramInt1 > 2999))
      paramInt1 = 2009;
    if (paramInt2 > 4)
      paramInt2 = 0;
    if (paramInt3 > 3)
      paramInt3 = 0;
    if (paramInt4 > 4)
      paramInt4 = 0;
    String str1 = "01";
    String str2 = "01";
    String str3 = "31";
    String str4 = "12";
    String[] arrayOfString = { str1 + "-" + str2 + "-" + paramInt1, str3 + "-" + str4 + "-" + paramInt1 };
    if (paramInt2 > 0)
      if (paramInt3 < 1)
      {
        if (paramInt2 == 1)
        {
          str3 = "31";
          str4 = "03";
        }
        else if (paramInt2 == 2)
        {
          str2 = "04";
          str4 = "06";
          str3 = "30";
        }
        else if (paramInt2 == 3)
        {
          str2 = "07";
          str4 = "09";
          str3 = "30";
        }
        else if (paramInt2 == 4)
        {
          str2 = "10";
          str4 = "12";
          str3 = "31";
        }
      }
      else
      {
        if ((paramInt4 > 0) && (paramInt4 < 4))
          if (paramInt4 == 1)
          {
            str3 = "07";
          }
          else if (paramInt4 == 2)
          {
            str1 = "08";
            str3 = "15";
          }
          else if (paramInt4 == 3)
          {
            str1 = "16";
            str3 = "23";
          }
          else if (paramInt4 == 4)
          {
            str1 = "24";
          }
        if (paramInt2 == 1)
        {
          if (paramInt3 == 1)
          {
            str2 = str4 = "01";
            if ((paramInt4 == 4) || (paramInt4 < 1))
              str3 = "31";
          }
          else if (paramInt3 == 2)
          {
            str2 = str4 = "02";
            if ((paramInt4 == 4) || (paramInt4 < 1))
              if (checkLeapyear(paramInt1) == true)
                str3 = "29";
              else
                str3 = "28";
          }
          else if (paramInt3 == 3)
          {
            str2 = str4 = "03";
            if ((paramInt4 == 4) || (paramInt4 < 1))
              str3 = "31";
          }
        }
        else if (paramInt2 == 2)
        {
          if (paramInt3 == 1)
          {
            str2 = str4 = "04";
            if ((paramInt4 == 4) || (paramInt4 < 1))
              str3 = "30";
          }
          else if (paramInt3 == 2)
          {
            str2 = str4 = "05";
            if ((paramInt4 == 4) || (paramInt4 < 1))
              str3 = "31";
          }
          else if (paramInt3 == 3)
          {
            str2 = str4 = "06";
            if ((paramInt4 == 4) || (paramInt4 < 1))
              str3 = "30";
          }
        }
        else if (paramInt2 == 3)
        {
          if (paramInt3 == 1)
          {
            str2 = str4 = "07";
            if ((paramInt4 == 4) || (paramInt4 < 1))
              str3 = "31";
          }
          else if (paramInt3 == 2)
          {
            str2 = str4 = "08";
            if ((paramInt4 == 4) || (paramInt4 < 1))
              str3 = "31";
          }
          else if (paramInt3 == 3)
          {
            str2 = str4 = "09";
            if ((paramInt4 == 4) || (paramInt4 < 1))
              str3 = "30";
          }
        }
        else if (paramInt2 == 4)
          if (paramInt3 == 1)
          {
            str2 = str4 = "10";
            if ((paramInt4 == 4) || (paramInt4 < 1))
              str3 = "31";
          }
          else if (paramInt3 == 2)
          {
            str2 = str4 = "11";
            if ((paramInt4 == 4) || (paramInt4 < 1))
              str3 = "30";
          }
          else if (paramInt3 == 3)
          {
            str2 = str4 = "12";
            if ((paramInt4 == 4) || (paramInt4 < 1))
              str3 = "31";
          }
      }
    arrayOfString[0] = (str1 + "-" + str2 + "-" + paramInt1);
    arrayOfString[1] = (str3 + "-" + str4 + "-" + paramInt1);
    return arrayOfString;
  }

  boolean checkLeapyear(int paramInt)
  {
    boolean i = false;
    if ((paramInt > 1970) && (paramInt % 4 == 0))
    {
      if (paramInt % 100 != 0)
        i = true;
      else if (paramInt % 400 == 0)
        i = true;
      else
        i = false;
    }
    else
      i = false;
    return i;
  }

  public Hashtable dayReports(Date paramDate1, Date paramDate2, String paramString1, String paramString2)
    throws SQLException
  {
    Hashtable localHashtable = new Hashtable();
    String str1 = "";
    String str2 = "";
    String str3 = "";
    Timestamp localTimestamp1 = new Timestamp(paramDate1.getTime());
    Timestamp localTimestamp2 = new Timestamp(paramDate2.getTime());
    double d1 = 0.0D;
    double d2 = 0.0D;
    int i = 0;
    String str4 = "";
    if ((paramString2 != null) && (paramString2.length() > 0))
      str4 = "AND d.user_id IN(" + paramString2 + ")";
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTime(paramDate1);
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ddMMyy");
    Date localDate1 = localCalendar.getTime();
    TransactionReportDayModel localTransactionReportDayModel = null;
    while (localDate1.before(paramDate2))
    {
      localTransactionReportDayModel = new TransactionReportDayModel();
      localHashtable.put(localSimpleDateFormat.format(localDate1), localTransactionReportDayModel);
      localCalendar.add(5, 1);
      localDate1 = localCalendar.getTime();
    }
    String str5 = "SELECT SUM(ttype*amount)/1000 amount, tdate,max(priority) priority FROM( SELECT d.transaction_type ttype, d.schedule_amount amount, to_char(t.schedule_date,'ddmmrr') tdate,t.priority priority FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id  and d.schedule_amount<>0 AND  t.schedule_date BETWEEN ? AND ? AND d.account_id in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + "))  " + str4 + str3 + str1 + ")GROUP BY tdate";
    System.out.println("sqlllllllllllll::" + str5);
    PreparedStatement localPreparedStatement = this.stmt.getConnection().prepareStatement(str5);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localPreparedStatement.setTimestamp(2, localTimestamp2);
    ResultSet localResultSet = localPreparedStatement.executeQuery();
    String str6;
    double d3;
    while (localResultSet.next())
    {
      str6 = localResultSet.getString("tdate");
      d3 = localResultSet.getDouble("amount");
      i = localResultSet.getInt("priority");
      localTransactionReportDayModel = (TransactionReportDayModel)localHashtable.get(str6);
      localTransactionReportDayModel.setPtojectedTransaction(d3);
      localTransactionReportDayModel.setPriority(i);
    }
    localPreparedStatement.close();
    str5 = "SELECT SUM(ttype*amount)/1000 amount, tdate FROM( SELECT d.transaction_type ttype, d.amount amount, to_char(t.transaction_date,'ddmmrr') tdate FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id AND d.amount<>0 AND  t.transaction_date BETWEEN ? AND ? AND d.account_id in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + str4 + str3 + str1 + ")GROUP BY tdate";
    System.out.println("2SQL>" + str5);
    localPreparedStatement = this.stmt.getConnection().prepareStatement(str5);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localPreparedStatement.setTimestamp(2, localTimestamp2);
    localResultSet = localPreparedStatement.executeQuery();
    while (localResultSet.next())
    {
      str6 = localResultSet.getString("tdate");
      d3 = localResultSet.getDouble("amount");
      localTransactionReportDayModel = (TransactionReportDayModel)localHashtable.get(str6);
      localTransactionReportDayModel.setActualTransaction(d3);
    }
    localPreparedStatement.close();
    str5 = "SELECT SUM(d.transaction_type*d.amount)/1000 amount FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id AND d.amount <> 0 AND  t.transaction_date < ? AND d.account_id in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + str4 + str3 + str2;
    localPreparedStatement = this.stmt.getConnection().prepareStatement(str5);
    System.out.println("INIT -A>" + str5);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localResultSet = localPreparedStatement.executeQuery();
    if (localResultSet.next())
      d2 = localResultSet.getDouble("amount");
    localPreparedStatement.close();
    str5 = "SELECT SUM(d.transaction_type*d.schedule_amount)/1000 amount FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id AND d.amount=0 and d.schedule_amount<>0 AND  t.transaction_date < ? AND d.account_id in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + "))" + str4 + str3 + str2;
    System.out.println("INIT -P>" + str5);
    localPreparedStatement = this.stmt.getConnection().prepareStatement(str5);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localResultSet = localPreparedStatement.executeQuery();
    if (localResultSet.next())
      d1 = localResultSet.getDouble("amount");
    localPreparedStatement.close();
    Date localDate2 = new Date();
    localCalendar.setTime(paramDate1);
    localDate1 = localCalendar.getTime();
    double d4 = d1;
    double d5 = d2;
    while (localDate1.before(paramDate2))
    {
      localTransactionReportDayModel = (TransactionReportDayModel)localHashtable.get(localSimpleDateFormat.format(localDate1));
      if (localDate1.after(localDate2))
        d5 += localTransactionReportDayModel.getPtojectedTransaction();
      else
        d5 += localTransactionReportDayModel.getActualTransaction();
      localTransactionReportDayModel.setActualBalance(d5);
      d4 += localTransactionReportDayModel.getPtojectedTransaction();
      localTransactionReportDayModel.setPtojectedBalance(d4);
      localCalendar.add(5, 1);
      localDate1 = localCalendar.getTime();
    }
    return localHashtable;
  }

  public TransactionReportUserModel getTransactionReportUserModel(Date paramDate1, Date paramDate2, String paramString1, String paramString2)
    throws SQLException
  {
    TransactionReportUserModel localTransactionReportUserModel = new TransactionReportUserModel();
    String str1 = "";
    if ((paramString2 != null) && (paramString2.length() > 0))
      str1 = " AND d.user_id IN(" + paramString2 + ")";
    String str2 = " SELECT SUM(d.transaction_type*d.amount)/1000 FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t  WHERE t.id = d.transaction_id AND d.amount <> 0 AND  t.transaction_date <= sysdate  AND d.account_id in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + "))" + str1;
    System.out.println("BSQL>" + str2);
    ResultSet localResultSet = this.stmt.executeQuery(str2);
    if (localResultSet.next())
      localTransactionReportUserModel.setBalance(localResultSet.getDouble(1));
    localTransactionReportUserModel.setDays(dayReports(paramDate1, paramDate2, paramString1, paramString2));
    return localTransactionReportUserModel;
  }

  public TransactionReportUserModel getTransactionReportUserModelforProfit(Date paramDate1, Date paramDate2, String paramString, long paramLong)
    throws SQLException
  {
    TransactionReportUserModel localTransactionReportUserModel = new TransactionReportUserModel();
    String str1 = "";
    if (paramLong > 0L)
      str1 = " AND d.user_id = " + paramLong;
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    String str2 = " SELECT SUM(d.transaction_type*d.amount)/1000 FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t  WHERE t.id = d.transaction_id AND d.amount <> 0 AND  t.transaction_date BETWEEN to_date('" + localSimpleDateFormat.format(paramDate1) + " 00:00:00','dd-mm-rrrr hh24:mi:ss') AND to_date('" + localSimpleDateFormat.format(paramDate2) + " 23:59:59','dd-mm-rrrr hh24:mi:ss') " + " AND d.account_id in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString + "))" + str1;
    System.out.println("BSQL>" + str2);
    ResultSet localResultSet = this.stmt.executeQuery(str2);
    if (localResultSet.next())
      localTransactionReportUserModel.setBalance(localResultSet.getDouble(1));
    str2 = "(select c.area_id, a.name from user_customer@OMS_LINK c, area_group@OMS_LINK a WHERE c.area_id = a.id AND user_id = " + paramLong + ") " + "UNION(select h.area_id, a.name from operational_hierarchy@" + "OMS_LINK" + " h, area_group@" + "OMS_LINK" + " a WHERE h.area_id = a.id AND user_id =" + paramLong + " )";
    localResultSet = this.stmt.executeQuery(str2);
    if (localResultSet.next())
    {
      localTransactionReportUserModel.setArea(localResultSet.getString(1));
      localTransactionReportUserModel.setAreaName(localResultSet.getString(2));
    }
    localTransactionReportUserModel.setDays(dayReports(paramDate1, paramDate2, paramString, ""));
    return localTransactionReportUserModel;
  }

  public Hashtable dayUserReports(Date paramDate1, Date paramDate2, String paramString, long paramLong)
    throws SQLException
  {
    Hashtable localHashtable = new Hashtable();
    Timestamp localTimestamp1 = new Timestamp(paramDate1.getTime());
    Timestamp localTimestamp2 = new Timestamp(paramDate2.getTime());
    double d1 = 0.0D;
    double d2 = 0.0D;
    int i = 0;
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTime(paramDate1);
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ddMMyy");
    Date localDate1 = localCalendar.getTime();
    TransactionReportDayModel localTransactionReportDayModel = null;
    while (localDate1.before(paramDate2))
    {
      localTransactionReportDayModel = new TransactionReportDayModel();
      localHashtable.put(localSimpleDateFormat.format(localDate1), localTransactionReportDayModel);
      localCalendar.add(5, 1);
      localDate1 = localCalendar.getTime();
    }
    int j = -1;
    String str1 = "SELECT SUM(" + j + "*ttype*AMOUNT)/1000 amount,tdate,max(priority) priority FROM(select td.transaction_type ttype,td.schedule_amount amount,t.priority priority,to_char(t.transaction_date,'ddmmrr') tdate " + "from transaction_detail@" + "OMS_LINK" + " td,transaction@" + "OMS_LINK" + " t where td.id NOT in(select id FROM transaction_detail@" + "OMS_LINK" + " WHERE account_id IN (15,22) and t.transaction_date between ? and ?) AND td.user_id = " + paramLong + " and td.transaction_id in (select transaction_id FROM transaction_detail@" + "OMS_LINK" + " td," + "transaction@" + "OMS_LINK" + " tt WHERE account_id IN (15,22) AND td.user_id = " + paramLong + " and tt.transaction_date between ? and ? and tt.id=td.transaction_id) and account_id in " + "(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString + ")) and t.id=td.transaction_id and t.voucher_type='" + "INV_SCHEDULE" + "')group by tdate";
    PreparedStatement localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localPreparedStatement.setTimestamp(2, localTimestamp2);
    localPreparedStatement.setTimestamp(3, localTimestamp1);
    localPreparedStatement.setTimestamp(4, localTimestamp2);
    ResultSet localResultSet = localPreparedStatement.executeQuery();
    String str2;
    double d3;
    while (localResultSet.next())
    {
      str2 = localResultSet.getString("tdate");
      d3 = localResultSet.getDouble("amount");
      i = localResultSet.getInt("priority");
      if (localHashtable == null)
        continue;
      localTransactionReportDayModel = (TransactionReportDayModel)localHashtable.get(str2);
      localTransactionReportDayModel.setPtojectedTransaction(d3);
      localTransactionReportDayModel.setPriority(i);
    }
    localPreparedStatement.close();
    str1 = "SELECT SUM(" + j + "*ttype*AMOUNT)/1000 amount,tdate FROM(select td.transaction_type ttype,td.amount amount,to_char(t.transaction_date,'ddmmrr') tdate " + "from transaction_detail@" + "OMS_LINK" + " td,transaction@" + "OMS_LINK" + " t where td.id NOT in(select id FROM transaction_detail@" + "OMS_LINK" + " WHERE account_id IN (15,22) and t.transaction_date between ? and ?) AND td.user_id = " + paramLong + " and td.transaction_id in (select transaction_id FROM transaction_detail@" + "OMS_LINK" + " td,transaction@" + "OMS_LINK" + " tt " + "WHERE account_id IN (15,22) AND td.user_id = " + paramLong + " and tt.transaction_date between ? and ? and tt.id=td.transaction_id) and account_id in " + "(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString + ")) and t.id=td.transaction_id and td.amount<>0)group by tdate";
    localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localPreparedStatement.setTimestamp(2, localTimestamp2);
    localPreparedStatement.setTimestamp(3, localTimestamp1);
    localPreparedStatement.setTimestamp(4, localTimestamp2);
    localResultSet = localPreparedStatement.executeQuery();
    while (localResultSet.next())
    {
      str2 = localResultSet.getString("tdate");
      d3 = localResultSet.getDouble("amount");
      localTransactionReportDayModel = (TransactionReportDayModel)localHashtable.get(str2);
      localTransactionReportDayModel.setActualTransaction(d3);
    }
    localPreparedStatement.close();
    str1 = "SELECT SUM(" + j + "*ttype*AMOUNT)/1000 amount FROM(select td.transaction_type ttype,td.amount amount,to_char(t.transaction_date,'ddmmrr') tdate " + "from transaction_detail@" + "OMS_LINK" + " td,transaction@" + "OMS_LINK" + " t where td.id NOT in(select id FROM transaction_detail@" + "OMS_LINK" + " WHERE account_id IN (15,22) and t.transaction_date < ?) AND td.user_id = " + paramLong + " and td.transaction_id in (select transaction_id FROM transaction_detail@" + "OMS_LINK" + " td,transaction@" + "OMS_LINK" + " tt " + "WHERE account_id IN (15,22) and tt.transaction_date < ? AND td.user_id = " + paramLong + " and tt.id=td.transaction_id) and account_id in " + "(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString + ")) and t.id=td.transaction_id and td.amount<>0 )";
    localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localPreparedStatement.setTimestamp(2, localTimestamp1);
    localResultSet = localPreparedStatement.executeQuery();
    if (localResultSet.next())
      d2 = localResultSet.getDouble("amount");
    localPreparedStatement.close();
    str1 = "SELECT SUM(" + j + "*ttype*AMOUNT)/1000 amount FROM(select td.transaction_type ttype,td.schedule_amount amount,to_char(t.transaction_date,'ddmmrr') tdate " + "from transaction_detail@" + "OMS_LINK" + " td,transaction@" + "OMS_LINK" + " t where td.id NOT in(select id FROM transaction_detail@" + "OMS_LINK" + " WHERE account_id IN (15,22) and t.schedule_date < ?) AND td.user_id = " + paramLong + " and td.transaction_id in (select transaction_id FROM transaction_detail@" + "OMS_LINK" + " td,transaction@" + "OMS_LINK" + " tt " + "WHERE account_id IN (15,22) and tt.schedule_date < ? AND td.user_id = " + paramLong + " and tt.id=td.transaction_id) and account_id in " + "(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString + ")) AND td.amount=0 and td.schedule_amount<>0 and t.id=td.transaction_id )";
    localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localPreparedStatement.setTimestamp(2, localTimestamp1);
    localResultSet = localPreparedStatement.executeQuery();
    if (localResultSet.next())
      d1 = localResultSet.getDouble("amount");
    localPreparedStatement.close();
    Date localDate2 = new Date();
    localCalendar.setTime(paramDate1);
    localDate1 = localCalendar.getTime();
    double d4 = d1;
    double d5 = d2;
    while (localDate1.before(paramDate2))
    {
      localTransactionReportDayModel = (TransactionReportDayModel)localHashtable.get(localSimpleDateFormat.format(localDate1));
      if (localDate1.after(localDate2))
        d5 += localTransactionReportDayModel.getPtojectedTransaction();
      else
        d5 += localTransactionReportDayModel.getActualTransaction();
      localTransactionReportDayModel.setActualBalance(d5);
      d4 += localTransactionReportDayModel.getPtojectedTransaction();
      localTransactionReportDayModel.setPtojectedBalance(d4);
      localCalendar.add(5, 1);
      localDate1 = localCalendar.getTime();
    }
    return localHashtable;
  }

  public TransactionReportAccountModel getTransactionReportAccountModel(Date paramDate1, Date paramDate2, String paramString, long paramLong)
    throws SQLException
  {
    TransactionReportAccountModel localTransactionReportAccountModel = new TransactionReportAccountModel();
    String str = " SELECT SUM(d.transaction_type*d.amount)/1000 FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t  WHERE t.id = d.transaction_id AND d.amount <> 0 AND  t.transaction_date <= sysdate  AND d.account_id in (SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString + "))" + " and d.transaction_id in (select ttd.transaction_id from transaction_detail@" + "OMS_LINK" + " ttd,transaction@" + "OMS_LINK" + " tt where ttd.user_id=" + paramLong + " and tt.transaction_date<=sysdate)";
    ResultSet localResultSet = this.stmt.executeQuery(str);
    if (localResultSet.next())
      localTransactionReportAccountModel.setBalance(localResultSet.getDouble(1));
    str = "(select c.area_id, a.name from user_customer@OMS_LINK c, area_group@OMS_LINK a WHERE c.area_id = a.id AND user_id = " + paramLong + ") " + "UNION(select h.area_id, a.name from operational_hierarchy@" + "OMS_LINK" + " h, area_group@" + "OMS_LINK" + " a WHERE h.area_id = a.id AND user_id =" + paramLong + " )";
    localResultSet = this.stmt.executeQuery(str);
    if (localResultSet.next())
    {
      localTransactionReportAccountModel.setAreaId(localResultSet.getInt(1));
      localTransactionReportAccountModel.setAreaName(localResultSet.getString(2));
    }
    localTransactionReportAccountModel.setDayHash(dayUserReports(paramDate1, paramDate2, paramString, paramLong));
    return localTransactionReportAccountModel;
  }

  public double getGrossProfit(String paramString)
    throws SQLException
  {
    String str = "";
    ResultSet localResultSet = null;
    double d1 = 0.0D;
    double d2 = 0.0D;
    str = "select sum(td.amount*td.transaction_type)/1000 from transaction_detail@OMS_LINK td,transaction@OMS_LINK t where td.account_id in ( SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id = 3) and t.id=td.transaction_id and t.transaction_date between to_date('" + paramString + " 00:00:00','ddMMyy hh24:mi:ss') and to_date('" + paramString + " 23:59:59','ddMMyy hh24:mi:ss') ";
    localResultSet = this.stmt.executeQuery(str);
    while (localResultSet.next())
      d1 = localResultSet.getDouble(1);
    str = "select sum(td.amount*td.transaction_type)/1000 from transaction_detail@OMS_LINK td,transaction@OMS_LINK t where td.account_id = 42 and t.id=td.transaction_id and t.transaction_date between to_date('" + paramString + " 00:00:00','ddMMyy hh24:mi:ss') and to_date('" + paramString + " 23:59:59','ddMMyy hh24:mi:ss') ";
    localResultSet = this.stmt.executeQuery(str);
    while (localResultSet.next())
      d2 = localResultSet.getDouble(1);
    d1 = -d1 - d2;
    return d1;
  }

  public Hashtable dayReportsWithUser(Date paramDate1, Date paramDate2, String paramString)
    throws SQLException
  {
    Hashtable localHashtable1 = null;
    Hashtable localHashtable2 = new Hashtable();
    Timestamp localTimestamp1 = new Timestamp(paramDate1.getTime());
    Timestamp localTimestamp2 = new Timestamp(paramDate2.getTime());
    double d1 = 0.0D;
    double d2 = 0.0D;
    int i = 0;
    String str1 = "";
    TransactionReportUserModel localTransactionReportUserModel = null;
    TransactionReportDayModel localTransactionReportDayModel = null;
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ddMMyy");
    String str2 = "SELECT SUM(ttype*amount) amount, tdate,max(priority) priority,user_id,user_name,area_name FROM( SELECT d.transaction_type ttype, d.schedule_amount amount, to_char(t.schedule_date,'ddmmrr') tdate,d.user_id user_id,t.priority priority,(select ou.first_name||' '||ou.last_name from oms_users@OMS_LINK ou where ou.id=d.user_id) user_name, NVL((SELECT a.name FROM operational_hierarchy@OMS_LINK c, area_group@OMS_LINK a WHERE  c.area_id = a.id AND c.user_id =d.user_id),'') area_name  FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id  and d.schedule_amount<>0 AND  t.schedule_date BETWEEN ? AND ? " + str1 + " AND d.account_id in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in (" + paramString + "))) GROUP BY tdate,user_id";
    System.out.println("sqldddddd::" + str2);
    PreparedStatement localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localPreparedStatement.setTimestamp(2, localTimestamp2);
    String str3 = "";
    ResultSet localResultSet = localPreparedStatement.executeQuery();
    String str5 = "";
    String str4;
    double d3;
    while (localResultSet.next())
    {
      str4 = localResultSet.getString("tdate");
      d3 = localResultSet.getDouble("amount");
      i = localResultSet.getInt("priority");
      str3 = localResultSet.getString("user_id");
      str5 = localResultSet.getString("user_name");
      if (!localHashtable2.containsKey(str3))
        localHashtable2 = initUser(str3, str5, localTimestamp1, localTimestamp2, localHashtable2);
      if ((localHashtable2 == null) || (localHashtable2.size() <= 0))
        continue;
      localTransactionReportUserModel = (TransactionReportUserModel)localHashtable2.get(str3);
      localTransactionReportUserModel.setArea(localResultSet.getString("area_name"));
      localTransactionReportUserModel.setUserName(str5);
      localHashtable1 = localTransactionReportUserModel.getDays();
      if ((localHashtable1 == null) || (localHashtable1.size() <= 0))
        continue;
      localTransactionReportDayModel = (TransactionReportDayModel)localHashtable1.get(str4);
      localTransactionReportDayModel.setPtojectedTransaction(d3);
      localTransactionReportDayModel.setPriority(i);
    }
    localPreparedStatement.close();
    str2 = "SELECT SUM(ttype*amount) amount, tdate,user_id,user_name FROM( SELECT d.transaction_type ttype, d.amount amount, to_char(t.transaction_date,'ddmmrr') tdate,d.user_id user_id,(select ou.first_name||' '||ou.last_name from oms_users@OMS_LINK ou where ou.id=d.user_id) user_name FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id AND d.amount<>0 AND  t.transaction_date BETWEEN ? AND ? " + str1 + " AND d.account_id in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in(" + paramString + ")))GROUP BY tdate,user_id";
    System.out.println("SQL-2>" + str2);
    localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localPreparedStatement.setTimestamp(2, localTimestamp2);
    localResultSet = localPreparedStatement.executeQuery();
    while (localResultSet.next())
    {
      str4 = localResultSet.getString("tdate");
      d3 = localResultSet.getDouble("amount");
      str3 = localResultSet.getString("user_id");
      str5 = localResultSet.getString("user_name");
      if (!localHashtable2.containsKey(str3))
        localHashtable2 = initUser(str3, str5, localTimestamp1, localTimestamp2, localHashtable2);
      localTransactionReportUserModel = (TransactionReportUserModel)localHashtable2.get(str3);
      localHashtable1 = localTransactionReportUserModel.getDays();
      localTransactionReportDayModel = (TransactionReportDayModel)localHashtable1.get(str4);
      localTransactionReportDayModel.setActualTransaction(d3);
    }
    localPreparedStatement.close();
    Enumeration localEnumeration1 = null;
    str2 = "SELECT SUM(d.transaction_type*d.amount) amount,SUM(d.transaction_type*d.quantity) quantity,d.user_id user_id FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id AND d.amount <> 0 AND  t.transaction_date < ? " + str1 + " AND d.account_id in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in(" + paramString + ")) group by user_id";
    System.out.println("SQL-3>" + str2);
    localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localResultSet = localPreparedStatement.executeQuery();
    while (localResultSet.next())
    {
      str3 = localResultSet.getString("user_id");
      d2 = localResultSet.getDouble("amount");
      if ((str3 == null) || (str3.length() <= 0) || (str3.equals("null")) || (localHashtable2 == null) || (localHashtable2.size() <= 0) || (!localHashtable2.containsKey(str3)))
        continue;
      localTransactionReportUserModel = (TransactionReportUserModel)localHashtable2.get(str3);
      localTransactionReportUserModel.setInitACB(d2);
    }
    localPreparedStatement.close();
    str2 = "SELECT SUM(d.transaction_type*d.schedule_amount) amount,d.user_id user_id FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id AND d.amount=0 and d.schedule_amount<>0 AND  t.transaction_date < ? " + str1 + " AND d.account_id in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in (" + paramString + ")) group by user_id";
    System.out.println("SQL-4>" + str2);
    localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localResultSet = localPreparedStatement.executeQuery();
    while (localResultSet.next())
    {
      str3 = localResultSet.getString("user_id");
      d1 = localResultSet.getDouble("amount");
      if ((str3 == null) || (str3.length() <= 0) || (str3.equals("null")) || (localHashtable2 == null) || (localHashtable2.size() <= 0) || (!localHashtable2.containsKey(str3)))
        continue;
      localTransactionReportUserModel = (TransactionReportUserModel)localHashtable2.get(str3);
      localTransactionReportUserModel.setInitPCB(d1);
    }
    localPreparedStatement.close();
    str2 = "SELECT u.id AS userid, sum(d.transaction_type*d.quantity) AS balance,u.first_name AS firstname, u.last_name AS lastname,i.name AS currency,i.purchase_price AS rate,NVL((SELECT a.name FROM operational_hierarchy@OMS_LINK c, area_group@OMS_LINK a WHERE c.area_id = a.id AND c.user_id =u.id),'') area_name FROM transaction_detail@OMS_LINK d, oms_users@OMS_LINK u, item@OMS_LINK i WHERE d.user_id = u.id and d.amount>0 AND u.currency_id = i.id " + str1 + " AND d.account_id IN( SELECT id FROM account_group@" + "OMS_LINK" + "" + " CONNECT BY PRIOR id = parent_id START WITH id IN(" + paramString + ")) GROUP BY u.id,i.name,i.purchase_price,u.first_name, u.last_name";
    System.out.println("SQL-5>" + str2);
    localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
    localResultSet = localPreparedStatement.executeQuery();
    while (localResultSet.next())
    {
      str3 = localResultSet.getString("userid");
      if ((str3 == null) || (str3.length() <= 0) || (str3.equals("null")))
        continue;
      if (((paramString.indexOf("15") != -1) && (paramString.indexOf("22") != -1)) || (((paramString.indexOf("12") != -1) || (paramString.indexOf("24") != -1) || (paramString.indexOf("13") != -1) || (paramString.indexOf("3") != -1) || (paramString.indexOf("7") != -1)) && (!localHashtable2.containsKey(str3))))
        localHashtable2 = initUser(str3, str5, localTimestamp1, localTimestamp2, localHashtable2);
      if ((localHashtable2 == null) || (localHashtable2.size() <= 0) || (!localHashtable2.containsKey(str3)))
        continue;
      localTransactionReportUserModel = (TransactionReportUserModel)localHashtable2.get(str3);
      str5 = localResultSet.getString("firstname");
      if ((localResultSet.getString("lastname") != null) && (localResultSet.getString("lastname").length() > 0) && (!localResultSet.getString("lastname").equals("null")))
        str5 = str5 + " " + localResultSet.getString("lastname");
      localTransactionReportUserModel.setUserName(str5);
      localTransactionReportUserModel.setAreaName(localResultSet.getString("area_name"));
      localTransactionReportUserModel.setRate(localResultSet.getDouble("rate"));
      localTransactionReportUserModel.setCurrencyName(localResultSet.getString("currency"));
      localTransactionReportUserModel.setCblLocal(localResultSet.getDouble("balance"));
    }
    Calendar localCalendar = Calendar.getInstance();
    Date localDate1 = localCalendar.getTime();
    Date localDate2 = new Date();
    localCalendar.setTime(paramDate1);
    double d4 = 0.0D;
    double d5 = 0.0D;
    Enumeration localEnumeration2 = localHashtable2.keys();
    while (localEnumeration2.hasMoreElements())
    {
      localCalendar.setTime(paramDate1);
      localDate1 = localCalendar.getTime();
      str3 = (String)localEnumeration2.nextElement();
      localTransactionReportUserModel = (TransactionReportUserModel)localHashtable2.get(str3);
      d4 = localTransactionReportUserModel.getInitPCB();
      d5 = localTransactionReportUserModel.getInitACB();
      localHashtable1 = localTransactionReportUserModel.getDays();
      localEnumeration1 = localHashtable1.keys();
      while (localDate1.before(paramDate2))
      {
        localTransactionReportDayModel = (TransactionReportDayModel)localHashtable1.get(localSimpleDateFormat.format(localDate1));
        if (localDate1.after(localDate2))
          d5 += localTransactionReportDayModel.getPtojectedTransaction();
        else
          d5 += localTransactionReportDayModel.getActualTransaction();
        localTransactionReportDayModel.setActualBalance(d5);
        d4 += localTransactionReportDayModel.getPtojectedTransaction();
        localTransactionReportDayModel.setPtojectedBalance(d4);
        localCalendar.add(5, 1);
        localDate1 = localCalendar.getTime();
      }
    }
    return localHashtable2;
  }

  private String[] getUserAreaCurrency(String paramString)
    throws SQLException
  {
    String[] arrayOfString = new String[4];
    ResultSet localResultSet = null;
    String str = "(select c.area_id, a.name from user_customer@OMS_LINK c, area_group@OMS_LINK a WHERE c.area_id = a.id AND user_id = " + paramString + ") " + "UNION(select h.area_id, a.name from operational_hierarchy@" + "OMS_LINK" + " h, area_group@" + "OMS_LINK" + " a WHERE h.area_id = a.id AND user_id =" + paramString + " )";
    localResultSet = this.stmt.executeQuery(str);
    if (localResultSet.next())
    {
      arrayOfString[0] = localResultSet.getString(1);
      arrayOfString[1] = localResultSet.getString(2);
    }
    str = "select it.id,it.name from oms_users@OMS_LINK ou, item@OMS_LINK it where ou.id=" + paramString + " and ou.currency_id=it.id";
    localResultSet = this.stmt.executeQuery(str);
    if (localResultSet.next())
    {
      arrayOfString[2] = localResultSet.getString(1);
      arrayOfString[3] = localResultSet.getString(2);
    }
    return arrayOfString;
  }

  private Hashtable initUser(String paramString1, String paramString2, Date paramDate1, Date paramDate2, Hashtable paramHashtable)
    throws SQLException
  {
    TransactionReportUserModel localTransactionReportUserModel = new TransactionReportUserModel();
    localTransactionReportUserModel.setUserId(paramString1);
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTime(paramDate1);
    Hashtable localHashtable = new Hashtable();
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ddMMyy");
    Date localDate = localCalendar.getTime();
    TransactionReportDayModel localTransactionReportDayModel = null;
    while (localDate.before(paramDate2))
    {
      localTransactionReportDayModel = new TransactionReportDayModel();
      localHashtable.put(localSimpleDateFormat.format(localDate), localTransactionReportDayModel);
      localCalendar.add(5, 1);
      localDate = localCalendar.getTime();
    }
    localTransactionReportUserModel.setDays(localHashtable);
    paramHashtable.put(paramString1, localTransactionReportUserModel);
    return paramHashtable;
  }

  public Hashtable getAdjustmentSum(Date paramDate1, Date paramDate2, String paramString)
    throws SQLException
  {
    Hashtable localHashtable = new Hashtable();
    Timestamp localTimestamp1 = new Timestamp(paramDate1.getTime());
    Timestamp localTimestamp2 = new Timestamp(paramDate2.getTime());
    double d1 = 0.0D;
    double d2 = 0.0D;
    int i = 0;
    String str1 = "";
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTime(paramDate1);
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ddMMyy");
    Date localDate1 = localCalendar.getTime();
    TransactionReportDayModel localTransactionReportDayModel = null;
    while (localDate1.before(paramDate2))
    {
      localTransactionReportDayModel = new TransactionReportDayModel();
      localHashtable.put(localSimpleDateFormat.format(localDate1), localTransactionReportDayModel);
      localCalendar.add(5, 1);
      localDate1 = localCalendar.getTime();
    }
    String str2 = "select SUM(ttype*amount) amount, tdate,max(priority) priority FROM(SELECT d.transaction_type ttype,d.schedule_amount amount, to_char(t.schedule_date,'ddmmrr') tdate,t.priority priority from transaction_detail@OMS_LINK d,transaction@OMS_LINK t WHERE account_id = " + paramString + " and d.schedule_amount<>0 and t.schedule_date BETWEEN ? and ? AND transaction_id NOT IN(SELECT transaction_id FROM transaction_detail@" + "OMS_LINK" + " WHERE account_id NOT IN(15,22))" + " and transaction_id in (SELECT transaction_id FROM transaction_detail@" + "OMS_LINK" + " WHERE account_id=" + paramString + ")  and t.id=d.transaction_id)GROUP BY tdate";
    PreparedStatement localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localPreparedStatement.setTimestamp(2, localTimestamp2);
    ResultSet localResultSet = localPreparedStatement.executeQuery();
    String str3;
    double d3;
    while (localResultSet.next())
    {
      str3 = localResultSet.getString("tdate");
      d3 = localResultSet.getDouble("amount");
      i = localResultSet.getInt("priority");
      localTransactionReportDayModel = (TransactionReportDayModel)localHashtable.get(str3);
      localTransactionReportDayModel.setPtojectedTransaction(d3);
      localTransactionReportDayModel.setPriority(i);
    }
    localPreparedStatement.close();
    str2 = "select SUM(ttype*amount) amount, tdate,max(priority) priority FROM(SELECT d.transaction_type ttype,d.amount amount, to_char(t.transaction_date,'ddmmrr') tdate,t.priority priority from transaction_detail@OMS_LINK d,transaction@OMS_LINK t WHERE account_id = " + paramString + " and d.amount<>0 and t.transaction_date BETWEEN ? and ? AND transaction_id NOT IN(SELECT transaction_id FROM transaction_detail@" + "OMS_LINK" + " WHERE account_id NOT IN(15,22))" + " and transaction_id in (SELECT transaction_id FROM transaction_detail@" + "OMS_LINK" + " WHERE account_id=" + paramString + ") and t.id=d.transaction_id)GROUP BY tdate";
    localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localPreparedStatement.setTimestamp(2, localTimestamp2);
    localResultSet = localPreparedStatement.executeQuery();
    while (localResultSet.next())
    {
      str3 = localResultSet.getString("tdate");
      d3 = localResultSet.getDouble("amount");
      localTransactionReportDayModel = (TransactionReportDayModel)localHashtable.get(str3);
      localTransactionReportDayModel.setActualTransaction(d3);
    }
    localPreparedStatement.close();
    str2 = "SELECT SUM(d.transaction_type*d.amount) amount FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE d.account_id=15 and t.id = d.transaction_id AND d.amount <> 0 AND  t.transaction_date < ? AND transaction_id NOT IN ( SELECT transaction_id FROM transaction_detail@OMS_LINK WHERE account_id NOT IN(15,22)) and transaction_id in (SELECT transaction_id FROM transaction_detail@OMS_LINK WHERE  account_id=" + paramString + ")";
    localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localResultSet = localPreparedStatement.executeQuery();
    if (localResultSet.next())
      d2 = localResultSet.getDouble("amount");
    localPreparedStatement.close();
    str2 = "SELECT SUM(d.transaction_type*d.schedule_amount) amount FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE d.account_id=15 and t.id = d.transaction_id AND d.schedule_amount <> 0 AND  t.schedule_date < ? AND transaction_id NOT IN ( SELECT transaction_id FROM transaction_detail@OMS_LINK WHERE account_id NOT IN(15,22)) and transaction_id in (SELECT transaction_id FROM transaction_detail@OMS_LINK WHERE  account_id=" + paramString + ")";
    localPreparedStatement = this.stmt.getConnection().prepareStatement(str2);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localResultSet = localPreparedStatement.executeQuery();
    if (localResultSet.next())
      d1 = localResultSet.getDouble("amount");
    localPreparedStatement.close();
    Date localDate2 = new Date();
    localCalendar.setTime(paramDate1);
    localDate1 = localCalendar.getTime();
    double d4 = d1;
    double d5 = d2;
    while (localDate1.before(paramDate2))
    {
      localTransactionReportDayModel = (TransactionReportDayModel)localHashtable.get(localSimpleDateFormat.format(localDate1));
      if (localDate1.after(localDate2))
        d5 += localTransactionReportDayModel.getPtojectedTransaction();
      else
        d5 += localTransactionReportDayModel.getActualTransaction();
      localTransactionReportDayModel.setActualBalance(d5);
      d4 += localTransactionReportDayModel.getPtojectedTransaction();
      localTransactionReportDayModel.setPtojectedBalance(d4);
      localCalendar.add(5, 1);
      localDate1 = localCalendar.getTime();
    }
    str2 = " SELECT SUM(d.transaction_type*d.amount) amount FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE d.account_id=15 and t.id = d.transaction_id AND d.amount <> 0 AND  t.transaction_date <=sysdate AND transaction_id NOT IN ( SELECT transaction_id FROM transaction_detail@OMS_LINK WHERE account_id NOT IN(15,22)) and transaction_id in (SELECT transaction_id FROM transaction_detail@OMS_LINK WHERE  account_id=" + paramString + ")";
    localResultSet = this.stmt.executeQuery(str2);
    if (localResultSet.next())
      localHashtable.put("current_balance", "" + localResultSet.getDouble("amount"));
    return localHashtable;
  }

  public Hashtable getAdjustmentUser(Date paramDate1, Date paramDate2, String paramString)
    throws SQLException
  {
    Hashtable localHashtable1 = new Hashtable();
    Hashtable localHashtable2 = new Hashtable();
    Hashtable localHashtable3 = new Hashtable();
    Hashtable localHashtable4 = new Hashtable();
    Timestamp localTimestamp1 = new Timestamp(paramDate1.getTime());
    Timestamp localTimestamp2 = new Timestamp(paramDate2.getTime());
    double d1 = 0.0D;
    double d2 = 0.0D;
    int i = 0;
    double d3 = 0.0D;
    TransactionReportUserModel localTransactionReportUserModel = null;
    TransactionReportDayModel localTransactionReportDayModel = null;
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ddMMyy");
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTime(paramDate1);
    for (Date localDate = localCalendar.getTime(); localDate.before(paramDate2); localDate = localCalendar.getTime())
    {
      localTransactionReportDayModel = new TransactionReportDayModel();
      localHashtable2.put(localSimpleDateFormat.format(localDate), localTransactionReportDayModel);
      localCalendar.add(5, 1);
    }
    String str1 = "select distinct user_id from transaction_detail@OMS_LINK t,transaction@OMS_LINK d  WHERE d.id=t.transaction_id and ((d.transaction_date between ? and ? ) or (d.schedule_date between ? and ? )) and account_id = " + paramString + " AND transaction_id NOT IN" + "( SELECT transaction_id FROM transaction_detail@" + "OMS_LINK" + " WHERE account_id NOT IN(15,22))";
    PreparedStatement localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localPreparedStatement.setTimestamp(2, localTimestamp2);
    localPreparedStatement.setTimestamp(3, localTimestamp1);
    localPreparedStatement.setTimestamp(4, localTimestamp2);
    String str2 = "";
    String str3 = "";
    Object localObject = null;
    ArrayList localArrayList = new ArrayList();
    ResultSet localResultSet = localPreparedStatement.executeQuery();
    while (localResultSet.next())
      localArrayList.add(localResultSet.getString("user_id"));
    localPreparedStatement.close();
    String str4 = "";
    String str5 = "";
    for (int j = 0; (localArrayList != null) && (j < localArrayList.size()); j++)
    {
      str1 = "select SUM(ttype*amount) amount, tdate,max(priority) priority,user_id,(select first_name||' '||last_name from oms_users@OMS_LINK where id=user_id) uname FROM(SELECT d.transaction_type ttype,d.schedule_amount amount, to_char(t.schedule_date,'ddmmrr') tdate,d.user_id user_id,t.priority priority from transaction_detail@OMS_LINK d,transaction@OMS_LINK t WHERE account_id = " + paramString + " and d.schedule_amount<>0 and t.schedule_date BETWEEN ? and ? AND transaction_id NOT IN(SELECT transaction_id FROM transaction_detail@" + "OMS_LINK" + " WHERE account_id NOT IN(15,22))" + " and transaction_id in (SELECT transaction_id FROM transaction_detail@" + "OMS_LINK" + " WHERE user_id=" + localArrayList.get(j) + " and account_id=" + paramString + ") and user_id!=" + localArrayList.get(j) + " and t.id=d.transaction_id)GROUP BY tdate,user_id";
      localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
      localPreparedStatement.setTimestamp(1, localTimestamp1);
      localPreparedStatement.setTimestamp(2, localTimestamp2);
      localResultSet = localPreparedStatement.executeQuery();
      while (localResultSet.next())
      {
        str5 = localResultSet.getString("tdate");
        d3 = localResultSet.getDouble("amount");
        i = localResultSet.getInt("priority");
        str2 = localResultSet.getString("user_id");
        str4 = "";
        if ((localResultSet.getString("uname") != null) && (localResultSet.getString("uname").length() > 0) && (!localResultSet.getString("uname").equals("null")))
          localHashtable4.put(localResultSet.getString("user_id"), localResultSet.getString("uname"));
        if (!localHashtable3.containsKey(str2))
          localHashtable3 = initUser(str2, str4, localTimestamp1, localTimestamp2, localHashtable3);
        if ((localHashtable3 == null) || (localHashtable3.size() <= 0))
          continue;
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable3.get(str2);
        if (localTransactionReportUserModel == null)
          continue;
        localHashtable2 = localTransactionReportUserModel.getDays();
        if ((localHashtable2 == null) || (localHashtable2.size() <= 0))
          continue;
        localTransactionReportDayModel = (TransactionReportDayModel)localHashtable2.get(str5);
        localTransactionReportDayModel.setPtojectedTransaction(d3);
        localTransactionReportDayModel.setPriority(i);
      }
      localPreparedStatement.close();
      str1 = "select SUM(ttype*amount) amount, tdate,max(priority) priority,user_id,(select first_name||' '||last_name from oms_users@OMS_LINK where id=user_id) uname FROM(SELECT d.transaction_type ttype,d.amount amount, to_char(t.transaction_date,'ddmmrr') tdate,d.user_id user_id,t.priority priority from transaction_detail@OMS_LINK d,transaction@OMS_LINK t WHERE account_id = " + paramString + " and d.amount<>0 and t.transaction_date BETWEEN ? and ? AND transaction_id NOT IN(SELECT transaction_id FROM transaction_detail@" + "OMS_LINK" + " WHERE account_id NOT IN(15,22))" + " and transaction_id in (SELECT transaction_id FROM transaction_detail@" + "OMS_LINK" + " WHERE user_id=" + localArrayList.get(j) + " and account_id=" + paramString + ") and user_id!=" + localArrayList.get(j) + " and t.id=d.transaction_id)GROUP BY tdate,user_id";
      localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
      localPreparedStatement.setTimestamp(1, localTimestamp1);
      localPreparedStatement.setTimestamp(2, localTimestamp2);
      localResultSet = localPreparedStatement.executeQuery();
      while (localResultSet.next())
      {
        str5 = localResultSet.getString("tdate");
        d3 = localResultSet.getDouble("amount");
        str2 = localResultSet.getString("user_id");
        str4 = "";
        if ((localResultSet.getString("uname") != null) && (localResultSet.getString("uname").length() > 0) && (!localResultSet.getString("uname").equals("null")))
          localHashtable4.put(localResultSet.getString("user_id"), localResultSet.getString("uname"));
        if (!localHashtable3.containsKey(str2))
          localHashtable3 = initUser(str2, str4, localTimestamp1, localTimestamp2, localHashtable3);
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable3.get(str2);
        localHashtable2 = localTransactionReportUserModel.getDays();
        localTransactionReportDayModel = (TransactionReportDayModel)localHashtable2.get(str5);
        localTransactionReportDayModel.setActualTransaction(d3);
      }
      localPreparedStatement.close();
      str1 = "SELECT SUM(d.transaction_type*d.amount) amount,d.user_id user_id FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE d.account_id=15 and t.id = d.transaction_id AND d.amount <> 0 AND  t.transaction_date < ? AND transaction_id NOT IN ( SELECT transaction_id FROM transaction_detail@OMS_LINK WHERE account_id NOT IN(15,22)) and transaction_id in (SELECT transaction_id FROM transaction_detail@OMS_LINK WHERE user_id=" + localArrayList.get(j) + " and account_id=" + paramString + ") and user_id!=" + localArrayList.get(j) + " group by user_id";
      localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
      localPreparedStatement.setTimestamp(1, localTimestamp1);
      localResultSet = localPreparedStatement.executeQuery();
      while (localResultSet.next())
      {
        str2 = localResultSet.getString("user_id");
        d2 = localResultSet.getDouble("amount");
        if ((str2 == null) || (str2.length() <= 0) || (str2.equals("null")) || (localHashtable3 == null) || (localHashtable3.size() <= 0) || (!localHashtable3.containsKey(str2)))
          continue;
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable3.get(str2);
        localTransactionReportUserModel.setInitACB(d2);
      }
      localPreparedStatement.close();
      str1 = "SELECT SUM(d.transaction_type*d.schedule_amount) amount,d.user_id user_id FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE d.account_id=15 and t.id = d.transaction_id AND d.schedule_amount <> 0 AND  t.schedule_date < ? AND transaction_id NOT IN ( SELECT transaction_id FROM transaction_detail@OMS_LINK WHERE account_id NOT IN(15,22)) and transaction_id in (SELECT transaction_id FROM transaction_detail@OMS_LINK WHERE user_id=" + localArrayList.get(j) + " and account_id=" + paramString + ") and user_id!=" + localArrayList.get(j) + " group by user_id";
      localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
      localPreparedStatement.setTimestamp(1, localTimestamp1);
      localResultSet = localPreparedStatement.executeQuery();
      while (localResultSet.next())
      {
        str2 = localResultSet.getString("user_id");
        d1 = localResultSet.getDouble("amount");
        if ((str2 == null) || (str2.length() <= 0) || (str2.equals("null")) || (localHashtable3 == null) || (localHashtable3.size() <= 0) || (!localHashtable3.containsKey(str2)))
          continue;
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable3.get(str2);
        localTransactionReportUserModel.setInitPCB(d1);
      }
      localPreparedStatement.close();
      localHashtable1.put("" + localArrayList.get(j), localHashtable3);
      localHashtable3 = new Hashtable();
    }
    if ((localHashtable4 != null) && (localHashtable4.size() > 0))
      localHashtable1.put("username", localHashtable4);
    return localHashtable1;
  }

  public Hashtable getOverHeadAdjustmentUser(Date paramDate1, Date paramDate2)
    throws SQLException
  {
    Hashtable localHashtable1 = new Hashtable();
    Hashtable localHashtable2 = new Hashtable();
    Hashtable localHashtable3 = new Hashtable();
    Timestamp localTimestamp1 = new Timestamp(paramDate1.getTime());
    Timestamp localTimestamp2 = new Timestamp(paramDate2.getTime());
    double d1 = 0.0D;
    double d2 = 0.0D;
    int i = 0;
    double d3 = 0.0D;
    TransactionReportUserModel localTransactionReportUserModel = null;
    TransactionReportDayModel localTransactionReportDayModel = null;
    String str1 = "SELECT distinct user_id FROM transaction_detail@OMS_LINK WHERE account_id IN(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id=parent_id START WITH id = 4) AND user_id NOT IN(SELECT distinct user_id FROM transaction_detail@OMS_LINK WHERE account_id IN(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id=parent_id START WITH id IN(7,24)))";
    System.out.println("OverHead SQL>" + str1);
    PreparedStatement localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
    String str2 = "";
    ArrayList localArrayList = new ArrayList();
    ResultSet localResultSet = localPreparedStatement.executeQuery();
    while (localResultSet.next())
      localArrayList.add(localResultSet.getString("user_id"));
    localPreparedStatement.close();
    String str3 = "";
    String str4 = "";
    String str5 = "";
    for (int j = 0; (localArrayList != null) && (j < localArrayList.size()); j++)
    {
      str1 = "select SUM(ttype*amount) amount, tdate,max(priority) priority,user_id,account_id,(select name from account_group@OMS_LINK where id=account_id) accName FROM(SELECT d.transaction_type ttype,d.schedule_amount amount,to_char(t.schedule_date,'ddmmrr') tdate,d.user_id user_id,t.priority priority,d.account_id from transaction_detail@OMS_LINK d,transaction@OMS_LINK t WHERE account_id in( SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id IN(4)) and d.schedule_amount<>0 and t.schedule_date BETWEEN ? and ? AND account_id NOT IN(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id IN(24,42)) and d.user_id=" + localArrayList.get(j) + "" + " and t.id=d.transaction_id)GROUP BY tdate,user_id,account_id";
      localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
      localPreparedStatement.setTimestamp(1, localTimestamp1);
      localPreparedStatement.setTimestamp(2, localTimestamp2);
      localResultSet = localPreparedStatement.executeQuery();
      while (localResultSet.next())
      {
        str4 = localResultSet.getString("tdate");
        d3 = localResultSet.getDouble("amount");
        i = localResultSet.getInt("priority");
        str2 = localResultSet.getString("user_id");
        str5 = localResultSet.getString("account_id");
        str3 = "";
        if ((str4 == null) || (str4.length() <= 0) || (str4.equals("null")))
          continue;
        if (!localHashtable3.containsKey(str2 + "<>" + str5))
          localHashtable3 = initUser(str2 + "<>" + str5, str3, localTimestamp1, localTimestamp2, localHashtable3);
        if ((localHashtable3 == null) || (localHashtable3.size() <= 0))
          continue;
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable3.get(str2 + "<>" + str5);
        localTransactionReportUserModel.setAccountName(localResultSet.getString("accname"));
        if (localTransactionReportUserModel == null)
          continue;
        localHashtable2 = localTransactionReportUserModel.getDays();
        if ((localHashtable2 == null) || (localHashtable2.size() <= 0))
          continue;
        localTransactionReportDayModel = (TransactionReportDayModel)localHashtable2.get(str4);
        localTransactionReportDayModel.setPtojectedTransaction(d3);
        localTransactionReportDayModel.setPriority(i);
      }
      localPreparedStatement.close();
      str1 = "select SUM(ttype*amount) amount, tdate,user_id,account_id,(select name from account_group@oms_link where id=account_id) accName FROM(SELECT d.transaction_type ttype,d.amount amount,to_char(t.transaction_date,'ddmmrr') tdate,d.user_id user_id,d.account_id from transaction_detail@OMS_LINK d,transaction@OMS_LINK t WHERE account_id in( SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id IN(4)) and d.amount<>0 and t.transaction_date BETWEEN ? and ? AND account_id NOT IN(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id IN(24,42)) and d.user_id=" + localArrayList.get(j) + "" + " and t.id=d.transaction_id)GROUP BY tdate,user_id,account_id";
      localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
      localPreparedStatement.setTimestamp(1, localTimestamp1);
      localPreparedStatement.setTimestamp(2, localTimestamp2);
      localResultSet = localPreparedStatement.executeQuery();
      while (localResultSet.next())
      {
        str4 = localResultSet.getString("tdate");
        d3 = localResultSet.getDouble("amount");
        str2 = localResultSet.getString("user_id");
        str5 = localResultSet.getString("account_id");
        str3 = "";
        if (!localHashtable3.containsKey(str2 + "<>" + str5))
          localHashtable3 = initUser(str2 + "<>" + str5, str3, localTimestamp1, localTimestamp2, localHashtable3);
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable3.get(str2 + "<>" + str5);
        localTransactionReportUserModel.setAccountName(localResultSet.getString("accname"));
        localHashtable2 = localTransactionReportUserModel.getDays();
        localTransactionReportDayModel = (TransactionReportDayModel)localHashtable2.get(str4);
        localTransactionReportDayModel.setActualTransaction(d3);
      }
      localPreparedStatement.close();
      str1 = "SELECT SUM(d.transaction_type*d.amount) amount,d.user_id user_id,d.account_id account_id FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE account_id in( SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id IN(4)) and d.amount<>0 and t.transaction_date<?  AND account_id NOT IN(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id IN(24,42)) and d.user_id=" + localArrayList.get(j) + " and t.id=d.transaction_id GROUP BY user_id,account_id";
      localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
      localPreparedStatement.setTimestamp(1, localTimestamp1);
      localResultSet = localPreparedStatement.executeQuery();
      while (localResultSet.next())
      {
        str2 = localResultSet.getString("user_id");
        d2 = localResultSet.getDouble("amount");
        str5 = localResultSet.getString("account_id");
        if ((str2 == null) || (str2.length() <= 0) || (str2.equals("null")) || (localHashtable3 == null) || (localHashtable3.size() <= 0) || (!localHashtable3.containsKey(str2 + "<>" + str5)))
          continue;
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable3.get(str2 + "<>" + str5);
        localTransactionReportUserModel.setInitACB(d2);
      }
      localPreparedStatement.close();
      str1 = "SELECT SUM(d.transaction_type*d.schedule_amount) amount,d.user_id user_id,d.account_id account_id FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE account_id in( SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id IN(4)) and d.schedule_amount<>0 and t.schedule_date<?  AND d.account_id NOT IN(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id IN(24,42)) and d.user_id=" + localArrayList.get(j) + " and t.id=d.transaction_id GROUP BY user_id,account_id";
      localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
      localPreparedStatement.setTimestamp(1, localTimestamp1);
      localResultSet = localPreparedStatement.executeQuery();
      while (localResultSet.next())
      {
        str2 = localResultSet.getString("user_id");
        d1 = localResultSet.getDouble("amount");
        str5 = localResultSet.getString("account_id");
        if ((str2 == null) || (str2.length() <= 0) || (str2.equals("null")) || (localHashtable3 == null) || (localHashtable3.size() <= 0) || (!localHashtable3.containsKey(str2 + "<>" + str5)))
          continue;
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable3.get(str2 + "<>" + str5);
        localTransactionReportUserModel.setInitPCB(d1);
      }
      localPreparedStatement.close();
      localHashtable1.put("" + localArrayList.get(j), localHashtable3);
      localHashtable3 = new Hashtable();
    }
    return localHashtable1;
  }

  public Hashtable dayCOGSUsers(Date paramDate1, Date paramDate2, String paramString)
    throws SQLException
  {
    Hashtable localHashtable1 = null;
    Hashtable localHashtable2 = new Hashtable();
    try
    {
      Timestamp localTimestamp1 = new Timestamp(paramDate1.getTime());
      Timestamp localTimestamp2 = new Timestamp(paramDate2.getTime());
      double d1 = 0.0D;
      double d2 = 0.0D;
      int i = 0;
      TransactionReportUserModel localTransactionReportUserModel = null;
      TransactionReportDayModel localTransactionReportDayModel = null;
      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ddMMyy");
      String str1 = "SELECT SUM(ttype*amount) amount, tdate,max(priority) priority,user_id,user_name,area_name FROM( SELECT d.transaction_type ttype, d.schedule_amount amount, to_char(t.schedule_date,'ddmmrr') tdate,d.user_id user_id,t.priority priority,(select ou.first_name||' '||ou.last_name from oms_users@OMS_LINK ou where ou.id=d.user_id) user_name, NVL((SELECT a.name FROM operational_hierarchy@OMS_LINK c, area_group@OMS_LINK a WHERE  c.area_id = a.id AND c.user_id =d.user_id),(SELECT a.name FROM user_customer@OMS_LINK c, area_group@OMS_LINK a WHERE c.area_id = a.id AND c.user_id =d.user_id )) area_name FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id  and d.schedule_amount<>0 AND  t.schedule_date BETWEEN ? AND ?   " + paramString + " " + "AND t.id IN(SELECT transaction_id FROM transaction_detail@" + "OMS_LINK" + " WHERE account_id IN(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id = " + 7 + ")) AND d.account_id  IN( " + 22 + "," + 15 + ")) " + "GROUP BY tdate,user_id";
      System.out.println("COGS::" + str1);
      PreparedStatement localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
      localPreparedStatement.setTimestamp(1, localTimestamp1);
      localPreparedStatement.setTimestamp(2, localTimestamp2);
      String str2 = "";
      ResultSet localResultSet = localPreparedStatement.executeQuery();
      String str4 = "";
      String str3;
      double d3;
      while (localResultSet.next())
      {
        str3 = localResultSet.getString("tdate");
        d3 = localResultSet.getDouble("amount");
        i = localResultSet.getInt("priority");
        str2 = localResultSet.getString("user_id");
        str4 = localResultSet.getString("user_name");
        if (!localHashtable2.containsKey(str2))
          localHashtable2 = initUser(str2, str4, localTimestamp1, localTimestamp2, localHashtable2);
        if ((localHashtable2 == null) || (localHashtable2.size() <= 0))
          continue;
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable2.get(str2);
        localTransactionReportUserModel.setArea(localResultSet.getString("area_name"));
        localTransactionReportUserModel.setUserName(str4);
        localHashtable1 = localTransactionReportUserModel.getDays();
        if ((localHashtable1 == null) || (localHashtable1.size() <= 0))
          continue;
        localTransactionReportDayModel = (TransactionReportDayModel)localHashtable1.get(str3);
        localTransactionReportDayModel.setPtojectedTransaction(d3);
        localTransactionReportDayModel.setPriority(i);
      }
      localPreparedStatement.close();
      str1 = "SELECT SUM(ttype*amount) amount, tdate,user_id,user_name FROM( SELECT d.transaction_type ttype, d.amount amount, to_char(t.transaction_date,'ddmmrr') tdate,d.user_id user_id,(select ou.first_name||' '||ou.last_name from oms_users@OMS_LINK ou where ou.id=d.user_id) user_name FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id AND d.amount<>0 AND  t.transaction_date BETWEEN ? AND ? " + paramString + " " + "AND t.id IN(SELECT transaction_id FROM transaction_detail@" + "OMS_LINK" + " WHERE account_id IN(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id = " + 7 + ")) AND d.account_id  IN( " + 22 + "," + 15 + ")) " + " GROUP BY tdate,user_id";
      System.out.println("SQL-2>" + str1);
      localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
      localPreparedStatement.setTimestamp(1, localTimestamp1);
      localPreparedStatement.setTimestamp(2, localTimestamp2);
      localResultSet = localPreparedStatement.executeQuery();
      while (localResultSet.next())
      {
        str3 = localResultSet.getString("tdate");
        d3 = localResultSet.getDouble("amount");
        str2 = localResultSet.getString("user_id");
        str4 = localResultSet.getString("user_name");
        if (!localHashtable2.containsKey(str2))
          localHashtable2 = initUser(str2, str4, localTimestamp1, localTimestamp2, localHashtable2);
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable2.get(str2);
        localHashtable1 = localTransactionReportUserModel.getDays();
        localTransactionReportDayModel = (TransactionReportDayModel)localHashtable1.get(str3);
        localTransactionReportDayModel.setActualTransaction(d3);
      }
      localPreparedStatement.close();
      Enumeration localEnumeration1 = null;
      str1 = "SELECT SUM(d.transaction_type*d.amount) amount,SUM(d.transaction_type*d.quantity) quantity,d.user_id user_id FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id AND d.amount <> 0 AND  t.transaction_date < ?  " + paramString + " " + "AND t.id IN(SELECT transaction_id FROM transaction_detail@" + "OMS_LINK" + " WHERE account_id IN(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id = " + 7 + ")) AND d.account_id  IN( " + 22 + "," + 15 + ") " + " GROUP BY user_id";
      System.out.println("SQL-3>" + str1);
      localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
      localPreparedStatement.setTimestamp(1, localTimestamp1);
      localResultSet = localPreparedStatement.executeQuery();
      while (localResultSet.next())
      {
        str2 = localResultSet.getString("user_id");
        d2 = localResultSet.getDouble("amount");
        if ((str2 == null) || (str2.length() <= 0) || (str2.equals("null")) || (localHashtable2 == null) || (localHashtable2.size() <= 0) || (!localHashtable2.containsKey(str2)))
          continue;
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable2.get(str2);
        localTransactionReportUserModel.setInitACB(d2);
      }
      localPreparedStatement.close();
      str1 = "SELECT SUM(d.transaction_type*d.schedule_amount) amount,d.user_id user_id FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id AND d.amount=0 and d.schedule_amount<>0 AND  t.transaction_date < ? " + paramString + " " + "AND t.id IN(SELECT transaction_id FROM transaction_detail@" + "OMS_LINK" + " WHERE account_id IN(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id = " + 7 + ")) AND d.account_id  IN( " + 22 + "," + 15 + ") " + " GROUP BY user_id";
      System.out.println("SQL-4>" + str1);
      localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
      localPreparedStatement.setTimestamp(1, localTimestamp1);
      localResultSet = localPreparedStatement.executeQuery();
      while (localResultSet.next())
      {
        str2 = localResultSet.getString("user_id");
        d1 = localResultSet.getDouble("amount");
        if ((str2 == null) || (str2.length() <= 0) || (str2.equals("null")) || (localHashtable2 == null) || (localHashtable2.size() <= 0) || (!localHashtable2.containsKey(str2)))
          continue;
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable2.get(str2);
        localTransactionReportUserModel.setInitPCB(d1);
      }
      localPreparedStatement.close();
      str1 = "SELECT u.id AS userid, sum(d.transaction_type*d.quantity) AS balance,u.first_name AS firstname, u.last_name AS lastname,i.name AS currency,i.purchase_price AS rate,NVL((SELECT a.name FROM operational_hierarchy@OMS_LINK c, area_group@OMS_LINK a WHERE c.area_id = a.id AND c.user_id =u.id),(SELECT a.name FROM user_customer@OMS_LINK c, area_group@OMS_LINK a WHERE c.area_id = a.id AND c.user_id =u.id )) area_name FROM transaction_detail@OMS_LINK d, oms_users@OMS_LINK u, item@OMS_LINK i WHERE d.user_id = u.id and d.amount>0 " + paramString + " AND " + "u.currency_id = i.id AND d.transaction_id IN(SELECT transaction_id FROM transaction_detail@" + "OMS_LINK" + " WHERE account_id IN(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id = " + 7 + ")) AND d.account_id  IN( " + 22 + "," + 15 + ")  GROUP BY u.id,i.name,i.purchase_price,u.first_name, u.last_name";
      System.out.println("COGS SQL-5>" + str1);
      localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
      localResultSet = localPreparedStatement.executeQuery();
      while (localResultSet.next())
      {
        str2 = localResultSet.getString("userid");
        if ((str2 == null) || (str2.length() <= 0) || (str2.equals("null")) || (localHashtable2 == null))
          continue;
        if (!localHashtable2.containsKey(str2))
        {
          localHashtable2 = initUser(str2, str4, localTimestamp1, localTimestamp2, localHashtable2);
          continue;
        }
        if (!localHashtable2.containsKey(str2))
          continue;
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable2.get(str2);
        str4 = localResultSet.getString("firstname");
        if ((localResultSet.getString("lastname") != null) && (localResultSet.getString("lastname").length() > 0) && (!localResultSet.getString("lastname").equals("null")))
          str4 = str4 + " " + localResultSet.getString("lastname");
        localTransactionReportUserModel.setUserName(str4);
        localTransactionReportUserModel.setAreaName(localResultSet.getString("area_name"));
        localTransactionReportUserModel.setRate(localResultSet.getDouble("rate"));
        localTransactionReportUserModel.setCurrencyName(localResultSet.getString("currency"));
        localTransactionReportUserModel.setCblLocal(localResultSet.getDouble("balance"));
      }
      Calendar localCalendar = Calendar.getInstance();
      Date localDate1 = localCalendar.getTime();
      Date localDate2 = new Date();
      localCalendar.setTime(paramDate1);
      double d4 = 0.0D;
      double d5 = 0.0D;
      Enumeration localEnumeration2 = localHashtable2.keys();
      while (localEnumeration2.hasMoreElements())
      {
        localCalendar.setTime(paramDate1);
        localDate1 = localCalendar.getTime();
        str2 = (String)localEnumeration2.nextElement();
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable2.get(str2);
        d4 = localTransactionReportUserModel.getInitPCB();
        d5 = localTransactionReportUserModel.getInitACB();
        localHashtable1 = localTransactionReportUserModel.getDays();
        localEnumeration1 = localHashtable1.keys();
        while (localDate1.before(paramDate2))
        {
          localTransactionReportDayModel = (TransactionReportDayModel)localHashtable1.get(localSimpleDateFormat.format(localDate1));
          if (localDate1.after(localDate2))
            d5 += localTransactionReportDayModel.getPtojectedTransaction();
          else
            d5 += localTransactionReportDayModel.getActualTransaction();
          localTransactionReportDayModel.setActualBalance(d5);
          d4 += localTransactionReportDayModel.getPtojectedTransaction();
          localTransactionReportDayModel.setPtojectedBalance(d4);
          localCalendar.add(5, 1);
          localDate1 = localCalendar.getTime();
        }
      }
    }
    catch (SQLException localSQLException)
    {
      System.out.println("++++++++++++++++++++++++++++++++++++++++++++==");
      throw localSQLException;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localHashtable2;
  }

  public Hashtable dayRevenueUsers(Date paramDate1, Date paramDate2)
    throws SQLException
  {
    Hashtable localHashtable1 = null;
    Hashtable localHashtable2 = new Hashtable();
    try
    {
      Timestamp localTimestamp1 = new Timestamp(paramDate1.getTime());
      Timestamp localTimestamp2 = new Timestamp(paramDate2.getTime());
      double d1 = 0.0D;
      double d2 = 0.0D;
      int i = 0;
      TransactionReportUserModel localTransactionReportUserModel = null;
      TransactionReportDayModel localTransactionReportDayModel = null;
      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ddMMyy");
      String str1 = "SELECT SUM(ttype*amount) amount, tdate,max(priority) priority,user_id,user_name,area_name FROM( SELECT d.transaction_type ttype, d.schedule_amount amount, to_char(t.schedule_date,'ddmmrr') tdate,d.user_id user_id,t.priority priority,(select ou.first_name||' '||ou.last_name from oms_users@OMS_LINK ou where ou.id=d.user_id) user_name, NVL((SELECT a.name FROM operational_hierarchy@OMS_LINK c, area_group@OMS_LINK a WHERE  c.area_id = a.id AND c.user_id =d.user_id),(SELECT a.name FROM user_customer@OMS_LINK c, area_group@OMS_LINK a WHERE c.area_id = a.id AND c.user_id =d.user_id )) area_name FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id  and d.schedule_amount<>0 AND  t.schedule_date BETWEEN ? AND ? AND t.id IN(SELECT transaction_id FROM transaction_detail@OMS_LINK WHERE account_id = 31) AND d.account_id  IN (22,15)) GROUP BY tdate,user_id";
      System.out.println("sqldddddd::" + str1);
      PreparedStatement localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
      localPreparedStatement.setTimestamp(1, localTimestamp1);
      localPreparedStatement.setTimestamp(2, localTimestamp2);
      String str2 = "";
      ResultSet localResultSet = localPreparedStatement.executeQuery();
      String str4 = "";
      String str3;
      double d3;
      while (localResultSet.next())
      {
        str3 = localResultSet.getString("tdate");
        d3 = localResultSet.getDouble("amount");
        i = localResultSet.getInt("priority");
        str2 = localResultSet.getString("user_id");
        str4 = localResultSet.getString("user_name");
        if (!localHashtable2.containsKey(str2))
          localHashtable2 = initUser(str2, str4, localTimestamp1, localTimestamp2, localHashtable2);
        if ((localHashtable2 == null) || (localHashtable2.size() <= 0))
          continue;
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable2.get(str2);
        localTransactionReportUserModel.setArea(localResultSet.getString("area_name"));
        localTransactionReportUserModel.setUserName(str4);
        localHashtable1 = localTransactionReportUserModel.getDays();
        if ((localHashtable1 == null) || (localHashtable1.size() <= 0))
          continue;
        localTransactionReportDayModel = (TransactionReportDayModel)localHashtable1.get(str3);
        localTransactionReportDayModel.setPtojectedTransaction(d3);
        localTransactionReportDayModel.setPriority(i);
      }
      localPreparedStatement.close();
      str1 = "SELECT SUM(ttype*amount) amount, tdate,user_id,user_name FROM( SELECT d.transaction_type ttype, d.amount amount, to_char(t.transaction_date,'ddmmrr') tdate,d.user_id user_id,(select ou.first_name||' '||ou.last_name from oms_users@OMS_LINK ou where ou.id=d.user_id) user_name FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id AND d.amount<>0 AND  t.transaction_date BETWEEN ? AND ? AND t.id IN(SELECT transaction_id FROM transaction_detail@OMS_LINK WHERE account_id = 31) AND d.account_id  IN( 22,15))  GROUP BY tdate,user_id";
      System.out.println("SQL-2>" + str1);
      localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
      localPreparedStatement.setTimestamp(1, localTimestamp1);
      localPreparedStatement.setTimestamp(2, localTimestamp2);
      localResultSet = localPreparedStatement.executeQuery();
      while (localResultSet.next())
      {
        str3 = localResultSet.getString("tdate");
        d3 = localResultSet.getDouble("amount");
        str2 = localResultSet.getString("user_id");
        str4 = localResultSet.getString("user_name");
        if (!localHashtable2.containsKey(str2))
          localHashtable2 = initUser(str2, str4, localTimestamp1, localTimestamp2, localHashtable2);
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable2.get(str2);
        localHashtable1 = localTransactionReportUserModel.getDays();
        localTransactionReportDayModel = (TransactionReportDayModel)localHashtable1.get(str3);
        localTransactionReportDayModel.setActualTransaction(d3);
      }
      localPreparedStatement.close();
      str1 = "SELECT SUM(d.transaction_type*d.amount) amount,SUM(d.transaction_type*d.quantity) quantity,d.user_id user_id FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id AND d.amount <> 0 AND  t.transaction_date < ?  AND t.id IN(SELECT transaction_id FROM transaction_detail@OMS_LINK WHERE account_id =31) AND d.account_id  IN( 22,15)  GROUP BY user_id";
      System.out.println("SQL-3>" + str1);
      localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
      localPreparedStatement.setTimestamp(1, localTimestamp1);
      localResultSet = localPreparedStatement.executeQuery();
      while (localResultSet.next())
      {
        str2 = localResultSet.getString("user_id");
        d2 = localResultSet.getDouble("amount");
        if ((str2 == null) || (str2.length() <= 0) || (str2.equals("null")) || (localHashtable2 == null) || (localHashtable2.size() <= 0) || (!localHashtable2.containsKey(str2)))
          continue;
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable2.get(str2);
        localTransactionReportUserModel.setInitACB(d2);
      }
      localPreparedStatement.close();
      str1 = "SELECT SUM(d.transaction_type*d.schedule_amount) amount,d.user_id user_id FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id AND d.amount=0 and d.schedule_amount<>0 AND  t.transaction_date < ? AND t.id IN(SELECT transaction_id FROM transaction_detail@OMS_LINK WHERE account_id =31) AND d.account_id  IN( 22,15)  GROUP BY user_id";
      System.out.println("SQL-4>" + str1);
      localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
      localPreparedStatement.setTimestamp(1, localTimestamp1);
      localResultSet = localPreparedStatement.executeQuery();
      while (localResultSet.next())
      {
        str2 = localResultSet.getString("user_id");
        d1 = localResultSet.getDouble("amount");
        if ((str2 == null) || (str2.length() <= 0) || (str2.equals("null")) || (localHashtable2 == null) || (localHashtable2.size() <= 0) || (!localHashtable2.containsKey(str2)))
          continue;
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable2.get(str2);
        localTransactionReportUserModel.setInitPCB(d1);
      }
      localPreparedStatement.close();
      str1 = "SELECT u.id AS userid, sum(d.transaction_type*d.quantity) AS balance,u.first_name AS firstname, u.last_name AS lastname,i.name AS currency,i.purchase_price AS rate,NVL((SELECT a.name FROM operational_hierarchy@OMS_LINK c, area_group@OMS_LINK a WHERE c.area_id = a.id AND c.user_id =u.id),(SELECT a.name FROM user_customer@OMS_LINK c, area_group@OMS_LINK a WHERE c.area_id = a.id AND c.user_id =u.id )) area_name FROM transaction_detail@OMS_LINK d, oms_users@OMS_LINK u, item@OMS_LINK i WHERE d.user_id = u.id and d.amount>0 AND u.currency_id = i.id AND d.transaction_id IN(SELECT transaction_id FROM transaction_detail@OMS_LINK WHERE account_id =31) AND d.account_id  IN( 22,15)  GROUP BY u.id,i.name,i.purchase_price,u.first_name, u.last_name";
      System.out.println("COGS SQL-5>" + str1);
      localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
      localResultSet = localPreparedStatement.executeQuery();
      while (localResultSet.next())
      {
        str2 = localResultSet.getString("userid");
        if ((str2 == null) || (str2.length() <= 0) || (str2.equals("null")) || (localHashtable2 == null))
          continue;
        if (!localHashtable2.containsKey(str2))
        {
          localHashtable2 = initUser(str2, str4, localTimestamp1, localTimestamp2, localHashtable2);
          continue;
        }
        if (!localHashtable2.containsKey(str2))
          continue;
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable2.get(str2);
        str4 = localResultSet.getString("firstname");
        if ((localResultSet.getString("lastname") != null) && (localResultSet.getString("lastname").length() > 0) && (!localResultSet.getString("lastname").equals("null")))
          str4 = str4 + " " + localResultSet.getString("lastname");
        localTransactionReportUserModel.setUserName(str4);
        localTransactionReportUserModel.setAreaName(localResultSet.getString("area_name"));
        localTransactionReportUserModel.setRate(localResultSet.getDouble("rate"));
        localTransactionReportUserModel.setCurrencyName(localResultSet.getString("currency"));
        localTransactionReportUserModel.setCblLocal(localResultSet.getDouble("balance"));
      }
      Calendar localCalendar = Calendar.getInstance();
      Date localDate1 = localCalendar.getTime();
      Date localDate2 = new Date();
      localCalendar.setTime(paramDate1);
      double d4 = 0.0D;
      double d5 = 0.0D;
      Enumeration localEnumeration = localHashtable2.keys();
      while (localEnumeration.hasMoreElements())
      {
        localCalendar.setTime(paramDate1);
        localDate1 = localCalendar.getTime();
        str2 = (String)localEnumeration.nextElement();
        localTransactionReportUserModel = (TransactionReportUserModel)localHashtable2.get(str2);
        d4 = localTransactionReportUserModel.getInitPCB();
        d5 = localTransactionReportUserModel.getInitACB();
        localHashtable1 = localTransactionReportUserModel.getDays();
        while (localDate1.before(paramDate2))
        {
          localTransactionReportDayModel = (TransactionReportDayModel)localHashtable1.get(localSimpleDateFormat.format(localDate1));
          if (localDate1.after(localDate2))
            d5 += localTransactionReportDayModel.getPtojectedTransaction();
          else
            d5 += localTransactionReportDayModel.getActualTransaction();
          localTransactionReportDayModel.setActualBalance(d5);
          d4 += localTransactionReportDayModel.getPtojectedTransaction();
          localTransactionReportDayModel.setPtojectedBalance(d4);
          localCalendar.add(5, 1);
          localDate1 = localCalendar.getTime();
        }
      }
    }
    catch (SQLException localSQLException)
    {
      System.out.println("++==++===================================================");
      throw localSQLException;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localHashtable2;
  }

  public TransactionReportUserModel getGroup(Date paramDate1, Date paramDate2, String paramString1, String paramString2)
    throws SQLException
  {
    TransactionReportUserModel localTransactionReportUserModel = new TransactionReportUserModel();
    Hashtable localHashtable = new Hashtable();
    Timestamp localTimestamp1 = new Timestamp(paramDate1.getTime());
    Timestamp localTimestamp2 = new Timestamp(paramDate2.getTime());
    double d1 = 0.0D;
    double d2 = 0.0D;
    int i = 0;
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTime(paramDate1);
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ddMMyy");
    Date localDate1 = localCalendar.getTime();
    TransactionReportDayModel localTransactionReportDayModel = null;
    while (localDate1.before(paramDate2))
    {
      localTransactionReportDayModel = new TransactionReportDayModel();
      localHashtable.put(localSimpleDateFormat.format(localDate1), localTransactionReportDayModel);
      localCalendar.add(5, 1);
      localDate1 = localCalendar.getTime();
    }
    String str1 = "SELECT SUM(ttype*amount) amount, tdate,max(priority) priority FROM( SELECT d.transaction_type ttype, d.schedule_amount amount, to_char(t.schedule_date,'" + paramString2 + "') tdate,t.priority priority FROM transaction_detail@" + "OMS_LINK" + " d, transaction@" + "OMS_LINK" + " t " + "WHERE t.id = d.transaction_id  and d.schedule_amount<>0 AND  t.schedule_date BETWEEN ? AND ? " + "AND d.account_id in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + "))  " + ")GROUP BY tdate";
    System.out.println("NSQL1>" + str1);
    PreparedStatement localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localPreparedStatement.setTimestamp(2, localTimestamp2);
    ResultSet localResultSet = localPreparedStatement.executeQuery();
    String str2;
    double d3;
    while (localResultSet.next())
    {
      str2 = localResultSet.getString("tdate");
      d3 = localResultSet.getDouble("amount");
      i = localResultSet.getInt("priority");
      localTransactionReportDayModel = (TransactionReportDayModel)localHashtable.get(str2);
      localTransactionReportDayModel.setPtojectedTransaction(d3);
      localTransactionReportDayModel.setPriority(i);
    }
    localPreparedStatement.close();
    str1 = "SELECT SUM(ttype*amount) amount, tdate FROM( SELECT d.transaction_type ttype, d.amount amount, to_char(t.transaction_date,'" + paramString2 + "') tdate FROM transaction_detail@" + "OMS_LINK" + " d, transaction@" + "OMS_LINK" + " t " + "WHERE t.id = d.transaction_id AND d.amount<>0 AND  t.transaction_date BETWEEN ? AND ? " + "AND d.account_id in(SELECT id FROM account_group@" + "OMS_LINK" + " CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) " + ")GROUP BY tdate";
    System.out.println("NSQL2>" + str1);
    localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localPreparedStatement.setTimestamp(2, localTimestamp2);
    localResultSet = localPreparedStatement.executeQuery();
    while (localResultSet.next())
    {
      str2 = localResultSet.getString("tdate");
      d3 = localResultSet.getDouble("amount");
      localTransactionReportDayModel = (TransactionReportDayModel)localHashtable.get(str2);
      localTransactionReportDayModel.setActualTransaction(d3);
    }
    localPreparedStatement.close();
    str1 = "SELECT SUM(d.transaction_type*d.amount) amount FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id AND d.amount <> 0 AND  t.transaction_date < ? AND d.account_id in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) ";
    localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
    System.out.println("NSQL3>" + str1);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localResultSet = localPreparedStatement.executeQuery();
    if (localResultSet.next())
      d2 = localResultSet.getDouble("amount");
    localPreparedStatement.close();
    str1 = "SELECT SUM(d.transaction_type*d.schedule_amount) amount FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id AND d.amount=0 and d.schedule_amount<>0 AND  t.transaction_date < ? AND d.account_id in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + "))";
    System.out.println("NSQL4>" + str1);
    localPreparedStatement = this.stmt.getConnection().prepareStatement(str1);
    localPreparedStatement.setTimestamp(1, localTimestamp1);
    localResultSet = localPreparedStatement.executeQuery();
    if (localResultSet.next())
      d1 = localResultSet.getDouble("amount");
    localPreparedStatement.close();
    Date localDate2 = new Date();
    localCalendar.setTime(paramDate1);
    localDate1 = localCalendar.getTime();
    double d4 = d1;
    double d5 = d2;
    while (localDate1.before(paramDate2))
    {
      localTransactionReportDayModel = (TransactionReportDayModel)localHashtable.get(localSimpleDateFormat.format(localDate1));
      if (localDate1.after(localDate2))
        d5 += localTransactionReportDayModel.getPtojectedTransaction();
      else
        d5 += localTransactionReportDayModel.getActualTransaction();
      localTransactionReportDayModel.setActualBalance(d5);
      d4 += localTransactionReportDayModel.getPtojectedTransaction();
      localTransactionReportDayModel.setPtojectedBalance(d4);
      localCalendar.add(5, 1);
      localDate1 = localCalendar.getTime();
    }
    localTransactionReportUserModel.setDays(localHashtable);
    str1 = "SELECT SUM(d.transaction_type*d.amount) amount FROM transaction_detail@OMS_LINK d, transaction@OMS_LINK t WHERE t.id = d.transaction_id AND d.amount <> 0 AND d.account_id in(SELECT id FROM account_group@OMS_LINK CONNECT BY PRIOR id = parent_id START WITH id in( " + paramString1 + ")) ";
    System.out.println("NSQL5>" + str1);
    localResultSet = this.stmt.executeQuery(str1);
    if (localResultSet.next())
      localTransactionReportUserModel.setBalance(localResultSet.getDouble("amount"));
    return localTransactionReportUserModel;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.dao.TransactionDAO
 * JD-Core Version:    0.6.0
 */