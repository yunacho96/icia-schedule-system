<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%-- href="${pageContext.request.contextPath}/resources/css/login.css" --%>
<link href="resources/css/login.css" type="text/css" rel="stylesheet" />
<script src="resources/js/schedule.js"></script>
<script type="application/javascript" src="https://api.ipify.org?format=jsonp&callback=getIP"></script>
<!-- <script type="application/javascript">
  function getIP(json) {
    document.write("My public IP address is: ", json.ip);
  }
</script> -->
<script>
 	const message = "${message}";
 	if(message !=""){
 		alert(message);
 	}
</script>


<title>LogIn Page</title>
</head>

<%-- <body onLoad = "callMessage('${message}')"> --%>

<body onLoad="getAjax('https://api.ipify.org', 'format=json', 'setPublicIp')">
	<div id="accesszone">
		
		<div class="title">TAK BUS</div>
		<div class="sTitle">schedule services</div>
		<div>
			<div>
				<input type="text" name="uCode" class="box" placeholder="아이디" />
			</div>
		</div>
		<div>
			<div>
				<input type="password" name="aCode" class="box" placeholder="비밀번호" />
			</div>
		</div>
		<div>
			<input type="button" class="submit" value="로그인" onClick="sendAccessInfo()">
		</div>
		<div style="text-align: center; color: #bbbbbb" class="found">
			아이디 찾기 | 비밀번호 찾기 | <a href="JoinForm" style="text-decoration: none; color: #bbbbbb">회원가입</a>
		</div>
		<!-- <input type="date" name="day"> -->
	</div>


</body>
</html>