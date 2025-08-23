package de.hybris.platform.hac.data.samplequery.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.hac.data.samplequery.SampleQuery;
import de.hybris.platform.util.Config;
import java.util.List;

public class ShowMySqlTablesSampleQuery implements SampleQuery
{
    private static final String DESCRIPTION = "Show tables";
    private static final List<String> COMPATIBLE_DBS = Lists.newArrayList((Object[])new String[] {"mysql"});


    public String getQueryDescription()
    {
        return "Show tables";
    }


    public String getAdditionalDescription()
    {
        return null;
    }


    public String getQuery()
    {
        return "SHOW TABLES";
    }


    public boolean isCompatibleWitCurrentDb()
    {
        return COMPATIBLE_DBS.contains(Config.getDatabase());
    }


    public boolean isFlexibleSearch()
    {
        return false;
    }
}
