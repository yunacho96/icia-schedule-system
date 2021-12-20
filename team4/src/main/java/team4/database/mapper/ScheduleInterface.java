package team4.database.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import team4.user.beans.ScheduleBean;
import team4.user.beans.TeamBean;

public interface ScheduleInterface {

	public boolean insAlbum(ScheduleBean sb);
	public boolean insSchedule(ScheduleBean sb);
	public int getMaxNum(ScheduleBean sb);
	public List<ScheduleBean> getMonSchedule(ScheduleBean sb);

	
}
