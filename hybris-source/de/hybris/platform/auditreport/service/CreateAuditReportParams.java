package de.hybris.platform.auditreport.service;

import de.hybris.platform.auditreport.model.AuditReportTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.Collection;

public class CreateAuditReportParams
{
    private final ItemModel rootItem;
    private final String configName;
    private final String reportId;
    private final boolean audit;
    private final Collection<LanguageModel> includedLanguages;
    private final AuditReportTemplateModel template;


    public CreateAuditReportParams(ItemModel rootItem, String configName, String reportId, boolean audit, Collection<LanguageModel> includedLanguages, AuditReportTemplateModel template)
    {
        this.rootItem = rootItem;
        this.configName = configName;
        this.reportId = reportId;
        this.audit = audit;
        this.includedLanguages = includedLanguages;
        this.template = template;
    }


    public ItemModel getRootItem()
    {
        return this.rootItem;
    }


    public String getConfigName()
    {
        return this.configName;
    }


    public String getReportId()
    {
        return this.reportId;
    }


    public boolean isAudit()
    {
        return this.audit;
    }


    public Collection<LanguageModel> getIncludedLanguages()
    {
        return this.includedLanguages;
    }


    public AuditReportTemplateModel getTemplate()
    {
        return this.template;
    }
}
