package de.hybris.platform.promotionengineservices.promotionengine;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import java.util.List;

public interface PromotionActionService
{
    void recalculateFiredPromotionMessage(PromotionResultModel paramPromotionResultModel);


    PromotionResultModel createPromotionResult(AbstractRuleActionRAO paramAbstractRuleActionRAO);


    void createDiscountValue(DiscountRAO paramDiscountRAO, String paramString, AbstractOrderModel paramAbstractOrderModel);


    List<ItemModel> removeDiscountValue(String paramString, AbstractOrderModel paramAbstractOrderModel);


    void createDiscountValue(DiscountRAO paramDiscountRAO, String paramString, AbstractOrderEntryModel paramAbstractOrderEntryModel);


    AbstractOrderEntryModel getOrderEntry(AbstractRuleActionRAO paramAbstractRuleActionRAO);


    AbstractOrderModel getOrder(AbstractRuleActionRAO paramAbstractRuleActionRAO);


    AbstractRuleEngineRuleModel getRule(AbstractRuleActionRAO paramAbstractRuleActionRAO);


    void recalculateTotals(AbstractOrderModel paramAbstractOrderModel);
}
