package de.hybris.platform.processengine.definition;

import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.task.RetryLaterException;

class JoinNode extends AbstractNode
{
    private final String then;


    JoinNode(String id, String then)
    {
        super(id);
        this.then = then;
        if(this.then == null || "".equals(this.then))
        {
            throw new IllegalArgumentException("Successor node id must not be null or empty.");
        }
    }


    public void trigger(BusinessProcessModel process)
    {
        throw new UnsupportedOperationException("Method not implemented yet.");
    }


    public String execute(BusinessProcessModel process) throws RetryLaterException, NodeExecutionException
    {
        throw new UnsupportedOperationException("Method not implemented yet.");
    }
}
