package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import base.ConfigReader;

public class ExtentManager {
	private static ExtentReports extent;

	public static ExtentReports getInstance() {
		if (extent == null) {
			String reportPath = System.getProperty("user.dir") + "/" + ConfigReader.getReportFolder()
					+ "/ExtentReport.html";
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("UI Automation Report");
			spark.config().setDocumentTitle("Test Results");

			extent = new ExtentReports();
			extent.attachReporter(spark);
			extent.setSystemInfo("Framework", "Selenium + TestNG");
			extent.setSystemInfo("Tester", "YourName");
		}
		return extent;
	}
}
