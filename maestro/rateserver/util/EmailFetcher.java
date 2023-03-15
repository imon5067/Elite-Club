package com.maestro.rateserver.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import oracle.sql.CLOB;

public class EmailFetcher
{
  public void popEmail(Statement paramStatement, String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean, String paramString5)
    throws Exception
  {
    POPEmail localPOPEmail = new POPEmail(paramString1, paramString2, paramString3, paramString4, paramBoolean);
    localPOPEmail.connect();
    Message[] arrayOfMessage = localPOPEmail.getMessages();
    String str7 = "";
    Timestamp localTimestamp1 = null;
    int i = 100;
    Timestamp localTimestamp2 = new Timestamp(System.currentTimeMillis());
    String str10 = "";
    PreparedStatement localPreparedStatement = null;
    Date localDate2 = new Date();
    Date localDate3 = new Date();
    try
    {
      ResultSet localResultSet = null;
      str7 = "SELECT NVL(MAX(SEND_DATE),TO_DATE(TO_CHAR(SYSDATE,'dd-mm-rrrr')||' 00:00:00', 'dd-mm-rrrr hh24:mi:ss' ))  FROM EMAIL WHERE RECIPIENT LIKE '%" + paramString3 + "%'";
      System.out.println("Date SQL>" + str7);
      localResultSet = paramStatement.executeQuery(str7);
      if (localResultSet.next())
        localTimestamp1 = localResultSet.getTimestamp(1);
      localDate2.setTime(localTimestamp1.getTime() - 259200000L);
      localDate3.setTime(localTimestamp1.getTime());
      System.out.println("Last Send Date: " + localTimestamp1 + "\t\t\t" + localDate3);
      for (int j = arrayOfMessage.length - 1; j >= 0; j--)
      {
        System.out.println("=============" + j + "=============");
        String str1 = InternetAddress.toString(arrayOfMessage[j].getFrom());
        String str3 = InternetAddress.toString(arrayOfMessage[j].getReplyTo());
        String str2 = InternetAddress.toString(arrayOfMessage[j].getRecipients(Message.RecipientType.TO));
        String str8 = InternetAddress.toString(arrayOfMessage[j].getRecipients(Message.RecipientType.CC));
        Enumeration localEnumeration = arrayOfMessage[j].getAllHeaders();
        Header localHeader = null;
        String str11 = "";
        while (localEnumeration.hasMoreElements())
        {
          localHeader = (Header)localEnumeration.nextElement();
          if (localHeader.getName().equals("Email-Priority"))
            str11 = localHeader.getValue();
          System.out.println(">>>" + localHeader.getName());
        }
        String str9 = InternetAddress.toString(arrayOfMessage[j].getAllRecipients());
        Date localDate1 = arrayOfMessage[j].getSentDate();
        System.out.println("S\t" + localDate1 + "\tL\t" + localTimestamp1 + "\t\tP\t" + localDate2 + "   : " + localDate1.compareTo(localTimestamp1) + "\t\t: \t" + localDate1.compareTo(localDate2));
        if (localTimestamp1 != null)
          i = localDate1.compareTo(localTimestamp1);
        if (localDate1.compareTo(localDate2) <= 0)
          break;
        if (i <= 0)
          continue;
        String str4 = arrayOfMessage[j].getSubject();
        System.out.println("From: " + str1.trim());
        System.out.println("Reply-To: " + str3.trim());
        System.out.println("To: " + paramString3);
        System.out.println("Cc: " + str8);
        System.out.println("Date: " + localDate1);
        System.out.println("Subject: " + str4);
        System.out.println();
        String str5 = arrayOfMessage[j].getContentType();
        localTimestamp2.setTime(localDate1.getTime());
        str7 = " Select count(*) from admin where  case       when instr(EMAIL ,'>') > 0 then substr(substr(EMAIL,instr(EMAIL,'<')+1),0,length(substr(EMAIL,instr(EMAIL,'<')+1))-1)        when instr(EMAIL ,'>') =0 then EMAIL  end like  case     when instr('" + str1 + "' ,'>') > 0 then substr(substr('" + str1 + "',instr('" + str1 + "','<')+1),0,length(substr('" + str1 + "',instr('" + str1 + "','<')+1))-1) " + "    when instr('" + str1 + "' ,'>') = 0 then '" + str1 + "' " + " end ";
        localResultSet = paramStatement.executeQuery(str7);
        int k = 0;
        if (localResultSet.next())
          k = localResultSet.getInt(1);
        str7 = "SELECT EMAIL_SQ.nextval AS EMAIL_ID FROM DUAL";
        localResultSet = paramStatement.executeQuery(str7);
        if (localResultSet.next())
          str10 = localResultSet.getString("EMAIL_ID");
        if (k != 0)
          str7 = " INSERT INTO EMAIL(ID,SENDER,RECIPIENT,CC,SEND_DATE,SUBJECT,ALL_RECIPIENTS,E_CHECKED,PRIORITY) VALUES(" + str10 + ",?,?,?,?,?,?,'I','" + str11 + "')";
        else
          str7 = " INSERT INTO EMAIL(ID,SENDER,RECIPIENT,CC,SEND_DATE,SUBJECT,ALL_RECIPIENTS,E_CHECKED,PRIORITY) VALUES(" + str10 + ",?,?,?,?,?,?,'U','" + str11 + "')";
        System.out.println("insert_email::" + str7);
        localPreparedStatement = paramStatement.getConnection().prepareStatement(str7);
        CLOB localCLOB1 = CLOB.createTemporary(paramStatement.getConnection(), false, 2);
        localCLOB1.putString(1L, str8);
        CLOB localCLOB2 = CLOB.createTemporary(paramStatement.getConnection(), false, 2);
        localCLOB1.putString(1L, str9);
        localPreparedStatement.setString(1, str1);
        localPreparedStatement.setString(2, str2);
        localPreparedStatement.setClob(3, localCLOB1);
        localPreparedStatement.setTimestamp(4, localTimestamp2);
        localPreparedStatement.setString(5, str4);
        localPreparedStatement.setClob(6, localCLOB2);
        localPreparedStatement.execute();
        String str6;
        Object localObject1;
        if ((str5.indexOf("text/") != -1) || (str5.indexOf("multipart/") == -1))
        {
          System.out.println("Content Type: " + str5.trim());
          str6 = (String)arrayOfMessage[j].getContent();
          str7 = "INSERT INTO EMAIL_DTL(ID,EMAIL_ID,CONTENT_TYPE,CONTENTS) VALUES(EMAIL_SQ.nextval," + str10 + ",'" + str5 + "',?)";
          System.out.println("MM SQL>" + str7);
          localPreparedStatement = paramStatement.getConnection().prepareStatement(str7);
          localObject1 = CLOB.createTemporary(paramStatement.getConnection(), false, 2);
          ((CLOB)localObject1).putString(1L, str6);
          localPreparedStatement.setClob(1, (Clob)localObject1);
          localPreparedStatement.execute();
        }
        else
        {
          localObject1 = (Multipart)arrayOfMessage[j].getContent();
          for (int m = 0; m < ((Multipart)localObject1).getCount(); m++)
          {
            BodyPart localBodyPart = ((Multipart)localObject1).getBodyPart(m);
            str5 = localBodyPart.getContentType();
            Object localObject2;
            if (str5.indexOf("text/") != -1)
            {
              str6 = (String)localBodyPart.getContent();
              str6 = str6.replaceAll("'", "''");
              System.out.println("Content Type: " + str5.trim());
              str7 = "INSERT INTO EMAIL_DTL(ID,EMAIL_ID,CONTENT_TYPE,CONTENTS) VALUES(EMAIL_SQ.nextval," + str10 + ",'" + str5 + "',?)";
              System.out.println("MM SQL>" + str7);
              localPreparedStatement = paramStatement.getConnection().prepareStatement(str7);
              localObject2 = CLOB.createTemporary(paramStatement.getConnection(), false, 2);
              ((CLOB)localObject2).putString(1L, str6);
              localPreparedStatement.setClob(1, (Clob)localObject2);
              localPreparedStatement.execute();
            }
            else
            {
              Object localObject3;
              if (str5.indexOf("message/") != -1)
              {
                localObject2 = (MimeMessage)localBodyPart.getContent();
                str5 = ((MimeMessage)localObject2).getContentType();
                if (str5.indexOf("text/") != -1)
                {
                  str6 = (String)((MimeMessage)localObject2).getContent();
                  str7 = "INSERT INTO EMAIL_DTL(ID,EMAIL_ID,CONTENT_TYPE,CONTENTS) VALUES(EMAIL_SQ.nextval," + str10 + ",'" + str5 + "',?)";
                  System.out.println("MM SQL>" + str7);
                  localPreparedStatement = paramStatement.getConnection().prepareStatement(str7);
                  localObject3 = CLOB.createTemporary(paramStatement.getConnection(), false, 2);
                  ((CLOB)localObject3).putString(1L, str6);
                  localPreparedStatement.setClob(1, (Clob)localObject3);
                  localPreparedStatement.execute();
                }
                else
                {
                  System.out.println("TODO : With email type......" + str5);
                }
              }
              else
              {
                str5 = "ATTACHMENT";
                localObject2 = localBodyPart.getDataHandler();
                System.out.println("Content Type: " + str5.trim());
                if (((DataHandler)localObject2).getName() != null)
                {
                  System.out.println("File Name: " + ((DataHandler)localObject2).getName());
                  str6 = str10 + "_" + ((DataHandler)localObject2).getName();
                  localObject3 = new FileOutputStream(new File(paramString5 + File.separator + str6));
                  ((DataHandler)localObject2).writeTo((OutputStream)localObject3);
                  str7 = "INSERT INTO EMAIL_DTL(ID,EMAIL_ID,CONTENT_TYPE,CONTENTS) VALUES(EMAIL_SQ.nextval," + str10 + ",'" + str5 + "',?)";
                  localPreparedStatement = paramStatement.getConnection().prepareStatement(str7);
                  localPreparedStatement.setString(1, str6);
                  localPreparedStatement.execute();
                }
                else
                {
                  System.out.println("bla-bla-bla-bla");
                }
              }
            }
            paramStatement.getConnection().commit();
          }
        }
        System.out.println();
      }
      localPOPEmail.disconnect();
    }
    catch (Exception localException2)
    {
      throw new Exception("Exception in fetch email:" + localException2.getMessage());
    }
    finally
    {
      try
      {
        localPreparedStatement.close();
      }
      catch (Exception localException3)
      {
      }
    }
  }

