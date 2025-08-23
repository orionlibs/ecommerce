package de.hybris.platform.ruleengineservices.order.dao;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.daos.OrderDao;

public interface ExtendedOrderDao extends OrderDao
{
    AbstractOrderModel findOrderByCode(String paramString);
}
