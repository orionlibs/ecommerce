package de.hybris.platform.auditreport.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commons.enums.RendererTypeEnum;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AuditReportTemplateModel extends RendererTemplateModel
{
    public static final String _TYPECODE = "AuditReportTemplate";
    public static final String INCLUDETEXT = "includeText";


    public AuditReportTemplateModel()
    {
    }


    public AuditReportTemplateModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AuditReportTemplateModel(String _code, Boolean _includeText, RendererTypeEnum _rendererType)
    {
        setCode(_code);
        setIncludeText(_includeText);
        setRendererType(_rendererType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AuditReportTemplateModel(String _code, Boolean _includeText, ItemModel _owner, RendererTypeEnum _rendererType)
    {
        setCode(_code);
        setIncludeText(_includeText);
        setOwner(_owner);
        setRendererType(_rendererType);
    }


    @Accessor(qualifier = "includeText", type = Accessor.Type.GETTER)
    public Boolean getIncludeText()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("includeText");
    }


    @Accessor(qualifier = "includeText", type = Accessor.Type.SETTER)
    public void setIncludeText(Boolean value)
    {
        getPersistenceContext().setPropertyValue("includeText", value);
    }
}
