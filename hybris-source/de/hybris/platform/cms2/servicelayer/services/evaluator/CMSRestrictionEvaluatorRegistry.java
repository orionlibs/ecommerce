package de.hybris.platform.cms2.servicelayer.services.evaluator;

import org.springframework.context.ApplicationContextAware;

public interface CMSRestrictionEvaluatorRegistry extends ApplicationContextAware
{
    CMSRestrictionEvaluator getCMSRestrictionEvaluator(String paramString);
}
