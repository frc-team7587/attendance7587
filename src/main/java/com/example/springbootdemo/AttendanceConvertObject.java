//package com.example.springbootdemo;
//
//import java.sql.Timestamp;
//
//public class AttendanceConvertObject {
//	@Override
//	public String toString() {
//		return "AttendanceObject [id = " + idString + ", name = " + nameString + ", timeIn = " + timeInString
//				+ ", timeOut = " + timeOutString + ", event = " + eventString + "]";
//	}
//
//	int idString;
//	String nameString;
//	String timeInString;
//	String timeOutString;
////	boolean checkedIn;
//	String eventString;
//
//	public AttendanceConvertObject(int id, String name, String timeIn, String timeOut, String event) {
//		super();
//		this.idString = id;
//		this.nameString = name;
//		this.timeInString = timeIn;
//		this.timeOutString = timeOut;
//		this.eventString = event;
//	}
//
//	public AttendanceConvertObject(int id, String name, String timeIn, String event) {
//		this(id, name, timeIn, null, event);
//	}
//
//	public AttendanceConvertObject(AttendanceObject att) {
//		this(att.getId(), att.getName(), att.getTimeInString(), att.getTimeOutString(), att.getEvent());
//	}
//
//	public int getId() {
//		return idString;
//	}
//
//	public void setId(int id) {
//		this.idString = id;
//	}
//
//	public String getName() {
//		return nameString;
//	}
//
//	public void setName(String name) {
//		this.nameString = name;
//	}
//
//	public String getTimeIn() {
//		return timeInString;
//	}
//
//	public void setTimeIn(Timestamp timeIn) {
//		this.timeInString = timeIn.toString();
//	}
//
//	public String getTimeOut() {
//		return timeOutString;
//	}
//
//	public void setTimeOut(Timestamp timeOut) {
//		this.timeOutString = timeOut.toString();
//	}
//
//	public String getEvent() {
//		return eventString;
//	}
//
//	public void setEvent(String event) {
//		this.eventString = event;
//	}
//
//	private Timestamp getTimeFromString(String time) {
//		return Timestamp.valueOf(time);
//	}
//}
