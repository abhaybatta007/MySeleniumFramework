package base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitHelper {

	private WebDriver driver;
	private WebDriverWait wait;

	public WaitHelper(WebDriver driver) {
		this.driver = driver;
		int timeout = Integer.parseInt(ConfigReader.getProperty("explicitWait", "20"));
		wait = new WebDriverWait(this.driver, Duration.ofSeconds(timeout));
	}

	public void waitForElementVisible(WebElement element) {
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public void waitForElementClickable(WebElement element) {
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public void waitForTitleContains(String titleFragment) {
		wait.until(ExpectedConditions.titleContains(titleFragment));
	}

	public void waitForPageLoadingComplete() {
		ExpectedCondition<Boolean> pageLoadCondition = driver -> ((JavascriptExecutor) driver)
				.executeScript("return document.readyState").equals("complete");
		wait.until(pageLoadCondition);
	}

}
