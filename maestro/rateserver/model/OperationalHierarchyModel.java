package com.maestro.rateserver.model;

import java.io.Serializable;

public class OperationalHierarchyModel
  implements Serializable
{
  private long id;
  private long userId;
  private long parentId;
  private String operationType = "";
  private String operationName = "";
  private String details = "";
  private String userType = "";
  private String userName = "";
  private String IsGroup = "";
  private long actorId;
  private long areaId;
  private long userAddressId;
  private OperationalHierarchyModel parent;
  private String isGroup;
  private String eMail = "";
  private String phoneNo = "";
  private String mobileNo = "";

  public OperationalHierarchyModel()
  {
    try
    {
      jbInit();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public long getUserAddressId()
  {
    return this.userAddressId;
  }

  public void setUserAddressId(long paramLong)
  {
    this.userAddressId = paramLong;
  }

  public long getId()
  {
    return this.id;
  }

  public void setId(long paramLong)
  {
    this.id = paramLong;
  }

  public long getUserId()
  {
    return this.userId;
  }

  public void setUserId(long paramLong)
  {
    this.userId = paramLong;
  }

  public long getParentId()
  {
    return this.parentId;
  }

  public void setParentId(long paramLong)
  {
    this.parentId = paramLong;
  }

  public String getOperationType()
  {
    return this.operationType;
  }

  public void setOperationType(String paramString)
  {
    this.operationType = paramString;
  }

  public String getUserName()
  {
    return this.userName;
  }

  public void setUserName(String paramString)
  {
    this.userName = paramString;
  }

  public String getIsGroup()
  {
    return this.isGroup;
  }

  public void setIsGroup(String paramString)
  {
    this.isGroup = paramString;
  }

  public String getOperationName()
  {
    return this.operationName;
  }

  public void setOperationName(String paramString)
  {
    this.operationName = paramString;
  }

  public String getEmail()
  {
    return this.eMail;
  }

  public void setEmail(String paramString)
  {
    this.eMail = paramString;
  }

  public String getPhoneNo()
  {
    return this.phoneNo;
  }

  public void setPhoneNo(String paramString)
  {
    this.phoneNo = paramString;
  }

  public String getMobileNo()
  {
    return this.mobileNo;
  }

  public void setMobileNo(String paramString)
  {
    this.mobileNo = paramString;
  }

  public String getDetails()
  {
    return this.details;
  }

  public void setDetails(String paramString)
  {
    this.details = paramString;
  }

  public long getActorId()
  {
    return this.actorId;
  }

  public void setActorId(long paramLong)
  {
    this.actorId = paramLong;
  }

  public long getAreaId()
  {
    return this.areaId;
  }

  public void setAreaId(long paramLong)
  {
    this.areaId = paramLong;
  }

  public String getUserType()
  {
    return this.userType;
  }

  public void setUserType(String paramString)
  {
    this.userType = paramString;
  }

  public OperationalHierarchyModel getParent()
  {
    return this.parent;
  }

  public void setParent(OperationalHierarchyModel paramOperationalHierarchyModel)
  {
    this.parent = paramOperationalHierarchyModel;
  }

  public String getHirerchy()
  {
    if ((null != this.parent) && (getParent().id > 0L))
      return this.parent.getHirerchy() + "->" + getDetails();
    return "" + getDetails();
  }

  private void jbInit()
    throws Exception
  {
  }

  public String getModelInfo()
  {
    StringBuffer localStringBuffer = new StringBuffer("OperationalHierarchyModel[");
    localStringBuffer.append(" id:" + this.id);
    localStringBuffer.append(", userId:" + this.userId);
    localStringBuffer.append(", parentId:" + this.parentId);
    localStringBuffer.append(", operationType:" + this.operationType);
    localStringBuffer.append(", operationName:" + this.operationName);
    localStringBuffer.append(", details:" + this.details);
    localStringBuffer.append(", userType:" + this.userType);
    localStringBuffer.append(", actorId:" + this.actorId);
    localStringBuffer.append(", areaId:" + this.areaId);
    localStringBuffer.append(", parentId:" + this.parentId);
    localStringBuffer.append(", eMail:" + this.eMail);
    localStringBuffer.append(", phoneNo:" + this.phoneNo);
    localStringBuffer.append(", mobileNo:" + this.mobileNo);
    if (this.parent != null)
      localStringBuffer.append(", parent:" + this.parent);
    localStringBuffer.append("]");
    return localStringBuffer.toString();
  }

  public String toString()
  {
    return getModelInfo();
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.model.OperationalHierarchyModel
 * JD-Core Version:    0.6.0
 */