package com.maestro.util;

public class ApplicationException extends Exception
{
  public ApplicationException()
  {
  }

  public ApplicationException(String paramString)
  {
    super(paramString);
  }

  public ApplicationException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }

  public ApplicationException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.util.ApplicationException
 * JD-Core Version:    0.6.0
 */