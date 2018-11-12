///* 
// * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
// */
//package org.umeframework.dora.web.controller;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import org.umeframework.dora.bean.BeanFactory;
//import org.umeframework.dora.service.BaseComponent;
//import org.umeframework.dora.service.ServiceResponse;
//import org.umeframework.dora.service.runner.impl.AjaxServiceRunnerImpl;
//import org.umeframework.dora.web.interceptor.DownloadEntity;
//import org.umeframework.dora.web.interceptor.FileDownloadEntity;
//
///**
// * Common download entry for request pattern mapping "file/download/{system}/{resource}"
// *
// * @author Yue MA
// */
//@RestController
//@RequestMapping("file/download/")
//public class FileDownloadController extends BaseComponent {
//	/**
//	 * Default bean factory instance.
//	 */
//	@Resource(name = "doraBeanFactory")
//	private BeanFactory beanFactory;
//
//	/**
//	 * doGetWithPathParam
//	 * 
//	 * @param request
//	 * @param response
//	 * @param system
//	 * @param service
//	 * @param jsonInput
//	 * @param printWriter
//	 * @throws Throwable
//	 */
//	@RequestMapping(value = "{system}/{resource}/{jsonInput}", method = RequestMethod.GET)
//	public String doGetWithPathParam(
//	        HttpServletRequest request,
//	        HttpServletResponse response,
//	        @PathVariable("system") String system,
//	        @PathVariable("resource") String resource,
//	        @PathVariable("jsonInput") String jsonInput) throws Throwable {
//
//		AjaxServiceRunnerImpl serviceRunner = null;
//		try {
//			serviceRunner = beanFactory.getBean(system);
//		} catch (Exception e) {
//			getLogger().error("No found service runner:" + system, e);
//			throw e;
//		}
//		String jsonOutput = null;
//
//		ServiceResponse<Object> serviceResponse = serviceRunner.executeAjax(resource, jsonInput);
//		doDownload(serviceResponse, request, response);
//		jsonOutput = serviceRunner.render(serviceResponse);
//
//		return jsonOutput;
//	}
//
//	/**
//	 * execute HTTP post-request by the defined service intercept chains.
//	 * 
//	 * @param chain
//	 * @param requestCategory
//	 * @param request
//	 * @param response
//	 * @param system
//	 * @param serviceId
//	 * @param printWriter
//	 * @throws IOException 
//	 */
//	protected void doDownload(ServiceResponse<Object> serviceResponse, HttpServletRequest request, HttpServletResponse response) throws IOException {
//		if (serviceResponse != null && serviceResponse.getResultObject() != null && serviceResponse.getResultObject() instanceof DownloadEntity) {
//			// In case of file download
//			DownloadEntity download = (DownloadEntity) serviceResponse.getResultObject();
//			InputStream inputStream = null;
//			OutputStream outputStream = null;
//			try {
//				inputStream = download.getInputStream();
//				if (inputStream != null) {
//					String downloadName = download.getDownloadName();
//					response.reset();
//					response.setHeader("Content-Disposition", "attachment;fileName=" + downloadName);
//					response.setContentType(download.getContentType());
//					response.setCharacterEncoding(download.getCharacterEncoding());
//					outputStream = response.getOutputStream();
//					byte[] buffer = new byte[2048];
//					int length;
//					while ((length = inputStream.read(buffer)) > 0) {
//						outputStream.write(buffer, 0, length);
//					}
//				}
//			} catch (IOException e) {
//				throw e;
//			} finally {
//				try {
//					if (outputStream != null) {
//						outputStream.close();
//					}
//					if (inputStream != null) {
//						inputStream.close();
//					}
//				} catch (IOException e) {
//					throw e;
//				}
//			}
//
//		}
//	}
//
//}
