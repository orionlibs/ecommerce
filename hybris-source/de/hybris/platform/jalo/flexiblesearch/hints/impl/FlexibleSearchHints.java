package de.hybris.platform.jalo.flexiblesearch.hints.impl;

import de.hybris.platform.jalo.flexiblesearch.hints.Hint;

public class FlexibleSearchHints
{
    public static Hint categorizedQuery(String category)
    {
        return (Hint)new CategorizedQueryHint(category);
    }
}
