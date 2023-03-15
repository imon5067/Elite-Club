package com.maestro.rateserver.email;

public class EmailDTO
{
  private String sender = null;
  private String receipient = null;
  private String cc = null;
  private String body = null;
  private String subject = null;
  private String[] attachments = null;

  public String getSender()
  {
    return this.sender;
  }

  public void setSender(String paramString)
  {
    this.sender = paramString;
  }

  public String getReceipient()
  {
    return this.receipient;
  }

  public void setReceipient(String paramString)
  {
    this.receipient = paramString;
  }

  public String getCc()
  {
    return this.cc;
  }

  public void setCc(String paramString)
  {
    this.cc = paramString;
  }

  public String getBody()
  {
    return this.body;
  }

  public void setBody(String paramString)
  {
    this.body = paramString;
  }

  public String getSubject()
  {
    return this.subject;
  }

  public void setSubject(String paramString)
  {
    this.subject = paramString;
  }

  public String[] getAttachments()
  {
    return this.attachments;
  }

  public void setAttachments(String[] paramArrayOfString)
  {
    this.attachments = paramArrayOfString;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.email.EmailDTO
 * JD-Core Version:    0.6.0
 */