package de.hybris.platform.fraud.symptom.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.fraud.impl.FraudServiceResponse;
import de.hybris.platform.fraud.impl.FraudSymptom;
import de.hybris.platform.fraud.model.ProductOrderLimitModel;
import de.hybris.platform.fraud.strategy.AbstractOrderFraudSymptomDetection;

public class OrderEntriesSymptom extends AbstractOrderFraudSymptomDetection
{
    public FraudServiceResponse recognizeSymptom(FraudServiceResponse fraudResponse, AbstractOrderModel order)
    {
        for(AbstractOrderEntryModel orderEntry : order.getEntries())
        {
            ProductOrderLimitModel limits = orderEntry.getProduct().getProductOrderLimit();
            if(null != limits && null != limits.getMaxNumberPerOrder() && orderEntry
                            .getQuantity().longValue() > limits.getMaxNumberPerOrder().longValue())
            {
                fraudResponse
                                .addSymptom(new FraudSymptom(getSymptomName(), getIncrement(), "Product :" + orderEntry.getProduct().getCode() + " quantity:" + orderEntry
                                                .getQuantity() + " allowed quantity:" + limits.getMaxNumberPerOrder()));
            }
        }
        return fraudResponse;
    }
}
