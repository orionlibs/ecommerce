package de.hybris.platform.audit.view.impl;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ReportView
{
    private final Map<String, Object> payload;
    private final Date reportTime;
    private final String changingUser;
    private final Map<String, Object> context;
    private final ReportViewType reportViewType;


    private ReportView(Builder builder)
    {
        this.payload = builder.payload;
        this.reportTime = builder.reportTime;
        this.changingUser = builder.changingUser;
        this.context = builder.context;
        this.reportViewType = builder.reportViewType;
    }


    public String getChangingUser()
    {
        return this.changingUser;
    }


    public Date getTimestamp()
    {
        return this.reportTime;
    }


    public Map<String, Object> getPayload()
    {
        return (Map<String, Object>)ImmutableMap.copyOf(this.payload);
    }


    public Map<String, Object> getContext()
    {
        return (this.context == null) ? Collections.<String, Object>emptyMap() : (Map<String, Object>)ImmutableMap.copyOf(this.context);
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof ReportView))
        {
            return false;
        }
        ReportView that = (ReportView)o;
        return (Objects.equals(getPayload(), that.getPayload()) && Objects.equals(getTimestamp(), that.getTimestamp()));
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {getPayload()});
    }


    public String toString()
    {
        String timeFieldName = ReportViewType.STANDARD.equals(this.reportViewType) ? "creationTime" : "deletionTime";
        return (new ToStringBuilder(this)).append("payload", this.payload).append(timeFieldName, this.reportTime.getTime())
                        .append("changingUser", this.changingUser).append("context", this.context).toString();
    }


    public static Builder builder(Map<String, Object> payload, Date creationTime, String changingUser)
    {
        return new Builder(payload, creationTime, changingUser);
    }
}
