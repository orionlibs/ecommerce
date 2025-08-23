package de.hybris.bootstrap.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;

public abstract class DefaultTagListener implements TagListener
{
    private static final Logger LOG = Logger.getLogger(DefaultTagListener.class.getName());
    private DefaultTagListener parent;
    private Map<String, TagListener> subListenerMap = null;
    private StringBuilder charactersVariable = null;
    private Object result = null;
    private Map<String, Object> subtagvalues = null;
    private Map<String, String> attributesMap = null;
    private int endLineNumber = -1;
    private int startLineNumber = -1;


    public DefaultTagListener()
    {
        this(null);
    }


    public DefaultTagListener(DefaultTagListener parent)
    {
        this.parent = parent;
    }


    protected List getPartentTags()
    {
        List<String> tags = new ArrayList();
        for(DefaultTagListener parent = getParent(); parent != null; parent = parent.getParent())
        {
            tags.add(parent.getTagName());
        }
        return tags;
    }


    protected List<DefaultTagListener> getParents()
    {
        List<DefaultTagListener> parents = new ArrayList<>();
        for(DefaultTagListener tl = getParent(); tl != null; tl = tl.getParent())
        {
            parents.add(tl);
        }
        return parents;
    }


    public DefaultTagListener getParent()
    {
        return this.parent;
    }


    public boolean isRoot()
    {
        return (getParent() == null);
    }


    public Object getResult()
    {
        return this.result;
    }


    protected Map<String, TagListener> getSubListenerMap()
    {
        if(this.subListenerMap == null)
        {
            this.subListenerMap = new LinkedHashMap<>();
            for(TagListener listener : createSubTagListeners())
            {
                addSubTagListener(listener);
            }
        }
        return this.subListenerMap;
    }


    public void addSubTagListener(TagListener listener)
    {
        getSubListenerMap().put(listener.getTagName(), listener);
        if(listener instanceof DefaultTagListener && ((DefaultTagListener)listener).parent == null)
        {
            ((DefaultTagListener)listener).parent = this;
        }
    }


    public String getAttribute(String qname)
    {
        return (this.attributesMap == null) ? null : this.attributesMap.get(qname);
    }


    protected void readAttributes(Attributes attributes)
    {
        Map<String, String> tmp = new LinkedHashMap<>();
        for(int i = 0; i < attributes.getLength(); i++)
        {
            tmp.put(attributes.getQName(i), attributes.getValue(i));
        }
        if(!tmp.isEmpty())
        {
            this.attributesMap = Collections.unmodifiableMap(tmp);
        }
    }


    protected void addSubTagValue(String subTagListenerName, Object newValue)
    {
        if(this.subtagvalues == null)
        {
            this.subtagvalues = new LinkedHashMap<>();
        }
        Object present = this.subtagvalues.get(subTagListenerName);
        if(present == null)
        {
            if(newValue != null)
            {
                if(newValue instanceof Map)
                {
                    this.subtagvalues.put(subTagListenerName, new LinkedHashMap<>((Map<?, ?>)newValue));
                }
                else if(newValue instanceof List)
                {
                    this.subtagvalues.put(subTagListenerName, new ArrayList((List)newValue));
                }
                else if(newValue instanceof Set)
                {
                    this.subtagvalues.put(subTagListenerName, new HashSet((Set)newValue));
                }
                else if(newValue instanceof Collection)
                {
                    this.subtagvalues.put(subTagListenerName, new ArrayList((Collection)newValue));
                }
                else
                {
                    this.subtagvalues.put(subTagListenerName, newValue);
                }
            }
            else
            {
                this.subtagvalues.put(subTagListenerName, null);
            }
        }
        else if(present instanceof Collection)
        {
            if(newValue instanceof Collection)
            {
                ((Collection)present).addAll((Collection)newValue);
            }
            else
            {
                ((Collection<Object>)present).add(newValue);
            }
        }
        else if(present instanceof Map)
        {
            if(newValue instanceof Map)
            {
                ((Map)present).putAll((Map)newValue);
            }
            else
            {
                ((Map<Object, Object>)present).put(newValue, newValue);
            }
        }
        else
        {
            ArrayList<Object> tmp = new ArrayList(Collections.singletonList(present));
            if(newValue instanceof Collection)
            {
                tmp.addAll((Collection)newValue);
            }
            else
            {
                tmp.add(newValue);
            }
            this.subtagvalues.put(subTagListenerName, tmp);
        }
    }


