package de.hybris.platform.warehousing.labels.strategy;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public interface PrintExportFormStrategy
{
    boolean canPrintExportForm(ConsignmentModel paramConsignmentModel);
}
