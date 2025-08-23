package com.hybris.backoffice.excel.importing.parser;

import java.util.List;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.OrderComparator;

public class DefaultParserRegistry implements ParserRegistry
{
    private List<ImportParameterParser> parsers;


    public ImportParameterParser getParser(@Nonnull String referenceFormat)
    {
        return (ImportParameterParser)this.parsers.stream()
                        .filter(parser -> parser.matches(referenceFormat))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("There's no parser for given value!"));
    }


    @Required
    public void setParsers(List<ImportParameterParser> parsers)
    {
        this.parsers = parsers;
        OrderComparator.sort(this.parsers);
    }
}
