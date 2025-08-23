package com.hybris.backoffice.excel.template.cell;

import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelCellServiceTest
{
    @Spy
    private CellValue stringCellValue = (CellValue)new StringCellValue();
    @Spy
    private CellValue numericCellValue = (CellValue)new NumericCellValue();
    @Spy
    private CellValue dataCellValue = (CellValue)new DataCellValue();
    @Spy
    private CellValue formulaCellValue = (CellValue)new FormulaCellValue();
    private DefaultExcelCellService excelCellService = new DefaultExcelCellService();


    @Before
    public void setUp()
    {
        this.excelCellService.setCellValues(Lists.newArrayList((Object[])new CellValue[] {this.stringCellValue, this.numericCellValue, this.formulaCellValue, this.dataCellValue}));
    }


    @Test
    public void shouldGetCellValueBeNullSafe()
    {
        AssertionsForClassTypes.assertThat(this.excelCellService.getCellValue(null)).isEqualTo("");
    }


    @Test
    public void shouldFirstMatchTerminateIterationAbstractFacetSearchConfigDAOTest()
    {
        Cell cell = (Cell)Mockito.mock(Cell.class);
        BDDMockito.given(cell.getCellType()).willReturn(CellType.STRING);
        this.excelCellService.getCellValue(cell);
        ((CellValue)Mockito.verify(this.stringCellValue)).getValue(cell);
        ((CellValue)Mockito.verify(this.numericCellValue, Mockito.never())).getValue((Cell)Matchers.any());
        ((CellValue)Mockito.verify(this.formulaCellValue, Mockito.never())).getValue((Cell)Matchers.any());
        ((CellValue)Mockito.verify(this.dataCellValue, Mockito.never())).getValue((Cell)Matchers.any());
    }


    @Test
    public void shouldDataCellValueBeInvokedAsDefault()
    {
        Cell cell = (Cell)Mockito.mock(Cell.class);
        BDDMockito.given(cell.getCellType()).willReturn(null);
        ((CellValue)Mockito.doReturn(Optional.of("")).when(this.dataCellValue)).getValue((Cell)Matchers.any());
        this.excelCellService.getCellValue(cell);
        ((CellValue)Mockito.verify(this.dataCellValue)).getValue(cell);
        ((CellValue)Mockito.verify(this.numericCellValue, Mockito.never())).getValue((Cell)Matchers.any());
        ((CellValue)Mockito.verify(this.formulaCellValue, Mockito.never())).getValue((Cell)Matchers.any());
        ((CellValue)Mockito.verify(this.stringCellValue, Mockito.never())).getValue((Cell)Matchers.any());
    }


    @Test
    public void shouldImportedValueBeEscaped()
    {
        String val = "=X";
        String nonEscapedVal = "'=X";
        Cell cell = (Cell)Mockito.mock(Cell.class);
        BDDMockito.given(cell.getCellType()).willReturn(CellType.STRING);
        ((CellValue)Mockito.doReturn(Optional.of("'=X")).when(this.stringCellValue)).getValue(cell);
        String returned = this.excelCellService.getCellValue(cell);
        AssertionsForClassTypes.assertThat(returned).isEqualTo("=X");
    }


    @Test
    public void shouldImportedValueBeNotEscaped()
    {
        String val = "'value";
        Cell cell = (Cell)Mockito.mock(Cell.class);
        BDDMockito.given(cell.getCellType()).willReturn(CellType.STRING);
        ((CellValue)Mockito.doReturn(Optional.of("'value")).when(this.stringCellValue)).getValue(cell);
        String returned = this.excelCellService.getCellValue(cell);
        AssertionsForClassTypes.assertThat(returned).isEqualTo("'value");
    }


    @Test
    public void shouldExportedValueBeEscaped()
    {
        String val = "=X";
        String escapedVal = "'=X";
        String returned = this.excelCellService.escapeExportFormula("=X");
        AssertionsForClassTypes.assertThat(returned).isEqualTo("'=X");
    }
}
