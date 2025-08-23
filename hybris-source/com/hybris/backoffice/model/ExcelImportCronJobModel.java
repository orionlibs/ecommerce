package com.hybris.backoffice.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ExcelImportCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "ExcelImportCronJob";
    public static final String EXCELFILE = "excelFile";
    public static final String REFERENCEDCONTENT = "referencedContent";


    public ExcelImportCronJobModel()
    {
    }


    public ExcelImportCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExcelImportCronJobModel(MediaModel _excelFile, JobModel _job)
    {
        setExcelFile(_excelFile);
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExcelImportCronJobModel(MediaModel _excelFile, JobModel _job, ItemModel _owner)
    {
        setExcelFile(_excelFile);
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "excelFile", type = Accessor.Type.GETTER)
    public MediaModel getExcelFile()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("excelFile");
    }


    @Accessor(qualifier = "referencedContent", type = Accessor.Type.GETTER)
    public MediaModel getReferencedContent()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("referencedContent");
    }


    @Accessor(qualifier = "excelFile", type = Accessor.Type.SETTER)
    public void setExcelFile(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("excelFile", value);
    }


    @Accessor(qualifier = "referencedContent", type = Accessor.Type.SETTER)
    public void setReferencedContent(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("referencedContent", value);
    }
}
