package com.hybris.backoffice.excel.translators.generic.factory;

import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.parser.DefaultImportParameterParser;
import com.hybris.backoffice.excel.importing.parser.ParsedValues;
import com.hybris.backoffice.excel.importing.parser.matcher.DefaultExcelParserMatcher;
import com.hybris.backoffice.excel.importing.parser.matcher.ExcelParserMatcher;
import com.hybris.backoffice.excel.importing.parser.splitter.DefaultExcelParserSplitter;
import com.hybris.backoffice.excel.importing.parser.splitter.ExcelParserSplitter;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class DefaultImportImpexFactoryTest
{
    private final ReferenceImportImpexFactoryStrategy referenceImportImpexFactoryStrategy = new ReferenceImportImpexFactoryStrategy();


    @Test
    public void shouldPrepareImpexHeaderForCatalogVersion()
    {
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ImpexHeaderValue impexHeaderValue = this.referenceImportImpexFactoryStrategy.prepareImpexHeader(RequiredAttributeTestFactory.prepareStructureForCatalogVersion(true, true), importParameters);
        Assertions.assertThat(impexHeaderValue.isUnique()).isTrue();
        Assertions.assertThat(impexHeaderValue.getName()).isEqualTo("catalogVersion(version,catalog(id))");
        Assertions.assertThat(impexHeaderValue.getLang()).isEqualTo(null);
    }


    @Test
    public void shouldPrepareImpexHeaderForLocalizedReference()
    {
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        BDDMockito.given(importParameters.getIsoCode()).willReturn("en");
        ImpexHeaderValue impexHeaderValue = this.referenceImportImpexFactoryStrategy.prepareImpexHeader(RequiredAttributeTestFactory.prepareStructureForCatalogVersion(true, true), importParameters);
        Assertions.assertThat(impexHeaderValue.isUnique()).isTrue();
        Assertions.assertThat(impexHeaderValue.getName()).isEqualTo("catalogVersion(version,catalog(id))");
        Assertions.assertThat(impexHeaderValue.getLang()).isEqualTo("en");
    }


    @Test
    public void shouldPrepareImpexHeaderForSupercategories()
    {
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ImpexHeaderValue impexHeaderValue = this.referenceImportImpexFactoryStrategy.prepareImpexHeader(RequiredAttributeTestFactory.prepareStructureForSupercategories(false, true), importParameters);
        Assertions.assertThat(impexHeaderValue.isUnique()).isFalse();
        Assertions.assertThat(impexHeaderValue.getName()).isEqualTo("supercategories(code,catalogVersion(version,catalog(id)))");
    }


    @Test
    public void shouldPrepareImpexValueForCatalogVersion()
    {
        ParsedValues parsedValues = createDefaultImportParameterParser().parseValue("CatalogVersion.version:Catalog.id", "", "Online:Default");
        ImportParameters importParameters = new ImportParameters(null, null, parsedValues.getCellValue(), null, parsedValues.getParameters());
        String impexValue = this.referenceImportImpexFactoryStrategy.prepareImpexValue(RequiredAttributeTestFactory.prepareStructureForCatalogVersion(), importParameters);
        Assertions.assertThat(impexValue).isEqualTo("Online:Default");
    }


    @Test
    public void shouldPrepareImpexValueForSupercategories()
    {
        ParsedValues parsedValues = createDefaultImportParameterParser().parseValue("Category.code:CatalogVersion.version:Catalog.id", ":Online:Default", "First:Online:Default,Second:Online:,Third::Default");
        ImportParameters importParameters = new ImportParameters(null, null, parsedValues.getCellValue(), null, parsedValues.getParameters());
        String impexValue = this.referenceImportImpexFactoryStrategy.prepareImpexValue(RequiredAttributeTestFactory.prepareStructureForSupercategories(), importParameters);
        Assertions.assertThat(impexValue).isEqualTo("First:Online:Default,Second:Online:Default,Third:Online:Default");
    }


    private DefaultImportParameterParser createDefaultImportParameterParser()
    {
        DefaultImportParameterParser parser = new DefaultImportParameterParser();
        parser.setMatcher((ExcelParserMatcher)new DefaultExcelParserMatcher());
        parser.setSplitter((ExcelParserSplitter)new DefaultExcelParserSplitter());
        return parser;
    }
}
