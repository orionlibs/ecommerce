package com.hybris.backoffice.excel.exporting.data.filter;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.function.Predicate;

@FunctionalInterface
public interface ExcelExportAttributePredicate extends Predicate<AttributeDescriptorModel>
{
    boolean test(AttributeDescriptorModel paramAttributeDescriptorModel);
}
