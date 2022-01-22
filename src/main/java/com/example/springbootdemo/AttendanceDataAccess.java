package com.example.springbootdemo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import org.springframework.stereotype.Component;

@Component
public class AttendanceDataAccess {

	private Statement state = null;
	private ResultSet rs = null;
//	private String url = "jdbc:mysql://localhost:3306/7587attendance?";
//	private String userName = "Admin";
//	private String password = "AdminSQL@127";

	private String url = "jdbc:mysql://database-1.crbahpmj0o55.us-east-1.rds.amazonaws.com:3306/team7587?";
	private String userName = "admin";
	private String password = "awsdbadmin127";

	public AttendanceDataAccess() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
			TimeZone.setDefault(TimeZone.getTimeZone("EST"));
//			do {
//				input = Integer.parseInt(JOptionPane.showInputDialog(null,
//						"1. Read database\n2. Insert attendance\n3. Update database\n4. Delete from database\n5. Quit"));
//				switch (input) {
//				case 1:
//					System.out.println(getAllAttendance());
//					break;
//				case 2:
//					System.out.println(insertCheckInTime(new AttendanceObject(-1,
//							JOptionPane.showInputDialog("Enter name: "), new Date(new java.util.Date().getTime()),
//							JOptionPane.showInputDialog("Enter event: "))));
//					break;
//				case 3:
//					System.out.println(updateCheckoutTime(
//							Integer.parseInt(JOptionPane.showInputDialog("Enter ID of entry you want to update: "))));
//					break;
//				case 4:
//					System.out.println(deleteAttendance(
//							Integer.parseInt(JOptionPane.showInputDialog("Enter ID of entry you want to delete: "))));
//					break;
//				case 5:
//					System.out.println("Bye bye");
//					getConnection().close();
//					break;
//				}
//			} while (input != 5);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		try {
//			boolean done = false;
//			state = getConnection().createStatement();
//			done = state.execute("create database if not exists 7587attendance");
//			state.execute("alter table `memberinfo` auto_increment = 1");
//			int input = 0;
//			Scanner scan = new Scanner(System.in);
//			do {
//				JOptionPane.showInputDialog(null,
//						"1. Read database\n2. Insert attendance\n3. Update database\n4. Delete from database");
//				input = scan.nextInt();
//				switch (input) {
//				case 1:
//				}
//			} while (input != 6);
//
//			System.out.println(done);
//			System.out.println(insertIntoMemberInfo("Jason", "Programming", 0, state));
//			System.out.println(updateMemberInfo("`subteam` = \"alumni\"", "`id` < 5", state));
//			res = selectMemberInfo("*", state);
//			System.out.println(getMemberInfo(res));
//			System.out.println(state.executeUpdate("delete from `memberinfo` where `id` != 0"));
//			res = selectMemberInfo("*", state);
//			System.out.println(getMemberInfo(res));
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			res.close();
//			state.close();
//		}
	}

	public Connection getConnection() {
		Connection conn = null;
		try {
			Properties props = new Properties();
			props.setProperty("enabledTLSProtocols", "TLSv1,TLSv1.1,TLSv1.2,TLSv1.3");
			String connURL = url + "user=" + userName + "&password=" + password;
			conn = DriverManager.getConnection(connURL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public AttendanceObject getAttendanceById(int id) {
		if (id < 0) {
			return null;
		}
		AttendanceObject pojo = null;
		try {
			state = getConnection().createStatement();
			rs = state.executeQuery("Select * from `attendance` where `id` = " + id);

			while (rs.next()) {
				pojo = new AttendanceObject(rs.getInt("id"), rs.getString("name"), rs.getTimestamp("timeIn"),
						rs.getTimestamp("timeOut"), rs.getString("event"));
			}
			state.close();
			return pojo;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public AttendanceObject constructSingleAttendanceObjectFromResultSet(ResultSet rs) {
		try {
			if (rs.next()) {
				return new AttendanceObject(rs.getInt("id"), rs.getString("name"), rs.getTimestamp("timeIn"),
						rs.getTimestamp("timeOut"), rs.getString("event"));
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List getAttendanceByName(String queryName) {
		try {
			state = getConnection().createStatement();
			rs = state.executeQuery("Select * from `attendance` where name = \"" + queryName + "\"");
			int id;
			String name, event;
			java.sql.Timestamp timeIn, timeOut;
			ArrayList<AttendanceObject> attendancePOJOs = new ArrayList<>();
			while (rs.next()) {
				id = rs.getInt("id");
				name = rs.getString("name");
				timeIn = rs.getTimestamp("timeIn");
				timeOut = rs.getTimestamp("timeOut");
				event = rs.getString("event");
				attendancePOJOs.add(new AttendanceObject(id, name, timeIn, timeOut, event));
			}
			state.close();
			rs.close();
			return attendancePOJOs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public List getAllAttendance() {
		try {
			state = getConnection().createStatement();
			rs = state.executeQuery("Select * from `attendance`");
			int id;
			String name, event;
			java.sql.Timestamp timeIn, timeOut;
			ArrayList<AttendanceObject> attendancePOJOs = new ArrayList<>();
			while (rs.next()) {
				id = rs.getInt("id");
				name = rs.getString("name");
				timeIn = rs.getTimestamp("timeIn");
				timeOut = rs.getTimestamp("timeOut");
				event = rs.getString("event");
				attendancePOJOs.add(new AttendanceObject(id, name, timeIn, timeOut, event));
			}
			state.close();
			return attendancePOJOs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public AttendanceObject updateCheckoutTime(AttendanceObject att) {
		try {
			state = getConnection().createStatement();
			state.executeUpdate(
					"update `attendance` set `timeOut` = '" + att.getTimeOut() + "' where `id` = " + att.getId());
			state.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return att;
	}

//	public AttendanceObject updateCheckoutTime(String name) {
//		if (name == null || name.isBlank()) {
//			return null;
//		}
//		java.util.Date javaNow = new java.util.Date();
//		Date now = new Date(javaNow.getTime());
//		Timestamp date = new Timestamp(now.getTime());
//		AttendanceObject att = null;
//		try {
//			state = getConnection().createStatement();
//			rs = state.executeQuery("select max(id) from `attendance` where name = \"" + name + "\"");
//			att = rs.next() ? getAttendanceById(rs.getInt("max(id)")) : null;
//			state.close();
//			state = getConnection().createStatement();
//			state.executeUpdate("update `attendance` set `timeOut` = '" + date + "' where id = " + att.getId());
//			att.setTimeOut(date);
//			state.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return null;
//		}
//		return att;
//	}

	public AttendanceObject insertCheckIn(AttendanceObject att) {
		if (att == null) {
			return null;
		}
		try {
			state = getConnection().createStatement();
			String sql = "insert into `attendance` (`name`, `timeIn`, `timeOut`, `event`) values (\"" + att.getName()
					+ "\", '" + att.getTimeIn() + "', null , \"" + att.getEvent() + "\")";
			state.executeUpdate(sql);
			rs = state.executeQuery("select last_insert_id() as id");
			att.setId(rs.next() ? rs.getInt("id") : null);
			state.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return att;
	}

	public boolean confirmCheck(AttendanceObject att) {
		try {
			state.getConnection().createStatement();
			if (att.getTimeOut() == null) {
				String sql = "insert into `attendance` values (\"" + att.getName() + "\", \""
						+ String.format("%1$TD %1$TT", att.getTimeIn()) + "\", \""
						+ String.format("%1$TD %1$TT", att.getTimeOut()) + "\", \"" + att.getEvent();
				state.executeQuery(sql);
				return true;
			} else {
				String sql = "update `attendance` set `timeOut` = \"" + String.format("%1$TD %1$TT", att.getTimeOut())
						+ "\" where `id` = " + att.getId();
				state.executeUpdate(sql);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<AttendanceObject> currentlyCheckedIn() {
		try {
			state.getConnection().createStatement();
			ArrayList<AttendanceObject> currentlyCheckedIn = new ArrayList<>();
			Date now = new java.util.Date();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String date = format.format(now);
			String sql = "select * from `attendance` where date(timestamp) = \'" + date + "\" and timeOut = null";
			rs = state.executeQuery(sql);
			int id;
			String name, event;
			Timestamp timeIn, timeOut;
			while (rs.next()) {
				id = rs.getInt("id");
				name = rs.getString("name");
				timeIn = rs.getTimestamp("timeIn");
				timeOut = rs.getTimestamp("timeOut");
				event = rs.getString("event");
				currentlyCheckedIn.add(new AttendanceObject(id, name, timeIn, timeOut, event));
			}
			state.close();
			rs.close();
			return currentlyCheckedIn;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public AttendanceObject scanCheck(String name) {
		if (name == null || name.isEmpty()) {
			return null;
		}
		try {
			state = getConnection().createStatement();
			AttendanceObject att = null;
			String sql = "select max(timeIn) as maxTimeIn from `attendance` where name = \"" + name + "\"";
			rs = state.executeQuery(sql);
			if (rs.next() && rs.getTimestamp("maxTimeIn") != null) { // has history
				Timestamp time = rs.getTimestamp("maxTimeIn");
				String sql2 = "select * from `attendance` where timeIn = '" + time + "' and name = \"" + name + "\"";
				rs.close();
				state = getConnection().createStatement();
				rs = state.executeQuery(sql2);
				att = constructSingleAttendanceObjectFromResultSet(rs);
				System.out.println(att);
				if (att.getTimeOut() == null) { // checking out
					att.setTimeOut(new Timestamp(new java.util.Date().getTime()));
					state.close();
					rs.close();
					return att;
				}
			}
			// else, checking in
			att = new AttendanceObject(null, name, new Timestamp(new java.util.Date().getTime()), null, "meeting");
			state.close();
			rs.close();
			return att;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getMaxId() {
		try {
			state = getConnection().createStatement();
			rs = state.executeQuery("select max(id) as max_id from `attendance`");
			int result = rs.next() ? rs.getInt("max_id") : -1;
			state.close();
			rs.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

//	state.executeUpdate("insert into `attendance` (`name`, `timeIn`, `timeOut`, `event`) values (\"" + name
//			+ "\", '" + att.getTimeIn() + " " + time.substring(0, time.indexOf('.')) + "', " + att.getTimeOut()
//			+ ", 	\"" + att.getEvent() + "\")");
//
//	att.setId(rs.next() ? rs.getInt("last_insert_id()") : -1);
//	state.close();
//	return att;

	public boolean deleteAttendance(int id) throws SQLException {
		if (id < 0) {
			return false;
		}
		state = getConnection().createStatement();
		boolean deleted = state.executeUpdate("delete from `attendance` where `id` = " + id) == 1;
		state.close();
		return deleted;
	}
}
