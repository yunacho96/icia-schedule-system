package team4.services.auth;


import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import team4.database.mapper.MybatisInterface;
import team4.user.beans.AccessInfo;
import team4.user.beans.UserBean;

@Repository
public class AuthDAO  {

	@Autowired
	SqlSessionTemplate sqlSession;
	
	
	
	public String getPwdInfo(AccessInfo ai) {
		
		
		return sqlSession.selectOne("getPwdInfo",ai);
	}


	public boolean isUserId(AccessInfo ai) {
		//	sqlSession.selectOne("isUserId", ai);

		return this.convertToBoolean(sqlSession.selectOne("isUserId",ai));
	}

	public boolean isAccess(AccessInfo ai) {

		return this.convertToBoolean(sqlSession.selectOne("isAccess",ai));
		//"isAccess는 xml의 id값
	}
	
	
public boolean sumAccess(AccessInfo ai) {
		
		return this.convertToBoolean(sqlSession.selectOne("sumAccess",ai));
		
	}


	public boolean insAccessHistory(AccessInfo ai) {


		return this.convertToBoolean(sqlSession.insert("insAccessHistory",ai));

	}

	

	public boolean insMembers(UserBean ub) {

		return this.convertToBoolean(sqlSession.insert("insMembers",ub));

	}

	//회워정보가져오는거//
	List<UserBean> selMemberInfo(AccessInfo ai){
					//MyBatisInterface.xml의 (id값, 파라미터)
		return sqlSession.selectList("selMemberInfo",ai);
	}
	
	private boolean convertToBoolean(int value) {
		return value>0? true: false;
	}


	public boolean forceLogOut(AccessInfo ai) {
		
		return this.convertToBoolean(sqlSession.insert("forceLogOut",ai));
		
	}
	public boolean isCurrentAccess(AccessInfo ai) {
		
		return this.convertToBoolean(sqlSession.selectOne("isCurrentAccess",ai));
		
	}

}