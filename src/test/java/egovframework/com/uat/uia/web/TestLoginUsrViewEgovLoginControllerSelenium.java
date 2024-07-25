package egovframework.com.uat.uia.web;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestLoginUsrViewEgovLoginControllerSelenium {

	WebDriver driver;

	@Before
	public void setup() {
		driver = new ChromeDriver();
	}

	@Test
	public void test() {
		// 로그인 화면 이동
		driver.get("http://localhost:8080/egovframework-all-in-one/uat/uia/egovLoginUsr.do");

		JavascriptExecutor executor = (JavascriptExecutor) driver;

		// 새로고침
		executor.executeScript("location.reload();");

		// 업무 탭 클릭
		WebElement typeUsr = driver.findElement(By.id("typeUsr"));
		typeUsr.click();
	}

}
