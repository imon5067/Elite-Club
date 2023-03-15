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
<script>
function save()
{
	//alert(document.Login_form.validation.value);
	if(document.Login_form.validation.value =='phone')
	{
		
			document.Login_form.action='confirm_pin_reset.jsp';
	}	
	else 
	{
		document.Login_form.action='reset_email_sent.jsp';
	}
	
	document.Login_form.submit();
	
}
</script>
    </head>
    <body>

   

        <!-- Landing Page Contents
        ================================================= -->
        <div id="lp-register">
            <div class="container wrapper">
                <div class="row">
                    <div class="col-sm-5">
                        <div class="intro-texts">
                            <h1 class="text-white">Join Architect Club !!!</h1>
                            <p>Why are you waiting for?</p>
                            <a href="register_club.html"> <button class="btn btn-primary">Sign Up</button></a>
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
                                    <h3>How do you want to reset your password?</h3>
                                    <p class="text-muted"> We found the following information associated with your account.</p>

                                    <!--Login Form-->
                                      <form name="Login_form" id='Login_form' method="post">									  
										  <input id="validation" type="radio" name="validation" value="phone" checked>
										  Text a code to my phone. 
										  <br>
										  <input id="validation" type="radio" name="validation" value="email" >
										  Email a link to my email Account. 
										  <br><br>
                                    
                                    <!--<a checked href="confirm_pin_reset.jsp"><button class="btn btn-primary">Continue</button></a>
									-->
								<button onclick="return save();" class="btn btn-primary">Continue</button>
									 </form><br><!--Login Form Ends--> 
								
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
