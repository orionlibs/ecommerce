package de.hybris.platform.europe1.jalo;

import de.hybris.platform.europe1.constants.Europe1Tools;
import de.hybris.platform.jalo.order.price.DiscountInformation;
import java.util.Collections;

public class Europe1DiscountInformation extends DiscountInformation
{
    private final AbstractDiscountRow src;


    public Europe1DiscountInformation(AbstractDiscountRow discountRow)
    {
        super(Collections.EMPTY_MAP, Europe1Tools.createDiscountValue(discountRow));
        this.src = discountRow;
    }


    public AbstractDiscountRow getDiscountRow()
    {
        return this.src;
    }
}
