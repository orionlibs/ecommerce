package de.hybris.platform.jalo.product;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.order.price.ProductPriceInformations;
import de.hybris.platform.jalo.type.AttributeSetter;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Deprecated(since = "ages", forRemoval = false)
public class Product extends GeneratedProduct
{
    private static final long serialVersionUID = -8785517992931803057L;


    @SLDSafe(portingClass = "MandatoryAttributesValidator", portingMethod = "onValidate(final Object model, final InterceptorContext ctx)")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(!checkMandatoryAttribute("code", allAttributes, missing))
        {
            throw new JaloInvalidParameterException("missing parameters " + missing + " to create a product", 0);
        }
        allAttributes.setAttributeMode("code", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("name", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("description", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("picture", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("thumbnail", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Map getAllNames(SessionContext ctx)
    {
        return getAllName(ctx);
    }


    @AttributeSetter("Product.name")
    @Deprecated(since = "ages", forRemoval = false)
    public void setAllNames(SessionContext ctx, Map names)
    {
        setAllName(ctx, names);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Map getAllDescriptions(SessionContext ctx)
    {
        return getAllDescription(ctx);
    }


    @AttributeSetter("Product.description")
    @Deprecated(since = "ages", forRemoval = false)
    public void setAllDescriptions(SessionContext ctx, Map descriptions)
    {
        setAllDescription(ctx, descriptions);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ProductPriceInformations getAllPriceInformations(boolean net) throws JaloPriceFactoryException
    {
        return getAllPriceInformations(getSession().getSessionContext(), net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ProductPriceInformations getAllPriceInformations(SessionContext ctx, boolean net) throws JaloPriceFactoryException
    {
        return getAllPriceInformations(ctx, new Date(), net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ProductPriceInformations getAllPriceInformations(Date forDate, boolean net) throws JaloPriceFactoryException
    {
        return getAllPriceInformations(getSession().getSessionContext(), forDate, net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ProductPriceInformations getAllPriceInformations(SessionContext ctx, Date forDate, boolean net) throws JaloPriceFactoryException
    {
        return getSession().getOrderManager().getPriceFactory().getAllPriceInformations(ctx, this, forDate, net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List getPriceInformations(Date forDate, boolean net) throws JaloPriceFactoryException
    {
        return getPriceInformations(getSession().getSessionContext(), forDate, net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List getPriceInformations(SessionContext ctx, Date forDate, boolean net) throws JaloPriceFactoryException
    {
        return getSession().getOrderManager().getPriceFactory().getProductPriceInformations(ctx, this, forDate, net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List getPriceInformations(boolean net) throws JaloPriceFactoryException
    {
        return getPriceInformations(getSession().getSessionContext(), net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List getPriceInformations(SessionContext ctx, boolean net) throws JaloPriceFactoryException
    {
        return getPriceInformations(ctx, new Date(), net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List getTaxInformations(Date forDate) throws JaloPriceFactoryException
    {
        return getTaxInformations(getSession().getSessionContext(), forDate);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List getTaxInformations(SessionContext ctx, Date forDate) throws JaloPriceFactoryException
    {
        return getSession().getOrderManager().getPriceFactory().getProductTaxInformations(ctx, this, forDate);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List getTaxInformations() throws JaloPriceFactoryException
    {
        return getTaxInformations(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List getTaxInformations(SessionContext ctx) throws JaloPriceFactoryException
    {
        return getTaxInformations(ctx, new Date());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List getDiscountInformations(Date forDate, boolean net) throws JaloPriceFactoryException
    {
        return getDiscountInformations(getSession().getSessionContext(), forDate, net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List getDiscountInformations(SessionContext ctx, Date forDate, boolean net) throws JaloPriceFactoryException
    {
        return getSession().getOrderManager().getPriceFactory().getProductDiscountInformations(ctx, this, forDate, net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List getDiscountInformations(boolean net) throws JaloPriceFactoryException
    {
        return getDiscountInformations(getSession().getSessionContext(), net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List getDiscountInformations(SessionContext ctx, boolean net) throws JaloPriceFactoryException
    {
        return getDiscountInformations(ctx, new Date(), net);
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return "Product '" + getCode() + "' (" + getPK().getLongValueAsString() + ")";
    }
}
