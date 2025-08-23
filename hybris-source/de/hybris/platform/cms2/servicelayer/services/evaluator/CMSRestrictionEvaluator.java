package de.hybris.platform.cms2.servicelayer.services.evaluator;

import de.hybris.platform.cms2.servicelayer.data.RestrictionData;

public interface CMSRestrictionEvaluator<T extends de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel>
{
    boolean evaluate(T paramT, RestrictionData paramRestrictionData);
}
