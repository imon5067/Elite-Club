package com.maestro.rateserver.model;

public class CountryCode
  implements Comparable
{
  private String country;
  private String code;
  private String rate;
  private String gress;
  private String less;
  private String bs_1;
  private String bs2;
  private String maxtalktime;
  private String id;

  public String getId()
  {
    return this.id;
  }

  public void setId(String paramString)
  {
    this.id = paramString;
  }

  public CountryCode(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9)
  {
    this.country = paramString1;
    this.code = paramString2;
    this.rate = paramString3;
    this.gress = paramString4;
    this.less = paramString5;
    this.bs2 = paramString7;
    this.bs_1 = paramString6;
    this.maxtalktime = paramString8;
    this.id = paramString9;
  }

  public int compareTo(Object paramObject)
  {
    CountryCode localCountryCode = (CountryCode)paramObject;
    String str1 = getCountry();
    String str2 = localCountryCode.getCountry();
    return str1.compareTo(str2);
  }

  public String getBs_1()
  {
    return this.bs_1;
  }

  public void setBs_1(String paramString)
  {
    this.bs_1 = paramString;
  }

  public String getBs2()
  {
    return this.bs2;
  }

  public void setBs2(String paramString)
  {
    this.bs2 = paramString;
  }

  public String getCode()
  {
    return this.code;
  }

  public void setCode(String paramString)
  {
    this.code = paramString;
  }

  public String getCountry()
  {
    return this.country;
  }

  public void setCountry(String paramString)
  {
    this.country = paramString;
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

  public String getMaxtalktime()
  {
    return this.maxtalktime;
  }

  public void setMaxtalktime(String paramString)
  {
    this.maxtalktime = paramString;
  }

  public String getRate()
  {
    return this.rate;
  }

  public void setRate(String paramString)
  {
    this.rate = paramString;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.model.CountryCode
 * JD-Core Version:    0.6.0
 */