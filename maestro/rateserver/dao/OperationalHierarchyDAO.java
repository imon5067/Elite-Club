package com.maestro.rateserver.dao;

import com.maestro.rateserver.model.OperationalHierarchyModel;
import com.maestro.rateserver.util.StringUtil;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class OperationalHierarchyDAO
{
  Statement stmt = null;
  String tableName = "";
  StringUtil strUtil = null;

  public OperationalHierarchyDAO(Statement paramStatement, String paramString)
  {
    this.stmt = paramStatement;
    this.tableName = paramString;
  }

  public OperationalHierarchyDAO(Statement paramStatement)
  {
    this.stmt = paramStatement;
    this.tableName = this.tableName;
  }

  public int changeParentId(long paramLong1, long paramLong2)
    throws Exception
  {
    int i = -1;
    String str = "UPDATE OPERATIONAL_HIERARCHY SET PARENT_ID=" + paramLong2 + " WHERE PARENT_ID=" + paramLong1;
    i = this.stmt.executeUpdate(str);
    return i;
  }

  private OperationalHierarchyModel retrieveData(ResultSet paramResultSet)
    throws Exception
  {
    OperationalHierarchyModel localOperationalHierarchyModel = new OperationalHierarchyModel();
    localOperationalHierarchyModel.setId(paramResultSet.getLong("id"));
    localOperationalHierarchyModel.setUserId(paramResultSet.getLong("user_id"));
    localOperationalHierarchyModel.setParentId(paramResultSet.getLong("parent_id"));
    localOperationalHierarchyModel.setOperationName(this.strUtil.checkString(paramResultSet.getString("operation_name")));
    localOperationalHierarchyModel.setOperationType(this.strUtil.checkString(paramResultSet.getString("OPERATION_TYPE")));
    localOperationalHierarchyModel.setDetails(this.strUtil.checkString(paramResultSet.getString("detail")));
    localOperationalHierarchyModel.setActorId(paramResultSet.getLong("actor_id"));
    localOperationalHierarchyModel.setAreaId(paramResultSet.getLong("area_id"));
    localOperationalHierarchyModel.setUserType(paramResultSet.getString("user_type"));
    localOperationalHierarchyModel.setUserAddressId(paramResultSet.getLong("USER_ADDRESS_ID"));
    localOperationalHierarchyModel.setEmail(this.strUtil.checkString(paramResultSet.getString("E_MAIL")));
    localOperationalHierarchyModel.setPhoneNo(this.strUtil.checkString(paramResultSet.getString("PHONE_NO")));
    localOperationalHierarchyModel.setMobileNo(this.strUtil.checkString(paramResultSet.getString("MOB_NO")));
    return localOperationalHierarchyModel;
  }

  public List getOperationHierarchy(String paramString1, String paramString2, String paramString3)
    throws Exception
  {
    boolean bool = true;
    return getOperationHierarchy(paramString1, paramString2, paramString3, bool);
  }

  public List getOperationHierarchy(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
    throws Exception
  {
    ArrayList localArrayList = new ArrayList();
    OperationalHierarchyModel localOperationalHierarchyModel1 = null;
    String str1 = "SELECT * FROM OPERATIONAL_HIERARCHY where area_Id in (select id from area_group where level_id=" + paramString1 + " and hierarchy like '%-" + paramString2 + "-%')";
    if ((paramString3 != null) && (paramString3.length() > 1))
      str1 = str1 + " AND " + paramString3;
    Hashtable localHashtable1 = new Hashtable();
    ResultSet localResultSet = this.stmt.executeQuery(str1);
    while (localResultSet.next())
    {
      localOperationalHierarchyModel1 = new OperationalHierarchyModel();
      localOperationalHierarchyModel1.setId(localResultSet.getLong("ID"));
      localOperationalHierarchyModel1.setUserId(localResultSet.getLong("USER_ID"));
      localOperationalHierarchyModel1.setParentId(localResultSet.getLong("PARENT_ID"));
      localOperationalHierarchyModel1.setOperationType(localResultSet.getString("OPERATION_TYPE"));
      localOperationalHierarchyModel1.setOperationName(localResultSet.getString("OPERATION_NAME"));
      localOperationalHierarchyModel1.setDetails(localResultSet.getString("DETAIL"));
      localOperationalHierarchyModel1.setAreaId(localResultSet.getLong("AREA_ID"));
      localOperationalHierarchyModel1.setActorId(localResultSet.getLong("ACTOR_ID"));
      localOperationalHierarchyModel1.setUserType(localResultSet.getString("USER_TYPE"));
      localOperationalHierarchyModel1.setUserAddressId(localResultSet.getLong("USER_ADDRESS_ID"));
      localOperationalHierarchyModel1.setEmail(localResultSet.getString("E_MAIL"));
      localOperationalHierarchyModel1.setMobileNo(localResultSet.getString("MOB_NO"));
      localOperationalHierarchyModel1.setPhoneNo(localResultSet.getString("PHONE_NO"));
      localHashtable1.put("" + localOperationalHierarchyModel1.getId(), localOperationalHierarchyModel1);
      localArrayList.add(localOperationalHierarchyModel1);
    }
    OperationalHierarchyModel localOperationalHierarchyModel2 = null;
    Hashtable localHashtable2 = getUserNames2(paramString1, paramString2, "");
    String str2 = "";
    OperationalHierarchyModel localOperationalHierarchyModel3 = new OperationalHierarchyModel();
    localOperationalHierarchyModel3.setId(0L);
    localOperationalHierarchyModel3.setDetails("root");
    localOperationalHierarchyModel3.setParentId(0L);
    for (int i = 0; i < localArrayList.size(); i++)
    {
      localOperationalHierarchyModel1 = (OperationalHierarchyModel)localArrayList.get(i);
      if (localHashtable2.containsKey("" + localOperationalHierarchyModel1.getId()))
      {
        if (paramBoolean)
          localOperationalHierarchyModel1.setDetails(localOperationalHierarchyModel1.getDetails() + "- " + (String)localHashtable2.get(new StringBuffer().append("").append(localOperationalHierarchyModel1.getId()).toString()));
        else
          localOperationalHierarchyModel1.setDetails(localOperationalHierarchyModel1.getDetails());
        localOperationalHierarchyModel1.setUserName((String)localHashtable2.get("" + localOperationalHierarchyModel1.getId()));
      }
      if (localHashtable1.containsKey("" + localOperationalHierarchyModel1.getParentId()))
      {
        localOperationalHierarchyModel2 = (OperationalHierarchyModel)localHashtable1.get("" + localOperationalHierarchyModel1.getParentId());
        localOperationalHierarchyModel1.setParent(localOperationalHierarchyModel2);
      }
      else
      {
        localOperationalHierarchyModel1.setParent(localOperationalHierarchyModel3);
      }
    }
    return localArrayList;
  }

  public Hashtable getUserNames(String paramString1, String paramString2)
    throws Exception
  {
    String str = "select distinct u.id as id, u.first_name||' '||u.last_name||' ('||u.username||'),EmpId '||o.id||',('||(select name from item@OMS_LINK where id=u.CURRENCY_ID )||')'  as fullname from oms_users@OMS_LINK u,operational_user@OMS_LINK o where u.id=o.user_id and area_id in (select id from area_group@OMS_LINK  start with " + (paramString1.equals("0") ? "PARENT_" : "") + "ID = " + paramString1 + " connect by prior id=PARENT_ID  )";
    System.out.println(">>>" + str);
    ResultSet localResultSet = this.stmt.executeQuery(str);
    Hashtable localHashtable = new Hashtable();
    while (localResultSet.next())
      localHashtable.put(localResultSet.getString("id"), localResultSet.getString("fullname"));
    return localHashtable;
  }

  public Hashtable getUserNames2(String paramString1, String paramString2, String paramString3)
    throws Exception
  {
    String str = "select distinct o.id as id, u.first_name||' '||u.last_name||' ('||u.username||'),EmpId '||ou.id  as fullname from oms_users u,operational_hierarchy o,operational_user ou where u.id=o.user_id  and u.id=ou.user_id and ou.area_id in (select id from area_group where level_id =" + paramString1 + " and hierarchy like '%-" + paramString2 + "-%')";
    ResultSet localResultSet = this.stmt.executeQuery(str);
    Hashtable localHashtable = new Hashtable();
    while (localResultSet.next())
      localHashtable.put(localResultSet.getString("id"), localResultSet.getString("fullname"));
    return localHashtable;
  }

  public Hashtable getOperationDetail()
    throws Exception
  {
    String str = "select id,DETAIL from OPERATIONAL_HIERARCHY";
    ResultSet localResultSet = this.stmt.executeQuery(str);
    Hashtable localHashtable = new Hashtable();
    while (localResultSet.next())
    {
      if (localResultSet.getString(2) == null)
        continue;
      localHashtable.put(localResultSet.getString(1), localResultSet.getString(2));
    }
    return localHashtable;
  }

  public OperationalHierarchyModel getObjectByUserId(String paramString1, String paramString2, String paramString3, String paramString4)
    throws Exception
  {
    String str = "SELECT * FROM OPERATIONAL_HIERARCHY where area_Id in (select id from area_group where level_id=" + paramString1 + " and hierarchy like '%-" + paramString2 + "-%') and user_id=" + paramString3 + " and operation_type='" + paramString4 + "'";
    ResultSet localResultSet = this.stmt.executeQuery(str);
    OperationalHierarchyModel localOperationalHierarchyModel = null;
    if (localResultSet.next())
      localOperationalHierarchyModel = retrieveData(localResultSet);
    localResultSet.close();
    return localOperationalHierarchyModel;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.dao.OperationalHierarchyDAO
 * JD-Core Version:    0.6.0
 */