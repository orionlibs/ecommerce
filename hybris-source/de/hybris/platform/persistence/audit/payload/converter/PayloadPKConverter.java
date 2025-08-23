package de.hybris.platform.persistence.audit.payload.converter;

import de.hybris.platform.core.PK;

public class PayloadPKConverter implements PayloadConverter<PK>
{
    public String convertToString(PK pk)
    {
        return pk.getLongValueAsString();
    }


    public PK convertFromString(String pk)
    {
        return PK.parse(pk);
    }


    public Class<PK> forClass()
    {
        return PK.class;
    }
}
