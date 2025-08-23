package de.hybris.platform.promotions.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.persistence.PolyglotPersistenceGenericItemSupport;
import de.hybris.platform.persistence.polyglot.TypeInfoFactory;
import de.hybris.platform.persistence.polyglot.config.TypeInfo;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.jalo.AbstractPromotionAction;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.model.AbstractPromotionActionModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.promotions.model.PromotionOrderEntryConsumedModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import de.hybris.platform.promotions.util.CompositeProduct;
import de.hybris.platform.promotions.util.Helper;
import de.hybris.platform.promotions.util.legacy.LegacyModeChecker;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.jeeapi.YNoSuchEntityException;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.voucher.jalo.Voucher;
import de.hybris.platform.voucher.jalo.util.VoucherValue;
import de.hybris.platform.voucher.model.VoucherModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPromotionsService extends AbstractPromotionsService implements PromotionsService
{
    private static final Logger LOG = Logger.getLogger(DefaultPromotionsService.class);
    private final Comparator<PromotionResultModel> promotionResultComparator = (Comparator<PromotionResultModel>)new PromotionResultByPromotionPriorityComparator();
    private transient FlexibleSearchService flexibleSearchService;
    private transient CalculationService calculationService;
    private transient LegacyModeChecker legacyModeChecker;
    protected static final String PROMOTION_RESULTS_QUERY = "SELECT {pr:" + Item.PK + "},{promo:priority} FROM   {PromotionResult as pr LEFT JOIN AbstractPromotion AS promo ON {pr:promotion}={promo:pk} } WHERE  {pr:order} = ?order ORDER BY {promo:priority} DESC";
    protected static final String ORDER_PROMOTIONS_QUERY = "SELECT DISTINCT {promo:pk}, {promo:priority} FROM {OrderPromotion as promo } WHERE (   {promo:PromotionGroup} IN (?promotionGroups) ) AND (   {promo:enabled}=1 AND {promo:startDate} <= ?now AND ?now <= {promo:endDate} ) ORDER BY {promo:priority} DESC";
    protected static final String PRODUCT_PROMOTIONS_QUERY = "SELECT DISTINCT pprom.pk, pprom.prio FROM ( {{ SELECT {p.pk} as pk, {p.priority} as prio FROM {ProductPromotion AS p JOIN ProductPromotionRelation AS p2p ON {p.pk} = {p2p.target} AND {p2p.source} = ?product } WHERE {p.PromotionGroup} IN (?promotionGroups) AND {p.enabled} =?true AND {p.startDate} <= ?now AND ?now <= {p.endDate} }}";
    protected static final String PRODUCT_PROMTIONS_QUERY_UNION = " UNION {{ SELECT {p.pk} as pk, {p.priority} as prio FROM {ProductPromotion AS p JOIN CategoryPromotionRelation AS c2p ON {p.pk} = {c2p.target} AND {c2p.source} IN (?productCategories) } WHERE {p.PromotionGroup} IN (?promotionGroups) AND {p.enabled} =?true AND {p.startDate} <= ?now AND ?now <= {p.endDate} }}";
    protected static final String PROMOTION_GROUP_QUERY = "SELECT {" + Item.PK + "} FROM {PromotionGroup} WHERE {Identifier} = ?identifier";
    protected static final String PRODUCT_PROMOTIONS_QUERY_DESC = " )pprom ORDER BY pprom.prio DESC";
    protected static final String SORTED_ORDER_AND_PRODUCT_PROMOTIONS_QUERY_START = "SELECT DISTINCT pprom.pk, pprom.prio FROM (";
    protected static final String SORTED_ORDER_AND_PRODUCT_PROMOTIONS_QUERY_PRODUCTS_PART = " {{ SELECT {p.pk} as pk, {p.priority} as prio FROM {ProductPromotion AS p JOIN ProductPromotionRelation AS p2p ON {p.pk} = {p2p.target} AND {p2p.source} in (?products) } WHERE {p.PromotionGroup} IN (?promotionGroups) AND {p.enabled} =?true AND {p.startDate} <= ?now AND ?now <= {p.endDate} }}";
    protected static final String SORTED_ORDER_AND_PRODUCT_PROMOTIONS_QUERY_CATEGORIES_PART = " UNION {{ SELECT {p.pk} as pk, {p.priority} as prio FROM {ProductPromotion AS p JOIN CategoryPromotionRelation AS c2p ON {p.pk} = {c2p.target} AND {c2p.source} IN (?productCategories) } WHERE {p.PromotionGroup} IN (?promotionGroups) AND {p.enabled} =?true AND {p.startDate} <= ?now AND ?now <= {p.endDate} }}";
    protected static final String SORTED_ORDER_AND_PRODUCT_PROMOTIONS_QUERY_UNION_ALL_PART = " UNION ALL ";
    protected static final String SORTED_ORDER_AND_PRODUCT_PROMOTIONS_QUERY_ORDER_PART = "{{ SELECT {p3:pk}, {p3.priority} as prio FROM {OrderPromotion as p3} WHERE {p3.PromotionGroup} IN (?promotionGroups) AND {p3.enabled} =?true AND {p3.startDate} <= ?now AND ?now <= {p3.endDate} }} )pprom ORDER BY pprom.prio DESC";


    protected PromotionsManager getPromotionsManager()
    {
        return PromotionsManager.getInstance();
    }


    protected JaloSession getSession()
    {
        return JaloSession.getCurrentSession();
    }


    public void cleanupCart(CartModel cart)
    {
        checkLegacyMode();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("cleanupCart for [" + cart + "]");
        }
        if(cart != null)
        {
            deleteStoredPromotionResults((AbstractOrderModel)cart, false);
        }
    }


    protected void deleteStoredPromotionResults(AbstractOrderModel order, boolean undoActions)
    {
        boolean calculateTotals = false;
        synchronized(getOrder(order).getSyncObject())
        {
            for(PromotionResultModel result : getPromotionResultsInternal(order))
            {
                try
                {
                    if(undoActions)
                    {
                        calculateTotals |= getResult(result).undo(getSessionContext());
                    }
                    getResult(result).remove(getSessionContext());
                }
                catch(ConsistencyCheckException ccEx)
                {
                    LOG.error("deleteStoredPromotionResult failed to undo and remove result [" + result + "]", (Throwable)ccEx);
                }
                catch(YNoSuchEntityException noEntity)
                {
                    LOG.error("deleteStoredPromotionResult failed to undo and remove result", (Throwable)noEntity);
                }
            }
        }
        if(calculateTotals)
        {
            try
            {
                refreshOrder(order);
                getCalculationService().calculateTotals(order, true);
            }
            catch(CalculationException ex)
            {
                LOG.error("deleteStoredPromotionResult failed to calculateTotals on order [" + order + "]", (Throwable)ex);
            }
        }
    }


    protected List<PromotionResultModel> getPromotionResultsInternal(AbstractOrderModel order)
    {
        try
        {
            if(order != null)
            {
                if(usingPolyglotPersistence())
                {
                    Set<PromotionResultModel> allPromotions = order.getAllPromotionResults();
                    return (List<PromotionResultModel>)allPromotions.stream().filter(Objects::nonNull).sorted(sortByPromotionPriority())
                                    .collect(Collectors.toList());
                }
                FlexibleSearchQuery query = new FlexibleSearchQuery(PROMOTION_RESULTS_QUERY);
                query.setCount(-1);
                query.setStart(0);
                query.setFailOnUnknownFields(true);
                query.setNeedTotal(true);
                query.setResultClassList(Collections.singletonList(PromotionResultModel.class));
                query.addQueryParameter("order", order);
                return getFlexibleSearchService().search(query).getResult();
            }
        }
        catch(Exception ex)
        {
            LOG.error("Failed to getPromotionResultsInternal", ex);
        }
        return new ArrayList<>(0);
    }


    protected boolean usingPolyglotPersistence()
    {
        TypeInfo typeInfoPR = TypeInfoFactory.getTypeInfo(5013);
        TypeInfo typeInfoAP = TypeInfoFactory.getTypeInfo(5010);
        Tenant currentTenant = getPromotionsManager().getTenant();
        return (PolyglotPersistenceGenericItemSupport.isFullyBackedByThePolyglotPersistence(currentTenant, typeInfoPR) &&
                        PolyglotPersistenceGenericItemSupport.isFullyBackedByThePolyglotPersistence(currentTenant, typeInfoAP));
    }


    protected Comparator<PromotionResultModel> sortByPromotionPriority()
    {
        return this.promotionResultComparator;
    }


    public PromotionGroupModel getDefaultPromotionGroup()
    {
        checkLegacyMode();
        return getPromotionGroup("default");
    }


    public List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> promotionGroups)
    {
        checkLegacyMode();
        return getOrderPromotions(promotionGroups, true, null, null);
    }


    public List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> promotionGroups, Date date)
    {
        checkLegacyMode();
        return getOrderPromotions(promotionGroups, true, null, date);
    }


    public List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> promotionGroups, ProductModel product)
    {
        checkLegacyMode();
        return getOrderPromotions(promotionGroups, true, product, null);
    }


    public List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> promotionGroups, ProductModel product, Date date)
    {
        checkLegacyMode();
        return getOrderPromotions(promotionGroups, true, product, date);
    }


    public List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> promotionGroups, boolean evaluateRestrictions)
    {
        checkLegacyMode();
        return getOrderPromotions(promotionGroups, evaluateRestrictions, null, null);
    }


    public List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> promotionGroups, boolean evaluateRestrictions, Date date)
    {
        checkLegacyMode();
        return getOrderPromotions(promotionGroups, evaluateRestrictions, null, date);
    }


    public List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> promotionGroups, boolean evaluateRestrictions, ProductModel product)
    {
        checkLegacyMode();
        return getOrderPromotions(promotionGroups, evaluateRestrictions, product, null);
    }


    public List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> promotionGroups, boolean evaluateRestrictions, ProductModel product, Date date)
    {
        checkLegacyMode();
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("getOrderPromotions promotionGroups=[" + Helper.join(promotionGroups) + "] evaluateRestrictions=[" + evaluateRestrictions + "] product=[" + product + "] date=[" + date + "]");
            }
            if(promotionGroups != null && !promotionGroups.isEmpty())
            {
                if(date == null)
                {
                    date = Helper.getDateNowRoundedToMinute();
                }
                FlexibleSearchQuery query = new FlexibleSearchQuery(
                                "SELECT DISTINCT {promo:pk}, {promo:priority} FROM {OrderPromotion as promo } WHERE (   {promo:PromotionGroup} IN (?promotionGroups) ) AND (   {promo:enabled}=1 AND {promo:startDate} <= ?now AND ?now <= {promo:endDate} ) ORDER BY {promo:priority} DESC");
                query.setCount(-1);
                query.setStart(0);
                query.setFailOnUnknownFields(true);
                query.setNeedTotal(true);
                query.setResultClassList(Collections.singletonList(OrderPromotionModel.class));
                query.addQueryParameter("now", date);
                query.addQueryParameter("promotionGroups", promotionGroups);
                List<OrderPromotionModel> allPromotions = getFlexibleSearchService().search(query).getResult();
                List<OrderPromotionModel> availablePromotions = null;
                if(evaluateRestrictions)
                {
                    availablePromotions = filterPromotionsByRestrictions(allPromotions, product, date);
                }
                else
                {
                    availablePromotions = new ArrayList<>(allPromotions);
                }
                printPromotions(availablePromotions);
                return availablePromotions;
            }
        }
        catch(Exception ex)
        {
            LOG.error("Failed to getOrderPromotions", ex);
        }
        return new ArrayList<>(0);
    }


    private void printPromotions(List<OrderPromotionModel> availablePromotions)
    {
        if(LOG.isDebugEnabled())
        {
            for(OrderPromotionModel promotion : availablePromotions)
            {
                LOG.debug("getOrderPromotions available promotion [" + promotion + "]");
            }
        }
    }


    protected <T extends AbstractPromotionModel> List<T> filterPromotionsByRestrictions(List<T> allPromotions, ProductModel product, Date date)
    {
        ArrayList<T> availablePromotions = new ArrayList<>(allPromotions.size());
        for(AbstractPromotionModel abstractPromotionModel : allPromotions)
        {
            if(isSatifiedRestrictions(product, date, abstractPromotionModel))
            {
                availablePromotions.add((T)abstractPromotionModel);
            }
        }
        return availablePromotions;
    }


    private <T extends AbstractPromotionModel> boolean isSatifiedRestrictions(ProductModel product, Date date, T promotion)
    {
        boolean satifiedRestrictions = true;
        Collection<AbstractPromotionRestrictionModel> restrictions = promotion.getRestrictions();
        if(restrictions != null)
        {
            for(AbstractPromotionRestrictionModel restriction : restrictions)
            {
                AbstractPromotionRestriction.RestrictionResult result = ((AbstractPromotionRestriction)getModelService().getSource(restriction)).evaluate(getSessionContext(), (Product)getModelService().getSource(product), date, null);
                if(result == AbstractPromotionRestriction.RestrictionResult.DENY || result == AbstractPromotionRestriction.RestrictionResult.ADJUSTED_PRODUCTS)
                {
                    satifiedRestrictions = false;
                    break;
                }
            }
        }
        return satifiedRestrictions;
    }


    public List<ProductPromotionModel> getProductPromotions(Collection<PromotionGroupModel> promotionGroups, ProductModel product)
    {
        checkLegacyMode();
        return getProductPromotions(promotionGroups, product, true, null);
    }


    public List<ProductPromotionModel> getProductPromotions(Collection<PromotionGroupModel> promotionGroups, ProductModel product, boolean evaluateRestrictions, Date date)
    {
        checkLegacyMode();
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("getProductPromotions for [" + product + "] promotionGroups=[" + Helper.join(promotionGroups) + "] evaluateRestrictions=[" + evaluateRestrictions + "] date=[" + date + "]");
            }
            if(promotionGroups != null && product != null && !promotionGroups.isEmpty())
            {
                List<ProductPromotionModel> availablePromotions;
                if(date == null)
                {
                    date = Helper.getDateNowRoundedToMinute();
                }
                Map<String, Object> args = new HashMap<>();
                args.put("promotionGroups", promotionGroups);
                args.put("product", product);
                args.put("now", date);
                args.put("true", Boolean.TRUE);
                FlexibleSearchQuery query = new FlexibleSearchQuery(buildQueryForDistinctProductPromotionQuery(product, args), args);
                query.setCount(-1);
                query.setStart(0);
                query.setFailOnUnknownFields(true);
                query.setNeedTotal(true);
                query.setResultClassList(Collections.singletonList(ProductPromotionModel.class));
                List<ProductPromotionModel> allPromotions = getFlexibleSearchService().search(query).getResult();
                if(evaluateRestrictions)
                {
                    availablePromotions = filterPromotionsByRestrictions(allPromotions, product, date);
                }
                else
                {
                    availablePromotions = new ArrayList<>(allPromotions);
                }
                printPromotions(product, availablePromotions);
                return availablePromotions;
            }
        }
        catch(Exception ex)
        {
            LOG.error("Failed to getProductPromotions", ex);
        }
        return Collections.emptyList();
    }


    private void printPromotions(ProductModel product, List<ProductPromotionModel> availablePromotions)
    {
        if(LOG.isDebugEnabled())
        {
            for(ProductPromotionModel promotion : availablePromotions)
            {
                LOG.debug("getProductPromotions for [" + product + "] available promotion [" + promotion + "]");
            }
        }
    }


    protected String buildQueryForDistinctProductPromotionQuery(ProductModel product, Map<String, Object> args)
    {
        StringBuilder promQuery = new StringBuilder(
                        "SELECT DISTINCT pprom.pk, pprom.prio FROM ( {{ SELECT {p.pk} as pk, {p.priority} as prio FROM {ProductPromotion AS p JOIN ProductPromotionRelation AS p2p ON {p.pk} = {p2p.target} AND {p2p.source} = ?product } WHERE {p.PromotionGroup} IN (?promotionGroups) AND {p.enabled} =?true AND {p.startDate} <= ?now AND ?now <= {p.endDate} }}");
        Collection<CategoryModel> productCategories = product.getSupercategories();
        if(!productCategories.isEmpty())
        {
            promQuery.append(
                            " UNION {{ SELECT {p.pk} as pk, {p.priority} as prio FROM {ProductPromotion AS p JOIN CategoryPromotionRelation AS c2p ON {p.pk} = {c2p.target} AND {c2p.source} IN (?productCategories) } WHERE {p.PromotionGroup} IN (?promotionGroups) AND {p.enabled} =?true AND {p.startDate} <= ?now AND ?now <= {p.endDate} }}");
            Set<CategoryModel> productSuperCategories = new HashSet<>();
            for(CategoryModel cat : productCategories)
            {
                productSuperCategories.add(cat);
                productSuperCategories.addAll(cat.getAllSupercategories());
            }
            args.put("productCategories", productSuperCategories);
        }
        promQuery.append(" )pprom ORDER BY pprom.prio DESC");
        return promQuery.toString();
    }


    public List<? extends AbstractPromotionModel> getAbstractProductPromotions(Collection<PromotionGroupModel> promotionGroups, ProductModel product)
    {
        checkLegacyMode();
        return (List)getProductPromotions(promotionGroups, product);
    }


    public List<? extends AbstractPromotionModel> getAbstractProductPromotions(Collection<PromotionGroupModel> promotionGroups, ProductModel product, boolean evaluateRestrictions, Date date)
    {
        checkLegacyMode();
        return (List)getProductPromotions(promotionGroups, product, evaluateRestrictions, date);
    }


    public PromotionGroupModel getPromotionGroup(String identifier)
    {
        checkLegacyMode();
        FlexibleSearchQuery query = new FlexibleSearchQuery(PROMOTION_GROUP_QUERY);
        query.setCount(-1);
        query.setStart(0);
        query.setFailOnUnknownFields(true);
        query.setNeedTotal(true);
        query.setResultClassList(Collections.singletonList(PromotionGroupModel.class));
        query.addQueryParameter("identifier", identifier);
        List<PromotionGroupModel> results = getFlexibleSearchService().search(query).getResult();
        if(results != null && !results.isEmpty())
        {
            return results.get(0);
        }
        return null;
    }


    public PromotionOrderResults getPromotionResults(AbstractOrderModel order)
    {
        if(getModelService().isNew(order))
        {
            return new PromotionOrderResults(getSessionContext(), null, Collections.emptyList(), 0.0D);
        }
        checkLegacyMode();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("getPromotionResults for [" + order + "]");
        }
        List<PromotionResultModel> promotionResults = getPromotionResultsInternal(order);
        List<PromotionResultModel> validPromotionResults = new ArrayList<>(promotionResults.size());
        for(PromotionResultModel pr : promotionResults)
        {
            if(isPromotionResultValid(pr))
            {
                validPromotionResults.add(pr);
            }
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("getPromotionResults for [" + order + "] found [" + validPromotionResults.size() + "] promotion results");
        }
        return new PromotionOrderResults(getSessionContext(), getOrder(order), (List)
                        getModelService().getAllSources(validPromotionResults, new ArrayList()), 0.0D);
    }


    protected boolean isPromotionResultValid(PromotionResultModel result)
    {
        return (result.getPromotion() != null);
    }


    protected boolean isPromotionResultFired(PromotionResultModel result)
    {
        return (result.getCertainty().floatValue() >= 1.0F);
    }


    protected boolean isPromotionResultApplied(PromotionResultModel result)
    {
        if(isPromotionResultFired(result))
        {
            Collection<AbstractPromotionActionModel> actions = result.getActions();
            if(actions != null && !actions.isEmpty())
            {
                for(AbstractPromotionActionModel action : actions)
                {
                    if(!((AbstractPromotionAction)getModelService().getSource(action)).isMarkedApplied(getSessionContext()).booleanValue())
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }


    protected double getPromotionResultTotalDiscount(PromotionResultModel result)
    {
        double totalDiscount = 0.0D;
        Collection<AbstractPromotionActionModel> actions = result.getActions();
        if(actions != null)
        {
            for(AbstractPromotionActionModel action : actions)
            {
                totalDiscount += ((AbstractPromotionAction)getModelService().getSource(action)).getValue(getSessionContext());
            }
        }
        return totalDiscount;
    }


    protected String getDataUniqueKey(PromotionResultModel result)
    {
        AbstractPromotionModel promotion = result.getPromotion();
        if(promotion != null)
        {
            return getPromotionResultDataUniqueKey(result);
        }
        return null;
    }


    protected final String getPromotionResultDataUniqueKey(PromotionResultModel promotionResult)
    {
        StringBuilder builder = new StringBuilder(255);
        builder.append(promotionResult.getPromotion().getClass().getSimpleName()).append('|');
        builder.append(promotionResult.getPromotion().getCode()).append('|');
        buildPromotionResultDataUniqueKey(promotionResult, builder);
        return builder.toString();
    }


    protected void buildPromotionResultDataUniqueKey(PromotionResultModel promotionResult, StringBuilder builder)
    {
        builder.append(promotionResult.getCertainty()).append('|');
        builder.append(promotionResult.getCustom()).append('|');
        Collection<PromotionOrderEntryConsumedModel> entries = promotionResult.getConsumedEntries();
        if(entries != null && !entries.isEmpty())
        {
            for(PromotionOrderEntryConsumedModel entry : entries)
            {
                builder.append(entry.getOrderEntry().getProduct().getCode()).append(',');
                builder.append(entry.getQuantity()).append('|');
            }
        }
        Collection<AbstractPromotionActionModel> actions = promotionResult.getActions();
        if(actions != null && !actions.isEmpty())
        {
            for(AbstractPromotionActionModel action : actions)
            {
                builder.append(action.getClass().getSimpleName()).append('|');
            }
        }
    }


    protected double getTotalDiscount(PromotionResultModel result)
    {
        double totalDiscount = 0.0D;
        Collection<AbstractPromotionActionModel> actions = result.getActions();
        if(actions != null)
        {
            for(AbstractPromotionActionModel action : actions)
            {
                totalDiscount += ((AbstractPromotionAction)getModelService().getSource(action)).getValue(getSessionContext());
            }
        }
        return totalDiscount;
    }


    public PromotionOrderResults getPromotionResults(Collection<PromotionGroupModel> promotionGroups, AbstractOrderModel order, boolean evaluateRestrictions, PromotionsManager.AutoApplyMode productPromotionMode, PromotionsManager.AutoApplyMode orderPromotionMode, Date date)
    {
        checkLegacyMode();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("getPromotionResults for [" + order + "]");
        }
        List<PromotionResultModel> promotionResults = getPromotionResultsInternal(order);
        boolean needsUpdate = false;
        for(PromotionResultModel pr : promotionResults)
        {
            if(!isPromotionResultValid(pr))
            {
                needsUpdate = true;
                break;
            }
        }
        if(needsUpdate)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("getPromotionResults for [" + order + "] some of the promotions are invalid, rebuilding promotions");
            }
            return updatePromotions(promotionGroups, order, evaluateRestrictions, productPromotionMode, orderPromotionMode, date);
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("getPromotionResults for [" + order + "] found [" + promotionResults.size() + "] promotion results");
        }
        return new PromotionOrderResults(getSessionContext(), getOrder(order), (List)
                        getModelService().getAllSources(promotionResults, new ArrayList()), 0.0D);
    }


    public void transferPromotionsToOrder(AbstractOrderModel source, OrderModel target, boolean onlyTransferAppliedPromotions)
    {
        checkLegacyMode();
        saveIfModified(source);
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("transferPromotionsToOrder from [" + source + "] to [" + target + "] onlyTransferAppliedPromotions=[" + onlyTransferAppliedPromotions + "]");
                LOG.debug("Dump Source Order\r\n" + Helper.dumpOrder(getSessionContext(), getOrder(source)));
                LOG.debug("Dump Target Order\r\n" + Helper.dumpOrder(getSessionContext(), (AbstractOrder)getOrder(target)));
            }
            getPromotionResultsForTransfer(source, target, onlyTransferAppliedPromotions);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("transferPromotionsToOrder completed");
                LOG.debug("Dump Target Order after transfer\r\n" + Helper.dumpOrder(getSessionContext(), (AbstractOrder)getOrder(target)));
            }
        }
        catch(Exception ex)
        {
            LOG.error("Failed to transferPromotionsToOrder", ex);
        }
        List<ItemModel> toRefresh = CollectionUtils.isEmpty(target.getAllPromotionResults()) ? new ArrayList<>(1) : new ArrayList<>(target.getAllPromotionResults());
        toRefresh.add(target);
        refreshModifiedModelsAfter(toRefresh);
    }


    private void getPromotionResultsForTransfer(AbstractOrderModel source, OrderModel target, boolean onlyTransferAppliedPromotions)
    {
        List<PromotionResultModel> promotionResults = getPromotionResultsInternal(source);
        if(promotionResults != null && !promotionResults.isEmpty())
        {
            for(PromotionResultModel result : promotionResults)
            {
                if(!onlyTransferAppliedPromotions || isPromotionResultApplied(result))
                {
                    getResult(result).transferToOrder(getSessionContext(), getOrder(target));
                }
            }
        }
    }


    public PromotionOrderResults updatePromotions(Collection<PromotionGroupModel> promotionGroups, AbstractOrderModel order)
    {
        checkLegacyMode();
        saveIfModified(order);
        PromotionOrderResults result = updatePromotionsInternal(promotionGroups, order, true, PromotionsManager.AutoApplyMode.APPLY_ALL, PromotionsManager.AutoApplyMode.KEEP_APPLIED,
                        Helper.getDateNowRoundedToMinute());
        refreshOrder(order);
        return result;
    }


    public PromotionOrderResults updatePromotions(Collection<PromotionGroupModel> promotionGroups, AbstractOrderModel order, boolean evaluateRestrictions, PromotionsManager.AutoApplyMode productPromotionMode, PromotionsManager.AutoApplyMode orderPromotionMode, Date date)
    {
        checkLegacyMode();
        saveIfModified(order);
        PromotionOrderResults result = updatePromotionsInternal(promotionGroups, order, evaluateRestrictions, productPromotionMode, orderPromotionMode, date);
        refreshOrder(order);
        return result;
    }


    protected PromotionOrderResults updatePromotionsInternal(Collection<PromotionGroupModel> promotionGroups, AbstractOrderModel order, boolean evaluateRestrictions, PromotionsManager.AutoApplyMode productPromotionMode, PromotionsManager.AutoApplyMode orderPromotionMode, Date date)
    {
        synchronized(getOrder(order).getSyncObject())
        {
            return updatePromotionsNotThreadSafe(promotionGroups, order, evaluateRestrictions, productPromotionMode, orderPromotionMode, date);
        }
    }


    protected PromotionOrderResults updatePromotionsNotThreadSafe(Collection<PromotionGroupModel> promotionGroups, AbstractOrderModel order, boolean evaluateRestrictions, PromotionsManager.AutoApplyMode productPromotionMode, PromotionsManager.AutoApplyMode orderPromotionMode, Date date)
    {
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("updatePromotions for [" + order + "] promotionGroups=[" + Helper.join(promotionGroups) + "] evaluateRestrictions=[" + evaluateRestrictions + "] productPromotionMode=[" + productPromotionMode + "] orderPromotionMode=[" + orderPromotionMode + "] date=[" + date + "]");
            }
            if(promotionGroups != null && order != null)
            {
                if(date == null)
                {
                    date = Helper.getDateNowRoundedToMinute();
                }
                refreshOrder(order);
                if(!order.getCalculated().booleanValue())
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("updatePromotions order [" + order + "] not calculated, calculating");
                    }
                    getCalculationService().calculate(order, date);
                }
                List<String> promotionResultsToKeepApplied = new ArrayList<>();
                double oldTotalAppliedDiscount = getOldTotalAppliedDiscount(order, productPromotionMode, orderPromotionMode, promotionResultsToKeepApplied);
                deleteStoredPromotionResults(order, true);
                Collection<ProductModel> products = getBaseProductsForOrder(order);
                List<PromotionResultModel> results = new LinkedList<>();
                double newTotalAppliedDiscount = 0.0D;
                List<AbstractPromotionModel> activePromotions = findOrderAndProductPromotionsSortByPriority(promotionGroups, products, date);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("updatePromotions found [" + activePromotions.size() + "] promotions to run");
                }
                newTotalAppliedDiscount = updateForActivePromotions(order, evaluateRestrictions, productPromotionMode, orderPromotionMode, date, promotionResultsToKeepApplied, results, newTotalAppliedDiscount, activePromotions);
                printPromotionResults(promotionResultsToKeepApplied);
                double appliedDiscountChange = newTotalAppliedDiscount - oldTotalAppliedDiscount;
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("updatePromotions for [" + order + "] returned [" + results.size() + "] PromotionResults appliedDiscountChange=[" + appliedDiscountChange + "]");
                }
                return new PromotionOrderResults(getSessionContext(), getOrder(order), (List)
                                getModelService().getAllSources(results, new ArrayList()), appliedDiscountChange);
            }
        }
        catch(Exception ex)
        {
            LOG.error("Failed to updatePromotions", ex);
        }
        return null;
    }


    private void printPromotionResults(List<String> promotionResultsToKeepApplied)
    {
        if(LOG.isDebugEnabled())
        {
            for(String prKey : promotionResultsToKeepApplied)
            {
                LOG.debug("updatePromotions PromotionResult not reapplied because it did not fire [" + prKey + "]");
            }
        }
    }


    private double getOldTotalAppliedDiscount(AbstractOrderModel order, PromotionsManager.AutoApplyMode productPromotionMode, PromotionsManager.AutoApplyMode orderPromotionMode, List<String> promotionResultsToKeepApplied)
    {
        double oldTotalAppliedDiscount = 0.0D;
        List<PromotionResultModel> currResults = getPromotionResultsInternal(order);
        if(currResults != null && !currResults.isEmpty())
        {
            for(PromotionResultModel pr : currResults)
            {
                if(isPromotionResultFired(pr))
                {
                    boolean prApplied = isPromotionResultApplied(pr);
                    if(prApplied)
                    {
                        oldTotalAppliedDiscount += getPromotionResultTotalDiscount(pr);
                    }
                    if(isPromotionResultValid(pr) && ((productPromotionMode == PromotionsManager.AutoApplyMode.KEEP_APPLIED && pr
                                    .getPromotion() instanceof ProductPromotionModel) || (orderPromotionMode == PromotionsManager.AutoApplyMode.KEEP_APPLIED && pr
                                    .getPromotion() instanceof OrderPromotionModel)) && prApplied)
                    {
                        String prKey = getDataUniqueKey(pr);
                        if(prKey != null && prKey.length() > 0)
                        {
                            if(LOG.isDebugEnabled())
                            {
                                LOG.debug("updatePromotions found applied PromotionResult [" + pr + "] key [" + prKey + "] that should be reapplied");
                            }
                            promotionResultsToKeepApplied.add(prKey);
                        }
                    }
                }
            }
        }
        return oldTotalAppliedDiscount;
    }


    protected Collection<ProductModel> getBaseProductsForOrder(AbstractOrderModel order)
    {
        SortedSet<ProductModel> products = new TreeSet<>((Comparator<? super ProductModel>)new ProductComparator(this));
        for(AbstractOrderEntryModel aoe : order.getEntries())
        {
            ProductModel product = aoe.getProduct();
            if(product != null)
            {
                products.add(product);
                List<ProductModel> baseProducts = getBaseProducts(product);
                if(baseProducts != null && !baseProducts.isEmpty())
                {
                    products.addAll(baseProducts);
                }
            }
        }
        return products;
    }


    protected ProductModel getBaseProduct(ProductModel product)
    {
        if(getProduct(product) instanceof CompositeProduct)
        {
            return (ProductModel)getModelService().get(((CompositeProduct)getProduct(product)).getCompositeParentProduct(getSessionContext()));
        }
        if(product instanceof VariantProductModel)
        {
            return ((VariantProductModel)product).getBaseProduct();
        }
        return null;
    }


    protected List<ProductModel> getBaseProducts(ProductModel product)
    {
        List<ProductModel> result = new ArrayList<>();
        getBaseProducts(product, result);
        return result;
    }


    protected void getBaseProducts(ProductModel product, List<ProductModel> result)
    {
        if(product != null && result != null)
        {
            ProductModel baseProduct = null;
            if(getProduct(product) instanceof CompositeProduct)
            {
                baseProduct = (ProductModel)getModelService().get(((CompositeProduct)getProduct(product)).getCompositeParentProduct(getSessionContext()));
            }
            else if(product instanceof VariantProductModel)
            {
                baseProduct = ((VariantProductModel)product).getBaseProduct();
            }
            if(baseProduct != null && !baseProduct.equals(product) && !result.contains(baseProduct))
            {
                result.add(baseProduct);
                getBaseProducts(baseProduct, result);
            }
        }
    }


    public List<AbstractPromotionModel> findOrderAndProductPromotionsSortByPriority(Collection<PromotionGroupModel> promotionGroups, Collection<ProductModel> products, Date date)
    {
        checkLegacyMode();
        if(promotionGroups == null || promotionGroups.isEmpty())
        {
            return Collections.emptyList();
        }
        StringBuilder promQuery = new StringBuilder("SELECT DISTINCT pprom.pk, pprom.prio FROM (");
        HashMap<Object, Object> args = new HashMap<>();
        if(products != null && !products.isEmpty())
        {
            promQuery.append(
                            " {{ SELECT {p.pk} as pk, {p.priority} as prio FROM {ProductPromotion AS p JOIN ProductPromotionRelation AS p2p ON {p.pk} = {p2p.target} AND {p2p.source} in (?products) } WHERE {p.PromotionGroup} IN (?promotionGroups) AND {p.enabled} =?true AND {p.startDate} <= ?now AND ?now <= {p.endDate} }}");
            args.put("products", products);
            Set<CategoryModel> productCategories = new HashSet<>();
            for(ProductModel product : products)
            {
                for(CategoryModel cat : product.getSupercategories())
                {
                    productCategories.add(cat);
                    productCategories.addAll(cat.getAllSupercategories());
                }
            }
            if(!productCategories.isEmpty())
            {
                promQuery.append(
                                " UNION {{ SELECT {p.pk} as pk, {p.priority} as prio FROM {ProductPromotion AS p JOIN CategoryPromotionRelation AS c2p ON {p.pk} = {c2p.target} AND {c2p.source} IN (?productCategories) } WHERE {p.PromotionGroup} IN (?promotionGroups) AND {p.enabled} =?true AND {p.startDate} <= ?now AND ?now <= {p.endDate} }}");
                args.put("productCategories", productCategories);
            }
            promQuery.append(" UNION ALL ");
        }
        promQuery.append("{{ SELECT {p3:pk}, {p3.priority} as prio FROM {OrderPromotion as p3} WHERE {p3.PromotionGroup} IN (?promotionGroups) AND {p3.enabled} =?true AND {p3.startDate} <= ?now AND ?now <= {p3.endDate} }} )pprom ORDER BY pprom.prio DESC");
        args.put("now", date);
        args.put("true", Boolean.TRUE);
        args.put("promotionGroups", promotionGroups);
        FlexibleSearchQuery query = new FlexibleSearchQuery(promQuery.toString(), args);
        query.setCount(-1);
        query.setStart(0);
        query.setFailOnUnknownFields(true);
        query.setNeedTotal(true);
        query.setResultClassList(Collections.singletonList(AbstractPromotionModel.class));
        return getFlexibleSearchService().search(query).getResult();
    }


    protected double updateForActivePromotions(AbstractOrderModel order, boolean evaluateRestrictions, PromotionsManager.AutoApplyMode productPromotionMode, PromotionsManager.AutoApplyMode orderPromotionMode, Date date, List<String> promotionResultsToKeepApplied, List<PromotionResultModel> results,
                    double newTotalAppliedDiscount, List<AbstractPromotionModel> activePromotions)
    {
        if(!activePromotions.isEmpty())
        {
            List<VoucherModel> vouchers = fixupVouchersRemoveVouchers(order);
            refreshOrder(order);
            PromotionEvaluationContext promoContext = new PromotionEvaluationContext(getOrder(order), evaluateRestrictions, date);
            for(AbstractPromotionModel promotion : activePromotions)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("updatePromotions evaluating promotion [" + promotion + "]");
                }
                List<PromotionResultModel> promoResults = evaluatePromotion(promoContext, promotion);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("updatePromotions promotion [" + promotion + "] returned [" + promoResults.size() + "] results");
                }
                boolean autoApply = autoApplyApplies(productPromotionMode, orderPromotionMode, promotion);
                boolean keepApplied = keepApplied(productPromotionMode, orderPromotionMode, promotion, autoApply);
                boolean needsCalculateTotals = false;
                if(autoApply || keepApplied)
                {
                    for(PromotionResultModel pr : promoResults)
                    {
                        if(isPromotionResultFired(pr))
                        {
                            if(autoApply)
                            {
                                if(LOG.isDebugEnabled())
                                {
                                    LOG.debug("updatePromotions auto applying result [" + pr + "] from promotion [" + promotion + "]");
                                }
                                needsCalculateTotals |= getResult(pr).apply(getSessionContext());
                                newTotalAppliedDiscount += getTotalDiscount(pr);
                            }
                            else if(keepApplied)
                            {
                                String prKey = getDataUniqueKey(pr);
                                if(prKey == null || prKey.length() == 0)
                                {
                                    LOG.error("updatePromotions promotion result [" + pr + "] from promotion [" + promotion + "] returned NULL or Empty DataUnigueKey");
                                }
                                else if(promotionResultsToKeepApplied.remove(prKey))
                                {
                                    if(LOG.isDebugEnabled())
                                    {
                                        LOG.debug("updatePromotions keeping applied the result [" + pr + "] from promotion [" + promotion + "]");
                                    }
                                    needsCalculateTotals |= getResult(pr).apply(getSessionContext());
                                    newTotalAppliedDiscount += getTotalDiscount(pr);
                                }
                            }
                        }
                        getModelService().refresh(pr);
                    }
                }
                if(needsCalculateTotals)
                {
                    try
                    {
                        refreshOrder(order);
                        getCalculationService().calculateTotals(order, true);
                    }
                    catch(CalculationException e)
                    {
                        LOG.error("Failed to calculate order" + order.getCode(), (Throwable)e);
                    }
                }
                results.addAll(promoResults);
            }
            fixupVouchersReapplyVouchers(order, vouchers);
        }
        return newTotalAppliedDiscount;
    }


    protected List<VoucherModel> fixupVouchersRemoveVouchers(AbstractOrderModel order)
    {
        try
        {
            if(Boolean.parseBoolean(Config.getParameter("promotions.voucher.fixupVouchers")))
            {
                Collection<DiscountModel> discounts = order.getDiscounts();
                if(discounts != null && !discounts.isEmpty())
                {
                    List<VoucherModel> appliedVouchers = new ArrayList<>();
                    for(DiscountModel discount : discounts)
                    {
                        if(discount instanceof VoucherModel)
                        {
                            VoucherModel voucher = (VoucherModel)discount;
                            DiscountValue testDiscountValue = ((Voucher)getModelService().getSource(voucher)).getDiscountValue(getOrder(order));
                            if(testDiscountValue != null)
                            {
                                DiscountValue oldDiscountValue = Helper.findGlobalDiscountValue(getSessionContext(),
                                                getOrder(order), testDiscountValue.getCode());
                                if(oldDiscountValue != null)
                                {
                                    if(LOG.isDebugEnabled())
                                    {
                                        LOG.debug("Removing GlobalDiscountValue created by Voucher [" + voucher.getName() + "]");
                                    }
                                    getOrder(order).removeGlobalDiscountValue(getSessionContext(), oldDiscountValue);
                                }
                                appliedVouchers.add(voucher);
                            }
                        }
                    }
                    refreshOrder(order);
                    return appliedVouchers;
                }
            }
        }
        catch(Exception ex)
        {
            LOG.error("Failed to fixupVouchersRemoveVouchers", ex);
        }
        return null;
    }


    protected void fixupVouchersReapplyVouchers(AbstractOrderModel order, List<VoucherModel> vouchers)
    {
        try
        {
            if(vouchers != null && !vouchers.isEmpty())
            {
                refreshOrder(order);
                getCalculationService().calculateTotals(order, true);
                double orderSubtotal = order.getSubtotal().doubleValue();
                for(VoucherModel voucher : vouchers)
                {
                    if(isDiscountAbsolute((DiscountModel)voucher))
                    {
                        getOrder(order).addGlobalDiscountValue(getSessionContext(), ((Voucher)
                                        getModelService().getSource(voucher)).getDiscountValue(getOrder(order)));
                        continue;
                    }
                    VoucherValue voucherValue = ((Voucher)getModelService().getSource(voucher)).getVoucherValue((AbstractOrder)getModelService().getSource(order));
                    double voucherDiscount = voucherValue.getValue();
                    DiscountValue voucherDiscountValue = new DiscountValue(voucher.getCode(), voucherDiscount, true, voucherDiscount, order.getCurrency().getIsocode());
                    getOrder(order).addGlobalDiscountValue(getSessionContext(), voucherDiscountValue);
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Reapplying Voucher [" + voucher.getName() + "], Relative Value: [" + voucher.getValue() + "%], Order Total: [" + orderSubtotal + "], New Adjustment Discount [" + voucherDiscountValue + "]");
                    }
                }
                refreshOrder(order);
                getCalculationService().calculateTotals(order, true);
            }
        }
        catch(Exception ex)
        {
            LOG.error("Failed to fixupVouchersReapplyVouchers", ex);
        }
    }


    protected boolean isDiscountAbsolute(DiscountModel discount)
    {
        return (discount.getCurrency() != null);
    }


    protected List<PromotionResultModel> evaluatePromotion(PromotionEvaluationContext promoContext, AbstractPromotionModel promotion)
    {
        List<PromotionResultModel> results = (List<PromotionResultModel>)getModelService().getAll(getPromotion(promotion).evaluate(getSessionContext(), promoContext), new ArrayList());
        if(Transaction.current().isRunning())
        {
            Transaction.current().flushDelayedStore();
        }
        return results;
    }


    protected boolean autoApplyApplies(PromotionsManager.AutoApplyMode productPromotionMode, PromotionsManager.AutoApplyMode orderPromotionMode, AbstractPromotionModel promotion)
    {
        return (isApplyAll(productPromotionMode, orderPromotionMode) ||
                        isProductPromotionApplyAll(productPromotionMode, promotion) ||
                        isOrderPromotionApplyAll(orderPromotionMode, promotion));
    }


    private boolean isOrderPromotionApplyAll(PromotionsManager.AutoApplyMode orderPromotionMode, AbstractPromotionModel promotion)
    {
        return (orderPromotionMode == PromotionsManager.AutoApplyMode.APPLY_ALL && promotion instanceof OrderPromotionModel);
    }


    private boolean isProductPromotionApplyAll(PromotionsManager.AutoApplyMode productPromotionMode, AbstractPromotionModel promotion)
    {
        return (productPromotionMode == PromotionsManager.AutoApplyMode.APPLY_ALL && promotion instanceof ProductPromotionModel);
    }


    private boolean isApplyAll(PromotionsManager.AutoApplyMode productPromotionMode, PromotionsManager.AutoApplyMode orderPromotionMode)
    {
        return (productPromotionMode == PromotionsManager.AutoApplyMode.APPLY_ALL && orderPromotionMode == PromotionsManager.AutoApplyMode.APPLY_ALL);
    }


    protected boolean keepApplied(PromotionsManager.AutoApplyMode productPromotionMode, PromotionsManager.AutoApplyMode orderPromotionMode, AbstractPromotionModel promotion, boolean autoApply)
    {
        boolean keepApplied = false;
        if((!autoApply && productPromotionMode == PromotionsManager.AutoApplyMode.KEEP_APPLIED && orderPromotionMode == PromotionsManager.AutoApplyMode.KEEP_APPLIED) || (productPromotionMode == PromotionsManager.AutoApplyMode.KEEP_APPLIED && promotion instanceof ProductPromotionModel) || (
                        orderPromotionMode == PromotionsManager.AutoApplyMode.KEEP_APPLIED && promotion instanceof OrderPromotionModel))
        {
            keepApplied = true;
        }
        return keepApplied;
    }


    protected void refreshOrder(AbstractOrderModel order)
    {
        List<ItemModel> toRefresh = new ArrayList<>(1);
        toRefresh.add(order);
        refreshModifiedModelsAfter(toRefresh);
        toRefresh = CollectionUtils.isEmpty(order.getEntries()) ? new ArrayList<>() : new ArrayList<>(order.getEntries());
        refreshModifiedModelsAfter(toRefresh);
    }


    protected void saveIfModified(AbstractOrderModel order)
    {
        if(getModelService().isModified(order))
        {
            getModelService().save(order);
        }
        if(order.getEntries() != null)
        {
            Collection<AbstractOrderEntryModel> orderEntries = (Collection<AbstractOrderEntryModel>)order.getEntries().stream().filter(oe -> getModelService().isModified(oe)).collect(Collectors.toList());
            if(!orderEntries.isEmpty())
            {
                getModelService().saveAll(orderEntries);
            }
        }
    }


    protected void checkLegacyMode()
    {
        getLegacyModeChecker().check();
    }


    public Collection<AbstractPromotionRestrictionModel> getRestrictions(AbstractPromotionModel promotion)
    {
        checkLegacyMode();
        return getModelService().getAll(getPromotion(promotion).getRestrictions(), new ArrayList());
    }


    public String getPromotionDescription(AbstractPromotionModel promotion)
    {
        return promotion.getDescription();
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setCalculationService(CalculationService calculationService)
    {
        this.calculationService = calculationService;
    }


    protected CalculationService getCalculationService()
    {
        return this.calculationService;
    }


    protected LegacyModeChecker getLegacyModeChecker()
    {
        return this.legacyModeChecker;
    }


    @Required
    public void setLegacyModeChecker(LegacyModeChecker legacyModeChecker)
    {
        this.legacyModeChecker = legacyModeChecker;
    }
}
