package com.hybris.backoffice.excel.template.cell;

import com.hybris.backoffice.excel.util.ExcelDateUtils;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NumericCellValueTest extends AbstractCellValueTest
{
    @Mock
    private ExcelDateUtils excelDateUtils;
    @Spy
    private NumericCellValue numericCellValue = new NumericCellValue();


    @Before
    public void setUp()
    {
        this.numericCellValue.setExcelDateUtils(this.excelDateUtils);
    }


    @Test
    public void shouldGivenTypeCellBeHandled()
    {
        Assert.assertTrue(this.numericCellValue.canHandle(CellType.NUMERIC));
    }


    @Test
    public void shouldGivenTypeCellNotBeHandled()
    {
        Assert.assertFalse(this.numericCellValue.canHandle(CellType.FORMULA));
    }


    @Test
    public void shouldGivenValueBeHandledCorrectly()
    {
        double cellValue = 2.57D;
        Cell cell = (Cell)Mockito.mock(Cell.class);
        BDDMockito.given(Double.valueOf(cell.getNumericCellValue())).willReturn(Double.valueOf(2.57D));
        ((NumericCellValue)Mockito.doReturn(Boolean.valueOf(false)).when(this.numericCellValue)).isCellDateFormatted((Cell)Matchers.any());
        Optional<String> returnedValue = this.numericCellValue.getValue(cell);
        Assert.assertTrue(returnedValue.isPresent());
        AssertionsForClassTypes.assertThat(returnedValue.get()).isEqualTo(String.valueOf(2.57D));
    }
}
