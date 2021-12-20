<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="resources/js/schedule.js"></script>
<link
	href="${pageContext.request.contextPath}/resources/css/dashboard.css"
	type="text/css" rel="stylesheet" />

<title>My Page</title>
<script>
const message = "${message}";
	if(message !=""){
		alert(message);
	}

function team(){
	let uCode = document.getElementsByName("uCode")[0];
	let form = makeForm("team","post");
	form.appendChild(uCode);
	alert(uCode.value);
	document.body.appendChild(form);
	form.submit();
}
function acceptTeam(){
	let uCode = document.getElementsByName("uCode")[0];
	let id = uCode.value;
	let list = [];
	list.push({teCode:pTecode,mbId:id});
	let clientData = JSON.stringify(list);
	alert(clientData+"확인용 ");
	postAjax('schedule/acceptTeam',clientData,'','json');
}
function addFriend(){
	let form = makeForm("searchFriend","post");
	document.body.appendChild(form);
	form.submit();
}

function callInviteList(){
	getAjax('schedule/getInviteList','','getInviteList');
	getAjax('schedule/getTeamInviteList','','getTeamInviteList');
	
}

function getInviteList(jsonData){
	let list = document.getElementById("invitelist");
	let data = "<div class='title' >friend requests</div><br>";
	for(i=0;i<jsonData.length;i++){
		data +="<input type='checkbox' name='requests' class='invitelists' value='"+ jsonData[i].mbId +"'>request Id : "+ jsonData[i].mbId +"</><br>";
 	}
	data += "<input type='button' class='submit3' value='친구 수락' onClick='acceptReq()'/>";
	data += "<input type='button' class='submit3' value='친구 거절' onClick='rejectReq()'/>";
	list.innerHTML = data;
}

function getTeamInviteList(jsonData){
	let list = document.getElementById("teamInvitelist");
	let data = "<div class='title' >team requests</div><br>";
	for(i=0;i<jsonData.length;i++){
		data +="<input type='checkbox' name='requests' class='invitelists' value=''>request Id : </><br>";
 	}
	data += "<input type='button' class='submit3' value='친구 수락' onClick='acceptReq()'/>";
	data += "<input type='button' class='submit3' value='친구 거절' onClick='rejectReq()'/>";
	list.innerHTML = data;
}


function acceptReq(){
	let sendData = [];
	let checkBox = document.getElementsByName("requests");
	for(i=0;i<checkBox.length;i++){
		if(checkBox[i].checked){
			sendData.push({mbId:checkBox[i].value});
		}
	}
	let clientData = JSON.stringify(sendData);
	postAjax('schedule/upReqAccept',clientData,'acceptResult','json');
}
function acceptResult(jsonData){
	alert(jsonData.message);
}
function rejectReq(){
	let sendData=[];
	let checkBox = document.getElementsByName("requests");
	for(i=0;i<checkBox.length;i++){
		sendData.push({mbId:checkBox[i].value});
	}
	let clientData = JSON.stringify(sendData);
	postAjax('schedule/upReqReject',clientData,'rejectResult','json');
}
function rejectResult(jsonData){
	alert(jsonData.message);
}


</script>
</head>


<body onLoad="getAjax('https://api.ipify.org', 'format=json', 'setPublicIp')">

	<%-- <div>  ${uName }님 로그인 성공 </div>
	<div>  ${uName }님 이메일 : ${uMail }  </div> --%>
	<div class="main">
		<div class="title">TAK BUS</div>
		<div id="stitle">my page</div>
	</div>

	<div class="box">
	
	<div id="info">
		<img src="resources/images/${stickerPath}"
			style="width: 160px; height: auto; margin-bottom:30px;">
		<div >내 프로필</div>
		<div >____________</div>		
		<div>이름    :   ${uName }</div>
		<div>이메일   :  ${uMail }</div>

	<input type="button" class="submit2" value="로그아웃" onClick="sendLogoutInfo()" />
	<input type="hidden" name="uCode" value="${uCode}" />
	</div>
	
	<input type="date" />
	
	<div id="invitelist"></div>
	<div id="teamInvitelist"></div>
	
	<div id="main">
		<input type="button" class="submit" value="팀 관리" onClick="team()"/> 
		<input type="button" class="submit" value="스케줄 관리" onClick="scheduleList()"/>
		<input type="button" class="submit" value="친구 추가" onClick="addFriend()"/>
		<input type="button" class="submit" value="내 초대관리" onClick="callInviteList()"/>
	
		<div id="friendslist"></div>
	</div>
	
	</div>
	
</body>
</html>