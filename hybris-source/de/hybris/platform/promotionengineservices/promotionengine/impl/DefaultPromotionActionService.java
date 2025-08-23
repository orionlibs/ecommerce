package de.hybris.platform.promotionengineservices.promotionengine.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.promotionengineservices.constants.PromotionEngineServicesConstants;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotionengineservices.promotionengine.PromotionActionService;
import de.hybris.platform.promotions.PromotionResultService;
import de.hybris.platform.promotions.model.AbstractPromotionActionModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionOrderEntryConsumedModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.versioning.ModuleVersioningService;
import de.hybris.platform.ruleengineservices.order.dao.ExtendedOrderDao;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryConsumedRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.DiscountValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPromotionActionService implements PromotionActionService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultPromotionActionService.class);
    private ModuleVersioningService moduleVersioningService;
    private ProductDao productDao;
    private CalculationService calculationService;
    private ExtendedOrderDao extendedOrderDao;
    private ModelService modelService;
    private EngineRuleDao engineRuleDao;
    private PromotionResultService promotionResultService;
    private CartService cartService;


    public void recalculateTotals(AbstractOrderModel order)
    {
        try
        {
            getCalculationService().calculateTotals(order, true);
        }
        catch(CalculationException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e.getMessage(), (Throwable)e);
            }
            order.setCalculated(Boolean.FALSE);
            getModelService().save(order);
        }
    }


    public void recalculateFiredPromotionMessage(PromotionResultModel promoResult)
    {
        promoResult.setMessageFired(getPromotionResultService().getDescription(promoResult));
        getModelService().save(promoResult);
    }


    public PromotionResultModel createPromotionResult(AbstractRuleActionRAO actionRao)
    {
        AbstractOrderModel order = getOrderInternal(actionRao);
        if(order == null)
        {
            AbstractOrderEntryModel orderEntry = getOrderEntry(actionRao);
            if(orderEntry != null)
            {
                order = orderEntry.getOrder();
            }
        }
        AbstractRuleEngineRuleModel engineRule = getRule(actionRao);
        PromotionResultModel promoResult = findExistingPromotionResultModel(engineRule, order);
        if(Objects.isNull(promoResult))
        {
            promoResult = (PromotionResultModel)getModelService().create(PromotionResultModel.class);
        }
        promoResult.setOrder(order);
        if(Objects.nonNull(order))
        {
            promoResult.setOrderCode(order.getCode());
        }
        promoResult.setPromotion((AbstractPromotionModel)getPromotion(actionRao));
        promoResult.setRulesModuleName(actionRao.getModuleName());
        if(StringUtils.isEmpty(promoResult.getMessageFired()))
        {
            promoResult.setMessageFired(getPromotionResultService().getDescription(promoResult));
        }
        if(Objects.nonNull(engineRule))
        {
            promoResult.setRuleVersion(engineRule.getVersion());
            setRuleModuleVersionIfApplicable(promoResult, engineRule);
        }
        Collection<PromotionOrderEntryConsumedModel> newConsumedEntries = createConsumedEntries(actionRao);
        if(CollectionUtils.isNotEmpty(newConsumedEntries))
        {
            for(PromotionOrderEntryConsumedModel ce : newConsumedEntries)
            {
                ce.setPromotionResult(promoResult);
            }
        }
        if(CollectionUtils.isEmpty(promoResult.getConsumedEntries()))
        {
            promoResult.setConsumedEntries(newConsumedEntries);
        }
        else if(CollectionUtils.isNotEmpty(newConsumedEntries))
        {
            List<PromotionOrderEntryConsumedModel> allConsumedEntries = Lists.newArrayList(promoResult.getConsumedEntries());
            allConsumedEntries.addAll(newConsumedEntries);
            promoResult.setConsumedEntries(allConsumedEntries);
        }
        if(actionRao instanceof de.hybris.platform.ruleengineservices.rao.DisplayMessageRAO)
        {
            promoResult.setCertainty(PromotionEngineServicesConstants.PromotionCertainty.POTENTIAL.value());
        }
        else
        {
            promoResult.setCertainty(PromotionEngineServicesConstants.PromotionCertainty.FIRED.value());
        }
        return promoResult;
    }


    protected void setRuleModuleVersionIfApplicable(PromotionResultModel promoResult, AbstractRuleEngineRuleModel rule)
    {
        promoResult.setModuleVersion(this.moduleVersioningService.getModuleVersion(rule).orElse(null));
    }


    protected PromotionResultModel findExistingPromotionResultModel(AbstractRuleEngineRuleModel rule, AbstractOrderModel order)
    {
        if(rule != null && order != null)
        {
            Set<PromotionResultModel> results = order.getAllPromotionResults();
            for(PromotionResultModel result : results)
            {
                Collection<AbstractPromotionActionModel> actions = result.getActions();
                if(actions.stream().filter(a -> a instanceof AbstractRuleBasedPromotionActionModel)
                                .map(a -> (AbstractRuleBasedPromotionActionModel)a)
                                .anyMatch(a -> (a.getRule() != null && rule.getPk().equals(a.getRule().getPk()))))
                {
                    return result;
                }
            }
        }
        return null;
    }


    protected Collection<PromotionOrderEntryConsumedModel> createConsumedEntries(AbstractRuleActionRAO action)
    {
        List<PromotionOrderEntryConsumedModel> promotionOrderEntriesConsumed = null;
        if(Objects.nonNull(action) && Objects.nonNull(action.getConsumedEntries()))
        {
            List<OrderEntryConsumedRAO> orderEntryConsumedRAOsForRule = (List<OrderEntryConsumedRAO>)action.getConsumedEntries().stream().filter(oec -> (oec.getFiredRuleCode() != null && oec.getFiredRuleCode().equals(action.getFiredRuleCode()))).collect(Collectors.toList());
            promotionOrderEntriesConsumed = new ArrayList<>();
            for(OrderEntryConsumedRAO orderEntryConsumedRAO : orderEntryConsumedRAOsForRule)
            {
                PromotionOrderEntryConsumedModel promotionOrderEntryConsumed = (PromotionOrderEntryConsumedModel)getModelService().create(PromotionOrderEntryConsumedModel.class);
                AbstractOrderEntryModel orderEntry = getOrderEntry(orderEntryConsumedRAO.getOrderEntry());
                promotionOrderEntryConsumed.setOrderEntry(orderEntry);
                promotionOrderEntryConsumed.setOrderEntryNumber(orderEntry.getEntryNumber());
                promotionOrderEntryConsumed.setQuantity(Long.valueOf(orderEntryConsumedRAO.getQuantity()));
                if(orderEntryConsumedRAO.getAdjustedUnitPrice() != null)
                {
                    promotionOrderEntryConsumed.setAdjustedUnitPrice(Double.valueOf(orderEntryConsumedRAO.getAdjustedUnitPrice()
                                    .doubleValue()));
                }
                promotionOrderEntriesConsumed.add(promotionOrderEntryConsumed);
            }
        }
        return promotionOrderEntriesConsumed;
    }


    public void createDiscountValue(DiscountRAO discountRao, String code, AbstractOrderModel order)
    {
        boolean isAbsoluteDiscount = (discountRao.getCurrencyIsoCode() != null);
        DiscountValue discountValue = new DiscountValue(code, discountRao.getValue().doubleValue(), isAbsoluteDiscount, order.getCurrency().getIsocode());
        List<DiscountValue> globalDVs = new ArrayList<>(order.getGlobalDiscountValues());
        globalDVs.add(discountValue);
        order.setGlobalDiscountValues(globalDVs);
        order.setCalculated(Boolean.FALSE);
    }


    protected boolean removeDiscount(String code, List<DiscountValue> discountValuesList, Consumer<List<DiscountValue>> setNewDiscountValues)
    {
        List<DiscountValue> filteredDVs = (List<DiscountValue>)discountValuesList.stream().filter(dv -> !code.equals(dv.getCode())).collect(Collectors.toList());
        boolean changed = (filteredDVs.size() != discountValuesList.size());
        if(setNewDiscountValues != null && changed)
        {
            setNewDiscountValues.accept(filteredDVs);
        }
        return changed;
    }


    protected boolean removeOrderLevelDiscount(String code, AbstractOrderModel order)
    {
        return removeDiscount(code, order.getGlobalDiscountValues(), dvs -> order.setGlobalDiscountValues(dvs));
    }


    protected boolean removeOrderEntryLevelDiscount(String code, AbstractOrderEntryModel orderEntry)
    {
        return removeDiscount(code, orderEntry.getDiscountValues(), dvs -> orderEntry.setDiscountValues(dvs));
    }


    protected List<ItemModel> removeOrderEntryLevelDiscounts(String code, AbstractOrderModel order)
    {
        return (List<ItemModel>)order.getEntries().stream().filter(entry -> removeOrderEntryLevelDiscount(code, entry)).collect(Collectors.toList());
    }


    public List<ItemModel> removeDiscountValue(String code, AbstractOrderModel order)
    {
        List<ItemModel> modifiedItems = new LinkedList<>();
        if(removeOrderLevelDiscount(code, order))
        {
            modifiedItems.add(order);
        }
        modifiedItems.addAll(removeOrderEntryLevelDiscounts(code, order));
        return modifiedItems;
    }


    public void createDiscountValue(DiscountRAO discountRao, String code, AbstractOrderEntryModel orderEntry)
    {
        boolean isAbsoluteDiscount = Objects.nonNull(discountRao.getCurrencyIsoCode());
        DiscountValue discountValue = new DiscountValue(code, discountRao.getValue().doubleValue(), isAbsoluteDiscount, orderEntry.getOrder().getCurrency().getIsocode());
        List<DiscountValue> globalDVs = new ArrayList<>(orderEntry.getDiscountValues());
        globalDVs.add(discountValue);
        orderEntry.setDiscountValues(globalDVs);
        orderEntry.setCalculated(Boolean.FALSE);
    }


    public AbstractOrderEntryModel getOrderEntry(AbstractRuleActionRAO action)
    {
        ServicesUtil.validateParameterNotNull(action, "action must not be null");
        if(!(action.getAppliedToObject() instanceof OrderEntryRAO))
        {
            return null;
        }
        return getOrderEntry((OrderEntryRAO)action.getAppliedToObject());
    }


    protected AbstractOrderEntryModel getOrderEntry(OrderEntryRAO orderEntryRao)
    {
        ServicesUtil.validateParameterNotNull(orderEntryRao, "orderEntryRao must not be null");
        ServicesUtil.validateParameterNotNull(orderEntryRao.getEntryNumber(), "orderEntryRao.entryNumber must not be null");
        ServicesUtil.validateParameterNotNull(orderEntryRao.getProductCode(), "orderEntryRao.productCode must not be null");
        AbstractOrderModel order = getOrder(orderEntryRao.getOrder());
        if(order == null)
        {
            return null;
        }
        for(AbstractOrderEntryModel entry : order.getEntries())
        {
            if(orderEntryRao.getEntryNumber().equals(entry.getEntryNumber()) && orderEntryRao
                            .getProductCode().equals(entry.getProduct().getCode()))
            {
                return entry;
            }
        }
        return null;
    }


    public AbstractOrderModel getOrder(AbstractRuleActionRAO action)
    {
        AbstractOrderModel order = getOrderInternal(action);
        if(order == null)
        {
            LOG.error("cannot look-up order for action: {}", action);
        }
        return order;
    }


    protected AbstractOrderModel getOrderInternal(AbstractRuleActionRAO action)
    {
        ServicesUtil.validateParameterNotNull(action, "action rao must not be null");
        AbstractOrderRAO orderRao = null;
        if(action.getAppliedToObject() instanceof OrderEntryRAO)
        {
            OrderEntryRAO entry = (OrderEntryRAO)action.getAppliedToObject();
            orderRao = entry.getOrder();
        }
        else if(action.getAppliedToObject() instanceof AbstractOrderRAO)
        {
            orderRao = (AbstractOrderRAO)action.getAppliedToObject();
        }
        if(orderRao != null)
        {
            return getOrder(orderRao);
        }
        return null;
    }


    protected RuleBasedPromotionModel getPromotion(AbstractRuleActionRAO abstractRao)
    {
        RuleBasedPromotionModel promotionModel = null;
        if(Objects.nonNull(abstractRao) && Objects.nonNull(abstractRao.getFiredRuleCode()))
        {
            AbstractRuleEngineRuleModel rule = getRule(abstractRao);
            if(Objects.nonNull(rule))
            {
                promotionModel = rule.getPromotion();
            }
            else
            {
                LOG.error("Cannot get promotion for AbstractRuleActionRAO: {}. No rule found for code: {}", abstractRao, abstractRao
                                .getFiredRuleCode());
            }
        }
        return promotionModel;
    }


    public AbstractRuleEngineRuleModel getRule(AbstractRuleActionRAO abstractRao)
    {
        AbstractRuleEngineRuleModel ruleModel = null;
        if(Objects.nonNull(abstractRao) && Objects.nonNull(abstractRao.getFiredRuleCode()))
        {
            String firedRuleCode = abstractRao.getFiredRuleCode();
            try
            {
                Optional<Long> deployedModuleVersion = getModuleVersioningService().getDeployedModuleVersionForRule(firedRuleCode, abstractRao
                                .getModuleName());
                ruleModel = deployedModuleVersion.<AbstractRuleEngineRuleModel>map(v -> getEngineRuleDao().getActiveRuleByCodeAndMaxVersion(firedRuleCode, abstractRao.getModuleName(), v.longValue())).orElse(null);
            }
            catch(ModelNotFoundException e)
            {
                LOG.error("cannot get rule from AbstractRuleActionRAO: {}. No rule found for code: {}", abstractRao, firedRuleCode);
            }
        }
        return ruleModel;
    }


    protected AbstractOrderModel getOrder(AbstractOrderRAO orderRao)
    {
        AbstractOrderModel order = null;
        String orderCode = orderRao.getCode();
        try
        {
            order = getExtendedOrderDao().findOrderByCode(orderCode);
        }
        catch(ModelNotFoundException ex)
        {
            LOG.debug(
                            String.format("Cannot look-up AbstractOrder for code '%s', order not found. Trying session cart instead (InMemoryCart support)", new Object[] {orderCode}));
            if(getCartService().hasSessionCart())
            {
                CartModel sessionCart = getCartService().getSessionCart();
                if(sessionCart instanceof de.hybris.platform.servicelayer.internal.model.order.InMemoryCartModel && orderCode.equals(sessionCart.getCode()))
                {
                    return (AbstractOrderModel)sessionCart;
                }
            }
            LOG.error("Cannot find the cart with code {} in the database nor in the session", orderCode);
            return null;
        }
        return order;
    }


    protected ExtendedOrderDao getExtendedOrderDao()
    {
        return this.extendedOrderDao;
    }


    @Required
    public void setExtendedOrderDao(ExtendedOrderDao extendedOrderDao)
    {
        this.extendedOrderDao = extendedOrderDao;
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


    protected CalculationService getCalculationService()
    {
        return this.calculationService;
    }


    @Required
    public void setCalculationService(CalculationService calculationService)
    {
        this.calculationService = calculationService;
    }


    protected ProductDao getProductDao()
    {
        return this.productDao;
    }


    @Required
    public void setProductDao(ProductDao productDao)
    {
        this.productDao = productDao;
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


    protected ModuleVersioningService getModuleVersioningService()
    {
        return this.moduleVersioningService;
    }


    @Required
    public void setModuleVersioningService(ModuleVersioningService moduleVersioningService)
    {
        this.moduleVersioningService = moduleVersioningService;
    }


    protected PromotionResultService getPromotionResultService()
    {
        return this.promotionResultService;
    }


    @Required
    public void setPromotionResultService(PromotionResultService promotionResultService)
    {
        this.promotionResultService = promotionResultService;
    }


    protected CartService getCartService()
    {
        return this.cartService;
    }


    @Required
    public void setCartService(CartService cartService)
    {
        this.cartService = cartService;
    }
}
