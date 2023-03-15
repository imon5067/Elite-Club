package com.maestro.rateserver.model;

public class CardsActivationModel
{
  private String vendorid = "";
  private String start = "";
  private String end = "";
  private String series_id = "";
  private String sc_id = "";
  private String me_id = "";
  private String omsme = "";
  private String seriesName = "";
  String[] cardStatus = null;
  int itemId = 0;
  int acc_company_id = 0;
  int acc_pay_acc_id = 0;
  int acc_vendor_id = 0;
  int card_count = 0;
  boolean hasAccIntegration;
  boolean isThirdParty;
  int total = 0;
  double price = 0.0D;
  double card_value = 0.0D;
  double comm_amount = 0.0D;

  public double getCommAmount()
  {
    return this.comm_amount;
  }

  public void setCommAmount(double paramDouble)
  {
    this.comm_amount = paramDouble;
  }

  public String[] getCardStatus()
  {
    return this.cardStatus;
  }

  public void setCardStatus(String[] paramArrayOfString)
  {
    this.cardStatus = paramArrayOfString;
  }

  public String getVendorId()
  {
    return this.vendorid;
  }

  public void setVendorId(String paramString)
  {
    this.vendorid = paramString;
  }

  public String getSeriesName()
  {
    return this.seriesName;
  }

  public void setSeriesName(String paramString)
  {
    this.seriesName = paramString;
  }

  public int getCardCount()
  {
    return this.card_count;
  }

  public void setCardCount(int paramInt)
  {
    this.card_count = paramInt;
  }

  public double getCardValue()
  {
    return this.card_value;
  }

  public void setCardValue(double paramDouble)
  {
    this.card_value = paramDouble;
  }

  public String getOmsMe()
  {
    return this.omsme;
  }

  public void setOmsMe(String paramString)
  {
    this.omsme = paramString;
  }

  public int getTotal()
  {
    return this.total;
  }

  public void setTotal(int paramInt)
  {
    this.total = paramInt;
  }

  public double getPrice()
  {
    return this.price;
  }

  public void setPrice(double paramDouble)
  {
    this.price = paramDouble;
  }

  public int getItemId()
  {
    return this.itemId;
  }

  public void setItemId(int paramInt)
  {
    this.itemId = paramInt;
  }

  public int getAccCompanyId()
  {
    return this.acc_company_id;
  }

  public void setAccCompanyId(int paramInt)
  {
    this.acc_company_id = paramInt;
  }

  public int getAccPayAccId()
  {
    return this.acc_pay_acc_id;
  }

  public void setAccPayAccId(int paramInt)
  {
    this.acc_pay_acc_id = paramInt;
  }

  public int getAccVendorId()
  {
    return this.acc_vendor_id;
  }

  public void setAccVendorId(int paramInt)
  {
    this.acc_vendor_id = paramInt;
  }

  public String getStart()
  {
    return this.start;
  }

  public void setStart(String paramString)
  {
    this.start = paramString;
  }

  public String getEnd()
  {
    return this.end;
  }

  public void setEnd(String paramString)
  {
    this.end = paramString;
  }

  public String getSeriesId()
  {
    return this.series_id;
  }

  public void setSeriesId(String paramString)
  {
    this.series_id = paramString;
  }

  public String getScId()
  {
    return this.sc_id;
  }

  public void setScId(String paramString)
  {
    this.sc_id = paramString;
  }

  public String getMeId()
  {
    return this.me_id;
  }

  public void setMeId(String paramString)
  {
    this.me_id = paramString;
  }

  public boolean getHasAccIntegration()
  {
    return this.hasAccIntegration;
  }

  public void setHasAccIntegration(boolean paramBoolean)
  {
    this.hasAccIntegration = paramBoolean;
  }

  public boolean getThirdParty()
  {
    return this.isThirdParty;
  }

  public void setThirdParty(boolean paramBoolean)
  {
    this.isThirdParty = paramBoolean;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.model.CardsActivationModel
 * JD-Core Version:    0.6.0
 */