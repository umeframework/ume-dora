/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

/**
 * A Tool which generate mock bean instance with sample data.<br>
 * Sample:<br>
 * 1) create instance by class full name:<br>
 * BeanMocker generate = new BeanMocker();<br>
 * generate.createMockObject("com.abc.SomeBean");<br>
 * <br>
 * 2) create instance by class type:<br>
 * BeanMocker generate = new BeanMocker();<br>
 * generate.createMockObject(SomeBean.class);<br>
 */
public class BeanMocker {

	/**
	 * createMockObject
	 * 
	 * @param clazzName
	 * @return
	 * @throws Exception
	 */
	public Object createMockObject(String clazzName) throws Exception {

		Object obj = null;
		clazzName = filtGenericType(clazzName);

		if (isIgnoreType(clazzName)) {
			obj = null;
		} else if (isSimpleType(clazzName)) {
			obj = createSimpleObject(clazzName);
		} else if (isArrayType(clazzName)) {
			String elementType = getArrayElementType(clazzName);
			Class<?> clazz = loadClass(elementType);
			obj = Array.newInstance(clazz, 2);
			Object value01 = createMockObject(clazz);
			Array.set(obj, 0, value01);
			Object value02 = createMockObject(clazz);
			Array.set(obj, 1, value02);
		} else if (isCollectionType(clazzName)) {
			obj = createCollection(clazzName);
		} else if (isMapType(clazzName)) {
			obj = createMap(clazzName);
		} else {
			try {
				String className = filtGenericType(clazzName);
				Class<?> clazz = loadClass(className);
				obj = createMockObject(clazz);
			} catch (Exception e) {
				System.out.println("Warning: Can not create objct by type " + clazzName);
				e.printStackTrace();
				obj = null;
			}
		}
		return obj;
	}

	/**
	 * createMockObject
	 * 
	 * @param clazz
	 */
	public Object createMockObject(Class<?> clazz) throws Exception {
		if (clazz == null) {
			return null;
		} else if (isSimpleClass(clazz)) {
			return createSimpleObject(clazz.getName());
		}
		Object instance = newClassInstance(clazz);

		if (instance == null) {
			return null;
		}

		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(clazz);
		} catch (IntrospectionException e) {
			throw e;
		}

