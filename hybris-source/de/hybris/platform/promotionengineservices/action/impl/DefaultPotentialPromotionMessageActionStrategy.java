package de.hybris.platform.promotionengineservices.action.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.promotionengineservices.model.PromotionActionParameterModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPotentialPromotionMessageActionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.DisplayMessageRAO;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultPotentialPromotionMessageActionStrategy extends AbstractRuleActionStrategy<RuleBasedPotentialPromotionMessageActionModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultPotentialPromotionMessageActionStrategy.class);


    public List<PromotionResultModel> apply(AbstractRuleActionRAO action)
    {
        if(!(action instanceof DisplayMessageRAO))
        {
            LOG.error("cannot apply {}, action is not of type DisplayMessageRAO", getClass().getSimpleName());
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
        RuleBasedPotentialPromotionMessageActionModel actionModel = (RuleBasedPotentialPromotionMessageActionModel)createPromotionAction(promoResult, action);
        handleActionMetadata(action, (AbstractRuleBasedPromotionActionModel)actionModel);
        supplementMessageActionModelWithParameters((DisplayMessageRAO)action, actionModel);
        getModelService().saveAll(new Object[] {promoResult, actionModel});
        getPromotionActionService().recalculateFiredPromotionMessage(promoResult);
        return Collections.singletonList(promoResult);
    }


    protected void supplementMessageActionModelWithParameters(DisplayMessageRAO action, RuleBasedPotentialPromotionMessageActionModel actionModel)
    {
        if(MapUtils.isNotEmpty(action.getParameters()))
        {
            actionModel.setParameters((Collection)action.getParameters().entrySet().stream().map(this::convertToActionParameterModel)
                            .collect(Collectors.toList()));
        }
    }


    protected PromotionActionParameterModel convertToActionParameterModel(Map.Entry<String, Object> actionParameterEntry)
    {
        PromotionActionParameterModel actionParameterModel = (PromotionActionParameterModel)getModelService().create(PromotionActionParameterModel.class);
        actionParameterModel.setUuid(actionParameterEntry.getKey());
        actionParameterModel.setValue(actionParameterEntry.getValue());
        return actionParameterModel;
    }


    public void undo(ItemModel item)
    {
        if(item instanceof RuleBasedPotentialPromotionMessageActionModel)
        {
            RuleBasedPotentialPromotionMessageActionModel action = (RuleBasedPotentialPromotionMessageActionModel)item;
            handleUndoActionMetadata((AbstractRuleBasedPromotionActionModel)action);
            removeMessageActionModelParameters(action);
            undoInternal((AbstractRuleBasedPromotionActionModel)action);
        }
    }


    protected void removeMessageActionModelParameters(RuleBasedPotentialPromotionMessageActionModel action)
    {
        if(CollectionUtils.isNotEmpty(action.getParameters()))
        {
            action.getParameters().stream().forEach(param -> getModelService().remove(param));
        }
    }
}
