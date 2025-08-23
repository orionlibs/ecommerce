package de.hybris.platform.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;

public class GZIPResponseStream extends ServletOutputStream
{
    private final boolean captureText;
    private GZIPOutputStream zipOutput = null;
    private ByteArrayOutputStream byteOutput = null;
    private HttpServletResponse response = null;
    private ServletOutputStream output = null;
    private boolean closedFlag = false;


    public GZIPResponseStream(HttpServletResponse response, boolean captureText) throws IOException
    {
        this.closedFlag = false;
        this.response = response;
        this.output = response.getOutputStream();
        this.byteOutput = new ByteArrayOutputStream();
        this.zipOutput = !captureText ? new GZIPOutputStream(this.byteOutput) : null;
        this.captureText = captureText;
    }


    protected boolean filterOutput()
    {
        return false;
    }


    public void flush() throws IOException
    {
        if(this.closedFlag)
        {
            throw new IOException("Cannot flush a closed output stream");
        }
        if(this.captureText)
        {
            this.byteOutput.flush();
        }
        else
        {
            this.zipOutput.flush();
        }
    }


    public void close() throws IOException
    {
        if(this.closedFlag)
        {
            throw new IOException("This output stream has already been closed");
        }
        if(this.captureText)
        {
            this.byteOutput.flush();
            if(this.response.getContentType() != null && this.response.getContentType().toLowerCase().startsWith("text/html"))
            {
                InputStreamReader ir = new InputStreamReader(new ByteArrayInputStream(this.byteOutput.toByteArray()), "UTF-8");
                StringBuilder complete = new StringBuilder();
                char[] buffer = new char[4096];
                int length;
                for(length = ir.read(buffer); length != -1; length = ir.read(buffer))
                {
                    complete.append(buffer, 0, length);
                }
                ir.close();
                this.byteOutput = new ByteArrayOutputStream();
                this.zipOutput = new GZIPOutputStream(this.byteOutput);
                OutputStreamWriter wr = new OutputStreamWriter(this.zipOutput, "UTF-8");
                wr.write(applyRegExp(complete));
                wr.flush();
            }
            else
            {
                byte[] ba = this.byteOutput.toByteArray();
                this.byteOutput = new ByteArrayOutputStream();
                this.zipOutput = new GZIPOutputStream(this.byteOutput);
                this.zipOutput.write(ba);
            }
        }
        if(this.zipOutput != null)
        {
            this.zipOutput.finish();
        }
        byte[] bytes = this.byteOutput.toByteArray();
        this.response.addHeader("Content-Length", Integer.toString(bytes.length));
        this.response.addHeader("Content-Encoding", "gzip");
        this.output.write(bytes);
        this.output.flush();
        this.output.close();
        this.closedFlag = true;
    }


    protected String applyRegExp(StringBuilder complete)
    {
        return complete.toString();
    }


    public boolean closed()
    {
        return this.closedFlag;
    }


    public void write(int b) throws IOException
    {
        if(this.closedFlag)
        {
            throw new IOException("Cannot write to a closed output stream");
        }
        if(this.captureText)
        {
            this.byteOutput.write(b);
        }
        else
        {
            this.zipOutput.write((byte)b);
        }
    }


    public void write(byte[] b) throws IOException
    {
        write(b, 0, b.length);
    }


    public void write(byte[] b, int off, int len) throws IOException
    {
        if(this.closedFlag)
        {
            throw new IOException("Cannot write to a closed output stream");
        }
        if(this.captureText)
        {
            this.byteOutput.write(b, off, len);
        }
        else
        {
            this.zipOutput.write(b, off, len);
        }
    }


    public boolean isReady()
    {
        return false;
    }


    public void setWriteListener(WriteListener writeListener)
    {
    }
}
