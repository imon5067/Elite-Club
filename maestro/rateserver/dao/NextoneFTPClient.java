package com.maestro.rateserver.dao;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPMessageCollector;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.enterprisedt.util.debug.FileAppender;
import com.enterprisedt.util.debug.Level;
import com.enterprisedt.util.debug.Logger;
import com.maestro.rateserver.common.AppConst;
import java.io.File;

public class NextoneFTPClient
{
  private static Logger log = Logger.getLogger("FTP CLASS");
  private static boolean appendLog;
  private static FTPClient ftp = null;
  FTPMessageCollector listener = null;
  String host = "ftp.gentel.net";
  String user = "mcc";
  String password = "dusifiy7";
  String localPath = AppConst.FILE_PATH_NEXTONE + File.separator + "cdr";
  String remotePath = "./";
  String logPath = AppConst.FILE_PATH_NEXTONE + File.separator + "log";

  public NextoneFTPClient()
  {
    try
    {
      ftp = new FTPClient();
      if (!appendLog)
      {
        Logger.addAppender(new FileAppender(this.logPath + File.separator + "ftp.log"));
        log.info("log file appended....");
        appendLog = true;
      }
    }
    catch (Exception localException)
    {
      log.error(localException.getMessage());
      localException.printStackTrace();
    }
  }

  public void connect()
    throws Exception
  {
    connect(this.host);
  }

  public void connect(String paramString)
    throws Exception
  {
    log.info("Connecting");
    ftp.setRemoteHost(paramString);
    this.host = paramString;
    this.listener = new FTPMessageCollector();
    ftp.setMessageListener(this.listener);
    ftp.connect();
    log.info("Connected");
  }

  public void login()
    throws Exception
  {
    login(this.user, this.password);
  }

  public void login(String paramString1, String paramString2)
    throws Exception
  {
    log.info("Logging in " + paramString1);
    this.user = paramString1;
    ftp.login(paramString1, paramString2);
  }

  public void disconnect()
    throws Exception
  {
    log.info("Disconnecting user " + this.user);
    ftp.quit();
    Logger.shutdown();
  }

  public void setLevel(Level paramLevel)
    throws Exception
  {
    Logger.setLevel(paramLevel);
  }

  public void setConnectMode(FTPConnectMode paramFTPConnectMode)
    throws Exception
  {
    log.debug("Connect Mode: " + paramFTPConnectMode.toString());
    ftp.setConnectMode(paramFTPConnectMode);
  }

  public void setTransferType(FTPTransferType paramFTPTransferType)
    throws Exception
  {
    log.debug("file transfer mode: " + paramFTPTransferType.toString());
    ftp.setType(paramFTPTransferType);
  }

  public void showFiles(String paramString, boolean paramBoolean)
    throws Exception
  {
    log.debug("Directory Contains:");
    String[] arrayOfString = ftp.dir(paramString, paramBoolean);
    for (int i = 0; i < arrayOfString.length; i++)
      log.info(arrayOfString[i]);
  }

  public String[] getFiles(String paramString, boolean paramBoolean)
    throws Exception
  {
    return ftp.dir(paramString, paramBoolean);
  }

  public void put(String paramString1, String paramString2)
    throws Exception
  {
    log.info("Putting local file (" + paramString1 + ") and saving in remote file (" + paramString2 + ")");
    ftp.put(paramString1, paramString2);
  }

  public void get(String paramString1, String paramString2)
    throws Exception
  {
    ftp.get(paramString1, paramString2);
  }

  public void getAll(String paramString1, String paramString2)
    throws Exception
  {
    String[] arrayOfString = getFiles(paramString2, false);
    for (int i = 0; i < arrayOfString.length; i++)
      try
      {
        get(paramString1 + arrayOfString[i], paramString2 + arrayOfString[i]);
      }
      catch (Exception localException)
      {
        log.error(localException.getMessage());
      }
  }

  public String getLog()
  {
    return this.listener.getLog();
  }

  public void clearLog()
  {
    this.listener.clearLog();
  }

  public void printLog(String paramString)
  {
    log.info(paramString);
  }

  public static void main(String[] paramArrayOfString)
  {
    try
    {
      NextoneFTPClient localNextoneFTPClient = new NextoneFTPClient();
      localNextoneFTPClient.connect();
      localNextoneFTPClient.login();
      localNextoneFTPClient.setLevel(Level.INFO);
      localNextoneFTPClient.setConnectMode(FTPConnectMode.PASV);
      localNextoneFTPClient.setTransferType(FTPTransferType.ASCII);
      localNextoneFTPClient.showFiles(".", false);
      localNextoneFTPClient.getAll(localNextoneFTPClient.localPath, localNextoneFTPClient.remotePath);
      localNextoneFTPClient.disconnect();
    }
    catch (Exception localException)
    {
      log.error("Failed", localException);
    }
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.dao.NextoneFTPClient
 * JD-Core Version:    0.6.0
 */