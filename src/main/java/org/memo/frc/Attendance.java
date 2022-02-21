package org.memo.frc;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
	LocalDateTime dateIn;
	LocalDateTime dateOut;
	String event;
	static String zone = "-05:00";
	public static DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	double timeSpent;

	public Attendance(Integer id, String name, LocalDateTime dateIn, LocalDateTime dateOut, String event) {
		System.out.println("Attendance constructed with localdatetime");
		this.id = id;
		this.name = name;
		this.dateIn = dateIn;
		this.dateOut = dateOut;
		this.event = event;
		this.timeIn = dateIn != null ? Timestamp.valueOf(dateIn) : null;
		this.timeOut = dateOut != null ? Timestamp.valueOf(dateOut) : null;
//		this.event = event;
//		timeIn.setTime(this.dateIn);
//		timeOut.setTime(this.dateOut);
	}

	public Attendance(Integer id, String name, Timestamp timeIn, Timestamp timeOut, String event) {
		this.id = id;
		this.name = name;
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.event = event;
		this.dateIn = timeIn != null ? timeIn.toInstant().atZone(ZoneId.of(zone)).toLocalDateTime() : null;
		this.dateOut = timeOut != null ? timeOut.toInstant().atZone(ZoneId.of(zone)).toLocalDateTime() : null;
	}

	public Attendance(Integer id, String name, Timestamp timeIn, String event) {
		this(id, name, timeIn, null, event);
	}

	public Attendance(Attendance att) {
		this(att.getId(), att.getName(), att.getDateIn(), att.getDateOut(), att.getEvent());
	}

	public LocalDateTime getDateIn() {
		return dateIn;
	}

	public LocalDateTime getDateOut() {
		return dateOut;
	}

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

	public Timestamp getTimeIn() {
		return timeIn;
	}

	public void setTimeIn(Timestamp timeIn) {
		this.timeIn = timeIn;
		dateIn = timeIn != null ? timeIn.toInstant().atZone(ZoneId.of(zone)).toLocalDateTime() : null;
	}

	public Timestamp getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(Timestamp timeOut) {
		this.timeOut = timeOut;
		dateOut = timeOut != null ? timeOut.toInstant().atZone(ZoneId.of(zone)).toLocalDateTime() : null;
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
