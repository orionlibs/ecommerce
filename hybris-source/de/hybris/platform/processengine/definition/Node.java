package de.hybris.platform.processengine.definition;

import de.hybris.platform.processengine.model.BusinessProcessModel;

public interface Node
{
    String getId();


    void trigger(BusinessProcessModel paramBusinessProcessModel);


    String execute(BusinessProcessModel paramBusinessProcessModel) throws NodeExecutionException;


    default boolean isExecutionContextRequired()
    {
        return false;
    }


    default String executeWithContext(NodeExecutionContext executionContext) throws NodeExecutionException
    {
        return execute(executionContext.getBusinessProcessModel());
    }


    static boolean isWaitNode(Node node)
    {
        return node instanceof WaitNode;
    }
}
