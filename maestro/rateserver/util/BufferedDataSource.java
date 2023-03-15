package com.maestro.rateserver.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;

public class BufferedDataSource
  implements DataSource
{
  private byte[] _data;
  private String _name;

  public BufferedDataSource(byte[] paramArrayOfByte, String paramString)
  {
    this._data = paramArrayOfByte;
    this._name = paramString;
  }

  public String getContentType()
  {
    return "application/octet-stream";
  }

  public InputStream getInputStream()
    throws IOException
  {
    return new ByteArrayInputStream(this._data);
  }

  public String getName()
  {
    return this._name;
  }

  public OutputStream getOutputStream()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    localByteArrayOutputStream.write(this._data);
    return localByteArrayOutputStream;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.util.BufferedDataSource
 * JD-Core Version:    0.6.0
 */