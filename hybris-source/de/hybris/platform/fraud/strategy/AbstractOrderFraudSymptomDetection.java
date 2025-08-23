package de.hybris.platform.fraud.strategy;

import de.hybris.platform.fraud.impl.FraudSymptom;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractOrderFraudSymptomDetection implements OrderFraudSymptomDetection
{
    public static final double DEFAULT_INCREMENT = 50.0D;
    private double increment = 50.0D;
    private String symptomName;


    protected FraudSymptom createSymptom(boolean positive)
    {
        return createSymptom(null, positive);
    }


    protected FraudSymptom createSymptom(String explanation, boolean positive)
    {
        return new FraudSymptom(explanation, positive ? getIncrement() : 0.0D, getSymptomName());
    }


    public String getStrategyName()
    {
        return getSymptomName() + " strategy";
    }


    public String getSymptomName()
    {
        return this.symptomName;
    }


    @Required
    public void setSymptomName(String name)
    {
        this.symptomName = name;
    }


    public double getIncrement()
    {
        return this.increment;
    }


    public void setIncrement(double increment)
    {
        this.increment = increment;
    }
}
