package de.hybris.platform.jalo.flexiblesearch;

import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.SearchRestriction;

final class SearchRestrictionFilter extends AbstractQueryFilter
{
    SearchRestrictionFilter(SearchRestriction sr)
    {
        super(sr.getCode(), sr.getRestrictionType(), sr.getQuery());
    }


    SearchRestrictionFilter(String code, ComposedType type, String query)
    {
        super(code, type, query);
    }
}
