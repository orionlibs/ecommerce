package de.hybris.platform.solrfacetsearch.config.exceptions;

public class FacetConfigServiceException extends Exception
{
    private final String configName;
    private final String message;


    public FacetConfigServiceException(String configName, String message, Throwable nested)
    {
        super(message, nested);
        this.message = "Solr configuration:" + configName + " ," + message;
        this.configName = configName;
    }


    public FacetConfigServiceException(String configName, String message)
    {
        super(message);
        this.configName = configName;
        this.message = "Solr configuration:" + configName + " ," + message;
    }


    public FacetConfigServiceException(String message, Throwable nested)
    {
        super(message, nested);
        this.configName = "";
        this.message = message;
    }


    public FacetConfigServiceException(String message)
    {
        super(message);
        this.configName = "";
        this.message = message;
    }


    public String getConfigName()
    {
        return this.configName;
    }


    public String getMessage()
    {
        return this.message;
    }
}
