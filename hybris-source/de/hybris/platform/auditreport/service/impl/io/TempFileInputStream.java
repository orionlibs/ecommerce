package de.hybris.platform.auditreport.service.impl.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

public class TempFileInputStream extends FileInputStream
{
    private final File tempTarget;


    public TempFileInputStream(File tempTarget) throws FileNotFoundException
    {
        super(tempTarget);
        this.tempTarget = tempTarget;
    }


    public void close() throws IOException
    {
        try
        {
            super.close();
        }
        finally
        {
            if(this.tempTarget != null && this.tempTarget.exists())
            {
                Files.delete(this.tempTarget.toPath());
            }
        }
    }
}
