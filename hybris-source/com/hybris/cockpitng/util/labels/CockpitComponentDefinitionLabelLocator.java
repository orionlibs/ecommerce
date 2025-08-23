/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.labels;

import com.google.common.io.CharSource;
import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.io.Files;
import org.zkoss.text.MessageFormats;
import org.zkoss.util.Maps;
import org.zkoss.util.resource.LabelLocator2;
import org.zkoss.util.resource.Labels;

/**
 * Locates labels in the component definitions. These are located in <code>labels/labels_*.properties</code> (relative
 * to the root of the definition). All label keys are processed and equipped with a prefix which is the component
 * definition code and the '.' character. For example the localization key <code>button.save</code> defined in the
 * component <code>com.simple.widget</code> will be translated to <code>com.simple.widget.button.save</code>. This is
 * then passed to the ZK label engine.
 * <p>
 * However, in a widget view you can access its labels without specifying the prefix like this:
 * <code>${labels.button.save}</code>. The <code>labels</code> map is prepared to contain widget specific labels only
 * and in the widget specific name space.
 * </p>
 * <p>
 * In the widget controller you can access the labels in an easy way as well. Let the {@link WidgetInstanceManager} be
 * injected in your controller class and use {@link WidgetInstanceManager#getLabel(String)} or
 * {@link WidgetInstanceManager#getLabel(String, Object[])} to get the label. Use the localization key as specified in
 * the properties file, e.g. <code>"button.save"</code>. If you use the {@link DefaultWidgetController} as the
 * superclass of your widget controller class you can call directly {@link DefaultWidgetController#getLabel(String)} or
 * {@link DefaultWidgetController#getLabel(String, Object[])}.
 * </p>
 */
public class CockpitComponentDefinitionLabelLocator implements LabelLocator2
{
    private static final Logger LOG = LoggerFactory.getLogger(CockpitComponentDefinitionLabelLocator.class);
    private CockpitComponentDefinitionService componentDefinitionService;


    public static Map<String, Object> getLabelMap(final AbstractCockpitComponentDefinition definition)
    {
        final String definitionCode = definition.getCode();
        final String[] tokens = StringUtils.split(definitionCode, '.');
        Map<String, Object> map = Labels.getSegmentedLabels();
        if(tokens != null)
        {
            for(final String token : tokens)
            {
                if(map.containsKey(token))
                {
                    final Object value = map.get(token);
                    if(value instanceof Map)
                    {
                        map = (Map<String, Object>)value;
                    }
                    else
                    {
                        map = Collections.emptyMap();
                        break;
                    }
                }
                else if(LOG.isDebugEnabled())
                {
                    LOG.debug("The key of [{}] is not found in labels!", token);
                }
            }
        }
        return new HashMap<>(map);
    }


    public static String getLabel(final Map<String, Object> labelMap, final String key)
    {
        if(StringUtils.isBlank(key))
        {
            return null;
        }
        Map<String, Object> map = labelMap;
        Object value = null;
        final String[] tokens = StringUtils.split(key, '.');
        for(final String token : tokens)
        {
            value = map.get(token);
            if(value instanceof Map)
            {
                map = (Map<String, Object>)value;
            }
            else
            {
                break;
            }
        }
        if(value instanceof Map)
        {
            value = ((Map)value).get("$");
        }
        return (value instanceof String) ? (String)value : null;
    }


    public static String getLabel(final Map<String, Object> labelMap, final String key, final Object[] args)
    {
        final String slabel = getLabel(labelMap, key);
        return slabel == null ? null : MessageFormats.format(slabel, args, null);
    }


    private static Map<String, String> addDefinitionPrefix(final AbstractCockpitComponentDefinition definition,
                    final Map<String, String> labels)
    {
        final Map<String, String> result = new HashMap<>();
        for(final Map.Entry<String, String> entry : labels.entrySet())
        {
            final String code = definition.getCode() != null ? definition.getCode() : definition.getParentCode();
            result.put(code + "." + entry.getKey(), entry.getValue());
        }
        return result;
    }


