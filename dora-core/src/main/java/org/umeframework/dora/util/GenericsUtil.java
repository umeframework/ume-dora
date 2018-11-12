/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.util;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

/**
 * GenericsUtil
 * 
 * @author Yue MA
 * 
 */
abstract public class GenericsUtil {
    /**
     * resolveParameterizedClass
     * 
     * @param genericClass
     * @param descendantClass
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    public static <T> Class<?>[] resolveParameterizedClass(
            Class<T> genericClass,
            Class<? extends T> descendantClass) throws IllegalArgumentException, IllegalStateException {
        if (genericClass == null) {
            throw new IllegalArgumentException("Argument 'genericsClass' (" + Class.class.getName() + ") is null");
        }
        if (descendantClass == null) {
            throw new IllegalArgumentException("Argument 'descendantClass'(" + Class.class.getName() + ") is null");
        }

        List<ParameterizedType> ancestorTypeList = getAncestorTypeList(genericClass, descendantClass);

        ParameterizedType parameterizedType = ancestorTypeList.get(ancestorTypeList.size() - 1);
        Type[] actualTypes = parameterizedType.getActualTypeArguments();
        Class<?>[] actualClasses = new Class[actualTypes.length];
        for (int i = 0; i < actualTypes.length; i++) {
            actualClasses[i] = resolveTypeVariable(actualTypes[i], ancestorTypeList);
        }
        return actualClasses;
    }

    /**
     * resolveParameterizedClass
     * 
     * @param genericClass
     * @param descendantClass
     * @param index
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    public static <T> Class<?> resolveParameterizedClass(
            Class<T> genericClass,
            Class<? extends T> descendantClass,
            int index) throws IllegalArgumentException, IllegalStateException {
        if (genericClass == null) {
            throw new IllegalArgumentException("Argument 'genericsClass' (" + Class.class.getName() + ") is null");
        }
        if (descendantClass == null) {
            throw new IllegalArgumentException("Argument 'descendantClass'(" + Class.class.getName() + ") is null");
        }

        List<ParameterizedType> ancestorTypeList = getAncestorTypeList(genericClass, descendantClass);

        ParameterizedType parameterizedType = ancestorTypeList.get(ancestorTypeList.size() - 1);
        Type[] actualTypes = parameterizedType.getActualTypeArguments();

        if (index < 0 || index >= actualTypes.length) {
            throw new IllegalArgumentException("Argument 'index'(" + Integer.toString(index) + ") is out of bounds of" + " generics parameters");
        }
        return resolveTypeVariable(actualTypes[index], ancestorTypeList);
    }

    /**
     * getAncestorTypeList
     * 
     * @param genericClass
     * @param descendantClass
     * @return
     * @throws IllegalStateException
     */
    protected static <T> List<ParameterizedType> getAncestorTypeList(
            Class<T> genericClass,
            Class<? extends T> descendantClass) throws IllegalStateException {
        List<ParameterizedType> ancestorTypeList = new ArrayList<ParameterizedType>();
        Class<?> clazz = descendantClass;
        boolean isInterface = genericClass.isInterface();

        while (clazz != null) {
            Type type = clazz.getGenericSuperclass();
            if (checkParameterizedType(type, genericClass, ancestorTypeList)) {
                break;
            }

            if (!isInterface) {
                clazz = clazz.getSuperclass();
                continue;
            }
            if (checkInterfaceAncestors(genericClass, ancestorTypeList, clazz)) {
                break;
            }

            clazz = clazz.getSuperclass();
        }

        if (ancestorTypeList.isEmpty()) {
            throw new IllegalStateException("Argument 'genericClass'(" + genericClass.getName() + ") does not declare type parameter");
        }

        ParameterizedType targetType = ancestorTypeList.get(ancestorTypeList.size() - 1);
        if (!targetType.getRawType().equals(genericClass)) {
            throw new IllegalStateException("Class(" + descendantClass.getName() + ") is not concrete class of Class(" + genericClass.getName() + ")");
        }
        return ancestorTypeList;
    }

