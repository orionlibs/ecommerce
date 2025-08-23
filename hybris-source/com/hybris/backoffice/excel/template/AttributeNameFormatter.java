package com.hybris.backoffice.excel.template;

import com.hybris.backoffice.excel.template.populator.ExcelAttributeContext;
import javax.annotation.Nonnull;

@FunctionalInterface
public interface AttributeNameFormatter<ATTRIBUTE extends com.hybris.backoffice.excel.data.ExcelAttribute>
{
    String format(@Nonnull ExcelAttributeContext<ATTRIBUTE> paramExcelAttributeContext);
}
