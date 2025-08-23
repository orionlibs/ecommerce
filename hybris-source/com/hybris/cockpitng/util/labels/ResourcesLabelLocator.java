/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.labels;

import com.hybris.cockpitng.core.util.ClassLoaderUtils;
import java.io.InputStream;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.LabelLocator2;
import org.zkoss.util.resource.Labels;

/**
 * Label locator loading labels from resources. To activate and configure add a spring bean to your web spring xml file
 * like this:
 *
 * {@code
 * <bean id="myLabelLocator" class="com.hybris.cockpitng.util.labels.ResourcesLabelLocator" scope="singleton" init-method="init" lazy-init="false">
 * 	<property name="location" value="/my/path/in/resources/labels"/>
 * 	<property name="name" value="labels"/>
 * </bean>
 * }
 */
public class ResourcesLabelLocator implements LabelLocator2
{
    private String location;
    private String name;


    /**
     * Registers this label locator. Should be specified as init-method if this will be registered as a spring bean.
     */
    public void init()
    {
        Labels.register(this);
    }


    /**
     * Sets the location of the localization files in resources. It must point to the package which contain the files.
     * Example: '/my/path/in/resources/labels'.
     *
     * @param location
     *           location of the localization files within resources (a package)
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
    public InputStream locate(final Locale locale)
    {
        final StringBuilder resourceName = new StringBuilder(location);
        if(!this.location.endsWith("/"))
        {
            resourceName.append('/');
        }
        resourceName.append(this.name);
        final ClassLoader currentClassLoader = ClassLoaderUtils.getCurrentClassLoader(this.getClass());
        if(locale != null)
        {
            resourceName.append('_').append(LabelLocatorUtils.toIso3166String(locale)).append(".properties");
            return currentClassLoader.getResourceAsStream(resolveName(resourceName.toString()));
        }
        else
        {
            resourceName.append(".properties");
            return LabelLocatorUtils.loadDefaultLabelsWithFallbackToEn(resolveName(resourceName.toString()), currentClassLoader);
        }
    }


    private static String resolveName(final String name)
    {
        String resolvedName = null;
        if(name == null)
        {
            return name;
        }
        if(name.charAt(0) == '/')
        {
            resolvedName = name.substring(1);
        }
        return resolvedName;
    }


    /**
     * Returns "UTF-8".
     */
    @Override
    public String getCharset()
    {
        return "UTF-8";
    }
}
