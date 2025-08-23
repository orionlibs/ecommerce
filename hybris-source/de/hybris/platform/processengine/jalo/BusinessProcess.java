package de.hybris.platform.processengine.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.processengine.constants.GeneratedProcessengineConstants;
import de.hybris.platform.processengine.definition.ProcessDefinitionFactory;
import de.hybris.platform.processengine.definition.ProcessDefinitionId;
import de.hybris.platform.processengine.impl.BusinessProcessServiceDao;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

public class BusinessProcess extends GeneratedBusinessProcess
{
    public EnumerationValue getProcessState(SessionContext ctx)
    {
        List<String> actions = getBusinessProcessServiceDao().findBusinessProcessTaskActions(getPK());
        if(CollectionUtils.isEmpty(actions))
        {
            return getState(ctx);
        }
        ProcessDefinitionFactory factory = getProcessDefinitionFactory();
        for(String action : actions)
        {
            if(factory.isProcessWaitingOnTask(getProcessDefinitionId(), action))
            {
                return getProcessState(GeneratedProcessengineConstants.Enumerations.ProcessState.WAITING);
            }
        }
        return getProcessState(GeneratedProcessengineConstants.Enumerations.ProcessState.RUNNING);
    }


    private ProcessDefinitionFactory getProcessDefinitionFactory()
    {
        return (ProcessDefinitionFactory)Registry.getApplicationContext().getBean("processDefinitionFactory", ProcessDefinitionFactory.class);
    }


    private BusinessProcessServiceDao getBusinessProcessServiceDao()
    {
        return (BusinessProcessServiceDao)Registry.getApplicationContext().getBean("businessProcessServiceDao", BusinessProcessServiceDao.class);
    }


    private ProcessDefinitionId getProcessDefinitionId()
    {
        return new ProcessDefinitionId(getProcessDefinitionName(), getProcessDefinitionVersion());
    }


    private EnumerationValue getProcessState(String state)
    {
        return EnumerationManager.getInstance().getEnumerationValue(GeneratedProcessengineConstants.TC.PROCESSSTATE, state);
    }
}
