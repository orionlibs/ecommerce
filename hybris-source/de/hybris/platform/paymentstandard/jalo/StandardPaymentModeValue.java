package de.hybris.platform.paymentstandard.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.HashSet;
import java.util.Set;

public class StandardPaymentModeValue extends GeneratedStandardPaymentModeValue
{
    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        checkMandatoryAttribute("paymentMode", allAttributes, missing);
        checkMandatoryAttribute("currency", allAttributes, missing);
        checkMandatoryAttribute("value", allAttributes, missing);
        if(!missing.isEmpty())
        {
            throw new JaloInvalidParameterException("missing standard payment mode value attributes " + missing, 0);
        }
        allAttributes.setAttributeMode("paymentMode", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("currency", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("value", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    @ForceJALO(reason = "consistency check")
    public void setCurrency(SessionContext ctx, Currency currency)
    {
        if(currency == null)
        {
            throw new JaloInvalidParameterException("currency cannot be null", 0);
        }
        super.setCurrency(ctx, currency);
    }


    @ForceJALO(reason = "consistency check")
    protected void setPaymentMode(SessionContext ctx, StandardPaymentMode value)
    {
        if(value == null)
        {
            throw new JaloInvalidParameterException("payment mode cannot be null", 0);
        }
        super.setPaymentMode(ctx, value);
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
}
