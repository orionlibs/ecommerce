package de.hybris.bootstrap.xml;

import java.util.Stack;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLContentHandler extends DefaultHandler
{
    private static final Logger log = Logger.getLogger(XMLContentHandler.class.getName());
    private final ObjectProcessor processor;
    private final Stack current = new Stack();
    private final TagListener root;
    private Locator locator;
    private final StringBuilder xml = new StringBuilder();
    private final StringBuilder chars = new StringBuilder();
    private boolean appending = false;
    private boolean appendingCharacters = false;
    private int tabsize = 0;
    private static final String TAB = "  ";


    public XMLContentHandler(TagListener rootListener, ObjectProcessor processor)
    {
        if(rootListener == null)
        {
            throw new NullPointerException("root listener cannot be null");
        }
        this.processor = processor;
        this.root = rootListener;
    }


    public final void setDocumentLocator(Locator locator)
    {
        this.locator = locator;
    }


    public final void characters(char[] ch, int offset, int length) throws SAXException
    {
        TagListener listener = getCurrentTagListener();
        if(listener != null)
        {
            try
            {
                listener.characters(ch, offset, length);
            }
            catch(Exception e)
            {
                log.error("error in taglistener " + listener + " at line " + this.locator.getLineNumber() + " : " + e.getMessage());
                e.printStackTrace(System.err);
                throw new UnknownParseError(e, e.getMessage());
            }
        }
        if(this.appending)
        {
            if(!this.appendingCharacters)
            {
                this.appendingCharacters = true;
            }
            this.chars.append(ch, offset, length);
        }
    }


    public final void startElement(String uri, String localName, String qname, Attributes attributes) throws SAXException
    {
        DummyTagListener dummyTagListener;
        TagListener matching;
        charactersDone();
        TagListener current = getCurrentTagListener();
        if(current == null)
        {
            matching = getRootListener();
            if(!qname.equals(matching.getTagName()))
            {
                throw new InvalidFormatException("wrong xml document type '" + qname + "' expected '" + matching
                                .getTagName() + "'");
            }
        }
        else
        {
            matching = current.getSubTagListener(qname);
        }
        if(matching != null)
        {
            setCurrentTagListener(current = matching);
            if(matching instanceof XMLContentLogger)
            {
                this.appending = true;
            }
        }
        else
        {
            setCurrentTagListener((TagListener)(dummyTagListener = new DummyTagListener()));
        }
        if(this.appending)
        {
            this.tabsize++;
            this.xml.append(getTabs()).append("<").append(qname);
            if(attributes != null && attributes.getLength() > 0)
            {
                for(int i = 0, s = attributes.getLength(); i < s; i++)
                {
                    this.xml.append(" ").append(attributes.getQName(i)).append("=\"").append(attributes.getValue(i)).append("\"");
                }
            }
            this.xml.append(">\n");
        }
        dummyTagListener.setStartLineNumber(this.locator.getLineNumber());
        try
        {
            dummyTagListener.startElement(this.processor, attributes);
        }
        catch(Exception e)
        {
            log.error("error in taglistener " + dummyTagListener + " at line " + this.locator.getLineNumber() + " : " + e.getMessage());
            e.printStackTrace(System.err);
            throw new UnknownParseError(e, e.getMessage());
        }
    }


    public final void endElement(String uri, String localName, String qname) throws SAXException
    {
        charactersDone();
        TagListener current = getCurrentTagListener();
        if(this.appending)
        {
            this.xml.append(getTabs()).append("</").append(qname).append(">\n");
            this.tabsize--;
        }
        if(current instanceof XMLContentLogger)
        {
            ((XMLContentLogger)current).setXML(this.xml.toString());
            this.xml.setLength(0);
            this.appending = false;
        }
        current.setEndLineNumber(this.locator.getLineNumber());
        try
        {
            current.endElement(this.processor);
        }
        catch(Exception e)
        {
            log.error("error in taglistener " + current + " at line " + this.locator.getLineNumber() + " : " + e.getMessage());
            e.printStackTrace(System.err);
            throw new UnknownParseError(e, e.getMessage());
        }
        removeCurrentTagListener();
    }


    private final String getTabs()
    {
        String tab = "";
        for(int i = 0; i < this.tabsize; i++)
        {
            tab = tab + "  ";
        }
        return tab;
    }


    private final void charactersDone()
    {
        if(this.appendingCharacters)
        {
            this.appendingCharacters = false;
            for(StringTokenizer st = new StringTokenizer(this.chars.toString(), "\n\r\f", false); st.hasMoreTokens(); )
            {
                this.xml.append(getTabs()).append("  ").append(st.nextToken()).append("\n");
            }
            this.chars.setLength(0);
        }
    }


    protected ObjectProcessor getObjetcProcessor()
    {
        return this.processor;
    }


    protected void setCurrentTagListener(TagListener newCurrentTagListener)
    {
        this.current.push(newCurrentTagListener);
    }


    protected void removeCurrentTagListener()
    {
        if(!this.current.empty())
        {
            this.current.pop();
        }
    }


    protected TagListener getCurrentTagListener()
    {
        return this.current.empty() ? null : this.current.peek();
    }


    protected TagListener getRootListener()
    {
        return this.root;
    }
}
