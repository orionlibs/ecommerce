package de.hybris.platform.ruleengineservices.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.ruleengineservices.rao.PaymentModeRAO;

public class PaymentModeRaoPopulator implements Populator<PaymentModeModel, PaymentModeRAO>
{
    public void populate(PaymentModeModel source, PaymentModeRAO target)
    {
        target.setCode(source.getCode());
    }
}
