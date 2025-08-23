package de.hybris.platform.jalo.media;

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
import org.apache.log4j.Logger;

public class MediaFormat extends GeneratedMediaFormat
{
    private static final Logger LOG = Logger.getLogger(MediaFormat.class.getName());


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(!checkMandatoryAttribute("qualifier", allAttributes, missing))
        {
            throw new JaloInvalidParameterException("missing " + missing + " for creating a new MediaFormat", 0);
        }
        checkConsistency((String)allAttributes.get("qualifier"), null);
        allAttributes.setAttributeMode("qualifier", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    protected void checkConsistency(String newQualifier, PK myPK) throws ConsistencyCheckException
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("quali", newQualifier);
        StringBuffer query = new StringBuffer(60);
        query.append("SELECT {").append(PK).append("} FROM {").append(GeneratedCoreConstants.TC.MEDIAFORMAT).append("} WHERE {").append("qualifier")
                        .append("}=?quali");
        if(myPK != null)
        {
            query.append(" AND {").append(PK).append("} <> ?myPK");
            params.put("myPK", myPK);
        }
        List result = FlexibleSearch.getInstance().search(query.toString(), params, Collections.singletonList(MediaFormat.class), true, true, 0, 1).getResult();
        if(!result.isEmpty())
        {
            throw new ConsistencyCheckException("Duplicate qualifier '" + newQualifier + "' for type MediaFormat", 0);
        }
    }


    @ForceJALO(reason = "consistency check")
    public void setQualifier(SessionContext ctx, String value) throws JaloInvalidParameterException
    {
        if(value == null)
        {
            throw new JaloInvalidParameterException("Given qualifier is null", 0);
        }
        try
        {
            checkConsistency(value, getPK());
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloInvalidParameterException(e, 0);
        }
        super.setQualifier(ctx, value);
    }
}
