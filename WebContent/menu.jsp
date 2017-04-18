<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="/Map/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="/Map/bootstrap/css/bootstrap-theme.css" rel="stylesheet">
<link rel="shortcut icon" type="image/x-icon" href="/Map/img/direction.png" />
<style>
body {
	padding: 70px 0px;
}
</style>

<title>Direction</title>
</head>
<body>


	<!-- nenu -->
	<nav class="navbar navbar-inverse navbar-fixed-top"> 
		<% 
		if (session.getAttribute("username")==null){
			out.println("<script type=\"text/javascript\">");
		    out.println("window.location = \"/Map/account/logout.html\"");
			out.println("</script>");
		}
	%>
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="/Map/homepage.html"><script>document.write(self.location.host)</script></a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
           	  <li><a href="/Map/homepage.html">Home</a></li>
			  <li><a href="/Map/tracker/mapTracker.html">View Map</a></li>
			  <li><a href="/Map/tracker/shareLocation.html">Share your location</a></li>
			  <li><a href="/Map/tracker/listUserTracking.html">Tracking user</a></li>
			  <li><a href="/Map/tracker/road.html">Derection</a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            	<li><a href="/Map/user/<%out.print(session.getAttribute("username"));%>/"><%out.print(session.getAttribute("username"));%></a></li>
				<li><a href="/Map/account/logout.html">Log out</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
	</nav>

	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
	<script src="/Map/bootstrap/js/bootstrap.js"></script>
</body>
</html>