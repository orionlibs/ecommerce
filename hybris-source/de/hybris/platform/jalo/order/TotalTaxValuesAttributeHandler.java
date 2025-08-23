package de.hybris.platform.jalo.order;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.TaxValue;
import java.util.Collection;

public class TotalTaxValuesAttributeHandler implements DynamicAttributeHandler<Collection<TaxValue>, AbstractOrderModel>
{
    public Collection<TaxValue> get(AbstractOrderModel model)
    {
        String values = model.getTotalTaxValuesInternal();
        return TaxValue.parseTaxValueCollection(values);
    }


    public void set(AbstractOrderModel model, Collection<TaxValue> discountValues)
    {
        String convertedValues = TaxValue.toString(discountValues);
        model.setTotalTaxValuesInternal(convertedValues);
    }
}
