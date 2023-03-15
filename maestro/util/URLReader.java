package com.maestro.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLReader
{
  public static int DEFAULT_CHUNK_SIZE = 256;

  public static String readURL(URL paramURL)
    throws MalformedURLException, IOException
  {
    String str1 = "";
    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramURL.openStream()));
    String str2;
    while ((str2 = localBufferedReader.readLine()) != null)
      str1 = str1 + str2 + "\n";
    localBufferedReader.close();
    return str1;
  }

  public static byte[] loadBytesFromURL(URL paramURL)
    throws IOException
  {
    byte[] arrayOfByte = null;
    URLConnection localURLConnection = paramURL.openConnection();
    int i = localURLConnection.getContentLength();
    InputStream localInputStream = null;
    try
    {
      if ((localInputStream = localURLConnection.getInputStream()) != null)
        arrayOfByte = i != -1 ? loadBytesFromStreamForSize(localInputStream, i) : loadBytesFromStream(localInputStream);
    }
    finally
    {
      if (localInputStream != null)
        try
        {
          localInputStream.close();
        }
        catch (IOException localIOException)
        {
        }
    }
    return arrayOfByte;
  }

  private static byte[] loadBytesFromStreamForSize(InputStream paramInputStream, int paramInt)
    throws IOException
  {
    int j = 0;
    byte[] arrayOfByte = new byte[paramInt];
    int i;
    while ((i = paramInputStream.read(arrayOfByte, j, paramInt)) > 0)
    {
      paramInt -= i;
      j += i;
    }
    return arrayOfByte;
  }

  private static byte[] loadBytesFromStream(InputStream paramInputStream)
    throws IOException
  {
    return loadBytesFromStream(paramInputStream, DEFAULT_CHUNK_SIZE);
  }

  private static byte[] loadBytesFromStream(InputStream paramInputStream, int paramInt)
    throws IOException
  {
    if (paramInt < 1)
      paramInt = DEFAULT_CHUNK_SIZE;
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    byte[] arrayOfByte1 = new byte[paramInt];
    try
    {
      int i;
      while ((i = paramInputStream.read(arrayOfByte1, 0, paramInt)) > 0)
        localByteArrayOutputStream.write(arrayOfByte1, 0, i);
      byte[] arrayOfByte2 = localByteArrayOutputStream.toByteArray();
      byte[] arrayOfByte3 = arrayOfByte2;
      return arrayOfByte3;
    }
    finally
    {
      localByteArrayOutputStream.close();
      localByteArrayOutputStream = null;
    }
  //  throw localObject;
  }
}

/* Location:           D:\Projects\Asterisk\sip_maxim_1\sip_maxim\WEB-INF\classes\
 * Qualified Name:     com.maestro.directDial.util.URLReader
 * JD-Core Version:    0.6.0
 */