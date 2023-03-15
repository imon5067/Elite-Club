package com.maestro.directDial.user;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserInfoDAO
{
  Statement stmt = null;
  String dblink = "";

  public UserInfoDAO(Statement paramStatement, String paramString)
  {
    this.stmt = paramStatement;
    this.dblink = paramString;
  }

  public Object getObjectById(String paramString)
    throws Exception
  {
    String str = "SELECT * FROM USER_INFO@" + this.dblink + " WHERE SB_ID=" + paramString;
    ResultSet localResultSet = this.stmt.executeQuery(str);
    UserInfoModel localUserInfoModel = null;
    if (localResultSet.next())
      localUserInfoModel = retrieveData(localResultSet);
    localResultSet.close();
    return localUserInfoModel;
  }

  public List getObjectList(String paramString1, String paramString2)
    throws Exception
  {
    String str = "SELECT * FROM USER_INFO@" + this.dblink;
    if ((paramString1 != null) && (paramString1.length() > 1))
      str = str + " WHERE " + paramString1;
    if ((paramString2 != null) && (paramString2.length() > 1))
      str = str + " ORDER BY " + paramString2;
    ResultSet localResultSet = this.stmt.executeQuery(str);
    UserInfoModel localUserInfoModel = null;
    ArrayList localArrayList = new ArrayList();
    while (localResultSet.next())
    {
      localUserInfoModel = retrieveData(localResultSet);
      localArrayList.add(localUserInfoModel);
    }
    localResultSet.close();
    return localArrayList;
  }

  public long save(UserInfoModel paramUserInfoModel)
    throws Exception
  {
    if (paramUserInfoModel.getSbId() > 0L)
    {
      String str = "INSERT INTO USER_INFO@" + this.dblink + "(SB_ID,FIRST_NAME,LAST_NAME,STREET,APT,CITY,ZIP_CODE,COUNTRY_ID,STATE,TIMEZONE_ID,EMAIL,PASSWORD,REGISTRATION_TIME, SB_STATUS,ACTOR,ME_ID,PREFERED_TIME) VALUES(" + paramUserInfoModel.getSbId() + ",'" + paramUserInfoModel.getFirstName() + "','" + paramUserInfoModel.getLastName() + "','" + paramUserInfoModel.getStreet() + "','" + paramUserInfoModel.getApt() + "','" + paramUserInfoModel.getCity() + "','" + paramUserInfoModel.getZipCode() + "','" + paramUserInfoModel.getCountryId() + "','" + paramUserInfoModel.getState() + "','" + paramUserInfoModel.getTimezoneId() + "','" + paramUserInfoModel.getEmail() + "','" + paramUserInfoModel.getPassword() + "', SYSDATE, 'New',''," + paramUserInfoModel.getMeId() + ",'" + paramUserInfoModel.getPreferedTime() + "')";
      this.stmt.executeUpdate(str);
    }
    return paramUserInfoModel.getSbId();
  }

  public int update(UserInfoModel paramUserInfoModel)
    throws Exception
  {
    int i = -1;
    String str = "UPDATE USER_INFO@" + this.dblink + " SET " + "FIRST_NAME= '" + paramUserInfoModel.getFirstName() + "'" + ", " + "LAST_NAME= '" + paramUserInfoModel.getLastName() + "'" + ", " + "STREET= '" + paramUserInfoModel.getStreet() + "'" + ", " + "APT= '" + paramUserInfoModel.getApt() + "'" + ", " + "CITY= '" + paramUserInfoModel.getCity() + "'" + ", " + "ZIP_CODE= '" + paramUserInfoModel.getZipCode() + "'" + ", " + "COUNTRY_ID= '" + paramUserInfoModel.getCountryId() + "'" + ", " + "STATE= '" + paramUserInfoModel.getState() + "'" + ", " + "TIMEZONE_ID= '" + paramUserInfoModel.getTimezoneId() + "'" + ", " + "EMAIL= '" + paramUserInfoModel.getEmail() + "'" + ", " + "PASSWORD= '" + paramUserInfoModel.getPassword() + "', " + "PREFERED_TIME= '" + paramUserInfoModel.getPreferedTime() + "' " + "WHERE SB_ID = " + paramUserInfoModel.getSbId();
    System.out.println(str);
    i = this.stmt.executeUpdate(str);
    return i;
  }

  public int delete(String paramString1, String paramString2)
    throws Exception
  {
    int i = -1;
    String str = "DELETE FROM USER_INFO@" + this.dblink + " WHERE SB_ID=" + paramString1;
    i = this.stmt.executeUpdate(str);
    return i;
  }

  public int deleteAll(String paramString1, String paramString2)
    throws Exception
  {
    int i = -1;
    String str = "DELETE FROM USER_INFO@" + this.dblink + " WHERE " + paramString1;
    i = this.stmt.executeUpdate(str);
    return i;
  }

  private UserInfoModel retrieveData(ResultSet paramResultSet)
    throws Exception
  {
    UserInfoModel localUserInfoModel = new UserInfoModel();
    localUserInfoModel.setSbId(paramResultSet.getLong("sb_id"));
    localUserInfoModel.setFirstName(paramResultSet.getString("first_name"));
    if ((localUserInfoModel.getFirstName() == null) || (localUserInfoModel.getFirstName().equals("null")))
      localUserInfoModel.setFirstName("");
    localUserInfoModel.setLastName(paramResultSet.getString("last_name"));
    if ((localUserInfoModel.getLastName() == null) || (localUserInfoModel.getLastName().equals("null")))
      localUserInfoModel.setLastName("");
    localUserInfoModel.setApt(paramResultSet.getString("apt"));
    if ((localUserInfoModel.getApt() == null) || (localUserInfoModel.getApt().equals("null")))
      localUserInfoModel.setApt("");
    localUserInfoModel.setStreet(paramResultSet.getString("street"));
    if ((localUserInfoModel.getStreet() == null) || (localUserInfoModel.getStreet().equals("null")))
      localUserInfoModel.setStreet("");
    localUserInfoModel.setCity(paramResultSet.getString("city"));
    if ((localUserInfoModel.getCity() == null) || (localUserInfoModel.getCity().equals("null")))
      localUserInfoModel.setCity("");
    localUserInfoModel.setZipCode(paramResultSet.getString("zip_code"));
    if ((localUserInfoModel.getZipCode() == null) || (localUserInfoModel.getZipCode().equals("null")))
      localUserInfoModel.setZipCode("");
    localUserInfoModel.setEmail(paramResultSet.getString("email"));
    if ((localUserInfoModel.getEmail() == null) || (localUserInfoModel.getEmail().equals("null")))
      localUserInfoModel.setEmail("");
    localUserInfoModel.setPassword(paramResultSet.getString("password"));
    if ((localUserInfoModel.getPassword() == null) || (localUserInfoModel.getPassword().equals("null")))
      localUserInfoModel.setPassword("");
    localUserInfoModel.setState(paramResultSet.getString("state"));
    if ((localUserInfoModel.getState() == null) || (localUserInfoModel.getState().equals("null")))
      localUserInfoModel.setState("");
    localUserInfoModel.setTimezoneId(paramResultSet.getLong("timezone_id"));
    localUserInfoModel.setCountryId(paramResultSet.getLong("country_id"));
    localUserInfoModel.setPreferedTime(paramResultSet.getString("PREFERED_TIME"));
    if ((localUserInfoModel.getPreferedTime() == null) || (localUserInfoModel.getPreferedTime().equals("null")))
      localUserInfoModel.setPreferedTime("");
    return localUserInfoModel;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.directDial.user.UserInfoDAO
 * JD-Core Version:    0.6.0
 */