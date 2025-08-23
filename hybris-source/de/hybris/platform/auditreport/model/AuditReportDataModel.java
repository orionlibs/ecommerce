package de.hybris.platform.auditreport.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.audit.AuditReportConfigModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AuditReportDataModel extends CatalogUnawareMediaModel
{
    public static final String _TYPECODE = "AuditReportData";
    public static final String AUDITROOTITEM = "auditRootItem";
    public static final String AUDITREPORTCONFIG = "auditReportConfig";


    public AuditReportDataModel()
    {
    }


    public AuditReportDataModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AuditReportDataModel(ItemModel _auditRootItem, CatalogVersionModel _catalogVersion, String _code)
    {
        setAuditRootItem(_auditRootItem);
        setCatalogVersion(_catalogVersion);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AuditReportDataModel(ItemModel _auditRootItem, CatalogVersionModel _catalogVersion, String _code, ItemModel _owner)
    {
        setAuditRootItem(_auditRootItem);
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "auditReportConfig", type = Accessor.Type.GETTER)
    public AuditReportConfigModel getAuditReportConfig()
    {
        return (AuditReportConfigModel)getPersistenceContext().getPropertyValue("auditReportConfig");
    }


    @Accessor(qualifier = "auditRootItem", type = Accessor.Type.GETTER)
    public ItemModel getAuditRootItem()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("auditRootItem");
    }


    @Accessor(qualifier = "auditReportConfig", type = Accessor.Type.SETTER)
    public void setAuditReportConfig(AuditReportConfigModel value)
    {
        getPersistenceContext().setPropertyValue("auditReportConfig", value);
    }


    @Accessor(qualifier = "auditRootItem", type = Accessor.Type.SETTER)
    public void setAuditRootItem(ItemModel value)
    {
        getPersistenceContext().setPropertyValue("auditRootItem", value);
    }
}
