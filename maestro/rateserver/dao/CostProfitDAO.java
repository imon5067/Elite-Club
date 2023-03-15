package com.maestro.rateserver.dao;

import com.maestro.crb.orig_billing.info.Inf;
import com.maestro.rateserver.model.CostProfit;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class CostProfitDAO
{
  String ignore_ws_user = "14,263,251,161,266,244,100";
  Statement stmt = null;

  public CostProfitDAO(Statement paramStatement)
  {
    this.stmt = paramStatement;
  }

  public List getUsageByCountryCodePID(String paramString1, String paramString2, String paramString3, Statement paramStatement, String paramString4, String paramString5, String paramString6)
    throws Exception
  {
    return getUsageByCountryCodePID(paramString1, paramString2, paramString3, paramStatement, paramString4, paramString5, paramString6, "");
  }

  public List getUsageByCountryCodePID(String paramString1, String paramString2, String paramString3, Statement paramStatement, String paramString4, String paramString5, String paramString6, String paramString7)
    throws Exception
  {
    ArrayList localArrayList1 = null;
    ArrayList localArrayList2 = null;
    Hashtable localHashtable1 = null;
    Hashtable localHashtable2 = null;
    CostProfit localCostProfit1 = new CostProfit();
    CostProfit localCostProfit2 = new CostProfit();
    CostProfit localCostProfit3 = null;
    String str1 = "";
    ResultSet localResultSet = null;
    String str2 = paramString6;
    if ((paramString6 == null) || (paramString6.length() < 1))
    {
      paramString6 = "dest_code";
      str2 = "orig_code";
    }
    if ((paramString5 != null) && (paramString5.length() > 0) && (paramString4 != null) && (paramString4.length() > 0))
    {
      if ((paramString3 != null) && (paramString3.length() > 0))
      {
        str1 = "select uc.user_id as uc_user_id,uc.user_name as station_name,pc.pack_id as pack_id,pc.pack_name as pack_name,oh." + paramString6 + " as dest,sum(oh.SESSIONTIME) as SESSIONTIME,sum(oh.billabletime) billabletime,sum(oh.INCOST) incost,sum(oh.COMMISSION) commission" + " ,sum(oh.pkg_rate*oh.billabletime) pkg_rate from orig_history oh,package_config pc,user_config uc where uc.user_id=" + paramString4 + " and oh.pid='" + paramString5 + "' and oh.pack_id=pc.pack_id and country='" + paramString3 + "'  and pc.user_id>0" + " and pc.user_id=uc.user_id and cdate between to_date('" + paramString1 + " 00:00:00','" + Inf.getDateFormat() + " hh24:mi:ss') and to_date('" + paramString2 + " 23:59:59','" + Inf.getDateFormat() + " hh24:mi:ss')";
        if (paramString7.length() > 0)
          str1 = str1 + " and oh.pack_id=" + paramString7;
        str1 = str1 + " group by uc.user_id,uc.user_name,pc.pack_id,pc.pack_name,oh." + paramString6 + " order by uc.user_name,uc.user_id,pc.pack_name,billabletime desc,oh." + paramString6;
      }
      else
      {
        str1 = "select uc.user_id as uc_user_id,uc.user_name as station_name,pc.pack_id as pack_id,pc.pack_name as pack_name,oh.country as dest,sum(oh.SESSIONTIME) as SESSIONTIME,sum(oh.billabletime) billabletime,sum(oh.INCOST) incost,sum(oh.COMMISSION) commission ,sum(oh.pkg_rate*oh.billabletime) pkg_rate from orig_history oh,package_config pc,user_config uc where uc.user_id=" + paramString4 + " and oh.pid='" + paramString5 + "' and oh.pack_id=pc.pack_id and pc.user_id>0" + " and pc.user_id=uc.user_id and cdate between to_date('" + paramString1 + " 00:00:00','" + Inf.getDateFormat() + " hh24:mi:ss') and to_date('" + paramString2 + " 23:59:59','" + Inf.getDateFormat() + " hh24:mi:ss')";
        if (paramString7.length() > 0)
          str1 = str1 + " and oh.pack_id=" + paramString7;
        str1 = str1 + " group by uc.user_id,uc.user_name,pc.pack_id,pc.pack_name,oh.country order by uc.user_name,uc.user_id,pc.pack_name,billabletime desc,oh.country";
      }
    }
    else if ((paramString4 != null) && (paramString4.length() > 0))
    {
      if ((paramString3 != null) && (paramString3.length() > 0))
      {
        str1 = "select uc.user_id as uc_user_id,uc.user_name as station_name,pc.pack_id as pack_id,pc.pack_name as pack_name,oh." + paramString6 + " as dest,sum(oh.SESSIONTIME) as SESSIONTIME,sum(oh.billabletime) billabletime,sum(oh.INCOST) incost,sum(oh.COMMISSION) commission" + " ,sum(oh.pkg_rate*oh.billabletime) pkg_rate from orig_history oh,package_config pc,user_config uc where uc.user_id=" + paramString4 + " and oh.pack_id=pc.pack_id and country='" + paramString3 + "'  and pc.user_id>0" + " and pc.user_id=uc.user_id and cdate between to_date('" + paramString1 + " 00:00:00','" + Inf.getDateFormat() + " hh24:mi:ss') and to_date('" + paramString2 + " 23:59:59','" + Inf.getDateFormat() + " hh24:mi:ss')";
        if (paramString7.length() > 0)
          str1 = str1 + " and oh.pack_id=" + paramString7;
        str1 = str1 + " group by uc.user_id,uc.user_name,pc.pack_id,pc.pack_name,oh." + paramString6 + " order by uc.user_name,uc.user_id,pc.pack_name,billabletime desc,oh." + paramString6;
      }
      else
      {
        str1 = "select uc.user_id as uc_user_id,uc.user_name as station_name,pc.pack_id as pack_id,pc.pack_name as pack_name,oh.country as dest,sum(oh.SESSIONTIME) as SESSIONTIME,sum(oh.billabletime) billabletime,sum(oh.INCOST) incost,sum(oh.COMMISSION) commission ,sum(oh.pkg_rate*oh.billabletime) pkg_rate from orig_history oh,package_config pc,user_config uc where uc.user_id=" + paramString4 + " and oh.pack_id=pc.pack_id and pc.user_id>0" + " and pc.user_id=uc.user_id and cdate between to_date('" + paramString1 + " 00:00:00','" + Inf.getDateFormat() + " hh24:mi:ss') and to_date('" + paramString2 + " 23:59:59','" + Inf.getDateFormat() + " hh24:mi:ss')";
        if (paramString7.length() > 0)
          str1 = str1 + " and oh.pack_id=" + paramString7;
        str1 = str1 + " group by uc.user_id,uc.user_name,pc.pack_id,pc.pack_name,oh.country order by uc.user_name,uc.user_id,pc.pack_name,billabletime desc,oh.country";
      }
    }
    else if ((paramString3 != null) && (paramString3.length() > 0))
    {
      str1 = "select uc.user_id as uc_user_id,uc.user_name as station_name,pc.pack_id as pack_id,pc.pack_name as pack_name,oh." + paramString6 + " as dest,sum(oh.SESSIONTIME) as SESSIONTIME,sum(oh.billabletime) billabletime,sum(oh.INCOST) incost,sum(oh.COMMISSION) commission" + " ,sum(oh.pkg_rate*oh.billabletime) pkg_rate from orig_history oh,package_config pc,user_config uc where oh.pack_id=pc.pack_id and country='" + paramString3 + "'  and pc.user_id>0" + " and pc.user_id=uc.user_id and cdate between to_date('" + paramString1 + " 00:00:00','" + Inf.getDateFormat() + " hh24:mi:ss') and to_date('" + paramString2 + " 23:59:59','" + Inf.getDateFormat() + " hh24:mi:ss')";
      if (paramString7.length() > 0)
        str1 = str1 + " and oh.pack_id=" + paramString7;
      str1 = str1 + " group by uc.user_id,uc.user_name,pc.pack_id,pc.pack_name,oh." + paramString6 + " order by uc.user_name,uc.user_id,pc.pack_name,billabletime desc,oh." + paramString6;
    }
    else
    {
      str1 = "select uc.user_id as uc_user_id,uc.user_name as station_name,pc.pack_id as pack_id,pc.pack_name as pack_name,oh.country as dest,sum(oh.SESSIONTIME) as SESSIONTIME,sum(oh.billabletime) billabletime,sum(oh.INCOST) incost,sum(oh.COMMISSION) commission ,sum(oh.pkg_rate*oh.billabletime) pkg_rate from orig_history oh,package_config pc,user_config uc where oh.pack_id=pc.pack_id and pc.user_id>0 and pc.user_id=uc.user_id and cdate between to_date('" + paramString1 + " 00:00:00','" + Inf.getDateFormat() + " hh24:mi:ss') and to_date('" + paramString2 + " 23:59:59','" + Inf.getDateFormat() + " hh24:mi:ss')";
      if (paramString7.length() > 0)
        str1 = str1 + " and oh.pack_id=" + paramString7;
      str1 = str1 + " group by uc.user_id,uc.user_name,pc.pack_id,pc.pack_name,oh.country order by uc.user_name,uc.user_id,pc.pack_name,billabletime desc,oh.country";
    }
    localResultSet = this.stmt.executeQuery(str1);
    localArrayList2 = new ArrayList();
    localHashtable1 = new Hashtable();
    localHashtable2 = new Hashtable();
    localArrayList1 = new ArrayList();
    CostProfit localCostProfit4 = null;
    Hashtable localHashtable3 = null;
    double[] arrayOfDouble = { 0.0D, 0.0D };
    while (localResultSet.next())
    {
      if (localCostProfit2.getId() != localResultSet.getInt("pack_id"))
      {
        localHashtable3 = getOutCost(paramString1, paramString2, localResultSet.getString("pack_id"), paramString3, paramStatement, paramString5, str2);
        if (localCostProfit2.getId() > 0)
        {
          localCostProfit2.setRevenue(localCostProfit2.getCost() - localCostProfit2.getCommission());
          if (localCostProfit2.getSessMinute() > 0.0D)
            localCostProfit2.setEffctSaleRate(localCostProfit2.getRevenue() / localCostProfit2.getSessMinute());
          if (localCostProfit2.getOutMinute() > 0.0D)
            localCostProfit2.setEffctBuyRate(localCostProfit2.getOutCost() / localCostProfit2.getOutMinute());
          localCostProfit2.setProfit(localCostProfit2.getRevenue() - localCostProfit2.getOutCost());
          localCostProfit2.setHtable(localHashtable1);
          localArrayList2.add(localCostProfit2);
          localCostProfit2 = new CostProfit();
          localHashtable1 = new Hashtable();
        }
        localCostProfit2.setId(localResultSet.getInt("pack_id"));
        localCostProfit2.setName(localResultSet.getString("pack_name"));
      }
      if (localCostProfit1.getId() != localResultSet.getInt("uc_user_id"))
      {
        if (localCostProfit1.getId() > 0)
        {
          Collections.sort(localArrayList2);
          localCostProfit1.setCollectn(localArrayList2);
          localCostProfit1.setHtable(localHashtable2);
          localCostProfit1.setRevenue(localCostProfit1.getCost() - localCostProfit1.getCommission());
          if (localCostProfit1.getSessMinute() > 0.0D)
            localCostProfit1.setEffctSaleRate(localCostProfit1.getRevenue() / localCostProfit1.getSessMinute());
          if (localCostProfit1.getOutMinute() > 0.0D)
            localCostProfit1.setEffctBuyRate(localCostProfit1.getOutCost() / localCostProfit1.getOutMinute());
          localCostProfit1.setProfit(localCostProfit1.getRevenue() - localCostProfit1.getOutCost());
          localArrayList1.add(localCostProfit1);
          localCostProfit1 = new CostProfit();
          localArrayList2 = new ArrayList();
          localHashtable2 = new Hashtable();
        }
        localCostProfit1.setId(localResultSet.getInt("uc_user_id"));
        localCostProfit1.setName(localResultSet.getString("station_name"));
      }
      localCostProfit3 = new CostProfit();
      localCostProfit3.setName(localResultSet.getString("dest"));
      localCostProfit3.setMinute(localResultSet.getDouble("billabletime"));
      localCostProfit3.setSessMinute(localResultSet.getDouble("SESSIONTIME"));
      localCostProfit3.setCost(localResultSet.getDouble("incost"));
      localCostProfit3.setCommission(localResultSet.getDouble("commission"));
      localCostProfit3.setRevenue(localResultSet.getDouble("incost") - localResultSet.getDouble("commission"));
      if (localCostProfit3.getSessMinute() > 0.0D)
      {
        localCostProfit3.setEffctSaleRate(localCostProfit3.getRevenue() / localCostProfit3.getSessMinute());
        localCostProfit3.setSaleRate(localResultSet.getDouble("pkg_rate") / localResultSet.getDouble("billabletime"));
      }
      if ((localHashtable3 != null) && (localHashtable3.containsKey(localResultSet.getString("dest"))))
        try
        {
          arrayOfDouble = (double[])localHashtable3.get(localResultSet.getString("dest"));
          localCostProfit3.setOutCost(arrayOfDouble[0]);
          localCostProfit3.setOutMinute(arrayOfDouble[1]);
          if (localCostProfit3.getOutMinute() > 0.0D)
            localCostProfit3.setEffctBuyRate(localCostProfit3.getOutCost() / localCostProfit3.getOutMinute());
          localCostProfit2.setOutCost(localCostProfit2.getOutCost() + localCostProfit3.getOutCost());
          localCostProfit2.setOutMinute(localCostProfit2.getOutMinute() + localCostProfit3.getOutMinute());
          localCostProfit1.setOutCost(localCostProfit1.getOutCost() + localCostProfit3.getOutCost());
          localCostProfit1.setOutMinute(localCostProfit1.getOutMinute() + localCostProfit3.getOutMinute());
        }
        catch (Exception localException)
        {
          System.out.println("Failed to get outcost for pack_id:" + localResultSet.getString("pack_id") + " code:" + localCostProfit3.getName());
        }
      localCostProfit3.setProfit(localCostProfit3.getRevenue() - localCostProfit3.getOutCost());
      localHashtable1.put(localCostProfit3.getName(), localCostProfit3);
      localCostProfit2.setMinute(localCostProfit2.getMinute() + localResultSet.getDouble("billabletime"));
      localCostProfit2.setSessMinute(localCostProfit2.getSessMinute() + localResultSet.getDouble("SESSIONTIME"));
      localCostProfit2.setCost(localCostProfit2.getCost() + localResultSet.getDouble("incost"));
      localCostProfit2.setCommission(localCostProfit2.getCommission() + localResultSet.getDouble("commission"));
      localCostProfit1.setMinute(localCostProfit1.getMinute() + localResultSet.getDouble("billabletime"));
      localCostProfit1.setSessMinute(localCostProfit1.getSessMinute() + localResultSet.getDouble("SESSIONTIME"));
      localCostProfit1.setCommission(localCostProfit1.getCommission() + localResultSet.getDouble("commission"));
      localCostProfit1.setCost(localCostProfit1.getCost() + localResultSet.getDouble("incost"));
      if (localHashtable2.containsKey(localCostProfit3.getName()))
      {
        localCostProfit4 = (CostProfit)localHashtable2.get(localCostProfit3.getName());
      }
      else
      {
        localCostProfit4 = new CostProfit();
        localCostProfit4.setId(localResultSet.getInt("uc_user_id"));
        localCostProfit4.setName(localResultSet.getString("station_name"));
      }
      localCostProfit4.setMinute(localCostProfit4.getMinute() + localResultSet.getDouble("billabletime"));
      localCostProfit4.setSessMinute(localCostProfit4.getSessMinute() + localResultSet.getDouble("SESSIONTIME"));
      localCostProfit4.setCommission(localCostProfit4.getCommission() + localResultSet.getDouble("commission"));
      localCostProfit4.setCost(localCostProfit4.getCost() + localResultSet.getDouble("incost"));
      localCostProfit4.setRevenue(localCostProfit4.getCost() - localCostProfit4.getCommission());
      localCostProfit4.setOutMinute(localCostProfit4.getOutMinute() + localCostProfit3.getOutMinute());
      localCostProfit4.setOutCost(localCostProfit4.getOutCost() + localCostProfit3.getOutCost());
      localCostProfit4.setRevenue(localCostProfit4.getCost() - localCostProfit4.getCommission());
      if (localCostProfit4.getSessMinute() > 0.0D)
        localCostProfit4.setEffctSaleRate(localCostProfit4.getRevenue() / localCostProfit4.getSessMinute());
      if (localCostProfit4.getOutMinute() > 0.0D)
        localCostProfit4.setEffctBuyRate(localCostProfit4.getOutCost() / localCostProfit4.getOutMinute());
      localCostProfit4.setProfit(localCostProfit4.getRevenue() - localCostProfit4.getOutCost());
      localHashtable2.put(localCostProfit3.getName(), localCostProfit4);
    }
    if (localCostProfit1.getId() > 0)
    {
      localCostProfit2.setHtable(localHashtable1);
      localCostProfit2.setRevenue(localCostProfit2.getCost() - localCostProfit2.getCommission());
      if (localCostProfit2.getSessMinute() > 0.0D)
        localCostProfit2.setEffctSaleRate(localCostProfit2.getRevenue() / localCostProfit2.getSessMinute());
      if (localCostProfit2.getOutMinute() > 0.0D)
        localCostProfit2.setEffctBuyRate(localCostProfit2.getOutCost() / localCostProfit2.getOutMinute());
      localCostProfit2.setProfit(localCostProfit2.getRevenue() - localCostProfit2.getOutCost());
      localCostProfit2.setHtable(localHashtable1);
      localArrayList2.add(localCostProfit2);
      localCostProfit2.setHtable(localHashtable1);
      localCostProfit1.setRevenue(localCostProfit1.getCost() - localCostProfit1.getCommission());
      if (localCostProfit1.getSessMinute() > 0.0D)
        localCostProfit1.setEffctSaleRate(localCostProfit1.getRevenue() / localCostProfit1.getSessMinute());
      if (localCostProfit1.getOutMinute() > 0.0D)
        localCostProfit1.setEffctBuyRate(localCostProfit1.getOutCost() / localCostProfit1.getOutMinute());
      localCostProfit1.setProfit(localCostProfit1.getRevenue() - localCostProfit1.getOutCost());
      localCostProfit1.setHtable(localHashtable2);
      localCostProfit1.setCollectn(localArrayList2);
      localArrayList1.add(localCostProfit1);
      Collections.sort(localArrayList1);
    }
    if (((paramString5 == null) || (paramString5.length() <= 0)) && ((paramString4 == null) || (paramString4.length() <= 0)))
    {
      localCostProfit1 = getWSUsageByCountryCode(paramString1, paramString2, paramString3, paramString6);
      localArrayList1.add(localCostProfit1);
    }
    return localArrayList1;
  }

  public CostProfit getWSUsageByCountryCode(String paramString1, String paramString2, String paramString3, String paramString4)
    throws Exception
  {
    ArrayList localArrayList = null;
    Hashtable localHashtable1 = null;
    CostProfit localCostProfit1 = new CostProfit();
    CostProfit localCostProfit2 = null;
    CostProfit localCostProfit3 = new CostProfit();
    String str = "";
    if ((paramString4 == null) || (paramString4.length() < 1))
      paramString4 = "dest_code";
    if ((paramString3 != null) && (paramString3.length() > 0))
      str = "select pc.pack_id as pack_id,pc.pack_name as pack_name,cs." + paramString4 + " as dest,sum(cs.in_sessiontime)/60 in_sessiontime,sum(cs.in_billabletime)/60 billabletime,sum(cs.IN_call_COST) incost,sum(out_call_cost) as outcost," + " sum(out_billabletime)/60 as outbillabletime from CDR_PREFIX_SUMMARY cs,package_config pc where pc.in_user_id not in (" + this.ignore_ws_user + ") and cs.in_user_id=pc.in_user_id and cs.in_user_id>0 and pc.in_user_id is not null and" + " cs.country='" + paramString3 + "' and cs.cdate between to_date('" + paramString1 + " 00:00:00','" + Inf.getDateFormat() + " hh24:mi:ss') and to_date('" + paramString2 + " 23:59:59','" + Inf.getDateFormat() + " hh24:mi:ss')" + " and cs.out_billabletime>0 group by pc.pack_id,pc.pack_name,cs." + paramString4 + " order by pc.pack_name,billabletime desc,cs." + paramString4;
    else
      str = "select pc.pack_id as pack_id,pc.pack_name as pack_name,cs.country as dest,sum(cs.in_sessiontime)/60 in_sessiontime,sum(cs.in_billabletime)/60 billabletime,sum(cs.IN_call_COST) incost,sum(out_call_cost) as outcost, sum(out_billabletime)/60 as outbillabletime from CDR_PREFIX_SUMMARY cs,package_config pc,in_billing bl where pc.in_user_id not in (" + this.ignore_ws_user + ") and cs.in_user_id=pc.in_user_id and cs.in_user_id>0 and  pc.in_user_id is not null and" + " and cs.cdate between to_date('" + paramString1 + " 00:00:00','" + Inf.getDateFormat() + " hh24:mi:ss') and to_date('" + paramString2 + " 23:59:59','" + Inf.getDateFormat() + " hh24:mi:ss')" + " and cs.out_billabletime>0 group by pc.pack_id,pc.pack_name,cs.country order by pc.pack_name,billabletime desc,cs.country";
    System.out.println("getWSUsageByCountryCode:\n" + str);
    ResultSet localResultSet = null;
    localResultSet = this.stmt.executeQuery(str);
    Hashtable localHashtable2 = new Hashtable();
    localArrayList = new ArrayList();
    localCostProfit1.setId(-9);
    localCostProfit1.setName("WholeSale");
    localHashtable1 = new Hashtable();
    CostProfit localCostProfit4 = null;
    while (localResultSet.next())
    {
      if (localCostProfit3.getId() != localResultSet.getInt("pack_id"))
      {
        if (localCostProfit3.getId() > 0)
        {
          localArrayList.add(localCostProfit3);
          localCostProfit3.setRevenue(localCostProfit3.getCost());
          localCostProfit3.setEffctSaleRate(localCostProfit3.getRevenue() / localCostProfit3.getSessMinute());
          localCostProfit3.setEffctBuyRate(localCostProfit3.getOutCost() / localCostProfit3.getOutMinute());
          localCostProfit3.setProfit(localCostProfit3.getRevenue() - localCostProfit3.getOutCost());
          localCostProfit3.setHtable(localHashtable2);
          localCostProfit3 = new CostProfit();
          localHashtable2 = new Hashtable();
        }
        localCostProfit3.setId(localResultSet.getInt("pack_id"));
        localCostProfit3.setName(localResultSet.getString("pack_name"));
      }
      localCostProfit2 = new CostProfit();
      localCostProfit2.setName(localResultSet.getString("dest"));
      localCostProfit2.setSessMinute(localResultSet.getDouble("in_sessiontime"));
      localCostProfit2.setMinute(localResultSet.getDouble("billabletime"));
      localCostProfit2.setOutMinute(localResultSet.getDouble("outbillabletime"));
      localCostProfit2.setCost(localResultSet.getDouble("incost"));
      localCostProfit2.setOutCost(localResultSet.getDouble("outcost"));
      localCostProfit2.setEffctSaleRate(localCostProfit2.getCost() / localCostProfit2.getSessMinute());
      localCostProfit2.setSaleRate(localCostProfit2.getCost() / localCostProfit2.getMinute());
      localCostProfit2.setEffctBuyRate(localResultSet.getDouble("outcost") / localCostProfit2.getOutMinute());
      localCostProfit2.setRevenue(localCostProfit2.getCost());
      localCostProfit2.setProfit(localCostProfit2.getRevenue() - localCostProfit2.getOutCost());
      localHashtable2.put(localCostProfit2.getName(), localCostProfit2);
      localCostProfit3.setMinute(localCostProfit3.getMinute() + localResultSet.getDouble("billabletime"));
      localCostProfit3.setSessMinute(localCostProfit3.getSessMinute() + localResultSet.getDouble("in_sessiontime"));
      localCostProfit3.setOutMinute(localCostProfit3.getOutMinute() + localResultSet.getDouble("outbillabletime"));
      localCostProfit3.setCost(localCostProfit3.getCost() + localResultSet.getDouble("incost"));
      localCostProfit3.setOutCost(localCostProfit3.getOutCost() + localResultSet.getDouble("outcost"));
      localCostProfit1.setMinute(localCostProfit1.getMinute() + localResultSet.getDouble("billabletime"));
      localCostProfit1.setSessMinute(localCostProfit1.getSessMinute() + localResultSet.getDouble("in_sessiontime"));
      localCostProfit1.setOutMinute(localCostProfit1.getOutMinute() + localResultSet.getDouble("outbillabletime"));
      localCostProfit1.setCost(localCostProfit1.getCost() + localResultSet.getDouble("incost"));
      localCostProfit1.setOutCost(localCostProfit1.getOutCost() + localResultSet.getDouble("outcost"));
      if (localHashtable1.containsKey(localCostProfit2.getName()))
      {
        localCostProfit4 = (CostProfit)localHashtable1.get(localCostProfit2.getName());
        localCostProfit4.setMinute(localCostProfit4.getMinute() + localResultSet.getDouble("billabletime"));
        localCostProfit4.setSessMinute(localCostProfit4.getSessMinute() + localResultSet.getDouble("in_sessiontime"));
        localCostProfit4.setOutMinute(localCostProfit4.getOutMinute() + localResultSet.getDouble("outbillabletime"));
        localCostProfit4.setCost(localCostProfit4.getCost() + localResultSet.getDouble("incost"));
        localCostProfit4.setOutCost(localCostProfit4.getOutCost() + localResultSet.getDouble("outCost"));
      }
      else
      {
        localCostProfit4 = new CostProfit();
        localCostProfit1.setId(-9);
        localCostProfit1.setName("WholeSale");
        localCostProfit4.setMinute(localResultSet.getDouble("billabletime"));
        localCostProfit4.setSessMinute(localResultSet.getDouble("in_sessiontime"));
        localCostProfit4.setOutMinute(localResultSet.getDouble("outbillabletime"));
        localCostProfit4.setCost(localResultSet.getDouble("incost"));
        localCostProfit4.setOutCost(localResultSet.getDouble("outcost"));
      }
      localCostProfit4.setEffctBuyRate(localCostProfit4.getOutCost() / localCostProfit4.getOutMinute());
      localCostProfit4.setRevenue(localCostProfit4.getCost());
      localCostProfit4.setProfit(localCostProfit4.getRevenue() - localCostProfit4.getOutCost());
      localCostProfit4.setEffctSaleRate(localCostProfit4.getRevenue() / localCostProfit4.getSessMinute());
      localHashtable1.put(localCostProfit2.getName(), localCostProfit4);
    }
    if (localCostProfit3.getId() > 0)
    {
      localCostProfit3.setRevenue(localCostProfit3.getCost());
      localCostProfit3.setEffctSaleRate(localCostProfit3.getRevenue() / localCostProfit3.getSessMinute());
      localCostProfit3.setEffctBuyRate(localCostProfit3.getOutCost() / localCostProfit3.getOutMinute());
      localCostProfit3.setProfit(localCostProfit3.getRevenue() - localCostProfit3.getOutCost());
      localCostProfit3.setHtable(localHashtable2);
      localArrayList.add(localCostProfit3);
      Collections.sort(localArrayList);
    }
    localCostProfit1.setHtable(localHashtable1);
    localCostProfit1.setEffctSaleRate(localCostProfit1.getCost() / localCostProfit1.getSessMinute());
    localCostProfit1.setEffctBuyRate(localCostProfit1.getOutCost() / localCostProfit1.getOutMinute());
    localCostProfit1.setProfit(localCostProfit1.getCost() - localCostProfit1.getOutCost());
    localCostProfit1.setRevenue(localCostProfit1.getCost());
    localCostProfit1.setCollectn(localArrayList);
    System.out.println("===============================\nstationCostProfit::" + localCostProfit1.toString());
    return localCostProfit1;
  }

  public Hashtable getOutCost(String paramString1, String paramString2, String paramString3, String paramString4, Statement paramStatement, String paramString5, String paramString6)
    throws Exception
  {
    Hashtable localHashtable = new Hashtable();
    ResultSet localResultSet = null;
    String str = "";
    if ((paramString6 == null) || (paramString6.length() < 1))
      paramString6 = "orig_code";
    str = "select pack_id from";
    if ((paramString5 != null) && (paramString5.equals("other")))
    {
      if ((paramString4 != null) && (paramString4.length() > 0))
        str = "select " + paramString6 + " as dest,sum(out_call_cost) as out_call_cost,sum(OUT_BILLABLETIME)/60 as OUT_BILLABLETIME from CDR_PREFIX_SUMMARY  where country='" + paramString4 + "'" + " and cdate between to_date('" + paramString1 + " 00:00:00','" + Inf.getDateFormat() + " hh24:mi:ss') and to_date('" + paramString2 + " 23:59:59','" + Inf.getDateFormat() + " hh24:mi:ss') and out_call_cost>0" + " and substr(in_prefix,1,4)in(select prefix from in_prefix where pack_id=" + paramString3 + ") and ( substr(in_prefix,5,6) not in (select distinct PREFIX from COUNTRY_DIRECTOR) or length(in_prefix)<6 )and " + paramString6 + " is not null group by " + paramString6;
      else
        str = "select country as dest,sum(out_call_cost) as out_call_cost,sum(OUT_BILLABLETIME)/60 as OUT_BILLABLETIME from CDR_PREFIX_SUMMARY  where  cdate between to_date('" + paramString1 + " 00:00:00','" + Inf.getDateFormat() + " hh24:mi:ss') and to_date('" + paramString2 + " 23:59:59','" + Inf.getDateFormat() + " hh24:mi:ss') and out_call_cost>0" + " and substr(in_prefix,1,4)in(select prefix from in_prefix where pack_id=" + paramString3 + ") and ( substr(in_prefix,5,6) not in (select distinct PREFIX from COUNTRY_DIRECTOR) or length(in_prefix)<6 ) and country is not null group by country";
    }
    else if ((paramString5 != null) && (paramString5.length() > 0))
    {
      if ((paramString4 != null) && (paramString4.length() > 0))
        str = "select " + paramString6 + " as dest,sum(out_call_cost) as out_call_cost,sum(OUT_BILLABLETIME)/60 as OUT_BILLABLETIME from CDR_PREFIX_SUMMARY  where country='" + paramString4 + "'" + " and cdate between to_date('" + paramString1 + " 00:00:00','" + Inf.getDateFormat() + " hh24:mi:ss') and to_date('" + paramString2 + " 23:59:59','" + Inf.getDateFormat() + " hh24:mi:ss') and out_call_cost>0" + " and substr(in_prefix,1,4)in(select prefix from in_prefix where pack_id=" + paramString3 + ") and substr(in_prefix,5,6)='" + paramString5 + "' and " + paramString6 + " is not null group by " + paramString6;
      else
        str = "select country as dest,sum(out_call_cost) as out_call_cost,sum(OUT_BILLABLETIME)/60 as OUT_BILLABLETIME from CDR_PREFIX_SUMMARY  where  cdate between to_date('" + paramString1 + " 00:00:00','" + Inf.getDateFormat() + " hh24:mi:ss') and to_date('" + paramString2 + " 23:59:59','" + Inf.getDateFormat() + " hh24:mi:ss') and out_call_cost>0" + " and substr(in_prefix,1,4)in(select prefix from in_prefix where pack_id=" + paramString3 + ") and substr(in_prefix,5,6)='" + paramString5 + "' and country is not null group by country";
    }
    else if ((paramString4 != null) && (paramString4.length() > 0))
      str = "select " + paramString6 + " as dest,sum(out_call_cost) as out_call_cost,sum(OUT_BILLABLETIME)/60 as OUT_BILLABLETIME from CDR_PREFIX_SUMMARY  where country='" + paramString4 + "'" + " and cdate between to_date('" + paramString1 + " 00:00:00','" + Inf.getDateFormat() + " hh24:mi:ss') and to_date('" + paramString2 + " 23:59:59','" + Inf.getDateFormat() + " hh24:mi:ss') and out_call_cost>0" + " and substr(in_prefix,1,4)in(select prefix from in_prefix where pack_id=" + paramString3 + ") and " + paramString6 + " is not null group by " + paramString6;
    else
      str = "select country as dest,sum(out_call_cost) as out_call_cost,sum(OUT_BILLABLETIME)/60 as OUT_BILLABLETIME from CDR_PREFIX_SUMMARY  where  cdate between to_date('" + paramString1 + " 00:00:00','" + Inf.getDateFormat() + " hh24:mi:ss') and to_date('" + paramString2 + " 23:59:59','" + Inf.getDateFormat() + " hh24:mi:ss') and out_call_cost>0" + " and substr(in_prefix,1,4)in(select prefix from in_prefix where pack_id=" + paramString3 + ") and country is not null group by country";
    localResultSet = paramStatement.executeQuery(str);
    while (localResultSet.next())
    {
      double[] arrayOfDouble = { 0.0D, 0.0D };
      arrayOfDouble[0] = localResultSet.getDouble("out_call_cost");
      arrayOfDouble[1] = localResultSet.getDouble("OUT_BILLABLETIME");
      localHashtable.put(localResultSet.getString("dest"), arrayOfDouble);
    }
    return localHashtable;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.dao.CostProfitDAO
 * JD-Core Version:    0.6.0
 */