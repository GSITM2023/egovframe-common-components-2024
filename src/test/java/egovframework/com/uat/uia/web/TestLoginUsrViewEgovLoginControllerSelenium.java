package egovframework.com.uat.uia.web;

import static org.junit.Assert.assertEquals;

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
		driver.get("http://localhost:8080/egovframework-all-in-one/uat/uia/egovLoginUsr.do");

		JavascriptExecutor executor = (JavascriptExecutor) driver;

		// 새로고침
		executor.executeScript("location.reload();");

		// 업무 탭 클릭
		WebElement typeUsr = driver.findElement(By.id("typeUsr"));
		typeUsr.click();

		// 아이디 입력
		WebElement id = driver.findElement(By.id("id"));
		id.sendKeys("TEST1");

		// 비밀번호 입력
		WebElement password = driver.findElement(By.id("password"));
		password.sendKeys("rhdxhd12");

		// 로그인 버튼 클릭
		executor.executeScript("actionLogin();");

		// 확인
		WebElement a = driver.findElement(By.tagName("a"));
		String aString = a.getText();
		assertEquals("", "로그아웃", aString);
	}

}
