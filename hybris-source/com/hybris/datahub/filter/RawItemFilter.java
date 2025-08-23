package com.hybris.datahub.filter;

import com.hybris.datahub.domain.RawItemStatusType;
import com.hybris.datahub.runtime.domain.DataLoadingAction;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Immutable
public class RawItemFilter extends DataItemFilter<RawItemStatusType>
{
    private DataLoadingAction dataLoadingAction;


    private RawItemFilter()
    {
        super(null);
    }


    public static Builder filter()
    {
        return new Builder();
    }


    public DataLoadingAction getDataLoadingAction()
    {
        return this.dataLoadingAction;
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
        RawItemFilter that = (RawItemFilter)o;
        return (new EqualsBuilder())
                        .appendSuper(super.equals(o))
                        .append(this.dataLoadingAction, that.dataLoadingAction)
                        .isEquals();
    }


    public int hashCode()
    {
        return (new HashCodeBuilder(17, 37))
                        .appendSuper(super.hashCode())
                        .append(this.dataLoadingAction)
                        .toHashCode();
    }
}
