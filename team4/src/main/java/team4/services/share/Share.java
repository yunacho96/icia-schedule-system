package team4.services.share;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import team4.database.mapper.ShareInterface;
import team4.services.auth.Encryption;
import team4.services.auth.ProjectUtils;
import team4.user.beans.AccessInfo;
import team4.user.beans.MailForm;
import team4.user.beans.TDetailsBean;
import team4.user.beans.TeamBean;
import team4.user.beans.UserBean;

@Service
public class Share implements ShareInterface{	
	@Autowired
	Encryption enc;
	@Autowired
	ProjectUtils pu;
	@Autowired
	SqlSessionTemplate sqlSession;
	@Autowired
	DataSourceTransactionManager tx;
	@Autowired
	JavaMailSenderImpl javaMail;
	
	private DefaultTransactionDefinition def;
	private TransactionStatus status;
	
	public Share() {}
	
	public List<TeamBean> getTeamList(TeamBean tb){
		return sqlSession.selectList("getTeamList", tb);
	}

	public List<TDetailsBean> getMemList(TDetailsBean db) {
		List<TDetailsBean> list;
		list = sqlSession.selectList("getMemList", db);
		
		for(int i=0;i < list.size();i++) {
			try {
				String mbName = enc.aesDecode(list.get(i).getMbName(), list.get(i).getMbId());
				list.get(i).setMbName(mbName);
			} catch (Exception e) {
				e.printStackTrace();	
			} 
		}
		return list;
	}
	
