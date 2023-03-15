package com.maestro.rateserver.model;

import java.util.Date;

public class BalanceSheet
{
  private int id = 0;
  private String hierarchy = "";
  private String name = "";
  private int level = 0;
  private double USDBalance = 0.0D;
  private double localBalance = 0.0D;
  private Date lastTransaction = null;
  private double date1Balance = 0.0D;
  private double date2Balance = 0.0D;
  private double changes = 0.0D;
  private boolean accountMode = true;
  private String currency = null;

  public String getCurrency()
  {
    return this.currency;
  }

  public void setCurrency(String paramString)
  {
    this.currency = paramString;
  }

  public String getHierarchy()
  {
    return this.hierarchy;
  }

  public void setHierarchy(String paramString)
  {
    this.hierarchy = paramString;
  }

  public int getId()
  {
    return this.id;
  }

  public void setId(int paramInt)
  {
    this.id = paramInt;
  }

  public boolean isAccountMode()
  {
    return this.accountMode;
  }

  public void setAccountMode(boolean paramBoolean)
  {
    this.accountMode = paramBoolean;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public int getLevel()
  {
    return this.level;
  }

  public void setLevel(int paramInt)
  {
    this.level = paramInt;
  }

  public double getUSDBalance()
  {
    return this.USDBalance;
  }

  public void setUSDBalance(double paramDouble)
  {
    this.USDBalance = paramDouble;
  }

  public double getLocalBalance()
  {
    return this.localBalance;
  }

  public void setLocalBalance(double paramDouble)
  {
    this.localBalance = paramDouble;
  }

  public Date getLastTransaction()
  {
    return this.lastTransaction;
  }

  public void setLastTransaction(Date paramDate)
  {
    this.lastTransaction = paramDate;
  }

  public double getDate1Balance()
  {
    return this.date1Balance;
  }

  public void setDate1Balance(double paramDouble)
  {
    this.date1Balance = paramDouble;
  }

  public double getDate2Balance()
  {
    return this.date2Balance;
  }

  public void setDate2Balance(double paramDouble)
  {
    this.date2Balance = paramDouble;
  }

  public double getChanges()
  {
    return this.changes;
  }

  public void setChanges(double paramDouble)
  {
    this.changes = paramDouble;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.model.BalanceSheet
 * JD-Core Version:    0.6.0
 */