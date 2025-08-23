package com.hybris.backoffice.excel.data;

import com.hybris.backoffice.excel.util.ExcelUtils;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

public class SelectedAttribute implements Comparable<SelectedAttribute>
{
    @Deprecated(since = "1808", forRemoval = true)
    public static final String REFERENCE_PATTERN_SEPARATOR = ":";
    private String isoCode;
    private String referenceFormat;
    private String defaultValues;
    private AttributeDescriptorModel attributeDescriptor;


    public SelectedAttribute()
    {
    }


    public SelectedAttribute(AttributeDescriptorModel attributeDescriptor)
    {
        this(null, attributeDescriptor);
    }


    public SelectedAttribute(String isoCode, AttributeDescriptorModel attributeDescriptor)
    {
        this(isoCode, null, null, attributeDescriptor);
    }


    public SelectedAttribute(String isoCode, String referenceFormat, String defaultValues, AttributeDescriptorModel attributeDescriptor)
    {
        this.isoCode = isoCode;
        this.referenceFormat = referenceFormat;
        this.defaultValues = defaultValues;
        this.attributeDescriptor = attributeDescriptor;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public Map<String, String> findDefaultValues()
    {
        Map<String, String> defaultValuesMap = new LinkedHashMap<>();
        String rawDefaultValues = (this.defaultValues != null) ? this.defaultValues : "";
        if(StringUtils.isBlank(this.referenceFormat))
        {
            return defaultValuesMap;
        }
        String[] referenceFormatTokens = ExcelUtils.extractExcelCellTokens(this.referenceFormat);
        String[] defaultValuesTokens = ExcelUtils.extractExcelCellTokens(rawDefaultValues);
        for(int i = 0; i < referenceFormatTokens.length; i++)
        {
            String referenceFormatToken = referenceFormatTokens[i];
            String defaultValueToken = (defaultValuesTokens.length > i && StringUtils.isNotBlank(defaultValuesTokens[i])) ? defaultValuesTokens[i] : null;
            defaultValuesMap.put(referenceFormatToken, defaultValueToken);
        }
        return defaultValuesMap;
    }


    public String getIsoCode()
    {
        return this.isoCode;
    }


    public void setIsoCode(String isoCode)
    {
        this.isoCode = isoCode;
    }


    public String getReferenceFormat()
    {
        return this.referenceFormat;
    }


    public void setReferenceFormat(String referenceFormat)
    {
        this.referenceFormat = referenceFormat;
    }


    public String getDefaultValues()
    {
        return this.defaultValues;
    }


    public void setDefaultValues(String defaultValues)
    {
        this.defaultValues = defaultValues;
    }


    public AttributeDescriptorModel getAttributeDescriptor()
    {
        return this.attributeDescriptor;
    }


    public void setAttributeDescriptor(AttributeDescriptorModel attributeDescriptor)
    {
        this.attributeDescriptor = attributeDescriptor;
    }


    public boolean isRequired(String currentLanguageIsoCode)
    {
        return ((isRequiredWithoutDefaultValue() || isUnique()) &&
                        isNotLocalizedOrLocalizedForCurrentLanguage(currentLanguageIsoCode));
    }


    private boolean isNotLocalizedOrLocalizedForCurrentLanguage(String currentLanguageIsoCode)
    {
        return (!isLocalized() || StringUtils.equals(getIsoCode(), currentLanguageIsoCode));
    }


    private boolean isUnique()
    {
        return BooleanUtils.isTrue(Boolean.valueOf((this.attributeDescriptor.getUnique().booleanValue() || this.attributeDescriptor
                        .getEnclosingType().getUniqueKeyAttributes().contains(this.attributeDescriptor))));
    }


    private boolean isRequiredWithoutDefaultValue()
    {
        return (BooleanUtils.isFalse(this.attributeDescriptor.getOptional()) && this.attributeDescriptor.getDefaultValue() == null);
    }


    public String getName()
    {
        return StringUtils.isNotEmpty(this.attributeDescriptor.getName()) ? this.attributeDescriptor.getName() : getQualifier();
    }


    public String getQualifier()
    {
        return this.attributeDescriptor.getQualifier();
    }


    public boolean isLocalized()
    {
        return BooleanUtils.isTrue(this.attributeDescriptor.getLocalized());
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
        SelectedAttribute that = (SelectedAttribute)o;
        if((this.isoCode != null) ? !this.isoCode.equals(that.isoCode) : (that.isoCode != null))
        {
            return false;
        }
        if((this.referenceFormat != null) ? !this.referenceFormat.equals(that.referenceFormat) : (that.referenceFormat != null))
        {
            return false;
        }
        if((this.defaultValues != null) ? !this.defaultValues.equals(that.defaultValues) : (that.defaultValues != null))
        {
            return false;
        }
        if(this.attributeDescriptor == null || that.getAttributeDescriptor() == null)
        {
            return false;
        }
        return ObjectUtils.equals(that.getAttributeDescriptor().getQualifier(), this.attributeDescriptor.getQualifier());
    }


    public int hashCode()
    {
        int result = (this.isoCode != null) ? this.isoCode.hashCode() : 0;
        result = 31 * result + ((this.referenceFormat != null) ? this.referenceFormat.hashCode() : 0);
        result = 31 * result + ((this.defaultValues != null) ? this.defaultValues.hashCode() : 0);
        result = 31 * result + ((this.attributeDescriptor != null) ? ((this.attributeDescriptor.getQualifier() != null) ? this.attributeDescriptor.getQualifier().hashCode() : 0) : 0);
        return result;
    }


    public int compareTo(SelectedAttribute o)
    {
        return getName().compareTo(o.getName());
    }
}
