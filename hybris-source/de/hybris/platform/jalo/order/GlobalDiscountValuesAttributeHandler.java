package de.hybris.platform.jalo.order;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.DiscountValue;
import java.util.Collections;
import java.util.List;

public class GlobalDiscountValuesAttributeHandler implements DynamicAttributeHandler<List<DiscountValue>, AbstractOrderModel>
{
    public List<DiscountValue> get(AbstractOrderModel model)
    {
        String values = model.getGlobalDiscountValuesInternal();
        List<DiscountValue> l = (List)DiscountValue.parseDiscountValueCollection(values);
        return (l != null) ? l : Collections.EMPTY_LIST;
    }


    public void set(AbstractOrderModel model, List<DiscountValue> discountValues)
    {
        String newOne = DiscountValue.toString(discountValues);
        model.setGlobalDiscountValuesInternal(newOne);
        model.setCalculated(Boolean.FALSE);
    }
}
