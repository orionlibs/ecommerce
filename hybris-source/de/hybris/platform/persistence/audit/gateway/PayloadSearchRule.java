package de.hybris.platform.persistence.audit.gateway;

import org.apache.commons.lang.builder.ToStringBuilder;

public class PayloadSearchRule<T> extends GenericSearchRule<T>
{
    public PayloadSearchRule(String fieldName, T value)
    {
        super(fieldName, value, true);
    }


    public String toString()
    {
        return (new ToStringBuilder(this))
                        .append("fieldName", getFieldName())
                        .append("value", getValue())
                        .append("isForPayload", isForPayload())
                        .toString();
    }
}
