package de.hybris.platform.externaltax;

import de.hybris.platform.core.model.order.AbstractOrderModel;

public interface ApplyExternalTaxesStrategy
{
    void applyExternalTaxes(AbstractOrderModel paramAbstractOrderModel, ExternalTaxDocument paramExternalTaxDocument);
}
