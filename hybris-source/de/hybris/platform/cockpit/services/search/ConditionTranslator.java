package de.hybris.platform.cockpit.services.search;

import de.hybris.platform.cockpit.model.search.SearchParameterValue;

public interface ConditionTranslator
{
    Object translate(SearchParameterValue paramSearchParameterValue, ConditionTranslatorContext paramConditionTranslatorContext);
}
