package com.example.springbootdemo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {

	private boolean isCheckIn;
	@Autowired
	private AttendanceDataAccess attDAO;

	private List<AttendanceObject> selectedAttendance;

	public List<AttendanceObject> getAllAttendance() {
		selectedAttendance = attDAO.getAllAttendance();
		return selectedAttendance;
	}

	public List<AttendanceObject> getAttendanceByName(String name) {
		selectedAttendance = attDAO.getAttendanceByName(name);
		return selectedAttendance;
	}

//	public AttendanceObject checkout(String name) {
//		return attDAO.updateCheckoutTime(name);
//	}

	public AttendanceObject scanCheck(String name) {
		AttendanceObject att = attDAO.scanCheck(name);
		isCheckIn = att.getTimeOut() == null;
		return att;
	}

	public AttendanceObject confirmCheck(AttendanceObject att) {
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
