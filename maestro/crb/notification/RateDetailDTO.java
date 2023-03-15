package com.maestro.crb.notification;

import java.util.Date;

public class RateDetailDTO
{
  private String destination;
  private String code;
  private double rate;
  private double bs1;
  private double bs2;
  private Date effectDate;
  private String country;

  public String getDestination()
  {
    return this.destination;
  }

  public void setDestination(String paramString)
  {
    this.destination = paramString;
  }

  public String getCode()
  {
    return this.code;
  }

  public void setCode(String paramString)
  {
    this.code = paramString;
  }

  public double getRate()
  {
    return this.rate;
  }

  public void setRate(double paramDouble)
  {
    this.rate = paramDouble;
  }

  public double getBs1()
  {
    return this.bs1;
  }

  public void setBs1(double paramDouble)
  {
    this.bs1 = paramDouble;
  }

  public double getBs2()
  {
    return this.bs2;
  }

  public void setBs2(double paramDouble)
  {
    this.bs2 = paramDouble;
  }

  public Date getEffectDate()
  {
    return this.effectDate;
  }

  public void setEffectDate(Date paramDate)
  {
    this.effectDate = paramDate;
  }

  public String getCountry()
  {
    return this.country;
  }

  public void setCountry(String paramString)
  {
    this.country = paramString;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.crb.notification.RateDetailDTO
 * JD-Core Version:    0.6.0
 */