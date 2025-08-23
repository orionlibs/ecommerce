package de.hybris.platform.europe1.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.europe1.constants.Europe1Tools;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.localization.Localization;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractDiscountRow extends GeneratedAbstractDiscountRow
{
    @SLDSafe(portingClass = "UniqueAttributesInterceptor,MandatoryAttributesValidator")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(!checkMandatoryAttribute("discount", allAttributes, missing))
        {
            throw new JaloInvalidParameterException(Localization.getLocalizedString("exception.abstractdiscountrow.createitem.jaloinvalidparameterexception1", new Object[] {missing}), 0);
        }
        return super.createItem(ctx, type, allAttributes);
    }


    public boolean hasValue()
    {
        return hasValue(getSession().getSessionContext());
    }


    public boolean hasValue(SessionContext ctx)
    {
        return (getValue(ctx) != null);
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.interceptor.impl.MandatoryAttributesValidator", portingMethod = "onValidate")
    protected void setDiscount(SessionContext ctx, Discount value)
    {
        if(value == null)
        {
            throw new JaloInvalidParameterException("discount cannot be null", 0);
        }
        super.setDiscount(ctx, value);
    }


    @SLDSafe(portingClass = "de.hybris.platform.europe1.model.AbstractDiscountRowModel", portingMethod = "getDiscountString")
    public String getDiscountString()
    {
        return getDiscountString(getSession().getSessionContext());
    }


    @SLDSafe(portingClass = "de.hybris.platform.europe1.model.AbstractDiscountRowModel", portingMethod = "getDiscountString")
    public String getDiscountString(SessionContext ctx)
    {
        DiscountValue discountValue = Europe1Tools.createDiscountValueList(Collections.singletonList(this)).iterator().next();
        if(discountValue.isAbsolute())
        {
            Currency currency = C2LManager.getInstance().getCurrencyByIsoCode(discountValue.getCurrencyIsoCode());
            return Utilities.getCurrencyInstance(currency).format(discountValue.getValue());
        }
        return Utilities.getPercentInstance().format(discountValue.getValue() / 100.0D);
    }


    @SLDSafe(portingClass = "de.hybris.platform.europe1.model.AbstractDiscountRowModel", portingMethod = "getAbsolute")
    public Boolean isAbsolute(SessionContext ctx)
    {
        return (getCurrency(ctx) != null) ? Boolean.TRUE : Boolean.FALSE;
    }


    @SLDSafe(portingClass = "de.hybris.platform.europe1.model.AbstractDiscountRowModel", portingMethod = "getAbsolute")
    public Boolean isAbsolute()
    {
        return isAbsolute(getSession().getSessionContext());
    }


    @SLDSafe(portingClass = "de.hybris.platform.europe1.model.AbstractDiscountRowModel", portingMethod = "getAbsolute")
    public boolean isAbsoluteAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAbsolute(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    @SLDSafe(portingClass = "de.hybris.platform.europe1.model.AbstractDiscountRowModel", portingMethod = "getAbsolute")
    public boolean isAbsoluteAsPrimitive()
    {
        return isAbsoluteAsPrimitive(getSession().getSessionContext());
    }
}
