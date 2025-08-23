package de.hybris.platform.processengine.helpers.impl;

import de.hybris.platform.processengine.definition.ProcessDefinition;
import de.hybris.platform.processengine.definition.ProcessDefinitionFactory;
import de.hybris.platform.processengine.definition.ProcessDefinitionId;
import de.hybris.platform.processengine.helpers.ProcessFactory;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.apache.log4j.Logger;

public class DefaultProcessFactory implements ProcessFactory
{
    private static final Logger LOG = Logger.getLogger(DefaultProcessFactory.class);
    private ProcessDefinitionFactory processDefinitionFactory;


    public <T extends BusinessProcessModel> T createProcessModel(String code, String processDefinitionName, Map<String, Object> contextParameter)
    {
        BusinessProcessModel process;
        Class<?> classDefinition;
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Creating process with code %s from definition %s", new Object[] {code, processDefinitionName}));
        }
        ProcessDefinitionId latestVersion = new ProcessDefinitionId(processDefinitionName);
        ProcessDefinition processDefinition = this.processDefinitionFactory.getProcessDefinition(latestVersion);
        try
        {
            classDefinition = Class.forName(processDefinition.getProcessClass());
        }
        catch(ClassNotFoundException e)
        {
            RuntimeException exc = new RuntimeException(e);
            throw exc;
        }
        try
        {
            process = (BusinessProcessModel)classDefinition.newInstance();
        }
        catch(InstantiationException e)
        {
            RuntimeException exc = new RuntimeException(e);
            throw exc;
        }
        catch(IllegalAccessException e)
        {
            RuntimeException exc = new RuntimeException(e);
            throw exc;
        }
        process.setCode(code);
        process.setProcessDefinitionName(processDefinitionName);
        if(contextParameter != null)
        {
            Collection<BusinessProcessParameterModel> processParameterModelCol = new ArrayList<>();
            for(Map.Entry<String, Object> contextParameterItem : contextParameter.entrySet())
            {
                BusinessProcessParameterModel processParameterModel = new BusinessProcessParameterModel();
                processParameterModel.setName(contextParameterItem.getKey());
                processParameterModel.setProcess(process);
                processParameterModel.setValue(contextParameterItem.getValue());
                processParameterModelCol.add(processParameterModel);
            }
            process.setContextParameters(processParameterModelCol);
        }
        return (T)process;
    }


    public void setProcessDefinitionFactory(ProcessDefinitionFactory processDefinitionFactory)
    {
        this.processDefinitionFactory = processDefinitionFactory;
    }


    public ProcessDefinitionFactory getProcessDefinitionFactory()
    {
        return this.processDefinitionFactory;
    }
}
