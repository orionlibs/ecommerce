package de.hybris.platform.platformbackoffice.labelproviders;

import com.hybris.cockpitng.labels.LabelProvider;
import de.hybris.platform.platformbackoffice.taxdiscountvalueparser.ValueParser;
import de.hybris.platform.util.TaxValue;
import org.springframework.beans.factory.annotation.Required;

public class TaxValueLabelProvider implements LabelProvider<TaxValue>
{
    private ValueParser<TaxValue> taxValueRenderer;


    public String getLabel(TaxValue taxValue)
    {
        if(taxValue == null)
        {
            return "";
        }
        return this.taxValueRenderer.render(taxValue);
    }


    public String getDescription(TaxValue taxValue)
    {
        return getLabel(taxValue);
    }


    public String getIconPath(TaxValue taxValue)
    {
        return null;
    }


    @Required
    public void setTaxValueRenderer(ValueParser<TaxValue> taxValueRenderer)
    {
        this.taxValueRenderer = taxValueRenderer;
    }
}
