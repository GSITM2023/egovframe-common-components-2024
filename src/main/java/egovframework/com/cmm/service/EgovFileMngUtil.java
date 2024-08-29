package egovframework.com.cmm.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.egovframe.rte.fdl.cmmn.exception.BaseRuntimeException;
import org.egovframe.rte.fdl.cmmn.exception.FdlException;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import egovframework.com.cmm.EgovWebUtil;
import egovframework.com.cmm.util.EgovResourceCloseHelper;

/**
 * @author 공통 서비스 개발팀 이삼섭
 * @version 1.0
 * @Class Name : EgovFileMngUtil.java
 * @Description : 파일 관리 처리 관련 유틸리티
 * @Modification Information
 * 
 *               <pre>
 *   수정일               수정자            수정내용
 *   ----------   --------   ---------------------------
 *   2009.02.13  이삼섭          최초 생성
 *   2011.08.09  서준식          utl.fcc패키지와 Dependency제거를 위해 getTimeStamp()메서드 추가
 *   2017.03.03  조성원          시큐어코딩(ES)-부적절한 예외 처리[CWE-253, CWE-440, CWE-754]
 *   2020.10.26  신용호          parseFileInf(List<MultipartFile> files ...) 추가
 *   2022.11.11  김혜준          시큐어코딩 처리
 *   2024.08.30  이백행          컨트리뷰션 시큐어코딩 Exception 제거
 *               </pre>
 * 
 * @see
 * @since 2009. 02. 13
 *
 */
