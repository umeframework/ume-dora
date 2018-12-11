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
import org.umeframework.dora.context.RequestContext;
import org.umeframework.dora.service.runner.impl.AjaxServiceRunnerImpl;
import org.umeframework.dora.util.StringUtil;

/**
 * Common HTTP API entries for request pattern mapping "/{COMMON_API_ROOT_MAPPING}/{system}/{resource}"
 *
 * @author Yue MA
 */
@RestController
public class CommonApiController extends BaseRestController {
	/**
	 * Constant of 'system' define
	 */
	public static final String PATH_VARIABLE_SYS = "system";
	/**
	 * Constant of 'resource/service' define
	 */
	public static final String PATH_VARIABLE_RES = "resource";
	/**
	 * Common API root URI mapping
	 */
	public static final String COMMON_API_ROOT_MAPPING = "capi";
	/**
	 * Common API service URI mapping:"/capi/{system}/{resource}"
	 */
	public static final String COMMON_API_SERVICE_MAPPING = "/" + COMMON_API_ROOT_MAPPING + "/{" + PATH_VARIABLE_SYS + "}/{" +  PATH_VARIABLE_RES + "}";

	/**
	 * doGet
	 * 
	 * @param request
	 * @param response
	 * @param system
	 * @param resource
	 * @param jsonInput
	 * @param printWriter
	 * @throws Throwable
	 */
	@ResponseBody
	@RequestMapping(value = "*" + COMMON_API_SERVICE_MAPPING + "/**", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String doGet(
	        HttpServletRequest request,
	        HttpServletResponse response,
	        @PathVariable(PATH_VARIABLE_SYS) String system,
	        @PathVariable(PATH_VARIABLE_RES) String resource,
	        @RequestBody(required = false) String jsonInput) throws Throwable {

		super.getLogger().debug(request.getMethod(), ":", request.getServletPath());
		if (jsonInput != null) {
			super.getLogger().debug("Http method:", request.getMethod(), "jsonInput:", jsonInput);
		}

		String pathParams = request.getServletPath();
		String matchStart = "/" + COMMON_API_ROOT_MAPPING + "/" + system + "/" + resource;
		pathParams = pathParams.substring(pathParams.indexOf(matchStart));
		pathParams = pathParams.replaceFirst(matchStart, "");
		pathParams = pathParams.startsWith("/") ? pathParams.substring(1) : pathParams;
		pathParams = pathParams.endsWith("/") ? pathParams.substring(0, pathParams.length() - 1) : pathParams;
		if (StringUtil.isNotEmpty(pathParams)) {
			RequestContext.open().set(AjaxServiceRunnerImpl.CONTEXT_KEY_SERVICE_URL_PARAMETER, pathParams.split("/"));
		}

		return execute(request, response, system, resource, jsonInput);
	}

	/**
	 * doPost
	 * 
	 * @param request
	 * @param response
	 * @param system
	 * @param resource
	 * @param jsonInput
	 * @param printWriter
	 * @throws Throwable
	 */
	@RequestMapping(value = "*" + COMMON_API_SERVICE_MAPPING, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String doPost(
	        HttpServletRequest request,
	        HttpServletResponse response,
	        @PathVariable(PATH_VARIABLE_SYS) String system,
	        @PathVariable(PATH_VARIABLE_RES) String resource,
	        @RequestBody(required = false) String jsonInput) throws Throwable {

		super.getLogger().debug(request.getMethod(), ":", request.getServletPath());
		if (jsonInput != null) {
			super.getLogger().debug("Http method:", request.getMethod(), "jsonInput:", jsonInput);
		}

		return execute(request, response, system, resource, jsonInput);
	}

	/**
	 * doPut
	 * 
	 * @param request
	 * @param response
	 * @param system
	 * @param resource
	 * @param jsonInput
	 * @param printWriter
	 * @throws Throwable
	 */
	@RequestMapping(value = "*" + COMMON_API_SERVICE_MAPPING, method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String doPut(
	        HttpServletRequest request,
	        HttpServletResponse response,
	        @PathVariable(PATH_VARIABLE_SYS) String system,
	        @PathVariable(PATH_VARIABLE_RES) String resource,
	        @RequestBody(required = false) String jsonInput) throws Throwable {

		super.getLogger().debug(request.getMethod(), ":", request.getServletPath());
		if (jsonInput != null) {
			super.getLogger().debug("Http method:", request.getMethod(), "jsonInput:", jsonInput);
		}

		return execute(request, response, system, resource, jsonInput);
	}

	/**
	 * doDelete
	 * 
	 * @param request
	 * @param response
	 * @param system
	 * @param resource
	 * @param jsonInput
	 * @param printWriter
	 * @throws Throwable
	 */
	@RequestMapping(value = "*" + COMMON_API_SERVICE_MAPPING, method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String doDelete(
	        HttpServletRequest request,
	        HttpServletResponse response,
	        @PathVariable(PATH_VARIABLE_SYS) String system,
	        @PathVariable(PATH_VARIABLE_RES) String resource,
	        @RequestBody(required = false) String jsonInput) throws Throwable {

		super.getLogger().debug(request.getMethod(), ":", request.getServletPath());
		if (jsonInput != null) {
			super.getLogger().debug("Http method:", request.getMethod(), "jsonInput:", jsonInput);
		}

		return execute(request, response, system, resource, jsonInput);
	}

	/**
	 * doDelete
	 * 
	 * @param request
	 * @param response
	 * @param system
	 * @param resource
	 * @param jsonInput
	 * @param printWriter
	 * @throws Throwable
	 */
	@RequestMapping(value = "*" + COMMON_API_SERVICE_MAPPING, method = RequestMethod.PATCH, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String doPatch(
	        HttpServletRequest request,
	        HttpServletResponse response,
	        @PathVariable(PATH_VARIABLE_SYS) String system,
	        @PathVariable(PATH_VARIABLE_RES) String resource,
	        @RequestBody(required = false) String jsonInput) throws Throwable {

		super.getLogger().debug(request.getMethod(), ":", request.getServletPath());
		if (jsonInput != null) {
			super.getLogger().debug("Http method:", request.getMethod(), "jsonInput:", jsonInput);
		}

		return execute(request, response, system, resource, jsonInput);
	}
}
