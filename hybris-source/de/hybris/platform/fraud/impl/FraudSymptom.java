package de.hybris.platform.fraud.impl;

public class FraudSymptom
{
    private final String explanation;
    private final double score;
    private final String symptom;


    public FraudSymptom(String symptom, double score)
    {
        this(null, score, symptom);
    }


    public FraudSymptom(String explanation, double score, String symptom)
    {
        this.explanation = explanation;
        this.score = score;
        this.symptom = symptom;
    }


    public String getExplanation()
    {
        return this.explanation;
    }


    public String getSymptom()
    {
        return this.symptom;
    }


    public double getScore()
    {
        return this.score;
    }
}
