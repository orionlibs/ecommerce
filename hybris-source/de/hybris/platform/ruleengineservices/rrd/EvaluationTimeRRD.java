package de.hybris.platform.ruleengineservices.rrd;

import java.io.Serializable;

public class EvaluationTimeRRD implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Long evaluationTime;


    public void setEvaluationTime(Long evaluationTime)
    {
        this.evaluationTime = evaluationTime;
    }


    public Long getEvaluationTime()
    {
        return this.evaluationTime;
    }
}
