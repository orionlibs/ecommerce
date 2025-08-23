package de.hybris.platform.ruleengine.infrastructure;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

public class PrototypedBeanPostProcessor implements BeanFactoryAware, BeanPostProcessor, Ordered
{
    private BeanFactory beanFactory;
    private final Map<String, Class> prototypedAwareBeans = Maps.newHashMap();
    private final Map<String, Set<Method>> prototypedMethodsMap = Maps.newHashMap();


    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.beanFactory = beanFactory;
    }


    public Object postProcessBeforeInitialization(Object bean, String beanName)
    {
        Class<?> beanClass = bean.getClass();
        Method[] methods = beanClass.getMethods();
        Set<Method> prototypedMethods = Sets.newHashSet();
        for(Method method : methods)
        {
            if(method.isAnnotationPresent((Class)Prototyped.class))
            {
                prototypedMethods.add(method);
            }
        }
        if(CollectionUtils.isNotEmpty(prototypedMethods))
        {
            this.prototypedAwareBeans.putIfAbsent(beanName, beanClass);
            this.prototypedMethodsMap.put(beanName, prototypedMethods);
        }
        return bean;
    }


    public Object postProcessAfterInitialization(Object bean, String beanName)
    {
        Class origBeanClass = this.prototypedAwareBeans.get(beanName);
        if(Objects.nonNull(origBeanClass))
        {
            Set<Method> prototypedMethods = this.prototypedMethodsMap.get(beanName);
            Map<String, String> methodsMap = (Map<String, String>)prototypedMethods.stream().collect(
                            Collectors.toMap(Method::getName, m -> ((Prototyped)m.<Prototyped>getAnnotation(Prototyped.class)).beanName()));
            return Proxy.newProxyInstance(getClass().getClassLoader(), ClassUtils.getAllInterfacesForClass(origBeanClass), (proxy, method1, args) -> getPrototypedBean(bean, method1, methodsMap, args));
        }
        return bean;
    }


    private Object getPrototypedBean(Object bean, Method proxyMethod, Map<String, String> prototypedBeans, Object... args)
    {
        String prototypedBeanName = prototypedBeans.get(proxyMethod.getName());
        if(Objects.nonNull(prototypedBeanName) && ArrayUtils.isEmpty(args))
        {
            return this.beanFactory.getBean(prototypedBeanName);
        }
        return ReflectionUtils.invokeMethod(proxyMethod, bean, args);
    }


    public int getOrder()
    {
        return Integer.MAX_VALUE;
    }
}
