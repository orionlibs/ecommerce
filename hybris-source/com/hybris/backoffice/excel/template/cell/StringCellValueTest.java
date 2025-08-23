package com.hybris.backoffice.excel.template.cell;

import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StringCellValueTest extends AbstractCellValueTest
{
    private StringCellValue stringCellValue = new StringCellValue();


    @Test
    public void shouldGivenTypeCellBeHandled()
    {
        Assert.assertTrue(this.stringCellValue.canHandle(CellType.STRING));
    }


    @Test
    public void shouldGivenTypeCellNotBeHandled()
    {
        Assert.assertFalse(this.stringCellValue.canHandle(CellType.BOOLEAN));
    }


    @Test
    public void shouldGivenValueBeHandledCorrectly()
    {
        String givenValue = "val";
        Cell cell = (Cell)Mockito.mock(Cell.class);
        BDDMockito.given(cell.getStringCellValue()).willReturn("val");
        Optional<String> receivedValue = this.stringCellValue.getValue(cell);
        Assert.assertTrue(receivedValue.isPresent());
        AssertionsForClassTypes.assertThat(receivedValue.get()).isEqualTo("val");
    }
}
