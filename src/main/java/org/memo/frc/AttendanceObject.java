package org.memo.frc;

import java.sql.Timestamp;

public class AttendanceObject {

	@Override
	public String toString() {
		return "AttendanceObject [id = " + id + ", name = " + name + ", timeIn = " + timeIn + ", timeOut = " + timeOut
				+ ", event = " + event + "]";
	}

	Integer id;
	String name;
	Timestamp timeIn;
	Timestamp timeOut;
	String event;

	public AttendanceObject(Integer id, String name, Timestamp timeIn, Timestamp timeOut, String event) {
		super();
		this.id = id;
		this.name = name;
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.event = event;
	}

	public AttendanceObject(Integer id, String name, Timestamp timeIn, String event) {
		this(id, name, timeIn, null, event);
	}

	public AttendanceObject() {
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

}
