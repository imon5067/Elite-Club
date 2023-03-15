package com.maestro.rateserver.model;

public class Schedule
{
  private String userId;
  private String oppositeUserId;
  private String userName;
  private String paymentReceiveDate;
  private String dueDate;
  private String paymentOrReceive;
  private String currencyId;
  private String description;
  private String oppositeUserCurrencyId;
  private String oppositeUserCurrencyName;
  private String currencyName;
  private double unitPrice;
  private double oppositeUnitPrice;
  private double amount;
  private double quantity;
  private double oppositeQuantity;
  private int accountId;
  private String cashBankAcc;
  private String cashBankAccName;
  private int paymentOrReceiveTransType;
  private int cashBankTransType;
  private int priority = 0;
  private String transactionId = "";
  private String areaId = "";
  private String oppositeAreaId = "";
  private String schedulecategory = "";

  public String getUserId()
  {
    return this.userId;
  }

  public void setUserId(String paramString)
  {
    this.userId = paramString;
  }

  public String getPaymentReceiveDate()
  {
    return this.paymentReceiveDate;
  }

  public void setPaymentReceiveDate(String paramString)
  {
    this.paymentReceiveDate = paramString;
  }

  public String getPaymentOrReceive()
  {
    return this.paymentOrReceive;
  }

  public void setPaymentOrReceive(String paramString)
  {
    this.paymentOrReceive = paramString;
  }

  public double getAmount()
  {
    return this.amount;
  }

  public void setAmount(double paramDouble)
  {
    this.amount = paramDouble;
  }

  public String getUserName()
  {
    return this.userName;
  }

  public void setUserName(String paramString)
  {
    this.userName = paramString;
  }

  public String getCashBankAcc()
  {
    return this.cashBankAcc;
  }

  public void setCashBankAcc(String paramString)
  {
    this.cashBankAcc = paramString;
  }

  public String getCashBankAccName()
  {
    return this.cashBankAccName;
  }

  public void setCashBankAccName(String paramString)
  {
    this.cashBankAccName = paramString;
  }

  public String getDueDate()
  {
    return this.dueDate;
  }

  public void setDueDate(String paramString)
  {
    this.dueDate = paramString;
  }

  public String getCurrencyId()
  {
    return this.currencyId;
  }

  public void setCurrencyId(String paramString)
  {
    this.currencyId = paramString;
  }

  public String getCurrencyName()
  {
    return this.currencyName;
  }

  public void setCurrencyName(String paramString)
  {
    this.currencyName = paramString;
  }

  public double getUnitPrice()
  {
    return this.unitPrice;
  }

  public void setUnitPrice(double paramDouble)
  {
    this.unitPrice = paramDouble;
  }

  public int getPaymentOrReceiveTransType()
  {
    return this.paymentOrReceiveTransType;
  }

  public void setPaymentOrReceiveTransType(int paramInt)
  {
    this.paymentOrReceiveTransType = paramInt;
  }

  public int getCashBankTransType()
  {
    return this.cashBankTransType;
  }

  public void setCashBankTransType(int paramInt)
  {
    this.cashBankTransType = paramInt;
  }

  public String getOppositeUserId()
  {
    return this.oppositeUserId;
  }

  public void setOppositeUserId(String paramString)
  {
    this.oppositeUserId = paramString;
  }

  public int getPriority()
  {
    return this.priority;
  }

  public void setPriority(int paramInt)
  {
    this.priority = paramInt;
  }

  public String getTransactionId()
  {
    return this.transactionId;
  }

  public void setTransactionId(String paramString)
  {
    this.transactionId = paramString;
  }

  public int getAccountId()
  {
    return this.accountId;
  }

  public void setAccountId(int paramInt)
  {
    this.accountId = paramInt;
  }

  public String getOppositeUserCurrencyId()
  {
    return this.oppositeUserCurrencyId;
  }

  public void setOppositeUserCurrencyId(String paramString)
  {
    this.oppositeUserCurrencyId = paramString;
  }

  public double getOppositeUnitPrice()
  {
    return this.oppositeUnitPrice;
  }

  public void setOppositeUnitPrice(double paramDouble)
  {
    this.oppositeUnitPrice = paramDouble;
  }

  public double getQuantity()
  {
    return this.quantity;
  }

  public void setQuantity(double paramDouble)
  {
    this.quantity = paramDouble;
  }

  public double getOppositeQuantity()
  {
    return this.oppositeQuantity;
  }

  public void setOppositeQuantity(double paramDouble)
  {
    this.oppositeQuantity = paramDouble;
  }

  public String getAreaId()
  {
    return this.areaId;
  }

  public void setAreaId(String paramString)
  {
    this.areaId = paramString;
  }

  public String getOppositeAreaId()
  {
    return this.oppositeAreaId;
  }

  public void setOppositeAreaId(String paramString)
  {
    this.oppositeAreaId = paramString;
  }

  public String getOppositeUserCurrencyName()
  {
    return this.oppositeUserCurrencyName;
  }

  public void setOppositeUserCurrencyName(String paramString)
  {
    this.oppositeUserCurrencyName = paramString;
  }

  public String getDescription()
  {
    return this.description;
  }

  public void setDescription(String paramString)
  {
    this.description = paramString;
  }

  public String getSchedulecategory()
  {
    return this.schedulecategory;
  }

  public void setSchedulecategory(String paramString)
  {
    this.schedulecategory = paramString;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.model.Schedule
 * JD-Core Version:    0.6.0
 */