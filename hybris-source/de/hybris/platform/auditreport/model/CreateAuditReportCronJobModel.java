package de.hybris.platform.auditreport.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class CreateAuditReportCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "CreateAuditReportCronJob";
    public static final String ROOTITEM = "rootItem";
    public static final String CONFIGNAME = "configName";
    public static final String REPORTID = "reportId";
    public static final String AUDIT = "audit";
    public static final String INCLUDEDLANGUAGES = "includedLanguages";
    public static final String AUDITREPORTTEMPLATE = "auditReportTemplate";


    public CreateAuditReportCronJobModel()
    {
    }


    public CreateAuditReportCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CreateAuditReportCronJobModel(AuditReportTemplateModel _auditReportTemplate, String _configName, JobModel _job, String _reportId, ItemModel _rootItem)
    {
        setAuditReportTemplate(_auditReportTemplate);
        setConfigName(_configName);
        setJob(_job);
        setReportId(_reportId);
        setRootItem(_rootItem);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CreateAuditReportCronJobModel(AuditReportTemplateModel _auditReportTemplate, String _configName, JobModel _job, ItemModel _owner, String _reportId, ItemModel _rootItem)
    {
        setAuditReportTemplate(_auditReportTemplate);
        setConfigName(_configName);
        setJob(_job);
        setOwner(_owner);
        setReportId(_reportId);
        setRootItem(_rootItem);
    }


    @Accessor(qualifier = "audit", type = Accessor.Type.GETTER)
    public Boolean getAudit()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("audit");
    }


    @Accessor(qualifier = "auditReportTemplate", type = Accessor.Type.GETTER)
    public AuditReportTemplateModel getAuditReportTemplate()
    {
        return (AuditReportTemplateModel)getPersistenceContext().getPropertyValue("auditReportTemplate");
    }


    @Accessor(qualifier = "configName", type = Accessor.Type.GETTER)
    public String getConfigName()
    {
        return (String)getPersistenceContext().getPropertyValue("configName");
    }


    @Accessor(qualifier = "includedLanguages", type = Accessor.Type.GETTER)
    public Collection<LanguageModel> getIncludedLanguages()
    {
        return (Collection<LanguageModel>)getPersistenceContext().getPropertyValue("includedLanguages");
    }


    @Accessor(qualifier = "reportId", type = Accessor.Type.GETTER)
    public String getReportId()
    {
        return (String)getPersistenceContext().getPropertyValue("reportId");
    }


    @Accessor(qualifier = "rootItem", type = Accessor.Type.GETTER)
    public ItemModel getRootItem()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("rootItem");
    }


    @Accessor(qualifier = "audit", type = Accessor.Type.SETTER)
    public void setAudit(Boolean value)
    {
        getPersistenceContext().setPropertyValue("audit", value);
    }


    @Accessor(qualifier = "auditReportTemplate", type = Accessor.Type.SETTER)
    public void setAuditReportTemplate(AuditReportTemplateModel value)
    {
        getPersistenceContext().setPropertyValue("auditReportTemplate", value);
    }


    @Accessor(qualifier = "configName", type = Accessor.Type.SETTER)
    public void setConfigName(String value)
    {
        getPersistenceContext().setPropertyValue("configName", value);
    }


    @Accessor(qualifier = "includedLanguages", type = Accessor.Type.SETTER)
    public void setIncludedLanguages(Collection<LanguageModel> value)
    {
        getPersistenceContext().setPropertyValue("includedLanguages", value);
    }


    @Accessor(qualifier = "reportId", type = Accessor.Type.SETTER)
    public void setReportId(String value)
    {
        getPersistenceContext().setPropertyValue("reportId", value);
    }


    @Accessor(qualifier = "rootItem", type = Accessor.Type.SETTER)
    public void setRootItem(ItemModel value)
    {
        getPersistenceContext().setPropertyValue("rootItem", value);
    }
}
