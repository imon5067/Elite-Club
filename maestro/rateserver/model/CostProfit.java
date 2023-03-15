package com.maestro.rateserver.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class CostProfit
  implements Comparable
{
  private int id = -1;
  private String name;
  private String pid = "";
  private double buyRate = 0.0D;
  private double saleRate = 0.0D;
  private double effctSaleRate = 0.0D;
  private double effctBuyRate = 0.0D;
  private double sessMinute = 0.0D;
  private double minute = 0.0D;
  private double outMinute = 0.0D;
  private double cost = 0.0D;
  private double outCost = 0.0D;
  private double revenue = 0.0D;
  private double commission = 0.0D;
  private double profit = 0.0D;
  private Collection collectn = null;
  private Hashtable htable = null;

  public int getId()
  {
    return this.id;
  }

  public CostProfit()
  {
  }

  public CostProfit(int paramInt, String paramString, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    this.id = paramInt;
    this.name = paramString;
    this.cost = paramDouble1;
    this.commission = paramDouble2;
    this.minute = paramDouble3;
  }

  public CostProfit(int paramInt, String paramString, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, Collection paramCollection)
  {
    this.id = paramInt;
    this.name = paramString;
    this.buyRate = paramDouble1;
    this.saleRate = paramDouble2;
    this.effctSaleRate = paramDouble3;
    this.effctBuyRate = paramDouble4;
    this.minute = paramDouble5;
    this.cost = paramDouble6;
    this.commission = paramDouble7;
    this.revenue = paramDouble8;
    this.profit = paramDouble9;
    this.collectn = paramCollection;
  }

  public double getCost()
  {
    return this.cost;
  }

  public void setCost(double paramDouble)
  {
    this.cost = paramDouble;
  }

  public double getOutCost()
  {
    return this.outCost;
  }

  public void setOutCost(double paramDouble)
  {
    this.outCost = paramDouble;
  }

  public double getOutMinute()
  {
    return this.outMinute;
  }

  public void setOutMinute(double paramDouble)
  {
    this.outMinute = paramDouble;
  }

  public double getCommission()
  {
    return this.commission;
  }

  public void setCommission(double paramDouble)
  {
    this.commission = paramDouble;
  }

  public void setId(int paramInt)
  {
    this.id = paramInt;
  }

  public double getBuyRate()
  {
    return this.buyRate;
  }

  public double getSaleRate()
  {
    return this.saleRate;
  }

  public void setEffctSaleRate(double paramDouble)
  {
    this.effctSaleRate = paramDouble;
  }

  public void setEffctBuyRate(double paramDouble)
  {
    this.effctBuyRate = paramDouble;
  }

  public double getEffctSaleRate()
  {
    return this.effctSaleRate;
  }

  public double getEffctBuyRate()
  {
    return this.effctBuyRate;
  }

  public double getProfit()
  {
    return this.profit;
  }

  public void setRate(double paramDouble)
  {
    this.buyRate = paramDouble;
  }

  public double getMinute()
  {
    return this.minute;
  }

  public void setMinute(double paramDouble)
  {
    this.minute = paramDouble;
  }

  public void setSessMinute(double paramDouble)
  {
    this.sessMinute = paramDouble;
  }

  public double getSessMinute()
  {
    return this.sessMinute;
  }

  public void setProfit(double paramDouble)
  {
    this.profit = paramDouble;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public String getName()
  {
    return this.name;
  }

  public void setPid(String paramString)
  {
    this.pid = paramString;
  }

  public String getPid()
  {
    return this.pid;
  }

  public double getRevenue()
  {
    return this.revenue;
  }

  public void setRevenue(double paramDouble)
  {
    this.revenue = paramDouble;
  }

  public void setBuyRate(double paramDouble)
  {
    this.buyRate = paramDouble;
  }

  public void setSaleRate(double paramDouble)
  {
    this.saleRate = paramDouble;
  }

  public void setCollectn(Collection paramCollection)
  {
    this.collectn = paramCollection;
  }

  public Collection getCollectn()
  {
    return this.collectn;
  }

  public Hashtable getHtable()
  {
    return this.htable;
  }

  public void setHtable(Hashtable paramHashtable)
  {
    this.htable = paramHashtable;
  }

  public int compareTo(Object paramObject)
  {
    CostProfit localCostProfit = (CostProfit)paramObject;
    if (getMinute() < localCostProfit.getMinute())
      return -1;
    if (getMinute() == localCostProfit.getMinute())
      return 0;
    return 1;
  }

  public List dummyCostProfit()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new CostProfit(1, "880", 1.5D, 0.51D, 1.0D));
    localArrayList.add(new CostProfit(2, "8801", 1.45D, 0.46D, 1.0D));
    localArrayList.add(new CostProfit(3, "8802", 1.2D, 0.21D, 2.0D));
    return localArrayList;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("CostProfit[");
    localStringBuffer.append("id:");
    localStringBuffer.append(this.id);
    localStringBuffer.append("name:");
    localStringBuffer.append(this.name);
    localStringBuffer.append("minute:");
    localStringBuffer.append(this.minute);
    localStringBuffer.append("cost:");
    localStringBuffer.append(this.cost);
    localStringBuffer.append("OUTminute:");
    localStringBuffer.append(this.outMinute);
    localStringBuffer.append("OUTcost:");
    localStringBuffer.append(this.outCost);
    localStringBuffer.append("comm:");
    localStringBuffer.append(this.commission);
    localStringBuffer.append("revenue:");
    localStringBuffer.append(this.revenue);
    localStringBuffer.append("profit:");
    localStringBuffer.append(this.profit);
    localStringBuffer.append("buyRate:");
    localStringBuffer.append(this.buyRate);
    localStringBuffer.append("saleRate:");
    localStringBuffer.append(this.saleRate);
    localStringBuffer.append("EffctSaleRate:");
    localStringBuffer.append(this.effctSaleRate);
    localStringBuffer.append("EffctBuyRate:");
    localStringBuffer.append(this.effctBuyRate);
    localStringBuffer.append("]\n");
    return localStringBuffer.toString();
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.model.CostProfit
 * JD-Core Version:    0.6.0
 */