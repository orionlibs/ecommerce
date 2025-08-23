package com.hybris.backoffice.excel.importing.parser;

import javax.annotation.Nonnull;

public interface ParserRegistry
{
    ImportParameterParser getParser(@Nonnull String paramString);
}
