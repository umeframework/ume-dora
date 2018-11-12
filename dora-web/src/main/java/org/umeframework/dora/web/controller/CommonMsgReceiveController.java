/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.umeframework.dora.util.StringUtil;

/**
 * HTTP message receiver implementation class.<br>
 * Receive HTTP post invoke from 3rd systems with specific charSet. <br>
 *
 * @author Yue MA
 */
@RestController
@RequestMapping("ext/")
public class CommonMsgReceiveController extends BaseRestController {
	/**
	 * process post request for "x-www-form-urlencoded" data type
	 *
	 * @param request
	 * @param response
	 * @param charset
	 * @param system
	 * @param resource
	 * @param jsonInput
	 * @param printWriter
	 * @throws Throwable 
	 */
	@RequestMapping(value = "post/{charset}/{system}/{resource}", method = RequestMethod.POST, headers = { "content-type=application/x-www-form-urlencoded" })
	@ResponseBody
	public void doPostMultipartFormDataWithURLCharset(
	        HttpServletRequest request,
	        HttpServletResponse response,
	        @PathVariable("charset") String charset,
	        @PathVariable("system") String system,
	        @PathVariable("resource") String resource,
	        @RequestBody(required=false) String jsonInput) throws Throwable {

		if (StringUtil.isNotEmpty(jsonInput)) {
			jsonInput = decode(jsonInput, charset);
		}

		execute(request, response, system, resource, jsonInput);
	}

	/**
	 * process get request for "x-www-form-urlencoded" data type
	 *
	 * @param request
	 * @param response
	 * @param charset
	 * @param system
	 * @param serviceId
	 * @param jsonInput
	 * @param printWriter
	 * @throws Throwable 
	 */
	@RequestMapping(value = "get/{charset}/{system}/{resource}/{jsonInput}", method = RequestMethod.GET, headers = { "content-type=application/x-www-form-urlencoded" })
	@ResponseBody
	public void doGetMultipartFormDataWithURLCharset(
	        HttpServletRequest request,
	        HttpServletResponse response,
	        @PathVariable("charset") String charset,
	        @PathVariable("system") String system,
	        @PathVariable("resource") String resource,
	        @RequestBody(required=false) String jsonInput) throws Throwable {

		if (StringUtil.isNotEmpty(jsonInput)) {
			jsonInput = decode(jsonInput, charset);
		}

		execute(request, response, system, resource, jsonInput);
	}
}
