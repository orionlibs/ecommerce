/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.bean;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.expression.ExpressionResolver;
import com.hybris.cockpitng.core.expression.ExpressionResolverFactory;
import com.hybris.cockpitng.core.util.impl.ReflectionUtils;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.expression.ExpressionException;

/**
 * Abstract bean post processor that should be used to change other bean that is defined in any spring context in this
 * bean's hierarchy (incl. parent context).
 */
public abstract class AbstractBeanExtender implements BeanFactoryAware, BeanPostProcessor, BeanNameAware
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBeanExtender.class);
    private final String extendedBeanName;
    private ExpressionResolverFactory resolverFactory;
    private ConfigurableListableBeanFactory beanFactory;
    private String property;
    private String beanName;
    private String getter;
    private String mapper;
    private String setter;


    protected AbstractBeanExtender(final String extendedBeanName)
    {
        this.extendedBeanName = extendedBeanName;
    }


    @PostConstruct
    public void initialize()
    {
        if(getBeanFactory().containsBean(getExtendedBeanName()) && getBeanFactory().isSingleton(getExtendedBeanName()))
        {
            final Object bean = getBeanFactory().getBean(getExtendedBeanName());
            modifyBean(bean, getExtendedBeanName());
        }
    }


    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String beanName)
    {
        return bean;
    }


    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName)
    {
        final Object modifiedBean;
        if(qualifiesForModification(bean, beanName))
        {
            modifiedBean = modifyBean(bean, beanName);
        }
        else
        {
            modifiedBean = bean;
        }
        return modifiedBean;
    }


    protected Object modifyBean(final Object bean, final String beanName)
    {
        Object target = readOriginalValue(bean, beanName);
        checkProperty(target);
        target = modifyProperty(target);
        target = mapProperty(bean, target);
        return writeNewValue(bean, beanName, target);
    }


    protected Object readOriginalValue(final Object bean, final String beanName)
    {
        final Object original;
        if(StringUtils.isNotEmpty(getGetter()))
        {
            original = resolveGetter(bean);
        }
        else if(StringUtils.isNotEmpty(getProperty()))
        {
            original = readProperty(bean, getProperty());
        }
        else
        {
            original = bean;
        }
        return original;
    }


    /**
     * @deprecated since 1905, use the
     *             {@link AbstractBeanExtender#resolveGetter(Object)} instead.
     */
    @Deprecated(since = "1905", forRemoval = true)
    protected Object resolveGetter(final Object bean, final String getter)
    {
        return resolveGetter(bean);
    }


    protected Object resolveGetter(final Object bean)
    {
        final ExpressionResolver resolver = getResolverFactory().createResolver();
        try
        {
            return resolver.getValue(bean, getGetter());
        }
        catch(final ExpressionException e)
        {
            throw new BeanInitializationException(getInitializationExceptionMessage(bean, getGetter()), e);
        }
    }


    protected Object readProperty(final Object bean, final String propertyName)
    {
        ImmutablePair<Optional<?>, Boolean> propertyVaulePair = readBeanPropertyValue(bean, propertyName);
        if(Boolean.FALSE.equals(propertyVaulePair.getRight()))
        {
            propertyVaulePair = readBeanSimplePropertyValue(bean, propertyName);
        }
        if(Boolean.FALSE.equals(propertyVaulePair.getRight()))
        {
            propertyVaulePair = readBeanPropertyValueWithGetter(bean, propertyName);
        }
        if(Boolean.FALSE.equals(propertyVaulePair.getRight()))
        {
            propertyVaulePair = readBeanPropertyValueWithReflection(bean, propertyName);
        }
        if(Boolean.FALSE.equals(propertyVaulePair.getRight()))
        {
            throw new BeanInitializationException(getInitializationExceptionMessage(bean, propertyName));
        }
        return propertyVaulePair.getLeft().orElse(null);
    }


    /**
     * @deprecated since 2205, use the {@link AbstractBeanExtender#readBeanPropertyValue(Object, String)} instead.
     */
    @Deprecated(since = "2205", forRemoval = true)
    protected Optional<?> readBeanProperty(final Object bean, final String propertyName)
    {
        try
        {
            return Optional.ofNullable(PropertyUtils.getProperty(bean, propertyName));
        }
        catch(final IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            LOGGER.debug(e.getLocalizedMessage(), e);
            return null;
        }
    }


    protected ImmutablePair<Optional<?>, Boolean> readBeanPropertyValue(final Object bean, final String propertyName)
    {
        try
        {
            final Optional<?> propertyValue = Optional.ofNullable(PropertyUtils.getProperty(bean, propertyName));
            return new ImmutablePair<>(propertyValue, true);
        }
        catch(final IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            LOGGER.debug(e.getLocalizedMessage(), e);
            return new ImmutablePair<>(Optional.empty(), false);
        }
    }


    /**
     * @deprecated since 2205, use the {@link AbstractBeanExtender#readBeanSimplePropertyValue(Object, String)} instead.
     */
    @Deprecated(since = "2205", forRemoval = true)
    protected Optional<?> readBeanSimpleProperty(final Object bean, final String propertyName)
    {
        try
        {
            return Optional.ofNullable(PropertyUtils.getSimpleProperty(bean, propertyName));
        }
        catch(final IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            LOGGER.debug(e.getLocalizedMessage(), e);
            return null;
        }
    }


    protected ImmutablePair<Optional<?>, Boolean> readBeanSimplePropertyValue(final Object bean, final String propertyName)
    {
        try
        {
            final Optional<?> propertyValue = Optional.ofNullable(PropertyUtils.getSimpleProperty(bean, propertyName));
            return new ImmutablePair<>(propertyValue, true);
        }
        catch(final IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            LOGGER.debug(e.getLocalizedMessage(), e);
            return new ImmutablePair<>(Optional.empty(), false);
        }
    }


    /**
     * @deprecated since 2205, use the {@link AbstractBeanExtender#readBeanPropertyValueWithGetter(Object, String)} instead.
     */
    @Deprecated(since = "2205", forRemoval = true)
    protected Optional<?> readBeanPropertyWithGetter(final Object bean, final String propertyName)
    {
        try
        {
            final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, propertyName);
            if(descriptor != null)
            {
                final Method readMethod = descriptor.getReadMethod();
                if(readMethod != null)
                {
                    final boolean dump = readMethod.isAccessible();
                    try
                    {
                        readMethod.setAccessible(true);
                        return Optional.ofNullable(readMethod.invoke(bean));
                    }
                    finally
                    {
                        readMethod.setAccessible(dump);
                    }
                }
            }
        }
        catch(final IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            LOGGER.debug(e.getLocalizedMessage(), e);
        }
        return null;
    }


    protected ImmutablePair<Optional<?>, Boolean> readBeanPropertyValueWithGetter(final Object bean, final String propertyName)
    {
        try
        {
            final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, propertyName);
            if(descriptor != null)
            {
                return applyMethodAccessible(descriptor, bean);
            }
        }
        catch(final IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            LOGGER.debug(e.getLocalizedMessage(), e);
        }
        return new ImmutablePair<>(Optional.empty(), false);
    }


    private ImmutablePair<Optional<?>, Boolean> applyMethodAccessible(final PropertyDescriptor descriptor, final Object bean)
    {
        final Method readMethod = descriptor.getReadMethod();
        if(readMethod != null)
        {
            final boolean dump = readMethod.isAccessible();
            try
            {
                readMethod.setAccessible(true);
                final Optional<?> propertyValue = Optional.ofNullable(readMethod.invoke(bean));
                return new ImmutablePair<>(propertyValue, true);
            }
            catch(final IllegalAccessException | InvocationTargetException e)
            {
                LOGGER.debug(e.getLocalizedMessage(), e);
            }
            finally
            {
                readMethod.setAccessible(dump);
            }
        }
        return new ImmutablePair<>(Optional.empty(), false);
    }


    /**
     * @deprecated since 2205, use the {@link AbstractBeanExtender#readBeanPropertyValueWithReflection(Object, String)}
     *             instead.
     */
    @Deprecated(since = "2205", forRemoval = true)
    protected Optional<?> readBeanPropertyWithReflection(final Object bean, final String propertyName)
    {
        try
        {
            final Field field = ReflectionUtils.getDeclaredField(bean.getClass(), propertyName);
            return Optional.ofNullable(ReflectionUtils.getField(field, bean));
        }
        catch(final NoSuchFieldException e)
        {
            LOGGER.debug(e.getLocalizedMessage(), e);
            return null;
        }
    }


    protected ImmutablePair<Optional<?>, Boolean> readBeanPropertyValueWithReflection(final Object bean, final String propertyName)
    {
        try
        {
            final Field field = ReflectionUtils.getDeclaredField(bean.getClass(), propertyName);
            final Optional<?> propertyValue = Optional.ofNullable(ReflectionUtils.getField(field, bean));
            return new ImmutablePair<>(propertyValue, true);
        }
        catch(final NoSuchFieldException e)
        {
            LOGGER.debug(e.getLocalizedMessage(), e);
            return new ImmutablePair<>(Optional.empty(), false);
        }
    }


    protected Object mapProperty(final Object bean, final Object value)
    {
        if(getMapper() != null)
        {
            final ExpressionResolver resolver = getResolverFactory().createResolver();
            try
            {
                return resolver.getValue(value, getMapper());
            }
            catch(final ExpressionException e)
            {
                throw new BeanInitializationException(getInitializationExceptionMessage(bean, getMapper()), e);
            }
        }
        return value;
    }


    protected Object writeNewValue(final Object bean, final String beanName, final Object value)
    {
        if(StringUtils.isNotEmpty(getSetter()))
        {
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("Calling setter for {} of '{}' and value: {}", beanName, getSetter(), Objects.toString(value));
            }
            return resolveSetter(bean, getSetter(), value);
        }
        else if(StringUtils.isNotEmpty(getProperty()))
        {
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("Changing {}#{} to {}", beanName, getProperty(), Objects.toString(value));
            }
            writeProperty(bean, getProperty(), value);
            return bean;
        }
        else
        {
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("Changing {} to {}", beanName, Objects.toString(value));
            }
            return value;
        }
    }


    protected Object resolveSetter(final Object bean, final String getter, final Object value)
    {
        final ExpressionResolver resolver = getResolverFactory().createResolver();
        try
        {
            resolver.setValue(bean, getSetter(), value);
            return value;
        }
        catch(final ExpressionException e)
        {
            throw new BeanInitializationException(getInitializationExceptionMessage(bean, getSetter()), e);
        }
    }


    protected void writeProperty(final Object bean, final String propertyName, final Object propertyValue)
    {
        final boolean propertySet = writeBeanProperty(bean, propertyName, propertyValue)
                        || writeBeanSimpleProperty(bean, propertyName, propertyValue)
                        || writeBeanPropertyWithSetter(bean, propertyName, propertyValue)
                        || writeBeanPropertyWithReflection(bean, propertyName, propertyValue);
        if(!propertySet)
        {
            throw new BeanInitializationException(
                            String.format("Unable to write property %s of bean %s", propertyName, Objects.toString(bean)));
        }
    }


    protected boolean writeBeanProperty(final Object bean, final String propertyName, final Object propertyValue)
    {
        try
        {
            PropertyUtils.setProperty(bean, propertyName, propertyValue);
            return true;
        }
        catch(final IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            LOGGER.debug(e.getLocalizedMessage(), e);
            return false;
        }
    }


    protected boolean writeBeanSimpleProperty(final Object bean, final String propertyName, final Object propertyValue)
    {
        try
        {
            PropertyUtils.setSimpleProperty(bean, propertyName, propertyValue);
            return true;
        }
        catch(final IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            LOGGER.debug(e.getLocalizedMessage(), e);
            return false;
        }
    }


    protected boolean writeBeanPropertyWithSetter(final Object bean, final String propertyName, final Object propertyValue)
    {
        try
        {
            final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, propertyName);
            if(descriptor != null)
            {
                final Method writeMethod = descriptor.getWriteMethod();
                if(writeMethod != null)
                {
                    final boolean dump = writeMethod.isAccessible();
                    try
                    {
                        writeMethod.setAccessible(true);
                        writeMethod.invoke(bean, propertyValue);
                        return true;
                    }
                    finally
                    {
                        writeMethod.setAccessible(dump);
                    }
                }
            }
        }
        catch(final IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            LOGGER.debug(e.getLocalizedMessage(), e);
        }
        return false;
    }


    protected boolean writeBeanPropertyWithReflection(final Object bean, final String propertyName, final Object propertyValue)
    {
        try
        {
            final Field field = ReflectionUtils.getDeclaredField(bean.getClass(), propertyName);
            ReflectionUtils.setField(field, bean, propertyValue);
            return true;
        }
        catch(final NoSuchFieldException e)
        {
            LOGGER.debug(e.getLocalizedMessage(), e);
            return false;
        }
    }


    protected String getInitializationExceptionMessage(final Object bean, final String propertyName)
    {
        return String.format("Unable to read property %s of bean %s", propertyName, Objects.toString(bean));
    }


    protected void checkPropertyType(final Object target, final Class<?> type)
    {
        if(target != null && !type.isAssignableFrom(target.getClass()))
        {
            throw new BeanInitializationException(String.format("%s is not valid for value modification. Expected value of type %s",
                            Objects.toString(target), type.getName()));
        }
    }


    /**
     * Method should check if value of property to be modified is proper (i.e. if bean is to modify a list, then it should
     * check if provided value is of type {@link List})
     *
     * @param target
     *           value that will be modified
     * @throws BeanInitializationException
     *            when provided value is compatible with this modification bean
     */
    protected abstract void checkProperty(final Object target);


    /**
     * Changes a provided value according to current modification bean configuration.
     *
     * @param target
     *           value to be changed
     * @return new value
     * @throws BeanInitializationException
     *            when a value may not be changed
     */
    protected abstract Object modifyProperty(final Object target);


    protected boolean qualifiesForModification(final Object bean, final String beanName)
    {
        return qualifiesForModification(getBeanFactory(), bean, beanName);
    }


    protected boolean qualifiesForModification(final ConfigurableListableBeanFactory beanFactory, final Object bean,
                    final String beanName)
    {
        final String[] aliases = beanFactory.getAliases(beanName);
        final List<String> names = Lists.asList(beanName, aliases);
        return names.contains(getExtendedBeanName());
    }


    @Override
    public void setBeanFactory(final BeanFactory beanFactory)
    {
        if(!(beanFactory instanceof ConfigurableListableBeanFactory))
        {
            throw new BeanCreationException(getBeanName(),
                            "Bean modifications may work on with org.springframework.beans.factory.config.ConfigurableListableBeanFactory");
        }
        this.beanFactory = (ConfigurableListableBeanFactory)beanFactory;
    }


    protected ConfigurableListableBeanFactory getBeanFactory()
    {
        return beanFactory;
    }


    @Override
    public void setBeanName(final String beanName)
    {
        this.beanName = beanName;
    }


    protected String getBeanName()
    {
        return beanName;
    }


    public String getExtendedBeanName()
    {
        return extendedBeanName;
    }


    protected String getProperty()
    {
        return property;
    }


    public void setProperty(final String property)
    {
        this.property = property;
    }


    protected ExpressionResolverFactory getResolverFactory()
    {
        return resolverFactory;
    }


    @Required
    public void setResolverFactory(final ExpressionResolverFactory resolverFactory)
    {
        this.resolverFactory = resolverFactory;
    }


    protected String getGetter()
    {
        return getter;
    }


    public void setGetter(final String getter)
    {
        this.getter = getter;
    }


    protected String getMapper()
    {
        return mapper;
    }


    public void setMapper(final String mapper)
    {
        this.mapper = mapper;
    }


    protected String getSetter()
    {
        return setter;
    }


    public void setSetter(final String setter)
    {
        this.setter = setter;
    }
}
