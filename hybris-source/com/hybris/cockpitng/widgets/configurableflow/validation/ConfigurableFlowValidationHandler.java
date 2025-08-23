/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow.validation;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.components.validation.ValidationFocusTransferHandler;
import com.hybris.cockpitng.config.jaxb.wizard.PropertyListType;
import com.hybris.cockpitng.config.jaxb.wizard.PropertyType;
import com.hybris.cockpitng.config.jaxb.wizard.StepType;
import com.hybris.cockpitng.config.jaxb.wizard.SubflowType;
import com.hybris.cockpitng.core.Focusable;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowController;
import com.hybris.cockpitng.widgets.configurableflow.renderer.ConfigurableFlowRenderer;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;

public class ConfigurableFlowValidationHandler implements ValidationFocusTransferHandler
{
    public static final String BREADCRUMB_DIV_ID = "breadcrumbDiv";
    private static final String SELECTOR_DIV_WITH_ID = String.format("%s[id^=\"%%s\"]", Div.class.getSimpleName());
    private static final String SELECTOR_EDITOR_WITH_PROPERTY = String.format("%s[%s=\"%%s\"]", Editor.class.getSimpleName(),
                    "property");
    private final ConfigurableFlowController controller;


    public ConfigurableFlowValidationHandler(final ConfigurableFlowController controller)
    {
        this.controller = controller;
    }


    @Override
    public int focusValidationPath(final Component parent, final String property)
    {
        final String anchor = ObjectValuePath.getNotLocalizedPath(property);
        Component component = findPropertyComponent(parent, anchor);
        if(component == null)
        {
            final Optional<Component> breadcrumbContainer = Selectors
                            .find(parent, String.format(SELECTOR_DIV_WITH_ID, BREADCRUMB_DIV_ID)).stream().findFirst();
            if(!breadcrumbContainer.isPresent())
            {
                return TRANSFER_ERROR_OTHER;
            }
            final Optional<StepType> stepTypeWithProperty = retrieveStepTypeWithAnchor(breadcrumbContainer.get(), anchor);
            if(!stepTypeWithProperty.isPresent())
            {
                return TRANSFER_ERROR_UNKNOWN_PATH;
            }
            controller.jumpToStep(stepTypeWithProperty.get());
            component = findPropertyComponent(parent, anchor);
        }
        if(component != null)
        {
            focusComponent(component, property, false);
        }
        return TRANSFER_SUCCESS;
    }


    private Optional<StepType> retrieveStepTypeWithAnchor(final Component breadcrumbContainer, final String property)
    {
        final StepType stepWithProperty = retrievePropertyStep(property);
        final String stepConfigurationId = null != stepWithProperty ? stepWithProperty.getId() : StringUtils.EMPTY;
        final Optional<Component> currentStepContainer = breadcrumbContainer.getChildren().stream().filter(entry -> StringUtils
                                        .equals(stepConfigurationId, (CharSequence)entry.getAttribute(ConfigurableFlowRenderer.STEP_ID_ATTRIBUTE)))
                        .findFirst();
        if(currentStepContainer.isPresent())
        {
            return Optional.ofNullable(stepWithProperty);
        }
        return Optional.empty();
    }


    /**
     * Retrieve {@link com.hybris.cockpitng.config.jaxb.wizard.StepType} for given property
     */
    protected StepType retrievePropertyStep(final String property)
    {
        if(controller.getFlowConfiguration() != null)
        {
            for(final Object stepOrSubflow : controller.getFlowConfiguration().getStepOrSubflow())
            {
                final StepType stepType = retrievePropertyStep(stepOrSubflow, property);
                if(stepType != null)
                {
                    return stepType;
                }
            }
        }
        return null;
    }


    protected StepType retrievePropertyStep(final Object stepOrSubflow, final String property)
    {
        if(stepOrSubflow instanceof StepType)
        {
            final StepType stepType = (StepType)stepOrSubflow;
            return retrieveStep(stepType, property);
        }
        else if(stepOrSubflow instanceof SubflowType)
        {
            return retrievePropertyStep(stepOrSubflow, property);
        }
        return null;
    }


    protected StepType retrieveStep(final StepType stepType, final String property)
    {
        if(stepType.getContent() != null)
        {
            for(final Object propertyOrPropertyListOrCustomView : stepType.getContent().getPropertyOrPropertyListOrCustomView())
            {
                if(propertyOrPropertyListOrCustomView instanceof PropertyType)
                {
                    final PropertyType propertyType = (PropertyType)propertyOrPropertyListOrCustomView;
                    if(StringUtils.equals(propertyType.getQualifier(), property))
                    {
                        return stepType;
                    }
                }
                else if(propertyOrPropertyListOrCustomView instanceof PropertyListType)
                {
                    final PropertyListType propertyListType = (PropertyListType)propertyOrPropertyListOrCustomView;
                    for(final PropertyType propertyType : propertyListType.getProperty())
                    {
                        if(StringUtils.equals(getFullQualifier((PropertyListType)propertyOrPropertyListOrCustomView, propertyType),
                                        property))
                        {
                            return stepType;
                        }
                    }
                }
            }
        }
        return null;
    }


    protected String getFullQualifier(final PropertyListType rootList, final PropertyType propertyType)
    {
        return new StringBuilder().append(rootList.getRoot()).append(".").append(propertyType.getQualifier()).toString();
    }


    protected Component findPropertyComponent(final Component comp, final String property)
    {
        return Selectors.find(comp, String.format(SELECTOR_EDITOR_WITH_PROPERTY, property)).stream().findFirst().orElse(null);
    }


    protected void focusComponent(Component component, final String property, final boolean queue)
    {
        Clients.scrollIntoView(component);
        if(component instanceof Editor)
        {
            component = ((Editor)component).getDefaultFocusComponent();
        }
        if(component instanceof Focusable)
        {
            ((Focusable)component).focus(property);
        }
        else if(component instanceof HtmlBasedComponent)
        {
            if(queue)
            {
                Events.echoEvent("focus", component, component);
            }
            else
            {
                ((HtmlBasedComponent)component).focus();
            }
        }
    }
}
