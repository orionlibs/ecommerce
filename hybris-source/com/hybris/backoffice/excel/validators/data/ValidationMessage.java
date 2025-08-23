package com.hybris.backoffice.excel.validators.data;

import de.hybris.platform.validation.enums.Severity;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ValidationMessage implements Serializable, Comparable<ValidationMessage>
{
    private Severity severity;
    private final String messageKey;
    private final Serializable[] params;
    private final transient Map<String, Object> metadata;


    public ValidationMessage(String messageKey, Serializable... params)
    {
        this.messageKey = messageKey;
        this.params = params;
        this.metadata = new HashMap<>();
        this.severity = Severity.ERROR;
    }


    public ValidationMessage(String messageKey)
    {
        this.messageKey = messageKey;
        this.params = new Serializable[0];
        this.metadata = new HashMap<>();
        this.severity = Severity.ERROR;
    }


    public ValidationMessage(String messageKey, Severity severity)
    {
        this(messageKey);
        this.severity = severity;
    }


    public ValidationMessage(String messageKey, Severity severity, Serializable... params)
    {
        this(messageKey, params);
        this.severity = severity;
    }


    public String getMessageKey()
    {
        return this.messageKey;
    }


    public Serializable[] getParams()
    {
        return this.params;
    }


    public void addMetadata(String key, Object value)
    {
        this.metadata.put(key, value);
    }


    public void addMetadataIfAbsent(String key, Object value)
    {
        this.metadata.putIfAbsent(key, value);
    }


    public Object getMetadata(String key)
    {
        return this.metadata.get(key);
    }


    public boolean containsMetadata(String key)
    {
        return this.metadata.containsKey(key);
    }


    public Severity getSeverity()
    {
        return this.severity;
    }


    public int compareTo(ValidationMessage another)
    {
        Integer messageKeyComparision = Integer.valueOf(compareMessageKeys(another));
        if(messageKeyComparision.intValue() != 0)
        {
            return messageKeyComparision.intValue();
        }
        int sheetNameComparision = compareSheetNames(another);
        if(sheetNameComparision != 0)
        {
            return sheetNameComparision;
        }
        return compareRows(another);
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        ValidationMessage that = (ValidationMessage)o;
        if((this.messageKey != null) ? !this.messageKey.equals(that.messageKey) : (that.messageKey != null))
        {
            return false;
        }
        return (Arrays.equals((Object[])this.params, (Object[])that.params) && ((this.metadata != null) ? this.metadata.equals(that.metadata) : (that.metadata == null)));
    }


    public int hashCode()
    {
        int result = (this.messageKey != null) ? this.messageKey.hashCode() : 0;
        result = 31 * result + Arrays.hashCode((Object[])this.params);
        result = 31 * result + ((this.metadata != null) ? this.metadata.hashCode() : 0);
        return result;
    }


    private int compareMessageKeys(ValidationMessage another)
    {
        if(this.messageKey == null)
        {
            return 1;
        }
        if(another.messageKey == null)
        {
            return -1;
        }
        return this.messageKey.compareTo(another.messageKey);
    }


    private int compareSheetNames(ValidationMessage another)
    {
        if(!containsMetadata("sheetName"))
        {
            return 1;
        }
        if(!another.containsMetadata("sheetName"))
        {
            return -1;
        }
        String firstSheetName = (String)getMetadata("sheetName");
        String secondSheetName = (String)another.getMetadata("sheetName");
        return firstSheetName.compareTo(secondSheetName);
    }


    private int compareRows(ValidationMessage another)
    {
        if(!containsMetadata("rowIndex"))
        {
            return 1;
        }
        if(!another.containsMetadata("rowIndex"))
        {
            return -1;
        }
        Integer firstRowIndex = (Integer)getMetadata("rowIndex");
        Integer secondRowIndex = (Integer)another.getMetadata("rowIndex");
        return firstRowIndex.compareTo(secondRowIndex);
    }
}
