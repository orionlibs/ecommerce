package de.hybris.platform.jalo.c2l;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import org.apache.log4j.Logger;

public class Country extends GeneratedCountry
{
    private static final Logger LOG = Logger.getLogger(Country.class.getName());


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Country.ItemCreator allAttributes: " + allAttributes);
        }
        if(!allAttributes.containsKey("isocode"))
        {
            throw new JaloInvalidParameterException("Missing parameter( isocode ) to create a Country", 0);
        }
        checkConsistencyIsocode((String)allAttributes.get("isocode"), null, type.getCode());
        allAttributes.setAttributeMode("isocode", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Region getRegionByCode(String code) throws JaloItemNotFoundException
    {
        return getSession().getC2LManager().getRegionByCode(this, code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Region addNewRegion(String code) throws ConsistencyCheckException
    {
        return addNewRegion(getSession().getSessionContext(), code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Region addNewRegion(SessionContext ctx, String code) throws ConsistencyCheckException
    {
        return C2LManager.getInstance().createRegion(null, code, this);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Region addNewRegion(SessionContext ctx, PK pk, String code) throws ConsistencyCheckException
    {
        return C2LManager.getInstance().createRegion(pk, code, this);
    }
}
