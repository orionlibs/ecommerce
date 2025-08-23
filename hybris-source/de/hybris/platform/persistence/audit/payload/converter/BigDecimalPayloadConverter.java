package de.hybris.platform.persistence.audit.payload.converter;

import java.math.BigDecimal;

public class BigDecimalPayloadConverter implements PayloadConverter<BigDecimal>
{
    public String convertToString(BigDecimal obj)
    {
        return obj.toPlainString();
    }


    public BigDecimal convertFromString(String str)
    {
        return new BigDecimal(str);
    }


    public Class<BigDecimal> forClass()
    {
        return BigDecimal.class;
    }
}
