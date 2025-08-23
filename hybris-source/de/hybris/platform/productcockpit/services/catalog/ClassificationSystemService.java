package de.hybris.platform.productcockpit.services.catalog;

import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import java.util.List;

public interface ClassificationSystemService
{
    List<ClassificationSystemModel> getClassificationSystems();


    List<ClassificationSystemVersionModel> getClassificationSystemVersions(ClassificationSystemModel paramClassificationSystemModel);


    List<ClassificationClassModel> getClassificationRootCategories(ClassificationSystemVersionModel paramClassificationSystemVersionModel);


    List<ClassificationClassModel> getClassificationSubcategories(ClassificationClassModel paramClassificationClassModel);


    List<ClassificationAttributeModel> getClassificationAttributes(ClassificationClassModel paramClassificationClassModel);
}
