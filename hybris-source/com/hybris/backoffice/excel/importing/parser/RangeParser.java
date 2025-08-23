package com.hybris.backoffice.excel.importing.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import javax.annotation.Nonnull;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Required;

public class RangeParser implements ImportParameterParser
{
    private int order = 1000;
    private ParserRegistry parserRegistry;


    public boolean matches(@Nonnull String referenceFormat)
    {
        return RangeParserUtils.RANGE_PATTERN.matcher(referenceFormat).matches();
    }


    public ParsedValues parseValue(@Nonnull String referenceFormat, String defaultValues, String values)
    {
        if(StringUtils.isNotBlank(defaultValues) && StringUtils.isBlank(values))
        {
            DefaultValues dv = parseDefaultValues(referenceFormat, "");
            return parseValue(defaultValues, dv);
        }
        DefaultValues parsedDefaultValues = parseDefaultValues(referenceFormat, defaultValues);
        return parseValue(values, parsedDefaultValues);
    }


    public DefaultValues parseDefaultValues(String referenceFormat, String defaultValues)
    {
        String trimmedReferenceFormat = StringUtils.trim(referenceFormat);
        String trimmedDefaultValues = StringUtils.trim(defaultValues);
        Pair<String, String> referenceFormatOfRange = RangeParserUtils.parseRangePattern(trimmedReferenceFormat);
        String referenceFormatOfFrom = (String)referenceFormatOfRange.getLeft();
        String referenceFormatOfTo = (String)referenceFormatOfRange.getRight();
        String defaultValueOfFrom = "";
        String defaultValueOfTo = "";
        if(StringUtils.isNotBlank(trimmedDefaultValues))
        {
            Pair<String, String> defaultValuesOfRange = RangeParserUtils.parseRangePattern(trimmedDefaultValues);
            defaultValueOfFrom = (String)defaultValuesOfRange.getLeft();
            defaultValueOfTo = (String)defaultValuesOfRange.getRight();
        }
        ImportParameterParser parser = this.parserRegistry.getParser(referenceFormatOfFrom);
        DefaultValues defaultValuesOfFrom = RangeParserUtils.appendPrefixToDefaultValues(parser
                        .parseDefaultValues(referenceFormatOfFrom, defaultValueOfFrom), RangeParserUtils.RangeBounds.FROM);
        DefaultValues defaultValuesOfTo = RangeParserUtils.appendPrefixToDefaultValues(parser
                        .parseDefaultValues(referenceFormatOfTo, defaultValueOfTo), RangeParserUtils.RangeBounds.TO);
        return mergeDefaultValues(defaultValuesOfFrom, defaultValuesOfTo);
    }


    protected DefaultValues mergeDefaultValues(DefaultValues from, DefaultValues to)
    {
        Map<String, String> parsedValues = new LinkedHashMap<>(from.toMap());
        parsedValues.putAll(to.toMap());
        String rangeFormat = "%s;%s";
        String referenceFormat = String.format("%s;%s", new Object[] {from.getReferenceFormat(), to.getReferenceFormat()});
        String defaultValues = (StringUtils.isBlank(from.getDefaultValues()) && StringUtils.isBlank(to.getDefaultValues())) ? "" : String.format("%s;%s", new Object[] {from.getDefaultValues(), to.getDefaultValues()});
        return new DefaultValues(defaultValues, referenceFormat, parsedValues);
    }


    public ParsedValues parseValue(String cellValue, DefaultValues defaultValues)
    {
        if(StringUtils.isBlank(cellValue) && StringUtils.isBlank(defaultValues.getDefaultValues()))
        {
            return new ParsedValues("", new ArrayList());
        }
        ParsedValues parsedValuesOfLeft = RangeParserUtils.appendPrefixToParsedValues(getLeftParsedValues(cellValue, defaultValues), RangeParserUtils.RangeBounds.FROM);
        ParsedValues parsedValuesOfRight = RangeParserUtils.appendPrefixToParsedValues(getRightParsedValues(cellValue, defaultValues), RangeParserUtils.RangeBounds.TO);
        List<Map<String, String>> params = ListUtils.union(parsedValuesOfLeft.getParameters(), parsedValuesOfRight
                        .getParameters());
        return new ParsedValues(cellValue, params);
    }


    protected ParsedValues getLeftParsedValues(String cellValue, DefaultValues defaultValues)
    {
        return getParsedValues(cellValue, defaultValues, RangeParserUtils.RangeBounds.FROM);
    }


    protected ParsedValues getRightParsedValues(String cellValue, DefaultValues defaultValues)
    {
        return getParsedValues(cellValue, defaultValues, RangeParserUtils.RangeBounds.TO);
    }


    private ParsedValues getParsedValues(String cellValue, DefaultValues defaultValues, RangeParserUtils.RangeBounds rangeType)
    {
        String prefixToSearchFor = (rangeType == RangeParserUtils.RangeBounds.FROM) ? "from$" : "to$";
        Function<Pair<String, String>, String> getFromOrTo = pair -> (rangeType == RangeParserUtils.RangeBounds.FROM) ? (String)pair.getLeft() : (String)pair.getRight();
        Map<String, String> parsedDefaultValues = (Map<String, String>)defaultValues.toMap().entrySet().stream().filter(entry -> StringUtils.contains((CharSequence)entry.getKey(), prefixToSearchFor))
                        .collect(LinkedHashMap::new, (newMap, convertedMap) -> newMap.put((String)convertedMap.getKey(), (String)convertedMap.getValue()), HashMap::putAll);
        DefaultValues newDefaultValues = new DefaultValues(getFromOrTo.apply(RangeParserUtils.splitByRangeSeparator(defaultValues.getDefaultValues())), getFromOrTo.apply(RangeParserUtils.splitByRangeSeparator(defaultValues.getReferenceFormat())), parsedDefaultValues);
        UnaryOperator<String> converter = val -> RangeParserUtils.deletePrefix(val, prefixToSearchFor);
        ImportParameterParser parser = this.parserRegistry.getParser(RangeParserUtils.convert(newDefaultValues.getReferenceFormat(), converter));
        if(StringUtils.contains(cellValue, ","))
        {
            String[] multiCellValue = cellValue.split(",");
            List<Map<String, String>> parameters = new ArrayList<>();
            for(String singleCellValue : multiCellValue)
            {
                ParsedValues singleParsedValues = parser.parseValue(getFromOrTo
                                .apply(RangeParserUtils.parseRangePattern(singleCellValue)), copyDefaultValues(newDefaultValues));
                parameters.addAll(singleParsedValues.getParameters());
            }
            return new ParsedValues(cellValue, parameters);
        }
        return parser.parseValue(getFromOrTo
                        .apply(RangeParserUtils.parseRangePattern(
                                        StringUtils.isBlank(cellValue) ? String.format("RANGE[%s]", new Object[] {defaultValues.getDefaultValues()}) : cellValue)), newDefaultValues);
    }


    private static DefaultValues copyDefaultValues(DefaultValues defaultValues)
    {
        Map<String, String> copiedParams = new LinkedHashMap<>(defaultValues.toMap());
        return new DefaultValues(defaultValues.getDefaultValues(), defaultValues.getReferenceFormat(), copiedParams);
    }


    @Required
    public void setParserRegistry(ParserRegistry parserRegistry)
    {
        this.parserRegistry = parserRegistry;
    }


    public int getOrder()
    {
        return this.order;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }
}
