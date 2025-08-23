package de.hybris.platform.promotions.jalo;

import com.google.common.collect.Maps;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.promotions.constants.GeneratedPromotionsConstants;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionException;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import de.hybris.platform.promotions.util.Helper;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.jeeapi.YNoSuchEntityException;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.util.migration.DeploymentMigrationUtil;
import de.hybris.platform.voucher.jalo.Voucher;
import de.hybris.platform.voucher.jalo.util.VoucherValue;
import java.rmi.dgc.VMID;
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
import org.apache.log4j.Logger;

public class PromotionsManager extends GeneratedPromotionsManager
{
    private static final Logger LOG = Logger.getLogger(PromotionsManager.class.getName());
    private boolean migrationMode = false;
    private static final String DEPLOYMENT_CHECK_PROPERTY = "deployment.check";
    private final Comparator<PromotionResult> promotionResultComparator = (Comparator<PromotionResult>)new PromotionResultByPromotionPriorityComparator();


    public void notifyInitializationStart(Map<String, String> params, JspContext ctx)
    {
        if("update".equals(params.get("initmethod")))
        {
            boolean deploymentCheck = Config.getBoolean("deployment.check", true);
            ComposedType type = null;
            try
            {
                type = TypeManager.getInstance().getComposedType("PromotionOrderEntryConsumed");
            }
            catch(JaloSystemException e)
            {
                type = null;
            }
            if(deploymentCheck && type != null && "de.hybris.jakarta.entity.PromotionOrderEntryConsumed"
                            .equalsIgnoreCase(type.getJNDIName()))
            {
                LOG.info("Activating migration mode.");
                this.migrationMode = true;
                Config.setParameter("deployment.check", Boolean.FALSE.toString());
                System.setProperty("deployment.check", Boolean.FALSE.toString());
                DeploymentMigrationUtil.migrateDeployments("promotions");
                DeploymentMigrationUtil.migrateDeploymentManually(5014, "PromotionOrderEntryConsumed");
                DeploymentMigrationUtil.migrateDeploymentManually(5016, "PromotionQuantityAndPricesRow");
                LOG.info("The following update will be performed with deployment.check set to false");
            }
        }
    }


    public void notifyInitializationEnd(Map<String, String> params, JspContext ctx)
    {
        if(this.migrationMode)
        {
            this.migrationMode = false;
            LOG.info("Deactivating migration mode.");
            Config.setParameter("deployment.check", Boolean.TRUE.toString());
            System.setProperty("deployment.check", Boolean.FALSE.toString());
        }
    }


    public static PromotionsManager getInstance()
    {
        JaloSession js = JaloSession.getCurrentSession();
        return (PromotionsManager)js.getExtensionManager().getExtension("promotions");
    }


