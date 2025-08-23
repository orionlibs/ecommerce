/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.labels;

import com.hybris.cockpitng.core.util.Validate;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for label locators
 */
public final class LabelLocatorUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(LabelLocatorUtils.class);


    private LabelLocatorUtils()
    {
    }


    /**
     * Loads labels for given resource name and adds to them locales for en language. Default labels have precedence over en
     * labels.
     *
     * @param resourceName
     *           name of a resource without language extension. Name must end with .properties postfix.
     * @param classLoader
     *           classLoader used to load resources.
     * @return combined default labels and labels for en language.
     */
    public static InputStream loadDefaultLabelsWithFallbackToEn(final String resourceName, final ClassLoader classLoader)
    {
        Validate.notBlank("Resource name cannot be blank", resourceName);
        Validate.notNull("Classloader cannot be null", classLoader);
        final String enResourceName = StringUtils.replaceOnce(resourceName, ".properties", "_en.properties");
        final InputStream defaultLabels = classLoader.getResourceAsStream(resourceName);
        final InputStream enLabels = classLoader.getResourceAsStream(enResourceName);
        if(defaultLabels == null)
        {
            return enLabels;
        }
        if(enLabels != null)
        {
            try
            {
                return mergeLabelsInputStreams(defaultLabels, enLabels);
            }
            finally
            {
                IOUtils.closeQuietly(defaultLabels);
                IOUtils.closeQuietly(enLabels);
            }
        }
        return defaultLabels;
    }


    private static InputStream mergeLabelsInputStreams(final InputStream defaultLabels, final InputStream fallbackLabels)
    {
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream())
        {
            final Properties defaultProps = new Properties();
            defaultProps.load(defaultLabels);
            final Properties fallbackProps = new Properties();
            fallbackProps.load(fallbackLabels);
            fallbackProps.putAll(defaultProps);
            fallbackProps.store(outputStream, null);
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
        catch(final IOException e)
        {
            LOG.error("Cannot load default labels with fallback", e);
        }
        return null;
    }


    /**
     * Returns a string representation of a {@link Locale} in which a language code will be represented in ISO 3166 2-letter
     * code (lower case) instead of ISO 639.
     *
     * @param locale
     *           the locale object.
     * @return a string representation of the {@link Locale}.
     */
    static String toIso3166String(final Locale locale)
    {
        if(locale == null)
        {
            return null;
        }
        return locale.toLanguageTag().replace('-', '_');
    }
}
