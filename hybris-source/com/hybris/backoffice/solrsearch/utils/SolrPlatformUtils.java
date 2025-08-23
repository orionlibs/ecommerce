package com.hybris.backoffice.solrsearch.utils;

import com.hybris.backoffice.ApplicationUtils;
import de.hybris.platform.solrfacetsearch.config.SolrServerMode;
import java.util.Locale;

public final class SolrPlatformUtils
{
    public static final String SOLR_EXPORTER_BEAN_NAME_PREFIX = "solr.exporter.";


    @Deprecated(since = "1808", forRemoval = true)
    public static boolean isPlatformReady()
    {
        return ApplicationUtils.isPlatformReady();
    }


    public static String createSolrExporterBeanName(SolrServerMode serverMode)
    {
        return "solr.exporter." + serverMode.toString().toLowerCase(Locale.getDefault());
    }
}
