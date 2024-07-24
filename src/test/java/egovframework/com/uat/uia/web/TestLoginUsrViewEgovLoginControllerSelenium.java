package egovframework.com.uat.uia.web;

import static org.junit.Assert.assertEquals;

import org.egovframe.rte.fdl.cmmn.exception.BaseRuntimeException;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import lombok.NoArgsConstructor;

/**
 * 
 * @author god
 *
 */
@NoArgsConstructor
public class TestLoginUsrViewEgovLoginControllerSelenium {

	/**
	 * 
	 */
	private WebDriver driver;

	/**
	 * 
	 */
	@Before
	public void setup() {
		driver = new ChromeDriver();
	}

	/**
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test() throws InterruptedException {
		driver.get("http://localhost:8080/egovframework-all-in-one/uat/uia/egovLoginUsr.do");
		sleep();

		final JavascriptExecutor executor = (JavascriptExecutor) driver;

		// 새로고침
		executor.executeScript("location.reload();");
		sleep();

		// 업무 탭 클릭
		final WebElement typeUsr = driver.findElement(By.id("typeUsr"));
		typeUsr.click();
		sleep();

		// 아이디 입력
		final WebElement id = driver.findElement(By.id("id"));
		id.sendKeys("TEST1");
		sleep();

		// 비밀번호 입력
		final WebElement password = driver.findElement(By.id("password"));
		password.sendKeys("rhdxhd12");
		sleep();

		// 로그인 버튼 클릭
		executor.executeScript("actionLogin();");

		// 확인
		final WebElement a = driver.findElement(By.tagName("a"));
		final String aString = a.getText();
		assertEquals("", "로그아웃", aString);
	}

	private void sleep() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			throw new BaseRuntimeException(e);
		}
	}

}
