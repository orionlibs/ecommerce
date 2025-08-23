package de.hybris.platform.ruleengineservices.rule.evaluation.actions.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.ruleengineservices.enums.OrderEntrySelectionStrategy;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.EntriesSelectionStrategyRPD;
import de.hybris.platform.ruleengineservices.rao.FreeProductRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.AbstractRulePartnerProductAction;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class RuleFreeGiftRAOAction extends AbstractRulePartnerProductAction
{
    public static final String QUALIFYING_CONTAINERS_PARAM = "qualifying_containers";
    private static final Logger LOG = LoggerFactory.getLogger(RuleFreeGiftRAOAction.class);
    private ProductService productService;
    private RuleOrderEntryPercentageDiscountRAOAction ruleOrderEntryPercentageDiscountRAOAction;


    public boolean performActionInternal(RuleActionContext context)
    {
        String productCode = (String)context.getParameter("product", String.class);
        Integer quantity = (Integer)context.getParameter("quantity", Integer.class);
        Map<String, Integer> qualifyingProductsContainers = (Map<String, Integer>)context.getParameter("qualifying_containers");
        return performAction(context, qualifyingProductsContainers, productCode, quantity);
    }


    protected boolean performAction(RuleActionContext context, Map<String, Integer> qualifyingProductsContainers, String productCode, Integer quantity)
    {
        int count = quantity.intValue();
        List<EntriesSelectionStrategyRPD> triggeringSelectionStrategyRPDs = createSelectionStrategyRPDsQualifyingProducts(context, OrderEntrySelectionStrategy.CHEAPEST, qualifyingProductsContainers);
        boolean isQualifyingContainersAvailable = MapUtils.isNotEmpty(qualifyingProductsContainers);
        if(isQualifyingContainersAvailable)
        {
            validateSelectionStrategy(triggeringSelectionStrategyRPDs, context);
            if(!getConsumptionSupport().hasEnoughQuantity(context, triggeringSelectionStrategyRPDs))
            {
                return false;
            }
            if(!getConsumptionSupport().isConsumptionEnabled())
            {
                count = getConsumptionSupport().adjustStrategyQuantity(triggeringSelectionStrategyRPDs, context);
            }
        }
        CartRAO cartRao = context.getCartRao();
        ProductModel product = findProduct(productCode, context);
        if(Objects.isNull(product))
        {
            return false;
        }
        FreeProductRAO freeProductRAO = getRuleEngineCalculationService().addFreeProductsToCart(cartRao, product, count);
        if(getConsumptionSupport().getConsumableQuantity(freeProductRAO.getAddedOrderEntry()) >= count)
        {
            setRAOMetaData(context, new AbstractRuleActionRAO[] {(AbstractRuleActionRAO)freeProductRAO});
            RuleEngineResultRAO result = context.getRuleEngineResultRao();
            result.getActions().add(freeProductRAO);
            context.scheduleForUpdate(new Object[] {cartRao, result});
            context.insertFacts(new Object[] {freeProductRAO, freeProductRAO.getAddedOrderEntry()});
            boolean isPerformed = getRuleOrderEntryPercentageDiscountRAOAction().processOrderEntry(context, freeProductRAO
                            .getAddedOrderEntry(), BigDecimal.valueOf(100.0D));
            if(isPerformed && getConsumptionSupport().isConsumptionEnabled() && isQualifyingContainersAvailable)
            {
                Objects.requireNonNull(DiscountRAO.class);
                AbstractRuleActionRAO discount = result.getActions().stream().filter(DiscountRAO.class::isInstance).filter(d -> getMetaDataFromRule(context, "ruleCode").equals(d.getFiredRuleCode())).findFirst().orElse(null);
                getConsumptionSupport().consumeOrderEntries(context, triggeringSelectionStrategyRPDs, discount);
                updateFactsWithOrderEntries(context, triggeringSelectionStrategyRPDs);
            }
            return isPerformed;
        }
        return false;
    }


    protected void updateFactsWithOrderEntries(RuleActionContext context, List<EntriesSelectionStrategyRPD> entriesSelectionStrategyRPDS)
    {
        for(EntriesSelectionStrategyRPD selectionStrategyRPDForTriggering : entriesSelectionStrategyRPDS)
        {
            for(OrderEntryRAO orderEntryRao : selectionStrategyRPDForTriggering.getOrderEntries())
            {
                context.scheduleForUpdate(new Object[] {orderEntryRao});
            }
        }
    }


    protected ProductModel findProduct(String productCode, RuleActionContext context)
    {
        ProductModel product = null;
        try
        {
            product = getProductService().getProductForCode(productCode);
        }
        catch(Exception e)
        {
            LOG.error("no product found for code{} in rule {}, cannot apply rule action.", new Object[] {productCode, getRuleCode(context), e});
        }
        return product;
    }


    protected ProductService getProductService()
    {
        return this.productService;
    }


    @Required
    public void setProductService(ProductService productService)
    {
        this.productService = productService;
    }


    protected RuleOrderEntryPercentageDiscountRAOAction getRuleOrderEntryPercentageDiscountRAOAction()
    {
        return this.ruleOrderEntryPercentageDiscountRAOAction;
    }


    @Required
    public void setRuleOrderEntryPercentageDiscountRAOAction(RuleOrderEntryPercentageDiscountRAOAction ruleOrderEntryPercentageDiscountRAOAction)
    {
        this.ruleOrderEntryPercentageDiscountRAOAction = ruleOrderEntryPercentageDiscountRAOAction;
    }
}
