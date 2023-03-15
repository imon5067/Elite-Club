package com.maestro.rateserver.util;

import java.io.PrintStream;
import java.util.List;

public class StringUtil
{
  public String checkString(String paramString)
  {
    if (paramString == null)
      paramString = "";
    else
      paramString = paramString.trim();
    if (paramString.equalsIgnoreCase("null"))
      paramString = "";
    return paramString;
  }

  public boolean isNullOrEmpty(String paramString)
  {
    return (paramString == null) || (paramString.trim().length() == 0) || (paramString.equalsIgnoreCase("null"));
  }

  public String printMessage(String paramString)
  {
    paramString = "<div class=style1>" + checkString(paramString) + "</div>";
    return paramString;
  }

  public String shortView(String paramString1, int paramInt, String paramString2)
  {
    paramString1 = checkString(paramString1);
    if (paramString1.length() == 0)
      return "<font title='Empty' color='#EDEDED' >...</font>";
    return paramString1.length() > paramInt ? "<span title='" + paramString1 + "' " + paramString2 + " >" + paramString1.substring(paramInt - 2) + ".." + "</span>" : paramString1;
  }

  public boolean isFound(String paramString1, String paramString2)
  {
    paramString1 = paramString1.trim().toUpperCase();
    paramString2 = paramString2.trim().toUpperCase();
    return (paramString1.startsWith(paramString2)) || (paramString1.indexOf(paramString2) > 0);
  }

  public String getDbString(String paramString)
  {
    paramString = checkString(paramString);
    if (paramString.indexOf("'") >= 0)
      paramString = paramString.replaceAll("'", "''");
    return paramString;
  }

  public static String checkValue(String paramString)
  {
    if ((null == paramString) || (paramString.equals("null")))
      return "";
    return paramString;
  }

  public static String checkValue(long paramLong)
  {
    if (paramLong == -1L)
      return "null";
    return paramLong + "";
  }

  public static String checkValue(int paramInt)
  {
    if (paramInt == -1)
      return "null";
    return paramInt + "";
  }

  public static int searchInList(List paramList, String paramString, boolean paramBoolean)
  {
    int i = -1;
    if ((paramList != null) && (paramList.size() > 0))
    {
      String str = "";
      int j = 0;
      for (int k = 0; k < paramList.size(); k++)
      {
        str = (String)paramList.get(k);
        System.out.print("position i: " + k + " containStr: " + str + " key: " + paramString + " Ret Value: ");
        if (paramBoolean)
        {
          if (!str.equals(paramString))
            continue;
          System.out.println(k);
          return k;
        }
        j = str.length() - paramString.length();
        if (j < 0)
        {
          if (!paramString.startsWith(str))
            continue;
          System.out.println(k);
          return k;
        }
        if (!str.startsWith(paramString))
          continue;
        System.out.println(k);
        return k;
      }
    }
    System.out.println(i);
    return i;
  }

  public static String[] getPartitionMonthYearFromPartitionName(String paramString)
  {
    String[] arrayOfString = new String[2];
    arrayOfString[0] = paramString.substring(1, 5);
    arrayOfString[1] = paramString.substring(5);
    return arrayOfString;
  }

  public String shortString(String paramString, int paramInt)
  {
    paramString = checkString(paramString);
    if (paramString.length() > paramInt)
    {
      paramString = paramString.substring(0, paramInt);
      paramString = paramString.concat("..");
    }
    return paramString;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.util.StringUtil
 * JD-Core Version:    0.6.0
 */