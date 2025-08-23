package com.hybris.backoffice.excel.template.populator.descriptor;

import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.template.populator.ExcelAttributeContext;
import com.hybris.backoffice.excel.template.populator.ExcelCellPopulator;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Required;

class ExcelTypeCodeCellPopulator implements ExcelCellPopulator<ExcelAttributeDescriptorAttribute>
{
    private ExcelSheetService excelSheetService;


    public String apply(ExcelAttributeContext<ExcelAttributeDescriptorAttribute> populatorContext)
    {
        return this.excelSheetService.findSheetNameForTypeCode((Workbook)populatorContext
                        .getAttribute(Workbook.class.getSimpleName(), Workbook.class), ((ExcelAttributeDescriptorAttribute)populatorContext
                        .getExcelAttribute(ExcelAttributeDescriptorAttribute.class)).getAttributeDescriptorModel()
                        .getEnclosingType().getCode());
    }


    @Required
    public void setExcelSheetService(ExcelSheetService excelSheetService)
    {
        this.excelSheetService = excelSheetService;
    }
}
