package de.hybris.platform.europe1.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.StandardDateRange;
import java.util.Date;

public abstract class PDTRow extends GeneratedPDTRow
{
    @Deprecated(since = "ages", forRemoval = false)
    public static final String DATERANGE = "dateRange";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String DATE_RANGE = "dateRange";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String START_TIME = "startTime";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String END_TIME = "endTime";
    public static final Product ALL_PRODUCTS = null;
    public static final String ALL_PRODUCT_GROUPS = null;
    public static final User ALL_USERS = null;
    public static final String ALL_USER_GROUPS = null;


    @SLDSafe(portingClass = "de.hybris.platform.product.impl.PDTRowPrepareInterceptor", portingMethod = "onPrepare")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(moreThanOneReferenceAssigned(allAttributes))
        {
            throw new JaloPriceFactoryException("You can only set only one of the following: PRODUCT, PG, PRODUCTID", 0);
        }
        if(allAttributes.get("user") != null && allAttributes.get("ug") != null)
        {
            throw new JaloPriceFactoryException("cannot set both USER and UG - set just one of them", 0);
        }
        allAttributes.put("productMatchQualifier", getInitialProductMatchField(allAttributes));
        allAttributes.put("userMatchQualifier", getInitialUserMatchField(allAttributes));
        allAttributes.setAttributeMode("product", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("pg", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("productMatchQualifier", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("user", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("ug", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("userMatchQualifier", Item.AttributeMode.INITIAL);
        StandardDateRange dateRange = (StandardDateRange)allAttributes.get("dateRange");
        Date startTime = (Date)allAttributes.get("startTime");
        Date endTime = (Date)allAttributes.get("endTime");
        if(dateRange != null)
        {
            if(startTime != null && !startTime.equals(dateRange.getStart()))
            {
                throw new JaloInvalidParameterException("cannot specify both dateRange=" + dateRange + " and startTime=" + startTime + " attributes", -1);
            }
            allAttributes.put("startTime", dateRange.getStart());
            if(endTime != null && !endTime.equals(dateRange.getEnd()))
            {
                throw new JaloInvalidParameterException("cannot specify both dateRange=" + dateRange + " and endTime=" + startTime + " attributes", -1);
            }
            allAttributes.put("endTime", dateRange.getEnd());
            allAttributes.remove("dateRange");
        }
        else
        {
            allAttributes.put("startTime", startTime);
            allAttributes.put("endTime", endTime);
        }
        allAttributes.setAttributeMode("startTime", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("endTime", Item.AttributeMode.INITIAL);
        PDTRow result = (PDTRow)super.createItem(ctx, type, allAttributes);
        result.markProductModified();
        return (Item)result;
    }


    private boolean moreThanOneReferenceAssigned(Item.ItemAttributeMap allAttributes)
    {
        int referenceCounter = 0;
        if(allAttributes.get("product") != null)
        {
            referenceCounter++;
        }
        if(allAttributes.get("pg") != null)
        {
            referenceCounter++;
        }
        if(allAttributes.get("productId") != null)
        {
            referenceCounter++;
        }
        return (referenceCounter > 1);
    }


    @SLDSafe(portingClass = "de.hybris.platform.product.impl.PDTRowPrepareInterceptor", portingMethod = "onPrepare")
    public void setProductMatchQualifier(SessionContext ctx, Long value)
    {
    }


    private void setProductMatchQualifierInternal(SessionContext ctx, Long qualifier)
    {
        super.setProductMatchQualifier(ctx, qualifier);
    }


    @SLDSafe(portingClass = "de.hybris.platform.product.impl.PDTRowPrepareInterceptor", portingMethod = "onPrepare")
    public void setUserMatchQualifier(SessionContext ctx, Long value)
    {
    }


    private void setUserMatchQualifierInternal(SessionContext ctx, Long qualifier)
    {
        super.setUserMatchQualifier(ctx, qualifier);
    }


    protected Long getInitialProductMatchField(Item.ItemAttributeMap allAttributes)
    {
        Product product = (Product)allAttributes.get("product");
        if(product != null)
        {
            return Long.valueOf(product.getPK().getLongValue());
        }
        EnumerationValue productGroup;
        if((productGroup = (EnumerationValue)allAttributes.get("pg")) != null)
        {
            return Long.valueOf(productGroup.getPK().getLongValue());
        }
        if(allAttributes.get("productId") != null)
        {
            return Long.valueOf(Europe1PriceFactory.MATCH_BY_PRODUCT_ID);
        }
        return Long.valueOf(Europe1PriceFactory.MATCH_ANY);
    }


    protected Long getInitialUserMatchField(Item.ItemAttributeMap allAttributes)
    {
        User user = (User)allAttributes.get("user");
        if(user != null)
        {
            return Long.valueOf(user.getPK().getLongValue());
        }
        EnumerationValue userGroup;
        if((userGroup = (EnumerationValue)allAttributes.get("ug")) != null)
        {
            return Long.valueOf(userGroup.getPK().getLongValue());
        }
        return Long.valueOf(PK.NULL_PK.getLongValue());
    }


    protected void updateProductMatchField()
    {
        Product product = getProduct();
        if(product != null)
        {
            setProductMatchQualifierInternal(ctx(), Long.valueOf(product.getPK().getLongValue()));
        }
        else
        {
            EnumerationValue productGroup;
            if((productGroup = getPg()) != null)
            {
                setProductMatchQualifierInternal(ctx(), Long.valueOf(productGroup.getPK().getLongValue()));
            }
            else if(getProductId() != null)
            {
                setProductMatchQualifier(ctx(), Long.valueOf(Europe1PriceFactory.MATCH_BY_PRODUCT_ID));
            }
            else
            {
                setProductMatchQualifierInternal(ctx(), Long.valueOf(PK.NULL_PK.getLongValue()));
            }
        }
    }


    protected void updateUserMatchField()
    {
        User user = getUser();
        if(user != null)
        {
            setUserMatchQualifierInternal(ctx(), Long.valueOf(user.getPK().getLongValue()));
        }
        else
        {
            EnumerationValue userGroup;
            if((userGroup = getUg()) != null)
            {
                setUserMatchQualifierInternal(ctx(), Long.valueOf(userGroup.getPK().getLongValue()));
            }
            else
            {
                setUserMatchQualifierInternal(ctx(), Long.valueOf(PK.NULL_PK.getLongValue()));
            }
        }
    }


    @SLDSafe(portingClass = "de.hybris.platform.product.impl.PDTRowPrepareInterceptor", portingMethod = "onPrepare")
    protected void setProduct(SessionContext ctx, Product value)
    {
        if(value != null)
        {
            super.setPg(ctx, null);
            super.setProductId(ctx, null);
        }
        super.setProduct(ctx, value);
        updateProductMatchField();
    }


    @SLDSafe(portingClass = "de.hybris.platform.product.impl.PDTRowPrepareInterceptor", portingMethod = "onPrepare")
    public void setProductId(SessionContext ctx, String value)
    {
        if(value != null)
        {
            super.setPg(ctx, null);
            super.setProduct(ctx, null);
        }
        super.setProductId(ctx, value);
        updateProductMatchField();
    }


    @SLDSafe(portingClass = "de.hybris.platform.product.impl.PDTRowPrepareInterceptor", portingMethod = "onPrepare")
    public void setUser(SessionContext ctx, User value)
    {
        if(value != null)
        {
            super.setUg(ctx, null);
        }
        super.setUser(ctx, value);
        updateUserMatchField();
    }


    @SLDSafe(portingClass = "de.hybris.platform.product.impl.PDTRowPrepareInterceptor", portingMethod = "onPrepare")
    protected void setPg(SessionContext ctx, EnumerationValue value)
    {
        if(value != null)
        {
            super.setProduct(ctx, null);
            setProductId(null);
        }
        super.setPg(ctx, value);
        updateProductMatchField();
    }


    @SLDSafe(portingClass = "de.hybris.platform.product.impl.PDTRowPrepareInterceptor", portingMethod = "onPrepare")
    public void setUg(SessionContext ctx, EnumerationValue value)
    {
        if(value != null)
        {
            super.setUser(ctx, null);
        }
        super.setUg(ctx, value);
        updateUserMatchField();
    }


    @SLDSafe
    protected void removeLinks()
    {
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getProductPriceGroup()
    {
        return getProductGroup(getSession().getSessionContext());
    }


    public EnumerationValue getProductGroup()
    {
        return getProductGroup(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getProductPriceGroup(SessionContext ctx)
    {
        return getProductGroup(ctx);
    }


    public EnumerationValue getProductGroup(SessionContext ctx)
    {
        return getPg(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Customer getCustomer()
    {
        return getCustomer(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Customer getCustomer(SessionContext ctx)
    {
        return (Customer)getUser(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getCustomerPriceGroup()
    {
        return getCustomerGroup(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getCustomerGroup()
    {
        return getCustomerGroup(getSession().getSessionContext());
    }


    public EnumerationValue getUserGroup()
    {
        return getUserGroup(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getCustomerPriceGroup(SessionContext ctx)
    {
        return getUserGroup(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getCustomerGroup(SessionContext ctx)
    {
        return getUserGroup(ctx);
    }


    public EnumerationValue getUserGroup(SessionContext ctx)
    {
        return getUg(ctx);
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.model.attribute.PDTRowDateRangeHandler", portingMethod = "get")
    public StandardDateRange getDateRange()
    {
        return getDateRange(getSession().getSessionContext());
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.model.attribute.PDTRowDateRangeHandler", portingMethod = "get")
    public StandardDateRange getDateRange(SessionContext ctx)
    {
        Date start = getStartTime();
        Date end = getEndTime();
        if(start != null && end != null)
        {
            return new StandardDateRange(start, end);
        }
        return null;
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.model.attribute.PDTRowDateRangeHandler", portingMethod = "set")
    public void setDateRange(StandardDateRange dateRange)
    {
        setDateRange(getSession().getSessionContext(), dateRange);
    }


    @SLDSafe(portingClass = "de.hybris.platform.servicelayer.model.attribute.PDTRowDateRangeHandler", portingMethod = "set")
    public void setDateRange(SessionContext ctx, StandardDateRange dateRange)
    {
        setStartTime((dateRange != null) ? dateRange.getStart() : null);
        setEndTime((dateRange != null) ? dateRange.getEnd() : null);
    }


    @SLDSafe(portingClass = "de.hybris.platform.product.impl.PDTRowRemoveInterceptor", portingMethod = "onRemove")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        markProductModified();
        super.remove(ctx);
    }


    public Object setProperty(SessionContext ctx, String name, Object value)
    {
        Object prev = super.setProperty(ctx, name, value);
        if(prev != value && (prev == null || !prev.equals(value)))
        {
            markProductModified();
            if(prev instanceof Product)
            {
                markProductModified((Product)prev);
            }
        }
        return prev;
    }


    protected void markProductModified()
    {
        boolean markProductModifiedSession = Boolean.TRUE.equals(getSession().getSessionContext().getAttribute("pdtrow.mark.product.modified"));
        boolean markProductModified = Config.getBoolean("pdtrow.mark.product.modified", false);
        if(markProductModifiedSession || markProductModified)
        {
            if(isAlive())
            {
                markProductModified(getProduct());
            }
        }
    }


    private void markProductModified(Product product)
    {
        if(product != null && !isCurrentlyRemoving((Item)product))
        {
            product.setModificationTime(new Date());
        }
    }
}
