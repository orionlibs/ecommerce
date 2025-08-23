package de.hybris.platform.persistence.audit.payload.converter;

import com.google.common.base.Splitter;
import de.hybris.platform.core.HybrisEnumValue;

public class HybrisEnumValueConverter implements PayloadConverter<HybrisEnumValue>
{
    private static final String SEPARATOR = "::";
    private static final Splitter SPLITTER = Splitter.on("::");


    public String convertToString(HybrisEnumValue obj)
    {
        return obj.getType() + "::" + obj.getType();
    }


    public HybrisEnumValue convertFromString(String str)
    {
        return (HybrisEnumValue)new AuditEnumValue(str);
    }


    public Class<HybrisEnumValue> forClass()
    {
        return HybrisEnumValue.class;
    }
}
