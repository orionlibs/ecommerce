package de.hybris.platform.servicelayer.impex.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.servicelayer.impex.ImpExHeaderError;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang.builder.ToStringBuilder;

public class HeaderError extends AbstractImpExError implements ImpExHeaderError
{
    private final HeaderDescriptor headerDescriptor;


    public HeaderError(HeaderDescriptor headerDescriptor)
    {
        this.headerDescriptor = Objects.<HeaderDescriptor>requireNonNull(headerDescriptor, "headerDescriptor is required");
    }


    public String getItemType()
    {
        return this.headerDescriptor.getTypeCode();
    }


    public String getErrorMessage()
    {
        HeaderValidationException ex = this.headerDescriptor.getInvalidHeaderException();
        return (ex == null) ? null : ex.getMessage();
    }


    public HeaderDescriptor getHeader()
    {
        return this.headerDescriptor;
    }


    public Exception getException()
    {
        return (Exception)this.headerDescriptor.getInvalidHeaderException();
    }


    public Map<Integer, String> getSource()
    {
        return (Map<Integer, String>)ImmutableMap.builder().putAll(this.headerDescriptor.dump()).build();
    }


    protected EnumerationValue getInternalMode()
    {
        return this.headerDescriptor.getMode();
    }


    public String toString()
    {
        return (new ToStringBuilder(this))
                        .append("headerDescriptor", this.headerDescriptor)
                        .append("itemType", getItemType())
                        .append("errorMessage", getErrorMessage())
                        .append("source", getSource())
                        .append("mode", getMode())
                        .toString();
    }
}
