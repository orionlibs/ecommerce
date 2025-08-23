package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.warehousing.model.AllocationEventModel;

public class CancellationEventDaoImpl extends AbstractWarehousingDao<AllocationEventModel>
{
    protected String getQuery()
    {
        return "SELECT {ca.pk} FROM {CancellationEvent as ca JOIN ConsignmentEntry as e ON {ca.consignmentEntry} = {e.pk} JOIN Consignment as c ON {e.consignment} = {c.pk}} WHERE {c.code}=?code";
    }
}
