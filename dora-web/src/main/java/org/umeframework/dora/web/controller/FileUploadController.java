/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.exception.ApplicationException;
import org.umeframework.dora.util.DateUtil;
import org.umeframework.dora.util.StringUtil;

/**
 * Common upload entry for request pattern mapping "file/upload/{system}/{resource}"
 *
 * @author Yue MA
 */
@RestController
@RequestMapping("file/upload/")
public class FileUploadController extends BaseRestController {
	/**
	 * file upload directory setup
	 */
	@Autowired(required = false)
	@Qualifier(BeanConfigConst.DEFAULT_FILE_UPLOAD_DIR)
	private String fileUploadDir;
	/**
	 * file upload extensions setup
	 */
	@Autowired(required = false)
	@Qualifier(BeanConfigConst.DEFAULT_FILE_UPLOAD_EXTENSIONS)
	private String fileUploadExtensions;

	/**
	 * doPost
	 * 
	 * @param request
	 * @param response
	 * @param system
	 * @param resource
	 * @param file
	 * @param jsonInput
	 * @param printWriter
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "{system}/{resource}", method = RequestMethod.POST)
	@ResponseBody
	public String doPost(
	        HttpServletRequest request,
	        HttpServletResponse response,
	        @PathVariable("system") String system,
	        @PathVariable("resource") String resource,
	        @RequestParam("file") CommonsMultipartFile file,
	        @RequestBody(required = false) String jsonInput) throws Throwable {

		doUpload(request, file);
		return execute(request, response, system, resource, jsonInput);
	}

	/**
	 * doUpload
	 * 
	 * @param request
	 * @param file
	 */
	protected void doUpload(HttpServletRequest request, CommonsMultipartFile file) {

		if (StringUtil.isEmpty(fileUploadDir)) {
			fileUploadDir = "TEMP-FILEUPLOAD-DIR";
		}
		if (!fileUploadDir.startsWith("/") && !fileUploadDir.contains(":")) {
			fileUploadDir = request.getSession().getServletContext().getRealPath("/") + "/" + fileUploadDir;
		}

		File path = new File(fileUploadDir);
		if (!path.exists()) {
			path.mkdirs();
			super.getLogger().info("Dir created for file upload:" + path);
		}

		if (!file.isEmpty()) {
			String fileName = file.getOriginalFilename();
			String fileExtension = FilenameUtils.getExtension(fileName);

			if (StringUtil.isNotEmpty(fileUploadExtensions)) {
				if (!Arrays.asList(fileUploadExtensions.split(";")).contains(fileExtension)) {
					throw new ApplicationException("Current system does not support this kind of file upload:" + fileExtension);
				}
			}

			File uploadFile = new File(fileUploadDir, fileName);
			if (uploadFile.exists() && uploadFile.isFile()) {
				String dateStr = DateUtil.dateToString(new Date(System.currentTimeMillis()), DateUtil.FORMAT.YYMMDDHHMMSSMMM);
				uploadFile.renameTo(new File(fileUploadDir, fileName + "." + dateStr));
			}
			uploadFile = new File(fileUploadDir, fileName);
			try {
				FileCopyUtils.copy(file.getBytes(), uploadFile);
			} catch (IOException e) {
				throw new ApplicationException(e, "Failed to uploading file " + uploadFile);
			}
		}

	}

}
