package de.hybris.platform.processengine.definition;

import de.hybris.platform.processengine.model.BusinessProcessModel;
import java.util.Set;
import org.apache.log4j.Logger;

class SplitNode extends AbstractNode
{
    private static final Logger LOG = Logger.getLogger(SplitNode.class);
    private final Set<String> successors;


    public Set<String> getSuccessors()
    {
        return this.successors;
    }


    SplitNode(String id, Set<String> successors)
    {
        super(id);
        this.successors = successors;
        if(successors.isEmpty())
        {
            throw new IllegalArgumentException("Successors of split node '" + id + "' must not be empty.");
        }
    }


    public void trigger(BusinessProcessModel process)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Splitting node with ID %s reached for process with code %s. Sucessors: %s", new Object[] {getId(), process
                            .getCode(), this.successors}));
        }
        for(String action : this.successors)
        {
            Node node = getProcessDefinition(ProcessDefinitionId.of(process)).retrieve(action);
            node.trigger(process);
        }
    }
}
