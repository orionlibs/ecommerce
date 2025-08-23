package com.hybris.datahub.client.extension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ExtensionStringSource implements ExtensionSource
{
    private final String content;


    public ExtensionStringSource(String xml)
    {
        this.content = xml;
    }


    public InputStream inputStream()
    {
        return new ByteArrayInputStream(this.content.getBytes(StandardCharsets.UTF_8));
    }


    public String toString()
    {
        return "ExtensionStringSource{\n" + this.content + "\n}";
    }
}
