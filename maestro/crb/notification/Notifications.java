package com.maestro.crb.notification;

import java.util.List;

public class Notifications
{
  private List userList;
  private NotificationObjectDTO notificationObject;
  private List notificationList;

  public List getUserList()
  {
    return this.userList;
  }

  public void setUserList(List paramList)
  {
    this.userList = paramList;
  }

  public List getNotificationList()
  {
    return this.notificationList;
  }

  public void setNotificationList(List paramList)
  {
    this.notificationList = paramList;
  }

  public NotificationObjectDTO getNotificationObject()
  {
    return this.notificationObject;
  }

  public void setNotificationObject(NotificationObjectDTO paramNotificationObjectDTO)
  {
    this.notificationObject = paramNotificationObjectDTO;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.crb.notification.Notifications
 * JD-Core Version:    0.6.0
 */