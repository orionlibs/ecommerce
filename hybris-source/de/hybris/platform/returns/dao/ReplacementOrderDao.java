package de.hybris.platform.returns.dao;

import de.hybris.platform.returns.model.ReplacementOrderModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

public interface ReplacementOrderDao extends Dao
{
    ReplacementOrderModel getReplacementOrder(String paramString);


    ReplacementOrderModel createReplacementOrder(ReturnRequestModel paramReturnRequestModel);
}
