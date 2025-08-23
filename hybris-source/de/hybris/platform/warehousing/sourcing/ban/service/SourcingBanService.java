package de.hybris.platform.warehousing.sourcing.ban.service;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.model.SourcingBanModel;
import java.util.Collection;

public interface SourcingBanService
{
    SourcingBanModel createSourcingBan(WarehouseModel paramWarehouseModel);


    Collection<SourcingBanModel> getSourcingBan(Collection<WarehouseModel> paramCollection);
}
