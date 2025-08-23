package de.hybris.platform.impex.model.cronjob;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.enums.EncodingEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.impex.enums.ImpExValidationModeEnum;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class ImpExImportCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "ImpExImportCronJob";
    public static final String _JOBCRONJOBRELATION = "JobCronJobRelation";
    public static final String JOBMEDIA = "jobMedia";
    public static final String WORKMEDIA = "workMedia";
    public static final String LASTSUCCESSFULLINE = "lastSuccessfulLine";
    public static final String MEDIASMEDIA = "mediasMedia";
    public static final String EXTERNALDATACOLLECTION = "externalDataCollection";
    public static final String LOCALE = "locale";
    public static final String DUMPFILEENCODING = "dumpFileEncoding";
    public static final String ENABLECODEEXECUTION = "enableCodeExecution";
    public static final String ENABLEEXTERNALCODEEXECUTION = "enableExternalCodeExecution";
    public static final String ENABLEEXTERNALSYNTAXPARSING = "enableExternalSyntaxParsing";
    public static final String ENABLEHMCSAVEDVALUES = "enableHmcSavedValues";
    public static final String MEDIASTARGET = "mediasTarget";
    public static final String VALUECOUNT = "valueCount";
    public static final String UNRESOLVEDDATASTORE = "unresolvedDataStore";
    public static final String ZIPENTRY = "zipentry";
    public static final String MODE = "mode";
    public static final String DUMPINGALLOWED = "dumpingAllowed";
    public static final String UNZIPMEDIASMEDIA = "unzipMediasMedia";
    public static final String MAXTHREADS = "maxThreads";
    public static final String LEGACYMODE = "legacyMode";


    public ImpExImportCronJobModel()
    {
    }


    public ImpExImportCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ImpExImportCronJobModel(ImpExImportJobModel _job)
    {
        setJob((JobModel)_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ImpExImportCronJobModel(ImpExImportJobModel _job, ItemModel _owner)
    {
        setJob((JobModel)_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "dumpFileEncoding", type = Accessor.Type.GETTER)
    public EncodingEnum getDumpFileEncoding()
    {
        return (EncodingEnum)getPersistenceContext().getPropertyValue("dumpFileEncoding");
    }


    @Accessor(qualifier = "dumpingAllowed", type = Accessor.Type.GETTER)
    public Boolean getDumpingAllowed()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("dumpingAllowed");
    }


    @Accessor(qualifier = "enableCodeExecution", type = Accessor.Type.GETTER)
    public Boolean getEnableCodeExecution()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("enableCodeExecution");
    }


    @Accessor(qualifier = "enableExternalCodeExecution", type = Accessor.Type.GETTER)
    public Boolean getEnableExternalCodeExecution()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("enableExternalCodeExecution");
    }


    @Accessor(qualifier = "enableExternalSyntaxParsing", type = Accessor.Type.GETTER)
    public Boolean getEnableExternalSyntaxParsing()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("enableExternalSyntaxParsing");
    }


    @Accessor(qualifier = "enableHmcSavedValues", type = Accessor.Type.GETTER)
    public Boolean getEnableHmcSavedValues()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("enableHmcSavedValues");
    }


    @Accessor(qualifier = "externalDataCollection", type = Accessor.Type.GETTER)
    public Collection<ImpExMediaModel> getExternalDataCollection()
    {
        return (Collection<ImpExMediaModel>)getPersistenceContext().getPropertyValue("externalDataCollection");
    }


    @Accessor(qualifier = "job", type = Accessor.Type.GETTER)
    public ImpExImportJobModel getJob()
    {
        return (ImpExImportJobModel)super.getJob();
    }


    @Accessor(qualifier = "jobMedia", type = Accessor.Type.GETTER)
    public ImpExMediaModel getJobMedia()
    {
        return (ImpExMediaModel)getPersistenceContext().getPropertyValue("jobMedia");
    }


    @Accessor(qualifier = "lastSuccessfulLine", type = Accessor.Type.GETTER)
    public Integer getLastSuccessfulLine()
    {
        return (Integer)getPersistenceContext().getPropertyValue("lastSuccessfulLine");
    }


    @Accessor(qualifier = "legacyMode", type = Accessor.Type.GETTER)
    public Boolean getLegacyMode()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("legacyMode");
    }


    @Accessor(qualifier = "locale", type = Accessor.Type.GETTER)
    public String getLocale()
    {
        return (String)getPersistenceContext().getPropertyValue("locale");
    }


    @Accessor(qualifier = "maxThreads", type = Accessor.Type.GETTER)
    public Integer getMaxThreads()
    {
        return (Integer)getPersistenceContext().getPropertyValue("maxThreads");
    }


    @Accessor(qualifier = "mediasMedia", type = Accessor.Type.GETTER)
    public MediaModel getMediasMedia()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("mediasMedia");
    }


    @Accessor(qualifier = "mediasTarget", type = Accessor.Type.GETTER)
    public String getMediasTarget()
    {
        return (String)getPersistenceContext().getPropertyValue("mediasTarget");
    }


    @Accessor(qualifier = "mode", type = Accessor.Type.GETTER)
    public ImpExValidationModeEnum getMode()
    {
        return (ImpExValidationModeEnum)getPersistenceContext().getPropertyValue("mode");
    }


    @Accessor(qualifier = "unresolvedDataStore", type = Accessor.Type.GETTER)
    public ImpExMediaModel getUnresolvedDataStore()
    {
        return (ImpExMediaModel)getPersistenceContext().getPropertyValue("unresolvedDataStore");
    }


    @Accessor(qualifier = "unzipMediasMedia", type = Accessor.Type.GETTER)
    public Boolean getUnzipMediasMedia()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("unzipMediasMedia");
    }


    @Accessor(qualifier = "valueCount", type = Accessor.Type.GETTER)
    public Integer getValueCount()
    {
        return (Integer)getPersistenceContext().getPropertyValue("valueCount");
    }


    @Accessor(qualifier = "workMedia", type = Accessor.Type.GETTER)
    public ImpExMediaModel getWorkMedia()
    {
        return (ImpExMediaModel)getPersistenceContext().getPropertyValue("workMedia");
    }


    @Accessor(qualifier = "zipentry", type = Accessor.Type.GETTER)
    public String getZipentry()
    {
        return (String)getPersistenceContext().getPropertyValue("zipentry");
    }


    @Accessor(qualifier = "dumpFileEncoding", type = Accessor.Type.SETTER)
    public void setDumpFileEncoding(EncodingEnum value)
    {
        getPersistenceContext().setPropertyValue("dumpFileEncoding", value);
    }


    @Accessor(qualifier = "dumpingAllowed", type = Accessor.Type.SETTER)
    public void setDumpingAllowed(Boolean value)
    {
        getPersistenceContext().setPropertyValue("dumpingAllowed", value);
    }


    @Accessor(qualifier = "enableCodeExecution", type = Accessor.Type.SETTER)
    public void setEnableCodeExecution(Boolean value)
    {
        getPersistenceContext().setPropertyValue("enableCodeExecution", value);
    }


    @Accessor(qualifier = "enableExternalCodeExecution", type = Accessor.Type.SETTER)
    public void setEnableExternalCodeExecution(Boolean value)
    {
        getPersistenceContext().setPropertyValue("enableExternalCodeExecution", value);
    }


    @Accessor(qualifier = "enableExternalSyntaxParsing", type = Accessor.Type.SETTER)
    public void setEnableExternalSyntaxParsing(Boolean value)
    {
        getPersistenceContext().setPropertyValue("enableExternalSyntaxParsing", value);
    }


    @Accessor(qualifier = "enableHmcSavedValues", type = Accessor.Type.SETTER)
    public void setEnableHmcSavedValues(Boolean value)
    {
        getPersistenceContext().setPropertyValue("enableHmcSavedValues", value);
    }


    @Accessor(qualifier = "externalDataCollection", type = Accessor.Type.SETTER)
    public void setExternalDataCollection(Collection<ImpExMediaModel> value)
    {
        getPersistenceContext().setPropertyValue("externalDataCollection", value);
    }


    @Accessor(qualifier = "job", type = Accessor.Type.SETTER)
    public void setJob(JobModel value)
    {
        if(value == null || value instanceof ImpExImportJobModel)
        {
            super.setJob(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.impex.model.cronjob.ImpExImportJobModel");
        }
    }


    @Accessor(qualifier = "jobMedia", type = Accessor.Type.SETTER)
    public void setJobMedia(ImpExMediaModel value)
    {
        getPersistenceContext().setPropertyValue("jobMedia", value);
    }


    @Accessor(qualifier = "lastSuccessfulLine", type = Accessor.Type.SETTER)
    public void setLastSuccessfulLine(Integer value)
    {
        getPersistenceContext().setPropertyValue("lastSuccessfulLine", value);
    }


    @Accessor(qualifier = "legacyMode", type = Accessor.Type.SETTER)
    public void setLegacyMode(Boolean value)
    {
        getPersistenceContext().setPropertyValue("legacyMode", value);
    }


    @Accessor(qualifier = "locale", type = Accessor.Type.SETTER)
    public void setLocale(String value)
    {
        getPersistenceContext().setPropertyValue("locale", value);
    }


    @Accessor(qualifier = "maxThreads", type = Accessor.Type.SETTER)
    public void setMaxThreads(Integer value)
    {
        getPersistenceContext().setPropertyValue("maxThreads", value);
    }


    @Accessor(qualifier = "mediasMedia", type = Accessor.Type.SETTER)
    public void setMediasMedia(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("mediasMedia", value);
    }


    @Accessor(qualifier = "mediasTarget", type = Accessor.Type.SETTER)
    public void setMediasTarget(String value)
    {
        getPersistenceContext().setPropertyValue("mediasTarget", value);
    }


    @Accessor(qualifier = "mode", type = Accessor.Type.SETTER)
    public void setMode(ImpExValidationModeEnum value)
    {
        getPersistenceContext().setPropertyValue("mode", value);
    }


    @Accessor(qualifier = "unresolvedDataStore", type = Accessor.Type.SETTER)
    public void setUnresolvedDataStore(ImpExMediaModel value)
    {
        getPersistenceContext().setPropertyValue("unresolvedDataStore", value);
    }


    @Accessor(qualifier = "unzipMediasMedia", type = Accessor.Type.SETTER)
    public void setUnzipMediasMedia(Boolean value)
    {
        getPersistenceContext().setPropertyValue("unzipMediasMedia", value);
    }


    @Accessor(qualifier = "valueCount", type = Accessor.Type.SETTER)
    public void setValueCount(Integer value)
    {
        getPersistenceContext().setPropertyValue("valueCount", value);
    }


    @Accessor(qualifier = "workMedia", type = Accessor.Type.SETTER)
    public void setWorkMedia(ImpExMediaModel value)
    {
        getPersistenceContext().setPropertyValue("workMedia", value);
    }


    @Accessor(qualifier = "zipentry", type = Accessor.Type.SETTER)
    public void setZipentry(String value)
    {
        getPersistenceContext().setPropertyValue("zipentry", value);
    }
}
