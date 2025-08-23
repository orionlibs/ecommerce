package de.hybris.platform.jalo.flexiblesearch;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SavedQuery extends GeneratedSavedQuery
{
    public static final String TYPE_PLACEHOLDER = "$$$";


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        String code = (String)allAttributes.get("code");
        if(FlexibleSearch.getInstance().getSavedQuery(code) != null)
        {
            throw new JaloInvalidParameterException("There is already a SavedQuery with the specified code attribute", 0);
        }
        return super.createItem(ctx, type, allAttributes);
    }


    @ForceJALO(reason = "something else")
    public Map<String, Type> getAllParams(SessionContext ctx)
    {
        Map<String, Type> params = super.getAllParams(ctx);
        if(params == null)
        {
            Collection<Type> paramList = getParamtypes();
            if(paramList != null)
            {
                params = new HashMap<>();
                int i = 1;
                for(Iterator<Type> it = paramList.iterator(); it.hasNext(); i++)
                {
                    Type type = it.next();
                    params.put(String.valueOf(i), type);
                }
                setAllParams(ctx, params);
            }
        }
        return params;
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe
    public void setParams(Map params)
    {
        setAllParams(params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe
    public Map getParams()
    {
        return getAllParams();
    }


    @ForceJALO(reason = "consistency check")
    public void setCode(SessionContext ctx, String value) throws JaloInvalidParameterException
    {
        if(FlexibleSearch.getInstance().getSavedQuery(value) != null && getCode() != value)
        {
            throw new JaloInvalidParameterException("There is already a SavedQuery with the specified code attribute", 0);
        }
        super.setCode(ctx, value);
    }
}
