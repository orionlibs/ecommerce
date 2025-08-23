package de.hybris.platform.processengine.model;

import de.hybris.platform.processengine.definition.ProcessDefinitionFactory;
import de.hybris.platform.processengine.definition.ProcessDefinitionId;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.impl.BusinessProcessServiceDao;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class BusinessProcessProcessStateDynamicHandler extends AbstractDynamicAttributeHandler<ProcessState, BusinessProcessModel>
{
    private ProcessDefinitionFactory factory;
    private BusinessProcessServiceDao dao;


    public ProcessState get(BusinessProcessModel model)
    {
        List<String> actions = this.dao.findBusinessProcessTaskActions(model.getPk());
        if(CollectionUtils.isEmpty(actions))
        {
            return model.getState();
        }
        for(String action : actions)
        {
            if(this.factory.isProcessWaitingOnTask(getProcessDefinitionId(model), action))
            {
                return ProcessState.WAITING;
            }
        }
        return ProcessState.RUNNING;
    }


    private ProcessDefinitionId getProcessDefinitionId(BusinessProcessModel model)
    {
        return new ProcessDefinitionId(model.getProcessDefinitionName(), model.getProcessDefinitionVersion());
    }


    @Required
    public void setFactory(ProcessDefinitionFactory factory)
    {
        this.factory = factory;
    }


    @Required
    public void setDao(BusinessProcessServiceDao dao)
    {
        this.dao = dao;
    }
}
