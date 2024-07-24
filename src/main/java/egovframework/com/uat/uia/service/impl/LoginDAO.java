package egovframework.com.uat.uia.service.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import lombok.NoArgsConstructor;

/**
 * 일반 로그인, 인증서 로그인을 처리하는 DAO 클래스
 * 
 * @author 공통서비스 개발팀 박지욱
 * @since 2009.03.06
 * @version 1.0
 * @see
 *
 *      <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일               수정자            수정내용
 *  ----------   --------   ---------------------------
 *  2009.03.06   박지욱            최초 생성
 *  2011.08.26   서준식            EsntlId를 이용한 로그인 추가
 *  2017.07.21   장동한            로그인인증제한 작업
 *  2020.07.08   신용호            비밀번호를 수정한후 경과한 날짜 조회
 *  2021.05.30   정진오            디지털원패스 인증 회원 조회
 *      </pre>
 */
@Repository("loginDAO")
@NoArgsConstructor
public class LoginDAO extends EgovComAbstractDAO {

	/**
	 * 2011.08.26 EsntlId를 이용한 로그인을 처리한다
	 * 
	 * @param loginVO LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
	public LoginVO actionLoginByEsntlId(final LoginVO loginVO) {
		return (LoginVO) selectOne("LoginUsr.ssoLoginByEsntlId", loginVO);
	}

	/**
	 * 일반 로그인을 처리한다
	 * 
	 * @param loginVO LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
	public LoginVO actionLogin(final LoginVO loginVO) {
		return (LoginVO) selectOne("LoginUsr.actionLogin", loginVO);
	}

	/**
	 * 인증서 로그인을 처리한다
	 * 
	 * @param loginVO LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
	public LoginVO actionCrtfctLogin(final LoginVO loginVO) {

		return (LoginVO) selectOne("LoginUsr.actionCrtfctLogin", loginVO);
	}

	/**
	 * 아이디를 찾는다.
	 * 
	 * @param loginVO LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
	public LoginVO searchId(final LoginVO loginVO) {

		return (LoginVO) selectOne("LoginUsr.searchId", loginVO);
	}

	/**
	 * 비밀번호를 찾는다.
	 * 
	 * @param loginVO LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
	public LoginVO searchPassword(final LoginVO loginVO) {

		return (LoginVO) selectOne("LoginUsr.searchPassword", loginVO);
	}

	/**
	 * 변경된 비밀번호를 저장한다.
	 * 
	 * @param loginVO LoginVO
	 * @exception Exception
	 */
	public void updatePassword(final LoginVO loginVO) {
		update("LoginUsr.updatePassword", loginVO);
	}

	/**
	 * 로그인인증제한 내역을 조회한다.
	 * 
	 * @param loginVO LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
	public Map<?, ?> selectLoginIncorrect(final LoginVO loginVO) {
		return (Map<?, ?>) selectOne("LoginUsr.selectLoginIncorrect", loginVO);
	}

	/**
	 * 로그인인증제한 내역을 업데이트 한다.
	 * 
	 * @param vo LoginVO
	 * @return vod
	 * @exception Exception
	 */
	public void updateLoginIncorrect(final Map<?, ?> map) {
		update("LoginUsr.updateLoginIncorrect" + map.get("USER_SE"), map);
	}

	/**
	 * 비밀번호를 수정한후 경과한 날짜를 조회한다.
	 * 
	 * @param loginVO LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
	public int selectPassedDayChangePWD(final LoginVO loginVO) {
		return selectOne("LoginUsr.selectPassedDayChangePWD", loginVO);
	}

	/**
	 * 디지털원패스 인증 회원 조회한다.
	 * 
	 * @param id
	 * @return LoginVO
	 * @exception Exception
	 */
	public LoginVO onepassLogin(final String id) {
		return (LoginVO) selectOne("LoginUsr.onepassLogin", id);
	}

}
