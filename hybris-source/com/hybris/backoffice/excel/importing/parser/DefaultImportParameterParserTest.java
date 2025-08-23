package com.hybris.backoffice.excel.importing.parser;

import com.hybris.backoffice.excel.importing.parser.matcher.DefaultExcelParserMatcher;
import com.hybris.backoffice.excel.importing.parser.matcher.ExcelParserMatcher;
import com.hybris.backoffice.excel.importing.parser.splitter.DefaultExcelParserSplitter;
import com.hybris.backoffice.excel.importing.parser.splitter.ExcelParserSplitter;
import java.util.Map;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultImportParameterParserTest
{
    private DefaultImportParameterParser parser = new DefaultImportParameterParser();


    @Before
    public void setUp()
    {
        this.parser.setMatcher((ExcelParserMatcher)new DefaultExcelParserMatcher());
        this.parser.setSplitter((ExcelParserSplitter)new DefaultExcelParserSplitter());
    }


    @Test
    public void shouldParseDefaultValuesWhenAllParametersAreProvided()
    {
        String referenceFormat = "catalog:version";
        String defaultValues = "Clothing:Online";
        DefaultValues parsedDefaultValues = this.parser.parseDefaultValues("catalog:version", "Clothing:Online");
        Assertions.assertThat(parsedDefaultValues.getKeys()).contains(new Object[] {"catalog", "version"});
        Assertions.assertThat(parsedDefaultValues.getDefaultValue("catalog")).isEqualTo("Clothing");
        Assertions.assertThat(parsedDefaultValues.getDefaultValue("version")).isEqualTo("Online");
    }


    @Test
    public void shouldParseDefaultValuesWhenOnlyFirstParameterIsProvided()
    {
        String referenceFormat = "category:catalog:version";
        String defaultValues = "Yellow::";
        DefaultValues parsedDefaultValues = this.parser.parseDefaultValues("category:catalog:version", "Yellow::");
        Assertions.assertThat(parsedDefaultValues.getKeys()).contains(new Object[] {"category", "catalog", "version"});
        Assertions.assertThat(parsedDefaultValues.getDefaultValue("category")).isEqualTo("Yellow");
        Assertions.assertThat(parsedDefaultValues.getDefaultValue("catalog")).isNull();
        Assertions.assertThat(parsedDefaultValues.getDefaultValue("version")).isNull();
    }


    @Test
    public void shouldParseDefaultValuesWhenNoValueIsProvided()
    {
        String referenceFormat = "category:catalog:version";
        String defaultValues = "";
        DefaultValues parsedDefaultValues = this.parser.parseDefaultValues("category:catalog:version", "");
        Assertions.assertThat(parsedDefaultValues.getKeys()).contains(new Object[] {"category", "catalog", "version"});
        Assertions.assertThat(parsedDefaultValues.getDefaultValue("category")).isNull();
        Assertions.assertThat(parsedDefaultValues.getDefaultValue("catalog")).isNull();
        Assertions.assertThat(parsedDefaultValues.getDefaultValue("version")).isNull();
    }


    @Test
    public void shouldReturnEmptyMapWhenReferenceFormatIsEmpty()
    {
        String referenceFormat = "";
        String defaultValues = "Yellow:Clothing:Online";
        DefaultValues parsedDefaultValues = this.parser.parseDefaultValues("", "Yellow:Clothing:Online");
        Assertions.assertThat(parsedDefaultValues.getKeys()).isEmpty();
    }


    @Test
    public void shouldParseCellValueWhenAllParametersAreProvided()
    {
        String referenceFormat = "category:catalog:version";
        String defaultValues = "Yellow:Clothing:Online";
        String cellValue = "Yellow:Clothing:Online";
        ParsedValues parsedValues = this.parser.parseValue("category:catalog:version", "Yellow:Clothing:Online", "Yellow:Clothing:Online");
        Assertions.assertThat(parsedValues.getCellValue()).isEqualTo("Yellow:Clothing:Online");
        Assertions.assertThat(parsedValues.getParameters()).hasSize(1);
        Assertions.assertThat(((Map)parsedValues.getParameters().get(0)).keySet()).contains(new Object[] {"category", "catalog", "version"});
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(0)).get("category")).isEqualTo("Yellow");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(0)).get("catalog")).isEqualTo("Clothing");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(0)).get("version")).isEqualTo("Online");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(0)).get("rawValue")).isEqualTo("Yellow:Clothing:Online");
    }


    @Test
    public void shouldParseCellValueWhenOneParameterIsNotProvided()
    {
        String referenceFormat = "category:catalog:version";
        String defaultValues = "Yellow:Clothing:Online";
        String cellValue = "Yellow::Online";
        ParsedValues parsedValues = this.parser.parseValue("category:catalog:version", "Yellow:Clothing:Online", "Yellow::Online");
        Assertions.assertThat(parsedValues.getCellValue()).isEqualTo("Yellow:Clothing:Online");
        Assertions.assertThat(parsedValues.getParameters()).hasSize(1);
        Assertions.assertThat(((Map)parsedValues.getParameters().get(0)).keySet()).contains(new Object[] {"category", "catalog", "version"});
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(0)).get("category")).isEqualTo("Yellow");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(0)).get("catalog")).isEqualTo("Clothing");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(0)).get("version")).isEqualTo("Online");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(0)).get("rawValue")).isEqualTo("Yellow:Clothing:Online");
    }


    @Test
    public void shouldParseCellValueWhenNoParametersAreProvided()
    {
        String referenceFormat = "category:catalog:version";
        String defaultValues = "Yellow:Clothing:Online";
        String cellValue = "";
        ParsedValues parsedValues = this.parser.parseValue("category:catalog:version", "Yellow:Clothing:Online", "");
        Assertions.assertThat(parsedValues.getCellValue()).isEqualTo("Yellow:Clothing:Online");
        Assertions.assertThat(parsedValues.getParameters()).hasSize(1);
        Assertions.assertThat(((Map)parsedValues.getParameters().get(0)).keySet()).contains(new Object[] {"category", "catalog", "version"});
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(0)).get("category")).isEqualTo("Yellow");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(0)).get("catalog")).isEqualTo("Clothing");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(0)).get("version")).isEqualTo("Online");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(0)).get("rawValue")).isEqualTo("Yellow:Clothing:Online");
    }


    @Test
    public void shouldTakeDefaultValueWhenReferenceFormatIsNotProvided()
    {
        String referenceFormat = "";
        String defaultValues = "Yellow:Clothing:Online";
        String cellValue = "";
        ParsedValues parsedValues = this.parser.parseValue("", "Yellow:Clothing:Online", "");
        Assertions.assertThat(parsedValues.getCellValue()).isEqualTo("Yellow:Clothing:Online");
        Assertions.assertThat(parsedValues.getParameters()).hasSize(1);
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(0)).get("rawValue")).isEqualTo("Yellow:Clothing:Online");
    }


    @Test
    public void shouldParseCellMultiValueWhenNoAllParametersAreProvided()
    {
        String referenceFormat = "category:catalog:version";
        String defaultValues = "Yellow:Clothing:Online";
        String cellValue = "Black:Clothing2:,Green::Staged,::";
        ParsedValues parsedValues = this.parser.parseValue("category:catalog:version", "Yellow:Clothing:Online", "Black:Clothing2:,Green::Staged,::");
        Assertions.assertThat(parsedValues.getCellValue()).isEqualTo("Black:Clothing2:Online,Green:Clothing:Staged,Yellow:Clothing:Online");
        Assertions.assertThat(parsedValues.getParameters()).hasSize(3);
        Assertions.assertThat(((Map)parsedValues.getParameters().get(0)).keySet()).contains(new Object[] {"category", "catalog", "version"});
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(0)).get("category")).isEqualTo("Black");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(0)).get("catalog")).isEqualTo("Clothing2");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(0)).get("version")).isEqualTo("Online");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(0)).get("rawValue")).isEqualTo("Black:Clothing2:Online");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(1)).get("category")).isEqualTo("Green");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(1)).get("catalog")).isEqualTo("Clothing");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(1)).get("version")).isEqualTo("Staged");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(1)).get("rawValue")).isEqualTo("Green:Clothing:Staged");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(2)).get("category")).isEqualTo("Yellow");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(2)).get("catalog")).isEqualTo("Clothing");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(2)).get("version")).isEqualTo("Online");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(2)).get("rawValue")).isEqualTo("Yellow:Clothing:Online");
    }


    @Test
    public void shouldParseMultivalueCellWithoutReferenceFormat()
    {
        String referenceFormat = "";
        String defaultValues = "";
        String cellValue = "Black,Green,Yellow";
        ParsedValues parsedValues = this.parser.parseValue("", "", "Black,Green,Yellow");
        Assertions.assertThat(parsedValues.getCellValue()).isEqualTo("Black,Green,Yellow");
        Assertions.assertThat(parsedValues.getParameters()).hasSize(3);
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(0)).get("rawValue")).isEqualTo("Black");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(1)).get("rawValue")).isEqualTo("Green");
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(2)).get("rawValue")).isEqualTo("Yellow");
    }


    @Test
    public void shouldUseDefaultValueForDateType()
    {
        String referenceFormat = "";
        String defaultValues = "17.04.2018 07:23:12";
        String cellValue = "    ";
        ParsedValues parsedValues = this.parser.parseValue("", "17.04.2018 07:23:12", "    ");
        Assertions.assertThat(parsedValues.getCellValue()).isEqualTo("17.04.2018 07:23:12");
        Assertions.assertThat(parsedValues.getParameters()).hasSize(1);
        Assertions.assertThat((String)((Map)parsedValues.getParameters().get(0)).get("rawValue")).isEqualTo("17.04.2018 07:23:12");
    }
}
