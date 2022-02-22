package org.memo.frc;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class Util {

	public static String getDisplayTimeString(Timestamp time) {
		return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(time);
	}

	public static String getSQLTimeString(Timestamp time) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
	}

	public static LocalDateTime ts2Dt(Timestamp t){
		return t==null ? null : t.toLocalDateTime();
	}
}
