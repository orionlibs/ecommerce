package com.hybris.backoffice.excel.template.cell;

import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FormulaCellValueTest extends AbstractCellValueTest
{
    private FormulaCellValue formulaCellValue = new FormulaCellValue();


    @Test
    public void shouldGivenTypeCellBeHandled()
    {
        Assert.assertTrue(this.formulaCellValue.canHandle(CellType.FORMULA));
    }


    @Test
    public void shouldGivenTypeCellNotBeHandled()
    {
        Assert.assertFalse(this.formulaCellValue.canHandle(CellType.STRING));
    }


    @Test
    public void shouldGivenValueBeHandledCorrectly()
    {
        String expectedValue = "val";
        Cell cell = (Cell)Mockito.mock(Cell.class);
        FormulaEvaluator formulaEvaluator = (FormulaEvaluator)Mockito.mock(FormulaEvaluator.class);
        CreationHelper creationHelper = (CreationHelper)Mockito.mock(CreationHelper.class);
        Workbook workbook = (Workbook)Mockito.mock(Workbook.class);
        Sheet sheet = (Sheet)Mockito.mock(Sheet.class);
        Row row = (Row)Mockito.mock(Row.class);
        CellValue cellValue = new CellValue("val");
        BDDMockito.given(cell.getRow()).willReturn(row);
        BDDMockito.given(row.getSheet()).willReturn(sheet);
        BDDMockito.given(sheet.getWorkbook()).willReturn(workbook);
        BDDMockito.given(workbook.getCreationHelper()).willReturn(creationHelper);
        BDDMockito.given(creationHelper.createFormulaEvaluator()).willReturn(formulaEvaluator);
        BDDMockito.given(formulaEvaluator.evaluate(cell)).willReturn(cellValue);
        Optional<String> returnedValue = this.formulaCellValue.getValue(cell);
        Assert.assertTrue(returnedValue.isPresent());
        AssertionsForClassTypes.assertThat(returnedValue.get()).isEqualTo("val");
    }
}
