package de.hybris.platform.warehousing.sourcing.fitness.evaluation.impl;

import de.hybris.platform.warehousing.data.sourcing.SourcingFactorIdentifiersEnum;
import de.hybris.platform.warehousing.sourcing.fitness.evaluation.FitnessEvaluator;
import de.hybris.platform.warehousing.sourcing.fitness.evaluation.FitnessEvaluatorFactory;
import java.util.Map;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class DefaultFitnessEvaluatorFactory implements FitnessEvaluatorFactory, InitializingBean
{
    private Map<SourcingFactorIdentifiersEnum, FitnessEvaluator> fitnessEvaluatorMap;


    public FitnessEvaluator getEvaluator(SourcingFactorIdentifiersEnum factorId)
    {
        return getFitnessEvaluatorMap().get(factorId);
    }


    protected Map<SourcingFactorIdentifiersEnum, FitnessEvaluator> getFitnessEvaluatorMap()
    {
        return this.fitnessEvaluatorMap;
    }


    @Required
    public void setFitnessEvaluatorMap(Map<SourcingFactorIdentifiersEnum, FitnessEvaluator> fitnessEvaluatorMap)
    {
        this.fitnessEvaluatorMap = fitnessEvaluatorMap;
    }


    public void afterPropertiesSet() throws Exception
    {
        if(getFitnessEvaluatorMap().isEmpty())
        {
            throw new IllegalArgumentException("Fitness evaluators map cannot be empty.");
        }
    }
}
