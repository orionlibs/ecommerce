/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.labels.impl;

import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.i18n.FallbackLocaleProvider;
import com.hybris.cockpitng.labels.LabelStringObjectHandler;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * {@link LabelStringObjectHandler} implementation that can get localized labels for {@link DataType} and
 * {@link DataAttribute} qualifiers.
 */
public class TypeQualifierStringObjectHandler implements LabelStringObjectHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(TypeQualifierStringObjectHandler.class);
    private static final Pattern TYPE_WITH_ATTRIBUTE = Pattern.compile("^([\\w]+)\\.([\\w]+)$");
    private TypeFacade typeFacade;
    private FallbackLocaleProvider fallbackLocaleProvider;


    @Override
    public String getObjectLabel(final String key)
    {
        final Pair<String, String> typeAndAttribute = parseTypeAndAttribute(key);
        if(typeAndAttribute != null && StringUtils.isNotBlank(typeAndAttribute.getRight()))
        {
            return getAttributeLabel(typeAndAttribute.getLeft(), typeAndAttribute.getRight());
        }
        else
        {
            return getTypeLabel(key);
        }
    }


    protected Pair<String, String> parseTypeAndAttribute(final String key)
    {
        if(key.contains("."))
        {
            String type = null;
            String attribute = null;
            if(CharUtils.isAsciiAlphaUpper(key.charAt(0)))
            {
                final String[] split = key.split("\\.", 2);
                type = split[0];
                attribute = split[1];
            }
            else
            {
                final String[] allTokens = key.split("\\.");
                final StringBuilder typeBuilder = new StringBuilder();
                final StringBuilder attributeBuilder = new StringBuilder();
                for(final String token : allTokens)
                {
                    if(StringUtils.isBlank(token))
                    {
                        return null;
                    }
                    if(type == null)
                    {
                        typeBuilder.append(token);
                        if(CharUtils.isAsciiAlphaUpper(token.charAt(0)))
                        {
                            type = typeBuilder.toString();
                        }
                        else
                        {
                            typeBuilder.append('.');
                        }
                    }
                    else
                    {
                        attributeBuilder.append('.').append(token);
                    }
                }
                if(attributeBuilder.length() > 0)
                {
                    attributeBuilder.deleteCharAt(0);
                }
                attribute = attributeBuilder.toString();
            }
            return new ImmutablePair<String, String>(type, attribute);
        }
        else
        {
            return new ImmutablePair<String, String>(key, null);
        }
    }


    /**
     * Returns the localized label of the {@link DataType} for the given typecode.
     */
    protected String getTypeLabel(final String typeCode)
    {
        try
        {
            final DataType dataType = typeFacade.load(typeCode);
            final String label = dataType.getLabel(getCurrentLocale());
            return StringUtils.isBlank(label) ? getFallbackDataTypeLabel(dataType) : label;
        }
        catch(final TypeNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Failed to resolve type for key " + typeCode, e);
            }
        }
        return null;
    }


    private String getFallbackDataTypeLabel(final DataType dataType)
    {
        if(getFallbackLocaleProvider() != null)
        {
            Map<Locale, String> map = new HashMap();
            for(final Locale fallback : getFallbackLocaleProvider().getFallbackLocales(getCurrentLocale()))
            {
                final String fallbacklabel = dataType.getLabel(fallback);
                if(StringUtils.isNotBlank(fallbacklabel))
                {
                    map.put(fallback, fallbacklabel);
                    dataType.getAllLabels().putAll(map);
                    LOG.debug("Less specific locale: " + fallback.getLanguage() + " used to resolve label for type: "
                                    + dataType.getCode());
                    return fallbacklabel;
                }
            }
        }
        final String englishFallbacklabel = dataType.getLabel(Locale.ENGLISH);
        if(StringUtils.isNotBlank(englishFallbacklabel))
        {
            LOG.debug("English locale used to resolve label for type: "
                            + dataType.getCode());
            return englishFallbacklabel;
        }
        return "";
    }


    private String getFallbackDataAttributeLabel(final DataAttribute dataAttribute)
    {
        if(getFallbackLocaleProvider() != null)
        {
            Map<Locale, String> map = new HashMap();
            for(final Locale fallback : getFallbackLocaleProvider().getFallbackLocales(getCurrentLocale()))
            {
                final String fallbacklabel = dataAttribute.getLabel(fallback);
                if(StringUtils.isNotBlank(fallbacklabel))
                {
                    map.put(fallback, fallbacklabel);
                    dataAttribute.getAllLabels().putAll(map);
                    LOG.debug("Less specific locale: " + fallback.getLanguage() + " used to resolve label for attribute: "
                                    + dataAttribute.getQualifier());
                    return fallbacklabel;
                }
            }
        }
        final String englishFallbacklabel = dataAttribute.getLabel(Locale.ENGLISH);
        if(StringUtils.isNotBlank(englishFallbacklabel))
        {
            LOG.debug("English locale used to resolve label for attribute: "
                            + dataAttribute.getQualifier());
            return englishFallbacklabel;
        }
        return "";
    }


    /**
     * Returns the localized label of the {@link DataAttribute} for the given typecode and attributeQualifier.
     * The attributeQualifier string can be in dot-notation to get nested attributes, e.g.
     * 'Product.catalogVersion.version'.
     */
    protected String getAttributeLabel(final String typeCode, final String attributeQualifier)
    {
        final DataAttribute attribute = getAttribute(typeCode, attributeQualifier);
        return attribute == null ? null : getLabel(attribute);
    }


    private String getLabel(final DataAttribute attribute)
    {
        final String label = attribute.getLabel(getCurrentLocale());
        if(StringUtils.isBlank(label))
        {
            return getFallbackDataAttributeLabel(attribute);
        }
        return label;
    }


    /**
     * See {@link #getAttributeLabel(String, String)}.
     */
    protected DataAttribute getAttribute(final String typeCode, final String attributeQualifier)
    {
        if(typeCode == null)
        {
            return null;
        }
        DataType dataType = null;
        try
        {
            dataType = typeFacade.load(typeCode);
        }
        catch(final TypeNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error occurred while getting attribute", e);
            }
            return null;
        }
        if(attributeQualifier.contains("."))
        {
            // handle nested
            final String[] split = attributeQualifier.split("\\.", 2);
            final DataAttribute attribute = dataType.getAttribute(split[0]);
            if(attribute != null && attribute.getValueType() != null)
            {
                return getAttribute(attribute.getValueType().getCode(), split[1]);
            }
        }
        else
        {
            final DataAttribute attribute = dataType.getAttribute(attributeQualifier);
            if(attribute != null)
            {
                return attribute;
            }
        }
        return null;
    }


    @Override
    public Locale getCurrentLocale()
    {
        return Locale.ENGLISH;
    }


    @Override
    public String getObjectDescription(final String key)
    {
        final Matcher matcher = TYPE_WITH_ATTRIBUTE.matcher(key);
        if(matcher.matches())
        {
            return typeFacade.getAttributeDescription(matcher.group(1), matcher.group(2));
        }
        return StringUtils.EMPTY;
    }


    @Override
    public String getObjectIconPath(final String key)
    {
        return null;
    }


    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public FallbackLocaleProvider getFallbackLocaleProvider()
    {
        return fallbackLocaleProvider;
    }


    @Required
    public void setFallbackLocaleProvider(final FallbackLocaleProvider fallbackLocaleProvider)
    {
        this.fallbackLocaleProvider = fallbackLocaleProvider;
    }
}
