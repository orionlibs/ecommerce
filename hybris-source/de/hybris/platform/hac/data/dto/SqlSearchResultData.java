package de.hybris.platform.hac.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Joiner;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class SqlSearchResultData
{
    private String query;
    private long executionTime;
    private Collection<CatalogVersionModel> catalogVersions;
    private Collection<Object> parameters;
    private int resultCount;
    private int totalCount;
    private Exception exception;
    private List<String[]> resultList;
    private List<String> headers;
    private boolean rawExecution = false;
    private String dataSourceId;


    public boolean isRawExecution()
    {
        return this.rawExecution;
    }


    public void setRawExecution(boolean rawExecution)
    {
        this.rawExecution = rawExecution;
    }


    public List<String> getHeaders()
    {
        return this.headers;
    }


    public void setHeaders(List<String> headers)
    {
        this.headers = headers;
    }


    public List<String[]> getResultList()
    {
        return this.resultList;
    }


    public void setResultList(List<String[]> resultList)
    {
        this.resultList = resultList;
    }


    public String getQuery()
    {
        return this.query;
    }


    public void setQuery(String query)
    {
        this.query = query;
    }


    public long getExecutionTime()
    {
        return this.executionTime;
    }


    public void setExecutionTime(long executionTime)
    {
        this.executionTime = executionTime;
    }


    public String getExceptionStackTrace()
    {
        StringBuilder builder = new StringBuilder();
        if(this.exception != null)
        {
            Joiner.on("\n").appendTo(builder, (Object[])this.exception.getStackTrace());
        }
        return builder.toString();
    }


    public String getCatalogVersionsAsString()
    {
        StringBuilder builder = new StringBuilder();
        if(this.catalogVersions != null)
        {
            builder.append("[");
            Joiner.on(", ").appendTo(builder, CollectionUtils.collect(this.catalogVersions, (Transformer)new Object(this)));
            builder.append("]");
        }
        return builder.toString();
    }


    public String getParametersAsString()
    {
        StringBuilder builder = new StringBuilder();
        if(this.parameters != null)
        {
            builder.append("[");
            Joiner.on(", ").appendTo(builder, this.parameters);
            builder.append("]");
        }
        return builder.toString();
    }


    @JsonIgnore
    public void setCatalogVersions(Collection<CatalogVersionModel> catalogVersions)
    {
        this.catalogVersions = catalogVersions;
    }


    @JsonIgnore
    public void setParameters(Collection<Object> parameters)
    {
        this.parameters = parameters;
    }


    public int getResultCount()
    {
        return (this.resultList == null) ? 0 : this.resultList.size();
    }


    public Exception getException()
    {
        return this.exception;
    }


    public void setException(Exception exception)
    {
        this.exception = exception;
    }


    public String getDataSourceId()
    {
        return this.dataSourceId;
    }


    public void setDataSourceId(String dataSourceId)
    {
        this.dataSourceId = dataSourceId;
    }
}
