package de.hybris.platform.ruleengine.infrastructure;

import com.google.common.collect.Maps;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

public class PostRuleEngineInitializeBeanPostProcessor implements BeanFactoryAware, BeanPostProcessor, Ordered
{
    private BeanFactory beanFactory;
    private final Map<String, Class> ruleGlobalsAwareBeans = Maps.newHashMap();
    private final Map<String, Method> ruleGlobalsRetrievalMethods = Maps.newHashMap();


    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.beanFactory = beanFactory;
    }


    public Object postProcessBeforeInitialization(Object bean, String beanName)
    {
        Class<?> beanClass = bean.getClass();
        Method[] methods = beanClass.getDeclaredMethods();
        for(Method method : methods)
        {
            if(method.isAnnotationPresent((Class)GetRuleEngineGlobalByName.class))
            {
                this.ruleGlobalsAwareBeans.put(beanName, beanClass);
                this.ruleGlobalsRetrievalMethods.put(beanName, method);
                break;
            }
        }
        return bean;
    }


    public Object postProcessAfterInitialization(Object bean, String beanName)
    {
        Class origBeanClass = this.ruleGlobalsAwareBeans.get(beanName);
        if(Objects.nonNull(origBeanClass))
        {
            Method ruleGlobalsRetrievalMethod = this.ruleGlobalsRetrievalMethods.get(beanName);
            return Proxy.newProxyInstance(getClass().getClassLoader(), ClassUtils.getAllInterfacesForClass(origBeanClass), (proxy, method1, args) -> getRuleGlobalBean(bean, method1, ruleGlobalsRetrievalMethod.getName(), args));
        }
        return bean;
    }


    private Object getRuleGlobalBean(Object bean, Method proxyMethod, String ruleGlobalsGetMethodName, Object... args)
    {
        if(proxyMethod.getName().equals(ruleGlobalsGetMethodName) && hasOneStringArg(args))
        {
            return this.beanFactory.getBean((String)args[0]);
        }
        return ReflectionUtils.invokeMethod(proxyMethod, bean, args);
    }


    protected boolean hasOneStringArg(Object... args)
    {
        return (ArrayUtils.isNotEmpty(args) && args.length == 1 && args[0] instanceof String);
    }


    public int getOrder()
    {
        return Integer.MAX_VALUE;
    }
}
