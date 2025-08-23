package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.util.Base64;
import de.hybris.platform.util.CSVUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class MapValueTranslator extends AbstractValueTranslator
{
    private static final Logger LOG = Logger.getLogger(MapValueTranslator.class);
    private AbstractValueTranslator keyTranslator = null;
    private AbstractValueTranslator valueTranslator = null;
    private char mapValueDelimiter;
    private String key2valueAssignment;


    public MapValueTranslator(MapType mapType, List<AbstractDescriptor.ColumnParams>[] patternLists) throws HeaderValidationException
    {
        this(createKeyTranslator(mapType, patternLists), createValueTranslator(mapType, patternLists));
    }


    public MapValueTranslator(AbstractValueTranslator keyTranslator, AbstractValueTranslator valueTranslator) throws HeaderValidationException
    {
        this(keyTranslator, valueTranslator, ';', "->");
    }


    public MapValueTranslator(MapType mapType, List<AbstractDescriptor.ColumnParams>[] patternLists, char keyValueDelimiter, String key2valueAssignment) throws HeaderValidationException
    {
        this(createKeyTranslator(mapType, patternLists), createValueTranslator(mapType, patternLists), keyValueDelimiter, key2valueAssignment);
    }


    public MapValueTranslator(AbstractValueTranslator keyTranslator, AbstractValueTranslator valueTranslator, char keyValueDelimiter, String key2valueAssignment) throws HeaderValidationException
    {
        if(keyTranslator == null)
        {
            throw new HeaderValidationException("No key translator set", 0);
        }
        this.keyTranslator = keyTranslator;
        if(valueTranslator == null)
        {
            throw new HeaderValidationException("No value translator set", 0);
        }
        this.valueTranslator = valueTranslator;
        this.mapValueDelimiter = keyValueDelimiter;
        if(key2valueAssignment == null)
        {
            LOG.warn("No key2value assignment set, using default one:->");
            this.key2valueAssignment = "->";
        }
        else
        {
            this.key2valueAssignment = key2valueAssignment;
        }
    }


    public void init(StandardColumnDescriptor columnDescriptor)
    {
        super.init(columnDescriptor);
        String customDelimiter = columnDescriptor.getDescriptorData().getModifier("key2value-delimiter");
        if(customDelimiter != null && customDelimiter.length() > 0)
        {
            this.key2valueAssignment = customDelimiter;
        }
        customDelimiter = columnDescriptor.getDescriptorData().getModifier("map-delimiter");
        if(customDelimiter != null && customDelimiter.length() > 0)
        {
            this.mapValueDelimiter = customDelimiter.charAt(0);
        }
        this.keyTranslator.init(columnDescriptor);
        this.valueTranslator.init(columnDescriptor);
    }


    public void validate(StandardColumnDescriptor columnDescriptor) throws HeaderValidationException
    {
        super.validate(columnDescriptor);
        this.keyTranslator.validate(columnDescriptor);
        this.valueTranslator.validate(columnDescriptor);
    }


    public Object importValue(String valueExpr, Item toItem) throws JaloInvalidParameterException
    {
        if(StringUtils.isBlank(valueExpr))
        {
            return null;
        }
        char[] valueDel = {this.mapValueDelimiter};
        char[] mappingDel = this.key2valueAssignment.toCharArray();
        Map<Object, Object> map = new HashMap<>();
        List<String> tokens = CSVUtils.splitAndUnescape(valueExpr, valueDel, false);
        for(Iterator<String> iter = tokens.iterator(); !wasUnresolved() && iter.hasNext(); )
        {
            String token = iter.next();
            if(token == null)
            {
                continue;
            }
            int index = token.indexOf(this.key2valueAssignment);
            Object keyObj = getKeyTranslator().importValue(
                            CSVUtils.unescapeString(token.substring(0, index).trim(), mappingDel, false), toItem);
            if(getKeyTranslator().wasUnresolved())
            {
                setError();
                continue;
            }
            Object valObj = getValueTranslator().importValue(
                            CSVUtils.unescapeString(token.substring(index + this.key2valueAssignment.length()).trim(), mappingDel, false), toItem);
            if(getValueTranslator().wasUnresolved())
            {
                setError();
                continue;
            }
            map.put(keyObj, valObj);
        }
        return wasUnresolved() ? null : map;
    }


    public String exportValue(Object valuemap) throws JaloInvalidParameterException
    {
        if(valuemap != null)
        {
            List<String> mappings = new LinkedList<>();
            for(Iterator<Map.Entry> it = ((Map)valuemap).entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry mapEntry = it.next();
                mappings.add(CSVUtils.escapeString(convertToString(mapEntry.getKey(), this.keyTranslator), this.key2valueAssignment
                                .toCharArray(), false) + CSVUtils.escapeString(convertToString(mapEntry.getKey(), this.keyTranslator), this.key2valueAssignment.toCharArray(), false) + this.key2valueAssignment);
            }
            return CSVUtils.joinAndEscape(mappings, null, this.mapValueDelimiter, false);
        }
        return "";
    }


    public AbstractValueTranslator getKeyTranslator()
    {
        return this.keyTranslator;
    }


    public AbstractValueTranslator getValueTranslator()
    {
        return this.valueTranslator;
    }


    private String convertToString(Object value, AbstractValueTranslator abstractValueTranslator)
    {
        if(abstractValueTranslator == null)
        {
            try
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(baos);
                objectOutputStream.writeObject(value);
                objectOutputStream.close();
                return Base64.encodeBytes(baos.toByteArray(), 8);
            }
            catch(NotSerializableException e)
            {
                return null;
            }
            catch(IOException e)
            {
                throw new JaloSystemException(e);
            }
        }
        return abstractValueTranslator.exportValue(value);
    }


    protected boolean isDelimiter(String collStr, int pos, char mapValueDelimiter)
    {
        return (this.mapValueDelimiter == mapValueDelimiter);
    }


    public static AbstractValueTranslator createKeyTranslator(MapType mapType) throws HeaderValidationException
    {
        return createKeyTranslator(mapType, null);
    }


    public static AbstractValueTranslator createKeyTranslator(MapType mapType, List<AbstractDescriptor.ColumnParams>[] patternLists) throws HeaderValidationException
    {
        AbstractValueTranslator abstractValueTranslator = null;
        if(patternLists == null || patternLists.length == 0 || patternLists[0].isEmpty())
        {
            abstractValueTranslator = getTranslator(mapType.getArgumentType(null));
        }
        else
        {
            if(patternLists.length > 1)
            {
                throw new JaloInvalidParameterException("alternative expressions are not allowed for map type attributes", 0);
            }
            for(int i = 0; i < patternLists[0].size(); i++)
            {
                AbstractDescriptor.ColumnParams columnParamLine = patternLists[0].get(i);
                if(columnParamLine.getQualifier().trim().equalsIgnoreCase("key"))
                {
                    AbstractDescriptor.ColumnParams columnParams = patternLists[0].get(i);
                    List[] keyPatternLists = columnParams.getItemPatternLists();
                    abstractValueTranslator = getTranslator(mapType.getArgumentType(null), (List<AbstractDescriptor.ColumnParams>[])keyPatternLists);
                }
            }
        }
        return (abstractValueTranslator != null) ? abstractValueTranslator : getTranslator((Type)mapType, patternLists);
    }


    public static AbstractValueTranslator createValueTranslator(MapType mapType) throws HeaderValidationException
    {
        return createValueTranslator(mapType, null);
    }


    public static AbstractValueTranslator createValueTranslator(MapType mapType, List<AbstractDescriptor.ColumnParams>[] patternLists) throws HeaderValidationException
    {
        AbstractValueTranslator abstractValueTranslator = null;
        if(patternLists == null || patternLists.length == 0 || patternLists[0].isEmpty())
        {
            abstractValueTranslator = getTranslator(mapType.getReturnType(null));
        }
        else
        {
            if(patternLists.length > 1)
            {
                throw new JaloInvalidParameterException("alternative expressions are not allowed for map type attributes", 0);
            }
            for(int i = 0; i < patternLists[0].size(); i++)
            {
                AbstractDescriptor.ColumnParams columnParamsLine = patternLists[0].get(i);
                if(columnParamsLine.getQualifier().trim().equalsIgnoreCase("value"))
                {
                    AbstractDescriptor.ColumnParams columnParams = patternLists[0].get(i);
                    List[] arrayOfList = columnParams.getItemPatternLists();
                    abstractValueTranslator = getTranslator(mapType.getReturnType(null), (List<AbstractDescriptor.ColumnParams>[])arrayOfList);
                }
            }
        }
        return (abstractValueTranslator != null) ? abstractValueTranslator : getTranslator((Type)mapType, patternLists);
    }


    public static AbstractValueTranslator getTranslator(Type type, List<AbstractDescriptor.ColumnParams>[] patternLists) throws HeaderValidationException
    {
        if(type instanceof AtomicType)
        {
            return AbstractValueTranslator.createTranslator(null, (AtomicType)type, (List[])patternLists);
        }
        if(type instanceof ComposedType)
        {
            return AbstractValueTranslator.createTranslator((ComposedType)type, (List[])patternLists);
        }
        if(type instanceof CollectionType)
        {
            return AbstractValueTranslator.createTranslator(null, (CollectionType)type, (List[])patternLists);
        }
        if(type instanceof MapType)
        {
            return (AbstractValueTranslator)new AtomicValueTranslator(Object.class);
        }
        throw new JaloSystemException("unsupported type! couldn't created valuetranslator for '" + (
                        (type != null) ? type.getCode() : "<null>"));
    }


    public static AbstractValueTranslator getTranslator(Type type) throws HeaderValidationException
    {
        return getTranslator(type, null);
    }
}
