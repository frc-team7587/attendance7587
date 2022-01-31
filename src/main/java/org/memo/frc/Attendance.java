package org.memo.frc;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Attendance {

	@Override
	public String toString() {
		return "AttendanceObject [id = " + id + ", name = " + name + ", timeIn = " + timeIn + ", timeOut = " + timeOut
				+ ", event = " + event + "]";
	}

	Integer id;
	String name;
	Timestamp timeIn;
	Timestamp timeOut;
	String dateIn;
	String dateOut;
	String event;
	DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	double timeSpent;

	public Attendance(Integer id, String name, Timestamp timeIn, Timestamp timeOut, String event) {
		super();
		this.id = id;
		this.name = name;
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.event = event;
		dateIn = format.format(new Date(timeIn.getTime()));
		dateOut = timeOut == null ? null : format.format(new Date(timeOut.getTime()));
	}

	public String getDateIn() {
		return dateIn;
	}

	public String getDateOut() {
		return dateOut;
	}

	public Attendance(Integer id, String name, Timestamp timeIn, String event) {
		this(id, name, timeIn, null, event);
	}

	public Attendance() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getTimeIn() {
		return timeIn;
	}

	public void setTimeIn(Timestamp timeIn) {
		this.timeIn = timeIn;
	}

	public Timestamp getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(Timestamp timeOut) {
		this.timeOut = timeOut;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public void setTimeSpent(double timeSpent) {
		this.timeSpent = timeSpent;
	}

	public double getTimeSpent() {
		return timeSpent;
	}
}
