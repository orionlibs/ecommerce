package de.hybris.platform.couponservices.action.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.couponservices.model.RuleBasedAddCouponActionModel;
import de.hybris.platform.promotionengineservices.action.impl.AbstractRuleActionStrategy;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.AddCouponRAO;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultAddCouponActionStrategy extends AbstractRuleActionStrategy<RuleBasedAddCouponActionModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAddCouponActionStrategy.class);


    public List<PromotionResultModel> apply(AbstractRuleActionRAO action)
    {
        if(!(action instanceof AddCouponRAO))
        {
            LOG.error("cannot apply {}, action is not of type AddCouponRAO, but {}", getClass().getSimpleName(), action);
            return Collections.emptyList();
        }
        AddCouponRAO addCouponAction = (AddCouponRAO)action;
        if(!(addCouponAction.getAppliedToObject() instanceof de.hybris.platform.ruleengineservices.rao.CartRAO))
        {
            LOG.error("cannot apply {}, appliedToObject is not of type CartRAO, but {}", getClass().getSimpleName(), action
                            .getAppliedToObject());
            return Collections.emptyList();
        }
        PromotionResultModel promoResult = getPromotionActionService().createPromotionResult(action);
        if(promoResult == null)
        {
            LOG.error("cannot apply {}, promotionResult could not be created.", getClass().getSimpleName());
            return Collections.emptyList();
        }
        AbstractOrderModel order = getPromotionResultUtils().getOrder(promoResult);
        if(Objects.isNull(order))
        {
            LOG.error("cannot apply {}, order or cart not found: {}", getClass().getSimpleName(), order);
            if(getModelService().isNew(promoResult))
            {
                getModelService().detach(promoResult);
            }
            return Collections.emptyList();
        }
        if(addCouponAction.getCouponId() == null)
        {
            LOG.error("CouponId can not be null!");
            return Collections.emptyList();
        }
        RuleBasedAddCouponActionModel actionModel = (RuleBasedAddCouponActionModel)createPromotionAction(promoResult, action);
        handleActionMetadata(action, (AbstractRuleBasedPromotionActionModel)actionModel);
        actionModel.setCouponId(addCouponAction.getCouponId());
        actionModel.setCouponCode(addCouponAction.getCouponId());
        LOG.debug("Coupon with code {} as a promotion applied", addCouponAction.getCouponId());
        getModelService().saveAll(new Object[] {promoResult, actionModel, order});
        return Collections.singletonList(promoResult);
    }


    public void undo(ItemModel item)
    {
        if(item instanceof RuleBasedAddCouponActionModel)
        {
            handleUndoActionMetadata((AbstractRuleBasedPromotionActionModel)item);
            RuleBasedAddCouponActionModel action = (RuleBasedAddCouponActionModel)item;
            String couponId = action.getCouponId();
            AbstractOrderModel order = getPromotionResultUtils().getOrder(action.getPromotionResult());
            undoInternal((AbstractRuleBasedPromotionActionModel)action);
            getModelService().save(order);
            LOG.debug("Coupon with code {} as a promotion removed", couponId);
        }
    }
}
