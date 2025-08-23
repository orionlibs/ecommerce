package de.hybris.bootstrap.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.znerd.xmlenc.LineBreak;
import org.znerd.xmlenc.XMLOutputter;

public abstract class XMLTagWriter
{
    private XMLWriter enclosingWriter;
    private final XMLTagWriter parent;
    private final boolean mandatory;
    private Map _subTagWriterMap = null;
    private static final Logger _log = Logger.getLogger(XMLTagWriter.class.getName());


    public XMLTagWriter(XMLTagWriter parent)
    {
        this(parent, false);
    }


    public XMLTagWriter(XMLTagWriter parent, boolean mandatory)
    {
        this.parent = parent;
        this.mandatory = mandatory;
    }


    void registerWriter(XMLWriter writer)
    {
        this.enclosingWriter = writer;
    }


    protected XMLWriter getEnclosingWriter()
    {
        return (this.enclosingWriter != null) ? this.enclosingWriter : (
                        (this.parent != null) ? this.parent.getEnclosingWriter() : null);
    }


    protected boolean isInfoEnabled()
    {
        return (this.parent != null) ? this.parent.isInfoEnabled() : _log.isInfoEnabled();
    }


    protected boolean isDebugEnabled()
    {
        return (this.parent != null) ? this.parent.isDebugEnabled() : _log.isDebugEnabled();
    }


    protected boolean isWarnEnabled()
    {
        return (this.parent != null) ? this.parent.isWarnEnabled() : _log.isEnabledFor((Priority)Level.WARN);
    }


    protected boolean isErrorEnabled()
    {
        return (this.parent != null) ? this.parent.isErrorEnabled() : _log.isEnabledFor((Priority)Level.ERROR);
    }


    protected void info(String message)
    {
        if(this.parent != null)
        {
            this.parent.info(message);
        }
        else
        {
            _log.info(message);
        }
    }


    protected void debug(String message)
    {
        if(this.parent != null)
        {
            this.parent.debug(message);
        }
        else
        {
            _log.debug(message);
        }
    }


    protected void warn(String message)
    {
        if(this.parent != null)
        {
            this.parent.warn(message);
        }
        else
        {
            _log.warn(message);
        }
    }


    protected void error(String message)
    {
        if(this.parent != null)
        {
            this.parent.error(message);
        }
        else
        {
            _log.error(message);
        }
    }


    protected abstract String getTagName();


    protected abstract void writeContent(XMLOutputter paramXMLOutputter, Object paramObject) throws IOException;


    protected Map getAttributesMap(Object object)
    {
        return Collections.EMPTY_MAP;
    }


    public final void write(XMLOutputter xmlOut, Object object) throws XMLWriteException
    {
        if(object == null)
        {
            if(isMandatory())
            {
                if(isErrorEnabled())
                {
                    error("tag: " + getTagName() + " is not optional!");
                }
                throw new XMLWriteException(this, "tag: " + getTagName() + " is not optional!");
            }
            if(isDebugEnabled())
            {
                debug("Skipped empty tag:" + getTagName());
            }
        }
        else
        {
            try
            {
                xmlOut.startTag(getTagName());
                for(Iterator<Map.Entry> it = getAttributesMap(object).entrySet().iterator(); it.hasNext(); )
                {
                    Map.Entry entry = it.next();
                    xmlOut.attribute((String)entry.getKey(), (String)entry.getValue());
                }
                writeContent(xmlOut, object);
                xmlOut.endTag();
                XMLWriter wr = getEnclosingWriter();
                xmlOut.setLineBreak((wr != null) ? wr.getLineBreak() : LineBreak.UNIX);
            }
            catch(IOException e)
            {
                throw new XMLWriteException(e, this, "io error " + e.getMessage());
            }
        }
    }


    protected XMLTagWriter getParent()
    {
        return this.parent;
    }


    protected Map getSubTagWriterMap(boolean create)
    {
        return (this._subTagWriterMap != null) ? this._subTagWriterMap : (create ? (this._subTagWriterMap = (Map)new LinkedMap()) : Collections.EMPTY_MAP);
    }


    public void addSubTagWriter(XMLTagWriter tagWriter)
    {
        getSubTagWriterMap(true).put(tagWriter.getTagName(), tagWriter);
    }


    public void addSubTagWriter(XMLTagWriter tagWriter, String type)
    {
        if(getSubTagWriterMap(true).containsKey(tagWriter.getTagName()))
        {
            Map<String, XMLTagWriter> map = (Map)getSubTagWriterMap(true).get(tagWriter.getTagName());
            map.put(type, tagWriter);
        }
        else
        {
            LinkedMap<String, XMLTagWriter> linkedMap = new LinkedMap();
            linkedMap.put(type, tagWriter);
            getSubTagWriterMap(true).put(tagWriter.getTagName(), linkedMap);
        }
    }


    public void addSubTagWriter(String tagName, Map typeMap)
    {
        if(getSubTagWriterMap(true).containsKey(tagName))
        {
            Map map = (Map)getSubTagWriterMap(true).get(tagName);
            map.putAll(typeMap);
        }
        else
        {
            getSubTagWriterMap(true).put(tagName, typeMap);
        }
    }


    public Collection getAllSubTagWriter()
    {
        Collection<Object> tagWriter = new ArrayList();
        for(Iterator it = getSubTagWriterMap(false).values().iterator(); it.hasNext(); )
        {
            Object o = it.next();
            if(o instanceof Map)
            {
                tagWriter.addAll(((Map)o).values());
                continue;
            }
            tagWriter.add(o);
        }
        return tagWriter;
    }


    public XMLTagWriter getSubTagWriter(String tagName)
    {
        return getSubTagWriter(tagName, null);
    }


    public XMLTagWriter getSubTagWriter(String tagName, String type)
    {
        if(getSubTagWriterMap(false).containsKey(tagName))
        {
            Object object = getSubTagWriterMap(false).get(tagName);
            if(object instanceof Map)
            {
                if(type != null)
                {
                    XMLTagWriter tagWriter = (XMLTagWriter)((Map)object).get(type);
                    if(tagWriter == null)
                    {
                        throw new IllegalArgumentException("No TagWriter found for tagname: " + tagName + " and type: " + type);
                    }
                    return tagWriter;
                }
                throw new IllegalArgumentException("Tag:" + tagName + " is a typed one but no type was provided!");
            }
            if(object instanceof XMLTagWriter)
            {
                return (XMLTagWriter)object;
            }
            throw new IllegalArgumentException("Wrong mapping! Should be Map or XMLTagWriter but is:" + object
                            .getClass().getName());
        }
        throw new IllegalArgumentException("TagWriter for tag name: " + tagName + " not known!");
    }


    public boolean isMandatory()
    {
        return this.mandatory;
    }
}
