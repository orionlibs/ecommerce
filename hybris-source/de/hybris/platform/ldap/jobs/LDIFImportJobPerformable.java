package de.hybris.platform.ldap.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.ldap.enums.LDIFImportModeEnum;
import de.hybris.platform.ldap.jobs.impl.DefaultLDIFImportStrategy;
import de.hybris.platform.ldap.model.LDIFImportCronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class LDIFImportJobPerformable extends AbstractJobPerformable<LDIFImportCronJobModel>
{
    private static final Logger LOG = Logger.getLogger(LDIFImportJobPerformable.class.getName());


    public PerformResult perform(LDIFImportCronJobModel cronJob)
    {
        if(cronJob != null)
        {
            try
            {
                performJob(cronJob);
            }
            catch(Exception e)
            {
                LOG.error("Exception at LDIFImportJobPerformable", e);
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


    protected void performJob(LDIFImportCronJobModel cronJob) throws LDAPImportException
    {
        LDIFImportModeEnum mode = cronJob.getImportmode();
        if(LDIFImportModeEnum.FILEBASED.equals(mode))
        {
            executeFileBasedImport(cronJob);
        }
        else if(LDIFImportModeEnum.QUERYBASED.equals(mode))
        {
            executeSearchBasedImport(cronJob);
        }
        else
        {
            throw new SystemException("Unsupported importmode: " + mode);
        }
    }


    private void executeFileBasedImport(LDIFImportCronJobModel cronJob) throws LDAPImportException
    {
        DefaultLDIFImportStrategy fileBasedImport = lookupLDIFImportStrategy();
        fileBasedImport.setConfig(cronJob.getConfigFile());
        fileBasedImport.executeFileBasedImport(cronJob.getLdifFile());
        cronJob.setDestMedia(fileBasedImport.getDestMedia());
        cronJob.setDumpMedia(fileBasedImport.getDumpMedia());
        this.modelService.save(cronJob);
    }


    private void executeSearchBasedImport(LDIFImportCronJobModel cronJob) throws LDAPImportException
    {
        DefaultLDIFImportStrategy fileBasedImport = lookupLDIFImportStrategy();
        fileBasedImport.setConfig(cronJob.getConfigFile());
        fileBasedImport.executeFileSearchBasedImport(performSearchAttributes(cronJob));
        cronJob.setDestMedia(fileBasedImport.getDestMedia());
        cronJob.setDumpMedia(fileBasedImport.getDumpMedia());
        this.modelService.save(cronJob);
    }


    private Map<String, String> performSearchAttributes(LDIFImportCronJobModel cronJob)
    {
        Map<String, String> searchAttributes = new HashMap<>();
        searchAttributes.put("resultFilter", cronJob.getResultfilter());
        searchAttributes.put("searchBase", cronJob.getSearchbase());
        searchAttributes.put("ldapQuery", cronJob.getLdapquery());
        return searchAttributes;
    }


    protected DefaultLDIFImportStrategy lookupLDIFImportStrategy()
    {
        throw new UnsupportedOperationException("please override LDIFGroupImportJobPerformable.lookupLDIFImportStrategy() or use <lookup-method name=\"lookupLDIFImportStrategy\" bean=\"..\">");
    }
}
