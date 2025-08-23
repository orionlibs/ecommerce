package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.template.AttributeNameFormatter;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.populator.ExcelAttributeContext;
import com.hybris.backoffice.excel.translators.ExcelAttributeTranslator;
import com.hybris.backoffice.excel.translators.ExcelAttributeTranslatorRegistry;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import java.util.Collections;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.OptionalAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractExcelExportWorkbookDecoratorTest
{
    @Mock
    ExcelCellService mockedExcelCellService;
    @Mock
    AttributeNameFormatter<ExcelClassificationAttribute> mockedAttributeNameFormatter;
    @Mock
    ExcelAttributeTranslatorRegistry mockedExcelAttributeTranslatorRegistry;
    @InjectMocks
    @Spy
    DefaultExcelExportClassificationWorkbookDecorator abstractExcelExportWorkbookDecorator;


    @Test
    public void shouldFindCellForAttributeAndItemAndFillCellWithDataFromTranslator()
    {
        ItemModel item = (ItemModel)Mockito.mock(ItemModel.class);
        ExcelClassificationAttribute excelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ExcelAttributeTranslator<ExcelAttribute> translator = (ExcelAttributeTranslator<ExcelAttribute>)Mockito.mock(ExcelAttributeTranslator.class);
        BDDMockito.given(translator.exportData((ExcelAttribute)ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn(Optional.of("exportedData"));
        BDDMockito.given(translator.referenceFormat((ExcelAttribute)ArgumentMatchers.any())).willReturn("referenceFormat");
        BDDMockito.given(this.mockedAttributeNameFormatter.format(aContextOfAttribute(excelAttribute, ExcelClassificationAttribute.class)))
                        .willReturn("headerValue");
        BDDMockito.given(this.mockedExcelAttributeTranslatorRegistry.findTranslator((ExcelAttribute)excelAttribute)).willReturn(Optional.of(translator));
        Workbook workbook = (Workbook)Mockito.mock(Workbook.class);
        Sheet sheet = (Sheet)Mockito.mock(Sheet.class);
        Cell headerCell = (Cell)Mockito.mock(Cell.class);
        Cell valueCell = (Cell)Mockito.mock(Cell.class);
        Row row = (Row)Mockito.mock(Row.class);
        BDDMockito.given(row.getSheet()).willReturn(sheet);
        ((DefaultExcelExportClassificationWorkbookDecorator)Mockito.doReturn(headerCell).when(this.abstractExcelExportWorkbookDecorator)).insertHeaderIfNecessary(sheet, "headerValue");
        ((DefaultExcelExportClassificationWorkbookDecorator)Mockito.doReturn(valueCell).when(this.abstractExcelExportWorkbookDecorator)).createCellIfNecessary(row, 0);
        ((DefaultExcelExportClassificationWorkbookDecorator)Mockito.doNothing().when(this.abstractExcelExportWorkbookDecorator)).insertReferenceFormatIfNecessary(valueCell, "referenceFormat");
        ((DefaultExcelExportClassificationWorkbookDecorator)Mockito.doReturn(Optional.of(row)).when(this.abstractExcelExportWorkbookDecorator)).findRow(workbook, item);
        this.abstractExcelExportWorkbookDecorator.decorate(workbook, Collections.singletonList(excelAttribute),
                        Collections.singletonList(item));
        ((ExcelAttributeTranslator)BDDMockito.then(translator).should()).exportData((ExcelAttribute)excelAttribute, item);
        ((ExcelCellService)BDDMockito.then(this.mockedExcelCellService).should()).insertAttributeValue(valueCell, "exportedData");
    }


    <T extends ExcelAttribute> ExcelAttributeContext<T> aContextOfAttribute(T firstExcelAttribute, Class<T> type)
    {
        return (ExcelAttributeContext<T>)ArgumentMatchers.argThat((ArgumentMatcher)new Object(this, type, (ExcelAttribute)firstExcelAttribute));
    }


    @Test
    public void shouldFindRow()
    {
        Workbook workbook = (Workbook)Mockito.mock(Workbook.class);
        ItemModel itemModel = (ItemModel)Mockito.mock(ItemModel.class);
        Sheet sheet = (Sheet)Mockito.mock(Sheet.class);
        Row firstRow = (Row)Mockito.mock(Row.class);
        Row secondRow = (Row)Mockito.mock(Row.class);
        Row expectedFoundRow = (Row)Mockito.mock(Row.class);
        BDDMockito.given(workbook.getSheet(ExcelTemplateConstants.UtilitySheet.PK.getSheetName())).willReturn(sheet);
        BDDMockito.given(sheet.getRow(0)).willReturn(firstRow);
        BDDMockito.given(sheet.getRow(1)).willReturn(secondRow);
        BDDMockito.given(Integer.valueOf(sheet.getFirstRowNum())).willReturn(Integer.valueOf(0));
        BDDMockito.given(Integer.valueOf(sheet.getLastRowNum())).willReturn(Integer.valueOf(1));
        BDDMockito.given(sheet.getRow(2)).willReturn(expectedFoundRow);
        BDDMockito.given(workbook.getSheet("foundSheetName")).willReturn(sheet);
        Cell foundPkCell = (Cell)Mockito.mock(Cell.class);
        Cell notMatchingPkCell = (Cell)Mockito.mock(Cell.class);
        BDDMockito.given(firstRow.getCell(0)).willReturn(notMatchingPkCell);
        BDDMockito.given(secondRow.getCell(0)).willReturn(foundPkCell);
        BDDMockito.given(itemModel.getPk()).willReturn(PK.fromLong(1337L));
        BDDMockito.given(this.mockedExcelCellService.getCellValue(foundPkCell)).willReturn("1337");
        BDDMockito.given(this.mockedExcelCellService.getCellValue(notMatchingPkCell)).willReturn("wrongPK");
        Cell sheetNameCell = (Cell)Mockito.mock(Cell.class);
        BDDMockito.given(secondRow.getCell(1)).willReturn(sheetNameCell);
        Cell rowIndexCell = (Cell)Mockito.mock(Cell.class);
        BDDMockito.given(secondRow.getCell(2)).willReturn(rowIndexCell);
        BDDMockito.given(this.mockedExcelCellService.getCellValue(sheetNameCell)).willReturn("foundSheetName");
        BDDMockito.given(this.mockedExcelCellService.getCellValue(rowIndexCell)).willReturn("2");
        Optional<Row> result = this.abstractExcelExportWorkbookDecorator.findRow(workbook, itemModel);
        ((OptionalAssert)Assertions.assertThat(result).isPresent()).hasValue(expectedFoundRow);
    }


    @Test
    public void shouldInsertReferenceFormat()
    {
        Cell excelCellValue = (Cell)Mockito.mock(Cell.class);
        String referenceFormat = "referenceFormat";
        Sheet sheet = (Sheet)Mockito.mock(Sheet.class);
        Row referencePatternRow = (Row)Mockito.mock(Row.class);
        Cell referencePatternCell = (Cell)Mockito.mock(Cell.class);
        int columnIndex = 1;
        BDDMockito.given(excelCellValue.getSheet()).willReturn(sheet);
        BDDMockito.given(Integer.valueOf(excelCellValue.getColumnIndex())).willReturn(Integer.valueOf(1));
        BDDMockito.given(sheet.getRow(1)).willReturn(referencePatternRow);
        BDDMockito.given(referencePatternRow.createCell(1)).willReturn(referencePatternCell);
        this.abstractExcelExportWorkbookDecorator.insertReferenceFormatIfNecessary(excelCellValue, "referenceFormat");
        ((ExcelCellService)BDDMockito.then(this.mockedExcelCellService).should()).insertAttributeValue(referencePatternCell, "referenceFormat");
    }


    @Test
    public void shouldNotInsertReferenceFormatIfItsBlank()
    {
        Cell excelCellValue = (Cell)Mockito.mock(Cell.class);
        String blankReferenceFormat = " ";
        this.abstractExcelExportWorkbookDecorator.insertReferenceFormatIfNecessary(excelCellValue, " ");
        ((ExcelCellService)BDDMockito.then(this.mockedExcelCellService).should(Mockito.never())).insertAttributeValue((Cell)ArgumentMatchers.any(), ArgumentMatchers.any());
    }


    @Test
    public void shouldInsertHeaderFindingColumnByContent()
    {
        Sheet sheet = (Sheet)Mockito.mock(Sheet.class);
        String headerValue = "headerValue";
        Row row = (Row)Mockito.mock(Row.class);
        int columnIndex = 1;
        Cell expectedCell = (Cell)Mockito.mock(Cell.class);
        BDDMockito.given(sheet.getRow(0)).willReturn(row);
        ((DefaultExcelExportClassificationWorkbookDecorator)Mockito.doReturn(Integer.valueOf(1)).when(this.abstractExcelExportWorkbookDecorator)).findColumnIndexByContentOrFirstEmptyCell(row, "headerValue");
        ((DefaultExcelExportClassificationWorkbookDecorator)Mockito.doReturn(expectedCell).when(this.abstractExcelExportWorkbookDecorator)).createNewHeaderCell(row, 1, "headerValue");
        Cell result = this.abstractExcelExportWorkbookDecorator.insertHeaderIfNecessary(sheet, "headerValue");
        Assertions.assertThat(result).isEqualTo(expectedCell);
    }


    @Test
    public void shouldInsertHeaderToFirstEmptyCell()
    {
        Sheet sheet = (Sheet)Mockito.mock(Sheet.class);
        String headerValue = "headerValue";
        Row row = (Row)Mockito.mock(Row.class);
        int columnIndex = -1;
        Cell expectedCell = (Cell)Mockito.mock(Cell.class);
        short lastCellNumber = 12;
        BDDMockito.given(Short.valueOf(row.getLastCellNum())).willReturn(Short.valueOf((short)12));
        BDDMockito.given(sheet.getRow(0)).willReturn(row);
        ((DefaultExcelExportClassificationWorkbookDecorator)Mockito.doReturn(Integer.valueOf(-1)).when(this.abstractExcelExportWorkbookDecorator)).findColumnIndexByContentOrFirstEmptyCell(row, "headerValue");
        ((DefaultExcelExportClassificationWorkbookDecorator)Mockito.doReturn(expectedCell).when(this.abstractExcelExportWorkbookDecorator)).createNewHeaderCell(row, 13, "headerValue");
        Cell result = this.abstractExcelExportWorkbookDecorator.insertHeaderIfNecessary(sheet, "headerValue");
        Assertions.assertThat(result).isEqualTo(expectedCell);
    }


    @Test
    public void shouldFindColumnIndexByContent()
    {
        Row row = (Row)Mockito.mock(Row.class);
        String content = "secondCellValue";
        Cell firstCell = (Cell)Mockito.mock(Cell.class);
        Cell secondCell = (Cell)Mockito.mock(Cell.class);
        BDDMockito.given(Short.valueOf(row.getFirstCellNum())).willReturn(Short.valueOf((short)0));
        BDDMockito.given(Short.valueOf(row.getLastCellNum())).willReturn(Short.valueOf((short)1));
        BDDMockito.given(row.getCell(0)).willReturn(firstCell);
        BDDMockito.given(row.getCell(1)).willReturn(secondCell);
        BDDMockito.given(this.mockedExcelCellService.getCellValue(firstCell)).willReturn("firstCellValue");
        BDDMockito.given(this.mockedExcelCellService.getCellValue(secondCell)).willReturn("secondCellValue");
        int result = this.abstractExcelExportWorkbookDecorator.findColumnIndexByContentOrFirstEmptyCell(row, "secondCellValue");
        Assertions.assertThat(result).isEqualTo(1);
    }


    @Test
    public void shouldFindColumnIndexByFirstEmptyCell()
    {
        Row row = (Row)Mockito.mock(Row.class);
        String content = "notExistingContent";
        Cell nonEmptyCell = (Cell)Mockito.mock(Cell.class);
        Cell firstEmptyCell = (Cell)Mockito.mock(Cell.class);
        BDDMockito.given(Short.valueOf(row.getFirstCellNum())).willReturn(Short.valueOf((short)0));
        BDDMockito.given(Short.valueOf(row.getLastCellNum())).willReturn(Short.valueOf((short)1));
        BDDMockito.given(row.getCell(0)).willReturn(nonEmptyCell);
        BDDMockito.given(row.getCell(1)).willReturn(firstEmptyCell);
        BDDMockito.given(this.mockedExcelCellService.getCellValue(nonEmptyCell)).willReturn("cellValue");
        BDDMockito.given(this.mockedExcelCellService.getCellValue(firstEmptyCell)).willReturn(" ");
        int result = this.abstractExcelExportWorkbookDecorator.findColumnIndexByContentOrFirstEmptyCell(row, "notExistingContent");
        Assertions.assertThat(result).isEqualTo(1);
    }


    @Test
    public void shouldCreateNewHeaderCell()
    {
        Row row = (Row)Mockito.mock(Row.class);
        String headerValue = "headerValue";
        int columnIndex = 0;
        Cell expectedCell = (Cell)Mockito.mock(Cell.class);
        ((DefaultExcelExportClassificationWorkbookDecorator)Mockito.doReturn(expectedCell).when(this.abstractExcelExportWorkbookDecorator)).createCellIfNecessary(row, 0);
        Cell result = this.abstractExcelExportWorkbookDecorator.createNewHeaderCell(row, 0, "headerValue");
        Assertions.assertThat(result).isEqualTo(expectedCell);
        ((ExcelCellService)BDDMockito.then(this.mockedExcelCellService).should()).insertAttributeValue(expectedCell, "headerValue");
    }


    @Test
    public void shouldCreateCell()
    {
        Row row = (Row)Mockito.mock(Row.class);
        int columnIndex = 0;
        Cell expectedCreatedCell = (Cell)Mockito.mock(Cell.class);
        BDDMockito.given(row.getCell(0)).willReturn(null);
        BDDMockito.given(row.createCell(0)).willReturn(expectedCreatedCell);
        Cell result = this.abstractExcelExportWorkbookDecorator.createCellIfNecessary(row, 0);
        ((Row)BDDMockito.then(row).should()).createCell(0);
        Assertions.assertThat(result).isEqualTo(expectedCreatedCell);
    }


    @Test
    public void shouldNotCreateCellAsItAlreadyExists()
    {
        Row row = (Row)Mockito.mock(Row.class);
        int columnIndex = 0;
        Cell expectedAlreadyCreatedCell = (Cell)Mockito.mock(Cell.class);
        BDDMockito.given(row.getCell(0)).willReturn(expectedAlreadyCreatedCell);
        Cell result = this.abstractExcelExportWorkbookDecorator.createCellIfNecessary(row, 0);
        ((Row)BDDMockito.then(row).should(Mockito.never())).createCell(0);
        Assertions.assertThat(result).isEqualTo(expectedAlreadyCreatedCell);
    }
}
