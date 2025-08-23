package de.hybris.platform.persistence.flexiblesearch.typecache;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.flexiblesearch.AbstractQueryFilter;
import de.hybris.platform.persistence.flexiblesearch.ParsedQuery;
import java.util.Collection;
import java.util.Set;

public interface FlexibleSearchTypeCacheProvider
{
    boolean isNonSearchableType(String paramString);


    UnkownPropertyInfo checkForUnknownPropertyAttribute(String paramString1, String paramString2);


    Set<PK> getExternalTableTypes(String paramString);


    boolean hasExternalTables(String paramString);


    boolean isAbstractRootTable(String paramString);


    Collection<AbstractQueryFilter> getQueryFilters(ParsedQuery paramParsedQuery, String paramString, boolean paramBoolean);


    Collection<String> getSearchableSubTypes(String paramString);


    PK getLanguagePkFromIsocode(String paramString);


    CachedTypeData getCachedTypeData(String paramString);
}
