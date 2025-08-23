package de.hybris.platform.cms2.servicelayer.services.evaluator.impl;

import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluator;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluatorMapping;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluatorRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class DefaultCMSRestrictionEvaluatorRegistry implements CMSRestrictionEvaluatorRegistry
{
    private Map<String, CMSRestrictionEvaluator> evaluatorsMap = new HashMap<>();
    private ApplicationContext applicationContext;


    public ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    public CMSRestrictionEvaluator getCMSRestrictionEvaluator(String restrictionTypeCode)
    {
        return this.evaluatorsMap.get(restrictionTypeCode.toLowerCase());
    }


    public Map<String, CMSRestrictionEvaluator> getEvaluatorsMap()
    {
        return this.evaluatorsMap;
    }


    public void init()
    {
        Map<String, CMSRestrictionEvaluatorMapping> beanMap = this.applicationContext.getBeansOfType(CMSRestrictionEvaluatorMapping.class);
        List<CMSRestrictionEvaluatorMapping> mappings = new ArrayList<>(beanMap.values());
        this.evaluatorsMap.clear();
        for(CMSRestrictionEvaluatorMapping mapping : mappings)
        {
            CMSRestrictionEvaluator handler = this.evaluatorsMap.get(mapping.getRestrictionTypeCode().toLowerCase());
            if(handler == null)
            {
                this.evaluatorsMap.put(mapping.getRestrictionTypeCode().toLowerCase(), mapping.getRestrictionEvaluator());
            }
        }
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }


    public void setEvaluatorsMap(Map<String, CMSRestrictionEvaluator> evaluatorMap)
    {
        this.evaluatorsMap = evaluatorMap;
    }
}
