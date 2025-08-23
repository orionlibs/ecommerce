package de.hybris.platform.auditreport.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CreateAuditReportJobModel extends ServicelayerJobModel
{
    public static final String _TYPECODE = "CreateAuditReportJob";


    public CreateAuditReportJobModel()
    {
    }


    public CreateAuditReportJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CreateAuditReportJobModel(String _code, String _springId)
    {
        setCode(_code);
        setSpringId(_springId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CreateAuditReportJobModel(String _code, Integer _nodeID, ItemModel _owner, String _springId)
    {
        setCode(_code);
        setNodeID(_nodeID);
        setOwner(_owner);
        setSpringId(_springId);
    }
}
