package de.hybris.platform.personalizationfacades.trigger.converters.populator;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.data.ExpressionTriggerData;
import de.hybris.platform.personalizationservices.model.CxExpressionTriggerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.springframework.util.Assert;

public class ExpressionTriggerReversePopulator extends ExpressionTriggerBasePopulator implements Populator<ExpressionTriggerData, CxExpressionTriggerModel>
{
    public void populate(ExpressionTriggerData source, CxExpressionTriggerModel target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        try
        {
            target.setExpression(getWriter().writeValueAsString(source.getExpression()));
        }
        catch(JsonProcessingException e)
        {
            throw new ConversionException("Failed to write json expression", e);
        }
    }
}
