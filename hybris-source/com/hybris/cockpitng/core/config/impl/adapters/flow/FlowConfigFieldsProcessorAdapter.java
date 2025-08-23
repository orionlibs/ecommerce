/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.adapters.flow;

import com.hybris.cockpitng.config.jaxb.wizard.Flow;
import com.hybris.cockpitng.core.config.CockpitConfigurationAdapter;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import org.springframework.beans.factory.annotation.Required;

public class FlowConfigFieldsProcessorAdapter implements CockpitConfigurationAdapter<Flow>
{
    protected ConfigurableFlowConfigurationProcessor configurableFlowConfigurationProcessor;


    @Override
    public Class<Flow> getSupportedType()
    {
        return Flow.class;
    }


    @Override
    public Flow adaptAfterLoad(final ConfigContext context, final Flow flow) throws CockpitConfigurationException
    {
        final String typeCode = context.getAttribute("type");
        if(typeCode.isEmpty())
        {
            return flow;
        }
        getConfigurableFlowConfigurationProcessor().applyNonDeclaredProperties(flow, typeCode);
        getConfigurableFlowConfigurationProcessor().processPropertiesToExclude(flow);
        return flow;
    }


    @Override
    public Flow adaptBeforeStore(final ConfigContext context, final Flow flow) throws CockpitConfigurationException
    {
        return flow;
    }


    public ConfigurableFlowConfigurationProcessor getConfigurableFlowConfigurationProcessor()
    {
        return configurableFlowConfigurationProcessor;
    }


    @Required
    public void setConfigurableFlowConfigurationProcessor(final ConfigurableFlowConfigurationProcessor configurableFlowConfigurationProcessor)
    {
        this.configurableFlowConfigurationProcessor = configurableFlowConfigurationProcessor;
    }
}
