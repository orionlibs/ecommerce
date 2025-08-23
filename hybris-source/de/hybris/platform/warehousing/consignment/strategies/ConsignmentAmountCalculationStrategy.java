package de.hybris.platform.warehousing.consignment.strategies;

import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import java.math.BigDecimal;

public interface ConsignmentAmountCalculationStrategy
{
    BigDecimal calculateCaptureAmount(ConsignmentModel paramConsignmentModel);


    BigDecimal calculateAlreadyCapturedAmount(ConsignmentModel paramConsignmentModel);


    BigDecimal calculateTotalOrderAmount(ConsignmentModel paramConsignmentModel);


    BigDecimal calculateDiscountAmount(ConsignmentModel paramConsignmentModel);


    BigDecimal calculateConsignmentEntryAmount(ConsignmentEntryModel paramConsignmentEntryModel, boolean paramBoolean);
}
