package de.hybris.platform.advancedsavedquery.jalo;

import de.hybris.platform.core.Operator;
import de.hybris.platform.jalo.SessionContext;

public class SimpleAdvancedSavedQuerySearchParameter extends GeneratedSimpleAdvancedSavedQuerySearchParameter
{
    public String toFlexibleSearchForm(SessionContext ctx, Operator operator)
    {
        return getFlexibleSearchPartFor(getSearchParameterName(), operator);
    }
}
