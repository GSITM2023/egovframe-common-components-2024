package egovframework.com.cop.adb.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import egovframework.com.cop.adb.service.AddressBook;
import egovframework.com.cop.adb.service.AddressBookUser;
import egovframework.com.cop.adb.service.AddressBookUserVO;
import egovframework.com.cop.adb.service.AddressBookVO;

/**
 * @Class Name : AdressBookDAO.java
 * @Description : 주소록을 관리하는 서비스를 정의하기위한 데이터 접근 클래스
 * @Modification Information
 * 
 *               <pre>
 *    수정일          수정자         수정내용
 *   -------        -------     -------------------
 *   2009.09.25  윤성록          최초 생성
 *   2016.12.13  최두영          클래스명 변경
 *   2024.09.05  이백행          컨트리뷰션 시큐어코딩 Exception 제거
 *               </pre>
 * 
 * @author 공통 컴포넌트 개발팀 윤성록
 * @since 2009. 9. 25.
 * @version
 * @see
 *
 */
@Repository("AdressBookDAO")
public class AddressBookDAO extends EgovComAbstractDAO {

	/**
	 * 주어진 조건에 따른 주소록목록을 불러온다.
	 * 
	 * @param AddressBookVO
	 * @return
	 */
	public List<AddressBookVO> selectAdressBookList(AddressBookVO adbkVO) {
		return selectList("AdressBookDAO.selectAdressBookList", adbkVO);
	}

	/**
	 * 주어진 조건에 따라 주소록에 추가할 사용자목록을 불러온다.
	 * 
	 * @param AddressBookUserVO
	 * @return
	 */
	public List<AddressBookUserVO> selectManList(AddressBookUserVO adbkUserVO) {
		return selectList("AdressBookDAO.selectManList", adbkUserVO);
	}

	/**
	 * 주어진 조건에 따라 주소록에 추가할 명함목록을 불러온다.
	 * 
	 * @param AddressBookUserVO
	 * @return
	 */
	public List<AddressBookUserVO> selectCardList(AddressBookUserVO adbkUserVO) {
		return selectList("AdressBookDAO.selectCardList", adbkUserVO);
	}

	/**
	 * 주어진 조건에 따라 주소록에 기등록된 구성원의 목록을 불러온다.
	 * 
	 * @param AddressBookVO
	 * @return
	 */
	public List<AddressBookUser> selectUserList(AddressBookVO adbkVO) {
		return selectList("AdressBookDAO.selectUserList", adbkVO);
	}

	/**
	 * 주어진 조건에 맞는 주소록을 불러온다.
	 * 
	 * @param AddressBookVO
	 * @return
	 */
	public AddressBookVO selectAdressBook(AddressBookVO adbkVO) {
		return (AddressBookVO) selectOne("AdressBookDAO.selectAdressBook", adbkVO);
	}

	/**
	 * 주소록 정보를 등록한다.
	 * 
	 * @param AddressBook
	 */
	public void insertAdressBook(AddressBook addressBook) {
		insert("AdressBookDAO.insertAdressBook", addressBook);
	}

	/**
	 * 주소록을 구성하는 구성원을 등록한다.
	 * 
	 * @param AddressBookUser
	 */
	public void insertAdressBookUser(AddressBookUser addressBookUser) {
		insert("AdressBookDAO.insertAdressBookUser", addressBookUser);
	}

	/**
	 * 주소록 정보를 수정한다.
	 * 
	 * @param AddressBook
	 */
	public void updateAdressBook(AddressBook addressBook) {
		update("AdressBookDAO.updateAdressBook", addressBook);
	}

	/**
	 * 주소록 구성원을 삭제한다.
	 * 
	 * @param AddressBookUser
	 */
	public void deleteAdressBookUser(AddressBookUser adbkUser) {
		delete("AdressBookDAO.deleteAdressBookUser", adbkUser);
	}

	/**
	 * 주소록 목록에 대한 전체 건수를 조회한다.
	 * 
	 * @param AddressBookUser
	 */
	public int selectAdressBookListCnt(AddressBookVO adbkVO) {
		return (Integer) selectOne("AdressBookDAO.selectAdressBookListCnt", adbkVO);
	}

	/**
	 * 사용자 목록에 대한 전체 건수를 조회한다.
	 * 
	 * @param AddressBookUser
	 */
	public int selectManListCnt(AddressBookUserVO adbkUserVO) {
		return (Integer) selectOne("AdressBookDAO.selectManListCnt", adbkUserVO);
	}

	/**
	 * 명함 목록에 대한 전체 건수를 조회한다.
	 * 
	 * @param AddressBookUser
	 */
	public int selectCardListCnt(AddressBookUserVO adbkUserVO) {
		return (Integer) selectOne("AdressBookDAO.selectCardListCnt", adbkUserVO);
	}

	/**
	 * 주소록을 구성할 사용자의 정보를 조회한다.
	 * 
	 * @param AddressBookUser
	 */
	public AddressBookUser selectManUser(String id) {
		return (AddressBookUser) selectOne("AdressBookDAO.selectManUser", id);
	}

	/**
	 * 주소록을 구성할 명함의 정보를 조회한다.
	 * 
	 * @param AddressBookUser
	 */
	public AddressBookUser selectCardUser(String id) {
		return (AddressBookUser) selectOne("AdressBookDAO.selectCardUser", id);
	}

}
