package com.maestro.rateserver.model;

public class PaymentReceive
{
  private String userId;
  private String userName;
  private String paymentReceiveDate;
  private String dueDate;
  private String paymentOrReceive;
  private String currencyId;
  private String currencyName;
  private double unitPrice;
  private double amount;
  private String cashBankAcc;
  private String cashBankAccName;
  private int paymentOrReceiveTransType;
  private int cashBankTransType;

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
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.model.PaymentReceive
 * JD-Core Version:    0.6.0
 */