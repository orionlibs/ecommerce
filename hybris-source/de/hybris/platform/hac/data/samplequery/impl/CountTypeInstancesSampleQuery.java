package de.hybris.platform.hac.data.samplequery.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.hac.data.samplequery.SampleQuery;
import de.hybris.platform.util.Config;
import java.util.List;

public class CountTypeInstancesSampleQuery implements SampleQuery
{
    private static final String DESCRIPTION = "Count the instances of a type";
    private static final String ADDITIONAL_DESCRIPTION = "Executing this query can take a long time depending on the fill level of the database";
    private static final List<String> COMPATIBLE_DBS = Lists.newArrayList((Object[])new String[] {"hsqldb", "mysql", "oracle", "sqlserver", "postgresql"});


    public String getQueryDescription()
    {
        return "Count the instances of a type";
    }


    public String getQuery()
    {
        return "SELECT count(a.pk) AS itemcount, a.code AS itemtype FROM ({{SELECT {i:pk} AS pk, {c:code} AS code FROM {Item AS i},{ComposedType AS c} WHERE {i:itemtype}={c:pk}}}) a GROUP BY a.code ORDER BY itemcount desc, code";
    }


    public boolean isCompatibleWitCurrentDb()
    {
        return COMPATIBLE_DBS.contains(Config.getDatabase());
    }


    public boolean isFlexibleSearch()
    {
        return true;
    }


    public String getAdditionalDescription()
    {
        return "Executing this query can take a long time depending on the fill level of the database";
    }
}
