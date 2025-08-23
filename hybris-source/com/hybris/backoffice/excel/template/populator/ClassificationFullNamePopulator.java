package com.hybris.backoffice.excel.template.populator;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.template.AttributeNameFormatter;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Required;

class ClassificationFullNamePopulator implements ExcelClassificationCellPopulator
{
    private AttributeNameFormatter<ExcelClassificationAttribute> attributeNameFormatter;


    public String apply(@Nonnull ExcelAttributeContext<ExcelClassificationAttribute> context)
    {
        return this.attributeNameFormatter.format(context);
    }


    public AttributeNameFormatter<ExcelClassificationAttribute> getAttributeNameFormatter()
    {
        return this.attributeNameFormatter;
    }


    @Required
    public void setAttributeNameFormatter(AttributeNameFormatter<ExcelClassificationAttribute> attributeNameFormatter)
    {
        this.attributeNameFormatter = attributeNameFormatter;
    }
}
