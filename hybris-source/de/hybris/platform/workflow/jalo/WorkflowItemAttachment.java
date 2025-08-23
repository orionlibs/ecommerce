package de.hybris.platform.workflow.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.JaloPropertyContainer;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class WorkflowItemAttachment extends GeneratedWorkflowItemAttachment
{
    private static final Logger LOG = Logger.getLogger(WorkflowItemAttachment.class.getName());


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(allAttributes.get("code") == null)
        {
            allAttributes.put("code", WorkflowManager.getInstance().getNextAttachmentNumber());
        }
        return super.createItem(ctx, type, allAttributes);
    }


    @ForceJALO(reason = "something else")
    protected JaloPropertyContainer getInitialProperties(JaloSession jaloSession, Item.ItemAttributeMap allAttributes)
    {
        JaloPropertyContainer props = jaloSession.createPropertyContainer();
        props.setProperty("code", allAttributes.get("code"));
        return props;
    }


    @ForceJALO(reason = "something else")
    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap ret = super.getNonInitialAttributes(ctx, allAttributes);
        ret.remove("code");
        return ret;
    }


    @ForceJALO(reason = "something else")
    public ComposedType getTypeOfItem(SessionContext ctx)
    {
        ComposedType type = (ComposedType)getProperty(ctx, "typeOfItem");
        if(type != null)
        {
            return type;
        }
        type = getItem(ctx).getComposedType();
        if(type != null)
        {
            setTypeOfItem(type);
            LOG.debug("Set type of item" + type.toString());
        }
        return type;
    }


    @SLDSafe
    public void setTypeOfItem(SessionContext ctx, ComposedType value)
    {
        setProperty(ctx, "typeOfItem", value);
    }


    public void setTypeOfItem(ComposedType value)
    {
        setProperty(getSession().getSessionContext(), "typeOfItem", value);
    }


    @ForceJALO(reason = "something else")
    public void setItem(SessionContext ctx, Item value)
    {
        if(value != null && value.getComposedType() != null)
        {
            setTypeOfItem(value.getComposedType());
        }
        super.setItem(ctx, value);
    }


    @ForceJALO(reason = "abstract method implementation")
    public String getActionStr(SessionContext ctx)
    {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for(WorkflowAction action : getActions(ctx))
        {
            if(first)
            {
                first = false;
            }
            else
            {
                builder.append(", ");
            }
            builder.append(action.getName());
        }
        return builder.toString();
    }
}
