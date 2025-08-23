package de.hybris.platform.ruleengineservices.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.ruleengineservices.rao.DeliveryModeRAO;

public class DeliveryModeRaoPopulator implements Populator<DeliveryModeModel, DeliveryModeRAO>
{
    public void populate(DeliveryModeModel source, DeliveryModeRAO target)
    {
        target.setCode(source.getCode());
    }
}
