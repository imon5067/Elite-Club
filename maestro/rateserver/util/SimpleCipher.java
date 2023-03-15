package com.maestro.rateserver.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class SimpleCipher
{
  Cipher ecipher;
  Cipher dcipher;
  byte[] salt = { -87, -101, -56, 50, 86, 53, -29, 3 };
  int iterationCount = 19;

  public SimpleCipher(String paramString)
  {
    try
    {
      PBEKeySpec localPBEKeySpec = new PBEKeySpec(paramString.toCharArray(), this.salt, this.iterationCount);
      SecretKey localSecretKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(localPBEKeySpec);
      this.ecipher = Cipher.getInstance(localSecretKey.getAlgorithm());
      this.dcipher = Cipher.getInstance(localSecretKey.getAlgorithm());
      PBEParameterSpec localPBEParameterSpec = new PBEParameterSpec(this.salt, this.iterationCount);
      this.ecipher.init(1, localSecretKey, localPBEParameterSpec);
      this.dcipher.init(2, localSecretKey, localPBEParameterSpec);
    }
    catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException)
    {
    }
    catch (InvalidKeySpecException localInvalidKeySpecException)
    {
    }
    catch (NoSuchPaddingException localNoSuchPaddingException)
    {
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
    }
    catch (InvalidKeyException localInvalidKeyException)
    {
    }
  }

  public String encrypt(String paramString)
  {
    try
    {
      byte[] arrayOfByte1 = paramString.getBytes("UTF8");
      byte[] arrayOfByte2 = this.ecipher.doFinal(arrayOfByte1);
      return new BASE64Encoder().encode(arrayOfByte2);
    }
    catch (BadPaddingException localBadPaddingException)
    {
    }
    catch (IllegalBlockSizeException localIllegalBlockSizeException)
    {
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    return null;
  }

  public String decrypt(String paramString)
  {
    try
    {
      byte[] arrayOfByte1 = new BASE64Decoder().decodeBuffer(paramString);
      byte[] arrayOfByte2 = this.dcipher.doFinal(arrayOfByte1);
      return new String(arrayOfByte2, "UTF8");
    }
    catch (BadPaddingException localBadPaddingException)
    {
    }
    catch (IllegalBlockSizeException localIllegalBlockSizeException)
    {
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    catch (IOException localIOException)
    {
    }
    return null;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.rateserver.util.SimpleCipher
 * JD-Core Version:    0.6.0
 */