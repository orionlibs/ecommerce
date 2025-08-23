package com.hybris.backoffice.excel.importing.parser.matcher;

import java.util.function.Predicate;
import javax.annotation.Nonnull;

@FunctionalInterface
public interface ExcelParserMatcher extends Predicate<String>
{
    boolean test(@Nonnull String paramString);
}
