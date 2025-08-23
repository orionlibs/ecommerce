package de.hybris.platform.deliveryzone.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.HashSet;
import java.util.Set;

public class ZoneDeliveryModeValue extends GeneratedZoneDeliveryModeValue
{
    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        checkMandatoryAttribute("deliveryMode", allAttributes, missing);
        checkMandatoryAttribute("zone", allAttributes, missing);
        checkMandatoryAttribute("currency", allAttributes, missing);
        checkMandatoryAttribute("minimum", allAttributes, missing);
        checkMandatoryAttribute("value", allAttributes, missing);
        if(!missing.isEmpty())
        {
            throw new JaloInvalidParameterException("missing zone delivery mode value attributes " + missing, 0);
        }
        ((ZoneDeliveryMode)allAttributes.get("deliveryMode")).checkAllowedNewValue((Currency)allAttributes.get("currency"), (Zone)allAttributes
                        .get("zone"), ((Double)allAttributes
                        .get("minimum"))
                        .doubleValue(), ((Double)allAttributes
                        .get("value")).doubleValue());
        allAttributes.setAttributeMode("deliveryMode", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("zone", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("currency", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("value", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("minimum", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    @ForceJALO(reason = "consistency check")
    public void setCurrency(SessionContext ctx, Currency value)
    {
        if(value == null)
        {
            throw new JaloInvalidParameterException("currency cannot be null", 0);
        }
        super.setCurrency(ctx, value);
    }


    @ForceJALO(reason = "consistency check")
    public void setValue(SessionContext ctx, Double value)
    {
        if(value == null)
        {
            throw new JaloInvalidParameterException("value cannot be null", 0);
        }
        super.setValue(ctx, value);
    }


    @ForceJALO(reason = "consistency check")
    public void setMinimum(SessionContext ctx, Double value)
    {
        if(value == null)
        {
            throw new JaloInvalidParameterException("minimum cannot be null", 0);
        }
        super.setMinimum(ctx, value);
    }
}
