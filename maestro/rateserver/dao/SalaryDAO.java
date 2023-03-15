package com.maestro.rateserver.dao;

import com.maestro.rateserver.model.SalaryModel;
import com.maestro.rateserver.util.StringUtil;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class SalaryDAO
{
  SalaryModel salaryModel = null;
  private Statement stmt = null;
  long salaryID = -1L;
  StringUtil strUtil = new StringUtil();

  public SalaryDAO(Statement paramStatement)
  {
    this.stmt = paramStatement;
  }

  public Object getObjectById(String paramString)
    throws Exception
  {
    String str = "SELECT ID,USER_ID,USER_DESIG_ID,AREA_ID,to_char(ACTION_DATE,'dd-mm-rrrr') AS ACTION_DATEARREAR_REMARK,DEDUCT_OTHER_REMARK,COMM_TARGET,COMM_AMT,COMM_REMARK,INCR_PURPOSE,INCR_AMOUNT,INCR_REMARK,DEDUCT_HS_RENT,DEDUCT_FD_AMT,DEDUCT_OTHER_AMT,SMONTH,SYEAR,ARREAR_AMT,ACTOR_ID,UNAUTH_LEAVE,DEDUCT_LEAVE_AMT,TRANSACTION_ID,USER_BASIC_SALARY,USER_HOUSE_RENT,USER_FOOD_ALLOWENCE,TOTALFROM USER_SALARY_RECORD@OMS_LINK WHERE ID=" + paramString;
    ResultSet localResultSet = this.stmt.executeQuery(str);
    SalaryModel localSalaryModel = null;
    if (localResultSet.next())
      localSalaryModel = retrieveData(localResultSet);
    localResultSet.close();
    return localSalaryModel;
  }

  private SalaryModel retrieveData(ResultSet paramResultSet)
    throws SQLException
  {
    SalaryModel localSalaryModel = new SalaryModel();
    localSalaryModel.setId(paramResultSet.getLong("ID"));
    localSalaryModel.setUseId(paramResultSet.getLong("USER_ID"));
    localSalaryModel.setUserDesigId(paramResultSet.getLong("USER_DESIG_ID"));
    localSalaryModel.setAreaId(paramResultSet.getLong("AREA_ID"));
    localSalaryModel.setActionDate(this.strUtil.checkString(paramResultSet.getString("ACTION_DATE")));
    localSalaryModel.setArrearRemark(this.strUtil.checkString(paramResultSet.getString("ARREAR_REMARK")));
    localSalaryModel.setDeductOtherRemark(this.strUtil.checkString(paramResultSet.getString("DEDUCT_OTHER_REMARK")));
    localSalaryModel.setCommTarget(this.strUtil.checkString(paramResultSet.getString("COMM_TARGET")));
    localSalaryModel.setCommAmount(paramResultSet.getLong("COMM_AMT"));
    localSalaryModel.setCommRemark(this.strUtil.checkString(paramResultSet.getString("COMM_REMARK")));
    localSalaryModel.setIncrPurpose(this.strUtil.checkString(paramResultSet.getString("INCR_PURPOSE")));
    localSalaryModel.setIncrAmount(paramResultSet.getLong("INCR_AMOUNT"));
    localSalaryModel.setIncrRemark(paramResultSet.getString("INCR_REMARK"));
    localSalaryModel.setDeductHsRent(paramResultSet.getLong("DEDUCT_HS_RENT"));
    localSalaryModel.setDeductFdAmount(paramResultSet.getLong("DEDUCT_FD_AMT"));
    localSalaryModel.setDeductOtherAmt(paramResultSet.getLong("DEDUCT_OTHER_AMT"));
    localSalaryModel.setMonth(paramResultSet.getLong("SMONTH"));
    localSalaryModel.setYear(paramResultSet.getLong("SYEAR"));
    localSalaryModel.setArrearAmount(paramResultSet.getLong("ARREAR_AMT"));
    localSalaryModel.setActorId(paramResultSet.getLong("ACTOR_ID"));
    localSalaryModel.setUnauthLeave(paramResultSet.getLong("UNAUTH_LEAVE"));
    localSalaryModel.setDeductLeaveAmt(paramResultSet.getLong("DEDUCT_LEAVE_AMT"));
    localSalaryModel.setTransactionId(paramResultSet.getLong("TRANSACTION_ID"));
    localSalaryModel.setUserBasicSalary(paramResultSet.getDouble("USER_BASIC_SALARY"));
    localSalaryModel.setUserHouseRent(paramResultSet.getLong("USER_HOUSE_RENT"));
    localSalaryModel.setUserFoodAllowence(paramResultSet.getLong("USER_FOOD_ALLOWENCE"));
    localSalaryModel.setTotal(paramResultSet.getLong("TOTAL"));
    return localSalaryModel;
  }

  public List getObjectList(String paramString1, String paramString2)
    throws Exception
  {
    String str = "SELECT ID,USER_ID,USER_DESIG_ID,AREA_ID,to_char(ACTION_DATE,'dd-mm-rrrr') AS ACTION_DATE,ARREAR_REMARK,DEDUCT_OTHER_REMARK,COMM_TARGET,COMM_AMT,COMM_REMARK,INCR_PURPOSE,INCR_AMOUNT,INCR_REMARK,DEDUCT_HS_RENT,DEDUCT_FD_AMT,DEDUCT_OTHER_AMT,SMONTH,SYEAR,ARREAR_AMT,ACTOR_ID,UNAUTH_LEAVE,DEDUCT_LEAVE_AMT,TRANSACTION_ID,USER_BASIC_SALARY,USER_HOUSE_RENT,USER_FOOD_ALLOWENCE,TOTAL FROM USER_SALARY_RECORD@OMS_LINK";
    if ((paramString1 != null) && (paramString1.length() > 1))
      str = str + " WHERE " + paramString1;
    if ((paramString2 != null) && (paramString2.length() > 1))
      str = str + " ORDER BY " + paramString2;
    System.out.print(str);
    ResultSet localResultSet = this.stmt.executeQuery(str);
    SalaryModel localSalaryModel = null;
    ArrayList localArrayList = new ArrayList();
    while (localResultSet.next())
    {
      localSalaryModel = retrieveData(localResultSet);
      localArrayList.add(localSalaryModel);
    }
    localResultSet.close();
    return localArrayList;
  }

  public Hashtable getObjectMap(String paramString1, String paramString2)
    throws Exception
  {
    String str = "SELECT ID,USER_ID,USER_DESIG_ID,AREA_ID,to_char(ACTION_DATE,'dd-mm-rrrr') AS ACTION_DATE,ARREAR_REMARK,DEDUCT_OTHER_REMARK,COMM_TARGET,COMM_AMT,COMM_REMARK,INCR_PURPOSE,INCR_AMOUNT,INCR_REMARK,DEDUCT_HS_RENT,DEDUCT_FD_AMT,DEDUCT_OTHER_AMT,SMONTH,SYEAR,ARREAR_AMT,ACTOR_ID,UNAUTH_LEAVE,DEDUCT_LEAVE_AMT,TRANSACTION_ID,USER_BASIC_SALARY,USER_HOUSE_RENT,USER_FOOD_ALLOWENCE,TOTAL FROM USER_SALARY_RECORD@OMS_LINK";
    if ((paramString1 != null) && (paramString1.length() > 1))
      str = str + " WHERE " + paramString1;
    if ((paramString2 != null) && (paramString2.length() > 1))
      str = str + " ORDER BY " + paramString2;
    System.out.print(str);
    ResultSet localResultSet = this.stmt.executeQuery(str);
    SalaryModel localSalaryModel = null;
    Hashtable localHashtable = new Hashtable();
    while (localResultSet.next())
    {
      localSalaryModel = retrieveData(localResultSet);
      localHashtable.put("" + localSalaryModel.getUseId(), localSalaryModel);
    }
    localResultSet.close();
    return localHashtable;
  }

  public int update(Object paramObject)
    throws Exception
  {
    SalaryModel localSalaryModel = (SalaryModel)paramObject;
    String str = "UPDATE USER_SALARY_RECORD@OMS_LINK SET USER_ID = " + localSalaryModel.getUseId() + ", " + "USER_DESIG_ID = " + localSalaryModel.getUserDesigId() + ", " + "AREA_ID = " + localSalaryModel.getAreaId() + ", " + "ACTION_DATE = " + "sysdate," + "ARREAR_REMARK = " + "'" + localSalaryModel.getArrearRemark() + "'" + "," + "DEDUCT_OTHER_REMARK=" + "'" + localSalaryModel.getDeductOtherRemark() + "'" + "," + "COMM_TARGET=" + "'" + localSalaryModel.getCommTarget() + "'" + "," + "COMM_AMT=" + localSalaryModel.getCommAmount() + "," + "COMM_REMARK=" + "'" + localSalaryModel.getCommRemark() + "'" + "," + "INCR_PURPOSE=" + "'" + localSalaryModel.getIncrPurpose() + "'" + "," + "INCR_AMOUNT=" + localSalaryModel.getIncrAmount() + "," + "INCR_REMARK=" + "'" + localSalaryModel.getIncrRemark() + "'" + "," + "DEDUCT_HS_RENT=" + localSalaryModel.getDeductHsRent() + "," + "DEDUCT_FD_AMT=" + localSalaryModel.getDeductFdAmount() + "," + "DEDUCT_OTHER_AMT=" + localSalaryModel.getDeductOtherAmt() + "," + "SMONTH=" + localSalaryModel.getMonth() + "," + "SYEAR=" + localSalaryModel.getYear() + "," + "ARREAR_AMT=" + localSalaryModel.getArrearAmount() + "," + "ACTOR_ID=" + localSalaryModel.getActorId() + "," + "UNAUTH_LEAVE=" + localSalaryModel.getUnauthLeave() + "," + "DEDUCT_LEAVE_AMT=" + localSalaryModel.getDeductLeaveAmt() + ",TRANSACTION_ID=" + localSalaryModel.getTransactionId() + "," + "USER_BASIC_SALARY=" + localSalaryModel.getUserBasicSalary() + "," + "USER_HOUSE_RENT=" + localSalaryModel.getUserHouseRent() + "," + "USER_FOOD_ALLOWENCE=" + localSalaryModel.getUserFoodAllowence() + "," + "TOTAL=" + localSalaryModel.getTotal() + " WHERE ID = " + localSalaryModel.getId();
    System.out.println("update query--->" + str);
    int i = this.stmt.executeUpdate(str);
    return i;
  }

  public int update(int paramInt1, int paramInt2)
    throws Exception
  {
    String str = "UPDATE USER_SALARY_RECORD@OMS_LINK SET  TRANSACTION_ID = " + paramInt2 + " WHERE ID = " + paramInt1;
    int i = this.stmt.executeUpdate(str);
    System.out.println(str);
    return i;
  }

  public long save(Object paramObject)
    throws Exception
  {
    this.salaryModel = ((SalaryModel)paramObject);
    String str = "SELECT SALARY_SQ.NEXTVAL@OMS_LINK AS ID FROM DUAL";
    ResultSet localResultSet = this.stmt.executeQuery(str);
    if (localResultSet.next())
    {
      this.salaryID = localResultSet.getLong("ID");
      this.salaryModel.setId(this.salaryID);
    }
    if (this.salaryID > 0L)
    {
      str = "INSERT INTO USER_SALARY_RECORD@OMS_LINK(ID,USER_ID,USER_DESIG_ID,AREA_ID,ACTION_DATE,ARREAR_REMARK,DEDUCT_OTHER_REMARK,COMM_TARGET,COMM_AMT,COMM_REMARK,INCR_PURPOSE,INCR_AMOUNT,INCR_REMARK,DEDUCT_HS_RENT,DEDUCT_FD_AMT,DEDUCT_OTHER_AMT,SMONTH,SYEAR,ARREAR_AMT,ACTOR_ID,UNAUTH_LEAVE,DEDUCT_LEAVE_AMT,TRANSACTION_ID,USER_BASIC_SALARY,USER_HOUSE_RENT,USER_FOOD_ALLOWENCE,TOTAL)VALUES(" + this.salaryID + "," + this.salaryModel.getUseId() + "," + this.salaryModel.getUserDesigId() + "," + this.salaryModel.getAreaId() + "," + "sysdate," + "'" + this.strUtil.getDbString(this.salaryModel.getArrearRemark()) + "'" + "," + "'" + this.strUtil.getDbString(this.salaryModel.getDeductOtherRemark()) + "'" + "," + "'" + this.strUtil.getDbString(this.salaryModel.getCommTarget()) + "'" + "," + this.salaryModel.getCommAmount() + "," + "'" + this.strUtil.getDbString(this.salaryModel.getCommRemark()) + "'" + "," + "'" + this.strUtil.getDbString(this.salaryModel.getIncrPurpose()) + "'" + "," + this.salaryModel.getIncrAmount() + "," + "'" + this.strUtil.getDbString(this.salaryModel.getIncrRemark()) + "'" + "," + this.salaryModel.getDeductHsRent() + "," + this.salaryModel.getDeductFdAmount() + "," + this.salaryModel.getDeductOtherAmt() + "," + this.salaryModel.getMonth() + "," + this.salaryModel.getYear() + "," + this.salaryModel.getArrearAmount() + "," + this.salaryModel.getActorId() + "," + this.salaryModel.getUnauthLeave() + "," + this.salaryModel.getDeductLeaveAmt() + "," + this.salaryModel.getTransactionId() + ", " + this.salaryModel.getUserBasicSalary() + "," + this.salaryModel.getUserHouseRent() + "," + this.salaryModel.getUserFoodAllowence() + "," + this.salaryModel.getTotal() + " ";
      str = str + ")";
      System.out.println("query====>" + str);
      this.stmt.executeQuery(str);
      localResultSet.close();
    }
    return this.salaryID;
  }

  public int delete(String paramString1, String paramString2)
    throws Exception
  {
    int i = -1;
    String str1 = "UPDATE USER_SALARY_RECORD@OMS_LINK set ID=" + paramString1 + ",ACTOR_ID=" + paramString2;
    i = this.stmt.executeUpdate(str1);
    String str2 = "DELETE FROM USER_SALARY_RECORD@OMS_LINK WHERE ID=" + paramString1;
    i = this.stmt.executeUpdate(str2);
    return i;
  }

  public int deleteAll(String paramString1, String paramString2)
    throws Exception
  {
    int i = -1;
    String str1 = "UPDATE USER_SALARY_RECORD@OMS_LINK set ACTOR_ID=" + paramString2 + " WHERE " + paramString1;
    i = this.stmt.executeUpdate(str1);
    String str2 = "DELETE FROM USER_SALARY_RECORD@OMS_LINK WHERE " + paramString1;
    i = this.stmt.executeUpdate(str2);
    return i;
  }

  public List getIdUserMonth(int paramInt1, int paramInt2)
    throws Exception
  {
    String str = "select ID,SMONTH,SYEAR from USER_SALARY_RECORD@OMS_LINK where SMONTH=" + paramInt1 + " and SYEAR=" + paramInt2;
    ResultSet localResultSet = this.stmt.executeQuery(str);
    ArrayList localArrayList = new ArrayList();
    while (localResultSet.next())
      localArrayList.add("" + localResultSet.getInt("ID"));
    return localArrayList;
  }

  public int CheckMonthYear(int paramInt1, int paramInt2)
    throws Exception
  {
    int i = 1;
    String str = "select distinct smonth,syear from user_salary_record@OMS_LINK order by smonth asc";
    ResultSet localResultSet = this.stmt.executeQuery(str);
    int j = -1;
    int k = -1;
    while (localResultSet.next())
    {
      j = localResultSet.getInt("SMONTH");
      k = localResultSet.getInt("SYEAR");
      if ((paramInt1 == j) && (paramInt2 == k))
        continue;
      System.out.println(paramInt1 + ":" + j);
      System.out.println(paramInt2 + ":" + k);
      i = 0;
    }
    return i;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.dao.SalaryDAO
 * JD-Core Version:    0.6.0
 */