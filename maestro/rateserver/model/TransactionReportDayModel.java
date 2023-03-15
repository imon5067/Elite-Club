package com.maestro.rateserver.model;

import java.util.Date;

public class TransactionReportDayModel
{
  private double ptojectedTransaction;
  private double actualTransaction;
  private double ptojectedBalance;
  private double actualBalance;
  private double difference;
  private double dayProfit;
  private Date date;
  private int priority;

  public double getPtojectedTransaction()
  {
    return this.ptojectedTransaction;
  }

  public int getPriority()
  {
    return this.priority;
  }

  public void setPriority(int paramInt)
  {
    this.priority = paramInt;
  }

  public double getDifference()
  {
    return this.difference;
  }

  public void setDifference(double paramDouble)
  {
    this.difference = paramDouble;
  }

  public void setPtojectedTransaction(double paramDouble)
  {
    this.ptojectedTransaction = paramDouble;
  }

  public double getActualTransaction()
  {
    return this.actualTransaction;
  }

  public void setActualTransaction(double paramDouble)
  {
    this.actualTransaction = paramDouble;
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

  public double getDayProfit()
  {
    return this.dayProfit;
  }

  public void setDayProfit(double paramDouble)
  {
    this.dayProfit = paramDouble;
  }

  public Date getDate()
  {
    return this.date;
  }

  public void setDate(Date paramDate)
  {
    this.date = paramDate;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.model.TransactionReportDayModel
 * JD-Core Version:    0.6.0
 */