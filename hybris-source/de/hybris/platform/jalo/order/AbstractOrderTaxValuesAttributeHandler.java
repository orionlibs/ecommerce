package de.hybris.platform.jalo.order;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.TaxValue;
import java.util.Collection;

public class AbstractOrderTaxValuesAttributeHandler implements DynamicAttributeHandler<Collection<TaxValue>, AbstractOrderEntryModel>
{
    public Collection<TaxValue> get(AbstractOrderEntryModel model)
    {
        String values = model.getTaxValuesInternal();
        return TaxValue.parseTaxValueCollection(values);
    }


    public void set(AbstractOrderEntryModel model, Collection<TaxValue> discountValues)
    {
        String convertedValues = TaxValue.toString(discountValues);
        model.setTaxValuesInternal(convertedValues);
        model.setCalculated(Boolean.FALSE);
        if(model.getOrder() != null)
        {
            model.getOrder().setCalculated(Boolean.FALSE);
        }
    }
}
