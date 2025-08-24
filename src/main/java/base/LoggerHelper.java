package base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public class LoggerHelper {

	public static Logger getLogger(Class<?> clazz) {
		return LogManager.getLogger(clazz);
	}

	public static void setTestName(String name) {
		ThreadContext.put("testName", name);
	}

	public static String getTestName() {
		return ThreadContext.get("testName");
	}

	public static void clearLogger() {
		ThreadContext.remove("testName");
	}
}
