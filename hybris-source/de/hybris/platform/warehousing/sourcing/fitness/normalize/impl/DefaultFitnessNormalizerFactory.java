package de.hybris.platform.warehousing.sourcing.fitness.normalize.impl;

import de.hybris.platform.warehousing.data.sourcing.SourcingFactorIdentifiersEnum;
import de.hybris.platform.warehousing.sourcing.fitness.normalize.FitnessNormalizer;
import de.hybris.platform.warehousing.sourcing.fitness.normalize.FitnessNormalizerFactory;
import java.util.Map;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class DefaultFitnessNormalizerFactory implements FitnessNormalizerFactory, InitializingBean
{
    private Map<SourcingFactorIdentifiersEnum, FitnessNormalizer> fitnessNormalizerMap;


    public FitnessNormalizer getNormalizer(SourcingFactorIdentifiersEnum factorId)
    {
        return this.fitnessNormalizerMap.get(factorId);
    }


    protected Map<SourcingFactorIdentifiersEnum, FitnessNormalizer> getFitnessNormalizerMap()
    {
        return this.fitnessNormalizerMap;
    }


    @Required
    public void setFitnessNormalizerMap(Map<SourcingFactorIdentifiersEnum, FitnessNormalizer> fitnessNormalizerMap)
    {
        this.fitnessNormalizerMap = fitnessNormalizerMap;
    }


    public void afterPropertiesSet() throws Exception
    {
        if(this.fitnessNormalizerMap.isEmpty())
        {
            throw new IllegalArgumentException("Fitness normalizer map cannot be empty.");
        }
    }
}
