package de.hybris.platform.workflow.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

public abstract class AbstractWorkflowDecision extends GeneratedAbstractWorkflowDecision
{
    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(allAttributes.get("code") == null)
        {
            allAttributes.put("code", WorkflowManager.getInstance().getNextDecisionNumber());
        }
        return super.createItem(ctx, type, allAttributes);
    }
}
