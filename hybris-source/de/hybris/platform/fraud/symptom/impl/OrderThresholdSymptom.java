package de.hybris.platform.fraud.symptom.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.fraud.impl.FraudServiceResponse;
import de.hybris.platform.fraud.impl.FraudSymptom;
import de.hybris.platform.fraud.strategy.AbstractOrderFraudSymptomDetection;

public class OrderThresholdSymptom extends AbstractOrderFraudSymptomDetection
{
    private double thresholdLimit = 1000.0D;
    private double thresholdDelta = 100.0D;


    public double getThresholdLimit()
    {
        return this.thresholdLimit;
    }


    public void setThresholdLimit(double thresholdLimit)
    {
        this.thresholdLimit = thresholdLimit;
    }


    public double getThresholdDelta()
    {
        return this.thresholdDelta;
    }


    public void setThresholdDelta(double thresholdDelta)
    {
        this.thresholdDelta = thresholdDelta;
    }


    public FraudServiceResponse recognizeSymptom(FraudServiceResponse fraudResponse, AbstractOrderModel order)
    {
        if(order.getTotalPrice().compareTo(Double.valueOf(getThresholdLimit())) > 0)
        {
            double difference = order.getTotalPrice().doubleValue() - getThresholdLimit();
            fraudResponse.addSymptom(new FraudSymptom(getSymptomName(), getIncrement(difference)));
        }
        else
        {
            fraudResponse.addSymptom(createSymptom(false));
        }
        return fraudResponse;
    }


    public double getIncrement(double orderDelta)
    {
        double stepIncrement = getIncrement();
        return Math.ceil(orderDelta / getThresholdDelta()) * stepIncrement;
    }
}
