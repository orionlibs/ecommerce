/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.labels;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.context.ServletContextAware;
import org.zkoss.util.resource.LabelLocator;
import org.zkoss.util.resource.Labels;

/**
 * Label locator for a web application. Use it to locate labels on a web application level. To activate and configure
 * add a spring bean to your web spring xml file like this:
 *
 * {@code
 * <bean id="mywebappLabelLocator" class="com.hybris.cockpitng.util.labels.WebappLabelLocator" scope=
"singleton" init-method="init" lazy-init="false">
 * 	<property name="location" value="/WEB-INF/labels"/>
 * 	<property name="name" value="labels"/>
 * </bean>
 * }
 */
public class WebappLabelLocator implements LabelLocator, ServletContextAware
{
    private static final Logger LOG = LoggerFactory.getLogger(WebappLabelLocator.class);
    private String location;
    private String name;
    private ServletContext servletContext;


    /**
     * Registers this label locator. Should be specified as init-method if this will be registered as a spring bean.
     */
    public void init()
    {
        Labels.register(this);
    }


    /**
     * Sets the location of the localization files within your web application. It must point to the directory which contain
     * the files. Example: '/WEB-INF/labels'.
     *
     * @param location
     *           location of the localization files within your web application (a directory)
     */
    @Required
    public void setLocation(final String location)
    {
        this.location = location;
    }


    /**
     * Sets the prefix of the label files. Will be used to locate the labels files in the specified location (@see
     * {@link #setLocation(String)}). For example: setting the name to 'labels' will look for files with naming scheme
     * labels_&lt;LOCALE_CODE&gt;.properties where &lt;LOCALE_CODE&gt; is the code of the current locale. (The default fallback file
     * will be labels.properties in this case.)
     *
     * @param name
     *           prefix of the label files
     */
    @Required
    public void setName(final String name)
    {
        this.name = name;
    }


    @Override
    public URL locate(final Locale locale)
    {
        final StringBuilder resourceName = new StringBuilder(location);
        if(!location.endsWith("/"))
        {
            resourceName.append('/');
        }
        resourceName.append(name);
        if(locale != null)
        {
            resourceName.append('_').append(LabelLocatorUtils.toIso3166String(locale));
        }
        resourceName.append(".properties");
        try
        {
            return servletContext.getResource(resourceName.toString());
        }
        catch(final MalformedURLException e)
        {
            LOG.warn(String.format("unable to locate the localization file [%s]", resourceName), e);
            return null;
        }
    }


    @Override
    public void setServletContext(final ServletContext servletContext)
    {
        this.servletContext = servletContext;
    }
}
