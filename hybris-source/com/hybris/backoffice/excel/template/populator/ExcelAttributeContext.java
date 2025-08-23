package com.hybris.backoffice.excel.template.populator;

import javax.annotation.Nonnull;

public interface ExcelAttributeContext<ATTRIBUTE extends com.hybris.backoffice.excel.data.ExcelAttribute>
{
    <TYPE> TYPE getAttribute(@Nonnull String paramString, @Nonnull Class<TYPE> paramClass);


    ATTRIBUTE getExcelAttribute(@Nonnull Class<ATTRIBUTE> paramClass);
}
