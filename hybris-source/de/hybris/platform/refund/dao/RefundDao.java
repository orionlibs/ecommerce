package de.hybris.platform.refund.dao;

import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import java.util.List;

public interface RefundDao
{
    List<RefundEntryModel> getRefunds(ReturnRequestModel paramReturnRequestModel);
}
