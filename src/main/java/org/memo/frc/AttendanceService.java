package org.memo.frc;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {

	private boolean isCheckIn;
	@Autowired
	private AttendanceDataAccess attDAO;

	private List<Attendance> selectedAttendance;

	public List<Attendance> getAllAttendance() {
		selectedAttendance = attDAO.getAllAttendance();
		return selectedAttendance;
	}

	public List<Attendance> attendanceTimeFrame(Date start, Date end) {
		selectedAttendance = attDAO.getAttendance(start, end);
		return selectedAttendance;
	}

	public List<Attendance> getAttendanceByName(String name) {
		selectedAttendance = attDAO.getAttendanceByName(name);
		return selectedAttendance;
	}

	public List<Attendance> currentlyCheckedIn() {
		selectedAttendance = attDAO.currentlyCheckedIn();
		return selectedAttendance;
	}

	public List<Attendance> weeklyHours() {
		selectedAttendance = attDAO.weeklyHours();
		return selectedAttendance;
	}

	public List<Attendance> weeklyHours(Date date) {
		selectedAttendance = attDAO.weeklyHours(date);
		return selectedAttendance;
	}
//	public AttendanceObject checkout(String name) {
//		return attDAO.updateCheckoutTime(name);
//	}

	public Attendance scanCheck(String name) {
		Attendance att = attDAO.scanCheck(name);
		isCheckIn = att.getTimeOut() == null;
		return att;
	}

	public Attendance confirmCheck(Attendance att) {
		System.out.println(att);
		return att.getId() != null ? attDAO.updateCheckoutTime(att) : attDAO.insertCheckIn(att);
	}

	public String getDBName() {
		Connection conn = attDAO.getConnection();
		try {
			return conn.getCatalog();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public boolean getIsCheckIn() {
		return isCheckIn;
	}
}
