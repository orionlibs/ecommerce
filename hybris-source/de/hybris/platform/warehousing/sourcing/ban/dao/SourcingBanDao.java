package de.hybris.platform.warehousing.sourcing.ban.dao;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.model.SourcingBanModel;
import java.util.Collection;
import java.util.Date;

public interface SourcingBanDao
{
    Collection<SourcingBanModel> getSourcingBan(Collection<WarehouseModel> paramCollection, Date paramDate);
}