@Component("EgovFileMngUtil")
public class EgovFileMngUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovFileMngUtil.class);
	private static final String FILE_STORE_PATH = EgovProperties.getProperty("Globals.fileStorePath");

	public static final int BUFF_SIZE = 2048;

	@Resource(name = "egovFileIdGnrService")
	private EgovIdGnrService idgenService;

	/**
	 * 첨부파일에 대한 목록 정보를 취득한다.
	 *
	 * @param files
	 * @return
	 */
	public List<FileVO> parseFileInf(Map<String, MultipartFile> files, String KeyStr, int fileKeyParam,
			String atchFileId, String storePath) {
		int fileKey = fileKeyParam;

		String storePathString = "";
		String atchFileIdString = "";

		if (storePath == null || "".equals(storePath)) {
			storePathString = EgovProperties.getProperty("Globals.fileStorePath");
		} else {
			storePathString = EgovProperties.getProperty(storePath);
		}

		if (atchFileId == null || "".equals(atchFileId)) {
			try {
				atchFileIdString = idgenService.getNextStringId();
			} catch (FdlException e) {
				throw new BaseRuntimeException("FdlException: egovFileIdGnrService", e);
			}
		} else {
			atchFileIdString = atchFileId;
		}

		File saveFolder = new File(EgovWebUtil.filePathBlackList(storePathString));

		if (!saveFolder.exists() || saveFolder.isFile()) {
			// 2017.03.03 조성원 시큐어코딩(ES)-부적절한 예외 처리[CWE-253, CWE-440, CWE-754]
			if (saveFolder.mkdirs()) {
				LOGGER.debug("[file.mkdirs] saveFolder : Creation Success ");
			} else {
				LOGGER.error("[file.mkdirs] saveFolder : Creation Fail ");
			}
		}

		Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
		MultipartFile file;
		List<FileVO> result = new ArrayList<FileVO>();
		FileVO fvo;

		while (itr.hasNext()) {
			Entry<String, MultipartFile> entry = itr.next();
			file = entry.getValue();
			String orginFileName = file.getOriginalFilename();
			if (StringUtils.isEmpty(orginFileName)) {
				continue;
			}

			// 2022.11.11 시큐어코딩 처리
			String fileExt = FilenameUtils.getExtension(orginFileName).toUpperCase();
			String newName = KeyStr + getTimeStamp() + fileKey;
			long size = file.getSize();
			String filePath = storePathString + File.separator + newName;
			try {
				file.transferTo(new File(EgovWebUtil.filePathBlackList(filePath)));
			} catch (IllegalStateException | IOException e) {
				throw new BaseRuntimeException(
						"IllegalStateException | IOException: parseFileInf(Map<String, MultipartFile> files, transferTo",
						e);
			}

			fvo = new FileVO();
			fvo.setFileExtsn(fileExt);
			fvo.setFileStreCours(storePathString);
			fvo.setFileMg(Long.toString(size));
			fvo.setOrignlFileNm(orginFileName);
			fvo.setStreFileNm(newName);
			fvo.setAtchFileId(atchFileIdString);
			fvo.setFileSn(String.valueOf(fileKey));
			result.add(fvo);

			fileKey++;
		}

		return result;
	}

	/**
	 * 첨부파일에 대한 목록 정보를 취득한다.
	 *
	 * @param files
	 * @return
	 */
	public List<FileVO> parseFileInf(List<MultipartFile> files, String KeyStr, int fileKeyParam, String atchFileId,
			String storePath) {
		int fileKey = fileKeyParam;

		String storePathString = "";
		String atchFileIdString = "";

		if (storePath == null || "".equals(storePath)) {
			storePathString = EgovProperties.getProperty("Globals.fileStorePath");
		} else {
			storePathString = EgovProperties.getProperty(storePath);
		}

		if (atchFileId == null || "".equals(atchFileId)) {
			try {
				atchFileIdString = idgenService.getNextStringId();
			} catch (FdlException e) {
				throw new BaseRuntimeException("FdlException: egovFileIdGnrService", e);
			}
		} else {
			atchFileIdString = atchFileId;
		}

		File saveFolder = new File(EgovWebUtil.filePathBlackList(storePathString));

		if (!saveFolder.exists() || saveFolder.isFile()) {
			// 2017.03.03 조성원 시큐어코딩(ES)-부적절한 예외 처리[CWE-253, CWE-440, CWE-754]
			if (saveFolder.mkdirs()) {
				LOGGER.debug("[file.mkdirs] saveFolder : Creation Success ");
			} else {
				LOGGER.error("[file.mkdirs] saveFolder : Creation Fail ");
			}
		}

		List<FileVO> result = new ArrayList<FileVO>();
		FileVO fvo;

		for (MultipartFile file : files) {

			String orginFileName = file.getOriginalFilename();
			if (StringUtils.isEmpty(orginFileName)) {
				continue;
			}

			// 2022.11.11 시큐어코딩 처리
			String fileExt = FilenameUtils.getExtension(orginFileName).toUpperCase();
			String newName = KeyStr + getTimeStamp() + fileKey;
			long size = file.getSize();
			String filePath = storePathString + File.separator + newName;
			try {
				file.transferTo(new File(EgovWebUtil.filePathBlackList(filePath)));
			} catch (IllegalStateException | IOException e) {
				throw new BaseRuntimeException(
						"IllegalStateException | IOException: parseFileInf(List<MultipartFile> files, transferTo", e);
			}

			fvo = new FileVO();
			fvo.setFileExtsn(fileExt);
			fvo.setFileStreCours(storePathString);
			fvo.setFileMg(Long.toString(size));
			fvo.setOrignlFileNm(orginFileName);
			fvo.setStreFileNm(newName);
			fvo.setAtchFileId(atchFileIdString);
			fvo.setFileSn(String.valueOf(fileKey));

			result.add(fvo);

			fileKey++;
		}

		return result;
	}

	/**
	 * 첨부파일을 서버에 저장한다.
	 *
	 * @param file
	 * @param newName
	 * @param stordFilePath
	 */
	protected void writeUploadedFile(MultipartFile file, String newName) {
		InputStream stream = null;
		OutputStream bos = null;

		try {
			stream = file.getInputStream();
			File cFile = new File(FILE_STORE_PATH);

			if (!cFile.isDirectory()) {
				boolean _flag = cFile.mkdir();
				if (!_flag) {
					throw new IOException("Directory creation Failed ");
				}
			}

			String writeFilePath = EgovWebUtil
					.filePathBlackList(FILE_STORE_PATH + File.separator + FilenameUtils.getName(newName));
			bos = new FileOutputStream(writeFilePath);

			int bytesRead = 0;
			byte[] buffer = new byte[BUFF_SIZE];

			while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			throw new BaseRuntimeException("IOException: writeUploadedFile", e);
		} finally {
			EgovResourceCloseHelper.close(bos, stream);
		}
	}

	/**
	 * 서버의 파일을 다운로드한다.
	 *
	 * @param request
	 * @param response
	 */
	public static void downFile(HttpServletRequest request, HttpServletResponse response) {

		String downFileName = "";
		String orgFileName = "";

		if ((String) request.getAttribute("downFile") == null) {
			downFileName = "";
		} else {
			downFileName = (String) request.getAttribute("downFile");
		}

		if ((String) request.getAttribute("orgFileName") == null) {
			orgFileName = "";
		} else {
			orgFileName = (String) request.getAttribute("orginFile");
		}

		orgFileName = orgFileName.replaceAll("\r", "").replaceAll("\n", "");

		File file = new File(EgovWebUtil.filePathBlackList(downFileName));

		if (!file.exists()) {
			throw new BaseRuntimeException(
					"FileNotFoundException: downFile(HttpServletRequest request, " + downFileName);
		}

		if (!file.isFile()) {
			throw new BaseRuntimeException(
					"FileNotFoundException: downFile(HttpServletRequest request, " + downFileName);
		}

		byte[] buffer = new byte[BUFF_SIZE]; // buffer size 2K.

		response.setContentType("application/x-msdownload");
		try {
			response.setHeader("Content-Disposition:",
					"attachment; filename=" + new String(orgFileName.getBytes(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new BaseRuntimeException(
					"UnsupportedEncodingException: downFile(HttpServletRequest request, new String", e);
		}
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");

		BufferedInputStream fin = null;
		BufferedOutputStream outs = null;

		try {
			fin = new BufferedInputStream(new FileInputStream(file));
			outs = new BufferedOutputStream(response.getOutputStream());
			int read = 0;

			while ((read = fin.read(buffer)) != -1) {
				outs.write(buffer, 0, read);
			}
		} catch (IOException e) {
			throw new BaseRuntimeException("IOException: downFile(HttpServletRequest request,", e);
		} finally {
			EgovResourceCloseHelper.close(outs, fin);
		}
	}

	/**
	 * 첨부로 등록된 파일을 서버에 업로드한다.
	 *
	 * @param file
	 * @return
	 */
	public static HashMap<String, String> uploadFile(MultipartFile file) {

		HashMap<String, String> map = new HashMap<String, String>();
		long size = file.getSize();
		String orginFileName = file.getOriginalFilename();
		String fileExt = "";
		String newName = "";
		// 2022.11.11 시큐어코딩 처리
		if (StringUtils.isNotEmpty(orginFileName)) {
			fileExt = FilenameUtils.getExtension(orginFileName);
		}

		// 2012.11 KISA 보안조치
		newName = getTimeStamp();
		writeFile(file, newName);
		map.put(Globals.ORIGIN_FILE_NM, orginFileName);
		map.put(Globals.UPLOAD_FILE_NM, newName);
		map.put(Globals.FILE_EXT, fileExt);
		map.put(Globals.FILE_PATH, FILE_STORE_PATH);
		map.put(Globals.FILE_SIZE, String.valueOf(size));

		return map;
	}

	/**
	 * 파일을 실제 물리적인 경로에 생성한다.
	 *
	 * @param file
	 * @param newName
	 * @param stordFilePath
	 */
	protected static void writeFile(MultipartFile file, String newName) {
		InputStream stream = null;
		OutputStream bos = null;

		try {
			stream = file.getInputStream();
			File cFile = new File(EgovWebUtil.filePathBlackList(FILE_STORE_PATH));

			if (!cFile.isDirectory()) {
				// 2017.03.03 조성원 시큐어코딩(ES)-부적절한 예외 처리[CWE-253, CWE-440, CWE-754]
				if (cFile.mkdirs()) {
					LOGGER.debug("[file.mkdirs] saveFolder : Creation Success ");
				} else {
					LOGGER.error("[file.mkdirs] saveFolder : Creation Fail ");
				}
			}

			bos = new FileOutputStream(
					EgovWebUtil.filePathBlackList(FILE_STORE_PATH + File.separator + FilenameUtils.getName(newName)));

			int bytesRead = 0;
			byte[] buffer = new byte[BUFF_SIZE];

			while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			throw new BaseRuntimeException("IOException: writeFile", e);
		} finally {
			EgovResourceCloseHelper.close(bos, stream);
		}
	}

	/**
	 * 서버 파일에 대하여 다운로드를 처리한다.
	 *
	 * @param response
	 * @param streFileNm  파일저장 경로가 포함된 형태
	 * @param orignFileNm
	 */
	public void downFile(HttpServletResponse response, String streFileNm, String orignFileNm) {
		String downFileName = EgovWebUtil.filePathBlackList(streFileNm);
		String orgFileName = orignFileNm;

		File file = new File(downFileName);

		if (!file.exists()) {
			throw new BaseRuntimeException(
					"FileNotFoundException: downFile(HttpServletResponse response, " + downFileName);
		}

		if (!file.isFile()) {
			throw new BaseRuntimeException(
					"FileNotFoundException: downFile(HttpServletResponse response, " + downFileName);
		}

		int fSize = (int) file.length();
		if (fSize > 0) {
			BufferedInputStream in = null;

			try {
				in = new BufferedInputStream(new FileInputStream(file));

				String mimetype = "application/x-msdownload";

				// response.setBufferSize(fSize);
				response.setContentType(mimetype);
				response.setHeader("Content-Disposition:", "attachment; filename=" + orgFileName);
				response.setContentLength(fSize);
				// response.setHeader("Content-Transfer-Encoding","binary");
				// response.setHeader("Pragma","no-cache");
				// response.setHeader("Expires","0");
				FileCopyUtils.copy(in, response.getOutputStream());

				response.getOutputStream().flush();
				response.getOutputStream().close();
			} catch (IOException e) {
				throw new BaseRuntimeException("IOException: downFile(HttpServletResponse response,", e);
			} finally {
				EgovResourceCloseHelper.close(in);
			}
		}

		/*
		 * String uploadPath = propertiesService.getString("fileDir");
		 * 
		 * File uFile = new File(uploadPath, requestedFile); int fSize = (int)
		 * uFile.length();
		 * 
		 * if (fSize > 0) { BufferedInputStream in = new BufferedInputStream(new
		 * FileInputStream(uFile));
		 * 
		 * String mimetype = "text/html";
		 * 
		 * //response.setBufferSize(fSize); response.setContentType(mimetype);
		 * response.setHeader("Content-Disposition", "attachment; filename=\"" +
		 * requestedFile + "\""); response.setContentLength(fSize);
		 * 
		 * FileCopyUtils.copy(in, response.getOutputStream()); in.close();
		 * response.getOutputStream().flush(); response.getOutputStream().close(); }
		 * else { response.setContentType("text/html"); PrintWriter printwriter =
		 * response.getWriter(); printwriter.println("<html>");
		 * printwriter.println("<br><br><br><h2>Could not get file name:<br>" +
		 * requestedFile + "</h2>"); printwriter.
		 * println("<br><br><br><center><h3><a href='javascript: history.go(-1)'>Back</a></h3></center>"
		 * ); printwriter.println("<br><br><br>&copy; webAccess");
		 * printwriter.println("</html>"); printwriter.flush(); printwriter.close(); }
		 * //
		 */

		/*
		 * response.setContentType("application/x-msdownload");
		 * response.setHeader("Content-Disposition:", "attachment; filename=" + new
		 * String(orgFileName.getBytes(),"UTF-8" ));
		 * response.setHeader("Content-Transfer-Encoding","binary");
		 * response.setHeader("Pragma","no-cache"); response.setHeader("Expires","0");
		 * 
		 * BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
		 * BufferedOutputStream outs = new
		 * BufferedOutputStream(response.getOutputStream()); int read = 0;
		 * 
		 * while ((read = fin.read(b)) != -1) { outs.write(b,0,read); }
		 * log.debug(this.getClass().getName()
		 * +" BufferedOutputStream Write Complete!!! ");
		 * 
		 * outs.close(); fin.close(); //
		 */
	}

	/**
	 * 공통 컴포넌트 utl.fcc 패키지와 Dependency 제거를 위해 내부 메서드로 추가 정의함 응용어플리케이션에서 고유값을 사용하기 위해
	 * 시스템에서 17자리의 TIMESTAMP값을 구하는 기능
	 *
	 * @param
	 * @return Timestamp 값
	 * @see
	 */
	private static String getTimeStamp() {

		String rtnStr = null;

		// 문자열로 변환하기 위한 패턴 설정(연도-월-일 시:분:초:초(자정이후 초))
		String pattern = "yyyyMMddhhmmssSSS";

		SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
		Timestamp ts = new Timestamp(System.currentTimeMillis());

		rtnStr = sdfCurrent.format(ts.getTime());

		return rtnStr;
	}
}
