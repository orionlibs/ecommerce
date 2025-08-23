package com.hybris.datahub.filter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Immutable
public abstract class DataItemFilter<STATUS_TYPE extends Enum>
{
    protected STATUS_TYPE[] statuses;
    protected Map<String, String> keyFields = new HashMap<>();


    public DataItemFilter(STATUS_TYPE[] statuses)
    {
        this.statuses = statuses;
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
        DataItemFilter that = (DataItemFilter)o;
        if(!Arrays.equals((Object[])this.statuses, (Object[])that.statuses))
        {
            return false;
        }
        if(!this.keyFields.equals(that.keyFields))
        {
            return false;
        }
        return true;
    }


    public STATUS_TYPE[] getStatuses()
    {
        return this.statuses;
    }


    public Map<String, String> getKeyFields()
    {
        return this.keyFields;
    }


    public void addKeyFields(Map<String, String> keyFields)
    {
        this.keyFields.putAll(keyFields);
    }


    public int hashCode()
    {
        return (new HashCodeBuilder(17, 37))
                        .append((Object[])this.statuses)
                        .append(this.keyFields)
                        .toHashCode();
    }
}
