package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WeatherShopperTest extends BaseTest {

	@Test
	public void validateWindowTitle() throws InterruptedException {
		logger.info("Starting validateWindowTitle test...");
		String actualTitle = getDriver().getTitle();
		Assert.assertEquals(actualTitle, "Current Temperature", "Window title mismatch!");
		logger.info("validateWindowTitle test completed successfully");
		Thread.sleep(10000);
	}
}
