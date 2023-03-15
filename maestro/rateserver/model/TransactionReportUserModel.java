package com.maestro.rateserver.model;

import java.util.Hashtable;

public class TransactionReportUserModel
{
  private String userName;
  private String userId;
  private int currency;
  private String accountName;
  private double rate;
  private double cblLocal;
  private double cblUsd;
  private double initPCB;
  private double initACB;
  private String area;
  private String areaName = "";
  private String currencyName = "";
  private double balance;
  private double quantity;
  private Hashtable days;

  public String getUserName()
  {
    return this.userName;
  }

  public void setUserName(String paramString)
  {
    this.userName = paramString;
  }

  public String getUserId()
  {
    return this.userId;
  }

  public void setUserId(String paramString)
  {
    this.userId = paramString;
  }

  public String getArea()
  {
    return this.area;
  }

  public void setArea(String paramString)
  {
    this.area = paramString;
  }

  public double getBalance()
  {
    return this.balance;
  }

  public void setBalance(double paramDouble)
  {
    this.balance = paramDouble;
  }

  public double getQuantity()
  {
    return this.quantity;
  }

  public void setQuantity(double paramDouble)
  {
    this.quantity = paramDouble;
  }

  public Hashtable getDays()
  {
    return this.days;
  }

  public void setDays(Hashtable paramHashtable)
  {
    this.days = paramHashtable;
  }

  public String getAreaName()
  {
    return this.areaName;
  }

  public void setAreaName(String paramString)
  {
    this.areaName = paramString;
  }

  public double getCblLocal()
  {
    return this.cblLocal;
  }

  public void setCblLocal(double paramDouble)
  {
    this.cblLocal = paramDouble;
  }

  public double getCblUsd()
  {
    return this.cblUsd;
  }

  public void setCblUsd(double paramDouble)
  {
    this.cblUsd = paramDouble;
  }

  public int getCurrency()
  {
    return this.currency;
  }

  public void setCurrency(int paramInt)
  {
    this.currency = paramInt;
  }

  public String getCurrencyName()
  {
    return this.currencyName;
  }

  public void setCurrencyName(String paramString)
  {
    this.currencyName = paramString;
  }

  public double getInitPCB()
  {
    return this.initPCB;
  }

  public void setInitPCB(double paramDouble)
  {
    this.initPCB = paramDouble;
  }

  public double getInitACB()
  {
    return this.initACB;
  }

  public void setInitACB(double paramDouble)
  {
    this.initACB = paramDouble;
  }

  public String getAccountName()
  {
    return this.accountName;
  }

  public void setAccountName(String paramString)
  {
    this.accountName = paramString;
  }

  public void setRate(double paramDouble)
  {
    this.rate = paramDouble;
  }

  public double getRate()
  {
    return this.rate;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.model.TransactionReportUserModel
 * JD-Core Version:    0.6.0
 */