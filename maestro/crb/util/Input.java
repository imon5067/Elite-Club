package com.maestro.crb.util;

import java.util.StringTokenizer;
import javax.servlet.jsp.JspWriter;

public class Input
{
  public static boolean check(String paramString1, String paramString2, JspWriter paramJspWriter)
  {
    if ((paramString1 == null) || (paramString1.trim().length() == 0))
    {
      try
      {
        paramJspWriter.print("<p><br><font color=red><b><center><br>Please enter valid value for \"" + paramString2 + "\".<br><br>");
      }
      catch (Exception localException)
      {
      }
      return false;
    }
    return true;
  }

  public static boolean checkInt(String paramString1, String paramString2, JspWriter paramJspWriter)
  {
    try
    {
      int i = Integer.parseInt(paramString1);
      if (i <= 0)
      {
        paramJspWriter.print("<p><br><font color=red><b><center><br>Please enter round number for \"" + paramString2 + "\".<br><br>");
        return false;
      }
    }
    catch (Exception localException1)
    {
      try
      {
        paramJspWriter.print("<p><br><font color=red><b><center><br>Please enter round number for \"" + paramString2 + "\".<br><br>");
      }
      catch (Exception localException2)
      {
      }
      return false;
    }
    return true;
  }

  public static boolean checkLong(String paramString1, String paramString2, JspWriter paramJspWriter)
  {
    try
    {
      Long.parseLong(paramString1);
    }
    catch (Exception localException1)
    {
      try
      {
        paramJspWriter.print("<p><br><font color=red><b><center><br>Please enter round number for \"" + paramString2 + "\".<br><br>");
      }
      catch (Exception localException2)
      {
      }
      return false;
    }
    return true;
  }

  public static boolean checkDouble(String paramString1, String paramString2, JspWriter paramJspWriter)
  {
    try
    {
      Double.parseDouble(paramString1);
    }
    catch (Exception localException1)
    {
      try
      {
        paramJspWriter.print("<p><br><font color=red><b><center><br>Please enter number for \"" + paramString2 + "\".<br><br>");
      }
      catch (Exception localException2)
      {
      }
      return false;
    }
    return true;
  }

  public static boolean checkChar(String paramString1, String paramString2, JspWriter paramJspWriter)
  {
    if (!Character.isLetter(paramString1.charAt(0)))
    {
      try
      {
        paramJspWriter.print("<p><br><font color=red><b><center><br>Please enter A-Z or a-z for \"" + paramString2 + "\".<br><br>");
      }
      catch (Exception localException)
      {
      }
      return false;
    }
    return true;
  }

  public static boolean checkDateUS(String paramString1, String paramString2, JspWriter paramJspWriter)
  {
    boolean i = true;
    try
    {
      StringTokenizer localStringTokenizer = new StringTokenizer(paramString1, "/");
      if (localStringTokenizer.countTokens() != 3)
      {
        i = false;
      }
      else
      {
        String str1 = localStringTokenizer.nextToken();
        String str2 = localStringTokenizer.nextToken();
        String str3 = localStringTokenizer.nextToken();
        int j = Integer.parseInt(str1);
        int k = Integer.parseInt(str2);
        Integer.parseInt(str3);
        if ((j < 1) || (j > 12))
          i = false;
        if ((k < 1) || (k > 31))
          i = false;
        if (str3.trim().length() < 2)
          i = false;
      }
    }
    catch (Exception localException1)
    {
      i = false;
    }
    try
    {
      if (!i)
        paramJspWriter.print("<p><br><font color=red><b><center><br>Invalid \"" + paramString2 + "\". </b><br><font color=green>Use USA style date format: &nbsp;mm/dd/yyyy <br><br>");
    }
    catch (Exception localException2)
    {
    }
    return i;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.crb.util.Input
 * JD-Core Version:    0.6.0
 */