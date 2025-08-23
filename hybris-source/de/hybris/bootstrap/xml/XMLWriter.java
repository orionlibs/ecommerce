package de.hybris.bootstrap.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.znerd.xmlenc.LineBreak;
import org.znerd.xmlenc.XMLOutputter;

public class XMLWriter
{
    private String encoding = "UTF-8";
    private String indent = "\t";
    private char quotation = '"';
    private LineBreak lineBreak = LineBreak.UNIX;
    private String[] dtd = null;
    private final Writer wr;
    private XMLOutputter xout = null;
    private final XMLTagWriter root;


    public XMLWriter(Writer wr, XMLTagWriter root)
    {
        if(wr == null)
        {
            throw new NullPointerException("writer cannot be null");
        }
        this.wr = wr;
        if(root == null)
        {
            throw new NullPointerException("root tag writer cannot be null");
        }
        this.root = root;
    }


    public XMLWriter(OutputStream os, XMLTagWriter root)
    {
        this(new OutputStreamWriter(os), root);
    }


    protected XMLOutputter createXMLOutputter(Writer wr) throws UnsupportedEncodingException
    {
        UndoableXMLOutputter xmlOut = new UndoableXMLOutputter(wr, this.encoding);
        xmlOut.setEscaping(true);
        xmlOut.setLineBreak(this.lineBreak);
        xmlOut.setIndentation(this.indent);
        xmlOut.setQuotationMark(this.quotation);
        return (XMLOutputter)xmlOut;
    }


    public void write(Object o) throws XMLWriteException
    {
        try
        {
            this.xout = createXMLOutputter(this.wr);
        }
        catch(UnsupportedEncodingException e)
        {
            throw new XMLWriteException(e, this.root, "could not create xml outputter due to " + e.getMessage());
        }
        startDocument(this.xout);
        this.root.write(this.xout, o);
        endDocument(this.xout);
    }


    protected void startDocument(XMLOutputter xout) throws XMLWriteException
    {
        try
        {
            xout.declaration();
            if(this.dtd != null && this.dtd.length > 0)
            {
                xout.dtd(this.dtd[0], (this.dtd.length > 1) ? this.dtd[1] : null, (this.dtd.length > 2) ? this.dtd[2] : null);
            }
            xout.whitespace(this.lineBreak.toString());
        }
        catch(Exception e)
        {
            throw new XMLWriteException(e, this.root, "could not start document due to " + e.getMessage());
        }
    }


    protected void endDocument(XMLOutputter xout)
    {
        try
        {
            xout.endDocument();
        }
        catch(Exception e)
        {
            throw new XMLWriteException(e, this.root, "could not end document due to " + e.getMessage());
        }
    }


    public void close() throws IllegalStateException, IOException
    {
        if(this.xout != null)
        {
            this.xout.close();
            this.xout = null;
        }
        this.wr.close();
    }


    public String[] getDtd()
    {
        return this.dtd;
    }


    public void setDtd(String[] dtd)
    {
        this.dtd = dtd;
    }


    public String getEncoding()
    {
        return this.encoding;
    }


    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }


    public String getIndent()
    {
        return this.indent;
    }


    public void setIndent(String indent)
    {
        this.indent = indent;
    }


    public LineBreak getLineBreak()
    {
        return this.lineBreak;
    }


    public void setLineBreak(LineBreak lineBreak)
    {
        this.lineBreak = lineBreak;
    }


    public char getQuotation()
    {
        return this.quotation;
    }


    public void setQuotation(char quotation)
    {
        this.quotation = quotation;
    }


    public XMLTagWriter getRootTagWriter()
    {
        return this.root;
    }
}
