package de.hybris.platform.promotionengineservices.promotionengine.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotionengineservices.dao.PromotionDao;
import de.hybris.platform.promotionengineservices.dao.PromotionSourceRuleDao;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotionengineservices.promotionengine.PromotionEngineService;
import de.hybris.platform.promotionengineservices.util.ActionUtils;
import de.hybris.platform.promotionengineservices.validators.RuleBasedPromotionsContextValidator;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.impl.DefaultPromotionsService;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.model.AbstractPromotionActionModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.promotions.model.PromotionOrderEntryConsumedModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import de.hybris.platform.ruleengine.RuleEngineService;
import de.hybris.platform.ruleengine.RuleEvaluationContext;
import de.hybris.platform.ruleengine.RuleEvaluationResult;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.dao.RuleEngineContextDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.exception.RuleEngineRuntimeException;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.strategies.RuleEngineContextFinderStrategy;
import de.hybris.platform.ruleengine.strategies.RuleEngineContextForCatalogVersionsFinderStrategy;
import de.hybris.platform.ruleengineservices.action.RuleActionService;
import de.hybris.platform.ruleengineservices.action.RuleActionStrategy;
import de.hybris.platform.ruleengineservices.enums.FactContextType;
import de.hybris.platform.ruleengineservices.rao.providers.FactContextFactory;
import de.hybris.platform.ruleengineservices.rao.providers.RAOProvider;
import de.hybris.platform.ruleengineservices.rao.providers.impl.FactContext;
import de.hybris.platform.ruleengineservices.util.ProductUtils;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.util.DiscountValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPromotionEngineService implements PromotionEngineService, PromotionsService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPromotionEngineService.class);
    private static final String GET_PROMOTIONS_FOR_PRODUCT_PROPERTY = "promotionengineservices.getpromotionsforproduct.disable";
    private static final String NO_RULE_ENGINE_CTX_FOR_ORDER_ERROR_MESSAGE = "No rule engine context could be derived for order [%s]";
    private static final String NO_RULE_ENGINE_CTX_FOR_PRODUCT_ERROR_MESSAGE = "No rule engine context could be derived for product [%s]";
    private RuleEngineService commerceRuleEngineService;
    private CalculationService calculationService;
    private RuleEngineContextDao ruleEngineContextDao;
    private RuleActionService ruleActionService;
    private List<RuleActionStrategy> strategies;
    private EngineRuleDao engineRuleDao;
    private FlexibleSearchService flexibleSearchService;
    private PromotionDao promotionDao;
    private FactContextFactory factContextFactory;
    private DefaultPromotionsService defaultPromotionsService;
    private PromotionSourceRuleDao promotionSourceRuleDao;
    private CategoryService categoryService;
    private ConfigurationService configurationService;
    private CatalogVersionService catalogVersionService;
    private RuleEngineContextForCatalogVersionsFinderStrategy ruleEngineContextForCatalogVersionsFinderStrategy;
    private RuleEngineContextFinderStrategy ruleEngineContextFinderStrategy;
    private RuleBasedPromotionsContextValidator ruleBasedPromotionsContextValidator;
    private SessionService sessionService;
    private ModelService modelService;
    private TimeService timeService;
    private ProductUtils productUtils;
    private ActionUtils actionUtils;


    public RuleEvaluationResult evaluate(AbstractOrderModel order, Collection<PromotionGroupModel> promotionGroups)
    {
        return evaluate(order, promotionGroups, getTimeService().getCurrentTime());
    }


    public RuleEvaluationResult evaluate(AbstractOrderModel order, Collection<PromotionGroupModel> promotionGroups, Date date)
    {
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Promotion cart evaluation triggered for cart with code '{}'", order.getCode());
        }
        List<Object> facts = Lists.newArrayList();
        facts.add(order);
        facts.addAll(promotionGroups);
        if(Objects.nonNull(date))
        {
            facts.add(date);
        }
        try
        {
            RuleEvaluationContext context = prepareContext(
                            getFactContextFactory().createFactContext(FactContextType.PROMOTION_ORDER, facts),
                            determineRuleEngineContext(order));
            return getCommerceRuleEngineService().evaluate(context);
        }
        catch(IllegalStateException e)
        {
            LOGGER.error("Promotion rule evaluation failed", e);
            RuleEvaluationResult result = new RuleEvaluationResult();
            result.setErrorMessage(e.getMessage());
            result.setEvaluationFailed(true);
            return result;
        }
    }


    protected RuleEvaluationContext prepareContext(FactContext factContext, AbstractRuleEngineContextModel ruleEngineContext)
    {
        Set<Object> convertedFacts = provideRAOs(factContext);
        RuleEvaluationContext evaluationContext = new RuleEvaluationContext();
        evaluationContext.setRuleEngineContext(ruleEngineContext);
        evaluationContext.setFacts(convertedFacts);
        return evaluationContext;
    }


    protected AbstractRuleEngineContextModel determineRuleEngineContext(AbstractOrderModel order)
    {
        return (AbstractRuleEngineContextModel)getRuleEngineContextFinderStrategy().findRuleEngineContext(order, RuleType.PROMOTION)
                        .orElseThrow(() -> new IllegalStateException(String.format("No rule engine context could be derived for order [%s]", new Object[] {order})));
    }


    protected AbstractRuleEngineContextModel determineRuleEngineContext(ProductModel product)
    {
        return (AbstractRuleEngineContextModel)getRuleEngineContextFinderStrategy().findRuleEngineContext(product, RuleType.PROMOTION)
                        .orElseThrow(() -> new IllegalStateException(String.format("No rule engine context could be derived for product [%s]", new Object[] {product})));
    }


    protected Set<Object> provideRAOs(FactContext factContext)
    {
        Set<Object> result = new HashSet();
        for(Object fact : factContext.getFacts())
        {
            for(RAOProvider raoProvider : factContext.getProviders(fact))
            {
                result.addAll(raoProvider.expandFactModel(fact));
            }
        }
        return result;
    }


    public List<? extends AbstractPromotionModel> getAbstractProductPromotions(Collection<PromotionGroupModel> promotionGroups, ProductModel product)
    {
        return Collections.emptyList();
    }


    public List<? extends AbstractPromotionModel> getAbstractProductPromotions(Collection<PromotionGroupModel> promotionGroups, ProductModel product, boolean evaluateRestrictions, Date date)
    {
        return (List)getPromotionsForProduct(promotionGroups, product);
    }


    public List<ProductPromotionModel> getProductPromotions(Collection<PromotionGroupModel> promotionGroups, ProductModel product)
    {
        return Collections.emptyList();
    }


    public List<ProductPromotionModel> getProductPromotions(Collection<PromotionGroupModel> promotionGroups, ProductModel product, boolean evaluateRestrictions, Date date)
    {
        return Collections.emptyList();
    }


    public List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> promotionGroups)
    {
        return Collections.emptyList();
    }


    public List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> promotionGroups, Date date)
    {
        return Collections.emptyList();
    }


    public List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> promotionGroups, ProductModel product)
    {
        return Collections.emptyList();
    }


    public List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> promotionGroups, ProductModel product, Date date)
    {
        return Collections.emptyList();
    }


    public List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> promotionGroups, boolean evaluateRestrictions)
    {
        return Collections.emptyList();
    }


    public List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> promotionGroups, boolean evaluateRestrictions, Date date)
    {
        return Collections.emptyList();
    }


    public List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> promotionGroups, boolean evaluateRestrictions, ProductModel product)
    {
        return Collections.emptyList();
    }


    public List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> promotionGroups, boolean evaluateRestrictions, ProductModel product, Date date)
    {
        return Collections.emptyList();
    }


    public PromotionOrderResults updatePromotions(Collection<PromotionGroupModel> promotionGroups, AbstractOrderModel order)
    {
        Object perSessionLock = getSessionService().getOrLoadAttribute("promotionsUpdateLock", SerializableObject::new);
        synchronized(perSessionLock)
        {
            return updatePromotionsNotThreadSafe(promotionGroups, order);
        }
    }


    protected PromotionOrderResults updatePromotionsNotThreadSafe(Collection<PromotionGroupModel> promotionGroups, AbstractOrderModel order)
    {
        return updatePromotionsNotThreadSafe(promotionGroups, order, getTimeService().getCurrentTime());
    }


    protected PromotionOrderResults updatePromotionsNotThreadSafe(Collection<PromotionGroupModel> promotionGroups, AbstractOrderModel order, Date date)
    {
        List<PromotionResult> actionApplicationResults;
        cleanupAbstractOrder(order);
        try
        {
            RuleEvaluationResult ruleEvaluationResult = evaluate(order, promotionGroups, date);
            if(!ruleEvaluationResult.isEvaluationFailed())
            {
                List<ItemModel> applyAllActionModels = getRuleActionService().applyAllActions(ruleEvaluationResult.getResult());
                List<PromotionResultModel> applyAllActionsPromotionResultModel = (List<PromotionResultModel>)applyAllActionModels.stream().filter(item -> item instanceof PromotionResultModel).map(item -> (PromotionResultModel)item).collect(Collectors.toList());
                actionApplicationResults = (List<PromotionResult>)getModelService().getAllSources(applyAllActionsPromotionResultModel, new ArrayList());
            }
            else
            {
                actionApplicationResults = Lists.newArrayList();
            }
        }
        catch(RuleEngineRuntimeException rere)
        {
            LOGGER.error(rere.getMessage(), (Throwable)rere);
            actionApplicationResults = new ArrayList<>();
        }
        getModelService().refresh(order);
        return new PromotionOrderResults(
                        JaloSession.getCurrentSession().getSessionContext(), (AbstractOrder)getModelService().getSource(order), actionApplicationResults, 0.0D);
    }


    public PromotionOrderResults updatePromotions(Collection<PromotionGroupModel> promotionGroups, AbstractOrderModel order, boolean evaluateRestrictions, PromotionsManager.AutoApplyMode productPromotionMode, PromotionsManager.AutoApplyMode orderPromotionMode, Date date)
    {
        Object perSessionLock = getSessionService().getOrLoadAttribute("promotionsUpdateLock", SerializableObject::new);
        synchronized(perSessionLock)
        {
            return updatePromotionsNotThreadSafe(promotionGroups, order, date);
        }
    }


    public PromotionOrderResults getPromotionResults(AbstractOrderModel order)
    {
        if(getModelService().isNew(order))
        {
            return new PromotionOrderResults(JaloSession.getCurrentSession().getSessionContext(), null,
                            Collections.emptyList(), 0.0D);
        }
        Set<PromotionResultModel> promotionResultModels = order.getAllPromotionResults();
        List<PromotionResult> promotionResults = (List<PromotionResult>)getModelService().getAllSources(promotionResultModels, new ArrayList());
        return new PromotionOrderResults(JaloSession.getCurrentSession().getSessionContext(), (AbstractOrder)
                        getModelService().getSource(order), promotionResults, 0.0D);
    }


    public PromotionOrderResults getPromotionResults(Collection<PromotionGroupModel> promotionGroups, AbstractOrderModel order, boolean evaluateRestrictions, PromotionsManager.AutoApplyMode productPromotionMode, PromotionsManager.AutoApplyMode orderPromotionMode, Date date)
    {
        Set<PromotionResultModel> promotionResultModels = order.getAllPromotionResults();
        if(CollectionUtils.isNotEmpty(promotionResultModels) && ((PromotionResultModel)promotionResultModels
                        .iterator().next()).getPromotion() instanceof RuleBasedPromotionModel)
        {
            return null;
        }
        return getDefaultPromotionsService().getPromotionResults(promotionGroups, order, evaluateRestrictions, productPromotionMode, orderPromotionMode, date);
    }


    public void cleanupCart(CartModel cart)
    {
        cleanupAbstractOrder((AbstractOrderModel)cart);
    }


    protected void cleanupAbstractOrder(AbstractOrderModel cart)
    {
        Set<PromotionResultModel> promotionResultModels = cart.getAllPromotionResults();
        if(CollectionUtils.isEmpty(promotionResultModels))
        {
            return;
        }
        for(PromotionResultModel promoResultModel : promotionResultModels)
        {
            for(AbstractPromotionActionModel action : promoResultModel.getActions())
            {
                if(action instanceof AbstractRuleBasedPromotionActionModel)
                {
                    undoPromotionAction(action);
                }
            }
            getModelService().removeAll(promoResultModel.getConsumedEntries());
            getModelService().removeAll(promoResultModel.getActions());
            getModelService().remove(promoResultModel);
        }
        for(AbstractOrderEntryModel entry : cart.getEntries())
        {
            List<DiscountValue> newDVs = (List<DiscountValue>)entry.getDiscountValues().stream().filter(dv -> !getActionUtils().isActionUUID(dv.getCode())).collect(Collectors.toList());
            entry.setDiscountValues(newDVs);
        }
        getModelService().refresh(cart);
        recalculateCart(cart);
    }


    protected void undoPromotionAction(AbstractPromotionActionModel action)
    {
        AbstractRuleBasedPromotionActionModel promoAction = (AbstractRuleBasedPromotionActionModel)action;
        RuleActionStrategy ruleActionStrategy = getRuleActionStrategy(promoAction.getStrategyId());
        if(ruleActionStrategy != null)
        {
            ruleActionStrategy.undo((ItemModel)promoAction);
        }
    }


    public void transferPromotionsToOrder(AbstractOrderModel source, OrderModel target, boolean onlyTransferAppliedPromotions)
    {
        Set<PromotionResultModel> sourcePromotionResults = source.getAllPromotionResults();
        if(CollectionUtils.isNotEmpty(sourcePromotionResults))
        {
            List<Object> toSave = Lists.newArrayList();
            for(PromotionResultModel sourcePromoResult : sourcePromotionResults)
            {
                PromotionResultModel targetPromoResult = (PromotionResultModel)getModelService().clone(sourcePromoResult);
                toSave.add(targetPromoResult);
                targetPromoResult.setOrder((AbstractOrderModel)target);
                targetPromoResult.setOrderCode(target.getCode());
                targetPromoResult.setActions(Lists.newArrayList(targetPromoResult.getAllPromotionActions()));
                if(Objects.isNull(targetPromoResult.getConsumedEntries()))
                {
                    targetPromoResult.setConsumedEntries(Lists.newArrayList());
                }
                for(PromotionOrderEntryConsumedModel consumedEntry : targetPromoResult.getConsumedEntries())
                {
                    consumedEntry.setOrderEntry(target.getEntries().stream().filter(entry ->
                                                    (Objects.nonNull(entry.getEntryNumber()) && Objects.equals(entry.getEntryNumber(), consumedEntry.getOrderEntryNumberWithFallback())))
                                    .findFirst().orElse(null));
                    consumedEntry.setPromotionResult(targetPromoResult);
                }
                targetPromoResult.setPromotion(sourcePromoResult.getPromotion());
            }
            getModelService().saveAll(toSave);
            toSave.forEach(o -> getModelService().refresh(o));
        }
    }


    protected String getDataUniqueKey(AbstractPromotionModel sourcePromotion)
    {
        SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
        StringBuilder builder = new StringBuilder();
        builder.append(sourcePromotion.getClass().getSimpleName()).append('|');
        if(sourcePromotion.getPromotionGroup() != null)
        {
            builder.append(sourcePromotion.getPromotionGroup().getIdentifier()).append('|');
        }
        builder.append(sourcePromotion.getCode()).append('|').append(sourcePromotion.getPriority()).append('|')
                        .append(ctx.getLanguage().getIsocode()).append('|');
        Date startDate = sourcePromotion.getStartDate();
        if(startDate == null)
        {
            builder.append("x|");
        }
        else
        {
            builder.append(startDate.getTime()).append('|');
        }
        Date endDate = sourcePromotion.getEndDate();
        if(endDate == null)
        {
            builder.append("x|");
        }
        else
        {
            builder.append(endDate.getTime()).append('|');
        }
        if(sourcePromotion instanceof RuleBasedPromotionModel)
        {
            AbstractRuleEngineRuleModel rule = ((RuleBasedPromotionModel)sourcePromotion).getRule();
            if(rule != null && rule.getRuleContent() != null)
            {
                builder.append(rule.getRuleContent()).append('|');
            }
            else
            {
                builder.append("x|");
            }
        }
        return builder.toString();
    }


    static String buildMD5Hash(String message)
    {
        return DigestUtils.md5Hex(message);
    }


    protected <T> T findImmutablePromotionByUniqueKey(String immutableKeyHash, Predicate<T> immutableKeyPredicate)
    {
        Map<String, String> params = new HashMap<>();
        params.put("immutableKeyHash", immutableKeyHash);
        String query = "SELECT {" + Item.PK + "} FROM   {AbstractPromotion} WHERE  {immutableKeyHash} = ?immutableKeyHash";
        SearchResult<T> searchResult = getFlexibleSearchService().search(query, params);
        if(!searchResult.getResult().isEmpty())
        {
            return searchResult.getResult().stream().filter(immutableKeyPredicate).findFirst().orElse(null);
        }
        return null;
    }


    public PromotionGroupModel getDefaultPromotionGroup()
    {
        return getPromotionDao().findDefaultPromotionGroup();
    }


    public PromotionGroupModel getPromotionGroup(String identifier)
    {
        return getPromotionDao().findPromotionGroupByCode(identifier);
    }


    public Collection<AbstractPromotionRestrictionModel> getRestrictions(AbstractPromotionModel promotion)
    {
        if(promotion instanceof RuleBasedPromotionModel)
        {
            return Collections.emptyList();
        }
        return getDefaultPromotionsService().getRestrictions(promotion);
    }


    public String getPromotionDescription(AbstractPromotionModel promotion)
    {
        if(promotion instanceof RuleBasedPromotionModel)
        {
            return ((RuleBasedPromotionModel)promotion).getPromotionDescription();
        }
        return promotion.getDescription();
    }


    protected RuleActionStrategy getRuleActionStrategy(String strategyId)
    {
        if(strategyId == null)
        {
            LOGGER.error("strategyId is not defined!");
            return null;
        }
        if(getStrategies() != null)
        {
            for(RuleActionStrategy strategy : getStrategies())
            {
                if(strategyId.equals(strategy.getStrategyId()))
                {
                    return strategy;
                }
            }
            LOGGER.error("cannot find RuleActionStrategy for given strategyId:{}", strategyId);
        }
        else
        {
            LOGGER.error("cannot call getRuleActionStrategy(\"{}\"), no strategies are defined! Please configure your {} bean to contain strategies.", strategyId,
                            getClass().getSimpleName());
        }
        return null;
    }


    protected List<RuleActionStrategy> getStrategies()
    {
        return this.strategies;
    }


    @Required
    public void setStrategies(List<RuleActionStrategy> strategies)
    {
        this.strategies = strategies;
    }


    protected boolean recalculateCart(AbstractOrderModel order)
    {
        try
        {
            getCalculationService().calculateTotals(order, true);
        }
        catch(CalculationException e)
        {
            LOGGER.error(String.format("Recalculation of order with code '%s' failed.", new Object[] {order.getCode()}), (Throwable)e);
            order.setCalculated(Boolean.FALSE);
            getModelService().save(order);
            return false;
        }
        return true;
    }


    protected EngineRuleDao getEngineRuleDao()
    {
        return this.engineRuleDao;
    }


    @Required
    public void setEngineRuleDao(EngineRuleDao engineRuleDao)
    {
        this.engineRuleDao = engineRuleDao;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected PromotionDao getPromotionDao()
    {
        return this.promotionDao;
    }


    @Required
    public void setPromotionDao(PromotionDao promotionDao)
    {
        this.promotionDao = promotionDao;
    }


    protected FactContextFactory getFactContextFactory()
    {
        return this.factContextFactory;
    }


    @Required
    public void setFactContextFactory(FactContextFactory factContextFactory)
    {
        this.factContextFactory = factContextFactory;
    }


    public DefaultPromotionsService getDefaultPromotionsService()
    {
        return this.defaultPromotionsService;
    }


    @Required
    public void setDefaultPromotionsService(DefaultPromotionsService defaultPromotionsService)
    {
        this.defaultPromotionsService = defaultPromotionsService;
    }


    protected RuleEngineService getCommerceRuleEngineService()
    {
        return this.commerceRuleEngineService;
    }


    @Required
    public void setCommerceRuleEngineService(RuleEngineService ruleEngineService)
    {
        this.commerceRuleEngineService = ruleEngineService;
    }


    protected CalculationService getCalculationService()
    {
        return this.calculationService;
    }


    @Required
    public void setCalculationService(CalculationService calculationService)
    {
        this.calculationService = calculationService;
    }


    protected RuleActionService getRuleActionService()
    {
        return this.ruleActionService;
    }


    @Required
    public void setRuleActionService(RuleActionService ruleActionService)
    {
        this.ruleActionService = ruleActionService;
    }


    protected RuleEngineContextDao getRuleEngineContextDao()
    {
        return this.ruleEngineContextDao;
    }


    @Required
    public void setRuleEngineContextDao(RuleEngineContextDao ruleEngineContextDao)
    {
        this.ruleEngineContextDao = ruleEngineContextDao;
    }


    protected List<RuleBasedPromotionModel> getPromotionsForProduct(Collection<PromotionGroupModel> promotionGroups, ProductModel product)
    {
        if(getConfigurationService().getConfiguration().getBoolean("promotionengineservices.getpromotionsforproduct.disable"))
        {
            LOGGER.info("Promotions for product are disabled. If you want to enable them, please change the property {} to false.", "promotionengineservices.getpromotionsforproduct.disable");
            return Collections.emptyList();
        }
        return (List<RuleBasedPromotionModel>)Streams.concat(new Stream[] {getPromotions(promotionGroups, product).stream(),
                                        getProductUtils().getAllBaseProducts(product).stream().flatMap(b -> getPromotions(promotionGroups, b).stream())}).distinct()
                        .filter(p -> isApplicable(product, p))
                        .sorted(Comparator.comparing(AbstractPromotionModel::getPriority))
                        .collect(Collectors.toList());
    }


    protected ArrayList<RuleBasedPromotionModel> getPromotions(Collection<PromotionGroupModel> promotionGroups, ProductModel product)
    {
        return Lists.newArrayList(getPromotionSourceRuleDao()
                        .findPromotions(promotionGroups, product.getCode(), getCategoryCodesForProduct(product)));
    }


    protected boolean isApplicable(ProductModel product, RuleBasedPromotionModel promotion)
    {
        return (Objects.nonNull(promotion) && getRuleBasedPromotionsContextValidator()
                        .isApplicable(promotion, product.getCatalogVersion(), RuleType.PROMOTION));
    }


    protected Set<String> getCategoryCodesForProduct(ProductModel product)
    {
        Set<CategoryModel> allCategories = new HashSet<>();
        Collection<CategoryModel> supercategories = product.getSupercategories();
        if(CollectionUtils.isNotEmpty(supercategories))
        {
            allCategories.addAll(supercategories);
            for(CategoryModel category : supercategories)
            {
                allCategories.addAll(getCategoryService().getAllSupercategoriesForCategory(category));
            }
        }
        Set<String> allCategoryCodes = new HashSet<>();
        allCategories.forEach(cat -> allCategoryCodes.add(cat.getCode()));
        return allCategoryCodes;
    }


    protected PromotionSourceRuleDao getPromotionSourceRuleDao()
    {
        return this.promotionSourceRuleDao;
    }


    @Required
    public void setPromotionSourceRuleDao(PromotionSourceRuleDao promotionSourceRuleDao)
    {
        this.promotionSourceRuleDao = promotionSourceRuleDao;
    }


    protected CategoryService getCategoryService()
    {
        return this.categoryService;
    }


    @Required
    public void setCategoryService(CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    protected RuleEngineContextForCatalogVersionsFinderStrategy getRuleEngineContextForCatalogVersionsFinderStrategy()
    {
        return this.ruleEngineContextForCatalogVersionsFinderStrategy;
    }


    @Required
    public void setRuleEngineContextForCatalogVersionsFinderStrategy(RuleEngineContextForCatalogVersionsFinderStrategy ruleEngineContextForCatalogVersionsFinderStrategy)
    {
        this.ruleEngineContextForCatalogVersionsFinderStrategy = ruleEngineContextForCatalogVersionsFinderStrategy;
    }


    protected RuleEngineContextFinderStrategy getRuleEngineContextFinderStrategy()
    {
        return this.ruleEngineContextFinderStrategy;
    }


    @Required
    public void setRuleEngineContextFinderStrategy(RuleEngineContextFinderStrategy ruleEngineContextFinderStrategy)
    {
        this.ruleEngineContextFinderStrategy = ruleEngineContextFinderStrategy;
    }


    protected RuleBasedPromotionsContextValidator getRuleBasedPromotionsContextValidator()
    {
        return this.ruleBasedPromotionsContextValidator;
    }


    @Required
    public void setRuleBasedPromotionsContextValidator(RuleBasedPromotionsContextValidator ruleBasedPromotionsContextValidator)
    {
        this.ruleBasedPromotionsContextValidator = ruleBasedPromotionsContextValidator;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected TimeService getTimeService()
    {
        return this.timeService;
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }


    protected ProductUtils getProductUtils()
    {
        return this.productUtils;
    }


    @Required
    public void setProductUtils(ProductUtils productUtils)
    {
        this.productUtils = productUtils;
    }


    public ActionUtils getActionUtils()
    {
        return this.actionUtils;
    }


    public void setActionUtils(ActionUtils actionUtils)
    {
        this.actionUtils = actionUtils;
    }
}
