package de.hybris.platform.ruleengineservices.rao.providers.impl;

import de.hybris.platform.ruleengineservices.rao.providers.RAOProvider;
import de.hybris.platform.ruleengineservices.rrd.EvaluationTimeRRD;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

public class DefaultEvaluationTimeRRDProvider implements RAOProvider
{
    public Set expandFactModel(Object modelFact)
    {
        if(modelFact instanceof Date)
        {
            EvaluationTimeRRD evaluationTimeRRD = new EvaluationTimeRRD();
            evaluationTimeRRD.setEvaluationTime(Long.valueOf(((Date)modelFact).getTime()));
            return Collections.singleton(evaluationTimeRRD);
        }
        return Collections.emptySet();
    }
}
