package de.hybris.platform.spring.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
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
import org.springframework.beans.factory.config.MapFactoryBean;
import org.springframework.util.ReflectionUtils;

public class MapMergeDirectiveBeanPostProcessor implements BeanFactoryAware, BeanPostProcessor, InitializingBean
{
    private static final Logger LOG = LoggerFactory.getLogger(MapMergeDirectiveBeanPostProcessor.class.getName());
    private static final String INVALID_MMD = "invalid MapMergeDirective [{}], depends-on [{}]";
    private static final String INVALID_MMD_FIELD = "invalid MapMergeDirective [{}], depends-on [{}] fieldName [{}]";
    private static final String INVALID_MMD_FIELD_NOT_A_MAP = "invalid MapMergeDirective [{}], depends-on [{}] fieldName [{}] is not of type java.util.Map";
    private static final String INVALID_MMD_FIELD_NULL = "invalid MapMergeDirective [{}], depends-on [{}] fieldName [{}] map is null";
    private static final String INVALID_MMD_FIELD_CANT_RESOLVE = "invalid MapMergeDirective [{}], depends-on [{}] fieldName [{}] did not resolve a field";
    private static final String INVALID_MMD_PROPERTY = "invalid MapMergeDirective [{}], depends-on [{}] propertyDescriptor [{}]";
    private static final String INVALID_MMD_PROPERTY_CANT_RESOLVE = "invalid MapMergeDirective [{}], depends-on [{}] propertyDescriptor [{}] did not resolve a property";
    private static final String INVALID_MMD_PROPERTY_NOT_A_MAP = "invalid MapMergeDirective [{}], depends-on [{}] propertyDescriptor [{}] is not of type java.util.Map";
    private static final String INVALID_MMD_PROPERTY_NULL = "invalid MapMergeDirective [{}], depends-on [{}] propertyDescriptor [{}] map is null";
    private ConfigurableListableBeanFactory beanFactory;
    private Map<String, List<String>> mergeDirectiveDependencyMap;
    private Map<String, BeanDefinition> mergeDirectiveMap;


    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.beanFactory = (ConfigurableListableBeanFactory)beanFactory;
    }


    public void afterPropertiesSet() throws Exception
    {
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
            return MapMergeDirective.class.isAssignableFrom(clazz);
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
        if(CollectionUtils.isNotEmpty(toMergeList) && !MapFactoryBean.class.isAssignableFrom(bean
                        .getClass()))
        {
            for(String mmdName : toMergeList)
            {
                LOG.info("Post Processing MapMergeDirective [{}] on Bean [{}]", mmdName, beanName);
                BeanDefinition mmdBeanDefinition = this.mergeDirectiveMap.get(mmdName);
                BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver((ConfigurableBeanFactory)this.beanFactory, mmdName, mmdBeanDefinition, (TypeConverter)new SimpleTypeConverter());
                MapMergeDirective mapMergeDirective = buildMapMergeDirective(valueResolver, mmdBeanDefinition);
                processMergeDirective(mapMergeDirective, mmdName, bean, beanName);
            }
            this.mergeDirectiveDependencyMap.remove(beanName);
        }
        else if(bean instanceof MapMergeDirective && shouldApplyMergeDirectiveBean(beanName))
        {
            MapMergeDirective mapMergeDirective = (MapMergeDirective)bean;
            BeanDefinition beanDefinition = this.mergeDirectiveMap.get(beanName);
            String[] dependsOn = beanDefinition.getDependsOn();
            if(dependsOn != null)
            {
                for(String targetName : dependsOn)
                {
                    LOG.info("Post Processing MapMergeDirective [{}] on Bean [{}]", beanName, targetName);
                    Object targetBean = this.beanFactory.getBean(targetName);
                    processMergeDirective(mapMergeDirective, beanName, targetBean, targetName);
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


    private void processMergeDirective(MapMergeDirective mapMergeDirective, String mmdName, Object bean, String beanName)
    {
        Map<Object, Object> map = resolveMapBean(mapMergeDirective, mmdName, bean, beanName);
        if(map != null)
        {
            processDirective(mapMergeDirective, map);
        }
    }


    private MapMergeDirective buildMapMergeDirective(BeanDefinitionValueResolver valueResolver, BeanDefinition mapMergeDirectiveBeanDefintion)
    {
        MapMergeDirective result = new MapMergeDirective();
        MutablePropertyValues propertyValues = mapMergeDirectiveBeanDefintion.getPropertyValues();
        for(PropertyValue propertyValue : propertyValues.getPropertyValues())
        {
            String name = propertyValue.getName();
            Object value = valueResolver.resolveValueIfNecessary(name, propertyValue.getValue());
            switch(name)
            {
                case "key":
                    result.setKey(value);
                    break;
                case "value":
                    result.setValue(value);
                    break;
                case "mapPropertyDescriptor":
                    result.setMapPropertyDescriptor((String)value);
                    break;
                case "fieldName":
                    result.setFieldName((String)value);
                    break;
                case "sourceMap":
                    result.setSourceMap((Map)value);
                    break;
            }
        }
        return result;
    }


    private Map<Object, Object> resolveMapBean(MapMergeDirective mmd, String directiveBeanName, Object bean, String beanName)
    {
        if(mmd.getMapPropertyDescriptor() == null && mmd.getFieldName() == null)
        {
            return getMapByDependency(bean, beanName, directiveBeanName);
        }
        if(mmd.getMapPropertyDescriptor() != null)
        {
            return getMapByPropertyDescriptor(bean, beanName, mmd.getMapPropertyDescriptor(), directiveBeanName);
        }
        return getMapByReflection(bean, mmd.getFieldName(), directiveBeanName, beanName);
    }


    private Map<Object, Object> getMapByDependency(Object bean, String beanName, String directiveBeanName)
    {
        if(bean instanceof Map)
        {
            return (Map<Object, Object>)bean;
        }
        LOG.error("{} is not of type java.util.Map {} {}", new Object[] {"invalid MapMergeDirective [{}], depends-on [{}]", directiveBeanName, beanName});
        return null;
    }


    private Map<Object, Object> getMapByPropertyDescriptor(Object bean, String beanName, String propertyDescriptor, String directiveBeanName)
    {
        try
        {
            Object map = PropertyUtils.getProperty(bean, propertyDescriptor);
            if(map == null)
            {
                LOG.error("invalid MapMergeDirective [{}], depends-on [{}] propertyDescriptor [{}] map is null", new Object[] {directiveBeanName, beanName, propertyDescriptor});
                return null;
            }
            if(!(map instanceof Map))
            {
                LOG.error("invalid MapMergeDirective [{}], depends-on [{}] propertyDescriptor [{}] is not of type java.util.Map", new Object[] {directiveBeanName, beanName, propertyDescriptor});
                return null;
            }
            return (Map<Object, Object>)map;
        }
        catch(NoSuchMethodException | java.lang.reflect.InvocationTargetException | IllegalAccessException e)
        {
            LOG.error("invalid MapMergeDirective [{}], depends-on [{}] propertyDescriptor [{}] did not resolve a property", new Object[] {directiveBeanName, beanName, propertyDescriptor});
            if(LOG.isDebugEnabled())
            {
                LOG.debug("invalid MapMergeDirective [{}], depends-on [{}] propertyDescriptor [{}] did not resolve a property", new Object[] {directiveBeanName, beanName, propertyDescriptor, e});
            }
            return null;
        }
    }


    private Map<Object, Object> getMapByReflection(Object bean, String fieldName, String directiveBeanName, String dependency)
    {
        try
        {
            Field field = ReflectionUtils.findField(bean.getClass(), fieldName);
            if(field == null)
            {
                LOG.error("invalid MapMergeDirective [{}], depends-on [{}] fieldName [{}] did not resolve a field", new Object[] {directiveBeanName, dependency, fieldName});
                return null;
            }
            field.setAccessible(true);
            Object map = field.get(bean);
            if(map == null)
            {
                LOG.error("invalid MapMergeDirective [{}], depends-on [{}] fieldName [{}] map is null", new Object[] {directiveBeanName, dependency, fieldName});
                return null;
            }
            if(!(map instanceof Map))
            {
                LOG.error("invalid MapMergeDirective [{}], depends-on [{}] fieldName [{}] is not of type java.util.Map", new Object[] {directiveBeanName, dependency, fieldName});
                return null;
            }
            return (Map<Object, Object>)map;
        }
        catch(SecurityException | IllegalAccessException e)
        {
            LOG.error("invalid MapMergeDirective [{}], depends-on [{}] fieldName [{}] did not resolve a field", new Object[] {directiveBeanName, dependency, fieldName});
            if(LOG.isDebugEnabled())
            {
                LOG.debug("invalid MapMergeDirective [{}], depends-on [{}] fieldName [{}] did not resolve a field", new Object[] {directiveBeanName, dependency, fieldName, e});
            }
            return null;
        }
    }


    private void processDirective(MapMergeDirective directive, Map<Object, Object> mapBean)
    {
        if(directive.getKey() != null && mapBean.containsKey(directive.getKey()))
        {
            Object value = mapBean.get(directive.getKey());
            if(value instanceof Collection)
            {
                if(directive.getValue() instanceof Collection)
                {
                    ((Collection)value).addAll((Collection)directive.getValue());
                }
                else
                {
                    ((Collection<Object>)value).add(directive.getValue());
                }
            }
            else
            {
                mapBean.put(directive.getKey(), directive.getValue());
            }
        }
        else if(directive.getSourceMap() != null)
        {
            mapBean.putAll(directive.getSourceMap());
        }
        else
        {
            mapBean.put(directive.getKey(), directive.getValue());
        }
    }
}
