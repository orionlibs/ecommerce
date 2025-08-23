package de.hybris.platform.fraud.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.fraud.strategy.OrderFraudSymptomDetection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.NotImplementedException;

public class DefaultHybrisFraudServiceProvider extends AbstractFraudServiceProvider
{
    private List<OrderFraudSymptomDetection> symptomList;


    public List<OrderFraudSymptomDetection> getSymptomList()
    {
        return (this.symptomList == null) ? Collections.<OrderFraudSymptomDetection>emptyList() : this.symptomList;
    }


    public void setSymptomList(List<OrderFraudSymptomDetection> symptomList)
    {
        this.symptomList = symptomList;
    }


    public FraudServiceResponse recognizeOrderFraudSymptoms(AbstractOrderModel order)
    {
        FraudServiceResponse response = new FraudServiceResponse(getProviderName());
        for(OrderFraudSymptomDetection strategy : getSymptomList())
        {
            response = strategy.recognizeSymptom(response, order);
        }
        return response;
    }


    public FraudServiceResponse recognizeUserActivitySymptoms(UserModel order)
    {
        throw new NotImplementedException(getClass());
    }
}
