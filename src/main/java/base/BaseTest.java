package base;

import java.io.File;
import java.lang.reflect.Method;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import factory.DriverFactory;

public class BaseTest {

	public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	protected Logger logger;

	@BeforeMethod(alwaysRun = true)
	public void setUp(Method method) {
		String testName = initializeLogger(method);
		logger.info("===== Starting test: " + testName + " =====");
		WebDriver webDriver = DriverFactory.getDriver();
		driver.set(webDriver);
		driver.get().manage().window().maximize();
		driver.get().get(ConfigReader.getProperty("baseUrl"));
		logger.info("WebDriver initialized");
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown(Method method) {
		if (driver.get() != null) {
			driver.get().quit();
			logger.info("WebDriver quit");
			driver.remove();
		}
		String testName = method.getName();
		logger.info("===== Finished test: " + testName + " =====");
	}

	public static WebDriver getDriver() {
		return driver.get();
	}

	public String initializeLogger(Method method) {
		String testName = sanitizeFileName(method.getName());
		LoggerHelper.setTestName(testName);
		ThreadContext.put("testName", testName);
		System.setProperty("reportFolder", ConfigReader.getReportFolder());
		File logDir = new File(ConfigReader.getReportFolder() + "/logs");
		if (!logDir.exists()) {
			logDir.mkdirs();
		}
		logger = LoggerHelper.getLogger(this.getClass());
		return testName;
	}

	public static String sanitizeFileName(String name) {
		if (name == null)
			return "unknown_test";
		return name.replaceAll("[\\\\/:*?\"<>|]", "_");
	}

}
