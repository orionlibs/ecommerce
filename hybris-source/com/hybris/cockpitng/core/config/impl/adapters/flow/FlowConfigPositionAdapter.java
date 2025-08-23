/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.adapters.flow;

import com.hybris.cockpitng.config.jaxb.wizard.AbstractFlowType;
import com.hybris.cockpitng.config.jaxb.wizard.ContentType;
import com.hybris.cockpitng.config.jaxb.wizard.Flow;
import com.hybris.cockpitng.config.jaxb.wizard.PropertyListType;
import com.hybris.cockpitng.config.jaxb.wizard.PropertyType;
import com.hybris.cockpitng.config.jaxb.wizard.StepType;
import com.hybris.cockpitng.config.jaxb.wizard.SubflowType;
import com.hybris.cockpitng.config.jaxb.wizard.ViewType;
import com.hybris.cockpitng.core.config.CockpitConfigurationAdapter;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import com.hybris.cockpitng.dataaccess.services.PositionedSort;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Configuration adapter for Flow. It is responsible for proper order of steps, subflows, and elements inside ContentType.
 */
public class FlowConfigPositionAdapter implements CockpitConfigurationAdapter<Flow>
{
    private PositionedSort<Positioned> positionedSort;


    @Required
    public void setPositionedSort(final PositionedSort<Positioned> positionedSort)
    {
        this.positionedSort = positionedSort;
    }


    @Override
    public Class<Flow> getSupportedType()
    {
        return Flow.class;
    }


    @Override
    public Flow adaptAfterLoad(final ConfigContext context, final Flow flow) throws CockpitConfigurationException
    {
        sortFlow(flow);
        return flow;
    }


    @Override
    public Flow adaptBeforeStore(final ConfigContext context, final Flow flow) throws CockpitConfigurationException
    {
        return flow;
    }


    protected void sortFlow(final AbstractFlowType flow)
    {
        final List<Object> stepOrSubflowCollection = flow.getStepOrSubflow();
        sortObjectCollection(stepOrSubflowCollection);
        for(final Object stepOrSubflow : flow.getStepOrSubflow())
        {
            if(stepOrSubflow instanceof SubflowType)
            {
                sortFlow((SubflowType)stepOrSubflow);
            }
            else if(stepOrSubflow instanceof StepType)
            {
                final StepType step = (StepType)stepOrSubflow;
                if(step.getContent() != null)
                {
                    sortContentType(step.getContent());
                }
            }
        }
    }


    protected void sortContentType(final ContentType contentType)
    {
        if(CollectionUtils.isNotEmpty(contentType.getColumn()))
        {
            sortObjectCollection(contentType.getColumn());
            for(final ContentType column : contentType.getColumn())
            {
                sortContentType(column);
            }
        }
        if(CollectionUtils.isNotEmpty(contentType.getPropertyOrPropertyListOrCustomView()))
        {
            sortObjectCollection(contentType.getPropertyOrPropertyListOrCustomView());
            for(final Object potentialPropertyList : contentType.getPropertyOrPropertyListOrCustomView())
            {
                if(potentialPropertyList instanceof PropertyListType)
                {
                    sortObjectCollection(((PropertyListType)potentialPropertyList).getProperty());
                }
            }
        }
    }


    protected <T> void sortObjectCollection(final List<T> inputList)
    {
        if(CollectionUtils.isNotEmpty(inputList))
        {
            final List<Positioned> listToSort = new ArrayList<>();
            for(final T obj : inputList)
            {
                listToSort.add(createPositionedObject(obj));
            }
            positionedSort.sort(listToSort);
            inputList.clear();
            for(final Positioned wrapper : listToSort)
            {
                final PositionAware<T> positionAware = (PositionAware)wrapper;
                inputList.add(positionAware.getObject());
            }
        }
    }


    protected <T> PositionAware<T> createPositionedObject(final T object)
    {
        if(object instanceof StepType)
        {
            return new PositionAware<>(object, ((StepType)object).getPosition());
        }
        else if(object instanceof SubflowType)
        {
            return new PositionAware<>(object, ((SubflowType)object).getPosition());
        }
        else if(object instanceof ContentType)
        {
            return new PositionAware<>(object, ((ContentType)object).getPosition());
        }
        else if(object instanceof PropertyType)
        {
            return new PositionAware<>(object, ((PropertyType)object).getPosition());
        }
        else if(object instanceof PropertyListType)
        {
            return new PositionAware<>(object, ((PropertyListType)object).getPosition());
        }
        else if(object instanceof ViewType)
        {
            return new PositionAware<>(object, ((ViewType)object).getPosition());
        }
        else
        {
            return new PositionAware(object, null);
        }
    }
}
