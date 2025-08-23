/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.labels.impl;

import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.WidgetConfigurationContextDecorator;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Labels;
import com.hybris.cockpitng.core.impl.ModelValueHandlerFactory;
import com.hybris.cockpitng.core.model.ModelValueHandler;
import com.hybris.cockpitng.labels.LabelProvider;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.labels.LabelStringObjectHandler;
import java.text.NumberFormat;
import java.util.List;
import java.util.function.Function;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Default implementation of {@link LabelService} that uses {@link Base} configuration.
 */
public class DefaultLabelService implements LabelService, ApplicationContextAware
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultLabelService.class);
    private static final String BASE_COMPONENT = "base";
    private static final String NO_LABEL_CONFIGURATION_FOR_TYPE_MESSAGE = "No label configuration for type {}";
    private CockpitConfigurationService cockpitConfigurationService;
    private ModelValueHandler valueHandler;
    private ModelValueHandlerFactory modelValueHandlerFactory;
    private ApplicationContext applicationContext;
    private LabelStringObjectHandler labelStringObjectHandler;
    /**
     * @deprecated since 6.7
     */
    @Deprecated(since = "6.7", forRemoval = true)
    private List<WidgetConfigurationContextDecorator> contextDecorators;
    private LabelServiceCache labelServiceCache;


    @Override
    public void setApplicationContext(final ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }


    protected ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }


    @Override
    public String getObjectLabel(final Object object)
    {
        final Function<LabelProvider, String> labelProvider = provider -> provider.getLabel(object);
        final Function<Labels, String> labelsHandler = Labels::getLabel;
        return getLabelServiceCache().getObjectLabel(object, () -> getLabelInternal(object, labelProvider, labelsHandler));
    }


    @Override
    public String getShortObjectLabel(final Object object)
    {
        final Function<LabelProvider, String> labelProvider = provider -> provider.getShortLabel(object);
        final Function<Labels, String> labelsHandler = labels -> StringUtils.defaultIfBlank(labels.getShortLabel(),
                        labels.getLabel());
        return getLabelServiceCache().getObjectLabel(object, () -> getLabelInternal(object, labelProvider, labelsHandler));
    }


    protected String getLabelInternal(final Object object, final Function<LabelProvider, String> provider,
                    final Function<Labels, String> handler)
    {
        if(object == null)
        {
            return StringUtils.EMPTY;
        }
        if(object instanceof String)
        {
            if(labelStringObjectHandler == null)
            {
                return (String)object;
            }
            else
            {
                return labelStringObjectHandler.getObjectLabel((String)object);
            }
        }
        final Labels labelConfig = getLabelConfiguration(object);
        if(labelConfig != null)
        {
            final LabelProvider<Object> labelProvider = getLabelProvider(object, labelConfig);
            if(labelProvider != null)
            {
                return provider.apply(labelProvider);
            }
            else if(labelConfig.getLabel() != null)
            {
                final Object value = getValueHandler().getValue(object, handler.apply(labelConfig), true);
                if(value != null)
                {
                    return value.toString();
                }
            }
            else
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("{}{}", NO_LABEL_CONFIGURATION_FOR_TYPE_MESSAGE, object.getClass().getName());
                }
            }
        }
        return getFallbackLabel(object);
    }


    protected String getFallbackLabel(final Object object)
    {
        if(object instanceof Double)
        {
            return NumberFormat.getInstance().format(((Double)object).doubleValue());
        }
        return object.toString();
    }


    @Override
    public String getObjectDescription(final Object object)
    {
        return getLabelServiceCache().getObjectDescription(object, () -> getDescriptionInternal(object));
    }


    protected String getDescriptionInternal(final Object object)
    {
        if(object instanceof String && labelStringObjectHandler != null)
        {
            final String description = labelStringObjectHandler.getObjectDescription((String)object);
            if(StringUtils.isNotBlank(description))
            {
                return description;
            }
        }
        String ret = null;
        final Labels labelConfig = getLabelConfiguration(object);
        if(labelConfig != null)
        {
            final LabelProvider<Object> labelProvider = getLabelProvider(object, labelConfig);
            if(labelProvider != null)
            {
                ret = labelProvider.getDescription(object);
            }
            else if(labelConfig.getDescription() != null)
            {
                ret = (String)getValueHandler().getValue(object, labelConfig.getDescription(), true);
            }
            else
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("{}{}", NO_LABEL_CONFIGURATION_FOR_TYPE_MESSAGE, object.getClass().getName());
                }
            }
        }
        return ret;
    }


    @Override
    public String getObjectIconPath(final Object object)
    {
        return getLabelServiceCache().getObjectIconPath(object, () -> getIconPathInternal(object));
    }


    protected String getIconPathInternal(final Object object)
    {
        if(object instanceof String && labelStringObjectHandler != null)
        {
            return labelStringObjectHandler.getObjectIconPath((String)object);
        }
        String ret = null;
        final Labels labelConfig = getLabelConfiguration(object);
        if(labelConfig != null)
        {
            final LabelProvider<Object> labelProvider = getLabelProvider(object, labelConfig);
            if(labelProvider != null)
            {
                ret = labelProvider.getIconPath(object);
            }
            else if(labelConfig.getIconPath() != null)
            {
                ret = (String)getValueHandler().getValue(object, labelConfig.getIconPath());
            }
            else
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(NO_LABEL_CONFIGURATION_FOR_TYPE_MESSAGE, object.getClass().getName());
                }
            }
        }
        return ret;
    }


    protected Labels getLabelConfiguration(final Object object)
    {
        Labels ret = null;
        try
        {
            ConfigContext context = new DefaultConfigContext(BASE_COMPONENT, getType(object));
            context = buildConfigurationContext(context, Base.class);
            final Base config = cockpitConfigurationService.loadConfiguration(context, Base.class);
            ret = config.getLabels();
        }
        catch(final CockpitConfigurationException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("No base configuration for type %s", object.getClass().getName()), e);
            }
        }
        if(ret == null)
        {
            LOG.debug("{}{}", NO_LABEL_CONFIGURATION_FOR_TYPE_MESSAGE, object.getClass().getName());
        }
        return ret;
    }


    /**
     * @deprecated since 6.7 this method has no replacement. If you need to decorate the context for the label service
     *             subclass from this class and implement the decorating logic. This method will be removed along with the
     *             complementary {@link #setContextDecorators(List)} method.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected <CONFIG> ConfigContext buildConfigurationContext(final ConfigContext additionalContext,
                    final Class<CONFIG> configurationType)
    {
        ConfigContext decoratedContext = additionalContext;
        if(CollectionUtils.isNotEmpty(this.contextDecorators))
        {
            for(final WidgetConfigurationContextDecorator decorator : this.contextDecorators)
            {
                decoratedContext = decorator.decorateContext(decoratedContext, configurationType, null);
            }
        }
        return decoratedContext;
    }


    protected String getType(final Object object)
    {
        return object.getClass().getName();
    }


    protected LabelProvider<Object> getLabelProvider(final Object object, final Labels labelConfig)
    {
        if(labelConfig.getBeanId() != null)
        {
            try
            {
                return applicationContext.getBean(labelConfig.getBeanId(), LabelProvider.class);
            }
            catch(final BeansException be)
            {
                final String errorMessage = String.format("Error creating object label provider for %s with Spring bean ID %s",
                                object.getClass().getSimpleName(), labelConfig.getBeanId());
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(errorMessage, be);
                }
            }
        }
        return null;
    }


    protected ModelValueHandler getValueHandler()
    {
        if(valueHandler == null)
        {
            valueHandler = modelValueHandlerFactory.createModelValueHandler();
        }
        return valueHandler;
    }


    @Required
    public void setCockpitConfigurationService(final CockpitConfigurationService cockpitConfigurationService)
    {
        this.cockpitConfigurationService = cockpitConfigurationService;
    }


    @Required
    public void setModelValueHandlerFactory(final ModelValueHandlerFactory modelValueHandlerFactory)
    {
        this.modelValueHandlerFactory = modelValueHandlerFactory;
    }


    /**
     * @deprecated since 6.7 this method has no replacement. If you need to decorate the context for the label service
     *             subclass from this class and implement the decorating logic. This method will be removed along with the
     *             complementary {@link #buildConfigurationContext(ConfigContext, Class)} method.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public void setContextDecorators(final List<WidgetConfigurationContextDecorator> contextDecorators)
    {
        this.contextDecorators = contextDecorators;
    }


    public void setLabelStringObjectHandler(final LabelStringObjectHandler labelStringObjectHandler)
    {
        this.labelStringObjectHandler = labelStringObjectHandler;
    }


    @Required
    public void setLabelServiceCache(final LabelServiceCache labelServiceCache)
    {
        this.labelServiceCache = labelServiceCache;
    }


    protected LabelServiceCache getLabelServiceCache()
    {
        return labelServiceCache;
    }
}
