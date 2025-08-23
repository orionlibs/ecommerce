package de.hybris.platform.core.model.audit;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.auditreport.model.AuditReportDataModel;
import de.hybris.platform.core.model.AbstractDynamicContentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class AuditReportConfigModel extends AbstractDynamicContentModel
{
    public static final String _TYPECODE = "AuditReportConfig";
    public static final String _AUDITREPORTDATA2AUDITREPORTCONFIGRELATION = "AuditReportData2AuditReportConfigRelation";
    public static final String AUDITREPORTDATA = "auditReportData";


    public AuditReportConfigModel()
    {
    }


    public AuditReportConfigModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AuditReportConfigModel(String _code, String _content)
    {
        setCode(_code);
        setContent(_content);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AuditReportConfigModel(String _code, String _content, ItemModel _owner)
    {
        setCode(_code);
        setContent(_content);
        setOwner(_owner);
    }


    @Accessor(qualifier = "auditReportData", type = Accessor.Type.GETTER)
    public Collection<AuditReportDataModel> getAuditReportData()
    {
        return (Collection<AuditReportDataModel>)getPersistenceContext().getPropertyValue("auditReportData");
    }


    @Accessor(qualifier = "auditReportData", type = Accessor.Type.SETTER)
    public void setAuditReportData(Collection<AuditReportDataModel> value)
    {
        getPersistenceContext().setPropertyValue("auditReportData", value);
    }
}
