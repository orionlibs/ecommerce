package de.hybris.platform.fraud.symptom.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.fraud.impl.FraudServiceResponse;
import de.hybris.platform.fraud.impl.FraudSymptom;
import de.hybris.platform.fraud.strategy.AbstractOrderFraudSymptomDetection;
import java.util.Objects;

public class FirstTimeOrderSymptom extends AbstractOrderFraudSymptomDetection
{
    public FraudServiceResponse recognizeSymptom(FraudServiceResponse fraudResponse, AbstractOrderModel order)
    {
        boolean firstOrder = (Objects.nonNull(order.getUser().getOrders()) && order.getUser().getOrders().size() == 1);
        fraudResponse.addSymptom(new FraudSymptom(getSymptomName(), firstOrder ? getIncrement() : 0.0D));
        return fraudResponse;
    }
}
