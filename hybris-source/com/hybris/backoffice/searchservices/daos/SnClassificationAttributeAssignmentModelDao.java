package com.hybris.backoffice.searchservices.daos;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import java.util.Optional;

public interface SnClassificationAttributeAssignmentModelDao extends GenericDao<ClassAttributeAssignmentModel>
{
    Optional<ClassAttributeAssignmentModel> findClassAttributeAssignmentByClassAndAttribute(ClassificationClassModel paramClassificationClassModel, ClassificationAttributeModel paramClassificationAttributeModel);
}
