package de.hybris.platform.warehousing.labels.strategy;

import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import java.io.Serializable;
import java.math.BigDecimal;

public interface ExportFormPriceStrategy extends Serializable
{
    BigDecimal calculateProductPrice(ConsignmentEntryModel paramConsignmentEntryModel);


    BigDecimal calculateTotalPrice(BigDecimal paramBigDecimal, ConsignmentEntryModel paramConsignmentEntryModel);
}
