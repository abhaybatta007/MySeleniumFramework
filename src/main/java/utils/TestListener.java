package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import java.io.File;

import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import base.BaseTest;
import base.ConfigReader;
import base.LoggerHelper;

public class TestListener implements ITestListener {
	private static ExtentReports extent = ExtentManager.getInstance();
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	@Override
	public void onTestStart(ITestResult result) {
		String testName = BaseTest.sanitizeFileName(result.getMethod().getMethodName());
		LoggerHelper.setTestName(testName);
		ThreadContext.put("testName", testName);
		ExtentTest extentTest = extent.createTest(testName);
		test.set(extentTest);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		test.get().log(Status.PASS, "Test Passed");
		attachLogToExtent(result);
		LoggerHelper.clearLogger(); // ✅ clear after test
		ThreadContext.remove("testName");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		test.get().log(Status.FAIL, "Test Failed: " + result.getThrowable());
		if (ConfigReader.getScreenshotOnFailure() && BaseTest.getDriver() != null) {
			String base64Screenshot = ((TakesScreenshot) BaseTest.getDriver()).getScreenshotAs(OutputType.BASE64);
			test.get().addScreenCaptureFromBase64String(base64Screenshot, result.getMethod().getMethodName());
		}
		attachLogToExtent(result);
		LoggerHelper.clearLogger();
		ThreadContext.remove("testName");
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		test.get().log(Status.SKIP, "Test Skipped: " + result.getThrowable());
		LoggerHelper.clearLogger();
		ThreadContext.remove("testName");
	}

	@Override
	public void onFinish(ITestContext context) {
		extent.flush();
	}

	private void attachLogToExtent(ITestResult result) {
		String logFilePath = System.getProperty("user.dir") + File.separator + ConfigReader.getReportFolder()
				+ File.separator + "logs" + File.separator + LoggerHelper.getTestName() + ".log";
		try {
			String base64Log = FileUtils.encodeFileToBase64(logFilePath);
			String htmlLink = "<a href='data:text/plain;base64," + base64Log + "' download='"
					+ LoggerHelper.getTestName() + ".log'>Download Log</a>";
			test.get().log(Status.INFO, "Attached test log: " + htmlLink);
		} catch (Exception e) {
			test.get().log(Status.WARNING, "Failed to attach log file: " + e.getMessage());
		}
	}

}
