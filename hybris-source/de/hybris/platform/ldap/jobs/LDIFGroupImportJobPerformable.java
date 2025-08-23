package de.hybris.platform.ldap.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.ldap.jobs.impl.DefaultLDIFImportStrategy;
import de.hybris.platform.ldap.model.LDIFGroupImportCronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class LDIFGroupImportJobPerformable extends AbstractJobPerformable<LDIFGroupImportCronJobModel>
{
    private static final Logger LOG = Logger.getLogger(LDIFGroupImportJobPerformable.class.getName());


    public PerformResult perform(LDIFGroupImportCronJobModel cronJob)
    {
        if(cronJob != null)
        {
            try
            {
                performJob(cronJob);
            }
            catch(Exception e)
            {
                LOG.error("Exception at LDIFGroupImportJobPerformable", e);
                return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
            }
            if(cronJob.getRequestAbort() != null && cronJob.getRequestAbort().booleanValue())
            {
                return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
            }
            return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
        }
        return new PerformResult(CronJobResult.ERROR, CronJobStatus.FINISHED);
    }


    protected void performJob(LDIFGroupImportCronJobModel cronJob)
    {
        boolean usermode = cronJob.getImportUsers().booleanValue();
        String searchAttributes = cronJob.getResultfilter();
        if(usermode && (searchAttributes == null || !searchAttributes.contains("member")))
        {
            LOG.warn("Missing 'member' attribute! Skipping user import!");
            usermode = false;
        }
        if(usermode)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("executeSearchBasedImport - usermode: " + usermode);
            }
            executeSearchBasedImportUsermode(cronJob);
        }
        else
        {
            executeSearchBasedImport(cronJob);
        }
    }


    private void executeSearchBasedImport(LDIFGroupImportCronJobModel cronJob) throws LDAPImportException
    {
        DefaultLDIFImportStrategy fileBasedImport = lookupLDIFImportStrategy();
        fileBasedImport.setConfig(cronJob.getConfigFile());
        fileBasedImport.executeFileSearchBasedImport(performSearchAttributes(cronJob));
        cronJob.setDestMedia(fileBasedImport.getDestMedia());
        cronJob.setDumpMedia(fileBasedImport.getDumpMedia());
        this.modelService.save(cronJob);
    }


    private void executeSearchBasedImportUsermode(LDIFGroupImportCronJobModel cronJob) throws LDAPImportException
    {
        DefaultLDIFImportStrategy fileBasedImport = lookupLDIFImportStrategy();
        fileBasedImport.setConfig(cronJob.getConfigFile());
        fileBasedImport.executeFileSearchBasedUserImport(performSearchAttributes(cronJob));
        cronJob.setDestMedia(fileBasedImport.getDestMedia());
        cronJob.setDumpMedia(fileBasedImport.getDumpMedia());
        this.modelService.save(cronJob);
    }


    private Map<String, String> performSearchAttributes(LDIFGroupImportCronJobModel cronJob)
    {
        Map<String, String> searchAttributes = new HashMap<>();
        searchAttributes.put("resultFilter", cronJob.getResultfilter());
        searchAttributes.put("searchBase", cronJob.getSearchbase());
        searchAttributes.put("ldapQuery", cronJob.getLdapquery());
        searchAttributes.put("userResultFilter", cronJob.getUserResultfilter());
        searchAttributes.put("userSearchFieldQualifier", cronJob.getUserSearchFieldQualifier());
        searchAttributes.put("userRootDN", cronJob.getUserRootDN());
        return searchAttributes;
    }


    protected DefaultLDIFImportStrategy lookupLDIFImportStrategy()
    {
        throw new UnsupportedOperationException("please override LDIFGroupImportJobPerformable.lookupLDIFImportStrategy() or use <lookup-method name=\"lookupLDIFImportStrategy\" bean=\"..\">");
    }
}
