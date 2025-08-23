package de.hybris.platform.fraud.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FraudServiceResponse
{
    private final String providerName;
    private final String description;
    private final String externalDescription;
    private List<FraudSymptom> symptoms;


    public FraudServiceResponse(String providerName)
    {
        this(null, providerName);
    }


    public FraudServiceResponse(String description, String providerName)
    {
        this(description, providerName, null, null);
    }


    public FraudServiceResponse(String description, String providerName, String externalDescription, Collection<FraudSymptom> symptoms)
    {
        this.description = description;
        this.providerName = providerName;
        this.externalDescription = externalDescription;
        this.symptoms = (symptoms == null) ? null : new ArrayList<>(symptoms);
    }


    public double getScore()
    {
        double score = 0.0D;
        for(FraudSymptom symptom : getSymptoms())
        {
            score += symptom.getScore();
        }
        return score;
    }


    public double getScore(String symptomName)
    {
        double score = 0.0D;
        for(FraudSymptom symptom : getSymptoms())
        {
            if(symptomName.equalsIgnoreCase(symptomName))
            {
                score += symptom.getScore();
            }
        }
        return score;
    }


    public String getDescription()
    {
        return this.description;
    }


    public String getProviderName()
    {
        return this.providerName;
    }


    public String getExternalDescription()
    {
        return this.externalDescription;
    }


    public void addSymptom(FraudSymptom symptom)
    {
        if(null == this.symptoms)
        {
            this.symptoms = new ArrayList<>();
        }
        this.symptoms.add(symptom);
    }


    public List<FraudSymptom> getSymptoms()
    {
        return (this.symptoms == null) ? Collections.<FraudSymptom>emptyList() : this.symptoms;
    }
}
