package com.maestro.rateserver.model;

import java.io.PrintStream;

public class SRDetail
  implements Comparable
{
  private String eid;
  private String sc_name;
  private String uname;
  private String upass;
  private String name;
  private String type;
  private String address;
  private String phone;
  private String mobile;
  private String email;
  private String active;
  private String sales_center_id;
  private String is_self;
  private int parent;
  private int level;
  private int me_id;
  private String auth_key;
  private String the_ip;
  private String the_agent_id;
  public int getMe_id()
  {
    return this.me_id;
  }

  public void setMe_id(int paramInt)
  {
    this.me_id = paramInt;
  }

  public int getLevel()
  {
    return this.level;
  }

  public void setLevel(int paramInt)
  {
    this.level = paramInt;
  }

  public int getParent()
  {
    return this.parent;
  }

  public void setParent(int paramInt)
  {
    this.parent = paramInt;
  }

  public String getIs_self()
  {
    return this.is_self;
  }

  public void setIs_self(String paramString)
  {
    this.is_self = paramString;
  }

  public String getSales_center_id()
  {
    return this.sales_center_id;
  }

  public void setSales_center_id(String paramString)
  {
    this.sales_center_id = paramString;
  }

  public String getActive()
  {
    return this.active;
  }

  public void setActive(String paramString)
  {
    this.active = paramString;
  }

  public String getEmail()
  {
    return this.email;
  }

  public void setEmail(String paramString)
  {
    this.email = paramString;
  }

  public String getMobile()
  {
    return this.mobile;
  }

  public void setMobile(String paramString)
  {
    this.mobile = paramString;
  }

  public String getPhone()
  {
    return this.phone;
  }

  public void setPhone(String paramString)
  {
    this.phone = paramString;
  }

  public String getAddress()
  {
    return this.address;
  }

  public void setAddress(String paramString)
  {
    this.address = paramString;
  }

  public String getType()
  {
    return this.type;
  }

  public void setType(String paramString)
  {
    this.type = paramString;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public String getUpass()
  {
    return this.upass;
  }

  public void setUpass(String paramString)
  {
    this.upass = paramString;
  }

  public String getUname()
  {
    return this.uname;
  }

  public void setUname(String paramString)
  {
    this.uname = paramString;
  }

  public String getEid()
  {
    return this.eid;
  }

  public void setEid(String paramString)
  {
    this.eid = paramString;
  }

  public String getSc_name()
  {
    return this.sc_name;
  }

  public void setSc_name(String paramString)
  {
    this.sc_name = paramString;
  }
/******************/
  public String getAuth_key()
  {
    return this.auth_key;
  }

  public void setAuth_key(String paramString)
  {
    this.auth_key = paramString;
  }

    public String getThe_ip()
    {
      return this.the_ip;
    }

    public void setThe_ip(String paramString)
    {
      this.the_ip = paramString;
    }
    public String getThe_agent_id()
    {
      return this.the_agent_id;
    }

    public void setThe_agent_id(String paramString)
    {
      this.the_agent_id = paramString;
    }


  public int compareTo(Object paramObject)
  {
    SRDetail localSRDetail = (SRDetail)paramObject;
    Double localDouble1 = new Double("" + getLevel());
    Double localDouble2 = new Double("" + localSRDetail.getLevel());
    Double localDouble3 = new Double("" + getParent());
    Double localDouble4 = new Double("" + localSRDetail.getParent());
    String str1 = getSales_center_id();
    String str2 = localSRDetail.getSales_center_id();
    String str3 = getType();
    String str4 = localSRDetail.getType();
    System.out.println("compare type::" + str3.compareTo(str4));
    System.out.println("compare seals_center::" + str1.compareTo(str2));
    System.out.println("compare level::" + localDouble1.compareTo(localDouble2));
    return localDouble1.compareTo(localDouble2) * localDouble3.compareTo(localDouble4);
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("SRDetail[");
    localStringBuffer.append("eid:");
    localStringBuffer.append(this.eid + " ");
    localStringBuffer.append("name:");
    localStringBuffer.append(this.name + " ");
    localStringBuffer.append("type:");
    localStringBuffer.append(this.type + " ");
    localStringBuffer.append("sc_name:");
    localStringBuffer.append(this.sc_name + " ");
    localStringBuffer.append("]/n");
    return localStringBuffer.toString();
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.model.SRDetail
 * JD-Core Version:    0.6.0
 */