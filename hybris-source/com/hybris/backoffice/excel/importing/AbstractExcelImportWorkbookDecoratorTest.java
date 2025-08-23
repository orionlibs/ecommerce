package com.hybris.backoffice.excel.importing;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.data.ExcelImportResult;
import com.hybris.backoffice.excel.importing.parser.DefaultImportParameterParser;
import com.hybris.backoffice.excel.importing.parser.ParserRegistry;
import com.hybris.backoffice.excel.importing.parser.matcher.DefaultExcelParserMatcher;
import com.hybris.backoffice.excel.importing.parser.matcher.ExcelParserMatcher;
import com.hybris.backoffice.excel.importing.parser.splitter.DefaultExcelParserSplitter;
import com.hybris.backoffice.excel.importing.parser.splitter.ExcelParserSplitter;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.header.ExcelHeaderService;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.translators.ExcelAttributeTranslator;
import com.hybris.backoffice.excel.translators.ExcelAttributeTranslatorRegistry;
import com.hybris.backoffice.excel.validators.ExcelAttributeValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractExcelImportWorkbookDecoratorTest
{
    @Mock
    private ParserRegistry parserRegistry;
    @Mock
    private Workbook workbook;
    @Mock
    private Sheet productSheet;
    @Mock
    private ExcelSheetService excelSheetService;
    @Mock
    private ExcelHeaderService excelHeaderService;
    @Mock
    private ExcelAttributeTranslatorRegistry excelAttributeTranslatorRegistry;
    @Mock
    private ExcelAttributeTranslator excelAttributeTranslator;
    @Mock
    private ExcelCellService excelCellService;
    @Spy
    @InjectMocks
    private DefaultExcelImportClassificationWorkbookDecorator defaultExcelImportClassificationWorkbookDecorator;


    @Before
    public void setup()
    {
        BDDMockito.given(this.productSheet.getWorkbook()).willReturn(this.workbook);
        BDDMockito.given(this.excelSheetService.getSheets(this.workbook)).willReturn(Arrays.asList(new Sheet[] {this.productSheet}));
        BDDMockito.given(this.productSheet.getSheetName()).willReturn("Product");
        BDDMockito.given(this.excelSheetService.findTypeCodeForSheetName(this.workbook, "Product")).willReturn("Product");
        BDDMockito.given(this.excelAttributeTranslatorRegistry.findTranslator((ExcelAttribute)ArgumentMatchers.any())).willReturn(Optional.of(this.excelAttributeTranslator));
        DefaultImportParameterParser defaultImportParameterParser = new DefaultImportParameterParser();
        defaultImportParameterParser.setSplitter((ExcelParserSplitter)new DefaultExcelParserSplitter());
        defaultImportParameterParser.setMatcher((ExcelParserMatcher)new DefaultExcelParserMatcher());
        BDDMockito.given(this.parserRegistry.getParser((String)ArgumentMatchers.any())).willReturn(defaultImportParameterParser);
        BDDMockito.given(this.excelHeaderService.getHeaderValueWithoutSpecialMarks((String)ArgumentMatchers.any())).will(inv -> inv.getArguments()[0]);
    }


    @Test
    public void shouldValidateClassificationAttributes()
    {
        ExcelAttribute dimensionsAttribute = prepareExcelAttribute("dimensions");
        ExcelAttribute weightAttribute = prepareExcelAttribute("weight");
        Collection<ExcelAttribute> attributes = Arrays.asList(new ExcelAttribute[] {dimensionsAttribute, weightAttribute});
        Row headerRow = prepareRow(this.productSheet, 0, new String[] {"dimensions", "weight"});
        ((DefaultExcelImportClassificationWorkbookDecorator)Mockito.doReturn(attributes).when(this.defaultExcelImportClassificationWorkbookDecorator)).getExcelAttributes(this.productSheet);
        BDDMockito.given(this.productSheet.getRow(0)).willReturn(headerRow);
        BDDMockito.given(Integer.valueOf(this.productSheet.getLastRowNum())).willReturn(Integer.valueOf(3));
        prepareRow(this.productSheet, 1, new String[] {"", ""});
        prepareRow(this.productSheet, 2, new String[] {"", ""});
        prepareRow(this.productSheet, 3, new String[] {"170 x 75 mm", "230.0"});
        ExcelAttributeValidator mockedValidator = (ExcelAttributeValidator)Mockito.mock(ExcelAttributeValidator.class);
        this.defaultExcelImportClassificationWorkbookDecorator.setValidators(Collections.singletonList(mockedValidator));
        ExcelValidationResult expectedValidationResult = new ExcelValidationResult(new ValidationMessage("Incorrect format of dimensions attribute"));
        BDDMockito.given(Boolean.valueOf(mockedValidator.canHandle((ExcelAttribute)ArgumentMatchers.eq(dimensionsAttribute), (ImportParameters)ArgumentMatchers.any()))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(mockedValidator.validate((ExcelAttribute)ArgumentMatchers.eq(dimensionsAttribute), (ImportParameters)ArgumentMatchers.any(), (Map)ArgumentMatchers.any())).willReturn(expectedValidationResult);
        List<ExcelValidationResult> validationResults = this.defaultExcelImportClassificationWorkbookDecorator.validate(this.workbook);
        Assertions.assertThat(validationResults).hasSize(1);
        Assertions.assertThat(validationResults).contains(new Object[] {expectedValidationResult});
    }


    @Test
    public void shouldDecorateImpexByClassificationAttributes()
    {
        ExcelAttribute dimensionsAttribute = prepareExcelAttribute("dimensions");
        ExcelAttribute weightAttribute = prepareExcelAttribute("weight");
        Collection<ExcelAttribute> attributes = Arrays.asList(new ExcelAttribute[] {dimensionsAttribute, weightAttribute});
        Row headerRow = prepareRow(this.productSheet, 0, new String[] {"dimensions", "weight"});
        ((DefaultExcelImportClassificationWorkbookDecorator)Mockito.doReturn(attributes).when(this.defaultExcelImportClassificationWorkbookDecorator)).getExcelAttributes(this.productSheet);
        BDDMockito.given(this.productSheet.getRow(0)).willReturn(headerRow);
        BDDMockito.given(Integer.valueOf(this.productSheet.getLastRowNum())).willReturn(Integer.valueOf(3));
        prepareRow(this.productSheet, 1, new String[] {"", ""});
        prepareRow(this.productSheet, 2, new String[] {"", ""});
        prepareRow(this.productSheet, 3, new String[] {"170 x 75 mm", "230.0"});
        Impex dimensionsImpex = new Impex();
        Impex weightImpex = new Impex();
        BDDMockito.given(this.excelAttributeTranslator.importData((ExcelAttribute)ArgumentMatchers.eq(dimensionsAttribute), (ImportParameters)ArgumentMatchers.any(), (ExcelImportContext)ArgumentMatchers.any())).willReturn(dimensionsImpex);
        BDDMockito.given(this.excelAttributeTranslator.importData((ExcelAttribute)ArgumentMatchers.eq(weightAttribute), (ImportParameters)ArgumentMatchers.any(), (ExcelImportContext)ArgumentMatchers.any())).willReturn(weightImpex);
        Impex mainImpex = (Impex)Mockito.mock(Impex.class);
        ImpexForType impexForType = (ImpexForType)Mockito.mock(ImpexForType.class);
        BDDMockito.given(impexForType.getRow((Integer)ArgumentMatchers.any())).willReturn(null);
        BDDMockito.given(mainImpex.findUpdates("Product")).willReturn(impexForType);
        this.defaultExcelImportClassificationWorkbookDecorator.decorate(new ExcelImportResult(this.workbook, mainImpex));
        ArgumentCaptor<Impex> impexArgumentCaptor = ArgumentCaptor.forClass(Impex.class);
        ((Impex)Mockito.verify(mainImpex, Mockito.times(2))).mergeImpex((Impex)impexArgumentCaptor.capture(), (String)ArgumentMatchers.eq("Product"), Integer.valueOf(ArgumentMatchers.eq(3)));
        Assertions.assertThat(impexArgumentCaptor.getAllValues()).contains(new Object[] {dimensionsImpex, weightImpex});
    }


    private ExcelAttribute prepareExcelAttribute(String code)
    {
        ClassAttributeAssignmentModel assignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassificationAttributeModel attribute = (ClassificationAttributeModel)Mockito.mock(ClassificationAttributeModel.class);
        Mockito.lenient().when(assignment.getClassificationAttribute()).thenReturn(attribute);
        Mockito.lenient().when(attribute.getCode()).thenReturn(code);
        ExcelClassificationAttribute classificationAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        Mockito.lenient().when(classificationAttribute.getAttributeAssignment()).thenReturn(assignment);
        BDDMockito.given(classificationAttribute.getName()).willReturn(code);
        return (ExcelAttribute)classificationAttribute;
    }


    private Row prepareRow(Sheet sheet, int rowIndex, String... values)
    {
        Row row = (Row)Mockito.mock(Row.class);
        BDDMockito.given(sheet.getRow(rowIndex)).willReturn(row);
        BDDMockito.given(Integer.valueOf(row.getRowNum())).willReturn(Integer.valueOf(rowIndex));
        BDDMockito.given(Short.valueOf(row.getFirstCellNum())).willReturn(Short.valueOf((short)0));
        BDDMockito.given(Short.valueOf(row.getLastCellNum())).willReturn(Short.valueOf((short)(values.length - 1)));
        for(int i = 0; i < values.length; i++)
        {
            Cell cell = (Cell)Mockito.mock(Cell.class);
            BDDMockito.given(Integer.valueOf(cell.getColumnIndex())).willReturn(Integer.valueOf(i));
            BDDMockito.given(row.getCell(i)).willReturn(cell);
            BDDMockito.given(cell.getSheet()).willReturn(sheet);
            BDDMockito.given(this.excelCellService.getCellValue(cell)).willReturn(values[i]);
        }
        return row;
    }
}
