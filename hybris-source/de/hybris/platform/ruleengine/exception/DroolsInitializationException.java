package de.hybris.platform.ruleengine.exception;

import com.google.common.collect.Lists;
import de.hybris.platform.ruleengine.ResultItem;
import java.util.Collection;

public class DroolsInitializationException extends RuntimeException
{
    private final Collection<ResultItem> results;


    public DroolsInitializationException(String message)
    {
        super(message);
        this.results = Lists.newArrayList();
    }


    public DroolsInitializationException(String message, Throwable cause)
    {
        super(message, cause);
        this.results = Lists.newArrayList();
    }


    public DroolsInitializationException(Collection<ResultItem> results, Throwable cause)
    {
        super(cause);
        this.results = results;
    }


    public DroolsInitializationException(Collection<ResultItem> results, String message)
    {
        super(message + message);
        this.results = results;
    }


    public Collection<ResultItem> getResults()
    {
        return this.results;
    }
}
