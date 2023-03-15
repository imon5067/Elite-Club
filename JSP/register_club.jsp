<%@ page language="java" import="com.maestro.crb.orig_billing.info.Inf,com.maestro.crb.util.*,java.sql.*,java.util.*"%>
<%@page import="com.maestro.rateserver.common.AppConst"%>
<!DOCTYPE html>
<html lang="en">

    <!-- Mirrored from thunder-team.com/friend-finder/index-register.html by HTTrack Website Copier/3.x [XR&CO'2014], Tue, 12 Dec 2017 04:16:06 GMT -->
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="This is social network html5 template available in themeforest......" />
        <meta name="keywords" content="Social Network, Social Media, Make Friends, Newsfeed, Profile Page" />
        <meta name="robots" content="index, follow" />
        <title>elite_club</title>

        <!-- Stylesheets
        ================================================= -->
        <link rel="stylesheet" href="css/bootstrap.min.css" />
        <link rel="stylesheet" href="css/style.css" />
        <link rel="stylesheet" href="css/ionicons.min.css" />
        <link rel="stylesheet" href="css/font-awesome.min.css" />

        <!--Google Font-->
        <link href="https://fonts.googleapis.com/css?family=Lato:300,400,400i,700,700i" rel="stylesheet">

        <!--Favicon-->
        <link rel="shortcut icon" type="image/png" href="images/fav.png"/>
<script type="text/javascript">
function trim(stringToTrim) {
	return stringToTrim.replace(/^\s+|\s+$/g,"");
}
function ltrim(stringToTrim) {
	return stringToTrim.replace(/^\s+/,"");
}
function rtrim(stringToTrim) {
	return stringToTrim.replace(/\s+$/,"");
}

function save()
{
	document.getElementById("firstname").value = trim(document.getElementById("firstname").value);
	document.getElementById("lastname").value = trim(document.getElementById("lastname").value);
	document.getElementById("email").value = trim(document.getElementById("email").value);
	document.getElementById("password").value = trim(document.getElementById("password").value);
	if(document.getElementById("firstname").value.length<1)
	{
		alert('Enter Your First Name');
		return false;
	}	
	if(document.getElementById("lastname").value.length<1)
	{
		alert('Enter Your Last Name');
		return false;
	}	
	if(document.getElementById("email").value.length<1)
	{
		alert('Enter a Valid e-Mail Address');
		return false;
	}
	if(document.getElementById("password").value.length<1)
	{
		alert('Enter Password');
		return false;
	}
	if(document.getElementById("city").value == '-1')
	{
		alert('Select your City');
		return false;
	}
	if(document.getElementById("day").value == '-1')
	{
		alert('Enter Date of Birth correctly');
		return false;
	}
	if(document.getElementById("month").value == '-1')
	{
		alert('Enter Date of Birth correctly');
		return false;
	}
	if(document.getElementById("year").value == '-1')
	{
		alert('Enter Date of Birth correctly');
		return false;
	}
	if(document.getElementById("user_group").value == '-1')
	{
		alert('Select occupation');
		return false;
	}	if (confirm("Are You Sure to Save This Entry?"))
	{
		document.registration_form.submit();
		return true;
	}else
	{
		return false;
	}
	
}
</script>        
    </head>
    <body>
<%
String msg = (String)session.getAttribute("msg");

if(msg!=null)
   out.println(msg);
session.removeAttribute("msg");

java.sql.Statement stmt = null;
ResultSet rs = null;
com.maestro.crb.orig_billing.ConnectDB cdb=null;    
String qry="";
String theContent="";
String theContent_param="";

String theId ="";
String theName ="";

try
{
	cdb=new com.maestro.crb.orig_billing.ConnectDB();
	stmt=cdb.connect();
%>

        <!-- Header
        ================================================= -->

        <!--Header End-->

        <!-- Landing Page Contents
        ================================================= -->
        <div id="lp-register">
            <div class="container wrapper">
                <div class="row">
                    <div class="col-sm-5">
                        <div class="intro-texts">
                            <h1 class="text-white">Login Architect Club</h1>
                            <p>you have already an account.</p>
                           <a href="login.jsp"> <button class="btn btn-primary">Login</button></a>
                        </div>
                    </div>
                    <div class="col-sm-6 col-sm-offset-1">
                        <div class="reg-form-container">

                            <!-- Register/Login Tabs-->
                            <div class="reg-options">
                                <ul class="nav nav-tabs">


                                </ul><!--Tabs End-->
                            </div>

                            <!--Registration Form Contents-->
                            <div class="tab-content">
                                <div class="tab-pane active" id="register">
                                    <h3>Register Now !!!</h3>
                                    <p class="text-muted">Be cool and join today. Meet millions</p>

                                    <!--Register Form-->
                                    <form name="registration_form" id='registration_form' class="form-inline" action="save_registration.jsp">
                                        <div class="row">
                                            <div class="form-group col-xs-6">
                                                <label for="firstname" class="sr-only">First Name</label>
                                                <input id="firstname" class="form-control input-group-lg" type="text" name="firstname" title="Enter first name" placeholder="First name"/>
                                            </div>
                                            <div class="form-group col-xs-6">
                                                <label for="lastname" class="sr-only">Last Name</label>
                                                <input id="lastname" class="form-control input-group-lg" type="text" name="lastname" title="Enter last name" placeholder="Last name"/>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group col-xs-12">
                                                <label for="email" class="sr-only">Email</label>
                                                <input id="email" class="form-control input-group-lg" type="text" name="Email" title="Enter Email" placeholder="Your Email"/>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group col-xs-12">
                                                <label for="password" class="sr-only">Password</label>
                                                <input id="password" class="form-control input-group-lg" type="password" name="password" title="Enter password" placeholder="Password"/>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group col-xs-12">
                                                <label for="Occupation" class="sr-only"></label>
                                                <select class="form-control" name="user_group" id="user_group">
					       <option value="-1" disabled selected>Occupation</option>

<%
						qry = "Select id, name from user_group order by id";
						System.out.println(qry);
						rs =  stmt.executeQuery(qry);
						while(rs.next())
						{
							theId = rs.getString(1);
							theName = rs.getString(2);

%>
							<option value="<%=theId%>"><%=theName%></option>
<%
						}
%>                                                 </select>
                                            </div>                                        
                                         </div>                                        
                                        <div class="row">
                                            <p class="birth"><strong>Date of Birth</strong></p>
                                            <div class="form-group col-sm-3 col-xs-6">
                                                <label for="month" class="sr-only"></label>
                                                <select class="form-control" name="day" id="day">
                                                    <option value="-1" disabled selected>Day</option>
<%
						  for(int i=1; i<=31; i++)
						  {
%>
                                                    <option value="<%=i%>"><%=i%></option>
<%
						  }	
%>                                                     
                                                </select>
                                            </div>
                                            <div class="form-group col-sm-3 col-xs-6">
                                                <label for="month" class="sr-only"></label>
                                                <select class="form-control" name="month" id="month">
                                                    <option value="-1" disabled selected>Month</option>
                                                    <option value="01">Jan</option>
                                                    <option value="02">Feb</option>
                                                    <option value="03">Mar</option>
                                                    <option value="04">Apr</option>
                                                    <option value="05">May</option>
                                                    <option value="06">Jun</option>
                                                    <option value="07">Jul</option>
                                                    <option value="08">Aug</option>
                                                    <option value="09">Sep</option>
                                                    <option value="10">Oct</option>
                                                    <option value="11">Nov</option>
                                                    <option value="12">Dec</option>
                                                </select>
                                            </div>
                                            <div class="form-group col-sm-6 col-xs-12">
                                                <label for="year" class="sr-only"></label>
                                                <select class="form-control" name="year" id="year">
                                                    <option value="-1" disabled selected>Year</option>
<%
						  for(int i=1940; i<=2017; i++)
						  {
%>
                                                    <option value="<%=i%>"><%=i%></option>
<%
						  }	
%>                                                    
                                                  </select>
                                            </div>
                                        </div>
                                        <div class="form-group gender">
                                            <label class="radio-inline">
                                                <input type="radio" name="gender" checked value="Male">Male
                                            </label>
                                            <label class="radio-inline">
                                                <input type="radio" name="gender" value="Female">Female
                                            </label>

                                            
                                        </div>
                                        <div class="row">
                                            <div class="form-group col-xs-6">
                                                <label for="city" class="sr-only">City</label>
                                                <!--input id="city" class="form-control input-group-lg reg_name" type="text" name="city" title="Enter city" placeholder="Your city"/-->
                                                <select class="form-control" name="city" id="city">
                                                   <option value="-1">Select City</option>
<%
						qry = "Select id, city from city order by city";
						System.out.println(qry);
						rs =  stmt.executeQuery(qry);
						while(rs.next())
						{
							theId = rs.getString(1);
							theName = rs.getString(2);

%>
							<option value="<%=theId%>"><%=theName%></option>
<%
						}
%>                                                   
                                                   
                                                 </select>                      
                                            </div>
                                            <div class="form-group col-xs-6">
                                                <label for="country" class="sr-only"></label>
                                                <select class="form-control" name="country" id="country">
                                                    <option value="BD" selected>Bangladesh</option>
                                                </select>
                                            </div>
                                        </div>
                                    </form><!--Register Now Form Ends-->
                                    <p><input type="checkbox"><a href="">&nbsp;I have read and agree to the Terms and Conditions and Privacy Policy</a></p>
                                    <button class="btn btn-primary">Register Now</button>
                                </div><!--Registration Form Contents Ends-->

                                <!--Login-->

                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6 col-sm-offset-6">

                        <!--Social Icons-->
                        <ul class="list-inline social-icons">
                            <li><a href="#"><i class="icon ion-social-facebook"></i></a></li>
                            <li><a href="#"><i class="icon ion-social-twitter"></i></a></li>
                            <li><a href="#"><i class="icon ion-social-googleplus"></i></a></li>
                            <li><a href="#"><i class="icon ion-social-pinterest"></i></a></li>
                            <li><a href="#"><i class="icon ion-social-linkedin"></i></a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>


        ================================================= -->
        <script src="js/jquery-3.1.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.appear.min.js"></script>
        <script src="js/jquery.incremental-counter.js"></script>
        <script src="js/script.js"></script>
<%


}catch(Exception e)
{
	stmt.getConnection().rollback();
	e.printStackTrace();
	out.println(qry);
	System.out.println("Exp "+request.getRequestURI()+" : "+e.getMessage());
}
finally{
	try{
		cdb.close();
	}catch(Exception ex){out.print("Exception:"+ex.getMessage());}
}

%>
    </body>

    <!-- Mirrored from thunder-team.com/friend-finder/index-register.html by HTTrack Website Copier/3.x [XR&CO'2014], Tue, 12 Dec 2017 04:16:06 GMT -->
</html>
