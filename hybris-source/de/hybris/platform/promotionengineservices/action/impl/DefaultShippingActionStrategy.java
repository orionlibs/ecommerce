package de.hybris.platform.promotionengineservices.action.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.order.daos.DeliveryModeDao;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedOrderChangeDeliveryModeActionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.ShipmentRAO;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultShippingActionStrategy extends AbstractRuleActionStrategy<RuleBasedOrderChangeDeliveryModeActionModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultShippingActionStrategy.class);
    private DeliveryModeDao deliveryModeDao;


    public List<PromotionResultModel> apply(AbstractRuleActionRAO action)
    {
        if(!(action instanceof ShipmentRAO))
        {
            LOG.error("cannot apply {}, action is not of type ShipmentRAO, but {}", getClass().getSimpleName(), action);
            return Collections.emptyList();
        }
        ShipmentRAO changeDeliveryMethodAction = (ShipmentRAO)action;
        if(!(changeDeliveryMethodAction.getAppliedToObject() instanceof de.hybris.platform.ruleengineservices.rao.CartRAO))
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
        ShipmentRAO shipmentRAO = (ShipmentRAO)action;
        DeliveryModeModel shipmentModel = getDeliveryModeForCode(shipmentRAO.getMode().getCode());
        if(shipmentModel == null)
        {
            LOG.error("Delivery Mode for code {} not found!", shipmentRAO.getMode());
            return Collections.emptyList();
        }
        DeliveryModeModel shipmentModelToReplace = order.getDeliveryMode();
        order.setDeliveryMode(shipmentModel);
        Double deliveryCostToReplace = order.getDeliveryCost();
        order.setDeliveryCost(Double.valueOf(shipmentRAO.getMode().getCost().doubleValue()));
        RuleBasedOrderChangeDeliveryModeActionModel actionModel = (RuleBasedOrderChangeDeliveryModeActionModel)createPromotionAction(promoResult, action);
        handleActionMetadata(action, (AbstractRuleBasedPromotionActionModel)actionModel);
        actionModel.setDeliveryMode(shipmentModel);
        actionModel.setDeliveryCost(shipmentRAO.getMode().getCost());
        actionModel.setReplacedDeliveryMode(shipmentModelToReplace);
        actionModel.setReplacedDeliveryCost(BigDecimal.valueOf(deliveryCostToReplace.doubleValue()));
        getModelService().saveAll(new Object[] {promoResult, actionModel, order});
        return Collections.singletonList(promoResult);
    }


    public void undo(ItemModel item)
    {
        if(item instanceof RuleBasedOrderChangeDeliveryModeActionModel)
        {
            handleUndoActionMetadata((AbstractRuleBasedPromotionActionModel)item);
            RuleBasedOrderChangeDeliveryModeActionModel action = (RuleBasedOrderChangeDeliveryModeActionModel)item;
            AbstractOrderModel order = getPromotionResultUtils().getOrder(action.getPromotionResult());
            order.setDeliveryMode(action.getReplacedDeliveryMode());
            order.setDeliveryCost(Double.valueOf(action.getReplacedDeliveryCost().doubleValue()));
            undoInternal((AbstractRuleBasedPromotionActionModel)action);
            getModelService().save(order);
        }
    }


    protected DeliveryModeModel getDeliveryModeForCode(String code)
    {
        ServicesUtil.validateParameterNotNull(code, "Parameter code cannot be null");
        List<DeliveryModeModel> deliveryModes = getDeliveryModeDao().findDeliveryModesByCode(code);
        return CollectionUtils.isNotEmpty(deliveryModes) ? deliveryModes.get(0) : null;
    }


    protected DeliveryModeDao getDeliveryModeDao()
    {
        return this.deliveryModeDao;
    }


    @Required
    public void setDeliveryModeDao(DeliveryModeDao deliveryModeDao)
    {
        this.deliveryModeDao = deliveryModeDao;
    }
}
