// Date 객체 생성
let date = new Date();
let selDay;
const renderCalendar = () => {

	const viewYear = date.getFullYear();
	const viewMonth = date.getMonth();
	
	// year-month 채우기
	document.querySelector('.year-month').textContent = `${viewYear}년 ${viewMonth + 1}월`;
	
	// 지난 달 마지막 Date, 이번 달 마지막 Date
	const prevLast = new Date(viewYear, viewMonth, 0);
	const thisLast = new Date(viewYear, viewMonth + 1, 0);
	
	const PLDate = prevLast.getDate();
	const PLDay = prevLast.getDay();
	
	const TLDate = thisLast.getDate();
	const TLDay = thisLast.getDay();
	
	// Dates 기본 배열들
	const prevDates = [];
	const thisDates = [...Array(TLDate + 1).keys()].slice(1);
	const nextDates = [];
	
	 // prevDates 계산
	if (PLDay !== 6) {
	  for (let i = 0; i < PLDay + 1; i++) {
	    prevDates.unshift(PLDate - i);
	  }
	}
	
	// nextDates 계산
	for (let i = 1; i < 7 - TLDay; i++) {
	  nextDates.push(i);
	}
	
	 // Dates 합치기
	const dates = prevDates.concat(thisDates, nextDates);
	
	const firstDateIndex = dates.indexOf(1);
	  const lastDateIndex = dates.lastIndexOf(TLDate);
	console.log(date);
	 // Dates 정리
	dates.forEach((date, i) => {
		
	  const condition = i >= firstDateIndex && i < lastDateIndex + 1 ? 'this':'other';
	  dates[i] = "<div class ='date' onclick=\"choiceDay(this,\'"+(firstDateIndex+date-1)+"\',\'"+condition+"\')\"><span class =\'"+condition+"\'>"+date+"</span></div>";
	    //dates[i] = `<div class="date" onClick="test(this,${firstDateIndex+date-1},${condition})"><span class="${condition}"><input type="hidden" value="${condition}"/>${date}</span></div>`;	
		})
	
	document.querySelector('.dates').innerHTML = dates.join('');
	
	 const today = new Date();
	let index=-1;
    if (viewMonth === today.getMonth() && viewYear === today.getFullYear()) {
    for (let date of document.querySelectorAll('.this')) {
		index++;
      if (+date.innerText === today.getDate()) {
        date.classList.add('today');
		choiceDay(date,index,'this');
		break;
      }
    }
  } 

};
renderCalendar();

function prevMonth(){
  date.setMonth(date.getMonth() - 1);
  renderCalendar();
}

function nextMonth(){
  date.setMonth(date.getMonth() + 1);
  renderCalendar();
}

function goToday(){
  date = new Date();
  renderCalendar();
}


function choiceDay(obj,index,condition){
	if(condition=='other'){
		return;
	}
	onOffDate(index);
	let sideInfo = document.getElementById("sideInfo"); 
	sideInfo.innerHTML = "<div id ='chDate'>"+replaceDate(obj.innerText)+"</div>";
	selectDay(replaceDate(obj.innerText)); // 날짜 전역변수로 넣기 
	
	let clientData = {date:selDay};
	
	postAjax('schedule/getMonSchedule',JSON.stringify(clientData),'getMonSchedule','json');
	
}
function onOffDate(index){
	let date = document.getElementsByClassName("date");

	for(i=0; i<date.length; i++){
	date[i].style.backgroundColor="#FFFFFF";
	}
	date[index].style.backgroundColor="#ffe6d6";
}

function getMonSchedule(jsonData){
	console.log(jsonData);
	let todaySchedule = document.getElementById("todaySchedule");
	let data = "<div id='dayschedule'><div>Today Schedule <br><br>";
	for(i=0; i<jsonData.length;i++){
		data += "<div> 팀내 일정순서 : "+jsonData[i].num +"</div><br>";
		data += "<div> 팀이름 : "+jsonData[i].tename +"</div><br>";
		data += "<div> 일정 : "+jsonData[i].title +"</div><br>";
		data += "<div> 일정일 : "+jsonData[i].date +"</div><br>";
		data += "<div> 일정 등록 멤버 : "+jsonData[i].mbname +"</div><br><br>";
	}
	data+="</div></div>";
	todaySchedule.innerHTML = data;
}


function replaceDate(obj){
   let yearmonth = document.getElementsByClassName("year-month")[0];
   let month = yearmonth.innerText.split("년")[1];
   month = month.replace('월','');
   month = month.replace(' ','');
   if(month.length == 1){
      month = "-0"+month;
   }else{
      month = "-"+month;
   }
   
   let day = obj;
   if(day.length == 1){
      day = "-0"+ day;
   }else{
      day = "-"+day;
   }
   return yearmonth.innerText.split("년")[0]+month+day;
}

function modalClose() {
   let modal = document.getElementById("modal");
   modal.style.display = "none";
}
function modalOpen() {
   let modal = document.getElementById("modal");
   modal.style.display = "flex";

   postAjax('schedule/TeamList','','getTCodeName','json');
	
}
////////////////////////////////////////////


function selectDay(day){
	selDay=day;
}
   /*  window.onload = function() {
 
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
};*/

function getTCodeName(jsonData){
	let teamlist = document.getElementById("teamlist");
	let data = "<select name='tecode'>";
	for(i=0;i<jsonData.length;i++){
		data += "<option value='"+jsonData[i].teCode+"'>"+jsonData[i].teName+"</option>";
	}
	data += "</select><br><br>";
	data += "<input type='hidden' name='date' value= '"+selDay+"'>";
	data += "<input type='text' name='title' placeholder='일정 제목을 입력하세요.' /><br><br>";
	data += "<input type='text' name='contents' placeholder='일정 내용을 입력하세요.' /><br><br>";
	data += "<input type='text' name='location' placeholder='일정 위치' /><br><br>";
	data += "<select name='open' > <option value='O'>공개</option> <option value='N'>비공개</option></select> <br><br>";
	data += "<select name='loop' > <option value='W'>주</option> <option value='M'>월</option> <option value='Y'>년</option> </select><br><br>";
	data += "<input type='button' value='파일 추가하기' onClick='getFile()'/><br><br>";

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
	getSchedule.submit();
	}
	
	
function postAjax(jobCode, clientData, fn, type){
   //Step 1
   let ajax = new XMLHttpRequest();
   
   //Step 2
   ajax.onreadystatechange = function(){
      if(ajax.readyState == 4 && ajax.status == 200){
         //Step 5
		jsonData = JSON.parse(ajax.responseText);
        window[fn](jsonData);
		
		}
   };
   //https://api.ipify.org?format=json
   //Step 3   +=   <-- 앞의변수에  뒤에 값을  더해줌.
  	ajax.open("POST", jobCode);
	if(type=="json"){
		ajax.setRequestHeader("Content-type","application/json;");
	}else{
		ajax.setRequestHeader("Content-type","application/x-www-form-urlencoded;");
	}

   //Step 4 form처럼 전송
  //ajax.setRequestHeader("content-type", "application/x-www-form-urlencoded");
  //	ajax.setRequestHeader("content-type", "application/json");
 
 	ajax.send(clientData);
}
