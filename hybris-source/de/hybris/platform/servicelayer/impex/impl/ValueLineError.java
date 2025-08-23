package de.hybris.platform.servicelayer.impex.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.servicelayer.impex.ImpExValueLineError;
import de.hybris.platform.servicelayer.impex.ProcessMode;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;

public class ValueLineError extends AbstractImpExError implements ImpExValueLineError
{
    private static final Pattern NOT_EXISTING_ITEM_PATTERN = Pattern.compile(".*no existing item found for.*");
    private static final Pattern REFERENCE_VIOLATION_PATTERN = Pattern.compile("(?:.*could not resolve item for.*)|(?:.*cannot resolve value '[^']*'.*)");
    private static final Pattern CANNOT_REMOVE_ITEM_PATTERN = Pattern.compile("(.*could not remove item.*)|(.*contains non removeable.*)");
    private static final Pattern NOT_PROCESSED_CONFLICTING_PATTERN = Pattern.compile(".*ambiguous unique keys.*");
    private static final Pattern INVALID_DATA_FORMAT_PATTERN = Pattern.compile(".*cannot parse \\w+ '[^']*' with format specified pattern.*");
    private static final Map<Pattern, ImpExValueLineError.ImpExLineErrorType> ADDITIONAL_ERROR_TYPES = (Map<Pattern, ImpExValueLineError.ImpExLineErrorType>)ImmutableMap.of(NOT_EXISTING_ITEM_PATTERN, ImpExValueLineError.ImpExLineErrorType.NOT_EXISTING_ITEM, REFERENCE_VIOLATION_PATTERN,
                    ImpExValueLineError.ImpExLineErrorType.REFERENCE_VIOLATION, CANNOT_REMOVE_ITEM_PATTERN, ImpExValueLineError.ImpExLineErrorType.CANNOT_REMOVE_ITEM, NOT_PROCESSED_CONFLICTING_PATTERN, ImpExValueLineError.ImpExLineErrorType.NOT_PROCESSED_CONFLICTING, INVALID_DATA_FORMAT_PATTERN,
                    ImpExValueLineError.ImpExLineErrorType.INVALID_DATA_FORMAT);
    private final ValueLine valueLine;


    public ValueLineError(ValueLine valueLine)
    {
        this.valueLine = Objects.<ValueLine>requireNonNull(valueLine, "valueLine is required");
    }


    public String getItemType()
    {
        return this.valueLine.getHeader().getTypeCode();
    }


    public String getErrorMessage()
    {
        String errorMessage = StringUtils.isEmpty(this.valueLine.getPlainUnresolvedReason()) ? getHeaderErrorMessage() : this.valueLine.getPlainUnresolvedReason();
        return (errorMessage == null) ? "" : errorMessage;
    }


    private String getHeaderErrorMessage()
    {
        HeaderDescriptor header = this.valueLine.getHeader();
        HeaderValidationException ex = header.getInvalidHeaderException();
        return (ex == null) ? null : ex.getMessage();
    }


    public Map<Integer, String> getSource()
    {
        return this.valueLine.dump();
    }


    public ValueLine getValueLine()
    {
        return this.valueLine;
    }


    public ValueLine getLine()
    {
        return this.valueLine;
    }


    public ImpExValueLineError.ImpExLineErrorType getErrorType()
    {
        Optional<ImpExValueLineError.ImpExLineErrorType> errorType = determineErrorTypeFromHeader();
        if(errorType.isPresent())
        {
            return errorType.get();
        }
        errorType = determineErrorTypeFromMessage();
        if(errorType.isPresent())
        {
            return errorType.get();
        }
        errorType = determineErrorTypeFromValueLineState();
        if(errorType.isPresent())
        {
            return errorType.get();
        }
        return ImpExValueLineError.ImpExLineErrorType.UNKNOWN;
    }


    private Optional<ImpExValueLineError.ImpExLineErrorType> determineErrorTypeFromHeader()
    {
        HeaderDescriptor header = this.valueLine.getHeader();
        if(header == null || header.getInvalidHeaderException() != null)
        {
            return Optional.of(ImpExValueLineError.ImpExLineErrorType.WRONG_OR_MISSING_HEADER);
        }
        return Optional.empty();
    }


    private Optional<ImpExValueLineError.ImpExLineErrorType> determineErrorTypeFromMessage()
    {
        Optional<Map.Entry<Pattern, ImpExValueLineError.ImpExLineErrorType>> found = ADDITIONAL_ERROR_TYPES.entrySet().stream().filter(e -> matchesErrorMessage((Pattern)e.getKey())).findFirst();
        return found.isPresent() ? Optional.<ImpExValueLineError.ImpExLineErrorType>of((ImpExValueLineError.ImpExLineErrorType)((Map.Entry)found.get()).getValue()) : Optional.<ImpExValueLineError.ImpExLineErrorType>empty();
    }


    private boolean matchesErrorMessage(Pattern pattern)
    {
        Matcher matcher = pattern.matcher(getErrorMessage());
        return matcher.matches();
    }


    private Optional<ImpExValueLineError.ImpExLineErrorType> determineErrorTypeFromValueLineState()
    {
        Optional<ImpExValueLineError.ImpExLineErrorType> errorType;
        ProcessMode mode = getMode();
        if(mode == ProcessMode.UPDATE || mode == ProcessMode.INSERT_UPDATE)
        {
            if(this.valueLine.getProcessedItemPK() == null)
            {
                errorType = Optional.of(ImpExValueLineError.ImpExLineErrorType.NOT_PROCESSED);
            }
            else
            {
                errorType = Optional.of(ImpExValueLineError.ImpExLineErrorType.PARTIALLY_PROCESSED);
            }
        }
        else if(mode == ProcessMode.INSERT)
        {
            if(this.valueLine.getConflictingItemPk() != null)
            {
                errorType = Optional.of(ImpExValueLineError.ImpExLineErrorType.NOT_PROCESSED_CONFLICTING);
            }
            else
            {
                errorType = Optional.of(ImpExValueLineError.ImpExLineErrorType.NOT_PROCESSED_WITH_ERROR);
            }
        }
        else
        {
            errorType = Optional.empty();
        }
        return errorType;
    }


    protected EnumerationValue getInternalMode()
    {
        return this.valueLine.getHeader().getMode();
    }


    public String toString()
    {
        return (new ToStringBuilder(this))
                        .append("valueLine", this.valueLine)
                        .append("itemType", getItemType())
                        .append("errorMessage", getErrorMessage())
                        .append("source", getSource())
                        .append("errorType", getErrorType())
                        .toString();
    }
}
