package com.hybris.backoffice.excel.template.populator;

import java.util.function.Function;

@FunctionalInterface
public interface ExcelCellPopulator<T extends com.hybris.backoffice.excel.data.ExcelAttribute> extends Function<ExcelAttributeContext<T>, String>
{
}
