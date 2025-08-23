package de.hybris.platform.hac.data.samplequery.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.hac.data.samplequery.SampleQuery;
import de.hybris.platform.util.Config;
import java.util.List;

public class TotalItemCountSampleQuery implements SampleQuery
{
    private static final String DESCRIPTION = "Total item count";
    private static final List<String> COMPATIBLE_DBS = Lists.newArrayList((Object[])new String[] {"hsqldb", "mysql", "oracle", "sqlserver", "postgresql"});


    public String getQueryDescription()
    {
        return "Total item count";
    }


    public String getQuery()
    {
        return "SELECT count({pk}) FROM {Item}";
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
        return null;
    }
}
