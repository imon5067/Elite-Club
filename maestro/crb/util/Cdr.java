package com.maestro.crb.util;

public class Cdr
{
  private String country;
  private String dest_cat;
  private double sessionTime;
  private double billableTime;
  private double callCost;

  public String getCountry()
  {
    return this.country;
  }

  public String getDest_cat()
  {
    return this.dest_cat;
  }

  public double getSessionTime()
  {
    return this.sessionTime;
  }

  public double getBillableTime()
  {
    return this.billableTime;
  }

  public double getCallCost()
  {
    return this.callCost;
  }

  public void setCountry(String paramString)
  {
    this.country = paramString;
  }

  public void setDest_cat(String paramString)
  {
    this.dest_cat = paramString;
  }

  public void setSessionTime(double paramDouble)
  {
    this.sessionTime = paramDouble;
  }

  public void setBillableTime(double paramDouble)
  {
    this.billableTime = paramDouble;
  }

  public void setCallCost(double paramDouble)
  {
    this.callCost = paramDouble;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.crb.util.Cdr
 * JD-Core Version:    0.6.0
 */