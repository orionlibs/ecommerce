package de.hybris.platform.personalizationfacades.trigger.converters.populator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import de.hybris.platform.personalizationfacades.data.ExpressionData;

public class ExpressionTriggerBasePopulator
{
    private final ObjectWriter writer;
    private final ObjectReader reader;


    public ExpressionTriggerBasePopulator()
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy((PropertyNamingStrategy)new PropertyNamingStrategies.LowerCaseStrategy());
        mapper.addMixIn(ExpressionData.class, ExpressionMixin.class);
        this.writer = mapper.writer().forType(ExpressionData.class);
        this.reader = mapper.reader().forType(ExpressionData.class);
    }


    public ObjectReader getReader()
    {
        return this.reader;
    }


    public ObjectWriter getWriter()
    {
        return this.writer;
    }
}
