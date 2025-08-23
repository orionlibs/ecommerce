package com.hybris.backoffice.excel.template.populator;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import javax.annotation.Nonnull;

@FunctionalInterface
public interface ExcelClassificationCellPopulator extends ExcelCellPopulator<ExcelClassificationAttribute>
{
    String apply(@Nonnull ExcelAttributeContext<ExcelClassificationAttribute> paramExcelAttributeContext);
}
