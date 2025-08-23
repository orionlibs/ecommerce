package com.hybris.backoffice.excel.importing.parser;

import javax.annotation.Nonnull;
import org.springframework.core.Ordered;

public interface ImportParameterParser extends Ordered
{
    boolean matches(@Nonnull String paramString);


    DefaultValues parseDefaultValues(String paramString1, String paramString2);


    ParsedValues parseValue(String paramString, DefaultValues paramDefaultValues);


    default ParsedValues parseValue(@Nonnull String referenceFormat, String defaultValues, String values)
    {
        DefaultValues parsedDefaultValues = parseDefaultValues(referenceFormat, defaultValues);
        return parseValue(values, parsedDefaultValues);
    }
}
