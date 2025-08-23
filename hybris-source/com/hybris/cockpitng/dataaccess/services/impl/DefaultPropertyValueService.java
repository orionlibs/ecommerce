/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.services.impl;

import com.hybris.cockpitng.core.expression.ExpressionResolver;
import com.hybris.cockpitng.core.expression.ExpressionResolverFactory;
import com.hybris.cockpitng.dataaccess.services.PropertyReadResult;
import com.hybris.cockpitng.dataaccess.services.PropertyValueService;
import com.hybris.cockpitng.dataaccess.services.impl.expression.RestrictedAccessException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelMessage;

/**
 * Default implementation of service for reading and setting property values of an object.
 */
public class DefaultPropertyValueService implements PropertyValueService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultPropertyValueService.class);
    private ExpressionResolverFactory resolverFactory;
    private ExpressionResolverFactory restrictedResolverFactory;


    @Override
    public void setLocalizedValue(final Object sourceObject, final String propertyQualifier, final Object value,
                    final Locale locale)
    {
        getResolverFactory().createResolver().setValue(sourceObject, propertyQualifier, value, locale);
    }


    /**
     * Calls the readValues method and return a value of an attribute.
     */
    @Override
    public Object readValue(final Object sourceObject, final String qualifier)
    {
        final Map<String, Object> valueMap = readValues(sourceObject, Collections.singletonList(qualifier));
        if(canProcessValueMap(valueMap, qualifier))
        {
            return valueMap.values().iterator().next();
        }
        return null;
    }


    /**
     * Calls the readValues method and return a map of attributes and the corresponding value of each attribute.
     */
    @Override
    public Map<String, Object> readValues(final Object sourceObject, final List<String> qualifiers)
    {
        return readValues(sourceObject, qualifiers, null);
    }


    @Override
    public PropertyReadResult readRestrictedValue(final Object sourceObject, final String propertyQualifier)
    {
        final List<String> qualifiers = Collections.singletonList(propertyQualifier);
        final Map<String, PropertyReadResult> valueMap = readRestrictedValues(sourceObject, qualifiers);
        if(canProcessValueMap(valueMap, propertyQualifier))
        {
            return valueMap.values().iterator().next();
        }
        return null;
    }


    @Override
    public Map<String, PropertyReadResult> readRestrictedValues(final Object sourceObject, final List<String> propertyQualifiers)
    {
        return readRestrictedValues(sourceObject, propertyQualifiers, null);
    }


    @Override
    public Map<Locale, Object> readValue(final Object sourceObject, final String qualifier, final List<Locale> locales)
    {
        final Map<String, Object> valueMap = readValues(sourceObject, Collections.singletonList(qualifier), locales);
        if(canProcessValueMap(valueMap, qualifier))
        {
            final Object value = valueMap.values().iterator().next();
            if(value instanceof Map)
            {
                return (Map<Locale, Object>)value;
            }
        }
        return new HashMap<>();
    }


    @Override
    public Map<Locale, PropertyReadResult> readRestrictedValue(final Object sourceObject, final String qualifier,
                    final List<Locale> locales)
    {
        final List<String> qualifiers = Collections.singletonList(qualifier);
        final Map<Locale, PropertyReadResult> resultMap = new HashMap<>(locales.size());
        final Map<String, PropertyReadResult> valueMap = readRestrictedValues(sourceObject, qualifiers, locales);
        if(!canProcessValueMap(valueMap, qualifier))
        {
            return resultMap;
        }
        final PropertyReadResult readResult = valueMap.values().iterator().next();
        if(readResult.isSuccessful() && (readResult.getValue() instanceof Map))
        {
            for(final Locale locale : locales)
            {
                resultMap.put(locale, new PropertyReadResult(((Map)readResult.getValue()).get(locale)));
            }
        }
        else if(readResult.isSuccessful() || readResult.isRestricted())
        {
            for(final Locale locale : locales)
            {
                resultMap.put(locale, readResult);
            }
        }
        else
        {
            for(final Locale locale : locales)
            {
                resultMap.put(locale, new PropertyReadResult(PropertyReadResult.READ_STATUS_ERROR));
            }
        }
        return resultMap;
    }


    protected boolean canProcessValueMap(final Map valueMap, final String propertyQualifier)
    {
        return MapUtils.isNotEmpty(valueMap) && valueMap.keySet().size() == 1 && valueMap.containsKey(propertyQualifier);
    }


    /**
     * @param sourceObject
     *           is the instance of a type
     * @param qualifiers
     *           is a list of the attribute qualifiers of the type
     * @param locales
     *           is a list of locales
     * @return a map of attributes and their corresponding localized values
     */
    @Override
    public Map<String, Object> readValues(final Object sourceObject, final List<String> qualifiers, final List<Locale> locales)
    {
        final ExpressionResolver resolver = getResolverFactory().createResolver();
        final Map<String, Object> values = new LinkedHashMap<>();
        for(final String qualifier : qualifiers)
        {
            try
            {
                final Object value = resolver.getValue(sourceObject, qualifier, Collections.singletonMap("locales", locales));
                values.put(qualifier, value);
            }
            catch(final ParseException | SpelEvaluationException e)
            {
                LOG.error("error parsing expression '" + qualifier + "' on object " + sourceObject, e);
            }
        }
        return values;
    }


    @Override
    public Map<String, PropertyReadResult> readRestrictedValues(final Object sourceObject, final List<String> qualifiers,
                    final List<Locale> locales)
    {
        final Map<String, PropertyReadResult> values = new LinkedHashMap<>();
        qualifiers.forEach(qualifier -> {
            final PropertyReadResult propertyResult = createPropertyResult(sourceObject, locales, qualifier);
            values.put(qualifier, propertyResult);
        });
        return values;
    }


    protected PropertyReadResult createPropertyResult(final Object sourceObject, final List<Locale> locales,
                    final String qualifier)
    {
        final ExpressionResolver resolver = getRestrictedResolverFactory().createResolver();
        try
        {
            final Object value = resolver.getValue(sourceObject, qualifier, Collections.singletonMap("locales", locales));
            return new PropertyReadResult(value);
        }
        catch(final RestrictedAccessException e)
        {
            LOG.debug("could not access value with qualifier '" + qualifier + "' on object " + sourceObject, e);
            return new PropertyReadResult(PropertyReadResult.READ_STATUS_RESTRICTED);
        }
        catch(final SpelEvaluationException e)
        {
            LOG.debug("could not evaluate SpEL expression with qualifier '" + qualifier + "' on object " + sourceObject, e);
            return handleEvaluationException(e);
        }
        catch(final ParseException e)
        {
            LOG.error("error while parsing expression '" + qualifier + "' on object " + sourceObject, e);
            return new PropertyReadResult(PropertyReadResult.READ_STATUS_ERROR);
        }
    }


    protected PropertyReadResult handleEvaluationException(final SpelEvaluationException e)
    {
        final boolean couldNotAccessProperty = e.getMessageCode().equals(SpelMessage.EXCEPTION_DURING_PROPERTY_READ);
        if(couldNotAccessProperty)
        {
            return new PropertyReadResult(PropertyReadResult.READ_STATUS_RESTRICTED);
        }
        return new PropertyReadResult(PropertyReadResult.READ_STATUS_ERROR);
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


    protected ExpressionResolverFactory getRestrictedResolverFactory()
    {
        return restrictedResolverFactory;
    }


    @Required
    public void setRestrictedResolverFactory(final ExpressionResolverFactory restrictedResolverFactory)
    {
        this.restrictedResolverFactory = restrictedResolverFactory;
    }
}
