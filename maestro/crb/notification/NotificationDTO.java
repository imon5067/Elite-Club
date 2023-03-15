package com.maestro.crb.notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationDTO
{
  private String notificationType;
  private String notificationSubject;
  private String notificationContent;
  private List notificationAttachments = new ArrayList();

  public String getNotificationType()
  {
    return this.notificationType;
  }

  public void setNotificationType(String paramString)
  {
    this.notificationType = paramString;
  }

  public String getNotificationSubject()
  {
    return this.notificationSubject;
  }

  public void setNotificationSubject(String paramString)
  {
    this.notificationSubject = paramString;
  }

  public String getNotificationContent()
  {
    return this.notificationContent;
  }

  public void setNotificationContent(String paramString)
  {
    this.notificationContent = paramString;
  }

  public List getNotificationAttachments()
  {
    return this.notificationAttachments;
  }

  public void setNotificationAttachments(List paramList)
  {
    this.notificationAttachments = paramList;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.crb.notification.NotificationDTO
 * JD-Core Version:    0.6.0
 */