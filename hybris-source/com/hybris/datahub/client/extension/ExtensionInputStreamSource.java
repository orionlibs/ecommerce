package com.hybris.datahub.client.extension;

import java.io.InputStream;

public class ExtensionInputStreamSource implements ExtensionSource
{
    private final InputStream inputStream;


    public ExtensionInputStreamSource(InputStream in)
    {
        this.inputStream = in;
    }


    public InputStream inputStream()
    {
        return this.inputStream;
    }


    public String toString()
    {
        return "ExtensionInputStreamSource{" + this.inputStream.toString() + "}";
    }
}
