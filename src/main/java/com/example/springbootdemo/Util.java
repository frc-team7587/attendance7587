package com.example.springbootdemo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Util {

	public static String getDisplayTimeString(Timestamp time) {
		return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(time);
	}

	public static String getSQLTimeString(Timestamp time) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
	}
}
