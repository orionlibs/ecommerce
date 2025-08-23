package de.hybris.platform.warehousing.labels.strategy.impl;

import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.labels.strategy.ExportFormPriceStrategy;
import java.math.BigDecimal;

public class DefaultExportFormPriceStrategy implements ExportFormPriceStrategy
{
    public BigDecimal calculateProductPrice(ConsignmentEntryModel consignmentEntry)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("consignmentEntry", consignmentEntry);
        return (consignmentEntry.getOrderEntry().getBasePrice() != null) ?
                        BigDecimal.valueOf(consignmentEntry.getOrderEntry().getBasePrice().doubleValue()) : BigDecimal.ZERO;
    }


    public BigDecimal calculateTotalPrice(BigDecimal productPrice, ConsignmentEntryModel consignmentEntry)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("consignmentEntry", consignmentEntry);
        return productPrice.multiply(BigDecimal.valueOf(consignmentEntry.getQuantity().longValue()));
    }
}
