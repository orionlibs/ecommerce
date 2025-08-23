package de.hybris.platform.europe1.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import java.util.HashSet;
import java.util.Set;

public class PriceRow extends GeneratedPriceRow
{
    @Deprecated(since = "ages", forRemoval = false)
    public static final String MIN_QUANTITY = "minqtd";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String UNIT_FACTOR = "unitFactor";
    public static final String PRICEROW = "pricerow";


    @Deprecated(since = "ages", forRemoval = false)
    protected int calculateMatchValue(Product product, EnumerationValue productGroup, User user, EnumerationValue userGroup)
    {
        return calculateMatchValue(product, null, productGroup, user, userGroup);
    }


    protected int calculateMatchValue(Product product, String productCode, EnumerationValue productGroup, User user, EnumerationValue userGroup)
    {
        boolean _product = (product != null || productCode != null);
        boolean _productGroup = (productGroup != null);
        boolean _user = (user != null);
        boolean _userGroup = (userGroup != null);
        int value = 0;
        if(_product)
        {
            if(_user)
            {
                value = 9;
            }
            else if(_userGroup)
            {
                value = 7;
            }
            else
            {
                value = 5;
            }
        }
        else if(_productGroup)
        {
            if(_user)
            {
                value = 8;
            }
            else if(_userGroup)
            {
                value = 6;
            }
            else
            {
                value = 4;
            }
        }
        else if(_user)
        {
            value = 3;
        }
        else if(_userGroup)
        {
            value = 2;
        }
        else
        {
            value = 1;
        }
        return value;
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.interceptor.impl.JaloInitDefaultsInterceptor", portingMethod = "loadDefaults")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        checkMandatoryAttribute("currency", allAttributes, missing);
        checkMandatoryAttribute("price", allAttributes, missing);
        if(!missing.isEmpty())
        {
            throw new JaloInvalidParameterException("missing price row attributes " + missing, 0);
        }
        if(allAttributes.get("minqtd") == null)
        {
            allAttributes.put("minqtd", Long.valueOf(1L));
        }
        if(allAttributes.get("net") == null)
        {
            allAttributes.put("net", Boolean.FALSE);
        }
        if(allAttributes.get("unitFactor") == null)
        {
            allAttributes.put("unitFactor", Integer.valueOf(1));
        }
        if(allAttributes.get("unit") == null)
        {
            Product product = (Product)allAttributes.get("product");
            Unit fallbackUnit = (product != null) ? product.getUnit() : null;
            if(fallbackUnit != null)
            {
                allAttributes.put("unit", fallbackUnit);
            }
            else
            {
                throw new JaloInvalidParameterException("missing unit for price row ", 0);
            }
        }
        allAttributes.setAttributeMode("minqtd", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("currency", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("net", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("price", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("unit", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("unitFactor", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("giveAwayPrice", Item.AttributeMode.INITIAL);
        allAttributes.put("matchValue", Integer.valueOf(calculateMatchValue((Product)allAttributes.get("product"), (String)allAttributes
                        .get("productId"), (EnumerationValue)allAttributes
                        .get("pg"), (User)allAttributes
                        .get("user"), (EnumerationValue)allAttributes
                        .get("ug"))));
        allAttributes.setAttributeMode("matchValue", Item.AttributeMode.INITIAL);
        PriceRow ret = (PriceRow)super.createItem(ctx, type, allAttributes);
        return (Item)ret;
    }


    @SLDSafe(portingClass = "de.hybris.platform.europe1.model.PDTRowModel", portingMethod = "getPg")
    public EnumerationValue getProductGroup(SessionContext ctx)
    {
        return getPg(ctx);
    }


    @SLDSafe(portingClass = "de.hybris.platform.europe1.model.PDTRowModel", portingMethod = "getUg")
    public EnumerationValue getUserGroup(SessionContext ctx)
    {
        return getUg(ctx);
    }


    protected void updateMatchValue()
    {
        setMatchValue(calculateMatchValue(getProduct(), getProductId(), getPg(), getUser(), getUg()));
    }


    @SLDSafe(portingClass = "de.hybris.platform.product.impl.PriceRowPrepareInterceptor", portingMethod = "onPrepare")
    protected void setPg(SessionContext ctx, EnumerationValue value)
    {
        super.setPg(ctx, value);
        updateMatchValue();
    }


    @SLDSafe(portingClass = "de.hybris.platform.product.impl.PriceRowPrepareInterceptor", portingMethod = "onPrepare")
    public void setUg(SessionContext ctx, EnumerationValue value)
    {
        super.setUg(ctx, value);
        updateMatchValue();
    }


    @SLDSafe(portingClass = "de.hybris.platform.product.impl.PriceRowPrepareInterceptor", portingMethod = "onPrepare")
    protected void setProduct(SessionContext ctx, Product value)
    {
        super.setProduct(ctx, value);
        updateMatchValue();
    }


    @SLDSafe(portingClass = "de.hybris.platform.product.impl.PriceRowPrepareInterceptor", portingMethod = "onPrepare")
    public void setUser(SessionContext ctx, User value)
    {
        super.setUser(ctx, value);
        updateMatchValue();
    }


    @SLDSafe(portingClass = "de.hybris.platform.product.impl.PriceRowPrepareInterceptor", portingMethod = "onPrepare")
    public void setProductId(SessionContext ctx, String value)
    {
        super.setProductId(ctx, value);
        updateMatchValue();
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.interceptor.impl.MandatoryAttributesValidator", portingMethod = "onValidate")
    public void setCurrency(SessionContext ctx, Currency value)
    {
        if(value == null)
        {
            throw new JaloInvalidParameterException("currency cannot be null", 0);
        }
        super.setCurrency(ctx, value);
    }


    public long getMinQuantity()
    {
        return getMinqtdAsPrimitive();
    }


    public long getMinQuantity(SessionContext ctx)
    {
        return getMinqtdAsPrimitive(ctx);
    }


    public void setMinQuantity(long minQtd) throws JaloPriceFactoryException
    {
        setMinqtd(minQtd);
    }


    public void setMinQuantity(SessionContext ctx, long minQtd) throws JaloPriceFactoryException
    {
        setMinqtd(ctx, minQtd);
    }


    @SLDSafe(portingClass = "de.hybris.platform.product.impl.PriceRowValidateInterceptor", portingMethod = "onValidate")
    public void setMinqtd(SessionContext ctx, Long value)
    {
        if(value == null)
        {
            throw new JaloInvalidParameterException("min quantity cannot be null", 0);
        }
        if(value.longValue() < 0L)
        {
            throw new JaloInvalidParameterException("min quantity must be equal or greater zero but was " + value, 0);
        }
        super.setMinqtd(ctx, value);
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.interceptor.impl.MandatoryAttributesValidator", portingMethod = "onValidate")
    public void setPrice(SessionContext ctx, Double value)
    {
        if(value == null)
        {
            throw new JaloInvalidParameterException("price cannot be null", 0);
        }
        super.setPrice(ctx, value);
    }


    @SLDSafe(portingClass = "de.hybris.platform.product.impl.PriceRowValidateInterceptor", portingMethod = "onValidate")
    public void setUnitFactor(SessionContext ctx, Integer value)
    {
        if(value == null)
        {
            throw new JaloInvalidParameterException("unit factor cannot be null", 0);
        }
        if(value.intValue() == 0)
        {
            throw new JaloInvalidParameterException("unit factor cannot be zero", 0);
        }
        super.setUnitFactor(ctx, value);
    }


    @SLDSafe(portingClass = "de.hybris.platform.product.impl.PriceRowValidateInterceptor", portingMethod = "onValidate")
    public void setUnit(SessionContext ctx, Unit value)
    {
        if(value == null)
        {
            throw new JaloInvalidParameterException("unit cannot be null", 0);
        }
        super.setUnit(ctx, value);
    }
}
