package com.maestro.util;

public class LinkData
{
  public static final int MODULE_COMMON = 0;
  public static final int MODULE_IVR = 1;
  public static final int MODULE_SIP = 2;
  private String m_id;
  private String m_name;
  private LinkData m_parent;
  private boolean m_isGroup = false;
  private String m_serverLink;
  private int m_moduleId;

  public LinkData()
  {
  }

  public LinkData(String paramString)
  {
    this();
    this.m_id = paramString;
  }

  public String getId()
  {
    return this.m_id;
  }

  public void setId(String paramString)
  {
    this.m_id = paramString;
  }

  public String getName()
  {
    return this.m_name;
  }

  public void setName(String paramString)
  {
    this.m_name = paramString;
  }

  public void setGroup()
  {
    this.m_isGroup = true;
  }

  public boolean isGroup()
  {
    return this.m_isGroup;
  }

  public LinkData getParent()
  {
    return this.m_parent;
  }

  public void setParent(LinkData paramLinkData)
  {
    this.m_parent = paramLinkData;
  }

  public String getServerLink()
  {
    return this.m_serverLink;
  }

  public void setServerLink(String paramString)
  {
    this.m_serverLink = paramString;
  }

  public int getModuleId()
  {
    return this.m_moduleId;
  }

  public void setModuleId(int paramInt)
  {
    this.m_moduleId = paramInt;
  }

  public String getHierarchy()
  {
    String str = getId();
    if (null != getParent())
      str = getParent().getHierarchy() + "-" + getId();
    return str;
  }
}

/* Location:           D:\Projects\Asterisk\sip_maxim_1\sip_maxim\WEB-INF\classes\
 * Qualified Name:     com.maestro.util.LinkData
 * JD-Core Version:    0.6.0
 */