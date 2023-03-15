package com.maestro.rateserver.model;

import java.util.Hashtable;

public class TransactionReportAccountModel
{
  private String accountName;
  private double currentBalance;
  private int areaId;
  private String areaName = "";
  private Hashtable dayHash;
  private double balance;

  public String getAreaName()
  {
    return this.areaName;
  }

  public void setAreaName(String paramString)
  {
    this.areaName = paramString;
  }

  public double getBalance()
  {
    return this.balance;
  }

  public void setBalance(double paramDouble)
  {
    this.balance = paramDouble;
  }

  public void setAreaId(int paramInt)
  {
    this.areaId = paramInt;
  }

  public int getAreaId()
  {
    return this.areaId;
  }

  public void setAccountName(String paramString)
  {
    this.accountName = paramString;
  }

  public String getAccountName()
  {
    return this.accountName;
  }

  public void setCurrentBalance(double paramDouble)
  {
    this.currentBalance = paramDouble;
  }

  public double getCurrentBalance()
  {
    return this.currentBalance;
  }

  public void setDayHash(Hashtable paramHashtable)
  {
    this.dayHash = paramHashtable;
  }

  public Hashtable getDayHash()
  {
    return this.dayHash;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.model.TransactionReportAccountModel
 * JD-Core Version:    0.6.0
 */