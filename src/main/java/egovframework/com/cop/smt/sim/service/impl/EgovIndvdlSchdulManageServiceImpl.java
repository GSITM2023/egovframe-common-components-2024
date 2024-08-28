package egovframework.com.cop.smt.sim.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.fdl.cmmn.exception.FdlException;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.cop.smt.sim.service.EgovIndvdlSchdulManageService;
import egovframework.com.cop.smt.sim.service.IndvdlSchdulManageVO;

/**
 * 일정관리를 처리하는 ServiceImpl Class 구현
 * 
 * @author 공통서비스 장동한
 * @since 2009.04.10
 * @version 1.0
 * @see
 *
 *      <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.10  장동한          최초 생성
 *   2024.08.29  이백행          컨트리뷰션 시큐어코딩 Exception 제거
 *
 *      </pre>
 */
@Service("egovIndvdlSchdulManageService")
public class EgovIndvdlSchdulManageServiceImpl extends EgovAbstractServiceImpl
		implements EgovIndvdlSchdulManageService {

	// final private Log log = LogFactory.getLog(this.getClass());

	@Resource(name = "indvdlSchdulManageDao")
	private IndvdlSchdulManageDao dao;

	@Resource(name = "deptSchdulManageIdGnrService")
	private EgovIdGnrService idgenService;

	/**
	 * 메인페이지/일정관리조회
	 * 
	 * @param map - 조회할 정보가 담긴 map
	 * @return List
	 */
	@Override
	public List<EgovMap> selectIndvdlSchdulManageMainList(Map<String, String> map) {
		return dao.selectIndvdlSchdulManageMainList(map);
	}

	/**
	 * 일정 목록을 Map(map)형식으로 조회한다.
	 * 
	 * @param Map(map) - 조회할 정보가 담긴 VO
	 * @return List
	 */
	@Override
	public List<EgovMap> selectIndvdlSchdulManageRetrieve(Map<String, String> map) {
		return dao.selectIndvdlSchdulManageRetrieve(map);
	}

	/**
	 * 일정 목록을 VO(model)형식으로 조회한다.
	 * 
	 * @param indvdlSchdulManageVO - 조회할 정보가 담긴 VO
	 * @return List
	 */
	@Override
	public IndvdlSchdulManageVO selectIndvdlSchdulManageDetailVO(IndvdlSchdulManageVO indvdlSchdulManageVO) {
		return dao.selectIndvdlSchdulManageDetailVO(indvdlSchdulManageVO);
	}

	/**
	 * 일정 목록을 조회한다.
	 * 
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return List
	 */
	@Override
	public List<IndvdlSchdulManageVO> selectIndvdlSchdulManageList(ComDefaultVO searchVO) {
		return dao.selectIndvdlSchdulManageList(searchVO);
	}

	/**
	 * 일정를(을) 상세조회 한다.
	 * 
	 * @param IndvdlSchdulManage - 회정정보가 담김 VO
	 * @return List
	 */
	@Override
	public List<IndvdlSchdulManageVO> selectIndvdlSchdulManageDetail(IndvdlSchdulManageVO indvdlSchdulManageVO) {
		return dao.selectIndvdlSchdulManageDetail(indvdlSchdulManageVO);
	}

	/**
	 * 일정를(을) 목록 전체 건수를(을) 조회한다.
	 * 
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return int
	 */
	@Override
	public int selectIndvdlSchdulManageListCnt(ComDefaultVO searchVO) {
		return dao.selectIndvdlSchdulManageListCnt(searchVO);
	}

	/**
	 * 일정를(을) 등록한다.
	 * 
	 * @param indvdlSchdulManageVO - 조회할 정보가 담긴 VO
	 * @throws Exception
	 */
	@Override
	public void insertIndvdlSchdulManage(IndvdlSchdulManageVO indvdlSchdulManageVO) throws Exception {
		String sMakeId;
		try {
			sMakeId = idgenService.getNextStringId();
		} catch (FdlException e) {
			throw processException("fail.common.msg", e);
		}
		indvdlSchdulManageVO.setSchdulId(sMakeId);

		dao.insertIndvdlSchdulManage(indvdlSchdulManageVO);
	}

	/**
	 * 일정를(을) 수정한다.
	 * 
	 * @param indvdlSchdulManageVO - 조회할 정보가 담긴 VO
	 */
	@Override
	public void updateIndvdlSchdulManage(IndvdlSchdulManageVO indvdlSchdulManageVO) {
		dao.updateIndvdlSchdulManage(indvdlSchdulManageVO);
	}

	/**
	 * 일정를(을) 삭제한다.
	 * 
	 * @param indvdlSchdulManageVO - 조회할 정보가 담긴 VO
	 */
	@Override
	public void deleteIndvdlSchdulManage(IndvdlSchdulManageVO indvdlSchdulManageVO) {
		dao.deleteIndvdlSchdulManage(indvdlSchdulManageVO);
	}
}
