package com.hybris.datahub.filter;

import com.hybris.datahub.domain.CompositionStatusType;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Immutable
public class CanonicalItemFilter extends DataItemFilter<CompositionStatusType>
{
    private String integrationKey;


    private CanonicalItemFilter()
    {
        super(null);
    }


    public static Builder filter()
    {
        return new Builder();
    }


    public String getIntegrationKey()
    {
        return this.integrationKey;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        CanonicalItemFilter that = (CanonicalItemFilter)o;
        return (new EqualsBuilder())
                        .appendSuper(super.equals(o))
                        .append(this.integrationKey, that.integrationKey)
                        .isEquals();
    }


    public int hashCode()
    {
        return (new HashCodeBuilder(17, 37))
                        .appendSuper(super.hashCode())
                        .append(this.integrationKey)
                        .toHashCode();
    }
}
