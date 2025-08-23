package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.workbook.ExcelWorkbookService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IsoCodeDecoratorTest
{
    @Mock
    private ExcelWorkbookService excelWorkbookService;
    @Mock
    private CommonI18NService commonI18NService;
    private final IsoCodeDecorator isoCodeDecorator = new IsoCodeDecorator();


    @Before
    public void setUp()
    {
        this.isoCodeDecorator.setCommonI18NService(this.commonI18NService);
        this.isoCodeDecorator.setExcelWorkbookService(this.excelWorkbookService);
    }


    @Test
    public void shouldPropertyBeAddedToTheWorbook()
    {
        Workbook workbook = (Workbook)Mockito.mock(Workbook.class);
        String key = "isoCode";
        String isoCode = "en";
        ExcelExportResult result = (ExcelExportResult)Mockito.mock(ExcelExportResult.class);
        BDDMockito.given(result.getWorkbook()).willReturn(workbook);
        BDDMockito.given(this.commonI18NService.getCurrentLanguage()).willReturn(Mockito.mock(LanguageModel.class, answer -> "en"));
        this.isoCodeDecorator.decorate(result);
        ((ExcelWorkbookService)Mockito.verify(this.excelWorkbookService)).addProperty(workbook, "isoCode", "en");
    }
}
