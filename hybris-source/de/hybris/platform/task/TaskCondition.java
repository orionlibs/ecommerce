package de.hybris.platform.task;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Date;

public class TaskCondition extends GeneratedTaskCondition
{
    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        allAttributes.setAttributeMode("uniqueID", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("task", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("expirationTimeMillis", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("fulfilled", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("consumed", Item.AttributeMode.INITIAL);
        if(allAttributes.get("uniqueID") == null)
        {
            throw new JaloInvalidParameterException("missing uniqueID for creating a new " + type.getCode(), 0);
        }
        allAttributes.put("expirationTimeMillis", (allAttributes.get("expirationTimeMillis") != null) ?
                        allAttributes.get("expirationTimeMillis") : null);
        if(allAttributes.get("fulfilled") == null)
        {
            allAttributes.put("fulfilled", Boolean.FALSE);
        }
        if(allAttributes.get("consumed") == null)
        {
            allAttributes.put("consumed", Boolean.FALSE);
        }
        return super.createItem(ctx, type, allAttributes);
    }


    public Date getExpirationDate()
    {
        return getExpirationDate(getSession().getSessionContext());
    }


    public Date getExpirationDate(SessionContext ctx)
    {
        Long milliSeconds = getExpirationTimeMillis(ctx);
        return (milliSeconds != null) ? new Date(milliSeconds.longValue()) : null;
    }


    public void setExpirationDate(Date value)
    {
        setExpirationDate(getSession().getSessionContext(), value);
    }


    public void setExpirationDate(SessionContext ctx, Date value)
    {
        setExpirationTimeMillis(ctx, (value != null) ? Long.valueOf(value.getTime()) : null);
    }
}
