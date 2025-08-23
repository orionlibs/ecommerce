package de.hybris.platform.processengine.helpers.impl;

import de.hybris.platform.processengine.helpers.ProcessParameterHelper;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultProcessParameterHelper implements ProcessParameterHelper
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultProcessParameterHelper.class);
    private ModelService modelService;


    public BusinessProcessParameterModel getProcessParameterByName(String parameterName, Collection<BusinessProcessParameterModel> parameters)
    {
        if(parameters != null)
        {
            for(BusinessProcessParameterModel processengineProcessParameterModel : parameters)
            {
                if(StringUtils.equals(processengineProcessParameterModel.getName(), parameterName))
                {
                    return processengineProcessParameterModel;
                }
            }
        }
        LOG.warn("Process parameter with name {} does not exist.", parameterName);
        return null;
    }


    public Set<String> getAllParameterNames(BusinessProcessModel processModel)
    {
        if(processModel.getContextParameters() == null)
        {
            return Collections.emptySet();
        }
        Set<String> parameterNames = new HashSet<>();
        for(BusinessProcessParameterModel processengineProcessParameterModel : processModel.getContextParameters())
        {
            parameterNames.add(processengineProcessParameterModel.getName());
        }
        return parameterNames;
    }


    public BusinessProcessParameterModel getProcessParameterByName(BusinessProcessModel processModel, String parameterName)
    {
        return getProcessParameterByName(parameterName, processModel.getContextParameters());
    }


    public boolean containsParameter(BusinessProcessModel process, String parameterName)
    {
        if(process.getContextParameters() != null)
        {
            for(BusinessProcessParameterModel processengineProcessParameterModel : process.getContextParameters())
            {
                if(StringUtils.equals(processengineProcessParameterModel.getName(), parameterName))
                {
                    return true;
                }
            }
        }
        return false;
    }


    public void setProcessParameter(BusinessProcessModel process, String parameterName, Object value)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Setting parameter: {} of the process with code: {} to value: {}", new Object[] {parameterName, process.getCode(), value});
        }
        if(containsParameter(process, parameterName))
        {
            BusinessProcessParameterModel parameter = getProcessParameterByName(process, parameterName);
            parameter.setValue(value);
            this.modelService.save(parameter);
        }
        else
        {
            BusinessProcessParameterModel parameter = (BusinessProcessParameterModel)this.modelService.create(BusinessProcessParameterModel.class);
            parameter.setProcess(process);
            parameter.setName(parameterName);
            parameter.setValue(value);
            this.modelService.save(parameter);
        }
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }
}
