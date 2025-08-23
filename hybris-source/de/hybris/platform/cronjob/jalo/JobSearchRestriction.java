package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.type.ComposedType;
import org.apache.log4j.Logger;

public class JobSearchRestriction extends GeneratedJobSearchRestriction
{
    private static final Logger log = Logger.getLogger(JobSearchRestriction.class.getName());


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        checkRestrictionQuery((ComposedType)allAttributes.get("type"), (String)allAttributes.get("query"));
        return item;
    }


    private void checkRestrictionQuery(ComposedType restrictedType, String query) throws JaloInvalidParameterException
    {
        if(restrictedType == null)
        {
            throw new JaloInvalidParameterException("restricted type cannot be NULL", 0);
        }
        if(query == null)
        {
            throw new JaloInvalidParameterException("query cannot be NULL", 0);
        }
        if("".equals(query.trim()))
        {
            throw new JaloInvalidParameterException("query cannot be empty", 0);
        }
        try
        {
            getSession().getFlexibleSearch().checkQuery("SELECT {" + Item.PK + "} FROM {" + restrictedType
                            .getCode() + "} WHERE " + query, true);
        }
        catch(FlexibleSearchException e)
        {
            throw new JaloInvalidParameterException(e, e.getErrorCode());
        }
    }
}