    protected void addResult(Object res)
    {
        this.result = res;
    }


    protected Object processStartElement(ObjectProcessor processor) throws ParseAbortException
    {
        return null;
    }


    protected abstract Object processEndElement(ObjectProcessor paramObjectProcessor) throws ParseAbortException;


    protected void processError(Exception exception) throws ParseAbortException
    {
        throw new UnknownParseError(exception, exception.getMessage());
    }


    protected void clearValues()
    {
        this.subtagvalues = null;
        this.attributesMap = null;
        this.charactersVariable = null;
        this.startLineNumber = -1;
        this.endLineNumber = -1;
    }


    public final void startElement(ObjectProcessor processor, Attributes attributes) throws ParseAbortException
    {
        readAttributes(attributes);
        Object value = null;
        try
        {
            value = processStartElement(processor);
        }
        catch(Exception e)
        {
            processError(e);
        }
        if(value != null)
        {
            if(getParent() != null)
            {
                getParent().addSubTagValue(getTagName(), value);
            }
            else
            {
                addResult(value);
            }
        }
    }


    public final void endElement(ObjectProcessor processor) throws ParseAbortException
    {
        Object value = null;
        try
        {
            value = processEndElement(processor);
        }
        catch(Exception e)
        {
            processError(e);
        }
        if(value != null)
        {
            if(getParent() != null)
            {
                getParent().addSubTagValue(getTagName(), value);
            }
            else
            {
                addResult(value);
            }
        }
        clearValues();
    }


    public TagListener getSubTagListener(String tagname)
    {
        return getSubListenerMap().get(tagname);
    }


    public final void characters(char[] character, int offset, int length)
    {
        if(this.charactersVariable == null)
        {
            this.charactersVariable = new StringBuilder();
        }
        this.charactersVariable.append(character, offset, length);
    }


    protected Collection<TagListener> createSubTagListeners()
    {
        return Collections.EMPTY_LIST;
    }


    public void setEndLineNumber(int endLineNumber)
    {
        this.endLineNumber = endLineNumber;
    }


    public void setStartLineNumber(int startLineNumber)
    {
        this.startLineNumber = startLineNumber;
    }


    protected Object getSubTagValue(String subTagListenerName)
    {
        return (this.subtagvalues != null) ? this.subtagvalues.get(subTagListenerName) : null;
    }


    protected int getSubTagIntValue(String subTagListenerName, int defaultValue)
    {
        Integer result = (Integer)getSubTagValue(subTagListenerName);
        return (result != null) ? result.intValue() : defaultValue;
    }


    protected double getSubTagDoubleValue(String subTagListenerName, double defaultValue)
    {
        Double result = (Double)getSubTagValue(subTagListenerName);
        return (result != null) ? result.doubleValue() : defaultValue;
    }


    protected Collection getSubTagValueCollection(String subTagListenerName)
    {
        Object val = getSubTagValue(subTagListenerName);
        return (val != null) ? ((val instanceof Collection) ? (Collection)val : Collections.<Object>singleton(val)) : Collections.EMPTY_LIST;
    }


    protected Map getSubTagValueMap(String subTagListenerName)
    {
        Object val = getSubTagValue(subTagListenerName);
        return (val != null) ? ((val instanceof Map) ? (Map)val : Collections.<Object, Object>singletonMap(val, val)) : Collections.EMPTY_MAP;
    }


    public String getCharacters()
    {
        String trimmed = (this.charactersVariable != null) ? this.charactersVariable.toString().trim() : null;
        return (trimmed != null && !"".equals(trimmed)) ? trimmed : null;
    }


    public int getEndLineNumber()
    {
        return this.endLineNumber;
    }


    public int getStartLineNumber()
    {
        return this.startLineNumber;
    }
}
