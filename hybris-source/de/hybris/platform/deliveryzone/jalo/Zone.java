package de.hybris.platform.deliveryzone.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.type.ComposedType;

public class Zone extends GeneratedZone
{
    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(allAttributes.get("code") == null)
        {
            throw new JaloInvalidParameterException("Missing parameter (code) to create a Zone", 0);
        }
        allAttributes.setAttributeMode("code", Item.AttributeMode.INITIAL);
        Zone existing = ZoneDeliveryModeManager.getInstance().getZone((String)allAttributes.get("code"));
        if(existing != null)
        {
            throw new ConsistencyCheckException("zone code '" + allAttributes.get("code") + "' is already used by zone " + existing
                            .getPK(), 0);
        }
        return super.createItem(ctx, type, allAttributes);
    }


    @ForceJALO(reason = "consistency check")
    public void setCode(SessionContext ctx, String code)
    {
        if(code == null)
        {
            throw new JaloInvalidParameterException("code cannot be null", 0);
        }
        Zone existing = ZoneDeliveryModeManager.getInstance().getZone(code);
        if(existing != null && !equals(existing))
        {
            throw new JaloInvalidParameterException("zone code '" + code + "' is already used by zone " + existing.getPK(), 0);
        }
        super.setCode(ctx, code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addCountry(Country country)
    {
        addToCountries(country);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeCountry(Country country)
    {
        removeFromCountries(country);
    }
}
