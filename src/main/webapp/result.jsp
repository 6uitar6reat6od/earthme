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
			File source=null, result=null;
			if(session.getAttribute("source")!=null){
				source = (File) session.getAttribute("source");
			}
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
						<img src="<%=result.getPath() %>" height="400" width="400"/>
					</div>
					
					<!-- <div class="row">
						<canvas id="canvas" height="400" width="400"></canvas>
					</div>-->
					<!-- <br>
					<h4>From the original...</h4>
					<br>
					<img src="<%=source.getPath() %>"/>-->
				</div>
			</div>
		</div>
	</body>
	
	<!-- 
	<script type="text/javascript">
	//Coge el elemento Canvas
	var c=document.getElementById("canvas");
	var ctx = c.getContext("2d");
	var img = new Image();
	img.src = "";
	//c.width=c.width;
	//c.height=c.height;
	ctx.drawImage(img,10,10);
	c.width=img.width;
	//Crea la imagen
	img = new Image();
	//Carga el enlace de la imagen
	img.src = "";
	img.onload = function () {
		var ctx=c.getContext("2d");
		//Dibuja la imagen en el canvas
		ctx.drawImage(img,10,10);
		//coge los píxeles
		var imgData=ctx.getImageData(0,0,c.width,c.height);
		//Modifica el canal alfa de cada pixel
		for (var i=0;i<imgData.data.length;i+=4){
		    imgData.data[i+3]=40;
		}
		//Redibuja la imagen
		ctx.putImageData(imgData,0,0);
	}
	</script>
	-->
	
		<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	    <!-- Include all compiled plugins (below), or include individual files as needed -->
	    <script src="js/bootstrap.min.js"></script>
	
</html>	