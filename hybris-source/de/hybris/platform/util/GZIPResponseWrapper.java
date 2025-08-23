package de.hybris.platform.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class GZIPResponseWrapper extends HttpServletResponseWrapper
{
    protected HttpServletResponse origResponse = null;
    protected ServletOutputStream outputStream = null;
    protected PrintWriter printWriter = null;


    public GZIPResponseWrapper(HttpServletResponse response)
    {
        super(response);
        this.origResponse = response;
    }


    public ServletOutputStream getOutputStream() throws IOException
    {
        if(this.printWriter != null)
        {
            throw new IllegalStateException("getWriter() has already been called!");
        }
        if(this.outputStream == null)
        {
            this.outputStream = createOutputStream();
        }
        return this.outputStream;
    }


    public PrintWriter getWriter() throws IOException
    {
        if(this.printWriter != null)
        {
            return this.printWriter;
        }
        if(this.outputStream != null)
        {
            throw new IllegalStateException("getOutputStream() has already been called!");
        }
        this.outputStream = createOutputStream();
        this.printWriter = new PrintWriter(new OutputStreamWriter((OutputStream)this.outputStream, "UTF-8"));
        return this.printWriter;
    }


    public ServletOutputStream createOutputStream() throws IOException
    {
        return (ServletOutputStream)new GZIPResponseStream(this.origResponse, false);
    }


    public void flushBuffer() throws IOException
    {
        this.outputStream.flush();
    }


    public void finishResponse()
    {
        try
        {
            if(this.printWriter != null)
            {
                this.printWriter.close();
            }
            else if(this.outputStream != null)
            {
                this.outputStream.close();
            }
        }
        catch(IOException iOException)
        {
        }
    }


    public void setContentLength(int length)
    {
    }
}
