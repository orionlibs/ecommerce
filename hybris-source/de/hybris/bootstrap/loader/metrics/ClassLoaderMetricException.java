package de.hybris.bootstrap.loader.metrics;

public class ClassLoaderMetricException extends RuntimeException
{
    public ClassLoaderMetricException(String msg, Exception e)
    {
        super(msg, e);
    }
}
