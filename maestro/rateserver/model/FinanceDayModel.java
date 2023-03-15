package com.maestro.rateserver.model;

import java.util.Date;

public class FinanceDayModel
{
  private double ptDebit;
  private double ptCredit;
  private double atDebit;
  private double atCredit;
  private double ptojectedBalance;
  private double actualBalance;
  private Date date;
  private int priority;
  private double shortage;

  public double getShortage()
  {
    return this.shortage;
  }

  public void setShortage(double paramDouble)
  {
    this.shortage = paramDouble;
  }

  public double getPtDebit()
  {
    return this.ptDebit;
  }

  public void setPtDebit(double paramDouble)
  {
    this.ptDebit = paramDouble;
  }

  public double getPtCredit()
  {
    return this.ptCredit;
  }

  public void setPtCredit(double paramDouble)
  {
    this.ptCredit = paramDouble;
  }

  public double getAtDebit()
  {
    return this.atDebit;
  }

  public void setAtDebit(double paramDouble)
  {
    this.atDebit = paramDouble;
  }

  public double getAtCredit()
  {
    return this.atCredit;
  }

  public void setAtCredit(double paramDouble)
  {
    this.atCredit = paramDouble;
  }

  public double getPtojectedBalance()
  {
    return this.ptojectedBalance;
  }

  public void setPtojectedBalance(double paramDouble)
  {
    this.ptojectedBalance = paramDouble;
  }

  public double getActualBalance()
  {
    return this.actualBalance;
  }

  public void setActualBalance(double paramDouble)
  {
    this.actualBalance = paramDouble;
  }

  public Date getDate()
  {
    return this.date;
  }

  public void setDate(Date paramDate)
  {
    this.date = paramDate;
  }

  public int getPriority()
  {
    return this.priority;
  }

  public void setPriority(int paramInt)
  {
    this.priority = paramInt;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.model.FinanceDayModel
 * JD-Core Version:    0.6.0
 */