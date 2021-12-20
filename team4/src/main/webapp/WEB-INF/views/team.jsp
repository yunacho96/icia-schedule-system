<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="resources/js/schedule.js"></script>
<link
	href="${pageContext.request.contextPath}/resources/css/team.css"
	type="text/css" rel="stylesheet" />
<meta charset="UTF-8">
<title>teamList page</title>
<script type="text/javascript">
function callTeamList(){
	/* let sendJsonData = [];
	sendJsonData.push({mbId:"qwert7777"});
	let clientData = JSON.stringify(sendJsonData); */
	postAjax('/schedule/TeamList','', 'getTeamList','json');
}
function getTeamList(jsonData){
	let data="<div class='title'>Team List</div><br><br><br>";
	for(var i=0; i<jsonData.length ;i++){
		data = data + "<span class='teamList' onClick='callMemberList(" + jsonData[i].teCode  +")'>"+ (i+1) +") "+ jsonData[i].teName +"</span><br><br>";

	}
	let teamList = document.getElementById("teamlist");
	teamList.innerHTML = data;
}
function callMemberList(tecode){
	
	getTeCode(tecode); // tecode 전역변수로 저장하는 곳 
	alert(pTecode+"안녕하세요 ");
	
	let sendJsonData = {teCode:tecode};
	let clientData = JSON.stringify(sendJsonData);
	
	postAjax('/schedule/MemberList', clientData ,'getMemList','json');
}
function getMemList(jsonD){
	let data="<div class='title'>Member List</div><br><br><br>";
	for(var i=0; i<jsonD.length;i++){
		data = data + "<span class='memList'>"+ (i+1) +") "+ 
		jsonD[i].cgName+"멤버 : "+jsonD[i].mbName +"</span><br><br>";
	}
	data+= "<input type='button' class='submit' value='멤버 추가' onClick='callFriends()'/>"
	
	let memList = document.getElementById("memlist");
	memList.innerHTML = data;
}
function addTeam(){
	let teamName = prompt("Team Name을 입력해주세요. ");
	let clientData = {teName:teamName};
	postAjax('schedule/addTeam',JSON.stringify(clientData),'getTeamList','json');
}
function makeTeCode(jsonData){
	alert(jsonData);
}

function callFriends(){
	postAjax('/schedule/member','', 'getFriends','json');
}
function getFriends(jsonData){
	let data="<div class='title'>Friends List</div><br><br><br>";

	for(var i=0; i<jsonData.length ;i++){
		data = data + "<input type='checkbox' name='mbId' value= '"+ jsonData[i].mbId +"' class='teamList'>"+ jsonData[i].mbId +" : "+ jsonData[i].mbName+ "</><br><br>";	
	}
	data += "<input type='button' class='submit' value='초대하기' onClick='sendFriendsList()' />"
	let teamList = document.getElementById("friendslist");
	teamList.innerHTML = data;
}
function sendFriendsList(){
	let checkBoxes = document.getElementsByName("mbId");
	let sendJsonData = [];
	
	let tdetail = [];
/* 	sendJsonData.push({teCode:"210728001"});
	 */
	for(i=0;i<checkBoxes.length;i++){
		if(checkBoxes[i].checked){
			tdetail.push({mbId:checkBoxes[i].value});
			}
		}
	//[{"teCode":"210728001"},{"tdetails":"mbId:qqqqq123,mbId:ttttt123"}]
	
	sendJsonData.push({teCode:pTecode,tdetails:tdetail});

	let clientData = JSON.stringify(sendJsonData);
	alert(clientData);
	postAjax('/schedule/inviteMember',clientData,'getadd','json');
	
}
function getadd(jsonData){
	alert(jsonData);
}

</script>
<title>Team Manage</title>
</head>
<body > <!-- onLoad="callTeamList()" -->
	<div id="box">
		<div id="sbox1">
			<div>
				<input type="button" class="submit" value="새 팀 등록" onClick="addTeam()" />
			</div>
			<div>
				<input type="button" class="submit" value="팀 리스트" onClick="callTeamList()" />
			</div>
			<div>
				<input type="button" class="submit" value="내 친구 리스트" onClick="callFriends()" />
			</div>
		</div>
		<div id="sbox2" >
			<div id="teamlist"></div>
		</div>
		<div id="sbox2" >
			<div id="memlist"></div>
		</div>
		<div id="sbox2" >
			<div id="friendslist"></div>
			
		</div>
	</div>
</body>
</html>