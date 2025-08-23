package de.hybris.platform.personalizationservices.segment.dao.impl;

import com.google.common.collect.Sets;
import de.hybris.platform.personalizationservices.dao.impl.AbstractCxDaoStrategy;
import de.hybris.platform.personalizationservices.segment.dao.CxSegmentDaoStrategy;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class CxSegmentCodeDaoStrategy extends AbstractCxDaoStrategy implements CxSegmentDaoStrategy
{
    private static final String CODE = "code";
    private static final Set<String> PARAMS = Collections.unmodifiableSet(Sets.newHashSet((Object[])new String[] {"code"}));


    public Set<String> getRequiredParameters()
    {
        return PARAMS;
    }


    public FlexibleSearchQuery getQuery(Map<String, String> params)
    {
        String code = "%" + (String)params.get("code") + "%";
        String query = "SELECT {pk} FROM {CxSegment} WHERE LOWER({code}) LIKE LOWER(?code) ORDER BY {code} ASC ";
        return getCxDaoQueryBuilder().buildQuery("SELECT {pk} FROM {CxSegment} WHERE LOWER({code}) LIKE LOWER(?code) ORDER BY {code} ASC ", Collections.singletonMap("code", code));
    }
}
