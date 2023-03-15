package com.maestro.crb.util;

public class DestinationCategory
{
  private String code;
  private String name;
  private String country;
  private String category;

  public String getCode()
  {
    return this.code;
  }

  public String getName()
  {
    return this.name;
  }

  public String getCountry()
  {
    return this.country;
  }

  public String getCategory()
  {
    if (this.name.toLowerCase().indexOf("mobile") >= 0)
      return "Mobile";
    return "Fixed";
  }

  public void setCode(String paramString)
  {
    this.code = paramString;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public void setCountry(String paramString)
  {
    this.country = paramString;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.crb.util.DestinationCategory
 * JD-Core Version:    0.6.0
 */