<%@ page language="java" import="com.maestro.crb.orig_billing.info.Inf,com.maestro.crb.util.*,java.sql.*,java.util.*"%>
<%@page import="com.maestro.rateserver.common.AppConst"%>
<html>
<head>
</head>
<body>
<font face="Verdana"><small>
<%

	String qry = "";

	String firstname = request.getParameter("firstname");
	String lastname = request.getParameter("lastname");
	String Email = request.getParameter("Email");
	String password = request.getParameter("password");
	String day = request.getParameter("day");
	String month = request.getParameter("month");
	String year = request.getParameter("year");
	String gender = request.getParameter("gender");
	String validation = request.getParameter("validation");
	String city = request.getParameter("city");
	String country = request.getParameter("country");
	String group = request.getParameter("user_group");
	String role = "2";

	com.maestro.crb.orig_billing.ConnectDB cdb=null;
	java.sql.Statement stmt = null;

	try
	{
		cdb=new com.maestro.crb.orig_billing.ConnectDB();
		stmt=cdb.connect();

		qry ="Insert into users (id,first_name,last_name,username,pass,date_of_birth,gender,status,city_id,role_id,group_id) values "+
		" (users_sq.nextval,'"+firstname+"','"+lastname+"','"+Email+"','"+password+"',to_date('"+day+"/"+month+"/"+year+"','dd/mm/rrrr'),'"+gender+"',"+
		" 'Y',"+city+","+group+","+role+")";
		System.out.println(qry);
		stmt.executeUpdate(qry);

		stmt.getConnection().commit();
		msg="<center><font face = Arial size = 2 color = \"green\"><b>Registration Successful</b></font></center>";
		session.setAttribute("msg",msg);
		out.print("<script>location.replace(\"register_club.jsp?id="+sessionId+"&users=Y\")</script>");
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
