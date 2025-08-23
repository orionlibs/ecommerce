package de.hybris.platform.persistence.flexiblesearch.internal;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import de.hybris.platform.core.Registry;

public final class QueryInterner
{
    private static final Interner<String> stringInterner = Interners.newWeakInterner();
    private static final String INTERN_QUERY_PROPERTY = "flexible.search.query.intern";


    public static String intern(String query)
    {
        if(query == null)
        {
            return null;
        }
        if(Registry.getCurrentTenant().getConfig().getBoolean("flexible.search.query.intern", true))
        {
            return (String)stringInterner.intern(query);
        }
        return query;
    }
}
