package com.maestro.crb.notification;

public class UserDTO
{
  private long id;
  private String userName;
  private String pass;
  private String firstName;
  private String lastName;
  private String userrole;
  private long currency_id;
  private String email;

  public long getId()
  {
    return this.id;
  }

  public void setId(long paramLong)
  {
    this.id = paramLong;
  }

  public String getUserName()
  {
    return this.userName;
  }

  public void setUserName(String paramString)
  {
    this.userName = paramString;
  }

  public String getPass()
  {
    return this.pass;
  }

  public void setPass(String paramString)
  {
    this.pass = paramString;
  }

  public String getFirstName()
  {
    return this.firstName;
  }

  public void setFirstName(String paramString)
  {
    this.firstName = paramString;
  }

  public String getLastName()
  {
    return this.lastName;
  }

  public void setLastName(String paramString)
  {
    this.lastName = paramString;
  }

  public String getUserrole()
  {
    return this.userrole;
  }

  public void setUserrole(String paramString)
  {
    this.userrole = paramString;
  }

  public long getCurrency_id()
  {
    return this.currency_id;
  }

  public void setCurrency_id(long paramLong)
  {
    this.currency_id = paramLong;
  }

  public String getEmail()
  {
    return this.email;
  }

  public void setEmail(String paramString)
  {
    this.email = paramString;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.crb.notification.UserDTO
 * JD-Core Version:    0.6.0
 */