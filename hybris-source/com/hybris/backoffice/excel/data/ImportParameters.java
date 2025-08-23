package com.hybris.backoffice.excel.data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

public class ImportParameters implements Serializable
{
    public static final String RAW_VALUE = "rawValue";
    public static final String MULTIVALUE_SEPARATOR = ",";
    private final String typeCode;
    private final String isoCode;
    private final Serializable cellValue;
    private final String entryRef;
    private final List<Map<String, String>> parameters;
    private final String formatError;


    public ImportParameters(String typeCode, String isoCode, Serializable cellValue, String entryRef, String formatError)
    {
        this.typeCode = typeCode;
        this.isoCode = isoCode;
        this.cellValue = cellValue;
        this.entryRef = entryRef;
        this.formatError = formatError;
        this.parameters = Collections.emptyList();
    }


    public ImportParameters(String typeCode, String isoCode, Serializable cellValue, String entryRef, List<Map<String, String>> parameters)
    {
        this.typeCode = typeCode;
        this.isoCode = isoCode;
        this.cellValue = cellValue;
        this.entryRef = entryRef;
        this.parameters = parameters;
        this.formatError = null;
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public String getIsoCode()
    {
        return this.isoCode;
    }


    public String getEntryRef()
    {
        return this.entryRef;
    }


    public Serializable getCellValue()
    {
        return this.cellValue;
    }


    public List<Map<String, String>> getMultiValueParameters()
    {
        return this.parameters;
    }


    public Map<String, String> getSingleValueParameters()
    {
        return !this.parameters.isEmpty() ? this.parameters.get(0) : (Map<String, String>)new HashedMap();
    }


    public boolean isCellValueBlank()
    {
        return (this.cellValue == null || StringUtils.isBlank(this.cellValue.toString()));
    }


    public boolean isCellValueNotBlank()
    {
        return !isCellValueBlank();
    }


    public boolean hasFormatErrors()
    {
        return (this.formatError != null);
    }


    public String getFormatError()
    {
        return this.formatError;
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
        ImportParameters that = (ImportParameters)o;
        if((this.typeCode != null) ? !this.typeCode.equals(that.typeCode) : (that.typeCode != null))
        {
            return false;
        }
        if((this.isoCode != null) ? !this.isoCode.equals(that.isoCode) : (that.isoCode != null))
        {
            return false;
        }
        if((this.cellValue != null) ? !this.cellValue.equals(that.cellValue) : (that.cellValue != null))
        {
            return false;
        }
        if((this.entryRef != null) ? !this.entryRef.equals(that.entryRef) : (that.entryRef != null))
        {
            return false;
        }
        if((this.parameters != null) ? !this.parameters.equals(that.parameters) : (that.parameters != null))
        {
            return false;
        }
        return (this.formatError != null) ? this.formatError.equals(that.formatError) : ((that.formatError == null));
    }


    public int hashCode()
    {
        int result = (this.typeCode != null) ? this.typeCode.hashCode() : 0;
        result = 31 * result + ((this.isoCode != null) ? this.isoCode.hashCode() : 0);
        result = 31 * result + ((this.cellValue != null) ? this.cellValue.hashCode() : 0);
        result = 31 * result + ((this.entryRef != null) ? this.entryRef.hashCode() : 0);
        result = 31 * result + ((this.parameters != null) ? this.parameters.hashCode() : 0);
        result = 31 * result + ((this.formatError != null) ? this.formatError.hashCode() : 0);
        return result;
    }
}
