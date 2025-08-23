package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.workbook.ExcelWorkbookService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import org.springframework.beans.factory.annotation.Required;

public class IsoCodeDecorator implements ExcelExportWorkbookDecorator
{
    private static final int ORDER_OF_ISO_DECORATOR = 10000;
    private int order = 10000;
    private CommonI18NService commonI18NService;
    private ExcelWorkbookService excelWorkbookService;


    public void decorate(ExcelExportResult excelExportResult)
    {
        this.excelWorkbookService.addProperty(excelExportResult.getWorkbook(), "isoCode", this.commonI18NService
                        .getCurrentLanguage().getIsocode());
    }


    public int getOrder()
    {
        return this.order;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setExcelWorkbookService(ExcelWorkbookService excelWorkbookService)
    {
        this.excelWorkbookService = excelWorkbookService;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }
}
