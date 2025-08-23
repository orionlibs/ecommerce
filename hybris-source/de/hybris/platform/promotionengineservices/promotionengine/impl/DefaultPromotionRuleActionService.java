package de.hybris.platform.promotionengineservices.promotionengine.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.promotionengine.PromotionActionService;
import de.hybris.platform.promotionengineservices.util.PromotionResultUtils;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengineservices.action.impl.DefaultRuleActionService;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPromotionRuleActionService extends DefaultRuleActionService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultPromotionRuleActionService.class);
    private PromotionActionService promotionActionService;
    private PromotionResultUtils promotionResultUtils;


    public List<ItemModel> applyAllActions(RuleEngineResultRAO ruleEngineResultRAO)
    {
        List<ItemModel> actionResults = super.applyAllActions(ruleEngineResultRAO);
        recalculateTotals(actionResults);
        return actionResults;
    }


    protected void recalculateTotals(List<ItemModel> actionResults)
    {
        if(CollectionUtils.isNotEmpty(actionResults))
        {
            ItemModel item = actionResults.get(0);
            if(!(item instanceof PromotionResultModel))
            {
                LOG.error("Can not recalculate totals. Action result is not PromotionResultModel {}", item);
                return;
            }
            PromotionResultModel promotionResult = (PromotionResultModel)item;
            AbstractOrderModel order = getPromotionResultUtils().getOrder(promotionResult);
            if(order == null)
            {
                LOG.error("Can not recalculate totals. No order found for PromotionResult: {}", promotionResult);
                return;
            }
            getPromotionActionService().recalculateTotals(order);
        }
    }


    protected PromotionActionService getPromotionActionService()
    {
        return this.promotionActionService;
    }


    @Required
    public void setPromotionActionService(PromotionActionService promotionActionService)
    {
        this.promotionActionService = promotionActionService;
    }


    protected PromotionResultUtils getPromotionResultUtils()
    {
        return this.promotionResultUtils;
    }


    @Required
    public void setPromotionResultUtils(PromotionResultUtils promotionResultUtils)
    {
        this.promotionResultUtils = promotionResultUtils;
    }
}
