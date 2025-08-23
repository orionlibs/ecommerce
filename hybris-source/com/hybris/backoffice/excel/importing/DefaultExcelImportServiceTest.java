package com.hybris.backoffice.excel.importing;

import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.importing.parser.DefaultImportParameterParser;
import com.hybris.backoffice.excel.importing.parser.ParserRegistry;
import com.hybris.backoffice.excel.importing.parser.matcher.DefaultExcelParserMatcher;
import com.hybris.backoffice.excel.importing.parser.matcher.ExcelParserMatcher;
import com.hybris.backoffice.excel.importing.parser.splitter.DefaultExcelParserSplitter;
import com.hybris.backoffice.excel.importing.parser.splitter.ExcelParserSplitter;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.header.ExcelHeaderService;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.translators.ExcelTranslatorRegistry;
import com.hybris.backoffice.excel.translators.ExcelValueTranslator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelImportServiceTest
{
    private static final String PRODUCT_TYPE_CODE = "Product";
    private static final String CATALOG_KEY = "catalog";
    private static final String VERSION_KEY = "version";
    private static final String CATALOG_CLOTHING_VALUE = "Clothing";
    private static final String CATALOG_DEFAULT_VALUE = "Default";
    private static final String VERSION_ONLINE_VALUE = "Online";
    private static final String VERSION_STAGED_VALUE = "Staged";
    private static final String APPROVAL_STATUS_APPROVED = "Approved";
    private static final String REFERENCE_VALUE = "productRef";
    private static final String CATALOG_VERSION_PATTERN = "%s:%s";
    private static final String CATALOG_VERSION_FORMAT = String.format("%s:%s", new Object[] {"catalog", "version"});
    private static final String EMPTY_CELL_VALUE = "";
    private static final String CATEGORY_KEY = "category";
    @Mock
    private ParserRegistry parserRegistry;
    @Mock
    private ExcelSheetService excelSheetService;
    @Mock
    private ExcelCellService excelCellService;
    @Mock
    private ExcelHeaderService excelHeaderService;
    @Mock
    private ExcelTranslatorRegistry excelTranslatorRegistry;
    @Spy
    @InjectMocks
    private DefaultExcelImportService excelImportService;


    @Before
    public void setUp()
    {
        DefaultImportParameterParser parameterParser = new DefaultImportParameterParser();
        parameterParser.setMatcher((ExcelParserMatcher)new DefaultExcelParserMatcher());
        parameterParser.setSplitter((ExcelParserSplitter)new DefaultExcelParserSplitter());
        BDDMockito.given(this.parserRegistry.getParser((String)Matchers.any())).willReturn(parameterParser);
    }


    @Test
    public void shouldFindDefaultValuesWhenCellIsNull()
    {
        SelectedAttribute selectedAttribute = new SelectedAttribute();
        Map<String, String> defaultValues = selectedAttribute.findDefaultValues();
        Assertions.assertThat(defaultValues).isNotNull();
        Assertions.assertThat(defaultValues.keySet()).isEmpty();
    }


    @Test
    public void shouldFindDefaultValuesWhenCellValueIsEmpty()
    {
        SelectedAttribute selectedAttribute = new SelectedAttribute();
        selectedAttribute.setReferenceFormat("");
        Map<String, String> defaultValues = selectedAttribute.findDefaultValues();
        Assertions.assertThat(defaultValues).isNotNull();
        Assertions.assertThat(defaultValues.keySet()).isEmpty();
    }


    @Test
    public void shouldFindDefaultValuesWhenOnlyPatternIsProvided()
    {
        SelectedAttribute selectedAttribute = new SelectedAttribute();
        selectedAttribute.setReferenceFormat(CATALOG_VERSION_FORMAT);
        Map<String, String> defaultValues = selectedAttribute.findDefaultValues();
        Assertions.assertThat(defaultValues).isNotNull();
        Assertions.assertThat(defaultValues.keySet()).contains((Object[])new String[] {"catalog", "version"});
    }


    @Test
    public void shouldFindDefaultValuesWhenFirstPatternHasDefault()
    {
        SelectedAttribute selectedAttribute = new SelectedAttribute();
        selectedAttribute.setReferenceFormat(String.format("%s:%s", new Object[] {"catalog", "version"}));
        selectedAttribute.setDefaultValues("Clothing");
        Map<String, String> defaultValues = selectedAttribute.findDefaultValues();
        Assertions.assertThat(defaultValues).isNotNull();
        Assertions.assertThat(defaultValues.keySet()).contains((Object[])new String[] {"catalog", "version"});
        Assertions.assertThat(defaultValues.values()).contains((Object[])new String[] {"Clothing", null});
    }


    @Test
    public void shouldFindDefaultValuesWhenSecondPatternHasDefault()
    {
        SelectedAttribute selectedAttribute = new SelectedAttribute();
        selectedAttribute.setReferenceFormat(String.format("%s:%s", new Object[] {"catalog", "version"}));
        selectedAttribute.setDefaultValues(String.format(":%s", new Object[] {"Online"}));
        Map<String, String> defaultValues = selectedAttribute.findDefaultValues();
        Assertions.assertThat(defaultValues).isNotNull();
        Assertions.assertThat(defaultValues.keySet()).contains((Object[])new String[] {"catalog", "version"});
        Assertions.assertThat(defaultValues.values()).contains((Object[])new String[] {null, "Online"});
    }


    @Test
    public void shouldFindDefaultValuesWhenBothPatternsHaveDefaults()
    {
        SelectedAttribute selectedAttribute = new SelectedAttribute();
        selectedAttribute.setReferenceFormat(CATALOG_VERSION_FORMAT);
        selectedAttribute.setDefaultValues(String.format("%s:%s", new Object[] {"Clothing", "Online"}));
        Map<String, String> defaultValues = selectedAttribute.findDefaultValues();
        Assertions.assertThat(defaultValues).isNotNull();
        Assertions.assertThat(defaultValues.keySet()).contains((Object[])new String[] {"catalog", "version"});
        Assertions.assertThat(defaultValues.values()).contains((Object[])new String[] {"Clothing", "Online"});
    }


    @Test
    public void shouldPrepareImportParametersForEmptyCellWithoutDefaults()
    {
        SelectedAttribute selectedAttribute = new SelectedAttribute();
        ImportParameters importParameters = this.excelImportService.findImportParameters(selectedAttribute, "", "Product", "productRef");
        Assertions.assertThat(importParameters).isNotNull();
        Assertions.assertThat(importParameters.getCellValue()).isEqualTo("");
        Assertions.assertThat(importParameters.getMultiValueParameters()).hasSize(1);
        Assertions.assertThat((String)((Map)importParameters.getMultiValueParameters().get(0)).get("rawValue")).isEqualTo("");
    }


    @Test
    public void shouldPrepareImportParametersForEmptyCellWithDefaults()
    {
        String defaultValue = String.format("%s:%s", new Object[] {"Clothing", "Online"});
        SelectedAttribute selectedAttribute = new SelectedAttribute();
        selectedAttribute.setReferenceFormat(CATALOG_VERSION_FORMAT);
        selectedAttribute.setDefaultValues(defaultValue);
        ImportParameters importParameters = this.excelImportService.findImportParameters(selectedAttribute, "", "Product", "productRef");
        Assertions.assertThat(importParameters).isNotNull();
        Assertions.assertThat(importParameters.getCellValue()).isEqualTo(selectedAttribute.getDefaultValues());
        Assertions.assertThat(importParameters.getMultiValueParameters()).hasSize(1);
        Assertions.assertThat(importParameters.getSingleValueParameters().keySet()).containsSequence((Object[])new String[] {"catalog", "version"});
        Assertions.assertThat(importParameters.getSingleValueParameters().values()).containsSequence((Object[])new String[] {"Clothing", "Online"});
    }


    @Test
    public void shouldPrepareImportParametersForNotReferenceCellWithoutDefaults()
    {
        SelectedAttribute selectedAttribute = new SelectedAttribute();
        ImportParameters importParameters = this.excelImportService.findImportParameters(selectedAttribute, "Approved", "Product", "productRef");
        Assertions.assertThat(importParameters).isNotNull();
        Assertions.assertThat(importParameters.getCellValue()).isEqualTo("Approved");
        Assertions.assertThat(importParameters.getMultiValueParameters()).isNotNull();
        Assertions.assertThat((String)((Map)importParameters.getMultiValueParameters().get(0)).get("rawValue"))
                        .isEqualTo("Approved");
    }


    @Test
    public void shouldPrepareImportParametersForReferenceCellWithoutDefaults()
    {
        SelectedAttribute selectedAttribute = new SelectedAttribute();
        selectedAttribute.setDefaultValues("");
        String cellValue = String.format("%s:%s", new Object[] {"Clothing", "Online"});
        selectedAttribute.setReferenceFormat(CATALOG_VERSION_FORMAT);
        ImportParameters importParameters = this.excelImportService.findImportParameters(selectedAttribute, cellValue, "Product", "productRef");
        Assertions.assertThat(importParameters).isNotNull();
        Assertions.assertThat(importParameters.getCellValue()).isEqualTo(cellValue);
        Assertions.assertThat(importParameters.getMultiValueParameters()).hasSize(1);
        Assertions.assertThat(importParameters.getSingleValueParameters().keySet()).containsSequence((Object[])new String[] {"catalog", "version"});
        Assertions.assertThat(importParameters.getSingleValueParameters().values()).containsSequence((Object[])new String[] {"Clothing", "Online"});
    }


    @Test
    public void shouldPrepareImportParametersForReferenceCellWithDefaultFirstValue()
    {
        SelectedAttribute selectedAttribute = new SelectedAttribute();
        String cellValue = String.format(":%s", new Object[] {"Online"});
        selectedAttribute.setReferenceFormat(CATALOG_VERSION_FORMAT);
        selectedAttribute.setDefaultValues(String.format("%s:%s", new Object[] {"Clothing", "Staged"}));
        ImportParameters importParameters = this.excelImportService.findImportParameters(selectedAttribute, cellValue, "Product", "productRef");
        Assertions.assertThat(importParameters).isNotNull();
        Assertions.assertThat(importParameters.getCellValue())
                        .isEqualTo(String.format("%s:%s", new Object[] {"Clothing", "Online"}));
        Assertions.assertThat(importParameters.getMultiValueParameters()).hasSize(1);
        Assertions.assertThat(importParameters.getSingleValueParameters().keySet()).containsSequence((Object[])new String[] {"catalog", "version"});
        Assertions.assertThat(importParameters.getSingleValueParameters().values()).containsSequence((Object[])new String[] {"Clothing", "Online"});
    }


    @Test
    public void shouldPrepareImportParametersForReferenceCellWithDefaultSecondValue()
    {
        SelectedAttribute selectedAttribute = new SelectedAttribute();
        String cellValue = String.format("%s", new Object[] {"Clothing"});
        selectedAttribute.setReferenceFormat(CATALOG_VERSION_FORMAT);
        selectedAttribute.setDefaultValues(String.format("%s:%s", new Object[] {"Clothing", "Online"}));
        ImportParameters importParameters = this.excelImportService.findImportParameters(selectedAttribute, cellValue, "Product", "productRef");
        Assertions.assertThat(importParameters).isNotNull();
        Assertions.assertThat(importParameters.getCellValue()).isEqualTo("Clothing:Online");
        Assertions.assertThat(importParameters.getMultiValueParameters()).hasSize(1);
        Assertions.assertThat(importParameters.getSingleValueParameters().keySet()).containsSequence((Object[])new String[] {"catalog", "version"});
        Assertions.assertThat(importParameters.getSingleValueParameters().values()).containsSequence((Object[])new String[] {"Clothing", "Online"});
    }


    @Test
    public void shouldPrepareImportParametersForReferenceCellWithDefaultBothValues()
    {
        SelectedAttribute selectedAttribute = new SelectedAttribute();
        String cellValue = ":";
        selectedAttribute.setReferenceFormat(CATALOG_VERSION_FORMAT);
        String defaultValues = String.format("%s:%s", new Object[] {"Clothing", "Online"});
        selectedAttribute.setDefaultValues(defaultValues);
        ImportParameters importParameters = this.excelImportService.findImportParameters(selectedAttribute, ":", "Product", "productRef");
        Assertions.assertThat(importParameters).isNotNull();
        Assertions.assertThat(importParameters.getCellValue()).isEqualTo(defaultValues);
        Assertions.assertThat(importParameters.getMultiValueParameters()).hasSize(1);
        Assertions.assertThat(importParameters.getSingleValueParameters().keySet()).containsSequence((Object[])new String[] {"catalog", "version"});
        Assertions.assertThat(importParameters.getSingleValueParameters().values()).containsSequence((Object[])new String[] {"Clothing", "Online"});
    }


    @Test
    public void shouldPrepareImportParametersForReferenceCellWith()
    {
        SelectedAttribute selectedAttribute = new SelectedAttribute();
        String cellValue = "15:EUR";
        selectedAttribute.setReferenceFormat("price:currency:scale:unit:unitFactor:pricing");
        selectedAttribute.setDefaultValues("15:EUR:1:piece:3:Gross");
        ImportParameters importParameters = this.excelImportService.findImportParameters(selectedAttribute, "15:EUR", "Product", "productRef");
        Assertions.assertThat(importParameters).isNotNull();
        Assertions.assertThat(importParameters.getCellValue()).isEqualTo("15:EUR:1:piece:3:Gross");
        Assertions.assertThat(importParameters.getMultiValueParameters()).hasSize(1);
        Assertions.assertThat(importParameters.getSingleValueParameters().keySet()).containsSequence((Object[])new String[] {"price", "currency", "scale", "unit", "unitFactor", "pricing"});
        Assertions.assertThat(importParameters.getSingleValueParameters().values()).containsSequence((Object[])new String[] {"15", "EUR", "1", "piece", "3", "Gross"});
    }


    @Test
    public void shouldPrepareImportParametersForMultiValue()
    {
        SelectedAttribute selectedAttribute = new SelectedAttribute();
        String cellValue = "Shoes:Online:Clothing,Hats,Jeans::Default,Shirts:Online:Default";
        selectedAttribute.setReferenceFormat("category:version:catalog");
        selectedAttribute.setDefaultValues(":Staged:Clothing");
        ImportParameters importParameters = this.excelImportService.findImportParameters(selectedAttribute, "Shoes:Online:Clothing,Hats,Jeans::Default,Shirts:Online:Default", "Product", "productRef");
        Assertions.assertThat(importParameters).isNotNull();
        Assertions.assertThat(importParameters.getCellValue())
                        .isEqualTo("Shoes:Online:Clothing,Hats:Staged:Clothing,Jeans:Staged:Default,Shirts:Online:Default");
        Assertions.assertThat(importParameters.getMultiValueParameters()).hasSize(4);
        Assertions.assertThat(((Map)importParameters.getMultiValueParameters().get(0)).keySet()).containsSequence((Object[])new String[] {"category", "version", "catalog"});
        Assertions.assertThat(((Map)importParameters.getMultiValueParameters().get(0)).values()).containsSequence((Object[])new String[] {"Shoes", "Online", "Clothing"});
        Assertions.assertThat(((Map)importParameters.getMultiValueParameters().get(1)).keySet()).containsSequence((Object[])new String[] {"category", "version", "catalog"});
        Assertions.assertThat(((Map)importParameters.getMultiValueParameters().get(1)).values()).containsSequence((Object[])new String[] {"Hats", "Staged", "Clothing"});
        Assertions.assertThat(((Map)importParameters.getMultiValueParameters().get(2)).keySet()).containsSequence((Object[])new String[] {"category", "version", "catalog"});
        Assertions.assertThat(((Map)importParameters.getMultiValueParameters().get(2)).values()).containsSequence((Object[])new String[] {"Jeans", "Staged", "Default"});
        Assertions.assertThat(((Map)importParameters.getMultiValueParameters().get(3)).keySet()).containsSequence((Object[])new String[] {"category", "version", "catalog"});
        Assertions.assertThat(((Map)importParameters.getMultiValueParameters().get(3)).values()).containsSequence((Object[])new String[] {"Shirts", "Online", "Default"});
    }


    @Test
    public void shouldInvokeTranslatorTwiceForTwoSelectedAttributes()
    {
        int lastRowNumber = 3;
        Sheet typeSystemSheet = (Sheet)Mockito.mock(Sheet.class);
        Sheet productSheet = (Sheet)Mockito.mock(Sheet.class);
        ExcelValueTranslator<Object> translator = (ExcelValueTranslator<Object>)Mockito.mock(ExcelValueTranslator.class);
        Optional<ExcelValueTranslator<Object>> translatorOpt = Optional.of(translator);
        Row firstRow = (Row)Mockito.mock(Row.class);
        Cell codeFirstCell = (Cell)Mockito.mock(Cell.class);
        Cell catalogVersionFirstCell = (Cell)Mockito.mock(Cell.class);
        List<SelectedAttribute> selectedAttributes = new ArrayList<>();
        List<String> documentRefs = Arrays.asList(new String[] {"documentRef1"});
        selectedAttributes.add(prepareSelectedAttribute("code", "", ""));
        selectedAttributes.add(prepareSelectedAttribute("catalogVersion", "catalog:version", "Clothing:"));
        BDDMockito.given(productSheet.getSheetName()).willReturn("Product");
        BDDMockito.given(this.excelSheetService.findTypeCodeForSheetName((Workbook)Matchers.any(), (String)Matchers.eq("Product"))).willReturn("Product");
        BDDMockito.given(Integer.valueOf(productSheet.getLastRowNum())).willReturn(Integer.valueOf(3));
        BDDMockito.given(this.excelTranslatorRegistry.getTranslator((AttributeDescriptorModel)Matchers.any())).willReturn(translatorOpt);
        BDDMockito.given(productSheet.getRow(3)).willReturn(firstRow);
        BDDMockito.given(firstRow.getCell(0)).willReturn(codeFirstCell);
        Mockito.lenient().when(firstRow.getCell(1)).thenReturn(catalogVersionFirstCell);
        short num = 1;
        BDDMockito.given(Short.valueOf(firstRow.getLastCellNum())).willReturn(Short.valueOf((short)1));
        BDDMockito.given(this.excelCellService.getCellValue(codeFirstCell)).willReturn("code");
        Mockito.lenient().when(this.excelCellService.getCellValue(catalogVersionFirstCell)).thenReturn("catalogVersion");
        BDDMockito.given(this.excelHeaderService.getHeaders(typeSystemSheet, productSheet)).willReturn(selectedAttributes);
        ((DefaultExcelImportService)Mockito.doReturn(documentRefs).when(this.excelImportService))
                        .generateDocumentRefs(Integer.valueOf(0));
        Impex impex = this.excelImportService.generateImpexForSheet(typeSystemSheet, productSheet);
        Assertions.assertThat(impex).isNotNull();
        ((ExcelValueTranslator)Mockito.verify(translator, Mockito.times(2))).importData((AttributeDescriptorModel)Matchers.any(), (ImportParameters)Matchers.any());
    }


    private SelectedAttribute prepareSelectedAttribute(String qualifier, String referenceFormat, String defaultValue)
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        Mockito.lenient().when(attributeDescriptor.getQualifier()).thenReturn(qualifier);
        return new SelectedAttribute(null, referenceFormat, defaultValue, attributeDescriptor);
    }
}
