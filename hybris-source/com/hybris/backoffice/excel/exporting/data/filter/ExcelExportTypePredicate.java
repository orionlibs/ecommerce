package com.hybris.backoffice.excel.exporting.data.filter;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.function.Predicate;

@FunctionalInterface
public interface ExcelExportTypePredicate extends Predicate<ComposedTypeModel>
{
    boolean test(ComposedTypeModel paramComposedTypeModel);
}
