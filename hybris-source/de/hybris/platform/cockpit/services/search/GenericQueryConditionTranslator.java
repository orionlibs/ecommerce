package de.hybris.platform.cockpit.services.search;

import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.core.GenericCondition;

public interface GenericQueryConditionTranslator extends ConditionTranslator
{
    GenericCondition translate(SearchParameterValue paramSearchParameterValue, ConditionTranslatorContext paramConditionTranslatorContext);
}
