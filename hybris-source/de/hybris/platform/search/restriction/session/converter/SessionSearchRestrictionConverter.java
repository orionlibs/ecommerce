package de.hybris.platform.search.restriction.session.converter;

import de.hybris.platform.jalo.flexiblesearch.ContextQueryFilter;
import de.hybris.platform.search.restriction.session.SessionSearchRestriction;
import java.util.Collection;

public interface SessionSearchRestrictionConverter
{
    ContextQueryFilter convert(SessionSearchRestriction paramSessionSearchRestriction);


    SessionSearchRestriction convert(ContextQueryFilter paramContextQueryFilter);


    Collection<ContextQueryFilter> convertFromRestrictions(Collection<SessionSearchRestriction> paramCollection);


    Collection<SessionSearchRestriction> convertFromFilters(Collection<ContextQueryFilter> paramCollection);
}