		for (PropertyDescriptor prop : beanInfo.getPropertyDescriptors()) {
			if (prop.getWriteMethod() == null) {
				continue;
			}
			Method setterMethod = prop.getWriteMethod();
			Class<?> targetFieldClass = setterMethod.getParameterTypes()[0];
			if (isSimpleClass(targetFieldClass)) {
				Object value = createSimpleObject(targetFieldClass.getName());
				setterMethod.invoke(instance, new Object[] { value });
			} else if (isArrayClass(targetFieldClass)) {
				if (!targetFieldClass.getComponentType().equals(clazz)) {
					Object objArray = Array.newInstance(targetFieldClass.getComponentType(), 2);
					Object value01 = createMockObject(targetFieldClass.getComponentType());
					Array.set(objArray, 0, value01);
					Object value02 = createMockObject(targetFieldClass.getComponentType());
					Array.set(objArray, 1, value02);
					setterMethod.invoke(instance, new Object[] { targetFieldClass.cast(objArray) });
				}
			} else if (isCollectionClass(targetFieldClass)) {
				String genericType = setterMethod.getGenericParameterTypes()[0].toString();
				if (!genericType.contains(clazz.getName())) {
					Collection<?> collectionObj = createCollection(genericType);
					setterMethod.invoke(instance, new Object[] { (Object) collectionObj });
				}
			} else if (isMapClass(targetFieldClass)) {
				String genericType = setterMethod.getGenericParameterTypes()[0].toString();
				if (!genericType.contains(clazz.getName())) {
					Map<?, ?> mapObj = createMap(genericType);
					setterMethod.invoke(instance, new Object[] { (Object) mapObj });
				}
			} else if (!targetFieldClass.equals(clazz)) {
				Object value = null;
				try {
					value = createMockObject(targetFieldClass);
				} catch (Exception ex) {
					continue;
				}
				setterMethod.invoke(instance, new Object[] { value });
			}
		}
		return instance;
	}

	/**
	 * create collection type object
	 * 
	 * @param typeName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Collection<?> createCollection(final String typeName) throws Exception {
		Object elementObj = null;

		if (isGenericType(typeName)) {
			String interGenericType = getGenericNestType(typeName);
			elementObj = createMockObject(interGenericType);
		} else {
			elementObj = new Object();
		}
		Collection collObj = null;
		String declareType = getGenericDeclareType(typeName);
		if (isLinkedListType(declareType)) {
			collObj = new LinkedList();
		} else if (isArrayListType(declareType)) {
			collObj = new ArrayList();
		} else if (isListType(declareType)) {
			collObj = new ArrayList();
		} else if (isLinkedHashSetType(declareType)) {
			collObj = new LinkedHashSet();
		} else if (isHashSetType(declareType)) {
			collObj = new HashSet();
		} else if (isSetType(declareType)) {
			collObj = new HashSet();
		} else if (isTreeSetType(declareType)) {
			collObj = new TreeSet();
		} else if (isVectorType(declareType)) {
			collObj = new Vector();
		} else {
			collObj = new ArrayList();
		}
		collObj.add(elementObj);
		return collObj;
	}

	/**
	 * create map type object
	 * 
	 * @param typeName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Map createMap(final String typeName) throws Exception {
		Object keyObj = null;
		Object valueObj = null;
		if (isGenericType(typeName)) {
			String interGenericType = getGenericNestType(typeName);
			int sepFlag = interGenericType.indexOf(",");
			String interKeyType = interGenericType.substring(0, sepFlag).trim();
			String interValueType = interGenericType.substring(sepFlag + 1).trim();
			keyObj = createMockObject(interKeyType);
			valueObj = createMockObject(interValueType);
		} else {
			keyObj = new Object();
			valueObj = new Object();
		}

		Map mapObj = null;
		String declareType = getGenericDeclareType(typeName);
		if (isHashtableType(declareType)) {
			mapObj = new Hashtable();
		} else if (isLinkedHashMapType(declareType)) {
			mapObj = new LinkedHashMap();
		} else if (isTreeMapType(declareType)) {
			mapObj = new TreeMap();
		} else if (isHashMapType(declareType)) {
			mapObj = new HashMap();
		} else if (isMapType(declareType)) {
			mapObj = new HashMap();
		} else {
			mapObj = new HashMap();
			mapObj.put(new Object(), new Object());
			return mapObj;
		}

		mapObj.put(keyObj, valueObj);

		return mapObj;
	}

	/**
	 * create simple object
	 * 
	 * @param className
	 * @return
	 */
	protected Object createSimpleObject(String className) {
		Object value = null;
		className = filtGenericType(className);

		if (className != null && isSimpleType(className)) {
			if (className.equals(java.lang.String.class.getName())) {
				value = String.valueOf("ABC");
			} else if (className.equals(java.lang.Integer.class.getName()) || className.equals(int.class.getName())) {
				value = 1;
			} else if (className.equals(java.lang.Short.class.getName()) || className.equals(short.class.getName())) {
				value = 1;
			} else if (className.equals(java.lang.Long.class.getName()) || className.equals(long.class.getName())) {
				value = 1L;
			} else if (className.equals(java.lang.Float.class.getName()) || className.equals(float.class.getName())) {
				value = 0.1F;
			} else if (className.equals(java.lang.Double.class.getName()) || className.equals(double.class.getName())) {
				value = 0.1D;
			} else if (className.equals(java.lang.Boolean.class.getName()) || className.equals(boolean.class.getName())) {
				value = Boolean.valueOf(false);
			} else if (className.equals(java.lang.Character.class.getName()) || className.equals(char.class.getName())) {
				value = Character.valueOf('a');
			} else if (className.equals(java.lang.Byte.class.getName()) || className.equals(byte.class.getName())) {
				value = Byte.MIN_VALUE;
			} else if (className.equals(java.lang.StringBuffer.class.getName())) {
				value = new StringBuffer("StrBuffer");
			} else if (className.equals(java.lang.StringBuilder.class.getName())) {
				value = new StringBuffer("StrBuilder");
			} else if (className.equals(java.sql.Date.class.getName())) {
				value = java.sql.Date.valueOf("1999-01-02");
			} else if (className.equals(java.sql.Time.class.getName())) {
				value = java.sql.Time.valueOf("11:22:33");
			} else if (className.equals(java.sql.Timestamp.class.getName())) {
				value = java.sql.Timestamp.valueOf("1999-01-02 11:22:33");
			} else if (className.equals(java.math.BigInteger.class.getName())) {
				value = java.math.BigInteger.valueOf(1L);
			} else if (className.equals(java.math.BigDecimal.class.getName())) {
				value = java.math.BigDecimal.valueOf(0.1D);
			} else if (className.equals(java.util.Date.class.getName())) {
				try {
					value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse("1999-01-02 11:22:33.999");
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if (className.equals(java.lang.Object.class.getName())) {
				value = new Object();
			} else if (className.equals(java.lang.Number.class.getName())) {
				value = null;
			} else if (className.equals(java.lang.Class.class.getName())) {
				value = null;
			} else if (className.equals(java.lang.Enum.class.getName())) {
				value = null;
			} else {
				value = null;
			}
		}
		return value;
	}

	/**
	 * load class by name
	 * 
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
	protected Class<?> loadClass(String className) throws ClassNotFoundException {
		if (isSimplePrimitiveType(className)) {
			return loadClassForPrimitiveType(className);
		} else {
			return Class.forName(className);
		}
	}

	/**
	 * create class instance
	 * 
	 * @param clazz
	 * @return
	 */
	protected Object newClassInstance(Class<?> clazz) {
		Object instance = null;
		try {
			if (clazz.isInterface()) {
				if (java.util.List.class.equals(clazz)) {
					return new ArrayList<>();
				} else if (java.util.Map.class.equals(clazz)) {
					return new HashMap<>();
				} else if (java.util.Set.class.equals(clazz)) {
					return new HashSet<>();
				}
			} else if (clazz.isArray()) {
				return null;
			} else {
				instance = clazz.newInstance();
			}
		} catch (Exception e) {
			System.out.println("Error: Fail to create mock instance for type <" + clazz + ">.");
		}
		return instance;
	}

	/**
	 * get getter method name list of class<br>
	 * 
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	protected String[] getBeanGetterNames(Class<?> clazz) throws Exception {
		List<String> nameList = new ArrayList<String>();
		for (Method method : clazz.getMethods()) {
			if (method.getName().startsWith("get") && !method.getName().equals("getClass") && method.getParameterTypes().length == 0) {
				nameList.add(method.getName());
			}
		}
		String[] nameArray = new String[nameList.size()];
		return nameList.toArray(nameArray);
	}

	/**
	 * get bean field name list by getter list<br>
	 * 
	 * @param getters
	 * @return
	 * @throws Exception
	 */
	protected String[] getBeanFieldNames(String[] getters) throws Exception {
		String[] fieldNames = new String[getters.length];
		for (int i = 0; i < getters.length; i++) {
			String name = getters[i].replace("get", "");
			fieldNames[i] = name.substring(0, 1).toLowerCase() + name.substring(1);
		}
		return fieldNames;
	}

	/**
	 * get setter method name list of class<br>
	 * 
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	protected String[] getBeanSetterNames(Class<?> clazz) throws Exception {
		List<String> nameList = new ArrayList<String>();
		for (Method method : clazz.getMethods()) {
			if (method.getName().startsWith("set") && !method.getName().equals("setClass")) {
				nameList.add(method.getName());
			}
		}
		String[] nameArray = new String[nameList.size()];
		return nameList.toArray(nameArray);
	}

	/**
	 * Get bean field's name and data type as Map<br>
	 * 
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	protected Map<String, Class<?>> getBeanFieldTypes(Class<?> clazz) throws Exception {
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		for (Field field : clazz.getDeclaredFields()) {
			if (field != null && !field.getName().startsWith("$")) {
				map.put(field.getName(), field.getType());
			}
		}
		return map;
	}

	/**
	 * Get bean property values by getter name list<br>
	 * 
	 * @param obj
	 * @param getters
	 * @return
	 * @throws Exception
	 */
	protected Object[] getBeanProperties(Object obj, String[] getters) throws Exception {
		Object[] values = new Object[getters.length];
		for (int i = 0; i < values.length; i++) {
			Method method = obj.getClass().getMethod(getters[i]);
			values[i] = method.invoke(obj);
		}
		return values;
	}

	/**
	 * Check if it is the simple data instance<br>
	 * 
	 * @param obj
	 * @return
	 */
	protected boolean isSimpleObject(Object obj) {
		if (obj == null) {
			return true;
		}
		return isSimpleClass(obj.getClass());
	}

	/**
	 * Check if it is the simple data type class<br>
	 * 
	 * @param clazz
	 * @return
	 */
	protected boolean isSimpleClass(Class<?> clazz) {
		if (clazz == null || clazz.isPrimitive() || SIMPLE_TYPE.contains(clazz.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it is the array instance<br>
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	protected boolean isArrayObject(Object obj) throws Exception {
		if (obj == null) {
			return false;
		}
		return obj.getClass().isArray();
	}

	/**
	 * Check if it is the array class<br>
	 * 
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	protected boolean isArrayClass(Class<?> clazz) throws Exception {
		if (clazz == null) {
			return false;
		}
		return clazz.isArray();
	}

	/**
	 * Get elements from array instance<br>
	 * 
	 * @param obj
	 * @return
	 */
	protected Object[] getArrayElements(Object obj) {
		Object[] arr = null;
		if (obj instanceof char[]) {
			char[] t = (char[]) obj;
			arr = new Object[t.length];
			for (int i = 0; i < t.length; i++) {
				arr[i] = t[i];
			}
		} else if (obj instanceof int[]) {
			int[] t = (int[]) obj;
			arr = new Object[t.length];
			for (int i = 0; i < t.length; i++) {
				arr[i] = t[i];
			}
		} else if (obj instanceof long[]) {
			long[] t = (long[]) obj;
			arr = new Object[t.length];
			for (int i = 0; i < t.length; i++) {
				arr[i] = t[i];
			}
		} else if (obj instanceof short[]) {
			short[] t = (short[]) obj;
			arr = new Object[t.length];
			for (int i = 0; i < t.length; i++) {
				arr[i] = t[i];
			}
		} else if (obj instanceof float[]) {
			float[] t = (float[]) obj;
			arr = new Object[t.length];
			for (int i = 0; i < t.length; i++) {
				arr[i] = t[i];
			}
		} else if (obj instanceof double[]) {
			double[] t = (double[]) obj;
			arr = new Object[t.length];
			for (int i = 0; i < t.length; i++) {
				arr[i] = t[i];
			}
		} else if (obj instanceof byte[]) {
			byte[] t = (byte[]) obj;
			arr = new Object[t.length];
			for (int i = 0; i < t.length; i++) {
				arr[i] = t[i];
			}
		} else if (obj instanceof boolean[]) {
			boolean[] t = (boolean[]) obj;
			arr = new Object[t.length];
			for (int i = 0; i < t.length; i++) {
				arr[i] = t[i];
			}
		} else {
			arr = (Object[]) obj;
		}
		return arr;
	}

	/**
	 * Check if it is the Map class<br>
	 * 
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	protected boolean isMapClass(Class<?> clazz) throws Exception {
		if (clazz == null) {
			return false;
		}
		if (clazz.equals(Map.class) || clazz.equals(HashMap.class) || clazz.equals(Hashtable.class)) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it is the map instance<br>
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	protected boolean isMapObject(Object obj) throws Exception {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Map || obj instanceof HashMap || obj instanceof Hashtable) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it is the collection class<br>
	 * 
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	protected boolean isCollectionClass(Class<?> clazz) throws Exception {
		if (clazz == null) {
			return false;
		}
		if (clazz.equals(Collection.class) || clazz.equals(List.class) || clazz.equals(ArrayList.class) || clazz.equals(LinkedList.class)) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it is the collection instance<br>
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	protected boolean isCollectionObject(Object obj) throws Exception {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Collection || obj instanceof List || obj instanceof ArrayList || obj instanceof LinkedList) {
			return true;
		}
		return false;
	}

	/**
	 * Get elements from Collection<br>
	 * 
	 * @param obj
	 * @return
	 */
	protected Object[] getCollectionElements(Collection<?> obj) {
		if (obj == null) {
			return null;
		}
		return obj.toArray();
	}

	/**
	 * Get key elements of Map<br>
	 * 
	 * @param obj
	 * @return
	 */
	protected Object[] getMapKeyElements(Map<?, ?> obj) {
		return obj.keySet().toArray();
	}

	/**
	 * Get values of Map<br>
	 * 
	 * @param obj
	 * @param keys
	 * @return
	 */
	protected Object[] getMapValueElements(Map<?, ?> obj, Object[] keys) {
		Object[] values = new Object[keys.length];
		int i = 0;
		for (Object key : keys) {
			values[i++] = obj.get(key);
		}
		return values;
	}

	/**
	 * Check if it is the simple data type by input type name<br>
	 * 
	 * @param typeName
	 * @return
	 */
	protected boolean isSimpleType(String typeName) {
		if (typeName == null) {
			return true;
		}
		typeName = filtGenericType(typeName);
		if (SIMPLE_TYPE.contains(typeName)) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it is the primitive data type by input type name<br>
	 * 
	 * @param typeName
	 * @return
	 */
	protected boolean isSimplePrimitiveType(String typeName) {
		if (typeName == null || SIMPLE_PRIMITIVE_TYPE.contains(typeName)) {
			return true;
		}
		return false;
	}

	/**
	 * Convert java primitive type to java object type<br>
	 * 
	 * @param typeName
	 * @return
	 */
	protected Class<?> loadClassForPrimitiveType(String typeName) {
		if (typeName.equals(int.class.getSimpleName())) {
			return int.class;
		} else if (typeName.equals(short.class.getSimpleName())) {
			return short.class;
		} else if (typeName.equals(long.class.getSimpleName())) {
			return long.class;
		} else if (typeName.equals(float.class.getSimpleName())) {
			return float.class;
		} else if (typeName.equals(double.class.getSimpleName())) {
			return double.class;
		} else if (typeName.equals(boolean.class.getSimpleName())) {
			return boolean.class;
		} else if (typeName.equals(char.class.getSimpleName())) {
			return char.class;
		} else if (typeName.equals(byte.class.getSimpleName())) {
			return byte.class;
		} else {
			return null;
		}
	}

	/**
	 * Check if it is the ignore data type<br>
	 * 
	 * @param typeName
	 * @return
	 */
	protected boolean isIgnoreType(String typeName) {
		typeName = filtGenericType(typeName);
		if (IGNORE_TYPE.contains(typeName.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * Filter the generic type's description<br>
	 * 
	 * @param typeName
	 * @return
	 */
	protected String filtGenericType(String typeName) {
		String outStr = typeName;

		if (typeName.startsWith("class ")) {
			outStr = typeName.replaceFirst("class ", "").trim();
		} else if (typeName.startsWith("interface ")) {
			outStr = typeName.replaceFirst("interface ", "").trim();
		}

		// Check array type
		if (outStr.startsWith("[L")) {
			outStr = outStr.substring(2);
			outStr = outStr.endsWith(";") ? outStr.substring(0, outStr.length() - 1) + "[]" : outStr + "[]";
		} else if (outStr.startsWith("[")) {
			if ("[C".equals(outStr)) {
				outStr = "char[]";
			} else if ("[I".equals(outStr)) {
				outStr = "int[]";
			} else if ("[F".equals(outStr)) {
				outStr = "float[]";
			} else if ("[J".equals(outStr)) {
				outStr = "long[]";
			} else if ("[B".equals(outStr)) {
				outStr = "byte[]";
			} else if ("[D".equals(outStr)) {
				outStr = "double[]";
			}
		}

		return outStr;
	}

	/**
	 * Check if it is the Map type<br>
	 * 
	 * @param typeName
	 * @return
	 * @throws Exception
	 */
	protected boolean isMapType(String typeName) throws Exception {
		typeName = getGenericDeclareType(typeName);
		if (MAP_TYPE.contains(typeName)) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it is the HashMap type<br>
	 * 
	 * @param typeName
	 * @return
	 * @throws Exception
	 */
	protected boolean isHashMapType(String typeName) throws Exception {
		if (typeName.startsWith(HashMap.class.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it is the LinkedHashMap type<br>
	 * 
	 * @param typeName
	 * @return
	 * @throws Exception
	 */
	protected boolean isLinkedHashMapType(String typeName) throws Exception {
		if (typeName.startsWith(LinkedHashMap.class.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it is the TreeMap type<br>
	 * 
	 * @param typeName
	 * @return
	 * @throws Exception
	 */
	protected boolean isTreeMapType(String typeName) throws Exception {
		if (typeName.startsWith(TreeMap.class.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it is the Hashtable type<br>
	 * 
	 * @param typeName
	 * @return
	 * @throws Exception
	 */
	protected boolean isHashtableType(String typeName) throws Exception {
		if (typeName.startsWith(Hashtable.class.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it is the Collection type<br>
	 * 
	 * @param typeName
	 * @return
	 * @throws Exception
	 */
	protected boolean isCollectionType(String typeName) throws Exception {
		typeName = getGenericDeclareType(typeName);
		if (COLLECTION_TYPE.contains(typeName)) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it is the List type<br>
	 * 
	 * @param typeName
	 * @return
	 * @throws Exception
	 */
	protected boolean isListType(String typeName) throws Exception {
		if (typeName.startsWith(List.class.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it is the ArrayList type<br>
	 * 
	 * @param typeName
	 * @return
	 * @throws Exception
	 */
	protected boolean isArrayListType(String typeName) throws Exception {
		if (typeName.startsWith(ArrayList.class.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it is the LinkedList type<br>
	 * 
	 * @param typeName
	 * @return
	 * @throws Exception
	 */
	protected boolean isLinkedListType(String typeName) throws Exception {
		if (typeName.startsWith(LinkedList.class.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it is the Set type<br>
	 * 
	 * @param typeName
	 * @return
	 * @throws Exception
	 */
	protected boolean isSetType(String typeName) throws Exception {
		if (typeName.startsWith(Set.class.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it is the HashSet type<br>
	 * 
	 * @param typeName
	 * @return
	 * @throws Exception
	 */
	protected boolean isHashSetType(String typeName) throws Exception {
		if (typeName.startsWith(HashSet.class.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it is the TreeSet type<br>
	 * 
	 * @param typeName
	 * @return
	 * @throws Exception
	 */
	protected boolean isTreeSetType(String typeName) throws Exception {
		if (typeName.startsWith(TreeSet.class.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it is the LinkedHashSet type<br>
	 * 
	 * @param typeName
	 * @return
	 * @throws Exception
	 */
	protected boolean isLinkedHashSetType(String typeName) throws Exception {
		if (typeName.startsWith(LinkedHashSet.class.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it is the Vector type<br>
	 * 
	 * @param typeName
	 * @return
	 * @throws Exception
	 */
	protected boolean isVectorType(String typeName) throws Exception {
		if (typeName.startsWith(Vector.class.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * Check if it is the Array type<br>
	 * 
	 * @param typeName
	 * @return
	 * @throws Exception
	 */
	protected boolean isArrayType(String typeName) throws Exception {
		typeName = filtGenericType(typeName);

		if (typeName.endsWith("[]")) {
			return true;
		}
		return false;
	}

	/**
	 * Get generic declare type<br>
	 * 
	 * @param typeName
	 * @return
	 */
	protected String getGenericDeclareType(String typeName) {
		typeName = filtGenericType(typeName);
		/**
		 * if (t.startsWith("class ")) { t = t.replace("class", "").trim(); } else if (t.startsWith("interface ")) { t = t.replace("interface", "").trim(); }
		 */
		typeName = typeName.contains("<") ? typeName.substring(0, typeName.indexOf("<")).trim() : typeName;

		return typeName.trim();
	}

	/**
	 * Get generic type's element type<br>
	 * 
	 * @param typeName
	 * @return
	 */
	protected String getGenericNestType(String typeName) {
		typeName = typeName.substring(typeName.indexOf('<') + 1, typeName.lastIndexOf('>'));
		return typeName.trim();
	}

	/**
	 * Check if it is the Generic type<br>
	 * 
	 * @param typeName
	 * @return
	 */
	protected boolean isGenericType(String typeName) {
		return typeName.contains("<");
	}

	/**
	 * Get Array's element type<br>
	 * 
	 * @param typeName
	 * @return
	 */
	protected String getArrayElementType(String typeName) {
		typeName = typeName.trim();
		return typeName.replace("[]", "").trim();
	}

	/**
	 * Ignore data type definition during jUnit assert.
	 */
	private static final Set<String> IGNORE_TYPE = new HashSet<String>();
	{
		IGNORE_TYPE.add(java.sql.Connection.class.getName());
		IGNORE_TYPE.add(java.sql.ResultSet.class.getName());
		IGNORE_TYPE.add(java.sql.Statement.class.getName());
		IGNORE_TYPE.add(java.sql.PreparedStatement.class.getName());
		IGNORE_TYPE.add(java.sql.ResultSetMetaData.class.getName());
		IGNORE_TYPE.add(java.io.InputStream.class.getName());
		IGNORE_TYPE.add(java.io.File.class.getName());
		IGNORE_TYPE.add(java.awt.image.RenderedImage.class.getName());
		IGNORE_TYPE.add(java.awt.image.BufferedImage.class.getName());
	}

	/**
	 * Base and primitive data type definition.
	 */
	private static final Set<String> SIMPLE_TYPE = new HashSet<String>();
	{
		// Basic data type
		SIMPLE_TYPE.add(Object.class.getName());
		SIMPLE_TYPE.add(Integer.class.getName());
		SIMPLE_TYPE.add(Short.class.getName());
		SIMPLE_TYPE.add(Long.class.getName());
		SIMPLE_TYPE.add(Float.class.getName());
		SIMPLE_TYPE.add(Double.class.getName());
		SIMPLE_TYPE.add(Boolean.class.getName());
		SIMPLE_TYPE.add(Character.class.getName());
		SIMPLE_TYPE.add(Byte.class.getName());
		SIMPLE_TYPE.add(Number.class.getName());
		SIMPLE_TYPE.add(Enum.class.getName());
		SIMPLE_TYPE.add(StringBuffer.class.getName());
		SIMPLE_TYPE.add(String.class.getName());
		SIMPLE_TYPE.add(Class.class.getName());

		// java.sql type
		SIMPLE_TYPE.add(java.sql.Date.class.getName());
		SIMPLE_TYPE.add(java.sql.Time.class.getName());
		SIMPLE_TYPE.add(java.sql.Timestamp.class.getName());
		// java.math type
		SIMPLE_TYPE.add(java.math.BigInteger.class.getName());
		SIMPLE_TYPE.add(java.math.BigDecimal.class.getName());
		// java.utilの日付型
		SIMPLE_TYPE.add(java.util.Date.class.getName());

		// java primitive type
		SIMPLE_TYPE.add(int.class.getName());
		SIMPLE_TYPE.add(short.class.getName());
		SIMPLE_TYPE.add(long.class.getName());
		SIMPLE_TYPE.add(float.class.getName());
		SIMPLE_TYPE.add(double.class.getName());
		SIMPLE_TYPE.add(boolean.class.getName());
		SIMPLE_TYPE.add(char.class.getName());
		SIMPLE_TYPE.add(byte.class.getName());
	}

	/**
	 * Primitive data type definition.
	 */
	private static final Set<String> SIMPLE_PRIMITIVE_TYPE = new HashSet<String>();
	static {
		// java Primitive
		SIMPLE_PRIMITIVE_TYPE.add(int.class.getName());
		SIMPLE_PRIMITIVE_TYPE.add(short.class.getName());
		SIMPLE_PRIMITIVE_TYPE.add(long.class.getName());
		SIMPLE_PRIMITIVE_TYPE.add(float.class.getName());
		SIMPLE_PRIMITIVE_TYPE.add(double.class.getName());
		SIMPLE_PRIMITIVE_TYPE.add(boolean.class.getName());
		SIMPLE_PRIMITIVE_TYPE.add(char.class.getName());
		SIMPLE_PRIMITIVE_TYPE.add(byte.class.getName());
	}

	/**
	 * Collection data type definition.
	 */
	private static final Set<String> COLLECTION_TYPE = new HashSet<String>();
	static {
		COLLECTION_TYPE.add(Collection.class.getName());
		COLLECTION_TYPE.add(List.class.getName());
		COLLECTION_TYPE.add(ArrayList.class.getName());
		COLLECTION_TYPE.add(LinkedList.class.getName());
		COLLECTION_TYPE.add(Set.class.getName());
		COLLECTION_TYPE.add(HashSet.class.getName());
		COLLECTION_TYPE.add(LinkedHashSet.class.getName());
		COLLECTION_TYPE.add(Vector.class.getName());
	}

	/**
	 * Map data type definition.
	 */
	private static final Set<String> MAP_TYPE = new HashSet<String>();
	static {
		MAP_TYPE.add(Map.class.getName());
		MAP_TYPE.add(HashMap.class.getName());
		MAP_TYPE.add(LinkedHashMap.class.getName());
		MAP_TYPE.add(TreeMap.class.getName());
		MAP_TYPE.add(Hashtable.class.getName());
	}

}
