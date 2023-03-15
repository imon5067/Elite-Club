package com.maestro.rateserver.model;

import java.util.ArrayList;
import java.util.List;

public class Item
  implements Comparable
{
  int id;
  String name;
  String detail;
  List childList = null;

  public Item(int paramInt, String paramString1, String paramString2)
  {
    this.id = paramInt;
    this.name = paramString1;
    this.detail = paramString2;
  }

  public void setChildList(List paramList)
  {
    if (this.childList == null)
      this.childList = new ArrayList();
    this.childList = paramList;
  }

  public List getChildList()
  {
    return this.childList;
  }

  public void setId(int paramInt)
  {
    this.id = paramInt;
  }

  public int getId()
  {
    return this.id;
  }

  public String getName()
  {
    return this.name;
  }

  public String getDetail()
  {
    return this.detail;
  }

  public int compareTo(Object paramObject)
  {
    Item localItem = (Item)paramObject;
    return getName().compareTo(localItem.getName());
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("Item[");
    localStringBuffer.append("id:");
    localStringBuffer.append(this.id);
    localStringBuffer.append("name:");
    localStringBuffer.append(this.name);
    localStringBuffer.append("]/n");
    return localStringBuffer.toString();
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.model.Item
 * JD-Core Version:    0.6.0
 */