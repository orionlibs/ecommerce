/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.dynamicforms.impl;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.exception.CyclicDynamicFormsException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.Context;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicAttribute;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicForms;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.dynamicforms.ComponentsVisitor;
import com.hybris.cockpitng.widgets.common.dynamicforms.ComponentsVisitorFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Factory which creates {@link ComponentsVisitor}
 */
public class DefaultComponentsVisitorFactory implements ComponentsVisitorFactory
{
    public static final String SETTING_DYNAMIC_FORMS_COMPONENT_NAME = "dynamicForms.componentName";
    private static final String NOTIFICATION_DYNAMIC_FORM_VISITOR = "DynamicFormVisitor";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultComponentsVisitorFactory.class);
    private String componentName;
    private ComponentsVisitor componentsVisitor;
    private ComponentsVisitor componentsDisablingVisitor;
    private NotificationService notificationService;


    @Override
    public ComponentsVisitor createVisitor(final String typeCode, final WidgetInstanceManager wim)
    {
        if(typeCode != null && wim != null)
        {
            final String componentNameWithFallback = getComponentNameWithFallback(wim);
            try
            {
                final DynamicForms dynamicForms = loadDynamicFormsConfiguration(typeCode, componentNameWithFallback, wim);
                if(areDynamicFormsConfigured(dynamicForms))
                {
                    LOG.debug("Initializing visitors for type:{} and componentName:{}", typeCode, componentNameWithFallback);
                    getComponentsVisitor().initialize(typeCode, wim, dynamicForms);
                    return getComponentsVisitor();
                }
            }
            catch(final CyclicDynamicFormsException ex)
            {
                LOG.error(ex.getMessage());
                getNotificationService().notifyUser(getNotificationSource(wim), NOTIFICATION_DYNAMIC_FORM_VISITOR,
                                NotificationEvent.Level.FAILURE, ex);
                return getComponentsDisablingVisitor();
            }
        }
        else
        {
            LOG.warn("Cannot create visitor with type:{} and widgetInstanceManager:{}", typeCode, wim);
        }
        return ComponentsVisitor.NULL;
    }


    /**
     * @deprecated since 6.7, use
     *             {@link NotificationService#getWidgetNotificationSource(com.hybris.cockpitng.engine.WidgetInstanceManager)}
     *             instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected String getNotificationSource(final WidgetInstanceManager wim)
    {
        return getNotificationService().getWidgetNotificationSource(wim) + "-dynamicForms";
    }


    /**
     * Loads dynamicForms configuration.
     *
     * @throws CyclicDynamicFormsException
     *            is thrown when {@link DynamicAttribute} in
     *            {@link com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicForms} have cyclic references.
     */
    protected DynamicForms loadDynamicFormsConfiguration(final String typeCode, final String componentName,
                    final WidgetInstanceManager wim)
    {
        final DefaultConfigContext configContext = new DefaultConfigContext(componentName);
        configContext.setType(typeCode);
        try
        {
            return wim.loadConfiguration(configContext, DynamicForms.class);
        }
        catch(final CockpitConfigurationException cce)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("Cannot find configuration for type=%s and component=%s", typeCode, componentName), cce);
            }
        }
        return null;
    }


    /**
     * Checks whether loaded config for dynamic forms has any: {@link DynamicForms#getAttribute()},
     * {@link DynamicForms#getSection()},{@link DynamicForms#getTab()} for current type
     *
     * @return true if dynamic forms are configured
     */
    protected boolean areDynamicFormsConfigured(final DynamicForms dynamicForms)
    {
        if(dynamicForms != null)
        {
            return CollectionUtils.isNotEmpty(dynamicForms.getAttribute()) || CollectionUtils.isNotEmpty(dynamicForms.getSection())
                            || CollectionUtils.isNotEmpty(dynamicForms.getTab()) || CollectionUtils.isNotEmpty(dynamicForms.getVisitor());
        }
        return false;
    }


    /**
     * Gets componentName used to extract dynamic forms configuration {@link Context#component}. By default the value is
     * taken from {@link DefaultComponentsVisitorFactory#componentName} but it can be overridden for a widget with a setting
     * {@link #SETTING_DYNAMIC_FORMS_COMPONENT_NAME}.
     *
     * @return component name
     */
    protected String getComponentNameWithFallback(final WidgetInstanceManager wim)
    {
        if(wim.getWidgetSettings() != null)
        {
            final String overriddenComponentName = wim.getWidgetSettings().getString(SETTING_DYNAMIC_FORMS_COMPONENT_NAME);
            if(StringUtils.isNotEmpty(overriddenComponentName))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("Component name:%s for dynamic forms is overridden with:%s", getComponentName(),
                                    overriddenComponentName));
                }
                return overriddenComponentName;
            }
        }
        return getComponentName();
    }


    protected String getComponentName()
    {
        return componentName;
    }


    @Required
    public void setComponentName(final String componentName)
    {
        this.componentName = componentName;
    }


    protected ComponentsVisitor getComponentsVisitor()
    {
        return componentsVisitor;
    }


    @Required
    public void setComponentsVisitor(final ComponentsVisitor componentsVisitor)
    {
        this.componentsVisitor = componentsVisitor;
    }


    protected ComponentsVisitor getComponentsDisablingVisitor()
    {
        return componentsDisablingVisitor;
    }


    @Required
    public void setComponentsDisablingVisitor(final ComponentsVisitor componentsDisablingVisitor)
    {
        this.componentsDisablingVisitor = componentsDisablingVisitor;
    }


    protected NotificationService getNotificationService()
    {
        return notificationService;
    }


    @Required
    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }
}
