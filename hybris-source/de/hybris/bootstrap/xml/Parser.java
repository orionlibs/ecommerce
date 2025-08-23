package de.hybris.bootstrap.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.DefaultHandler;

public class Parser
{
    private static final String NAMESPACES_FEATURE_ID = "http://xml.org/sax/features/namespaces";
    private static final String NAMESPACE_PREFIXES_FEATURE_ID = "http://xml.org/sax/features/namespace-prefixes";
    private static final String VALIDATION_FEATURE_ID = "http://xml.org/sax/features/validation";
    private static final String SCHEMA_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/schema";
    private static final String SCHEMA_FULL_CHECKING_FEATURE_ID = "http://apache.org/xml/features/validation/schema-full-checking";
    private static final String DYNAMIC_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/dynamic";
    private static final String LOAD_EXTERNAL_DTD_FEATURE_ID = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
    private final SAXParser saxParser;
    private XMLContentHandler handler;
    private final ObjectProcessor processor;


    public Parser(ObjectProcessor processor, Map props)
    {
        this.saxParser = createSaxParser(props);
        this.processor = processor;
    }


    protected SAXParser getSaxParser()
    {
        return this.saxParser;
    }


    protected TagListener getCurrentTagListener()
    {
        if(this.handler == null)
        {
            throw new IllegalStateException();
        }
        return this.handler.getCurrentTagListener();
    }


    protected ObjectProcessor getObjectProcessor()
    {
        return this.processor;
    }


