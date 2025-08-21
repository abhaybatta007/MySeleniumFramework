package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

	private static Properties properties;

	static {
		try {
			String configPath = System.getProperty("user.dir") + "/src/main/resources/config.properties";
			FileInputStream fis = new FileInputStream(configPath);
			properties = new Properties();
			properties.load(fis);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("❌ Failed to load config.properties file.");
		}
	}

	/**
	 * Get property value with support for command-line overrides. Priority: 1.
	 * System property (passed via -D) 2. config.properties 3. null if not found
	 */
	public static String getProperty(String key) {
		String sysProp = System.getProperty(key);
		if (sysProp != null && !sysProp.isEmpty()) {
			return sysProp;
		}
		return properties.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		String sysProp = System.getProperty(key);
		if (sysProp != null && !sysProp.isEmpty()) {
			return sysProp;
		}
		return properties.getProperty(key, defaultValue);
	}

	public static boolean getScreenshotOnFailure() {
		return Boolean.parseBoolean(getProperty("screenshotOnFailure", "false"));
	}

	public static String getReportFolder() {
		return getProperty("reportPath", "false");
	}
}
