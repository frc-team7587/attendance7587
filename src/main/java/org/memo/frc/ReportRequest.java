package org.memo.frc;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class ReportRequest {

	private Date start, end;
	static DateFormat format = new SimpleDateFormat("MM-dd-yyyy");

	public ReportRequest(Date start, Date end) {
		this.start = start;
		this.end = end;
	}

	public ReportRequest(String start, String end) throws ParseException {
		this(new Date(format.parse(start).getTime()), new Date(format.parse(end).getTime()));
	}

	public ReportRequest() {
//		this(new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()));
	}

	public boolean isEmpty(){
		return start==null && end==null;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

}
