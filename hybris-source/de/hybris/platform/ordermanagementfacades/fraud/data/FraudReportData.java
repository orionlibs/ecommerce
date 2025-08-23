package de.hybris.platform.ordermanagementfacades.fraud.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class FraudReportData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String explanation;
    private List<FraudSymptomScoringsData> fraudSymptomScorings;
    private String provider;
    private String status;
    private Date timestamp;


    public void setExplanation(String explanation)
    {
        this.explanation = explanation;
    }


    public String getExplanation()
    {
        return this.explanation;
    }


    public void setFraudSymptomScorings(List<FraudSymptomScoringsData> fraudSymptomScorings)
    {
        this.fraudSymptomScorings = fraudSymptomScorings;
    }


    public List<FraudSymptomScoringsData> getFraudSymptomScorings()
    {
        return this.fraudSymptomScorings;
    }


    public void setProvider(String provider)
    {
        this.provider = provider;
    }


    public String getProvider()
    {
        return this.provider;
    }


    public void setStatus(String status)
    {
        this.status = status;
    }


    public String getStatus()
    {
        return this.status;
    }


    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }


    public Date getTimestamp()
    {
        return this.timestamp;
    }
}
