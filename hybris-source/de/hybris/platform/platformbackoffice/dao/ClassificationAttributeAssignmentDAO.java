package de.hybris.platform.platformbackoffice.dao;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;

public interface ClassificationAttributeAssignmentDAO
{
    @Deprecated(since = "1905", forRemoval = true)
    ClassAttributeAssignmentModel getClassificationAttributeAssignmnent(String paramString1, String paramString2, String paramString3, String paramString4);


    default ClassAttributeAssignmentModel getClassificationAttributeAssignment(String catalogId, String systemVersion, String classificationClass, String attribute)
    {
        return getClassificationAttributeAssignmnent(catalogId, systemVersion, classificationClass, attribute);
    }
}
