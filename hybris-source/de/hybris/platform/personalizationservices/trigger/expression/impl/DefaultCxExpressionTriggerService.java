package de.hybris.platform.personalizationservices.trigger.expression.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import de.hybris.platform.personalizationservices.model.CxExpressionTriggerModel;
import de.hybris.platform.personalizationservices.trigger.expression.CxExpression;
import de.hybris.platform.personalizationservices.trigger.expression.CxExpressionTriggerService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import java.io.IOException;

public class DefaultCxExpressionTriggerService implements CxExpressionTriggerService
{
    private final ObjectWriter writer;
    private final ObjectReader reader;


    public DefaultCxExpressionTriggerService()
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy((PropertyNamingStrategy)new PropertyNamingStrategies.LowerCaseStrategy());
        mapper.addMixIn(CxExpression.class, CxExpressionMixin.class);
        this.writer = mapper.writer().forType(CxExpression.class);
        this.reader = mapper.reader().forType(CxExpression.class);
    }


    public CxExpression extractExpression(CxExpressionTriggerModel trigger)
    {
        try
        {
            return (CxExpression)this.reader.readValue(trigger.getExpression());
        }
        catch(IOException e)
        {
            throw new ConversionException("Invalid expression", e);
        }
    }


    public void saveExpression(CxExpressionTriggerModel trigger, CxExpression expression)
    {
        try
        {
            String json = this.writer.writeValueAsString(expression);
            trigger.setExpression(json);
        }
        catch(JsonProcessingException e)
        {
            throw new ConversionException("Invalid expression", e);
        }
    }
}
