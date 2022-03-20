package org.memo.frc;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Util {

	public static final ZoneId TZ = ZoneId.of("America/New_York");

	public static LocalDateTime now(){
		return LocalDateTime.now(TZ);
	}

	public static String getDisplayTimeString(Timestamp time) {
		return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(time);
	}

	public static String getSQLTimeString(Timestamp time) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
	}

	public static LocalDateTime ts2Dt(Timestamp t){
		return t==null ? null : t.toLocalDateTime();
	}

	public static double convertToHours(int minutes) {
		double hours = (minutes / 60) + (0.1 * ((minutes % 60) / 6));
		String hourStr = String.format("%.2f", hours);
		return Double.parseDouble(hourStr);
	}
}
