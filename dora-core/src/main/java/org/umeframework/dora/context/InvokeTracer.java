package org.umeframework.dora.context;

import java.lang.reflect.Method;

import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * InvokeTracer
 * 
 * @author MA YUE
 */
public class InvokeTracer {
    /**
     * method
     */
    private Signature signature;
    /**
     * args
     */
    private Object[] args;

    /**
     * InvokeTracer
     * 
     * @param method
     * @param args
     */
    public InvokeTracer(Signature signature, Object[] args) {
        this.signature = signature;
        this.args = args;
    }

    /**
     * @return the args
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * @return the signature
     */
    public Signature getSignature() {
        return signature;
    }

    /**
     * getTargetMethod
     * 
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public Method getTargetMethod() throws NoSuchMethodException, SecurityException {
        if (this.signature instanceof MethodSignature) {
            MethodSignature msignature = (MethodSignature) signature;
            Class<?> targetClass = signature.getDeclaringType();
            return targetClass.getMethod(msignature.getName(), msignature.getParameterTypes());
        }
        return null;
    }
}
