package de.hybris.platform.cms2.namedquery;

public class NamedQueryConversionDto
{
    private String query;
    private NamedQuery namedQuery;


    public void setQuery(String query)
    {
        this.query = query;
    }


    public NamedQueryConversionDto withQuery(String query)
    {
        this.query = query;
        return this;
    }


    public String getQuery()
    {
        return this.query;
    }


    public void setNamedQuery(NamedQuery namedQuery)
    {
        this.namedQuery = namedQuery;
    }


    public NamedQueryConversionDto withNamedQuery(NamedQuery namedQuery)
    {
        this.namedQuery = namedQuery;
        return this;
    }


    public NamedQuery getNamedQuery()
    {
        return this.namedQuery;
    }
}
