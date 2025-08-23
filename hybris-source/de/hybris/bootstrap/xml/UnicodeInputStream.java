package de.hybris.bootstrap.xml;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class UnicodeInputStream extends InputStream
{
    private final PushbackInputStream internalIn;
    private boolean isInited = false;
    private final String defaultEnc;
    private String encoding;
    private boolean skipBOM = true;
    private static final int BOM_SIZE = 4;


    public UnicodeInputStream(InputStream in, String defaultEnc)
    {
        this.internalIn = new PushbackInputStream(new BufferedInputStream(in), 4);
        this.defaultEnc = defaultEnc;
    }


    public boolean isBOMSkipped()
    {
        return this.skipBOM;
    }


    public void setSkipBOM(boolean b)
    {
        this.skipBOM = b;
    }


    public String getDefaultEncoding()
    {
        return this.defaultEnc;
    }


    public String getEncoding()
    {
        if(!this.isInited)
        {
            try
            {
                init();
            }
            catch(IOException ex)
            {
                throw new IllegalStateException("Init method failed." + ex.getMessage());
            }
        }
        return this.encoding;
    }


    protected void init() throws IOException
    {
        int i;
        if(this.isInited)
        {
            return;
        }
        byte[] bom = new byte[4];
        int n = this.internalIn.read(bom, 0, bom.length);
        if(bom[0] == -17 && bom[1] == -69 && bom[2] == -65)
        {
            this.encoding = "UTF-8";
            i = n - 3;
        }
        else if(bom[0] == -2 && bom[1] == -1)
        {
            this.encoding = "UTF-16BE";
            i = n - 2;
        }
        else if(bom[0] == -1 && bom[1] == -2)
        {
            this.encoding = "UTF-16LE";
            i = n - 2;
        }
        else if(bom[0] == 0 && bom[1] == 0 && bom[2] == -2 && bom[3] == -1)
        {
            this.encoding = "UTF-32BE";
            i = n - 4;
        }
        else if(bom[0] == -1 && bom[1] == -2 && bom[2] == 0 && bom[3] == 0)
        {
            this.encoding = "UTF-32LE";
            i = n - 4;
        }
        else
        {
            this.encoding = this.defaultEnc;
            i = n;
        }
        if(!this.skipBOM)
        {
            i = n;
        }
        if(i > 0)
        {
            this.internalIn.unread(bom, n - i, i);
        }
        this.isInited = true;
    }


    public void close() throws IOException
    {
        this.isInited = true;
        this.internalIn.close();
    }


    public int read() throws IOException
    {
        init();
        return this.internalIn.read();
    }


    public int available() throws IOException
    {
        return this.internalIn.available();
    }
}
