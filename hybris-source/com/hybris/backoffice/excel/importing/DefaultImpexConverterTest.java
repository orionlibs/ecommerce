package com.hybris.backoffice.excel.importing;

import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class DefaultImpexConverterTest
{
    private final DefaultImpexConverter defaultImpexConverter = new DefaultImpexConverter();


    @Test
    public void shouldEscapeValueWhenItContainsNewLineN()
    {
        String value = "First line\nSecond line";
        String escapedValue = this.defaultImpexConverter.getValue("First line\nSecond line");
        Assertions.assertThat(escapedValue).isEqualTo("\"First line\nSecond line\"");
    }


    @Test
    public void shouldEscapeValueWhenItContainsNewLineRN()
    {
        String value = "First line\r\nSecond line";
        String escapedValue = this.defaultImpexConverter.getValue("First line\r\nSecond line");
        Assertions.assertThat(escapedValue).isEqualTo("\"First line\r\nSecond line\"");
    }


    @Test
    public void shouldEscapeValueWhenItContainsSemicolon()
    {
        String value = "First line;Second line";
        String escapedValue = this.defaultImpexConverter.getValue("First line;Second line");
        Assertions.assertThat(escapedValue).isEqualTo("\"First line;Second line\"");
    }


    @Test
    public void shouldNotEscapeValueWhenItContainsBoolean()
    {
        Boolean value = Boolean.TRUE;
        String escapedValue = this.defaultImpexConverter.getValue(value);
        Assertions.assertThat(escapedValue).isEqualTo(value.toString());
    }


    @Test
    public void shouldNotEscapeValueWhenItContainsNumber()
    {
        Double value = Double.valueOf(3.14D);
        String escapedValue = this.defaultImpexConverter.getValue(value);
        Assertions.assertThat(escapedValue).isEqualTo(value.toString());
    }


    @Test
    public void shouldNotEscapeValueWhenItContainsStringWithoutSpecialCharacters()
    {
        String value = "First line and second line";
        String escapedValue = this.defaultImpexConverter.getValue("First line and second line");
        Assertions.assertThat(escapedValue).isEqualTo("First line and second line");
    }


    @Test
    public void shouldPrepareHeaderWithoutSpecialAttributes()
    {
        ImpexHeaderValue impexHeaderValue = (new ImpexHeaderValue.Builder("code")).build();
        String headerAttribute = this.defaultImpexConverter.prepareHeaderAttribute(impexHeaderValue);
        Assertions.assertThat(headerAttribute).isEqualTo(String.format("%s", new Object[] {"code"}));
    }


    @Test
    public void shouldPrepareHeaderWithUniqueAttribute()
    {
        ImpexHeaderValue impexHeaderValue = (new ImpexHeaderValue.Builder("code")).withUnique(true).build();
        String headerAttribute = this.defaultImpexConverter.prepareHeaderAttribute(impexHeaderValue);
        Assertions.assertThat(headerAttribute).isEqualTo(String.format("%s[unique=true]", new Object[] {"code"}));
    }


    @Test
    public void shouldPrepareHeaderWithLangAttribute()
    {
        ImpexHeaderValue impexHeaderValue = (new ImpexHeaderValue.Builder("code")).withLang("en").build();
        String headerAttribute = this.defaultImpexConverter.prepareHeaderAttribute(impexHeaderValue);
        Assertions.assertThat(headerAttribute).isEqualTo(String.format("%s[lang=en]", new Object[] {"code"}));
    }


    @Test
    public void shouldPrepareHeaderWithUniqueAndLangAttributes()
    {
        ImpexHeaderValue impexHeaderValue = (new ImpexHeaderValue.Builder("code")).withUnique(true).withLang("en").build();
        String headerAttribute = this.defaultImpexConverter.prepareHeaderAttribute(impexHeaderValue);
        Assertions.assertThat(headerAttribute).isEqualTo(String.format("%s[unique=true,lang=en]", new Object[] {"code"}));
    }


    @Test
    public void shouldPrepareHeaderWithDateAttribute()
    {
        ImpexHeaderValue impexHeaderValue = (new ImpexHeaderValue.Builder("code")).withDateFormat("M/d/yy h:mm a").build();
        String headerAttribute = this.defaultImpexConverter.prepareHeaderAttribute(impexHeaderValue);
        Assertions.assertThat(headerAttribute).isEqualTo(String.format("%s[dateformat=M/d/yy h:mm a]", new Object[] {"code"}));
    }


    @Test
    public void shouldPrepareImpexHeader()
    {
        ImpexForType impexForType = new ImpexForType("Product");
        impexForType.putValue(Integer.valueOf(0), (new ImpexHeaderValue.Builder("code")).withUnique(true).build(), "");
        impexForType.putValue(Integer.valueOf(0), (new ImpexHeaderValue.Builder("name")).withLang("en").build(), "");
        String impexHeader = this.defaultImpexConverter.prepareImpexHeader(impexForType);
        Assertions.assertThat(impexHeader).isEqualTo("INSERT_UPDATE Product;code[unique=true];name[lang=en];\n");
    }


    @Test
    public void shouldReturnTrueWhenAllUniqueValuesAreNotEmpty()
    {
        Map<ImpexHeaderValue, Object> row = new HashMap<>();
        row.put((new ImpexHeaderValue.Builder("code")).withUnique(true).build(), "notEmptyValue");
        row.put((new ImpexHeaderValue.Builder("name")).build(), "notEmptyValue");
        row.put((new ImpexHeaderValue.Builder("deliveryTime")).build(), null);
        row.put((new ImpexHeaderValue.Builder("detail")).build(), "");
        row.put((new ImpexHeaderValue.Builder("catalogVersion")).withUnique(true).build(), "notEmptyValue");
        boolean result = this.defaultImpexConverter.areUniqueAttributesPopulated(row);
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void shouldReturnFalseWhenNotAllUniqueValuesAreNotEmpty()
    {
        Map<ImpexHeaderValue, Object> row = new HashMap<>();
        row.put((new ImpexHeaderValue.Builder("code")).withUnique(true).withMandatory(true).build(), "notEmptyValue");
        row.put((new ImpexHeaderValue.Builder("name")).build(), "notEmptyValue");
        row.put((new ImpexHeaderValue.Builder("deliveryTime")).build(), null);
        row.put((new ImpexHeaderValue.Builder("detail")).build(), "");
        row.put((new ImpexHeaderValue.Builder("catalogVersion")).withUnique(true).withMandatory(true).build(), "");
        boolean result = this.defaultImpexConverter.areUniqueAttributesPopulated(row);
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void shouldReturnTrueWhenNotAllUniqueValuesAreNotEmptyButTheFieldsAreNotMandatory()
    {
        Map<ImpexHeaderValue, Object> row = new HashMap<>();
        row.put((new ImpexHeaderValue.Builder("code")).withUnique(true).withMandatory(true).build(), "notEmptyValue");
        row.put((new ImpexHeaderValue.Builder("name")).build(), "notEmptyValue");
        row.put((new ImpexHeaderValue.Builder("deliveryTime")).build(), null);
        row.put((new ImpexHeaderValue.Builder("detail")).build(), "");
        row.put((new ImpexHeaderValue.Builder("catalogVersion")).withUnique(true).withMandatory(false).build(), "");
        boolean result = this.defaultImpexConverter.areUniqueAttributesPopulated(row);
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void shouldGeneratedSingleLineForImpexScript()
    {
        ImpexForType impexForType = new ImpexForType("Product");
        impexForType.putValue(Integer.valueOf(0), (new ImpexHeaderValue.Builder("code")).withUnique(true).build(), "ProductCode");
        impexForType.putValue(Integer.valueOf(0), (new ImpexHeaderValue.Builder("name")).withLang("en").build(), "ProductName");
        String singleRow = this.defaultImpexConverter.prepareImpexRows(impexForType);
        Assertions.assertThat(singleRow).isEqualTo(";ProductCode;ProductName;");
    }


    @Test
    public void shouldGeneratedMultiLineForImpexScript()
    {
        ImpexForType impexForType = new ImpexForType("Product");
        impexForType.putValue(Integer.valueOf(0), (new ImpexHeaderValue.Builder("code")).withUnique(true).build(), "First product code");
        impexForType.putValue(Integer.valueOf(0), (new ImpexHeaderValue.Builder("name")).withLang("en").build(), "First product name");
        impexForType.putValue(Integer.valueOf(1), (new ImpexHeaderValue.Builder("code")).withUnique(true).build(), "Second product code");
        impexForType.putValue(Integer.valueOf(1), (new ImpexHeaderValue.Builder("name")).withLang("en").build(), "Second product name");
        String singleRow = this.defaultImpexConverter.prepareImpexRows(impexForType);
        Assertions.assertThat(singleRow).isEqualTo(";First product code;First product name;\n;Second product code;Second product name;");
    }


    @Test
    public void shouldGeneratedMultiLineForImpexScriptWithEmptyNotUniqueValues()
    {
        ImpexForType impexForType = new ImpexForType("Product");
        impexForType.putValue(Integer.valueOf(0), (new ImpexHeaderValue.Builder("code")).withUnique(true).build(), "First product code");
        impexForType.putValue(Integer.valueOf(0), (new ImpexHeaderValue.Builder("name")).withLang("en").build(), "First product name");
        impexForType.putValue(Integer.valueOf(1), (new ImpexHeaderValue.Builder("code")).withUnique(true).build(), "Second product code");
        impexForType.putValue(Integer.valueOf(1), (new ImpexHeaderValue.Builder("name")).withLang("en").build(), "");
        String singleRow = this.defaultImpexConverter.prepareImpexRows(impexForType);
        Assertions.assertThat(singleRow).isEqualTo(";First product code;First product name;\n;Second product code;;");
    }


    @Test
    public void shouldGeneratedMultiLineForImpexScriptOnlyWithLinesWhichHaveAllUniqueValues()
    {
        ImpexForType impexForType = new ImpexForType("Product");
        impexForType.putValue(Integer.valueOf(0), (new ImpexHeaderValue.Builder("code")).withUnique(true).build(), "First product code");
        impexForType.putValue(Integer.valueOf(0), (new ImpexHeaderValue.Builder("name")).withLang("en").build(), "First product name");
        impexForType.putValue(Integer.valueOf(1), (new ImpexHeaderValue.Builder("code")).withUnique(true).build(), "");
        impexForType.putValue(Integer.valueOf(1), (new ImpexHeaderValue.Builder("name")).withLang("en").build(), "");
        impexForType.putValue(Integer.valueOf(1), (new ImpexHeaderValue.Builder("code")).withUnique(true).build(), "Third product code");
        impexForType.putValue(Integer.valueOf(1), (new ImpexHeaderValue.Builder("name")).withLang("en").build(), "Third product name");
        String singleRow = this.defaultImpexConverter.prepareImpexRows(impexForType);
        Assertions.assertThat(singleRow).isEqualTo(";First product code;First product name;\n;Third product code;Third product name;");
    }


    @Test
    public void shouldGenerateWholeImpexScriptForSingleTypeCode()
    {
        Impex impex = new Impex();
        ImpexForType impexForProduct = impex.findUpdates("Product");
        impexForProduct.putValue(Integer.valueOf(0), (new ImpexHeaderValue.Builder("code")).withUnique(true).build(), "First product code");
        impexForProduct.putValue(Integer.valueOf(0), (new ImpexHeaderValue.Builder("name")).withLang("en").build(), "First product name");
        impexForProduct.putValue(Integer.valueOf(1), (new ImpexHeaderValue.Builder("code")).withUnique(true).build(), "Second product code");
        impexForProduct.putValue(Integer.valueOf(1), (new ImpexHeaderValue.Builder("name")).withLang("en").build(), "Second product name");
        String generatedImpex = this.defaultImpexConverter.convert(impex);
        Assertions.assertThat(generatedImpex).isEqualTo("INSERT_UPDATE Product;code[unique=true];name[lang=en];\n;First product code;First product name;\n;Second product code;Second product name;\n\n");
    }


    @Test
    public void shouldGenerateWholeImpexScriptForManyTypeCodes()
    {
        Impex impex = new Impex();
        ImpexForType impexForProduct = impex.findUpdates("Product");
        impexForProduct.setOrder(100);
        impexForProduct.putValue(Integer.valueOf(0), (new ImpexHeaderValue.Builder("code")).withUnique(true).build(), "First product code");
        impexForProduct.putValue(Integer.valueOf(0), (new ImpexHeaderValue.Builder("name")).withLang("en").build(), "First product name");
        impexForProduct.putValue(Integer.valueOf(1), (new ImpexHeaderValue.Builder("code")).withUnique(true).build(), "Second product code");
        impexForProduct.putValue(Integer.valueOf(1), (new ImpexHeaderValue.Builder("name")).withLang("en").build(), "Second product name");
        ImpexForType impexForCatalog = impex.findUpdates("Catalog");
        impexForCatalog.putValue(Integer.valueOf(0), (new ImpexHeaderValue.Builder("id")).withUnique(true).build(), "Clothing");
        String generatedImpex = this.defaultImpexConverter.convert(impex);
        Assertions.assertThat(generatedImpex).isEqualTo("INSERT_UPDATE Catalog;id[unique=true];\n;Clothing;\n\nINSERT_UPDATE Product;code[unique=true];name[lang=en];\n;First product code;First product name;\n;Second product code;Second product name;\n\n");
    }
}
