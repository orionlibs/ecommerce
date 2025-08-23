package de.hybris.platform.promotionengineservices.action.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedOrderEntryAdjustActionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultOrderEntryAdjustActionStrategy extends AbstractRuleActionStrategy<RuleBasedOrderEntryAdjustActionModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultOrderEntryAdjustActionStrategy.class);


    public List<PromotionResultModel> apply(AbstractRuleActionRAO action)
    {
        if(!(action instanceof DiscountRAO))
        {
            LOG.error("cannot apply {}, action is not of type DiscountRAO", getClass().getSimpleName());
            return Collections.emptyList();
        }
        AbstractOrderEntryModel entry = getPromotionActionService().getOrderEntry(action);
        if(entry == null)
        {
            LOG.error("cannot apply {}, orderEntry could not be found.", getClass().getSimpleName());
            return Collections.emptyList();
        }
        PromotionResultModel promoResult = getPromotionActionService().createPromotionResult(action);
        if(promoResult == null)
        {
            LOG.error("cannot apply {}, promotionResult could not be created.", getClass().getSimpleName());
            return Collections.emptyList();
        }
        AbstractOrderModel order = entry.getOrder();
        if(order == null)
        {
            LOG.error("cannot apply {}, order does not exist for order entry", getClass().getSimpleName());
            if(getModelService().isNew(promoResult))
            {
                getModelService().detach(promoResult);
            }
            return Collections.emptyList();
        }
        DiscountRAO discountRao = (DiscountRAO)action;
        BigDecimal discountAmount = discountRao.getValue();
        adjustDiscountRaoValue(entry, discountRao, discountAmount);
        RuleBasedOrderEntryAdjustActionModel actionModel = createOrderEntryAdjustAction(promoResult, action, entry, discountAmount);
        handleActionMetadata(action, (AbstractRuleBasedPromotionActionModel)actionModel);
        getPromotionActionService().createDiscountValue(discountRao, actionModel.getGuid(), entry);
        getModelService().saveAll(new Object[] {promoResult, actionModel, order, entry});
        recalculateIfNeeded(order);
        return Collections.singletonList(promoResult);
    }


    protected void adjustDiscountRaoValue(AbstractOrderEntryModel entry, DiscountRAO discountRao, BigDecimal discountAmount)
    {
        BigDecimal amount = discountAmount;
        if(discountRao.isPerUnit())
        {
            long appliedToQuantity = discountRao.getAppliedToQuantity();
            BigDecimal fraction = BigDecimal.valueOf(appliedToQuantity / entry.getQuantity().longValue());
            amount = amount.multiply(fraction);
        }
        discountRao.setValue(amount);
    }


    protected RuleBasedOrderEntryAdjustActionModel createOrderEntryAdjustAction(PromotionResultModel promoResult, AbstractRuleActionRAO action, AbstractOrderEntryModel entry, BigDecimal discountAmount)
    {
        RuleBasedOrderEntryAdjustActionModel actionModel = (RuleBasedOrderEntryAdjustActionModel)createPromotionAction(promoResult, action);
        actionModel.setAmount(discountAmount);
        actionModel.setOrderEntryNumber(entry.getEntryNumber());
        actionModel.setOrderEntryProduct(entry.getProduct());
        actionModel.setOrderEntryQuantity(Long.valueOf(getConsumedQuantity(promoResult)));
        return actionModel;
    }


    public void undo(ItemModel action)
    {
        if(action instanceof RuleBasedOrderEntryAdjustActionModel)
        {
            handleUndoActionMetadata((AbstractRuleBasedPromotionActionModel)action);
            AbstractOrderModel order = undoInternal((AbstractRuleBasedPromotionActionModel)action);
            recalculateIfNeeded(order);
        }
    }


    protected long getConsumedQuantity(PromotionResultModel promoResult)
    {
        long consumedQuantity = 0L;
        if(CollectionUtils.isNotEmpty(promoResult.getConsumedEntries()))
        {
            consumedQuantity = promoResult.getConsumedEntries().stream().mapToLong(consumedEntry -> consumedEntry.getQuantity().longValue()).sum();
        }
        return consumedQuantity;
    }
}
