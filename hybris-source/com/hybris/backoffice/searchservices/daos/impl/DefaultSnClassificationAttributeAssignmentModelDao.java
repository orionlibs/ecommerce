package com.hybris.backoffice.searchservices.daos.impl;

import com.hybris.backoffice.searchservices.daos.SnClassificationAttributeAssignmentModelDao;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;

public class DefaultSnClassificationAttributeAssignmentModelDao extends DefaultGenericDao<ClassAttributeAssignmentModel> implements SnClassificationAttributeAssignmentModelDao
{
    public DefaultSnClassificationAttributeAssignmentModelDao()
    {
        super("ClassAttributeAssignment");
    }


    public Optional<ClassAttributeAssignmentModel> findClassAttributeAssignmentByClassAndAttribute(ClassificationClassModel classificationClass, ClassificationAttributeModel classificationAttribute)
    {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("classificationClass", classificationClass);
        queryParams.put("classificationAttribute", classificationAttribute);
        List<ClassAttributeAssignmentModel> classAttributeAssignments = find(queryParams);
        return CollectionUtils.isEmpty(classAttributeAssignments) ?
                        Optional.<ClassAttributeAssignmentModel>empty() :
                        Optional.<ClassAttributeAssignmentModel>of(classAttributeAssignments.get(0));
    }
}
