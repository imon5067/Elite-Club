package com.maestro.crb.notification;

import java.util.List;

public class NotificationObjectDTO
{
  private long id;
  private String name;
  private String objectType;
  private List dataList;

  public long getId()
  {
    return this.id;
  }

  public void setId(long paramLong)
  {
    this.id = paramLong;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public String getObjectType()
  {
    return this.objectType;
  }

  public void setObjectType(String paramString)
  {
    this.objectType = paramString;
  }

  public List getDataList()
  {
    return this.dataList;
  }

  public void setDataList(List paramList)
  {
    this.dataList = paramList;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.crb.notification.NotificationObjectDTO
 * JD-Core Version:    0.6.0
 */