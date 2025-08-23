package com.hybris.backoffice.excel.template.mapper;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

@FunctionalInterface
public interface ToAttributeDescriptorsMapper<INPUT> extends ExcelMapper<INPUT, AttributeDescriptorModel>
{
}
