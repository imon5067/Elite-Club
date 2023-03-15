package com.maestro.rateserver.model;

public class Journal
{
  private String userId;
  private String oppositeUserId;
  private String userName;
  private String oppUserName;
  private String transactionDate;
  private String currencyId;
  private String currencyName;
  private double unitPrice;
  private double amount;
  private int accountId;
  private int editAccountId;
  private String accountName;
  private String cashBankAcc;
  private String cashBankAccName;
  private int CrType;
  private int DrType;
  private int cashBankTransType;
  private int paymentOrReceiveTransType;
  private int priority = 0;
  private String transactionId = "";
  private int CareaId = 0;
  private int DAreaId = 0;
  private int Cruserid = 0;
  private int Druserid = 0;
  private int Caccountid = 0;
  private int Daccountid = 0;
  private double Cquantity = 0.0D;
  private double Dquantity = 0.0D;
  private double Cunitprice = 0.0D;
  private double Dunitprice = 0.0D;
  private String CaccountName = "";
  private String DaccountName = "";
  private int Crcurrencyid = 0;
  private int Drcurrencyid = 0;
  private String description = "";

  public String getDescription()
  {
    return this.description;
  }

  public void setDescription(String paramString)
  {
    this.description = paramString;
  }

  public int getCareaId()
  {
    return this.CareaId;
  }

  public void setCareaId(int paramInt)
  {
    this.CareaId = paramInt;
  }

  public int getDAreaId()
  {
    return this.DAreaId;
  }

  public void setDAreaId(int paramInt)
  {
    this.DAreaId = paramInt;
  }

  public int getCruserid()
  {
    return this.Cruserid;
  }

  public void setCruserid(int paramInt)
  {
    this.Cruserid = paramInt;
  }

  public int getDruserid()
  {
    return this.Druserid;
  }

  public void setDruserid(int paramInt)
  {
    this.Druserid = paramInt;
  }

  public String getUserId()
  {
    return this.userId;
  }

  public void setUserId(String paramString)
  {
    this.userId = paramString;
  }

  public String getOppUserName()
  {
    return this.oppUserName;
  }

  public void setOppUserName(String paramString)
  {
    this.oppUserName = paramString;
  }

  public String getAccountName()
  {
    return this.accountName;
  }

  public void setAccountName(String paramString)
  {
    this.accountName = paramString;
  }

  public int getPaymentOrReceiveTransType()
  {
    return this.paymentOrReceiveTransType;
  }

  public void setPaymentOrReceiveTransType(int paramInt)
  {
    this.paymentOrReceiveTransType = paramInt;
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

  public String getTransactionDate()
  {
    return this.transactionDate;
  }

  public void setTransactionDate(String paramString)
  {
    this.transactionDate = paramString;
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

  public int getEditAccountId()
  {
    return this.editAccountId;
  }

  public void setEditAccountId(int paramInt)
  {
    this.editAccountId = paramInt;
  }

  public int getCaccountid()
  {
    return this.Caccountid;
  }

  public void setCaccountid(int paramInt)
  {
    this.Caccountid = paramInt;
  }

  public int getDaccountid()
  {
    return this.Daccountid;
  }

  public void setDaccountid(int paramInt)
  {
    this.Daccountid = paramInt;
  }

  public double getCquantity()
  {
    return this.Cquantity;
  }

  public void setCquantity(double paramDouble)
  {
    this.Cquantity = paramDouble;
  }

  public double getCunitprice()
  {
    return this.Cunitprice;
  }

  public void setCunitprice(double paramDouble)
  {
    this.Cunitprice = paramDouble;
  }

  public double getDquantity()
  {
    return this.Dquantity;
  }

  public void setDquantity(double paramDouble)
  {
    this.Dquantity = paramDouble;
  }

  public double getDunitprice()
  {
    return this.Dunitprice;
  }

  public void setDunitprice(double paramDouble)
  {
    this.Dunitprice = paramDouble;
  }

  public String getCaccountName()
  {
    return this.CaccountName;
  }

  public void setCaccountName(String paramString)
  {
    this.CaccountName = paramString;
  }

  public String getDaccountName()
  {
    return this.DaccountName;
  }

  public void setDaccountName(String paramString)
  {
    this.DaccountName = paramString;
  }

  public int getCrType()
  {
    return this.CrType;
  }

  public void setCrType(int paramInt)
  {
    this.CrType = paramInt;
  }

  public int getDrType()
  {
    return this.DrType;
  }

  public void setDrType(int paramInt)
  {
    this.DrType = paramInt;
  }

  public int getCrcurrencyid()
  {
    return this.Crcurrencyid;
  }

  public void setCrcurrencyid(int paramInt)
  {
    this.Crcurrencyid = paramInt;
  }

  public int getDrcurrencyid()
  {
    return this.Drcurrencyid;
  }

  public void setDrcurrencyid(int paramInt)
  {
    this.Drcurrencyid = paramInt;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.model.Journal
 * JD-Core Version:    0.6.0
 */