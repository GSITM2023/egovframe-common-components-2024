package egovframework.com.uat.uia.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.config.EgovLoginConfig;
import egovframework.com.cop.ems.service.EgovSndngMailRegistService;
import egovframework.com.cop.ems.service.SndngMailVO;
import egovframework.com.uat.uia.service.EgovLoginService;
import egovframework.com.utl.fcc.service.EgovNumberUtil;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.com.utl.sim.service.EgovFileScrty;
import lombok.RequiredArgsConstructor;

/**
 * 일반 로그인, 인증서 로그인을 처리하는 비즈니스 구현 클래스
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
 *  2014.12.08   이기하            암호화방식 변경(EgovFileScrty.encryptPassword)
 *  2017.07.21   장동한            로그인인증제한 작업
 *  2020.07.08   신용호            비밀번호를 수정한후 경과한 날짜 조회
 *  2021.05.30   정진오            디지털원패스 인증 회원 조회
 *      </pre>
 */
@Service("loginService")
@RequiredArgsConstructor
public class EgovLoginServiceImpl extends EgovAbstractServiceImpl implements EgovLoginService {

	/**
	 * 일반 로그인, 인증서 로그인을 처리하는 DAO 클래스
	 */
	@Resource(name = "loginDAO")
	private LoginDAO loginDAO;

	/** EgovSndngMailRegistService */
	@Resource(name = "sndngMailRegistService")
	private EgovSndngMailRegistService sndngMailRegistService;

	/**
	 * EgovLoginConfig 클래스 사용자 인증수행제한에 대한 설정을 관리하는 클래스 N/A
	 */
	@Resource(name = "egovLoginConfig")
	private EgovLoginConfig egovLoginConfig;

	/**
	 * 
	 */
	private static final String Y_STRING = "Y";

	/**
	 * 2011.08.26 EsntlId를 이용한 로그인을 처리한다
	 * 
	 * @param loginVO LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
	@Override
	public LoginVO actionLoginByEsntlId(final LoginVO loginVO) {

		LoginVO result = loginDAO.actionLoginByEsntlId(loginVO);

		// 3. 결과를 리턴한다.
		if (result != null && !"".equals(result.getId()) && !"".equals(result.getPassword())) {
			return result;
		} else {
			result = new LoginVO();
		}

		return result;
	}

	/**
	 * 일반 로그인을 처리한다
	 * 
	 * @param loginVO LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
	@Override
	public LoginVO actionLogin(final LoginVO loginVO) {

		// 1. 입력한 비밀번호를 암호화한다.
		final String enpassword = EgovFileScrty.encryptPassword(loginVO.getPassword(), loginVO.getId());
		loginVO.setPassword(enpassword);

		// 2. 아이디와 암호화된 비밀번호가 DB와 일치하는지 확인한다.
		LoginVO result = loginDAO.actionLogin(loginVO);

		// 3. 결과를 리턴한다.
		if (result != null && !"".equals(result.getId()) && !"".equals(result.getPassword())) {
			return result;
		} else {
			result = new LoginVO();
		}

		return result;
	}

	/**
	 * 인증서 로그인을 처리한다
	 * 
	 * @param loginVO LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
	@Override
	public LoginVO actionCrtfctLogin(final LoginVO loginVO) {

		// 1. DN값으로 ID, PW를 조회한다.
		LoginVO result = loginDAO.actionCrtfctLogin(loginVO);

		// 3. 결과를 리턴한다.
		if (result != null && !"".equals(result.getId()) && !"".equals(result.getPassword())) {
			return result;
		} else {
			result = new LoginVO();
		}

		return result;
	}

	/**
	 * 아이디를 찾는다.
	 * 
	 * @param loginVO LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
	@Override
	public LoginVO searchId(final LoginVO loginVO) {

		// 1. 이름, 이메일주소가 DB와 일치하는 사용자 ID를 조회한다.
		LoginVO result = loginDAO.searchId(loginVO);

		// 2. 결과를 리턴한다.
		if (result != null && !"".equals(result.getId())) {
			return result;
		} else {
			result = new LoginVO();
		}

		return result;
	}

	/**
	 * 비밀번호를 찾는다.
	 * 
	 * @param loginVO LoginVO
	 * @return boolean
	 * @exception Exception
	 */
	@Override
	public boolean searchPassword(final LoginVO loginVO) {

		// 1. 아이디, 이름, 이메일주소, 비밀번호 힌트, 비밀번호 정답이 DB와 일치하는 사용자 Password를 조회한다.
		final LoginVO result = loginDAO.searchPassword(loginVO);
		if (result == null || result.getPassword() == null || "".equals(result.getPassword())) {
			return false;
		}

		// 2. 임시 비밀번호를 생성한다.(영+영+숫+영+영+숫+영+영=8자리)
		String newpassword = "";
		for (int i = 1; i <= 8; i++) {
			if (i % 3 == 0) {
				// 숫자
				newpassword += EgovNumberUtil.getRandomNum(0, 9);
			} else {
				// 영자
				newpassword += EgovStringUtil.getRandomStr('a', 'z');
			}
		}

		// 3. 임시 비밀번호를 암호화하여 DB에 저장한다.
		final LoginVO pwVO = new LoginVO();
		final String enpassword = EgovFileScrty.encryptPassword(newpassword, loginVO.getId());
		pwVO.setId(loginVO.getId());
		pwVO.setPassword(enpassword);
		pwVO.setUserSe(loginVO.getUserSe());
		loginDAO.updatePassword(pwVO);

		// 4. 임시 비밀번호를 이메일 발송한다.(메일연동솔루션 활용)
		final SndngMailVO sndngMailVO = new SndngMailVO();
		sndngMailVO.setDsptchPerson("webmaster");
		sndngMailVO.setRecptnPerson(loginVO.getEmail());
		sndngMailVO.setSj("[MOIS] 임시 비밀번호를 발송했습니다.");
		sndngMailVO.setEmailCn("고객님의 임시 비밀번호는 " + newpassword + " 입니다.");
		sndngMailVO.setAtchFileId("");

		return sndngMailRegistService.insertSndngMail(sndngMailVO);
	}