    protected final SAXParser createSaxParser(Map props)
    {
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware((props == null || Boolean.TRUE.equals(props.get("http://xml.org/sax/features/namespaces")) || Boolean.TRUE
                            .toString().equals(props.get("http://xml.org/sax/features/namespaces"))));
            factory.setValidating((props != null && (Boolean.TRUE
                            .equals(props.get("http://xml.org/sax/features/validation")) || Boolean.TRUE.toString()
                            .equals(props
                                            .get("http://xml.org/sax/features/validation")))));
            return getSAXParserFromFactory(factory);
        }
        catch(Exception e)
        {
            try
            {
                SAXParserFactory factory = SAXParserFactory.newInstance("com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl", null);
                factory.setNamespaceAware((props == null || Boolean.TRUE.equals(props.get("http://xml.org/sax/features/namespaces")) || Boolean.TRUE
                                .toString().equals(props.get("http://xml.org/sax/features/namespaces"))));
                factory.setValidating((props != null && (Boolean.TRUE
                                .equals(props.get("http://xml.org/sax/features/validation")) || Boolean.TRUE.toString()
                                .equals(props
                                                .get("http://xml.org/sax/features/validation")))));
                return getSAXParserFromFactory(factory);
            }
            catch(SAXException | ParserConfigurationException e1)
            {
                System.err.println("could not load fallback parserd due to " + e1.getMessage());
                throw new IllegalStateException("error: Unable to instantiate parser due to " + e.getMessage());
            }
        }
    }


    private SAXParser getSAXParserFromFactory(SAXParserFactory factory) throws ParserConfigurationException, SAXException
    {
        SAXParser parser = factory.newSAXParser();
        parser.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
        parser.setProperty("http://javax.xml.XMLConstants/property/accessExternalSchema", "");
        return parser;
    }


    protected void setFeatures(Map props)
    {
        try
        {
            if(props.get("http://xml.org/sax/features/namespace-prefixes") != null)
            {
                getSaxParser().setProperty("http://xml.org/sax/features/namespace-prefixes", props.get("http://xml.org/sax/features/namespace-prefixes"));
            }
        }
        catch(SAXException e)
        {
            System.err.println("warning: Parser does not support feature (http://xml.org/sax/features/namespace-prefixes)");
        }
        try
        {
            if(props.get("http://apache.org/xml/features/nonvalidating/load-external-dtd") != null)
            {
                getSaxParser().setProperty("http://apache.org/xml/features/nonvalidating/load-external-dtd", props.get("http://apache.org/xml/features/nonvalidating/load-external-dtd"));
            }
        }
        catch(SAXNotRecognizedException e)
        {
            System.err.println("warning: Parser does not recognize feature (http://apache.org/xml/features/nonvalidating/load-external-dtd)");
        }
        catch(SAXNotSupportedException e)
        {
            System.err.println("warning: Parser does not support feature (http://apache.org/xml/features/nonvalidating/load-external-dtd)");
        }
        try
        {
            if(props.get("http://apache.org/xml/features/validation/schema") != null)
            {
                getSaxParser().setProperty("http://apache.org/xml/features/validation/schema", props.get("http://apache.org/xml/features/validation/schema"));
            }
        }
        catch(SAXNotRecognizedException e)
        {
            System.err.println("warning: Parser does not recognize feature (http://apache.org/xml/features/validation/schema)");
        }
        catch(SAXNotSupportedException e)
        {
            System.err.println("warning: Parser does not support feature (http://apache.org/xml/features/validation/schema)");
        }
        try
        {
            if(props.get("http://apache.org/xml/features/validation/schema-full-checking") != null)
            {
                getSaxParser().setProperty("http://apache.org/xml/features/validation/schema-full-checking", props.get("http://apache.org/xml/features/validation/schema-full-checking"));
            }
        }
        catch(SAXNotRecognizedException e)
        {
            System.err.println("warning: Parser does not recognize feature (http://apache.org/xml/features/validation/schema-full-checking)");
        }
        catch(SAXNotSupportedException e)
        {
            System.err.println("warning: Parser does not support feature (http://apache.org/xml/features/validation/schema-full-checking)");
        }
        try
        {
            if(props.get("http://apache.org/xml/features/validation/dynamic") != null)
            {
                getSaxParser().setProperty("http://apache.org/xml/features/validation/dynamic", props.get("http://apache.org/xml/features/validation/dynamic"));
            }
        }
        catch(SAXNotRecognizedException e)
        {
            System.err.println("warning: Parser does not recognize feature (http://apache.org/xml/features/validation/dynamic)");
        }
        catch(SAXNotSupportedException e)
        {
            System.err.println("warning: Parser does not support feature (http://apache.org/xml/features/validation/dynamic)");
        }
        try
        {
            if(props.get("http://apache.org/xml/features/nonvalidating/load-external-dtd") != null)
            {
                getSaxParser().setProperty("http://apache.org/xml/features/nonvalidating/load-external-dtd", props.get("http://apache.org/xml/features/nonvalidating/load-external-dtd"));
            }
        }
        catch(SAXNotRecognizedException e)
        {
            System.err.println("warning: Parser does not recognize feature (http://apache.org/xml/features/nonvalidating/load-external-dtd)");
        }
        catch(SAXNotSupportedException e)
        {
            System.err.println("warning: Parser does not support feature (http://apache.org/xml/features/nonvalidating/load-external-dtd)");
        }
    }


    protected Map getDefaultFeatures()
    {
        Map<Object, Object> props = new HashMap<>();
        props.put("http://xml.org/sax/features/namespaces", Boolean.TRUE.toString());
        props.put("http://xml.org/sax/features/validation", Boolean.FALSE.toString());
        return props;
    }


    protected XMLContentHandler createHandler(TagListener root)
    {
        return new XMLContentHandler(root, getObjectProcessor());
    }


    public void parse(InputStream stream, String encoding, TagListener root) throws ParseAbortException
    {
        InputSource src = new InputSource();
        src.setByteStream((InputStream)new UnicodeInputStream(stream, "utf-8"));
        parse(src, root);
    }


    public void parse(InputSource src, TagListener root) throws ParseAbortException
    {
        try
        {
            this.handler = createHandler(root);
            getSaxParser().parse(src, (DefaultHandler)this.handler);
        }
        catch(ParseFinishedException parseFinishedException)
        {
        }
        catch(ParseAbortException e)
        {
            throw e;
        }
        catch(SAXException e)
        {
            throw new UnknownParseError(e, e.getMessage());
        }
        catch(IOException e)
        {
            throw new UnknownParseError(e, e.getMessage());
        }
        finally
        {
            this.handler = null;
        }
    }
}
