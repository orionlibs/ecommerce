package de.hybris.platform.jalo.security;

import de.hybris.platform.core.Registry;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.persistence.security.ACLEntryJDBC;

public class UserRight extends GeneratedUserRight
{
    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(!allAttributes.containsKey("code"))
        {
            throw new JaloInvalidParameterException("Missing parameter (code) to create a UserRight", 0);
        }
        consistencyCheck((String)allAttributes.get("code"));
        allAttributes.setAttributeMode("code", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    @ForceJALO(reason = "consistency check")
    public void setCode(SessionContext ctx, String value) throws ConsistencyCheckException
    {
        consistencyCheck(value);
        super.setCode(ctx, value);
    }


    protected void consistencyCheck(String code) throws ConsistencyCheckException
    {
        try
        {
            if(AccessManager.getInstance().getUserRightByCode(code) == null)
            {
                return;
            }
        }
        catch(JaloSystemException jaloSystemException)
        {
        }
        throw new ConsistencyCheckException(null, "error: duplicate userright code: " + code + " for typecode ", 0);
    }


    @ForceJALO(reason = "abstract method implementation")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        super.remove(ctx);
        ACLEntryJDBC.removeAllEntriesForUserright(getPK(), Registry.getCurrentTenant().getPersistencePool().getDataSource());
    }
}
