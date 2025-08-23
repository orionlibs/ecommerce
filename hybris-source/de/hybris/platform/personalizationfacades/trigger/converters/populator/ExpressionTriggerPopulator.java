package de.hybris.platform.personalizationfacades.trigger.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.data.ExpressionData;
import de.hybris.platform.personalizationfacades.data.ExpressionTriggerData;
import de.hybris.platform.personalizationservices.model.CxExpressionTriggerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import java.io.IOException;
import org.springframework.util.Assert;

public class ExpressionTriggerPopulator extends ExpressionTriggerBasePopulator implements Populator<CxExpressionTriggerModel, ExpressionTriggerData>
{
    public void populate(CxExpressionTriggerModel source, ExpressionTriggerData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        try
        {
            target.setExpression((ExpressionData)getReader().readValue(source.getExpression()));
        }
        catch(IOException e)
        {
            throw new ConversionException("Failed to read json expression", e);
        }
    }
}
