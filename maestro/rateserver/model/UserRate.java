package com.maestro.rateserver.model;

import java.util.ArrayList;
import java.util.List;

public class UserRate
  implements Comparable
{
  private int id;
  private String detail;
  private double rate;
  private double offrate;
  private double dummyrate;
  private double dummyOffrate;
  private String effectDate;
  private String endDate;

  public int getId()
  {
    return this.id;
  }

  public UserRate()
  {
  }

  public UserRate(int paramInt, double paramDouble)
  {
    this.id = paramInt;
    this.rate = paramDouble;
  }

  public UserRate(int paramInt, String paramString1, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, String paramString2, String paramString3)
  {
    this.id = paramInt;
    this.detail = paramString1;
    this.rate = paramDouble1;
    this.offrate = paramDouble2;
    this.dummyrate = paramDouble3;
    this.dummyOffrate = paramDouble4;
    this.effectDate = paramString2;
    this.endDate = paramString3;
  }

  public void setId(int paramInt)
  {
    this.id = paramInt;
  }

  public double getRate()
  {
    return this.rate;
  }

  public double getOffRate()
  {
    return this.offrate;
  }

  public double getDummyRate()
  {
    return this.dummyrate;
  }

  public double getDummyOffRate()
  {
    return this.dummyOffrate;
  }

  public String getEffectDate()
  {
    return this.effectDate;
  }

  public String getEndDate()
  {
    return this.endDate;
  }

  public void setRate(double paramDouble)
  {
    this.rate = paramDouble;
  }

  public void setEffectDate(String paramString)
  {
    this.effectDate = paramString;
  }

  public void setEndDate(String paramString)
  {
    this.endDate = paramString;
  }

  public void setDetail(String paramString)
  {
    this.detail = paramString;
  }

  public String getDetail()
  {
    return this.detail;
  }

  public int compareTo(Object paramObject)
  {
    UserRate localUserRate = (UserRate)paramObject;
    if (getRate() < localUserRate.getRate())
      return -1;
    if (getRate() == localUserRate.getRate())
      return 0;
    return 1;
  }

  public List dummyUserRate()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new UserRate(1, 1.5D));
    localArrayList.add(new UserRate(2, 1.45D));
    localArrayList.add(new UserRate(3, 1.2D));
    localArrayList.add(new UserRate(4, 1.6D));
    localArrayList.add(new UserRate(5, 1.7D));
    localArrayList.add(new UserRate(6, 1.8D));
    localArrayList.add(new UserRate(7, 2.0D));
    localArrayList.add(new UserRate(8, 2.1D));
    localArrayList.add(new UserRate(9, 2.0D));
    localArrayList.add(new UserRate(10, 1.9D));
    return localArrayList;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("UserRate[");
    localStringBuffer.append("id:");
    localStringBuffer.append(this.id);
    localStringBuffer.append("rate:");
    localStringBuffer.append(this.rate);
    localStringBuffer.append("]/n");
    return localStringBuffer.toString();
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.model.UserRate
 * JD-Core Version:    0.6.0
 */