    /**
     * Locates labels for all widget definitions in the system.
     */
    @Override
    public InputStream locate(final Locale locale)
    {
        final List<LabelsInputSource> inputSources = new ArrayList<>();
        final Collection<AbstractCockpitComponentDefinition> allDefinitions = getComponentDefinitionService()
                        .getAllComponentDefinitions();
        allDefinitions.addAll(getComponentDefinitionService().getExtensionsDefinitions());
        for(final AbstractCockpitComponentDefinition definition : allDefinitions)
        {
            Map<String, String> labels = loadDefinitionLabels(definition, locale);
            if(!labels.isEmpty())
            {
                labels = addDefinitionPrefix(definition, labels);
                inputSources.add(new LabelsInputSource(labels));
            }
        }
        if(!inputSources.isEmpty())
        {
            final CharSource multi = CharSource.concat(inputSources);
            try
            {
                return new ReaderInputStream(multi.openStream(), getCharset());
            }
            catch(final IOException e)
            {
                LOG.error("error loading labels for cockpit component definitions, locale '" + locale + "'", e);
            }
        }
        return null;
    }


    private Map<String, String> loadDefinitionLabels(final AbstractCockpitComponentDefinition definition, final Locale locale)
    {
        final StringBuilder resourceName = new StringBuilder(ObjectUtils.defaultIfNull(definition.getResourcePath(), ""));
        if(StringUtils.isNotBlank(resourceName))
        {
            resourceName.append("/");
        }
        resourceName.append("labels/labels");
        final ClassLoader classLoader = getComponentDefinitionService().getClassLoader(definition);
        final InputStream stream;
        if(locale != null)
        {
            resourceName.append('_').append(LabelLocatorUtils.toIso3166String(locale)).append(".properties");
            stream = classLoader.getResourceAsStream(Files.normalize(resourceName.toString()));
        }
        else
        {
            resourceName.append(".properties");
            stream = LabelLocatorUtils.loadDefaultLabelsWithFallbackToEn(Files.normalize(resourceName.toString()), classLoader);
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Loading labels from {}", resourceName);
        }
        if(stream != null)
        {
            try
            {
                return loadLabels(stream);
            }
            catch(final IOException e)
            {
                LOG.error("error loading labels for cockpit component definition '" + definition + "' and locale '" + locale + "'",
                                e);
            }
        }
        return Collections.emptyMap();
    }


    private Map<String, String> loadLabels(final InputStream inputStream) throws IOException
    {
        final Map<String, String> labels = new HashMap<>();
        try
        {
            Maps.load(labels, inputStream, getCharset());
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch(final IOException ex)
            {
                LOG.error(ex.getMessage(), ex);
            }
        }
        return labels;
    }


    @Override
    public String getCharset()
    {
        return "UTF-8";
    }


    public void init()
    {
        Labels.register(this);
    }


    private CockpitComponentDefinitionService getComponentDefinitionService()
    {
        return componentDefinitionService;
    }


    @Required
    public void setComponentDefinitionService(final CockpitComponentDefinitionService componentDefinitionService)
    {
        this.componentDefinitionService = componentDefinitionService;
    }


    private static class LabelsInputSource extends CharSource
    {
        private final String lineSeparator = System.getProperty("line.separator");
        private final Map<String, String> labels;
        private String labelsAsString;


        public LabelsInputSource(final Map<String, String> labels)
        {
            this.labels = labels;
        }


        @Override
        public Reader openStream() throws IOException
        {
            if(this.labelsAsString == null)
            {
                this.labelsAsString = labelsToString(this.labels);
            }
            return new StringReader(this.labelsAsString);
        }


        private String labelsToString(final Map<String, String> labelMap)
        {
            final StringBuilder builder = new StringBuilder();
            builder.append(lineSeparator);
            for(final Map.Entry<String, String> entry : labelMap.entrySet())
            {
                builder.append(entry.getKey()).append('=').append(processNewLines(entry.getValue())).append(lineSeparator);
            }
            return builder.toString();
        }


        private String processNewLines(final String value)
        {
            if(value != null && (value.contains("\n") || value.contains("\r")))
            {
                return "{" + lineSeparator + value + lineSeparator + "}";
            }
            else
            {
                return value;
            }
        }
    }
}
