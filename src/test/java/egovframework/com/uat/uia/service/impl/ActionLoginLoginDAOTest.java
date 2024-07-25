package egovframework.com.uat.uia.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;

import egovframework.com.cmm.LoginVO;
import egovframework.com.test.EgovTestAbstractDAO;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 일반 로그인을 처리한다 DAO 단위 테스트
 * 
 * @author 이백행
 * @since 2024-07-25
 */

@ContextConfiguration(classes = { ActionLoginLoginDAOTest.class, EgovTestAbstractDAO.class })
@Configuration
@ComponentScan(useDefaultFilters = false, basePackages = {
		"egovframework.com.uat.uia.service.impl", }, includeFilters = {
				@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { LoginDAO.class, }) })
@NoArgsConstructor
@Slf4j
public class ActionLoginLoginDAOTest extends EgovTestAbstractDAO {

	/**
	 * 일반 로그인, 인증서 로그인을 처리하는 DAO 클래스
	 */
	@Autowired
	private LoginDAO loginDAO;

	/**
	 * 일반 로그인을 처리한다
	 * 
	 * @throws Exception
	 */
	@Test
	public void actionLogin() throws Exception {
		// given
		LoginVO vo = new LoginVO();

		// when
		LoginVO result = loginDAO.actionLogin(vo);

		log.debug("result={}", result);
//		log.debug("getDeptId={}, {}", testDataDeptJobBx.getDeptJobBxId(), result.getDeptJobId());

		// then

//		assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), testDataDeptJob.getDeptJobId(),
//				result.getDeptJobId());
	}

}
