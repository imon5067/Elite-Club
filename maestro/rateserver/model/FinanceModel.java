package com.maestro.rateserver.model;

import java.util.Date;
import java.util.Hashtable;

public class FinanceModel
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
  private Date lastTransaction;
  private double actualDebits;
  private double actualCredits;
  private double projectedDebits;
  private double projectedCredits;
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

  public Date getLastTransaction()
  {
    return this.lastTransaction;
  }

  public void setLastTransaction(Date paramDate)
  {
    this.lastTransaction = paramDate;
  }

  public double getActualDebits()
  {
    return this.actualDebits;
  }

  public void setActualDebits(double paramDouble)
  {
    this.actualDebits = paramDouble;
  }

  public double getActualCredits()
  {
    return this.actualCredits;
  }

  public void setActualCredits(double paramDouble)
  {
    this.actualCredits = paramDouble;
  }

  public double getProjectedDebits()
  {
    return this.projectedDebits;
  }

  public void setProjectedDebits(double paramDouble)
  {
    this.projectedDebits = paramDouble;
  }

  public double getProjectedCredits()
  {
    return this.projectedCredits;
  }

  public void setProjectedCredits(double paramDouble)
  {
    this.projectedCredits = paramDouble;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.model.FinanceModel
 * JD-Core Version:    0.6.0
 */