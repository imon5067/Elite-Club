package com.maestro.rateserver.model;

import java.io.Serializable;

public class SalaryModel
  implements Serializable
{
  private long id;
  private long useId;
  private long userDesigId;
  private long areaId;
  private String actionDate;
  private String arrearRemark;
  private String deductOtherRemark;
  private String commTarget;
  private long commAmount;
  private String commRemark;
  private String incrPurpose;
  private long incrAmount;
  private String incrRemark;
  private long deductHsRent;
  private long deductFdAmount;
  private long deductOtherAmt;
  private long month;
  private long year;
  private long arrearAmount;
  private long actorId;
  private long unauthLeave;
  private long deductLeaveAmt;
  private long transactionId = 0L;
  private double userBasicSalary;
  private double userHouseRent;
  private double userFoodAllowence;
  private double total;
  private String userInfo = "";

  public long getId()
  {
    return this.id;
  }

  public void setId(long paramLong)
  {
    this.id = paramLong;
  }

  public long getUseId()
  {
    return this.useId;
  }

  public void setUseId(long paramLong)
  {
    this.useId = paramLong;
  }

  public long getUserDesigId()
  {
    return this.userDesigId;
  }

  public void setUserDesigId(long paramLong)
  {
    this.userDesigId = paramLong;
  }

  public long getAreaId()
  {
    return this.areaId;
  }

  public void setAreaId(long paramLong)
  {
    this.areaId = paramLong;
  }

  public String getActionDate()
  {
    return this.actionDate;
  }

  public void setActionDate(String paramString)
  {
    this.actionDate = paramString;
  }

  public String getArrearRemark()
  {
    return this.arrearRemark;
  }

  public void setArrearRemark(String paramString)
  {
    this.arrearRemark = paramString;
  }

  public String getDeductOtherRemark()
  {
    return this.deductOtherRemark;
  }

  public void setDeductOtherRemark(String paramString)
  {
    this.deductOtherRemark = paramString;
  }

  public String getCommTarget()
  {
    return this.commTarget;
  }

  public void setCommTarget(String paramString)
  {
    this.commTarget = paramString;
  }

  public long getCommAmount()
  {
    return this.commAmount;
  }

  public void setCommAmount(long paramLong)
  {
    this.commAmount = paramLong;
  }

  public String getCommRemark()
  {
    return this.commRemark;
  }

  public void setCommRemark(String paramString)
  {
    this.commRemark = paramString;
  }

  public String getIncrPurpose()
  {
    return this.incrPurpose;
  }

  public void setIncrPurpose(String paramString)
  {
    this.incrPurpose = paramString;
  }

  public long getIncrAmount()
  {
    return this.incrAmount;
  }

  public void setIncrAmount(long paramLong)
  {
    this.incrAmount = paramLong;
  }

  public String getIncrRemark()
  {
    return this.incrRemark;
  }

  public void setIncrRemark(String paramString)
  {
    this.incrRemark = paramString;
  }

  public long getDeductHsRent()
  {
    return this.deductHsRent;
  }

  public void setDeductHsRent(long paramLong)
  {
    this.deductHsRent = paramLong;
  }

  public long getDeductFdAmount()
  {
    return this.deductFdAmount;
  }

  public void setDeductFdAmount(long paramLong)
  {
    this.deductFdAmount = paramLong;
  }

  public long getDeductOtherAmt()
  {
    return this.deductOtherAmt;
  }

  public void setDeductOtherAmt(long paramLong)
  {
    this.deductOtherAmt = paramLong;
  }

  public long getMonth()
  {
    return this.month;
  }

  public void setMonth(long paramLong)
  {
    this.month = paramLong;
  }

  public long getYear()
  {
    return this.year;
  }

  public void setYear(long paramLong)
  {
    this.year = paramLong;
  }

  public long getArrearAmount()
  {
    return this.arrearAmount;
  }

  public void setArrearAmount(long paramLong)
  {
    this.arrearAmount = paramLong;
  }

  public long getActorId()
  {
    return this.actorId;
  }

  public void setActorId(long paramLong)
  {
    this.actorId = paramLong;
  }

  public long getUnauthLeave()
  {
    return this.unauthLeave;
  }

  public void setUnauthLeave(long paramLong)
  {
    this.unauthLeave = paramLong;
  }

  public long getDeductLeaveAmt()
  {
    return this.deductLeaveAmt;
  }

  public void setDeductLeaveAmt(long paramLong)
  {
    this.deductLeaveAmt = paramLong;
  }

  public long getTransactionId()
  {
    return this.transactionId;
  }

  public void setTransactionId(long paramLong)
  {
    this.transactionId = paramLong;
  }

  public String getValueAsStr(String paramString)
  {
    String str1 = "";
    String str2 = "";
    if (paramString.equalsIgnoreCase("basicSalary"))
      str1 = this.userBasicSalary + "";
    else if (paramString.equalsIgnoreCase("total"))
      str1 = this.total + "";
    if (str1.indexOf("E") != -1)
    {
      int i = Integer.parseInt(str1.substring(str1.indexOf("E") + 1));
      String str3 = str1.substring(0, str1.indexOf("E"));
      int j = str3.length();
      for (int k = j; k <= i + 1; k++)
        str3 = str3 + "0";
      String str4 = str3.substring(0, str3.indexOf("."));
      String str5 = str3.substring(str3.indexOf(".") + 1);
      str2 = str4 + str5;
    }
    else
    {
      str2 = str1;
    }
    return str2;
  }

  public double getUserBasicSalary()
  {
    return this.userBasicSalary;
  }

  public void setUserBasicSalary(double paramDouble)
  {
    this.userBasicSalary = paramDouble;
  }

  public double getUserHouseRent()
  {
    return this.userHouseRent;
  }

  public void setUserHouseRent(double paramDouble)
  {
    this.userHouseRent = paramDouble;
  }

  public double getUserFoodAllowence()
  {
    return this.userFoodAllowence;
  }

  public void setUserFoodAllowence(double paramDouble)
  {
    this.userFoodAllowence = paramDouble;
  }

  public double getTotal()
  {
    return this.total;
  }

  public void setTotal(double paramDouble)
  {
    this.total = paramDouble;
  }

  public String getUserInfo()
  {
    return this.userInfo;
  }

  public void setUserInfo(String paramString)
  {
    this.userInfo = paramString;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("SalaryModel[");
    localStringBuffer.append("id:" + this.id);
    localStringBuffer.append(",useId:" + this.useId);
    localStringBuffer.append(",userInfo:" + this.userInfo);
    localStringBuffer.append(",userDesigId:" + this.userDesigId);
    localStringBuffer.append(",areaId:" + this.areaId);
    localStringBuffer.append(",actionDate:" + this.actionDate);
    localStringBuffer.append(",arrearRemark:" + this.arrearRemark);
    localStringBuffer.append(",deductOtherRemark:" + this.deductOtherRemark);
    localStringBuffer.append(",commTarget:" + this.commTarget);
    localStringBuffer.append(",commAmount:" + this.commAmount);
    localStringBuffer.append(",commRemark:" + this.commRemark);
    localStringBuffer.append(",incrPurpose:" + this.incrPurpose);
    localStringBuffer.append(",incrAmount:" + this.incrAmount);
    localStringBuffer.append(",incrRemark:" + this.incrRemark);
    localStringBuffer.append(",deductHsRent:" + this.deductHsRent);
    localStringBuffer.append(",deductFdAmount:" + this.deductFdAmount);
    localStringBuffer.append(",deductOtherAmt:" + this.deductOtherAmt);
    localStringBuffer.append(",month:" + this.month);
    localStringBuffer.append(",year:" + this.year);
    localStringBuffer.append(",arrearAmount:" + this.arrearAmount);
    localStringBuffer.append(",actorId:" + this.actorId);
    localStringBuffer.append(",unauthLeave:" + this.unauthLeave);
    localStringBuffer.append(",deductLeaveAmt:" + this.deductLeaveAmt);
    localStringBuffer.append(",transactionId:" + this.transactionId);
    localStringBuffer.append(",userBasicSalary:" + this.userBasicSalary);
    localStringBuffer.append(",userHouseRent:" + this.userHouseRent);
    localStringBuffer.append(",userFoodAllowence:" + this.userFoodAllowence);
    localStringBuffer.append(",total:" + this.total);
    localStringBuffer.append("]");
    return localStringBuffer.toString();
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.model.SalaryModel
 * JD-Core Version:    0.6.0
 */