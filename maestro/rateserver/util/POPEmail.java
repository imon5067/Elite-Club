package com.maestro.rateserver.util;

import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class POPEmail
{
  private String host;
  private String port = "110";
  private String user;
  private String password;
  private boolean ssl = false;
  private String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
  private Session session = null;
  private Store store = null;
  Folder folder = null;

  public POPEmail(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean)
  {
    this.host = paramString1;
    if (paramString2 != null)
      this.port = paramString2;
    this.user = paramString3;
    this.password = paramString4;
    this.ssl = paramBoolean;
  }

  public void connect()
    throws Exception
  {
    Properties localProperties = System.getProperties();
    if (this.ssl)
    {
      localProperties.setProperty("mail.pop3.socketFactory.class", this.SSL_FACTORY);
      localProperties.setProperty("mail.pop3.socketFactory.fallback", "false");
      localProperties.setProperty("mail.pop3.socketFactory.port", this.port);
    }
    localProperties.setProperty("mail.pop3.port", this.port);
    this.session = Session.getDefaultInstance(localProperties);
    this.store = this.session.getStore("pop3");
    this.store.connect(this.host, this.user, this.password);
    this.folder = this.store.getFolder("inbox");
    if (!this.folder.exists())
      throw new Exception("No Inbox...");
    this.folder.open(1);
  }

  public void disconnect()
  {
    try
    {
      this.folder.close(true);
    }
    catch (MessagingException localMessagingException1)
    {
      localMessagingException1.printStackTrace();
    }
    try
    {
      this.store.close();
    }
    catch (MessagingException localMessagingException2)
    {
      localMessagingException2.printStackTrace();
    }
  }

  public Message[] getMessages()
    throws Exception
  {
    if (this.folder == null)
      throw new Exception("[EMAIL EXCEPTION] Not connected, Connect first");
    return this.folder.getMessages();
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.util.POPEmail
 * JD-Core Version:    0.6.0
 */