  public void receiveEmails(Statement paramStatement, String paramString)
    throws Exception
  {
    ResultSet localResultSet = paramStatement.executeQuery("SELECT company_email_address, pop_host, pop_port, password, is_ssl FROM email_recv");
    Vector localVector = new Vector();
    while (localResultSet.next())
      localVector.add(new String[] { localResultSet.getString("company_email_address"), localResultSet.getString("pop_host"), localResultSet.getString("pop_port"), localResultSet.getString("password"), localResultSet.getString("is_ssl") });
    for (int i = 0; i < localVector.size(); i++)
    {
      String[] arrayOfString = (String[])localVector.get(i);
      System.out.println("EMAIL: " + arrayOfString[0]);
      System.out.println("HOST: " + arrayOfString[1]);
      System.out.println("PORT: " + arrayOfString[2]);
      System.out.println("PASS: " + arrayOfString[3]);
      System.out.println("SSL: " + arrayOfString[4]);
      SimpleCipher localSimpleCipher = new SimpleCipher("MAESTRO");
      arrayOfString[3] = localSimpleCipher.decrypt(arrayOfString[3]);
      try
      {
        popEmail(paramStatement, arrayOfString[1], arrayOfString[2], arrayOfString[0], arrayOfString[3], arrayOfString[4].equals("Y"), paramString);
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.util.EmailFetcher
 * JD-Core Version:    0.6.0
 */