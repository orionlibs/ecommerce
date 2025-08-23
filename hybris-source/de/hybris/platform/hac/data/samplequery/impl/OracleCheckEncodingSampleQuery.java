package de.hybris.platform.hac.data.samplequery.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.hac.data.samplequery.SampleQuery;
import de.hybris.platform.util.Config;
import java.util.List;

public class OracleCheckEncodingSampleQuery implements SampleQuery
{
    private static final String DESCRIPTION = "Oracle: check encoding";
    private static final List<String> COMPATIBLE_DBS = Lists.newArrayList((Object[])new String[] {"oracle"});


    public String getQueryDescription()
    {
        return "Oracle: check encoding";
    }


    public String getQuery()
    {
        return "SELECT value FROM nls_database_parameters WHERE parameter = 'NLS_CHARACTERSET'";
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
