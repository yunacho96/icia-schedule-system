<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="resources/css/test.css" type="text/css" rel="stylesheet" />
<script src="resources/js/schedule.js"></script>
</head>

<script>

function selectDay(day){
	selDay=day;
}
    window.onload = function() {
 
    function onClick() {
        document.querySelector('.modal_wrap').style.display ='block';
        document.querySelector('.black_bg').style.display ='block';
        let sday= document.querySelector("#input_date").value;
        
        
        
       	selectDay(sday); // 전역변수에 저장 
       	
        postAjax('schedule/TeamList','','getTCodeName','json');
       
    }   
    function offClick() {
        document.querySelector('.modal_wrap').style.display ='none';
        document.querySelector('.black_bg').style.display ='none';
    }
    document.getElementById('modal_btn').addEventListener('click', onClick);
    document.querySelector('.modal_close').addEventListener('click', offClick);
};

function getTCodeName(jsonData){
	let teamlist = document.getElementById("teamlist");
	let data = "<select name='tecode'>";
	for(i=0;i<jsonData.length;i++){
		data += "<option value='"+jsonData[i].teCode+"'>"+jsonData[i].teName+"</option>";
	}
	data += "</select><br>";
	data += "<input type='hidden' name='date' value= '"+selDay+"'>";
	data += "<input type='text' name='title' placeholder='일정 제목을 입력하세요.' /><br>";
	data += "<input type='text' name='contents' placeholder='일정 내용을 입력하세요.' /><br>";
	data += "<input type='text' name='location' placeholder='일정 위치' /><br>";
	data += "<select name='open' > <option value='O'>공개</option> <option value='N'>비공개</option></select> <br>";
	data += "<select name='loop' > <option value='W'>주</option> <option value='M'>월</option> <option value='Y'>년</option> </select><br>";
	data += "<input type='button' value='파일 추가하기' onClick='getFile()'/>";

	alert(jsonData[0].teCode + "확인 ");
	teamlist.innerHTML = data;
	console.log(jsonData);
}

function getFile(){
	let input = document.getElementById("getFile");
	let data = "<input type='file' name='mpFile'/>";
	data += "<input type='button' value='삭제'/>";
	input.innerHTML += data;	
}

function addSchedule(){
	
	let getSchedule = document.getElementsByName("getSchedule")[0];
	getSchedule.submit();}
	/* let tecode1 =  document.getElementById("tCode");
	let title1 = document.getElementsByName("title")[0];
	let date1 = document.getElementsByName("date")[0];
	let contents1 = document.getElementsByName("contents")[0];
	let location1 = document.getElementsByName("location")[0];
	let open1 = document.getElementsByName("open")[0];
	let loop1 = document.getElementsByName("loop")[0];
	let file = document.getElementsByName("mpFile");	
	let form = makeForm("schedule/insSchedule","post");
	
	form.enctype="multipart/form-data";
	form.appendChild(date1);
	form.appendChild(tecode1.options[tecode1.selectedIndex].value);
	form.appendChild(title1);
	form.appendChild(contents1);
	form.appendChild(location1);
	form.appendChild(open1);
	form.appendChild(loop1);
	
	for(i=file.length-1;i>=0;i--){
		form.appendChild(file[i]);
	}
	
	document.body.appendChild(form);
	form.submit(); */
	
	/* console.log(file.files.length + "길이 길이 기링 보이 ");
	if(files.files.length>3){
		files=null;
	}
	 */







</script>

<body>

<!-- <button type='button' id="modal_btn">모달창아 나와랏</button> -->
<input type="date" id="input_date"/>
<input type="button" id="modal_btn" value="확인"/>

	<div class="black_bg"></div>
	<div class="modal_wrap">
		<div class="modal_close">
			<a href="#">close</a>
		</div>
		<div>ADD SCHEDULE</div>
		
		<form action="insSchedule" method="post" enctype="multipart/form-data" name="getSchedule">
			<div id="teamlist"></div>

			<div id="getFile"></div>
			<div>
				<input type="button" value="전송" onClick="addSchedule()" />
			</div>
		</form>
		
	</div>


</body>
</html>