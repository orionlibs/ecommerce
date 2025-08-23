package de.hybris.platform.impex.jalo;

import de.hybris.platform.impex.constants.ImpExConstants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.log4j.Logger;

public class ImpExCsvFile implements ImpExFile
{
    private OutputStream output = null;
    private File file = null;
    private static final Logger LOG = Logger.getLogger(ImpExCsvFile.class.getName());


    public ImpExCsvFile() throws IOException
    {
        this(null);
    }


    public ImpExCsvFile(String fileName) throws IOException
    {
        if(fileName == null || fileName.length() < 1)
        {
            this.file = File.createTempFile("ImpExCSV_" + System.currentTimeMillis(), ".csv");
        }
        else
        {
            this.file = new File(fileName);
            this.file.createNewFile();
        }
        this.output = new FileOutputStream(this.file);
    }


    public OutputStream getOutputStream()
    {
        return this.output;
    }


    public void close() throws IOException
    {
        if(this.output != null)
        {
            this.output.close();
        }
    }


    public File getFile()
    {
        return this.file;
    }


    public String getMimeType()
    {
        return ImpExConstants.File.MIME_TYPE_CSV;
    }


    public String getFileExtension()
    {
        return "csv";
    }
}
