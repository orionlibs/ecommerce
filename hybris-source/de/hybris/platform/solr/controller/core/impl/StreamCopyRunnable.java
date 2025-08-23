package de.hybris.platform.solr.controller.core.impl;

import de.hybris.platform.solr.controller.SolrControllerRuntimeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamCopyRunnable implements Runnable
{
    private final InputStream inputStream;
    private final OutputStream outputStream;


    public StreamCopyRunnable(InputStream inputStream, OutputStream outputStream)
    {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }


    public void run()
    {
        try
        {
            int chr;
            while((chr = this.inputStream.read()) != -1)
            {
                this.outputStream.write(chr);
            }
        }
        catch(IOException e)
        {
            throw new SolrControllerRuntimeException(e);
        }
    }
}
