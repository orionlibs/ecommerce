package de.hybris.platform.hac.data.samplequery.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.hac.data.samplequery.SampleQuery;
import de.hybris.platform.util.Config;
import java.util.List;

public class CountFromLanguageSampleQuery implements SampleQuery
{
    private static final String DESCRIPTION = "Count languages";
    private static final List<String> COMPATIBLE_DBS = Lists.newArrayList((Object[])new String[] {"hsqldb", "mysql", "oracle", "sqlserver", "postgresql"});


    public String getQueryDescription()
    {
        return "Count languages";
    }


    public String getQuery()
    {
        return "SELECT count(*) sum_languages FROM languages";
    }


    public boolean isCompatibleWitCurrentDb()
    {
        return COMPATIBLE_DBS.contains(Config.getDatabase());
    }


    public boolean isFlexibleSearch()
    {
        return false;
    }


    public String getAdditionalDescription()
    {
        return null;
    }
}