    /**
     * checkInterfaceAncestors
     * 
     * @param genericClass
     * @param ancestorTypeList
     * @param clazz
     * @return
     */
    protected static <T> boolean checkInterfaceAncestors(
            Class<T> genericClass,
            List<ParameterizedType> ancestorTypeList,
            Class<?> clazz) {
        boolean genericTypeFound = false;
        Type[] interfaceTypes = clazz.getGenericInterfaces();
        for (Type interfaceType : interfaceTypes) {
            genericTypeFound = checkParameterizedType(interfaceType, genericClass, ancestorTypeList);
            if (genericTypeFound) {
                return true;
            }
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> interfaceClass : interfaces) {
                if (checkInterfaceAncestors(genericClass, ancestorTypeList, interfaceClass)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * checkParameterizedType
     * 
     * @param type
     * @param genericClass
     * @param ancestorTypeList
     * @return
     */
    protected static <T> boolean checkParameterizedType(
            Type type,
            Class<T> genericClass,
            List<ParameterizedType> ancestorTypeList) {
        if (!(type instanceof ParameterizedType)) {
            return false;
        }

        ParameterizedType parameterlizedType = (ParameterizedType) type;

        if (!genericClass.isAssignableFrom((Class<?>) parameterlizedType.getRawType())) {
            return false;
        }
        ancestorTypeList.add(parameterlizedType);

        if (parameterlizedType.getRawType().equals(genericClass)) {
            return true;
        }
        return false;
    }

    /**
     * resolveTypeVariable
     * 
     * @param type
     * @param ancestorTypeList
     * @return
     * @throws IllegalStateException
     */
    protected static Class<?> resolveTypeVariable(
            Type type,
            List<ParameterizedType> ancestorTypeList) throws IllegalStateException {

        if (isNotTypeVariable(type)) {
            return getRawClass(type);
        }

        TypeVariable<?> targetType = (TypeVariable<?>) type;
        Type actualType = null;
        for (int i = ancestorTypeList.size() - 1; i >= 0; i--) {
            ParameterizedType ancestorType = ancestorTypeList.get(i);
            GenericDeclaration declaration = targetType.getGenericDeclaration();
            if (!(declaration instanceof Class)) {
                throw new IllegalStateException("TypeVariable(" + targetType.getName() + " is not declared at Class " + "(ie. is declared at Method or Constructor)");
            }
            Class<?> declaredClass = (Class<?>) declaration;
            if (declaredClass != ancestorType.getRawType()) {
                continue;
            }
            Type[] parameterTypes = declaredClass.getTypeParameters();
            int index = ArrayUtils.indexOf(parameterTypes, targetType);
            if (index == -1) {
                throw new IllegalStateException("Class(" + declaredClass.getName() + ") does not declare TypeValidable(" + targetType.getName() + ")");
            }
            actualType = ancestorType.getActualTypeArguments()[index];

            if (isNotTypeVariable(actualType)) {
                return getRawClass(actualType);
            }
            targetType = (TypeVariable<?>) actualType;
        }

        throw new IllegalStateException("Concrete type of Type(" + type + ") was not found in ancestorList(" + ancestorTypeList + ")");
    }

    /**
     * isNotTypeVariable
     * 
     * @param type
     * @return
     * @throws IllegalStateException
     */
    protected static boolean isNotTypeVariable(
            Type type) throws IllegalStateException {
        if (type instanceof Class) {
            return true;
        } else if (type instanceof TypeVariable) {
            return false;
        } else if (type instanceof ParameterizedType) {
            return true;
        } else if (type instanceof GenericArrayType) {
            return true;
        }
        throw new IllegalStateException("Type(" + type + ") is not instance of " + TypeVariable.class.getName() + ", " + ParameterizedType.class.getName() + ", " + GenericArrayType.class.getName() + " nor " + Class.class.getName());
    }

    /**
     * getRawClass
     * 
     * @param type
     * @return
     * @throws IllegalStateException
     */
    protected static Class<?> getRawClass(
            Type type) throws IllegalStateException {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return getRawClass(((ParameterizedType) type).getRawType());
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            Class<?> componentClass = getRawClass(componentType);
            return Array.newInstance(componentClass, 0).getClass();
        }
        throw new IllegalStateException("Type(" + type + ") is not instance of " + ParameterizedType.class.getName() + ", " + GenericArrayType.class.getName() + " nor " + Class.class.getName());
    }

}
