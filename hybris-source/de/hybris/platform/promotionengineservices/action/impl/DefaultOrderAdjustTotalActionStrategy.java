package de.hybris.platform.promotionengineservices.action.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedOrderAdjustTotalActionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultOrderAdjustTotalActionStrategy extends AbstractRuleActionStrategy<RuleBasedOrderAdjustTotalActionModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultOrderAdjustTotalActionStrategy.class);


    public List<PromotionResultModel> apply(AbstractRuleActionRAO action)
    {
        if(!(action instanceof DiscountRAO))
        {
            LOG.error("cannot apply {}, action is not of type DiscountRAO", getClass().getSimpleName());
            return Collections.emptyList();
        }
        PromotionResultModel promoResult = getPromotionActionService().createPromotionResult(action);
        if(promoResult == null)
        {
            LOG.error("cannot apply {}, promotionResult could not be created.", getClass().getSimpleName());
            return Collections.emptyList();
        }
        AbstractOrderModel order = getPromotionResultUtils().getOrder(promoResult);
        if(order == null)
        {
            LOG.error("cannot apply {}, order not found", getClass().getSimpleName());
            if(getModelService().isNew(promoResult))
            {
                getModelService().detach(promoResult);
            }
            return Collections.emptyList();
        }
        DiscountRAO discountRao = (DiscountRAO)action;
        RuleBasedOrderAdjustTotalActionModel actionModel = createOrderAdjustTotalAction(promoResult, discountRao);
        handleActionMetadata(action, (AbstractRuleBasedPromotionActionModel)actionModel);
        getPromotionActionService().createDiscountValue(discountRao, actionModel.getGuid(), order);
        getModelService().saveAll(new Object[] {promoResult, actionModel, order});
        recalculateIfNeeded(order);
        return Collections.singletonList(promoResult);
    }


    protected RuleBasedOrderAdjustTotalActionModel createOrderAdjustTotalAction(PromotionResultModel promoResult, DiscountRAO discountRao)
    {
        RuleBasedOrderAdjustTotalActionModel actionModel = (RuleBasedOrderAdjustTotalActionModel)createPromotionAction(promoResult, (AbstractRuleActionRAO)discountRao);
        actionModel.setAmount(discountRao.getValue());
        return actionModel;
    }


    public void undo(ItemModel action)
    {
        if(action instanceof RuleBasedOrderAdjustTotalActionModel)
        {
            handleUndoActionMetadata((AbstractRuleBasedPromotionActionModel)action);
            AbstractOrderModel order = undoInternal((AbstractRuleBasedPromotionActionModel)action);
            recalculateIfNeeded(order);
        }
    }
}
