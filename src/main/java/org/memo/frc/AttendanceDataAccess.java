package org.memo.frc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AttendanceDataAccess {

	private Statement state = null;
	private ResultSet rs = null;
//	private String url = "jdbc:mysql://localhost:3306/7587attendance";
//	private String userName = "Admin";
//	private String password = "AdminSQL@127";
	private DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	@Value("${mysql.db.url}")
	private String url;

	@Value("${mysql.db.username}")
	private String userName;

	@Value("${mysql.db.pwd}")
	private String password;

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

	private double convertToHours(int minutes) {
		double hours = (minutes / 60) + (0.1 * ((minutes % 60) / 6));
		String hourStr = String.format("%.2f", hours);
		return Double.parseDouble(hourStr);
	}

	public Connection getConnection() {
		Connection conn = null;
		try {
			Properties props = new Properties();
			props.setProperty("enabledTLSProtocols", "TLSv1,TLSv1.1,TLSv1.2,TLSv1.3");
			String connURL = url + "?user=" + userName + "&password=" + password;
			conn = DriverManager.getConnection(connURL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public Attendance getAttendanceById(int id) {
		if (id < 0) {
			return null;
		}
		Attendance pojo = null;
		try {
			state = getConnection().createStatement();
			rs = state.executeQuery("Select * from `attendance` where `id` = " + id);

			while (rs.next()) {
				pojo = new Attendance(rs.getInt("id"), rs.getString("name"), rs.getTimestamp("timeIn"),
						rs.getTimestamp("timeOut"), rs.getString("event"));
			}
			state.close();
			return pojo;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Attendance constructSingleAttendanceObjectFromResultSet(ResultSet rs) {
		try {
			if (rs.next()) {
				return new Attendance(rs.getInt("id"), rs.getString("name"), rs.getTimestamp("timeIn"),
						rs.getTimestamp("timeOut"), rs.getString("event"));
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Attendance> getAttendanceByName(String queryName) {
		try {
			state = getConnection().createStatement();
			rs = state.executeQuery("Select * from `attendance` where name = \"" + queryName + "\"");
			int id;
			String name, event;
			java.sql.Timestamp timeIn, timeOut;
			List<Attendance> attendancePOJOs = new ArrayList<>();
			while (rs.next()) {
				id = rs.getInt("id");
				name = rs.getString("name");
				timeIn = rs.getTimestamp("timeIn");
				timeOut = rs.getTimestamp("timeOut");
				event = rs.getString("event");
				attendancePOJOs.add(new Attendance(id, name, timeIn, timeOut, event));
			}
			state.close();
			rs.close();
			return attendancePOJOs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public List<Attendance> getAttendance(Date start, Date end) {
		try {
			state = getConnection().createStatement();

			String sql = "Select * from `attendance" + ((start == null && end == null) ? "`"
					: "` where date(timeIn) >= '" + format.format(start) + "' and date(timeIn) <= '" + format.format(end) + "'");
			rs = state.executeQuery(sql);
			int id, timeSpent;
			String name, event;
			java.sql.Timestamp timeIn, timeOut;
			List<Attendance> attendancePOJOs = new ArrayList<>();
			Attendance pojo;
			ResultSet rs2;
			Statement state2 = getConnection().createStatement();
			while (rs.next()) {
				id = rs.getInt("id");
				name = rs.getString("name");
				timeIn = rs.getTimestamp("timeIn");
				timeOut = rs.getTimestamp("timeOut");
				event = rs.getString("event");
				pojo = new Attendance(id, name, timeIn, timeOut, event);
				rs2 = state2.executeQuery(
						"select timestampdiff (minute, timeIn, timeOut) as totalTime from `attendance` where id = "
								+ id);
				timeSpent = rs2.next() ? rs2.getInt("totalTime") : -1;
				pojo.setTimeSpent(convertToHours(timeSpent));
				attendancePOJOs.add(pojo);
			}
			state.close();
			return attendancePOJOs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public List<Attendance> getAllAttendance() {
		return getAttendance(null, null);
	}

	public Attendance updateCheckoutTime(Attendance att) {
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

	public Attendance insertCheckIn(Attendance att) {
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

	public boolean confirmCheck(Attendance att) {
		try {
			state = getConnection().createStatement();
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

	public List<Attendance> currentlyCheckedIn() {
		return currentlyCheckedIn(true);
	}

	public List<Attendance> currentlyCheckedIn(boolean checkedInOnly) {
		try {
			state = getConnection().createStatement();
			List<Attendance> currentlyCheckedIn = new ArrayList<>();
			Date now = new java.util.Date();
			String date = format.format(now);
			String sql = "select * from `attendance` where date(timeIn) = '" + date
					+ (checkedInOnly ? "' and timeOut is null" : "");
			rs = state.executeQuery(sql);
			String name;
			Timestamp timeIn;
			while (rs.next()) {
				name = rs.getString("name");
				timeIn = checkedInOnly ? rs.getTimestamp("timeIn") : null;
				currentlyCheckedIn.add(new Attendance(null, name, timeIn, null, null));
			}
			state.close();
			rs.close();
			return currentlyCheckedIn;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Attendance> weeklyHours() { // default to this week only
		Date now = new java.util.Date();
		return weeklyHours(now, now);
	}

	public List<Attendance> weeklyHours(Date date1) { // past week to this week
		Date now = new java.util.Date();
		return weeklyHours(date1, now);
	}

	public List<Attendance> weeklyHours(Date date1, Date date2) { // parameterize time ranges; give current week by //
																	// default, else take user-given time range
		try {
			if (date2.getTime() < date1.getTime()) {
				return null;
			}
			state = getConnection().createStatement();
			String name;
			Timestamp timeIn;
			double time;
			int SQLtime;
			DateFormat format = new SimpleDateFormat("ww");
			int week1 = Integer.parseInt(format.format(date1));
			int week2 = Integer.parseInt(format.format(date2));
			List<Attendance> weeklyHours = new ArrayList<>();
			Attendance pojo;
			String sql = "select name,timestampdiff(minute, timeIn, timeOut) as totalTime, timeIn from `attendance` where week(timeIn,0) <= "
					+ (week2) + " and week(timeIn,0) >= " + (week1 - 1);
			rs = state.executeQuery(sql);
			while (rs.next()) {
				name = rs.getString("name");
				SQLtime = rs.getInt("totalTime");
				timeIn = rs.getTimestamp("timeIn");
				time = convertToHours(SQLtime);
				pojo = new Attendance(null, name, timeIn, null, null);
				pojo.setTimeSpent(time);
				weeklyHours.add(pojo);
			}
			state.close();
			rs.close();
			return weeklyHours;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Attendance scanCheck(String name) {
		if (name == null || name.isEmpty()) {
			return null;
		}
		try {
			state = getConnection().createStatement();
			Date now = new java.util.Date();
			DateFormat format = new SimpleDateFormat("DDD");
			String dayStr = format.format(now);
			int day = Integer.parseInt(dayStr);
			Attendance att = null;
			String sql = "select max(timeIn) as maxTimeIn from `attendance` where name = \"" + name + "\"";
			rs = state.executeQuery(sql);
			if (rs.next() && rs.getTimestamp("maxTimeIn") != null) { // has history
				Timestamp time = rs.getTimestamp("maxTimeIn");
				String sql2 = "select * from `attendance` where timeIn = '" + time + "' and dayofyear(timeIn) = " + day
						+ " and name = \"" + name + "\"";
				rs.close();
				state = getConnection().createStatement();
				rs = state.executeQuery(sql2);
				att = constructSingleAttendanceObjectFromResultSet(rs);
				if (att != null && att.getTimeOut() == null) { // checking out
					att.setTimeOut(new Timestamp(new java.util.Date().getTime()));
					System.out.println("This is date out: " + att.getDateOut());
					state.close();
					rs.close();
					return att;
				}
			}
			// else, checking in

			att = new Attendance(null, name, new Timestamp(new java.util.Date().getTime()), null, "meeting");
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
