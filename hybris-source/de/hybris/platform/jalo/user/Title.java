package de.hybris.platform.jalo.user;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Title extends GeneratedTitle
{
    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set<String> missing = new HashSet<>();
        if(!checkMandatoryAttribute("code", allAttributes, missing))
        {
            throw new JaloInvalidParameterException("missing " + missing + " for creating a new Title", 0);
        }
        checkConsistency((String)allAttributes.get("code"), null);
        allAttributes.setAttributeMode("code", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    private void checkConsistency(String newCode, PK myPK) throws ConsistencyCheckException
    {
        Map<String, Object> params = new HashMap<>();
        params.put("code", newCode);
        StringBuffer query = new StringBuffer(60);
        query.append("GET {").append(GeneratedCoreConstants.TC.TITLE).append("} WHERE {").append("code")
                        .append("}=?code");
        if(myPK != null)
        {
            query.append(" AND {").append(PK).append("} <> ?myPK");
            params.put("myPK", myPK);
        }
        List<Title> result = FlexibleSearch.getInstance().search(query.toString(), params, Collections.singletonList(Title.class), true, true, 0, 1).getResult();
        if(!result.isEmpty())
        {
            throw new ConsistencyCheckException("Duplicate code '" + newCode + "' for type Title", 0);
        }
    }


    @ForceJALO(reason = "consistency check")
    public void setCode(SessionContext ctx, String value) throws ConsistencyCheckException
    {
        if(value == null)
        {
            throw new JaloInvalidParameterException("Given code is null", 0);
        }
        checkConsistency(value, getPK());
        super.setCode(ctx, value);
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


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return "Title " + getCode() + "(" + getPK().toString() + ")";
    }
}
