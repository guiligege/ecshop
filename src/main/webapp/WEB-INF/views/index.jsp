<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>Bootstrap, from Twitter</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="description" content="">
    <meta name="author" content="">
 
	<%@include file="commons/config.jsp" %>
    <!-- Le styles -->
    <link href="css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .myinput{
      	padding-top: 10px;
      	width:418px;
      	/**margin-left:300px;**/
      	margin-top:10px;
      }
      .ecshopdiv{
     	text-align:center;
      }
      .innerdiv{
		 border-bottom:1px dashed #cccccc;
		 padding-left: 5px;
		}
	   .innerdiv:last-child{
		 border:none;
	   }	
      .outerdiv{
      		margin-left:266px;
      		margin-top:10px;
      		border:1px outset #F8F8F8;
      		width:432px;
      }
      .hide{
      	display:none;
      }
      .webname{
      		border-bottom:2px solid #7F5F97;
      		display:inline-block;
      		font-family:cursive;
      }
      .webdiv{
      		width:80px;
      }
    </style>
    <link href="css/bootstrap-responsive.css" rel="stylesheet">
    <script src="js/jquery.js"></script>
 
    <!-- HTML5 shim, for IE6-8 support of HTML5 elements 
    style="margin-left:302px;margin-right:318px;margin-top:10px;border:3px outset #F8F8F8;border-style: outset;display:none;"
    -->
    
    <!--[if lt IE 9]>
      <script src="../assets/js/html5shiv.js"></script>
    <![endif]-->
 
    <!-- Fav and touch icons -->
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="ico/apple-touch-icon-57-precomposed.png">
    <link rel="shortcut icon" href="ico/favicon.png">
    
  </head>
 
  <body>
 
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="#">Project name</a>
          <div class="nav-collapse collapse">
            <ul class="nav">
              <li class="active"><a href="#">Home</a></li>
              <li><a href="#about">About</a></li>
              <li><a href="#contact">Contact</a></li>
              <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <b class="caret"></b></a>
                <ul class="dropdown-menu">
                  <li><a href="#">Action</a></li>
                  <li><a href="#">Another action</a></li>
                  <li><a href="#">Something else here</a></li>
                  <li class="divider"></li>
                  <li class="nav-header">Nav header</li>
                  <li><a href="#">Separated link</a></li>
                  <li><a href="#">One more separated link</a></li>
                </ul>
              </li>
            </ul>
            <form class="navbar-form pull-right">
              <input class="span2" type="text" placeholder="Email">
              <input class="span2" type="password" placeholder="Password">
              <button type="submit" class="btn">Sign in</button>
            </form>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>
 
    <div class="container">
 
      <!-- Main hero unit for a primary marketing message or call to action -->
      <!-- <div class="hero-unit">
        <h1>Hello, world!</h1>
        <p>This is a template for a simple marketing or informational website. It includes a large callout called the hero unit and three supporting pieces of content. Use it as a starting point to create something more unique.</p>
        <p><a href="#" class="btn btn-primary btn-large">Learn more &raquo;</a></p>
      </div> -->
      <div class="hero-unit" style="background-color:#FFFFFF">
		      <div >
		       	   <h2 style="text-align:center;">查查<span class="webname">百度</span>查查</h2>
		      </div>
			  <div class="input-group" style="width:550px;margin:0 auto;position:relative;">
				  <input name="q" type="text" placeholder="" class="myinput" style="height:29px;"/>
				  <span >
				    <button class="btn btn-primary" type="submit" onclick="submitSearch();return false;" style="height:29px;width:95px;">查看一下</button>
				  </span>
				  <div class="outerdiv" style="margin:0;left:0;top:50px; background-color:#FFFFFF;display:none">
					  <div class="innerdiv" >卖家总评评分：<span id="prevscore"></span></div>
					  <div class="innerdiv" >产品总评评分：<span  id="productScore"></div>
					  <div class="innerdiv" >评论重复率评分：<span id="repeatScore"></div>
					  <div class="innerdiv" >总分：<span id="result"></div>
					  <div class="innerdiv">总数：<span  id="isless"></div>
				  </div>
			  </div>
			  
      </div>
      
      <div id="oDiv" class="hide">
	    <ul id="oUl">
	        <li class="hide">天猫</li>
	        <li class="hide">淘宝</li>
	       <!--  <li class="hide">京东</li>
	        <li class="hide">易迅</li> -->
	        <!-- <li class="hide">第5个li元素</li>
	        <li class="hide">第6个li元素</li>
	        <li class="hide">第7个li元素</li>
	        <li class="hide">第8个li元素</li> -->
	    </ul>
	  </div>
 
      <!-- Example row of columns -->
      <div class="row">
        <div class="span4">
          <h2>Heading</h2>
          <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
          <p><a class="btn" href="#">View details &raquo;</a></p>
        </div>
        <div class="span4">
          <h2>Heading</h2>
          <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
          <p><a class="btn" href="#">View details &raquo;</a></p>
       </div>
        <div class="span4">
          <h2>Heading</h2>
          <p>Donec sed odio dui. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Vestibulum id ligula porta felis euismod semper. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus.</p>
          <p><a class="btn" href="#">View details &raquo;</a></p>
        </div>
      </div>
 
      <hr>
 
      <footer>
        <p>&copy; Company 2013</p>
      </footer>
 
    </div> <!-- /container -->
 
    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="js/bootstrap-transition.js"></script>
    <script src="js/bootstrap-alert.js"></script>
    <script src="js/bootstrap-modal.js"></script>
    <script src="js/bootstrap-dropdown.js"></script>
    <script src="js/bootstrap-scrollspy.js"></script>
    <script src="js/bootstrap-tab.js"></script>
    <script src="js/bootstrap-tooltip.js"></script>
    <script src="js/bootstrap-popover.js"></script>
    <script src="js/bootstrap-button.js"></script>
    <script src="js/bootstrap-collapse.js"></script>
    <script src="js/bootstrap-carousel.js"></script>
    <script src="js/bootstrap-typeahead.js"></script>
    
    <script type="text/javascript">
		function autoScroll(obj, ul_bz){
			$(obj).find(ul_bz).animate({
				marginTop : "-25px"
			},500,function(){
				//$(this).css({marginTop : "0px"}).find("li:first").hide();
				$(this).css({marginTop : "0px"}).find("li:first").appendTo(this);
				//$(this).css({marginTop : "0px"}).find("li:first").show();
				$(".webname").html($(this).css({marginTop : "0px"}).find("li:first").html());
			});
		}
		//循环显示提示文字
		var i=1;
		function scrollText(){
			var allalert="看看什么情况呢";
			if(i>allalert.length){
				i=1;
			}
			var temp=allalert.substring(0,i);
			i=i+1;
			//$(".myinput").attr("placeholder",temp);
		}
		var intervalId1=setInterval('scrollText()',1000);
		var intervalId2=setInterval('autoScroll("#oDiv", "#oUl")',3000);
		function clearInputInterval(){
			 clearInterval(intervalId1);
		}
		//文档加载
		//提交请求
		function submitSearch(){
			var url=$(".myinput").val();
			$.ajax({
  				url:_contextPath+"/evaluate/evaluateProduct.htm",
  				type:"get",
  				data:{url:url},
  				dataType:"json",
  				async:false,
  				success:function(data){
  					//prevScore,productScore,repeatScore,result,isless
  					$("#prevscore").html(data.prevScore);
  					$("#productScore").html(data.productScore);
  					$("#repeatScore").html(data.repeatScore);
  					$("#result").html(data.result);
  					$("#isless").html(data.isless);
  					$(".outerdiv").show();
  				},
  				error:function(data){
  				
  				}
  			});
		}
	</script>
 
  </body>
</html>