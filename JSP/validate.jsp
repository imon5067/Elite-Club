<%@ page language="java" import="com.maestro.crb.orig_billing.info.Inf,com.maestro.crb.util.*,java.sql.*,java.util.*"%>
<%@page import="com.maestro.rateserver.common.AppConst"%>
<html>
<head>
</head>
<body>
<font face="Verdana"><small>
<%
	//String sessionId = session.getId();
	HttpSession existingSess = request.getSession(false);
	if(null  != existingSess)
	{
 		System.out.println("prevSession::"+session.getId());
		existingSess.invalidate();
	}
	String sql = "";

   	session = request.getSession(true);
 	String sessionId = session.getId();
   	System.out.println("PostSession::"+session.getId());

	String username = request.getParameter("Email");
	String pass = request.getParameter("password");
	String oms_id = "";
	String name = "";
	String group = "";
	String role = "";

	if(username != null && pass != null)
	{
		username = username.trim();
		pass = pass.trim();
		if(pass.length() <= 0 || username.length() <= 0)
		{
		   out.println("<b><center>You must enter all the fields</center></b>");
		}
	}

	com.maestro.crb.orig_billing.ConnectDB cdb=null;
	java.sql.Statement stmt = null;

	try
	{
		cdb=new com.maestro.crb.orig_billing.ConnectDB();
		stmt=cdb.connect();

		String s11 ="select id, first_name||' '|| last_name name,"+
		" (Select name from userrole where id = role_id) role,(Select name from user_group where id = group_id) ugroup "+
		" from USERS ou where ou.USERNAME='" + username + "' and ou.PASS='"+pass+"' ";
		System.out.println(s11);
		ResultSet resultset = stmt.executeQuery(s11);
		boolean found = false, valid=false;

		if(resultset.next())
		{
			found = true;
			oms_id = resultset.getString("id");
			name = resultset.getString("name");
			group = resultset.getString("ugroup");
			role = resultset.getString("role");

			session.setAttribute("login_oms_id",oms_id);
			session.setAttribute("admin",username);
			session.setAttribute("role",role);
			session.setAttribute("name",name);
			session.setAttribute("group",group);
		}

		if(!found)
		{
			session.setAttribute("msg","<center><font face = Arial size = -1 color = #FF0000><b>Authentication Failed. Incorret username/password</b></font></center>");
			out.print("<script>location.replace(\"login.jsp\")</script>");        
		}
		
	
		out.print("<script>location.replace(\"mainpage.jsp?id="+sessionId+"&users=Y\")</script>");
	}
	catch(Exception runtimeexception)
	{
	    out.println(runtimeexception.getMessage());
	    runtimeexception.printStackTrace();
	}
	finally
	{
		try
		{
			stmt.close();
			cdb.close();
		}
		catch(Exception ee)
		{
		}
	}
%>
</body>
</html>
