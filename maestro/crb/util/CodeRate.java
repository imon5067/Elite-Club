package com.maestro.crb.util;

public class CodeRate
{
  int bs1 = 0;
  int bs2 = 0;
  String code;
  String country;
  double rate;
  int gress_time;
  int less_pct = 0;
  int max_talktime = 28800;

  public CodeRate(String paramString1, String paramString2, double paramDouble, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    this.code = paramString1;
    this.country = paramString2;
    this.rate = paramDouble;
    this.bs1 = paramInt1;
    this.bs2 = paramInt2;
    this.gress_time = paramInt3;
    this.less_pct = paramInt4;
    this.max_talktime = paramInt5;
  }

  public boolean isPerSecond()
  {
    return (this.bs1 == 1) && (this.bs1 == this.bs2);
  }

  public String getCode()
  {
    return this.code;
  }

  public String getName()
  {
    return this.country;
  }

  public String getCountry()
  {
    return this.country;
  }

  public int getBS_1()
  {
    return this.bs1;
  }

  public int getBS_2()
  {
    return this.bs2;
  }

  public double getRate()
  {
    return this.rate;
  }

  public double getCost(long paramLong, String paramString, int paramInt)
  {
    double d1 = 0.0D;
    int i = getBillableTime(paramLong, paramString, paramInt);
    if (isPerSecond())
    {
      d1 = i / 60.0D * this.rate;
    }
    else
    {
      double d2 = i - this.bs1;
      double d3 = 0.0D;
      if (d2 > 0.0D)
        d3 = Math.ceil(d2 * 1.0D / this.bs2) * this.rate * this.bs2 * 1.0D / 60.0D;
      d1 = d3 + this.bs1 * this.rate * 1.0D / 60.0D;
    }
    return d1;
  }

  public int getBillableTime(long paramLong, String paramString, int paramInt)
  {
    double d1 = 0.0D;
    if (isBillable(paramLong, paramString, paramInt) == 0)
    {
      if (this.less_pct > 0)
        paramLong -= (int)(paramLong * 1.0D * this.less_pct / 100.0D);
      if (isPerSecond())
      {
        d1 = paramLong;
      }
      else
      {
        double d2 = paramLong - this.bs1;
        d1 += this.bs1;
        if (d2 > 0.0D)
          d1 += Math.ceil(d2 * 1.0D / this.bs2) * this.bs2;
      }
    }
    else
    {
      d1 = 0.0D;
    }
    return (int)d1;
  }

  public int isBillable(long paramLong, String paramString, int paramInt)
  {
    int i = 0;
    if (paramString == null)
      i = 1;
    if (paramInt == 1)
    {
      if (paramString.equals("10"))
        i = 0;
      else
        i = 1;
    }
    else if ((paramString.equals("10")) || (paramString.equals("29")) || (paramString.equals("07")))
      i = 0;
    else
      i = 1;
    if (paramLong <= this.gress_time)
      i = 2;
    else if (paramLong > this.max_talktime)
      i = 3;
    return i;
  }

  public int getLessTime(long paramLong)
  {
    int i = 0;
    if (this.less_pct > 0)
      i = (int)(paramLong * 1.0D * this.less_pct / 100.0D);
    return i;
  }

  public int getGressTime()
  {
    return this.gress_time;
  }

  public int getMaxTalkTime()
  {
    return this.max_talktime;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.crb.util.CodeRate
 * JD-Core Version:    0.6.0
 */