package team4.database.mapper;

import java.util.List;
import java.util.Map;

import team4.user.beans.MailForm;
import team4.user.beans.TDetailsBean;
import team4.user.beans.TeamBean;
import team4.user.beans.UserBean;

public interface ShareInterface {

	public List<TeamBean> getTeamList(TeamBean tb);
	public List<TDetailsBean> getMemList(TDetailsBean db);
	public List<TeamBean> getTeCode();
	public void addMember(List<TeamBean> tb);
	public boolean insTeamTable(TeamBean tb);
	public boolean insTeamDetailsTable(TDetailsBean db);
	public List<TDetailsBean> getFriends(TDetailsBean db);
	public List<UserBean> searchFriend(UserBean ub);
	public Map<String,String> insWaitFriends(List<TDetailsBean> db);
	public Map<String,String> sendEmail(MailForm mf);
	public List<TDetailsBean> getInviteList();
	public Map<String,String> upReqAccept(List<TDetailsBean> db);
	public Map<String, String> upReqReject(List<TDetailsBean> db);
	
}