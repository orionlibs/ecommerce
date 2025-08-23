package com.hybris.datahub.client.extension;

public class InvalidDataHubExtensionException extends RuntimeException
{
    public InvalidDataHubExtensionException()
    {
        this("The extension is invalid - check DataHub logs for details");
    }


    public InvalidDataHubExtensionException(String msg)
    {
        super(msg);
    }
}
