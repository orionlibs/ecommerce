package com.hybris.datahub.client.extension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ExtensionFileSource implements ExtensionSource
{
    private final File extensionFile;


    public ExtensionFileSource(String path)
    {
        this(new File(path));
    }


    public ExtensionFileSource(File file)
    {
        this.extensionFile = file;
    }


    public InputStream inputStream() throws FileNotFoundException
    {
        return new FileInputStream(this.extensionFile);
    }


    public String toString()
    {
        return "ExtensionFileSource{" + this.extensionFile.toString() + "}";
    }
}
