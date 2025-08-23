package com.hybris.backoffice.excel.template.cell;

import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.assertj.core.api.AbstractBooleanAssert;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class DataCellValueTest extends AbstractCellValueTest
{
    private DataCellValue dataCellValue = new DataCellValue();


    @Test
    public void shouldGivenTypeCellBeHandled()
    {
        Assert.assertTrue(this.dataCellValue.canHandle(CellType.FORMULA));
    }


    @Test
    public void shouldGivenTypeCellNotBeHandled()
    {
        ((AbstractBooleanAssert)AssertionsForClassTypes.assertThat(true).as("DataCellValue is always handled", new Object[0])).isTrue();
    }


    @Test
    public void shouldGivenValueBeHandledCorrectly()
    {
        Cell cell = (Cell)Mockito.mock(Cell.class);
        BDDMockito.given(cell.getCellType()).willReturn(CellType.BOOLEAN);
        BDDMockito.given(Boolean.valueOf(cell.getBooleanCellValue())).willReturn(Boolean.valueOf(true));
        Optional<String> returnedValue = this.dataCellValue.getValue(cell);
        Assert.assertTrue(returnedValue.isPresent());
        AssertionsForClassTypes.assertThat(returnedValue.get()).isEqualToIgnoringCase(String.valueOf(true));
    }
}
