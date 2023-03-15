package com.maestro.rateserver.dao;

import com.maestro.rateserver.model.OperationalUserModel;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OperationalUserDAO
{
  Statement stmt = null;

  public OperationalUserDAO(Statement paramStatement)
  {
    this.stmt = paramStatement;
  }

  public Object getObjectById(String paramString)
    throws Exception
  {
    String str = "SELECT * FROM OPERATIONAL_USER@OMS_LINK WHERE ID = " + paramString;
    ResultSet localResultSet = this.stmt.executeQuery(str);
    OperationalUserModel localOperationalUserModel = null;
    if (localResultSet.next())
      localOperationalUserModel = (OperationalUserModel)retrieveData(localResultSet);
    localResultSet.close();
    return localOperationalUserModel;
  }

  public List getObjectList(String paramString1, String paramString2)
    throws Exception
  {
    String str = " SELECT ID,USER_ID,SALARY,(select AREA_ID from OPERATIONAL_HIERARCHY@OMS_LINK where user_id=op.user_id and rownum<2 ) AREA_ID  ,ACTOR_ID,USER_ADDRESS_ID,USER_BASIC_SALARY,  USER_HOUSE_RENT,USER_FOOD_ALLOWENCE FROM OPERATIONAL_USER@OMS_LINK op ";
    if ((paramString1 != null) && (paramString1.length() > 1))
      str = str + " WHERE " + paramString1;
    if ((paramString2 != null) && (paramString2.length() > 1))
      str = str + " ORDER BY " + paramString2;
    ArrayList localArrayList = new ArrayList();
    ResultSet localResultSet = this.stmt.executeQuery(str);
    OperationalUserModel localOperationalUserModel = null;
    while (localResultSet.next())
    {
      localOperationalUserModel = (OperationalUserModel)retrieveData(localResultSet);
      localArrayList.add(localOperationalUserModel);
    }
    localResultSet.close();
    return localArrayList;
  }

  public void save(OperationalUserModel paramOperationalUserModel)
    throws Exception
  {
    long l = -1L;
    String str = "UPDATE OPERATIONAL_USER SET SENIORITY_LEVEL=SENIORITY_LEVEL+1 WHERE DESIGNATION= " + paramOperationalUserModel.getDesignationId() + " and SENIORITY_LEVEL >= " + paramOperationalUserModel.getSeniorityLevel();
    this.stmt.executeUpdate(str);
    str = "SELECT OPERATIONAL_USER_SQ.nextval FROM dual";
    ResultSet localResultSet = this.stmt.executeQuery(str);
    if (localResultSet.next())
    {
      l = localResultSet.getLong(1);
      if (l > 0L)
      {
        str = "INSERT INTO OPERATIONAL_USER(ID,DESIGNATION,SENIORITY_LEVEL,SALARY,USER_ID,AREA_ID,ACTOR_ID,USER_ADDRESS_ID,USER_BASIC_SALARY,USER_HOUSE_RENT,USER_FOOD_ALLOWENCE,RANK_ID) VALUES(" + l + ",'" + paramOperationalUserModel.getDesignationId() + "','" + paramOperationalUserModel.getSeniorityLevel() + "','" + paramOperationalUserModel.getSalary() + "','" + paramOperationalUserModel.getUserId() + "','" + paramOperationalUserModel.getAreaId() + "','" + paramOperationalUserModel.getActorId() + "','" + paramOperationalUserModel.getUserAddressId() + "','" + paramOperationalUserModel.getUserBasicSalary() + "','" + paramOperationalUserModel.getUserHouseRent() + "','" + paramOperationalUserModel.getUserFoodAllowence() + "'," + paramOperationalUserModel.getRankid() + " )";
        this.stmt.executeUpdate(str);
      }
    }
    localResultSet.close();
  }

  public int update(Object paramObject)
    throws Exception
  {
    OperationalUserModel localOperationalUserModel = (OperationalUserModel)paramObject;
    String str = "UPDATE OPERATIONAL_USER@OMS_LINK SET SALARY = " + localOperationalUserModel.getSalary() + ", " + "AREA_ID = " + localOperationalUserModel.getAreaId() + ", " + "ACTOR_ID = " + localOperationalUserModel.getActorId() + ", " + "USER_ADDRESS_ID = " + localOperationalUserModel.getUserAddressId() + ", " + "USER_ID = " + localOperationalUserModel.getUserId() + ", " + "USER_BASIC_SALARY = " + localOperationalUserModel.getUserBasicSalary() + ", " + "USER_HOUSE_RENT = " + localOperationalUserModel.getUserHouseRent() + ", " + "USER_FOOD_ALLOWENCE = " + localOperationalUserModel.getUserFoodAllowence() + " " + "WHERE ID = " + localOperationalUserModel.getId();
    int i = this.stmt.executeUpdate(str);
    return i;
  }

  public int delete(String paramString1, String paramString2)
    throws Exception
  {
    int i = -1;
    String str1 = "UPDATE OPERATIONAL_USER set ID=" + paramString1 + ",ACTOR_ID=" + paramString2;
    i = this.stmt.executeUpdate(str1);
    String str2 = "DELETE FROM OPERATIONAL_USER WHERE ID=" + paramString1;
    i = this.stmt.executeUpdate(str2);
    return i;
  }

  public int deleteAll(String paramString1, String paramString2)
    throws Exception
  {
    int i = -1;
    String str1 = "UPDATE OPERATIONAL_USER set ACTOR_ID=" + paramString2 + " WHERE " + paramString1;
    i = this.stmt.executeUpdate(str1);
    String str2 = "DELETE FROM OPERATIONAL_USER WHERE " + paramString1;
    i = this.stmt.executeUpdate(str2);
    return i;
  }

  public Object retrieveData(ResultSet paramResultSet)
    throws Exception
  {
    OperationalUserModel localOperationalUserModel = new OperationalUserModel();
    localOperationalUserModel.setId(paramResultSet.getLong("ID"));
    localOperationalUserModel.setUserId(paramResultSet.getLong("USER_ID"));
    localOperationalUserModel.setSalary(paramResultSet.getLong("SALARY"));
    localOperationalUserModel.setAreaId(paramResultSet.getLong("AREA_ID"));
    localOperationalUserModel.setActorId(paramResultSet.getLong("ACTOR_ID"));
    localOperationalUserModel.setUserAddressId(paramResultSet.getLong("USER_ADDRESS_ID"));
    localOperationalUserModel.setUserBasicSalary(paramResultSet.getLong("USER_BASIC_SALARY"));
    localOperationalUserModel.setUserHouseRent(paramResultSet.getLong("USER_HOUSE_RENT"));
    localOperationalUserModel.setUserFoodAllowence(paramResultSet.getLong("USER_FOOD_ALLOWENCE"));
    return localOperationalUserModel;
  }

  private int updateSiniorityLevels(OperationalUserModel paramOperationalUserModel1, OperationalUserModel paramOperationalUserModel2)
    throws Exception
  {
    int i = 0;
    int j = 0;
    String str="";
    ArrayList localArrayList=null;
    OperationalUserModel localOperationalUserModel;
    if (paramOperationalUserModel1.getSeniorityLevel() > paramOperationalUserModel2.getSeniorityLevel())
    {
      str = " (SENIORITY_LEVEL > " + paramOperationalUserModel2.getSeniorityLevel() + ") AND (SENIORITY_LEVEL <= " + paramOperationalUserModel1.getSeniorityLevel() + ") ";
      localArrayList = (ArrayList)getObjectList(str, "SENIORITY_LEVEL");
      for (j = 0; j < localArrayList.size(); j++)
      {
        localOperationalUserModel = (OperationalUserModel)localArrayList.get(j);
        localOperationalUserModel.setSeniorityLevel(localOperationalUserModel.getSeniorityLevel() - 1L);
        i += update2(localOperationalUserModel);
      }
    }
    str = " (SENIORITY_LEVEL >= " + paramOperationalUserModel1.getSeniorityLevel() + ") AND (SENIORITY_LEVEL < " + paramOperationalUserModel2.getSeniorityLevel() + ") ";
    localArrayList = (ArrayList)getObjectList(str, "SENIORITY_LEVEL");
    for ( j = 0; j < localArrayList.size(); j++)
    {
      localOperationalUserModel = (OperationalUserModel)localArrayList.get(j);
      localOperationalUserModel.setSeniorityLevel(localOperationalUserModel.getSeniorityLevel() + 1L);
      i += update2(localOperationalUserModel);
    }
    i += update2(paramOperationalUserModel1);
    return i;
  }

  private boolean siniorityLevelChanged(OperationalUserModel paramOperationalUserModel1, OperationalUserModel paramOperationalUserModel2)
  {
    return paramOperationalUserModel1.getSeniorityLevel() != paramOperationalUserModel2.getSeniorityLevel();
  }

  public int update2(Object paramObject)
    throws Exception
  {
    OperationalUserModel localOperationalUserModel = (OperationalUserModel)paramObject;
    String str = "UPDATE OPERATIONAL_USER SET SALARY = " + localOperationalUserModel.getSalary() + ", " + "AREA_ID = " + localOperationalUserModel.getAreaId() + ", " + "ACTOR_ID = " + localOperationalUserModel.getActorId() + ", " + "USER_ID = " + localOperationalUserModel.getUserId() + ", " + "USER_BASIC_SALARY = " + localOperationalUserModel.getUserBasicSalary() + ", " + "USER_HOUSE_RENT = " + localOperationalUserModel.getUserHouseRent() + ", " + "USER_FOOD_ALLOWENCE = " + localOperationalUserModel.getUserFoodAllowence() + ", " + "RANK_ID=" + localOperationalUserModel.getRankid() + " " + "WHERE ID = " + localOperationalUserModel.getId();
    int i = this.stmt.executeUpdate(str);
    return i;
  }

  public int updateOnlySiniorityLevel(Object paramObject)
    throws Exception
  {
    OperationalUserModel localOperationalUserModel = (OperationalUserModel)paramObject;
    String str = "UPDATE OPERATIONAL_USER SET SENIORITY_LEVEL = " + localOperationalUserModel.getSeniorityLevel() + " " + "WHERE ID = " + localOperationalUserModel.getId();
    int i = this.stmt.executeUpdate(str);
    return i;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.dao.OperationalUserDAO
 * JD-Core Version:    0.6.0
 */