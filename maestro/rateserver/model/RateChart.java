package com.maestro.rateserver.model;

public class RateChart
{
  private String country = null;
  private String code = null;
  private double ratePeak = 0.0D;
  private double rateOffPeak = 0.0D;
  private double rateDummy = 0.0D;
  private double rateDummyOffPeak = 0.0D;
  private String effectDate = null;
  private String endDate = null;
  private String gress = null;
  private String less = null;
  private String bs1 = null;
  private String bs2 = null;
  private String maxTalkTime = null;
  private String mainDest = null;
  private boolean replace = false;
  private String previousEffectDate = null;
  private String previousEndDate = null;

  public boolean isReplace()
  {
    return this.replace;
  }

  public void setReplace(boolean paramBoolean)
  {
    this.replace = paramBoolean;
  }

  public String getPreviousEffectDate()
  {
    return this.previousEffectDate;
  }

  public void setPreviousEffectDate(String paramString)
  {
    this.previousEffectDate = paramString;
  }

  public String getPreviousEndDate()
  {
    return this.previousEndDate;
  }

  public void setPreviousEndDate(String paramString)
  {
    this.previousEndDate = paramString;
  }

  public String getCountry()
  {
    return this.country;
  }

  public void setCountry(String paramString)
  {
    this.country = paramString;
  }

  public String getCode()
  {
    return this.code;
  }

  public void setCode(String paramString)
  {
    this.code = paramString;
  }

  public double getRatePeak()
  {
    return this.ratePeak;
  }

  public void setRatePeak(double paramDouble)
  {
    this.ratePeak = paramDouble;
  }

  public double getRateOffPeak()
  {
    return this.rateOffPeak;
  }

  public void setRateOffPeak(double paramDouble)
  {
    this.rateOffPeak = paramDouble;
  }

  public double getRateDummy()
  {
    return this.rateDummy;
  }

  public void setRateDummy(double paramDouble)
  {
    this.rateDummy = paramDouble;
  }

  public double getRateDummyOffPeak()
  {
    return this.rateDummyOffPeak;
  }

  public void setRateDummyOffPeak(double paramDouble)
  {
    this.rateDummyOffPeak = paramDouble;
  }

  public String getEffectDate()
  {
    return this.effectDate;
  }

  public void setEffectDate(String paramString)
  {
    this.effectDate = paramString;
  }

  public String getEndDate()
  {
    return this.endDate;
  }

  public void setEndDate(String paramString)
  {
    this.endDate = paramString;
  }

  public String getGress()
  {
    return this.gress;
  }

  public void setGress(String paramString)
  {
    this.gress = paramString;
  }

  public String getLess()
  {
    return this.less;
  }

  public void setLess(String paramString)
  {
    this.less = paramString;
  }

  public String getBs1()
  {
    return this.bs1;
  }

  public void setBs1(String paramString)
  {
    this.bs1 = paramString;
  }

  public String getBs2()
  {
    return this.bs2;
  }

  public void setBs2(String paramString)
  {
    this.bs2 = paramString;
  }

  public String getMaxTalkTime()
  {
    return this.maxTalkTime;
  }

  public void setMaxTalkTime(String paramString)
  {
    this.maxTalkTime = paramString;
  }

  public String getMainDest()
  {
    return this.mainDest;
  }

  public void setMainDest(String paramString)
  {
    this.mainDest = paramString;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.model.RateChart
 * JD-Core Version:    0.6.0
 */