package org.memo.frc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class Attendance {
	@Override
	public String toString() {
		return "AttendanceObject [id = " + id + ", name = " + name + ", timeIn = " + timeIn + ", timeOut = " + timeOut
				+ ", event = " + event + "]";
	}

	Integer id;
	String name;
	LocalDateTime timeIn;
	LocalDateTime timeOut;
	String event;
	double timeSpent;

	public Attendance(Integer id, String name, LocalDateTime timeIn, LocalDateTime timeOut, String event) {
		this.id = id;
		this.name = name;
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.event = event;
	}

	public Attendance(String name, LocalDateTime timeIn) {
		this(null, name, timeIn, null, "Meeting");
	}

	/* public Attendance(Attendance att) {
		this(att.getId(), att.getName(), new Timestamp(att.getTimeIn().getTime()),
				new Timestamp(att.getTimeOut().getTime()), att.getEvent());
		
	} */

	// public String getDateIn() {
	// 	return dateIn;
	// }

	// public String getDateOut() {
	// 	return dateOut;
	// }

//	public void setDateIn(Timestamp time) {
//		dateIn.setTime(time != null ? time.getTime() : null);
//	}
//
//	public void setDateOut(Timestamp time) {
//		dateOut.setTime(time != null ? time.getTime() : null);
//	}

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

	public LocalDateTime getTimeIn() {
		return timeIn;
	}

	public void setTimeIn(LocalDateTime timeIn) {
		this.timeIn = timeIn;
		// dateIn = timeIn != null ? format.format(new Date(timeIn.getTime())) : null;
	}

	public LocalDateTime getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(LocalDateTime timeOut) {
		this.timeOut = timeOut;
		// dateOut = timeOut != null ? format.format(new Date(timeOut.getTime())) : null;
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
