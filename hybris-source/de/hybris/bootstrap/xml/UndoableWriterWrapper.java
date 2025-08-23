package de.hybris.bootstrap.xml;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class UndoableWriterWrapper extends Writer
{
    private final Writer wrapped;
    private Writer tempWriter;
    private boolean flushCalled = false;
    private boolean closeCalled = false;


    public UndoableWriterWrapper(Writer writer)
    {
        this.wrapped = writer;
    }


    private Writer getWriter()
    {
        return (this.tempWriter != null) ? this.tempWriter : this.wrapped;
    }


    public void close() throws IOException
    {
        this.closeCalled = true;
        getWriter().close();
    }


    public void flush() throws IOException
    {
        this.flushCalled = true;
        getWriter().flush();
    }


    public void write(char[] cbuf, int off, int len) throws IOException
    {
        getWriter().write(cbuf, off, len);
    }


    public void markSavePoint()
    {
        if(this.tempWriter != null)
        {
            throw new IllegalStateException("already got a temp writer");
        }
        this.tempWriter = new StringWriter();
        this.flushCalled = false;
        this.closeCalled = false;
    }


    public void restoreSavePoint()
    {
        if(this.tempWriter == null)
        {
            throw new IllegalStateException("got no temp writer");
        }
        this.tempWriter = null;
        this.flushCalled = false;
        this.closeCalled = false;
    }


    public void commitSavePoint()
    {
        if(this.tempWriter == null)
        {
            throw new IllegalStateException("got no temp writer");
        }
        try
        {
            this.tempWriter.flush();
            StringBuffer buffer = ((StringWriter)this.tempWriter).getBuffer();
            char[] data = new char[buffer.length()];
            buffer.getChars(0, buffer.length(), data, 0);
            this.wrapped.write(data, 0, data.length);
            if(this.flushCalled)
            {
                this.wrapped.flush();
            }
            if(this.closeCalled)
            {
                this.wrapped.close();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            this.tempWriter = null;
            this.flushCalled = false;
            this.closeCalled = false;
        }
    }
}
