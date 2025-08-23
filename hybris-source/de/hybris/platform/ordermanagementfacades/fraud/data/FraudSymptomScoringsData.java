package de.hybris.platform.ordermanagementfacades.fraud.data;

import java.io.Serializable;

public class FraudSymptomScoringsData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String explanation;
    private String name;
    private Double score;


    public void setExplanation(String explanation)
    {
        this.explanation = explanation;
    }


    public String getExplanation()
    {
        return this.explanation;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setScore(Double score)
    {
        this.score = score;
    }


    public Double getScore()
    {
        return this.score;
    }
}
