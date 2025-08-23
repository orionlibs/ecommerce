/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.fulltextsearch.renderer;

import com.hybris.cockpitng.core.model.impl.AbstractObservable;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple observable POJO to collect all data available and needed for the fulltext search to perform its' job.
 *
 * @see #PROPERTY_ENABLED
 * @see #PROPERTY_NAME
 * @see #PROPERTY_VALUE
 */
public class FullTextSearchFilter extends AbstractObservable implements Cloneable
{
    /**
     * Name of property used in notification of <code>enabled</code> flag change
     *
     * @see #setEnabled(boolean)
     */
    public static final String PROPERTY_ENABLED = "enabled";
    /**
     * Name of property used in notification of <code>value</code> change
     *
     * @see #setValue(Object)
     */
    public static final String PROPERTY_VALUE = "value";
    /**
     * Name of property used in notification of <code>name</code> change
     *
     * @see #setName(String)
     */
    public static final String PROPERTY_NAME = "name";
    /**
     * Name of property used in notification of <code>operator</code> change
     *
     * @see #setOperator(ValueComparisonOperator)
     */
    public static final String PROPERTY_OPERATOR = "operator";
    /**
     * Name of property used in notification of <code>locale</code> change
     *
     * @see #setLocale(Locale)
     */
    public static final String PROPERTY_LOCALE = "locale";
    private static final Logger LOGGER = LoggerFactory.getLogger(FullTextSearchFilter.class);
    private String name;
    private boolean enabled;
    private ValueComparisonOperator operator;
    private Object value;
    private Locale locale;


    public FullTextSearchFilter()
    {
        // default constructor
    }


    public FullTextSearchFilter(final boolean enabled)
    {
        this.enabled = enabled;
    }


    /**
     * @param name
     *           name of filter to be used
     */
    public FullTextSearchFilter(final String name)
    {
        this.name = name;
    }


    public FullTextSearchFilter(final FullTextSearchFilter filter)
    {
        this.enabled = filter.enabled;
        this.locale = filter.locale;
        this.name = filter.name;
        this.operator = filter.operator;
        this.value = filter.value;
    }


    /**
     * @return name of filter to be used by Fulltext search
     */
    public String getName()
    {
        return name;
    }


    /**
     * All observers are notified about change that is done by this method.
     *
     * @param name
     *           new name of filter to be used by Fulltext search
     * @see #PROPERTY_NAME
     */
    public void setName(final String name)
    {
        this.name = name;
        changed(PROPERTY_NAME);
    }


    /**
     * @return operator to be used by Fulltext search
     */
    public ValueComparisonOperator getOperator()
    {
        return operator;
    }


    /**
     * All observers are notified about change that is done by this method.
     *
     * @param operator
     *           new name of filter to be used by Fulltext search
     * @see #PROPERTY_OPERATOR
     */
    public void setOperator(final ValueComparisonOperator operator)
    {
        this.operator = operator;
        changed(PROPERTY_OPERATOR);
    }


    /**
     * @return <code>true</code> if filter should be applied
     */
    public boolean isEnabled()
    {
        return enabled;
    }


    /**
     * All observers are notified about change that is done by this method.
     *
     * @param enabled
     *           <code>true</code> if filter should be applied
     * @see #PROPERTY_ENABLED
     */
    public void setEnabled(final boolean enabled)
    {
        this.enabled = enabled;
        changed(PROPERTY_ENABLED);
    }


    /**
     * @return value of a filter
     */
    public Object getValue()
    {
        return value;
    }


    /**
     * All observers are notified about change that is done by this method.
     *
     * @param value
     *           new value of filter to be used by Fulltext search
     * @see #PROPERTY_VALUE
     */
    public void setValue(final Object value)
    {
        this.value = value;
        changed(PROPERTY_VALUE);
    }


    /**
     * All observers are notified about change that is done by this method.
     *
     * @param locale
     *           new value of filter to be used by Fulltext search
     * @see #PROPERTY_LOCALE
     */
    public void setLocale(final Locale locale)
    {
        this.locale = locale;
        changed(PROPERTY_LOCALE);
    }


    public Locale getLocale()
    {
        return locale;
    }


    /**
     * @deprecated since 6.7 Use copy constructor instead.
     */
    @Override
    @Deprecated(since = "6.7", forRemoval = true)
    public FullTextSearchFilter clone()
    {
        try
        {
            return (FullTextSearchFilter)super.clone();
        }
        catch(final CloneNotSupportedException e)
        {
            LOGGER.error(e.getLocalizedMessage(), e);
            return new FullTextSearchFilter(name);
        }
    }
}
