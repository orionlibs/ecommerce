package com.hybris.backoffice.excel.classification;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.core.model.ItemModel;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ExcelClassificationService
{
    Map<ClassificationSystemVersionModel, List<ClassificationClassModel>> getItemsIntersectedClassificationClasses(Collection<ItemModel> paramCollection);


    Map<ClassificationSystemVersionModel, List<ClassificationClassModel>> getItemsAddedClassificationClasses(Collection<ItemModel> paramCollection);


    Map<ClassificationSystemVersionModel, List<ClassificationClassModel>> getAllClassificationClasses();
}
