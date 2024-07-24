package egovframework.com.uat.uia.web;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestLoginUsrViewEgovLoginControllerSelenium {

	WebDriver driver;

	@Before
	public void setup() {
		driver = new ChromeDriver();
	}

	@Test
	public void test() {
		driver.get("http://localhost:8080/egovframework-all-in-one/uat/uia/egovLoginUsr.do");
	}

}
