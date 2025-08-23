package de.hybris.platform.impex.model.cronjob;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.enums.EncodingEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.impex.enums.ExportConverterEnum;
import de.hybris.platform.impex.enums.ImpExValidationModeEnum;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.impex.model.exp.ExportModel;
import de.hybris.platform.impex.model.exp.HeaderLibraryModel;
import de.hybris.platform.impex.model.exp.ImpExExportMediaModel;
import de.hybris.platform.impex.model.exp.ReportModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ImpExExportCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "ImpExExportCronJob";
    public static final String ENCODING = "encoding";
    public static final String MODE = "mode";
    public static final String DATAEXPORTTARGET = "dataExportTarget";
    public static final String MEDIASEXPORTTARGET = "mediasExportTarget";
    public static final String EXPORTTEMPLATE = "exportTemplate";
    public static final String EXPORT = "export";
    public static final String ITEMSEXPORTED = "itemsExported";
    public static final String ITEMSMAXCOUNT = "itemsMaxCount";
    public static final String ITEMSSKIPPED = "itemsSkipped";
    public static final String JOBMEDIA = "jobMedia";
    public static final String FIELDSEPARATOR = "fieldSeparator";
    public static final String QUOTECHARACTER = "quoteCharacter";
    public static final String COMMENTCHARACTER = "commentCharacter";
    public static final String DATAEXPORTMEDIACODE = "dataExportMediaCode";
    public static final String MEDIASEXPORTMEDIACODE = "mediasExportMediaCode";
    public static final String REPORT = "report";
    public static final String CONVERTERCLASS = "converterClass";
    public static final String SINGLEFILE = "singleFile";


    public ImpExExportCronJobModel()
    {
    }


    public ImpExExportCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ImpExExportCronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ImpExExportCronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "commentCharacter", type = Accessor.Type.GETTER)
    public Character getCommentCharacter()
    {
        Character value = (Character)getPersistenceContext().getPropertyValue("commentCharacter");
        return (value != null) ? value : Character.valueOf('#');
    }


    @Accessor(qualifier = "converterClass", type = Accessor.Type.GETTER)
    public ExportConverterEnum getConverterClass()
    {
        return (ExportConverterEnum)getPersistenceContext().getPropertyValue("converterClass");
    }


    @Accessor(qualifier = "dataExportMediaCode", type = Accessor.Type.GETTER)
    public String getDataExportMediaCode()
    {
        String value = (String)getPersistenceContext().getPropertyValue("dataExportMediaCode");
        return (value != null) ? value : ("dataexport_" + getCode());
    }


    @Accessor(qualifier = "dataExportTarget", type = Accessor.Type.GETTER)
    public ImpExExportMediaModel getDataExportTarget()
    {
        return (ImpExExportMediaModel)getPersistenceContext().getPropertyValue("dataExportTarget");
    }


    @Accessor(qualifier = "encoding", type = Accessor.Type.GETTER)
    public EncodingEnum getEncoding()
    {
        return (EncodingEnum)getPersistenceContext().getPropertyValue("encoding");
    }


    @Accessor(qualifier = "export", type = Accessor.Type.GETTER)
    public ExportModel getExport()
    {
        return (ExportModel)getPersistenceContext().getPropertyValue("export");
    }


    @Accessor(qualifier = "exportTemplate", type = Accessor.Type.GETTER)
    public HeaderLibraryModel getExportTemplate()
    {
        return (HeaderLibraryModel)getPersistenceContext().getPropertyValue("exportTemplate");
    }


    @Accessor(qualifier = "fieldSeparator", type = Accessor.Type.GETTER)
    public Character getFieldSeparator()
    {
        Character value = (Character)getPersistenceContext().getPropertyValue("fieldSeparator");
        return (value != null) ? value : Character.valueOf(';');
    }


    @Accessor(qualifier = "itemsExported", type = Accessor.Type.GETTER)
    public Integer getItemsExported()
    {
        return (Integer)getPersistenceContext().getPropertyValue("itemsExported");
    }


    @Accessor(qualifier = "itemsMaxCount", type = Accessor.Type.GETTER)
    public Integer getItemsMaxCount()
    {
        return (Integer)getPersistenceContext().getPropertyValue("itemsMaxCount");
    }


    @Accessor(qualifier = "itemsSkipped", type = Accessor.Type.GETTER)
    public Integer getItemsSkipped()
    {
        return (Integer)getPersistenceContext().getPropertyValue("itemsSkipped");
    }


    @Accessor(qualifier = "jobMedia", type = Accessor.Type.GETTER)
    public ImpExMediaModel getJobMedia()
    {
        return (ImpExMediaModel)getPersistenceContext().getPropertyValue("jobMedia");
    }


    @Accessor(qualifier = "mediasExportMediaCode", type = Accessor.Type.GETTER)
    public String getMediasExportMediaCode()
    {
        String value = (String)getPersistenceContext().getPropertyValue("mediasExportMediaCode");
        return (value != null) ? value : ("mediasexport_" + getCode());
    }


    @Accessor(qualifier = "mediasExportTarget", type = Accessor.Type.GETTER)
    public ImpExExportMediaModel getMediasExportTarget()
    {
        return (ImpExExportMediaModel)getPersistenceContext().getPropertyValue("mediasExportTarget");
    }


    @Accessor(qualifier = "mode", type = Accessor.Type.GETTER)
    public ImpExValidationModeEnum getMode()
    {
        return (ImpExValidationModeEnum)getPersistenceContext().getPropertyValue("mode");
    }


    @Accessor(qualifier = "quoteCharacter", type = Accessor.Type.GETTER)
    public Character getQuoteCharacter()
    {
        Character value = (Character)getPersistenceContext().getPropertyValue("quoteCharacter");
        return (value != null) ? value : Character.valueOf('"');
    }


    @Accessor(qualifier = "report", type = Accessor.Type.GETTER)
    public ReportModel getReport()
    {
        return (ReportModel)getPersistenceContext().getPropertyValue("report");
    }


    @Accessor(qualifier = "singleFile", type = Accessor.Type.GETTER)
    public Boolean getSingleFile()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("singleFile");
    }


    @Accessor(qualifier = "commentCharacter", type = Accessor.Type.SETTER)
    public void setCommentCharacter(Character value)
    {
        getPersistenceContext().setPropertyValue("commentCharacter", value);
    }


    @Accessor(qualifier = "converterClass", type = Accessor.Type.SETTER)
    public void setConverterClass(ExportConverterEnum value)
    {
        getPersistenceContext().setPropertyValue("converterClass", value);
    }


    @Accessor(qualifier = "dataExportMediaCode", type = Accessor.Type.SETTER)
    public void setDataExportMediaCode(String value)
    {
        getPersistenceContext().setPropertyValue("dataExportMediaCode", value);
    }


    @Accessor(qualifier = "dataExportTarget", type = Accessor.Type.SETTER)
    public void setDataExportTarget(ImpExExportMediaModel value)
    {
        getPersistenceContext().setPropertyValue("dataExportTarget", value);
    }


    @Accessor(qualifier = "encoding", type = Accessor.Type.SETTER)
    public void setEncoding(EncodingEnum value)
    {
        getPersistenceContext().setPropertyValue("encoding", value);
    }


    @Accessor(qualifier = "export", type = Accessor.Type.SETTER)
    public void setExport(ExportModel value)
    {
        getPersistenceContext().setPropertyValue("export", value);
    }


    @Accessor(qualifier = "exportTemplate", type = Accessor.Type.SETTER)
    public void setExportTemplate(HeaderLibraryModel value)
    {
        getPersistenceContext().setPropertyValue("exportTemplate", value);
    }


    @Accessor(qualifier = "fieldSeparator", type = Accessor.Type.SETTER)
    public void setFieldSeparator(Character value)
    {
        getPersistenceContext().setPropertyValue("fieldSeparator", value);
    }


    @Accessor(qualifier = "jobMedia", type = Accessor.Type.SETTER)
    public void setJobMedia(ImpExMediaModel value)
    {
        getPersistenceContext().setPropertyValue("jobMedia", value);
    }


    @Accessor(qualifier = "mediasExportMediaCode", type = Accessor.Type.SETTER)
    public void setMediasExportMediaCode(String value)
    {
        getPersistenceContext().setPropertyValue("mediasExportMediaCode", value);
    }


    @Accessor(qualifier = "mediasExportTarget", type = Accessor.Type.SETTER)
    public void setMediasExportTarget(ImpExExportMediaModel value)
    {
        getPersistenceContext().setPropertyValue("mediasExportTarget", value);
    }


    @Accessor(qualifier = "mode", type = Accessor.Type.SETTER)
    public void setMode(ImpExValidationModeEnum value)
    {
        getPersistenceContext().setPropertyValue("mode", value);
    }


    @Accessor(qualifier = "quoteCharacter", type = Accessor.Type.SETTER)
    public void setQuoteCharacter(Character value)
    {
        getPersistenceContext().setPropertyValue("quoteCharacter", value);
    }


    @Accessor(qualifier = "report", type = Accessor.Type.SETTER)
    public void setReport(ReportModel value)
    {
        getPersistenceContext().setPropertyValue("report", value);
    }


    @Accessor(qualifier = "singleFile", type = Accessor.Type.SETTER)
    public void setSingleFile(Boolean value)
    {
        getPersistenceContext().setPropertyValue("singleFile", value);
    }
}
