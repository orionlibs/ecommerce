/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.config.jaxb.wizard.CancelType;
import com.hybris.cockpitng.config.jaxb.wizard.ContentType;
import com.hybris.cockpitng.config.jaxb.wizard.DoneType;
import com.hybris.cockpitng.config.jaxb.wizard.Flow;
import com.hybris.cockpitng.config.jaxb.wizard.InitializeType;
import com.hybris.cockpitng.config.jaxb.wizard.NavigationType;
import com.hybris.cockpitng.config.jaxb.wizard.PrepareType;
import com.hybris.cockpitng.config.jaxb.wizard.PropertyListType;
import com.hybris.cockpitng.config.jaxb.wizard.PropertyType;
import com.hybris.cockpitng.config.jaxb.wizard.SaveType;
import com.hybris.cockpitng.config.jaxb.wizard.StepType;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.editors.EditorUtils;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Fallback configuration strategy for wizards This strategy creates configuration for wizard with only one step, on
 * which all mandatory, writable fields are presented
 */
public class DefaultFlowConfigFallbackStrategy extends AbstractCockpitConfigurationFallbackStrategy<Flow>
{
    protected static final String ATTRIBUTE_TYPE = "type";
    protected static final String NEW_OBJECT_KEY = "newObject";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFlowConfigFallbackStrategy.class);
    private PermissionFacade permissionFacade;


    @Override
    public Flow loadFallbackConfiguration(final ConfigContext context, final Class<Flow> configurationType)
    {
        Validate.notNull("Cannot create configuration for null context", context);
        final Flow flow = new Flow();
        final String type = readType(context);
        setupWizardTitle(flow, type);
        flow.setPrepare(createPrepareSection(type));
        final StepType step1 = createFirstStep();
        addAllMandatoryFieldsToStep(step1, type);
        flow.getStepOrSubflow().add(step1);
        return flow;
    }


    protected PrepareType createPrepareSection(final String type)
    {
        final PrepareType prepareSection = new PrepareType();
        final InitializeType objectToCreate = new InitializeType();
        objectToCreate.setProperty(NEW_OBJECT_KEY);
        objectToCreate.setType(type);
        prepareSection.getInitialize().add(objectToCreate);
        return prepareSection;
    }


    protected void setupWizardTitle(final Flow flow, final String type)
    {
        flow.setTitle("flow.general.create(" + type + ")");
    }


    protected StepType createFirstStep()
    {
        final StepType step1 = new StepType();
        step1.setLabel("flow.allmanadatory");
        final ContentType content = new ContentType();
        step1.setContent(content);
        final NavigationType navigation = new NavigationType();
        final DoneType doneAction = new DoneType();
        final SaveType save = new SaveType();
        save.setProperty(NEW_OBJECT_KEY);
        doneAction.getSave().add(save);
        navigation.setDone(doneAction);
        final CancelType cancel = new CancelType();
        navigation.setCancel(cancel);
        step1.setNavigation(navigation);
        return step1;
    }


    protected String readType(final ConfigContext context)
    {
        final String type = context.getAttribute("type");
        if(StringUtils.isBlank(type))
        {
            throw new IllegalStateException("Configuration context does not contain 'type' attribute");
        }
        return type;
    }


    protected void addAllMandatoryFieldsToStep(final StepType step1, final String type)
    {
        try
        {
            final DataType dataType = loadType(type);
            final PropertyListType propertyListType = new PropertyListType();
            propertyListType.setRoot(NEW_OBJECT_KEY);
            if(step1.getContent() != null)
            {
                step1.getContent().getPropertyOrPropertyListOrCustomView().add(propertyListType);
            }
            final Set<String> displayedAttributes = getMandatoryAttributes(type);
            displayedAttributes.addAll(getAttributes(type, INITIAL, UNIQUE));
            for(final String qualifier : displayedAttributes)
            {
                final Optional<PropertyType> propertyType = prepareMandatoryAttribute(dataType, qualifier);
                propertyType.ifPresent(propertyListType.getProperty()::add);
            }
            if(CollectionUtils.isEmpty(propertyListType.getProperty()))
            {
                step1.setLabel("flow.nomandatoryfields");
            }
        }
        catch(final TypeNotFoundException e)
        {
            LOG.error("Cannot create fallback configuration for wizard: " + e.getMessage(), e);
        }
    }


    protected Optional<PropertyType> prepareMandatoryAttribute(final DataType dataType, final String qualifier)
    {
        if(!getPermissionFacade().canChangeProperty(dataType.getCode(), qualifier))
        {
            return Optional.empty();
        }
        final DataAttribute attribute = dataType.getAttribute(qualifier);
        if(attribute.isWritable() || attribute.isWritableOnCreation())
        {
            final PropertyType property = new PropertyType();
            property.setQualifier(qualifier);
            property.setType(EditorUtils.getEditorType(attribute));
            return Optional.of(property);
        }
        return Optional.empty();
    }


    public PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    @Required
    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }
}
