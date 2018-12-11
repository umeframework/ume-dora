package org.umeframework.dora.web.interceptor;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.umeframework.dora.ajax.AjaxRender;
import org.umeframework.dora.ajax.ParserException;
import org.umeframework.dora.contant.BeanConfigConst;
import org.umeframework.dora.exception.AuthenticationException;
import org.umeframework.dora.service.ServiceErrorResponse;

/**
 * GlobalExceptionController
 * 
 * @author Yue MA
 */
@ControllerAdvice
public class GlobalExceptionInterceptor {
	/**
	 * exception handler map
	 */
	@Resource(name = BeanConfigConst.DEFAULT_EXCEPTION_HANDLER)
	private org.umeframework.dora.exception.ExceptionHandler exceptionHandler;

	/**
	 * ajax render
	 */
	@Resource(name = BeanConfigConst.DEFAULT_AJAX_RENDER)
	private AjaxRender<String> ajaxRender;
	
	/**
	 * render
	 * 
	 * @param cause
	 * @return
	 */
	private String render(HttpStatus httpStatus, Throwable cause) {
	    ServiceErrorResponse result = new ServiceErrorResponse();
	    // setup HTTP status code
	    result.setStatusCode(httpStatus.value());
	    // setup exception details
		exceptionHandler.handleException(result, cause);
		// render error object to JSON
		String jsonOutputData = ajaxRender.render(result);
		return jsonOutputData;
	}

	/**
	 * Authentication Exception
	 * 
	 * @param cause
	 * @return
	 */
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	@ExceptionHandler(AuthenticationException.class)
	public String handleAuthenticationException(AuthenticationException cause) {
		return render(HttpStatus.UNAUTHORIZED, cause);
	}
	
	/**
	 * Parser Exception
	 * 
	 * @param cause
	 * @return
	 */
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ResponseBody
	@ExceptionHandler(ParserException.class)
	public String handleParserException(ParserException cause) {
		return render(HttpStatus.NOT_ACCEPTABLE, cause);
	}
	
	/**
	 * Default Exception
	 * 
	 * @param cause
	 * @return
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	@ExceptionHandler(Throwable.class)
	public String handleException(Throwable cause) {
		return render(HttpStatus.INTERNAL_SERVER_ERROR, cause);
	}

	/**
	 * handleHttpMessageNotReadableException
	 * 
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public String handleHttpMessageNotReadableException(HttpMessageNotReadableException cause) {
		return render(HttpStatus.BAD_REQUEST, cause);
	}

	/**
	 * handleHttpRequestMethodNotSupportedException
	 * 
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public String handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException cause) {
		return render(HttpStatus.BAD_REQUEST, cause);
	}

	/**
	 * handleHttpMediaTypeNotSupportedException
	 * 
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public String handleHttpMediaTypeNotSupportedException(Exception cause) {
		return render(HttpStatus.UNSUPPORTED_MEDIA_TYPE, cause);
	}
}
