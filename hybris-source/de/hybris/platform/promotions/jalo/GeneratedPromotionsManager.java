package de.hybris.platform.promotions.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.promotions.constants.GeneratedPromotionsConstants;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedPromotionsManager extends Extension
{
    protected static final OneToManyHandler<PromotionResult> ORDER2PROMOTIONRESULTSRELATIONALLPROMOTIONRESULTSHANDLER = new OneToManyHandler(GeneratedPromotionsConstants.TC.PROMOTIONRESULT, true, "order", null, false, true, 1);
    protected static String PRODUCTPROMOTIONRELATION_SRC_ORDERED = "relation.ProductPromotionRelation.source.ordered";
    protected static String PRODUCTPROMOTIONRELATION_TGT_ORDERED = "relation.ProductPromotionRelation.target.ordered";
    protected static String PRODUCTPROMOTIONRELATION_MARKMODIFIED = "relation.ProductPromotionRelation.markmodified";
    protected static String CATEGORYPROMOTIONRELATION_SRC_ORDERED = "relation.CategoryPromotionRelation.source.ordered";
    protected static String CATEGORYPROMOTIONRELATION_TGT_ORDERED = "relation.CategoryPromotionRelation.target.ordered";
    protected static String CATEGORYPROMOTIONRELATION_MARKMODIFIED = "relation.CategoryPromotionRelation.markmodified";
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("previousDeliveryMode", Item.AttributeMode.INITIAL);
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


    public Set<PromotionResult> getAllPromotionResults(SessionContext ctx, AbstractOrder item)
    {
        return (Set<PromotionResult>)ORDER2PROMOTIONRESULTSRELATIONALLPROMOTIONRESULTSHANDLER.getValues(ctx, (Item)item);
    }


    public Set<PromotionResult> getAllPromotionResults(AbstractOrder item)
    {
        return getAllPromotionResults(getSession().getSessionContext(), item);
    }


    public void setAllPromotionResults(SessionContext ctx, AbstractOrder item, Set<PromotionResult> value)
    {
        ORDER2PROMOTIONRESULTSRELATIONALLPROMOTIONRESULTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setAllPromotionResults(AbstractOrder item, Set<PromotionResult> value)
    {
        setAllPromotionResults(getSession().getSessionContext(), item, value);
    }


    public void addToAllPromotionResults(SessionContext ctx, AbstractOrder item, PromotionResult value)
    {
        ORDER2PROMOTIONRESULTSRELATIONALLPROMOTIONRESULTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToAllPromotionResults(AbstractOrder item, PromotionResult value)
    {
        addToAllPromotionResults(getSession().getSessionContext(), item, value);
    }


    public void removeFromAllPromotionResults(SessionContext ctx, AbstractOrder item, PromotionResult value)
    {
        ORDER2PROMOTIONRESULTSRELATIONALLPROMOTIONRESULTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromAllPromotionResults(AbstractOrder item, PromotionResult value)
    {
        removeFromAllPromotionResults(getSession().getSessionContext(), item, value);
    }


    public AbstractPromotionAction createAbstractPromotionAction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.ABSTRACTPROMOTIONACTION);
            return (AbstractPromotionAction)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating AbstractPromotionAction : " + e.getMessage(), 0);
        }
    }


    public AbstractPromotionAction createAbstractPromotionAction(Map attributeValues)
    {
        return createAbstractPromotionAction(getSession().getSessionContext(), attributeValues);
    }


    public AbstractPromotionRestriction createAbstractPromotionRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.ABSTRACTPROMOTIONRESTRICTION);
            return (AbstractPromotionRestriction)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating AbstractPromotionRestriction : " + e.getMessage(), 0);
        }
    }


    public AbstractPromotionRestriction createAbstractPromotionRestriction(Map attributeValues)
    {
        return createAbstractPromotionRestriction(getSession().getSessionContext(), attributeValues);
    }


    public CachedPromotionNullAction createCachedPromotionNullAction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.CACHEDPROMOTIONNULLACTION);
            return (CachedPromotionNullAction)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CachedPromotionNullAction : " + e.getMessage(), 0);
        }
    }


    public CachedPromotionNullAction createCachedPromotionNullAction(Map attributeValues)
    {
        return createCachedPromotionNullAction(getSession().getSessionContext(), attributeValues);
    }


    public CachedPromotionOrderAddFreeGiftAction createCachedPromotionOrderAddFreeGiftAction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.CACHEDPROMOTIONORDERADDFREEGIFTACTION);
            return (CachedPromotionOrderAddFreeGiftAction)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CachedPromotionOrderAddFreeGiftAction : " + e.getMessage(), 0);
        }
    }


    public CachedPromotionOrderAddFreeGiftAction createCachedPromotionOrderAddFreeGiftAction(Map attributeValues)
    {
        return createCachedPromotionOrderAddFreeGiftAction(getSession().getSessionContext(), attributeValues);
    }


    public CachedPromotionOrderAdjustTotalAction createCachedPromotionOrderAdjustTotalAction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.CACHEDPROMOTIONORDERADJUSTTOTALACTION);
            return (CachedPromotionOrderAdjustTotalAction)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CachedPromotionOrderAdjustTotalAction : " + e.getMessage(), 0);
        }
    }


    public CachedPromotionOrderAdjustTotalAction createCachedPromotionOrderAdjustTotalAction(Map attributeValues)
    {
        return createCachedPromotionOrderAdjustTotalAction(getSession().getSessionContext(), attributeValues);
    }


    public CachedPromotionOrderChangeDeliveryModeAction createCachedPromotionOrderChangeDeliveryModeAction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.CACHEDPROMOTIONORDERCHANGEDELIVERYMODEACTION);
            return (CachedPromotionOrderChangeDeliveryModeAction)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CachedPromotionOrderChangeDeliveryModeAction : " + e.getMessage(), 0);
        }
    }


    public CachedPromotionOrderChangeDeliveryModeAction createCachedPromotionOrderChangeDeliveryModeAction(Map attributeValues)
    {
        return createCachedPromotionOrderChangeDeliveryModeAction(getSession().getSessionContext(), attributeValues);
    }


    public CachedPromotionOrderEntryAdjustAction createCachedPromotionOrderEntryAdjustAction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.CACHEDPROMOTIONORDERENTRYADJUSTACTION);
            return (CachedPromotionOrderEntryAdjustAction)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CachedPromotionOrderEntryAdjustAction : " + e.getMessage(), 0);
        }
    }


    public CachedPromotionOrderEntryAdjustAction createCachedPromotionOrderEntryAdjustAction(Map attributeValues)
    {
        return createCachedPromotionOrderEntryAdjustAction(getSession().getSessionContext(), attributeValues);
    }


    public CachedPromotionOrderEntryConsumed createCachedPromotionOrderEntryConsumed(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.CACHEDPROMOTIONORDERENTRYCONSUMED);
            return (CachedPromotionOrderEntryConsumed)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CachedPromotionOrderEntryConsumed : " + e.getMessage(), 0);
        }
    }


    public CachedPromotionOrderEntryConsumed createCachedPromotionOrderEntryConsumed(Map attributeValues)
    {
        return createCachedPromotionOrderEntryConsumed(getSession().getSessionContext(), attributeValues);
    }


    public CachedPromotionResult createCachedPromotionResult(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.CACHEDPROMOTIONRESULT);
            return (CachedPromotionResult)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CachedPromotionResult : " + e.getMessage(), 0);
        }
    }


    public CachedPromotionResult createCachedPromotionResult(Map attributeValues)
    {
        return createCachedPromotionResult(getSession().getSessionContext(), attributeValues);
    }


    public OrderPromotion createOrderPromotion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.ORDERPROMOTION);
            return (OrderPromotion)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating OrderPromotion : " + e.getMessage(), 0);
        }
    }


    public OrderPromotion createOrderPromotion(Map attributeValues)
    {
        return createOrderPromotion(getSession().getSessionContext(), attributeValues);
    }


    public OrderThresholdChangeDeliveryModePromotion createOrderThresholdChangeDeliveryModePromotion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.ORDERTHRESHOLDCHANGEDELIVERYMODEPROMOTION);
            return (OrderThresholdChangeDeliveryModePromotion)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating OrderThresholdChangeDeliveryModePromotion : " + e.getMessage(), 0);
        }
    }


    public OrderThresholdChangeDeliveryModePromotion createOrderThresholdChangeDeliveryModePromotion(Map attributeValues)
    {
        return createOrderThresholdChangeDeliveryModePromotion(getSession().getSessionContext(), attributeValues);
    }


    public OrderThresholdDiscountPromotion createOrderThresholdDiscountPromotion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.ORDERTHRESHOLDDISCOUNTPROMOTION);
            return (OrderThresholdDiscountPromotion)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating OrderThresholdDiscountPromotion : " + e.getMessage(), 0);
        }
    }


    public OrderThresholdDiscountPromotion createOrderThresholdDiscountPromotion(Map attributeValues)
    {
        return createOrderThresholdDiscountPromotion(getSession().getSessionContext(), attributeValues);
    }


    public OrderThresholdFreeGiftPromotion createOrderThresholdFreeGiftPromotion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.ORDERTHRESHOLDFREEGIFTPROMOTION);
            return (OrderThresholdFreeGiftPromotion)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating OrderThresholdFreeGiftPromotion : " + e.getMessage(), 0);
        }
    }


    public OrderThresholdFreeGiftPromotion createOrderThresholdFreeGiftPromotion(Map attributeValues)
    {
        return createOrderThresholdFreeGiftPromotion(getSession().getSessionContext(), attributeValues);
    }


    public OrderThresholdFreeVoucherPromotion createOrderThresholdFreeVoucherPromotion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.ORDERTHRESHOLDFREEVOUCHERPROMOTION);
            return (OrderThresholdFreeVoucherPromotion)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating OrderThresholdFreeVoucherPromotion : " + e.getMessage(), 0);
        }
    }


    public OrderThresholdFreeVoucherPromotion createOrderThresholdFreeVoucherPromotion(Map attributeValues)
    {
        return createOrderThresholdFreeVoucherPromotion(getSession().getSessionContext(), attributeValues);
    }


    public OrderThresholdPerfectPartnerPromotion createOrderThresholdPerfectPartnerPromotion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.ORDERTHRESHOLDPERFECTPARTNERPROMOTION);
            return (OrderThresholdPerfectPartnerPromotion)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating OrderThresholdPerfectPartnerPromotion : " + e.getMessage(), 0);
        }
    }


    public OrderThresholdPerfectPartnerPromotion createOrderThresholdPerfectPartnerPromotion(Map attributeValues)
    {
        return createOrderThresholdPerfectPartnerPromotion(getSession().getSessionContext(), attributeValues);
    }


    public ProductBOGOFPromotion createProductBOGOFPromotion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PRODUCTBOGOFPROMOTION);
            return (ProductBOGOFPromotion)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ProductBOGOFPromotion : " + e.getMessage(), 0);
        }
    }


    public ProductBOGOFPromotion createProductBOGOFPromotion(Map attributeValues)
    {
        return createProductBOGOFPromotion(getSession().getSessionContext(), attributeValues);
    }


    public ProductBundlePromotion createProductBundlePromotion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PRODUCTBUNDLEPROMOTION);
            return (ProductBundlePromotion)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ProductBundlePromotion : " + e.getMessage(), 0);
        }
    }


    public ProductBundlePromotion createProductBundlePromotion(Map attributeValues)
    {
        return createProductBundlePromotion(getSession().getSessionContext(), attributeValues);
    }


    public ProductFixedPricePromotion createProductFixedPricePromotion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PRODUCTFIXEDPRICEPROMOTION);
            return (ProductFixedPricePromotion)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ProductFixedPricePromotion : " + e.getMessage(), 0);
        }
    }


    public ProductFixedPricePromotion createProductFixedPricePromotion(Map attributeValues)
    {
        return createProductFixedPricePromotion(getSession().getSessionContext(), attributeValues);
    }


    public ProductMultiBuyPromotion createProductMultiBuyPromotion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PRODUCTMULTIBUYPROMOTION);
            return (ProductMultiBuyPromotion)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ProductMultiBuyPromotion : " + e.getMessage(), 0);
        }
    }


    public ProductMultiBuyPromotion createProductMultiBuyPromotion(Map attributeValues)
    {
        return createProductMultiBuyPromotion(getSession().getSessionContext(), attributeValues);
    }


    public ProductOneToOnePerfectPartnerPromotion createProductOneToOnePerfectPartnerPromotion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PRODUCTONETOONEPERFECTPARTNERPROMOTION);
            return (ProductOneToOnePerfectPartnerPromotion)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ProductOneToOnePerfectPartnerPromotion : " + e.getMessage(), 0);
        }
    }


    public ProductOneToOnePerfectPartnerPromotion createProductOneToOnePerfectPartnerPromotion(Map attributeValues)
    {
        return createProductOneToOnePerfectPartnerPromotion(getSession().getSessionContext(), attributeValues);
    }


    public ProductPercentageDiscountPromotion createProductPercentageDiscountPromotion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PRODUCTPERCENTAGEDISCOUNTPROMOTION);
            return (ProductPercentageDiscountPromotion)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ProductPercentageDiscountPromotion : " + e.getMessage(), 0);
        }
    }


    public ProductPercentageDiscountPromotion createProductPercentageDiscountPromotion(Map attributeValues)
    {
        return createProductPercentageDiscountPromotion(getSession().getSessionContext(), attributeValues);
    }


    public ProductPerfectPartnerBundlePromotion createProductPerfectPartnerBundlePromotion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PRODUCTPERFECTPARTNERBUNDLEPROMOTION);
            return (ProductPerfectPartnerBundlePromotion)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ProductPerfectPartnerBundlePromotion : " + e.getMessage(), 0);
        }
    }


    public ProductPerfectPartnerBundlePromotion createProductPerfectPartnerBundlePromotion(Map attributeValues)
    {
        return createProductPerfectPartnerBundlePromotion(getSession().getSessionContext(), attributeValues);
    }


    public ProductPerfectPartnerPromotion createProductPerfectPartnerPromotion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PRODUCTPERFECTPARTNERPROMOTION);
            return (ProductPerfectPartnerPromotion)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ProductPerfectPartnerPromotion : " + e.getMessage(), 0);
        }
    }


    public ProductPerfectPartnerPromotion createProductPerfectPartnerPromotion(Map attributeValues)
    {
        return createProductPerfectPartnerPromotion(getSession().getSessionContext(), attributeValues);
    }


    public ProductPromotion createProductPromotion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PRODUCTPROMOTION);
            return (ProductPromotion)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ProductPromotion : " + e.getMessage(), 0);
        }
    }


    public ProductPromotion createProductPromotion(Map attributeValues)
    {
        return createProductPromotion(getSession().getSessionContext(), attributeValues);
    }


    public ProductSteppedMultiBuyPromotion createProductSteppedMultiBuyPromotion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PRODUCTSTEPPEDMULTIBUYPROMOTION);
            return (ProductSteppedMultiBuyPromotion)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ProductSteppedMultiBuyPromotion : " + e.getMessage(), 0);
        }
    }


    public ProductSteppedMultiBuyPromotion createProductSteppedMultiBuyPromotion(Map attributeValues)
    {
        return createProductSteppedMultiBuyPromotion(getSession().getSessionContext(), attributeValues);
    }


    public PromotionGroup createPromotionGroup(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PROMOTIONGROUP);
            return (PromotionGroup)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating PromotionGroup : " + e.getMessage(), 0);
        }
    }


    public PromotionGroup createPromotionGroup(Map attributeValues)
    {
        return createPromotionGroup(getSession().getSessionContext(), attributeValues);
    }


    public PromotionNullAction createPromotionNullAction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PROMOTIONNULLACTION);
            return (PromotionNullAction)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating PromotionNullAction : " + e.getMessage(), 0);
        }
    }


    public PromotionNullAction createPromotionNullAction(Map attributeValues)
    {
        return createPromotionNullAction(getSession().getSessionContext(), attributeValues);
    }


    public PromotionOrderAddFreeGiftAction createPromotionOrderAddFreeGiftAction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PROMOTIONORDERADDFREEGIFTACTION);
            return (PromotionOrderAddFreeGiftAction)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating PromotionOrderAddFreeGiftAction : " + e.getMessage(), 0);
        }
    }


    public PromotionOrderAddFreeGiftAction createPromotionOrderAddFreeGiftAction(Map attributeValues)
    {
        return createPromotionOrderAddFreeGiftAction(getSession().getSessionContext(), attributeValues);
    }


    public PromotionOrderAdjustTotalAction createPromotionOrderAdjustTotalAction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PROMOTIONORDERADJUSTTOTALACTION);
            return (PromotionOrderAdjustTotalAction)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating PromotionOrderAdjustTotalAction : " + e.getMessage(), 0);
        }
    }


    public PromotionOrderAdjustTotalAction createPromotionOrderAdjustTotalAction(Map attributeValues)
    {
        return createPromotionOrderAdjustTotalAction(getSession().getSessionContext(), attributeValues);
    }


    public PromotionOrderChangeDeliveryModeAction createPromotionOrderChangeDeliveryModeAction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PROMOTIONORDERCHANGEDELIVERYMODEACTION);
            return (PromotionOrderChangeDeliveryModeAction)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating PromotionOrderChangeDeliveryModeAction : " + e.getMessage(), 0);
        }
    }


    public PromotionOrderChangeDeliveryModeAction createPromotionOrderChangeDeliveryModeAction(Map attributeValues)
    {
        return createPromotionOrderChangeDeliveryModeAction(getSession().getSessionContext(), attributeValues);
    }


    public PromotionOrderEntryAdjustAction createPromotionOrderEntryAdjustAction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PROMOTIONORDERENTRYADJUSTACTION);
            return (PromotionOrderEntryAdjustAction)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating PromotionOrderEntryAdjustAction : " + e.getMessage(), 0);
        }
    }


    public PromotionOrderEntryAdjustAction createPromotionOrderEntryAdjustAction(Map attributeValues)
    {
        return createPromotionOrderEntryAdjustAction(getSession().getSessionContext(), attributeValues);
    }


    public PromotionOrderEntryConsumed createPromotionOrderEntryConsumed(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PROMOTIONORDERENTRYCONSUMED);
            return (PromotionOrderEntryConsumed)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating PromotionOrderEntryConsumed : " + e.getMessage(), 0);
        }
    }


    public PromotionOrderEntryConsumed createPromotionOrderEntryConsumed(Map attributeValues)
    {
        return createPromotionOrderEntryConsumed(getSession().getSessionContext(), attributeValues);
    }


    public PromotionPriceRow createPromotionPriceRow(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PROMOTIONPRICEROW);
            return (PromotionPriceRow)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating PromotionPriceRow : " + e.getMessage(), 0);
        }
    }


    public PromotionPriceRow createPromotionPriceRow(Map attributeValues)
    {
        return createPromotionPriceRow(getSession().getSessionContext(), attributeValues);
    }


    public PromotionProductRestriction createPromotionProductRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PROMOTIONPRODUCTRESTRICTION);
            return (PromotionProductRestriction)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating PromotionProductRestriction : " + e.getMessage(), 0);
        }
    }


    public PromotionProductRestriction createPromotionProductRestriction(Map attributeValues)
    {
        return createPromotionProductRestriction(getSession().getSessionContext(), attributeValues);
    }


    public PromotionQuantityAndPricesRow createPromotionQuantityAndPricesRow(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PROMOTIONQUANTITYANDPRICESROW);
            return (PromotionQuantityAndPricesRow)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating PromotionQuantityAndPricesRow : " + e.getMessage(), 0);
        }
    }


    public PromotionQuantityAndPricesRow createPromotionQuantityAndPricesRow(Map attributeValues)
    {
        return createPromotionQuantityAndPricesRow(getSession().getSessionContext(), attributeValues);
    }


    public PromotionResult createPromotionResult(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PROMOTIONRESULT);
            return (PromotionResult)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating PromotionResult : " + e.getMessage(), 0);
        }
    }


    public PromotionResult createPromotionResult(Map attributeValues)
    {
        return createPromotionResult(getSession().getSessionContext(), attributeValues);
    }


    public PromotionUserRestriction createPromotionUserRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPromotionsConstants.TC.PROMOTIONUSERRESTRICTION);
            return (PromotionUserRestriction)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating PromotionUserRestriction : " + e.getMessage(), 0);
        }
    }


    public PromotionUserRestriction createPromotionUserRestriction(Map attributeValues)
    {
        return createPromotionUserRestriction(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "promotions";
    }


    public DeliveryMode getPreviousDeliveryMode(SessionContext ctx, AbstractOrder item)
    {
        return (DeliveryMode)item.getProperty(ctx, GeneratedPromotionsConstants.Attributes.AbstractOrder.PREVIOUSDELIVERYMODE);
    }


    public DeliveryMode getPreviousDeliveryMode(AbstractOrder item)
    {
        return getPreviousDeliveryMode(getSession().getSessionContext(), item);
    }


    public void setPreviousDeliveryMode(SessionContext ctx, AbstractOrder item, DeliveryMode value)
    {
        item.setProperty(ctx, GeneratedPromotionsConstants.Attributes.AbstractOrder.PREVIOUSDELIVERYMODE, value);
    }


    public void setPreviousDeliveryMode(AbstractOrder item, DeliveryMode value)
    {
        setPreviousDeliveryMode(getSession().getSessionContext(), item, value);
    }


    public Collection<ProductPromotion> getPromotions(SessionContext ctx, Product item)
    {
        List<ProductPromotion> items = item.getLinkedItems(ctx, true, GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION, "ProductPromotion", null, false, false);
        return items;
    }


    public Collection<ProductPromotion> getPromotions(Product item)
    {
        return getPromotions(getSession().getSessionContext(), item);
    }


    public long getPromotionsCount(SessionContext ctx, Product item)
    {
        return item.getLinkedItemsCount(ctx, true, GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION, "ProductPromotion", null);
    }


    public long getPromotionsCount(Product item)
    {
        return getPromotionsCount(getSession().getSessionContext(), item);
    }


    public void setPromotions(SessionContext ctx, Product item, Collection<ProductPromotion> value)
    {
        item.setLinkedItems(ctx, true, GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(PRODUCTPROMOTIONRELATION_MARKMODIFIED));
    }


    public void setPromotions(Product item, Collection<ProductPromotion> value)
    {
        setPromotions(getSession().getSessionContext(), item, value);
    }


    public void addToPromotions(SessionContext ctx, Product item, ProductPromotion value)
    {
        item.addLinkedItems(ctx, true, GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRODUCTPROMOTIONRELATION_MARKMODIFIED));
    }


    public void addToPromotions(Product item, ProductPromotion value)
    {
        addToPromotions(getSession().getSessionContext(), item, value);
    }


    public void removeFromPromotions(SessionContext ctx, Product item, ProductPromotion value)
    {
        item.removeLinkedItems(ctx, true, GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRODUCTPROMOTIONRELATION_MARKMODIFIED));
    }


    public void removeFromPromotions(Product item, ProductPromotion value)
    {
        removeFromPromotions(getSession().getSessionContext(), item, value);
    }


    public Collection<ProductPromotion> getPromotions(SessionContext ctx, Category item)
    {
        List<ProductPromotion> items = item.getLinkedItems(ctx, true, GeneratedPromotionsConstants.Relations.CATEGORYPROMOTIONRELATION, "ProductPromotion", null, false, false);
        return items;
    }


    public Collection<ProductPromotion> getPromotions(Category item)
    {
        return getPromotions(getSession().getSessionContext(), item);
    }


    public long getPromotionsCount(SessionContext ctx, Category item)
    {
        return item.getLinkedItemsCount(ctx, true, GeneratedPromotionsConstants.Relations.CATEGORYPROMOTIONRELATION, "ProductPromotion", null);
    }


    public long getPromotionsCount(Category item)
    {
        return getPromotionsCount(getSession().getSessionContext(), item);
    }


    public void setPromotions(SessionContext ctx, Category item, Collection<ProductPromotion> value)
    {
        item.setLinkedItems(ctx, true, GeneratedPromotionsConstants.Relations.CATEGORYPROMOTIONRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(CATEGORYPROMOTIONRELATION_MARKMODIFIED));
    }


    public void setPromotions(Category item, Collection<ProductPromotion> value)
    {
        setPromotions(getSession().getSessionContext(), item, value);
    }


    public void addToPromotions(SessionContext ctx, Category item, ProductPromotion value)
    {
        item.addLinkedItems(ctx, true, GeneratedPromotionsConstants.Relations.CATEGORYPROMOTIONRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CATEGORYPROMOTIONRELATION_MARKMODIFIED));
    }


    public void addToPromotions(Category item, ProductPromotion value)
    {
        addToPromotions(getSession().getSessionContext(), item, value);
    }


    public void removeFromPromotions(SessionContext ctx, Category item, ProductPromotion value)
    {
        item.removeLinkedItems(ctx, true, GeneratedPromotionsConstants.Relations.CATEGORYPROMOTIONRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CATEGORYPROMOTIONRELATION_MARKMODIFIED));
    }


    public void removeFromPromotions(Category item, ProductPromotion value)
    {
        removeFromPromotions(getSession().getSessionContext(), item, value);
    }
}
