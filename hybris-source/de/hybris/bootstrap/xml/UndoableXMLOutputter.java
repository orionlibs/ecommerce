package de.hybris.bootstrap.xml;

import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.znerd.xmlenc.XMLEventListenerState;
import org.znerd.xmlenc.XMLOutputter;

public class UndoableXMLOutputter extends XMLOutputter
{
    private String[] elementStack;
    private XMLEventListenerState state;


    UndoableXMLOutputter(Writer wr, String encoding) throws UnsupportedEncodingException
    {
        super((Writer)new UndoableWriterWrapper(wr), encoding);
    }


    public void markSavePoint()
    {
        if(hasSavePoint())
        {
            throw new IllegalStateException("xml outputter already holds a savepoint - use commit or rollback before calling again");
        }
        this.state = getState();
        String[] tmp = getElementStack();
        this.elementStack = new String[tmp.length];
        System.arraycopy(tmp, 0, this.elementStack, 0, tmp.length);
        ((UndoableWriterWrapper)getWriter()).markSavePoint();
    }


    public boolean hasSavePoint()
    {
        return (this.state != null && this.elementStack != null);
    }


    public void restoreSavePoint()
    {
        if(!hasSavePoint())
        {
            throw new IllegalStateException("xml outputter has no savepoint");
        }
        ((UndoableWriterWrapper)getWriter()).restoreSavePoint();
        setState(this.state, this.elementStack);
        this.state = null;
        this.elementStack = null;
    }


    public void commitSavePoint()
    {
        if(!hasSavePoint())
        {
            throw new IllegalStateException("xml outputter has no savepoint");
        }
        ((UndoableWriterWrapper)getWriter()).commitSavePoint();
        this.state = null;
        this.elementStack = null;
    }
}
