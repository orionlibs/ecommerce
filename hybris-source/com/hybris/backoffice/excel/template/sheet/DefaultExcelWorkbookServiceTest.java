package com.hybris.backoffice.excel.template.sheet;

import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.workbook.DefaultExcelWorkbookService;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelWorkbookServiceTest
{
    private final DefaultExcelWorkbookService workbookService = new DefaultExcelWorkbookService();


    @Test
    public void shouldMetaInformationSheetBeReturned()
    {
        ExcelTemplateConstants.UtilitySheet metaInformationSheet = ExcelTemplateConstants.UtilitySheet.TYPE_SYSTEM;
        Sheet sheet = (Sheet)Mockito.mock(Sheet.class);
        Workbook workbook = (Workbook)Mockito.mock(Workbook.class);
        BDDMockito.given(workbook.getSheet(metaInformationSheet.getSheetName())).willReturn(sheet);
        AssertionsForClassTypes.assertThat(sheet).isEqualTo(this.workbookService.getMetaInformationSheet(workbook));
    }
}
