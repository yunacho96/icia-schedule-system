package team4.services.schedule;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import team4.database.mapper.ScheduleInterface;
import team4.services.auth.Encryption;
import team4.services.auth.ProjectUtils;
import team4.user.beans.ScheduleBean;
import team4.user.beans.TeamBean;
import team4.user.beans.UserBean;

@Service
public class Schedule implements ScheduleInterface{

	@Autowired
	ProjectUtils pu;
	@Autowired
	SqlSessionTemplate sqlSession;
	@Autowired
	Encryption enc;
	
	public Schedule() {}
	
	public ModelAndView sendFiles(UserBean ub) {
		ModelAndView mav = new ModelAndView();
		ScheduleBean sb = new ScheduleBean();
		String[] list= pu.savingFile(ub.getMpFile());
		
		for(int i=0;i<list.length;i++) {
			sb.setStickerPath(list);
		}
		
		
		for(int i=0;i<sb.getStickerPath().length;i++) {
			sb.setTecode("210804001");
			sb.setNum(1);
			sb.setPath(sb.getStickerPath()[i]);
			
			mav.addObject("path", sb.getStickerPath()[i]);
//			if(this.insAlbum(sb)) {
//				System.out.println("인서트가 됨요 ~!!!!!!!");
//			}	
		}
		
		mav.setViewName("dashboard");
		mav.addObject("message","사진 등록이 완료되었습니다.");
		
		return mav;
	}
	public boolean insAlbum(ScheduleBean sb) {
		return this.convertData(sqlSession.insert("insAlbum",sb));
	}
	
	private boolean convertData(int value) {
		return value>0? true:false;
	}
	
	public ModelAndView addSchedule(ScheduleBean sb) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("test");
		
		try {
			sb.setMbid((String)pu.getAttribute("uCode"));
			int maxNum = this.getMaxNum(sb);
			sb.setProcess("B");
			sb.setNum(maxNum+1);
			
			if(this.insSchedule(sb)) {
				if(sb.getMpFile().length != 0) {
					
					for(int i=0; i<sb.getMpFile().length;i++) {
						sb.setPath("resources/image/"+pu.savingFile(sb.getMpFile()[i]));
						
						if(this.insAlbum(sb)) {
							mav.setViewName("calendar");
						}else {System.out.println("앨범 인서트 실패 ! ");}
					}
					
				}else {
					System.out.println(" 업로드할 파일 없음");
				}
				
			}else {System.out.println("인서트 실패 ! ");}
		
		} catch (Exception e) {e.printStackTrace();}
		/* 프로세스는 예정 B 로 넣어주기 */
		/* insert 후 트랜잭션 처리도 해줘야함 */
		
		return mav;
	}
	
	public boolean insSchedule(ScheduleBean sb) {
		return this.convertData(sqlSession.insert("insSchedule",sb));
	}
	
	public int getMaxNum(ScheduleBean sb) {
		 return sqlSession.selectOne("getMaxNum",sb);
	}

	public List<ScheduleBean> getMonSchedule(ScheduleBean sb) {
		
		
		try {
			sb.setDate(sb.getDate().replace("-", ""));
			sb.setMbid((String)pu.getAttribute("uCode"));
		} catch (Exception e) {e.printStackTrace();}
		
		List<ScheduleBean> list = sqlSession.selectList("getMonSchedule",sb);
		
		for(int i=0; i<list.size();i++) {
			try { // 이름 복호화 
				list.get(i).setMbname(enc.aesDecode(list.get(i).getMbname(), list.get(i).getMbid()));
			} catch (Exception e) {e.printStackTrace();}
		}
		
		return list;
	}
	
public List<ScheduleBean> getDaySchedule(ScheduleBean sb) {
		List<ScheduleBean> newlist = new ArrayList<ScheduleBean>();
		
		try {
			sb.setDate(sb.getDate().replace("-", ""));
			sb.setMbid((String)pu.getAttribute("uCode"));
		} catch (Exception e) {e.printStackTrace();}
		
		List<ScheduleBean> list = sqlSession.selectList("getMonSchedule",sb);
		
		for(int i=0; i<list.size();i++) {
			try { // 이름 복호화 
				list.get(i).setMbname(enc.aesDecode(list.get(i).getMbname(), list.get(i).getMbid()));
			} catch (Exception e) {e.printStackTrace();}

			list.get(i).setDate(list.get(i).getDate().replace("-",""));

			if(list.get(i).getDate().contains(sb.getDate())) {
				newlist.add(list.get(i));
			}
		}

		return newlist;
}

	

}
