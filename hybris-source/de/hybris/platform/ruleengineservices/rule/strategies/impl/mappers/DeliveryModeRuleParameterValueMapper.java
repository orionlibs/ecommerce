package de.hybris.platform.ruleengineservices.rule.strategies.impl.mappers;

import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.order.DeliveryModeService;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.springframework.beans.factory.annotation.Required;

public class DeliveryModeRuleParameterValueMapper implements RuleParameterValueMapper<DeliveryModeModel>
{
    private DeliveryModeService deliveryModeService;


    public String toString(DeliveryModeModel deliveryMode)
    {
        ServicesUtil.validateParameterNotNull(deliveryMode, "Object cannot be null");
        return deliveryMode.getCode();
    }


    public DeliveryModeModel fromString(String deliveryModeCode)
    {
        ServicesUtil.validateParameterNotNull(deliveryModeCode, "String deliveryModeCode cannot be null");
        DeliveryModeModel deliveryMode = this.deliveryModeService.getDeliveryModeForCode(deliveryModeCode);
        if(deliveryMode == null)
        {
            throw new RuleParameterValueMapperException("Cannot find delivery mode with code: " + deliveryModeCode);
        }
        return deliveryMode;
    }


    public DeliveryModeService getDeliveryModeService()
    {
        return this.deliveryModeService;
    }


    @Required
    public void setDeliveryModeService(DeliveryModeService deliveryModeService)
    {
        this.deliveryModeService = deliveryModeService;
    }
}
