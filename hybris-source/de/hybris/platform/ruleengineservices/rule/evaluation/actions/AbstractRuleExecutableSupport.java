package de.hybris.platform.ruleengineservices.rule.evaluation.actions;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengineservices.calculation.PriceAdjustmentStrategy;
import de.hybris.platform.ruleengineservices.calculation.RuleEngineCalculationService;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.EntriesSelectionStrategyRPD;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.impl.RuleAndRuleGroupExecutionTracker;
import de.hybris.platform.ruleengineservices.util.CurrencyUtils;
import de.hybris.platform.ruleengineservices.util.RaoUtils;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.drools.core.spi.KnowledgeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;

public class AbstractRuleExecutableSupport implements BeanNameAware, RAOAction
{
    private RaoUtils raoUtils;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRuleExecutableSupport.class);
    private ConfigurationService configurationService;
    private RuleEngineCalculationService ruleEngineCalculationService;
    private CurrencyUtils currencyUtils;
    private List<ActionSupplementStrategy> actionSupplementStrategies;
    private RAOConsumptionSupport consumptionSupport;
    private RAOLookupService raoLookupService;
    private PriceAdjustmentStrategy<OrderEntryRAO> priceAdjustmentStrategy;
    private String beanName;


    public void performAction(RuleActionContext context)
    {
        validateParameters(context.getParameters());
        validateRule(context);
        if(performActionInternal(context))
        {
            getConsumptionSupport().trackConsumedProducts(context);
            trackActionExecution(context);
            context.updateScheduledFacts();
        }
    }


    protected void validateParameters(Map<String, Object> parameters)
    {
        Preconditions.checkArgument(MapUtils.isNotEmpty(parameters), "Properties passed as a method argument must not be empty");
    }


    protected boolean performActionInternal(RuleActionContext context)
    {
        return true;
    }


    protected void postProcessAction(AbstractRuleActionRAO actionRao, RuleActionContext context)
    {
        getActionSupplementStrategies().stream().filter(strategy -> strategy.isActionProperToHandle(actionRao, context))
                        .forEach(strategy -> strategy.postProcessAction(actionRao, context));
    }


    protected boolean shouldPerformAction(AbstractRuleActionRAO actionRao, RuleActionContext context)
    {
        return getActionSupplementStrategies().stream().filter(strategy -> strategy.isActionProperToHandle(actionRao, context))
                        .anyMatch(strategy -> strategy.shouldPerformAction(actionRao, context));
    }


    protected void trackActionExecution(RuleActionContext context)
    {
        String ruleCode = getRuleCode(context);
        RuleAndRuleGroupExecutionTracker tracker = getRuntimeTracker(context);
        tracker.trackActionExecutionStarted(ruleCode);
    }


    protected KnowledgeHelper checkAndGetRuleContext(RuleActionContext context)
    {
        Object delegate = context.getDelegate();
        Preconditions.checkState(delegate instanceof KnowledgeHelper, "context must be of type org.kie.api.runtime.rule.RuleContext.");
        return (KnowledgeHelper)delegate;
    }


    protected void validateRule(RuleActionContext context)
    {
        boolean validateRuleCode = getConfigurationService().getConfiguration().getBoolean("droolsruleengineservices.validate.droolsrule.rulecode", true);
        if(!validateRuleCode)
        {
            LOG.debug("ignoring validation of rule code. Set 'droolsruleengineservices.validate.droolsrule.rulecode' to true to re-enable validation.");
            return;
        }
        Map<String, Object> ruleMetaData = context.getRuleMetadata();
        Preconditions.checkState((ruleMetaData.get("ruleCode") != null), "rule %s is missing metadata key %s for the rule code.", context
                        .getRuleName(), "ruleCode");
        boolean validateRulesModuleName = getConfigurationService().getConfiguration().getBoolean("droolsruleengineservices.validate.droolsrule.moduleName", true);
        if(!validateRulesModuleName)
        {
            LOG.debug("ignoring validation of rules module name. Set 'droolsruleengineservices.validate.droolsrule.modulename' to true to re-enable validation.");
            return;
        }
        Preconditions.checkState((ruleMetaData.get("moduleName") != null), "rule %s is missing metadata key %s for the module name.", context
                        .getRulesModuleName(), "moduleName");
    }


    protected String getRuleCode(RuleActionContext context)
    {
        return getMetaDataFromRule(context, "ruleCode");
    }


    protected String getRuleGroupCode(RuleActionContext context)
    {
        return getMetaDataFromRule(context, "ruleGroupCode");
    }


    protected boolean isRuleGroupExclusive(RuleActionContext context)
    {
        String exclusive = getMetaDataFromRule(context, "ruleGroupExclusive");
        return Boolean.parseBoolean(exclusive);
    }


    protected String getMetaDataFromRule(RuleActionContext context, String key)
    {
        Object value = context.getRuleMetadata().get(key);
        return Objects.isNull(value) ? null : value.toString();
    }


    protected Map<String, String> getMetaDataFromRule(RuleActionContext context)
    {
        return (Map<String, String>)context.getRuleMetadata().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> String.valueOf(e.getValue())));
    }


    public void setRAOMetaData(RuleActionContext context, AbstractRuleActionRAO... raos)
    {
        if(Objects.nonNull(raos))
        {
            Stream.<AbstractRuleActionRAO>of(raos).filter(Objects::nonNull).forEach(r -> addMetadataToRao(r, context));
        }
    }


    protected void addMetadataToRao(AbstractRuleActionRAO rao, RuleActionContext context)
    {
        rao.setFiredRuleCode(getMetaDataFromRule(context, "ruleCode"));
        rao.setModuleName(getMetaDataFromRule(context, "moduleName"));
        rao.setActionStrategyKey(getBeanName());
        rao.setMetadata(getMetaDataFromRule(context));
    }


    protected RuleAndRuleGroupExecutionTracker getRuntimeTracker(RuleActionContext context)
    {
        return getRaoLookupService().lookupRAOByType(RuleAndRuleGroupExecutionTracker.class, context, new java.util.function.Predicate[0]).orElse(null);
    }


    protected void validateSelectionStrategy(Collection<EntriesSelectionStrategyRPD> strategies, RuleActionContext context)
    {
        KnowledgeHelper helper = checkAndGetRuleContext(context);
        String ruleName = helper.getRule().getName();
        Preconditions.checkState(CollectionUtils.isNotEmpty(strategies), "rule %s has empty list of entriesSelectionStrategyRPDs.", ruleName);
        AbstractOrderRAO orderRao = null;
        for(EntriesSelectionStrategyRPD strategy : strategies)
        {
            Preconditions.checkState(CollectionUtils.isNotEmpty(strategy.getOrderEntries()), "rule %s has empty order entry list in entriesSelectionStrategyRPDs.", ruleName);
            if(orderRao == null)
            {
                orderRao = ((OrderEntryRAO)strategy.getOrderEntries().get(0)).getOrder();
            }
            for(OrderEntryRAO orderEntryRao : strategy.getOrderEntries())
            {
                Preconditions.checkState((orderEntryRao.getOrder() != null && orderEntryRao.getOrder().equals(orderRao)), "rule %s has inconsistent OrderRao in different OrderEntryRao-s of entriesSelectionStrategyRPDs.", ruleName);
            }
        }
    }


    protected void splitEntriesSelectionStrategies(List<EntriesSelectionStrategyRPD> entriesSelectionStrategyRPDs, List<EntriesSelectionStrategyRPD> selectionStrategyRPDsForAction, List<EntriesSelectionStrategyRPD> selectionStrategyRPDsForTriggering)
    {
        Objects.requireNonNull(selectionStrategyRPDsForAction);
        entriesSelectionStrategyRPDs.stream().filter(EntriesSelectionStrategyRPD::isTargetOfAction).forEach(selectionStrategyRPDsForAction::add);
        Objects.requireNonNull(selectionStrategyRPDsForTriggering);
        entriesSelectionStrategyRPDs.stream().filter(s -> !s.isTargetOfAction()).forEach(selectionStrategyRPDsForTriggering::add);
    }


    protected boolean mergeDiscounts(RuleActionContext context, DiscountRAO discountRao, OrderEntryRAO entry)
    {
        Objects.requireNonNull(DiscountRAO.class);
        Optional<AbstractRuleActionRAO> actionOptional = entry.getActions().stream().filter(DiscountRAO.class::isInstance).filter(a -> Objects.nonNull(a.getFiredRuleCode())).filter(a -> a.getFiredRuleCode().equals(context.getRuleName())).findFirst();
        if(actionOptional.isPresent())
        {
            DiscountRAO originalDiscount = (DiscountRAO)actionOptional.get();
            originalDiscount.setAppliedToQuantity(originalDiscount.getAppliedToQuantity() + discountRao.getAppliedToQuantity());
        }
        return actionOptional.isPresent();
    }


    protected void validateCurrencyIsoCode(boolean absolute, String currencyIsoCode)
    {
        if(absolute)
        {
            ServicesUtil.validateParameterNotNull(currencyIsoCode, "currencyIsoCode must not be bull");
        }
        else if(!StringUtils.isEmpty(currencyIsoCode))
        {
            LOG.error("currencyIsoCode '{}' will be ignored as absolute is set to false.", currencyIsoCode);
        }
    }


    protected Optional<BigDecimal> extractAmountForCurrency(RuleActionContext context, Object currencyAmount)
    {
        Preconditions.checkArgument(Objects.nonNull(currencyAmount), "The currency-amount map must not be empty: specify at least one CURRENCY->AMOUNT entry.");
        Optional<BigDecimal> amountForCurrency = Optional.empty();
        if(currencyAmount instanceof BigDecimal)
        {
            amountForCurrency = Optional.ofNullable((BigDecimal)currencyAmount);
        }
        else if(currencyAmount instanceof Map)
        {
            Map<String, BigDecimal> currencyAmountMap = (Map<String, BigDecimal>)currencyAmount;
            CartRAO cartRao = context.getCartRao();
            amountForCurrency = Objects.nonNull(cartRao) ? Optional.<BigDecimal>ofNullable(currencyAmountMap.get(cartRao.getCurrencyIsoCode())) : Optional.<BigDecimal>of((BigDecimal)((Map.Entry)currencyAmountMap.entrySet().iterator().next()).getValue());
        }
        return amountForCurrency;
    }


    public String getBeanName()
    {
        return this.beanName;
    }


    public void setBeanName(String beanName)
    {
        this.beanName = beanName;
    }


    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    public void setRuleEngineCalculationService(RuleEngineCalculationService ruleEngineCalculationService)
    {
        this.ruleEngineCalculationService = ruleEngineCalculationService;
    }


    protected RuleEngineCalculationService getRuleEngineCalculationService()
    {
        return this.ruleEngineCalculationService;
    }


    protected CurrencyUtils getCurrencyUtils()
    {
        return this.currencyUtils;
    }


    public void setCurrencyUtils(CurrencyUtils currencyUtils)
    {
        this.currencyUtils = currencyUtils;
    }


    protected List<ActionSupplementStrategy> getActionSupplementStrategies()
    {
        return this.actionSupplementStrategies;
    }


    public void setActionSupplementStrategies(List<ActionSupplementStrategy> actionSupplementStrategies)
    {
        this.actionSupplementStrategies = actionSupplementStrategies;
    }


    protected RaoUtils getRaoUtils()
    {
        return this.raoUtils;
    }


    public void setRaoUtils(RaoUtils raoUtils)
    {
        this.raoUtils = raoUtils;
    }


    protected RAOConsumptionSupport getConsumptionSupport()
    {
        return this.consumptionSupport;
    }


    public void setConsumptionSupport(RAOConsumptionSupport consumptionSupport)
    {
        this.consumptionSupport = consumptionSupport;
    }


    protected RAOLookupService getRaoLookupService()
    {
        return this.raoLookupService;
    }


    public void setRaoLookupService(RAOLookupService raoLookupService)
    {
        this.raoLookupService = raoLookupService;
    }


    protected PriceAdjustmentStrategy<OrderEntryRAO> getPriceAdjustmentStrategy()
    {
        return this.priceAdjustmentStrategy;
    }


    public void setPriceAdjustmentStrategy(PriceAdjustmentStrategy<OrderEntryRAO> priceAdjustmentStrategy)
    {
        this.priceAdjustmentStrategy = priceAdjustmentStrategy;
    }
}
