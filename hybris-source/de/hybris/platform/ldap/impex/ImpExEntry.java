package de.hybris.platform.ldap.impex;

import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.CSVConstants;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class ImpExEntry
{
    private static final Logger LOG = Logger.getLogger(ImpExEntry.class.getName());
    public static final int VALUE_START_POSTION = 1;
    private int currentHeaderPos = 0;
    private int currentValueLinePos = 1;
    private HeaderMode mode;
    private final Map<Integer, String> headerAttributes;
    private final Map<Integer, String> valueLine;
    private String type;
    private String defaultHeaderImpExEntry;


    public ImpExEntry()
    {
        this(HeaderMode.INSERT_UPDATE);
    }


    public ImpExEntry(HeaderMode mode)
    {
        this(mode, "");
    }


    public ImpExEntry(HeaderMode mode, String type)
    {
        this.headerAttributes = new TreeMap<>();
        this.valueLine = new TreeMap<>();
        this.mode = mode;
        if(StringUtils.isNotBlank(type))
        {
            setTypeCode(type);
        }
    }


    public void setMode(HeaderMode mode)
    {
        this.mode = mode;
    }


    public HeaderMode getMode()
    {
        return this.mode;
    }


    public void setTypeCode(Class type)
    {
        setTypeCode(TypeManager.getInstance().getComposedType(type).getCode());
    }


    public void setTypeCode(String type)
    {
        this.type = type;
    }


    public String getTypeCode()
    {
        return this.type;
    }


    public void addValue(String ldapAttribute, int pos, String newValue)
    {
        String currentValue = this.valueLine.get(Integer.valueOf(pos));
        if(StringUtils.isNotBlank(currentValue))
        {
            this.valueLine.put(Integer.valueOf(pos), currentValue + "," + currentValue);
        }
        else
        {
            this.valueLine.put(Integer.valueOf(pos), newValue);
        }
    }


    public void addValues(String ldapAttribute, List<String> values)
    {
        addValues(ldapAttribute, this.currentValueLinePos++, values);
    }


    public void addValues(String ldapAttribute, int pos, List<String> values)
    {
        if(values == null || values.isEmpty())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Handling PLA-5941");
            }
            addValue(ldapAttribute, pos, "");
        }
        for(String value : values)
        {
            if(value instanceof String)
            {
                addValue(ldapAttribute, pos, value);
            }
        }
    }


    public void addImpExHeaderAttribute(String attribute)
    {
        addImpExHeaderAttribute(this.currentHeaderPos++, attribute);
    }


    public void addImpExHeaderAttribute(int pos, String attribute)
    {
        this.headerAttributes.put(Integer.valueOf(pos), attribute);
    }


    public String getHeader()
    {
        StringBuilder headerExpr = new StringBuilder();
        switch(null.$SwitchMap$de$hybris$platform$ldap$impex$ImpExEntry$HeaderMode[this.mode.ordinal()])
        {
            case 1:
                headerExpr.append("INSERT ");
                break;
            case 2:
                headerExpr.append("UPDATE ");
                break;
            case 3:
                headerExpr.append("INSERT_UPDATE ");
                break;
            default:
                headerExpr.append("<UNSUPPORTED> ");
                break;
        }
        headerExpr.append(getTypeCode()).append(CSVConstants.DEFAULT_FIELD_SEPARATOR).append(" ");
        for(String attribute : this.headerAttributes.values())
        {
            headerExpr.append(attribute).append(CSVConstants.DEFAULT_FIELD_SEPARATOR).append(" ");
        }
        if(StringUtils.isNotBlank(this.defaultHeaderImpExEntry))
        {
            headerExpr.append(this.defaultHeaderImpExEntry).append(CSVConstants.DEFAULT_FIELD_SEPARATOR).append(" ");
        }
        return headerExpr.toString();
    }


    public String getValueLine()
    {
        StringBuilder valueLineExpr = new StringBuilder();
        valueLineExpr.append(getTypeCode()).append(CSVConstants.DEFAULT_FIELD_SEPARATOR).append(" ");
        for(String value : this.valueLine.values())
        {
            valueLineExpr.append(value).append(CSVConstants.DEFAULT_FIELD_SEPARATOR).append(" ");
        }
        return valueLineExpr.toString();
    }


    public void addImpExHeaderDefault(String headerdefault)
    {
        this.defaultHeaderImpExEntry = headerdefault;
    }


    public String toString()
    {
        return "HEADER: " + getHeader() + "VALUE: " + getValueLine();
    }
}
