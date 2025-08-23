package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import java.util.Collection;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetVisibility;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HideUtilitySheetsDecoratorTest
{
    private final Collection<ExcelTemplateConstants.UtilitySheet> sheetsToHide = Lists.newArrayList((Object[])new ExcelTemplateConstants.UtilitySheet[] {ExcelTemplateConstants.UtilitySheet.HEADER_PROMPT});
    @Spy
    private final HideUtilitySheetsDecorator hideUtilitySheetsDecorator = new HideUtilitySheetsDecorator();


    @Before
    public void setUp()
    {
        this.hideUtilitySheetsDecorator.setUtilitySheets(this.sheetsToHide);
    }


    @Test
    public void shouldUtilitySheetBeHidden()
    {
        Workbook workbook = (Workbook)Mockito.mock(Workbook.class);
        int index = 1;
        SheetVisibility hiddenLevel = SheetVisibility.VERY_HIDDEN;
        BDDMockito.given(Integer.valueOf(workbook.getSheetIndex(ExcelTemplateConstants.UtilitySheet.HEADER_PROMPT.getSheetName()))).willReturn(Integer.valueOf(1));
        BDDMockito.given(Boolean.valueOf(workbook.isSheetHidden(1))).willReturn(Boolean.valueOf(false));
        BDDMockito.given(workbook.getSheetAt(1)).willReturn(Mockito.mock(Sheet.class));
        ((HideUtilitySheetsDecorator)Mockito.doNothing().when(this.hideUtilitySheetsDecorator)).activateFirstNonUtilitySheet(workbook);
        ((HideUtilitySheetsDecorator)Mockito.doReturn(hiddenLevel).when(this.hideUtilitySheetsDecorator)).getUtilitySheetHiddenLevel();
        this.hideUtilitySheetsDecorator.decorate(new ExcelExportResult(workbook));
        ((Workbook)Mockito.verify(workbook)).setSheetVisibility(1, hiddenLevel);
    }
}
