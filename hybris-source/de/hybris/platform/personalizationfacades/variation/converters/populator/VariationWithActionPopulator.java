package de.hybris.platform.personalizationfacades.variation.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.data.ActionData;
import de.hybris.platform.personalizationfacades.data.VariationData;
import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.List;
import org.springframework.util.Assert;

public class VariationWithActionPopulator implements Populator<CxVariationModel, VariationData>
{
    private Converter<CxAbstractActionModel, ActionData> actionConverter;


    public void populate(CxVariationModel source, VariationData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setActions(convertActions(source));
    }


    protected List<ActionData> convertActions(CxVariationModel source)
    {
        return this.actionConverter.convertAll(source.getActions());
    }


    protected Converter<CxAbstractActionModel, ActionData> getActionConverter()
    {
        return this.actionConverter;
    }


    public void setActionConverter(Converter<CxAbstractActionModel, ActionData> actionConverter)
    {
        this.actionConverter = actionConverter;
    }
}
