package de.hybris.platform.solrfacetsearch.jalo.indexer.cron;

import de.hybris.platform.jalo.JaloSession;

public class SolrIndexerCronJob extends GeneratedSolrIndexerCronJob
{
    public static final String DISABLE_RESTRICTIONS = "disableRestrictions";
    public static final String DISABLE_RESTRICTIONS_GROUP_INHERITANCE = "disableRestrictionGroupInheritance";


    protected JaloSession createSessionForCronJob(JaloSession jaloSession)
    {
        JaloSession parentSession = super.createSessionForCronJob(jaloSession);
        jaloSession.getSessionContext().setAttribute("disableRestrictions", Boolean.FALSE);
        jaloSession.getSessionContext().setAttribute("disableRestrictionGroupInheritance", Boolean.FALSE);
        return parentSession;
    }
}
