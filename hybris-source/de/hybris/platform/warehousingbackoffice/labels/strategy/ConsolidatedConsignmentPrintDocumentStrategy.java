package de.hybris.platform.warehousingbackoffice.labels.strategy;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import java.util.List;

public interface ConsolidatedConsignmentPrintDocumentStrategy
{
    void printDocument(List<ConsignmentModel> paramList);
}
