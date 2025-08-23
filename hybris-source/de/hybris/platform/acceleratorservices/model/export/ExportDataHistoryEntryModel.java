package de.hybris.platform.acceleratorservices.model.export;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorservices.enums.ExportDataStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class ExportDataHistoryEntryModel extends ItemModel
{
    public static final String _TYPECODE = "ExportDataHistoryEntry";
    public static final String _EXPORTDATACRONJOB2EXPORTDATAHISTORYREL = "ExportDataCronJob2ExportDataHistoryRel";
    public static final String CODE = "code";
    public static final String STATUS = "status";
    public static final String STARTTIME = "startTime";
    public static final String FINISHTIME = "finishTime";
    public static final String PROCESSEDRESULTCOUNT = "processedResultCount";
    public static final String FAILUREMESSAGE = "failureMessage";
    public static final String EXPORTDATACRONJOB = "exportDataCronJob";


    public ExportDataHistoryEntryModel()
    {
    }


    public ExportDataHistoryEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExportDataHistoryEntryModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExportDataHistoryEntryModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "exportDataCronJob", type = Accessor.Type.GETTER)
    public ExportDataCronJobModel getExportDataCronJob()
    {
        return (ExportDataCronJobModel)getPersistenceContext().getPropertyValue("exportDataCronJob");
    }


    @Accessor(qualifier = "failureMessage", type = Accessor.Type.GETTER)
    public String getFailureMessage()
    {
        return (String)getPersistenceContext().getPropertyValue("failureMessage");
    }


    @Accessor(qualifier = "finishTime", type = Accessor.Type.GETTER)
    public Date getFinishTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("finishTime");
    }


    @Accessor(qualifier = "processedResultCount", type = Accessor.Type.GETTER)
    public Integer getProcessedResultCount()
    {
        return (Integer)getPersistenceContext().getPropertyValue("processedResultCount");
    }


    @Accessor(qualifier = "startTime", type = Accessor.Type.GETTER)
    public Date getStartTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("startTime");
    }


    @Accessor(qualifier = "status", type = Accessor.Type.GETTER)
    public ExportDataStatus getStatus()
    {
        return (ExportDataStatus)getPersistenceContext().getPropertyValue("status");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "exportDataCronJob", type = Accessor.Type.SETTER)
    public void setExportDataCronJob(ExportDataCronJobModel value)
    {
        getPersistenceContext().setPropertyValue("exportDataCronJob", value);
    }


    @Accessor(qualifier = "failureMessage", type = Accessor.Type.SETTER)
    public void setFailureMessage(String value)
    {
        getPersistenceContext().setPropertyValue("failureMessage", value);
    }


    @Accessor(qualifier = "finishTime", type = Accessor.Type.SETTER)
    public void setFinishTime(Date value)
    {
        getPersistenceContext().setPropertyValue("finishTime", value);
    }


    @Accessor(qualifier = "processedResultCount", type = Accessor.Type.SETTER)
    public void setProcessedResultCount(Integer value)
    {
        getPersistenceContext().setPropertyValue("processedResultCount", value);
    }


    @Accessor(qualifier = "startTime", type = Accessor.Type.SETTER)
    public void setStartTime(Date value)
    {
        getPersistenceContext().setPropertyValue("startTime", value);
    }


    @Accessor(qualifier = "status", type = Accessor.Type.SETTER)
    public void setStatus(ExportDataStatus value)
    {
        getPersistenceContext().setPropertyValue("status", value);
    }
}
