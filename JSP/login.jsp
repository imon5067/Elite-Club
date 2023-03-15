<!DOCTYPE html>
<html lang="en">


    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="This is social network html5 template available in themeforest......" />
        <meta name="keywords" content="Social Network, Social Media, Make Friends, Newsfeed, Profile Page" />
        <meta name="robots" content="index, follow" />
        <title></title>

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
    </head>
    <body>

   <%
String linkName = request.getParameter("linkName");
if(linkName == null || linkName.equals("") || linkName.equals("null"))
{
	linkName = "HOME";
}
	if(linkName != null && linkName.equals("HOME"))
	{
%>
<%
		if(session.getAttribute("msg")!=null)
		{
			out.print(session.getAttribute("msg"));
			session.removeAttribute("msg");
		 }
	}
%>
    

        <!-- Landing Page Contents
        ================================================= -->
        <div id="lp-register">
            <div class="container wrapper">
                <div class="row">
                    <div class="col-sm-5">
                        <div class="intro-texts">
                            <h1 class="text-white">Join Architect Club !!!</h1>
                            <p>Why are you waiting for?</p>
                            <a href="register_club.jsp"> <button class="btn btn-primary">Sign Up</button></a>
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


                                <div class="tab-pane active" id="login">
                                    <h3>Login</h3>
                                    <p class="text-muted">Log into your account</p>

                                    <!--Login Form-->
                                    <form name="Login_form" id='Login_form'>
                                        <div class="row">
                                            <div class="form-group col-xs-12">
                                                <label for="my-email" class="sr-only">Email</label>
                                                <input id="my-email" class="form-control input-group-lg" type="text" name="Email" title="Enter Email" placeholder="Your Email"/>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group col-xs-12">
                                                <label for="my-password" class="sr-only">Password</label>
                                                <input id="my-password" class="form-control input-group-lg" type="password" name="password" title="Enter password" placeholder="Password"/>
                                            </div>
                                        </div>
                                    </form><!--Login Form Ends--> 
                                    <p><a href="begin_password_reset.jsp">Forgot Password?</a></p>
                                    <a href="validate.jsp"><button class="btn btn-primary">Login</button></a> 
                                </div>
                                
                                
                                
                                
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

        <script src="js/jquery-3.1.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.appear.min.js"></script>
        <script src="js/jquery.incremental-counter.js"></script>
        <script src="js/script.js"></script>

    </body>

   
</html>
