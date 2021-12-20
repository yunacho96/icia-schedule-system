package team4.database.mapper;

import java.util.List;

import team4.user.beans.AccessInfo;
import team4.user.beans.UserBean;

public interface MybatisInterface {
	// interface 의 메소드 이름이 mybatis.xml 에서 쓰는 id 이다 
	String getUserPwdInfo(); 
	boolean isUCode(AccessInfo ai);  // 접근제한자를 없애도 되는건 패키지 명이 같아서 가능하다 !
	boolean insAccessHistory(AccessInfo ai);
	boolean insMembers(UserBean ub); 
	List<UserBean> selMemberInfo(AccessInfo ai);
	List<AccessInfo> selAccessInfo(AccessInfo ai);
	boolean insLogOut(AccessInfo ai);
	int sumOfAccess(AccessInfo ai);
}
