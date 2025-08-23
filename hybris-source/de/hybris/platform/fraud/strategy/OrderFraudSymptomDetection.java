package de.hybris.platform.fraud.strategy;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.fraud.impl.FraudServiceResponse;

public interface OrderFraudSymptomDetection
{
    FraudServiceResponse recognizeSymptom(FraudServiceResponse paramFraudServiceResponse, AbstractOrderModel paramAbstractOrderModel);
}
