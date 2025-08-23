package de.hybris.platform.servicelayer.search;

import de.hybris.platform.jalo.flexiblesearch.hints.Hint;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlexibleSearchQuery extends AbstractQuery
{
    private final String query;
    private final Map<String, Object> queryParameters = new HashMap<>();
    private final List<Hint> hints = new ArrayList<>();


    public FlexibleSearchQuery(String query)
    {
        this(query, null);
    }


    public FlexibleSearchQuery(StringBuilder sb)
    {
        this(sb.toString(), null);
    }


    public FlexibleSearchQuery(String query, Map<? extends String, ?> queryParams)
    {
        ServicesUtil.validateParameterNotNull(query, "No valid query (null)");
        this.query = query;
        if(queryParams != null)
        {
            this.queryParameters.putAll(queryParams);
        }
    }


    public void addHints(Hint... hints)
    {
        ServicesUtil.validateParameterNotNull(hints, "hints can't be null");
        if(hints.length > 0)
        {
            this.hints.addAll(Arrays.asList(hints));
        }
    }


    public void addHints(List<? extends Hint> hints)
    {
        ServicesUtil.validateParameterNotNull(hints, "hints can't be null");
        this.hints.addAll(hints);
    }


    public List<Hint> getHints()
    {
        return Collections.unmodifiableList(this.hints);
    }


    public void addQueryParameter(String key, Object value)
    {
        ServicesUtil.validateParameterNotNull(value, "Value is required, null given for key: " + key);
        if(value instanceof Collection && ((Collection)value).isEmpty())
        {
            throw new IllegalArgumentException("Value is instanceof Collection but cannot be empty collection");
        }
        this.queryParameters.put(key, value);
    }


    public void addQueryParameters(Map<String, ? extends Object> params)
    {
        for(Map.Entry<String, ? extends Object> entry : params.entrySet())
        {
            ServicesUtil.validateParameterNotNull(entry.getValue(), "Value is required, null given for key: " + (String)entry.getKey());
            if(entry.getValue() instanceof Collection && ((Collection)entry.getValue()).isEmpty())
            {
                throw new IllegalArgumentException("Value is instanceof Collection but cannot be empty collection for key: " + (String)entry
                                .getKey());
            }
        }
        this.queryParameters.putAll(params);
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(!(obj instanceof FlexibleSearchQuery))
        {
            return false;
        }
        FlexibleSearchQuery fsq = (FlexibleSearchQuery)obj;
        return (this.query.equals(fsq.query) &&
                        getResultClassList().equals(fsq.getResultClassList()) && this.queryParameters
                        .equals(fsq.queryParameters) &&
                        getCount() == fsq.getCount() &&
                        getStart() == fsq.getStart() &&
                        isNeedTotal() == fsq.isNeedTotal() &&
                        isFailOnUnknownFields() == fsq.isFailOnUnknownFields() && (
                        (getUser() == null) ? (fsq.getUser() == null) : getUser().equals(fsq.getUser())) && (
                        (getLanguage() == null) ? (fsq.getLanguage() == null) : getLanguage().equals(fsq.getLanguage())) && (
                        (getCatalogVersions() == null) ? (fsq.getCatalogVersions() == null) : getCatalogVersions().equals(fsq
                                        .getCatalogVersions())));
    }


    public String getQuery()
    {
        return this.query;
    }


    public Map<String, Object> getQueryParameters()
    {
        return this.queryParameters;
    }


    public int hashCode()
    {
        return this.query.hashCode();
    }


    public String toString()
    {
        return "query: [" + this.query + "], query parameters: [" + this.queryParameters + "]";
    }
}
