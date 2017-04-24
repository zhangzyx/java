package prev.zyx.utility;

import org.apache.commons.lang.CharUtils;
import org.apache.log4j.LogManager;

public class LogUtil {
	
	public void log() {
		LogManager.getCurrentLoggers();
		System.out.println(CharUtils.toChar("LogUtil"));
	}
}
