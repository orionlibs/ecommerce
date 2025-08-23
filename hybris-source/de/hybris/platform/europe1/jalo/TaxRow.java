package de.hybris.platform.europe1.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.price.Tax;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.HashSet;
import java.util.Set;

public class TaxRow extends GeneratedTaxRow
{
    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.interceptor.impl.MandatoryAttributesValidator", portingMethod = "onValidate")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        checkMandatoryAttribute("tax", allAttributes, missing);
        if(!missing.isEmpty())
        {
            throw new JaloInvalidParameterException("missing tax row attributes " + missing, 0);
        }
        allAttributes.setAttributeMode("tax", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("value", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("currency", Item.AttributeMode.INITIAL);
        TaxRow ret = (TaxRow)super.createItem(ctx, type, allAttributes);
        return (Item)ret;
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.interceptor.impl.MandatoryAttributesValidator", portingMethod = "onValidate")
    protected void setTax(SessionContext ctx, Tax value)
    {
        if(value == null)
        {
            throw new JaloInvalidParameterException("tax cannot be null", 0);
        }
        super.setTax(ctx, value);
    }


    public boolean hasValue()
    {
        return hasValue(getSession().getSessionContext());
    }


    public boolean hasValue(SessionContext ctx)
    {
        return (getValue(ctx) != null);
    }


    @SLDSafe(portingClass = "de.hybris.platform.europe1.model.TaxRowModel", portingMethod = "getAbsolute")
    public boolean isAbsoluteAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAbsolute(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    @SLDSafe(portingClass = "de.hybris.platform.europe1.model.TaxRowModel", portingMethod = "getAbsolute")
    public boolean isAbsoluteAsPrimitive()
    {
        return isAbsoluteAsPrimitive(getSession().getSessionContext());
    }


    @SLDSafe(portingClass = "de.hybris.platform.europe1.model.TaxRowModel", portingMethod = "getAbsolute")
    public Boolean isAbsolute(SessionContext ctx)
    {
        return (getCurrency() != null) ? Boolean.TRUE : Boolean.FALSE;
    }
}
