/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.util.labels;

import com.hybris.backoffice.constants.BackofficeModules;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.util.labels.ResourcesLabelLocator;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class BackofficeModulesLabelLocatorInit
{
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeModulesLabelLocatorInit.class);
    private CockpitLocaleService localeService;
    private String location;
    private String name;


    /**
     * Registers label locators for all backoffice module extensions. Should be specified as init-method if this will be
     * registered as a spring bean.
     */
    public void init()
    {
        final String normalizedLocation = getNormalizedLocation(location);
        final Set<String> propertyCandidates = lookupCandidatePropertyFiles(normalizedLocation);
        if(CollectionUtils.isEmpty(propertyCandidates))
        {
            return;
        }
        final Locale[] availableLocales = Locale.getAvailableLocales();
        for(final String extensionName : getAllBackofficeExtensionNames())
        {
            final String resourceName = StringUtils.replace(name, "{extName}", extensionName);
            final String resourcePath = normalizedLocation + '/' + resourceName;
            final ResourceBundle.Control control = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES);
            final List<String> properties = Stream.of(availableLocales)
                            .map(locale -> control.getCandidateLocales(resourcePath, locale)).flatMap(Collection::stream)
                            .map(locale -> control.toBundleName(resourcePath, locale))
                            .map(bundleName -> control.toResourceName(bundleName, "properties")).distinct().collect(Collectors.toList());
            final boolean hasLocalizedProperties = properties.stream().anyMatch(propertyCandidates::contains);
            if(hasLocalizedProperties)
            {
                final ResourcesLabelLocator extensionLocator = createResourcesLabelLocator(resourceName);
                extensionLocator.init();
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("Registering backoffice module label locator for extension '%s'", extensionName));
                }
            }
        }
    }


    protected Set<String> lookupCandidatePropertyFiles(final String location)
    {
        try
        {
            return Arrays
                            .stream(new PathMatchingResourcePatternResolver(Thread.currentThread().getContextClassLoader())
                                            .getResources("classpath*:" + location + "/*.properties"))
                            .filter(Resource::exists).map(r -> location + "/" + r.getFilename()).collect(Collectors.toSet());
        }
        catch(final IOException ioe)
        {
            throw new IllegalStateException("Could not read resources from classpath", ioe);
        }
    }


    /**
     * Creates a {@link ResourcesLabelLocator} with the given resource name and the current location.
     */
    protected ResourcesLabelLocator createResourcesLabelLocator(final String resourceName)
    {
        final ResourcesLabelLocator locator = new ResourcesLabelLocator();
        locator.setLocation(location);
        locator.setName(resourceName);
        return locator;
    }


    /**
     * Returns all backoffice module extension names in build order.
     */
    protected List<String> getAllBackofficeExtensionNames()
    {
        return BackofficeModules.getBackofficeModulesNames();
    }


    /**
     * Removes a possible leading or trailing '/'.
     */
    protected String getNormalizedLocation(final String location)
    {
        if(StringUtils.isBlank(location))
        {
            return StringUtils.EMPTY;
        }
        String ret = location.trim();
        while(ret.startsWith("/"))
        {
            ret = ret.substring(1);
        }
        while(StringUtils.isNotBlank(ret) && ret.charAt(ret.length() - 1) == '/')
        {
            if(ret.length() > 1)
            {
                ret = ret.substring(0, ret.length() - 1);
            }
            else
            {
                ret = StringUtils.EMPTY;
            }
        }
        return ret;
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
        ServicesUtil.validateParameterNotNullStandardMessage("location", location);
        this.location = location;
    }


    /**
     * Sets the prefix of the label files. Will be used to locate the labels files in the specified location (@see
     * {@link #setLocation(String)}). For example: setting the name to 'labels' will look for files with naming scheme
     * labels_<LOCALE_CODE>.properties where <LOCALE_CODE> is the code of the current locale. (The default fallback file
     * will be labels.properties in this case.)
     *
     * @param name
     *           prefix of the label files
     */
    @Required
    public void setName(final String name)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("name", name);
        this.name = name;
    }


    protected CockpitLocaleService getLocaleService()
    {
        return localeService;
    }


    @Required
    public void setLocaleService(final CockpitLocaleService localeService)
    {
        this.localeService = localeService;
    }
}
