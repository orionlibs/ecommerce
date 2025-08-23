package de.hybris.platform.jalo.order.price;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Tax extends GeneratedTax
{
    public static final String EXTERNAL_KEY = "Tax.externalKey";
    public static final String VALUE_START = "Tax.valueStart";
    public static final String VALUE_END = "Tax.valueEnd";


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        String code = (String)allAttributes.get("code");
        if(code == null)
        {
            throw new JaloInvalidParameterException("Missing parameter( code) to create a Tax", 0);
        }
        if(!checkConsistency(ctx, code))
        {
            throw new ConsistencyCheckException(null, "tax code \"" + code + "\" is already used", 0);
        }
        allAttributes.setAttributeMode("code", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Map getAllNames(SessionContext ctx)
    {
        return getAllName(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setAllNames(SessionContext ctx, Map names)
    {
        setAllName(ctx, names);
    }


    @ForceJALO(reason = "consistency check")
    public void setCode(SessionContext ctx, String code) throws ConsistencyCheckException
    {
        if(!checkConsistency(ctx, code))
        {
            throw new ConsistencyCheckException(null, "tax code \"" + code + "\" is already used", 0);
        }
        super.setCode(ctx, code);
    }


    @ForceJALO(reason = "something else")
    public Boolean isAbsolute(SessionContext ctx)
    {
        return Boolean.valueOf((getCurrency(ctx) != null));
    }


    private boolean checkConsistency(SessionContext ctx, String code)
    {
        List<Tax> result = FlexibleSearch.getInstance().search(ctx, "SELECT {PK} FROM {Tax} WHERE {code}=?code", Collections.singletonMap("code", code), Tax.class).getResult();
        return result.isEmpty();
    }
}
