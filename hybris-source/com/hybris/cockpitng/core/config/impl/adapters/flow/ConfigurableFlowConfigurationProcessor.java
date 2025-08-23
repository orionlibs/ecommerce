/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.adapters.flow;

import com.hybris.cockpitng.config.jaxb.wizard.ContentType;
import com.hybris.cockpitng.config.jaxb.wizard.Flow;
import com.hybris.cockpitng.config.jaxb.wizard.PropertyListType;
import com.hybris.cockpitng.config.jaxb.wizard.PropertyType;
import com.hybris.cockpitng.config.jaxb.wizard.StepType;
import com.hybris.cockpitng.config.jaxb.wizard.SubflowType;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.editors.EditorUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ConfigurableFlowConfigurationProcessor
{
    private TypeFacade typeFacade;
    private PermissionFacade permissionFacade;


    public void applyNonDeclaredProperties(final Flow flowConfiguration, final String typeCode)
    {
        for(final Object stepOrSubflow : flowConfiguration.getStepOrSubflow())
        {
            applyNonDeclaredProperties(flowConfiguration, typeCode, stepOrSubflow);
        }
    }


    protected void applyNonDeclaredProperties(final Flow flowConfiguration, final String typeCode, final Object stepOrSubflow)
    {
        if(stepOrSubflow instanceof StepType)
        {
            final StepType stepType = (StepType)stepOrSubflow;
            if(Objects.nonNull(stepType.getContent()))
            {
                applyNonDeclaredProperties(flowConfiguration, typeCode, stepType.getContent());
            }
        }
        else if(stepOrSubflow instanceof SubflowType)
        {
            final SubflowType subflowType = (SubflowType)stepOrSubflow;
            for(final Object nestedSubflowType : subflowType.getStepOrSubflow())
            {
                applyNonDeclaredProperties(flowConfiguration, typeCode, nestedSubflowType);
            }
        }
    }


    protected void applyNonDeclaredProperties(final Flow flowConfiguration, final String typeCode, final ContentType contentType)
    {
        if(Objects.nonNull(contentType.getPropertyOrPropertyListOrCustomView()))
        {
            contentType.getPropertyOrPropertyListOrCustomView().stream()
                            .filter(propertyOrPropertyListOrCustomView -> propertyOrPropertyListOrCustomView instanceof PropertyListType)
                            .forEach(propertyOrPropertyListOrCustomView -> {
                                final PropertyListType propertyListType = (PropertyListType)propertyOrPropertyListOrCustomView;
                                if(propertyListType.isEnableNonDeclaredIncludes())
                                {
                                    propertyListType.getProperty()
                                                    .addAll(retrieveMissingProperties(flowConfiguration, typeCode, propertyListType));
                                }
                            });
        }
        if(Objects.nonNull(contentType.getColumn()))
        {
            for(final ContentType nestedContentType : contentType.getColumn())
            {
                applyNonDeclaredProperties(flowConfiguration, typeCode, nestedContentType);
            }
        }
    }


    protected Set<PropertyType> retrieveMissingProperties(final Flow flowConfiguration, final String typeCode,
                    final PropertyListType propertyListType)
    {
        final Set<PropertyType> missingAttributes = new LinkedHashSet<>();
        final Set<DataAttribute> dataAttributes = resolveMissingProperties(flowConfiguration, typeCode, propertyListType);
        dataAttributes.stream().filter(attribute -> getPermissionFacade().canChangeProperty(typeCode, attribute.getQualifier()))
                        .filter(attribute -> attribute.isWritable() || attribute.isWritableOnCreation()).forEach(attribute -> {
                            final PropertyType property = new PropertyType();
                            property.setQualifier(attribute.getQualifier());
                            property.setType(EditorUtils.getEditorType(attribute));
                            missingAttributes.add(property);
                        });
        return missingAttributes;
    }


    protected Set<DataAttribute> resolveMissingProperties(final Flow flowConfiguration, final String typeCode,
                    final PropertyListType propertyListType)
    {
        final Set<DataAttribute> result;
        try
        {
            final DataType loadedType = loadType(typeCode);
            result = Modifier.getDataAttributesForAllPossibleModifiers(propertyListType, loadedType);
        }
        catch(final TypeNotFoundException e)
        {
            throw new IllegalStateException(e);
        }
        for(final Object stepOrSubflow : flowConfiguration.getStepOrSubflow())
        {
            filterMissingProperties(stepOrSubflow, result);
        }
        return result;
    }


    protected void filterMissingProperties(final Object stepOrSubflow, final Set<DataAttribute> attributes)
    {
        if(stepOrSubflow instanceof StepType)
        {
            final StepType step = (StepType)stepOrSubflow;
            if(step.getContent() != null)
            {
                for(final Object propertyOrPropertyListOrCustomView : step.getContent().getPropertyOrPropertyListOrCustomView())
                {
                    if(propertyOrPropertyListOrCustomView instanceof PropertyType)
                    {
                        final PropertyType property = (PropertyType)propertyOrPropertyListOrCustomView;
                        filterQualifiers(attributes, property);
                    }
                    else if(propertyOrPropertyListOrCustomView instanceof PropertyListType)
                    {
                        final PropertyListType propertyList = (PropertyListType)propertyOrPropertyListOrCustomView;
                        for(final PropertyType property : propertyList.getProperty())
                        {
                            filterQualifiers(attributes, property);
                        }
                    }
                }
            }
        }
        else if(stepOrSubflow instanceof SubflowType)
        {
            final SubflowType subflowType = (SubflowType)stepOrSubflow;
            for(final Object nestedStepOrSubflow : subflowType.getStepOrSubflow())
            {
                filterMissingProperties(nestedStepOrSubflow, attributes);
            }
        }
    }


    protected void filterQualifiers(final Set<DataAttribute> attributes, final PropertyType property)
    {
        final Iterator<DataAttribute> dataAttributeIterator = attributes.iterator();
        while(dataAttributeIterator.hasNext())
        {
            final DataAttribute dataAttribute = dataAttributeIterator.next();
            if(dataAttribute.getQualifier().equals(property.getQualifier()))
            {
                dataAttributeIterator.remove();
                break;
            }
        }
    }


    public void processPropertiesToExclude(final Flow flowConfiguration)
    {
        flowConfiguration.getStepOrSubflow().forEach(this::processPropertiesToExclude);
    }


    protected void processPropertiesToExclude(final Object stepOrSubflow)
    {
        if(stepOrSubflow instanceof StepType)
        {
            final StepType stepType = (StepType)stepOrSubflow;
            if(Objects.nonNull(stepType.getContent()))
            {
                processPropertiesToExclude(stepType.getContent());
            }
        }
        else if(stepOrSubflow instanceof SubflowType)
        {
            final SubflowType subflowType = (SubflowType)stepOrSubflow;
            subflowType.getStepOrSubflow().forEach(this::processPropertiesToExclude);
        }
    }


    protected void processPropertiesToExclude(final ContentType contentType)
    {
        if(Objects.nonNull(contentType.getPropertyOrPropertyListOrCustomView())
                        && !contentType.getPropertyOrPropertyListOrCustomView().isEmpty())
        {
            final Iterator<Object> propertyOrPropertyListOrCustomViewIterator = contentType.getPropertyOrPropertyListOrCustomView()
                            .iterator();
            while(propertyOrPropertyListOrCustomViewIterator.hasNext())
            {
                final Object propertyOrPropertyListOrCustomView = propertyOrPropertyListOrCustomViewIterator.next();
                if(propertyOrPropertyListOrCustomView instanceof PropertyType)
                {
                    final PropertyType propertyType = (PropertyType)propertyOrPropertyListOrCustomView;
                    if(propertyType.isExclude())
                    {
                        propertyOrPropertyListOrCustomViewIterator.remove();
                    }
                }
                else if(propertyOrPropertyListOrCustomView instanceof PropertyListType)
                {
                    final PropertyListType propertyListType = (PropertyListType)propertyOrPropertyListOrCustomView;
                    final Iterator<PropertyType> propertyTypeIterator = propertyListType.getProperty().iterator();
                    while(propertyTypeIterator.hasNext())
                    {
                        final PropertyType propertyType = propertyTypeIterator.next();
                        if(propertyType.isExclude())
                        {
                            propertyTypeIterator.remove();
                        }
                    }
                }
            }
        }
        if(Objects.nonNull(contentType.getColumn()) && !contentType.getColumn().isEmpty())
        {
            contentType.getColumn().forEach(this::processPropertiesToExclude);
        }
    }


    public DataType loadType(final String type) throws TypeNotFoundException
    {
        return getTypeFacade().load(type);
    }


    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    public PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    protected enum Modifier
    {
        MANDATORY(DataAttribute::isMandatory, PropertyListType::isIncludeNonDeclaredMandatory), UNIQUE(DataAttribute::isUnique,
                    PropertyListType::isIncludeNonDeclaredUnique), INITIAL(DataAttribute::isWritableOnCreation,
                    PropertyListType::isIncludeNonDeclaredWritableOnCreation);
        private final Predicate<DataAttribute> dataAttributePredicate;
        private final Predicate<PropertyListType> propertyListTypePredicate;


        Modifier(final Predicate<DataAttribute> dataAttributePredicate, final Predicate<PropertyListType> propertyListTypePredicate)
        {
            this.dataAttributePredicate = dataAttributePredicate;
            this.propertyListTypePredicate = propertyListTypePredicate;
        }


        public boolean checkAttributeMatchesToConfig(final DataAttribute dataAttribute, final PropertyListType propertyListType)
        {
            return propertyListTypePredicate.test(propertyListType) && dataAttributePredicate.test(dataAttribute);
        }


        public static Set<DataAttribute> getDataAttributesForAllPossibleModifiers(final PropertyListType propertyListType,
                        final DataType loadedType)
        {
            final Collection<DataAttribute> attributes = loadedType.getAttributes();
            return attributes.stream()
                            .filter(dataAttribute -> Arrays.stream(Modifier.values())
                                            .anyMatch(modifier -> modifier.checkAttributeMatchesToConfig(dataAttribute, propertyListType)))
                            .collect(Collectors.toCollection(LinkedHashSet::new));//
        }
    }
}
