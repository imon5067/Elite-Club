package com.maestro.rateserver.model;

import java.io.Serializable;

public class OperationalUserModel
  implements Serializable
{
  private long id;
  private long seniorityLevel;
  private double salary;
  private long designationId;
  private long userId;
  private long areaId;
  private long actorId;
  private long userAddressId;
  private double userBasicSalary;
  private long userHouseRent;
  private long userFoodAllowence;
  private long rankid;

  public long getRankid()
  {
    return this.rankid;
  }

  public void setRankid(long paramLong)
  {
    this.rankid = paramLong;
  }

  public long getUserAddressId()
  {
    return this.userAddressId;
  }

  public void setUserAddressId(long paramLong)
  {
    this.userAddressId = paramLong;
  }

  public OperationalUserModel()
  {
  }

  public OperationalUserModel(long paramLong)
  {
    this.id = paramLong;
  }

  public OperationalUserModel(long paramLong1, long paramLong2, double paramDouble)
  {
    this.id = paramLong1;
    this.seniorityLevel = paramLong2;
    this.salary = paramDouble;
  }

  public long getId()
  {
    return this.id;
  }

  public void setId(long paramLong)
  {
    this.id = paramLong;
  }

  public long getSeniorityLevel()
  {
    return this.seniorityLevel;
  }

  public void setSeniorityLevel(long paramLong)
  {
    this.seniorityLevel = paramLong;
  }

  public double getSalary()
  {
    return this.salary;
  }

  public void setSalary(double paramDouble)
  {
    this.salary = paramDouble;
  }

  public long getUserId()
  {
    return this.userId;
  }

  public void setUserId(long paramLong)
  {
    this.userId = paramLong;
  }

  public long getDesignationId()
  {
    return this.designationId;
  }

  public void setDesignationId(long paramLong)
  {
    this.designationId = paramLong;
  }

  public long getAreaId()
  {
    return this.areaId;
  }

  public void setAreaId(long paramLong)
  {
    this.areaId = paramLong;
  }

  public long getActorId()
  {
    return this.actorId;
  }

  public void setActorId(long paramLong)
  {
    this.actorId = paramLong;
  }

  public double getUserBasicSalary()
  {
    return this.userBasicSalary;
  }

  public void setUserBasicSalary(double paramDouble)
  {
    this.userBasicSalary = paramDouble;
  }

  public long getUserHouseRent()
  {
    return this.userHouseRent;
  }

  public void setUserHouseRent(long paramLong)
  {
    this.userHouseRent = paramLong;
  }

  public long getUserFoodAllowence()
  {
    return this.userFoodAllowence;
  }

  public void setUserFoodAllowence(long paramLong)
  {
    this.userFoodAllowence = paramLong;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("TreeModel[");
    localStringBuffer.append("id:" + this.id);
    localStringBuffer.append(",seniorityLevel:" + this.seniorityLevel);
    localStringBuffer.append(",salary:" + this.salary);
    localStringBuffer.append(",designationId:" + this.designationId);
    localStringBuffer.append(",userId:" + this.userId);
    localStringBuffer.append(",areaId:" + this.areaId);
    localStringBuffer.append(",actorId:" + this.actorId);
    localStringBuffer.append(",userBasicSalary:" + this.userBasicSalary);
    localStringBuffer.append(",userHouseRent:" + this.userHouseRent);
    localStringBuffer.append(",userFoodAllowence:" + this.userFoodAllowence);
    localStringBuffer.append(",rankid:" + this.rankid);
    localStringBuffer.append("]");
    return localStringBuffer.toString();
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.model.OperationalUserModel
 * JD-Core Version:    0.6.0
 */