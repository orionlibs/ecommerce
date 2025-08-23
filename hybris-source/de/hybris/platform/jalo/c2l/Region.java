package de.hybris.platform.jalo.c2l;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.localization.Localization;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Region extends GeneratedRegion
{
    @Deprecated(since = "ages", forRemoval = false)
    public static final String CODE = "isocode";


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("isocode", allAttributes, missing) ? 1 : 0) | (!checkMandatoryAttribute("country", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing " + missing + " got " + allAttributes, 0);
        }
        if(!(allAttributes.get("country") instanceof Country))
        {
            throw new JaloInvalidParameterException("Parameter country should be instance of Country", 0);
        }
        checkConsistencyIsocode((String)allAttributes.get("isocode"), (Country)allAttributes.get("country"), null);
        allAttributes.setAttributeMode("isocode", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("country", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    protected void checkConsistencyIsocode(String newIsoCodeRegion, Country country, String composedTypeCode) throws ConsistencyCheckException
    {
        Map<String, Object> params = new HashMap<>();
        params.put("isocode", newIsoCodeRegion);
        params.put("country", country.getPK());
        StringBuffer query = new StringBuffer(60);
        query.append("SELECT {").append(PK).append("} FROM {").append(GeneratedCoreConstants.TC.REGION).append("} WHERE {").append("isocode")
                        .append("}=?").append("isocode").append(" AND {").append("country").append("}=?").append("country");
        List<Region> result = FlexibleSearch.getInstance().search(query.toString(), params, Collections.singletonList(Region.class), true, true, 0, 1).getResult();
        if(!result.isEmpty())
        {
            throw new ConsistencyCheckException(null, Localization.getLocalizedString("exception.core.duplicateregioncode", new Object[] {newIsoCodeRegion, country
                            .getIsocode()}), 0);
        }
    }


    @ForceJALO(reason = "something else")
    public void setCountry(SessionContext ctx, Country value)
    {
        try
        {
            setOwner((Item)value);
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloSystemException(e);
        }
        super.setCountry(ctx, value);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String getCode()
    {
        return getIsocode();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setCode(String code) throws ConsistencyCheckException
    {
        setIsocode(code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String getCode(SessionContext ctx)
    {
        return getIsocode(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setCode(SessionContext ctx, String code) throws ConsistencyCheckException
    {
        setIsocode(ctx, code);
    }
}
