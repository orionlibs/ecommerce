package de.hybris.platform.warehousing.returns.service;

import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import java.math.BigDecimal;

public interface RefundAmountCalculationService
{
    BigDecimal getCustomRefundAmount(ReturnRequestModel paramReturnRequestModel);


    BigDecimal getCustomRefundEntryAmount(ReturnEntryModel paramReturnEntryModel);


    BigDecimal getOriginalRefundAmount(ReturnRequestModel paramReturnRequestModel);


    BigDecimal getOriginalRefundEntryAmount(ReturnEntryModel paramReturnEntryModel);
}
