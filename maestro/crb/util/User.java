package com.maestro.crb.util;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;

public class User
{
  int user_id = -1;
  double in_credit_limit;
  double in_balance;
  double in_min_balance;
  double out_current_bill;
  double out_bill_limit;
  String uname = "";
  String table_prefix = null;
  Hashtable in_billing = new Hashtable();
  Hashtable out_billing = new Hashtable();

  public User(int paramInt, Statement paramStatement)
  {
    this.user_id = paramInt;
    try
    {
      ResultSet localResultSet = paramStatement.executeQuery("select in_balance,in_min_balance,in_credit_limit,out_current_bill,out_bill_limit,uname,table_prefix from users where id='" + this.user_id + "'");
      if (localResultSet.next())
        try
        {
          this.in_credit_limit = localResultSet.getDouble("in_credit_limit");
          this.in_balance = localResultSet.getDouble("in_balance");
          this.in_min_balance = localResultSet.getDouble("in_min_balance");
          this.out_current_bill = localResultSet.getDouble("out_current_bill");
          this.out_bill_limit = localResultSet.getDouble("out_bill_limit");
          this.uname = localResultSet.getString("uname");
          this.table_prefix = localResultSet.getString("table_prefix");
        }
        catch (Exception localException2)
        {
          System.out.println("Exception in User, getting values from databse :: " + localException2.getMessage());
        }
    }
    catch (Exception localException1)
    {
      System.out.println("Exception in User, update balance :: " + localException1.getMessage());
    }
    DBAccess localDBAccess = new DBAccess(paramStatement);
    this.in_billing = localDBAccess.getInBilling(this.user_id);
    this.out_billing = localDBAccess.getOutBilling(this.user_id);
  }

  public void update(int paramInt, double paramDouble, Statement paramStatement)
  {
    if (paramInt == 1)
    {
      this.out_current_bill += paramDouble;
      if (this.out_current_bill <= this.out_bill_limit);
    }
    else
    {
      this.in_balance -= paramDouble;
      if ((this.in_balance < this.in_min_balance) && (this.in_balance >= -1.0D * this.in_credit_limit));
    }
    try
    {
      paramStatement.executeUpdate("update users set in_balance ='" + this.in_balance + "',out_current_bill='" + this.out_current_bill + "'  where id='" + this.user_id + "'");
    }
    catch (Exception localException)
    {
      System.out.println("Exception in User, update balance :: " + localException.getMessage());
    }
  }

  public CodeRate getCodeRate(int paramInt, String paramString)
  {
    CodeRate localCodeRate = null;
    if (paramInt == 1)
      localCodeRate = (CodeRate)this.out_billing.get(paramString);
    else
      localCodeRate = (CodeRate)this.in_billing.get(paramString);
    return localCodeRate;
  }

  public String getUname()
  {
    return this.uname;
  }

  public double getCurrentBill()
  {
    return this.out_current_bill;
  }

  public double getInBalance()
  {
    return this.in_balance;
  }

  public String getTablePrefix()
  {
    return this.table_prefix;
  }
}

/* Location:           D:\Projects\crb\WEB-INF\classes\
 * Qualified Name:     com.maestro.crb.util.User
 * JD-Core Version:    0.6.0
 */