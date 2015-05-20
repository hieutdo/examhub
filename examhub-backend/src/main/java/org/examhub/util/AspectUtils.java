package org.examhub.util;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

/**
 * @author Hieu Do
 */
public final class AspectUtils {
    /**
     * Return the original object from an AOP proxied bean.
     * @param bean The bean to extract
     * @return Original object if the bean is a proxy, otherwise return the bean
     * @throws Exception
     */
    public static final Object unwrapProxy(Object bean) throws Exception {
        if (AopUtils.isAopProxy(bean) && bean instanceof Advised) {
            return ((Advised) bean).getTargetSource().getTarget();
        }
        return bean;
    }
}
