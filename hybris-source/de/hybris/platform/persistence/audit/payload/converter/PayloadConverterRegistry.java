package de.hybris.platform.persistence.audit.payload.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

public class PayloadConverterRegistry
{
    @Autowired
    private List<PayloadConverter> payloadConverters;
    private final Map<String, PayloadConverter> converterMapping = new HashMap<>();


    @PostConstruct
    private void doInit()
    {
        for(PayloadConverter converter : this.payloadConverters)
        {
            this.converterMapping.put(converter.forClass().getName(), converter);
        }
    }


    public String convertToString(Object obj, Class clazz)
    {
        if(clazz == null)
        {
            return obj.toString();
        }
        PayloadConverter converter = this.converterMapping.get(clazz.getName());
        return (converter == null) ? obj.toString() : converter.convertToString(obj);
    }


    public Object convertToObject(String str, Class clazz)
    {
        if(clazz == null)
        {
            return ((PayloadConverter)this.converterMapping.get(Object.class.getName())).convertFromString(str);
        }
        PayloadConverter converter = this.converterMapping.get(clazz.getName());
        if(converter == null)
        {
            converter = this.converterMapping.get(Object.class.getName());
        }
        if(converter instanceof HybrisEnumValueConverter)
        {
            return converter.convertFromString(str).toString();
        }
        return converter.convertFromString(str);
    }
}
