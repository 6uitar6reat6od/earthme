<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" 
	import = "java.io.File"%>

<!DOCTYPE html>
<html lang="en">
	<head>
		<title>EarthMe!</title>
		<link rel="icon" type="image/png" href="img/favicon.png">
		
		<meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1">
		
	    <!-- Bootstrap -->
	    <link href="css/bootstrap.min.css" rel="stylesheet">
	    <link href="css/customcss.css" rel="stylesheet">
	</head>
	
	<body>
	
		<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
			<div class="container">
				<div class="navbar-header">
						<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
							<span class="sr-only">Toggle navigation</span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
						</button>
						<a class="navbar-brand logo" href="main.html" ><img style="max-width:100px; max-height:100px" src="./img/logo.png"/></a>
				</div>
				<div class="collapse navbar-collapse">
					<ul class="nav navbar-nav navbar-right">
						<li>
							<a href="main.html">Home</a>
						</li>
						<li>
							<a href="about.html">About</a>
						</li>
						<li>
							<a href="code.html">Code</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
			
		<%
			session = request.getSession();
			File result=null;
			if(session.getAttribute("result")!=null){
				result = (File) session.getAttribute("result");
			}
		%>
		
		<div class="container">
			<div class="row myPanel">
				<div class="jumbotron">
					<h3>Here is your result!</h3>
					<br>
					
					<div class="row">
						<img src="<%=result.getPath() %>" height="500" width="500"/>
					</div>
				</div>
			</div>
		</div>
	</body>
	
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>
	
</html>	
