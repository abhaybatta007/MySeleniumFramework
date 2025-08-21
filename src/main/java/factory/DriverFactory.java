package factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import base.ConfigReader;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverFactory {

	// ThreadLocal for parallel execution
	private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

	/**
	 * Get the WebDriver instance for current thread
	 */
	public static WebDriver getDriver() {
		if (tlDriver.get() == null) {
			tlDriver.set(createDriver());
		}
		return tlDriver.get();
	}

	/**
	 * Create WebDriver instance based on config
	 */
	private static WebDriver createDriver() {
		String browser = ConfigReader.getProperty("browser", "chrome").toLowerCase();
		boolean headless = Boolean.parseBoolean(ConfigReader.getProperty("headless", "false"));
		boolean remote = Boolean.parseBoolean(ConfigReader.getProperty("remoteExecution", "false"));
		String gridUrl = ConfigReader.getProperty("gridUrl");
		WebDriver driver = null;

		try {
			switch (browser) {
			case "chrome":
				ChromeOptions chromeOptions = new ChromeOptions();
				if (headless)
					chromeOptions.addArguments("--headless=new");
				chromeOptions.addArguments("--disable-gpu", "--window-size=1920,1080");
				driver = remote ? new RemoteWebDriver(new URL(gridUrl), chromeOptions)
						: new ChromeDriver(chromeOptions);
				break;

			case "firefox":
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				if (headless)
					firefoxOptions.addArguments("--headless");
				driver = remote ? new RemoteWebDriver(new URL(gridUrl), firefoxOptions)
						: new FirefoxDriver(firefoxOptions);
				break;

			case "edge":
				EdgeOptions edgeOptions = new EdgeOptions();
				if (headless)
					edgeOptions.addArguments("headless");
				driver = remote ? new RemoteWebDriver(new URL(gridUrl), edgeOptions) : new EdgeDriver(edgeOptions);
				break;

			default:
				throw new IllegalArgumentException("❌ Browser not supported: " + browser);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException("❌ Invalid Grid URL: " + gridUrl);
		}

		return driver;
	}

	/**
	 * 
	 * Quit WebDriver and remove ThreadLocal reference
	 */
	public static void quitDriver() {
		if (tlDriver.get() != null) {
			tlDriver.get().quit();
			tlDriver.remove();
		}
	}
}