    public void createEssentialData(Map params, JspContext jspc) throws Exception
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Setting localized default values");
        }
        SessionContext languageNeutralSessionContext = getLanguageNeutralSessionContext();
        initialiseDefaultLocalisedValues(languageNeutralSessionContext, getComposedType(AbstractPromotion.class).getAllSubTypes());
        initialiseDefaultLocalisedValues(languageNeutralSessionContext,
                        getComposedType(AbstractPromotionRestriction.class).getAllSubTypes());
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Creating default promotions group");
        }
        PromotionGroup defaultPromotionGroup = getDefaultPromotionGroup(getSession().getSessionContext());
        if(defaultPromotionGroup == null)
        {
            createPromotionGroup(getSession().getSessionContext(), "default");
        }
    }


    protected SessionContext getLanguageNeutralSessionContext()
    {
        SessionContext ctx = getSession().createSessionContext();
        ctx.setLanguage(null);
        return ctx;
    }


    protected void initialiseDefaultLocalisedValues(SessionContext languageNeutralSessionContext, Set<ComposedType> types)
    {
        for(ComposedType subType : types)
        {
            initialiseDefaultLocalisedValues(languageNeutralSessionContext, subType);
        }
    }


    protected void initialiseDefaultLocalisedValues(SessionContext languageNeutralSessionContext, ComposedType type)
    {
        for(AttributeDescriptor attributeDescriptor : type.getAttributeDescriptorsIncludingPrivate())
        {
            if(attributeDescriptor.isLocalized() && attributeDescriptor.getAttributeType().isInstance(""))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Setting default value on " + type.getCode() + "." + attributeDescriptor.getQualifier());
                }
                String resourceKey = "type." + type.getCode().toLowerCase() + "." + attributeDescriptor.getQualifier().toLowerCase() + ".defaultvalue";
                attributeDescriptor.setDefaultValue(languageNeutralSessionContext, Localization.getLocalizedMap(resourceKey));
            }
        }
    }


    protected ComposedType getComposedType(Class aClass)
    {
        try
        {
            ComposedType type = getSession().getTypeManager().getComposedType(aClass);
            if(type == null)
            {
                throw new JaloSystemException("Got type null for " + aClass, 0);
            }
            return type;
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "Required type missing", 0);
        }
    }


    public final List<ProductPromotion> getProductPromotions(Collection<PromotionGroup> promotionGroups, Product product)
    {
        return getProductPromotions(getSession().getSessionContext(), promotionGroups, product, true,
                        Helper.getDateNowRoundedToMinute());
    }


    public final List<ProductPromotion> getProductPromotions(Collection<PromotionGroup> promotionGroups, Product product, boolean evaluateRestrictions, Date date)
    {
        return getProductPromotions(getSession().getSessionContext(), promotionGroups, product, evaluateRestrictions, date);
    }


    public List<ProductPromotion> getProductPromotions(SessionContext ctx, Collection<PromotionGroup> promotionGroups, Product product, boolean evaluateRestrictions, Date date)
    {
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("getProductPromotions for [" + product + "] promotionGroups=[" + Helper.join(promotionGroups) + "] evaluateRestrictions=[" + evaluateRestrictions + "] date=[" + date + "]");
            }
            if(promotionGroups != null && product != null && !promotionGroups.isEmpty())
            {
                List<ProductPromotion> availablePromotions;
                if(date == null)
                {
                    date = Helper.getDateNowRoundedToMinute();
                }
                Map<String, Object> args = new HashMap<>();
                args.put("promotionGroups", promotionGroups);
                args.put("product", product);
                args.put("now", date);
                args.put("true", Boolean.TRUE);
                String query = buildQueryForDistinctProductPromotionQuery(product, ctx, args);
                List<ProductPromotion> allPromotions = getSession().getFlexibleSearch().search(ctx, query, args, Collections.singletonList(ProductPromotion.class), true, false, 0, -1).getResult();
                if(evaluateRestrictions)
                {
                    availablePromotions = filterPromotionsByRestrictions(ctx, allPromotions, product, date);
                }
                else
                {
                    availablePromotions = new ArrayList<>(allPromotions);
                }
                if(LOG.isDebugEnabled())
                {
                    for(ProductPromotion promotion : availablePromotions)
                    {
                        LOG.debug("getProductPromotions for [" + product + "] available promotion [" + promotion + "]");
                    }
                }
                return availablePromotions;
            }
        }
        catch(Exception ex)
        {
            LOG.error("Failed to getProductPromotions", ex);
        }
        return Collections.emptyList();
    }


    protected String buildQueryForDistinctProductPromotionQuery(Product product, SessionContext ctx, Map<String, Object> args)
    {
        StringBuilder promQuery = new StringBuilder("SELECT DISTINCT pprom.pk, pprom.prio FROM (");
        promQuery.append(" {{ SELECT {p." + ProductPromotion.PK + "} as pk, ");
        promQuery.append(" {p.priority} as prio FROM");
        promQuery.append(" {" + TypeManager.getInstance().getComposedType(ProductPromotion.class).getCode() + " AS p");
        promQuery.append(" JOIN " + GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION + " AS p2p ");
        promQuery.append(" ON {p." + ProductPromotion.PK + "} = {p2p.target} ");
        promQuery.append(" AND {p2p.source} = ?product } ");
        promQuery.append(" WHERE {p.PromotionGroup} IN (?promotionGroups) AND");
        promQuery.append(" {p.enabled} =?true AND ");
        promQuery.append(" {p.startDate} <= ?now AND ");
        promQuery.append(" ?now <= {p.endDate} }}");
        Collection<Category> productCategories = CategoryManager.getInstance().getCategoriesByProduct(product, ctx);
        if(!productCategories.isEmpty())
        {
            promQuery.append(" UNION ");
            promQuery.append(" {{ SELECT {p." + ProductPromotion.PK + "} as pk, ");
            promQuery.append(" {p.priority} as prio FROM");
            promQuery.append(" {" + TypeManager.getInstance().getComposedType(ProductPromotion.class).getCode() + " AS p");
            promQuery.append(" JOIN " + GeneratedPromotionsConstants.Relations.CATEGORYPROMOTIONRELATION + " AS c2p ");
            promQuery.append(" ON {p." + ProductPromotion.PK + "} = {c2p.target} ");
            promQuery.append(" AND {c2p.source} IN (?productCategories) } ");
            promQuery.append(" WHERE {p.PromotionGroup} IN (?promotionGroups) AND");
            promQuery.append(" {p.enabled} =?true AND ");
            promQuery.append(" {p.startDate} <= ?now AND ");
            promQuery.append(" ?now <= {p.endDate} }}");
            Set<Category> productSuperCategories = new HashSet<>();
            for(Category cat : productCategories)
            {
                productSuperCategories.add(cat);
                productSuperCategories.addAll(cat.getAllSupercategories(ctx));
            }
            args.put("productCategories", productSuperCategories);
        }
        promQuery.append(" )pprom ORDER BY pprom.prio DESC");
        return promQuery.toString();
    }


    public List<OrderPromotion> getOrderPromotions(Collection<PromotionGroup> promotionGroups)
    {
        return getOrderPromotions(getSession().getSessionContext(), promotionGroups, true, null,
                        Helper.getDateNowRoundedToMinute());
    }


    public List<OrderPromotion> getOrderPromotions(Collection<PromotionGroup> promotionGroups, Date date)
    {
        return getOrderPromotions(getSession().getSessionContext(), promotionGroups, true, null, date);
    }


    public List<OrderPromotion> getOrderPromotions(Collection<PromotionGroup> promotionGroups, Product product)
    {
        return getOrderPromotions(getSession().getSessionContext(), promotionGroups, true, product,
                        Helper.getDateNowRoundedToMinute());
    }


    public List<OrderPromotion> getOrderPromotions(Collection<PromotionGroup> promotionGroups, Product product, Date date)
    {
        return getOrderPromotions(getSession().getSessionContext(), promotionGroups, true, product, date);
    }


    public List<OrderPromotion> getOrderPromotions(Collection<PromotionGroup> promotionGroups, boolean evaluateRestrictions)
    {
        return getOrderPromotions(getSession().getSessionContext(), promotionGroups, evaluateRestrictions, null,
                        Helper.getDateNowRoundedToMinute());
    }


    public List<OrderPromotion> getOrderPromotions(Collection<PromotionGroup> promotionGroups, boolean evaluateRestrictions, Date date)
    {
        return getOrderPromotions(getSession().getSessionContext(), promotionGroups, evaluateRestrictions, null, date);
    }


    public List<OrderPromotion> getOrderPromotions(Collection<PromotionGroup> promotionGroups, boolean evaluateRestrictions, Product product)
    {
        return getOrderPromotions(getSession().getSessionContext(), promotionGroups, evaluateRestrictions, product,
                        Helper.getDateNowRoundedToMinute());
    }


    public List<OrderPromotion> getOrderPromotions(Collection<PromotionGroup> promotionGroups, boolean evaluateRestrictions, Product product, Date date)
    {
        return getOrderPromotions(getSession().getSessionContext(), promotionGroups, evaluateRestrictions, product, date);
    }


    public List<OrderPromotion> getOrderPromotions(SessionContext ctx, Collection<PromotionGroup> promotionGroups, boolean evaluateRestrictions, Product product, Date date)
    {
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
                String query = "SELECT DISTINCT {promo:" + Item.PK + "}, {promo:priority} FROM {" + TypeManager.getInstance().getComposedType(OrderPromotion.class).getCode()
                                + " as promo } WHERE (   {promo:PromotionGroup} IN (?promotionGroups) ) AND (   {promo:enabled}=1 AND {promo:startDate} <= ?now AND ?now <= {promo:endDate} ) ORDER BY {promo:priority} DESC";
                HashMap<Object, Object> args = new HashMap<>();
                args.put("promotionGroups", promotionGroups);
                args.put("now", date);
                List<OrderPromotion> allPromotions = getSession().getFlexibleSearch().search(ctx, query, args, Collections.singletonList(OrderPromotion.class), true, false, 0, -1).getResult();
                List<OrderPromotion> availablePromotions = null;
                if(evaluateRestrictions)
                {
                    availablePromotions = filterPromotionsByRestrictions(ctx, allPromotions, product, date);
                }
                else
                {
                    availablePromotions = new ArrayList<>(allPromotions);
                }
                if(LOG.isDebugEnabled())
                {
                    for(OrderPromotion promotion : availablePromotions)
                    {
                        LOG.debug("getOrderPromotions available promotion [" + promotion + "]");
                    }
                }
                return availablePromotions;
            }
        }
        catch(Exception ex)
        {
            LOG.error("Failed to getOrderPromotions", ex);
        }
        return new ArrayList<>(0);
    }


    public <T extends AbstractPromotion> List<T> filterPromotionsByRestrictions(SessionContext ctx, List<T> allPromotions, Product product, Date date)
    {
        ArrayList<T> availablePromotions = new ArrayList<>(allPromotions.size());
        for(AbstractPromotion abstractPromotion : allPromotions)
        {
            boolean satifiedRestrictions = true;
            Collection<AbstractPromotionRestriction> restrictions = abstractPromotion.getRestrictions();
            if(restrictions != null)
            {
                for(AbstractPromotionRestriction restriction : restrictions)
                {
                    AbstractPromotionRestriction.RestrictionResult result = restriction.evaluate(ctx, product, date, null);
                    if(result == AbstractPromotionRestriction.RestrictionResult.DENY || result == AbstractPromotionRestriction.RestrictionResult.ADJUSTED_PRODUCTS)
                    {
                        satifiedRestrictions = false;
                        break;
                    }
                }
            }
            if(satifiedRestrictions)
            {
                availablePromotions.add((T)abstractPromotion);
            }
        }
        return availablePromotions;
    }


    public final PromotionOrderResults updatePromotions(Collection<PromotionGroup> promotionGroups, AbstractOrder order)
    {
        return updatePromotions(getSession().getSessionContext(), promotionGroups, order);
    }


    public PromotionOrderResults updatePromotions(SessionContext ctx, Collection<PromotionGroup> promotionGroups, AbstractOrder order)
    {
        return updatePromotions(ctx, promotionGroups, order, true, AutoApplyMode.APPLY_ALL, AutoApplyMode.KEEP_APPLIED,
                        Helper.getDateNowRoundedToMinute());
    }


    public PromotionOrderResults updatePromotions(SessionContext ctx, Collection<PromotionGroup> promotionGroups, AbstractOrder order, boolean evaluateRestrictions, AutoApplyMode productPromotionMode, AutoApplyMode orderPromotionMode, Date date)
    {
        synchronized(order.getSyncObject())
        {
            return updatePromotionsNotThreadSafe(ctx, promotionGroups, order, evaluateRestrictions, productPromotionMode, orderPromotionMode, date);
        }
    }


    protected PromotionOrderResults updatePromotionsNotThreadSafe(SessionContext ctx, Collection<PromotionGroup> promotionGroups, AbstractOrder order, boolean evaluateRestrictions, AutoApplyMode productPromotionMode, AutoApplyMode orderPromotionMode, Date date)
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
                if(!order.isCalculated(ctx).booleanValue())
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("updatePromotions order [" + order + "] not calculated, calculating");
                    }
                    order.calculate(date);
                }
                List<String> promotionResultsToKeepApplied = new ArrayList<>();
                double oldTotalAppliedDiscount = 0.0D;
                List<PromotionResult> currResults = getPromotionResultsInternal(ctx, order);
                if(currResults != null && !currResults.isEmpty())
                {
                    for(PromotionResult pr : currResults)
                    {
                        if(pr.getFired(ctx))
                        {
                            boolean prApplied = pr.isApplied(ctx);
                            if(prApplied)
                            {
                                oldTotalAppliedDiscount += pr.getTotalDiscount(ctx);
                            }
                            if(pr.isValid(ctx) && ((productPromotionMode == AutoApplyMode.KEEP_APPLIED && pr
                                            .getPromotion(ctx) instanceof ProductPromotion) || (orderPromotionMode == AutoApplyMode.KEEP_APPLIED && pr
                                            .getPromotion(ctx) instanceof OrderPromotion)) && prApplied)
                            {
                                String prKey = pr.getDataUnigueKey(ctx);
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
                deleteStoredPromotionResults(ctx, order, true);
                Collection<Product> products = getBaseProductsForOrder(ctx, order);
                List<PromotionResult> results = new LinkedList<>();
                double newTotalAppliedDiscount = 0.0D;
                List<AbstractPromotion> activePromotions = findOrderAndProductPromotionsSortByPriority(ctx, getSession(), promotionGroups, products, date);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("updatePromotions found [" + activePromotions.size() + "] promotions to run");
                }
                newTotalAppliedDiscount = updateForActivePromotions(ctx, order, evaluateRestrictions, productPromotionMode, orderPromotionMode, date, promotionResultsToKeepApplied, results, newTotalAppliedDiscount, activePromotions);
                if(LOG.isDebugEnabled())
                {
                    for(String prKey : promotionResultsToKeepApplied)
                    {
                        LOG.debug("updatePomrotions PromotionResult not reapplied because it did not fire [" + prKey + "]");
                    }
                }
                double appliedDiscountChange = newTotalAppliedDiscount - oldTotalAppliedDiscount;
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("updatePromotions for [" + order + "] returned [" + results.size() + "] PromotionResults appliedDiscountChange=[" + appliedDiscountChange + "]");
                }
                return new PromotionOrderResults(ctx, order, Collections.unmodifiableList(results), appliedDiscountChange);
            }
        }
        catch(Exception ex)
        {
            LOG.error("Failed to updatePromotions", ex);
        }
        return null;
    }


    private double updateForActivePromotions(SessionContext ctx, AbstractOrder order, boolean evaluateRestrictions, AutoApplyMode productPromotionMode, AutoApplyMode orderPromotionMode, Date date, List<String> promotionResultsToKeepApplied, List<PromotionResult> results,
                    double newTotalAppliedDiscount, List<AbstractPromotion> activePromotions) throws JaloPriceFactoryException
    {
        if(!activePromotions.isEmpty())
        {
            List<Voucher> vouchers = fixupVouchersRemoveVouchers(ctx, order);
            PromotionEvaluationContext promoContext = new PromotionEvaluationContext(order, evaluateRestrictions, date);
            for(AbstractPromotion promotion : activePromotions)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("updatePromotions evaluating promotion [" + promotion + "]");
                }
                List<PromotionResult> promoResults = evaluatePromotion(ctx, promoContext, promotion);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("updatePromotions promotion [" + promotion + "] returned [" + promoResults.size() + "] results");
                }
                boolean autoApply = autoApplyApplies(productPromotionMode, orderPromotionMode, promotion);
                boolean keepApplied = keepApplied(productPromotionMode, orderPromotionMode, promotion, autoApply);
                boolean needsCalculateTotals = false;
                if(autoApply || keepApplied)
                {
                    for(PromotionResult pr : promoResults)
                    {
                        if(pr.getFired(ctx))
                        {
                            if(autoApply)
                            {
                                if(LOG.isDebugEnabled())
                                {
                                    LOG.debug("updatePromotions auto applying result [" + pr + "] from promotion [" + promotion + "]");
                                }
                                needsCalculateTotals |= pr.apply(ctx);
                                newTotalAppliedDiscount += pr.getTotalDiscount(ctx);
                                continue;
                            }
                            if(keepApplied)
                            {
                                String prKey = pr.getDataUnigueKey(ctx);
                                if(prKey == null || prKey.length() == 0)
                                {
                                    LOG.error("updatePromotions promotion result [" + pr + "] from promotion [" + promotion + "] returned NULL or Empty DataUnigueKey");
                                    continue;
                                }
                                if(promotionResultsToKeepApplied.remove(prKey))
                                {
                                    if(LOG.isDebugEnabled())
                                    {
                                        LOG.debug("updatePromotions keeping applied the result [" + pr + "] from promotion [" + promotion + "]");
                                    }
                                    needsCalculateTotals |= pr.apply(ctx);
                                    newTotalAppliedDiscount += pr.getTotalDiscount(ctx);
                                }
                            }
                        }
                    }
                }
                if(needsCalculateTotals)
                {
                    order.calculateTotals(true);
                }
                results.addAll(promoResults);
            }
            fixupVouchersReapplyVouchers(ctx, order, vouchers);
        }
        return newTotalAppliedDiscount;
    }


    protected boolean keepApplied(AutoApplyMode productPromotionMode, AutoApplyMode orderPromotionMode, AbstractPromotion promotion, boolean autoApply)
    {
        boolean keepApplied = false;
        if((!autoApply && productPromotionMode == AutoApplyMode.KEEP_APPLIED && orderPromotionMode == AutoApplyMode.KEEP_APPLIED) || (productPromotionMode == AutoApplyMode.KEEP_APPLIED && promotion instanceof ProductPromotion) || (orderPromotionMode == AutoApplyMode.KEEP_APPLIED
                        && promotion instanceof OrderPromotion))
        {
            keepApplied = true;
        }
        return keepApplied;
    }


    protected boolean autoApplyApplies(AutoApplyMode productPromotionMode, AutoApplyMode orderPromotionMode, AbstractPromotion promotion)
    {
        boolean autoApply = false;
        if((productPromotionMode == AutoApplyMode.APPLY_ALL && orderPromotionMode == AutoApplyMode.APPLY_ALL) || (productPromotionMode == AutoApplyMode.APPLY_ALL && promotion instanceof ProductPromotion) || (orderPromotionMode == AutoApplyMode.APPLY_ALL && promotion instanceof OrderPromotion))
        {
            autoApply = true;
        }
        return autoApply;
    }


    protected List<PromotionResult> evaluatePromotion(SessionContext ctx, PromotionEvaluationContext promoContext, AbstractPromotion promotion)
    {
        List<PromotionResult> results = promotion.evaluate(ctx, promoContext);
        if(Transaction.current().isRunning())
        {
            Transaction.current().flushDelayedStore();
        }
        return results;
    }


    protected static List<Voucher> fixupVouchersRemoveVouchers(SessionContext ctx, AbstractOrder order)
    {
        try
        {
            if(Boolean.parseBoolean(Config.getParameter("promotions.voucher.fixupVouchers")))
            {
                Collection<Discount> discounts = order.getDiscounts();
                if(discounts != null && !discounts.isEmpty())
                {
                    List<Voucher> appliedVouchers = new ArrayList<>();
                    for(Discount discount : discounts)
                    {
                        if(discount instanceof Voucher)
                        {
                            Voucher voucher = (Voucher)discount;
                            DiscountValue testDiscountValue = voucher.getDiscountValue(order);
                            if(testDiscountValue != null)
                            {
                                DiscountValue oldDiscountValue = Helper.findGlobalDiscountValue(ctx, order, testDiscountValue
                                                .getCode());
                                if(oldDiscountValue != null)
                                {
                                    if(LOG.isDebugEnabled())
                                    {
                                        LOG.debug("Removing GlobalDiscountValue created by Voucher [" + voucher.getName(ctx) + "]");
                                    }
                                    order.removeGlobalDiscountValue(ctx, oldDiscountValue);
                                }
                                appliedVouchers.add(voucher);
                            }
                        }
                    }
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


    protected static void fixupVouchersReapplyVouchers(SessionContext ctx, AbstractOrder order, List<Voucher> vouchers)
    {
        try
        {
            if(vouchers != null && !vouchers.isEmpty())
            {
                order.calculateTotals(true);
                double orderSubtotal = order.getSubtotal(ctx).doubleValue();
                for(Voucher voucher : vouchers)
                {
                    if(voucher.isAbsolute().booleanValue())
                    {
                        order.addGlobalDiscountValue(ctx, voucher.getDiscountValue(order));
                        continue;
                    }
                    VoucherValue voucherValue = voucher.getVoucherValue(order);
                    double voucherDiscount = voucherValue.getValue();
                    DiscountValue voucherDiscountValue = new DiscountValue(voucher.getCode(), voucherDiscount, true, voucherDiscount, order.getCurrency(ctx).getIsoCode(ctx));
                    order.addGlobalDiscountValue(ctx, voucherDiscountValue);
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Reapplying Voucher [" + voucher.getName(ctx) + "], Relative Value: [" + voucher.getValue() + "%], Order Total: [" + orderSubtotal + "], New Adjustment Discount [" + voucherDiscountValue + "]");
                    }
                }
                order.calculateTotals(true);
            }
        }
        catch(Exception ex)
        {
            LOG.error("Failed to fixupVouchersReapplyVouchers", ex);
        }
    }


    public static List<AbstractPromotion> findOrderAndProductPromotionsSortByPriority(SessionContext ctx, JaloSession jaloSession, Collection<PromotionGroup> promotionGroups, Collection<Product> products, Date date)
    {
        if(promotionGroups == null || promotionGroups.isEmpty())
        {
            return Collections.emptyList();
        }
        StringBuilder promQuery = new StringBuilder("SELECT DISTINCT pprom.pk, pprom.prio FROM (");
        HashMap<Object, Object> args = new HashMap<>();
        if(products != null && !products.isEmpty())
        {
            promQuery.append(" {{ SELECT {p.").append(ProductPromotion.PK).append("} as pk, ");
            promQuery.append(" {p.").append("priority").append("} as prio FROM");
            promQuery.append(" {").append(TypeManager.getInstance().getComposedType(ProductPromotion.class).getCode())
                            .append(" AS p");
            promQuery.append(" JOIN ").append(GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION).append(" AS p2p ");
            promQuery.append(" ON {p.").append(ProductPromotion.PK).append("} = {p2p.").append("target").append("} ");
            promQuery.append(" AND {p2p.").append("source").append("} in (?products) } ");
            promQuery.append(" WHERE {p.").append("PromotionGroup").append("} IN (?promotionGroups) AND");
            promQuery.append(" {p.").append("enabled").append("} =?true AND ");
            promQuery.append(" {p.").append("startDate").append("} <= ?now AND ");
            promQuery.append(" ?now <= {p.").append("endDate").append("} }}");
            args.put("products", products);
            Set<Category> productCategories = new HashSet<>();
            for(Product product : products)
            {
                for(Category cat : CategoryManager.getInstance().getCategoriesByProduct(product, ctx))
                {
                    productCategories.add(cat);
                    productCategories.addAll(cat.getAllSupercategories(ctx));
                }
            }
            if(!productCategories.isEmpty())
            {
                promQuery.append(" UNION ");
                promQuery.append(" {{ SELECT {p.").append(ProductPromotion.PK).append("} as pk, ");
                promQuery.append(" {p.").append("priority").append("} as prio FROM");
                promQuery.append(" {").append(TypeManager.getInstance().getComposedType(ProductPromotion.class).getCode())
                                .append(" AS p");
                promQuery.append(" JOIN ").append(GeneratedPromotionsConstants.Relations.CATEGORYPROMOTIONRELATION).append(" AS c2p ");
                promQuery.append(" ON {p.").append(ProductPromotion.PK).append("} = {c2p.").append("target").append("} ");
                promQuery.append(" AND {c2p.").append("source").append("} IN (?productCategories) } ");
                promQuery.append(" WHERE {p.").append("PromotionGroup").append("} IN (?promotionGroups) AND");
                promQuery.append(" {p.").append("enabled").append("} =?true AND ");
                promQuery.append(" {p.").append("startDate").append("} <= ?now AND ");
                promQuery.append(" ?now <= {p.").append("endDate").append("} }}");
                args.put("productCategories", productCategories);
            }
            promQuery.append(" UNION ALL ");
        }
        promQuery.append("{{ SELECT {p3:").append(OrderPromotion.PK).append("}, {p3.").append("priority")
                        .append("} as prio ");
        promQuery.append(" FROM {").append(TypeManager.getInstance().getComposedType(OrderPromotion.class).getCode())
                        .append(" as p3} ");
        promQuery.append(" WHERE {p3.").append("PromotionGroup").append("} IN (?promotionGroups) AND");
        promQuery.append(" {p3.").append("enabled").append("} =?true AND ");
        promQuery.append(" {p3.").append("startDate").append("} <= ?now AND ");
        promQuery.append(" ?now <= {p3.").append("endDate").append("}").append("        }} ");
        promQuery.append(" )pprom ORDER BY pprom.prio DESC");
        args.put("now", date);
        args.put("true", Boolean.TRUE);
        args.put("promotionGroups", promotionGroups);
        return jaloSession.getFlexibleSearch().search(ctx, promQuery.toString(), args, AbstractPromotion.class).getResult();
    }


    public static Collection<Product> getBaseProductsForOrder(SessionContext ctx, AbstractOrder order)
    {
        SortedSet<Product> products = new TreeSet<>();
        for(AbstractOrderEntry aoe : order.getAllEntries())
        {
            Product product = aoe.getProduct(ctx);
            if(product != null)
            {
                products.add(product);
                List<Product> baseProducts = Helper.getBaseProducts(ctx, product);
                if(baseProducts != null && !baseProducts.isEmpty())
                {
                    products.addAll(baseProducts);
                }
            }
        }
        return products;
    }


    public final PromotionOrderResults getPromotionResults(AbstractOrder order)
    {
        return getPromotionResults(getSession().getSessionContext(), order);
    }


    public PromotionOrderResults getPromotionResults(SessionContext ctx, AbstractOrder order)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("getPromotionResults for [" + order + "]");
        }
        List<PromotionResult> promotionResults = getPromotionResultsInternal(ctx, order);
        List<PromotionResult> validPromotionResults = new ArrayList<>(promotionResults.size());
        for(PromotionResult pr : promotionResults)
        {
            if(pr.isValid(ctx))
            {
                validPromotionResults.add(pr);
            }
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("getPromotionResults for [" + order + "] found [" + validPromotionResults.size() + "] promotion results");
        }
        return new PromotionOrderResults(ctx, order, validPromotionResults, 0.0D);
    }


    public PromotionOrderResults getPromotionResults(SessionContext ctx, Collection<PromotionGroup> promotionGroups, AbstractOrder order, boolean evaluateRestrictions, AutoApplyMode productPromotionMode, AutoApplyMode orderPromotionMode, Date date)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("getPromotionResults for [" + order + "]");
        }
        List<PromotionResult> promotionResults = getPromotionResultsInternal(ctx, order);
        boolean needsUpdate = false;
        for(PromotionResult pr : promotionResults)
        {
            if(!pr.isValid(ctx))
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
            return updatePromotions(ctx, promotionGroups, order, evaluateRestrictions, productPromotionMode, orderPromotionMode, date);
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("getPromotionResults for [" + order + "] found [" + promotionResults.size() + "] promotion results");
        }
        return new PromotionOrderResults(ctx, order, promotionResults, 0.0D);
    }


    protected List<PromotionResult> getPromotionResultsInternal(SessionContext ctx, AbstractOrder order)
    {
        try
        {
            if(order != null)
            {
                Set<PromotionResult> allPromotions = getAllPromotionResults(ctx, order);
                return (List<PromotionResult>)allPromotions.stream().filter(Objects::nonNull).sorted(sortByPromotionPriority()).collect(Collectors.toList());
            }
        }
        catch(Exception ex)
        {
            LOG.error("Failed to getPromotionResultsInternal", ex);
        }
        return new ArrayList<>(0);
    }


    protected Comparator<PromotionResult> sortByPromotionPriority()
    {
        return this.promotionResultComparator;
    }


    protected void deleteStoredPromotionResults(SessionContext ctx, AbstractOrder order, boolean undoActions)
    {
        AbstractOrder orderMtx = order;
        boolean calculateTotals = false;
        synchronized(orderMtx)
        {
            List<PromotionResult> results = getPromotionResultsInternal(ctx, order);
            for(PromotionResult result : results)
            {
                try
                {
                    if(undoActions)
                    {
                        calculateTotals |= result.undo(ctx);
                    }
                    result.remove(ctx);
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
                order.calculateTotals(true);
            }
            catch(JaloPriceFactoryException ex)
            {
                LOG.error("deleteStoredPromotionResult failed to calculateTotals on order [" + order + "]", (Throwable)ex);
            }
        }
    }


    public final void cleanupCart(Cart cart)
    {
        cleanupCart(getSession().getSessionContext(), cart);
    }


    public void cleanupCart(SessionContext ctx, Cart cart)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("cleanupCart for [" + cart + "]");
        }
        if(ctx != null && cart != null)
        {
            deleteStoredPromotionResults(ctx, (AbstractOrder)cart, false);
        }
    }


    public final void cleanupOrphanedResults()
    {
        cleanupOrphanedResults(getSession().getSessionContext());
    }


    public void cleanupOrphanedResults(SessionContext ctx)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("cleanupOrphanedResults");
        }
        try
        {
            String query = "SELECT {pr:" + Item.PK + "} FROM   {" + TypeManager.getInstance().getComposedType(PromotionResult.class).getCode() + " as pr LEFT JOIN " + TypeManager.getInstance().getComposedType(AbstractOrder.class).getCode() + " AS order ON {pr:order}={order:" + Item.PK
                            + "} } WHERE  {order:" + Item.PK + "} IS NULL";
            List<PromotionResult> promotionResults = getSession().getFlexibleSearch().search(ctx, query, Collections.emptyMap(), PromotionResult.class).getResult();
            if(promotionResults != null)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("cleanupOrphanedResults found [" + promotionResults.size() + "] results to remove");
                }
                for(PromotionResult result : promotionResults)
                {
                    try
                    {
                        result.remove(ctx);
                    }
                    catch(ConsistencyCheckException ccEx)
                    {
                        LOG.error("In cleanupOrphanedResults failed to remove promotion result [" + result + "]", (Throwable)ccEx);
                    }
                }
            }
        }
        catch(Exception ex)
        {
            LOG.error("Failed to cleanupOrphanedResults", ex);
        }
    }


    public final void transferPromotionsToOrder(AbstractOrder source, Order target, boolean onlyTransferAppliedPromotions)
    {
        transferPromotionsToOrder(getSession().getSessionContext(), source, target, onlyTransferAppliedPromotions);
    }


    public void transferPromotionsToOrder(SessionContext ctx, AbstractOrder source, Order target, boolean onlyTransferAppliedPromotions)
    {
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("transferPromotionsToOrder from [" + source + "] to [" + target + "] onlyTransferAppliedPromotions=[" + onlyTransferAppliedPromotions + "]");
                LOG.debug("Dump Source Order\r\n" + Helper.dumpOrder(ctx, source));
                LOG.debug("Dump Target Order\r\n" + Helper.dumpOrder(ctx, (AbstractOrder)target));
            }
            List<PromotionResult> promotionResults = getPromotionResultsInternal(ctx, source);
            if(promotionResults != null && !promotionResults.isEmpty())
            {
                for(PromotionResult result : promotionResults)
                {
                    if(!onlyTransferAppliedPromotions || result.isApplied(ctx))
                    {
                        result.transferToOrder(ctx, target);
                    }
                }
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("transferPromotionsToOrder completed");
                LOG.debug("Dump Target Order after transfer\r\n" + Helper.dumpOrder(ctx, (AbstractOrder)target));
            }
        }
        catch(Exception ex)
        {
            LOG.error("Failed to transferPromotionsToOrder", ex);
        }
    }


    public final RestrictionSetResult evaluateRestrictions(SessionContext ctx, List<Product> products, AbstractOrder order, AbstractPromotion promo, Date date)
    {
        return evaluateRestrictions(ctx, products, order, promo.getRestrictions(), date);
    }


    public RestrictionSetResult evaluateRestrictions(SessionContext ctx, List<Product> products, AbstractOrder order, Collection<AbstractPromotionRestriction> restrictions, Date date)
    {
        List<Product> allowedProducts = new ArrayList<>(products);
        if(restrictions != null && !restrictions.isEmpty())
        {
            for(AbstractPromotionRestriction apr : restrictions)
            {
                AbstractPromotionRestriction.RestrictionResult res = apr.evaluate(ctx, allowedProducts, date, order);
                if(res == AbstractPromotionRestriction.RestrictionResult.DENY || (res == AbstractPromotionRestriction.RestrictionResult.ADJUSTED_PRODUCTS && apr
                                .getPromotion() instanceof OrderPromotion && allowedProducts.isEmpty()))
                {
                    return new RestrictionSetResult();
                }
            }
        }
        return new RestrictionSetResult(allowedProducts);
    }


    public PromotionGroup getDefaultPromotionGroup()
    {
        return getDefaultPromotionGroup(getSession().getSessionContext());
    }


    public PromotionGroup getDefaultPromotionGroup(SessionContext ctx)
    {
        HashMap<String, Object> params = new HashMap<>();
        params.put("identifier", "default");
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" + GeneratedPromotionsConstants.TC.PROMOTIONGROUP + "} WHERE {Identifier} = ?identifier", params,
                        Collections.singletonList(PromotionGroup.class), true, true, 0, -1);
        List<PromotionGroup> results = res.getResult();
        if(results != null && !results.isEmpty())
        {
            return results.get(0);
        }
        return null;
    }


    public PromotionGroup getPromotionGroup(String identifier)
    {
        if(identifier == null)
        {
            throw new IllegalArgumentException("identifier cannot be null");
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("identifier", identifier);
        String query = "SELECT {" + Item.PK + "} FROM {" + GeneratedPromotionsConstants.TC.PROMOTIONGROUP + "} WHERE {Identifier} = ?identifier";
        SearchResult res = getSession().getFlexibleSearch().search(query, params,
                        Collections.singletonList(PromotionGroup.class), true, true, 0, -1);
        List<PromotionGroup> results = res.getResult();
        if(results != null && !results.isEmpty())
        {
            return results.get(0);
        }
        return null;
    }


    public PromotionOrderEntryConsumed createPromotionOrderEntryConsumed(SessionContext ctx, String code, AbstractOrderEntry orderEntry, long quantity)
    {
        double unitPrice = orderEntry.getBasePrice(ctx).doubleValue();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", code);
        parameters.put("orderEntry", orderEntry);
        parameters.put("quantity", Long.valueOf(quantity));
        parameters.put("adjustedUnitPrice", Double.valueOf(unitPrice));
        return createPromotionOrderEntryConsumed(ctx, parameters);
    }


    public PromotionOrderEntryConsumed createPromotionOrderEntryConsumed(SessionContext ctx, String code, AbstractOrderEntry orderEntry, long quantity, double adjustedUnitPrice)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", code);
        parameters.put("orderEntry", orderEntry);
        parameters.put("quantity", Long.valueOf(quantity));
        parameters.put("adjustedUnitPrice", Double.valueOf(adjustedUnitPrice));
        return createPromotionOrderEntryConsumed(ctx, parameters);
    }


    public PromotionResult createPromotionResult(SessionContext ctx, AbstractPromotion promotion, AbstractOrder order, float certainty)
    {
        if(promotion == null || order == null || certainty < 0.0F || certainty > 1.0F)
        {
            throw new PromotionException("Invalid attempt to create a promotion result");
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("promotion", promotion);
        parameters.put("order", order);
        parameters.put("certainty", Float.valueOf(certainty));
        return createPromotionResult(ctx, parameters);
    }


    public PromotionOrderAdjustTotalAction createPromotionOrderAdjustTotalAction(SessionContext ctx, double totalAdjustment)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("guid", makeActionGUID());
        parameters.put("amount", Double.valueOf(totalAdjustment));
        return createPromotionOrderAdjustTotalAction(ctx, parameters);
    }


    public PromotionOrderAddFreeGiftAction createPromotionOrderAddFreeGiftAction(SessionContext ctx, Product product, PromotionResult result)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("guid", makeActionGUID());
        parameters.put("freeProduct", product);
        parameters.put("promotionResult", result);
        return createPromotionOrderAddFreeGiftAction(ctx, parameters);
    }


    public PromotionOrderChangeDeliveryModeAction createPromotionOrderChangeDeliveryModeAction(SessionContext ctx, DeliveryMode deliveryMode)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("guid", makeActionGUID());
        parameters.put("deliveryMode", deliveryMode);
        return createPromotionOrderChangeDeliveryModeAction(ctx, parameters);
    }


    public PromotionPriceRow createPromotionPriceRow(Currency currency, double price)
    {
        return createPromotionPriceRow(getSession().getSessionContext(), currency, price);
    }


    public PromotionPriceRow createPromotionPriceRow(SessionContext ctx, Currency currency, double price)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("currency", currency);
        parameters.put("price", Double.valueOf(price));
        return createPromotionPriceRow(ctx, parameters);
    }


    public PromotionQuantityAndPricesRow createPromotionQuantityAndPricesRow(SessionContext ctx, long quantity, Collection<PromotionPriceRow> prices)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("quantity", Long.valueOf(quantity));
        parameters.put("prices", prices);
        return createPromotionQuantityAndPricesRow(ctx, parameters);
    }


    public PromotionOrderEntryAdjustAction createPromotionOrderEntryAdjustAction(SessionContext ctx, AbstractOrderEntry entry, long quantity, double adjustment)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("guid", makeActionGUID());
        parameters.put("amount", Double.valueOf(adjustment));
        parameters.put("orderEntryProduct", entry.getProduct(ctx));
        parameters.put("orderEntryNumber", entry.getEntryNumber());
        parameters.put("orderEntryQuantity", Long.valueOf(quantity));
        return createPromotionOrderEntryAdjustAction(ctx, parameters);
    }


    public PromotionOrderEntryAdjustAction createPromotionOrderEntryAdjustAction(SessionContext ctx, AbstractOrderEntry entry, double adjustment)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("guid", makeActionGUID());
        parameters.put("amount", Double.valueOf(adjustment));
        parameters.put("orderEntryProduct", entry.getProduct(ctx));
        parameters.put("orderEntryNumber", entry.getEntryNumber());
        parameters.put("orderEntryQuantity", entry.getQuantity(ctx));
        return createPromotionOrderEntryAdjustAction(ctx, parameters);
    }


    public PromotionGroup createPromotionGroup(SessionContext ctx, String identifier)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("Identifier", identifier);
        return createPromotionGroup(ctx, parameters);
    }


    public PromotionNullAction createPromotionNullAction(SessionContext ctx)
    {
        Map<String, Object> parameters = Maps.newHashMap();
        parameters.put("guid", makeActionGUID());
        return createPromotionNullAction(ctx, parameters);
    }


    protected static String makeActionGUID()
    {
        return "Action[" + (new VMID()).toString() + "]";
    }
}
