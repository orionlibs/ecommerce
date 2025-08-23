package com.hybris.backoffice.excel.importing.parser;

import com.hybris.backoffice.excel.data.ImportParameters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RangeParserUtilsTest
{
    @Test
    public void shouldPrependFromPrefix()
    {
        String input = "input";
        String output = RangeParserUtils.prependFromPrefix("input");
        Assertions.assertThat(output).isEqualTo("from$input");
    }


    @Test
    public void shouldPrependToPrefix()
    {
        String input = "input";
        String output = RangeParserUtils.prependToPrefix("input");
        Assertions.assertThat(output).isEqualTo("to$input");
    }


    @Test
    public void shouldExceptionBeThrownWhenInputDoesntMatchToRangePattern()
    {
        String input = "RAGNE[from;to]";
        Assertions.assertThatThrownBy(() -> RangeParserUtils.parseRangePattern("RAGNE[from;to]")).isInstanceOf(ExcelParserException.class);
    }


    @Test
    public void shouldRangePatternBeSplitCorrectly()
    {
        String left = "left";
        String right = "right";
        String input = "RANGE[left;right]";
        Pair<String, String> output = RangeParserUtils.parseRangePattern("RANGE[left;right]");
        Assertions.assertThat((String)output.getLeft()).isEqualTo("left");
        Assertions.assertThat((String)output.getRight()).isEqualTo("right");
    }


    @Test
    public void shouldExceptionNotBeThrownWhenTheresNoRangeDelimiter()
    {
        String input = "left";
        Pair<String, String> output = RangeParserUtils.splitByRangeSeparator("left");
        Assertions.assertThat((String)output.getLeft()).isEqualTo("left");
        Assertions.assertThat((String)output.getRight()).isEqualTo("");
    }


    @Test
    public void shouldDeleteFromPrefix()
    {
        String val = "val";
        String input = "from$val";
        String output = RangeParserUtils.deleteFromPrefix("from$val");
        Assertions.assertThat(output).isEqualTo("val");
    }


    @Test
    public void shouldDeleteToPrefix()
    {
        String val = "val";
        String input = "to$val";
        String output = RangeParserUtils.deleteToPrefix("to$val");
        Assertions.assertThat(output).isEqualTo("val");
    }


    @Test
    public void shouldDeletePrefixFromImportParameters()
    {
        List<Map<String, String>> params = createParamsWithPrefix(RangeParserUtils.RangeBounds.FROM);
        ImportParameters importParameters = new ImportParameters(null, null, null, null, params);
        ImportParameters output = RangeParserUtils.deletePrefixFromImportParameters(importParameters, RangeParserUtils.RangeBounds.FROM);
        output.getMultiValueParameters().forEach(p -> Assertions.assertThat(p.entrySet().stream().filter(()).collect((Collector)getMapCollector())).isEmpty());
    }


    @Test
    public void shouldDeletePrefixFromParsedValues()
    {
        List<Map<String, String>> params = createParamsWithPrefix(RangeParserUtils.RangeBounds.FROM);
        ParsedValues parsedValues = new ParsedValues("any", params);
        ParsedValues output = RangeParserUtils.deletePrefixFromParsedValues(parsedValues, RangeParserUtils.RangeBounds.FROM);
        output.getParameters().forEach(p -> Assertions.assertThat(p.entrySet().stream().filter(()).collect((Collector)getMapCollector())).isEmpty());
    }


    @Test
    public void shouldAppendPrefixToParsedValues()
    {
        List<Map<String, String>> params = createParamsWithPrefix(null);
        ParsedValues parsedValues = new ParsedValues("any", params);
        ParsedValues output = RangeParserUtils.appendPrefixToParsedValues(parsedValues, RangeParserUtils.RangeBounds.FROM);
        output.getParameters().forEach(p -> Assertions.assertThat(((Map)p.entrySet().stream().filter(()).collect((Collector)getMapCollector())).size()).isEqualTo(1));
    }


    private List<Map<String, String>> createParamsWithPrefix(RangeParserUtils.RangeBounds rangeBounds)
    {
        UnaryOperator<String> convert = val -> (rangeBounds == null) ? val : ((rangeBounds == RangeParserUtils.RangeBounds.FROM) ? RangeParserUtils.prependFromPrefix(val) : RangeParserUtils.prependToPrefix(val));
        Map<String, String> map1 = new HashMap<>();
        map1.put(convert.apply("rawValue"), "val1");
        map1.put(convert.apply("key1"), "val2");
        Map<String, String> map2 = new HashMap<>();
        map2.put(convert.apply("rawValue"), "val1");
        map2.put(convert.apply("key2"), "val2");
        return Lists.newArrayList((Object[])new Map[] {map1, map2});
    }


    private Collector<Map.Entry<String, String>, ?, Map<String, String>> getMapCollector()
    {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
    }
}
