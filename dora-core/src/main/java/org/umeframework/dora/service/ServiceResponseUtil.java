/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;
import org.umeframework.dora.ajax.ParserException;
import org.umeframework.dora.ajax.impl.JsonParserImpl;
import org.umeframework.dora.message.Message;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * ServiceResponseUtil.<br>
 * 
 * @author Yue MA
 *
 */
@Deprecated
public abstract class ServiceResponseUtil {
	// /**
	// * main
	// *
	// * @param args
	// */
	// public static void main(String[] args) {
	// try {
	// org.umeframework.dora.ajax.impl.JsonRenderImpl render = new org.umeframework.dora.ajax.impl.JsonRenderImpl();
	// // org.umeframework.dora.ajax.impl.JsonParserImpl parser = new org.umeframework.dora.ajax.impl.JsonParserImpl();
	// org.umeframework.dora.service.TableObject ro = new org.umeframework.dora.service.TableObject();
	// ro.setTheSchema("Schema001");
	// List<org.umeframework.dora.service.TableObject> rol = new ArrayList<>();
	// rol.add(ro);
	//
	// ServiceResponse<List<org.umeframework.dora.service.TableObject>> r = new ServiceResponse<List<org.umeframework.dora.service.TableObject>>();
	// r.setResultObject(rol);
	// r.addException(new Message("M01", new Object[] { 100, Double.MAX_VALUE, "abc", null, true }));
	// String json = render.render(r);
	// System.out.println(json);
	//
	// ServiceResponse<?> nr = parseFromJson(json, List.class, new Type[] {org.umeframework.dora.service.TableObject.class});
	// System.out.println(render.render(nr));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * Filed name in "ServiceResponse" class
	 */
	private static final String RESPONSE_FIELD_RESULT_OBJECT = "resultObject";
	/**
	 * Filed name in "ServiceResponse" class
	 */
	private static final String RESPONSE_FIELD_RESULT_CODE = "resultCode";
	/**
	 * Filed name in "ServiceResponse" class
	 */
	private static final String RESPONSE_FIELD_EXCEPTIONS = "exceptions";
	/**
	 * Filed name in "ServiceResponse" class
	 */
	private static final String RESPONSE_FIELD_MESSAGES = "messages";

	/**
	 * parseFromJson
	 * 
	 * @param jsonStr
	 * @param resultObjectRawType
	 * @param actualTypeArgs
	 * @param ownerType
	 * @return
	 */
	public static <T> ServiceResponse<T> parseFromJson(String jsonStr, Class<T> resultObjectRawType, Type... actualTypeArgs) {
		ParameterizedType parameterizedType = null;
		if (actualTypeArgs != null) {
			parameterizedType = ParameterizedTypeImpl.make(resultObjectRawType, actualTypeArgs, null);
		}
		return parse(jsonStr, resultObjectRawType, parameterizedType);
	}

	// /**
	// * parseFromJson
	// *
	// * @param jsonStr
	// * @param resultObjectClazz
	// * @return
	// */
	// public static <T> ServiceResponse<T> parseFromJson(String jsonStr, Class<T> resultObjectRawType) {
	// return parse(jsonStr, resultObjectRawType, null);
	// }

	/**
	 * parse
	 * 
	 * @param jsonStr
	 * @param resultObjectClazz
	 * @param resultObjectGenericType
	 * @return
	 */
	private static <T> ServiceResponse<T> parse(String jsonStr, Class<T> resultObjectClazz, Type resultObjectGenericType) {
		ServiceResponse<T> serviceResponse = new ServiceResponse<T>();
		try {
			JsonParserImpl parser = new JsonParserImpl();
			JSONObject joServiceResponse = new JSONObject(jsonStr);
			int resultCode = joServiceResponse.getInt(RESPONSE_FIELD_RESULT_CODE);
			serviceResponse.setResultCode(resultCode);
			if (joServiceResponse.has(RESPONSE_FIELD_EXCEPTIONS)) {
				JSONArray jaExceptions = joServiceResponse.getJSONArray(RESPONSE_FIELD_EXCEPTIONS);
				List<Message> exceptions = new ArrayList<>();
				for (int i = 0; i < jaExceptions.size(); i++) {
					Message exception = parser.parse(jaExceptions.getJSONObject(i).toString(), Message.class, null, null);
					exceptions.add(exception);
				}
				serviceResponse.setExceptions(exceptions);
			}
			if (joServiceResponse.has(RESPONSE_FIELD_MESSAGES)) {
				JSONArray jaMessages = joServiceResponse.getJSONArray(RESPONSE_FIELD_MESSAGES);
				List<String> messages = new ArrayList<>();
				for (int i = 0; i < jaMessages.size(); i++) {
					String message = parser.parse(jaMessages.getJSONObject(i).toString(), String.class, null, null);
					messages.add(message);
				}
				serviceResponse.setMessages(messages);
			}
			if (joServiceResponse.has(RESPONSE_FIELD_RESULT_OBJECT)) {
				Object obj = joServiceResponse.get(RESPONSE_FIELD_RESULT_OBJECT);
				T resultObject = parser.parse(obj.toString(), resultObjectClazz, resultObjectGenericType, null);
				serviceResponse.setResultObject(resultObject);
			}
		} catch (JSONException e) {
			throw new ParserException("JSON parsing error:" + jsonStr + "," + resultObjectClazz, e);
		}
		return serviceResponse;
	}

	// /**
	// * Get result code of ServiceResponse
	// *
	// * @param jsonResponseText
	// * @return
	// */
	// public static int getResultCodeFromJson(String jsonResponseText) {
	// try {
	// JSONObject joResponse = new JSONObject(jsonResponseText);
	//
	// int resultCode = joResponse.getInt(RESPONSE_FIELD_RESULT_CODE);
	// return resultCode;
	// } catch (JSONException e) {
	// throw new ParserException("JSON parsing error:" + jsonResponseText, e);
	// }
	// }
	//
	// /**
	// * Get result object of ServiceResponse
	// *
	// * @param jsonResponseText
	// * @param resultObjectClazz
	// * @return
	// */
	// public static <T> T getResultObjectFromJson(String jsonResponseText, Class<T> resultObjectClazz) {
	// T resultObject = null;
	// try {
	// JSONObject joResponse = new JSONObject(jsonResponseText);
	//
	// int resultCode = joResponse.getInt(RESPONSE_FIELD_RESULT_CODE);
	// if (ServiceResponse.SUCCESS == resultCode) {
	// JSONObject joResultObject = joResponse.getJSONObject(RESPONSE_FIELD_RESULT_OBJECT);
	//
	// JsonParserImpl jsonParser = new JsonParserImpl();
	// resultObject = jsonParser.parse(joResultObject.toString(), resultObjectClazz, null, null);
	// }
	// } catch (JSONException e) {
	// throw new ParserException("JSON parsing error:" + jsonResponseText + "," + resultObjectClazz, e);
	// }
	// return resultObject;
	// }

}