	@Transactional  // 에러가 뜨면 롤백할거라는 의미 , 트랜잭션처리하는 어노테이션  : (rollbackFor = Exception.class)  	
	public void addTeam(TeamBean tb) {
		TDetailsBean db = new TDetailsBean();
		
		try {
			db.setMbId((String)pu.getAttribute("uCode"));
			db.setCgCode("L");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		//select 팀코드  + 숫자값 증가 
		int index = this.getTeCode().get(0).getIndex() +1;
		String strIndex = index+"";
		String newIndex="";
		for(int i=0;i< 3-strIndex.length();i++) {
			newIndex += "0";
		}
		newIndex += strIndex;
		
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		
		/* Explicit Transaction configure*/
		this.setTransactionConf(TransactionDefinition.PROPAGATION_REQUIRED,TransactionDefinition.ISOLATION_READ_COMMITTED,false);
		
		try {
			tb.setTeCode(sdf.format(cal.getTime()) + newIndex);
			//tb.setTeCode("123456789");
			/* te 테이블에 새로운 팀 insert */
			this.insTeamTable(tb);
			/* td 테이블에 새로운 팀디테일 insert */
			db.setTeCode(tb.getTeCode());
			this.insTeamDetailsTable(db);
			this.setTransactionResult(true);
		}catch(Exception e) {
			this.setTransactionResult(false);
			e.printStackTrace();
		}
		
		
//		// te 테이블에 insert 
//		try {
//			tb.setMbId((String)pu.getAttribute("uCode"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if(this.insTeamTable(tb)) {
//			db.setTeCode(this.getTeCode()+"");
//			db.setMbId(tb.getMbId());
//			db.setCgCode("L");
//			System.out.println(db.getTeCode()+  " : " + db.getMbId());
//			if(this.insTeamDetailsTable(db)) {
//				System.out.println("팀디테일 오오호호 ");
//			}
//		}
		// td 테이블에 insert 
		// getTeamList 호출을 통한 갱신된 데이터 가져오기 
//		return this.getTeamList(tb);
	}
	
	// Transaction Configuration 
	private void setTransactionConf(int propagation, int isolationLevel, boolean isRead) {
		def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(propagation);
		def.setIsolationLevel(isolationLevel);
		def.setReadOnly(isRead);
		status = tx.getTransaction(def);
	}
	
	//Transaction Result
	private void setTransactionResult(boolean isCheck) {
		if(isCheck) {
			tx.commit(status);
		}else {
			tx.rollback(status);
		}
	}
	

	/* - 선언적 트래잭션  : aop 방식
	 * @Transactional 어노테이션을 이용해 클래스나 하위 메서드에 적용 가능 
	 * --> 반든시 관련된 메서드는 public ,default 접근제한자를 사용해야 한다.  
	 * - 명시적 트랜잭션 Programa Transaction : 우리가 사용할것 
	 * -- 환경설정
	 *   --> propagation : 전파방식 
	 *       트랜잭션 3개 insert1 --> insert2 --> delete 3 
	 *                  tran  <--   tran <--- tran : 하나의 묶음으로 만들겠다는 것임
	 *                  
	 *   --> isolation  : 격리수준 
	 *   
	 * */

	/*propagation
	 * - required : default value 이미 시작된 트랜잭션이 있으면 참여하고 없으면 새로 시작한다. 
	 * 					ins1{commit or rollback
	 *                  	ins2{ 얘도 같이 커밋 롤백 적용 
	 *                  	}
	 *                  } 
	 * - supportss : 이미 시작된 트랜잭션이 있는 경우 참여하고 그렇지 않으면 트랜잭션 없이 진행 
	 * - mandatory : 이미 시작된 트랜잭션이 있으면 참여하고 그렇지 않으면
	 *               예외를 발생 
	 * - requires_new : 모두 새로운 트랜잭션 
	 * - not_supported : 트랜잭션을 사용하지 않음  
	 * */ 
	
	/* isolation
	 *  동시에 여러 트랜잭션이 진행될 때 특정한 트랜잭션의 결과를 다른 트랜잭션에 노출시킬 방법 
	 *  - default : read_commited  커밋된거만 읽어라 
	 *  - read_uncommited : 가장 낮은 수준의 isolation : 커밋되지않은것도 읽어라 
	 *  - serialization : 가장 강력한 격리수준 
	 * */
	public boolean insTeamTable(TeamBean tb) {
		return this.convertToBoolean(sqlSession.insert("insTeamTable",tb));
	}
	public boolean insTeamDetailsTable(TDetailsBean db){
		return this.convertToBoolean(sqlSession.insert("insTeamDetailsTable",db));
	}

	public List<TeamBean> getTeCode() {
		return sqlSession.selectList("getTeCode");
//		String newNum="";
//		maxNum +=1;
//			for(int i =0 ; i <  3-length; i++){
//				newNum += "0";
//			}
//			newNum += maxNum;
//		}else {
//			maxNum=1;
//			for(int i =0 ; i <  2; i++){
//				newNum += "0";
//			}
//			newNum += maxNum;
	}
	
	public List<TDetailsBean> getFriends(TDetailsBean db) {
		List<TDetailsBean> list= sqlSession.selectList("getFriends",db);
		for(int i=0; i<list.size();i++) {
			try {
				list.get(i).setMbName(enc.aesDecode(list.get(i).getMbName(), list.get(i).getMbId()));
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return list;
	}
	
	private boolean convertToBoolean(int value) {
		return value>0? true: false;
	}

	public void addMember(List<TeamBean> tb) {
		
		tb.get(0).getTdetails().get(0).setEmail("whdbsk9520@naver.com");
		tb.get(0).getTdetails().get(1).setEmail("youuuuun9520@naver.com");
		
		this.friendsAuth(tb);
	}
	
	public void friendsAuth(List<TeamBean> tb) {
		String subject = "Invitation";
		String contents = "<a href ='http://192.168.1.107/mailAuth?teCode="+ tb.get(0).getTeCode() +"'>어서와 여기는 처음이지 ?</a>";
		
		String from = "dbsk9520@naver.com";
		String[] to = new String[tb.get(0).getTdetails().size()];
		for(int i=0; i< tb.get(0).getTdetails().size() ;i++) {
			to[i] = tb.get(0).getTdetails().get(i).getEmail();
		}
		
		MimeMessage mail = javaMail.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail,"UTF-8");
		
		try {
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(contents,true);
			javaMail.send(mail);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public void friendsAuth(MailForm mf) { // 지금은 to에 넣을 메일이 1개이므로 for문 없이 함 

		MimeMessage mail = javaMail.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail,"UTF-8");
		
		try {
			helper.setFrom(mf.getFrom());
			helper.setTo(mf.getTo());
			helper.setSubject(mf.getSubject());
			helper.setText(mf.getContents(),true);
			javaMail.send(mail);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public List<UserBean> searchFriend(UserBean ub) {
		List<UserBean> decList = sqlSession.selectList("selAllMemInfo");
		List<UserBean> newList = new ArrayList<UserBean>();
		
		for(int i=0;i<decList.size();i++) {
			try { // 이름과 메일을 복호화해서 다시 저장 
				decList.get(i).setUName(enc.aesDecode(decList.get(i).getUName(), decList.get(i).getUCode()));
				decList.get(i).setUMail(enc.aesDecode(decList.get(i).getUMail(), decList.get(i).getUCode()));
			} catch (Exception e) {e.printStackTrace();}
		}
		
		for(int i=0;i<decList.size();i++) {
			String amount = decList.get(i).getUName()+decList.get(i).getUCode();
			if(amount.contains(ub.getSearch())) {
				newList.add(decList.get(i));
			}
		}
//		List<UserBean> list = sqlSession.selectList("searchFriend",ub);
//		List<UserBean> newlist = list;
//		
//		for(int i=0; i<list.size();i++) {
//			//이름먼저 복호화해서 다시 리스트에 저장 
//			try {
//				list.get(i).setUName(enc.aesDecode(list.get(i).getUName(), list.get(i).getUCode()));
//			} catch (Exception e) {
//				e.printStackTrace();
//			} 
//			
//			System.out.println(list.get(i).getUCode()+ " 아이디  ");
//			System.out.println(list.get(i).getUName()+ " 이름  ");
//		
//			System.out.println(ub.getSearch()+" 검색한 단어");
//
//			String amount = list.get(i).getUName()+list.get(i).getUCode();
//			
//			if(list.get(i).getUCode().contains(ub.getSearch())) {
//
//				newlist.get(i).setUCode(list.get(i).getUCode());
//				newlist.get(i).setUName(list.get(i).getUName());
//			}else {System.out.println("컨테인즈를 인식못함");}
//		}
//		
//		for(int i=0;i<newlist.size();i++) {
//			System.out.println(newlist.get(i).getUCode()+" : 새로운 리스트출력 : " + newlist.get(i).getUName());
//		}
		
		return newList;
	}

	public Map<String,String> insWaitFriends(List<TDetailsBean> db) { // 친구를 팀에 초대 후 현황을 대기중으로 인서트 하는 부분 
		Map<String,String> map = new HashMap<String,String>();
		map.put("message","이미 신청한 친구입니다.");
		for(int i=0;i<db.size();i++) {
			try {
				db.get(i).setMbId((String)pu.getAttribute("uCode"));
				db.get(i).setCgCode("0");
				
				System.out.println(db.get(i).getRmbId() + " : 추가한 친구 아이디" );
				System.out.println(db.get(i).getMbId() + " : 내 아이디 " );
				System.out.println(db.get(i).getCgCode() + " : 대기현황이 0인거 확인" );
				
				
				if(this.convertToBoolean(sqlSession.insert("insWaitFriends",db.get(i)))){
					map.put("message","친구 신청이 완료되었습니다.");
				}
				
				
			} catch (Exception e) {e.printStackTrace();}
		}
		
		/* insert TDetailsBean */
		
		return map;
	}

	public Map<String,String> sendEmail(MailForm mf) { // 친구 검색시 없으면 이메일을 직접 써서 초대 메일을 보내는 부분 
		Map<String, String> map = new HashMap<String, String>();
		map.put("message","메일 전송이 실패하였습니다.");
		AccessInfo ai= new AccessInfo();
		
		try {
			ai.setUCode((String)pu.getAttribute("uCode"));
			mf.setTo(mf.getTo());
			mf.setSubject(ai.getUCode() + "님이 친구초대 메일을 발송하였습니다.");
			mf.setContents("<a href ='http://192.168.1.107/JoinForm'>친구 일정관리 홈페이지에 초대합니다.</a>");
			mf.setFrom("dbsk9520@naver.com");
//			mf.setFrom(enc.aesDecode(sqlSession.selectOne("selMemberInfo",ai), ai.getUCode()));
			map.put("message",  mf.getTo() + "님께 메일이 성공적으로 발송되었습니다.");
		} catch (Exception e) {e.printStackTrace();}
	
		this.friendsAuth(mf);
		return map;
	}

	public List<TDetailsBean> getInviteList() { // 친구 신청이 온 리스트를 출력해오는 부분 
		TDetailsBean tb = new TDetailsBean();
		try {
			tb.setRmbId((String)pu.getAttribute("uCode"));
			tb.setCgCode("0");
		} catch (Exception e) { e.printStackTrace();}
		
		return sqlSession.selectList("getInviteList",tb);
	}
	
	public List<TDetailsBean> getTeamInviteList() {
		return null;
	}

	public Map<String,String> upReqAccept(List<TDetailsBean> db) { // 친구를 수락하는 경우 
		Map<String,String> map = new HashMap<String,String>();
		
		for(int i=0; i<db.size();i++) {
			try {
				db.get(i).setRmbId((String)pu.getAttribute("uCode"));
				
				if(this.convertToBoolean(sqlSession.update("upReqAccept", db.get(i)))) {
					map.put("message", db.get(i).getMbId() +"님을 친구로 맞이하였습니다.");
				}else {map.put("message", db.get(i).getMbId() + "님을 친구로 맞이하는데 어려움이 있습니다.");}
				
			} catch (Exception e) {e.printStackTrace();}
		}
		return map;
	}

	public Map<String, String> upReqReject(List<TDetailsBean> db) {
		Map<String, String> map = new HashMap<String,String>();
		map.put("message", "친구 거절을 실패하였습니다.");
		
		for(int i=0;i<db.size();i++) {
			try {
				db.get(i).setRmbId((String)pu.getAttribute("uCode"));
				
				if(this.convertToBoolean(sqlSession.update("upReqReject",db.get(i)))) {
					map.put("message", db.get(i).getMbId() + "님을 친구 거절 처리하였습니다.");
				}else {map.put("message", db.get(i).getMbId() + "님을 친구 거절처리에 실패하였습니다.");}
				
			} catch (Exception e) {e.printStackTrace();}
		}
		return map;
	}



	




}
