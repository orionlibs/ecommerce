package com.hybris.backoffice.excel.template.populator.appender;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import org.springframework.beans.factory.annotation.Required;

public class MandatoryExcelMarkAppender implements ExcelMarkAppender<ExcelAttribute>
{
    private CommonI18NService commonI18NService;


    public String apply(String s, ExcelAttribute excelAttribute)
    {
        return (isMandatory(excelAttribute) && !hasDefaultValue(excelAttribute)) ? (
                        s + s) :
                        s;
    }


    private boolean isMandatory(ExcelAttribute excelAttribute)
    {
        if(!excelAttribute.isMandatory())
        {
            return false;
        }
        if(excelAttribute.isLocalized())
        {
            return this.commonI18NService.getCurrentLanguage().getIsocode().equals(excelAttribute.getIsoCode());
        }
        return true;
    }


    private static boolean hasDefaultValue(ExcelAttribute excelAttribute)
    {
        if(excelAttribute instanceof ExcelAttributeDescriptorAttribute)
        {
            return (((ExcelAttributeDescriptorAttribute)excelAttribute).getAttributeDescriptorModel().getDefaultValue() != null);
        }
        return false;
    }


    public int getOrder()
    {
        return 1000;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }
}
