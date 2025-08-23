/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.labels;

import com.google.common.collect.Iterators;
import com.hybris.cockpitng.core.util.ClassLoaderUtils;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.LabelLocator2;
import org.zkoss.util.resource.Labels;

/**
 * Label locator loading all labels matching the pattern from resources. To activate and configure add a spring bean to
 * your web spring xml file like this:
 * <P>{@code
 * <bean id="myLabelLocator" class="com.hybris.cockpitng.util.labels.ResourceBundleLabelLocator"
 * 		scope="singleton" init-method="init" lazy-init="false">
 * 	<property name="location" value="/my/path/in/resources/labels/"/>
 * 	<property name="name" value="labels"/>
 * </bean>}</P>
 */
public class ResourceBundleLabelLocator implements LabelLocator2
{
    private static final String FILE_FORMAT_PROPERTIES = ".properties";
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
        final ClassLoader currentClassLoader = ClassLoaderUtils.getCurrentClassLoader(this.getClass());
        final String pkg = StringUtils.removeEnd(StringUtils.removeStart(location.replaceAll("/", "."), "."), ".");
        final Collection<URL> urls = ClasspathHelper.forPackage(pkg, currentClassLoader);
        final Reflections reflections = new Reflections(
                        new ConfigurationBuilder().filterInputsBy(new FilterBuilder.Include(FilterBuilder.prefix(pkg))).setUrls(urls)
                                        .setScanners(new ResourcesScanner()));
        final StringBuilder resourceName = new StringBuilder(name);
        final Collection<String> files;
        if(locale != null)
        {
            resourceName.append('_').append(LabelLocatorUtils.toIso3166String(locale)).append(FILE_FORMAT_PROPERTIES);
            files = reflections.getResources(Pattern.compile(".*" + resourceName));
        }
        else
        {
            resourceName.append(FILE_FORMAT_PROPERTIES);
            final String enResourceName = StringUtils.replaceOnce(String.valueOf(resourceName), FILE_FORMAT_PROPERTIES,
                            "_en.properties");
            final Collection<String> defaultFiles = reflections.getResources(Pattern.compile(".*" + resourceName));
            final Collection<String> enFiles = reflections.getResources(Pattern.compile(".*" + enResourceName));
            defaultFiles.addAll(enFiles);
            files = defaultFiles;
        }
        final Iterator<InputStream> inputStreamIterator = files.stream().map(currentClassLoader::getResourceAsStream).iterator();
        return new SequenceInputStream(Iterators.asEnumeration(inputStreamIterator));
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
