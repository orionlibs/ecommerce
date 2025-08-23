package de.hybris.platform.spring.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ListFactoryBean;
import org.springframework.util.ReflectionUtils;

public class ListMergeDirectiveBeanPostProcessor implements BeanFactoryAware, BeanPostProcessor, InitializingBean
{
    private static final Logger LOG = LoggerFactory.getLogger(ListMergeDirectiveBeanPostProcessor.class.getName());
    private static final String INVALID_LMD_PROPERTY = "invalid ListMergeDirective [{}], depends-on [{}] propertyDescriptor [{}]";
    private static final String INVALID_LMD_PROPERTY_CANT_RESOLVE = "invalid ListMergeDirective [{}], depends-on [{}] propertyDescriptor [{}] did not resolve a property";
    private static final String INVALID_LMD_PROPERTY_NOT_A_LIST = "invalid ListMergeDirective [{}], depends-on [{}] propertyDescriptor [{}] is not of type java.util.List";
    private static final String INVALID_LMD_PROPERTY_NULL = "invalid ListMergeDirective [{}], depends-on [{}] propertyDescriptor [{}] list is null";
    private static final String INVALID_LMD_FIELD = "invalid ListMergeDirective [{}], depends-on [{}] fieldName [{}]";
    private static final String INVALID_LMD_FIELD_NOT_A_LIST = "invalid ListMergeDirective [{}], depends-on [{}] fieldName [{}] is not of type java.util.List";
    private static final String INVALID_LMD_FIELD_NULL = "invalid ListMergeDirective [{}], depends-on [{}] fieldName [{}] list is null";
    private static final String INVALID_LMD_FIELD_CANT_RESOLVE = "invalid ListMergeDirective [{}], depends-on [{}] fieldName [{}] did not resolve a field";
    private ConfigurableListableBeanFactory beanFactory;
    private Map<String, List<String>> mergeDirectiveDependencyMap;
    private Map<String, BeanDefinition> mergeDirectiveMap;
    private ListMergeDirectiveProcessor addProcessor;
    private ListMergeDirectiveProcessor sortProcessor;


    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.beanFactory = (ConfigurableListableBeanFactory)beanFactory;
    }


    public void afterPropertiesSet() throws Exception
    {
        this.addProcessor = (ListMergeDirectiveProcessor)new AddListProcessor();
        this.sortProcessor = (ListMergeDirectiveProcessor)new SortListProcessor(this.beanFactory);
        this.mergeDirectiveDependencyMap = new HashMap<>();
        this.mergeDirectiveMap = new HashMap<>();
        String[] definitionNames = this.beanFactory.getBeanDefinitionNames();
        for(String definitionName : definitionNames)
        {
            BeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition(definitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if(isSupportedClass(beanClassName))
            {
                String[] dependsOn = beanDefinition.getDependsOn();
                if(dependsOn != null)
                {
                    for(String dependencyName : dependsOn)
                    {
                        ((List<String>)this.mergeDirectiveDependencyMap.computeIfAbsent(dependencyName, s -> new ArrayList())).add(definitionName);
                        this.mergeDirectiveMap.put(definitionName, beanDefinition);
                    }
                }
            }
        }
    }


    protected boolean isSupportedClass(String name)
    {
        if(name == null)
        {
            return false;
        }
        try
        {
            Class<?> clazz = Class.forName(name);
            return ListMergeDirective.class.isAssignableFrom(clazz);
        }
        catch(ClassNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("error while checking the class is supported", e);
            }
            return false;
        }
    }


    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
    {
        return bean;
    }


    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        List<String> toMergeList = this.mergeDirectiveDependencyMap.get(beanName);
        if(CollectionUtils.isNotEmpty(toMergeList) && !ListFactoryBean.class.isAssignableFrom(bean
                        .getClass()))
        {
            for(String lmdName : toMergeList)
            {
                LOG.info("Post Processing ListMergeDirective [{}] on Bean [{}]", lmdName, beanName);
                BeanDefinition lmdBeanDefinition = this.mergeDirectiveMap.get(lmdName);
                BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver((ConfigurableBeanFactory)this.beanFactory, lmdName, lmdBeanDefinition, (TypeConverter)new SimpleTypeConverter());
                ListMergeDirective listMergeDirective = buildListMergeDirective(valueResolver, lmdBeanDefinition);
                processMergeDirective(listMergeDirective, lmdName, bean, beanName);
            }
            this.mergeDirectiveDependencyMap.remove(beanName);
        }
        else if(bean instanceof ListMergeDirective && shouldApplyMergeDirectiveBean(beanName))
        {
            ListMergeDirective listMergeDirective = (ListMergeDirective)bean;
            BeanDefinition beanDefinition = this.mergeDirectiveMap.get(beanName);
            String[] dependsOn = beanDefinition.getDependsOn();
            if(dependsOn != null)
            {
                for(String targetName : dependsOn)
                {
                    LOG.info("Post Processing ListMergeDirective [{}] on Bean [{}]", beanName, targetName);
                    Object targetBean = this.beanFactory.getBean(targetName);
                    processMergeDirective(listMergeDirective, beanName, targetBean, targetName);
                }
            }
        }
        return bean;
    }


    protected boolean shouldApplyMergeDirectiveBean(String beanName)
    {
        BeanDefinition beanDefinition = this.mergeDirectiveMap.get(beanName);
        String[] dependsOn = beanDefinition.getDependsOn();
        if(dependsOn != null)
        {
            for(String targetName : dependsOn)
            {
                if(!this.mergeDirectiveDependencyMap.containsKey(targetName))
                {
                    return false;
                }
            }
        }
        return true;
    }


    protected void processMergeDirective(ListMergeDirective listMergeDirective, String lmdBeanName, Object targetBean, String targetName)
    {
        List<Object> list = resolveListBean(listMergeDirective, lmdBeanName, targetBean, targetName);
        if(list != null)
        {
            this.addProcessor.processDirective(listMergeDirective, list, lmdBeanName, targetName);
            this.sortProcessor.processDirective(listMergeDirective, list, lmdBeanName, targetName);
        }
    }


    protected List<Object> resolveListBean(ListMergeDirective lmd, String directiveBeanName, Object bean, String beanName)
    {
        if(lmd.getListPropertyDescriptor() == null && lmd.getFieldName() == null)
        {
            return getListByDependency(bean, beanName, directiveBeanName);
        }
        if(lmd.getListPropertyDescriptor() != null)
        {
            return getListByPropertyDescriptor(bean, beanName, lmd.getListPropertyDescriptor(), directiveBeanName);
        }
        return getListByReflection(bean, beanName, lmd.getFieldName(), directiveBeanName);
    }


    protected List<Object> getListByDependency(Object dependency, String dependencyName, String directiveBeanName)
    {
        if(dependency instanceof List)
        {
            return (List<Object>)dependency;
        }
        LOG.error("invalid list merge directive[{}], depends-on [{}] is not of type java.util.List", directiveBeanName, dependencyName);
        return null;
    }


    protected List<Object> getListByPropertyDescriptor(Object bean, String beanName, String propertyDescriptor, String directiveBeanName)
    {
        try
        {
            Object list = PropertyUtils.getProperty(bean, propertyDescriptor);
            if(list == null)
            {
                LOG.error("invalid ListMergeDirective [{}], depends-on [{}] propertyDescriptor [{}] list is null", new Object[] {directiveBeanName, beanName, propertyDescriptor});
                return null;
            }
            if(!(list instanceof List))
            {
                LOG.error("invalid ListMergeDirective [{}], depends-on [{}] propertyDescriptor [{}] is not of type java.util.List", new Object[] {directiveBeanName, beanName, propertyDescriptor});
                return null;
            }
            return (List<Object>)list;
        }
        catch(NoSuchMethodException | java.lang.reflect.InvocationTargetException | IllegalAccessException e)
        {
            LOG.error("invalid ListMergeDirective [{}], depends-on [{}] propertyDescriptor [{}] did not resolve a property", new Object[] {directiveBeanName, beanName, propertyDescriptor});
            if(LOG.isDebugEnabled())
            {
                LOG.debug("invalid ListMergeDirective [{}], depends-on [{}] propertyDescriptor [{}] did not resolve a property", new Object[] {directiveBeanName, beanName, propertyDescriptor, e});
            }
            return null;
        }
    }


    protected List<Object> getListByReflection(Object bean, String beanName, String fieldName, String directiveBeanName)
    {
        try
        {
            Field field = ReflectionUtils.findField(bean.getClass(), fieldName);
            if(field == null)
            {
                LOG.error("invalid ListMergeDirective [{}], depends-on [{}] fieldName [{}] did not resolve a field", new Object[] {directiveBeanName, beanName, fieldName});
                return null;
            }
            field.setAccessible(true);
            Object list = field.get(bean);
            if(list == null)
            {
                LOG.error("invalid ListMergeDirective [{}], depends-on [{}] fieldName [{}] list is null", new Object[] {directiveBeanName, beanName, fieldName});
                return null;
            }
            if(!(list instanceof List))
            {
                LOG.error("invalid ListMergeDirective [{}], depends-on [{}] fieldName [{}] is not of type java.util.List", new Object[] {directiveBeanName, beanName, fieldName});
                return null;
            }
            return (List<Object>)list;
        }
        catch(SecurityException | IllegalAccessException e)
        {
            LOG.error("invalid ListMergeDirective [{}], depends-on [{}] fieldName [{}] did not resolve a field", new Object[] {directiveBeanName, beanName, fieldName});
            if(LOG.isDebugEnabled())
            {
                LOG.debug("invalid ListMergeDirective [{}], depends-on [{}] fieldName [{}] did not resolve a field", new Object[] {directiveBeanName, beanName, fieldName, e});
            }
            return null;
        }
    }


    private ListMergeDirective buildListMergeDirective(BeanDefinitionValueResolver valueResolver, BeanDefinition listMergeDirectiveBeanDefintion)
    {
        ListMergeDirective result = new ListMergeDirective();
        MutablePropertyValues propertyValues = listMergeDirectiveBeanDefintion.getPropertyValues();
        for(PropertyValue propertyValue : propertyValues.getPropertyValues())
        {
            String name = propertyValue.getName();
            Object value = valueResolver.resolveValueIfNecessary(name, propertyValue.getValue());
            switch(name)
            {
                case "add":
                    result.setAdd(value);
                    break;
                case "afterBeanNames":
                    result.setAfterBeanNames((List)value);
                    break;
                case "afterClasses":
                    result.setAfterClasses((List)value);
                    break;
                case "afterValues":
                    result.setAfterValues((List)value);
                    break;
                case "beforeBeanNames":
                    result.setBeforeBeanNames((List)value);
                    break;
                case "beforeClasses":
                    result.setBeforeClasses((List)value);
                    break;
                case "beforeValues":
                    result.setBeforeValues((List)value);
                    break;
                case "fieldName":
                    result.setFieldName((String)value);
                    break;
                case "listPropertyDescriptor":
                    result.setListPropertyDescriptor((String)value);
                    break;
            }
        }
        return result;
    }
}
