<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" 
	import = "java.io.File"%>

<!DOCTYPE html>
<html lang="en">
	<head>
		<title>EarthMe!</title>
		<link rel="icon" type="image/png" href="img/favicon.png">
		
		<link rel="StyleSheet" href="css/bootstrap.css" type="text/css" />
		<link rel="StyleSteet" href="css/bootstrap-theme.css" type="text/css" />
		<meta
			charset="UTF-8"
			name="EarthMe!" />
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
						<li class="active">
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
			File source=null, source_res=null, result=null;
			if(session.getAttribute("source")!=null){
				source = (File) session.getAttribute("source");
			}
			if(session.getAttribute("source_res")!=null){
				source_res = (File) session.getAttribute("source_res");
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
					

					<!-- <div class="row">
						<img src="<%=result.getPath() %>"/>
					</div>-->
					
					<div class="row">
						<canvas id="canvas" height="400" width="400"></canvas>
					</div>
					<br>
					<h4>From the original...</h4>
					<br>
					<img src="<%=source.getPath() %>"/>
				</div>
			</div>
		</div>
	</body>
	
	<script type="text/javascript">
	//Coge el elemento Canvas
	var c=document.getElementById("canvas");
	var ctx = c.getContext("2d");
	var img = new Image();
	img.src = "<%= source_res.getPath()%>";
	//c.width=c.width;
	//c.height=c.height;
	ctx.drawImage(img,10,10);
	c.width=img.width;
	//Crea la imagen
	img = new Image();
	//Carga el enlace de la imagen
	img.src = "<%= result.getPath()%>";
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
	
	<script>
		(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
		(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
		m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
		})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
		
		ga('create', 'UA-50093906-1', 'earthme-e17.rhcloud.com');
		ga('send', 'pageview');
	</script>
	
</html>	