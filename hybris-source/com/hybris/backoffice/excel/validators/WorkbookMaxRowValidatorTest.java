package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorkbookMaxRowValidatorTest
{
    public static final int MAX_ROWS = 2000;
    @Mock
    private ExcelSheetService excelSheetService;
    @Mock
    private ExcelCellService excelCellService;
    @Spy
    @InjectMocks
    private WorkbookMaxRowValidator workbookMaxRowValidator;


    @Test
    public void shouldNotReturnValidationErrorWhenNumberOfRowsIsLessThanMaxValue()
    {
        Workbook workbook = (Workbook)Mockito.mock(Workbook.class);
        Sheet productSheet = (Sheet)Mockito.mock(Sheet.class);
        Mockito.when(this.excelSheetService.getSheets(workbook)).thenReturn(Collections.singletonList(productSheet));
        ((WorkbookMaxRowValidator)Mockito.doReturn(Integer.valueOf(5)).when(this.workbookMaxRowValidator)).getNumberOfCorrectRows(productSheet);
        ((WorkbookMaxRowValidator)Mockito.doReturn(Integer.valueOf(2000)).when(this.workbookMaxRowValidator)).getMaxRow();
        List<ExcelValidationResult> validationResults = this.workbookMaxRowValidator.validate(workbook);
        Assertions.assertThat(validationResults).isEmpty();
    }


    @Test
    public void shouldNotReturnValidationErrorWhenNumberOfRowsExactlyEqualsToMaxValue()
    {
        Workbook workbook = (Workbook)Mockito.mock(Workbook.class);
        Sheet productSheet = (Sheet)Mockito.mock(Sheet.class);
        Mockito.when(this.excelSheetService.getSheets(workbook)).thenReturn(Collections.singletonList(productSheet));
        ((WorkbookMaxRowValidator)Mockito.doReturn(Integer.valueOf(2000)).when(this.workbookMaxRowValidator)).getNumberOfCorrectRows(productSheet);
        ((WorkbookMaxRowValidator)Mockito.doReturn(Integer.valueOf(2000)).when(this.workbookMaxRowValidator)).getMaxRow();
        List<ExcelValidationResult> validationResults = this.workbookMaxRowValidator.validate(workbook);
        Assertions.assertThat(validationResults).isEmpty();
    }


    @Test
    public void shouldReturnValidationErrorWhenNumberOfRowsIsExceeded()
    {
        Workbook workbook = (Workbook)Mockito.mock(Workbook.class);
        Sheet productSheet = (Sheet)Mockito.mock(Sheet.class);
        Mockito.when(this.excelSheetService.getSheets(workbook)).thenReturn(Collections.singletonList(productSheet));
        ((WorkbookMaxRowValidator)Mockito.doReturn(Integer.valueOf(2003)).when(this.workbookMaxRowValidator))
                        .getNumberOfCorrectRows(productSheet);
        ((WorkbookMaxRowValidator)Mockito.doReturn(Integer.valueOf(2000)).when(this.workbookMaxRowValidator)).getMaxRow();
        List<ExcelValidationResult> validationResults = this.workbookMaxRowValidator.validate(workbook);
        Assertions.assertThat(validationResults).isNotEmpty();
    }


    @Test
    public void shouldReturnValidationErrorWhenSumOfNumberOfRowsIsExceeded()
    {
        Workbook workbook = (Workbook)Mockito.mock(Workbook.class);
        Sheet productSheet = (Sheet)Mockito.mock(Sheet.class);
        Sheet shoeSheet = (Sheet)Mockito.mock(Sheet.class);
        Mockito.when(this.excelSheetService.getSheets(workbook)).thenReturn(Arrays.asList(new Sheet[] {productSheet, shoeSheet}));
        ((WorkbookMaxRowValidator)Mockito.doReturn(Integer.valueOf(1004)).when(this.workbookMaxRowValidator))
                        .getNumberOfCorrectRows(productSheet);
        ((WorkbookMaxRowValidator)Mockito.doReturn(Integer.valueOf(1004)).when(this.workbookMaxRowValidator))
                        .getNumberOfCorrectRows(shoeSheet);
        ((WorkbookMaxRowValidator)Mockito.doReturn(Integer.valueOf(2000)).when(this.workbookMaxRowValidator)).getMaxRow();
        List<ExcelValidationResult> validationResults = this.workbookMaxRowValidator.validate(workbook);
        Assertions.assertThat(validationResults).isNotEmpty();
    }
}
