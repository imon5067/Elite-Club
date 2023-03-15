package com.maestro.rateserver.util;

import java.util.ArrayList;
import java.util.List;

public class CSV
{
  public static final char DEFAULT_SEP = ',';
  protected List list = new ArrayList();
  protected char fieldSep;

  public CSV()
  {
    this(',');
  }

  public CSV(char paramChar)
  {
    this.fieldSep = paramChar;
  }

  public List parse(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    this.list.clear();
    int i = 0;
    if (paramString.length() == 0)
    {
      this.list.add(paramString);
      return this.list;
    }
    do
    {
      localStringBuffer.setLength(0);
      if ((i < paramString.length()) && (paramString.charAt(i) == '"'))
      {
        i++;
        i = advQuoted(paramString, localStringBuffer, i);
      }
      else
      {
        i = advPlain(paramString, localStringBuffer, i);
      }
      this.list.add(localStringBuffer.toString());
      i++;
    }
    while (i < paramString.length());
    return this.list;
  }

  protected int advQuoted(String paramString, StringBuffer paramStringBuffer, int paramInt)
  {
    int j = paramString.length();
    int i;
    for (i = paramInt; i < j; i++)
    {
      if ((paramString.charAt(i) == '"') && (i + 1 < j))
      {
        if (paramString.charAt(i + 1) == '"')
        {
          i++;
        }
        else if (paramString.charAt(i + 1) == this.fieldSep)
        {
          i++;
          break;
        }
      }
      else
        if ((paramString.charAt(i) == '"') && (i + 1 == j))
          break;
      paramStringBuffer.append(paramString.charAt(i));
    }
    return i;
  }

  protected int advPlain(String paramString, StringBuffer paramStringBuffer, int paramInt)
  {
    int i = paramString.indexOf(this.fieldSep, paramInt);
    if (i == -1)
    {
      paramStringBuffer.append(paramString.substring(paramInt));
      return paramString.length();
    }
    paramStringBuffer.append(paramString.substring(paramInt, i));
    return i;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.util.CSV
 * JD-Core Version:    0.6.0
 */