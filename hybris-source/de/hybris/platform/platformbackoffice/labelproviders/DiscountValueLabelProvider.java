package de.hybris.platform.platformbackoffice.labelproviders;

import com.hybris.cockpitng.labels.LabelProvider;
import de.hybris.platform.platformbackoffice.taxdiscountvalueparser.ValueParser;
import de.hybris.platform.util.DiscountValue;
import org.springframework.beans.factory.annotation.Required;

public class DiscountValueLabelProvider implements LabelProvider<DiscountValue>
{
    private ValueParser<DiscountValue> discountValueRenderer;


    public String getLabel(DiscountValue discountValue)
    {
        if(discountValue == null)
        {
            return "";
        }
        return this.discountValueRenderer.render(discountValue);
    }


    public String getDescription(DiscountValue discountValue)
    {
        return getLabel(discountValue);
    }


    public String getIconPath(DiscountValue discountValue)
    {
        return null;
    }


    @Required
    public void setDiscountValueRenderer(ValueParser<DiscountValue> discountValueRenderer)
    {
        this.discountValueRenderer = discountValueRenderer;
    }
}
