package com.hybris.backoffice.excel.importing.parser.splitter;

import java.util.function.Function;
import javax.annotation.Nonnull;

@FunctionalInterface
public interface ExcelParserSplitter extends Function<String, String[]>
{
    String[] apply(@Nonnull String paramString);
}
