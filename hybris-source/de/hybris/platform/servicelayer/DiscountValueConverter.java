package de.hybris.platform.servicelayer;

import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.europe1.model.AbstractDiscountRowModel;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.util.DiscountValue;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.BooleanUtils;

public class DiscountValueConverter
{
    public DiscountValue discountToValue(AbstractDiscountRowModel discountRow)
    {
        DiscountModel discount = discountRow.getDiscount();
        boolean rowHasValue = (discountRow.getValue() != null);
        boolean absolute = rowHasValue ? BooleanUtils.isTrue(discountRow.getAbsolute()) : BooleanUtils.isTrue(discount
                        .getAbsolute());
        boolean asTargetPrice = (rowHasValue && absolute && discountRow instanceof DiscountRowModel && Boolean.TRUE.equals(((DiscountRowModel)discountRow).getAsTargetPrice()));
        if(asTargetPrice)
        {
            return DiscountValue.createTargetPrice(discount.getCode(), discountRow.getValue(), discountRow
                            .getCurrency().getIsocode());
        }
        if(absolute)
        {
            return rowHasValue ?
                            DiscountValue.createAbsolute(discount.getCode(), discountRow.getValue(), discountRow
                                            .getCurrency().getIsocode()) :
                            DiscountValue.createAbsolute(discount.getCode(), discount.getValue(), discount.getCurrency().getIsocode());
        }
        return DiscountValue.createRelative(discount.getCode(), rowHasValue ? discountRow.getValue() : discount.getValue());
    }


    public List<DiscountValue> discountsToValues(List<AbstractDiscountRowModel> discountRows)
    {
        return (List<DiscountValue>)discountRows.stream().map(i -> discountToValue(i)).collect(Collectors.toList());
    }
}
