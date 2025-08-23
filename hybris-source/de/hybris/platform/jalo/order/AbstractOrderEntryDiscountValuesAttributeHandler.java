package de.hybris.platform.jalo.order;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.DiscountValue;
import java.util.Collections;
import java.util.List;

public class AbstractOrderEntryDiscountValuesAttributeHandler implements DynamicAttributeHandler<List<DiscountValue>, AbstractOrderEntryModel>
{
    public List<DiscountValue> get(AbstractOrderEntryModel model)
    {
        String values = model.getDiscountValuesInternal();
        List<DiscountValue> l = (List)DiscountValue.parseDiscountValueCollection(values);
        return (l != null) ? l : Collections.EMPTY_LIST;
    }


    public void set(AbstractOrderEntryModel model, List<DiscountValue> discountValues)
    {
        String newOne = DiscountValue.toString(discountValues);
        model.setDiscountValuesInternal(newOne);
        model.setCalculated(Boolean.FALSE);
        if(model.getOrder() != null)
        {
            model.getOrder().setCalculated(Boolean.FALSE);
        }
    }
}
