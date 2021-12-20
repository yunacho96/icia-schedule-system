package team4.controller.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import team4.services.auth.ProjectUtils;
import team4.services.schedule.Schedule;
import team4.services.share.Share;
import team4.user.beans.AccessInfo;
import team4.user.beans.MailForm;
import team4.user.beans.ScheduleBean;
import team4.user.beans.TDetailsBean;
import team4.user.beans.TeamBean;
import team4.user.beans.UserBean;

@RestController
@RequestMapping("/schedule")
public class RestAPIController { // 페이지의 바디 부분만 바꿀때 사용하는 컨트롤러
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	Share sh;
	@Autowired
	Schedule sd;
	@Autowired
	ProjectUtils pu;

	@PostMapping("/TeamList")
	//@ModelAttribute  은 아작스에서 폼데이터로 넘길때만 자동주입이 된다 . 
	// 데이터만 리턴하므로 String type --> list<> 를 사용한 이유는 스트링은 어떤 데이터가 있는지 모르기에 직관적으로 보기 위해서
	public List<TeamBean> getTeam() {  
		//		System.out.println("RestAPIController 진입 성공");
		TeamBean tb = new TeamBean();
		//		List<TeamBean> teamList = new ArrayList<TeamBean>();
		try {
			tb.setMbId((String)pu.getAttribute("uCode"));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		//		tb.setTeCode("210723001");
		//		tb.setTeName("chobus-club");
		//		teamList.add(tb);
		return sh.getTeamList(tb);
	}

	@PostMapping("/MemberList")
	public List<TDetailsBean> getMember(@RequestBody TDetailsBean db){
		//TDetailsBean db = new TDetailsBean();
		//db.setTeCode("210724001");
		return sh.getMemList(db);
	}

	@PostMapping("/addTeam")
	public List<TeamBean> addTeam(@RequestBody TeamBean tb){
		sh.addTeam(tb);
		return this.getTeam();
	}
	@PostMapping("/member")
	public List<TDetailsBean> getFriends(){
		TDetailsBean db = new TDetailsBean();
		try {
			db.setMbId((String)pu.getAttribute("uCode"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sh.getFriends(db);
	} 
	@PostMapping("/inviteMember")
	public void addMember(@RequestBody List<TeamBean> tb) {
		sh.addMember(tb); 
	}
	
	@PostMapping("/acceptTeam")
	public void acceptTeam(@RequestBody TDetailsBean db) {
		System.out.println(db.getMbId() + " : 제발요 "+ db.getTeCode());
		//System.out.println(teCode + "tecode 확인이요 ~~~ " );
		db.setCgCode("G");
		sh.insTeamDetailsTable(db);
	}
	
	@PostMapping("/searchFriend")
	public List<UserBean> searchFriend(@RequestBody UserBean ub) {
		return sh.searchFriend(ub);
	}
	
	@PostMapping("/sendFriendInfo")
	public Map<String,String> insWaitFriends(@RequestBody List<TDetailsBean> db) {
		/* mbid 넘기는 곳 */
		return sh.insWaitFriends(db);
	}
	
	@GetMapping("/sendEmail")
	public Map<String,String> sendEmail(@ModelAttribute MailForm mf) {
		return sh.sendEmail(mf);	
	}
	
	@GetMapping("/getInviteList")
	public List<TDetailsBean> getInviteList() {
		return sh.getInviteList();
	}
	
	@GetMapping("/getTeamInviteList")
	public List<TDetailsBean> getTeamInviteList(){
		return sh.getTeamInviteList();
	}
	
	@PostMapping("/upReqAccept")
	public Map<String,String> upReqAccept(@RequestBody List<TDetailsBean> db){
		return sh.upReqAccept(db);
	}
	
	@PostMapping("/upReqReject")
	public Map<String,String> upReqReject(@RequestBody List<TDetailsBean> db){
		return sh.upReqReject(db);
	}


	@PostMapping("/getMonSchedule")
	public List<ScheduleBean> getMonSchedule(@RequestBody ScheduleBean sb) {
		
		return sd.getDaySchedule(sb);
	}

	
}
	
	

