package com.hybris.backoffice.excel.template.mapper;

@FunctionalInterface
public interface ToExcelAttributesMapper<INPUT, ATTRIBUTE extends com.hybris.backoffice.excel.data.ExcelAttribute> extends ExcelMapper<INPUT, ATTRIBUTE>
{
}