	/**
	 * 로그인인증제한을 조회한다.
	 * 
	 * @param loginVO LoginVO
	 * @return Map
	 * @exception Exception
	 */
	@Override
	public Map<?, ?> selectLoginIncorrect(final LoginVO loginVO) {
		return loginDAO.selectLoginIncorrect(loginVO);
	}

	/**
	 * 로그인인증제한을 처리한다.
	 * 
	 * @param loginVO LoginVO
	 * @param loginVO mapLockUserInfo
	 * @return String
	 * @exception Exception
	 */
	@Override
	public String processLoginIncorrect(final LoginVO loginVO, final Map<?, ?> mapLockUserInfo) {
		String sRtnCode = "C";
		// KISA 보안약점 조치 (2018-10-29, 윤창원)
		final String enpassword = EgovFileScrty.encryptPassword(loginVO.getPassword(),
				EgovStringUtil.isNullToString(loginVO.getId()));
		final Map<String, String> mapParam = new ConcurrentHashMap<String, String>();
		mapParam.put("USER_SE", loginVO.getUserSe());
		mapParam.put("id", EgovStringUtil.isNullToString(loginVO.getId()));// KISA 보안약점 조치 (2018-10-29, 윤창원)
		// 잠김시
		if (Y_STRING.equals(mapLockUserInfo.get("lockAt"))) {
			sRtnCode = "L";
			// 패드워드 인증시
		} else if (((String) mapLockUserInfo.get("userPw")).equals(enpassword)) {
			// LOCK 해제
			mapParam.put("updateAt", "E");
			loginDAO.updateLoginIncorrect(mapParam);
			sRtnCode = "E";
			// 패드워드 비인증시
		} else if (!"Y".equals(mapLockUserInfo.get("lockAt"))) {
			// LOCK 설정
			if (Integer.parseInt(String.valueOf(mapLockUserInfo.get("lockCnt"))) + 1 >= egovLoginConfig
					.getLockCount()) {
				mapParam.put("updateAt", "L");
				loginDAO.updateLoginIncorrect(mapParam);
				sRtnCode = "L";
				// LOCK 증가
			} else {
				mapParam.put("updateAt", "C");
				loginDAO.updateLoginIncorrect(mapParam);
				sRtnCode = "C";
			}
		}
		return sRtnCode;
	}

	/**
	 * 비밀번호를 수정한후 경과한 날짜를 조회한다.
	 * 
	 * @param loginVO LoginVO
	 * @return int
	 * @exception Exception
	 */
	@Override
	public int selectPassedDayChangePWD(final LoginVO loginVO) {
		return loginDAO.selectPassedDayChangePWD(loginVO);
	}

	/**
	 * 디지털원패스 인증 회원 조회한다.
	 * 
	 * @param id
	 * @return LoginVO
	 * @exception Exception
	 */
	@Override
	public LoginVO onepassLogin(final String id) {
		return loginDAO.onepassLogin(id);
	}

}
