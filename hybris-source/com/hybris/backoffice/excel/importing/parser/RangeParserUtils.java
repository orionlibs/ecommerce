package com.hybris.backoffice.excel.importing.parser;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.util.Lists;

public class RangeParserUtils
{
    public static final String RANGE_DELIMITER = ";";
    public static final String RANGE_PREFIX = "RANGE[";
    public static final String RANGE_SUFFIX = "]";
    public static final String RANGE_FROM_PREFIX = "from$";
    public static final String RANGE_TO_PREFIX = "to$";
    public static final String COMPLEX_TYPE_RANGE_FORMAT = "RANGE[%1$s;%1$s]";
    public static final String SIMPLE_TYPE_RANGE_FORMAT = "RANGE[value;value]";
    public static final Pattern RANGE_PATTERN = Pattern.compile("RANGE\\[(?<from>.*);(?<to>.*)]");


    public static Pair<String, String> parseRangePattern(@Nonnull String input) throws ExcelParserException
    {
        Matcher matcher = RANGE_PATTERN.matcher(input);
        if(!matcher.matches())
        {
            throw new ExcelParserException(input, RANGE_PATTERN.pattern());
        }
        String from = matcher.group("from");
        String to = matcher.group("to");
        return (Pair<String, String>)ImmutablePair.of(from, to);
    }


    public static Pair<String, String> splitByRangeSeparator(@Nonnull String input) throws ExcelParserException
    {
        String[] arr = input.split(";");
        int maxRangeParts = 2;
        return (Pair<String, String>)ImmutablePair.of(arr[0], (arr.length == 2) ? arr[1] : "");
    }


    public static String prependFromPrefix(@Nonnull String input)
    {
        return "from$" + input;
    }


    public static String prependToPrefix(@Nonnull String input)
    {
        return "to$" + input;
    }


    public static String deleteFromPrefix(@Nonnull String input)
    {
        return deletePrefix(input, "from$");
    }


    public static String deleteToPrefix(@Nonnull String input)
    {
        return deletePrefix(input, "to$");
    }


    public static String deletePrefix(String input, String prefix)
    {
        return input.substring(prefix.length());
    }


    public static ImportParameters deletePrefixFromImportParameters(ImportParameters importParameters, RangeBounds rangeBounds)
    {
        List<Map<String, String>> newParams = (List<Map<String, String>>)importParameters.getMultiValueParameters().stream().map(params -> deletePrefixFromMapKey(params, rangeBounds)).filter(MapUtils::isNotEmpty).collect(Collectors.toList());
        return new ImportParameters(importParameters.getTypeCode(), importParameters.getIsoCode(), importParameters.getCellValue(), importParameters
                        .getEntryRef(), newParams);
    }


    public static ImportParameters getSingleImportParameters(ExcelClassificationAttribute excelClassificationAttribute, ImportParameters importParameters, Map<String, String> params, RangeBounds rangeBounds)
    {
        boolean isSimpleType = (excelClassificationAttribute.getAttributeAssignment().getAttributeType() != ClassificationAttributeTypeEnum.REFERENCE);
        Supplier<String> complexCellValue = () -> {
            Pair<String, String> pair = parseRangePattern((String)importParameters.getCellValue());
            return (rangeBounds == RangeBounds.FROM) ? (String)pair.getLeft() : (String)pair.getRight();
        };
        Supplier<String> rawValueKey = () -> (rangeBounds == RangeBounds.FROM) ? prependFromPrefix("rawValue") : prependToPrefix("rawValue");
        String cellValue = isSimpleType ? params.get(rawValueKey.get()) : complexCellValue.get();
        return deletePrefixFromImportParameters(new ImportParameters(importParameters.getTypeCode(), importParameters
                        .getIsoCode(), cellValue, importParameters.getEntryRef(), Lists.newArrayList((Object[])new Map[] {params})), rangeBounds);
    }


    public static ParsedValues appendPrefixToParsedValues(ParsedValues parsedValues, RangeBounds range)
    {
        UnaryOperator<String> converter = val -> appendPrefixToInput(val, range);
        return handlePrefixOfParsedValues(parsedValues, converter);
    }


    public static ParsedValues deletePrefixFromParsedValues(ParsedValues parsedValues, RangeBounds range)
    {
        UnaryOperator<String> converter = val -> deletePrefixFromInput(val, range);
        return handlePrefixOfParsedValues(parsedValues, converter);
    }


    private static ParsedValues handlePrefixOfParsedValues(ParsedValues parsedValues, UnaryOperator<String> converter)
    {
        List<Map<String, String>> newParameters = (List<Map<String, String>>)parsedValues.getParameters().stream().filter(map -> map.containsKey("rawValue")).peek(peek -> {
            String rawValue = (String)peek.get("rawValue");
            peek.remove("rawValue");
            String rawKey = converter.apply("rawValue");
            peek.put(rawKey, rawValue);
        }).collect(Collectors.toList());
        return new ParsedValues(parsedValues.getCellValue(), newParameters);
    }


    public static DefaultValues appendPrefixToDefaultValues(DefaultValues defaultValues, RangeBounds range)
    {
        UnaryOperator<String> converter = val -> appendPrefixToInput(val, range);
        return handlePrefixForDefaultValues(defaultValues, converter);
    }


    public static DefaultValues deletePrefixFromDefaultValues(DefaultValues defaultValues, RangeBounds range)
    {
        UnaryOperator<String> converter = val -> deletePrefixFromInput(val, range);
        return handlePrefixForDefaultValues(defaultValues, converter);
    }


    private static DefaultValues handlePrefixForDefaultValues(DefaultValues defaultValues, UnaryOperator<String> converter)
    {
        String newReferenceFormat = converter.apply(defaultValues.getReferenceFormat());
        Map<String, String> newParsedParameters = (Map<String, String>)defaultValues.toMap().entrySet().stream().collect(LinkedHashMap::new, (newMap, convertedMap) -> newMap.put(converter.apply((String)convertedMap.getKey()), (String)convertedMap.getValue()), HashMap::putAll);
        return new DefaultValues(defaultValues.getDefaultValues(), newReferenceFormat, newParsedParameters);
    }


    private static String appendPrefixToInput(String input, RangeBounds range)
    {
        UnaryOperator<String> converter = val -> (range == RangeBounds.FROM) ? prependFromPrefix(val) : prependToPrefix(val);
        return convert(input, converter);
    }


    private static String deletePrefixFromInput(String input, RangeBounds rangeBounds)
    {
        UnaryOperator<String> converter = val -> (rangeBounds == RangeBounds.FROM) ? deleteFromPrefix(val) : deleteToPrefix(val);
        return convert(input, converter);
    }


    public static String convert(String input, UnaryOperator<String> converter)
    {
        if(StringUtils.isBlank(input))
        {
            return input;
        }
        return Stream.<String>of(input.split(":")).map(converter)
                        .collect(Collectors.joining(":"));
    }


    private static Map<String, String> deletePrefixFromMapKey(Map<String, String> map, RangeBounds rangeBounds)
    {
        return (Map<String, String>)map.entrySet()
                        .stream()
                        .filter(entry -> containsPrefix((String)entry.getKey(), rangeBounds))
                        .collect(LinkedHashMap::new, (newMap, currentMap) -> newMap.put(deletePrefixFromInput((String)currentMap.getKey(), rangeBounds), (String)currentMap.getValue()), HashMap::putAll);
    }


    private static boolean containsPrefix(String input, RangeBounds rangeBounds)
    {
        String prefix = (rangeBounds == RangeBounds.FROM) ? "from$" : "to$";
        return StringUtils.contains(input, prefix);
    }
}
