package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.voucher.constants.GeneratedVoucherConstants;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedVoucherManager extends Extension
{
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("appliedVoucherCodes", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.order.AbstractOrder", Collections.unmodifiableMap(tmp));
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    Collection<String> getAppliedVoucherCodes(SessionContext ctx, AbstractOrder item)
    {
        Collection<String> coll = (Collection<String>)item.getProperty(ctx, GeneratedVoucherConstants.Attributes.AbstractOrder.APPLIEDVOUCHERCODES);
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    Collection<String> getAppliedVoucherCodes(AbstractOrder item)
    {
        return getAppliedVoucherCodes(getSession().getSessionContext(), item);
    }


    void setAppliedVoucherCodes(SessionContext ctx, AbstractOrder item, Collection<String> value)
    {
        item.setProperty(ctx, GeneratedVoucherConstants.Attributes.AbstractOrder.APPLIEDVOUCHERCODES, (value == null || !value.isEmpty()) ? value : null);
    }


    void setAppliedVoucherCodes(AbstractOrder item, Collection<String> value)
    {
        setAppliedVoucherCodes(getSession().getSessionContext(), item, value);
    }


    public DateRestriction createDateRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedVoucherConstants.TC.DATERESTRICTION);
            return (DateRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating DateRestriction : " + e.getMessage(), 0);
        }
    }


    public DateRestriction createDateRestriction(Map attributeValues)
    {
        return createDateRestriction(getSession().getSessionContext(), attributeValues);
    }


    public NewCustomerRestriction createNewCustomerRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedVoucherConstants.TC.NEWCUSTOMERRESTRICTION);
            return (NewCustomerRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating NewCustomerRestriction : " + e.getMessage(), 0);
        }
    }


    public NewCustomerRestriction createNewCustomerRestriction(Map attributeValues)
    {
        return createNewCustomerRestriction(getSession().getSessionContext(), attributeValues);
    }


    public OrderRestriction createOrderRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedVoucherConstants.TC.ORDERRESTRICTION);
            return (OrderRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating OrderRestriction : " + e.getMessage(), 0);
        }
    }


    public OrderRestriction createOrderRestriction(Map attributeValues)
    {
        return createOrderRestriction(getSession().getSessionContext(), attributeValues);
    }


    public ProductCategoryRestriction createProductCategoryRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedVoucherConstants.TC.PRODUCTCATEGORYRESTRICTION);
            return (ProductCategoryRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ProductCategoryRestriction : " + e.getMessage(), 0);
        }
    }


    public ProductCategoryRestriction createProductCategoryRestriction(Map attributeValues)
    {
        return createProductCategoryRestriction(getSession().getSessionContext(), attributeValues);
    }


    public ProductQuantityRestriction createProductQuantityRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedVoucherConstants.TC.PRODUCTQUANTITYRESTRICTION);
            return (ProductQuantityRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ProductQuantityRestriction : " + e.getMessage(), 0);
        }
    }


    public ProductQuantityRestriction createProductQuantityRestriction(Map attributeValues)
    {
        return createProductQuantityRestriction(getSession().getSessionContext(), attributeValues);
    }


    public ProductRestriction createProductRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedVoucherConstants.TC.PRODUCTRESTRICTION);
            return (ProductRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ProductRestriction : " + e.getMessage(), 0);
        }
    }


    public ProductRestriction createProductRestriction(Map attributeValues)
    {
        return createProductRestriction(getSession().getSessionContext(), attributeValues);
    }


    public PromotionVoucher createPromotionVoucher(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedVoucherConstants.TC.PROMOTIONVOUCHER);
            return (PromotionVoucher)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating PromotionVoucher : " + e.getMessage(), 0);
        }
    }


    public PromotionVoucher createPromotionVoucher(Map attributeValues)
    {
        return createPromotionVoucher(getSession().getSessionContext(), attributeValues);
    }


    public RegularCustomerOrderQuantityRestriction createRegularCustomerOrderQuantityRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedVoucherConstants.TC.REGULARCUSTOMERORDERQUANTITYRESTRICTION);
            return (RegularCustomerOrderQuantityRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RegularCustomerOrderQuantityRestriction : " + e.getMessage(), 0);
        }
    }


    public RegularCustomerOrderQuantityRestriction createRegularCustomerOrderQuantityRestriction(Map attributeValues)
    {
        return createRegularCustomerOrderQuantityRestriction(getSession().getSessionContext(), attributeValues);
    }


    public RegularCustomerOrderTotalRestriction createRegularCustomerOrderTotalRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedVoucherConstants.TC.REGULARCUSTOMERORDERTOTALRESTRICTION);
            return (RegularCustomerOrderTotalRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RegularCustomerOrderTotalRestriction : " + e.getMessage(), 0);
        }
    }


    public RegularCustomerOrderTotalRestriction createRegularCustomerOrderTotalRestriction(Map attributeValues)
    {
        return createRegularCustomerOrderTotalRestriction(getSession().getSessionContext(), attributeValues);
    }


    public Restriction createRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedVoucherConstants.TC.RESTRICTION);
            return (Restriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Restriction : " + e.getMessage(), 0);
        }
    }


    public Restriction createRestriction(Map attributeValues)
    {
        return createRestriction(getSession().getSessionContext(), attributeValues);
    }


    public SerialVoucher createSerialVoucher(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedVoucherConstants.TC.SERIALVOUCHER);
            return (SerialVoucher)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SerialVoucher : " + e.getMessage(), 0);
        }
    }


    public SerialVoucher createSerialVoucher(Map attributeValues)
    {
        return createSerialVoucher(getSession().getSessionContext(), attributeValues);
    }


    public UserRestriction createUserRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedVoucherConstants.TC.USERRESTRICTION);
            return (UserRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating UserRestriction : " + e.getMessage(), 0);
        }
    }


    public UserRestriction createUserRestriction(Map attributeValues)
    {
        return createUserRestriction(getSession().getSessionContext(), attributeValues);
    }


    public VoucherInvalidation createVoucherInvalidation(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedVoucherConstants.TC.VOUCHERINVALIDATION);
            return (VoucherInvalidation)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating VoucherInvalidation : " + e.getMessage(), 0);
        }
    }


    public VoucherInvalidation createVoucherInvalidation(Map attributeValues)
    {
        return createVoucherInvalidation(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "voucher";
    }
}
